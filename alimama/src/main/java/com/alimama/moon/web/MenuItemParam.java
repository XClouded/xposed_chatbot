package com.alimama.moon.web;

import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;

public class MenuItemParam {
    @Nullable
    private transient Bitmap icon;
    @Nullable
    private String iconTitle;
    private String iconUrl;
    @JSONField(name = "_index")
    private String index;

    @Nullable
    public String getIconTitle() {
        return this.iconTitle;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconTitle(@Nullable String str) {
        this.iconTitle = str;
    }

    public void setIconUrl(String str) {
        this.iconUrl = str;
    }

    public void setIcon(Bitmap bitmap) {
        this.icon = bitmap;
    }

    public void setIndex(String str) {
        this.index = str;
    }

    public String getIndex() {
        return this.index;
    }

    public Bitmap getIcon() {
        return this.icon;
    }
}
