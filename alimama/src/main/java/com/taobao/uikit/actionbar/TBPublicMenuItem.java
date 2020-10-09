package com.taobao.uikit.actionbar;

import android.graphics.drawable.Drawable;

public class TBPublicMenuItem implements Cloneable {
    Drawable mIconDrawable = null;
    String mIconUrl = null;
    int mId = -1;
    String mMessage = "";
    MessageMode mMessageMode;
    String mNavUrl = null;
    String mTitle = null;
    String mUTControlName = null;

    public enum MessageMode {
        NONE,
        DOT_ONLY,
        DOT_WITH_NUMBER,
        TEXT;

        public static MessageMode valueOf(int i) {
            if (i == DOT_ONLY.ordinal()) {
                return DOT_ONLY;
            }
            if (i == DOT_WITH_NUMBER.ordinal()) {
                return DOT_WITH_NUMBER;
            }
            if (i == TEXT.ordinal()) {
                return TEXT;
            }
            return NONE;
        }
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public MessageMode getMessageMode() {
        return this.mMessageMode;
    }

    public void setMessageMode(MessageMode messageMode) {
        this.mMessageMode = messageMode;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void setMessage(String str) {
        this.mMessage = str;
    }

    public Drawable getIconDrawable() {
        return this.mIconDrawable;
    }

    public void setIconDrawable(Drawable drawable) {
        this.mIconDrawable = drawable;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setIconUrl(String str) {
        this.mIconUrl = str;
    }

    public String getUTControlName() {
        return this.mUTControlName;
    }

    public void setUTControlName(String str) {
        this.mUTControlName = str;
    }

    public String getNavUrl() {
        return this.mNavUrl;
    }

    public void setNavUrl(String str) {
        this.mNavUrl = str;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public boolean checkValidation() {
        if (this.mMessageMode == null || this.mMessageMode != MessageMode.DOT_WITH_NUMBER) {
            return true;
        }
        try {
            Integer.valueOf(this.mMessage);
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        if (checkValidation()) {
            return new Builder().setTitle(getTitle()).setMessageMode(getMessageMode()).setUTControlName(getUTControlName()).setNavUrl(getNavUrl()).setIcon(getIconDrawable()).setIcon(getIconUrl()).setMessage(getMessage()).setId(getId()).build();
        }
        return null;
    }

    public static class Builder {
        private TBPublicMenuItem mMenuItem = new TBPublicMenuItem();

        public int getId() {
            return this.mMenuItem.getId();
        }

        public Builder setId(int i) {
            this.mMenuItem.setId(i);
            return this;
        }

        public Builder setTitle(String str) {
            this.mMenuItem.setTitle(str);
            return this;
        }

        public Builder setNavUrl(String str) {
            this.mMenuItem.setNavUrl(str);
            return this;
        }

        public Builder setUTControlName(String str) {
            this.mMenuItem.setUTControlName(str);
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.mMenuItem.setIconDrawable(drawable);
            return this;
        }

        public Builder setIcon(String str) {
            this.mMenuItem.setIconUrl(str);
            return this;
        }

        public Builder setMessageMode(MessageMode messageMode) {
            this.mMenuItem.setMessageMode(messageMode);
            return this;
        }

        public Builder setMessage(String str) {
            this.mMenuItem.setMessage(str);
            return this;
        }

        public TBPublicMenuItem build() {
            if (!this.mMenuItem.checkValidation()) {
                return null;
            }
            return this.mMenuItem;
        }
    }
}
