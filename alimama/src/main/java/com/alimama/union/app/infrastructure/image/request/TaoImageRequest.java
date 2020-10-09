package com.alimama.union.app.infrastructure.image.request;

import android.widget.ImageView;
import com.alimama.moon.R;

public abstract class TaoImageRequest {
    protected int errorResId;
    protected boolean noFade = false;
    protected String path;
    protected int placeholderResId;
    protected int resId;

    public abstract void into(ImageView imageView);

    protected TaoImageRequest(String str, int i) {
        this.path = str;
        this.resId = i;
        this.placeholderResId = R.drawable.img_loading_bg;
        this.errorResId = R.drawable.img_loading_bg;
    }

    public TaoImageRequest placeholder(int i) {
        if (i != 0) {
            this.placeholderResId = i;
            return this;
        }
        throw new IllegalArgumentException("Placeholder image resource invalid.");
    }

    public TaoImageRequest error(int i) {
        if (i != 0) {
            this.errorResId = i;
            return this;
        }
        throw new IllegalArgumentException("Error image resource invalid.");
    }

    public TaoImageRequest noFade() {
        this.noFade = true;
        return this;
    }
}
