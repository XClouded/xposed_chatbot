package com.ali.user.mobile.security;

import android.content.ContextWrapper;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.rpc.login.model.GestureList;
import com.ali.user.mobile.rpc.login.model.GestureModel;
import com.ali.user.mobile.rpc.login.model.SessionList;
import com.ali.user.mobile.rpc.login.model.SessionModel;
import com.ali.user.mobile.rpc.login.model.WSecurityData;
import com.ali.user.mobile.rpc.login.model.WUAData;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.taobao.login4android.utils.FileUtils;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class SecurityGuardManagerWraper {
    private static final String GESTURE_LIST = "gestureList";
    private static final String HISTORY_LOGIN_ACCOUNTS = "aliusersdk_history_acounts";
    private static final String SESSION_LIST = "aliusersdk_session_lists";
    private static final String TAG = "login.SecurityManager";
    private static boolean hadReadHistory = false;
    private static LoginHistory mLoginHistory;
    private static SecurityGuardManager mSecurityGuardManager;

    public static synchronized SecurityGuardManager getSecurityGuardManager() {
        SecurityGuardManager securityGuardManager;
        synchronized (SecurityGuardManagerWraper.class) {
            if (mSecurityGuardManager == null) {
                try {
                    mSecurityGuardManager = SecurityGuardManager.getInstance(new ContextWrapper(DataProviderFactory.getApplicationContext()));
                } catch (SecException e) {
                    e.printStackTrace();
                }
            }
            securityGuardManager = mSecurityGuardManager;
        }
        return securityGuardManager;
    }

    public static WUAData getWUA() {
        ISecurityBodyComponent securityBodyComp;
        try {
            com.taobao.wireless.security.sdk.SecurityGuardManager instance = com.taobao.wireless.security.sdk.SecurityGuardManager.getInstance(new ContextWrapper(DataProviderFactory.getApplicationContext()));
            if (instance == null || (securityBodyComp = instance.getSecurityBodyComp()) == null) {
                return null;
            }
            long currentTimeMillis = System.currentTimeMillis();
            String valueOf = String.valueOf(currentTimeMillis);
            String appkey = DataProviderFactory.getDataProvider().getAppkey();
            String securityBodyOpen = getSecurityBodyOpen(currentTimeMillis, appkey);
            if (TextUtils.isEmpty(securityBodyOpen)) {
                securityBodyOpen = securityBodyComp.getSecurityBodyData(valueOf, appkey);
            }
            return new WUAData(DataProviderFactory.getDataProvider().getAppkey(), valueOf, securityBodyOpen);
        } catch (Exception e) {
            TLogAdapter.e(TAG, (Throwable) e);
            return null;
        }
    }

    private static WUAData getRPWUA() {
        if (ServiceFactory.getService(FaceService.class) == null) {
            return null;
        }
        return new WUAData(DataProviderFactory.getDataProvider().getAppkey(), String.valueOf(System.currentTimeMillis()), ((FaceService) ServiceFactory.getService(FaceService.class)).getDeviceInfo());
    }

    private static String getSecurityBodyOpen(long j, String str) {
        try {
            return ((com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent) getSecurityGuardManager().getInterface(com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent.class)).getSecurityBodyDataEx(String.valueOf(j), str, "", (HashMap<String, String>) null, 4, convertEnvToMtop());
        } catch (Exception e) {
            TLogAdapter.e(TAG, (Throwable) e);
            return null;
        } catch (Throwable th) {
            TLogAdapter.e(TAG, th);
            return null;
        }
    }

    private static int convertEnvToMtop() {
        if (DataProviderFactory.getDataProvider().getEnvType() == 1) {
            return 2;
        }
        return DataProviderFactory.getDataProvider().getEnvType() == 2 ? 1 : 0;
    }

    public static WSecurityData buildWSecurityData() {
        WSecurityData wSecurityData = new WSecurityData();
        WUAData wua = getWUA();
        if (wua != null) {
            wSecurityData.wua = wua.wua;
            wSecurityData.t = wua.t;
        }
        wSecurityData.apdId = AlipayInfo.getInstance().getApdid();
        wSecurityData.umidToken = AppInfo.getInstance().getUmidToken();
        return wSecurityData;
    }

    public static WSecurityData buildRPSecurityData() {
        WSecurityData wSecurityData = new WSecurityData();
        WUAData rpwua = getRPWUA();
        if (rpwua != null) {
            wSecurityData.wua = rpwua.wua;
            wSecurityData.t = rpwua.t;
        }
        wSecurityData.apdId = AlipayInfo.getInstance().getApdid();
        wSecurityData.umidToken = AppInfo.getInstance().getUmidToken();
        return wSecurityData;
    }

    public static String staticSafeEncrypt(String str) {
        try {
            IStaticDataEncryptComponent staticDataEncryptComp = SecurityGuardManager.getInstance(new ContextWrapper(DataProviderFactory.getApplicationContext())).getStaticDataEncryptComp();
            if (staticDataEncryptComp != null) {
                return staticDataEncryptComp.staticSafeEncrypt(16, DataProviderFactory.getDataProvider().getAppkey(), str, "");
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public static void removeAllHistoryAccount() {
        try {
            IDynamicDataStoreComponent dynamicDataStoreComp = getSecurityGuardManager().getDynamicDataStoreComp();
            if (dynamicDataStoreComp != null) {
                dynamicDataStoreComp.removeStringDDpEx(HISTORY_LOGIN_ACCOUNTS, 0);
                updateMemoryHistory((LoginHistory) null);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            try {
                Properties properties = new Properties();
                properties.setProperty("errorCode", "80005");
                properties.setProperty("cause", "Throwable: " + th);
                UserTrackAdapter.sendUT("Event_removeHistoryAccountFail", properties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:6|7|8|9|10|11|12|(1:14)(1:15)|(2:29|(4:31|(4:34|(2:39|67)(2:38|66)|64|32)|65|40)(4:41|(4:44|(2:46|70)(2:47|71)|68|42)|69|48))|(3:50|(2:52|(1:54)(1:55))|56)|57|73) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001d */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0023 A[Catch:{ JSONException -> 0x0032, Throwable -> 0x011f }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0029 A[Catch:{ JSONException -> 0x0032, Throwable -> 0x011f }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a8 A[Catch:{ JSONException -> 0x0032, Throwable -> 0x011f }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d0 A[Catch:{ JSONException -> 0x0032, Throwable -> 0x011f }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00f3 A[Catch:{ JSONException -> 0x0032, Throwable -> 0x011f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void removeHistoryAccount(com.ali.user.mobile.rpc.HistoryAccount r13) {
        /*
            if (r13 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = r13.tokenKey     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.security.AlibabaSecurityTokenService.removeSafeToken(r0)     // Catch:{ Throwable -> 0x011f }
            com.alibaba.wireless.security.open.SecurityGuardManager r0 = getSecurityGuardManager()     // Catch:{ Throwable -> 0x011f }
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r0 = r0.getDynamicDataStoreComp()     // Catch:{ Throwable -> 0x011f }
            if (r0 != 0) goto L_0x0013
            return
        L_0x0013:
            java.lang.String r1 = ""
            r2 = 0
            java.lang.String r3 = "aliusersdk_history_acounts"
            java.lang.String r3 = r0.getStringDDpEx(r3, r2)     // Catch:{ SecException -> 0x001d }
            r1 = r3
        L_0x001d:
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ JSONException -> 0x0032 }
            if (r3 == 0) goto L_0x0029
            com.ali.user.mobile.rpc.LoginHistory r3 = new com.ali.user.mobile.rpc.LoginHistory     // Catch:{ JSONException -> 0x0032 }
            r3.<init>()     // Catch:{ JSONException -> 0x0032 }
            goto L_0x0095
        L_0x0029:
            java.lang.Class<com.ali.user.mobile.rpc.LoginHistory> r3 = com.ali.user.mobile.rpc.LoginHistory.class
            java.lang.Object r3 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r3)     // Catch:{ JSONException -> 0x0032 }
            com.ali.user.mobile.rpc.LoginHistory r3 = (com.ali.user.mobile.rpc.LoginHistory) r3     // Catch:{ JSONException -> 0x0032 }
            goto L_0x0095
        L_0x0032:
            r3 = move-exception
            java.lang.String r4 = "login.SecurityManager"
            java.lang.String r5 = "removeHistoryAccount JSONException"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r4, (java.lang.String) r5)     // Catch:{ Throwable -> 0x011f }
            r3.printStackTrace()     // Catch:{ Throwable -> 0x011f }
            java.util.Properties r4 = new java.util.Properties     // Catch:{ Exception -> 0x0065 }
            r4.<init>()     // Catch:{ Exception -> 0x0065 }
            java.lang.String r5 = "errorCode"
            java.lang.String r6 = "80005"
            r4.setProperty(r5, r6)     // Catch:{ Exception -> 0x0065 }
            java.lang.String r5 = "cause"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0065 }
            r6.<init>()     // Catch:{ Exception -> 0x0065 }
            java.lang.String r7 = "JSONException: "
            r6.append(r7)     // Catch:{ Exception -> 0x0065 }
            r6.append(r1)     // Catch:{ Exception -> 0x0065 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0065 }
            r4.setProperty(r5, r6)     // Catch:{ Exception -> 0x0065 }
            java.lang.String r5 = "Event_removeHistoryAccountFail"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r5, (java.util.Properties) r4)     // Catch:{ Exception -> 0x0065 }
            goto L_0x0069
        L_0x0065:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Throwable -> 0x011f }
        L_0x0069:
            java.lang.String r4 = "SecurityGuardManagerWraper"
            java.lang.String r5 = "removeHistoryAccount"
            java.lang.String r6 = "218"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011f }
            r7.<init>()     // Catch:{ Throwable -> 0x011f }
            java.lang.String r8 = "JSONException "
            r7.append(r8)     // Catch:{ Throwable -> 0x011f }
            r7.append(r3)     // Catch:{ Throwable -> 0x011f }
            java.lang.String r3 = " json="
            r7.append(r3)     // Catch:{ Throwable -> 0x011f }
            r7.append(r1)     // Catch:{ Throwable -> 0x011f }
            java.lang.String r1 = r7.toString()     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.log.AppMonitorAdapter.commitFail(r4, r5, r6, r1)     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.rpc.LoginHistory r3 = new com.ali.user.mobile.rpc.LoginHistory     // Catch:{ Throwable -> 0x011f }
            r3.<init>()     // Catch:{ Throwable -> 0x011f }
            java.lang.String r1 = "aliusersdk_history_acounts"
            r0.removeStringDDpEx(r1, r2)     // Catch:{ Throwable -> 0x011f }
        L_0x0095:
            if (r3 == 0) goto L_0x00f1
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r1 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            if (r1 == 0) goto L_0x00f1
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ Throwable -> 0x011f }
            r1.<init>()     // Catch:{ Throwable -> 0x011f }
            long r4 = r13.alipayHid     // Catch:{ Throwable -> 0x011f }
            r6 = 0
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x00d0
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r4 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ Throwable -> 0x011f }
        L_0x00ae:
            boolean r5 = r4.hasNext()     // Catch:{ Throwable -> 0x011f }
            if (r5 == 0) goto L_0x00cd
            java.lang.Object r5 = r4.next()     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.rpc.HistoryAccount r5 = (com.ali.user.mobile.rpc.HistoryAccount) r5     // Catch:{ Throwable -> 0x011f }
            long r8 = r5.userId     // Catch:{ Throwable -> 0x011f }
            long r10 = r13.userId     // Catch:{ Throwable -> 0x011f }
            int r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r12 != 0) goto L_0x00c9
            long r8 = r5.alipayHid     // Catch:{ Throwable -> 0x011f }
            int r10 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r10 != 0) goto L_0x00c9
            goto L_0x00ae
        L_0x00c9:
            r1.add(r5)     // Catch:{ Throwable -> 0x011f }
            goto L_0x00ae
        L_0x00cd:
            r3.accountHistory = r1     // Catch:{ Throwable -> 0x011f }
            goto L_0x00f1
        L_0x00d0:
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r4 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ Throwable -> 0x011f }
        L_0x00d6:
            boolean r5 = r4.hasNext()     // Catch:{ Throwable -> 0x011f }
            if (r5 == 0) goto L_0x00ef
            java.lang.Object r5 = r4.next()     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.rpc.HistoryAccount r5 = (com.ali.user.mobile.rpc.HistoryAccount) r5     // Catch:{ Throwable -> 0x011f }
            long r6 = r13.alipayHid     // Catch:{ Throwable -> 0x011f }
            long r8 = r5.alipayHid     // Catch:{ Throwable -> 0x011f }
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 != 0) goto L_0x00eb
            goto L_0x00d6
        L_0x00eb:
            r1.add(r5)     // Catch:{ Throwable -> 0x011f }
            goto L_0x00d6
        L_0x00ef:
            r3.accountHistory = r1     // Catch:{ Throwable -> 0x011f }
        L_0x00f1:
            if (r3 == 0) goto L_0x011b
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r13 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            if (r13 == 0) goto L_0x0116
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r13 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            boolean r13 = r13.isEmpty()     // Catch:{ Throwable -> 0x011f }
            if (r13 == 0) goto L_0x0100
            goto L_0x0116
        L_0x0100:
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r13 = r3.accountHistory     // Catch:{ Throwable -> 0x011f }
            com.ali.user.mobile.rpc.HistoryAccount r1 = new com.ali.user.mobile.rpc.HistoryAccount     // Catch:{ Throwable -> 0x011f }
            r1.<init>()     // Catch:{ Throwable -> 0x011f }
            java.util.Collections.sort(r13, r1)     // Catch:{ Throwable -> 0x011f }
            r3.index = r2     // Catch:{ Throwable -> 0x011f }
            java.lang.String r13 = "aliusersdk_history_acounts"
            java.lang.String r1 = com.alibaba.fastjson.JSON.toJSONString(r3)     // Catch:{ Throwable -> 0x011f }
            r0.putStringDDpEx(r13, r1, r2)     // Catch:{ Throwable -> 0x011f }
            goto L_0x011b
        L_0x0116:
            java.lang.String r13 = "aliusersdk_history_acounts"
            r0.removeStringDDpEx(r13, r2)     // Catch:{ Throwable -> 0x011f }
        L_0x011b:
            updateMemoryHistory(r3)     // Catch:{ Throwable -> 0x011f }
            goto L_0x014f
        L_0x011f:
            r13 = move-exception
            r13.printStackTrace()
            java.util.Properties r0 = new java.util.Properties     // Catch:{ Exception -> 0x014b }
            r0.<init>()     // Catch:{ Exception -> 0x014b }
            java.lang.String r1 = "errorCode"
            java.lang.String r2 = "80005"
            r0.setProperty(r1, r2)     // Catch:{ Exception -> 0x014b }
            java.lang.String r1 = "cause"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014b }
            r2.<init>()     // Catch:{ Exception -> 0x014b }
            java.lang.String r3 = "Throwable: "
            r2.append(r3)     // Catch:{ Exception -> 0x014b }
            r2.append(r13)     // Catch:{ Exception -> 0x014b }
            java.lang.String r13 = r2.toString()     // Catch:{ Exception -> 0x014b }
            r0.setProperty(r1, r13)     // Catch:{ Exception -> 0x014b }
            java.lang.String r13 = "Event_removeHistoryAccountFail"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r13, (java.util.Properties) r0)     // Catch:{ Exception -> 0x014b }
            goto L_0x014f
        L_0x014b:
            r13 = move-exception
            r13.printStackTrace()
        L_0x014f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.removeHistoryAccount(com.ali.user.mobile.rpc.HistoryAccount):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void removeSessionModelFromFile(java.lang.String r6) {
        /*
            java.lang.Class<com.ali.user.mobile.security.SecurityGuardManagerWraper> r0 = com.ali.user.mobile.security.SecurityGuardManagerWraper.class
            monitor-enter(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x005e }
            if (r1 == 0) goto L_0x000b
            monitor-exit(r0)
            return
        L_0x000b:
            com.ali.user.mobile.rpc.login.model.SessionList r1 = getSessionListFromFile()     // Catch:{ Exception -> 0x0058 }
            if (r1 == 0) goto L_0x0056
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r2 = r1.sessionModels     // Catch:{ Exception -> 0x0058 }
            if (r2 == 0) goto L_0x0056
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r2 = r1.sessionModels     // Catch:{ Exception -> 0x0058 }
            int r2 = r2.size()     // Catch:{ Exception -> 0x0058 }
            if (r2 != 0) goto L_0x001e
            goto L_0x0056
        L_0x001e:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ Exception -> 0x0058 }
            r2.<init>()     // Catch:{ Exception -> 0x0058 }
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r3 = r1.sessionModels     // Catch:{ Exception -> 0x0058 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Exception -> 0x0058 }
        L_0x0029:
            boolean r4 = r3.hasNext()     // Catch:{ Exception -> 0x0058 }
            if (r4 == 0) goto L_0x0042
            java.lang.Object r4 = r3.next()     // Catch:{ Exception -> 0x0058 }
            com.ali.user.mobile.rpc.login.model.SessionModel r4 = (com.ali.user.mobile.rpc.login.model.SessionModel) r4     // Catch:{ Exception -> 0x0058 }
            java.lang.String r5 = r4.userId     // Catch:{ Exception -> 0x0058 }
            boolean r5 = r5.equals(r6)     // Catch:{ Exception -> 0x0058 }
            if (r5 == 0) goto L_0x003e
            goto L_0x0029
        L_0x003e:
            r2.add(r4)     // Catch:{ Exception -> 0x0058 }
            goto L_0x0029
        L_0x0042:
            r1.sessionModels = r2     // Catch:{ Exception -> 0x0058 }
            android.content.Context r6 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ Exception -> 0x0058 }
            java.lang.String r2 = "aliusersdk_session_lists"
            java.lang.String r1 = com.alibaba.fastjson.JSON.toJSONString(r1)     // Catch:{ Exception -> 0x0058 }
            java.lang.String r1 = encode(r1)     // Catch:{ Exception -> 0x0058 }
            com.taobao.login4android.utils.FileUtils.writeFileData(r6, r2, r1)     // Catch:{ Exception -> 0x0058 }
            goto L_0x005c
        L_0x0056:
            monitor-exit(r0)
            return
        L_0x0058:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ all -> 0x005e }
        L_0x005c:
            monitor-exit(r0)
            return
        L_0x005e:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.removeSessionModelFromFile(java.lang.String):void");
    }

    public static synchronized void removeGestureModelFromFile(String str) {
        synchronized (SecurityGuardManagerWraper.class) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:6|7|8|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void putSessionModelToFile(com.ali.user.mobile.rpc.login.model.SessionModel r7) {
        /*
            java.lang.Class<com.ali.user.mobile.security.SecurityGuardManagerWraper> r0 = com.ali.user.mobile.security.SecurityGuardManagerWraper.class
            monitor-enter(r0)
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x0095 }
            java.lang.String r2 = "aliusersdk_session_lists"
            java.lang.String r1 = com.taobao.login4android.utils.FileUtils.readFileData(r1, r2)     // Catch:{ all -> 0x0095 }
            java.lang.String r1 = decrypt(r1)     // Catch:{ all -> 0x0095 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0095 }
            if (r2 == 0) goto L_0x001d
            com.ali.user.mobile.rpc.login.model.SessionList r1 = new com.ali.user.mobile.rpc.login.model.SessionList     // Catch:{ all -> 0x0095 }
            r1.<init>()     // Catch:{ all -> 0x0095 }
            goto L_0x002e
        L_0x001d:
            java.lang.Class<com.ali.user.mobile.rpc.login.model.SessionList> r2 = com.ali.user.mobile.rpc.login.model.SessionList.class
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r2)     // Catch:{ JSONException -> 0x0026 }
            com.ali.user.mobile.rpc.login.model.SessionList r1 = (com.ali.user.mobile.rpc.login.model.SessionList) r1     // Catch:{ JSONException -> 0x0026 }
            goto L_0x002e
        L_0x0026:
            com.ali.user.mobile.rpc.login.model.SessionList r1 = new com.ali.user.mobile.rpc.login.model.SessionList     // Catch:{ all -> 0x0095 }
            r1.<init>()     // Catch:{ all -> 0x0095 }
            emptySessionListFromFile()     // Catch:{ all -> 0x0095 }
        L_0x002e:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0095 }
            r2.<init>()     // Catch:{ all -> 0x0095 }
            r2.add(r7)     // Catch:{ all -> 0x0095 }
            if (r1 == 0) goto L_0x005c
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r3 = r1.sessionModels     // Catch:{ all -> 0x0095 }
            if (r3 == 0) goto L_0x005c
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r3 = r1.sessionModels     // Catch:{ all -> 0x0095 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0095 }
        L_0x0042:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0095 }
            if (r4 == 0) goto L_0x005c
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0095 }
            com.ali.user.mobile.rpc.login.model.SessionModel r4 = (com.ali.user.mobile.rpc.login.model.SessionModel) r4     // Catch:{ all -> 0x0095 }
            java.lang.String r5 = r7.userId     // Catch:{ all -> 0x0095 }
            java.lang.String r6 = r4.userId     // Catch:{ all -> 0x0095 }
            boolean r5 = android.text.TextUtils.equals(r5, r6)     // Catch:{ all -> 0x0095 }
            if (r5 != 0) goto L_0x0042
            r2.add(r4)     // Catch:{ all -> 0x0095 }
            goto L_0x0042
        L_0x005c:
            com.ali.user.mobile.app.dataprovider.IDataProvider r7 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x0095 }
            int r7 = r7.getMaxSessionSize()     // Catch:{ all -> 0x0095 }
            r3 = 20
            if (r7 <= r3) goto L_0x006a
            r7 = 20
        L_0x006a:
            int r3 = r2.size()     // Catch:{ all -> 0x0095 }
            int r3 = r3 - r7
            if (r3 <= 0) goto L_0x0080
            int r7 = r2.size()     // Catch:{ all -> 0x0095 }
            if (r7 <= 0) goto L_0x0080
            int r7 = r2.size()     // Catch:{ all -> 0x0095 }
            int r7 = r7 + -1
            r2.remove(r7)     // Catch:{ all -> 0x0095 }
        L_0x0080:
            r1.sessionModels = r2     // Catch:{ all -> 0x0095 }
            android.content.Context r7 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x0095 }
            java.lang.String r2 = "aliusersdk_session_lists"
            java.lang.String r1 = com.alibaba.fastjson.JSON.toJSONString(r1)     // Catch:{ all -> 0x0095 }
            java.lang.String r1 = encode(r1)     // Catch:{ all -> 0x0095 }
            com.taobao.login4android.utils.FileUtils.writeFileData(r7, r2, r1)     // Catch:{ all -> 0x0095 }
            monitor-exit(r0)
            return
        L_0x0095:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.putSessionModelToFile(com.ali.user.mobile.rpc.login.model.SessionModel):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:6|7|8|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void putGestureModelToFile(com.ali.user.mobile.rpc.login.model.GestureModel r7) {
        /*
            java.lang.Class<com.ali.user.mobile.security.SecurityGuardManagerWraper> r0 = com.ali.user.mobile.security.SecurityGuardManagerWraper.class
            monitor-enter(r0)
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x00ab }
            java.lang.String r2 = "gestureList"
            java.lang.String r1 = com.taobao.login4android.utils.FileUtils.readFileData(r1, r2)     // Catch:{ all -> 0x00ab }
            java.lang.String r1 = decrypt(r1)     // Catch:{ all -> 0x00ab }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x00ab }
            if (r2 == 0) goto L_0x001d
            com.ali.user.mobile.rpc.login.model.GestureList r1 = new com.ali.user.mobile.rpc.login.model.GestureList     // Catch:{ all -> 0x00ab }
            r1.<init>()     // Catch:{ all -> 0x00ab }
            goto L_0x002e
        L_0x001d:
            java.lang.Class<com.ali.user.mobile.rpc.login.model.GestureList> r2 = com.ali.user.mobile.rpc.login.model.GestureList.class
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r2)     // Catch:{ JSONException -> 0x0026 }
            com.ali.user.mobile.rpc.login.model.GestureList r1 = (com.ali.user.mobile.rpc.login.model.GestureList) r1     // Catch:{ JSONException -> 0x0026 }
            goto L_0x002e
        L_0x0026:
            com.ali.user.mobile.rpc.login.model.GestureList r1 = new com.ali.user.mobile.rpc.login.model.GestureList     // Catch:{ all -> 0x00ab }
            r1.<init>()     // Catch:{ all -> 0x00ab }
            emptyGestureListFromFile()     // Catch:{ all -> 0x00ab }
        L_0x002e:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x00ab }
            r2.<init>()     // Catch:{ all -> 0x00ab }
            r2.add(r7)     // Catch:{ all -> 0x00ab }
            if (r1 == 0) goto L_0x005c
            java.util.List<com.ali.user.mobile.rpc.login.model.GestureModel> r3 = r1.gestureList     // Catch:{ all -> 0x00ab }
            if (r3 == 0) goto L_0x005c
            java.util.List<com.ali.user.mobile.rpc.login.model.GestureModel> r3 = r1.gestureList     // Catch:{ all -> 0x00ab }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x00ab }
        L_0x0042:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x00ab }
            if (r4 == 0) goto L_0x005c
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x00ab }
            com.ali.user.mobile.rpc.login.model.GestureModel r4 = (com.ali.user.mobile.rpc.login.model.GestureModel) r4     // Catch:{ all -> 0x00ab }
            java.lang.String r5 = r4.userId     // Catch:{ all -> 0x00ab }
            java.lang.String r6 = r7.userId     // Catch:{ all -> 0x00ab }
            boolean r5 = android.text.TextUtils.equals(r5, r6)     // Catch:{ all -> 0x00ab }
            if (r5 != 0) goto L_0x0042
            r2.add(r4)     // Catch:{ all -> 0x00ab }
            goto L_0x0042
        L_0x005c:
            com.ali.user.mobile.app.dataprovider.IDataProvider r7 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x00ab }
            int r7 = r7.getMaxSessionSize()     // Catch:{ all -> 0x00ab }
            r3 = 20
            if (r7 <= r3) goto L_0x006a
            r7 = 20
        L_0x006a:
            int r3 = r2.size()     // Catch:{ all -> 0x00ab }
            int r3 = r3 - r7
            if (r3 <= 0) goto L_0x0080
            int r7 = r2.size()     // Catch:{ all -> 0x00ab }
            if (r7 <= 0) goto L_0x0080
            int r7 = r2.size()     // Catch:{ all -> 0x00ab }
            int r7 = r7 + -1
            r2.remove(r7)     // Catch:{ all -> 0x00ab }
        L_0x0080:
            r1.gestureList = r2     // Catch:{ all -> 0x00ab }
            java.lang.String r7 = com.alibaba.fastjson.JSON.toJSONString(r1)     // Catch:{ all -> 0x00ab }
            java.lang.String r1 = "login.SecurityManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ab }
            r2.<init>()     // Catch:{ all -> 0x00ab }
            java.lang.String r3 = "gesture list = "
            r2.append(r3)     // Catch:{ all -> 0x00ab }
            r2.append(r7)     // Catch:{ all -> 0x00ab }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00ab }
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x00ab }
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x00ab }
            java.lang.String r2 = "gestureList"
            java.lang.String r7 = encode(r7)     // Catch:{ all -> 0x00ab }
            com.taobao.login4android.utils.FileUtils.writeFileData(r1, r2, r7)     // Catch:{ all -> 0x00ab }
            monitor-exit(r0)
            return
        L_0x00ab:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.putGestureModelToFile(com.ali.user.mobile.rpc.login.model.GestureModel):void");
    }

    public static void emptySessionListFromFile() {
        FileUtils.writeFileData(DataProviderFactory.getApplicationContext(), SESSION_LIST, "");
    }

    public static void emptyGestureListFromFile() {
        FileUtils.writeFileData(DataProviderFactory.getApplicationContext(), GESTURE_LIST, "");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:29|30|31|32) */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        emptySessionListFromFile();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0077, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0073 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:29:0x0073=Splitter:B:29:0x0073, B:14:0x002d=Splitter:B:14:0x002d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void clearAutologinTokenFromFile(java.lang.String r6) {
        /*
            java.lang.Class<com.ali.user.mobile.security.SecurityGuardManagerWraper> r0 = com.ali.user.mobile.security.SecurityGuardManagerWraper.class
            monitor-enter(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x000b
            monitor-exit(r0)
            return
        L_0x000b:
            android.content.Context r1 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "aliusersdk_session_lists"
            java.lang.String r1 = com.taobao.login4android.utils.FileUtils.readFileData(r1, r2)     // Catch:{ all -> 0x0078 }
            java.lang.String r1 = decrypt(r1)     // Catch:{ all -> 0x0078 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0078 }
            if (r2 == 0) goto L_0x0025
            com.ali.user.mobile.rpc.login.model.SessionList r1 = new com.ali.user.mobile.rpc.login.model.SessionList     // Catch:{ all -> 0x0078 }
            r1.<init>()     // Catch:{ all -> 0x0078 }
            goto L_0x002d
        L_0x0025:
            java.lang.Class<com.ali.user.mobile.rpc.login.model.SessionList> r2 = com.ali.user.mobile.rpc.login.model.SessionList.class
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r2)     // Catch:{ JSONException -> 0x0073 }
            com.ali.user.mobile.rpc.login.model.SessionList r1 = (com.ali.user.mobile.rpc.login.model.SessionList) r1     // Catch:{ JSONException -> 0x0073 }
        L_0x002d:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0078 }
            r2.<init>()     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x005e
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r3 = r1.sessionModels     // Catch:{ all -> 0x0078 }
            if (r3 == 0) goto L_0x005e
            java.util.List<com.ali.user.mobile.rpc.login.model.SessionModel> r3 = r1.sessionModels     // Catch:{ all -> 0x0078 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0078 }
        L_0x003e:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0078 }
            if (r4 == 0) goto L_0x005e
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0078 }
            com.ali.user.mobile.rpc.login.model.SessionModel r4 = (com.ali.user.mobile.rpc.login.model.SessionModel) r4     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = r4.userId     // Catch:{ all -> 0x0078 }
            boolean r5 = android.text.TextUtils.equals(r6, r5)     // Catch:{ all -> 0x0078 }
            if (r5 == 0) goto L_0x005a
            java.lang.String r5 = ""
            r4.autoLoginToken = r5     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = ""
            r4.sid = r5     // Catch:{ all -> 0x0078 }
        L_0x005a:
            r2.add(r4)     // Catch:{ all -> 0x0078 }
            goto L_0x003e
        L_0x005e:
            r1.sessionModels = r2     // Catch:{ all -> 0x0078 }
            android.content.Context r6 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "aliusersdk_session_lists"
            java.lang.String r1 = com.alibaba.fastjson.JSON.toJSONString(r1)     // Catch:{ all -> 0x0078 }
            java.lang.String r1 = encode(r1)     // Catch:{ all -> 0x0078 }
            com.taobao.login4android.utils.FileUtils.writeFileData(r6, r2, r1)     // Catch:{ all -> 0x0078 }
            monitor-exit(r0)
            return
        L_0x0073:
            emptySessionListFromFile()     // Catch:{ all -> 0x0078 }
            monitor-exit(r0)
            return
        L_0x0078:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.clearAutologinTokenFromFile(java.lang.String):void");
    }

    public static synchronized void putLoginHistory(HistoryAccount historyAccount, String str) {
        synchronized (SecurityGuardManagerWraper.class) {
            if (AlibabaSecurityTokenService.saveToken(historyAccount.tokenKey, str)) {
                saveHistoryOnly(historyAccount);
            }
        }
    }

    public static void saveHistoryOnly(HistoryAccount historyAccount) {
        LoginHistory loginHistory;
        String str;
        IDynamicDataStoreComponent dynamicDataStoreComp = getSecurityGuardManager().getDynamicDataStoreComp();
        if (dynamicDataStoreComp == null) {
            try {
                Properties properties = new Properties();
                properties.setProperty("errorCode", "80016");
                properties.setProperty("cause", "dynamicDataStoreComp = null");
                UserTrackAdapter.sendUT("Event_putLoginHistoryFail", properties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String str2 = "";
            try {
                str2 = dynamicDataStoreComp.getStringDDpEx(HISTORY_LOGIN_ACCOUNTS, 0);
            } catch (SecException e2) {
                e2.printStackTrace();
            }
            if (TextUtils.isEmpty(str2)) {
                loginHistory = new LoginHistory();
            } else {
                try {
                    loginHistory = (LoginHistory) JSON.parseObject(str2, LoginHistory.class);
                } catch (JSONException e3) {
                    TLogAdapter.e(TAG, "JSONException " + e3);
                    e3.printStackTrace();
                    try {
                        Properties properties2 = new Properties();
                        properties2.setProperty("errorCode", "80006");
                        properties2.setProperty("cause", "JSONException: " + str2);
                        UserTrackAdapter.sendUT("Event_putLoginHistoryFail", properties2);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    loginHistory = new LoginHistory();
                    try {
                        dynamicDataStoreComp.removeStringDDpEx(HISTORY_LOGIN_ACCOUNTS, 0);
                    } catch (SecException unused) {
                    }
                }
            }
            if (loginHistory != null) {
                if (loginHistory.accountHistory != null) {
                    ArrayList arrayList = new ArrayList();
                    if (historyAccount.alipayHid != 0) {
                        for (HistoryAccount next : loginHistory.accountHistory) {
                            if (next.alipayHid == historyAccount.alipayHid) {
                                next.update(historyAccount);
                                historyAccount = next;
                            } else {
                                arrayList.add(next);
                            }
                        }
                        arrayList.add(historyAccount);
                    } else {
                        for (HistoryAccount next2 : loginHistory.accountHistory) {
                            if (next2.userId == historyAccount.userId && next2.alipayHid == 0) {
                                next2.update(historyAccount);
                                historyAccount = next2;
                            } else {
                                arrayList.add(next2);
                            }
                        }
                        arrayList.add(historyAccount);
                    }
                    int size = arrayList.size() - DataProviderFactory.getDataProvider().getMaxHistoryAccount();
                    Collections.sort(arrayList, new HistoryAccount());
                    if (size > 0) {
                        AlibabaSecurityTokenService.removeSafeToken(((HistoryAccount) arrayList.remove(arrayList.size() - 1)).tokenKey);
                    }
                    loginHistory.accountHistory = arrayList;
                    loginHistory.index = arrayList.indexOf(historyAccount);
                    try {
                        dynamicDataStoreComp.putStringDDpEx(HISTORY_LOGIN_ACCOUNTS, JSON.toJSONString(loginHistory), 0);
                    } catch (SecException unused2) {
                    }
                } else if (DataProviderFactory.getDataProvider().getMaxHistoryAccount() > 0) {
                    loginHistory.accountHistory = new ArrayList();
                    loginHistory.accountHistory.add(historyAccount);
                    loginHistory.index = 0;
                    String jSONString = JSON.toJSONString(loginHistory);
                    try {
                        dynamicDataStoreComp.putStringDDpEx(HISTORY_LOGIN_ACCOUNTS, jSONString, 0);
                    } catch (SecException unused3) {
                    }
                    try {
                        str = dynamicDataStoreComp.getStringDDpEx(HISTORY_LOGIN_ACCOUNTS, 0);
                    } catch (SecException unused4) {
                        str = null;
                    }
                    if (!(jSONString == null && str == null) && (jSONString == null || !jSONString.equals(str))) {
                        try {
                            Properties properties3 = new Properties();
                            properties3.setProperty("errorCode", "80006");
                            properties3.setProperty("cause", "saveJson != getJson");
                            UserTrackAdapter.sendUT("Event_putLoginHistoryError", properties3);
                        } catch (Exception e5) {
                            e5.printStackTrace();
                        }
                    }
                }
            }
            updateMemoryHistory(loginHistory);
            TLogAdapter.e(TAG, "putLoginHistory Success");
            AppMonitorAdapter.commitSuccess("SecurityGuardManager", "putLoginHistory", historyAccount.nick + ",t=" + System.currentTimeMillis() + "umid=" + AppInfo.getInstance().getUmidToken());
        }
    }

    private static void updateMemoryHistory(LoginHistory loginHistory) {
        mLoginHistory = loginHistory;
        hadReadHistory = true;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001b */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0021 A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void updateLoginHistoryIndex(com.ali.user.mobile.rpc.HistoryAccount r10) {
        /*
            java.lang.Class<com.ali.user.mobile.security.SecurityGuardManagerWraper> r0 = com.ali.user.mobile.security.SecurityGuardManagerWraper.class
            monitor-enter(r0)
            if (r10 != 0) goto L_0x0007
            monitor-exit(r0)
            return
        L_0x0007:
            long r1 = r10.userId     // Catch:{ all -> 0x00d7 }
            java.lang.String r3 = ""
            r4 = 0
            com.alibaba.wireless.security.open.SecurityGuardManager r5 = getSecurityGuardManager()     // Catch:{ SecException -> 0x001b }
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r5 = r5.getDynamicDataStoreComp()     // Catch:{ SecException -> 0x001b }
            java.lang.String r6 = "aliusersdk_history_acounts"
            java.lang.String r5 = r5.getStringDDpEx(r6, r4)     // Catch:{ SecException -> 0x001b }
            r3 = r5
        L_0x001b:
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            if (r5 == 0) goto L_0x0023
            monitor-exit(r0)
            return
        L_0x0023:
            java.lang.Class<com.ali.user.mobile.rpc.LoginHistory> r5 = com.ali.user.mobile.rpc.LoginHistory.class
            java.lang.Object r3 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r3, r5)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            com.ali.user.mobile.rpc.LoginHistory r3 = (com.ali.user.mobile.rpc.LoginHistory) r3     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            if (r3 == 0) goto L_0x00d5
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r5 = r3.accountHistory     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            if (r5 == 0) goto L_0x00d5
            r5 = 0
        L_0x0032:
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r6 = r3.accountHistory     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            int r6 = r6.size()     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            if (r5 >= r6) goto L_0x0055
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r6 = r3.accountHistory     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            java.lang.Object r6 = r6.get(r5)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            com.ali.user.mobile.rpc.HistoryAccount r6 = (com.ali.user.mobile.rpc.HistoryAccount) r6     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            long r7 = r6.userId     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            int r9 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r9 != 0) goto L_0x0052
            long r1 = r10.loginTime     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            r6.loginTime = r1     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            int r10 = r10.hasPwd     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            r6.hasPwd = r10     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            r10 = 1
            goto L_0x0056
        L_0x0052:
            int r5 = r5 + 1
            goto L_0x0032
        L_0x0055:
            r10 = 0
        L_0x0056:
            if (r10 == 0) goto L_0x00d5
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r10 = r3.accountHistory     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            com.ali.user.mobile.rpc.HistoryAccount r1 = new com.ali.user.mobile.rpc.HistoryAccount     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            r1.<init>()     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            java.util.Collections.sort(r10, r1)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            r3.index = r4     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            java.lang.String r10 = com.alibaba.fastjson.JSON.toJSONString(r3)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            com.alibaba.wireless.security.open.SecurityGuardManager r1 = getSecurityGuardManager()     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r1 = r1.getDynamicDataStoreComp()     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            java.lang.String r2 = "aliusersdk_history_acounts"
            r1.putStringDDpEx(r2, r10, r4)     // Catch:{ JSONException -> 0x00a4, Exception -> 0x0076 }
            goto L_0x00d5
        L_0x0076:
            r10 = move-exception
            java.util.Properties r1 = new java.util.Properties     // Catch:{ Exception -> 0x009f }
            r1.<init>()     // Catch:{ Exception -> 0x009f }
            java.lang.String r2 = "errorCode"
            java.lang.String r3 = "80117"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x009f }
            java.lang.String r2 = "cause"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x009f }
            r3.<init>()     // Catch:{ Exception -> 0x009f }
            java.lang.String r4 = "Exception"
            r3.append(r4)     // Catch:{ Exception -> 0x009f }
            r3.append(r10)     // Catch:{ Exception -> 0x009f }
            java.lang.String r10 = r3.toString()     // Catch:{ Exception -> 0x009f }
            r1.setProperty(r2, r10)     // Catch:{ Exception -> 0x009f }
            java.lang.String r10 = "Event_updateLoginHistoryFailException"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r10, (java.util.Properties) r1)     // Catch:{ Exception -> 0x009f }
            goto L_0x00d5
        L_0x009f:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ all -> 0x00d7 }
            goto L_0x00d5
        L_0x00a4:
            r10 = move-exception
            java.util.Properties r1 = new java.util.Properties     // Catch:{ Exception -> 0x00d1 }
            r1.<init>()     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r2 = "errorCode"
            java.lang.String r3 = "80117"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r2 = "cause"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d1 }
            r3.<init>()     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r4 = "Exception:"
            r3.append(r4)     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r10 = r10.getMessage()     // Catch:{ Exception -> 0x00d1 }
            r3.append(r10)     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r10 = r3.toString()     // Catch:{ Exception -> 0x00d1 }
            r1.setProperty(r2, r10)     // Catch:{ Exception -> 0x00d1 }
            java.lang.String r10 = "Event_updateLoginHistoryFailJsonException"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r10, (java.util.Properties) r1)     // Catch:{ Exception -> 0x00d1 }
            goto L_0x00d5
        L_0x00d1:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ all -> 0x00d7 }
        L_0x00d5:
            monitor-exit(r0)
            return
        L_0x00d7:
            r10 = move-exception
            monitor-exit(r0)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.updateLoginHistoryIndex(com.ali.user.mobile.rpc.HistoryAccount):void");
    }

    public static synchronized void updateLoginToken(long j, String str) {
        synchronized (SecurityGuardManagerWraper.class) {
            try {
                String stringDDpEx = getSecurityGuardManager().getDynamicDataStoreComp().getStringDDpEx(HISTORY_LOGIN_ACCOUNTS, 0);
                if (!TextUtils.isEmpty(stringDDpEx)) {
                    LoginHistory loginHistory = (LoginHistory) JSON.parseObject(stringDDpEx, LoginHistory.class);
                    if (!(loginHistory == null || loginHistory.accountHistory == null)) {
                        boolean z = false;
                        for (int i = 0; i < loginHistory.accountHistory.size(); i++) {
                            HistoryAccount historyAccount = loginHistory.accountHistory.get(i);
                            if (historyAccount.userId == j) {
                                historyAccount.autologinToken = str;
                                z = true;
                            }
                        }
                        if (z) {
                            getSecurityGuardManager().getDynamicDataStoreComp().putStringDDpEx(HISTORY_LOGIN_ACCOUNTS, JSON.toJSONString(loginHistory), 0);
                        }
                    }
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001b */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0021 A[Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.user.mobile.rpc.LoginHistory getLoginHistory() {
        /*
            com.ali.user.mobile.rpc.LoginHistory r0 = mLoginHistory     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            if (r0 != 0) goto L_0x0057
            boolean r0 = hadReadHistory     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            if (r0 == 0) goto L_0x0009
            goto L_0x0057
        L_0x0009:
            java.lang.String r0 = ""
            r1 = 0
            com.alibaba.wireless.security.open.SecurityGuardManager r2 = getSecurityGuardManager()     // Catch:{ SecException -> 0x001b }
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r2 = r2.getDynamicDataStoreComp()     // Catch:{ SecException -> 0x001b }
            java.lang.String r3 = "aliusersdk_history_acounts"
            java.lang.String r2 = r2.getStringDDpEx(r3, r1)     // Catch:{ SecException -> 0x001b }
            r0 = r2
        L_0x001b:
            boolean r2 = com.ali.user.mobile.app.init.Debuggable.isDebug()     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            if (r2 == 0) goto L_0x0037
            java.lang.String r2 = "login.SecurityManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            r3.<init>()     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            java.lang.String r4 = "getLoginHistoryJson="
            r3.append(r4)     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            r3.append(r0)     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            java.lang.String r3 = r3.toString()     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            com.ali.user.mobile.log.TLogAdapter.d(r2, r3)     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
        L_0x0037:
            java.lang.Class<com.ali.user.mobile.rpc.LoginHistory> r2 = com.ali.user.mobile.rpc.LoginHistory.class
            java.lang.Object r0 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r0, r2)     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            com.ali.user.mobile.rpc.LoginHistory r0 = (com.ali.user.mobile.rpc.LoginHistory) r0     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            if (r0 == 0) goto L_0x0051
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r2 = r0.accountHistory     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            if (r2 == 0) goto L_0x0051
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r2 = r0.accountHistory     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            com.ali.user.mobile.rpc.HistoryAccount r3 = new com.ali.user.mobile.rpc.HistoryAccount     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            r3.<init>()     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            java.util.Collections.sort(r2, r3)     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            r0.index = r1     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
        L_0x0051:
            mLoginHistory = r0     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            r1 = 1
            hadReadHistory = r1     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            return r0
        L_0x0057:
            com.ali.user.mobile.rpc.LoginHistory r0 = mLoginHistory     // Catch:{ JSONException -> 0x0088, Exception -> 0x005a, Throwable -> 0x00b5 }
            return r0
        L_0x005a:
            r0 = move-exception
            java.util.Properties r1 = new java.util.Properties     // Catch:{ Exception -> 0x0083 }
            r1.<init>()     // Catch:{ Exception -> 0x0083 }
            java.lang.String r2 = "errorCode"
            java.lang.String r3 = "80027"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x0083 }
            java.lang.String r2 = "cause"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0083 }
            r3.<init>()     // Catch:{ Exception -> 0x0083 }
            java.lang.String r4 = "Exception"
            r3.append(r4)     // Catch:{ Exception -> 0x0083 }
            r3.append(r0)     // Catch:{ Exception -> 0x0083 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x0083 }
            r1.setProperty(r2, r0)     // Catch:{ Exception -> 0x0083 }
            java.lang.String r0 = "Event_getLoginHistoryFailException"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r0, (java.util.Properties) r1)     // Catch:{ Exception -> 0x0083 }
            goto L_0x00b5
        L_0x0083:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00b5
        L_0x0088:
            r0 = move-exception
            java.util.Properties r1 = new java.util.Properties     // Catch:{ Exception -> 0x00b1 }
            r1.<init>()     // Catch:{ Exception -> 0x00b1 }
            java.lang.String r2 = "errorCode"
            java.lang.String r3 = "80017"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x00b1 }
            java.lang.String r2 = "cause"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b1 }
            r3.<init>()     // Catch:{ Exception -> 0x00b1 }
            java.lang.String r4 = "Exception"
            r3.append(r4)     // Catch:{ Exception -> 0x00b1 }
            r3.append(r0)     // Catch:{ Exception -> 0x00b1 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x00b1 }
            r1.setProperty(r2, r0)     // Catch:{ Exception -> 0x00b1 }
            java.lang.String r0 = "Event_getLoginHistoryFailJsonException"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r0, (java.util.Properties) r1)     // Catch:{ Exception -> 0x00b1 }
            goto L_0x00b5
        L_0x00b1:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00b5:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.getLoginHistory():com.ali.user.mobile.rpc.LoginHistory");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|3|4|5|(1:7)(3:8|(1:10)|(2:12|13)(1:14))) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0070, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0071, code lost:
        com.ali.user.mobile.log.AppMonitorAdapter.commitFail("SecurityGuardManager", "getHistoryAccountsFail", "325", "exception=" + r1 + ",umid=" + com.ali.user.mobile.info.AppInfo.getInstance().getUmidToken() + ",t=" + java.lang.System.currentTimeMillis());
        r1.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00aa, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x003f */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0045 A[Catch:{ Exception -> 0x0070 }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0046 A[Catch:{ Exception -> 0x0070 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.ali.user.mobile.rpc.HistoryAccount> getHistoryAccounts() {
        /*
            long r0 = java.lang.System.currentTimeMillis()
            java.lang.String r2 = "SecurityGuardManager"
            java.lang.String r3 = "getHistoryAccounts"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = " t = "
            r4.append(r5)
            r4.append(r0)
            java.lang.String r0 = "umid="
            r4.append(r0)
            com.ali.user.mobile.info.AppInfo r0 = com.ali.user.mobile.info.AppInfo.getInstance()
            java.lang.String r0 = r0.getUmidToken()
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            com.ali.user.mobile.log.AppMonitorAdapter.commitSuccess(r2, r3, r0)
            r0 = 0
            java.lang.String r1 = ""
            com.alibaba.wireless.security.open.SecurityGuardManager r2 = getSecurityGuardManager()     // Catch:{ SecException -> 0x003f }
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r2 = r2.getDynamicDataStoreComp()     // Catch:{ SecException -> 0x003f }
            java.lang.String r3 = "aliusersdk_history_acounts"
            r4 = 0
            java.lang.String r2 = r2.getStringDDpEx(r3, r4)     // Catch:{ SecException -> 0x003f }
            r1 = r2
        L_0x003f:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0070 }
            if (r2 == 0) goto L_0x0046
            return r0
        L_0x0046:
            java.lang.Class<com.ali.user.mobile.rpc.LoginHistory> r2 = com.ali.user.mobile.rpc.LoginHistory.class
            java.lang.Object r2 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r2)     // Catch:{ Exception -> 0x0070 }
            com.ali.user.mobile.rpc.LoginHistory r2 = (com.ali.user.mobile.rpc.LoginHistory) r2     // Catch:{ Exception -> 0x0070 }
            boolean r3 = com.ali.user.mobile.app.init.Debuggable.isDebug()     // Catch:{ Exception -> 0x0070 }
            if (r3 == 0) goto L_0x006a
            java.lang.String r3 = "login.SecurityManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0070 }
            r4.<init>()     // Catch:{ Exception -> 0x0070 }
            java.lang.String r5 = "loginHistoryJson="
            r4.append(r5)     // Catch:{ Exception -> 0x0070 }
            r4.append(r1)     // Catch:{ Exception -> 0x0070 }
            java.lang.String r1 = r4.toString()     // Catch:{ Exception -> 0x0070 }
            com.ali.user.mobile.log.TLogAdapter.d(r3, r1)     // Catch:{ Exception -> 0x0070 }
        L_0x006a:
            if (r2 == 0) goto L_0x006f
            java.util.List<com.ali.user.mobile.rpc.HistoryAccount> r1 = r2.accountHistory     // Catch:{ Exception -> 0x0070 }
            return r1
        L_0x006f:
            return r0
        L_0x0070:
            r1 = move-exception
            java.lang.String r2 = "SecurityGuardManager"
            java.lang.String r3 = "getHistoryAccountsFail"
            java.lang.String r4 = "325"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "exception="
            r5.append(r6)
            r5.append(r1)
            java.lang.String r6 = ",umid="
            r5.append(r6)
            com.ali.user.mobile.info.AppInfo r6 = com.ali.user.mobile.info.AppInfo.getInstance()
            java.lang.String r6 = r6.getUmidToken()
            r5.append(r6)
            java.lang.String r6 = ",t="
            r5.append(r6)
            long r6 = java.lang.System.currentTimeMillis()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.ali.user.mobile.log.AppMonitorAdapter.commitFail(r2, r3, r4, r5)
            r1.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.getHistoryAccounts():java.util.List");
    }

    public static SessionList getSessionListFromFile() {
        try {
            String decrypt = decrypt(FileUtils.readFileData(DataProviderFactory.getApplicationContext(), SESSION_LIST));
            if (TextUtils.isEmpty(decrypt)) {
                return null;
            }
            return (SessionList) JSON.parseObject(decrypt, SessionList.class);
        } catch (Exception unused) {
            return null;
        }
    }

    public static SessionModel findSessionFromModel(String str) {
        SessionList sessionListFromFile;
        if (TextUtils.isEmpty(str) || (sessionListFromFile = getSessionListFromFile()) == null || sessionListFromFile.sessionModels == null || sessionListFromFile.sessionModels.size() == 0) {
            return null;
        }
        for (SessionModel next : sessionListFromFile.sessionModels) {
            if (TextUtils.equals(str, next.userId)) {
                return next;
            }
        }
        return null;
    }

    public static boolean equalPattern(GestureModel gestureModel) {
        GestureList gestureList;
        if (gestureModel == null || TextUtils.isEmpty(gestureModel.userId) || TextUtils.isEmpty(gestureModel.pattern)) {
            return false;
        }
        try {
            String decrypt = decrypt(FileUtils.readFileData(DataProviderFactory.getApplicationContext(), GESTURE_LIST));
            if (!(TextUtils.isEmpty(decrypt) || (gestureList = (GestureList) JSON.parseObject(decrypt, GestureList.class)) == null || gestureList.gestureList == null)) {
                if (gestureList.gestureList.size() != 0) {
                    for (GestureModel next : gestureList.gestureList) {
                        if (TextUtils.equals(next.userId, gestureModel.userId) && TextUtils.equals(next.pattern, gestureModel.pattern)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.user.mobile.rpc.HistoryAccount matchHistoryAccount(java.lang.String r4) {
        /*
            java.util.List r0 = getHistoryAccounts()
            r1 = 0
            if (r0 == 0) goto L_0x0041
            java.util.Iterator r0 = r0.iterator()
        L_0x000b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0040
            java.lang.Object r2 = r0.next()
            com.ali.user.mobile.rpc.HistoryAccount r2 = (com.ali.user.mobile.rpc.HistoryAccount) r2
            java.lang.String r3 = r2.userInputName
            boolean r3 = android.text.TextUtils.equals(r4, r3)
            if (r3 != 0) goto L_0x003f
            java.lang.String r3 = r2.nick
            boolean r3 = android.text.TextUtils.equals(r4, r3)
            if (r3 != 0) goto L_0x003f
            java.lang.String r3 = r2.mobile
            boolean r3 = android.text.TextUtils.equals(r4, r3)
            if (r3 != 0) goto L_0x003f
            java.lang.String r3 = r2.email
            boolean r3 = android.text.TextUtils.equals(r4, r3)
            if (r3 != 0) goto L_0x003f
            java.lang.String r3 = r2.autologinToken
            boolean r3 = android.text.TextUtils.equals(r4, r3)
            if (r3 == 0) goto L_0x000b
        L_0x003f:
            return r2
        L_0x0040:
            return r1
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.security.SecurityGuardManagerWraper.matchHistoryAccount(java.lang.String):com.ali.user.mobile.rpc.HistoryAccount");
    }

    public static HistoryAccount findHistoryAccount(long j) {
        try {
            List<HistoryAccount> historyAccounts = getHistoryAccounts();
            if (historyAccounts == null) {
                return null;
            }
            for (HistoryAccount next : historyAccounts) {
                if (j == next.userId) {
                    return next;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasHistoryAccounts() {
        List<HistoryAccount> historyAccounts = getHistoryAccounts();
        return historyAccounts != null && historyAccounts.size() > 0;
    }

    public static String encode(String str) {
        try {
            IDynamicDataEncryptComponent dynamicDataEncryptComp = getSecurityGuardManager().getDynamicDataEncryptComp();
            if (dynamicDataEncryptComp != null) {
                String dynamicEncryptDDp = dynamicDataEncryptComp.dynamicEncryptDDp(str);
                return TextUtils.isEmpty(dynamicEncryptDDp) ? str : dynamicEncryptDDp;
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public static String decrypt(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            IDynamicDataEncryptComponent dynamicDataEncryptComp = getSecurityGuardManager().getDynamicDataEncryptComp();
            if (dynamicDataEncryptComp != null) {
                String dynamicDecryptDDp = dynamicDataEncryptComp.dynamicDecryptDDp(str);
                return TextUtils.isEmpty(dynamicDecryptDDp) ? dynamicDataEncryptComp.dynamicDecrypt(str) : dynamicDecryptDDp;
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public static String getDeviceTokenKey(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                HistoryAccount findHistoryAccount = findHistoryAccount(Long.parseLong(str));
                if (findHistoryAccount != null) {
                    return findHistoryAccount.tokenKey;
                }
                return null;
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }
}
