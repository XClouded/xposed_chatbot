package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.taobao.android.dinamicx.widget.utils.DXUtils;

public class RedEnvelopesPicCollageImpI implements ICollage {
    private static volatile RedEnvelopesPicCollageImpI instance;
    private final Context appContext;

    private RedEnvelopesPicCollageImpI(Context context) {
        this.appContext = context;
    }

    public static RedEnvelopesPicCollageImpI getInstance(Context context) {
        if (instance == null) {
            synchronized (RedEnvelopesPicCollageImpI.class) {
                if (instance == null) {
                    instance = new RedEnvelopesPicCollageImpI(context);
                }
            }
        }
        return instance;
    }

    public Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception {
        return getBitmapFromView(shareObj, bitmap, bitmap2);
    }

    private Bitmap getBitmapFromView(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) {
        FrameLayout.LayoutParams layoutParams;
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.appContext).inflate(R.layout.layout_share_red_envelopes, (ViewGroup) null);
        LinearLayout linearLayout = (LinearLayout) viewGroup.findViewById(R.id.mian_content_view);
        TextView textView = (TextView) viewGroup.findViewById(R.id.title_text_view);
        if (shareObj != null && !TextUtils.isEmpty(shareObj.getTitle())) {
            textView.setText(shareObj.getTitle());
            if (textView.getText().length() > 10 && (layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams()) != null) {
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin - 70, layoutParams.rightMargin, layoutParams.bottomMargin);
            }
        }
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.main_image_view);
        ((ImageView) viewGroup.findViewById(R.id.qrcode_image_view)).setImageBitmap(bitmap2);
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(880, 1073741824), View.MeasureSpec.makeMeasureSpec(DXUtils.SCREEN_WIDTH, 1073741824));
        viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        viewGroup.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
