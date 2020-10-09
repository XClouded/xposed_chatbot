package com.taobao.agoo;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.IAgooAppReceiver;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.ForeBackManager;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.control.RequestListener;
import com.taobao.agoo.control.data.AliasDO;
import com.taobao.agoo.control.data.RegisterDO;
import com.taobao.agoo.control.data.SwitchDO;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.Map;
import org.android.agoo.common.CallBack;
import org.android.agoo.common.Config;
import org.android.agoo.control.NotifManager;

public final class TaobaoRegister {
    private static final int EVENT_ID = 66001;
    static final String PREFERENCES = "Agoo_AppStore";
    static final String PROPERTY_APP_NOTIFICATION_CUSTOM_SOUND = "app_notification_custom_sound";
    static final String PROPERTY_APP_NOTIFICATION_ICON = "app_notification_icon";
    static final String PROPERTY_APP_NOTIFICATION_SOUND = "app_notification_sound";
    static final String PROPERTY_APP_NOTIFICATION_VIBRATE = "app_notification_vibrate";
    private static final String SERVICEID = "agooSend";
    protected static final String TAG = "TaobaoRegister";
    /* access modifiers changed from: private */
    public static boolean isRegisterSuccess;
    private static long lastCurrentLaunchTime;
    /* access modifiers changed from: private */
    public static RequestListener mRequestListener;

    @Deprecated
    public static void setBuilderSound(Context context, String str) {
    }

    @Deprecated
    public static void setNotificationIcon(Context context, int i) {
    }

    @Deprecated
    public static void setNotificationSound(Context context, boolean z) {
    }

    @Deprecated
    public static void setNotificationVibrate(Context context, boolean z) {
    }

    private TaobaoRegister() {
        throw new UnsupportedOperationException();
    }

    public static synchronized void setAccsConfigTag(Context context, String str) {
        synchronized (TaobaoRegister.class) {
            Config.mAccsConfigTag = str;
            AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
            if (configByTag != null) {
                ALog.i(TAG, "setAccsConfigTag", BindingXConstants.KEY_CONFIG, configByTag.toString());
                AdapterGlobalClientInfo.mAuthCode = configByTag.getAuthCode();
                Config.setAgooAppKey(context, configByTag.getAppKey());
                AdapterUtilityImpl.mAgooAppSecret = configByTag.getAppSecret();
                if (!TextUtils.isEmpty(AdapterUtilityImpl.mAgooAppSecret)) {
                    AdapterGlobalClientInfo.mSecurityType = 2;
                }
            } else {
                throw new RuntimeException("accs config not exist!! please set accs config first!!");
            }
        }
    }

    @Deprecated
    public static synchronized void register(Context context, String str, String str2, String str3, IRegister iRegister) throws AccsException {
        synchronized (TaobaoRegister.class) {
            register(context, "default", str, str2, str3, iRegister);
        }
    }

    public static synchronized void register(Context context, String str, String str2, String str3, String str4, IRegister iRegister) throws AccsException {
        Context context2 = context;
        String str5 = str;
        String str6 = str2;
        String str7 = str3;
        synchronized (TaobaoRegister.class) {
            if (context2 != null) {
                if (!TextUtils.isEmpty(str2)) {
                    if (!TextUtils.isEmpty(str)) {
                        if ((context.getApplicationInfo().flags & 2) != 0) {
                            ALog.isUseTlog = false;
                            anet.channel.util.ALog.setUseTlog(false);
                        }
                        ALog.i(TAG, "register", "appKey", str6, Constants.KEY_CONFIG_TAG, str5);
                        Context applicationContext = context.getApplicationContext();
                        Config.mAccsConfigTag = str5;
                        Config.setAgooAppKey(context, str6);
                        AdapterUtilityImpl.mAgooAppSecret = str7;
                        if (!TextUtils.isEmpty(str3)) {
                            AdapterGlobalClientInfo.mSecurityType = 2;
                        }
                        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
                        if (configByTag == null) {
                            new AccsClientConfig.Builder().setAppKey(str6).setAppSecret(str7).setTag(str).build();
                        } else {
                            AdapterGlobalClientInfo.mAuthCode = configByTag.getAuthCode();
                        }
                        IACCSManager accsInstance = ACCSManager.getAccsInstance(context, str6, str);
                        final Context context3 = applicationContext;
                        final Context context4 = context;
                        final IRegister iRegister2 = iRegister;
                        final String str8 = str2;
                        final String str9 = str4;
                        final IACCSManager iACCSManager = accsInstance;
                        accsInstance.bindApp(applicationContext, str2, str3, str4, new IAgooAppReceiver() {
                            public Map<String, String> getAllServices() {
                                return null;
                            }

                            public String getService(String str) {
                                return null;
                            }

                            public void onBindApp(int i) {
                            }

                            public void onBindUser(String str, int i) {
                            }

                            public void onData(String str, String str2, byte[] bArr) {
                            }

                            public void onSendData(String str, int i) {
                            }

                            public void onUnbindApp(int i) {
                            }

                            public void onUnbindUser(int i) {
                            }

                            public void onBindApp(int i, String str) {
                                try {
                                    ALog.i(TaobaoRegister.TAG, "onBindApp", "errorCode", Integer.valueOf(i));
                                    if (i == 200) {
                                        if (TaobaoRegister.mRequestListener == null) {
                                            RequestListener unused = TaobaoRegister.mRequestListener = new RequestListener(context3);
                                        }
                                        GlobalClientInfo.getInstance(context4).registerListener(TaobaoConstants.SERVICE_ID_DEVICECMD, (AccsDataListener) TaobaoRegister.mRequestListener);
                                        if (!RequestListener.mAgooBindCache.isAgooRegistered(context3.getPackageName()) || !UtilityImpl.notificationStateChanged(Constants.SP_CHANNEL_FILE_NAME, context4)) {
                                            byte[] buildRegister = RegisterDO.buildRegister(context3, str8, str9);
                                            if (buildRegister != null) {
                                                String sendRequest = iACCSManager.sendRequest(context3, new ACCSManager.AccsRequest((String) null, TaobaoConstants.SERVICE_ID_DEVICECMD, buildRegister, (String) null));
                                                if (TextUtils.isEmpty(sendRequest)) {
                                                    if (iRegister2 != null) {
                                                        iRegister2.onFailure(TaobaoConstants.REGISTER_ERROR, "accs channel disabled!");
                                                    }
                                                } else if (iRegister2 != null) {
                                                    TaobaoRegister.mRequestListener.mListeners.put(sendRequest, iRegister2);
                                                }
                                            } else if (iRegister2 != null) {
                                                iRegister2.onFailure(TaobaoConstants.REGISTER_ERROR, "req data null");
                                            }
                                        } else {
                                            ALog.i(TaobaoRegister.TAG, "agoo aready Registered return ", new Object[0]);
                                            boolean unused2 = TaobaoRegister.isRegisterSuccess = true;
                                            ForeBackManager.getManager().reportSaveClickMessage();
                                            if (iRegister2 != null) {
                                                iRegister2.onSuccess(Config.getDeviceToken(context3));
                                            }
                                        }
                                    } else if (iRegister2 != null) {
                                        iRegister2.onFailure(String.valueOf(i), "accs bindapp error!");
                                    }
                                } catch (Throwable th) {
                                    ALog.e(TaobaoRegister.TAG, "register onBindApp", th, new Object[0]);
                                }
                            }

                            public String getAppkey() {
                                return str8;
                            }
                        });
                        return;
                    }
                }
            }
            ALog.e(TAG, "register params null", "appkey", str6, Constants.KEY_CONFIG_TAG, str5);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void setAlias(android.content.Context r9, java.lang.String r10, com.taobao.agoo.ICallback r11) {
        /*
            java.lang.Class<com.taobao.agoo.TaobaoRegister> r0 = com.taobao.agoo.TaobaoRegister.class
            monitor-enter(r0)
            java.lang.String r1 = "TaobaoRegister"
            java.lang.String r2 = "setAlias"
            r3 = 2
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00f4 }
            java.lang.String r5 = "alias"
            r6 = 0
            r4[r6] = r5     // Catch:{ all -> 0x00f4 }
            r5 = 1
            r4[r5] = r10     // Catch:{ all -> 0x00f4 }
            com.taobao.accs.utl.ALog.i(r1, r2, r4)     // Catch:{ all -> 0x00f4 }
            java.lang.String r1 = org.android.agoo.common.Config.getDeviceToken(r9)     // Catch:{ all -> 0x00f4 }
            java.lang.String r2 = org.android.agoo.common.Config.getAgooAppKey(r9)     // Catch:{ all -> 0x00f4 }
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x00f4 }
            if (r4 != 0) goto L_0x00c1
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x00f4 }
            if (r4 != 0) goto L_0x00c1
            if (r9 == 0) goto L_0x00c1
            boolean r4 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x00f4 }
            if (r4 == 0) goto L_0x0033
            goto L_0x00c1
        L_0x0033:
            com.taobao.agoo.control.RequestListener r4 = mRequestListener     // Catch:{ Throwable -> 0x00b5 }
            if (r4 != 0) goto L_0x0042
            com.taobao.agoo.control.RequestListener r4 = new com.taobao.agoo.control.RequestListener     // Catch:{ Throwable -> 0x00b5 }
            android.content.Context r7 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x00b5 }
            r4.<init>(r7)     // Catch:{ Throwable -> 0x00b5 }
            mRequestListener = r4     // Catch:{ Throwable -> 0x00b5 }
        L_0x0042:
            com.taobao.agoo.control.AgooBindCache r4 = com.taobao.agoo.control.RequestListener.mAgooBindCache     // Catch:{ Throwable -> 0x00b5 }
            boolean r4 = r4.isAgooAliasBinded(r10)     // Catch:{ Throwable -> 0x00b5 }
            if (r4 == 0) goto L_0x0060
            java.lang.String r9 = "TaobaoRegister"
            java.lang.String r1 = "setAlias already set"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r3 = "alias"
            r2[r6] = r3     // Catch:{ Throwable -> 0x00b5 }
            r2[r5] = r10     // Catch:{ Throwable -> 0x00b5 }
            com.taobao.accs.utl.ALog.i(r9, r1, r2)     // Catch:{ Throwable -> 0x00b5 }
            if (r11 == 0) goto L_0x005e
            r11.onSuccess()     // Catch:{ Throwable -> 0x00b5 }
        L_0x005e:
            monitor-exit(r0)
            return
        L_0x0060:
            java.lang.String r3 = org.android.agoo.common.Config.getAccsConfigTag(r9)     // Catch:{ Throwable -> 0x00b5 }
            com.taobao.accs.IACCSManager r3 = com.taobao.accs.ACCSManager.getAccsInstance(r9, r2, r3)     // Catch:{ Throwable -> 0x00b5 }
            com.taobao.agoo.control.AgooBindCache r4 = com.taobao.agoo.control.RequestListener.mAgooBindCache     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r5 = r9.getPackageName()     // Catch:{ Throwable -> 0x00b5 }
            boolean r4 = r4.isAgooRegistered(r5)     // Catch:{ Throwable -> 0x00b5 }
            if (r4 == 0) goto L_0x00ab
            com.taobao.accs.client.GlobalClientInfo r4 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r5 = "AgooDeviceCmd"
            com.taobao.agoo.control.RequestListener r7 = mRequestListener     // Catch:{ Throwable -> 0x00b5 }
            r4.registerListener((java.lang.String) r5, (com.taobao.accs.base.AccsDataListener) r7)     // Catch:{ Throwable -> 0x00b5 }
            byte[] r1 = com.taobao.agoo.control.data.AliasDO.buildsetAlias(r2, r1, r10)     // Catch:{ Throwable -> 0x00b5 }
            com.taobao.accs.ACCSManager$AccsRequest r2 = new com.taobao.accs.ACCSManager$AccsRequest     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r4 = "AgooDeviceCmd"
            r5 = 0
            r2.<init>(r5, r4, r1, r5)     // Catch:{ Throwable -> 0x00b5 }
            java.lang.String r9 = r3.sendRequest(r9, r2)     // Catch:{ Throwable -> 0x00b5 }
            boolean r1 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x00b5 }
            if (r1 == 0) goto L_0x009f
            if (r11 == 0) goto L_0x00bf
            java.lang.String r9 = "504.1"
            java.lang.String r10 = "accs channel disabled!"
            r11.onFailure(r9, r10)     // Catch:{ Throwable -> 0x00b5 }
            goto L_0x00bf
        L_0x009f:
            if (r11 == 0) goto L_0x00bf
            r11.extra = r10     // Catch:{ Throwable -> 0x00b5 }
            com.taobao.agoo.control.RequestListener r10 = mRequestListener     // Catch:{ Throwable -> 0x00b5 }
            java.util.Map<java.lang.String, com.taobao.agoo.ICallback> r10 = r10.mListeners     // Catch:{ Throwable -> 0x00b5 }
            r10.put(r9, r11)     // Catch:{ Throwable -> 0x00b5 }
            goto L_0x00bf
        L_0x00ab:
            if (r11 == 0) goto L_0x00bf
            java.lang.String r9 = "504.1"
            java.lang.String r10 = "bindApp first!!"
            r11.onFailure(r9, r10)     // Catch:{ Throwable -> 0x00b5 }
            goto L_0x00bf
        L_0x00b5:
            r9 = move-exception
            java.lang.String r10 = "TaobaoRegister"
            java.lang.String r11 = "setAlias"
            java.lang.Object[] r1 = new java.lang.Object[r6]     // Catch:{ all -> 0x00f4 }
            com.taobao.accs.utl.ALog.e(r10, r11, r9, r1)     // Catch:{ all -> 0x00f4 }
        L_0x00bf:
            monitor-exit(r0)
            return
        L_0x00c1:
            if (r11 == 0) goto L_0x00ca
            java.lang.String r4 = "504.1"
            java.lang.String r7 = "input params null!!"
            r11.onFailure(r4, r7)     // Catch:{ all -> 0x00f4 }
        L_0x00ca:
            java.lang.String r11 = "TaobaoRegister"
            java.lang.String r4 = "setAlias param null"
            r7 = 8
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00f4 }
            java.lang.String r8 = "appkey"
            r7[r6] = r8     // Catch:{ all -> 0x00f4 }
            r7[r5] = r2     // Catch:{ all -> 0x00f4 }
            java.lang.String r2 = "deviceId"
            r7[r3] = r2     // Catch:{ all -> 0x00f4 }
            r2 = 3
            r7[r2] = r1     // Catch:{ all -> 0x00f4 }
            r1 = 4
            java.lang.String r2 = "alias"
            r7[r1] = r2     // Catch:{ all -> 0x00f4 }
            r1 = 5
            r7[r1] = r10     // Catch:{ all -> 0x00f4 }
            r10 = 6
            java.lang.String r1 = "context"
            r7[r10] = r1     // Catch:{ all -> 0x00f4 }
            r10 = 7
            r7[r10] = r9     // Catch:{ all -> 0x00f4 }
            com.taobao.accs.utl.ALog.e(r11, r4, r7)     // Catch:{ all -> 0x00f4 }
            monitor-exit(r0)
            return
        L_0x00f4:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.agoo.TaobaoRegister.setAlias(android.content.Context, java.lang.String, com.taobao.agoo.ICallback):void");
    }

    public static synchronized void removeAlias(Context context, String str, ICallback iCallback) {
        synchronized (TaobaoRegister.class) {
            ALog.i(TAG, AliasDO.JSON_CMD_REMOVEALIAS, new Object[0]);
            try {
                String deviceToken = Config.getDeviceToken(context);
                String agooAppKey = Config.getAgooAppKey(context);
                if (!TextUtils.isEmpty(agooAppKey) && !TextUtils.isEmpty(deviceToken) && context != null) {
                    if (!TextUtils.isEmpty(str)) {
                        IACCSManager accsInstance = ACCSManager.getAccsInstance(context, agooAppKey, Config.getAccsConfigTag(context));
                        if (mRequestListener == null) {
                            mRequestListener = new RequestListener(context.getApplicationContext());
                        }
                        GlobalClientInfo.getInstance(context).registerListener(TaobaoConstants.SERVICE_ID_DEVICECMD, (AccsDataListener) mRequestListener);
                        String sendRequest = accsInstance.sendRequest(context, new ACCSManager.AccsRequest((String) null, TaobaoConstants.SERVICE_ID_DEVICECMD, AliasDO.buildremoveAliasByName(agooAppKey, deviceToken, str), (String) null));
                        if (TextUtils.isEmpty(sendRequest)) {
                            if (iCallback != null) {
                                iCallback.onFailure(TaobaoConstants.ALIAS_ERROR, "accs channel disabled!");
                            }
                        } else if (iCallback != null) {
                            mRequestListener.mListeners.put(sendRequest, iCallback);
                        }
                    }
                }
                if (iCallback != null) {
                    iCallback.onFailure(TaobaoConstants.ALIAS_ERROR, "input params null!!");
                }
                ALog.e(TAG, "setAlias param null", "appkey", agooAppKey, "deviceId", deviceToken, "alias", str, "context", context);
                return;
            } catch (Throwable th) {
                ALog.e(TAG, AliasDO.JSON_CMD_REMOVEALIAS, th, new Object[0]);
            }
        }
        return;
    }

    public static synchronized void removeAlias(Context context, ICallback iCallback) {
        synchronized (TaobaoRegister.class) {
            ALog.i(TAG, AliasDO.JSON_CMD_REMOVEALIAS, new Object[0]);
            try {
                String deviceToken = Config.getDeviceToken(context);
                String pushAliasToken = Config.getPushAliasToken(context);
                String agooAppKey = Config.getAgooAppKey(context);
                if (!TextUtils.isEmpty(agooAppKey) && !TextUtils.isEmpty(deviceToken) && context != null) {
                    if (!TextUtils.isEmpty(pushAliasToken)) {
                        IACCSManager accsInstance = ACCSManager.getAccsInstance(context, agooAppKey, Config.getAccsConfigTag(context));
                        if (mRequestListener == null) {
                            mRequestListener = new RequestListener(context.getApplicationContext());
                        }
                        GlobalClientInfo.getInstance(context).registerListener(TaobaoConstants.SERVICE_ID_DEVICECMD, (AccsDataListener) mRequestListener);
                        String sendRequest = accsInstance.sendRequest(context, new ACCSManager.AccsRequest((String) null, TaobaoConstants.SERVICE_ID_DEVICECMD, AliasDO.buildremoveAlias(agooAppKey, deviceToken, pushAliasToken), (String) null));
                        if (TextUtils.isEmpty(sendRequest)) {
                            if (iCallback != null) {
                                iCallback.onFailure(TaobaoConstants.ALIAS_ERROR, "accs channel disabled!");
                            }
                        } else if (iCallback != null) {
                            mRequestListener.mListeners.put(sendRequest, iCallback);
                        }
                    }
                }
                if (iCallback != null) {
                    iCallback.onFailure(TaobaoConstants.ALIAS_ERROR, "input params null!!");
                }
                ALog.e(TAG, "setAlias param null", "appkey", agooAppKey, "deviceId", deviceToken, AliasDO.JSON_PUSH_USER_TOKEN, pushAliasToken, "context", context);
                return;
            } catch (Throwable th) {
                ALog.e(TAG, AliasDO.JSON_CMD_REMOVEALIAS, th, new Object[0]);
            }
        }
        return;
    }

    @Deprecated
    public static void bindAgoo(Context context, String str, String str2, CallBack callBack) {
        bindAgoo(context, (ICallback) null);
    }

    @Deprecated
    public static void unBindAgoo(Context context, String str, String str2, CallBack callBack) {
        unbindAgoo(context, (ICallback) null);
    }

    private static synchronized void sendSwitch(Context context, ICallback iCallback, boolean z) {
        synchronized (TaobaoRegister.class) {
            try {
                String deviceToken = Config.getDeviceToken(context);
                String agooAppKey = Config.getAgooAppKey(context);
                String deviceId = UtilityImpl.getDeviceId(context);
                if (!TextUtils.isEmpty(agooAppKey) && context != null) {
                    if (!TextUtils.isEmpty(deviceToken) || !TextUtils.isEmpty(deviceId)) {
                        IACCSManager accsInstance = ACCSManager.getAccsInstance(context, agooAppKey, Config.getAccsConfigTag(context));
                        if (mRequestListener == null) {
                            mRequestListener = new RequestListener(context.getApplicationContext());
                        }
                        GlobalClientInfo.getInstance(context).registerListener(TaobaoConstants.SERVICE_ID_DEVICECMD, (AccsDataListener) mRequestListener);
                        String sendRequest = accsInstance.sendRequest(context, new ACCSManager.AccsRequest((String) null, TaobaoConstants.SERVICE_ID_DEVICECMD, SwitchDO.buildSwitchDO(agooAppKey, deviceToken, deviceId, z), (String) null));
                        if (TextUtils.isEmpty(sendRequest)) {
                            if (iCallback != null) {
                                iCallback.onFailure(TaobaoConstants.BINDAGOO_ERROR, "accs channel disabled!");
                            }
                        } else if (iCallback != null) {
                            mRequestListener.mListeners.put(sendRequest, iCallback);
                        }
                    }
                }
                if (iCallback != null) {
                    iCallback.onFailure(TaobaoConstants.UNBINDAGOO_ERROR, "input params null!!");
                }
                ALog.e(TAG, "sendSwitch param null", "appkey", agooAppKey, "deviceId", deviceToken, "context", context, SwitchDO.JSON_CMD_ENABLEPUSH, Boolean.valueOf(z));
                return;
            } catch (Throwable th) {
                ALog.e(TAG, "sendSwitch", th, new Object[0]);
            }
        }
        return;
    }

    public static void bindAgoo(Context context, ICallback iCallback) {
        sendSwitch(context, iCallback, true);
        UTMini.getInstance().commitEvent(66001, "bindAgoo", UtilityImpl.getDeviceId(context));
    }

    public static void unbindAgoo(Context context, ICallback iCallback) {
        sendSwitch(context, iCallback, false);
        UTMini.getInstance().commitEvent(66001, MiPushClient.COMMAND_UNREGISTER, UtilityImpl.getDeviceId(context));
    }

    public static void clickMessage(Context context, String str, String str2) {
        clickMessage(context, str, str2, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0124  */
    /* JADX WARNING: Removed duplicated region for block: B:72:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void clickMessage(android.content.Context r21, java.lang.String r22, java.lang.String r23, int r24) {
        /*
            r0 = r21
            r1 = r22
            r2 = r23
            org.android.agoo.control.NotifManager r4 = new org.android.agoo.control.NotifManager
            r4.<init>()
            r6 = 0
            com.taobao.accs.utl.ALog$Level r7 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            boolean r7 = com.taobao.accs.utl.ALog.isPrintLog(r7)     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            r8 = 3
            r9 = 2
            r10 = 4
            r11 = 1
            if (r7 == 0) goto L_0x0036
            java.lang.String r7 = "TaobaoRegister"
            java.lang.String r12 = "clickMessage"
            java.lang.Object[] r13 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            java.lang.String r14 = "msgid"
            r13[r6] = r14     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            r13[r11] = r1     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            java.lang.String r14 = "extData"
            r13[r9] = r14     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            r13[r8] = r2     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            com.taobao.accs.utl.ALog.i(r7, r12, r13)     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            goto L_0x0036
        L_0x002e:
            r0 = move-exception
            r13 = 0
            goto L_0x0122
        L_0x0032:
            r0 = move-exception
            r5 = 0
            goto L_0x0101
        L_0x0036:
            java.lang.String r7 = "accs"
            java.lang.String r12 = "8"
            boolean r13 = android.text.TextUtils.isEmpty(r22)     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            if (r13 == 0) goto L_0x004a
            java.lang.String r0 = "TaobaoRegister"
            java.lang.String r1 = "messageId == null"
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            com.taobao.accs.utl.ALog.d(r0, r1, r2)     // Catch:{ Throwable -> 0x0032, all -> 0x002e }
            return
        L_0x004a:
            r4.init(r0)     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            org.android.agoo.common.MsgDO r13 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            r13.<init>()     // Catch:{ Throwable -> 0x00fe, all -> 0x00fa }
            r13.evokeAppStatus = r11     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r14 = r24 & 1
            if (r14 != r11) goto L_0x005a
            r14 = 1
            goto L_0x005b
        L_0x005a:
            r14 = 0
        L_0x005b:
            r15 = r24 & 2
            if (r15 != r9) goto L_0x0061
            r15 = 1
            goto L_0x0062
        L_0x0061:
            r15 = 0
        L_0x0062:
            r5 = r24 & 4
            if (r5 != r10) goto L_0x0068
            r5 = 1
            goto L_0x0069
        L_0x0068:
            r5 = 0
        L_0x0069:
            r10 = 8
            r3 = r24 & 8
            if (r3 != r10) goto L_0x0071
            r3 = 1
            goto L_0x0072
        L_0x0071:
            r3 = 0
        L_0x0072:
            r10 = r14 ^ r15
            r13.isGlobalClick = r10     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            boolean r10 = r13.isGlobalClick     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            if (r10 == 0) goto L_0x00db
            java.lang.String r10 = "TaobaoRegister"
            java.lang.String r8 = "clickMessage"
            r9 = 10
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.String r20 = "isGlobalClick"
            r9[r6] = r20     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            boolean r6 = r13.isGlobalClick     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r9[r11] = r6     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.String r6 = "isLaunchByAgoo"
            r11 = 2
            r9[r11] = r6     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r15)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r19 = 3
            r9[r19] = r6     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.String r6 = "isEvokeByAgoo"
            r16 = 4
            r9[r16] = r6     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r6 = 5
            java.lang.Boolean r18 = java.lang.Boolean.valueOf(r14)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r9[r6] = r18     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r6 = 6
            java.lang.String r18 = "isComeFromBg"
            r9[r6] = r18     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r6 = 7
            java.lang.Boolean r18 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r9[r6] = r18     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.String r6 = "isSameDay"
            r17 = 8
            r9[r17] = r6     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r6 = 9
            java.lang.Boolean r17 = java.lang.Boolean.valueOf(r3)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r9[r6] = r17     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            com.taobao.accs.utl.ALog.i(r10, r8, r9)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            long r8 = lastCurrentLaunchTime     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r13.lastActiveTime = r8     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            if (r14 == 0) goto L_0x00cd
            if (r5 != 0) goto L_0x00cf
        L_0x00cd:
            if (r15 == 0) goto L_0x00db
        L_0x00cf:
            if (r3 == 0) goto L_0x00d8
            if (r15 == 0) goto L_0x00d4
            goto L_0x00d5
        L_0x00d4:
            r11 = 3
        L_0x00d5:
            r13.evokeAppStatus = r11     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            goto L_0x00db
        L_0x00d8:
            r3 = 4
            r13.evokeAppStatus = r3     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
        L_0x00db:
            r13.msgIds = r1     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r13.extData = r2     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r13.messageSource = r7     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r13.msgStatus = r12     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            org.android.agoo.control.AgooFactory r2 = new org.android.agoo.control.AgooFactory     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r2.<init>()     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r3 = 0
            r2.init(r0, r4, r3)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            java.lang.String r0 = "8"
            r2.updateMsgStatus(r1, r0)     // Catch:{ Throwable -> 0x00f7, all -> 0x00f5 }
            r4.reportNotifyMessage(r13)
            goto L_0x011f
        L_0x00f5:
            r0 = move-exception
            goto L_0x0122
        L_0x00f7:
            r0 = move-exception
            r5 = r13
            goto L_0x0101
        L_0x00fa:
            r0 = move-exception
            r3 = 0
            r13 = r3
            goto L_0x0122
        L_0x00fe:
            r0 = move-exception
            r3 = 0
            r5 = r3
        L_0x0101:
            java.lang.String r1 = "TaobaoRegister"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0120 }
            r2.<init>()     // Catch:{ all -> 0x0120 }
            java.lang.String r3 = "clickMessage,error="
            r2.append(r3)     // Catch:{ all -> 0x0120 }
            r2.append(r0)     // Catch:{ all -> 0x0120 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0120 }
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0120 }
            com.taobao.accs.utl.ALog.e(r1, r0, r2)     // Catch:{ all -> 0x0120 }
            if (r5 == 0) goto L_0x011f
            r4.reportNotifyMessage(r5)
        L_0x011f:
            return
        L_0x0120:
            r0 = move-exception
            r13 = r5
        L_0x0122:
            if (r13 == 0) goto L_0x0127
            r4.reportNotifyMessage(r13)
        L_0x0127:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.agoo.TaobaoRegister.clickMessage(android.content.Context, java.lang.String, java.lang.String, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void dismissMessage(android.content.Context r8, java.lang.String r9, java.lang.String r10) {
        /*
            org.android.agoo.control.NotifManager r0 = new org.android.agoo.control.NotifManager
            r0.<init>()
            r1 = 0
            r2 = 0
            com.taobao.accs.utl.ALog$Level r3 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0065 }
            boolean r3 = com.taobao.accs.utl.ALog.isPrintLog(r3)     // Catch:{ Throwable -> 0x0065 }
            if (r3 == 0) goto L_0x0028
            java.lang.String r3 = "TaobaoRegister"
            java.lang.String r4 = "dismissMessage"
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0065 }
            java.lang.String r6 = "msgid"
            r5[r2] = r6     // Catch:{ Throwable -> 0x0065 }
            r6 = 1
            r5[r6] = r9     // Catch:{ Throwable -> 0x0065 }
            r6 = 2
            java.lang.String r7 = "extData"
            r5[r6] = r7     // Catch:{ Throwable -> 0x0065 }
            r6 = 3
            r5[r6] = r10     // Catch:{ Throwable -> 0x0065 }
            com.taobao.accs.utl.ALog.i(r3, r4, r5)     // Catch:{ Throwable -> 0x0065 }
        L_0x0028:
            java.lang.String r3 = "accs"
            java.lang.String r4 = "9"
            boolean r5 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0065 }
            if (r5 == 0) goto L_0x003c
            java.lang.String r8 = "TaobaoRegister"
            java.lang.String r9 = "messageId == null"
            java.lang.Object[] r10 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0065 }
            com.taobao.accs.utl.ALog.d(r8, r9, r10)     // Catch:{ Throwable -> 0x0065 }
            return
        L_0x003c:
            r0.init(r8)     // Catch:{ Throwable -> 0x0065 }
            org.android.agoo.common.MsgDO r5 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x0065 }
            r5.<init>()     // Catch:{ Throwable -> 0x0065 }
            r5.msgIds = r9     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r5.extData = r10     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r5.messageSource = r3     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r5.msgStatus = r4     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            org.android.agoo.control.AgooFactory r10 = new org.android.agoo.control.AgooFactory     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r10.<init>()     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r10.init(r8, r0, r1)     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            java.lang.String r8 = "9"
            r10.updateMsgStatus(r9, r8)     // Catch:{ Throwable -> 0x0060, all -> 0x005d }
            r0.reportNotifyMessage(r5)
            goto L_0x0083
        L_0x005d:
            r8 = move-exception
            r1 = r5
            goto L_0x0084
        L_0x0060:
            r8 = move-exception
            r1 = r5
            goto L_0x0066
        L_0x0063:
            r8 = move-exception
            goto L_0x0084
        L_0x0065:
            r8 = move-exception
        L_0x0066:
            java.lang.String r9 = "TaobaoRegister"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
            r10.<init>()     // Catch:{ all -> 0x0063 }
            java.lang.String r3 = "clickMessage,error="
            r10.append(r3)     // Catch:{ all -> 0x0063 }
            r10.append(r8)     // Catch:{ all -> 0x0063 }
            java.lang.String r8 = r10.toString()     // Catch:{ all -> 0x0063 }
            java.lang.Object[] r10 = new java.lang.Object[r2]     // Catch:{ all -> 0x0063 }
            com.taobao.accs.utl.ALog.e(r9, r8, r10)     // Catch:{ all -> 0x0063 }
            if (r1 == 0) goto L_0x0083
            r0.reportNotifyMessage(r1)
        L_0x0083:
            return
        L_0x0084:
            if (r1 == 0) goto L_0x0089
            r0.reportNotifyMessage(r1)
        L_0x0089:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.agoo.TaobaoRegister.dismissMessage(android.content.Context, java.lang.String, java.lang.String):void");
    }

    public static void pingApp(Context context, String str, String str2, String str3, int i) {
        NotifManager notifManager = new NotifManager();
        notifManager.init(context);
        notifManager.pingApp(str, str2, str3, i);
    }

    public static void isEnableDaemonServer(Context context, boolean z) {
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "isEnableDaemonServer begin,enable=" + z, new Object[0]);
        }
        Config.setDaemonServerFlag(context, z);
    }

    public static void setAgooMsgReceiveService(String str) {
        AdapterGlobalClientInfo.mAgooCustomServiceName = str;
    }

    public static void setEnv(Context context, @AccsClientConfig.ENV int i) {
        ACCSClient.setEnvironment(context, i);
    }

    @Deprecated
    public static void unregister(Context context, CallBack callBack) {
        unbindAgoo(context, (ICallback) null);
    }

    public static void setLastCurrentLaunchTime(long j) {
        lastCurrentLaunchTime = j;
    }

    public static boolean isRegisterSuccess() {
        return isRegisterSuccess;
    }

    public static void setIsRegisterSuccess(boolean z) {
        isRegisterSuccess = z;
    }
}
