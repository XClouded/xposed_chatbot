package com.taobao.weex.module;

import com.alibaba.aliweex.adapter.IShareModuleAdapter;

public class WXShareModule implements IShareModuleAdapter {
    private static final String TAG = "WXShareModule";

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v11, resolved type: java.util.Map} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doShare(android.content.Context r7, java.lang.String r8, final com.taobao.weex.bridge.JSCallback r9) {
        /*
            r6 = this;
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x023b }
            r1.<init>(r8)     // Catch:{ JSONException -> 0x023b }
            com.ut.share.business.ShareContent r8 = new com.ut.share.business.ShareContent     // Catch:{ JSONException -> 0x023b }
            r8.<init>()     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "businessId"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x001a
            java.lang.String r2 = "businessId"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x001c
        L_0x001a:
            java.lang.String r2 = ""
        L_0x001c:
            r8.businessId = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "disableHeadUrl"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x002d
            java.lang.String r2 = "disableHeadUrl"
            boolean r2 = r1.getBoolean(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x002e
        L_0x002d:
            r2 = 0
        L_0x002e:
            r8.disableHeadUrl = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "disableQrcode"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x003f
            java.lang.String r2 = "disableQrcode"
            boolean r2 = r1.getBoolean(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0040
        L_0x003f:
            r2 = 0
        L_0x0040:
            r8.disableQrcode = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "disableTextInfo"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0051
            java.lang.String r2 = "disableTextInfo"
            boolean r2 = r1.getBoolean(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0052
        L_0x0051:
            r2 = 0
        L_0x0052:
            r8.disableTextInfo = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "title"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0063
            java.lang.String r2 = "title"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0065
        L_0x0063:
            java.lang.String r2 = ""
        L_0x0065:
            r8.title = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "text"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0076
            java.lang.String r2 = "text"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0078
        L_0x0076:
            java.lang.String r2 = ""
        L_0x0078:
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ JSONException -> 0x023b }
            if (r3 == 0) goto L_0x0080
            java.lang.String r2 = "我分享给你了一个淘宝页面，快来看看吧"
        L_0x0080:
            r8.description = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "image"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0091
            java.lang.String r2 = "image"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0093
        L_0x0091:
            java.lang.String r2 = ""
        L_0x0093:
            r8.imageUrl = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "images"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            r3 = 0
            if (r2 == 0) goto L_0x00ab
            java.lang.String r2 = "images"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            java.util.List r2 = com.alibaba.fastjson.JSON.parseArray((java.lang.String) r2, r4)     // Catch:{ JSONException -> 0x023b }
            goto L_0x00ac
        L_0x00ab:
            r2 = r3
        L_0x00ac:
            r8.snapshotImages = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "url"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x00bd
            java.lang.String r2 = "url"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x00bf
        L_0x00bd:
            java.lang.String r2 = ""
        L_0x00bf:
            r8.url = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "contentType"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x00d0
            java.lang.String r2 = "contentType"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x00d2
        L_0x00d0:
            java.lang.String r2 = ""
        L_0x00d2:
            r8.contentType = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "extendParams"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x00eb
            java.lang.String r2 = "extendParams"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            java.lang.Object r2 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r2, r4)     // Catch:{ JSONException -> 0x023b }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ JSONException -> 0x023b }
            goto L_0x00ec
        L_0x00eb:
            r2 = r3
        L_0x00ec:
            r8.extendParams = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "businessInfo"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0105
            java.lang.String r2 = "businessInfo"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            java.lang.Object r2 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r2, r4)     // Catch:{ JSONException -> 0x023b }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ JSONException -> 0x023b }
            goto L_0x0106
        L_0x0105:
            r2 = r3
        L_0x0106:
            r8.businessInfo = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "isActivity"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0117
            java.lang.String r2 = "isActivity"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0119
        L_0x0117:
            java.lang.String r2 = "false"
        L_0x0119:
            r8.isActivity = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "activityParams"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0132
            java.lang.String r2 = "activityParams"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            java.lang.Object r2 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r2, r4)     // Catch:{ JSONException -> 0x023b }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ JSONException -> 0x023b }
            goto L_0x0133
        L_0x0132:
            r2 = r3
        L_0x0133:
            r8.activityParams = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "needSaveContent"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0144
            java.lang.String r2 = "needSaveContent"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0146
        L_0x0144:
            java.lang.String r2 = "false"
        L_0x0146:
            r8.needSaveContent = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "disableBackToClient"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0157
            java.lang.String r2 = "disableBackToClient"
            boolean r2 = r1.getBoolean(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0158
        L_0x0157:
            r2 = 0
        L_0x0158:
            r8.disableBackToClient = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "weixinAppId"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0169
            java.lang.String r2 = "weixinAppId"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x016b
        L_0x0169:
            java.lang.String r2 = ""
        L_0x016b:
            r8.weixinAppId = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "weixinMsgType"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x017c
            java.lang.String r2 = "weixinMsgType"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x017e
        L_0x017c:
            java.lang.String r2 = ""
        L_0x017e:
            r8.weixinMsgType = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "qrTipsText"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x018f
            java.lang.String r2 = "qrTipsText"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0191
        L_0x018f:
            java.lang.String r2 = ""
        L_0x0191:
            r8.qrTipsText = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "templateId"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x01a2
            java.lang.String r2 = "templateId"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x01a4
        L_0x01a2:
            java.lang.String r2 = ""
        L_0x01a4:
            r8.templateId = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "shareId"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x01b5
            java.lang.String r2 = "shareId"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x01b7
        L_0x01b5:
            java.lang.String r2 = ""
        L_0x01b7:
            r8.shareId = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "scene"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x01c8
            java.lang.String r2 = "scene"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x01ca
        L_0x01c8:
            java.lang.String r2 = ""
        L_0x01ca:
            r8.shareScene = r2     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "targets"
            boolean r2 = r1.has(r2)     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x01db
            java.lang.String r2 = "targets"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x023b }
            goto L_0x01dd
        L_0x01db:
            java.lang.String r2 = ""
        L_0x01dd:
            java.lang.String r4 = "templateParams"
            boolean r4 = r1.has(r4)     // Catch:{ JSONException -> 0x023b }
            if (r4 == 0) goto L_0x01f4
            java.lang.String r3 = "templateParams"
            java.lang.String r1 = r1.getString(r3)     // Catch:{ JSONException -> 0x023b }
            java.lang.Class<java.util.Map> r3 = java.util.Map.class
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r3)     // Catch:{ JSONException -> 0x023b }
            r3 = r1
            java.util.Map r3 = (java.util.Map) r3     // Catch:{ JSONException -> 0x023b }
        L_0x01f4:
            r8.templateParams = r3     // Catch:{ JSONException -> 0x023b }
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ JSONException -> 0x023b }
            r1.<init>()     // Catch:{ JSONException -> 0x023b }
            java.lang.String r3 = "all"
            boolean r3 = android.text.TextUtils.equals(r3, r2)     // Catch:{ JSONException -> 0x023b }
            if (r3 == 0) goto L_0x0204
            goto L_0x022b
        L_0x0204:
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ JSONException -> 0x023b }
            if (r3 != 0) goto L_0x021c
            java.lang.String r3 = ","
            java.lang.String[] r2 = r2.split(r3)     // Catch:{ JSONException -> 0x023b }
            int r3 = r2.length     // Catch:{ JSONException -> 0x023b }
            r4 = 0
        L_0x0212:
            if (r4 >= r3) goto L_0x022b
            r5 = r2[r4]     // Catch:{ JSONException -> 0x023b }
            r1.add(r5)     // Catch:{ JSONException -> 0x023b }
            int r4 = r4 + 1
            goto L_0x0212
        L_0x021c:
            java.lang.String r2 = "3"
            r1.add(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "13"
            r1.add(r2)     // Catch:{ JSONException -> 0x023b }
            java.lang.String r2 = "0"
            r1.add(r2)     // Catch:{ JSONException -> 0x023b }
        L_0x022b:
            boolean r2 = r7 instanceof android.app.Activity     // Catch:{ JSONException -> 0x023b }
            if (r2 == 0) goto L_0x0253
            r2 = r7
            android.app.Activity r2 = (android.app.Activity) r2     // Catch:{ JSONException -> 0x023b }
            com.taobao.weex.module.WXShareModule$1 r3 = new com.taobao.weex.module.WXShareModule$1     // Catch:{ JSONException -> 0x023b }
            r3.<init>(r9)     // Catch:{ JSONException -> 0x023b }
            com.ut.share.business.ShareBusiness.share(r2, r1, r8, r3)     // Catch:{ JSONException -> 0x023b }
            goto L_0x0253
        L_0x023b:
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.lang.String r1 = "result"
            java.lang.String r2 = "failure"
            r8.put(r1, r2)
            r9.invoke(r8)
            java.lang.String r8 = "分享失败"
            android.widget.Toast r7 = android.widget.Toast.makeText(r7, r8, r0)
            r7.show()
        L_0x0253:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.module.WXShareModule.doShare(android.content.Context, java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }
}
