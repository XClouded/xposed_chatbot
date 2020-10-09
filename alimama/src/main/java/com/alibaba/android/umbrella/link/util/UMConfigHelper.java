package com.alibaba.android.umbrella.link.util;

import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.UMStringUtils;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UMConfigHelper {
    private final Map<String, String> memConfigs = new ConcurrentHashMap();

    public UMConfigHelper(final String str) {
        OrangeConfig.getInstance().registerListener(new String[]{str}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
            public void onConfigUpdate(String str, boolean z) {
                UMConfigHelper.this.refreshConfigs(str);
            }
        });
        refreshConfigs(str);
    }

    /* access modifiers changed from: private */
    public void refreshConfigs(String str) {
        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(str);
        this.memConfigs.clear();
        MapUtils.safePutAllString(this.memConfigs, configs);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0010, code lost:
        r2 = r1.memConfigs.get(r2);
     */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getOrDefault(@androidx.annotation.Nullable java.lang.String r2, @androidx.annotation.NonNull java.lang.String r3) {
        /*
            r1 = this;
            boolean r0 = com.alibaba.android.umbrella.link.UMStringUtils.isEmpty(r2)
            if (r0 == 0) goto L_0x0007
            return r3
        L_0x0007:
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.memConfigs
            boolean r0 = r0.containsKey(r2)
            if (r0 != 0) goto L_0x0010
            return r3
        L_0x0010:
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.memConfigs
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x001b
            return r3
        L_0x001b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.umbrella.link.util.UMConfigHelper.getOrDefault(java.lang.String, java.lang.String):java.lang.String");
    }

    public double getDouble(String str, double d) {
        try {
            return Double.parseDouble(getOrDefault(str, String.valueOf(d)));
        } catch (NumberFormatException unused) {
            return d;
        }
    }

    @Nullable
    public Integer getInt(String str) {
        String orDefault = getOrDefault(str, "");
        if (!UMStringUtils.isNotEmpty(orDefault)) {
            return null;
        }
        try {
            return Integer.valueOf(orDefault);
        } catch (Throwable unused) {
            return null;
        }
    }

    @Nullable
    public Boolean getBool(String str) {
        String orDefault = getOrDefault(str, "");
        if (UMStringUtils.isEmpty(orDefault)) {
            return null;
        }
        return Boolean.valueOf(orDefault);
    }
}
