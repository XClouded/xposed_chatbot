package android.taobao.windvane.packageapp.zipapp;

import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.TaoLog;

import com.taobao.ju.track.constants.Constants;

import java.util.ArrayList;

public class ConfigManager {
    private static String Tag = "PackageApp-ConfigManager";
    private static ZipGlobalConfig locGobalConfig;

    public static ZipGlobalConfig getLocGlobalConfig() {
        if (WVPackageAppService.getWvPackageAppConfig() == null) {
            WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
        }
        return WVPackageAppService.getWvPackageAppConfig().getGlobalConfig();
    }

    public static void updateZcacheurlMap(String str, ArrayList<String> arrayList) {
        getLocGlobalConfig().addZcacheResConfig(str, arrayList);
    }

    public static boolean updateGlobalConfig(ZipAppInfo zipAppInfo, String str, boolean z) {
        ZipAppInfo appInfo;
        if (zipAppInfo == null && str == null) {
            try {
                TaoLog.w(Tag, "UpdateGlobalConfig:param is null");
                return false;
            } catch (Exception e) {
                String str2 = Tag;
                TaoLog.e(str2, "updateGlobalConfig:exception  " + e.getMessage());
                return false;
            }
        } else {
            if (!z) {
                getLocGlobalConfig().putAppInfo2Table(zipAppInfo.name, zipAppInfo);
            } else if (zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                getLocGlobalConfig().removeAppInfoFromTable(zipAppInfo.name);
            } else if (zipAppInfo.status == ZipAppConstants.ZIP_REMOVED && (appInfo = getLocGlobalConfig().getAppInfo(zipAppInfo.name)) != null) {
                appInfo.installedSeq = 0;
                appInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
            }
            if (!saveGlobalConfigToloc(getLocGlobalConfig())) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(Tag, "UpdateGlobalConfig:save to localfile fail  ");
                }
                return false;
            } else if (ZipAppUtils.savaZcacheMapToLoc(getLocGlobalConfig().getZcacheResConfig())) {
                return true;
            } else {
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(Tag, "UpdateZcacheConfig:save to localfile fail  ");
                }
                return false;
            }
        }
    }

    public static boolean saveGlobalConfigToloc(ZipGlobalConfig zipGlobalConfig) {
        if (WVPackageAppService.getWvPackageAppConfig() != null) {
            return WVPackageAppService.getWvPackageAppConfig().saveLocalConfig(zipGlobalConfig);
        }
        return false;
    }

    public static void updateGlobalConfigAppStatus(ZipAppInfo zipAppInfo, int i) {
        ZipAppInfo appInfo = getLocGlobalConfig().getAppInfo(zipAppInfo.name);
        if (appInfo != null) {
            appInfo.status = i;
        }
        updateGlobalConfig(zipAppInfo, (String) null, false);
    }
}
