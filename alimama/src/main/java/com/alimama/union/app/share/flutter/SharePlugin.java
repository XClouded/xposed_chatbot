package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.logger.BusinessMonitorLogger;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyConfig;
import com.alimama.union.app.privacy.PrivacyDialog;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.share.flutter.network.ShareInfoRequest2;
import com.alimama.union.app.share.flutter.network.ShareInfoResponse2;
import com.google.gson.Gson;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class SharePlugin implements MethodChannel.MethodCallHandler, PermissionInterface {
    private static final String SHARE_CHANNEL = "com.alimama.moon/share";
    private static final String TAG = "SharePlugin";
    /* access modifiers changed from: private */
    public Activity mOwnerActivity;
    private final StoragePermissionValidator mPermissionValidator = new StoragePermissionValidator();
    private final ShareChannelClickHandler mShareChannelHandler;
    private ShareInfoResponse2 mSharedInfo;
    private PrivacyPermissionManager privacyPermissionManager;

    public void closePermissionRequest() {
    }

    public SharePlugin(Activity activity) {
        this.mOwnerActivity = activity;
        this.mShareChannelHandler = new ShareChannelClickHandler(activity);
        this.privacyPermissionManager = new PrivacyPermissionManager((Context) activity, (PermissionInterface) this);
    }

    public static void register(Activity activity, BinaryMessenger binaryMessenger) {
        new MethodChannel(binaryMessenger, SHARE_CHANNEL).setMethodCallHandler(new SharePlugin(activity));
    }

    public void openPermissionRequest() {
        this.mPermissionValidator.checkRequiredPermission(this.mOwnerActivity);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMethodCall(io.flutter.plugin.common.MethodCall r5, io.flutter.plugin.common.MethodChannel.Result r6) {
        /*
            r4 = this;
            java.lang.String r0 = r5.method
            int r1 = r0.hashCode()
            r2 = 1
            r3 = 0
            switch(r1) {
                case -1591963017: goto L_0x0048;
                case -506458366: goto L_0x003e;
                case 111185: goto L_0x0034;
                case 79319367: goto L_0x002a;
                case 290603145: goto L_0x0020;
                case 776691285: goto L_0x0016;
                case 1184695735: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0052
        L_0x000c:
            java.lang.String r1 = "copyShareText"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 4
            goto L_0x0053
        L_0x0016:
            java.lang.String r1 = "saveImages"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 6
            goto L_0x0053
        L_0x0020:
            java.lang.String r1 = "goToSharePoster"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 2
            goto L_0x0053
        L_0x002a:
            java.lang.String r1 = "shareTextTo"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 3
            goto L_0x0053
        L_0x0034:
            java.lang.String r1 = "pop"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 0
            goto L_0x0053
        L_0x003e:
            java.lang.String r1 = "copyCode"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 5
            goto L_0x0053
        L_0x0048:
            java.lang.String r1 = "getShareInfo"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 1
            goto L_0x0053
        L_0x0052:
            r0 = -1
        L_0x0053:
            switch(r0) {
                case 0: goto L_0x00ed;
                case 1: goto L_0x00e9;
                case 2: goto L_0x00df;
                case 3: goto L_0x00cb;
                case 4: goto L_0x009d;
                case 5: goto L_0x0088;
                case 6: goto L_0x0057;
                default: goto L_0x0056;
            }
        L_0x0056:
            return
        L_0x0057:
            java.lang.String r6 = "shared_text"
            java.lang.Object r6 = r5.argument(r6)
            java.lang.String r6 = (java.lang.String) r6
            com.alimama.moon.utils.CommonUtils.copyToClipboard(r6)
            java.lang.String r6 = "imgs"
            java.lang.Object r5 = r5.argument(r6)
            java.util.List r5 = (java.util.List) r5
            if (r5 == 0) goto L_0x0087
            boolean r6 = r5.isEmpty()
            if (r6 == 0) goto L_0x0073
            goto L_0x0087
        L_0x0073:
            android.app.Activity r6 = r4.mOwnerActivity
            boolean r6 = com.alimama.union.app.privacy.PrivacyUtil.hasWriteExternalStorage(r6)
            if (r6 != 0) goto L_0x0081
            com.alimama.union.app.privacy.PrivacyPermissionManager r5 = r4.privacyPermissionManager
            r5.showReadWritePermissionDialog()
            return
        L_0x0081:
            android.app.Activity r6 = r4.mOwnerActivity
            com.alimama.union.app.share.flutter.ImageShareSaverDialog.startDownloading(r6, r5)
            return
        L_0x0087:
            return
        L_0x0088:
            com.alimama.union.app.share.flutter.network.ShareInfoResponse2 r5 = r4.mSharedInfo
            java.lang.String r5 = r5.getTaoCode()
            com.alimama.moon.utils.CommonUtils.copyToClipboard(r5)
            android.app.Activity r5 = r4.mOwnerActivity
            com.alimama.union.app.share.flutter.network.ShareInfoResponse2 r6 = r4.mSharedInfo
            java.lang.String r6 = r6.getTaoCode()
            com.alimama.union.app.share.flutter.TaoCodeShareDialog.show(r5, r6)
            return
        L_0x009d:
            java.lang.Object r5 = r5.arguments
            java.lang.String r5 = (java.lang.String) r5
            boolean r6 = com.alimama.moon.utils.CommonUtils.copyToClipboard(r5)
            if (r6 == 0) goto L_0x00b9
            android.app.Activity r6 = r4.mOwnerActivity
            r0 = 2131296524(0x7f09010c, float:1.8210967E38)
            com.alimama.moon.utils.ToastUtil.showToast((android.content.Context) r6, (int) r0)
            java.lang.String r6 = "SharePlugin"
            java.lang.String r5 = r4.generateShareText(r5)
            com.alimama.union.app.logger.BusinessMonitorLogger.Share.flutterViewCopyShareTextSuccess(r6, r5)
            goto L_0x00ca
        L_0x00b9:
            android.app.Activity r6 = r4.mOwnerActivity
            r0 = 2131296522(0x7f09010a, float:1.8210963E38)
            com.alimama.moon.utils.ToastUtil.showToast((android.content.Context) r6, (int) r0)
            java.lang.String r6 = "SharePlugin"
            java.lang.String r5 = r4.generateShareText(r5)
            com.alimama.union.app.logger.BusinessMonitorLogger.Share.flutterViewCopyShareTextFailed(r6, r5)
        L_0x00ca:
            return
        L_0x00cb:
            java.lang.Object r5 = r5.arguments
            java.util.List r5 = (java.util.List) r5
            java.lang.Object r6 = r5.get(r3)
            java.lang.String r6 = (java.lang.String) r6
            java.lang.Object r5 = r5.get(r2)
            java.lang.String r5 = (java.lang.String) r5
            r4.shareToChannel(r6, r5)
            return
        L_0x00df:
            java.lang.Object r5 = r5.arguments
            java.lang.String r5 = (java.lang.String) r5
            com.alimama.union.app.share.flutter.network.ShareInfoResponse2 r6 = r4.mSharedInfo
            com.alimama.union.app.share.SharePosterActivity.openPage(r5, r6)
            return
        L_0x00e9:
            r4.fetchShareInfo(r6)
            return
        L_0x00ed:
            android.app.Activity r5 = r4.mOwnerActivity
            r5.finish()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.share.flutter.SharePlugin.onMethodCall(io.flutter.plugin.common.MethodCall, io.flutter.plugin.common.MethodChannel$Result):void");
    }

    private void fetchShareInfo(MethodChannel.Result result) {
        String str = "";
        Intent intent = this.mOwnerActivity.getIntent();
        if (!(intent == null || intent.getData() == null)) {
            str = intent.getData().toString();
        }
        if (TextUtils.isEmpty(str)) {
            ToastUtil.showToast((Context) this.mOwnerActivity, (int) R.string.server_error_retry_msg);
        } else {
            new ShareInfoRequest2(str).sendRequest(new RxMtopRequest.RxMtopResult(result) {
                private final /* synthetic */ MethodChannel.Result f$1;

                {
                    this.f$1 = r2;
                }

                public final void result(RxMtopResponse rxMtopResponse) {
                    SharePlugin.lambda$fetchShareInfo$18(SharePlugin.this, this.f$1, rxMtopResponse);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$fetchShareInfo$18(SharePlugin sharePlugin, MethodChannel.Result result, RxMtopResponse rxMtopResponse) {
        if (!rxMtopResponse.isReqSuccess || rxMtopResponse.result == null) {
            result.error(rxMtopResponse.retCode, rxMtopResponse.retMsg, (Object) null);
            return;
        }
        sharePlugin.mSharedInfo = (ShareInfoResponse2) rxMtopResponse.result;
        if ("NORMAL".equals(sharePlugin.mSharedInfo.getStatus())) {
            HashMap hashMap = new HashMap();
            hashMap.put("commission_percent", sharePlugin.mSharedInfo.getCommissionRate());
            hashMap.put("commission_amount", sharePlugin.mSharedInfo.getCommissionTotal());
            hashMap.put("shared_text", sharePlugin.mSharedInfo.getSharedText());
            hashMap.put("shared_imgs", new Gson().toJson((Object) sharePlugin.mSharedInfo.getImages()));
            result.success(hashMap);
            return;
        }
        new PrivacyDialog(sharePlugin.mOwnerActivity, sharePlugin.getTaoCodeErrorConfig(), 0, PrivacyDialog.BottomBarType.OneButton).show();
    }

    private String generateShareText(String str) {
        String str2 = "接口淘口令: " + this.mSharedInfo.getTaoCode();
        try {
            return str2 + ", 分享文案前200字符:" + str.substring(0, 200);
        } catch (Exception unused) {
            return str2;
        }
    }

    private void shareToChannel(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            CommonUtils.copyToClipboard(str2);
            String lowerCase = str.toLowerCase();
            char c = 65535;
            int hashCode = lowerCase.hashCode();
            if (hashCode != -791770330) {
                if (hashCode != 3616) {
                    if (hashCode == 113011944 && lowerCase.equals("weibo")) {
                        c = 1;
                    }
                } else if (lowerCase.equals("qq")) {
                    c = 2;
                }
            } else if (lowerCase.equals("wechat")) {
                c = 0;
            }
            switch (c) {
                case 0:
                    this.mShareChannelHandler.shareToWechat();
                    BusinessMonitorLogger.Share.flutterViewShareToChannal(TAG, "wechat", generateShareText(str2));
                    return;
                case 1:
                    this.mShareChannelHandler.shareToWeibo(str2, (ArrayList<Uri>) null);
                    BusinessMonitorLogger.Share.flutterViewShareToChannal(TAG, "weibo", generateShareText(str2));
                    return;
                case 2:
                    this.mShareChannelHandler.shareToQQ();
                    BusinessMonitorLogger.Share.flutterViewShareToChannal(TAG, "qq", generateShareText(str2));
                    return;
                default:
                    Log.e(TAG, "shareToChannel unsupported channel: " + str);
                    return;
            }
        }
    }

    private PrivacyConfig getTaoCodeErrorConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig(this.mOwnerActivity.getString(R.string.taocode_error), this.mOwnerActivity.getString(R.string.taocode_error_text), this.mOwnerActivity.getString(R.string.taocode_error_confirm));
        privacyConfig.setNotShowCloseImg(true);
        privacyConfig.setCenterBtnCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                if (SharePlugin.this.mOwnerActivity != null && !SharePlugin.this.mOwnerActivity.isFinishing()) {
                    UTHelper.ShareFlutterPage.clickTaoCodeErrorConfirm();
                    SharePlugin.this.mOwnerActivity.finish();
                }
            }
        });
        return privacyConfig;
    }
}
