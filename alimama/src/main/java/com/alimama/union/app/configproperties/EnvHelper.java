package com.alimama.union.app.configproperties;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.alimama.moon.utils.ISSharedPreferences;
import com.alimama.moon.utils.StringUtil;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;

public class EnvHelper {
    public static final String API_ENV_DEBUG = "debug";
    public static final String API_ENV_ONLINE = "online";
    public static final String API_ENV_PREPARE = "prepare";
    public static final String API_ENV_TEST = "test";
    private static final String DEFAULT_API_ENV = "online";
    private static final String TAG = "EnvHelper";
    private static volatile EnvHelper sInstance;
    private String mApiEnv;

    public static EnvHelper getInstance() {
        if (sInstance == null) {
            synchronized (EnvHelper.class) {
                if (sInstance == null) {
                    sInstance = new EnvHelper();
                }
            }
        }
        return sInstance;
    }

    private EnvHelper() {
        initEnv();
    }

    private void initEnv() {
        String deserializeEnv = deserializeEnv();
        if (TextUtils.isEmpty(deserializeEnv)) {
            changeApiEnv((String) StringUtil.optVal(ConfigProperties.getApiEnvironment(), "online"));
        } else {
            changeApiEnv(deserializeEnv);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean changeApiEnv(java.lang.String r4) {
        /*
            r3 = this;
            serializeEnv(r4)
            int r0 = r4.hashCode()
            r1 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r2 = 1
            if (r0 == r1) goto L_0x002c
            r1 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r0 == r1) goto L_0x0022
            r1 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r0 == r1) goto L_0x0018
            goto L_0x0036
        L_0x0018:
            java.lang.String r0 = "debug"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0036
            r0 = 2
            goto L_0x0037
        L_0x0022:
            java.lang.String r0 = "prepare"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0036
            r0 = 1
            goto L_0x0037
        L_0x002c:
            java.lang.String r0 = "online"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0036
            r0 = 0
            goto L_0x0037
        L_0x0036:
            r0 = -1
        L_0x0037:
            switch(r0) {
                case 0: goto L_0x003f;
                case 1: goto L_0x003f;
                case 2: goto L_0x003f;
                default: goto L_0x003a;
            }
        L_0x003a:
            java.lang.String r4 = "online"
            r3.mApiEnv = r4
            goto L_0x0041
        L_0x003f:
            r3.mApiEnv = r4
        L_0x0041:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.configproperties.EnvHelper.changeApiEnv(java.lang.String):boolean");
    }

    public String getCurrentApiEnv() {
        return this.mApiEnv;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003c A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003e A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCurrentApiEnvOfint() {
        /*
            r6 = this;
            java.lang.String r0 = r6.mApiEnv
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 2
            r4 = 1
            r5 = 0
            if (r1 == r2) goto L_0x002d
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0023
            r2 = 3556498(0x364492, float:4.983715E-39)
            if (r1 == r2) goto L_0x0019
            goto L_0x0037
        L_0x0019:
            java.lang.String r1 = "test"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 2
            goto L_0x0038
        L_0x0023:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 1
            goto L_0x0038
        L_0x002d:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 0
            goto L_0x0038
        L_0x0037:
            r0 = -1
        L_0x0038:
            switch(r0) {
                case 0: goto L_0x003f;
                case 1: goto L_0x003e;
                case 2: goto L_0x003c;
                default: goto L_0x003b;
            }
        L_0x003b:
            goto L_0x003f
        L_0x003c:
            r5 = 2
            goto L_0x003f
        L_0x003e:
            r5 = 1
        L_0x003f:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.configproperties.EnvHelper.getCurrentApiEnvOfint():int");
    }

    public boolean isDailyEnv() {
        return "debug".equals(this.mApiEnv);
    }

    public boolean isOnLineEnv() {
        return "online".equals(this.mApiEnv);
    }

    public boolean isPre() {
        return API_ENV_PREPARE.equals(this.mApiEnv);
    }

    public static void restartApp(final Context context) {
        final long j = TextUtils.equals("MI 2S", Build.MODEL) ? TBToast.Duration.MEDIUM : 1000;
        new Handler().post(new Runnable() {
            public void run() {
                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntentForPackage.addFlags(67108864);
                ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(1, System.currentTimeMillis() + j, PendingIntent.getActivity(context, 0, launchIntentForPackage, 268435456));
                Log.d(EnvHelper.TAG, "killProcess ---------");
                Process.killProcess(Process.myPid());
            }
        });
    }

    public static void serializeEnv(String str) {
        new ISSharedPreferences("env_for_develop").putString("env_for_test", str).commit();
    }

    private static String deserializeEnv() {
        return new ISSharedPreferences("env_for_develop").getString("env_for_test", "");
    }
}
