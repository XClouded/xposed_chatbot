package com.alibaba.android.prefetchx.core.jsmodule;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;
import anet.channel.util.HttpConstant;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Map;

public class PFJSModuleIntegration extends WXModule {
    public static final String PF_DEGENERATE_URL = "wx_prefetchx_degenerate_url";
    public static final String PF_DISABLE_EVOLVE = "wx_prefetchx_disable_evolve";
    private static volatile Boolean usePrefetchXUrlMapping;
    private String fatBundleUrl;
    private String fatWeexUrl;
    protected RemoteConfigSpec.IJSModuleRemoteConfig jsModuleRemoteConfig = PrefetchX.getInstance().getGlobalOnlineConfigManager().getJSModuleConfig();
    private final Object lock = new Object();
    private long prefetchxProcessStartTime = 0;
    private String thinBundleUrl;
    private String thinHostPath;

    @Nullable
    public String evolve(Context context, String str, String str2) {
        String str3;
        this.prefetchxProcessStartTime = SystemClock.uptimeMillis();
        String str4 = null;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        this.fatBundleUrl = str2;
        if (TextUtils.isEmpty(str)) {
            this.fatWeexUrl = str2;
        } else {
            this.fatWeexUrl = str;
        }
        if (!this.jsModuleRemoteConfig.isJSModuleEnable()) {
            PFLog.JSModule.w("PrefetchX disabled. and cost " + delta(), new Throwable[0]);
            return null;
        } else if (!PFJSModule.getInstance().isReady()) {
            PFLog.JSModule.w("PrefetchX not ready. and cost " + delta(), new Throwable[0]);
            PFMonitor.JSModule.fail("PrefetchX", PFConstant.JSModule.TAG_BUNDLE, argsEvolve(), PFConstant.JSModule.PF_JSMODULE_NOT_READY, "prefetchx is not ready");
            boolean isApkDebugable = WXEnvironment.isApkDebugable();
            return null;
        } else {
            Uri parse = Uri.parse(str2);
            if (parse == null || parse.getBooleanQueryParameter(PF_DISABLE_EVOLVE, false)) {
                return null;
            }
            Map<String, String> mappingUrls = this.jsModuleRemoteConfig.getMappingUrls();
            if (usePrefetchXUrlMapping == null && mappingUrls != null) {
                String str5 = mappingUrls.get("startVersion");
                String str6 = WXEnvironment.getConfig().get("appVersion");
                if (TextUtils.isEmpty(str5) || TextUtils.isEmpty(str6) || PFUtil.compareVersion(str6, str5) < 0) {
                    synchronized (this.lock) {
                        usePrefetchXUrlMapping = false;
                    }
                } else {
                    synchronized (this.lock) {
                        usePrefetchXUrlMapping = true;
                    }
                }
            }
            String str7 = parse.getHost() + parse.getPath();
            if (usePrefetchXUrlMapping.booleanValue() && mappingUrls != null && mappingUrls.containsKey(str7)) {
                String str8 = mappingUrls.get(str7);
                if (str8.contains("//")) {
                    str3 = str8;
                } else {
                    str3 = "https://" + str8;
                }
                Uri parse2 = Uri.parse(str3);
                String str9 = parse2.getHost() + parse2.getPath();
                StringBuilder sb = new StringBuilder();
                if (PFUtil.isDebug() || str8.contains("30")) {
                    sb.append("http");
                } else {
                    sb.append("https");
                }
                sb.append(HttpConstant.SCHEME_SPLIT);
                sb.append(parse2.getHost());
                if (parse2.getPort() > 0) {
                    sb.append(":");
                    sb.append(parse2.getPort());
                }
                if (!TextUtils.isEmpty(parse2.getPath())) {
                    sb.append(parse2.getPath());
                }
                if (!TextUtils.isEmpty(parse2.getQuery()) || !TextUtils.isEmpty(parse.getQuery())) {
                    sb.append("?");
                    if (!TextUtils.isEmpty(parse2.getQuery())) {
                        sb.append(parse2.getQuery());
                    }
                    if (!TextUtils.isEmpty(parse2.getQuery()) && !TextUtils.isEmpty(parse.getQuery())) {
                        sb.append("&");
                    }
                    if (!TextUtils.isEmpty(parse.getQuery())) {
                        sb.append(parse.getQuery());
                    }
                }
                if (!TextUtils.isEmpty(parse.getFragment())) {
                    sb.append("#");
                    sb.append(parse.getFragment());
                }
                str4 = sb.toString();
                this.thinBundleUrl = str4;
                this.thinHostPath = str9;
                String str10 = "PrefetchX evolved, " + str2 + ". and cost " + delta();
                WXLogUtils.w(str10);
                PFMonitor.JSModule.success(PFConstant.JSModule.TAG_BUNDLE, argsEvolve());
                if (WXEnvironment.isApkDebugable() && context != null) {
                    Toast.makeText(context, str10, 1).show();
                }
            }
            return str4;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x009c A[Catch:{ Throwable -> 0x013f }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<java.lang.String, java.lang.String> degenerate(android.content.Context r10, java.lang.String r11, @androidx.annotation.Nullable java.util.Map<java.lang.String, java.lang.String> r12) {
        /*
            r9 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            r0 = 2
            r2 = 3
            r3 = 0
            r4 = 1
            android.net.Uri r11 = android.net.Uri.parse(r11)     // Catch:{ Throwable -> 0x013f }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013f }
            r5.<init>()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r6 = r11.getHost()     // Catch:{ Throwable -> 0x013f }
            r5.append(r6)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r6 = r11.getPath()     // Catch:{ Throwable -> 0x013f }
            r5.append(r6)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r6 = "wx_prefetchx_degenerate_url"
            java.lang.String r6 = r11.getQueryParameter(r6)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r7 = ""
            boolean r8 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x013f }
            if (r8 != 0) goto L_0x007f
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013f }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r7 = "?"
            boolean r6 = r6.contains(r7)     // Catch:{ Throwable -> 0x013f }
            if (r6 != 0) goto L_0x0048
            java.lang.String r6 = "?"
            r5.append(r6)     // Catch:{ Throwable -> 0x013f }
        L_0x0048:
            java.util.Set r6 = r11.getQueryParameterNames()     // Catch:{ Throwable -> 0x013f }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ Throwable -> 0x013f }
        L_0x0050:
            boolean r7 = r6.hasNext()     // Catch:{ Throwable -> 0x013f }
            if (r7 == 0) goto L_0x007a
            java.lang.Object r7 = r6.next()     // Catch:{ Throwable -> 0x013f }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x013f }
            java.lang.String r8 = "wx_prefetchx_degenerate_url"
            boolean r8 = r7.equals(r8)     // Catch:{ Throwable -> 0x013f }
            if (r8 != 0) goto L_0x0050
            java.lang.String r8 = "&"
            r5.append(r8)     // Catch:{ Throwable -> 0x013f }
            r5.append(r7)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r8 = "="
            r5.append(r8)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r7 = r11.getQueryParameter(r7)     // Catch:{ Throwable -> 0x013f }
            r5.append(r7)     // Catch:{ Throwable -> 0x013f }
            goto L_0x0050
        L_0x007a:
            java.lang.String r7 = r5.toString()     // Catch:{ Throwable -> 0x013f }
            goto L_0x0097
        L_0x007f:
            java.lang.String r11 = r9.fatBundleUrl     // Catch:{ Throwable -> 0x013f }
            boolean r11 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Throwable -> 0x013f }
            if (r11 != 0) goto L_0x0099
            boolean r11 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x013f }
            if (r11 != 0) goto L_0x0099
            java.lang.String r11 = r9.thinHostPath     // Catch:{ Throwable -> 0x013f }
            boolean r11 = r5.equals(r11)     // Catch:{ Throwable -> 0x013f }
            if (r11 == 0) goto L_0x0099
            java.lang.String r7 = r9.fatBundleUrl     // Catch:{ Throwable -> 0x013f }
        L_0x0097:
            r11 = 1
            goto L_0x009a
        L_0x0099:
            r11 = 0
        L_0x009a:
            if (r11 == 0) goto L_0x015c
            r9.fatBundleUrl = r1     // Catch:{ Throwable -> 0x013f }
            java.lang.String r11 = "?"
            boolean r11 = r7.contains(r11)     // Catch:{ Throwable -> 0x013f }
            if (r11 == 0) goto L_0x00b8
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013f }
            r11.<init>()     // Catch:{ Throwable -> 0x013f }
            r11.append(r7)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r5 = "&wx_prefetchx_disable_evolve=true"
            r11.append(r5)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x013f }
            goto L_0x00c9
        L_0x00b8:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013f }
            r11.<init>()     // Catch:{ Throwable -> 0x013f }
            r11.append(r7)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r5 = "?wx_prefetchx_disable_evolve=true"
            r11.append(r5)     // Catch:{ Throwable -> 0x013f }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x013f }
        L_0x00c9:
            android.util.Pair r5 = new android.util.Pair     // Catch:{ Throwable -> 0x013f }
            java.lang.String r6 = r9.fatWeexUrl     // Catch:{ Throwable -> 0x013f }
            r5.<init>(r6, r11)     // Catch:{ Throwable -> 0x013f }
            com.alibaba.android.prefetchx.core.jsmodule.PFJSModule r1 = com.alibaba.android.prefetchx.core.jsmodule.PFJSModule.getInstance()     // Catch:{ Throwable -> 0x013c }
            r1.refresh()     // Catch:{ Throwable -> 0x013c }
            java.lang.String r1 = "PrefetchX"
            java.lang.String r6 = "PrefetchX_JSModule_In_Bundle"
            java.lang.Object[] r7 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x013c }
            java.lang.String r8 = r9.argsDegenerate(r12)     // Catch:{ Throwable -> 0x013c }
            r7[r3] = r8     // Catch:{ Throwable -> 0x013c }
            java.lang.String r8 = "-50005"
            r7[r4] = r8     // Catch:{ Throwable -> 0x013c }
            java.lang.String r8 = "prefetchx degenerate in weex_bundle"
            r7[r0] = r8     // Catch:{ Throwable -> 0x013c }
            com.alibaba.android.prefetchx.PFMonitor.JSModule.fail(r1, r6, r7)     // Catch:{ Throwable -> 0x013c }
            boolean r1 = com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ Throwable -> 0x013c }
            if (r1 == 0) goto L_0x011a
            if (r10 == 0) goto L_0x011a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013c }
            r1.<init>()     // Catch:{ Throwable -> 0x013c }
            java.lang.String r6 = "PrefetchX degenerate, "
            r1.append(r6)     // Catch:{ Throwable -> 0x013c }
            r1.append(r11)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r6 = ". wasted time "
            r1.append(r6)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r6 = r9.delta()     // Catch:{ Throwable -> 0x013c }
            r1.append(r6)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x013c }
            android.widget.Toast r10 = android.widget.Toast.makeText(r10, r1, r4)     // Catch:{ Throwable -> 0x013c }
            r10.show()     // Catch:{ Throwable -> 0x013c }
        L_0x011a:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013c }
            r10.<init>()     // Catch:{ Throwable -> 0x013c }
            java.lang.String r1 = "PrefetchX degenerate, "
            r10.append(r1)     // Catch:{ Throwable -> 0x013c }
            r10.append(r11)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r11 = ". wasted time "
            r10.append(r11)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r11 = r9.delta()     // Catch:{ Throwable -> 0x013c }
            r10.append(r11)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x013c }
            com.taobao.weex.utils.WXLogUtils.w(r10)     // Catch:{ Throwable -> 0x013c }
            r1 = r5
            goto L_0x015c
        L_0x013c:
            r10 = move-exception
            r1 = r5
            goto L_0x0140
        L_0x013f:
            r10 = move-exception
        L_0x0140:
            java.lang.String r11 = "error in replace to full bundle when js exception in rendering thin bundle by PrefetchX. will downgrade to h5."
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r11, (java.lang.Throwable) r10)
            java.lang.String r10 = "PrefetchX"
            java.lang.String r11 = "PrefetchX_JSModule_In_Bundle"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r12 = r9.argsDegenerate(r12)
            r2[r3] = r12
            java.lang.String r12 = "-50007"
            r2[r4] = r12
            java.lang.String r12 = "prefetchx degenerate exception"
            r2[r0] = r12
            com.alibaba.android.prefetchx.PFMonitor.JSModule.fail(r10, r11, r2)
        L_0x015c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.jsmodule.PFJSModuleIntegration.degenerate(android.content.Context, java.lang.String, java.util.Map):android.util.Pair");
    }

    public void onLowMemory(@Nullable String str) {
        try {
            PFJSModule.getInstance().onLowMemory();
        } catch (Throwable th) {
            WXLogUtils.e("error in onLowMemory of PrefetchX", th);
            PFMonitor.JSModule.fail("PrefetchX", PFConstant.JSModule.TAG_BUNDLE, str, PFConstant.JSModule.PF_JSMODULE_EXCEPTION, "prefetchx onLowMemory exception");
        }
    }

    private String argsEvolve() {
        HashMap hashMap = new HashMap(4);
        if (!TextUtils.isEmpty(this.fatBundleUrl)) {
            hashMap.put("fatBundle", this.fatBundleUrl);
        }
        if (!TextUtils.isEmpty(this.thinBundleUrl)) {
            hashMap.put("thinBundle", this.thinBundleUrl);
        }
        hashMap.put("evolovdCostTime", delta());
        return JSON.toJSONString(hashMap);
    }

    private String argsDegenerate(Map<String, String> map) {
        HashMap hashMap = new HashMap(8);
        if (!TextUtils.isEmpty(this.fatBundleUrl)) {
            hashMap.put("fatBundle", this.fatBundleUrl);
        }
        if (!TextUtils.isEmpty(this.thinBundleUrl)) {
            hashMap.put("thinBundle", this.thinBundleUrl);
        }
        hashMap.put("wastedTime", delta());
        if (map != null) {
            hashMap.putAll(map);
        }
        return JSON.toJSONString(hashMap);
    }

    private String delta() {
        return String.valueOf(SystemClock.uptimeMillis() - this.prefetchxProcessStartTime) + "ms";
    }
}
