package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import com.alimama.unwdinamicxcontainer.R;
import com.taobao.android.dinamicx.widget.DXTextViewWidgetNode;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;

public class DXUNWFinalPriceViewWidgetNode extends DXTextViewWidgetNode {
    public static final long DXUNWFINALPRICEVIEW_PRICENAME = -1774401557251293571L;
    public static final long DXUNWFINALPRICEVIEW_PRICEVALUE = -3304601787760991750L;
    public static final long DXUNWFINALPRICEVIEW_UNWFINALPRICEVIEW = 8715072507900618115L;
    private String PriceName;
    private String PriceValue;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXUNWFinalPriceViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXUNWFinalPriceViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXUNWFinalPriceViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            DXUNWFinalPriceViewWidgetNode dXUNWFinalPriceViewWidgetNode = (DXUNWFinalPriceViewWidgetNode) dXWidgetNode;
            this.PriceName = dXUNWFinalPriceViewWidgetNode.PriceName;
            this.PriceValue = dXUNWFinalPriceViewWidgetNode.PriceValue;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return super.onCreateView(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
    }

    private void setNonPriceSpan(Resources resources, SpannableString spannableString, int i) {
        if (i > 0) {
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, i, 33);
            spannableString.setSpan(new ForegroundColorSpan(resources.getColor(R.color.gray_999999)), 0, i, 33);
        }
    }

    private void setCurrencySpan(Resources resources, SpannableString spannableString, int i) {
        if (i >= 0) {
            int i2 = i + 1;
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), i, i2, 33);
            spannableString.setSpan(new ForegroundColorSpan(resources.getColor(R.color.red_ff0033)), i, i2, 33);
        }
    }

    private void setPriceSpan(Resources resources, SpannableString spannableString, int i) {
        if (i >= 0) {
            int length = spannableString.length();
            spannableString.setSpan(new StyleSpan(1), i, length, 33);
            spannableString.setSpan(new AbsoluteSizeSpan(16, true), i, length, 33);
            spannableString.setSpan(new ForegroundColorSpan(resources.getColor(R.color.red_ff0033)), i, length, 33);
        }
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setText(getBuyPriceStr(getDXRuntimeContext().getContext()));
        super.onMeasure(i, i2);
    }

    private SpannableString getBuyPriceStr(Context context) {
        Resources resources = context.getResources();
        int indexOf = this.PriceValue.indexOf("¥");
        SpannableString spannableString = new SpannableString(this.PriceValue);
        setNonPriceSpan(resources, spannableString, indexOf);
        setCurrencySpan(resources, spannableString, indexOf);
        setPriceSpan(resources, spannableString, indexOf >= 0 ? indexOf + 1 : 0);
        return spannableString;
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXUNWFINALPRICEVIEW_PRICENAME) {
            this.PriceName = str;
        } else if (j == DXUNWFINALPRICEVIEW_PRICEVALUE) {
            this.PriceValue = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
