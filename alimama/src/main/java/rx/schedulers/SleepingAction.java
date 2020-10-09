package rx.schedulers;

import rx.Scheduler;
import rx.functions.Action0;

class SleepingAction implements Action0 {
    private final long execTime;
    private final Scheduler.Worker innerScheduler;
    private final Action0 underlying;

    public SleepingAction(Action0 action0, Scheduler.Worker worker, long j) {
        this.underlying = action0;
        this.innerScheduler = worker;
        this.execTime = j;
    }

    public void call() {
        if (!this.innerScheduler.isUnsubscribed()) {
            if (this.execTime > this.innerScheduler.now()) {
                long now = this.execTime - this.innerScheduler.now();
                if (now > 0) {
                    try {
                        Thread.sleep(now);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!this.innerScheduler.isUnsubscribed()) {
                this.underlying.call();
            }
        }
    }
}
