package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func2;

public final class OperatorScan<R, T> implements Observable.Operator<R, T> {
    /* access modifiers changed from: private */
    public static final Object NO_INITIAL_VALUE = new Object();
    /* access modifiers changed from: private */
    public final Func2<R, ? super T, R> accumulator;
    /* access modifiers changed from: private */
    public final Func0<R> initialValueFactory;

    public OperatorScan(final R r, Func2<R, ? super T, R> func2) {
        this(new Func0<R>() {
            public R call() {
                return r;
            }
        }, func2);
    }

    public OperatorScan(Func0<R> func0, Func2<R, ? super T, R> func2) {
        this.initialValueFactory = func0;
        this.accumulator = func2;
    }

    public OperatorScan(Func2<R, ? super T, R> func2) {
        this(NO_INITIAL_VALUE, func2);
    }

    public Subscriber<? super T> call(final Subscriber<? super R> subscriber) {
        return new Subscriber<T>(subscriber) {
            /* access modifiers changed from: private */
            public final R initialValue = OperatorScan.this.initialValueFactory.call();
            boolean initialized = false;
            private R value = this.initialValue;

            /* JADX WARNING: type inference failed for: r3v0, types: [R, T, java.lang.Object] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onNext(T r3) {
                /*
                    r2 = this;
                    rx.Subscriber r0 = r2
                    r2.emitInitialValueIfNeeded(r0)
                    R r0 = r2.value
                    java.lang.Object r1 = rx.internal.operators.OperatorScan.NO_INITIAL_VALUE
                    if (r0 != r1) goto L_0x0010
                    r2.value = r3
                    goto L_0x001e
                L_0x0010:
                    rx.internal.operators.OperatorScan r0 = rx.internal.operators.OperatorScan.this     // Catch:{ Throwable -> 0x0026 }
                    rx.functions.Func2 r0 = r0.accumulator     // Catch:{ Throwable -> 0x0026 }
                    R r1 = r2.value     // Catch:{ Throwable -> 0x0026 }
                    java.lang.Object r0 = r0.call(r1, r3)     // Catch:{ Throwable -> 0x0026 }
                    r2.value = r0     // Catch:{ Throwable -> 0x0026 }
                L_0x001e:
                    rx.Subscriber r3 = r2
                    R r0 = r2.value
                    r3.onNext(r0)
                    return
                L_0x0026:
                    r0 = move-exception
                    rx.exceptions.Exceptions.throwIfFatal(r0)
                    rx.Subscriber r1 = r2
                    java.lang.Throwable r3 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r3)
                    r1.onError(r3)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorScan.AnonymousClass2.onNext(java.lang.Object):void");
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onCompleted() {
                emitInitialValueIfNeeded(subscriber);
                subscriber.onCompleted();
            }

            private void emitInitialValueIfNeeded(Subscriber<? super R> subscriber) {
                if (!this.initialized) {
                    this.initialized = true;
                    if (this.initialValue != OperatorScan.NO_INITIAL_VALUE) {
                        subscriber.onNext(this.initialValue);
                    }
                }
            }

            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() {
                    final AtomicBoolean excessive = new AtomicBoolean();
                    final AtomicBoolean once = new AtomicBoolean();

                    public void request(long j) {
                        if (this.once.compareAndSet(false, true)) {
                            if (AnonymousClass2.this.initialValue == OperatorScan.NO_INITIAL_VALUE || j == Long.MAX_VALUE) {
                                producer.request(j);
                            } else if (j == 1) {
                                this.excessive.set(true);
                                producer.request(1);
                            } else {
                                producer.request(j - 1);
                            }
                        } else if (j <= 1 || !this.excessive.compareAndSet(true, false) || j == Long.MAX_VALUE) {
                            producer.request(j);
                        } else {
                            producer.request(j - 1);
                        }
                    }
                });
            }
        };
    }
}
