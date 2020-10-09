package alimama.com.unwupdate;

public interface DownLoadListener {
    void onCancel();

    void onDone(boolean z, int i);

    void onPercentUpdate(int i);
}
