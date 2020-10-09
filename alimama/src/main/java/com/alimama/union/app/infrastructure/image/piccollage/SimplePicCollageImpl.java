package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;

public class SimplePicCollageImpl implements ICollage {
    private static volatile SimplePicCollageImpl instance;
    private final Context appContext;

    private SimplePicCollageImpl(Context context) {
        this.appContext = context;
    }

    public static SimplePicCollageImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (SimplePicCollageImpl.class) {
                if (instance == null) {
                    instance = new SimplePicCollageImpl(context);
                }
            }
        }
        return instance;
    }

    public Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception {
        return getBitmapFromView(bitmap, bitmap2);
    }

    private Bitmap getBitmapFromView(Bitmap bitmap, Bitmap bitmap2) throws Exception {
        int max = Math.max(bitmap.getWidth(), bitmap2.getWidth() * 2);
        int height = bitmap.getHeight();
        double d = (double) max;
        Double.isNaN(d);
        int max2 = (int) Math.max(d * 0.375d, (double) bitmap2.getHeight());
        int i = height + max2;
        Bitmap createBitmap = Bitmap.createBitmap(max, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.appContext).inflate(R.layout.layout_share_qrcode, (ViewGroup) null);
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.qr_code_pic);
        ImageView imageView2 = (ImageView) viewGroup.findViewById(R.id.footprint_pic);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = max2;
        layoutParams.height = max2;
        imageView.setLayoutParams(layoutParams);
        double d2 = (double) max2;
        Double.isNaN(d2);
        int i2 = (int) (d2 * 0.4d);
        ViewGroup.LayoutParams layoutParams2 = imageView2.getLayoutParams();
        layoutParams2.width = i2;
        layoutParams2.height = i2;
        imageView2.setLayoutParams(layoutParams2);
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.findViewById(R.id.qrcode_layout);
        ViewGroup.LayoutParams layoutParams3 = viewGroup2.getLayoutParams();
        layoutParams3.width = max;
        layoutParams3.height = max2;
        viewGroup2.setLayoutParams(layoutParams3);
        float f = (float) (i2 / 12);
        TextView textView = (TextView) viewGroup.findViewById(R.id.text_view);
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams4.topMargin = (int) f;
        textView.setLayoutParams(layoutParams4);
        textView.setTextSize(f);
        ((ImageView) viewGroup.findViewById(R.id.item_pic)).setImageBitmap(bitmap);
        imageView.setImageBitmap(bitmap2);
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(max, 1073741824), View.MeasureSpec.makeMeasureSpec(i, 1073741824));
        viewGroup.layout(0, 0, max, i);
        viewGroup.draw(canvas);
        return createBitmap;
    }
}
