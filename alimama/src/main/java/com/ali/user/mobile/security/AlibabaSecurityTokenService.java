package com.ali.user.mobile.security;

import android.content.ContextWrapper;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.safetoken.ISafeTokenComponent;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import mtopsdk.common.util.SymbolExpUtil;

public class AlibabaSecurityTokenService {
    private static final String TAG = "login.AlibabaSecurityTokenService";
    private static SecurityGuardManager mSecurityGuardManager;

    public static synchronized SecurityGuardManager getSecurityGuardManager() {
        SecurityGuardManager securityGuardManager;
        synchronized (AlibabaSecurityTokenService.class) {
            if (mSecurityGuardManager == null) {
                try {
                    mSecurityGuardManager = SecurityGuardManager.getInstance(new ContextWrapper(DataProviderFactory.getApplicationContext()));
                } catch (SecException e) {
                    AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "SecException", "SecException", "mSecurityGuardManager is exception");
                    TLogAdapter.e(TAG, "SecException", e);
                    try {
                        Properties properties = new Properties();
                        properties.setProperty("errorCode", "50001");
                        properties.setProperty("cause", "getSecurityException " + e + "errorCode," + e.getErrorCode());
                        UserTrackAdapter.sendUT("Event_saveTokenFail", properties);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (Throwable th) {
                    try {
                        Properties properties2 = new Properties();
                        properties2.setProperty("errorCode", "50001");
                        properties2.setProperty("cause", "getSecurityException" + th);
                        UserTrackAdapter.sendUT("Event_getSecurityException", properties2);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
            securityGuardManager = mSecurityGuardManager;
        }
        return securityGuardManager;
    }

    public static synchronized boolean saveToken(String str, String str2) {
        synchronized (AlibabaSecurityTokenService.class) {
            if (Debuggable.isDebug()) {
                TLogAdapter.d(TAG, "savekey=" + str + ",salt=" + str2);
            }
            if (TextUtils.isEmpty(str)) {
                try {
                    Properties properties = new Properties();
                    properties.setProperty("errorCode", "60001");
                    properties.setProperty("cause", "key = null");
                    UserTrackAdapter.sendUT("Event_saveTokenFail", properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            try {
                ISafeTokenComponent safeTokenComp = getSecurityGuardManager().getSafeTokenComp();
                if (safeTokenComp != null) {
                    AppMonitorAdapter.commitSuccess("AlibabaSecurityTokenService", "saveTokenSucc", "umid=" + AppInfo.getInstance().getUmidToken() + ",t=" + System.currentTimeMillis());
                    boolean saveToken = safeTokenComp.saveToken(str, str2, (String) null, 0);
                    return saveToken;
                }
                if (Debuggable.isDebug()) {
                    TLogAdapter.d(TAG, "ISafeTokenComponent is null");
                    AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "saveTokenFail", "saveToken", "ISafeTokenComponent is null");
                }
                try {
                    Properties properties2 = new Properties();
                    properties2.setProperty("errorCode", "60001");
                    properties2.setProperty("cause", "ISafeTokenComponent = null");
                    UserTrackAdapter.sendUT("Event_saveTokenFail", properties2);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } catch (SecException e3) {
                TLogAdapter.e(TAG, "saveToken SecException", e3);
                AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "saveTokenFail", "saveToken", e3.toString());
                try {
                    Properties properties3 = new Properties();
                    properties3.setProperty("errorCode", "60001");
                    properties3.setProperty("cause", "ISafeTokenComponent " + e3 + ",errorCode=" + e3.getErrorCode());
                    UserTrackAdapter.sendUT("Event_saveTokenFail", properties3);
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                return false;
            } catch (Throwable th) {
                TLogAdapter.e(TAG, "saveToken Throwable", th);
                AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "saveTokenFail", "saveToken", th.toString());
                try {
                    Properties properties4 = new Properties();
                    properties4.setProperty("errorCode", "60001");
                    properties4.setProperty("cause", "ISafeTokenComponent " + th);
                    UserTrackAdapter.sendUT("Event_saveTokenFail", properties4);
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    public static void removeSafeToken(String str) {
        if (Debuggable.isDebug()) {
            TLogAdapter.d(TAG, "removekey=" + str);
        }
        try {
            getSecurityGuardManager().getSafeTokenComp().removeToken(str);
            AppMonitorAdapter.commitSuccess("AlibabaSecurityTokenService", "removeSafeTokenSucc", "umid=" + AppInfo.getInstance().getUmidToken() + ",t=" + System.currentTimeMillis());
        } catch (SecException e) {
            TLogAdapter.e(TAG, "removeSafeToken SecException", e);
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "removeTokenFail", "removeToken", e.toString());
            try {
                Properties properties = new Properties();
                properties.setProperty("errorCode", "60002");
                properties.setProperty("cause", "removeToken " + e + ",errorCode=" + e.getErrorCode());
                UserTrackAdapter.sendUT("Event_removeTokenFail", properties);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                Properties properties2 = new Properties();
                properties2.setProperty("errorCode", "60002");
                properties2.setProperty("cause", "removeToke " + th);
                UserTrackAdapter.sendUT("Event_removeTokenFail", properties2);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            TLogAdapter.e(TAG, "removeSafeToken SecException", th);
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "removeTokenFail", "removeToken", th.toString());
        }
    }

    public static String sign(String str, TreeMap<String, String> treeMap) {
        if (Debuggable.isDebug()) {
            TLogAdapter.d(TAG, "signkey=" + str);
        }
        if (TextUtils.isEmpty(str)) {
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "signNullKey", "sign_error", "key is null");
            try {
                Properties properties = new Properties();
                properties.setProperty("errorCode", "60003");
                properties.setProperty("cause", "signError key is null");
                UserTrackAdapter.sendUT("Event_signFail", properties);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry next : treeMap.entrySet()) {
                sb.append((String) next.getKey());
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append((String) next.getValue());
                sb.append("&");
            }
            return sign(str, sb.substring(0, sb.length() - 1));
        }
    }

    public static String sign(String str, String str2) {
        try {
            String signWithToken = getSecurityGuardManager().getSafeTokenComp().signWithToken(str, str2.getBytes("UTF-8"), 0);
            if (signWithToken == null) {
                AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "signResultNull", "key=" + str, " signRes  = null");
                try {
                    Properties properties = new Properties();
                    properties.setProperty("errorCode", "60009");
                    properties.setProperty("cause", "signResultNull");
                    UserTrackAdapter.sendUT("Event_signNull", properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppMonitorAdapter.commitSuccess("AlibabaSecurityTokenService", "signSucc");
            }
            return signWithToken;
        } catch (UnsupportedEncodingException e2) {
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "signWithTokenFail", "UnsupportedEncodingException", e2.toString());
            e2.printStackTrace();
            try {
                Properties properties2 = new Properties();
                properties2.setProperty("errorCode", "60005");
                properties2.setProperty("cause", "signUnsupportedEncodingException" + e2);
                UserTrackAdapter.sendUT("Event_signFail", properties2);
                return null;
            } catch (Exception e3) {
                e3.printStackTrace();
                return null;
            }
        } catch (SecException e4) {
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "signWithTokenFail", "Exception", e4.toString());
            e4.printStackTrace();
            try {
                Properties properties3 = new Properties();
                properties3.setProperty("errorCode", "60005");
                properties3.setProperty("cause", "signExceptionError " + e4 + ",errorCode=" + e4.getErrorCode());
                UserTrackAdapter.sendUT("Event_signFail", properties3);
                return null;
            } catch (Exception e5) {
                e5.printStackTrace();
                return null;
            }
        } catch (Throwable th) {
            AppMonitorAdapter.commitFail("AlibabaSecurityTokenService", "signWithTokenFail", "Throwable", th.toString());
            th.printStackTrace();
            try {
                Properties properties4 = new Properties();
                properties4.setProperty("errorCode", "60005");
                properties4.setProperty("cause", "signThrowableError " + th);
                UserTrackAdapter.sendUT("Event_signFail", properties4);
                return null;
            } catch (Exception e6) {
                e6.printStackTrace();
                return null;
            }
        }
    }
}
