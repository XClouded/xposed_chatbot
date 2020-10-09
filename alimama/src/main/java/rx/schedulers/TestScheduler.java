package rx.schedulers;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.Subscriptions;

public class TestScheduler extends Scheduler {
    private static long counter;
    /* access modifiers changed from: private */
    public final Queue<TimedAction> queue = new PriorityQueue(11, new CompareActionsByTime());
    /* access modifiers changed from: private */
    public long time;

    static /* synthetic */ long access$108() {
        long j = counter;
        counter = 1 + j;
        return j;
    }

    private static final class TimedAction {
        /* access modifiers changed from: private */
        public final Action0 action;
        /* access modifiers changed from: private */
        public final long count;
        /* access modifiers changed from: private */
        public final Scheduler.Worker scheduler;
        /* access modifiers changed from: private */
        public final long time;

        private TimedAction(Scheduler.Worker worker, long j, Action0 action0) {
            this.count = TestScheduler.access$108();
            this.time = j;
            this.action = action0;
            this.scheduler = worker;
        }

        public String toString() {
            return String.format("TimedAction(time = %d, action = %s)", new Object[]{Long.valueOf(this.time), this.action.toString()});
        }
    }

    private static class CompareActionsByTime implements Comparator<TimedAction> {
        private CompareActionsByTime() {
        }

        public int compare(TimedAction timedAction, TimedAction timedAction2) {
            if (timedAction.time == timedAction2.time) {
                return Long.valueOf(timedAction.count).compareTo(Long.valueOf(timedAction2.count));
            }
            return Long.valueOf(timedAction.time).compareTo(Long.valueOf(timedAction2.time));
        }
    }

    public long now() {
        return TimeUnit.NANOSECONDS.toMillis(this.time);
    }

    public void advanceTimeBy(long j, TimeUnit timeUnit) {
        advanceTimeTo(this.time + timeUnit.toNanos(j), TimeUnit.NANOSECONDS);
    }

    public void advanceTimeTo(long j, TimeUnit timeUnit) {
        triggerActions(timeUnit.toNanos(j));
    }

    public void triggerActions() {
        triggerActions(this.time);
    }

    private void triggerActions(long j) {
        while (!this.queue.isEmpty()) {
            TimedAction peek = this.queue.peek();
            if (peek.time > j) {
                break;
            }
            this.time = peek.time == 0 ? this.time : peek.time;
            this.queue.remove();
            if (!peek.scheduler.isUnsubscribed()) {
                peek.action.call();
            }
        }
        this.time = j;
    }

    public Scheduler.Worker createWorker() {
        return new InnerTestScheduler();
    }

    private final class InnerTestScheduler extends Scheduler.Worker {
        private final BooleanSubscription s;

        private InnerTestScheduler() {
            this.s = new BooleanSubscription();
        }

        public void unsubscribe() {
            this.s.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.s.isUnsubscribed();
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            final TimedAction timedAction = new TimedAction(this, TestScheduler.this.time + timeUnit.toNanos(j), action0);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new Action0() {
                public void call() {
                    TestScheduler.this.queue.remove(timedAction);
                }
            });
        }

        public Subscription schedule(Action0 action0) {
            final TimedAction timedAction = new TimedAction(this, 0, action0);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new Action0() {
                public void call() {
                    TestScheduler.this.queue.remove(timedAction);
                }
            });
        }

        public long now() {
            return TestScheduler.this.now();
        }
    }
}
