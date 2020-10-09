package com.taobao.weex.adapter;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

public interface IDrawableLoader {

    public interface AnimatedTarget extends DrawableTarget {
        void setAnimatedDrawable(@Nullable Drawable drawable);
    }

    public interface DrawableTarget {
        void setDrawable(@Nullable Drawable drawable, boolean z);
    }

    public interface StaticTarget extends DrawableTarget {
        void setDrawable(@Nullable Drawable drawable, boolean z);
    }

    void setDrawable(String str, DrawableTarget drawableTarget, DrawableStrategy drawableStrategy);
}
