package com.taobao.phenix.compat;

import com.taobao.phenix.strategy.ModuleStrategy;
import com.taobao.phenix.strategy.ModuleStrategySupplier;
import java.util.HashMap;
import java.util.Map;

public class TBModuleStrategySupplier implements ModuleStrategySupplier {
    private static final String BOOT_IMAGE = "boot-image";
    private static final String COMMON = "common";
    private static final String FESTIVAL_SKIN = "festival-skin";
    private static final String HOMEPAGE_ADS = "homepage-ads";
    private static final String TAOLIVE_GIFT = "taolive-gift";
    private static final String WANGXIN_CHAT = "wangxin-chat";
    private final Map<String, ModuleStrategy> mPriorityMap = new HashMap();

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ae  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.taobao.phenix.strategy.ModuleStrategy get(java.lang.String r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0009
            java.lang.String r9 = "common"
        L_0x0009:
            java.util.Map<java.lang.String, com.taobao.phenix.strategy.ModuleStrategy> r0 = r8.mPriorityMap     // Catch:{ all -> 0x00b5 }
            java.lang.Object r0 = r0.get(r9)     // Catch:{ all -> 0x00b5 }
            com.taobao.phenix.strategy.ModuleStrategy r0 = (com.taobao.phenix.strategy.ModuleStrategy) r0     // Catch:{ all -> 0x00b5 }
            if (r0 != 0) goto L_0x00b3
            java.lang.String r1 = "common"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x002c
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 17
            r5 = 1
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
        L_0x0029:
            r0 = r7
            goto L_0x00ac
        L_0x002c:
            java.lang.String r1 = "wangxin-chat"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x0043
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 34
            r5 = 1
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
            goto L_0x0029
        L_0x0043:
            java.lang.String r1 = "taolive-gift"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x005a
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 34
            r5 = 1
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
            goto L_0x0029
        L_0x005a:
            java.lang.String r1 = "homepage-ads"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x0071
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 51
            r5 = 1
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
            goto L_0x0029
        L_0x0071:
            java.lang.String r1 = "festival-skin"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x0088
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 68
            r5 = 0
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
            goto L_0x0029
        L_0x0088:
            java.lang.String r1 = "boot-image"
            boolean r1 = r1.equals(r9)     // Catch:{ all -> 0x00b5 }
            if (r1 == 0) goto L_0x009f
            com.taobao.phenix.strategy.ModuleStrategy r7 = new com.taobao.phenix.strategy.ModuleStrategy     // Catch:{ all -> 0x00b5 }
            r2 = 2
            r3 = 17
            r4 = 85
            r5 = 0
            r6 = 1
            r0 = r7
            r1 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00b5 }
            goto L_0x0029
        L_0x009f:
            java.lang.String r1 = "TBCompat4Phenix"
            java.lang.String r2 = "not found module strategy with name=%s"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b5 }
            r4 = 0
            r3[r4] = r9     // Catch:{ all -> 0x00b5 }
            com.taobao.phenix.common.UnitedLog.w(r1, r2, r3)     // Catch:{ all -> 0x00b5 }
        L_0x00ac:
            if (r0 == 0) goto L_0x00b3
            java.util.Map<java.lang.String, com.taobao.phenix.strategy.ModuleStrategy> r1 = r8.mPriorityMap     // Catch:{ all -> 0x00b5 }
            r1.put(r9, r0)     // Catch:{ all -> 0x00b5 }
        L_0x00b3:
            monitor-exit(r8)
            return r0
        L_0x00b5:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.TBModuleStrategySupplier.get(java.lang.String):com.taobao.phenix.strategy.ModuleStrategy");
    }
}
