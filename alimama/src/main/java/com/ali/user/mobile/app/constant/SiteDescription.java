package com.ali.user.mobile.app.constant;

import androidx.annotation.DrawableRes;

public class SiteDescription {
    public String desc;
    @DrawableRes
    public int iconRes;
    public int site;

    public SiteDescription(int i, String str, @DrawableRes int i2) {
        this.site = i;
        this.desc = str;
        this.iconRes = i2;
    }
}
