package com.alimama.union.app.infrastructure.socialShare;

import android.app.Activity;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.socialShare.Share;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WVSharePlugin extends WVApiPlugin {
    private Logger logger = LoggerFactory.getLogger((Class<?>) WVSharePlugin.class);
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;

    public WVSharePlugin() {
        App.getAppComponent().inject(this);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        this.logger.info("execute, action: {}, params: {}", (Object) str2);
        if (((str.hashCode() == 1813738004 && str.equals("doShare")) ? (char) 0 : 65535) != 0) {
            return false;
        }
        doShare(str2, wVCallBackContext);
        return true;
    }

    private void doShare(String str, WVCallBackContext wVCallBackContext) {
        Activity activity = (Activity) wVCallBackContext.getWebview().getContext();
        ShareObj shareObj = (ShareObj) JSON.parseObject(str, ShareObj.class);
        if (!checkAppInstalled(activity, shareObj.getPlatform())) {
            appNotInstalled(wVCallBackContext);
            return;
        }
        switch (this.permission.checkPermission(activity)) {
            case -1:
                if (this.permission.shouldShowPermissionRationale(activity)) {
                    noPermission(wVCallBackContext);
                    return;
                } else {
                    this.permission.requestPermission(activity);
                    return;
                }
            case 0:
                share(activity, shareObj, wVCallBackContext);
                return;
            default:
                return;
        }
    }

    private void noPermission(WVCallBackContext wVCallBackContext) {
        wVCallBackContext.error(new WVResult(WVImagePlugin.HY_NO_PERMISSION_WRITE_EXTERNAL_STORAGE));
    }

    private void share(Context context, ShareObj shareObj, final WVCallBackContext wVCallBackContext) {
        new ShareImpl().doShare(context, shareObj, new Share.Callback() {
            public void onSuccess() {
                wVCallBackContext.success();
            }

            public void onFailure() {
                wVCallBackContext.error();
            }
        });
    }

    private void appNotInstalled(WVCallBackContext wVCallBackContext) {
        wVCallBackContext.error(new WVResult("HY_APP_NOT_INSTALLED"));
    }

    private boolean checkAppInstalled(Context context, String str) {
        ShareUtils.InstallApp isInstallAppWithPlatform = ShareUtils.isInstallAppWithPlatform(context, str);
        if (!isInstallAppWithPlatform.isInstalled()) {
            ShareUtils.showDialog(context, isInstallAppWithPlatform.getErrorMsg());
        }
        return isInstallAppWithPlatform.isInstalled();
    }
}
