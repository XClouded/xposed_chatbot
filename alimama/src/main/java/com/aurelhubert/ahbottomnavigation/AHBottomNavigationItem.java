package com.aurelhubert.ahbottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

public class AHBottomNavigationItem {
    private int color = -7829368;
    @ColorRes
    private int colorRes = 0;
    private Drawable drawable;
    @DrawableRes
    private int drawableRes = 0;
    private String title = "";
    @StringRes
    private int titleRes = 0;

    public AHBottomNavigationItem(String str, @DrawableRes int i) {
        this.title = str;
        this.drawableRes = i;
    }

    @Deprecated
    public AHBottomNavigationItem(String str, @DrawableRes int i, @ColorRes int i2) {
        this.title = str;
        this.drawableRes = i;
        this.color = i2;
    }

    public AHBottomNavigationItem(@StringRes int i, @DrawableRes int i2, @ColorRes int i3) {
        this.titleRes = i;
        this.drawableRes = i2;
        this.colorRes = i3;
    }

    public AHBottomNavigationItem(String str, Drawable drawable2) {
        this.title = str;
        this.drawable = drawable2;
    }

    public AHBottomNavigationItem(String str, Drawable drawable2, @ColorInt int i) {
        this.title = str;
        this.drawable = drawable2;
        this.color = i;
    }

    public String getTitle(Context context) {
        if (this.titleRes != 0) {
            return context.getString(this.titleRes);
        }
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
        this.titleRes = 0;
    }

    public void setTitle(@StringRes int i) {
        this.titleRes = i;
        this.title = "";
    }

    public int getColor(Context context) {
        if (this.colorRes != 0) {
            return ContextCompat.getColor(context, this.colorRes);
        }
        return this.color;
    }

    public void setColor(@ColorInt int i) {
        this.color = i;
        this.colorRes = 0;
    }

    public void setColorRes(@ColorRes int i) {
        this.colorRes = i;
        this.color = 0;
    }

    public Drawable getDrawable(Context context) {
        if (this.drawableRes == 0) {
            return this.drawable;
        }
        try {
            return VectorDrawableCompat.create(context.getResources(), this.drawableRes, (Resources.Theme) null);
        } catch (Resources.NotFoundException unused) {
            return ContextCompat.getDrawable(context, this.drawableRes);
        }
    }

    public void setDrawable(@DrawableRes int i) {
        this.drawableRes = i;
        this.drawable = null;
    }

    public void setDrawable(Drawable drawable2) {
        this.drawable = drawable2;
        this.drawableRes = 0;
    }
}
