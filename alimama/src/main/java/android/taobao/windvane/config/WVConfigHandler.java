package android.taobao.windvane.config;

public abstract class WVConfigHandler {
    private boolean isUpdating = false;
    private String snapshotN = "0";

    public abstract void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback);

    public void setUpdateStatus(boolean z) {
        this.isUpdating = z;
    }

    public boolean getUpdateStatus() {
        return this.isUpdating;
    }

    public void setSnapshotN(String str) {
        this.snapshotN = str;
    }

    public String getSnapshotN() {
        return this.snapshotN;
    }
}
