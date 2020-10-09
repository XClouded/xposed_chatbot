package com.taobao.android;

import com.taobao.android.AliImageStrategyConfigBuilderInterface;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.TaobaoImageUrlStrategy;

public class ImageStrategyConfigBuilderAdapter implements AliImageStrategyConfigBuilderInterface {
    private final ImageStrategyConfig.Builder mBuilder;

    public ImageStrategyConfigBuilderAdapter(ImageStrategyConfig.Builder builder) {
        this.mBuilder = builder;
    }

    public ImageStrategyConfigBuilderAdapter skip(boolean z) {
        this.mBuilder.skip(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter setFinalWidth(int i) {
        this.mBuilder.setFinalWidth(i);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter setFinalHeight(int i) {
        this.mBuilder.setFinalHeight(i);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter setCutType(TaobaoImageUrlStrategy.CutType cutType) {
        this.mBuilder.setCutType(cutType);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter enableWebP(boolean z) {
        this.mBuilder.enableWebP(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter setSizeLimitType(AliImageStrategyConfigBuilderInterface.AliSizeLimitType aliSizeLimitType) {
        this.mBuilder.setSizeLimitType(ImageStrategyConfig.SizeLimitType.valueOf(aliSizeLimitType.toString()));
        return this;
    }

    public ImageStrategyConfigBuilderAdapter forceWebPOn(boolean z) {
        this.mBuilder.forceWebPOn(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter enableQuality(boolean z) {
        this.mBuilder.enableQuality(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter enableSharpen(boolean z) {
        this.mBuilder.enableSharpen(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter enableMergeDomain(boolean z) {
        this.mBuilder.enableMergeDomain(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter enableLevelModel(boolean z) {
        this.mBuilder.enableLevelModel(z);
        return this;
    }

    public ImageStrategyConfigBuilderAdapter setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality imageQuality) {
        this.mBuilder.setFinalImageQuality(imageQuality);
        return this;
    }

    public Object build() {
        return this.mBuilder.build();
    }
}
