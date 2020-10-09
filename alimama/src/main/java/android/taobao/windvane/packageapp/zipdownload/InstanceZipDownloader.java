package android.taobao.windvane.packageapp.zipdownload;

public class InstanceZipDownloader extends Thread implements IDownLoader {
    private static final String TAG = "InstanceZipDownloader";
    private DownLoadManager downLoadManager = null;

    public void cancelTask(boolean z) {
    }

    public InstanceZipDownloader(String str, DownLoadListener downLoadListener, int i, Object obj) {
        this.downLoadManager = new DownLoadManager(str, downLoadListener, i, obj, true);
        this.downLoadManager.isTBDownloaderEnabled = false;
    }

    public void run() {
        if (this.downLoadManager != null) {
            this.downLoadManager.doTask();
        }
    }

    public Thread.State getDownLoaderStatus() {
        return Thread.currentThread().getState();
    }
}
