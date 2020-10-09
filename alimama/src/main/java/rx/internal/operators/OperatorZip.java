package rx.internal.operators;

import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.MissingBackpressureException;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;
import rx.functions.Functions;
import rx.internal.util.RxRingBuffer;
import rx.subscriptions.CompositeSubscription;

public final class OperatorZip<R> implements Observable.Operator<R, Observable<?>[]> {
    final FuncN<? extends R> zipFunction;

    public OperatorZip(FuncN<? extends R> funcN) {
        this.zipFunction = funcN;
    }

    public OperatorZip(Func2 func2) {
        this.zipFunction = Functions.fromFunc(func2);
    }

    public OperatorZip(Func3 func3) {
        this.zipFunction = Functions.fromFunc(func3);
    }

    public OperatorZip(Func4 func4) {
        this.zipFunction = Functions.fromFunc(func4);
    }

    public OperatorZip(Func5 func5) {
        this.zipFunction = Functions.fromFunc(func5);
    }

    public OperatorZip(Func6 func6) {
        this.zipFunction = Functions.fromFunc(func6);
    }

    public OperatorZip(Func7 func7) {
        this.zipFunction = Functions.fromFunc(func7);
    }

    public OperatorZip(Func8 func8) {
        this.zipFunction = Functions.fromFunc(func8);
    }

    public OperatorZip(Func9 func9) {
        this.zipFunction = Functions.fromFunc(func9);
    }

    public Subscriber<? super Observable[]> call(Subscriber<? super R> subscriber) {
        Zip zip = new Zip(subscriber, this.zipFunction);
        ZipProducer zipProducer = new ZipProducer(zip);
        subscriber.setProducer(zipProducer);
        return new ZipSubscriber(subscriber, zip, zipProducer);
    }

    private final class ZipSubscriber extends Subscriber<Observable[]> {
        final Subscriber<? super R> child;
        final ZipProducer<R> producer;
        boolean started = false;
        final Zip<R> zipper;

        public ZipSubscriber(Subscriber<? super R> subscriber, Zip<R> zip, ZipProducer<R> zipProducer) {
            super(subscriber);
            this.child = subscriber;
            this.zipper = zip;
            this.producer = zipProducer;
        }

        public void onCompleted() {
            if (!this.started) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }

        public void onNext(Observable[] observableArr) {
            if (observableArr == null || observableArr.length == 0) {
                this.child.onCompleted();
                return;
            }
            this.started = true;
            this.zipper.start(observableArr, this.producer);
        }
    }

    private static final class ZipProducer<R> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1216676403723546796L;
        private Zip<R> zipper;

        public ZipProducer(Zip<R> zip) {
            this.zipper = zip;
        }

        public void request(long j) {
            BackpressureUtils.getAndAddRequest(this, j);
            this.zipper.tick();
        }
    }

    private static final class Zip<R> {
        static final AtomicLongFieldUpdater<Zip> COUNTER_UPDATER = AtomicLongFieldUpdater.newUpdater(Zip.class, IWXUserTrackAdapter.COUNTER);
        static final int THRESHOLD;
        /* access modifiers changed from: private */
        public final Observer<? super R> child;
        private final CompositeSubscription childSubscription = new CompositeSubscription();
        volatile long counter;
        int emitted = 0;
        private Object[] observers;
        private AtomicLong requested;
        private final FuncN<? extends R> zipFunction;

        static {
            double d = (double) RxRingBuffer.SIZE;
            Double.isNaN(d);
            THRESHOLD = (int) (d * 0.7d);
        }

        public Zip(Subscriber<? super R> subscriber, FuncN<? extends R> funcN) {
            this.child = subscriber;
            this.zipFunction = funcN;
            subscriber.add(this.childSubscription);
        }

        public void start(Observable[] observableArr, AtomicLong atomicLong) {
            this.observers = new Object[observableArr.length];
            this.requested = atomicLong;
            for (int i = 0; i < observableArr.length; i++) {
                InnerSubscriber innerSubscriber = new InnerSubscriber();
                this.observers[i] = innerSubscriber;
                this.childSubscription.add(innerSubscriber);
            }
            for (int i2 = 0; i2 < observableArr.length; i2++) {
                observableArr[i2].unsafeSubscribe((InnerSubscriber) this.observers[i2]);
            }
        }

        /* access modifiers changed from: package-private */
        public void tick() {
            Object[] objArr = this.observers;
            if (objArr != null && COUNTER_UPDATER.getAndIncrement(this) == 0) {
                int length = objArr.length;
                Observer<? super R> observer = this.child;
                AtomicLong atomicLong = this.requested;
                while (true) {
                    Object[] objArr2 = new Object[length];
                    boolean z = true;
                    for (int i = 0; i < length; i++) {
                        RxRingBuffer rxRingBuffer = ((InnerSubscriber) objArr[i]).items;
                        Object peek = rxRingBuffer.peek();
                        if (peek == null) {
                            z = false;
                        } else if (rxRingBuffer.isCompleted(peek)) {
                            observer.onCompleted();
                            this.childSubscription.unsubscribe();
                            return;
                        } else {
                            objArr2[i] = rxRingBuffer.getValue(peek);
                        }
                    }
                    if (atomicLong.get() > 0 && z) {
                        try {
                            observer.onNext(this.zipFunction.call(objArr2));
                            atomicLong.decrementAndGet();
                            this.emitted++;
                            for (Object obj : objArr) {
                                RxRingBuffer rxRingBuffer2 = ((InnerSubscriber) obj).items;
                                rxRingBuffer2.poll();
                                if (rxRingBuffer2.isCompleted(rxRingBuffer2.peek())) {
                                    observer.onCompleted();
                                    this.childSubscription.unsubscribe();
                                    return;
                                }
                            }
                            if (this.emitted > THRESHOLD) {
                                for (Object obj2 : objArr) {
                                    ((InnerSubscriber) obj2).requestMore((long) this.emitted);
                                }
                                this.emitted = 0;
                            }
                        } catch (Throwable th) {
                            observer.onError(OnErrorThrowable.addValueAsLastCause(th, objArr2));
                            return;
                        }
                    } else if (COUNTER_UPDATER.decrementAndGet(this) <= 0) {
                        return;
                    }
                }
            }
        }

        final class InnerSubscriber extends Subscriber {
            final RxRingBuffer items = RxRingBuffer.getSpmcInstance();

            InnerSubscriber() {
            }

            public void onStart() {
                request((long) RxRingBuffer.SIZE);
            }

            public void requestMore(long j) {
                request(j);
            }

            public void onCompleted() {
                this.items.onCompleted();
                Zip.this.tick();
            }

            public void onError(Throwable th) {
                Zip.this.child.onError(th);
            }

            public void onNext(Object obj) {
                try {
                    this.items.onNext(obj);
                } catch (MissingBackpressureException e) {
                    onError(e);
                }
                Zip.this.tick();
            }
        }
    }
}
