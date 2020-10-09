package com.taobao.weex.ui.module;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;
import com.taobao.weex.WXSDKEngine;

public class WXModalUIModule extends WXSDKEngine.DestroyableModule {
    public static final String CANCEL = "Cancel";
    public static final String CANCEL_TITLE = "cancelTitle";
    public static final String DATA = "data";
    public static final String DEFAULT = "default";
    public static final String DURATION = "duration";
    public static final String MESSAGE = "message";
    public static final String OK = "OK";
    public static final String OK_TITLE = "okTitle";
    public static final String RESULT = "result";
    /* access modifiers changed from: private */
    public Dialog activeDialog;
    private Toast toast;

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0040  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    @android.annotation.SuppressLint({"ShowToast"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void toast(com.alibaba.fastjson.JSONObject r4) {
        /*
            r3 = this;
            com.taobao.weex.WXSDKInstance r0 = r3.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            java.lang.String r0 = ""
            r1 = 0
            if (r4 == 0) goto L_0x0033
            java.lang.String r2 = "message"
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x002d }
            java.lang.String r0 = "duration"
            boolean r0 = r4.containsKey(r0)     // Catch:{ Exception -> 0x002a }
            if (r0 == 0) goto L_0x0027
            java.lang.String r0 = "duration"
            java.lang.Integer r4 = r4.getInteger(r0)     // Catch:{ Exception -> 0x002a }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x002a }
            goto L_0x0028
        L_0x0027:
            r4 = 0
        L_0x0028:
            r0 = r2
            goto L_0x0034
        L_0x002a:
            r4 = move-exception
            r0 = r2
            goto L_0x002e
        L_0x002d:
            r4 = move-exception
        L_0x002e:
            java.lang.String r2 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r4)
        L_0x0033:
            r4 = 0
        L_0x0034:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0040
            java.lang.String r4 = "[WXModalUIModule] toast param parse is null "
            com.taobao.weex.utils.WXLogUtils.e(r4)
            return
        L_0x0040:
            r2 = 3
            if (r4 <= r2) goto L_0x0045
            r4 = 1
            goto L_0x0046
        L_0x0045:
            r4 = 0
        L_0x0046:
            android.widget.Toast r2 = r3.toast
            if (r2 != 0) goto L_0x0057
            com.taobao.weex.WXSDKInstance r2 = r3.mWXSDKInstance
            android.content.Context r2 = r2.getContext()
            android.widget.Toast r4 = android.widget.Toast.makeText(r2, r0, r4)
            r3.toast = r4
            goto L_0x0061
        L_0x0057:
            android.widget.Toast r2 = r3.toast
            r2.setDuration(r4)
            android.widget.Toast r4 = r3.toast
            r4.setText(r0)
        L_0x0061:
            android.widget.Toast r4 = r3.toast
            r0 = 17
            r4.setGravity(r0, r1, r1)
            android.widget.Toast r4 = r3.toast
            r4.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.toast(com.alibaba.fastjson.JSONObject):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0044  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alert(com.alibaba.fastjson.JSONObject r4, final com.taobao.weex.bridge.JSCallback r5) {
        /*
            r3 = this;
            com.taobao.weex.WXSDKInstance r0 = r3.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x005d
            java.lang.String r0 = ""
            java.lang.String r1 = "OK"
            if (r4 == 0) goto L_0x0028
            java.lang.String r2 = "message"
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x0022 }
            java.lang.String r0 = "okTitle"
            java.lang.String r4 = r4.getString(r0)     // Catch:{ Exception -> 0x001f }
            r1 = r4
            r0 = r2
            goto L_0x0028
        L_0x001f:
            r4 = move-exception
            r0 = r2
            goto L_0x0023
        L_0x0022:
            r4 = move-exception
        L_0x0023:
            java.lang.String r2 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r4)
        L_0x0028:
            boolean r4 = android.text.TextUtils.isEmpty(r0)
            if (r4 == 0) goto L_0x0030
            java.lang.String r0 = ""
        L_0x0030:
            android.app.AlertDialog$Builder r4 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r2 = r3.mWXSDKInstance
            android.content.Context r2 = r2.getContext()
            r4.<init>(r2)
            r4.setMessage(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 == 0) goto L_0x0046
            java.lang.String r1 = "OK"
        L_0x0046:
            com.taobao.weex.ui.module.WXModalUIModule$1 r0 = new com.taobao.weex.ui.module.WXModalUIModule$1
            r0.<init>(r5, r1)
            r4.setPositiveButton(r1, r0)
            android.app.AlertDialog r4 = r4.create()
            r5 = 0
            r4.setCanceledOnTouchOutside(r5)
            r4.show()
            r3.tracking(r4)
            goto L_0x0062
        L_0x005d:
            java.lang.String r4 = "[WXModalUIModule] when call alert mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r4)
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.alert(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0058  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void confirm(com.alibaba.fastjson.JSONObject r5, final com.taobao.weex.bridge.JSCallback r6) {
        /*
            r4 = this;
            com.taobao.weex.WXSDKInstance r0 = r4.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x0079
            java.lang.String r0 = ""
            java.lang.String r1 = "OK"
            java.lang.String r2 = "Cancel"
            if (r5 == 0) goto L_0x0034
            java.lang.String r3 = "message"
            java.lang.String r3 = r5.getString(r3)     // Catch:{ Exception -> 0x002e }
            java.lang.String r0 = "okTitle"
            java.lang.String r0 = r5.getString(r0)     // Catch:{ Exception -> 0x002b }
            java.lang.String r1 = "cancelTitle"
            java.lang.String r5 = r5.getString(r1)     // Catch:{ Exception -> 0x0028 }
            r2 = r5
            r1 = r0
            r0 = r3
            goto L_0x0034
        L_0x0028:
            r5 = move-exception
            r1 = r0
            goto L_0x002c
        L_0x002b:
            r5 = move-exception
        L_0x002c:
            r0 = r3
            goto L_0x002f
        L_0x002e:
            r5 = move-exception
        L_0x002f:
            java.lang.String r3 = "[WXModalUIModule] confirm param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.Throwable) r5)
        L_0x0034:
            boolean r5 = android.text.TextUtils.isEmpty(r0)
            if (r5 == 0) goto L_0x003c
            java.lang.String r0 = ""
        L_0x003c:
            android.app.AlertDialog$Builder r5 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r3 = r4.mWXSDKInstance
            android.content.Context r3 = r3.getContext()
            r5.<init>(r3)
            r5.setMessage(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 == 0) goto L_0x0052
            java.lang.String r1 = "OK"
        L_0x0052:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L_0x005a
            java.lang.String r2 = "Cancel"
        L_0x005a:
            com.taobao.weex.ui.module.WXModalUIModule$2 r0 = new com.taobao.weex.ui.module.WXModalUIModule$2
            r0.<init>(r6, r1)
            r5.setPositiveButton(r1, r0)
            com.taobao.weex.ui.module.WXModalUIModule$3 r0 = new com.taobao.weex.ui.module.WXModalUIModule$3
            r0.<init>(r6, r2)
            r5.setNegativeButton(r2, r0)
            android.app.AlertDialog r5 = r5.create()
            r6 = 0
            r5.setCanceledOnTouchOutside(r6)
            r5.show()
            r4.tracking(r5)
            goto L_0x007e
        L_0x0079:
            java.lang.String r5 = "[WXModalUIModule] when call confirm mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r5)
        L_0x007e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.confirm(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0075  */
    @com.taobao.weex.annotation.JSMethod(uiThread = true)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void prompt(com.alibaba.fastjson.JSONObject r6, final com.taobao.weex.bridge.JSCallback r7) {
        /*
            r5 = this;
            com.taobao.weex.WXSDKInstance r0 = r5.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x0097
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            java.lang.String r2 = "OK"
            java.lang.String r3 = "Cancel"
            if (r6 == 0) goto L_0x0040
            java.lang.String r4 = "message"
            java.lang.String r4 = r6.getString(r4)     // Catch:{ Exception -> 0x003a }
            java.lang.String r0 = "okTitle"
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Exception -> 0x0037 }
            java.lang.String r2 = "cancelTitle"
            java.lang.String r2 = r6.getString(r2)     // Catch:{ Exception -> 0x0034 }
            java.lang.String r3 = "default"
            java.lang.String r6 = r6.getString(r3)     // Catch:{ Exception -> 0x0031 }
            r1 = r6
            r3 = r2
            r2 = r0
            r0 = r4
            goto L_0x0040
        L_0x0031:
            r6 = move-exception
            r3 = r2
            goto L_0x0035
        L_0x0034:
            r6 = move-exception
        L_0x0035:
            r2 = r0
            goto L_0x0038
        L_0x0037:
            r6 = move-exception
        L_0x0038:
            r0 = r4
            goto L_0x003b
        L_0x003a:
            r6 = move-exception
        L_0x003b:
            java.lang.String r4 = "[WXModalUIModule] confirm param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r4, (java.lang.Throwable) r6)
        L_0x0040:
            boolean r6 = android.text.TextUtils.isEmpty(r0)
            if (r6 == 0) goto L_0x0048
            java.lang.String r0 = ""
        L_0x0048:
            android.app.AlertDialog$Builder r6 = new android.app.AlertDialog$Builder
            com.taobao.weex.WXSDKInstance r4 = r5.mWXSDKInstance
            android.content.Context r4 = r4.getContext()
            r6.<init>(r4)
            r6.setMessage(r0)
            android.widget.EditText r0 = new android.widget.EditText
            com.taobao.weex.WXSDKInstance r4 = r5.mWXSDKInstance
            android.content.Context r4 = r4.getContext()
            r0.<init>(r4)
            r0.setText(r1)
            r6.setView(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r2)
            if (r1 == 0) goto L_0x006f
            java.lang.String r2 = "OK"
        L_0x006f:
            boolean r1 = android.text.TextUtils.isEmpty(r3)
            if (r1 == 0) goto L_0x0077
            java.lang.String r3 = "Cancel"
        L_0x0077:
            com.taobao.weex.ui.module.WXModalUIModule$5 r1 = new com.taobao.weex.ui.module.WXModalUIModule$5
            r1.<init>(r7, r2, r0)
            android.app.AlertDialog$Builder r1 = r6.setPositiveButton(r2, r1)
            com.taobao.weex.ui.module.WXModalUIModule$4 r2 = new com.taobao.weex.ui.module.WXModalUIModule$4
            r2.<init>(r7, r3, r0)
            r1.setNegativeButton(r3, r2)
            android.app.AlertDialog r6 = r6.create()
            r7 = 0
            r6.setCanceledOnTouchOutside(r7)
            r6.show()
            r5.tracking(r6)
            goto L_0x009c
        L_0x0097:
            java.lang.String r6 = "when call prompt mWXSDKInstance.getContext() must instanceof Activity"
            com.taobao.weex.utils.WXLogUtils.e(r6)
        L_0x009c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.module.WXModalUIModule.prompt(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback):void");
    }

    private void tracking(Dialog dialog) {
        this.activeDialog = dialog;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                Dialog unused = WXModalUIModule.this.activeDialog = null;
            }
        });
    }

    public void destroy() {
        if (this.activeDialog != null && this.activeDialog.isShowing()) {
            this.activeDialog.dismiss();
        }
    }
}
