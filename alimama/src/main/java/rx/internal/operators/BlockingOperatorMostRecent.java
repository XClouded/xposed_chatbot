package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorMostRecent {
    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> observable, final T t) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                MostRecentObserver mostRecentObserver = new MostRecentObserver(t);
                observable.subscribe(mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }

    private static final class MostRecentObserver<T> extends Subscriber<T> {
        final NotificationLite<T> nl;
        volatile Object value;

        private MostRecentObserver(T t) {
            this.nl = NotificationLite.instance();
            this.value = this.nl.next(t);
        }

        public void onCompleted() {
            this.value = this.nl.completed();
        }

        public void onError(Throwable th) {
            this.value = this.nl.error(th);
        }

        public void onNext(T t) {
            this.value = this.nl.next(t);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                private Object buf = null;

                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !MostRecentObserver.this.nl.isCompleted(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentObserver.this.value;
                        }
                        if (MostRecentObserver.this.nl.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (!MostRecentObserver.this.nl.isError(this.buf)) {
                            T value = MostRecentObserver.this.nl.getValue(this.buf);
                            this.buf = obj;
                            return value;
                        } else {
                            throw Exceptions.propagate(MostRecentObserver.this.nl.getError(this.buf));
                        }
                    } finally {
                        this.buf = obj;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }
}
