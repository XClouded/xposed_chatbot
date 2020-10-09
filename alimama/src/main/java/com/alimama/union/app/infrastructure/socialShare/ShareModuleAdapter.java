package com.alimama.union.app.infrastructure.socialShare;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.alibaba.aliweex.adapter.IShareModuleAdapter;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.socialShare.Share;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.taobao.weex.bridge.JSCallback;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareModuleAdapter implements IShareModuleAdapter {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ShareModuleAdapter.class);
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;

    public ShareModuleAdapter() {
        App.getAppComponent().inject(this);
    }

    public void doShare(Context context, String str, JSCallback jSCallback) {
        logger.info("doShare: {}", (Object) str);
        try {
            ShareObj shareObj = (ShareObj) JSON.parseObject(str, ShareObj.class);
            if (!checkAppInstalled(context, shareObj.getPlatform())) {
                appNotInstalled(jSCallback);
                return;
            }
            switch (this.permission.checkPermission((Activity) context)) {
                case -1:
                    if (this.permission.shouldShowPermissionRationale((Activity) context)) {
                        noPermission(jSCallback);
                        return;
                    }
                    this.permission.requestPermission((Activity) context);
                    jSCallback.invoke("success");
                    return;
                case 0:
                    share(context, shareObj, jSCallback);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void appNotInstalled(JSCallback jSCallback) {
        jSCallback.invoke("failure");
    }

    private void noPermission(JSCallback jSCallback) {
        jSCallback.invoke("failure");
    }

    private void share(Context context, ShareObj shareObj, final JSCallback jSCallback) {
        new ShareImpl().doShare(context, shareObj, new Share.Callback() {
            public void onSuccess() {
                jSCallback.invoke("success");
            }

            public void onFailure() {
                jSCallback.invoke("failure");
            }
        });
    }

    private boolean checkAppInstalled(Context context, String str) {
        ShareUtils.InstallApp isInstallAppWithPlatform = ShareUtils.isInstallAppWithPlatform(context, str);
        if (!isInstallAppWithPlatform.isInstalled()) {
            Toast.makeText(context, isInstallAppWithPlatform.getErrorMsg(), 0).show();
        }
        return isInstallAppWithPlatform.isInstalled();
    }
}
