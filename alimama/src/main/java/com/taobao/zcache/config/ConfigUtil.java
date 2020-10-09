package com.taobao.zcache.config;

import android.text.TextUtils;
import com.coloros.mcssdk.mode.CommandMessage;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigUtil {
    public static final HashMap<String, String> COMMON_TRANSFER_KEYS = new HashMap<>();

    public static boolean isForceDelete(int i) {
        return (i & CommandMessage.COMMAND_BASE) == 4096;
    }

    public static boolean isForceUpdate(int i) {
        return (i & 3840) == 256;
    }

    static {
        if (COMMON_TRANSFER_KEYS.size() == 0) {
            COMMON_TRANSFER_KEYS.put("packageZipPreviewPrefix", "zipPreviewPrefix");
            COMMON_TRANSFER_KEYS.put("packageZipPrefix", "zipPrefix");
            COMMON_TRANSFER_KEYS.put("packageAccessInterval", "accessInterval");
            COMMON_TRANSFER_KEYS.put("packageRemoveInterval", "removeInterval");
            COMMON_TRANSFER_KEYS.put("packageDownloadLimit", "downloadLimit");
            COMMON_TRANSFER_KEYS.put("packageMaxAppCount", "maxAppCount");
            COMMON_TRANSFER_KEYS.put("packagePriorityWeight", "priorityWeight");
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:73|74|75|76|77|(1:79)|80|111|108|71) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:76:0x024c */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x025c A[Catch:{ JSONException -> 0x027b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String resembleConfig(java.lang.String r14) {
        /*
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r1.<init>(r14)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r14 = "prefixes"
            java.lang.String r14 = r1.getString(r14)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r2 = "common"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r3 = "customs"
            java.lang.String r3 = r1.getString(r3)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r4 = "package"
            java.lang.String r1 = r1.getString(r4)     // Catch:{ JSONException -> 0x027b }
            boolean r4 = android.text.TextUtils.isEmpty(r14)     // Catch:{ JSONException -> 0x027b }
            if (r4 != 0) goto L_0x0276
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ JSONException -> 0x027b }
            if (r4 == 0) goto L_0x0030
            goto L_0x0276
        L_0x0030:
            java.lang.String r4 = "v"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x027b }
            r5.<init>()     // Catch:{ JSONException -> 0x027b }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x027b }
            r5.append(r6)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r6 = ""
            r5.append(r6)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r5 = r5.toString()     // Catch:{ JSONException -> 0x027b }
            r0.put(r4, r5)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r4 = "i"
            r5 = -1
            r0.put(r4, r5)     // Catch:{ JSONException -> 0x027b }
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r4.<init>(r2)     // Catch:{ JSONException -> 0x027b }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r5 = "apps"
            org.json.JSONObject r2 = r2.optJSONObject(r5)     // Catch:{ JSONException -> 0x027b }
            if (r2 != 0) goto L_0x0080
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r2.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x027b }
            r5.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r6 = "package error, package = ["
            r5.append(r6)     // Catch:{ JSONException -> 0x027b }
            r5.append(r1)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r1 = "]"
            r5.append(r1)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r1 = r5.toString()     // Catch:{ JSONException -> 0x027b }
            com.taobao.zcache.log.ZLog.e(r1)     // Catch:{ JSONException -> 0x027b }
        L_0x0080:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r1.<init>(r3)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r5 = "apps"
            org.json.JSONObject r1 = r1.optJSONObject(r5)     // Catch:{ JSONException -> 0x027b }
            if (r1 != 0) goto L_0x00ab
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r1.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x027b }
            r5.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r6 = "customs error, customs = ["
            r5.append(r6)     // Catch:{ JSONException -> 0x027b }
            r5.append(r3)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r3 = "]"
            r5.append(r3)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r3 = r5.toString()     // Catch:{ JSONException -> 0x027b }
            com.taobao.zcache.log.ZLog.e(r3)     // Catch:{ JSONException -> 0x027b }
        L_0x00ab:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r3.<init>(r14)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r5 = "rules"
            org.json.JSONObject r3 = r3.optJSONObject(r5)     // Catch:{ JSONException -> 0x027b }
            if (r3 != 0) goto L_0x00d6
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r3.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x027b }
            r5.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r6 = "prefix error, prefix = ["
            r5.append(r6)     // Catch:{ JSONException -> 0x027b }
            r5.append(r14)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r14 = "]"
            r5.append(r14)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r14 = r5.toString()     // Catch:{ JSONException -> 0x027b }
            com.taobao.zcache.log.ZLog.e(r14)     // Catch:{ JSONException -> 0x027b }
        L_0x00d6:
            org.json.JSONObject r14 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r14.<init>()     // Catch:{ JSONException -> 0x027b }
            java.util.Iterator r5 = r4.keys()     // Catch:{ JSONException -> 0x027b }
        L_0x00df:
            boolean r6 = r5.hasNext()     // Catch:{ JSONException -> 0x027b }
            if (r6 == 0) goto L_0x010f
            java.lang.Object r6 = r5.next()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ JSONException -> 0x027b }
            java.util.HashMap<java.lang.String, java.lang.String> r7 = COMMON_TRANSFER_KEYS     // Catch:{ JSONException -> 0x027b }
            java.util.Set r7 = r7.keySet()     // Catch:{ JSONException -> 0x027b }
            boolean r7 = r7.contains(r6)     // Catch:{ JSONException -> 0x027b }
            if (r7 == 0) goto L_0x0107
            java.util.HashMap<java.lang.String, java.lang.String> r7 = COMMON_TRANSFER_KEYS     // Catch:{ JSONException -> 0x027b }
            java.lang.Object r7 = r7.get(r6)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ JSONException -> 0x027b }
            java.lang.Object r6 = r4.get(r6)     // Catch:{ JSONException -> 0x027b }
            r14.put(r7, r6)     // Catch:{ JSONException -> 0x027b }
            goto L_0x00df
        L_0x0107:
            java.lang.Object r7 = r4.get(r6)     // Catch:{ JSONException -> 0x027b }
            r14.put(r6, r7)     // Catch:{ JSONException -> 0x027b }
            goto L_0x00df
        L_0x010f:
            java.lang.String r4 = "common"
            r0.put(r4, r14)     // Catch:{ JSONException -> 0x027b }
            java.util.HashMap r14 = new java.util.HashMap     // Catch:{ JSONException -> 0x027b }
            r14.<init>()     // Catch:{ JSONException -> 0x027b }
            java.util.Iterator r4 = r3.keys()     // Catch:{ JSONException -> 0x027b }
        L_0x011d:
            boolean r5 = r4.hasNext()     // Catch:{ JSONException -> 0x027b }
            if (r5 == 0) goto L_0x0169
            java.lang.Object r5 = r4.next()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ JSONException -> 0x027b }
            org.json.JSONObject r6 = r3.optJSONObject(r5)     // Catch:{ JSONException -> 0x027b }
            java.util.Iterator r7 = r6.keys()     // Catch:{ JSONException -> 0x027b }
        L_0x0131:
            boolean r8 = r7.hasNext()     // Catch:{ JSONException -> 0x027b }
            if (r8 == 0) goto L_0x011d
            java.lang.Object r8 = r7.next()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ JSONException -> 0x027b }
            java.lang.String r9 = r6.getString(r8)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r10 = "*"
            boolean r10 = r10.equals(r8)     // Catch:{ JSONException -> 0x027b }
            if (r10 != 0) goto L_0x0165
            java.lang.String r10 = ""
            boolean r10 = r10.equals(r8)     // Catch:{ JSONException -> 0x027b }
            if (r10 == 0) goto L_0x0152
            goto L_0x0165
        L_0x0152:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x027b }
            r10.<init>()     // Catch:{ JSONException -> 0x027b }
            r10.append(r5)     // Catch:{ JSONException -> 0x027b }
            r10.append(r8)     // Catch:{ JSONException -> 0x027b }
            java.lang.String r8 = r10.toString()     // Catch:{ JSONException -> 0x027b }
            putString2HashMapArrayListValue(r14, r9, r8)     // Catch:{ JSONException -> 0x027b }
            goto L_0x0131
        L_0x0165:
            putString2HashMapArrayListValue(r14, r9, r5)     // Catch:{ JSONException -> 0x027b }
            goto L_0x0131
        L_0x0169:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r3.<init>()     // Catch:{ JSONException -> 0x027b }
            java.util.Iterator r4 = r2.keys()     // Catch:{ JSONException -> 0x027b }
        L_0x0172:
            boolean r5 = r4.hasNext()     // Catch:{ Throwable -> 0x0230 }
            if (r5 == 0) goto L_0x0234
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Throwable -> 0x0230 }
            r5.<init>()     // Catch:{ Throwable -> 0x0230 }
            java.lang.Object r6 = r4.next()     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Throwable -> 0x0230 }
            java.lang.Object r7 = r14.get(r6)     // Catch:{ Throwable -> 0x0230 }
            if (r7 == 0) goto L_0x0199
            java.lang.String r7 = "p"
            org.json.JSONArray r8 = new org.json.JSONArray     // Catch:{ Throwable -> 0x0230 }
            java.lang.Object r9 = r14.get(r6)     // Catch:{ Throwable -> 0x0230 }
            java.util.Collection r9 = (java.util.Collection) r9     // Catch:{ Throwable -> 0x0230 }
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0230 }
            r5.put(r7, r8)     // Catch:{ Throwable -> 0x0230 }
        L_0x0199:
            org.json.JSONObject r7 = r2.getJSONObject(r6)     // Catch:{ Throwable -> 0x0230 }
            java.util.Iterator r8 = r7.keys()     // Catch:{ Throwable -> 0x0230 }
        L_0x01a1:
            boolean r9 = r8.hasNext()     // Catch:{ Throwable -> 0x0230 }
            r10 = 0
            if (r9 == 0) goto L_0x01ca
            java.lang.Object r9 = r8.next()     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r11 = "f"
            boolean r11 = r11.equals(r9)     // Catch:{ Throwable -> 0x0230 }
            if (r11 == 0) goto L_0x01a1
            int r8 = r7.getInt(r9)     // Catch:{ Throwable -> 0x0230 }
            boolean r8 = isForceUpdate(r8)     // Catch:{ Throwable -> 0x0230 }
            int r9 = r7.getInt(r9)     // Catch:{ Throwable -> 0x0230 }
            boolean r9 = isForceDelete(r9)     // Catch:{ Throwable -> 0x0230 }
            if (r9 == 0) goto L_0x01cb
            r10 = 1
            goto L_0x01cb
        L_0x01ca:
            r8 = 0
        L_0x01cb:
            if (r10 == 0) goto L_0x01d8
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r7 = "{}"
            r5.<init>(r7)     // Catch:{ Throwable -> 0x0230 }
            r3.put(r6, r5)     // Catch:{ Throwable -> 0x0230 }
            goto L_0x0172
        L_0x01d8:
            java.util.Iterator r9 = r7.keys()     // Catch:{ Throwable -> 0x0230 }
        L_0x01dc:
            boolean r10 = r9.hasNext()     // Catch:{ Throwable -> 0x0230 }
            if (r10 == 0) goto L_0x022b
            java.lang.Object r10 = r9.next()     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r11 = "v"
            boolean r11 = r11.equals(r10)     // Catch:{ Throwable -> 0x0230 }
            if (r11 == 0) goto L_0x0208
            java.lang.Object r10 = r7.get(r10)     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Throwable -> 0x0230 }
            java.lang.String r11 = "b"
            java.lang.String r12 = "."
            java.lang.String r13 = ""
            java.lang.String r10 = r10.replace(r12, r13)     // Catch:{ Throwable -> 0x0230 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0230 }
            r5.put(r11, r10)     // Catch:{ Throwable -> 0x0230 }
            goto L_0x01dc
        L_0x0208:
            java.lang.String r11 = "s"
            boolean r11 = r11.equals(r10)     // Catch:{ Throwable -> 0x0230 }
            if (r11 == 0) goto L_0x0223
            if (r8 == 0) goto L_0x021b
            java.lang.String r11 = "m"
            java.lang.Object r12 = r7.get(r10)     // Catch:{ Throwable -> 0x0230 }
            r5.put(r11, r12)     // Catch:{ Throwable -> 0x0230 }
        L_0x021b:
            java.lang.Object r11 = r7.get(r10)     // Catch:{ Throwable -> 0x0230 }
            r5.put(r10, r11)     // Catch:{ Throwable -> 0x0230 }
            goto L_0x01dc
        L_0x0223:
            java.lang.Object r11 = r7.get(r10)     // Catch:{ Throwable -> 0x0230 }
            r5.put(r10, r11)     // Catch:{ Throwable -> 0x0230 }
            goto L_0x01dc
        L_0x022b:
            r3.put(r6, r5)     // Catch:{ Throwable -> 0x0230 }
            goto L_0x0172
        L_0x0230:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ JSONException -> 0x027b }
        L_0x0234:
            java.util.Iterator r2 = r1.keys()     // Catch:{ JSONException -> 0x027b }
        L_0x0238:
            boolean r4 = r2.hasNext()     // Catch:{ JSONException -> 0x027b }
            if (r4 == 0) goto L_0x0270
            java.lang.Object r4 = r2.next()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ JSONException -> 0x027b }
            long r5 = r1.getLong(r4)     // Catch:{ JSONException -> 0x027b }
            r3.get(r4)     // Catch:{ JSONException -> 0x024c }
            goto L_0x0238
        L_0x024c:
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ JSONException -> 0x027b }
            r7.<init>()     // Catch:{ JSONException -> 0x027b }
            java.lang.String r8 = "s"
            r7.put(r8, r5)     // Catch:{ JSONException -> 0x027b }
            java.lang.Object r5 = r14.get(r4)     // Catch:{ JSONException -> 0x027b }
            if (r5 == 0) goto L_0x026c
            java.lang.String r5 = "p"
            org.json.JSONArray r6 = new org.json.JSONArray     // Catch:{ JSONException -> 0x027b }
            java.lang.Object r8 = r14.get(r4)     // Catch:{ JSONException -> 0x027b }
            java.util.Collection r8 = (java.util.Collection) r8     // Catch:{ JSONException -> 0x027b }
            r6.<init>(r8)     // Catch:{ JSONException -> 0x027b }
            r7.put(r5, r6)     // Catch:{ JSONException -> 0x027b }
        L_0x026c:
            r3.put(r4, r7)     // Catch:{ JSONException -> 0x027b }
            goto L_0x0238
        L_0x0270:
            java.lang.String r14 = "packs"
            r0.put(r14, r3)     // Catch:{ JSONException -> 0x027b }
            goto L_0x027f
        L_0x0276:
            java.lang.String r14 = r0.toString()     // Catch:{ JSONException -> 0x027b }
            return r14
        L_0x027b:
            r14 = move-exception
            r14.printStackTrace()
        L_0x027f:
            java.lang.String r14 = r0.toString()
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.zcache.config.ConfigUtil.resembleConfig(java.lang.String):java.lang.String");
    }

    public static void putString2HashMapArrayListValue(HashMap<String, ArrayList<String>> hashMap, String str, String str2) {
        ArrayList arrayList;
        if (hashMap != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (hashMap.get(str) == null) {
                arrayList = new ArrayList();
            } else {
                arrayList = hashMap.get(str);
            }
            arrayList.add(str2);
            hashMap.put(str, arrayList);
        }
    }
}
