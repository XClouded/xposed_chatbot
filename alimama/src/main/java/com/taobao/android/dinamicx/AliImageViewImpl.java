package com.taobao.android.dinamicx;

import android.content.Context;
import android.widget.ImageView;
import com.taobao.android.AliImageServiceFetcher;
import com.taobao.android.AliImageStrategyConfigBuilderInterface;
import com.taobao.android.AliUrlImageViewInterface;
import com.taobao.android.dinamic.constructor.DImageViewConstructor;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.property.ScreenTool;

public class AliImageViewImpl implements DImageViewConstructor.DXWebImageInterface {
    public ImageView buildView(Context context) {
        return (ImageView) AliImageServiceFetcher.getImageService().newUrlImageView(context);
    }

    public void setImage(ImageView imageView, String str, DImageViewConstructor.ImageOption imageOption) {
        AliUrlImageViewInterface aliUrlImageViewInterface = (AliUrlImageViewInterface) imageView;
        if (imageOption.isNeedSetImageUrl()) {
            aliUrlImageViewInterface.setImageUrl(str);
        }
        if (imageOption.isNeedClipRadius()) {
            float px = (float) ScreenTool.getPx(imageView.getContext(), imageOption.cornerRadius, 0);
            aliUrlImageViewInterface.setCornerRadius(px, px, px, px);
            aliUrlImageViewInterface.setShape(1);
        }
        if (imageOption.isNeedBorderWidth()) {
            aliUrlImageViewInterface.setStrokeWidth((float) ScreenTool.getPx(imageView.getContext(), imageOption.borderWidth, 0));
        }
        if (imageOption.isNeedBorderColor()) {
            aliUrlImageViewInterface.setStrokeColor(DAttrUtils.parseColor(imageOption.borderColor, 0));
        }
        if (imageOption.isNeedLimitSize() && "heightLimit".equals(imageOption.sizeType)) {
            AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder = aliUrlImageViewInterface.newImageStrategyConfigBuilder(imageOption.module);
            newImageStrategyConfigBuilder.setSizeLimitType(AliImageStrategyConfigBuilderInterface.AliSizeLimitType.HEIGHT_LIMIT);
            aliUrlImageViewInterface.setStrategyConfig(newImageStrategyConfigBuilder.build());
        }
        if (imageOption.isNeedRatio()) {
            aliUrlImageViewInterface.setOrientation(imageOption.orientation);
            aliUrlImageViewInterface.setRatio(imageOption.ratio);
        }
    }
}
