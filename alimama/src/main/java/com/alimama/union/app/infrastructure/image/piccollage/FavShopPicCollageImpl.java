package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;

public class FavShopPicCollageImpl implements ICollage {
    private static final int IMAGE_HEIGHT = 723;
    private static final int IMAGE_WIDTH = 553;
    private static volatile FavShopPicCollageImpl instance;
    private final Context appContext;

    private FavShopPicCollageImpl(Context context) {
        this.appContext = context;
    }

    public static FavShopPicCollageImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (FavShopPicCollageImpl.class) {
                if (instance == null) {
                    instance = new FavShopPicCollageImpl(context);
                }
            }
        }
        return instance;
    }

    public Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception {
        return getBitmapFromView(shareObj, bitmap, bitmap2);
    }

    private Bitmap getBitmapFromView(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.appContext).inflate(R.layout.layout_share_fav_shop, (ViewGroup) null);
        ((ImageView) viewGroup.findViewById(R.id.iv_icon)).setImageBitmap(bitmap);
        ((ImageView) viewGroup.findViewById(R.id.iv_qrcode)).setImageBitmap(bitmap2);
        ((TextView) viewGroup.findViewById(R.id.tv_shop_name)).setText(shareObj.getFavShopName());
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(IMAGE_WIDTH, 1073741824), View.MeasureSpec.makeMeasureSpec(IMAGE_HEIGHT, 1073741824));
        viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        viewGroup.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
