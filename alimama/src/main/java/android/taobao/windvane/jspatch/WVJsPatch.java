package android.taobao.windvane.jspatch;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WVJsPatch implements WVEventListener {
    private static final String TAG = "WVJsPatch";
    private static WVJsPatch jsPatch;
    private Map<String, WVPatchConfig> configRuleMap = new HashMap();
    private Map<String, WVPatchConfig> ruleMap = new HashMap();

    public static synchronized WVJsPatch getInstance() {
        WVJsPatch wVJsPatch;
        synchronized (WVJsPatch.class) {
            if (jsPatch == null) {
                jsPatch = new WVJsPatch();
            }
            wVJsPatch = jsPatch;
        }
        return wVJsPatch;
    }

    private WVJsPatch() {
        WVEventService.getInstance().addEventListener(jsPatch);
    }

    public void addRuleWithPattern(String str, String str2) {
        WVPatchConfig wVPatchConfig = new WVPatchConfig();
        wVPatchConfig.jsString = str2;
        this.ruleMap.put(str, wVPatchConfig);
    }

    public void addRuleWithPattern(String str, String str2, String str3) {
        WVPatchConfig wVPatchConfig = new WVPatchConfig();
        wVPatchConfig.jsString = str3;
        wVPatchConfig.key = str;
        this.ruleMap.put(str2, wVPatchConfig);
    }

    public void removeRuleWithKey(String str) {
        if (this.ruleMap == null || this.ruleMap.isEmpty() || str == null) {
            TaoLog.w(TAG, "not need removeRuleWithKey");
            return;
        }
        for (Map.Entry next : this.ruleMap.entrySet()) {
            WVPatchConfig wVPatchConfig = (WVPatchConfig) next.getValue();
            if (!(wVPatchConfig == null || wVPatchConfig.key == null || !str.equals(wVPatchConfig.key))) {
                String str2 = (String) next.getKey();
                this.ruleMap.remove(str2);
                TaoLog.i(TAG, "removeRuleWithKey : " + str2);
            }
        }
    }

    public void removeAllRules() {
        this.ruleMap.clear();
    }

    public void removeAllConfigRules() {
        this.configRuleMap.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0075, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void config(java.lang.String r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r5.removeAllConfigRules()     // Catch:{ all -> 0x008e }
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x008e }
            if (r0 == 0) goto L_0x0013
            java.lang.String r6 = "WVJsPatch"
            java.lang.String r0 = "no jspatch"
            android.taobao.windvane.util.TaoLog.d(r6, r0)     // Catch:{ all -> 0x008e }
            monitor-exit(r5)
            return
        L_0x0013:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0076 }
            r0.<init>(r6)     // Catch:{ JSONException -> 0x0076 }
            java.util.Iterator r1 = r0.keys()     // Catch:{ JSONException -> 0x0076 }
        L_0x001c:
            boolean r2 = r1.hasNext()     // Catch:{ JSONException -> 0x0076 }
            if (r2 == 0) goto L_0x0047
            java.lang.Object r2 = r1.next()     // Catch:{ JSONException -> 0x0076 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ JSONException -> 0x0076 }
            java.lang.Object r3 = r0.get(r2)     // Catch:{ JSONException -> 0x0076 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ JSONException -> 0x0076 }
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ JSONException -> 0x0076 }
            if (r4 != 0) goto L_0x001c
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ JSONException -> 0x0076 }
            if (r4 != 0) goto L_0x001c
            android.taobao.windvane.jspatch.WVPatchConfig r4 = new android.taobao.windvane.jspatch.WVPatchConfig     // Catch:{ JSONException -> 0x0076 }
            r4.<init>()     // Catch:{ JSONException -> 0x0076 }
            r4.jsString = r3     // Catch:{ JSONException -> 0x0076 }
            java.util.Map<java.lang.String, android.taobao.windvane.jspatch.WVPatchConfig> r3 = r5.configRuleMap     // Catch:{ JSONException -> 0x0076 }
            r3.put(r2, r4)     // Catch:{ JSONException -> 0x0076 }
            goto L_0x001c
        L_0x0047:
            java.util.Map<java.lang.String, android.taobao.windvane.jspatch.WVPatchConfig> r0 = r5.ruleMap     // Catch:{ all -> 0x008e }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x008e }
            if (r0 == 0) goto L_0x0058
            java.lang.String r6 = "WVJsPatch"
            java.lang.String r0 = "jspatch config is Empty"
            android.taobao.windvane.util.TaoLog.d(r6, r0)     // Catch:{ all -> 0x008e }
            monitor-exit(r5)
            return
        L_0x0058:
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x008e }
            if (r0 == 0) goto L_0x0074
            java.lang.String r0 = "WVJsPatch"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x008e }
            r1.<init>()     // Catch:{ all -> 0x008e }
            java.lang.String r2 = "config success, config: "
            r1.append(r2)     // Catch:{ all -> 0x008e }
            r1.append(r6)     // Catch:{ all -> 0x008e }
            java.lang.String r6 = r1.toString()     // Catch:{ all -> 0x008e }
            android.taobao.windvane.util.TaoLog.d(r0, r6)     // Catch:{ all -> 0x008e }
        L_0x0074:
            monitor-exit(r5)
            return
        L_0x0076:
            java.lang.String r0 = "WVJsPatch"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x008e }
            r1.<init>()     // Catch:{ all -> 0x008e }
            java.lang.String r2 = "get config error, config: "
            r1.append(r2)     // Catch:{ all -> 0x008e }
            r1.append(r6)     // Catch:{ all -> 0x008e }
            java.lang.String r6 = r1.toString()     // Catch:{ all -> 0x008e }
            android.taobao.windvane.util.TaoLog.e(r0, r6)     // Catch:{ all -> 0x008e }
            monitor-exit(r5)
            return
        L_0x008e:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jspatch.WVJsPatch.config(java.lang.String):void");
    }

    public synchronized void execute(IWVWebView iWVWebView, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "start execute jspatch, url: " + str);
        }
        tryJsPatch(this.ruleMap, iWVWebView, str);
        tryJsPatch(this.configRuleMap, iWVWebView, str);
    }

    private boolean tryJsPatch(Map<String, WVPatchConfig> map, IWVWebView iWVWebView, String str) {
        if (map == null || map.isEmpty() || iWVWebView == null || TextUtils.isEmpty(str)) {
            TaoLog.d(TAG, "no jspatch need execute");
            return false;
        }
        for (Map.Entry next : map.entrySet()) {
            String str2 = (String) next.getKey();
            WVPatchConfig wVPatchConfig = (WVPatchConfig) next.getValue();
            if (wVPatchConfig == null) {
                TaoLog.w(TAG, "config is null");
            } else {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "start match rules, rule: " + str2);
                }
                if (wVPatchConfig.pattern == null) {
                    try {
                        wVPatchConfig.pattern = Pattern.compile(str2);
                    } catch (PatternSyntaxException unused) {
                        TaoLog.e(TAG, "compile rule error, pattern: " + str2);
                    }
                }
                if (wVPatchConfig.pattern != null && wVPatchConfig.pattern.matcher(str).matches()) {
                    if (!wVPatchConfig.jsString.startsWith("javascript:")) {
                        wVPatchConfig.jsString = "javascript:" + wVPatchConfig.jsString;
                    }
                    iWVWebView.evaluateJavascript(wVPatchConfig.jsString);
                    if (!TaoLog.getLogStatus()) {
                        return true;
                    }
                    TaoLog.d(TAG, "url matched, start execute jspatch, jsString: " + wVPatchConfig.jsString);
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, WVPatchConfig> getRuleMap() {
        return this.ruleMap;
    }

    public Map<String, WVPatchConfig> getConfigRuleMap() {
        return this.configRuleMap;
    }

    public synchronized void putConfig(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            WVPatchConfig wVPatchConfig = new WVPatchConfig();
            wVPatchConfig.jsString = str2;
            this.configRuleMap.put(str, wVPatchConfig);
            TaoLog.d(TAG, "putConfig, url: " + str + " js: " + wVPatchConfig.jsString);
        }
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        if (i == 1002) {
            execute(wVEventContext.webView, wVEventContext.url);
        }
        return new WVEventResult(false);
    }
}
