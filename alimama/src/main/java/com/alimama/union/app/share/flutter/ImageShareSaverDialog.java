package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.share.SocialCamp;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.share.flutter.ImageShareSaverDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImageShareSaverDialog extends Dialog implements View.OnClickListener {
    private View mChannelContainer;
    private TextView mContentInfoTextView;
    private TextView mDownloadInfoTextView;
    private final ArrayList<Uri> mImageList = new ArrayList<>();
    private final UniversalImageDownloader mImageLoader = new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance());
    private final List<String> mRemoteImageUrls;
    private final ShareChannelClickHandler mShareChannelHandler;
    private boolean mSystemShare = false;

    public ImageShareSaverDialog(@NonNull Context context, List<String> list) {
        super(context, R.style.common_dialog_style);
        SocialCamp.getInstance().init(context);
        this.mRemoteImageUrls = new ArrayList();
        this.mRemoteImageUrls.addAll(list);
        this.mShareChannelHandler = new ShareChannelClickHandler(context);
    }

    public static void startDownloading(Activity activity, List<String> list) {
        new ImageShareSaverDialog(activity, list).show();
    }

    public static void immediateShareDownloading(Activity activity, List<String> list) {
        ImageShareSaverDialog imageShareSaverDialog = new ImageShareSaverDialog(activity, list);
        imageShareSaverDialog.mSystemShare = true;
        imageShareSaverDialog.show();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.new_share_panel_layout);
        this.mDownloadInfoTextView = (TextView) findViewById(R.id.tv_result);
        this.mContentInfoTextView = (TextView) findViewById(R.id.tv_panel_content);
        this.mChannelContainer = findViewById(R.id.ll_share_channels);
        View findViewById = findViewById(R.id.share_wechat_moments);
        findViewById(R.id.share_wechat).setOnClickListener(this);
        findViewById.setOnClickListener(this);
        findViewById(R.id.share_qq).setOnClickListener(this);
        findViewById(R.id.share_weibo).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        if (!this.mSystemShare || this.mRemoteImageUrls.size() > 1) {
            findViewById.setVisibility(8);
            findViewById(R.id.wechat_moments_spacer).setVisibility(8);
        }
        setCanceledOnTouchOutside(false);
        startDownloading();
    }

    private void startDownloading() {
        this.mChannelContainer.setVisibility(8);
        this.mDownloadInfoTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
        this.mDownloadInfoTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_text_color));
        this.mDownloadInfoTextView.setText(getContext().getString(R.string.save_images_progress, new Object[]{"0", String.valueOf(this.mRemoteImageUrls.size())}));
        this.mContentInfoTextView.setText(R.string.save_images_in_progress);
        this.mImageLoader.batchDownload(this.mRemoteImageUrls, new BatchImageDownloader(this));
    }

    private static class BatchImageDownloader implements IImageDownloader.BatchImageDownloadCallback {
        private final WeakReference<ImageShareSaverDialog> callbackHolder;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        BatchImageDownloader(ImageShareSaverDialog imageShareSaverDialog) {
            this.callbackHolder = new WeakReference<>(imageShareSaverDialog);
        }

        public void onSuccess(List<Uri> list) {
            if (this.callbackHolder.get() != null) {
                ((ImageShareSaverDialog) this.callbackHolder.get()).onImageDownloaded(list);
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
                            ((ImageShareSaverDialog) ImageShareSaverDialog.BatchImageDownloader.this.callbackHolder.get()).updateDownloadProgress(this.f$1.size());
                        }
                    });
                }
            } catch (Exception unused) {
            }
        }

        public void onFailure(String str) {
            if (this.callbackHolder.get() != null) {
                ((ImageShareSaverDialog) this.callbackHolder.get()).onImageDownloadError();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateDownloadProgress(int i) {
        this.mDownloadInfoTextView.setText(getContext().getString(R.string.save_images_progress, new Object[]{String.valueOf(i), String.valueOf(this.mRemoteImageUrls.size())}));
    }

    /* access modifiers changed from: private */
    public void onImageDownloaded(List<Uri> list) {
        this.mImageList.clear();
        this.mImageList.addAll(list);
        this.mDownloadInfoTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_saved_checkmark), (Drawable) null, (Drawable) null, (Drawable) null);
        this.mDownloadInfoTextView.setCompoundDrawablePadding(getContext().getResources().getDimensionPixelOffset(R.dimen.common_padding_3));
        this.mDownloadInfoTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.share_content_saved_text_color));
        this.mDownloadInfoTextView.setText(R.string.save_success);
        this.mContentInfoTextView.setText(R.string.share_save_images_panel_content);
        this.mChannelContainer.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void onImageDownloadError() {
        this.mDownloadInfoTextView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_error), (Drawable) null, (Drawable) null, (Drawable) null);
        this.mDownloadInfoTextView.setCompoundDrawablePadding(getContext().getResources().getDimensionPixelOffset(R.dimen.common_padding_3));
        this.mDownloadInfoTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.share_error_text_color));
        this.mDownloadInfoTextView.setText(R.string.save_failed);
        this.mContentInfoTextView.setText(R.string.retry_save_images);
        this.mChannelContainer.setVisibility(8);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_close) {
            if (id != R.id.share_wechat_moments) {
                switch (id) {
                    case R.id.share_wechat:
                        if (!this.mSystemShare) {
                            UTHelper.ShareFlutterPage.clickWechatImgShare();
                            this.mShareChannelHandler.shareToWechat();
                            break;
                        } else {
                            UTHelper.ShareFlutterPage.clickWechatSysShare();
                            this.mShareChannelHandler.immediateShareToWechat(this.mImageList);
                            break;
                        }
                    case R.id.share_qq:
                        if (!this.mSystemShare) {
                            UTHelper.ShareFlutterPage.clickQqImgShare();
                            this.mShareChannelHandler.shareToQQ();
                            break;
                        } else {
                            UTHelper.ShareFlutterPage.clickQqSysShare();
                            this.mShareChannelHandler.immediateShareToQq(this.mImageList);
                            break;
                        }
                    case R.id.share_weibo:
                        if (this.mSystemShare) {
                            UTHelper.ShareFlutterPage.clickWeiboSysShare();
                        } else {
                            UTHelper.ShareFlutterPage.clickWeiboImgShare();
                        }
                        this.mShareChannelHandler.shareToWeibo(CommonUtils.getClipboardContent(), this.mImageList);
                        break;
                }
            } else {
                UTHelper.ShareFlutterPage.clickWechatMoments();
                this.mShareChannelHandler.shareToWechatMoments(this.mImageList);
            }
        }
        dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
