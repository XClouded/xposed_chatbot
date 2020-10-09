package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Notification;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

public final class OperatorMaterialize<T> implements Observable.Operator<Notification<T>, T> {

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE = new OperatorMaterialize<>();

        private Holder() {
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorMaterialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> subscriber) {
        final ParentSubscriber parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.add(parentSubscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                if (j > 0) {
                    parentSubscriber.requestMore(j);
                }
            }
        });
        return parentSubscriber;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private static final AtomicLongFieldUpdater<ParentSubscriber> REQUESTED = AtomicLongFieldUpdater.newUpdater(ParentSubscriber.class, "requested");
        private boolean busy = false;
        private final Subscriber<? super Notification<T>> child;
        private boolean missed = false;
        private volatile long requested;
        private volatile Notification<T> terminalNotification;

        ParentSubscriber(Subscriber<? super Notification<T>> subscriber) {
            this.child = subscriber;
        }

        public void onStart() {
            request(0);
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long j) {
            BackpressureUtils.getAndAddRequest(REQUESTED, this, j);
            request(j);
            drain();
        }

        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        public void onError(Throwable th) {
            this.terminalNotification = Notification.createOnError(th);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
            drain();
        }

        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        private void decrementRequested() {
            long j;
            do {
                j = this.requested;
                if (j == Long.MAX_VALUE) {
                    return;
                }
            } while (!REQUESTED.compareAndSet(this, j, j - 1));
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            r0 = r6.terminalNotification;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
            if (r0 == null) goto L_0x0035;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
            if (r6.requested <= 0) goto L_0x0035;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001f, code lost:
            r6.terminalNotification = null;
            r6.child.onNext(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
            r6.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0035, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
            if (r6.missed != false) goto L_0x003f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
            r6.busy = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x003d, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003f, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0044, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
            if (r6.child.isUnsubscribed() != false) goto L_0x0044;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void drain() {
            /*
                r6 = this;
                monitor-enter(r6)
                boolean r0 = r6.busy     // Catch:{ all -> 0x0045 }
                if (r0 == 0) goto L_0x000a
                r0 = 1
                r6.missed = r0     // Catch:{ all -> 0x0045 }
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
                return
            L_0x000a:
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
            L_0x000b:
                rx.Subscriber<? super rx.Notification<T>> r0 = r6.child
                boolean r0 = r0.isUnsubscribed()
                if (r0 != 0) goto L_0x0044
                rx.Notification<T> r0 = r6.terminalNotification
                if (r0 == 0) goto L_0x0035
                long r1 = r6.requested
                r3 = 0
                int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r5 <= 0) goto L_0x0035
                r1 = 0
                r6.terminalNotification = r1
                rx.Subscriber<? super rx.Notification<T>> r1 = r6.child
                r1.onNext(r0)
                rx.Subscriber<? super rx.Notification<T>> r0 = r6.child
                boolean r0 = r0.isUnsubscribed()
                if (r0 != 0) goto L_0x0034
                rx.Subscriber<? super rx.Notification<T>> r0 = r6.child
                r0.onCompleted()
            L_0x0034:
                return
            L_0x0035:
                monitor-enter(r6)
                boolean r0 = r6.missed     // Catch:{ all -> 0x0041 }
                if (r0 != 0) goto L_0x003f
                r0 = 0
                r6.busy = r0     // Catch:{ all -> 0x0041 }
                monitor-exit(r6)     // Catch:{ all -> 0x0041 }
                return
            L_0x003f:
                monitor-exit(r6)     // Catch:{ all -> 0x0041 }
                goto L_0x000b
            L_0x0041:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0041 }
                throw r0
            L_0x0044:
                return
            L_0x0045:
                r0 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0045 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMaterialize.ParentSubscriber.drain():void");
        }
    }
}
