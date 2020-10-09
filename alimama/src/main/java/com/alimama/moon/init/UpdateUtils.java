package com.alimama.moon.init;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IUpdateManager;
import alimama.com.unwupdate.CancelUpdateListener;
import alimama.com.unwupdate.UpdateIntercept;
import alimama.com.unwupdate.UpdateParams;
import android.app.Activity;
import android.widget.Toast;
import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import java.lang.ref.WeakReference;

public class UpdateUtils {
    public static void checkUpdate(final Activity activity, boolean z) {
        IUpdateManager iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class);
        if (iUpdateManager != null && activity != null && !activity.isFinishing()) {
            UpdateParams updateParams = new UpdateParams();
            updateParams.activityRef = new WeakReference<>(activity);
            updateParams.appName = "淘宝联盟";
            updateParams.groupValue = "alimamamoon";
            updateParams.fileProviderAuthority = InitConstants.FILE_AUTHORITY;
            updateParams.downloadChannelId = "moonDownloadApk";
            updateParams.downloadChannelName = "淘宝联盟下载安装包";
            updateParams.downloadChannelDesc = "通知展示淘宝联盟安装包的下载进度";
            updateParams.isForceUpdate = OrangeConfigCenterManager.getInstance().isAppForceUpdate();
            updateParams.cancelUpdateListener = new CancelUpdateListener() {
                public void cancelUpdate() {
                    MoonComponentManager.getInstance().getPageRouter().finishAll();
                }
            };
            if (z) {
                updateParams.updateIntercept = new UpdateIntercept() {
                    public void cancelUpdateIntercept() {
                    }

                    public void updateIntercept() {
                    }

                    public void noUpdateIntercept() {
                        Toast.makeText(activity, "已经是最新版本了", 0).show();
                    }
                };
            }
            iUpdateManager.checkUpdate(true, updateParams);
        }
    }
}
