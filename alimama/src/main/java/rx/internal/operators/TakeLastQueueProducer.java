package rx.internal.operators;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Producer;
import rx.Subscriber;

final class TakeLastQueueProducer<T> implements Producer {
    private static final AtomicLongFieldUpdater<TakeLastQueueProducer> REQUESTED_UPDATER = AtomicLongFieldUpdater.newUpdater(TakeLastQueueProducer.class, "requested");
    private final Deque<Object> deque;
    private volatile boolean emittingStarted = false;
    private final NotificationLite<T> notification;
    private volatile long requested = 0;
    private final Subscriber<? super T> subscriber;

    public TakeLastQueueProducer(NotificationLite<T> notificationLite, Deque<Object> deque2, Subscriber<? super T> subscriber2) {
        this.notification = notificationLite;
        this.deque = deque2;
        this.subscriber = subscriber2;
    }

    /* access modifiers changed from: package-private */
    public void startEmitting() {
        if (!this.emittingStarted) {
            this.emittingStarted = true;
            emit(0);
        }
    }

    public void request(long j) {
        long j2;
        if (this.requested != Long.MAX_VALUE) {
            if (j == Long.MAX_VALUE) {
                j2 = REQUESTED_UPDATER.getAndSet(this, Long.MAX_VALUE);
            } else {
                j2 = BackpressureUtils.getAndAddRequest(REQUESTED_UPDATER, this, j);
            }
            if (this.emittingStarted) {
                emit(j2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void emit(long j) {
        Object poll;
        if (this.requested == Long.MAX_VALUE) {
            if (j == 0) {
                try {
                    for (Object next : this.deque) {
                        if (this.subscriber.isUnsubscribed()) {
                            this.deque.clear();
                            return;
                        }
                        this.notification.accept(this.subscriber, next);
                    }
                } catch (Throwable th) {
                    this.deque.clear();
                    throw th;
                }
                this.deque.clear();
            }
        } else if (j == 0) {
            while (true) {
                long j2 = this.requested;
                int i = 0;
                while (true) {
                    j2--;
                    if (j2 >= 0 && (poll = this.deque.poll()) != null) {
                        if (!this.subscriber.isUnsubscribed() && !this.notification.accept(this.subscriber, poll)) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
                while (true) {
                    long j3 = this.requested;
                    long j4 = j3 - ((long) i);
                    if (j3 == Long.MAX_VALUE) {
                        break;
                    } else if (REQUESTED_UPDATER.compareAndSet(this, j3, j4)) {
                        if (j4 == 0) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
