package rx.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.util.RxThreadFactory;

final class GenericScheduledExecutorService {
    private static final GenericScheduledExecutorService INSTANCE = new GenericScheduledExecutorService();
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    private static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";
    private final ScheduledExecutorService executor;

    private GenericScheduledExecutorService() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        availableProcessors = availableProcessors > 4 ? availableProcessors / 2 : availableProcessors;
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(availableProcessors > 8 ? 8 : availableProcessors, THREAD_FACTORY);
        if (!NewThreadWorker.tryEnableCancelPolicy(newScheduledThreadPool) && (newScheduledThreadPool instanceof ScheduledThreadPoolExecutor)) {
            NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) newScheduledThreadPool);
        }
        this.executor = newScheduledThreadPool;
    }

    public static ScheduledExecutorService getInstance() {
        return INSTANCE.executor;
    }
}
