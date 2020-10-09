package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;

public final class BlockingOperatorToIterator {
    private BlockingOperatorToIterator() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterator<T> toIterator(Observable<? extends T> observable) {
        final LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        final Subscription subscribe = observable.materialize().subscribe(new Subscriber<Notification<? extends T>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                linkedBlockingQueue.offer(Notification.createOnError(th));
            }

            public void onNext(Notification<? extends T> notification) {
                linkedBlockingQueue.offer(notification);
            }
        });
        return new Iterator<T>() {
            private Notification<? extends T> buf;

            public boolean hasNext() {
                if (this.buf == null) {
                    this.buf = take();
                }
                if (!this.buf.isOnError()) {
                    return !this.buf.isOnCompleted();
                }
                throw Exceptions.propagate(this.buf.getThrowable());
            }

            public T next() {
                if (hasNext()) {
                    T value = this.buf.getValue();
                    this.buf = null;
                    return value;
                }
                throw new NoSuchElementException();
            }

            private Notification<? extends T> take() {
                try {
                    Notification<? extends T> notification = (Notification) linkedBlockingQueue.poll();
                    if (notification != null) {
                        return notification;
                    }
                    return (Notification) linkedBlockingQueue.take();
                } catch (InterruptedException e) {
                    subscribe.unsubscribe();
                    throw Exceptions.propagate(e);
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Read-only iterator");
            }
        };
    }
}
