package alimama.com.unwupdate;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SimpleTask implements Runnable {
    private static final int MSG_TASK_DONE = 1;
    private static final int STATE_CANCELLED = 8;
    private static final int STATE_FINISH = 4;
    private static final int STATE_NEW = 1;
    private static final int STATE_RUNNING = 2;
    private static InternalHandler sHandler = new InternalHandler(Looper.getMainLooper());
    private Thread mCurrentThread;
    /* access modifiers changed from: private */
    public AtomicInteger mState = new AtomicInteger(1);

    public abstract void doInBackground();

    /* access modifiers changed from: protected */
    public void onCancel() {
    }

    public abstract void onFinish(boolean z);

    public void restart() {
        this.mState.set(1);
    }

    public void run() {
        if (this.mState.compareAndSet(1, 2)) {
            this.mCurrentThread = Thread.currentThread();
            doInBackground();
            sHandler.obtainMessage(1, this).sendToTarget();
        }
    }

    public boolean isCancelled() {
        return this.mState.get() == 8;
    }

    public boolean isDone() {
        return this.mState.get() == 4;
    }

    public void cancel() {
        if (this.mState.get() < 4) {
            if (this.mState.get() == 2 && this.mCurrentThread != null) {
                try {
                    this.mCurrentThread.interrupt();
                } catch (Exception unused) {
                }
            }
            this.mState.set(8);
            onCancel();
        }
    }

    private static class InternalHandler extends Handler {
        InternalHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            SimpleTask simpleTask = (SimpleTask) message.obj;
            if (message.what == 1) {
                boolean isCancelled = simpleTask.isCancelled();
                simpleTask.mState.set(4);
                simpleTask.onFinish(isCancelled);
            }
        }
    }

    public static void post(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long j) {
        sHandler.postDelayed(runnable, j);
    }
}
