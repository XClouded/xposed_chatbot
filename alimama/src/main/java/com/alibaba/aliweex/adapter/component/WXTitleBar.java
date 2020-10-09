package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageSharpen;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;

public class WXTitleBar extends WXComponent<View> {
    WXTitleBorderView borderView;
    int iconTextSize;
    View leftLine;
    int mBarSpace;
    String mlineColor;
    int mlineWeight = 1;
    String mtextBorderColor;
    int mtextBorderRadius;
    int mtextBorderWidth;
    String mtextColor;
    String mtextFontFamliy;
    int mtextFontSize;
    int mtextInnerSpace;
    int mtextLineHeight;
    int mtextOuterSpace;
    String mtextTitle;
    View rightLine;
    int titleWidth;
    int width;
    WXTitleBorderView wxTitleBorderView;

    public WXTitleBar(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public void init() {
        this.mlineColor = (String) getValue(TitlebarConstant.lineColor, TitlebarConstant.defaultColor);
        this.mlineWeight = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.lineWeight, 1)).intValue());
        this.mBarSpace = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.barSpace, 30)).intValue());
        this.mtextBorderWidth = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textBorderWidth, 1)).intValue());
        this.mtextInnerSpace = this.mtextBorderWidth != 0 ? (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textInnerSpace, 20)).intValue()) : 0;
        this.mtextOuterSpace = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textOuterSpace, 10)).intValue());
        this.mtextColor = (String) getValue(TitlebarConstant.textColor, TitlebarConstant.defaultColor);
        this.mtextFontSize = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textFontSize, 20)).intValue());
        this.mtextTitle = getAttrs().get("value") != null ? (String) getAttrs().get("value") : TitlebarConstant.title;
        this.mtextFontFamliy = (String) getValue(TitlebarConstant.textFontFamily, "");
        this.mtextLineHeight = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textLineHeight, Integer.valueOf(this.mtextFontSize))).intValue());
        this.mtextBorderRadius = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textBorderRadius, Integer.valueOf(this.mtextLineHeight / 2))).intValue());
        this.mtextBorderColor = (String) getValue(TitlebarConstant.textBorderColor, TitlebarConstant.defaultColor);
        this.iconTextSize = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.iconTextFontSize, Integer.valueOf(this.mtextFontSize))).intValue());
        this.width = (int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue("width", Integer.valueOf(FeatureFactory.PRIORITY_ABOVE_NORMAL))).intValue());
    }

    public <T> T getValue(String str, T t) {
        T t2 = getStyles().get(str);
        return t2 != null ? t2 : t;
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        init();
        drawLine(context);
        drawTilebar(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.addView(this.leftLine);
        linearLayout.addView(this.wxTitleBorderView);
        linearLayout.addView(this.rightLine);
        return linearLayout;
    }

    public void drawTilebar(Context context) {
        this.wxTitleBorderView = new WXTitleBorderView(context, this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.titleWidth, this.mtextFontSize + 10);
        layoutParams.leftMargin = this.mtextOuterSpace;
        this.wxTitleBorderView.setLayoutParams(layoutParams);
        this.wxTitleBorderView.setGravity(17);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(-16776961);
        if (this.mtextBorderWidth == 0) {
            paint.setColor(0);
        }
        paint.setStrokeWidth((float) this.mtextBorderWidth);
        RoundCornerDrawable roundCornerDrawable = new RoundCornerDrawable(paint, this.mtextBorderRadius);
        if (Build.VERSION.SDK_INT >= 16) {
            this.wxTitleBorderView.setBackground(roundCornerDrawable);
        }
    }

    public void drawLine(Context context) {
        this.titleWidth = (this.mtextTitle.length() * this.mtextFontSize) + this.iconTextSize + this.mtextInnerSpace + 2;
        int i = (((this.width / 2) - (this.titleWidth / 2)) - this.mBarSpace) - this.mtextOuterSpace;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, this.mlineWeight);
        layoutParams.leftMargin = this.mBarSpace;
        layoutParams.width = i;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i, this.mlineWeight);
        layoutParams2.leftMargin = this.mtextOuterSpace;
        layoutParams2.width = i;
        this.leftLine = new View(context);
        this.rightLine = new View(context);
        this.leftLine.setBackgroundColor(Color.parseColor(this.mlineColor));
        this.rightLine.setBackgroundColor(Color.parseColor(this.mlineColor));
        this.leftLine.setLayoutParams(layoutParams);
        this.rightLine.setLayoutParams(layoutParams2);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        if (((str.hashCode() == 114148 && str.equals("src")) ? (char) 0 : 65535) != 0) {
            return super.setProperty(str, obj);
        }
        String string = WXUtils.getString(obj, (String) null);
        if (string == null) {
            return true;
        }
        setSrc(string);
        return true;
    }

    @WXComponentProp(name = "src")
    public void setSrc(String str) {
        WXImageStrategy wXImageStrategy = new WXImageStrategy();
        boolean z = true;
        wXImageStrategy.isClipping = true;
        if (getAttrs().getImageSharpen() != WXImageSharpen.SHARPEN) {
            z = false;
        }
        wXImageStrategy.isSharpen = z;
        IWXImgLoaderAdapter imgLoaderAdapter = getInstance().getImgLoaderAdapter();
        if (imgLoaderAdapter != null) {
            imgLoaderAdapter.setImage(str, this.wxTitleBorderView.icon, getAttrs().getImageQuality(), wXImageStrategy);
        }
    }
}
