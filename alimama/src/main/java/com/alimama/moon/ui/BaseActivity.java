package com.alimama.moon.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.eventbus.LoginEvent;
import com.alimama.moon.features.search.SearchInputActivity;
import com.alimama.moon.ui.dialog.AvatarDialog;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.LoginManager;
import com.alimama.union.app.aalogin.view.LoginChooserActivity;
import com.alimama.union.app.contact.model.ContactEvent;
import com.alimama.union.app.infrastructure.image.capture.Event;
import com.alimama.union.app.network.response.TaoCodeItemInfo;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.personalCenter.view.RestrictedUseDialog;
import com.alimama.union.app.taotokenConvert.TaoCodeTransferEvent;
import com.alimama.union.app.taotokenConvert.view.TransformTaoCodeDialog;
import com.alimama.union.app.views.AlertMsgDialog;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseActivity extends PageRouterActivity implements ISubscriber {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BaseActivity.class);
    @Inject
    IEventBus eventBus;
    @Inject
    public ILogin login;
    @Nullable
    private Dialog mTaoCodeDialog;
    private String pvid;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        App.getAppComponent().inject(this);
        this.pvid = UnionLensUtil.generatePrepvid();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
        registerEventBus();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        UnionLensUtil.updatePvid(this.pvid);
        App.sApplication.getTaoCodeTransferPresenter().onPageResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    /* access modifiers changed from: package-private */
    public void registerEventBus() {
        if (!this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.register(this);
            } catch (EventBusException e) {
                logger.error("EventBus register exception: {}", (Object) e.getMessage());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterEventBus() {
        if (this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.unregister(this);
            } catch (EventBusException e) {
                logger.error("EventBus unregister exception: {}", (Object) e.getMessage());
            }
        }
    }

    public void login() {
        LoginManager.getInstance().login(this, (LoginManager.LoginCallbackListener) null);
    }

    public void showTaoCodeTransferDialog(String str, TaoCodeItemInfo taoCodeItemInfo) {
        dismissTaoCodeDialogIfShowing();
        TransformTaoCodeDialog transformTaoCodeDialog = new TransformTaoCodeDialog(this);
        transformTaoCodeDialog.show(str, taoCodeItemInfo);
        this.mTaoCodeDialog = transformTaoCodeDialog;
    }

    public void showWorshipInviteAvatarDialog(TaoCodeItemInfo taoCodeItemInfo) {
        if (taoCodeItemInfo != null) {
            AvatarDialog.showAvatarDialog(this, taoCodeItemInfo.getTitle(), taoCodeItemInfo.getMsg(), taoCodeItemInfo.getPictUrl(), taoCodeItemInfo.getRawUrl());
        }
    }

    public void showTaoCodeNotSupportedDialog(String str) {
        dismissTaoCodeDialogIfShowing();
        this.mTaoCodeDialog = new AlertMsgDialog(this).title(R.string.tao_code_not_supported_dialog_title).content(str).positiveButtonText(R.string.tao_code_not_supported_dialog_positive_button).negativeButtonText(R.string.close).canceledOnClickOutside(false).clickListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_RULES_CONTROL_NAME);
                    dialogInterface.dismiss();
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getTaoCodeTransferFaqUrl());
                }
                if (i == -2) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_CLOSE_CONTROL_NAME);
                    dialogInterface.dismiss();
                }
            }
        });
        this.mTaoCodeDialog.show();
    }

    public void showTaoCodeItemNotFoundDialog(final String str, String str2) {
        dismissTaoCodeDialogIfShowing();
        this.mTaoCodeDialog = new AlertMsgDialog(this).title(R.string.tao_code_item_not_found_dialog_title).content(str2).positiveButtonText(R.string.confirm_okay).negativeButtonText(R.string.tao_code_item_not_found_dialog_negative_button).canceledOnClickOutside(false).clickListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_CLOSE_CONTROL_NAME);
                    dialogInterface.dismiss();
                }
                if (i == -2) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_SIMILAR_CONTROL_NAME);
                    dialogInterface.dismiss();
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(SpmProcessor.appendSpm(WebPageIntentGenerator.getSimilarItemPage(str), UTHelper.TaoCodeDialog.SPM_VAL_TO_SIMILAR));
                }
            }
        });
        this.mTaoCodeDialog.show();
    }

    private void showTaoCodeItemTitleDetectedDialog(final String str) {
        dismissTaoCodeDialogIfShowing();
        this.mTaoCodeDialog = new AlertMsgDialog(this).title(R.string.tao_code_dialog_detect_item_title).content(str).positiveButtonText(R.string.tao_code_dialog_search_by_title).negativeButtonText(R.string.ignore).canceledOnClickOutside(false).clickListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_SEARCH_TITLE_CONTROL_NAME);
                    dialogInterface.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString(SearchInputActivity.SEARCH_RESULT_KEYWORD, str);
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SEARCH_RESULTS, bundle);
                } else if (i == -2) {
                    UTHelper.sendControlHit(UTHelper.UT_TAO_CODE_DIALOG_PAGE_NAME, TransformTaoCodeDialog.SPM_CLOSE_CONTROL_NAME);
                    dialogInterface.dismiss();
                }
            }
        });
        this.mTaoCodeDialog.show();
    }

    @Subscribe
    public void onTaoCodeTransferEvent(TaoCodeTransferEvent taoCodeTransferEvent) {
        switch (taoCodeTransferEvent.getTransferResult()) {
            case TAO_CODE_ITEM_TITLE:
                showTaoCodeItemTitleDetectedDialog(taoCodeTransferEvent.getClipboardContent());
                return;
            case TAO_CODE_PERMISSION_DENIED:
                showPermissionDenyDialog(getResources().getString(R.string.taotoken_convert_permission_deny));
                return;
            case TAO_CODE_NOT_SUPPORTED:
                showTaoCodeNotSupportedDialog(taoCodeTransferEvent.getErrorMessage());
                return;
            case TAO_CODE_ITEM_NOT_FOUND:
                showTaoCodeItemNotFoundDialog(taoCodeTransferEvent.getItemRawUrl(), taoCodeTransferEvent.getErrorMessage());
                return;
            case TAO_CODE_ITEM:
                showTaoCodeTransferDialog(taoCodeTransferEvent.getClipboardContent(), taoCodeTransferEvent.getItemInfo());
                return;
            case TAO_CODE_WORSHIP_INVITE:
                showWorshipInviteAvatarDialog(taoCodeTransferEvent.getItemInfo());
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutSuccess(LoginEvent.LogoutEvent logoutEvent) {
        if (!CommonUtils.getTopActivity(this).contains("LoginChooserActivity")) {
            showLoginChooserPage();
        }
    }

    private void showLoginChooserPage() {
        Intent intent = new Intent(this, LoginChooserActivity.class);
        intent.addFlags(32768);
        startActivity(intent);
        finish();
    }

    public void showPermissionDenyDialog(String str) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag("restrictedUseDialog") == null) {
            beginTransaction.addToBackStack((String) null);
            RestrictedUseDialog restrictedUseDialog = new RestrictedUseDialog();
            Bundle bundle = new Bundle();
            bundle.putString(RestrictedUseDialog.EXTRA_MSG_BODY, str);
            restrictedUseDialog.setArguments(bundle);
            try {
                restrictedUseDialog.show(beginTransaction, "restrictedUseDialog");
            } catch (IllegalStateException e) {
                logger.error("restrictedUseDialog show error: {}", (Object) e.getMessage());
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        switch (i) {
            case 1:
                if (permissionGranted(iArr)) {
                    this.eventBus.post(new ContactEvent.PermissionGranted());
                    return;
                } else {
                    this.eventBus.post(new ContactEvent.PermissionDenied());
                    return;
                }
            case 2:
                if (permissionGranted(iArr)) {
                    this.eventBus.post(new Event.WriteExternalStoragePermissionGranted());
                    return;
                } else {
                    this.eventBus.post(new Event.WriteExternalStoragePermissionDenied());
                    return;
                }
            default:
                return;
        }
    }

    private void dismissTaoCodeDialogIfShowing() {
        if (this.mTaoCodeDialog != null && this.mTaoCodeDialog.isShowing()) {
            this.mTaoCodeDialog.dismiss();
        }
    }

    private boolean permissionGranted(int[] iArr) {
        return iArr.length > 0 && iArr[0] == 0;
    }
}
