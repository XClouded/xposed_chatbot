package com.alimama.union.app.infrastructure.weex;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

public class ShareImageModule extends WXModule implements PermissionInterface {
    Context context;
    JSCallback jsCallback;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;
    private PrivacyPermissionManager privacyPermissionManager;
    ShareObj shareObj;

    public ShareImageModule() {
        App.getAppComponent().inject(this);
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
    }

    public void closePermissionRequest() {
        if (this.jsCallback != null) {
            this.jsCallback.invoke("success");
        }
    }

    public void openPermissionRequest() {
        try {
            switch (this.permission.checkPermission((Activity) this.context)) {
                case -1:
                    this.permission.requestPermission((Activity) this.context);
                    Toast.makeText(this.mWXSDKInstance.getContext(), this.mWXSDKInstance.getContext().getString(R.string.permission_request_sdcard), 0).show();
                    this.jsCallback.invoke("success");
                    return;
                case 0:
                    downloadImage(this.context, this.shareObj, this.jsCallback);
                    return;
                default:
                    return;
            }
        } catch (Exception unused) {
        }
    }

    @JSMethod
    public void shareImage(String str, JSCallback jSCallback) {
        try {
            this.context = this.mWXSDKInstance.getContext();
            this.shareObj = (ShareObj) JSON.parseObject(str, ShareObj.class);
            this.jsCallback = jSCallback;
            this.privacyPermissionManager = new PrivacyPermissionManager(this.context, (PermissionInterface) this);
            if (!PrivacyUtil.hasWriteExternalStorage(this.context)) {
                this.privacyPermissionManager.showReadWritePermissionDialog();
            } else {
                openPermissionRequest();
            }
        } catch (Exception unused) {
        }
    }

    private void downloadImage(final Context context2, final ShareObj shareObj2, final JSCallback jSCallback) {
        new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance()).batchDownload(shareObj2.getImages(), new IImageDownloader.BatchImageDownloadCallback() {
            public void onProgressUpdate(List<Uri> list, List<String> list2) {
            }

            public void onSuccess(List<Uri> list) {
                jSCallback.invoke("success");
                ShareImageModule.this.processImageDownloadSuccess(context2, shareObj2, list);
            }

            public void onFailure(String str) {
                jSCallback.invoke("failure");
            }
        });
    }

    /* access modifiers changed from: private */
    public void processImageDownloadSuccess(Context context2, ShareObj shareObj2, List<Uri> list) {
        showSharePanel(context2, shareObj2.getText(), ShareUtils.processShareImages(context2, shareObj2, list));
    }

    private void showSharePanel(Context context2, String str, ArrayList<Uri> arrayList) {
        try {
            new SharePanelDialog(context2, str, arrayList).show();
        } catch (Exception unused) {
        }
    }

    private void noPermission(JSCallback jSCallback) {
        jSCallback.invoke("failure");
    }
}
