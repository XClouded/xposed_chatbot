package rx.schedulers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.Subscriptions;

final class ExecutorScheduler extends Scheduler {
    final Executor executor;

    public ExecutorScheduler(Executor executor2) {
        this.executor = executor2;
    }

    public Scheduler.Worker createWorker() {
        return new ExecutorSchedulerWorker(this.executor);
    }

    static final class ExecutorSchedulerWorker extends Scheduler.Worker implements Runnable {
        final Executor executor;
        final ConcurrentLinkedQueue<ScheduledAction> queue = new ConcurrentLinkedQueue<>();
        final CompositeSubscription tasks = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger();

        public ExecutorSchedulerWorker(Executor executor2) {
            this.executor = executor2;
        }

        public Subscription schedule(Action0 action0) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            ScheduledAction scheduledAction = new ScheduledAction(action0, this.tasks);
            this.tasks.add(scheduledAction);
            this.queue.offer(scheduledAction);
            if (this.wip.getAndIncrement() == 0) {
                try {
                    this.executor.execute(this);
                } catch (RejectedExecutionException e) {
                    this.tasks.remove(scheduledAction);
                    this.wip.decrementAndGet();
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    throw e;
                }
            }
            return scheduledAction;
        }

        public void run() {
            do {
                ScheduledAction poll = this.queue.poll();
                if (!poll.isUnsubscribed()) {
                    poll.run();
                }
            } while (this.wip.decrementAndGet() > 0);
        }

        public Subscription schedule(final Action0 action0, long j, TimeUnit timeUnit) {
            ScheduledExecutorService scheduledExecutorService;
            if (j <= 0) {
                return schedule(action0);
            }
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            if (this.executor instanceof ScheduledExecutorService) {
                scheduledExecutorService = (ScheduledExecutorService) this.executor;
            } else {
                scheduledExecutorService = GenericScheduledExecutorService.getInstance();
            }
            MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
            final MultipleAssignmentSubscription multipleAssignmentSubscription2 = new MultipleAssignmentSubscription();
            multipleAssignmentSubscription2.set(multipleAssignmentSubscription);
            this.tasks.add(multipleAssignmentSubscription2);
            final Subscription create = Subscriptions.create(new Action0() {
                public void call() {
                    ExecutorSchedulerWorker.this.tasks.remove(multipleAssignmentSubscription2);
                }
            });
            ScheduledAction scheduledAction = new ScheduledAction(new Action0() {
                public void call() {
                    if (!multipleAssignmentSubscription2.isUnsubscribed()) {
                        Subscription schedule = ExecutorSchedulerWorker.this.schedule(action0);
                        multipleAssignmentSubscription2.set(schedule);
                        if (schedule.getClass() == ScheduledAction.class) {
                            ((ScheduledAction) schedule).add(create);
                        }
                    }
                }
            });
            multipleAssignmentSubscription.set(scheduledAction);
            try {
                scheduledAction.add((Future<?>) scheduledExecutorService.schedule(scheduledAction, j, timeUnit));
                return create;
            } catch (RejectedExecutionException e) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                throw e;
            }
        }

        public boolean isUnsubscribed() {
            return this.tasks.isUnsubscribed();
        }

        public void unsubscribe() {
            this.tasks.unsubscribe();
        }
    }
}
