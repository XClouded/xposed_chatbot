package com.alimama.union.app.infrastructure.image.download;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.alimama.moon.App;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.capture.Event;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Named;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageDownloaderModule extends WXModule implements ISubscriber {
    private SoftReference<JSCallback> cacheCallback;
    private SoftReference<String> cacheImageUrl;
    @Inject
    IEventBus eventBus;
    private Logger logger = LoggerFactory.getLogger((Class<?>) ImageDownloaderModule.class);
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;

    public ImageDownloaderModule() {
        this.logger.info("constructor");
        App.getAppComponent().inject(this);
        this.eventBus.register(this);
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        this.logger.info("onActivityDestroy");
        this.eventBus.unregister(this);
    }

    @JSMethod
    public void download(String str, JSCallback jSCallback) {
        BaseActivity baseActivity = (BaseActivity) this.mWXSDKInstance.getContext();
        switch (this.permission.checkPermission(baseActivity)) {
            case -1:
                if (this.permission.shouldShowPermissionRationale(baseActivity)) {
                    this.logger.info("PERMISSION_DENIED, user rejected");
                    noPermission(jSCallback);
                    return;
                }
                this.logger.info("PERMISSION_DENIED, request permission");
                this.cacheImageUrl = new SoftReference<>(str);
                this.cacheCallback = new SoftReference<>(jSCallback);
                this.permission.requestPermission(baseActivity);
                return;
            case 0:
                this.logger.info("PERMISSION_GRANTED");
                doDownload(str, jSCallback);
                return;
            default:
                return;
        }
    }

    private void doDownload(String str, JSCallback jSCallback) {
        Uri parse = Uri.parse(str);
        if (TextUtils.isEmpty(parse.getScheme())) {
            parse = parse.buildUpon().scheme("https").build();
        }
        new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance()).download(parse.toString(), new IImageDownloader.ImageDownloadCallback() {
            public void onSuccess(Uri uri) {
                Context context = ImageDownloaderModule.this.mWXSDKInstance.getContext();
                ToastUtil.toast(context, (CharSequence) "保存成功: " + ExternalPublicStorageSaver.getInstance().getBaseFileDir());
            }

            public void onFailure(String str) {
                ToastUtil.toast(ImageDownloaderModule.this.mWXSDKInstance.getContext(), (CharSequence) "保存失败");
            }
        });
    }

    @Subscribe
    public void onPermissionGranted(Event.WriteExternalStoragePermissionGranted writeExternalStoragePermissionGranted) {
        if (this.cacheImageUrl != null && this.cacheImageUrl.get() != null && this.cacheCallback != null && this.cacheCallback.get() != null) {
            doDownload(this.cacheImageUrl.get(), this.cacheCallback.get());
        }
    }

    @Subscribe
    public void onPermissionDenied(Event.WriteExternalStoragePermissionDenied writeExternalStoragePermissionDenied) {
        if (this.cacheCallback != null && this.cacheCallback.get() != null) {
            noPermission(this.cacheCallback.get());
        }
    }

    private void noPermission(JSCallback jSCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("ret", WVImagePlugin.HY_NO_PERMISSION_WRITE_EXTERNAL_STORAGE);
        jSCallback.invoke(hashMap);
    }
}
