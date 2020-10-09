package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.EnvEnum;

public class WVPackageAppService {
    private static IPackageZipPrefixAdapter packageZipPrefixAdapter;
    private static WVPackageAppConfigInterface wvPackageApp;

    public interface IPackageZipPrefixAdapter {
        String getPackageZipPrefix(EnvEnum envEnum, boolean z);
    }

    public static WVPackageAppConfigInterface getWvPackageAppConfig() {
        return wvPackageApp;
    }

    public static void registerWvPackageAppConfig(WVPackageAppConfigInterface wVPackageAppConfigInterface) {
        wvPackageApp = wVPackageAppConfigInterface;
    }

    static void setPackageZipPrefixAdapter(IPackageZipPrefixAdapter iPackageZipPrefixAdapter) {
        packageZipPrefixAdapter = iPackageZipPrefixAdapter;
    }

    public static IPackageZipPrefixAdapter getPackageZipPrefixAdapter() {
        return packageZipPrefixAdapter;
    }
}
