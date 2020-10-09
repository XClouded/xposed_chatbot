package com.alimama.union.app.share;

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
import com.alimama.union.app.share.network.ShareInfoResponse;
import com.alimama.union.app.views.AlertMsgDialog;
import java.util.List;

public class SingleImageShareView extends LinearLayout {
    private static final int DELAY_STATE_TRANSITION_MILLIS = 1000;
    public static final String SHARE_QRCODE_SWITCH_DATA = "qrcode_switch_data";
    /* access modifiers changed from: private */
    public SharedImagesAdapter mImagesAdapter;
    /* access modifiers changed from: private */
    @Nullable
    public View.OnClickListener mOnSaveCollageListener;
    /* access modifiers changed from: private */
    @Nullable
    public View.OnClickListener mOnShareCollageListener;
    /* access modifiers changed from: private */
    public GoodsPicCollageView mPicCollageView;
    private SwitchCompat mQrCodeSwitch;
    private Button mSaveButton;
    /* access modifiers changed from: private */
    public TextView mSelectedImagesNum;
    private Button mShareButton;
    private TextView mTotalImagesNum;

    public SingleImageShareView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public SingleImageShareView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SingleImageShareView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull final Context context) {
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
            public void onClick(View view) {
                SingleImageShareView.this.mSelectedImagesNum.setText(String.valueOf(SingleImageShareView.this.mImagesAdapter.getSelectedItemCount()));
                SingleImageShareView.this.mPicCollageView.layoutCollage(SingleImageShareView.this.mImagesAdapter.getSelectedImagesSortByIndex());
                SingleImageShareView.this.resetSaveState();
            }
        });
        recyclerView.setAdapter(this.mImagesAdapter);
        this.mSaveButton = (Button) findViewById(R.id.btn_save_image);
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_SAVE_PIC);
                if (SingleImageShareView.this.mOnSaveCollageListener != null) {
                    SingleImageShareView.this.mOnSaveCollageListener.onClick(view);
                }
            }
        });
        this.mQrCodeSwitch = (SwitchCompat) findViewById(R.id.sw_qrcode);
        if (getQrcodeSwitchData(getContext())) {
            this.mQrCodeSwitch.setChecked(true);
            this.mPicCollageView.showQrCode();
        } else {
            this.mQrCodeSwitch.setChecked(false);
            this.mPicCollageView.hideQrCode();
        }
        this.mQrCodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (compoundButton.isChecked()) {
                    SingleImageShareView.this.setQrcodeSwitchData(SingleImageShareView.this.getContext(), true);
                    SingleImageShareView.this.mPicCollageView.showQrCode();
                    return;
                }
                SingleImageShareView.this.setQrcodeSwitchData(SingleImageShareView.this.getContext(), false);
                SingleImageShareView.this.mPicCollageView.hideQrCode();
            }
        });
        findViewById(R.id.image_question).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertMsgDialog(context).title(R.string.share_qrcode_question_title).content((int) R.string.share_qrcode_question_content).positiveButtonText(R.string.confirm_okay).show();
            }
        });
        this.mShareButton = (Button) findViewById(R.id.btn_share_image);
        this.mShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.sendControlHit("union/union_share", UTHelper.SPM_SYSTEM_SHARE_CLICK);
                if (SingleImageShareView.this.mOnShareCollageListener != null) {
                    SingleImageShareView.this.mOnShareCollageListener.onClick(view);
                }
            }
        });
    }

    public void setOnSaveCollageListener(@Nullable View.OnClickListener onClickListener) {
        this.mOnSaveCollageListener = onClickListener;
    }

    public void setOnShareCollageListener(@Nullable View.OnClickListener onClickListener) {
        this.mOnShareCollageListener = onClickListener;
    }

    public void setQrCodeUrl(@NonNull String str) {
        this.mPicCollageView.generateQrCode(str);
    }

    public void setSavingState() {
        this.mSaveButton.setText(R.string.saving_images);
    }

    public void setTransientSuccessState() {
        this.mSaveButton.setText(R.string.save_success);
        postDelayed(new Runnable() {
            public void run() {
                SingleImageShareView.this.resetSaveState();
            }
        }, 1000);
    }

    public void resetSaveState() {
        this.mSaveButton.setText(R.string.save_images);
    }

    @MainThread
    public void saveCollageAsync(@NonNull String str, @Nullable final Result<Uri> result) {
        final String str2 = str + System.currentTimeMillis();
        final Bitmap saveAsImage = this.mPicCollageView.saveAsImage();
        AsyncTask.execute(new Runnable() {
            public void run() {
                final Uri saveImageSync = ShareUtils.saveImageSync(SingleImageShareView.this.getContext(), str2, saveAsImage);
                SingleImageShareView.this.post(new Runnable() {
                    public void run() {
                        if (result != null) {
                            result.onResult(saveImageSync);
                        }
                    }
                });
            }
        });
    }

    public Uri saveCollageUri() {
        Bitmap saveAsImage = this.mPicCollageView.saveAsImage();
        try {
            return ShareUtils.uriForShare(getContext(), ExternalPublicStorageSaver.getInstance().saveBitmapAs(saveAsImage, "share_image.png"));
        } catch (Exception e) {
            e.printStackTrace();
            if (saveAsImage == null || saveAsImage.isRecycled()) {
                return null;
            }
            saveAsImage.recycle();
            return null;
        }
    }

    public void setSharedInfo(ShareInfoResponse shareInfoResponse) {
        setImages(shareInfoResponse.getImages());
        CollageModel collageModel = new CollageModel();
        collageModel.setPrice(shareInfoResponse.getPrice()).setPriceAfterCoupon(shareInfoResponse.getPriceAfterCoupon()).setCouponAmount(shareInfoResponse.getCouponAmount()).setTitle(shareInfoResponse.getTitle()).setCollageImageUrls(this.mImagesAdapter.getSelectedImagesSortByIndex());
        this.mPicCollageView.setCollageInfo(collageModel);
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
