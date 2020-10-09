package android.taobao.windvane.packageapp.zipdownload;

import android.os.Handler;
import android.os.HandlerThread;
import android.taobao.windvane.util.TaoLog;

import com.taobao.weex.el.parse.Operators;

public class WVZipBPDownloader implements IDownLoader, Runnable {
    private static final String TAG = "WVZipBPDownloader";
    private DownLoadManager downLoadManager = null;
    private Handler handler;
    private HandlerThread handlerThread;

    public WVZipBPDownloader(String str, DownLoadListener downLoadListener, int i, Object obj) {
        this.downLoadManager = new DownLoadManager(str, downLoadListener, i, obj, false);
        this.downLoadManager.isTBDownloaderEnabled = true;
        this.handlerThread = new HandlerThread("Download");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void update(String str, int i, Object obj) {
        if (this.downLoadManager != null) {
            this.downLoadManager.updateParam(str, i, obj, false);
            return;
        }
        throw new NullPointerException("downloadManager is null");
    }

    public void run() {
        TaoLog.i("WVThread", "current thread = [" + Thread.currentThread().getName() + Operators.ARRAY_END_STR);
        if (this.downLoadManager != null) {
            this.downLoadManager.doTask();
        }
    }

    public Thread.State getDownLoaderStatus() {
        return Thread.currentThread().getState();
    }

    public void cancelTask(boolean z) {
        if (!Thread.currentThread().isInterrupted()) {
            Thread.currentThread().interrupt();
        }
    }
}
