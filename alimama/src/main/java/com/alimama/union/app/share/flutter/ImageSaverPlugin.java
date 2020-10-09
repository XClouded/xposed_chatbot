package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.lang.ref.WeakReference;

public class ImageSaverPlugin implements MethodChannel.MethodCallHandler, PermissionInterface {
    private final Activity mActivity;
    private final UniversalImageDownloader mImageLoader = new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance());
    private final StoragePermissionValidator mPermissionValidator;
    private PrivacyPermissionManager privacyPermissionManager;

    public void closePermissionRequest() {
    }

    public static void register(Activity activity, BinaryMessenger binaryMessenger) {
        new MethodChannel(binaryMessenger, "com.alimama.moon/saveImgs").setMethodCallHandler(new ImageSaverPlugin(activity));
    }

    public ImageSaverPlugin(Activity activity) {
        this.mActivity = activity;
        this.mPermissionValidator = new StoragePermissionValidator();
        this.privacyPermissionManager = new PrivacyPermissionManager((Context) activity, (PermissionInterface) this);
    }

    public void openPermissionRequest() {
        this.mPermissionValidator.checkRequiredPermission(this.mActivity);
    }

    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        if ("saveImage".equals(methodCall.method)) {
            String str = (String) methodCall.arguments;
            if (!PrivacyUtil.hasWriteExternalStorage(this.mActivity)) {
                this.privacyPermissionManager.showReadWritePermissionDialog();
                return;
            }
            ToastUtil.showToast((Context) this.mActivity, (int) R.string.save_image_in_progress);
            this.mImageLoader.download(str, new ImageDownloader(this.mActivity));
        }
    }

    private static class ImageDownloader implements IImageDownloader.ImageDownloadCallback {
        private final WeakReference<Activity> mOwnerActivity;

        private ImageDownloader(Activity activity) {
            this.mOwnerActivity = new WeakReference<>(activity);
        }

        public void onSuccess(@Nullable Uri uri) {
            Activity activity = (Activity) this.mOwnerActivity.get();
            if (activity != null) {
                if (uri == null) {
                    ToastUtil.showToast((Context) activity, (int) R.string.images_save_failed);
                } else {
                    ToastUtil.showToast((Context) activity, (int) R.string.image_saved_to_album);
                }
            }
        }

        public void onFailure(String str) {
            if (this.mOwnerActivity.get() != null) {
                ToastUtil.showToast((Context) this.mOwnerActivity.get(), (int) R.string.images_save_failed);
            }
        }
    }
}
