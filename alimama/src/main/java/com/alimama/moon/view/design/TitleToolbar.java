package com.alimama.moon.view.design;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class TitleToolbar extends Toolbar {
    private TextView mTitleView;

    public TitleToolbar(Context context) {
        this(context, (AttributeSet) null);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initViews(@androidx.annotation.NonNull android.content.Context r5, @androidx.annotation.Nullable android.util.AttributeSet r6) {
        /*
            r4 = this;
            r0 = 2130837590(0x7f020056, float:1.7280138E38)
            android.graphics.drawable.Drawable r0 = androidx.core.content.ContextCompat.getDrawable(r5, r0)
            r4.setBackground(r0)
            android.widget.TextView r0 = new android.widget.TextView
            r0.<init>(r5)
            androidx.appcompat.widget.Toolbar$LayoutParams r1 = new androidx.appcompat.widget.Toolbar$LayoutParams
            r2 = -2
            r3 = 17
            r1.<init>(r2, r2, r3)
            android.content.res.Resources r2 = r5.getResources()
            r3 = 2131362103(0x7f0a0137, float:1.8343977E38)
            int r2 = r2.getDimensionPixelOffset(r3)
            float r2 = (float) r2
            r3 = 0
            r0.setTextSize(r3, r2)
            r2 = 2131624239(0x7f0e012f, float:1.8875652E38)
            int r2 = androidx.core.content.ContextCompat.getColor(r5, r2)
            r0.setTextColor(r2)
            r4.addView(r0, r1)
            r4.mTitleView = r0
            if (r6 == 0) goto L_0x005a
            r0 = 0
            int[] r1 = com.alimama.moon.R.styleable.TitleToolbar     // Catch:{ Exception -> 0x0054 }
            android.content.res.TypedArray r5 = r5.obtainStyledAttributes(r6, r1)     // Catch:{ Exception -> 0x0054 }
            r6 = -1
            int r6 = r5.getResourceId(r3, r6)     // Catch:{ Exception -> 0x0052 }
            if (r6 <= 0) goto L_0x005a
            android.content.res.Resources r0 = r4.getResources()     // Catch:{ Exception -> 0x0052 }
            java.lang.String r6 = r0.getString(r6)     // Catch:{ Exception -> 0x0052 }
            r4.setTitle(r6)     // Catch:{ Exception -> 0x0052 }
            goto L_0x005a
        L_0x0052:
            goto L_0x0055
        L_0x0054:
            r5 = r0
        L_0x0055:
            if (r5 == 0) goto L_0x005a
            r5.recycle()
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.view.design.TitleToolbar.initViews(android.content.Context, android.util.AttributeSet):void");
    }

    public void setTitle(String str) {
        this.mTitleView.setText(str);
    }
}
