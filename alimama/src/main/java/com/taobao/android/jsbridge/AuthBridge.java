package com.taobao.android.jsbridge;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.DataCallback;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.taobao.android.sso.R;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.weex.BuildConfig;

public class AuthBridge extends WVApiPlugin {
    private BroadcastReceiver mLoginReceiver;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("bridgeAlipaySSOLogin".equals(str)) {
            alipay(wVCallBackContext);
            return true;
        } else if ("bridgeAlipaySupported".equals(str)) {
            checkIfExist(wVCallBackContext);
            return true;
        } else if ("bridgeTaobaoSSOLogin".equals(str)) {
            taobao(wVCallBackContext);
            return true;
        } else if (!"bridgeTaobaoSupported".equals(str)) {
            return false;
        } else {
            checkIfTaobaoExist(wVCallBackContext);
            return true;
        }
    }

    private synchronized void checkIfTaobaoExist(WVCallBackContext wVCallBackContext) {
        try {
            if (SsoLogin.isSupportTBAuthBind(this.mContext)) {
                wVCallBackContext.success();
            } else {
                wVCallBackContext.error();
            }
        } catch (Throwable th) {
            th.printStackTrace();
            wVCallBackContext.error();
        }
        return;
    }

    private synchronized void checkIfExist(WVCallBackContext wVCallBackContext) {
        if (SsoLogin.isSupportAliaySso()) {
            wVCallBackContext.success();
        } else {
            wVCallBackContext.error();
        }
    }

    private synchronized void taobao(WVCallBackContext wVCallBackContext) {
        if (this.mContext instanceof Activity) {
            registerBroadcast(wVCallBackContext);
            SsoLogin.launchTao((Activity) this.mContext, new ISsoRemoteParam() {
                public String getServerTime() {
                    return BuildConfig.buildJavascriptFrameworkVersion;
                }

                public String getTtid() {
                    return DataProviderFactory.getDataProvider().getTTID();
                }

                public String getImei() {
                    return DataProviderFactory.getDataProvider().getImei();
                }

                public String getImsi() {
                    return DataProviderFactory.getDataProvider().getImsi();
                }

                public String getDeviceId() {
                    return DataProviderFactory.getDataProvider().getDeviceId();
                }

                public String getAppKey() {
                    return DataProviderFactory.getDataProvider().getAppkey();
                }

                public String getApdid() {
                    return AlipayInfo.getInstance().getApdid();
                }

                public String getUmidToken() {
                    return AppInfo.getInstance().getUmidToken();
                }

                public String getAtlas() {
                    return DataProviderFactory.getDataProvider().getEnvType() == LoginEnvType.DEV.getSdkEnvType() ? "daily" : "";
                }
            }, DataProviderFactory.getDataProvider().getResultActivityPath());
        } else {
            wVCallBackContext.error();
        }
    }

    private synchronized void alipay(WVCallBackContext wVCallBackContext) {
        if (this.mContext instanceof Activity) {
            registerBroadcast(wVCallBackContext);
            alipayAuth((Activity) this.mContext);
        } else {
            wVCallBackContext.error();
        }
    }

    public static void alipayAuth(final Activity activity) {
        AlipayInfo.getInstance().getApdidToken(new DataCallback<String>() {
            public void result(String str) {
                if (!TextUtils.isEmpty(str)) {
                    try {
                        SsoLogin.launchAlipay(activity, DataProviderFactory.getDataProvider().getAlipaySsoDesKey(), str, DataProviderFactory.getDataProvider().getContext().getPackageName(), DataProviderFactory.getDataProvider().getResultActivityPath());
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        Toast.makeText(DataProviderFactory.getApplicationContext(), DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0).show();
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
        }
    }

    private void registerBroadcast(final WVCallBackContext wVCallBackContext) {
        this.mLoginReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                LoginAction valueOf;
                if (intent != null && (valueOf = LoginAction.valueOf(intent.getAction())) != null) {
                    switch (AnonymousClass4.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[valueOf.ordinal()]) {
                        case 1:
                            AuthBridge.this.doWhenReceiveSuccess(wVCallBackContext);
                            return;
                        case 2:
                            AuthBridge.this.doWhenReceivedCancel(wVCallBackContext);
                            return;
                        case 3:
                        case 4:
                        case 5:
                            AuthBridge.this.doWhenReceivedCancel(wVCallBackContext);
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        LoginBroadcastHelper.registerLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
    }

    /* renamed from: com.taobao.android.jsbridge.AuthBridge$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.taobao.login4android.broadcast.LoginAction[] r0 = com.taobao.login4android.broadcast.LoginAction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction = r0
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_SUCCESS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x001f }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_CANCEL     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x002a }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_LOGIN_FAILED     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_ALIPAY_SSO_FAIL     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_ALIPAY_SSO_CANCEL     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.jsbridge.AuthBridge.AnonymousClass4.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public void doWhenReceiveSuccess(WVCallBackContext wVCallBackContext) {
        if (wVCallBackContext != null) {
            wVCallBackContext.success();
        }
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void doWhenReceivedCancel(WVCallBackContext wVCallBackContext) {
        if (wVCallBackContext != null) {
            wVCallBackContext.error();
        }
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
    }
}
