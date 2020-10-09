package android.taobao.windvane.packageapp.zipdownload;

public interface IDownLoader {
    void cancelTask(boolean z);

    Thread.State getDownLoaderStatus();
}
