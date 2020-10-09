package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;

public class OperatorSubscribeOn<T> implements Observable.Operator<T, Observable<T>> {
    private final Scheduler scheduler;

    public OperatorSubscribeOn(Scheduler scheduler2) {
        this.scheduler = scheduler2;
    }

    public Subscriber<? super Observable<T>> call(final Subscriber<? super T> subscriber) {
        final Scheduler.Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        return new Subscriber<Observable<T>>(subscriber) {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(final Observable<T> observable) {
                createWorker.schedule(new Action0() {
                    public void call() {
                        final Thread currentThread = Thread.currentThread();
                        observable.unsafeSubscribe(new Subscriber<T>(subscriber) {
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            public void onError(Throwable th) {
                                subscriber.onError(th);
                            }

                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }

                            public void setProducer(final Producer producer) {
                                subscriber.setProducer(new Producer() {
                                    public void request(final long j) {
                                        if (Thread.currentThread() == currentThread) {
                                            producer.request(j);
                                        } else {
                                            createWorker.schedule(new Action0() {
                                                public void call() {
                                                    producer.request(j);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
    }
}
