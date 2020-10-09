package com.alimama.moon.windvane.jsbridge;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.features.commoditymoments.PasteTaoCodeInCommentsDialog;
import com.alimama.moon.features.commoditymoments.SaveSuccessDialog;
import com.alimama.moon.windvane.jsbridge.model.ShareModel;
import com.alimama.union.app.infrastructure.image.picPreviewer.PictureBrowserManager;

public class InfrastructureJSBridge extends WVApiPlugin {
    private static final String ACTION_COPY_TAO_CODE = "copyTaoCode";
    private static final String ACTION_SHARE_TO_EARN = "shareToEarn";
    private static final String ACTION_VIEW_PIC_ON_PICTURE_BROWSER = "viewPicOnPictureBrowser";

    public boolean canExecute(String str) {
        return TextUtils.equals(str, ACTION_COPY_TAO_CODE) || TextUtils.equals(str, ACTION_SHARE_TO_EARN) || TextUtils.equals(str, ACTION_VIEW_PIC_ON_PICTURE_BROWSER);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0059 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0062  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean execute(java.lang.String r6, java.lang.String r7, android.taobao.windvane.jsbridge.WVCallBackContext r8) {
        /*
            r5 = this;
            boolean r0 = r5.canExecute(r6)
            r1 = 0
            if (r0 != 0) goto L_0x0021
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "the method "
            r7.append(r0)
            r7.append(r6)
            java.lang.String r6 = " is not found"
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            r8.error((java.lang.String) r6)
            return r1
        L_0x0021:
            r0 = -1
            int r2 = r6.hashCode()
            r3 = 87017906(0x52fc9b2, float:8.265506E-36)
            r4 = 1
            if (r2 == r3) goto L_0x004b
            r3 = 1104359118(0x41d32ece, float:26.397854)
            if (r2 == r3) goto L_0x0041
            r3 = 2130037626(0x7ef5cb7a, float:1.6335868E38)
            if (r2 == r3) goto L_0x0037
            goto L_0x0055
        L_0x0037:
            java.lang.String r2 = "copyTaoCode"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0055
            r6 = 0
            goto L_0x0056
        L_0x0041:
            java.lang.String r2 = "viewPicOnPictureBrowser"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0055
            r6 = 2
            goto L_0x0056
        L_0x004b:
            java.lang.String r2 = "shareToEarn"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0055
            r6 = 1
            goto L_0x0056
        L_0x0055:
            r6 = -1
        L_0x0056:
            switch(r6) {
                case 0: goto L_0x0062;
                case 1: goto L_0x005e;
                case 2: goto L_0x005a;
                default: goto L_0x0059;
            }
        L_0x0059:
            return r1
        L_0x005a:
            r5.viewPicOnPictureBrowser(r7, r8)
            return r4
        L_0x005e:
            r5.shareToEarn(r7, r8)
            return r4
        L_0x0062:
            r5.copyTaoCode(r7, r8)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.windvane.jsbridge.InfrastructureJSBridge.execute(java.lang.String, java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):boolean");
    }

    private void copyTaoCode(String str, WVCallBackContext wVCallBackContext) {
        PasteTaoCodeInCommentsDialog.showPasteTaoCodeInCommentsDialog(this.mContext, PasteTaoCodeInCommentsDialog.DialogType.Click_Copy_Taocode_Btn, JsBridgeUtil.parseParams(str, "taoCodeStr", wVCallBackContext));
    }

    private void shareToEarn(String str, WVCallBackContext wVCallBackContext) {
        SaveSuccessDialog.showSaveTaoCodeAndImgSuccessDialog(this.mContext, (ShareModel) JSON.parseObject(str, ShareModel.class));
    }

    private void viewPicOnPictureBrowser(String str, WVCallBackContext wVCallBackContext) {
        PictureBrowserManager.getInstance().goToPictureManager(this.mContext, str);
    }
}
