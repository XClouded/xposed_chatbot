package com.taobao.android.dxcontainer.vlayout.layout;

import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;

public class ScrollFixLayoutHelper extends FixLayoutHelper {
    public static final int SHOW_ALWAYS = 0;
    public static final int SHOW_ON_ENTER = 1;
    public static final int SHOW_ON_LEAVE = 2;
    private static final String TAG = "ScrollFixLayoutHelper";
    private int mShowType;

    public ScrollFixLayoutHelper(int i, int i2) {
        this(0, i, i2);
    }

    public ScrollFixLayoutHelper(int i, int i2, int i3) {
        super(i, i2, i3);
        this.mShowType = 0;
    }

    public void setShowType(int i) {
        this.mShowType = i;
    }

    public int getShowType() {
        return this.mShowType;
    }

    /* access modifiers changed from: protected */
    public boolean shouldBeDraw(LayoutManagerHelper layoutManagerHelper, int i, int i2, int i3) {
        switch (this.mShowType) {
            case 1:
                if (i2 >= getRange().getLower().intValue() - 1) {
                    return true;
                }
                return false;
            case 2:
                if (i >= getRange().getLower().intValue() + 1) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }
}
