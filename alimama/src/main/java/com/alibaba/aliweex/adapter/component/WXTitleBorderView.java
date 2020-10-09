package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.WXImageView;
import com.taobao.weex.utils.WXViewUtils;

public class WXTitleBorderView extends LinearLayout {
    public WXImageView icon;
    WXComponent mComponent;
    Context mContext;
    int mtextLineHeight = ((int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textLineHeight, Integer.valueOf(this.realTextFontSize))).intValue()));
    int realTextFontSize = ((int) WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textFontSize, 20)).intValue()));
    String title;
    private TextView titleText;

    public WXTitleBorderView(Context context, WXComponent wXComponent) {
        super(context);
        this.mContext = context;
        this.mComponent = wXComponent;
        setOrientation(0);
        this.icon = new WXImageView(context);
        this.title = wXComponent.getAttrs().get("value") != null ? (String) wXComponent.getAttrs().get("value") : TitlebarConstant.title;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((this.realTextFontSize * this.title.length()) + 2, this.mtextLineHeight + 5);
        layoutParams.leftMargin = 6;
        this.titleText = new TextView(context);
        this.titleText.setGravity(17);
        this.titleText.setSingleLine();
        this.titleText.setLayoutParams(layoutParams);
        this.titleText.setText(this.title);
        this.titleText.setTextColor(Color.parseColor((String) getValue(TitlebarConstant.textColor, TitlebarConstant.defaultColor)));
        this.titleText.setTextSize(0, WXViewUtils.getRealPxByWidth((float) ((Integer) getValue(TitlebarConstant.textFontSize, 20)).intValue()));
        this.icon.setScaleType(ImageView.ScaleType.FIT_XY);
        this.icon.setLayoutParams(new LinearLayout.LayoutParams(this.realTextFontSize, this.realTextFontSize));
        addView(this.icon);
        addView(this.titleText);
    }

    public <T> T getValue(String str, T t) {
        T t2 = this.mComponent.getAttrs().get(str);
        return t2 != null ? t2 : t;
    }
}
