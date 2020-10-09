package com.alimama.union.app.share;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.moon.view.SegmentedGroup;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.pagerouter.IUTPage;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.share.network.ShareInfoRequest;
import com.alimama.union.app.share.network.ShareInfoResponse;
import com.alimama.union.app.share.network.TaoCodeRequest;
import com.alimama.union.app.share.network.TaoCodeResponse;
import com.alimama.union.app.taotokenConvert.TaoCodeTransferPresenter;
import com.alimama.union.app.views.AlertMsgDialog;
import com.alimama.union.app.views.ISViewContainer;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

public class ShareActivity extends BaseActivity implements View.OnClickListener, RxMtopRequest.RxMtopResult<ShareInfoResponse>, IUTPage {
    private static final String FIRST_USE_SYSTEM_SHARE = "first_use_system_share";
    private static final String TAG = "ShareActivity";
    private static final String TAO_CODE_PLACEHOLDER = "{￥选择分享渠道后自动生成淘口令￥}";
    private static final String TAO_CODE_SHORT_LINK_PLACEHOLDER = "{选择分享渠道后自动生成链接}";
    private ShareCommissionInfoView mCommissionInfo;
    private Dialog mDialog;
    private RadioButton mImageShareButton;
    /* access modifiers changed from: private */
    public SingleImageShareView mImageShareView;
    /* access modifiers changed from: private */
    public String mLastEditedText;
    private final View.OnClickListener mOnSaveImageListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (ShareActivity.this.checkPermission()) {
                ShareActivity.this.saveAndShareImage(view, false);
            }
        }
    };
    private final View.OnClickListener mOnShareImageListener = new View.OnClickListener() {
        public void onClick(final View view) {
            if (ShareUtils.isFirstTimeUsingSysShare(view.getContext())) {
                ShareUtils.markUsedSystemShare(view.getContext());
                AlertMsgDialog positiveButtonText = new AlertMsgDialog(view.getContext()).title(R.string.first_use_system_share_tips_title).content((int) R.string.first_use_system_share_tips_content).positiveButtonText(R.string.confirm_okay);
                positiveButtonText.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        ShareActivity.this.saveAndShareImage(view, true);
                    }
                });
                positiveButtonText.show();
                return;
            }
            ShareActivity.this.saveAndShareImage(view, true);
        }
    };
    private ShareInfoRequest mShareInfoRequest;
    private ShareInfoResponse mSharedInfo;
    private LinearLayout mTabBarContainer;
    @Nullable
    private TaoCodeReqManager mTaoCodeReqManager;
    private RadioButton mTextShareButton;
    /* access modifiers changed from: private */
    public TextShareView mTextShareView;
    private ISViewContainer mViewStatusContainer;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission storagePermission;

    public String getCurrentPageName() {
        return "union/union_share";
    }

    public String getCurrentSpmCnt() {
        return "a21wq.11162860";
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        setContentView((int) R.layout.activity_share);
        this.mViewStatusContainer = (ISViewContainer) findViewById(R.id.view_status_container);
        this.mViewStatusContainer.setContainerRefreshListener(new ISViewContainer.IViewContainerRefreshListener() {
            public void refreshPage() {
                ShareActivity.this.fetchShareInfo();
            }
        });
        this.mCommissionInfo = (ShareCommissionInfoView) findViewById(R.id.rebate_info);
        this.mCommissionInfo.findViewById(R.id.tv_rules).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        this.mTabBarContainer = (LinearLayout) findViewById(R.id.lr_segmented_container);
        addMenuBar();
        setMenuBarClickListener();
        this.mTextShareView = (TextShareView) findViewById(R.id.share_text_container);
        this.mImageShareView = (SingleImageShareView) findViewById(R.id.share_single_image_container);
        this.mTextShareView.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
        this.mTextShareView.findViewById(R.id.ll_share_sina).setOnClickListener(this);
        this.mTextShareView.findViewById(R.id.ll_share_qq).setOnClickListener(this);
        this.mTextShareView.setOnCopyClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShareActivity.this.genAndCopySharedText("20140618.1.01010001.s101c6", new Result<String>() {
                    public void onResult(String str) {
                        ToastUtil.showToast((Context) ShareActivity.this, TextUtils.isEmpty(str) ? R.string.copy_failed : R.string.copy_success);
                    }
                });
            }
        });
        this.mImageShareView.setOnSaveCollageListener(this.mOnSaveImageListener);
        this.mImageShareView.setOnShareCollageListener(this.mOnShareImageListener);
        displayTextShare();
        fetchShareInfo();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_BACK);
            finish();
        } else if (id == R.id.btn_dialog_dismiss) {
            this.mDialog.dismiss();
        } else if (id != R.id.tv_rules) {
            switch (id) {
                case R.id.ll_share_wechat:
                    shareToWechat();
                    return;
                case R.id.ll_share_sina:
                    shareToWeibo();
                    return;
                case R.id.ll_share_qq:
                    shareToQq();
                    return;
                default:
                    return;
            }
        } else {
            UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_RULE_TIP);
            showShareRules();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mShareInfoRequest.setRxMtopResult((RxMtopRequest.RxMtopResult) null);
        if (this.mTaoCodeReqManager != null) {
            this.mTaoCodeReqManager.cleanup();
        }
        super.onDestroy();
    }

    public void result(RxMtopResponse<ShareInfoResponse> rxMtopResponse) {
        if (!rxMtopResponse.isReqSuccess || rxMtopResponse.result == null) {
            this.mViewStatusContainer.onDataLoadError();
            return;
        }
        this.mViewStatusContainer.onDataLoaded();
        this.mSharedInfo = (ShareInfoResponse) rxMtopResponse.result;
        this.mCommissionInfo.setCommissionInfo(this.mSharedInfo.getCommissionInfo(this));
        this.mTextShareView.setSharedText(this.mSharedInfo.getSharedText());
        this.mImageShareView.setSharedInfo(this.mSharedInfo);
        this.mLastEditedText = this.mTextShareView.getSharedEditText().toString();
        setSegmentControlEnabled(true);
        this.mTaoCodeReqManager = new TaoCodeReqManager(this.mSharedInfo.getShareUrl());
    }

    @Nullable
    private String getShareUrl() {
        if (this.mSharedInfo == null) {
            return null;
        }
        return this.mSharedInfo.getShareUrl();
    }

    /* access modifiers changed from: private */
    public void fetchShareInfo() {
        String str = "";
        if (!(getIntent() == null || getIntent().getData() == null)) {
            str = getIntent().getData().toString();
        }
        if (TextUtils.isEmpty(str)) {
            ToastUtil.showToast((Context) this, (int) R.string.server_error_retry_msg);
            return;
        }
        this.mShareInfoRequest = new ShareInfoRequest(str);
        this.mShareInfoRequest.sendRequest(this);
    }

    private void setSegmentControlEnabled(boolean z) {
        this.mTextShareButton.setEnabled(z);
        this.mImageShareButton.setEnabled(z);
    }

    /* access modifiers changed from: private */
    public void displayTextShare() {
        this.mTextShareView.setVisibility(0);
        this.mImageShareView.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void displaySingleImageShare() {
        if (this.mImageShareView.getVisibility() != 0 && this.mSharedInfo != null && this.mTaoCodeReqManager != null) {
            String shareUrl = getShareUrl();
            if (this.mSharedInfo.getImages().isEmpty() || TextUtils.isEmpty(shareUrl)) {
                ToastUtil.showToast((Context) this, (int) R.string.server_error_retry_msg);
                return;
            }
            this.mTextShareView.setVisibility(8);
            this.mImageShareView.setVisibility(0);
            Uri.Builder buildUpon = Uri.parse(shareUrl).buildUpon();
            buildUpon.appendQueryParameter(UTHelper.SCM_URI_PARAMETER_KEY, "20140618.1.01010001.s101c6");
            this.mTaoCodeReqManager.genQrCodeUrl(new TaoCodeRequest.ReqParam(buildUpon.toString(), this.mSharedInfo.getImages().get(0), this.mSharedInfo.getTitle()), new Result<String>() {
                public void onResult(String str) {
                    ShareActivity.this.mImageShareView.setQrCodeUrl(str);
                }
            });
        }
    }

    private void addMenuBar() {
        SegmentedGroup segmentedGroup = new SegmentedGroup(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, LocalDisplay.dp2px(29.0f));
        segmentedGroup.setOrientation(0);
        this.mTabBarContainer.addView(segmentedGroup, layoutParams);
        this.mTextShareButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_item, (ViewGroup) null);
        this.mTextShareButton.setText(getResources().getString(R.string.share_goods_bar_text));
        this.mTextShareButton.setPadding((int) getResources().getDimension(R.dimen.share_padding), 0, (int) getResources().getDimension(R.dimen.share_padding), 0);
        this.mImageShareButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_item, (ViewGroup) null);
        this.mImageShareButton.setText(getResources().getString(R.string.share_goods_bar_image));
        this.mImageShareButton.setPadding((int) getResources().getDimension(R.dimen.share_padding), 0, (int) getResources().getDimension(R.dimen.share_padding), 0);
        segmentedGroup.addView(this.mTextShareButton);
        segmentedGroup.addView(this.mImageShareButton);
        segmentedGroup.updateBackground();
        this.mTextShareButton.setChecked(true);
        setSegmentControlEnabled(false);
    }

    private void setMenuBarClickListener() {
        this.mTextShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_CODE);
                ShareActivity.this.displayTextShare();
            }
        });
        this.mImageShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_SHARE_IMG);
                ShareActivity.this.displaySingleImageShare();
            }
        });
    }

    private void showShareRules() {
        this.mDialog = new Dialog(this, R.style.common_dialog_style);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share_goods_rule, (ViewGroup) null);
        this.mDialog.setContentView(inflate);
        inflate.findViewById(R.id.btn_dialog_dismiss).setOnClickListener(this);
        this.mDialog.show();
    }

    /* access modifiers changed from: private */
    public void genAndCopySharedText(String str, @Nullable final Result<String> result) {
        TaoCodeRequest.ReqParam genTaoCodeReqParam;
        if (this.mTaoCodeReqManager != null && (genTaoCodeReqParam = genTaoCodeReqParam(str)) != null) {
            this.mTaoCodeReqManager.getTaoCode(genTaoCodeReqParam, new Result<TaoCodeResponse>() {
                public void onResult(@NonNull TaoCodeResponse taoCodeResponse) {
                    String charSequence = ShareActivity.this.mTextShareView.getSharedEditText().toString();
                    String replace = charSequence.replace(ShareActivity.TAO_CODE_SHORT_LINK_PLACEHOLDER, taoCodeResponse.getTinyLink()).replace(ShareActivity.TAO_CODE_PLACEHOLDER, taoCodeResponse.getTaoCode());
                    if (!charSequence.equals(ShareActivity.this.mLastEditedText)) {
                        UTHelper.sendCustomUT("union/union_share", UTHelper.SPM_CUSTOM_SHARE_MSG_EDIT);
                        String unused = ShareActivity.this.mLastEditedText = charSequence;
                    }
                    boolean access$900 = ShareActivity.this.copyToClipboard(replace);
                    if (result != null) {
                        Result result = result;
                        if (!access$900) {
                            replace = null;
                        }
                        result.onResult(replace);
                    }
                }

                /* access modifiers changed from: package-private */
                public void onFailure(@Nullable String str) {
                    super.onFailure(str);
                    ShareActivity.this.handleTaoCodeFailure(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleTaoCodeFailure(@Nullable String str) {
        if (!isFinishing()) {
            int i = R.string.tao_code_generate_failed_msg_content;
            if (str != null && str.contains(TaoCodeReqManager.ERROR_CODE_TITLE_ILLEGAL)) {
                i = R.string.tao_code_failure_illegal_title;
            }
            new AlertMsgDialog(this).title(R.string.tao_code_generate_failed_msg_title).content(i).positiveButtonText(R.string.confirm_okay).show();
        }
    }

    /* access modifiers changed from: private */
    public boolean copyToClipboard(@Nullable CharSequence charSequence) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
        if (clipboardManager == null) {
            Log.w(TAG, "copyToClipboard couldn't get ClipboardManager service");
            return false;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(TaoCodeTransferPresenter.TAO_CODE_CLIP_DATA_LABEL, charSequence));
        return true;
    }

    private TaoCodeRequest.ReqParam genTaoCodeReqParam(String str) {
        String shareUrl = getShareUrl();
        if (this.mSharedInfo == null || this.mSharedInfo.getImages().isEmpty() || TextUtils.isEmpty(shareUrl)) {
            ToastUtil.showToast((Context) this, (int) R.string.server_error_retry_msg);
            return null;
        }
        String charSequence = this.mTextShareView.getSharedEditText().toString();
        if (TextUtils.isEmpty(charSequence)) {
            this.mTextShareView.setSharedText(this.mSharedInfo.getSharedText());
        } else if (!charSequence.contains(TAO_CODE_SHORT_LINK_PLACEHOLDER) && !charSequence.contains(TAO_CODE_PLACEHOLDER)) {
            new AlertMsgDialog(this).title(R.string.tao_code_generate_failed_msg_title).content((int) R.string.tao_code_failure_editing_placeholder).positiveButtonText(R.string.confirm_okay).show();
            return null;
        }
        Uri.Builder buildUpon = Uri.parse(shareUrl).buildUpon();
        buildUpon.appendQueryParameter(UTHelper.SCM_URI_PARAMETER_KEY, str);
        return new TaoCodeRequest.ReqParam(buildUpon.toString(), this.mSharedInfo.getImages().get(0), this.mSharedInfo.getTitle());
    }

    private void shareToWechat() {
        UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_TEXT_SHARE_TO_WECHAT);
        genAndCopySharedText(UTHelper.SCM_SHARE_TO_WECHAT, new Result<String>() {
            public void onResult(String str) {
                ShareActivity shareActivity = ShareActivity.this;
                if (!TextUtils.isEmpty(str) && !ShareUtils.openWechat(shareActivity)) {
                    ToastUtil.showToast((Context) shareActivity, (int) R.string.wechat_not_installed);
                }
            }
        });
    }

    private void shareToQq() {
        UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_TEXT_SHARE_TO_QQ);
        genAndCopySharedText(UTHelper.SCM_SHARE_TO_QQ, new Result<String>() {
            public void onResult(String str) {
                ShareActivity shareActivity = ShareActivity.this;
                if (!TextUtils.isEmpty(str) && !ShareUtils.openQq(shareActivity)) {
                    ToastUtil.showToast((Context) shareActivity, (int) R.string.qq_not_installed);
                }
            }
        });
    }

    private void shareToWeibo() {
        UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_TEXT_SHARE_TO_WEIBO);
        genAndCopySharedText(UTHelper.SCM_SHARE_TO_WEIBO, new Result<String>() {
            public void onResult(String str) {
                if (!TextUtils.isEmpty(str)) {
                    ShareActivity shareActivity = ShareActivity.this;
                    if (!ShareUtils.shareWeibo(shareActivity, str, (ArrayList<Uri>) null)) {
                        ToastUtil.showToast((Context) shareActivity, (int) R.string.weibo_not_installed);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean checkPermission() {
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

    /* access modifiers changed from: private */
    public void saveAndShareImage(View view, final boolean z) {
        TaoCodeRequest.ReqParam genTaoCodeReqParam;
        if (this.mTaoCodeReqManager != null && (genTaoCodeReqParam = genTaoCodeReqParam("20140618.1.01010001.s101c6")) != null) {
            if (!z) {
                this.mImageShareView.setSavingState();
            }
            final Context context = view.getContext();
            this.mTaoCodeReqManager.getTaoCode(genTaoCodeReqParam, new Result<TaoCodeResponse>() {
                public void onResult(@NonNull TaoCodeResponse taoCodeResponse) {
                    String charSequence = ShareActivity.this.mTextShareView.getSharedEditText().toString();
                    String replace = charSequence.replace(ShareActivity.TAO_CODE_SHORT_LINK_PLACEHOLDER, taoCodeResponse.getTinyLink()).replace(ShareActivity.TAO_CODE_PLACEHOLDER, taoCodeResponse.getTaoCode());
                    boolean unused = ShareActivity.this.copyToClipboard(replace);
                    if (!charSequence.equals(ShareActivity.this.mLastEditedText)) {
                        UTHelper.sendCustomUT("union/union_share", UTHelper.SPM_CUSTOM_SHARE_MSG_EDIT);
                        String unused2 = ShareActivity.this.mLastEditedText = charSequence;
                    }
                    ShareActivity.this.saveImages(replace, z, context);
                }

                /* access modifiers changed from: package-private */
                public void onFailure(@Nullable String str) {
                    super.onFailure(str);
                    ShareActivity.this.handleTaoCodeFailure(str);
                    ShareActivity.this.saveImages((String) null, z, context);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void saveImages(@Nullable final String str, final boolean z, final Context context) {
        if (!z) {
            this.mImageShareView.saveCollageAsync(this.mTaoCodeReqManager.getSharedImageKey(), new Result<Uri>() {
                public void onResult(Uri uri) {
                    if (uri != null) {
                        ShareActivity.this.mImageShareView.setTransientSuccessState();
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(uri);
                        if (!TextUtils.isEmpty(str) && !ShareActivity.this.isFinishing()) {
                            new NativeSharePanelDialog(context, str, arrayList, z).show();
                            return;
                        }
                        return;
                    }
                    ShareActivity.this.mImageShareView.resetSaveState();
                    Log.w(ShareActivity.TAG, "onTaoCodeReady saving images failed");
                    ToastUtil.showToast(context, (int) R.string.images_save_failed);
                }
            });
            return;
        }
        Uri saveCollageUri = this.mImageShareView.saveCollageUri();
        if (saveCollageUri == null) {
            ToastUtil.showToast(context, (int) R.string.share_failed);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(saveCollageUri);
        if (!TextUtils.isEmpty(str) && !isFinishing()) {
            new NativeSharePanelDialog(context, str, arrayList, z).show();
        }
    }
}
