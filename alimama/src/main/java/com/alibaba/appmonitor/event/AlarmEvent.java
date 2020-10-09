package com.alibaba.appmonitor.event;

import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.ReuseJSONArray;
import com.alibaba.appmonitor.pool.ReuseJSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class AlarmEvent extends Event {
    private static final int ERROR_MSG_MAX_LENGTH = 100;
    public Map<String, Integer> errorCodeCount;
    public Map<String, String> errorMsgMap;
    public int failCount = 0;
    public int successCount = 0;

    public synchronized void incrSuccess(Long l) {
        this.successCount++;
        super.commit(l);
    }

    public synchronized void incrFail(Long l) {
        this.failCount++;
        super.commit(l);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0066, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addError(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isBlank(r4)     // Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x0009
            monitor-exit(r3)
            return
        L_0x0009:
            java.util.Map<java.lang.String, java.lang.String> r0 = r3.errorMsgMap     // Catch:{ all -> 0x0067 }
            if (r0 != 0) goto L_0x0014
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0067 }
            r0.<init>()     // Catch:{ all -> 0x0067 }
            r3.errorMsgMap = r0     // Catch:{ all -> 0x0067 }
        L_0x0014:
            java.util.Map<java.lang.String, java.lang.Integer> r0 = r3.errorCodeCount     // Catch:{ all -> 0x0067 }
            if (r0 != 0) goto L_0x001f
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0067 }
            r0.<init>()     // Catch:{ all -> 0x0067 }
            r3.errorCodeCount = r0     // Catch:{ all -> 0x0067 }
        L_0x001f:
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isNotBlank(r5)     // Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x003c
            r0 = 0
            int r1 = r5.length()     // Catch:{ all -> 0x0067 }
            r2 = 100
            if (r1 <= r2) goto L_0x002f
            goto L_0x0033
        L_0x002f:
            int r2 = r5.length()     // Catch:{ all -> 0x0067 }
        L_0x0033:
            java.lang.String r5 = r5.substring(r0, r2)     // Catch:{ all -> 0x0067 }
            java.util.Map<java.lang.String, java.lang.String> r0 = r3.errorMsgMap     // Catch:{ all -> 0x0067 }
            r0.put(r4, r5)     // Catch:{ all -> 0x0067 }
        L_0x003c:
            java.util.Map<java.lang.String, java.lang.Integer> r5 = r3.errorCodeCount     // Catch:{ all -> 0x0067 }
            boolean r5 = r5.containsKey(r4)     // Catch:{ all -> 0x0067 }
            r0 = 1
            if (r5 != 0) goto L_0x004f
            java.util.Map<java.lang.String, java.lang.Integer> r5 = r3.errorCodeCount     // Catch:{ all -> 0x0067 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0067 }
            r5.put(r4, r0)     // Catch:{ all -> 0x0067 }
            goto L_0x0065
        L_0x004f:
            java.util.Map<java.lang.String, java.lang.Integer> r5 = r3.errorCodeCount     // Catch:{ all -> 0x0067 }
            java.util.Map<java.lang.String, java.lang.Integer> r1 = r3.errorCodeCount     // Catch:{ all -> 0x0067 }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x0067 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x0067 }
            int r1 = r1.intValue()     // Catch:{ all -> 0x0067 }
            int r1 = r1 + r0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0067 }
            r5.put(r4, r0)     // Catch:{ all -> 0x0067 }
        L_0x0065:
            monitor-exit(r3)
            return
        L_0x0067:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.AlarmEvent.addError(java.lang.String, java.lang.String):void");
    }

    public synchronized JSONObject dumpToJSONObject() {
        JSONObject dumpToJSONObject;
        dumpToJSONObject = super.dumpToJSONObject();
        dumpToJSONObject.put("successCount", (Object) Integer.valueOf(this.successCount));
        dumpToJSONObject.put("failCount", (Object) Integer.valueOf(this.failCount));
        if (this.errorCodeCount != null) {
            JSONArray jSONArray = (JSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
            for (Map.Entry next : this.errorCodeCount.entrySet()) {
                JSONObject jSONObject = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
                String str = (String) next.getKey();
                jSONObject.put("errorCode", (Object) str);
                jSONObject.put("errorCount", next.getValue());
                if (this.errorMsgMap.containsKey(str)) {
                    jSONObject.put(ILocatable.ERROR_MSG, (Object) this.errorMsgMap.get(str));
                }
                jSONArray.add(jSONObject);
            }
            dumpToJSONObject.put("errors", (Object) jSONArray);
        }
        return dumpToJSONObject;
    }

    public synchronized void clean() {
        super.clean();
        this.successCount = 0;
        this.failCount = 0;
        if (this.errorMsgMap != null) {
            this.errorMsgMap.clear();
        }
        if (this.errorCodeCount != null) {
            this.errorCodeCount.clear();
        }
    }
}
