package com.alimama.union.app.share.flutter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.infrastructure.image.piccollage.CollageModel;
import com.alimama.union.app.infrastructure.image.piccollage.GoodsPicCollageView;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.logger.MonitorErrorCode;
import com.alimama.union.app.share.MarginItemDecoration;
import com.alimama.union.app.share.Result;
import com.alimama.union.app.share.SharedImagesAdapter;
import com.alimama.union.app.share.flutter.network.ShareInfoResponse2;
import com.alimama.union.app.views.AlertMsgDialog;
import java.util.List;

public class SharePosterView extends LinearLayout {
    private static final int DELAY_STATE_TRANSITION_MILLIS = 1000;
    public static final String SHARE_QRCODE_SWITCH_DATA = "qrcode_switch_data";
    private final String TAG;
    private SharedImagesAdapter mImagesAdapter;
    @Nullable
    private View.OnClickListener mOnSaveCollageListener;
    @Nullable
    private View.OnClickListener mOnShareCollageListener;
    private GoodsPicCollageView mPicCollageView;
    private Button mSaveButton;
    private TextView mSelectedImagesNum;
    private TextView mTotalImagesNum;

    public SharePosterView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public SharePosterView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SharePosterView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "SharePosterView";
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        inflate(context, R.layout.fragment_single_image_share, this);
        setOrientation(1);
        this.mTotalImagesNum = (TextView) findViewById(R.id.tv_images_num);
        this.mSelectedImagesNum = (TextView) findViewById(R.id.tv_selected_images_num);
        this.mPicCollageView = (GoodsPicCollageView) findViewById(R.id.pic_collage_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        recyclerView.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelOffset(R.dimen.share_thumbnail_right_margin)));
        recyclerView.setLayoutManager(linearLayoutManager);
        this.mImagesAdapter = new SharedImagesAdapter(new View.OnClickListener() {
            public final void onClick(View view) {
                SharePosterView.lambda$initViews$9(SharePosterView.this, view);
            }
        });
        recyclerView.setAdapter(this.mImagesAdapter);
        this.mSaveButton = (Button) findViewById(R.id.btn_save_image);
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SharePosterView.lambda$initViews$10(SharePosterView.this, view);
            }
        });
        this.mSaveButton.setText(R.string.save_poster);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.sw_qrcode);
        if (getQrcodeSwitchData(getContext())) {
            switchCompat.setChecked(true);
            this.mPicCollageView.showQrCode();
            BusinessMonitorLogger.Share.switchQrCode("SharePosterView", true);
        } else {
            switchCompat.setChecked(false);
            this.mPicCollageView.hideQrCode();
            BusinessMonitorLogger.Share.switchQrCode("SharePosterView", false);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharePosterView.lambda$initViews$11(SharePosterView.this, compoundButton, z);
            }
        });
        findViewById(R.id.image_question).setOnClickListener(new View.OnClickListener(context) {
            private final /* synthetic */ Context f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                new AlertMsgDialog(this.f$0).title(R.string.share_qrcode_question_title).content((int) R.string.share_qrcode_question_content).positiveButtonText(R.string.confirm_okay).show();
            }
        });
        findViewById(R.id.btn_share_image).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SharePosterView.lambda$initViews$13(SharePosterView.this, view);
            }
        });
    }

    public static /* synthetic */ void lambda$initViews$9(SharePosterView sharePosterView, View view) {
        if (view.isSelected()) {
            UTHelper.SharePosterPage.selectCollageImage();
        } else {
            UTHelper.SharePosterPage.deselectCollageImage();
        }
        sharePosterView.mSelectedImagesNum.setText(String.valueOf(sharePosterView.mImagesAdapter.getSelectedItemCount()));
        sharePosterView.mPicCollageView.layoutCollage(sharePosterView.mImagesAdapter.getSelectedImagesSortByIndex());
        sharePosterView.resetSaveState();
    }

    public static /* synthetic */ void lambda$initViews$10(SharePosterView sharePosterView, View view) {
        UTHelper.SharePosterPage.clickSavePoster();
        if (sharePosterView.mOnSaveCollageListener != null) {
            sharePosterView.mOnSaveCollageListener.onClick(view);
        }
    }

    public static /* synthetic */ void lambda$initViews$11(SharePosterView sharePosterView, CompoundButton compoundButton, boolean z) {
        if (compoundButton.isChecked()) {
            sharePosterView.setQrcodeSwitchData(sharePosterView.getContext(), true);
            sharePosterView.mPicCollageView.showQrCode();
            UTHelper.SharePosterPage.turnOnQrCode();
            return;
        }
        sharePosterView.setQrcodeSwitchData(sharePosterView.getContext(), false);
        sharePosterView.mPicCollageView.hideQrCode();
        UTHelper.SharePosterPage.turnOffQrCode();
    }

    public static /* synthetic */ void lambda$initViews$13(SharePosterView sharePosterView, View view) {
        UTHelper.SharePosterPage.clickPosterSysShare();
        if (sharePosterView.mOnShareCollageListener != null) {
            sharePosterView.mOnShareCollageListener.onClick(view);
        }
    }

    public void setOnSaveCollageListener(@Nullable View.OnClickListener onClickListener) {
        this.mOnSaveCollageListener = onClickListener;
    }

    public void setOnShareCollageListener(@Nullable View.OnClickListener onClickListener) {
        this.mOnShareCollageListener = onClickListener;
    }

    public void setSavingState() {
        this.mSaveButton.setText(R.string.saving_images);
    }

    public void setTransientSuccessState() {
        this.mSaveButton.setText(R.string.save_success);
        postDelayed(new Runnable() {
            public final void run() {
                SharePosterView.this.resetSaveState();
            }
        }, 1000);
    }

    public void resetSaveState() {
        this.mSaveButton.setText(R.string.save_poster);
    }

    @MainThread
    public void saveCollageAsync(@NonNull String str, @Nullable Result<Uri> result) {
        AsyncTask.execute(new Runnable(str + System.currentTimeMillis(), this.mPicCollageView.saveAsImage(), result) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ Bitmap f$2;
            private final /* synthetic */ Result f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                SharePosterView.lambda$saveCollageAsync$15(SharePosterView.this, this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public static /* synthetic */ void lambda$saveCollageAsync$15(SharePosterView sharePosterView, String str, Bitmap bitmap, Result result) {
        Uri saveImageSync = ShareUtils.saveImageSync(sharePosterView.getContext(), str, bitmap);
        if (saveImageSync == null) {
            BusinessMonitorLogger.Share.savePosterFailed("SharePosterView", "点击保存海报", String.valueOf(MonitorErrorCode.SAVE_POSTER_PIC_TO_LOCAL_FAILED_CLICK_SAVE_PIC));
        }
        sharePosterView.post(new Runnable(saveImageSync) {
            private final /* synthetic */ Uri f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                SharePosterView.lambda$null$14(Result.this, this.f$1);
            }
        });
    }

    static /* synthetic */ void lambda$null$14(Result result, Uri uri) {
        if (result != null) {
            result.onResult(uri);
        }
    }

    @MainThread
    public void saveCollageAsync(@Nullable Result<Uri> result) {
        AsyncTask.execute(new Runnable(this.mPicCollageView.saveAsImage(), getContext(), result) {
            private final /* synthetic */ Bitmap f$1;
            private final /* synthetic */ Context f$2;
            private final /* synthetic */ Result f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                SharePosterView.lambda$saveCollageAsync$17(SharePosterView.this, this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public static /* synthetic */ void lambda$saveCollageAsync$17(SharePosterView sharePosterView, Bitmap bitmap, Context context, Result result) {
        Uri uriForShare = ShareUtils.uriForShare(context, ExternalPublicStorageSaver.getInstance().saveBitmapAs(bitmap, "share_image.png"));
        if (uriForShare == null) {
            BusinessMonitorLogger.Share.savePosterFailed("SharePosterView", "点击立即分享", String.valueOf(MonitorErrorCode.SAVE_POSTER_PIC_TO_LOCAL_FAILED_CLICK_IMMEDIATE_SHARE));
        }
        sharePosterView.post(new Runnable(uriForShare) {
            private final /* synthetic */ Uri f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                SharePosterView.lambda$null$16(Result.this, this.f$1);
            }
        });
    }

    static /* synthetic */ void lambda$null$16(Result result, Uri uri) {
        if (result != null) {
            result.onResult(uri);
        }
    }

    public void setSharedInfo(ShareInfoResponse2 shareInfoResponse2) {
        setImages(shareInfoResponse2.getImages());
        CollageModel collageModel = new CollageModel();
        collageModel.setPrice(shareInfoResponse2.getPrice()).setPriceAfterCoupon(shareInfoResponse2.getPriceAfterCoupon()).setCouponAmount(shareInfoResponse2.getCouponAmount()).setTitle(shareInfoResponse2.getTitle()).setCollageImageUrls(this.mImagesAdapter.getSelectedImagesSortByIndex());
        this.mPicCollageView.setCollageInfo(collageModel);
        this.mPicCollageView.setQrCodeUrl(shareInfoResponse2.getQrCodeUrl());
    }

    public void setImages(@NonNull List<String> list) {
        this.mImagesAdapter.setImages(list);
        this.mTotalImagesNum.setText(getContext().getString(R.string.share_total_images_num, new Object[]{Integer.valueOf(this.mImagesAdapter.getItemCount())}));
        this.mSelectedImagesNum.setText(String.valueOf(this.mImagesAdapter.getSelectedItemCount()));
    }

    public void setQrcodeSwitchData(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences("qrcode_switch_data", 0).edit();
        edit.putBoolean("qrcode_switch_data", z);
        edit.apply();
    }

    public boolean getQrcodeSwitchData(Context context) {
        return context.getSharedPreferences("qrcode_switch_data", 0).getBoolean("qrcode_switch_data", true);
    }
}
