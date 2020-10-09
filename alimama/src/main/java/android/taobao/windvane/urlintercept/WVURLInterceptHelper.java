package android.taobao.windvane.urlintercept;

import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;

import com.taobao.accs.common.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WVURLInterceptHelper {
    private static String TAG = "WVURLInterceptHelper";
    private static final String URL_FILTER_TAG = "_wv_url_hyid";

    /* JADX WARNING: Can't wrap try/catch for region: R(5:14|13|15|16|(0)(0)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0033 */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x004d A[SYNTHETIC, Splitter:B:20:0x004d] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x004b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized android.taobao.windvane.urlintercept.WVURLInterceptData.URLInfo parseByRule(java.lang.String r9, java.util.Set<android.taobao.windvane.urlintercept.WVURLInterceptData.RuleData> r10, java.util.Map<java.lang.String, java.util.regex.Pattern> r11) {
        /*
            java.lang.Class<android.taobao.windvane.urlintercept.WVURLInterceptHelper> r0 = android.taobao.windvane.urlintercept.WVURLInterceptHelper.class
            monitor-enter(r0)
            android.taobao.windvane.urlintercept.WVURLInterceptData$URLInfo r1 = new android.taobao.windvane.urlintercept.WVURLInterceptData$URLInfo     // Catch:{ all -> 0x00ea }
            r1.<init>()     // Catch:{ all -> 0x00ea }
            r1.url = r9     // Catch:{ all -> 0x00ea }
            java.util.Hashtable r2 = new java.util.Hashtable     // Catch:{ all -> 0x00ea }
            r2.<init>()     // Catch:{ all -> 0x00ea }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x00ea }
        L_0x0013:
            boolean r3 = r10.hasNext()     // Catch:{ all -> 0x00ea }
            if (r3 == 0) goto L_0x00e6
            java.lang.Object r3 = r10.next()     // Catch:{ all -> 0x00ea }
            android.taobao.windvane.urlintercept.WVURLInterceptData$RuleData r3 = (android.taobao.windvane.urlintercept.WVURLInterceptData.RuleData) r3     // Catch:{ all -> 0x00ea }
            java.lang.String r4 = r3.pattern     // Catch:{ all -> 0x00ea }
            java.lang.Object r5 = r11.get(r4)     // Catch:{ all -> 0x00ea }
            java.util.regex.Pattern r5 = (java.util.regex.Pattern) r5     // Catch:{ all -> 0x00ea }
            if (r5 != 0) goto L_0x0049
            java.util.regex.Pattern r6 = java.util.regex.Pattern.compile(r4)     // Catch:{ PatternSyntaxException -> 0x0033 }
            r11.put(r4, r6)     // Catch:{ PatternSyntaxException -> 0x0032 }
            r5 = r6
            goto L_0x0049
        L_0x0032:
            r5 = r6
        L_0x0033:
            java.lang.String r6 = TAG     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r7.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r8 = "pattern:"
            r7.append(r8)     // Catch:{ all -> 0x00ea }
            r7.append(r4)     // Catch:{ all -> 0x00ea }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x00ea }
            android.taobao.windvane.util.TaoLog.e(r6, r7)     // Catch:{ all -> 0x00ea }
        L_0x0049:
            if (r5 != 0) goto L_0x004d
            monitor-exit(r0)
            return r1
        L_0x004d:
            java.util.regex.Matcher r5 = r5.matcher(r9)     // Catch:{ all -> 0x00ea }
            boolean r6 = r5.matches()     // Catch:{ all -> 0x00ea }
            if (r6 == 0) goto L_0x0013
            boolean r10 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x00ea }
            if (r10 == 0) goto L_0x0073
            java.lang.String r10 = TAG     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r11.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r6 = "url matched for pattern "
            r11.append(r6)     // Catch:{ all -> 0x00ea }
            r11.append(r4)     // Catch:{ all -> 0x00ea }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x00ea }
            android.taobao.windvane.util.TaoLog.d(r10, r11)     // Catch:{ all -> 0x00ea }
        L_0x0073:
            int r10 = r3.target     // Catch:{ all -> 0x00ea }
            r1.code = r10     // Catch:{ all -> 0x00ea }
            r1.rule = r4     // Catch:{ all -> 0x00ea }
            int r10 = r3.rutype     // Catch:{ all -> 0x00ea }
            if (r10 != 0) goto L_0x00b5
            int r9 = r5.groupCount()     // Catch:{ all -> 0x00ea }
            java.util.Map<java.lang.String, java.lang.Integer> r10 = r3.indexp     // Catch:{ all -> 0x00ea }
            java.util.Set r10 = r10.entrySet()     // Catch:{ all -> 0x00ea }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x00ea }
        L_0x008b:
            boolean r11 = r10.hasNext()     // Catch:{ all -> 0x00ea }
            if (r11 == 0) goto L_0x00e6
            java.lang.Object r11 = r10.next()     // Catch:{ all -> 0x00ea }
            java.util.Map$Entry r11 = (java.util.Map.Entry) r11     // Catch:{ all -> 0x00ea }
            java.lang.Object r3 = r11.getValue()     // Catch:{ all -> 0x00ea }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x00ea }
            int r3 = r3.intValue()     // Catch:{ all -> 0x00ea }
            java.lang.String r4 = r5.group(r3)     // Catch:{ all -> 0x00ea }
            if (r9 < r3) goto L_0x008b
            boolean r3 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x00ea }
            if (r3 != 0) goto L_0x008b
            java.lang.Object r11 = r11.getKey()     // Catch:{ all -> 0x00ea }
            r2.put(r11, r4)     // Catch:{ all -> 0x00ea }
            goto L_0x008b
        L_0x00b5:
            r11 = 1
            if (r10 != r11) goto L_0x00e6
            java.util.Map<java.lang.String, java.lang.String> r10 = r3.namep     // Catch:{ all -> 0x00ea }
            java.util.Set r10 = r10.entrySet()     // Catch:{ all -> 0x00ea }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x00ea }
        L_0x00c2:
            boolean r11 = r10.hasNext()     // Catch:{ all -> 0x00ea }
            if (r11 == 0) goto L_0x00e6
            java.lang.Object r11 = r10.next()     // Catch:{ all -> 0x00ea }
            java.util.Map$Entry r11 = (java.util.Map.Entry) r11     // Catch:{ all -> 0x00ea }
            java.lang.Object r3 = r11.getKey()     // Catch:{ all -> 0x00ea }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x00ea }
            java.lang.String r3 = android.taobao.windvane.util.WVUrlUtil.getParamVal(r9, r3)     // Catch:{ all -> 0x00ea }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x00ea }
            if (r4 != 0) goto L_0x00c2
            java.lang.Object r11 = r11.getValue()     // Catch:{ all -> 0x00ea }
            r2.put(r11, r3)     // Catch:{ all -> 0x00ea }
            goto L_0x00c2
        L_0x00e6:
            r1.params = r2     // Catch:{ all -> 0x00ea }
            monitor-exit(r0)
            return r1
        L_0x00ea:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.urlintercept.WVURLInterceptHelper.parseByRule(java.lang.String, java.util.Set, java.util.Map):android.taobao.windvane.urlintercept.WVURLInterceptData$URLInfo");
    }

    public static List<WVURLInterceptData.RuleData> parseRuleData(String str) {
        List<WVURLInterceptData.RuleData> synchronizedList = Collections.synchronizedList(new ArrayList());
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jSONObject = apiResponse.parseJsonResult(str).success ? apiResponse.data : null;
        if (jSONObject != null) {
            try {
                ArrayList arrayList = new ArrayList();
                try {
                    WVServerConfig.URL_FILTER = jSONObject.optInt("lock", 0) == 0;
                    if (!jSONObject.has("rules")) {
                        return arrayList;
                    }
                    JSONArray jSONArray = jSONObject.getJSONArray("rules");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = (JSONObject) jSONArray.get(i);
                        WVURLInterceptData.RuleData ruleData = new WVURLInterceptData.RuleData();
                        ruleData.target = jSONObject2.getInt(Constants.KEY_TARGET);
                        ruleData.pattern = jSONObject2.getString("pattern");
                        ruleData.rutype = jSONObject2.optInt("rutype");
                        for (String split : jSONObject2.optString("indexp").split(",")) {
                            String[] split2 = split.split(":");
                            if (split2.length == 2 && TextUtils.isDigitsOnly(split2[1].trim())) {
                                ruleData.indexp.put(split2[0].trim(), Integer.valueOf(Integer.parseInt(split2[1].trim())));
                            }
                        }
                        for (String split3 : jSONObject2.optString("namep").split(",")) {
                            String[] split4 = split3.split(":");
                            if (split4.length == 2) {
                                ruleData.namep.put(split4[1].trim(), split4[0].trim());
                            }
                        }
                        arrayList.add(ruleData);
                    }
                    return arrayList;
                } catch (Exception unused) {
                    return arrayList;
                }
            } catch (Exception unused2) {
            }
        }
        return synchronizedList;
    }

    public static WVURLInterceptData.URLInfo parseByTag(String str) {
        String paramVal = WVUrlUtil.getParamVal(str, URL_FILTER_TAG);
        if (TextUtils.isEmpty(paramVal)) {
            return null;
        }
        WVURLInterceptData.URLInfo uRLInfo = new WVURLInterceptData.URLInfo();
        uRLInfo.url = str;
        if (!paramVal.contains(";")) {
            return null;
        }
        int indexOf = paramVal.indexOf(";");
        uRLInfo.code = Integer.parseInt(TextUtils.substring(paramVal, 0, indexOf));
        HashMap hashMap = new HashMap();
        for (String split : TextUtils.substring(paramVal, indexOf + 1, paramVal.length()).split(",")) {
            String[] split2 = split.split(":");
            if (split2.length == 2) {
                String paramVal2 = WVUrlUtil.getParamVal(str, split2[1].trim());
                if (!TextUtils.isEmpty(paramVal2)) {
                    hashMap.put(split2[0].trim(), paramVal2);
                }
            }
        }
        uRLInfo.params = hashMap;
        return uRLInfo;
    }
}
