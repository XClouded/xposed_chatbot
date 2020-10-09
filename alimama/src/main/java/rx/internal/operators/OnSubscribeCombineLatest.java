package rx.internal.operators;

import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.MissingBackpressureException;
import rx.functions.FuncN;
import rx.internal.util.RxRingBuffer;

public final class OnSubscribeCombineLatest<T, R> implements Observable.OnSubscribe<R> {
    final FuncN<? extends R> combinator;
    final List<? extends Observable<? extends T>> sources;

    public OnSubscribeCombineLatest(List<? extends Observable<? extends T>> list, FuncN<? extends R> funcN) {
        this.sources = list;
        this.combinator = funcN;
        if (list.size() > RxRingBuffer.SIZE) {
            throw new IllegalArgumentException("More than RxRingBuffer.SIZE sources to combineLatest is not supported.");
        }
    }

    public void call(Subscriber<? super R> subscriber) {
        if (this.sources.isEmpty()) {
            subscriber.onCompleted();
        } else if (this.sources.size() == 1) {
            subscriber.setProducer(new SingleSourceProducer(subscriber, (Observable) this.sources.get(0), this.combinator));
        } else {
            subscriber.setProducer(new MultiSourceProducer(subscriber, this.sources, this.combinator));
        }
    }

    static final class MultiSourceProducer<T, R> implements Producer {
        private static final AtomicLongFieldUpdater<MultiSourceProducer> WIP = AtomicLongFieldUpdater.newUpdater(MultiSourceProducer.class, IWXUserTrackAdapter.COUNTER);
        private final RxRingBuffer buffer = RxRingBuffer.getSpmcInstance();
        private final Subscriber<? super R> child;
        private final Object[] collectedValues;
        private final FuncN<? extends R> combinator;
        private final BitSet completion;
        private volatile int completionCount;
        private volatile long counter;
        private final BitSet haveValues;
        private volatile int haveValuesCount;
        private final AtomicLong requested = new AtomicLong();
        private final List<? extends Observable<? extends T>> sources;
        private final AtomicBoolean started = new AtomicBoolean();
        private final MultiSourceRequestableSubscriber<T, R>[] subscribers;

        public MultiSourceProducer(Subscriber<? super R> subscriber, List<? extends Observable<? extends T>> list, FuncN<? extends R> funcN) {
            this.sources = list;
            this.child = subscriber;
            this.combinator = funcN;
            int size = list.size();
            this.subscribers = new MultiSourceRequestableSubscriber[size];
            this.collectedValues = new Object[size];
            this.haveValues = new BitSet(size);
            this.completion = new BitSet(size);
        }

        public void request(long j) {
            BackpressureUtils.getAndAddRequest(this.requested, j);
            if (!this.started.get()) {
                int i = 0;
                if (this.started.compareAndSet(false, true)) {
                    int size = RxRingBuffer.SIZE / this.sources.size();
                    int size2 = RxRingBuffer.SIZE % this.sources.size();
                    while (i < this.sources.size()) {
                        Observable observable = (Observable) this.sources.get(i);
                        MultiSourceRequestableSubscriber<T, R> multiSourceRequestableSubscriber = new MultiSourceRequestableSubscriber<>(i, i == this.sources.size() - 1 ? size + size2 : size, this.child, this);
                        this.subscribers[i] = multiSourceRequestableSubscriber;
                        observable.unsafeSubscribe(multiSourceRequestableSubscriber);
                        i++;
                    }
                }
            }
            tick();
        }

        /* access modifiers changed from: package-private */
        public void tick() {
            Object poll;
            if (WIP.getAndIncrement(this) == 0) {
                int i = 0;
                do {
                    if (this.requested.get() > 0 && (poll = this.buffer.poll()) != null) {
                        if (this.buffer.isCompleted(poll)) {
                            this.child.onCompleted();
                        } else {
                            this.buffer.accept(poll, this.child);
                            i++;
                            this.requested.decrementAndGet();
                        }
                    }
                } while (WIP.decrementAndGet(this) > 0);
                if (i > 0) {
                    for (MultiSourceRequestableSubscriber<T, R> requestUpTo : this.subscribers) {
                        requestUpTo.requestUpTo((long) i);
                    }
                }
            }
        }

        public void onCompleted(int i, boolean z) {
            boolean z2;
            if (!z) {
                this.child.onCompleted();
                return;
            }
            synchronized (this) {
                z2 = false;
                if (!this.completion.get(i)) {
                    this.completion.set(i);
                    this.completionCount++;
                    if (this.completionCount == this.collectedValues.length) {
                        z2 = true;
                    }
                }
            }
            if (z2) {
                this.buffer.onCompleted();
                tick();
            }
        }

        public boolean onNext(int i, T t) {
            synchronized (this) {
                if (!this.haveValues.get(i)) {
                    this.haveValues.set(i);
                    this.haveValuesCount++;
                }
                this.collectedValues[i] = t;
                if (this.haveValuesCount != this.collectedValues.length) {
                    return false;
                }
                try {
                    this.buffer.onNext(this.combinator.call(this.collectedValues));
                } catch (MissingBackpressureException e) {
                    onError(e);
                } catch (Throwable th) {
                    onError(th);
                }
            }
            tick();
            return true;
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }
    }

    static final class MultiSourceRequestableSubscriber<T, R> extends Subscriber<T> {
        final AtomicLong emitted = new AtomicLong();
        boolean hasValue = false;
        final int index;
        final MultiSourceProducer<T, R> producer;

        public MultiSourceRequestableSubscriber(int i, int i2, Subscriber<? super R> subscriber, MultiSourceProducer<T, R> multiSourceProducer) {
            super(subscriber);
            this.index = i;
            this.producer = multiSourceProducer;
            request((long) i2);
        }

        public void requestUpTo(long j) {
            long j2;
            long min;
            do {
                j2 = this.emitted.get();
                min = Math.min(j2, j);
            } while (!this.emitted.compareAndSet(j2, j2 - min));
            request(min);
        }

        public void onCompleted() {
            this.producer.onCompleted(this.index, this.hasValue);
        }

        public void onError(Throwable th) {
            this.producer.onError(th);
        }

        public void onNext(T t) {
            this.hasValue = true;
            this.emitted.incrementAndGet();
            if (!this.producer.onNext(this.index, t)) {
                request(1);
            }
        }
    }

    static final class SingleSourceProducer<T, R> implements Producer {
        final Subscriber<? super R> child;
        final FuncN<? extends R> combinator;
        final Observable<? extends T> source;
        final AtomicBoolean started = new AtomicBoolean();
        final SingleSourceRequestableSubscriber<T, R> subscriber;

        public SingleSourceProducer(Subscriber<? super R> subscriber2, Observable<? extends T> observable, FuncN<? extends R> funcN) {
            this.source = observable;
            this.child = subscriber2;
            this.combinator = funcN;
            this.subscriber = new SingleSourceRequestableSubscriber<>(subscriber2, funcN);
        }

        public void request(long j) {
            this.subscriber.requestMore(j);
            if (this.started.compareAndSet(false, true)) {
                this.source.unsafeSubscribe(this.subscriber);
            }
        }
    }

    static final class SingleSourceRequestableSubscriber<T, R> extends Subscriber<T> {
        private final Subscriber<? super R> child;
        private final FuncN<? extends R> combinator;

        SingleSourceRequestableSubscriber(Subscriber<? super R> subscriber, FuncN<? extends R> funcN) {
            super(subscriber);
            this.child = subscriber;
            this.combinator = funcN;
        }

        public void requestMore(long j) {
            request(j);
        }

        public void onNext(T t) {
            this.child.onNext(this.combinator.call(t));
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }

        public void onCompleted() {
            this.child.onCompleted();
        }
    }
}
