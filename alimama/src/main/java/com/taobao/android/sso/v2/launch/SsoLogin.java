package com.taobao.android.sso.v2.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.utils.Constants;
import com.alibaba.wireless.security.open.SecException;
import com.alipay.auth.mobile.api.IAlipayAuthEventHandler;
import com.alipay.auth.mobile.common.LoggerUtils;
import com.alipay.auth.mobile.exception.AlipayAuthIllegalArgumentException;
import com.alipay.auth.mobile.exception.PreAlipayAuthException;
import com.taobao.android.sso.v2.launch.alipay.AlipayAuthManager;
import com.taobao.android.sso.v2.launch.exception.SSOException;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.android.sso.v2.launch.model.SSOSlaveParam;
import com.taobao.android.sso.v2.launch.util.RSAKey;
import com.taobao.android.sso.v2.launch.util.Rsa;
import com.taobao.android.sso.v2.model.SSOIPCConstants;
import com.taobao.android.sso.v2.security.SSOSecurityService;
import java.util.Properties;
import java.util.UUID;
import mtopsdk.common.util.SymbolExpUtil;
import org.android.agoo.common.AgooConstants;

public class SsoLogin {
    public static final String TAG = "Login.TBSsoLogin";
    private static String uuid;

    public static void launchTao(Activity activity, ISsoRemoteParam iSsoRemoteParam) throws SSOException {
        launchTao(activity, iSsoRemoteParam, (String) null);
    }

    public static void launchTao(Activity activity, ISsoRemoteParam iSsoRemoteParam, String str) throws SSOException {
        UserTrackAdapter.sendUT("TaobaoAuth_Open");
        if (activity == null || iSsoRemoteParam == null) {
            UserTrackAdapter.sendUT("TaobaoAuth_Open_ParamError");
            throw new SSOException("activity and remoteParam can't be null");
        }
        SSOSlaveParam sSOSlaveParam = new SSOSlaveParam();
        sSOSlaveParam.appKey = iSsoRemoteParam.getAppKey();
        sSOSlaveParam.ssoVersion = SSOIPCConstants.CURRENT_SSO_VERSION;
        sSOSlaveParam.t = System.currentTimeMillis();
        sSOSlaveParam.targetUrl = getTargetUrl(activity);
        uuid = UUID.randomUUID().toString();
        activity.getSharedPreferences("uuid", 0).edit().putString("uuid", uuid).commit();
        try {
            if (!TextUtils.isEmpty(RSAKey.SSO_RSA_KEY)) {
                sSOSlaveParam.uuidKey = Rsa.encrypt(uuid, RSAKey.SSO_RSA_KEY);
                try {
                    sSOSlaveParam.sign = SSOSecurityService.getInstace(activity.getApplicationContext()).sign(iSsoRemoteParam.getAppKey(), sSOSlaveParam.toMap(), iSsoRemoteParam.getAtlas());
                    Intent intent = new Intent();
                    intent.setAction("com.taobao.open.intent.action.GETWAY");
                    StringBuilder sb = new StringBuilder("tbopen://m.taobao.com/sso?");
                    sb.append("appKey");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.appKey);
                    sb.append("&");
                    sb.append("ssoVersion");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.ssoVersion);
                    sb.append("&");
                    sb.append("t");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.t);
                    sb.append("&");
                    sb.append("uuidKey");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.uuidKey);
                    sb.append("&");
                    sb.append("targetUrl");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.targetUrl);
                    sb.append("&");
                    sb.append("sign");
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(sSOSlaveParam.sign);
                    if (!TextUtils.isEmpty(str)) {
                        sb.append("&");
                        sb.append(SSOIPCConstants.IPC_SLAVE_CALLBACK);
                        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                        sb.append(str);
                    }
                    intent.setData(Uri.parse(sb.toString()));
                    if (activity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        intent.setFlags(268468224);
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            UserTrackAdapter.sendUT("TaobaoAuth_Open_StartError");
                            throw new SSOException("startActivity Exception");
                        }
                    } else {
                        UserTrackAdapter.sendUT("TaobaoAuth_Open_Unsupported");
                        throw new SSOException("taobao isn't support sso v2");
                    }
                } catch (SecException e2) {
                    Properties properties = new Properties();
                    properties.setProperty("code", e2.getErrorCode() + "");
                    UserTrackAdapter.sendUT("TaobaoAuth_Open_SignError", properties);
                    throw new SSOException((Integer) 505, "errorCode=" + e2.getErrorCode());
                }
            } else {
                Log.e("Login.TBSsoLogin", "RSAKey == null");
                UserTrackAdapter.sendUT("TaobaoAuth_Open_EncError");
                throw new SSOException("getRsaKeyResult is null");
            }
        } catch (SSOException e3) {
            UserTrackAdapter.sendUT("TaobaoAuth_Open_EncError");
            throw new SSOException("get RSA exception===> " + e3.getMessage());
        }
    }

    public static void bindAuth(Activity activity, ISsoRemoteParam iSsoRemoteParam, String str) throws SSOException {
        UserTrackAdapter.sendUT("TaobaoAuth_Bind");
        if (activity != null && iSsoRemoteParam != null && !TextUtils.isEmpty(str)) {
            SSOSlaveParam sSOSlaveParam = new SSOSlaveParam();
            sSOSlaveParam.appKey = iSsoRemoteParam.getAppKey();
            sSOSlaveParam.ssoVersion = SSOIPCConstants.CURRENT_SSO_VERSION;
            sSOSlaveParam.t = System.currentTimeMillis();
            sSOSlaveParam.targetUrl = getTargetUrl(activity);
            uuid = UUID.randomUUID().toString();
            activity.getSharedPreferences("uuid", 0).edit().putString("uuid", uuid).commit();
            try {
                sSOSlaveParam.uuidKey = Rsa.encrypt(uuid, RSAKey.SSO_RSA_KEY);
                try {
                    sSOSlaveParam.sign = SSOSecurityService.getInstace(activity.getApplicationContext()).sign(iSsoRemoteParam.getAppKey(), sSOSlaveParam.toMap(), iSsoRemoteParam.getAtlas());
                    Intent intent = new Intent();
                    intent.setAction("com.taobao.open.intent.action.BIND_AUTH");
                    intent.setData(Uri.parse("tbopen://m.taobao.com/bind_auth?" + "appKey" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.appKey + "&" + "ssoVersion" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.ssoVersion + "&" + "t" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.t + "&" + "uuidKey" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.uuidKey + "&" + "targetUrl" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.targetUrl + "&" + "sign" + SymbolExpUtil.SYMBOL_EQUAL + sSOSlaveParam.sign + "&" + SSOIPCConstants.IPC_SLAVE_CALLBACK + SymbolExpUtil.SYMBOL_EQUAL + str + "&" + Constants.AUTH_TYPE + SymbolExpUtil.SYMBOL_EQUAL + Constants.AuthType.BIND_AUTH.getAuthType()));
                    if (activity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        intent.setFlags(268468224);
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            UserTrackAdapter.sendUT("TaobaoBind_Open_StartError");
                            throw new SSOException("startActivity Exception");
                        }
                    } else {
                        UserTrackAdapter.sendUT("TaobaoBind_Open_Unsupported");
                        throw new SSOException("taobao isn't support auth bind");
                    }
                } catch (SecException e2) {
                    Properties properties = new Properties();
                    properties.setProperty("code", e2.getErrorCode() + "");
                    UserTrackAdapter.sendUT("TaobaoAuth_Open_SignError", properties);
                    throw new SSOException((Integer) 505, "errorCode=" + e2.getErrorCode());
                }
            } catch (Exception unused) {
                throw new SSOException("RSAException");
            }
        }
    }

    private static String getTargetUrl(Context context) {
        return context.getPackageName();
    }

    public static void handleResultIntent(ILoginListener iLoginListener, Intent intent) {
        if (iLoginListener != null) {
            int i = -1;
            if (intent == null) {
                iLoginListener.onFail(new SSOException((Integer) -1, "intent is null"));
                return;
            }
            Bundle extras = intent.getExtras();
            if (extras != null) {
                try {
                    i = extras.getInt(SSOIPCConstants.APPLY_SSO_RESULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (i == 500) {
                    iLoginListener.onSuccess(intent);
                } else {
                    iLoginListener.onFail(new SSOException(Integer.valueOf(i), ""));
                }
            } else {
                broadcastFail();
                iLoginListener.onFail(new SSOException((Integer) -1, "bundle is null"));
            }
        }
    }

    private static void broadcastFail() {
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_FAIL_ACTION));
    }

    public static void launchAlipay(Activity activity) throws AlipayAuthIllegalArgumentException, PreAlipayAuthException {
        String alipaySsoDesKey = DataProviderFactory.getDataProvider().getAlipaySsoDesKey();
        String apdidToken = AlipayInfo.getInstance().getApdidToken();
        String targetUrl = getTargetUrl(activity);
        launchAlipay(activity, alipaySsoDesKey, apdidToken, targetUrl, getTargetUrl(activity) + ".ResultActivity");
    }

    public static void launchAlipay(Activity activity, String str) throws AlipayAuthIllegalArgumentException, PreAlipayAuthException {
        String apdidToken = AlipayInfo.getInstance().getApdidToken();
        String targetUrl = getTargetUrl(activity);
        launchAlipay(activity, str, apdidToken, targetUrl, getTargetUrl(activity) + ".ResultActivity");
    }

    public static void launchAlipay(Activity activity, String str, String str2, String str3, String str4) throws AlipayAuthIllegalArgumentException, PreAlipayAuthException {
        DataProviderFactory.getDataProvider().setAlipaySsoDesKey(str);
        AlipayAuthManager.getInstance().getAlipayAuth().openAlipayAuth(activity, str, str2, str3, str4);
    }

    public static void handleAlipaySSOIntent(Intent intent, IAlipayAuthEventHandler iAlipayAuthEventHandler) {
        if (isAlipayAuthCallBack(intent)) {
            try {
                AlipayAuthManager.getInstance().getAlipayAuth().handleIntent(intent, iAlipayAuthEventHandler);
            } catch (AlipayAuthIllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handleAlipaySSOResultIntent(Intent intent, final ILoginListener iLoginListener) {
        if (isAlipayAuthCallBack(intent)) {
            try {
                AlipayAuthManager.getInstance().getAlipayAuth().handleIntent(intent, new IAlipayAuthEventHandler() {
                    public void alipayAuthSuccess(String str) {
                        iLoginListener.onSuccess((Intent) null);
                    }

                    public void alipayAuthDidCancel() {
                        iLoginListener.onFail(new SSOException("-1"));
                    }

                    public void alipayAuthFailure() {
                        iLoginListener.onFail(new SSOException("-2"));
                    }
                });
            } catch (AlipayAuthIllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isAlipayAuthCallBack(Intent intent) {
        try {
            return AlipayAuthManager.getInstance().getAlipayAuth().isAlipayAuthCallBack(intent);
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean isSupportTBSsoV2(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(AgooConstants.TAOBAO_PACKAGE, 1);
            Intent intent = new Intent();
            intent.setAction("com.taobao.open.intent.action.GETWAY");
            intent.setData(Uri.parse(new StringBuilder("tbopen://m.taobao.com/sso?").toString()));
            return packageManager.queryIntentActivities(intent, 0).size() > 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isSupportTBAuthBind(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(AgooConstants.TAOBAO_PACKAGE, 1);
            Intent intent = new Intent();
            intent.setAction("com.taobao.open.intent.action.BIND_AUTH");
            intent.setData(Uri.parse(new StringBuilder("tbopen://m.taobao.com/bind_auth?").toString()));
            return packageManager.queryIntentActivities(intent, 0).size() > 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isTaobaoAppInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo(AgooConstants.TAOBAO_PACKAGE, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            LoggerUtils.e("Login.TBSsoLogin", "isTaobaoAppInstalled Exception", e);
            return false;
        } catch (Throwable th) {
            LoggerUtils.e("Login.TBSsoLogin", "isTaobaoAppInstalled Throwable", th);
            return false;
        }
    }

    public static boolean isSupportAliaySso() {
        return AlipayAuthManager.getInstance().getAlipayAuth().isAlipayAppInstalled() && AlipayAuthManager.getInstance().getAlipayAuth().isAlipayAppSurpportAPI();
    }
}
