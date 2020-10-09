package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import java.text.DecimalFormat;

public class GoodsPicCollageImpl implements ICollage {
    private static DecimalFormat couponDecimalFormat = new DecimalFormat("0");
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static volatile GoodsPicCollageImpl instance;
    private final Context appContext;

    private GoodsPicCollageImpl(Context context) {
        this.appContext = context;
    }

    public static GoodsPicCollageImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (GoodsPicCollageImpl.class) {
                if (instance == null) {
                    instance = new GoodsPicCollageImpl(context);
                }
            }
        }
        return instance;
    }

    public Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception {
        return getBitmapFromView(shareObj, bitmap, bitmap2);
    }

    private Bitmap getBitmapFromView(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.appContext).inflate(R.layout.layout_share_goods_qrcode, (ViewGroup) null);
        TextView textView = (TextView) viewGroup.findViewById(R.id.title_text_view);
        TextView textView2 = (TextView) viewGroup.findViewById(R.id.freeshipping_tag_text_view);
        String title = shareObj.getTitle();
        if (shareObj.getFreeShipping().booleanValue()) {
            title = "          " + title;
            textView2.setVisibility(0);
        } else {
            textView2.setVisibility(4);
        }
        textView.setText(title);
        String format = decimalFormat.format(shareObj.getDiscountPrice());
        SpannableString spannableString = new SpannableString("现价 ￥" + format);
        spannableString.setSpan(new StrikethroughSpan(), "现价 ￥".length(), ("现价 ￥" + format).length(), 33);
        ((TextView) viewGroup.findViewById(R.id.price_text_view)).setText(spannableString);
        ((TextView) viewGroup.findViewById(R.id.coupon_text_view)).setText(couponDecimalFormat.format(shareObj.getAmount()) + "元");
        String str = "券后价 ￥" + decimalFormat.format(shareObj.getDiscountCouponPrice());
        SpannableString spannableString2 = new SpannableString(str);
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#fd561f")), "券后价 ￥".length() - 1, str.length(), 33);
        spannableString2.setSpan(new AbsoluteSizeSpan(48), "券后价 ￥".length(), str.length(), 33);
        ((TextView) viewGroup.findViewById(R.id.coupon_after_price_text_view)).setText(spannableString2);
        ((ImageView) viewGroup.findViewById(R.id.main_image_view)).setImageBitmap(bitmap);
        ((ImageView) viewGroup.findViewById(R.id.qrcode_image_view)).setImageBitmap(bitmap2);
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(889, 1073741824), View.MeasureSpec.makeMeasureSpec(1264, 1073741824));
        viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        viewGroup.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
