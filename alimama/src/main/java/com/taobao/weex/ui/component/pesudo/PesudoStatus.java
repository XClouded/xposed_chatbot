package com.taobao.weex.ui.component.pesudo;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.taobao.weex.common.Constants;
import java.util.Map;

public class PesudoStatus {
    static final int CLASS_ACTIVE = 0;
    static final int CLASS_DISABLED = 3;
    static final int CLASS_ENABLED = 2;
    static final int CLASS_FOCUS = 1;
    private static final int SET = 1;
    private static final int UNSET = 0;
    private int[] mStatuses = new int[4];

    public PesudoStatus() {
        for (int i = 0; i < this.mStatuses.length; i++) {
            this.mStatuses[i] = 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setStatus(java.lang.String r7, boolean r8) {
        /*
            r6 = this;
            int r0 = r7.hashCode()
            r1 = -1487344704(0xffffffffa758ebc0, float:-3.0103822E-15)
            r2 = 1
            r3 = 2
            r4 = 3
            r5 = 0
            if (r0 == r1) goto L_0x003b
            r1 = -1482202954(0xffffffffa7a760b6, float:-4.6456665E-15)
            if (r0 == r1) goto L_0x0031
            r1 = 689157575(0x2913b5c7, float:3.2798224E-14)
            if (r0 == r1) goto L_0x0027
            r1 = 1758095582(0x68ca68de, float:7.64682E24)
            if (r0 == r1) goto L_0x001d
            goto L_0x0045
        L_0x001d:
            java.lang.String r0 = ":focus"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0045
            r7 = 3
            goto L_0x0046
        L_0x0027:
            java.lang.String r0 = ":enabled"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0045
            r7 = 2
            goto L_0x0046
        L_0x0031:
            java.lang.String r0 = ":disabled"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0045
            r7 = 1
            goto L_0x0046
        L_0x003b:
            java.lang.String r0 = ":active"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0045
            r7 = 0
            goto L_0x0046
        L_0x0045:
            r7 = -1
        L_0x0046:
            switch(r7) {
                case 0: goto L_0x0056;
                case 1: goto L_0x0052;
                case 2: goto L_0x004e;
                case 3: goto L_0x004a;
                default: goto L_0x0049;
            }
        L_0x0049:
            goto L_0x0059
        L_0x004a:
            r6.setStatus((int) r2, (boolean) r8)
            goto L_0x0059
        L_0x004e:
            r6.setStatus((int) r3, (boolean) r8)
            goto L_0x0059
        L_0x0052:
            r6.setStatus((int) r4, (boolean) r8)
            goto L_0x0059
        L_0x0056:
            r6.setStatus((int) r5, (boolean) r8)
        L_0x0059:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.pesudo.PesudoStatus.setStatus(java.lang.String, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void setStatus(int i, boolean z) {
        this.mStatuses[i] = z;
    }

    public boolean isSet(int i) {
        return this.mStatuses[i] == 1;
    }

    @Nullable
    public String getStatuses() {
        StringBuilder sb = new StringBuilder();
        if (isSet(0)) {
            sb.append(Constants.PSEUDO.ACTIVE);
        }
        if (isSet(3)) {
            sb.append(Constants.PSEUDO.DISABLED);
        }
        if (isSet(1) && !isSet(3)) {
            sb.append(Constants.PSEUDO.FOCUS);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    public Map<String, Object> updateStatusAndGetUpdateStyles(String str, boolean z, Map<String, Map<String, Object>> map, Map<String, Object> map2) {
        String statuses = getStatuses();
        setStatus(str, z);
        Map map3 = map.get(getStatuses());
        Map map4 = map.get(statuses);
        ArrayMap arrayMap = new ArrayMap();
        if (map4 != null) {
            arrayMap.putAll(map4);
        }
        for (String str2 : arrayMap.keySet()) {
            arrayMap.put(str2, map2.containsKey(str2) ? map2.get(str2) : "");
        }
        if (map3 != null) {
            for (Map.Entry entry : map3.entrySet()) {
                arrayMap.put(entry.getKey(), entry.getValue());
            }
        }
        return arrayMap;
    }
}
