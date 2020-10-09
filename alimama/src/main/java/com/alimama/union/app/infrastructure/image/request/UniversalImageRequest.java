package com.alimama.union.app.infrastructure.image.request;

import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class UniversalImageRequest extends TaoImageRequest {
    private static final int DELAY_BEFORE_LOADING_IN_MILLIS = 50;
    private static final int FADE_ANIMATION_DURATION_MILLIS = 400;
    private ImageLoader imageLoader;

    public UniversalImageRequest(ImageLoader imageLoader2, String str, int i) {
        super(str, i);
        this.imageLoader = imageLoader2;
    }

    public void into(ImageView imageView) {
        DisplayImageOptions build = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).showImageForEmptyUri(this.placeholderResId).showImageOnLoading(this.placeholderResId).showImageOnFail(this.errorResId).displayer(new FadeInBitmapDisplayer(400, !this.noFade, true ^ this.noFade, false)).build();
        if (this.resId == 0) {
            this.imageLoader.displayImage(this.path, imageView, build);
        } else {
            imageView.setImageResource(this.resId);
        }
    }
}
