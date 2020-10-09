package com.alimama.union.app.taotokenConvert;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.moon.utils.PageRouterUtil;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.network.response.TaoCodeItemInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import org.greenrobot.eventbus.EventBus;

public class TaoCodeTransferPresenter {
    private static final String ERROR_CODE_ITEM_NOT_FOUND = "TAOTOKEN_ITEM_NOT_FOUND";
    private static final String ERROR_CODE_MEMBER_PERMISSION_DENY = "MEMBER_PERMISSION_DENY";
    private static final String ERROR_CODE_NOT_SUPPORTED = "TAOTOKEN_NOT_SUPPORT";
    private static final String ERROR_CODE_WORSHIP_INVITE_TAOTOKEN = "WORSHIP_INVITE_TAOTOKEN";
    private static final String ERROR_TAO_CODE_NOT_EXIST = "TAOTOKEN_TEXT_NOT_TAOTOKEN";
    public static final String ORDER_CHECK_CLIP_DATA_LABEL = "UNION_APP_NATIVE_ORDER_CHECK";
    public static final String TAO_CODE_CLIP_DATA_LABEL = "UNION_APP_NATIVE_TAO_CODE_CLIP";
    private static final String WEEX_CLIP_DATA_LABEL = "WEEX_CLIP_KEY_MAIN";
    private static final String WINDVANE_CLIP_DATA_LABEL = "WINDVANE_CLIP_KEY_MAIN";
    /* access modifiers changed from: private */
    public String Tag = TaoCodeTransferPresenter.class.getSimpleName();
    /* access modifiers changed from: private */
    public final RxMtopRequest.RxMtopResult<TaoCodeItemInfo> itemInfoListener = new RxMtopRequest.RxMtopResult<TaoCodeItemInfo>() {
        public void result(RxMtopResponse<TaoCodeItemInfo> rxMtopResponse) {
            if (rxMtopResponse.isReqSuccess) {
                TaoCodeItemInfo taoCodeItemInfo = (TaoCodeItemInfo) rxMtopResponse.result;
                if (taoCodeItemInfo == null || TextUtils.isEmpty(taoCodeItemInfo.getRawUrl())) {
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(TaoCodeTransferPresenter.this.Tag, String.valueOf(-2001), "-2001, 服务端异常，返回的数据为空");
                    EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                    return;
                }
                BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferSuccess(TaoCodeTransferPresenter.this.Tag, "转链成功");
                EventBus.getDefault().post(TaoCodeTransferEvent.success(TaoCodeTransferPresenter.this.mClipboardContent, taoCodeItemInfo));
                EventBus.getDefault().post(TaoCodeDialogEvent.dialogShow());
                TaoCodeTransferPresenter.this.cleanClipboard();
                return;
            }
            String str = rxMtopResponse.retCode;
            char c = 65535;
            switch (str.hashCode()) {
                case -1364713902:
                    if (str.equals(TaoCodeTransferPresenter.ERROR_CODE_ITEM_NOT_FOUND)) {
                        c = 2;
                        break;
                    }
                    break;
                case -703518003:
                    if (str.equals(TaoCodeTransferPresenter.ERROR_TAO_CODE_NOT_EXIST)) {
                        c = 3;
                        break;
                    }
                    break;
                case -141089065:
                    if (str.equals(TaoCodeTransferPresenter.ERROR_CODE_MEMBER_PERMISSION_DENY)) {
                        c = 0;
                        break;
                    }
                    break;
                case 927614852:
                    if (str.equals(TaoCodeTransferPresenter.ERROR_CODE_WORSHIP_INVITE_TAOTOKEN)) {
                        c = 4;
                        break;
                    }
                    break;
                case 1621397883:
                    if (str.equals(TaoCodeTransferPresenter.ERROR_CODE_NOT_SUPPORTED)) {
                        c = 1;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    String access$200 = TaoCodeTransferPresenter.this.Tag;
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(access$200, str, str + ", 权限不足");
                    EventBus.getDefault().post(TaoCodeTransferEvent.permissionDenied());
                    EventBus.getDefault().post(TaoCodeDialogEvent.dialogShow());
                    TaoCodeTransferPresenter.this.cleanClipboard();
                    return;
                case 1:
                    String access$2002 = TaoCodeTransferPresenter.this.Tag;
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(access$2002, str, str + ", url暂不支持");
                    EventBus.getDefault().post(TaoCodeTransferEvent.taoCodeNotSupported(rxMtopResponse.retMsg));
                    EventBus.getDefault().post(TaoCodeDialogEvent.dialogShow());
                    TaoCodeTransferPresenter.this.cleanClipboard();
                    return;
                case 2:
                    String access$2003 = TaoCodeTransferPresenter.this.Tag;
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(access$2003, str, str + ", 商品下架或未加入淘宝客");
                    TaoCodeItemInfo taoCodeItemInfo2 = (TaoCodeItemInfo) rxMtopResponse.result;
                    if (taoCodeItemInfo2 != null && !TextUtils.isEmpty(taoCodeItemInfo2.getRawUrl())) {
                        EventBus.getDefault().post(TaoCodeTransferEvent.itemNotFound(taoCodeItemInfo2.getRawUrl(), rxMtopResponse.retMsg));
                        EventBus.getDefault().post(TaoCodeDialogEvent.dialogShow());
                        TaoCodeTransferPresenter.this.cleanClipboard();
                        return;
                    }
                    return;
                case 3:
                    String access$2004 = TaoCodeTransferPresenter.this.Tag;
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(access$2004, str, str + ", 不是淘口令, 不是商品链接");
                    new SearchByTitleRequest(TaoCodeTransferPresenter.this.mClipboardContent).sendRequest(TaoCodeTransferPresenter.this.searchByTitleListener);
                    return;
                case 4:
                    String access$2005 = TaoCodeTransferPresenter.this.Tag;
                    BusinessMonitorLogger.TaoCodeTransfer.taoCodeTransferFailed(access$2005, str, str + ", 师徒邀请淘口令");
                    EventBus.getDefault().post(TaoCodeTransferEvent.worshipInviteTaoCode((TaoCodeItemInfo) rxMtopResponse.result));
                    TaoCodeTransferPresenter.this.cleanClipboard();
                    return;
                default:
                    EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mAppContext;
    private boolean mAppJustForegrounded;
    /* access modifiers changed from: private */
    @Nullable
    public String mClipboardContent;
    /* access modifiers changed from: private */
    public final RxMtopRequest.RxMtopResult<Boolean> searchByTitleListener = new RxMtopRequest.RxMtopResult<Boolean>() {
        public void result(RxMtopResponse<Boolean> rxMtopResponse) {
            if (!rxMtopResponse.isReqSuccess) {
                EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
            } else if (rxMtopResponse.result != null && ((Boolean) rxMtopResponse.result).booleanValue()) {
                EventBus.getDefault().post(TaoCodeTransferEvent.itemTitle(TaoCodeTransferPresenter.this.mClipboardContent));
                EventBus.getDefault().post(TaoCodeDialogEvent.dialogShow());
                TaoCodeTransferPresenter.this.cleanClipboard();
            }
        }
    };

    public TaoCodeTransferPresenter(@NonNull Context context) {
        this.mAppContext = context.getApplicationContext();
    }

    public void onAppForegrounded() {
        this.mAppJustForegrounded = true;
    }

    public void onPageResume() {
        if (this.mAppJustForegrounded) {
            this.mAppJustForegrounded = false;
            parseClipboardContent();
        }
    }

    private void parseClipboardContent() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    ClipboardManager clipboardManager = (ClipboardManager) TaoCodeTransferPresenter.this.mAppContext.getSystemService("clipboard");
                    if (clipboardManager == null) {
                        EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                        return;
                    }
                    ClipData primaryClip = clipboardManager.getPrimaryClip();
                    if (primaryClip == null) {
                        EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                    } else if (TaoCodeTransferPresenter.this.isAppSelfClipData(primaryClip)) {
                        EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                    } else {
                        ClipData.Item itemAt = primaryClip.getItemAt(0);
                        if (itemAt == null) {
                            EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                            return;
                        }
                        CharSequence coerceToText = itemAt.coerceToText(TaoCodeTransferPresenter.this.mAppContext);
                        if (TextUtils.isEmpty(coerceToText)) {
                            EventBus.getDefault().post(TaoCodeDialogEvent.dialogNotShow());
                            return;
                        }
                        String unused = TaoCodeTransferPresenter.this.mClipboardContent = coerceToText.toString();
                        new TaoCodeItemInfoRequest(TaoCodeTransferPresenter.this.mClipboardContent).sendRequest(TaoCodeTransferPresenter.this.itemInfoListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void cleanClipboard() {
        this.mClipboardContent = null;
        ClipboardManager clipboardManager = (ClipboardManager) this.mAppContext.getSystemService("clipboard");
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""));
            PageRouterUtil.CLIP_BOARD_CONTENT_BEFORE_GO_TO_PAGE = "";
        }
    }

    /* access modifiers changed from: private */
    public boolean isAppSelfClipData(ClipData clipData) {
        if (clipData.getDescription() == null || clipData.getDescription().getLabel() == null) {
            return false;
        }
        String charSequence = clipData.getDescription().getLabel().toString();
        if (WEEX_CLIP_DATA_LABEL.equals(charSequence) || WINDVANE_CLIP_DATA_LABEL.equals(charSequence) || TAO_CODE_CLIP_DATA_LABEL.equals(charSequence)) {
            return true;
        }
        return false;
    }
}
