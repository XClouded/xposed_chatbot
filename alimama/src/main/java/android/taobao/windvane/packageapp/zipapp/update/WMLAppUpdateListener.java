package android.taobao.windvane.packageapp.zipapp.update;

public interface WMLAppUpdateListener extends WVPackageUpdateListener {
    void onPackageUpdateError();

    void onPackageUpdateProgress();
}
