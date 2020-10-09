package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorLatest {
    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(final Observable<? extends T> observable) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                LatestObserverIterator latestObserverIterator = new LatestObserverIterator();
                observable.materialize().subscribe(latestObserverIterator);
                return latestObserverIterator;
            }
        };
    }

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        static final AtomicReferenceFieldUpdater<LatestObserverIterator, Notification> REFERENCE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(LatestObserverIterator.class, Notification.class, "value");
        Notification<? extends T> iNotif;
        final Semaphore notify = new Semaphore(0);
        volatile Notification<? extends T> value;

        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        LatestObserverIterator() {
        }

        public void onNext(Notification<? extends T> notification) {
            if (REFERENCE_UPDATER.getAndSet(this, notification) == null) {
                this.notify.release();
            }
        }

        public boolean hasNext() {
            if (this.iNotif == null || !this.iNotif.isOnError()) {
                if ((this.iNotif == null || !this.iNotif.isOnCompleted()) && this.iNotif == null) {
                    try {
                        this.notify.acquire();
                        this.iNotif = REFERENCE_UPDATER.getAndSet(this, (Object) null);
                        if (this.iNotif.isOnError()) {
                            throw Exceptions.propagate(this.iNotif.getThrowable());
                        }
                    } catch (InterruptedException e) {
                        unsubscribe();
                        Thread.currentThread().interrupt();
                        this.iNotif = Notification.createOnError(e);
                        throw Exceptions.propagate(e);
                    }
                }
                return !this.iNotif.isOnCompleted();
            }
            throw Exceptions.propagate(this.iNotif.getThrowable());
        }

        public T next() {
            if (!hasNext() || !this.iNotif.isOnNext()) {
                throw new NoSuchElementException();
            }
            T value2 = this.iNotif.getValue();
            this.iNotif = null;
            return value2;
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }
}
