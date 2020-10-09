package com.alibaba.aliweex.bubble;

import androidx.annotation.NonNull;
import com.taobao.weex.el.parse.Operators;

public class BubblePosition implements Comparable<BubblePosition> {
    private static final String TAG = "BubblePosition";
    static float sMaxHeight;
    static float sMaxWidth;
    int column;
    float height;
    boolean isNail;
    BubblePosition mBottom;
    BubblePosition mLeft;
    BubblePosition mRight;
    BubblePosition mTop;
    int row;
    float width;
    float x;
    float y;

    public BubblePosition(float[] fArr) {
        if (fArr != null && fArr.length == 4) {
            this.x = fArr[0];
            this.y = fArr[1];
            this.width = fArr[2];
            this.height = fArr[3];
        }
    }

    /* access modifiers changed from: package-private */
    public void setRightSibling(BubblePosition bubblePosition) {
        this.mRight = bubblePosition;
    }

    /* access modifiers changed from: package-private */
    public BubblePosition getLeftSibling() {
        return this.mLeft;
    }

    /* access modifiers changed from: package-private */
    public BubblePosition getRightSibling() {
        return this.mRight;
    }

    /* access modifiers changed from: package-private */
    public void setLeftSibling(BubblePosition bubblePosition) {
        this.mLeft = bubblePosition;
    }

    /* access modifiers changed from: package-private */
    public void setTopSibling(BubblePosition bubblePosition) {
        this.mTop = bubblePosition;
    }

    /* access modifiers changed from: package-private */
    public void setBottomSibling(BubblePosition bubblePosition) {
        this.mBottom = bubblePosition;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        sb.append(this.row);
        sb.append(",");
        sb.append(this.column);
        sb.append(Operators.ARRAY_END_STR);
        sb.append("x=");
        sb.append(this.x);
        sb.append(", y=");
        sb.append(this.y);
        sb.append(", width=");
        sb.append(this.width);
        sb.append(", height=");
        sb.append(this.height);
        sb.append(", mLeft=");
        boolean z = false;
        sb.append(this.mLeft != null);
        sb.append(", mRight=");
        sb.append(this.mRight != null);
        sb.append(", mTop=");
        sb.append(this.mTop != null);
        sb.append(", mBottom=");
        if (this.mBottom != null) {
            z = true;
        }
        sb.append(z);
        sb.append('}');
        return sb.toString();
    }

    public int compareTo(@NonNull BubblePosition bubblePosition) {
        int i = this.row - bubblePosition.row;
        int i2 = this.column - bubblePosition.column;
        if (i2 > 0) {
            return 1;
        }
        if (i2 < 0) {
            return -1;
        }
        if (i > 0) {
            return 1;
        }
        return i < 0 ? -1 : 0;
    }
}
