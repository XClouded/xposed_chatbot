package com.taobao.weex.module;

import com.taobao.weex.ui.module.WXModalUIModule;

public class WXTBModalUIModule extends WXModalUIModule {
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003a  */
    @com.taobao.weex.common.WXModuleAnno
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void toast(java.lang.String r5) {
        /*
            r4 = this;
            java.lang.String r0 = ""
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            r2 = 0
            if (r1 != 0) goto L_0x002e
            java.lang.String r1 = "utf-8"
            java.lang.String r5 = java.net.URLDecoder.decode(r5, r1)     // Catch:{ Exception -> 0x0028 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0028 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0028 }
            java.lang.String r5 = "message"
            java.lang.String r5 = r1.optString(r5)     // Catch:{ Exception -> 0x0028 }
            java.lang.String r0 = "duration"
            int r0 = r1.optInt(r0)     // Catch:{ Exception -> 0x0023 }
            r2 = r0
            r0 = r5
            goto L_0x002e
        L_0x0023:
            r0 = move-exception
            r3 = r0
            r0 = r5
            r5 = r3
            goto L_0x0029
        L_0x0028:
            r5 = move-exception
        L_0x0029:
            java.lang.String r1 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r5)
        L_0x002e:
            boolean r5 = android.text.TextUtils.isEmpty(r0)
            if (r5 == 0) goto L_0x003a
            java.lang.String r5 = "[WXModalUIModule] toast param parse is null "
            com.taobao.weex.utils.WXLogUtils.e(r5)
            goto L_0x0058
        L_0x003a:
            com.taobao.weex.WXSDKInstance r5 = r4.mWXSDKInstance
            android.content.Context r5 = r5.getContext()
            if (r5 != 0) goto L_0x0048
            java.lang.String r5 = "[WXModalUIModule]  mWXSDKInstance.getContext() == null"
            com.taobao.weex.utils.WXLogUtils.e(r5)
            goto L_0x0058
        L_0x0048:
            com.taobao.weex.WXSDKInstance r5 = r4.mWXSDKInstance
            android.content.Context r5 = r5.getContext()
            int r2 = r2 * 1000
            long r1 = (long) r2
            com.taobao.uikit.extend.component.unify.Toast.TBToast r5 = com.taobao.uikit.extend.component.unify.Toast.TBToast.makeText(r5, r0, r1)
            r5.show()
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.module.WXTBModalUIModule.toast(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d  */
    @com.taobao.weex.common.WXModuleAnno
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void promptWithClose(java.lang.String r3) {
        /*
            r2 = this;
            java.lang.String r0 = ""
            boolean r1 = android.text.TextUtils.isEmpty(r3)
            if (r1 != 0) goto L_0x0020
            java.lang.String r1 = "utf-8"
            java.lang.String r3 = java.net.URLDecoder.decode(r3, r1)     // Catch:{ Exception -> 0x001a }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x001a }
            r1.<init>(r3)     // Catch:{ Exception -> 0x001a }
            java.lang.String r3 = "url"
            java.lang.String r3 = r1.optString(r3)     // Catch:{ Exception -> 0x001a }
            goto L_0x0021
        L_0x001a:
            r3 = move-exception
            java.lang.String r1 = "[WXModalUIModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r3)
        L_0x0020:
            r3 = r0
        L_0x0021:
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 == 0) goto L_0x002d
            java.lang.String r3 = "[WXModalUIModule] toast param parse is null "
            com.taobao.weex.utils.WXLogUtils.e(r3)
            goto L_0x0057
        L_0x002d:
            android.taobao.windvane.extra.uc.WVUCWebView r0 = new android.taobao.windvane.extra.uc.WVUCWebView
            com.taobao.weex.WXSDKInstance r1 = r2.mWXSDKInstance
            android.content.Context r1 = r1.getContext()
            r0.<init>(r1)
            r0.loadUrl(r3)
            com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog$Builder r3 = new com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog$Builder
            com.taobao.weex.WXSDKInstance r1 = r2.mWXSDKInstance
            android.content.Context r1 = r1.getContext()
            r3.<init>(r1)
            r1 = 1
            com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog$Builder r3 = r3.cardDialog(r1)
            r1 = 0
            com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog$Builder r3 = r3.customView((android.view.View) r0, (boolean) r1)
            com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog r3 = r3.build()
            r3.show()
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.module.WXTBModalUIModule.promptWithClose(java.lang.String):void");
    }
}
