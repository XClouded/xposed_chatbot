package mtopsdk.mtop.upload.util;

import android.os.Process;
import com.taobao.weex.el.parse.Operators;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class FileUploadThreadPoolExecutorFactory {
    private static final int KEEP_ALIVE_TIME = 10;
    private static final int REMOVE_TASKS_CORE_POOL_SIZE = 1;
    private static final int REMOVE_TASKS_MAX_POOL_SIZE = 1;
    private static final String TAG = "mtopsdk.FileUploadThreadPoolExecutorFactory";
    private static int priority = 10;
    private static volatile ThreadPoolExecutor removeTasksExecutor;
    private static volatile ThreadPoolExecutor uploadTasksExecutor;

    private static class FileUploadThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger();
        /* access modifiers changed from: private */
        public int priority = 10;
        private String type = "";

        public FileUploadThreadFactory(int i, String str) {
            this.priority = i;
            this.type = str;
        }

        public Thread newThread(Runnable runnable) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("FileUpload ");
            if (StringUtils.isNotBlank(this.type)) {
                sb.append(this.type);
                sb.append(Operators.SPACE_STR);
            }
            sb.append("Thread:");
            sb.append(this.mCount.getAndIncrement());
            return new Thread(runnable, sb.toString()) {
                public void run() {
                    Process.setThreadPriority(FileUploadThreadFactory.this.priority);
                    super.run();
                }
            };
        }
    }

    public static Future<?> submitRemoveTask(Runnable runnable) {
        try {
            if (removeTasksExecutor == null) {
                synchronized (FileUploadThreadPoolExecutorFactory.class) {
                    if (removeTasksExecutor == null) {
                        removeTasksExecutor = createExecutor(1, 1, 10, 0, new FileUploadThreadFactory(priority, "RemoveTasks"));
                    }
                }
            }
            return removeTasksExecutor.submit(runnable);
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[submitRemoveTask]submit runnable to FileUpload RemoveTasks ThreadPool error ---" + th.toString());
            return null;
        }
    }

    public static Future<?> submitUploadTask(Runnable runnable) {
        try {
            if (uploadTasksExecutor == null) {
                synchronized (FileUploadThreadPoolExecutorFactory.class) {
                    if (uploadTasksExecutor == null) {
                        int uploadThreadsNums = FileUploadSetting.getUploadThreadsNums();
                        uploadTasksExecutor = createExecutor(uploadThreadsNums, uploadThreadsNums, 10, 0, new FileUploadThreadFactory(priority, "UploadTasks"));
                    }
                }
            }
            return uploadTasksExecutor.submit(runnable);
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[submitUploadTask]submit runnable to FileUpload UploadTasks ThreadPool error ---" + th.toString());
            return null;
        }
    }

    public static void setUploadTasksThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor != null) {
            uploadTasksExecutor = threadPoolExecutor;
        }
    }

    private static ThreadPoolExecutor createExecutor(int i, int i2, int i3, int i4, ThreadFactory threadFactory) {
        LinkedBlockingQueue linkedBlockingQueue;
        if (i4 > 0) {
            linkedBlockingQueue = new LinkedBlockingQueue(i4);
        } else {
            linkedBlockingQueue = new LinkedBlockingQueue();
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i2, (long) i3, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory);
        if (i3 > 0) {
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        }
        return threadPoolExecutor;
    }
}
