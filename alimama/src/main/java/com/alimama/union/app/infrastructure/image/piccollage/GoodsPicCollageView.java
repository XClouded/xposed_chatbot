package com.alimama.union.app.infrastructure.image.piccollage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.alimama.moon.qrcode.NativeQrCodeGenerator;
import com.alimama.moon.utils.Preconditions;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;
import com.facebook.drawee.view.SimpleDraweeView;
import java.text.DecimalFormat;
import java.util.List;

public class GoodsPicCollageView extends LinearLayout {
    private static final int MAX_COLLAGE_IMAGE_NUM = 5;
    private static final String TAG = "GoodsPicCollageView";
    private static final double WIDTH_SCALE = 0.8d;
    private final DecimalFormat couponDecimalFormat;
    private final DecimalFormat decimalFormat;
    private View mBgContainer;
    private View mCouponContainer;
    private TextView mCouponTextView;
    private LinearLayout mImageCollageContainer;
    private EtaoDraweeView mMainImageView;
    private TextView mPriceAfterCouponView;
    private TextView mPriceTextView;
    private SimpleDraweeView mQrImageView;
    private TextView mTitleView;
    private int mWidthPaddingPixels;
    private int mWidthPixels;

    public GoodsPicCollageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public GoodsPicCollageView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GoodsPicCollageView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.decimalFormat = new DecimalFormat("0.00");
        this.couponDecimalFormat = new DecimalFormat("0");
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        this.mBgContainer = inflate(context, R.layout.layout_share_goods_qrcode_new, this).findViewById(R.id.bg_container);
        double screenWidthPixels = (double) LocalDisplay.getScreenWidthPixels(getContext());
        Double.isNaN(screenWidthPixels);
        this.mWidthPixels = (int) (screenWidthPixels * WIDTH_SCALE);
        this.mWidthPaddingPixels = this.mWidthPixels - LocalDisplay.dp2px(30.0f);
        this.mBgContainer.setLayoutParams(new LinearLayout.LayoutParams(this.mWidthPixels, -2));
        setOrientation(1);
        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        this.mImageCollageContainer = (LinearLayout) findViewById(R.id.goods_linear_layout);
        this.mImageCollageContainer.setLayoutParams(new LinearLayout.LayoutParams(this.mWidthPaddingPixels, -2));
        this.mPriceTextView = (TextView) findViewById(R.id.price_text_view);
        this.mTitleView = (TextView) findViewById(R.id.title_text_view);
        this.mCouponTextView = (TextView) findViewById(R.id.coupon_text_view);
        this.mPriceAfterCouponView = (TextView) findViewById(R.id.coupon_after_price_text_view);
        this.mMainImageView = (EtaoDraweeView) findViewById(R.id.main_image_view);
        this.mQrImageView = (SimpleDraweeView) findViewById(R.id.qrcode_image_view);
        this.mCouponContainer = findViewById(R.id.coupon_linear_layout);
    }

    public void setCollageInfo(@NonNull CollageModel collageModel) {
        this.mTitleView.setText(collageModel.getTitle());
        if (Double.isNaN(collageModel.getCouponAmount().doubleValue()) || collageModel.getCouponAmount().doubleValue() <= 0.0d) {
            setStyleNonCoupon(collageModel);
        } else {
            setStyleCoupon(collageModel);
        }
        generateQrCode(collageModel.getUrl());
        layoutCollage(collageModel.getCollageImageUrls());
    }

    public void generateQrCode(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.mQrImageView.setImageBitmap(NativeQrCodeGenerator.getInstance().genQRCodeBitmap(str));
                BusinessMonitorLogger.Share.qrCodeLoadSuccess(TAG);
            } catch (Exception e) {
                BusinessMonitorLogger.Share.qrCodeLoadFail(TAG, e.toString());
                Log.e(TAG, "generate Qr code failed", e);
            }
        }
    }

    public void setQrCodeUrl(String str) {
        this.mQrImageView.setImageURI(str);
    }

    public void layoutCollage(@NonNull List<String> list) {
        if (!list.isEmpty() && list.size() <= 5) {
            int size = list.size();
            String str = list.get(0);
            if (!TextUtils.equals(str, this.mMainImageView.getCurrentUrl())) {
                this.mMainImageView.setAnyImageUrl(str);
            }
            if (size == 1) {
                this.mImageCollageContainer.removeAllViews();
            } else if (size == 2) {
                layout2Images(list);
            } else if (size == 3) {
                layout3Images(list);
            } else if (size == 4) {
                layout4Images(list);
            } else {
                layout5Images(list);
            }
        }
    }

    @MainThread
    public Bitmap saveAsImage() {
        Bitmap createBitmap = Bitmap.createBitmap(this.mBgContainer.getMeasuredWidth(), this.mBgContainer.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        this.mBgContainer.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public void hideQrCode() {
        findViewById(R.id.iv_qrcode_delimiter).setVisibility(8);
        findViewById(R.id.qrcode_container).setVisibility(8);
        findViewById(R.id.iv_usage_guide).setVisibility(8);
        this.mCouponContainer.setPadding(this.mCouponContainer.getPaddingLeft(), this.mCouponContainer.getPaddingTop(), this.mCouponContainer.getPaddingRight(), getResources().getDimensionPixelOffset(R.dimen.common_padding_9));
        this.mBgContainer.setPadding(this.mBgContainer.getPaddingLeft(), this.mBgContainer.getPaddingTop(), this.mBgContainer.getPaddingRight(), getResources().getDimensionPixelOffset(R.dimen.share_image_collage_vertical_padding));
    }

    public void showQrCode() {
        findViewById(R.id.iv_qrcode_delimiter).setVisibility(0);
        findViewById(R.id.qrcode_container).setVisibility(0);
        findViewById(R.id.iv_usage_guide).setVisibility(0);
        this.mCouponContainer.setPadding(this.mCouponContainer.getPaddingLeft(), this.mCouponContainer.getPaddingTop(), this.mCouponContainer.getPaddingRight(), 0);
        this.mBgContainer.setPadding(this.mBgContainer.getPaddingLeft(), this.mBgContainer.getPaddingTop(), this.mBgContainer.getPaddingRight(), 0);
    }

    public boolean isQrCodeShown() {
        return findViewById(R.id.qrcode_container).isShown();
    }

    private void setStyleCoupon(CollageModel collageModel) {
        Resources resources = getResources();
        String string = resources.getString(R.string.share_goods_price);
        String format = this.decimalFormat.format(collageModel.getPrice());
        SpannableString spannableString = new SpannableString(string + format);
        spannableString.setSpan(new StrikethroughSpan(), string.length(), (string + format).length(), 33);
        this.mPriceTextView.setText(spannableString);
        this.mCouponTextView.setText(resources.getString(R.string.share_goods_price_end, new Object[]{this.couponDecimalFormat.format(collageModel.getCouponAmount())}));
        String string2 = getResources().getString(R.string.share_goods_coupon);
        String str = string2 + this.decimalFormat.format(collageModel.getPriceAfterCoupon());
        SpannableString spannableString2 = new SpannableString(str);
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#fd561f")), string2.length() - 1, str.length(), 33);
        spannableString2.setSpan(new AbsoluteSizeSpan(24, true), string2.length(), str.length(), 33);
        this.mPriceAfterCouponView.setText(spannableString2);
    }

    private void setStyleNonCoupon(CollageModel collageModel) {
        String string = getResources().getString(R.string.share_goods_price);
        String str = "";
        if (collageModel.getPrice() != null && !Double.isNaN(collageModel.getPrice().doubleValue())) {
            str = this.decimalFormat.format(collageModel.getPrice());
        }
        String str2 = string + str;
        SpannableString spannableString = new SpannableString(string + str);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#4a4a4a")), 0, string.length() + -2, 33);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#fd561f")), string.length() - 1, str2.length(), 33);
        spannableString.setSpan(new AbsoluteSizeSpan(15, true), 0, string.length() - 1, 33);
        spannableString.setSpan(new AbsoluteSizeSpan(24, true), string.length(), str2.length(), 33);
        this.mPriceTextView.setText(spannableString);
        this.mCouponContainer.setVisibility(8);
    }

    private void layout2Images(@NonNull List<String> list) {
        Preconditions.checkArgument(list.size() == 2, "需要2张图片");
        this.mImageCollageContainer.removeAllViews();
        ((EtaoDraweeView) inflate(getContext(), R.layout.layout_share_goods_qrcode_item3, this.mImageCollageContainer).findViewById(R.id.goods_item3_image_view)).setAnyImageUrl(list.get(1));
    }

    private void layout3Images(@NonNull List<String> list) {
        Preconditions.checkArgument(list.size() == 3, "需要3张图片");
        this.mImageCollageContainer.removeAllViews();
        View inflate = inflate(getContext(), R.layout.layout_share_goods_qrcode_item2, this.mImageCollageContainer);
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image3_view)).setVisibility(8);
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image4_view)).setVisibility(8);
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image1_view)).setAnyImageUrl(list.get(1));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image2_view)).setAnyImageUrl(list.get(2));
    }

    private void layout4Images(@NonNull List<String> list) {
        Preconditions.checkArgument(list.size() == 4, "需要4张图片");
        this.mImageCollageContainer.removeAllViews();
        View inflate = inflate(getContext(), R.layout.layout_share_goods_qrcode_item1, this.mImageCollageContainer);
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item1_image1_view)).setAnyImageUrl(list.get(1));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item1_image2_view)).setAnyImageUrl(list.get(2));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item1_image3_view)).setAnyImageUrl(list.get(3));
    }

    private void layout5Images(@NonNull List<String> list) {
        Preconditions.checkArgument(list.size() >= 5, "至少需要5张图片");
        this.mImageCollageContainer.removeAllViews();
        View inflate = inflate(getContext(), R.layout.layout_share_goods_qrcode_item2, this.mImageCollageContainer);
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image1_view)).setAnyImageUrl(list.get(1));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image2_view)).setAnyImageUrl(list.get(2));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image3_view)).setAnyImageUrl(list.get(3));
        ((EtaoDraweeView) inflate.findViewById(R.id.goods_item2_image4_view)).setAnyImageUrl(list.get(4));
    }
}
