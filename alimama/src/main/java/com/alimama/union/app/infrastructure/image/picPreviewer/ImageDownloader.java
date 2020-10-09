package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import java.lang.ref.WeakReference;

public class ImageDownloader implements IImageDownloader.ImageDownloadCallback {
    private final WeakReference<Activity> mOwnerActivity;

    public ImageDownloader(Activity activity) {
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
