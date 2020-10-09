package com.alimamaunion.support.debugmode;

import android.view.View;

public class DebugItemData {
    public View.OnClickListener listener;
    public String title;

    public DebugItemData(String str, View.OnClickListener onClickListener) {
        this.title = str;
        this.listener = onClickListener;
    }
}
