package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.text.TextUtils;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.taobao.tao.image.ImageStrategyConfig;

public final class PicCollageFactory {
    public static ICollage createPicCollage(Context context, ShareObj shareObj) {
        if (shareObj == null || TextUtils.isEmpty(shareObj.getMaterialType())) {
            throw new IllegalArgumentException("illegal argument");
        }
        String materialType = shareObj.getMaterialType();
        char c = 65535;
        switch (materialType.hashCode()) {
            case -1354573786:
                if (materialType.equals("coupon")) {
                    c = 1;
                    break;
                }
                break;
            case -1075325775:
                if (materialType.equals("favShop")) {
                    c = 4;
                    break;
                }
                break;
            case -931102249:
                if (materialType.equals("rights")) {
                    c = 2;
                    break;
                }
                break;
            case 3242771:
                if (materialType.equals("item")) {
                    c = 0;
                    break;
                }
                break;
            case 3529462:
                if (materialType.equals(ImageStrategyConfig.SHOP)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
                UTHelper.sendControlHit(UTHelper.PAGE_NAME_ITEM_WEEX_SHARE, UTHelper.CONTROL_NAME_CLICK_SAVE_PIC);
                if (shareObj.getAmount() == null || shareObj.getAmount().doubleValue() <= 0.0d) {
                    return GoodsPicCollageImpl1.getInstance(context);
                }
                return GoodsPicCollageImpl.getInstance(context);
            case 2:
                return RedEnvelopesPicCollageImpI.getInstance(context);
            case 3:
                return ShopPicCollageImpl.getInstance(context);
            case 4:
                return FavShopPicCollageImpl.getInstance(context);
            default:
                return SimplePicCollageImpl.getInstance(context);
        }
    }
}
