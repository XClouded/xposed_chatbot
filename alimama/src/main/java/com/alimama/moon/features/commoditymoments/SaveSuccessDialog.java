package com.alimama.moon.features.commoditymoments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.alimama.moon.features.commoditymoments.PasteTaoCodeInCommentsDialog;
import com.alimama.moon.features.commoditymoments.SaveSuccessDialog;
import com.alimama.moon.usertrack.DialogUTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.windvane.jsbridge.model.ShareModel;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.alimama.union.app.share.flutter.ShareChannelClickHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SaveSuccessDialog extends Dialog implements View.OnClickListener, PermissionInterface {
    private Context mContext;
    private RelativeLayout mCopyShareInfoRl;
    private TextView mDivideLineTv;
    private LinearLayout mGoToWeChatMomentsLl;
    private final UniversalImageDownloader mImageLoader = new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance());
    private final StoragePermissionValidator mPermissionValidator;
    private final List<String> mRemoteImageUrls;
    private TextView mSaveSuccessTextTv;
    private final ShareChannelClickHandler mShareChannelHandler;
    private ShareModel mShareModel;
    private TextView mTipsTv;
    private PrivacyPermissionManager privacyPermissionManager;

    public void closePermissionRequest() {
    }

    public static void showSaveTaoCodeAndImgSuccessDialog(Context context, ShareModel shareModel) {
        new SaveSuccessDialog(context, shareModel);
    }

    public void openPermissionRequest() {
        this.mPermissionValidator.checkRequiredPermission((Activity) this.mContext);
    }

    private SaveSuccessDialog(Context context, ShareModel shareModel) {
        super(context, R.style.common_dialog_style);
        this.mContext = context;
        this.mShareModel = shareModel;
        this.mRemoteImageUrls = new ArrayList();
        this.mRemoteImageUrls.addAll(this.mShareModel.getImgList());
        this.mShareChannelHandler = new ShareChannelClickHandler(context);
        this.mPermissionValidator = new StoragePermissionValidator();
        this.privacyPermissionManager = new PrivacyPermissionManager(context, (PermissionInterface) this);
        if (!PrivacyUtil.hasWriteExternalStorage(context)) {
            this.privacyPermissionManager.showReadWritePermissionDialog();
        } else {
            show();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.save_success_dialog);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.go_to_wechat_comments_ll).setOnClickListener(this);
        this.mCopyShareInfoRl = (RelativeLayout) findViewById(R.id.copy_taocode_success_rl);
        this.mGoToWeChatMomentsLl = (LinearLayout) findViewById(R.id.go_to_wechat_comments_ll);
        this.mTipsTv = (TextView) findViewById(R.id.tips_tv);
        this.mDivideLineTv = (TextView) findViewById(R.id.divide_line);
        this.mSaveSuccessTextTv = (TextView) findViewById(R.id.save_success_text);
        copyShareInfoToClipboard();
        startDownloadingImage();
    }

    private void copyShareInfoToClipboard() {
        if (this.mShareModel != null) {
            CommonUtils.copyToClipboard(this.mShareModel.getShareInfo());
        }
    }

    private void startDownloadingImage() {
        beforeImageDownloaded();
        this.mImageLoader.batchDownload(this.mRemoteImageUrls, new BatchImageDownloader(this));
    }

    private void beforeImageDownloaded() {
        this.mGoToWeChatMomentsLl.setVisibility(8);
        this.mDivideLineTv.setVisibility(0);
        this.mCopyShareInfoRl.setVisibility(0);
        this.mSaveSuccessTextTv.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
        this.mSaveSuccessTextTv.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_text_color));
        this.mSaveSuccessTextTv.setText(getContext().getString(R.string.save_images_progress, new Object[]{"0", String.valueOf(this.mRemoteImageUrls.size())}));
        this.mTipsTv.setText(R.string.save_images_in_progress);
    }

    private void afterImageDownloaded() {
        this.mCopyShareInfoRl.setVisibility(0);
        this.mGoToWeChatMomentsLl.setVisibility(0);
        this.mDivideLineTv.setVisibility(8);
        this.mSaveSuccessTextTv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_saved_checkmark), (Drawable) null, (Drawable) null, (Drawable) null);
        this.mSaveSuccessTextTv.setTextColor(ContextCompat.getColor(getContext(), R.color.share_content_saved_text_color));
        this.mSaveSuccessTextTv.setText(R.string.save_success);
        this.mTipsTv.setText(R.string.share_to_wechat_tips);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_close) {
            DialogUTHelper.clickDialog(DialogUTHelper.CLOSE_SHARE_WECHAT_COMMENTS_DIALOG);
        } else if (id == R.id.go_to_wechat_comments_ll) {
            DialogUTHelper.clickDialog(DialogUTHelper.SHARE_WECHAT_COMMENTS_CONTROL_NAME);
            if (this.mShareChannelHandler.shareToWechat()) {
                PasteTaoCodeInCommentsDialog.showPasteTaoCodeInCommentsDialog(this.mContext, PasteTaoCodeInCommentsDialog.DialogType.Jump_To_WeChat_Back, this.mShareModel.getTaoCodeStr());
            }
        }
        dismiss();
    }

    /* access modifiers changed from: private */
    public void onImageDownloaded(List<Uri> list) {
        afterImageDownloaded();
    }

    /* access modifiers changed from: private */
    public void onImageDownloadError() {
        this.mSaveSuccessTextTv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_error), (Drawable) null, (Drawable) null, (Drawable) null);
        this.mSaveSuccessTextTv.setCompoundDrawablePadding(getContext().getResources().getDimensionPixelOffset(R.dimen.common_padding_3));
        this.mSaveSuccessTextTv.setTextColor(ContextCompat.getColor(getContext(), R.color.share_error_text_color));
        this.mSaveSuccessTextTv.setText(R.string.save_failed);
        this.mTipsTv.setText(R.string.retry_save_images);
    }

    /* access modifiers changed from: private */
    public void updateDownloadProgress(int i) {
        this.mSaveSuccessTextTv.setText(getContext().getString(R.string.save_images_progress, new Object[]{String.valueOf(i), String.valueOf(this.mRemoteImageUrls.size())}));
    }

    private static class BatchImageDownloader implements IImageDownloader.BatchImageDownloadCallback {
        private final WeakReference<SaveSuccessDialog> callbackHolder;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        BatchImageDownloader(SaveSuccessDialog saveSuccessDialog) {
            this.callbackHolder = new WeakReference<>(saveSuccessDialog);
        }

        public void onSuccess(List<Uri> list) {
            if (this.callbackHolder.get() != null) {
                ((SaveSuccessDialog) this.callbackHolder.get()).onImageDownloaded(list);
            }
        }

        public void onProgressUpdate(List<Uri> list, List<String> list2) {
            try {
                if (this.callbackHolder.get() != null) {
                    this.mainHandler.post(new Runnable(list) {
                        private final /* synthetic */ List f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void run() {
                            ((SaveSuccessDialog) SaveSuccessDialog.BatchImageDownloader.this.callbackHolder.get()).updateDownloadProgress(this.f$1.size());
                        }
                    });
                }
            } catch (Exception unused) {
            }
        }

        public void onFailure(String str) {
            if (this.callbackHolder.get() != null) {
                ((SaveSuccessDialog) this.callbackHolder.get()).onImageDownloadError();
            }
        }
    }
}
