package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public final class OperatorWindowWithSize<T> implements Observable.Operator<Observable<T>, T> {
    final int size;
    final int skip;

    public OperatorWindowWithSize(int i, int i2) {
        this.size = i;
        this.skip = i2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> subscriber) {
        if (this.skip == this.size) {
            ExactSubscriber exactSubscriber = new ExactSubscriber(subscriber);
            exactSubscriber.init();
            return exactSubscriber;
        }
        InexactSubscriber inexactSubscriber = new InexactSubscriber(subscriber);
        inexactSubscriber.init();
        return inexactSubscriber;
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        int count;
        volatile boolean noWindow = true;
        BufferUntilSubscriber<T> window;

        public ExactSubscriber(Subscriber<? super Observable<T>> subscriber) {
            this.child = subscriber;
        }

        /* access modifiers changed from: package-private */
        public void init() {
            this.child.add(Subscriptions.create(new Action0() {
                public void call() {
                    if (ExactSubscriber.this.noWindow) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
            this.child.setProducer(new Producer() {
                public void request(long j) {
                    if (j > 0) {
                        long j2 = ((long) OperatorWindowWithSize.this.size) * j;
                        if (!((j2 >>> 31) == 0 || j2 / j == ((long) OperatorWindowWithSize.this.size))) {
                            j2 = Long.MAX_VALUE;
                        }
                        ExactSubscriber.this.requestMore(j2);
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long j) {
            request(j);
        }

        public void onNext(T t) {
            if (this.window == null) {
                this.noWindow = false;
                this.window = BufferUntilSubscriber.create();
                this.child.onNext(this.window);
            }
            this.window.onNext(t);
            int i = this.count + 1;
            this.count = i;
            if (i % OperatorWindowWithSize.this.size == 0) {
                this.window.onCompleted();
                this.window = null;
                this.noWindow = true;
                if (this.child.isUnsubscribed()) {
                    unsubscribe();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.window != null) {
                this.window.onError(th);
            }
            this.child.onError(th);
        }

        public void onCompleted() {
            if (this.window != null) {
                this.window.onCompleted();
            }
            this.child.onCompleted();
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSubject<T>> chunks = new LinkedList();
        int count;
        volatile boolean noWindow = true;

        public InexactSubscriber(Subscriber<? super Observable<T>> subscriber) {
            this.child = subscriber;
        }

        /* access modifiers changed from: package-private */
        public void init() {
            this.child.add(Subscriptions.create(new Action0() {
                public void call() {
                    if (InexactSubscriber.this.noWindow) {
                        InexactSubscriber.this.unsubscribe();
                    }
                }
            }));
            this.child.setProducer(new Producer() {
                public void request(long j) {
                    if (j > 0) {
                        long j2 = ((long) OperatorWindowWithSize.this.size) * j;
                        if (!((j2 >>> 31) == 0 || j2 / j == ((long) OperatorWindowWithSize.this.size))) {
                            j2 = Long.MAX_VALUE;
                        }
                        InexactSubscriber.this.requestMore(j2);
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long j) {
            request(j);
        }

        public void onNext(T t) {
            int i = this.count;
            this.count = i + 1;
            if (i % OperatorWindowWithSize.this.skip == 0 && !this.child.isUnsubscribed()) {
                if (this.chunks.isEmpty()) {
                    this.noWindow = false;
                }
                CountedSubject createCountedSubject = createCountedSubject();
                this.chunks.add(createCountedSubject);
                this.child.onNext(createCountedSubject.producer);
            }
            Iterator<CountedSubject<T>> it = this.chunks.iterator();
            while (it.hasNext()) {
                CountedSubject next = it.next();
                next.consumer.onNext(t);
                int i2 = next.count + 1;
                next.count = i2;
                if (i2 == OperatorWindowWithSize.this.size) {
                    it.remove();
                    next.consumer.onCompleted();
                }
            }
            if (this.chunks.isEmpty()) {
                this.noWindow = true;
                if (this.child.isUnsubscribed()) {
                    unsubscribe();
                }
            }
        }

        public void onError(Throwable th) {
            ArrayList<CountedSubject> arrayList = new ArrayList<>(this.chunks);
            this.chunks.clear();
            this.noWindow = true;
            for (CountedSubject countedSubject : arrayList) {
                countedSubject.consumer.onError(th);
            }
            this.child.onError(th);
        }

        public void onCompleted() {
            ArrayList<CountedSubject> arrayList = new ArrayList<>(this.chunks);
            this.chunks.clear();
            this.noWindow = true;
            for (CountedSubject countedSubject : arrayList) {
                countedSubject.consumer.onCompleted();
            }
            this.child.onCompleted();
        }

        /* access modifiers changed from: package-private */
        public CountedSubject<T> createCountedSubject() {
            BufferUntilSubscriber create = BufferUntilSubscriber.create();
            return new CountedSubject<>(create, create);
        }
    }

    static final class CountedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSubject(Observer<T> observer, Observable<T> observable) {
            this.consumer = observer;
            this.producer = observable;
        }
    }
}
