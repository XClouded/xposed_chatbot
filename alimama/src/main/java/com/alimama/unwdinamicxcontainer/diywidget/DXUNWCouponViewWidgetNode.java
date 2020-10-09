package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.alimama.unwdinamicxcontainer.R;
import com.taobao.android.dinamicx.widget.DXTextViewWidgetNode;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;

public class DXUNWCouponViewWidgetNode extends DXTextViewWidgetNode {
    public static final long DXUNWCOUPONVIEW_COUPONTEXT = -8099277359819629757L;
    public static final long DXUNWCOUPONVIEW_UNWCOUPONVIEW = 968253645300362262L;
    private String couponText;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXUNWCouponViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXUNWCouponViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXUNWCouponViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            this.couponText = ((DXUNWCouponViewWidgetNode) dXWidgetNode).couponText;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return super.onCreateView(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        view.setBackgroundResource(R.drawable.common_icon_coupon_bg);
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onBeforeMeasure(TextView textView) {
        super.onBeforeMeasure(textView);
        textView.setBackgroundResource(R.drawable.common_icon_coupon_bg);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXUNWCOUPONVIEW_COUPONTEXT) {
            this.couponText = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
