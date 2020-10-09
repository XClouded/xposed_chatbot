package com.taobao.android;

import android.graphics.drawable.Drawable;

public interface AliUrlImageViewInterface {
    void failListener(AliImageListener<AliImageFailEvent> aliImageListener);

    AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder(String str);

    void setAutoRelease(boolean z);

    void setCornerRadius(float f, float f2, float f3, float f4);

    void setDarkModeOverlay(boolean z, int i);

    void setErrorImageResId(int i);

    void setImageUrl(String str);

    void setOrientation(int i);

    void setPlaceHoldForeground(Drawable drawable);

    void setPlaceHoldImageResId(int i);

    void setPriorityModuleName(String str);

    void setRatio(float f);

    void setShape(int i);

    void setSkipAutoSize(boolean z);

    void setStrategyConfig(Object obj);

    void setStrokeColor(int i);

    void setStrokeWidth(float f);

    void succListener(AliImageListener<AliImageSuccEvent> aliImageListener);
}
