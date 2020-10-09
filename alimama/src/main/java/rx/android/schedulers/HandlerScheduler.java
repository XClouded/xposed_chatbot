package rx.android.schedulers;

import android.os.Handler;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    public static HandlerScheduler from(Handler handler2) {
        if (handler2 != null) {
            return new HandlerScheduler(handler2);
        }
        throw new NullPointerException("handler == null");
    }

    HandlerScheduler(Handler handler2) {
        this.handler = handler2;
    }

    public Scheduler.Worker createWorker() {
        return new HandlerWorker(this.handler);
    }

    static class HandlerWorker extends Scheduler.Worker {
        private final CompositeSubscription compositeSubscription = new CompositeSubscription();
        /* access modifiers changed from: private */
        public final Handler handler;

        HandlerWorker(Handler handler2) {
            this.handler = handler2;
        }

        public void unsubscribe() {
            this.compositeSubscription.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.compositeSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            if (this.compositeSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            final ScheduledAction scheduledAction = new ScheduledAction(RxAndroidPlugins.getInstance().getSchedulersHook().onSchedule(action0));
            scheduledAction.addParent(this.compositeSubscription);
            this.compositeSubscription.add(scheduledAction);
            this.handler.postDelayed(scheduledAction, timeUnit.toMillis(j));
            scheduledAction.add(Subscriptions.create(new Action0() {
                public void call() {
                    HandlerWorker.this.handler.removeCallbacks(scheduledAction);
                }
            }));
            return scheduledAction;
        }

        public Subscription schedule(Action0 action0) {
            return schedule(action0, 0, TimeUnit.MILLISECONDS);
        }
    }
}
