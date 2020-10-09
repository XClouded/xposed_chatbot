package com.taobao.android.dinamic.view;

import android.annotation.SuppressLint;
import android.content.Context;
import com.taobao.android.dinamic.Dinamic;

@SuppressLint({"ViewConstructor"})
public class CompatibleView extends DFrameLayout {
    public CompatibleView(Context context, String str) {
        super(context);
        if (Dinamic.isDebugable()) {
            setContentDescription(str);
        }
    }
}
