package com.alibaba.aliweex.preLoad;

import android.content.Context;
import android.net.Uri;
import com.taobao.weex.WXSDKInstance;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WXPreLoadManager {
    private Map<String, WXSDKInstance> mInstanceMap;

    private static class SingleTonHolder {
        /* access modifiers changed from: private */
        public static final WXPreLoadManager INSTANCE = new WXPreLoadManager();

        private SingleTonHolder() {
        }
    }

    private WXPreLoadManager() {
        this.mInstanceMap = new ConcurrentHashMap();
    }

    public static WXPreLoadManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public WXSDKInstance offerPreInitInstance(String str, Context context) {
        WXSDKInstance remove = this.mInstanceMap.remove(str);
        if (remove != null) {
            remove.init(context);
        }
        return remove;
    }

    public void preInit(Uri uri, String str) {
        preInit(true, uri.toString(), (Map<String, Object>) null, (String) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void preInit(boolean r11, java.lang.String r12, java.util.Map<java.lang.String, java.lang.Object> r13, java.lang.String r14) {
        /*
            r10 = this;
            android.net.Uri r0 = android.net.Uri.parse(r12)
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            boolean r1 = com.taobao.weex.WXSDKEngine.isInitialized()
            if (r1 != 0) goto L_0x000e
            return
        L_0x000e:
            java.lang.String r1 = r0.toString()
            boolean r2 = com.alibaba.aliweex.bundle.UrlValidate.isValid(r1)
            if (r2 != 0) goto L_0x0019
            return
        L_0x0019:
            com.alibaba.aliweex.AliWeex r2 = com.alibaba.aliweex.AliWeex.getInstance()
            android.content.Context r2 = r2.getContext()
            if (r2 != 0) goto L_0x0024
            return
        L_0x0024:
            android.taobao.windvane.webview.WVSchemeIntercepterInterface r2 = android.taobao.windvane.webview.WVSchemeInterceptService.getWVSchemeIntercepter()
            if (r2 == 0) goto L_0x0032
            java.lang.String r1 = r2.dealUrlScheme(r1)
            android.net.Uri r0 = android.net.Uri.parse(r1)
        L_0x0032:
            r7 = r1
            java.lang.String r1 = "preInitInstance"
            java.lang.String r1 = r0.getQueryParameter(r1)
            java.lang.String r2 = "preDownLoad"
            r3 = 0
            boolean r0 = r0.getBooleanQueryParameter(r2, r3)
            java.lang.String r2 = "vue"
            boolean r2 = r2.equalsIgnoreCase(r1)
            if (r2 != 0) goto L_0x0053
            java.lang.String r2 = "rax"
            boolean r2 = r2.equalsIgnoreCase(r1)
            if (r2 != 0) goto L_0x0053
            if (r0 != 0) goto L_0x0053
            return
        L_0x0053:
            if (r11 == 0) goto L_0x005b
            com.alibaba.aliweex.AliWXSDKInstance r11 = new com.alibaba.aliweex.AliWXSDKInstance
            r11.<init>(r7)
            goto L_0x0060
        L_0x005b:
            com.taobao.weex.WXSDKInstance r11 = new com.taobao.weex.WXSDKInstance
            r11.<init>()
        L_0x0060:
            com.alibaba.aliweex.AliWeex r2 = com.alibaba.aliweex.AliWeex.getInstance()
            android.content.Context r2 = r2.getContext()
            r11.setContext(r2)
            if (r13 != 0) goto L_0x0072
            java.util.HashMap r13 = new java.util.HashMap
            r13.<init>()
        L_0x0072:
            java.lang.String r2 = "bundleUrl"
            boolean r2 = r13.containsKey(r2)
            if (r2 != 0) goto L_0x00a9
            long r2 = com.taobao.weex.utils.WXUtils.getFixUnixTime()
            java.lang.String r12 = com.alibaba.aliweex.utils.WXPrefetchUtil.handleUrl(r11, r12)
            java.lang.String r4 = "test->"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "WXPrefetchUtil.handleUrl time cost :"
            r5.append(r6)
            long r8 = com.taobao.weex.utils.WXUtils.getFixUnixTime()
            long r8 = r8 - r2
            r5.append(r8)
            java.lang.String r2 = r5.toString()
            android.util.Log.e(r4, r2)
            java.lang.String r2 = "bundleUrl"
            boolean r3 = android.text.TextUtils.isEmpty(r12)
            if (r3 == 0) goto L_0x00a6
            r12 = r7
        L_0x00a6:
            r13.put(r2, r12)
        L_0x00a9:
            java.lang.String r12 = "render_strategy"
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.APPEND_ASYNC
            java.lang.String r2 = r2.toString()
            r13.put(r12, r2)
            r12 = 1
            if (r0 == 0) goto L_0x00cf
            java.lang.String r2 = "wxPreDownLoad"
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r12)
            r13.put(r2, r3)
            com.taobao.weex.performance.WXInstanceApm r2 = r11.getApmForInstance()
            java.lang.String r3 = "wxPreDownLoad"
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r12)
            r2.addProperty(r3, r4)
        L_0x00cf:
            r2 = 0
            java.lang.String r3 = "vue"
            boolean r3 = r3.equalsIgnoreCase(r1)
            if (r3 == 0) goto L_0x00dc
            java.lang.String r1 = "// { \"framework\": \"Vue\" }\n"
        L_0x00da:
            r3 = r1
            goto L_0x00fe
        L_0x00dc:
            java.lang.String r3 = "rax"
            boolean r3 = r3.equalsIgnoreCase(r1)
            if (r3 == 0) goto L_0x00e7
            java.lang.String r1 = "// { \"framework\": \"Rax\" }\n"
            goto L_0x00da
        L_0x00e7:
            java.lang.String r3 = "WXPreLoadManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "unsupport init bundle type :"
            r4.append(r5)
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r1)
            r3 = r2
        L_0x00fe:
            if (r3 == 0) goto L_0x012d
            java.lang.String r1 = "test->"
            java.lang.String r2 = "start preInit :"
            android.util.Log.e(r1, r2)
            java.util.Map<java.lang.String, com.taobao.weex.WXSDKInstance> r1 = r10.mInstanceMap
            r1.put(r7, r11)
            java.lang.String r1 = "wxPreInit"
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r12)
            r13.put(r1, r2)
            com.taobao.weex.performance.WXInstanceApm r1 = r11.getApmForInstance()
            java.lang.String r2 = "wxPreInit"
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)
            r1.addProperty(r2, r12)
            com.taobao.weex.common.WXRenderStrategy r6 = com.taobao.weex.common.WXRenderStrategy.APPEND_ASYNC
            r1 = r11
            r2 = r7
            r4 = r13
            r5 = r14
            r1.preInit(r2, r3, r4, r5, r6)
        L_0x012d:
            if (r0 == 0) goto L_0x0148
            java.util.Map<java.lang.String, com.taobao.weex.WXSDKInstance> r12 = r10.mInstanceMap
            boolean r12 = r12.containsKey(r7)
            if (r12 != 0) goto L_0x013c
            java.util.Map<java.lang.String, com.taobao.weex.WXSDKInstance> r12 = r10.mInstanceMap
            r12.put(r7, r11)
        L_0x013c:
            java.lang.String r12 = "test->"
            java.lang.String r0 = "start preDownLoad :"
            android.util.Log.e(r12, r0)
            com.taobao.weex.common.WXRenderStrategy r12 = com.taobao.weex.common.WXRenderStrategy.APPEND_ASYNC
            r11.preDownLoad(r7, r13, r14, r12)
        L_0x0148:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.preLoad.WXPreLoadManager.preInit(boolean, java.lang.String, java.util.Map, java.lang.String):void");
    }
}
