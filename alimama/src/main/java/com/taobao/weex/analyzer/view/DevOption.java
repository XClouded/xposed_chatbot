package com.taobao.weex.analyzer.view;

import androidx.annotation.DrawableRes;

public class DevOption {
    @DrawableRes
    public int iconRes;
    public boolean isOverlayView;
    public boolean isPermissionGranted;
    public OnOptionClickListener listener;
    public String optionName;
    public boolean showLabel;

    public interface OnOptionClickListener {
        void onOptionClick();
    }

    public DevOption() {
        this.isPermissionGranted = true;
        this.showLabel = false;
    }

    public DevOption(String str, int i, OnOptionClickListener onOptionClickListener) {
        this(str, i, onOptionClickListener, false);
    }

    public DevOption(String str, int i, OnOptionClickListener onOptionClickListener, boolean z) {
        this(str, i, onOptionClickListener, z, true);
    }

    public DevOption(String str, int i, OnOptionClickListener onOptionClickListener, boolean z, boolean z2) {
        this.isPermissionGranted = true;
        this.showLabel = false;
        this.optionName = str;
        this.iconRes = i;
        this.listener = onOptionClickListener;
        this.isOverlayView = z;
        this.isPermissionGranted = z2;
    }

    public DevOption(String str, int i, OnOptionClickListener onOptionClickListener, boolean z, boolean z2, boolean z3) {
        this.isPermissionGranted = true;
        this.showLabel = false;
        this.optionName = str;
        this.iconRes = i;
        this.listener = onOptionClickListener;
        this.isOverlayView = z;
        this.isPermissionGranted = z2;
        this.showLabel = z3;
    }
}
