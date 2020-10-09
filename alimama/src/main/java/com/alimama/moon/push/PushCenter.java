package com.alimama.moon.push;

import android.app.Application;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.ILoginInfo;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.TaobaoRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushCenter {
    private static final String OPPO_APP_KEY = "cXMPL8XziWowCs8o4KOogc0CK";
    private static final String OPPO_APP_SECRET = "9Fdb2589AB2c4B25A45b44EfD9a16746";
    private static final String TAG = "PushCenter";
    private static final String XIAOMI_APP_ID = "2882303761517263160";
    private static final String XIAOMI_APP_KEY = "5771726338160";
    private static PushCenter instance;
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) PushCenter.class);
    private final Application appContext;
    private final String appKey;
    private final IAppReceiver appReceiver;
    private final ILoginInfo loginInfo;

    private PushCenter(Application application, String str, ILoginInfo iLoginInfo, IAppReceiver iAppReceiver) {
        this.appContext = application;
        this.appKey = str;
        this.loginInfo = iLoginInfo;
        this.appReceiver = iAppReceiver;
        init();
    }

    public static PushCenter getInstance(Application application, String str, ILoginInfo iLoginInfo, IAppReceiver iAppReceiver) {
        if (instance == null) {
            instance = new PushCenter(application, str, iLoginInfo, iAppReceiver);
        }
        return instance;
    }

    public static PushCenter getInstance() {
        return instance;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void init() {
        /*
            r9 = this;
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 2
            r4 = 1
            r5 = 0
            if (r1 == r2) goto L_0x0033
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0029
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x001f
            goto L_0x003d
        L_0x001f:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003d
            r0 = 2
            goto L_0x003e
        L_0x0029:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003d
            r0 = 0
            goto L_0x003e
        L_0x0033:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003d
            r0 = 1
            goto L_0x003e
        L_0x003d:
            r0 = -1
        L_0x003e:
            switch(r0) {
                case 0: goto L_0x0044;
                case 1: goto L_0x0045;
                case 2: goto L_0x0042;
                default: goto L_0x0041;
            }
        L_0x0041:
            goto L_0x0045
        L_0x0042:
            r5 = 2
            goto L_0x0045
        L_0x0044:
            r5 = 1
        L_0x0045:
            android.app.Application r0 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            com.taobao.agoo.TaobaoRegister.setEnv(r0, r5)     // Catch:{ Exception -> 0x0085 }
            org.slf4j.Logger r0 = logger     // Catch:{ Exception -> 0x0085 }
            java.lang.String r1 = "deviceId: {}"
            android.app.Application r2 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            java.lang.String r2 = com.taobao.accs.utl.UtilityImpl.getDeviceId(r2)     // Catch:{ Exception -> 0x0085 }
            r0.info((java.lang.String) r1, (java.lang.Object) r2)     // Catch:{ Exception -> 0x0085 }
            android.app.Application r3 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            java.lang.String r4 = "default"
            java.lang.String r5 = r9.appKey     // Catch:{ Exception -> 0x0085 }
            r6 = 0
            java.lang.String r7 = "10002089@moon_android_7.3.4"
            com.alimama.moon.push.PushCenter$1 r8 = new com.alimama.moon.push.PushCenter$1     // Catch:{ Exception -> 0x0085 }
            r8.<init>()     // Catch:{ Exception -> 0x0085 }
            com.taobao.agoo.TaobaoRegister.register(r3, r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0085 }
            android.app.Application r0 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            java.lang.String r1 = "2882303761517263160"
            java.lang.String r2 = "5771726338160"
            org.android.agoo.xiaomi.MiPushRegistar.register(r0, r1, r2)     // Catch:{ Exception -> 0x0085 }
            android.app.Application r0 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            java.lang.String r1 = "cXMPL8XziWowCs8o4KOogc0CK"
            java.lang.String r2 = "9Fdb2589AB2c4B25A45b44EfD9a16746"
            org.android.agoo.oppo.OppoRegister.register(r0, r1, r2)     // Catch:{ Exception -> 0x0085 }
            android.app.Application r0 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            org.android.agoo.huawei.HuaWeiRegister.register(r0)     // Catch:{ Exception -> 0x0085 }
            android.app.Application r0 = r9.appContext     // Catch:{ Exception -> 0x0085 }
            org.android.agoo.vivo.VivoRegister.register(r0)     // Catch:{ Exception -> 0x0085 }
            goto L_0x009a
        L_0x0085:
            r0 = move-exception
            java.lang.String r1 = "PushCenter"
            java.lang.String r2 = r0.toString()
            com.alimama.union.app.logger.NewMonitorLogger.Agoo.registerCrash(r1, r2)
            org.slf4j.Logger r1 = logger
            java.lang.String r2 = "register push service failed "
            java.lang.String r0 = r0.getMessage()
            r1.warn((java.lang.String) r2, (java.lang.Object) r0)
        L_0x009a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.push.PushCenter.init():void");
    }

    public void bindUser(String str) {
        TaobaoRegister.setAlias(this.appContext, str, (ICallback) null);
    }

    public void unBindUser() {
        TaobaoRegister.removeAlias(this.appContext, (ICallback) null);
    }
}
