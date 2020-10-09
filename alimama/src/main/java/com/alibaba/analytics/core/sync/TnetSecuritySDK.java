package com.alibaba.analytics.core.sync;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.taobao.orange.OConstant;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import java.lang.reflect.Method;

public class TnetSecuritySDK {
    private static volatile TnetSecuritySDK mTnetSecuritySDK;
    private String authcode = "";
    private Method getByteArrayMethod = null;
    private Object mDynamicDataStoreCompObj = null;
    private boolean mInitSecurityCheck = false;
    private Object mSecurityGuardManagerObj = null;
    private Object mStaticDataEncryptCompObj = null;
    private Method putByteArrayMethod = null;
    private Method staticBinarySafeDecryptNoB64Method = null;

    private TnetSecuritySDK() {
    }

    public static TnetSecuritySDK getInstance() {
        TnetSecuritySDK tnetSecuritySDK;
        if (mTnetSecuritySDK != null) {
            return mTnetSecuritySDK;
        }
        synchronized (TnetSecuritySDK.class) {
            if (mTnetSecuritySDK == null) {
                mTnetSecuritySDK = new TnetSecuritySDK();
                mTnetSecuritySDK.initSecurityCheck();
            }
            tnetSecuritySDK = mTnetSecuritySDK;
        }
        return tnetSecuritySDK;
    }

    public boolean getInitSecurityCheck() {
        Logger.d("", "mInitSecurityCheck", Boolean.valueOf(this.mInitSecurityCheck));
        return this.mInitSecurityCheck;
    }

    private synchronized void initSecurityCheck() {
        Logger.d();
        try {
            IUTRequestAuthentication requestAuthenticationInstance = Variables.getInstance().getRequestAuthenticationInstance();
            if (requestAuthenticationInstance instanceof UTBaseRequestAuthentication) {
                this.mInitSecurityCheck = false;
            }
            if (requestAuthenticationInstance != null) {
                Class<?> cls = Class.forName(OConstant.REFLECT_SECURITYGUARD);
                Class<?> cls2 = Class.forName("com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent");
                Class<?> cls3 = Class.forName("com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent");
                if (requestAuthenticationInstance instanceof UTSecurityThridRequestAuthentication) {
                    this.authcode = ((UTSecurityThridRequestAuthentication) requestAuthenticationInstance).getAuthcode();
                }
                if (cls == null || cls2 == null || cls3 == null) {
                    this.mInitSecurityCheck = false;
                } else {
                    this.mSecurityGuardManagerObj = cls.getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{Variables.getInstance().getContext()});
                    this.mStaticDataEncryptCompObj = cls.getMethod("getStaticDataEncryptComp", new Class[0]).invoke(this.mSecurityGuardManagerObj, new Object[0]);
                    this.mDynamicDataStoreCompObj = cls.getMethod("getDynamicDataStoreComp", new Class[0]).invoke(this.mSecurityGuardManagerObj, new Object[0]);
                    this.staticBinarySafeDecryptNoB64Method = cls2.getMethod("staticBinarySafeDecryptNoB64", new Class[]{Integer.TYPE, String.class, byte[].class, String.class});
                    this.putByteArrayMethod = cls3.getMethod("putByteArray", new Class[]{String.class, byte[].class});
                    this.getByteArrayMethod = cls3.getMethod("getByteArray", new Class[]{String.class});
                    this.mInitSecurityCheck = true;
                }
            }
        } catch (Throwable th) {
            this.mInitSecurityCheck = false;
            Logger.e("initSecurityCheck", "e.getCode", th.getCause(), th, th.getMessage());
        }
        return;
    }

    public byte[] staticBinarySafeDecryptNoB64(int i, String str, byte[] bArr) {
        if (this.staticBinarySafeDecryptNoB64Method == null || this.mStaticDataEncryptCompObj == null) {
            return null;
        }
        try {
            Object invoke = this.staticBinarySafeDecryptNoB64Method.invoke(this.mStaticDataEncryptCompObj, new Object[]{Integer.valueOf(i), str, bArr, this.authcode});
            Logger.i("", "mStaticDataEncryptCompObj", this.mStaticDataEncryptCompObj, UploadQueueMgr.MSGTYPE_INTERVAL, Integer.valueOf(i), "str", str, "bArr", bArr, "authcode", this.authcode, "obj", invoke);
            if (invoke == null) {
                return null;
            }
            return (byte[]) invoke;
        } catch (Throwable th) {
            Logger.e((String) null, th, new Object[0]);
            return null;
        }
    }

    public int putByteArray(String str, byte[] bArr) {
        if (this.putByteArrayMethod == null || this.mDynamicDataStoreCompObj == null) {
            return 0;
        }
        try {
            Object invoke = this.putByteArrayMethod.invoke(this.mDynamicDataStoreCompObj, new Object[]{str, bArr});
            if (invoke == null) {
                return 0;
            }
            int intValue = ((Integer) invoke).intValue();
            Logger.d("", "ret", Integer.valueOf(intValue));
            return intValue;
        } catch (Throwable th) {
            Logger.e((String) null, th, new Object[0]);
            return -1;
        }
    }

    public byte[] getByteArray(String str) {
        if (this.getByteArrayMethod == null || this.mDynamicDataStoreCompObj == null) {
            return null;
        }
        try {
            Object invoke = this.getByteArrayMethod.invoke(this.mDynamicDataStoreCompObj, new Object[]{str});
            if (invoke == null) {
                return null;
            }
            return (byte[]) invoke;
        } catch (Throwable th) {
            Logger.e((String) null, th, new Object[0]);
            return null;
        }
    }
}
