package com.alimama.union.app.share;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.IUTPage;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.alimama.union.app.share.flutter.SharePosterChannelDialog;
import com.alimama.union.app.share.flutter.SharePosterView;
import com.alimama.union.app.share.flutter.network.ShareInfoResponse2;
import com.alimama.union.app.views.AlertMsgDialog;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

public class SharePosterActivity extends BaseActivity implements IUTPage, PermissionInterface {
    private static final String ARG_SHARE_INFO = "shareInfo";
    private static final String ARG_SHARE_TEXT = "shareText";
    private static final String TAG = "SharePosterActivity";
    /* access modifiers changed from: private */
    public SharePosterView mSharePoster;
    private String mShareText;
    private ShareInfoResponse2 mSharedInfo;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission storagePermission;

    public void closePermissionRequest() {
    }

    public String getCurrentPageName() {
        return UTHelper.SharePosterPage.PAGE_NAME;
    }

    public String getCurrentSpmCnt() {
        return UTHelper.SharePosterPage.SPM_CNT;
    }

    public static void openPage(@NonNull String str, @NonNull ShareInfoResponse2 shareInfoResponse2) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SHARE_INFO, shareInfoResponse2);
        bundle.putString(ARG_SHARE_TEXT, str);
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SHARE_POSTER, bundle);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        setContentView((int) R.layout.activity_share_poster);
        this.mSharePoster = (SharePosterView) findViewById(R.id.share_single_image_container);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), true);
        PrivacyPermissionManager privacyPermissionManager = new PrivacyPermissionManager((Context) this, (PermissionInterface) this);
        this.mSharePoster.setOnSaveCollageListener(new View.OnClickListener(privacyPermissionManager) {
            private final /* synthetic */ PrivacyPermissionManager f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                SharePosterActivity.lambda$onCreate$6(SharePosterActivity.this, this.f$1, view);
            }
        });
        this.mSharePoster.setOnShareCollageListener(new View.OnClickListener(privacyPermissionManager) {
            private final /* synthetic */ PrivacyPermissionManager f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                SharePosterActivity.lambda$onCreate$8(SharePosterActivity.this, this.f$1, view);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        ShareInfoResponse2 shareInfoResponse2 = (ShareInfoResponse2) extras.getParcelable(ARG_SHARE_INFO);
        if (shareInfoResponse2 == null) {
            finish();
            return;
        }
        this.mSharedInfo = shareInfoResponse2;
        this.mShareText = extras.getString(ARG_SHARE_TEXT);
        this.mSharePoster.setSharedInfo(shareInfoResponse2);
    }

    public static /* synthetic */ void lambda$onCreate$6(SharePosterActivity sharePosterActivity, PrivacyPermissionManager privacyPermissionManager, View view) {
        CommonUtils.copyToClipboard(sharePosterActivity.mShareText);
        if (PrivacyUtil.hasWriteExternalStorage(sharePosterActivity)) {
            sharePosterActivity.shareByInAppPanel();
        } else {
            privacyPermissionManager.showReadWritePermissionDialog();
        }
    }

    public static /* synthetic */ void lambda$onCreate$8(SharePosterActivity sharePosterActivity, PrivacyPermissionManager privacyPermissionManager, View view) {
        CommonUtils.copyToClipboard(sharePosterActivity.mShareText);
        if (!PrivacyUtil.hasWriteExternalStorage(sharePosterActivity)) {
            privacyPermissionManager.showReadWritePermissionDialog();
        } else if (!ShareUtils.isFirstTimeUsingSysShare(sharePosterActivity.getApplicationContext())) {
            ToastUtil.showToast((Context) sharePosterActivity, (int) R.string.share_content_copied_for_system_share);
            sharePosterActivity.shareBySystem();
            BusinessMonitorLogger.Share.createPosterSharingSuccess(TAG, "第一次点击立即分享，创建分享成功");
        } else {
            ShareUtils.markUsedSystemShare(sharePosterActivity.getApplicationContext());
            AlertMsgDialog positiveButtonText = new AlertMsgDialog(view.getContext()).title(R.string.first_use_system_share_tips_title).content((int) R.string.first_use_system_share_tips_content).positiveButtonText(R.string.confirm_okay);
            positiveButtonText.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public final void onDismiss(DialogInterface dialogInterface) {
                    SharePosterActivity.lambda$null$7(SharePosterActivity.this, dialogInterface);
                }
            });
            positiveButtonText.show();
        }
    }

    public static /* synthetic */ void lambda$null$7(SharePosterActivity sharePosterActivity, DialogInterface dialogInterface) {
        ToastUtil.showToast((Context) sharePosterActivity, (int) R.string.share_content_copied_for_system_share);
        sharePosterActivity.shareBySystem();
        BusinessMonitorLogger.Share.createPosterSharingSuccess(TAG, "创建分享成功");
    }

    public void openPermissionRequest() {
        checkPermission();
    }

    private boolean checkPermission() {
        switch (this.storagePermission.checkPermission(this)) {
            case -1:
                if (!this.storagePermission.shouldShowPermissionRationale(this)) {
                    ToastUtil.showToast((Context) this, (int) R.string.permission_request_sdcard);
                }
                this.storagePermission.requestPermission(this);
                return false;
            case 0:
                return true;
            default:
                return true;
        }
    }

    private void shareBySystem() {
        this.mSharePoster.saveCollageAsync(new Result<Uri>() {
            public void onResult(Uri uri) {
                if (uri == null) {
                    ToastUtil.showToast((Context) SharePosterActivity.this, (int) R.string.share_failed);
                    BusinessMonitorLogger.Share.createPosterSharingFailed(SharePosterActivity.TAG);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(uri);
                SharePosterChannelDialog.immediateShare(SharePosterActivity.this, arrayList);
            }
        });
    }

    private void shareByInAppPanel() {
        this.mSharePoster.setSavingState();
        this.mSharePoster.saveCollageAsync(this.mSharedInfo.getShareUrl(), new Result<Uri>() {
            public void onResult(Uri uri) {
                if (uri != null) {
                    SharePosterActivity.this.mSharePoster.setTransientSuccessState();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(uri);
                    SharePosterChannelDialog.inAppShare(this, arrayList);
                    BusinessMonitorLogger.Share.savePosterSuccess(SharePosterActivity.TAG);
                    return;
                }
                SharePosterActivity.this.mSharePoster.resetSaveState();
                Log.w(SharePosterActivity.TAG, "onTaoCodeReady saving images failed");
                ToastUtil.showToast((Context) this, (int) R.string.images_save_failed);
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }
}
