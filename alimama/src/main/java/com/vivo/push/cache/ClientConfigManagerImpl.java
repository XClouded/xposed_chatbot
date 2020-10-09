package com.vivo.push.cache;

import android.content.Context;
import android.text.TextUtils;
import com.vivo.push.model.a;
import com.vivo.push.util.p;
import java.util.HashSet;
import java.util.Set;

public class ClientConfigManagerImpl implements e {
    private static final Object SLOCK = new Object();
    private static final String TAG = "ClientConfigManager";
    private static volatile ClientConfigManagerImpl sClientConfigManagerImpl;
    private a mAppConfigSettings = new a(this.mContext);
    private Context mContext;
    private f mPushConfigSettings = new f(this.mContext);

    public Set<String> getBlackEventList() {
        return null;
    }

    private ClientConfigManagerImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static ClientConfigManagerImpl getInstance(Context context) {
        if (sClientConfigManagerImpl == null) {
            synchronized (SLOCK) {
                if (sClientConfigManagerImpl == null) {
                    sClientConfigManagerImpl = new ClientConfigManagerImpl(context);
                }
            }
        }
        return sClientConfigManagerImpl;
    }

    public boolean isEnablePush() {
        prepareAppConfig();
        a c = this.mAppConfigSettings.c(this.mContext.getPackageName());
        if (c != null) {
            return "1".equals(c.b());
        }
        return true;
    }

    private void prepareAppConfig() {
        if (this.mAppConfigSettings == null) {
            this.mAppConfigSettings = new a(this.mContext);
        } else {
            this.mAppConfigSettings.c();
        }
    }

    public void clearPush() {
        this.mAppConfigSettings.d();
    }

    public boolean isInBlackList(long j) {
        String c = preparePushConfigSettings().c("BL");
        if (!TextUtils.isEmpty(c)) {
            for (String str : c.split(",")) {
                try {
                    if (!TextUtils.isEmpty(str) && Long.parseLong(str) == j) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public int getNotifyStyle() {
        try {
            String c = preparePushConfigSettings().c("DPL");
            if (!TextUtils.isEmpty(c)) {
                try {
                    return Integer.parseInt(c);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0021 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isCancleBroadcastReceiver() {
        /*
            r3 = this;
            com.vivo.push.cache.f r0 = r3.preparePushConfigSettings()
            java.lang.String r1 = "PSM"
            java.lang.String r0 = r0.c(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 0
            if (r1 != 0) goto L_0x001a
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x0016 }
            goto L_0x001b
        L_0x0016:
            r0 = move-exception
            r0.printStackTrace()
        L_0x001a:
            r0 = 0
        L_0x001b:
            r0 = r0 & 4
            if (r0 == 0) goto L_0x0021
            r0 = 1
            return r0
        L_0x0021:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.cache.ClientConfigManagerImpl.isCancleBroadcastReceiver():boolean");
    }

    private f preparePushConfigSettings() {
        if (this.mPushConfigSettings == null) {
            this.mPushConfigSettings = new f(this.mContext);
        } else {
            this.mPushConfigSettings.c();
        }
        return this.mPushConfigSettings;
    }

    public String getSuitTag() {
        return preparePushConfigSettings().c("CSPT");
    }

    public boolean isDebug() {
        this.mAppConfigSettings.c();
        return a.a(this.mAppConfigSettings.b());
    }

    public boolean isDebug(int i) {
        return a.a(i);
    }

    public String getValueByKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        this.mPushConfigSettings.c();
        return this.mPushConfigSettings.c(str);
    }

    public Set<Long> getWhiteLogList() {
        HashSet hashSet = new HashSet();
        String valueByKey = getValueByKey("WLL");
        if (!TextUtils.isEmpty(valueByKey)) {
            for (String parseLong : valueByKey.split(",")) {
                try {
                    hashSet.add(Long.valueOf(Long.parseLong(parseLong)));
                } catch (Exception unused) {
                }
            }
        }
        p.d(TAG, " initWhiteLogList " + hashSet);
        return hashSet;
    }
}
