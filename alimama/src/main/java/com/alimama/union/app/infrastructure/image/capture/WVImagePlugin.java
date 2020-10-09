package com.alimama.union.app.infrastructure.image.capture;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.web.WebActivity;
import com.alimama.union.app.infrastructure.image.capture.Capture;
import com.alimama.union.app.infrastructure.image.capture.Event;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import java.io.File;
import java.lang.ref.SoftReference;
import javax.inject.Inject;
import javax.inject.Named;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WVImagePlugin extends WVApiPlugin implements ISubscriber, PermissionInterface {
    public static final String HY_NO_PERMISSION_WRITE_EXTERNAL_STORAGE = "HY_NO_PERMISSION_WRITE_EXTERNAL_STORAGE";
    public static final String HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE = "HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE";
    private SoftReference<String> cacheParams;
    private SoftReference<WVCallBackContext> cacheWVCallBackContext;
    WVCallBackContext callBackContext;
    @Inject
    Capture capture;
    WebActivity context;
    @Inject
    IEventBus eventBus;
    private Logger logger = LoggerFactory.getLogger((Class<?>) WVImagePlugin.class);
    String params;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;
    private PrivacyPermissionManager privacyPermissionManager;

    public WVImagePlugin() {
        App.getAppComponent().inject(this);
        this.eventBus.register(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.eventBus.unregister(this);
    }

    public void openPermissionRequest() {
        CaptureHtml captureHtml = (CaptureHtml) JSON.parseObject(this.params, CaptureHtml.class);
        switch (this.permission.checkPermission(this.context)) {
            case -1:
                this.logger.info("HY_NO_PERMISSION_WRITE_EXTERNAL_STORAGE, request permission");
                this.cacheParams = new SoftReference<>(this.params);
                this.cacheWVCallBackContext = new SoftReference<>(this.callBackContext);
                this.permission.requestPermission(this.context);
                return;
            case 0:
                this.logger.info("HY_SUCCESS, has permission");
                this.capture.captureHtml(this.context, captureHtml.getHtml(), captureHtml.getWidth(), captureHtml.getHeight(), new Capture.CaptureCallback() {
                    public void onSuccess(File file) {
                        WVImagePlugin.this.callBackContext.success();
                    }
                });
                return;
            default:
                return;
        }
    }

    public void closePermissionRequest() {
        this.cacheWVCallBackContext = new SoftReference<>(this.callBackContext);
        if (this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            this.logger.info(HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE);
            this.cacheWVCallBackContext.get().error(new WVResult(HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE));
        }
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        this.logger.info("execute, action: {}, params: {}, WVCallBackContext: {}", str, str2, wVCallBackContext.getToken());
        if (((str.hashCode() == 1872800461 && str.equals("savePic")) ? (char) 0 : 65535) != 0) {
            return false;
        }
        savePic(str2, wVCallBackContext);
        return true;
    }

    private void savePic(String str, WVCallBackContext wVCallBackContext) {
        WebActivity webActivity = (WebActivity) wVCallBackContext.getWebview().getContext();
        this.privacyPermissionManager = new PrivacyPermissionManager((Context) webActivity, (PermissionInterface) this);
        this.context = webActivity;
        this.params = str;
        this.callBackContext = wVCallBackContext;
        if (!PrivacyUtil.hasWriteExternalStorage(webActivity)) {
            this.privacyPermissionManager.showReadWritePermissionDialog();
        } else {
            openPermissionRequest();
        }
    }

    @Subscribe
    public void onPermissionGranted(Event.WriteExternalStoragePermissionGranted writeExternalStoragePermissionGranted) {
        if (this.cacheParams != null && this.cacheParams.get() != null && this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            this.logger.info("user grant permission, continue savePic process");
            savePic(this.cacheParams.get(), this.cacheWVCallBackContext.get());
        }
    }

    @Subscribe
    public void onPermissionDenied(Event.WriteExternalStoragePermissionDenied writeExternalStoragePermissionDenied) {
        if (this.cacheWVCallBackContext != null && this.cacheWVCallBackContext.get() != null) {
            this.logger.info(HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE);
            this.cacheWVCallBackContext.get().error(new WVResult(HY_REJECT_PERMISSION_WRITE_EXTERNAL_STORAGE));
        }
    }
}
