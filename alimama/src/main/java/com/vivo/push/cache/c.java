package com.vivo.push.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.coloros.mcssdk.c.a;
import com.vivo.push.util.g;
import com.vivo.push.util.h;
import com.vivo.push.util.p;
import com.vivo.push.util.w;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: IAppManager */
public abstract class c<T> {
    public static final byte[] CRPYT_IV_BYTE = {34, 32, Framer.ENTER_FRAME_PREFIX, 37, Framer.ENTER_FRAME_PREFIX, 34, 32, Framer.ENTER_FRAME_PREFIX, Framer.ENTER_FRAME_PREFIX, Framer.ENTER_FRAME_PREFIX, 34, 41, 35, 35, 32, 32};
    public static final byte[] CRPYT_KEY_BYTE = {Framer.ENTER_FRAME_PREFIX, 34, 35, 36, 37, 38, 39, 40, 41, 32, 38, 37, Framer.ENTER_FRAME_PREFIX, 35, 34, Framer.ENTER_FRAME_PREFIX};
    private static int MAX_CLIENT_SAVE_LENGTH = 10000;
    protected static final String TAG = "IAppManager";
    protected static final Object sAppLock = new Object();
    protected Set<T> mAppDatas = new HashSet();
    protected Context mContext;
    private w mSharePreferenceManager;

    /* access modifiers changed from: protected */
    public abstract String generateStrByType();

    /* access modifiers changed from: protected */
    public abstract Set<T> parseAppStr(String str);

    /* access modifiers changed from: protected */
    public abstract String toAppStr(Set<T> set);

    public c(Context context) {
        this.mContext = context.getApplicationContext();
        this.mSharePreferenceManager = w.b();
        this.mSharePreferenceManager.a(this.mContext);
        loadData();
    }

    /* access modifiers changed from: protected */
    public void loadData() {
        synchronized (sAppLock) {
            h.a(generateStrByType());
            this.mAppDatas.clear();
            String a = this.mSharePreferenceManager.a(generateStrByType());
            if (TextUtils.isEmpty(a)) {
                p.d(TAG, "AppManager init strApps empty.");
            } else if (a.length() > MAX_CLIENT_SAVE_LENGTH) {
                p.d(TAG, "sync  strApps lenght too large");
                clearData();
            } else {
                try {
                    String str = new String(g.a(g.a(CRPYT_IV_BYTE), g.a(CRPYT_KEY_BYTE), Base64.decode(a, 2)), "utf-8");
                    p.d(TAG, "AppManager init strApps : " + str);
                    Set parseAppStr = parseAppStr(str);
                    if (parseAppStr != null) {
                        this.mAppDatas.addAll(parseAppStr);
                    }
                } catch (Exception e) {
                    clearData();
                    p.d(TAG, p.a((Throwable) e));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addData(T t) {
        synchronized (sAppLock) {
            Iterator<T> it = this.mAppDatas.iterator();
            while (it.hasNext()) {
                if (t.equals(it.next())) {
                    it.remove();
                }
            }
            this.mAppDatas.add(t);
            updateDataToSP(this.mAppDatas);
        }
    }

    /* access modifiers changed from: protected */
    public void removeData(T t) {
        synchronized (sAppLock) {
            boolean z = false;
            Iterator<T> it = this.mAppDatas.iterator();
            while (it.hasNext()) {
                if (t.equals(it.next())) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeDatas(Set<T> set) {
        synchronized (sAppLock) {
            Iterator<T> it = this.mAppDatas.iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (set.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addDatas(Set<T> set) {
        if (set != null) {
            synchronized (sAppLock) {
                Iterator<T> it = this.mAppDatas.iterator();
                while (it.hasNext()) {
                    if (set.contains(it.next())) {
                        it.remove();
                    }
                }
                this.mAppDatas.addAll(set);
                updateDataToSP(this.mAppDatas);
            }
        }
    }

    public String updateDataToSP(Set<T> set) {
        String appStr = toAppStr(set);
        try {
            String a = g.a(CRPYT_IV_BYTE);
            String a2 = g.a(CRPYT_KEY_BYTE);
            byte[] bytes = appStr.getBytes("utf-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(a2.getBytes("utf-8"), "AES");
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(1, secretKeySpec, new IvParameterSpec(a.getBytes("utf-8")));
            String encodeToString = Base64.encodeToString(instance.doFinal(bytes), 2);
            if (TextUtils.isEmpty(encodeToString) || encodeToString.length() <= MAX_CLIENT_SAVE_LENGTH) {
                p.d(TAG, "sync  strApps: " + encodeToString);
                this.mSharePreferenceManager.a(generateStrByType(), encodeToString);
                return appStr;
            }
            p.d(TAG, "sync  strApps lenght too large");
            clearData();
            return null;
        } catch (Exception e) {
            p.d(TAG, p.a((Throwable) e));
            clearData();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void clearData() {
        synchronized (sAppLock) {
            this.mAppDatas.clear();
            this.mSharePreferenceManager.c(generateStrByType());
        }
    }

    public boolean isEmpty() {
        return this.mAppDatas == null || this.mAppDatas.size() == 0;
    }
}
