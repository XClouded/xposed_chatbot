package com.alimama.unwdinamicxcontainer.diywidget;

import alimama.com.unwimage.UNWImageView;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.unwdinamicxcontainer.R;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXUNWBigSalesViewWidgetNode extends DXWidgetNode {
    public static final long DXUNWBIGSALESVIEW_TYPE = 38200462374L;
    public static final long DXUNWBIGSALESVIEW_UNWBIGSALESVIEW = -8998589662073722194L;
    private String iconUrl;
    private String type;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXUNWBigSalesViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXUNWBigSalesViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXUNWBigSalesViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            this.type = ((DXUNWBigSalesViewWidgetNode) dXWidgetNode).type;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.big_sales_view_layout, (ViewGroup) null);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        render(view);
    }

    private void render(View view) {
        if (!TextUtils.isEmpty(this.type)) {
            view.setVisibility(0);
            ((UNWImageView) view.findViewById(R.id.iv_tag)).setAnyImageUrl(this.iconUrl);
            return;
        }
        view.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(resolveSize(DXScreenTool.getPx(DinamicXEngine.getApplicationContext(), "24np", 24), i), resolveSize(DXScreenTool.getPx(DinamicXEngine.getApplicationContext(), "12np", 12), i2));
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXUNWBIGSALESVIEW_TYPE) {
            this.type = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
