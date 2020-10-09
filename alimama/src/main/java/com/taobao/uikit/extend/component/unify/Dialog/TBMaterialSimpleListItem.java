package com.taobao.uikit.extend.component.unify.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.taobao.uikit.extend.utils.ResourceUtils;

public class TBMaterialSimpleListItem {
    private final Builder mBuilder;

    private TBMaterialSimpleListItem(Builder builder) {
        this.mBuilder = builder;
    }

    public Drawable getIcon() {
        return this.mBuilder.mIcon;
    }

    public CharSequence getContent() {
        return this.mBuilder.mContent;
    }

    public int getIconPadding() {
        return this.mBuilder.mIconPadding;
    }

    @ColorInt
    public int getBackgroundColor() {
        return this.mBuilder.mBackgroundColor;
    }

    public static class Builder {
        protected int mBackgroundColor = Color.parseColor("#BCBCBC");
        protected CharSequence mContent;
        private final Context mContext;
        protected Drawable mIcon;
        protected int mIconPadding;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder icon(Drawable drawable) {
            this.mIcon = drawable;
            return this;
        }

        public Builder icon(@DrawableRes int i) {
            return icon(ContextCompat.getDrawable(this.mContext, i));
        }

        public Builder iconPadding(@IntRange(from = 0, to = 2147483647L) int i) {
            this.mIconPadding = i;
            return this;
        }

        public Builder iconPaddingDp(@IntRange(from = 0, to = 2147483647L) int i) {
            this.mIconPadding = (int) TypedValue.applyDimension(1, (float) i, this.mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder iconPaddingRes(@DimenRes int i) {
            return iconPadding(this.mContext.getResources().getDimensionPixelSize(i));
        }

        public Builder content(CharSequence charSequence) {
            this.mContent = charSequence;
            return this;
        }

        public Builder content(@StringRes int i) {
            return content((CharSequence) this.mContext.getString(i));
        }

        public Builder backgroundColor(@ColorInt int i) {
            this.mBackgroundColor = i;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int i) {
            return backgroundColor(ResourceUtils.getColor(this.mContext, i));
        }

        public Builder backgroundColorAttr(@AttrRes int i) {
            return backgroundColor(ResourceUtils.resolveColor(this.mContext, i));
        }

        public TBMaterialSimpleListItem build() {
            return new TBMaterialSimpleListItem(this);
        }
    }

    public String toString() {
        return getContent() != null ? getContent().toString() : "(no content)";
    }
}
