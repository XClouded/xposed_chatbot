package com.taobao.uikit.immersive;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TBFragmentFrameLayout extends FrameLayout {
    public TBFragmentFrameLayout(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public TBFragmentFrameLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TBFragmentFrameLayout(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewRemoved(View view, View view2) {
            }

            public void onChildViewAdded(View view, View view2) {
                if (Build.VERSION.SDK_INT >= 20) {
                    TBFragmentFrameLayout.this.requestApplyInsets();
                } else if (Build.VERSION.SDK_INT >= 19) {
                    TBFragmentFrameLayout.this.requestFitSystemWindows();
                }
            }
        });
    }
}
