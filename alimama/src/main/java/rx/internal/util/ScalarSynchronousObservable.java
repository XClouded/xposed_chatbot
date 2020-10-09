package rx.internal.util;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

public final class ScalarSynchronousObservable<T> extends Observable<T> {
    /* access modifiers changed from: private */
    public final T t;

    public static final <T> ScalarSynchronousObservable<T> create(T t2) {
        return new ScalarSynchronousObservable<>(t2);
    }

    protected ScalarSynchronousObservable(final T t2) {
        super(new Observable.OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(t2);
                subscriber.onCompleted();
            }
        });
        this.t = t2;
    }

    public T get() {
        return this.t;
    }

    public Observable<T> scalarScheduleOn(Scheduler scheduler) {
        if (scheduler instanceof EventLoopsScheduler) {
            return create(new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.t));
        }
        return create(new NormalScheduledEmission(scheduler, this.t));
    }

    static final class DirectScheduledEmission<T> implements Observable.OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler eventLoopsScheduler, T t) {
            this.es = eventLoopsScheduler;
            this.value = t;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(this.es.scheduleDirect(new ScalarSynchronousAction(subscriber, this.value)));
        }
    }

    static final class NormalScheduledEmission<T> implements Observable.OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler2, T t) {
            this.scheduler = scheduler2;
            this.value = t;
        }

        public void call(Subscriber<? super T> subscriber) {
            Scheduler.Worker createWorker = this.scheduler.createWorker();
            subscriber.add(createWorker);
            createWorker.schedule(new ScalarSynchronousAction(subscriber, this.value));
        }
    }

    static final class ScalarSynchronousAction<T> implements Action0 {
        private final Subscriber<? super T> subscriber;
        private final T value;

        private ScalarSynchronousAction(Subscriber<? super T> subscriber2, T t) {
            this.subscriber = subscriber2;
            this.value = t;
        }

        public void call() {
            try {
                this.subscriber.onNext(this.value);
                this.subscriber.onCompleted();
            } catch (Throwable th) {
                this.subscriber.onError(th);
            }
        }
    }

    public <R> Observable<R> scalarFlatMap(final Func1<? super T, ? extends Observable<? extends R>> func1) {
        return create(new Observable.OnSubscribe<R>() {
            public void call(final Subscriber<? super R> subscriber) {
                Observable observable = (Observable) func1.call(ScalarSynchronousObservable.this.t);
                if (observable.getClass() == ScalarSynchronousObservable.class) {
                    subscriber.onNext(((ScalarSynchronousObservable) observable).t);
                    subscriber.onCompleted();
                    return;
                }
                observable.unsafeSubscribe(new Subscriber<R>(subscriber) {
                    public void onNext(R r) {
                        subscriber.onNext(r);
                    }

                    public void onError(Throwable th) {
                        subscriber.onError(th);
                    }

                    public void onCompleted() {
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }
}
