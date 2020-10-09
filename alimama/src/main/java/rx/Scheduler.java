package rx;

import java.util.concurrent.TimeUnit;
import rx.functions.Action0;
import rx.subscriptions.MultipleAssignmentSubscription;

public abstract class Scheduler {
    public abstract Worker createWorker();

    public static abstract class Worker implements Subscription {
        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action0, long j, long j2, TimeUnit timeUnit) {
            final long nanos = timeUnit.toNanos(j2);
            final long nanos2 = TimeUnit.MILLISECONDS.toNanos(now()) + timeUnit.toNanos(j);
            MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
            final MultipleAssignmentSubscription multipleAssignmentSubscription2 = multipleAssignmentSubscription;
            final Action0 action02 = action0;
            AnonymousClass1 r0 = new Action0() {
                long count = 0;

                public void call() {
                    if (!multipleAssignmentSubscription2.isUnsubscribed()) {
                        action02.call();
                        long j = nanos2;
                        long j2 = this.count + 1;
                        this.count = j2;
                        multipleAssignmentSubscription2.set(Worker.this.schedule(this, (j + (j2 * nanos)) - TimeUnit.MILLISECONDS.toNanos(Worker.this.now()), TimeUnit.NANOSECONDS));
                    }
                }
            };
            MultipleAssignmentSubscription multipleAssignmentSubscription3 = new MultipleAssignmentSubscription();
            multipleAssignmentSubscription.set(multipleAssignmentSubscription3);
            multipleAssignmentSubscription3.set(schedule(r0, j, timeUnit));
            return multipleAssignmentSubscription;
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
