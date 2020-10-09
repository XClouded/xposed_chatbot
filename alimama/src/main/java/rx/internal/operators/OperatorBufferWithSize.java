package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

public final class OperatorBufferWithSize<T> implements Observable.Operator<List<T>, T> {
    final int count;
    final int skip;

    public OperatorBufferWithSize(int i, int i2) {
        if (i <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        } else if (i2 > 0) {
            this.count = i;
            this.skip = i2;
        } else {
            throw new IllegalArgumentException("skip must be greater than 0");
        }
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> subscriber) {
        return this.count == this.skip ? new Subscriber<T>(subscriber) {
            List<T> buffer;

            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() {
                    private volatile boolean infinite = false;

                    public void request(long j) {
                        if (!this.infinite) {
                            if (j >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.count)) {
                                this.infinite = true;
                                producer.request(Long.MAX_VALUE);
                                return;
                            }
                            producer.request(j * ((long) OperatorBufferWithSize.this.count));
                        }
                    }
                });
            }

            public void onNext(T t) {
                if (this.buffer == null) {
                    this.buffer = new ArrayList(OperatorBufferWithSize.this.count);
                }
                this.buffer.add(t);
                if (this.buffer.size() == OperatorBufferWithSize.this.count) {
                    List<T> list = this.buffer;
                    this.buffer = null;
                    subscriber.onNext(list);
                }
            }

            public void onError(Throwable th) {
                this.buffer = null;
                subscriber.onError(th);
            }

            public void onCompleted() {
                List<T> list = this.buffer;
                this.buffer = null;
                if (list != null) {
                    try {
                        subscriber.onNext(list);
                    } catch (Throwable th) {
                        onError(th);
                        return;
                    }
                }
                subscriber.onCompleted();
            }
        } : new Subscriber<T>(subscriber) {
            final List<List<T>> chunks = new LinkedList();
            int index;

            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() {
                    private volatile boolean firstRequest = true;
                    private volatile boolean infinite = false;

                    private void requestInfinite() {
                        this.infinite = true;
                        producer.request(Long.MAX_VALUE);
                    }

                    public void request(long j) {
                        if (j != 0) {
                            if (j < 0) {
                                throw new IllegalArgumentException("request a negative number: " + j);
                            } else if (!this.infinite) {
                                if (j == Long.MAX_VALUE) {
                                    requestInfinite();
                                } else if (this.firstRequest) {
                                    this.firstRequest = false;
                                    long j2 = j - 1;
                                    if (j2 >= (Long.MAX_VALUE - ((long) OperatorBufferWithSize.this.count)) / ((long) OperatorBufferWithSize.this.skip)) {
                                        requestInfinite();
                                    } else {
                                        producer.request(((long) OperatorBufferWithSize.this.count) + (((long) OperatorBufferWithSize.this.skip) * j2));
                                    }
                                } else if (j >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.skip)) {
                                    requestInfinite();
                                } else {
                                    producer.request(((long) OperatorBufferWithSize.this.skip) * j);
                                }
                            }
                        }
                    }
                });
            }

            public void onNext(T t) {
                int i = this.index;
                this.index = i + 1;
                if (i % OperatorBufferWithSize.this.skip == 0) {
                    this.chunks.add(new ArrayList(OperatorBufferWithSize.this.count));
                }
                Iterator<List<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    List next = it.next();
                    next.add(t);
                    if (next.size() == OperatorBufferWithSize.this.count) {
                        it.remove();
                        subscriber.onNext(next);
                    }
                }
            }

            public void onError(Throwable th) {
                this.chunks.clear();
                subscriber.onError(th);
            }

            public void onCompleted() {
                try {
                    for (List onNext : this.chunks) {
                        subscriber.onNext(onNext);
                    }
                    subscriber.onCompleted();
                } catch (Throwable th) {
                    onError(th);
                } finally {
                    this.chunks.clear();
                }
            }
        };
    }
}
