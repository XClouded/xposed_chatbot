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

public class ShopPicCollageImpl implements ICollage {
    private static volatile ShopPicCollageImpl instance;
    private final Context appContext;

    private ShopPicCollageImpl(Context context) {
        this.appContext = context;
    }

    public static ShopPicCollageImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (ShopPicCollageImpl.class) {
                if (instance == null) {
                    instance = new ShopPicCollageImpl(context);
                }
            }
        }
        return instance;
    }

    public Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception {
        return getBitmapFromView(shareObj, bitmap, bitmap2);
    }

    private Bitmap getBitmapFromView(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.appContext).inflate(R.layout.layout_share_shop_qrcode, (ViewGroup) null);
        ((TextView) viewGroup.findViewById(R.id.shop_title_text_view)).setText(shareObj.getTitle());
        ((ImageView) viewGroup.findViewById(R.id.shop_image_view)).setImageBitmap(bitmap);
        ((ImageView) viewGroup.findViewById(R.id.qrcode_image_view)).setImageBitmap(bitmap2);
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(800, 1073741824), View.MeasureSpec.makeMeasureSpec(657, 1073741824));
        viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        viewGroup.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
