package com.alimama.share.beans;

import android.net.Uri;
import android.text.TextUtils;

public class ImageBean {
    public byte[] data = null;
    public String path = "";
    public byte[] thumb = null;
    public Uri uri = null;

    public boolean isPath() {
        return !TextUtils.isEmpty(this.path);
    }

    public boolean isHasThumb() {
        return this.thumb != null;
    }

    public boolean isHasData() {
        return this.data != null;
    }

    public boolean isUri() {
        return this.uri != null;
    }
}
