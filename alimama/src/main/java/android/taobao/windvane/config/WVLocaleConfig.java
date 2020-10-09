package android.taobao.windvane.config;

import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WVLocaleConfig {
    private static volatile WVLocaleConfig instance;
    public String mCurrentLocale = null;
    public String mLastLocale = null;

    public static WVLocaleConfig getInstance() {
        if (instance == null) {
            synchronized (WVLocaleConfig.class) {
                if (instance == null) {
                    instance = new WVLocaleConfig();
                }
            }
        }
        return instance;
    }

    public void init() {
        try {
            String stringVal = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "locale");
            if (!TextUtils.isEmpty(stringVal)) {
                JSONObject jSONObject = new JSONObject(stringVal);
                this.mCurrentLocale = jSONObject.optString("currentLocale", (String) null);
                this.mLastLocale = jSONObject.optString("lastLocale", (String) null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLocale(String str) {
        this.mCurrentLocale = str;
        save();
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean needFull() {
        /*
            r3 = this;
            java.lang.String r0 = r3.mCurrentLocale
            r1 = 1
            if (r0 == 0) goto L_0x001e
            java.lang.String r0 = r3.mLastLocale
            if (r0 != 0) goto L_0x000f
            java.lang.String r0 = r3.mCurrentLocale
            r3.mLastLocale = r0
        L_0x000d:
            r0 = 1
            goto L_0x001f
        L_0x000f:
            java.lang.String r0 = r3.mCurrentLocale
            java.lang.String r2 = r3.mLastLocale
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x001e
            java.lang.String r0 = r3.mCurrentLocale
            r3.mLastLocale = r0
            goto L_0x000d
        L_0x001e:
            r0 = 0
        L_0x001f:
            java.lang.String r2 = r3.mLastLocale
            if (r2 == 0) goto L_0x002b
            java.lang.String r2 = r3.mCurrentLocale
            if (r2 != 0) goto L_0x002b
            r0 = 0
            r3.mLastLocale = r0
            r0 = 1
        L_0x002b:
            if (r0 == 0) goto L_0x0030
            r3.save()
        L_0x0030:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.config.WVLocaleConfig.needFull():boolean");
    }

    public void save() {
        if (this.mCurrentLocale != null || this.mLastLocale != null) {
            final JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("currentLocale", this.mCurrentLocale);
                jSONObject.put("lastLocale", this.mLastLocale);
                WVThreadPool.getInstance().execute(new Runnable() {
                    public void run() {
                        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "locale", jSONObject.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
