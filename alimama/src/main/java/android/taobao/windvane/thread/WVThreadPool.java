package android.taobao.windvane.thread;

import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.weex.el.parse.Operators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WVThreadPool {
    private static final int CORE_POOL_SIZE = (CPU_COUNT + 1);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE = 500;
    private static final int MAX_POOL_SIZE = ((CPU_COUNT * 2) + 1);
    private static String TAG = "WVThreadPool";
    private static WVThreadPool threadManager;
    private ExecutorService executor = null;
    LinkedHashMap<String, Future> tasks = new LinkedHashMap<>(CORE_POOL_SIZE - 1);

    public void setClientExecutor(Executor executor2) {
        if (this.executor == null && executor2 != null && (executor2 instanceof ExecutorService)) {
            TAG += "tb";
            this.executor = (ExecutorService) executor2;
        }
    }

    public static WVThreadPool getInstance() {
        if (threadManager == null) {
            synchronized (WVThreadPool.class) {
                if (threadManager == null) {
                    threadManager = new WVThreadPool();
                }
            }
        }
        return threadManager;
    }

    public void execute(Runnable runnable) {
        execute(runnable, (String) null);
    }

    public void execute(Runnable runnable, String str) {
        if (this.executor == null) {
            this.executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(CORE_POOL_SIZE));
        }
        if (runnable == null) {
            TaoLog.w(TAG, "execute task is null.");
            return;
        }
        refreshTaskMap();
        if (TextUtils.isEmpty(str)) {
            this.executor.execute(runnable);
        } else if (this.tasks.size() == 0 || this.tasks.size() != CORE_POOL_SIZE - 1 || this.tasks.containsKey(str)) {
            Future future = (Future) this.tasks.put(str, this.executor.submit(runnable));
            if (future != null) {
                future.cancel(true);
            }
            String str2 = TAG;
            TaoLog.d(str2, "overlap the same name task:[" + str + Operators.ARRAY_END_STR);
        } else {
            String str3 = (String) this.tasks.keySet().toArray()[0];
            Future future2 = (Future) this.tasks.remove(str3);
            if (future2 != null) {
                future2.cancel(true);
            }
            this.tasks.put(str, this.executor.submit(runnable));
            String str4 = TAG;
            TaoLog.d(str4, "remove first task:[" + str3 + Operators.ARRAY_END_STR);
        }
        String str5 = TAG;
        TaoLog.d(str5, "activeTask count after:" + ((ThreadPoolExecutor) this.executor).getActiveCount());
    }

    private void refreshTaskMap() {
        if (this.tasks.size() != 0) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(((ThreadPoolExecutor) this.executor).getActiveCount());
            for (Map.Entry next : this.tasks.entrySet()) {
                if (!((Future) next.getValue()).isDone()) {
                    linkedHashMap.put(next.getKey(), next.getValue());
                }
            }
            this.tasks.clear();
            this.tasks.putAll(linkedHashMap);
        }
    }

    public ExecutorService getExecutor() {
        if (this.executor == null) {
            this.executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(CORE_POOL_SIZE));
        }
        return this.executor;
    }
}
