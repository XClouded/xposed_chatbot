package com.taobao.android.dinamicx;

import android.content.Context;
import android.widget.ImageView;
import com.taobao.android.AliImageListener;
import com.taobao.android.AliImageServiceFetcher;
import com.taobao.android.AliImageStrategyConfigBuilderInterface;
import com.taobao.android.AliImageSuccEvent;
import com.taobao.android.AliUrlImageViewInterface;
import com.taobao.android.dinamicx.widget.DXImageWidgetNode;
import com.taobao.android.dinamicx.widget.IDXWebImageInterface;

public class AliDXImageViewImpl implements IDXWebImageInterface {
    public ImageView buildView(Context context) {
        return (ImageView) AliImageServiceFetcher.getImageService().newUrlImageView(context);
    }

    public void setImage(ImageView imageView, String str, final DXImageWidgetNode.ImageOption imageOption) {
        AliUrlImageViewInterface aliUrlImageViewInterface = (AliUrlImageViewInterface) imageView;
        if (imageOption.isNeedSetImageUrl()) {
            aliUrlImageViewInterface.setImageUrl(str);
        }
        aliUrlImageViewInterface.setSkipAutoSize(imageOption.isAnimated() || imageOption.isForceOriginal());
        aliUrlImageViewInterface.setAutoRelease(imageOption.isAutoRelease());
        aliUrlImageViewInterface.setPlaceHoldForeground(imageOption.placeHolder);
        aliUrlImageViewInterface.setPlaceHoldImageResId(imageOption.placeHolderResId);
        aliUrlImageViewInterface.setDarkModeOverlay(imageOption.isNeedDarkModeOverlay(), (int) (imageOption.getDarkModeOverlayOpacity() * 255.0d));
        if (imageOption.isNeedClipRadius()) {
            int[] iArr = imageOption.cornerRadii;
            aliUrlImageViewInterface.setCornerRadius((float) iArr[0], (float) iArr[1], (float) iArr[3], (float) iArr[2]);
            aliUrlImageViewInterface.setShape(1);
        }
        if (imageOption.isNeedBorderWidth()) {
            aliUrlImageViewInterface.setStrokeWidth((float) imageOption.borderWidth);
        }
        if (imageOption.isNeedBorderColor()) {
            aliUrlImageViewInterface.setStrokeColor(imageOption.borderColor);
        }
        if (imageOption.isNeedLimitSize() && "heightLimit".equals(imageOption.sizeType)) {
            AliImageStrategyConfigBuilderInterface newImageStrategyConfigBuilder = aliUrlImageViewInterface.newImageStrategyConfigBuilder(imageOption.module);
            newImageStrategyConfigBuilder.setSizeLimitType(AliImageStrategyConfigBuilderInterface.AliSizeLimitType.HEIGHT_LIMIT);
            aliUrlImageViewInterface.setStrategyConfig(newImageStrategyConfigBuilder.build());
        }
        if (imageOption.listener != null) {
            aliUrlImageViewInterface.succListener(new AliImageListener<AliImageSuccEvent>() {
                public boolean onHappen(AliImageSuccEvent aliImageSuccEvent) {
                    DXImageWidgetNode.ImageResult imageResult = new DXImageWidgetNode.ImageResult();
                    imageResult.drawable = aliImageSuccEvent.getDrawable();
                    imageOption.listener.onHappen(imageResult);
                    return false;
                }
            });
        } else {
            aliUrlImageViewInterface.succListener((AliImageListener<AliImageSuccEvent>) null);
        }
    }
}
