package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimama.unwdinamicxcontainer.R;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;

public class DXWNWDiscountTagViewWidgetNode extends DXWidgetNode {
    public static final long DXWNWDISCOUNTTAGVIEW_PRICETEXT = -632784431399785535L;
    public static final long DXWNWDISCOUNTTAGVIEW_WNWDISCOUNTTAGVIEW = 6012361638446792206L;
    private String priceText;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXWNWDiscountTagViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXWNWDiscountTagViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXWNWDiscountTagViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            this.priceText = ((DXWNWDiscountTagViewWidgetNode) dXWidgetNode).priceText;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.discount_tag_view_layout, (ViewGroup) null);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        renderPriceText(view);
    }

    private void renderPriceText(View view) {
        if (TextUtils.isEmpty(this.priceText) || this.priceText.trim().length() != 4) {
            view.setVisibility(4);
            return;
        }
        view.setVisibility(0);
        String str = this.priceText;
        String substring = str.substring(0, 1);
        String substring2 = str.substring(1, 3);
        String substring3 = str.substring(3, 4);
        ((TextView) view.findViewById(R.id.tv_discount_left)).setText(substring);
        ((TextView) view.findViewById(R.id.tv_discount_right)).setText(substring2);
        ((TextView) view.findViewById(R.id.tv_discount_bottom)).setText(substring3);
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXWNWDISCOUNTTAGVIEW_PRICETEXT) {
            this.priceText = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
