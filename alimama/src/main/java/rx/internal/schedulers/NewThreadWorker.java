package rx.internal.schedulers;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.internal.util.PlatformDependent;
import rx.internal.util.SubscriptionList;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class NewThreadWorker extends Scheduler.Worker implements Subscription {
    private static final ConcurrentHashMap<ScheduledThreadPoolExecutor, ScheduledThreadPoolExecutor> EXECUTORS = new ConcurrentHashMap<>();
    private static final String FREQUENCY_KEY = "rx.scheduler.jdk6.purge-frequency-millis";
    private static final AtomicReference<ScheduledExecutorService> PURGE = new AtomicReference<>();
    private static final String PURGE_FORCE_KEY = "rx.scheduler.jdk6.purge-force";
    public static final int PURGE_FREQUENCY = Integer.getInteger(FREQUENCY_KEY, 1000).intValue();
    private static final String PURGE_THREAD_PREFIX = "RxSchedulerPurge-";
    private static final Object SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED = new Object();
    private static final boolean SHOULD_TRY_ENABLE_CANCEL_POLICY;
    private static volatile Object cachedSetRemoveOnCancelPolicyMethod;
    private final ScheduledExecutorService executor;
    volatile boolean isUnsubscribed;
    private final RxJavaSchedulersHook schedulersHook;

    static {
        boolean z = Boolean.getBoolean(PURGE_FORCE_KEY);
        int androidApiVersion = PlatformDependent.getAndroidApiVersion();
        SHOULD_TRY_ENABLE_CANCEL_POLICY = !z && (androidApiVersion == 0 || androidApiVersion >= 21);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void registerExecutor(java.util.concurrent.ScheduledThreadPoolExecutor r10) {
        /*
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<java.util.concurrent.ScheduledExecutorService> r0 = PURGE
            java.lang.Object r0 = r0.get()
            java.util.concurrent.ScheduledExecutorService r0 = (java.util.concurrent.ScheduledExecutorService) r0
            if (r0 == 0) goto L_0x000b
            goto L_0x0030
        L_0x000b:
            r0 = 1
            rx.internal.util.RxThreadFactory r1 = new rx.internal.util.RxThreadFactory
            java.lang.String r2 = "RxSchedulerPurge-"
            r1.<init>(r2)
            java.util.concurrent.ScheduledExecutorService r3 = java.util.concurrent.Executors.newScheduledThreadPool(r0, r1)
            java.util.concurrent.atomic.AtomicReference<java.util.concurrent.ScheduledExecutorService> r0 = PURGE
            r1 = 0
            boolean r0 = r0.compareAndSet(r1, r3)
            if (r0 == 0) goto L_0x0000
            rx.internal.schedulers.NewThreadWorker$1 r4 = new rx.internal.schedulers.NewThreadWorker$1
            r4.<init>()
            int r0 = PURGE_FREQUENCY
            long r5 = (long) r0
            int r0 = PURGE_FREQUENCY
            long r7 = (long) r0
            java.util.concurrent.TimeUnit r9 = java.util.concurrent.TimeUnit.MILLISECONDS
            r3.scheduleAtFixedRate(r4, r5, r7, r9)
        L_0x0030:
            java.util.concurrent.ConcurrentHashMap<java.util.concurrent.ScheduledThreadPoolExecutor, java.util.concurrent.ScheduledThreadPoolExecutor> r0 = EXECUTORS
            r0.putIfAbsent(r10, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.schedulers.NewThreadWorker.registerExecutor(java.util.concurrent.ScheduledThreadPoolExecutor):void");
    }

    public static void deregisterExecutor(ScheduledExecutorService scheduledExecutorService) {
        EXECUTORS.remove(scheduledExecutorService);
    }

    static void purgeExecutors() {
        try {
            Iterator<ScheduledThreadPoolExecutor> it = EXECUTORS.keySet().iterator();
            while (it.hasNext()) {
                ScheduledThreadPoolExecutor next = it.next();
                if (!next.isShutdown()) {
                    next.purge();
                } else {
                    it.remove();
                }
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
        }
    }

    public static boolean tryEnableCancelPolicy(ScheduledExecutorService scheduledExecutorService) {
        Method method;
        Object obj;
        if (SHOULD_TRY_ENABLE_CANCEL_POLICY) {
            if (scheduledExecutorService instanceof ScheduledThreadPoolExecutor) {
                Object obj2 = cachedSetRemoveOnCancelPolicyMethod;
                if (obj2 == SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED) {
                    return false;
                }
                if (obj2 == null) {
                    method = findSetRemoveOnCancelPolicyMethod(scheduledExecutorService);
                    if (method != null) {
                        obj = method;
                    } else {
                        obj = SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED;
                    }
                    cachedSetRemoveOnCancelPolicyMethod = obj;
                } else {
                    method = (Method) obj2;
                }
            } else {
                method = findSetRemoveOnCancelPolicyMethod(scheduledExecutorService);
            }
            if (method != null) {
                try {
                    method.invoke(scheduledExecutorService, new Object[]{true});
                    return true;
                } catch (Exception e) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                }
            }
        }
        return false;
    }

    static Method findSetRemoveOnCancelPolicyMethod(ScheduledExecutorService scheduledExecutorService) {
        for (Method method : scheduledExecutorService.getClass().getMethods()) {
            if (method.getName().equals("setRemoveOnCancelPolicy")) {
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == Boolean.TYPE) {
                    return method;
                }
            }
        }
        return null;
    }

    public NewThreadWorker(ThreadFactory threadFactory) {
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, threadFactory);
        if (!tryEnableCancelPolicy(newScheduledThreadPool) && (newScheduledThreadPool instanceof ScheduledThreadPoolExecutor)) {
            registerExecutor((ScheduledThreadPoolExecutor) newScheduledThreadPool);
        }
        this.schedulersHook = RxJavaPlugins.getInstance().getSchedulersHook();
        this.executor = newScheduledThreadPool;
    }

    public Subscription schedule(Action0 action0) {
        return schedule(action0, 0, (TimeUnit) null);
    }

    public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
        if (this.isUnsubscribed) {
            return Subscriptions.unsubscribed();
        }
        return scheduleActual(action0, j, timeUnit);
    }

    public ScheduledAction scheduleActual(Action0 action0, long j, TimeUnit timeUnit) {
        Future future;
        ScheduledAction scheduledAction = new ScheduledAction(this.schedulersHook.onSchedule(action0));
        if (j <= 0) {
            future = this.executor.submit(scheduledAction);
        } else {
            future = this.executor.schedule(scheduledAction, j, timeUnit);
        }
        scheduledAction.add((Future<?>) future);
        return scheduledAction;
    }

    public ScheduledAction scheduleActual(Action0 action0, long j, TimeUnit timeUnit, CompositeSubscription compositeSubscription) {
        Future future;
        ScheduledAction scheduledAction = new ScheduledAction(this.schedulersHook.onSchedule(action0), compositeSubscription);
        compositeSubscription.add(scheduledAction);
        if (j <= 0) {
            future = this.executor.submit(scheduledAction);
        } else {
            future = this.executor.schedule(scheduledAction, j, timeUnit);
        }
        scheduledAction.add((Future<?>) future);
        return scheduledAction;
    }

    public ScheduledAction scheduleActual(Action0 action0, long j, TimeUnit timeUnit, SubscriptionList subscriptionList) {
        Future future;
        ScheduledAction scheduledAction = new ScheduledAction(this.schedulersHook.onSchedule(action0), subscriptionList);
        subscriptionList.add(scheduledAction);
        if (j <= 0) {
            future = this.executor.submit(scheduledAction);
        } else {
            future = this.executor.schedule(scheduledAction, j, timeUnit);
        }
        scheduledAction.add((Future<?>) future);
        return scheduledAction;
    }

    public void unsubscribe() {
        this.isUnsubscribed = true;
        this.executor.shutdownNow();
        deregisterExecutor(this.executor);
    }

    public boolean isUnsubscribed() {
        return this.isUnsubscribed;
    }
}
