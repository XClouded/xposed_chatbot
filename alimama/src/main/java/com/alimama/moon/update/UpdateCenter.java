package com.alimama.moon.update;

import android.content.Context;
import com.alibaba.android.update4mtl.Update4MTL;
import com.alibaba.android.update4mtl.UpdateRequestParams;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.service.BeanContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public final class UpdateCenter {
    private final Context appContext;
    private final IEventBus eventBus;

    @Inject
    public UpdateCenter(@Named("appContext") Context context, IEventBus iEventBus) {
        this.appContext = context;
        this.eventBus = iEventBus;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void init(@androidx.annotation.NonNull android.content.Context r13) {
        /*
            com.alimama.moon.update.LoggerServiceProxy r0 = new com.alimama.moon.update.LoggerServiceProxy
            r0.<init>(r13)
            com.alibaba.android.common.ServiceProxyFactory.registerProxy(r0)
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 2
            r4 = 1
            r5 = 0
            if (r1 == r2) goto L_0x003b
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0031
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x0027
            goto L_0x0045
        L_0x0027:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0045
            r0 = 2
            goto L_0x0046
        L_0x0031:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0045
            r0 = 1
            goto L_0x0046
        L_0x003b:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0045
            r0 = 0
            goto L_0x0046
        L_0x0045:
            r0 = -1
        L_0x0046:
            switch(r0) {
                case 0: goto L_0x0049;
                case 1: goto L_0x004d;
                case 2: goto L_0x004b;
                default: goto L_0x0049;
            }
        L_0x0049:
            r10 = 0
            goto L_0x004e
        L_0x004b:
            r10 = 2
            goto L_0x004e
        L_0x004d:
            r10 = 1
        L_0x004e:
            java.lang.String r0 = android.os.Environment.DIRECTORY_DOWNLOADS
            java.io.File r0 = android.os.Environment.getExternalStoragePublicDirectory(r0)
            java.lang.String r0 = r0.getAbsolutePath()
            com.alibaba.android.update4mtl.Update4MTL r1 = com.alibaba.android.update4mtl.Update4MTL.getInstance()
            com.alibaba.android.update4mtl.Update4MTL r6 = r1.setDownloadDirectory(r13, r0)
            java.lang.String r8 = "alimamamoon"
            java.lang.String r9 = "10002089@moon_android_7.3.4"
            java.lang.String r11 = "AllInOne"
            com.alibaba.android.anynetwork.plugin.allinone.AllInOneANService r12 = new com.alibaba.android.anynetwork.plugin.allinone.AllInOneANService
            r12.<init>(r13)
            r7 = r13
            r6.init(r7, r8, r9, r10, r11, r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.update.UpdateCenter.init(android.content.Context):void");
    }

    public void checkUpdate() {
        UpdateRequestParams updateRequestParams = new UpdateRequestParams();
        updateRequestParams.put(UpdateRequestParams.PARAM_USER_ID, ((SettingManager) BeanContext.get(SettingManager.class)).getUserId());
        Update4MTL.getInstance().execute(this.appContext, updateRequestParams, new DefaultUpdateCallback(this.eventBus));
    }
}
