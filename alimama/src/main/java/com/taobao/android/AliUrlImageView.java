package com.taobao.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.uikit.feature.features.ImageShapeFeature;
import com.taobao.uikit.feature.features.RatioFeature;

public class AliUrlImageView extends TUrlImageView implements AliUrlImageViewInterface {
    private ImageShapeFeature mImageShapeFeature;
    private RatioFeature mRatioFeature;

    public void setDarkModeOverlay(boolean z, int i) {
    }

    public AliUrlImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setEnableLayoutOptimize(true);
    }

    public AliUrlImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setEnableLayoutOptimize(true);
    }

    public AliUrlImageView(Context context) {
        super(context);
        setEnableLayoutOptimize(true);
    }

    public AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder(String str, int i) {
        return new ImageStrategyConfigBuilderAdapter(ImageStrategyConfig.newBuilderWithName(str, i));
    }

    public AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder(String str) {
        return new ImageStrategyConfigBuilderAdapter(ImageStrategyConfig.newBuilderWithName(str));
    }

    public AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder(String str, String str2) {
        return new ImageStrategyConfigBuilderAdapter(ImageStrategyConfig.newBuilderWithName(str, str2));
    }

    public void setImageUrl(String str) {
        super.setImageUrl(str);
    }

    public void setStrategyConfig(Object obj) {
        super.setStrategyConfig(obj);
    }

    private RatioFeature getRatioFeature() {
        if (this.mRatioFeature == null) {
            this.mRatioFeature = new RatioFeature();
            addFeature(this.mRatioFeature);
        }
        return this.mRatioFeature;
    }

    public void setRatio(float f) {
        getRatioFeature().setRatio(f);
    }

    public void setOrientation(int i) {
        getRatioFeature().setOrientation(i);
    }

    private ImageShapeFeature getImageShapeFeature() {
        if (this.mImageShapeFeature == null) {
            this.mImageShapeFeature = new ImageShapeFeature();
            addFeature(this.mImageShapeFeature);
        }
        return this.mImageShapeFeature;
    }

    public void setShape(int i) {
        getImageShapeFeature().setShape(i);
    }

    public void setCornerRadius(float f, float f2, float f3, float f4) {
        getImageShapeFeature().setCornerRadius(f, f2, f3, f4);
    }

    public void setStrokeColor(int i) {
        getImageShapeFeature().setStrokeEnable(true);
        getImageShapeFeature().setStrokeColor(i);
    }

    public void setStrokeWidth(float f) {
        getImageShapeFeature().setStrokeEnable(true);
        getImageShapeFeature().setStrokeWidth(f);
    }

    public void setSkipAutoSize(boolean z) {
        super.setSkipAutoSize(z);
    }

    public void succListener(AliImageListener<AliImageSuccEvent> aliImageListener) {
        if (aliImageListener == null) {
            super.succListener((IPhenixListener<SuccPhenixEvent>) null);
        } else {
            super.succListener(new AliImageSuccListenerAdapter(aliImageListener));
        }
    }

    public void failListener(AliImageListener<AliImageFailEvent> aliImageListener) {
        if (aliImageListener == null) {
            super.failListener((IPhenixListener<FailPhenixEvent>) null);
        } else {
            super.failListener(new AliImageFailListenerAdapter(aliImageListener));
        }
    }

    public void setAutoRelease(boolean z) {
        super.setAutoRelease(z);
    }

    public void setPlaceHoldImageResId(int i) {
        super.setPlaceHoldImageResId(i);
    }

    public void setPlaceHoldForeground(Drawable drawable) {
        super.setPlaceHoldForeground(drawable);
    }

    public void setErrorImageResId(int i) {
        super.setErrorImageResId(i);
    }

    public void setPriorityModuleName(String str) {
        super.setPriorityModuleName(str);
    }
}
