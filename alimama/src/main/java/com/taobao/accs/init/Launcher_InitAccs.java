package com.taobao.accs.init;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.ILoginInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.utl.ALog;
import com.taobao.weex.ui.component.WXBasicComponentType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

public class Launcher_InitAccs implements Serializable {
    public static final Map<String, String> SERVICES = new HashMap();
    private static final String TAG = "Launcher_InitAccs";
    public static String defaultAppkey = "21646297";
    public static IAppReceiver mAppReceiver = new IAppReceiver() {
        public void onUnbindUser(int i) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onUnbindUser, errorCode:" + i, new Object[0]);
            }
        }

        public void onUnbindApp(int i) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onUnbindApp,  errorCode:" + i, new Object[0]);
            }
        }

        public void onSendData(String str, int i) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onSendData,  dataId:" + str + " errorCode:" + i, new Object[0]);
            }
        }

        public void onData(String str, String str2, byte[] bArr) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                StringBuilder sb = new StringBuilder();
                sb.append("onData,  userId:");
                sb.append(str);
                sb.append("dataId:");
                sb.append(str2);
                sb.append(" dataLen:");
                sb.append(bArr == null ? 0 : bArr.length);
                ALog.d(Launcher_InitAccs.TAG, sb.toString(), new Object[0]);
            }
        }

        public void onBindUser(String str, int i) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onBindUser, userId:" + str + " errorCode:" + i, new Object[0]);
            }
            if (i == 300) {
                ACCSManager.bindApp(Launcher_InitAccs.mContext, AppInfoUtil.getAppkey(), Launcher_InitAccs.mTtid, (IAppReceiver) null);
            }
        }

        public void onBindApp(int i) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onBindApp,  errorCode:" + i, new Object[0]);
            }
            if (i != 200) {
                return;
            }
            if (!TextUtils.isEmpty(Launcher_InitAccs.mUserId)) {
                ACCSManager.bindUser(Launcher_InitAccs.mContext, Launcher_InitAccs.mUserId, Launcher_InitAccs.mForceBindUser);
                Launcher_InitAccs.mForceBindUser = false;
            } else if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(Launcher_InitAccs.TAG, "onBindApp,  bindUser userid :" + Launcher_InitAccs.mUserId, new Object[0]);
            }
        }

        public String getService(String str) {
            return Launcher_InitAccs.SERVICES.get(str);
        }

        public Map<String, String> getAllServices() {
            return Launcher_InitAccs.SERVICES;
        }
    };
    public static String mAppkey = null;
    public static Context mContext = null;
    public static boolean mForceBindUser = false;
    public static boolean mIsInited = false;
    public static String mSid;
    public static String mTtid;
    public static String mUserId;

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00aa A[Catch:{ Throwable -> 0x0134 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00bd A[Catch:{ Throwable -> 0x0134 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init(android.app.Application r9, java.util.HashMap<java.lang.String, java.lang.Object> r10) {
        /*
            r8 = this;
            java.lang.String r0 = "Launcher_InitAccs"
            java.lang.String r1 = "init"
            r2 = 0
            java.lang.Object[] r3 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.i(r0, r1, r3)
            r0 = 1
            anet.channel.AwcnConfig.setAccsSessionCreateForbiddenInBg(r0)     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x0134 }
            mContext = r9     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            android.content.pm.ApplicationInfo r9 = r9.getApplicationInfo()     // Catch:{ Throwable -> 0x0134 }
            int r9 = r9.flags     // Catch:{ Throwable -> 0x0134 }
            r1 = 2
            r9 = r9 & r1
            if (r9 == 0) goto L_0x0022
            r9 = 1
            goto L_0x0023
        L_0x0022:
            r9 = 0
        L_0x0023:
            if (r9 == 0) goto L_0x002a
            com.taobao.accs.utl.ALog.isUseTlog = r2     // Catch:{ Throwable -> 0x0134 }
            anet.channel.util.ALog.setUseTlog(r2)     // Catch:{ Throwable -> 0x0134 }
        L_0x002a:
            r9 = 0
            r3 = 3
            java.lang.String r4 = "envIndex"
            java.lang.Object r4 = r10.get(r4)     // Catch:{ Throwable -> 0x0096 }
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch:{ Throwable -> 0x0096 }
            int r4 = r4.intValue()     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r5 = "onlineAppKey"
            java.lang.Object r5 = r10.get(r5)     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x0096 }
            mAppkey = r5     // Catch:{ Throwable -> 0x0096 }
            if (r4 != r0) goto L_0x0050
            java.lang.String r4 = "preAppKey"
            java.lang.Object r4 = r10.get(r4)     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x0096 }
            mAppkey = r4     // Catch:{ Throwable -> 0x0096 }
            r4 = 1
            goto L_0x006a
        L_0x0050:
            if (r4 != r1) goto L_0x0054
            r5 = 1
            goto L_0x0055
        L_0x0054:
            r5 = 0
        L_0x0055:
            if (r4 != r3) goto L_0x0059
            r4 = 1
            goto L_0x005a
        L_0x0059:
            r4 = 0
        L_0x005a:
            r4 = r4 | r5
            if (r4 == 0) goto L_0x0069
            java.lang.String r4 = "dailyAppkey"
            java.lang.Object r4 = r10.get(r4)     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x0096 }
            mAppkey = r4     // Catch:{ Throwable -> 0x0096 }
            r4 = 2
            goto L_0x006a
        L_0x0069:
            r4 = 0
        L_0x006a:
            java.lang.String r5 = "process"
            java.lang.Object r5 = r10.get(r5)     // Catch:{ Throwable -> 0x0093 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x0093 }
            java.lang.String r9 = "ttid"
            java.lang.Object r9 = r10.get(r9)     // Catch:{ Throwable -> 0x0091 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Throwable -> 0x0091 }
            mTtid = r9     // Catch:{ Throwable -> 0x0091 }
            java.lang.String r9 = "userId"
            java.lang.Object r9 = r10.get(r9)     // Catch:{ Throwable -> 0x0091 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Throwable -> 0x0091 }
            mUserId = r9     // Catch:{ Throwable -> 0x0091 }
            java.lang.String r9 = "sid"
            java.lang.Object r9 = r10.get(r9)     // Catch:{ Throwable -> 0x0091 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Throwable -> 0x0091 }
            mSid = r9     // Catch:{ Throwable -> 0x0091 }
            goto L_0x00a2
        L_0x0091:
            r10 = move-exception
            goto L_0x0099
        L_0x0093:
            r10 = move-exception
            r5 = r9
            goto L_0x0099
        L_0x0096:
            r10 = move-exception
            r5 = r9
            r4 = 0
        L_0x0099:
            java.lang.String r9 = "Launcher_InitAccs"
            java.lang.String r6 = "init get param error"
            java.lang.Object[] r7 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.utl.ALog.e(r9, r6, r10, r7)     // Catch:{ Throwable -> 0x0134 }
        L_0x00a2:
            java.lang.String r9 = mAppkey     // Catch:{ Throwable -> 0x0134 }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0134 }
            if (r9 == 0) goto L_0x00b7
            java.lang.String r9 = "Launcher_InitAccs"
            java.lang.String r10 = "init get appkey null！！"
            java.lang.Object[] r6 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.utl.ALog.e(r9, r10, r6)     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r9 = defaultAppkey     // Catch:{ Throwable -> 0x0134 }
            mAppkey = r9     // Catch:{ Throwable -> 0x0134 }
        L_0x00b7:
            boolean r9 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0134 }
            if (r9 == 0) goto L_0x00d0
            java.lang.String r9 = "Launcher_InitAccs"
            java.lang.String r10 = "init get process null！！"
            java.lang.Object[] r5 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.utl.ALog.e(r9, r10, r5)     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            int r10 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r5 = com.taobao.accs.utl.AdapterUtilityImpl.getProcessName(r9, r10)     // Catch:{ Throwable -> 0x0134 }
        L_0x00d0:
            java.lang.String r9 = "Launcher_InitAccs"
            java.lang.String r10 = "init"
            r6 = 6
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r7 = "appkey"
            r6[r2] = r7     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r7 = mAppkey     // Catch:{ Throwable -> 0x0134 }
            r6[r0] = r7     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r0 = "mode"
            r6[r1] = r0     // Catch:{ Throwable -> 0x0134 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x0134 }
            r6[r3] = r0     // Catch:{ Throwable -> 0x0134 }
            r0 = 4
            java.lang.String r1 = "process"
            r6[r0] = r1     // Catch:{ Throwable -> 0x0134 }
            r0 = 5
            r6[r0] = r5     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.utl.ALog.i(r9, r10, r6)     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r10 = mAppkey     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.ACCSManager.setAppkey(r9, r10, r4)     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.ACCSManager.setMode(r9, r4)     // Catch:{ Throwable -> 0x0134 }
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.init.Launcher_InitAccs$AccsLoginInfo r10 = new com.taobao.accs.init.Launcher_InitAccs$AccsLoginInfo     // Catch:{ Throwable -> 0x0134 }
            r10.<init>()     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.ACCSManager.setLoginInfoImpl(r9, r10)     // Catch:{ Throwable -> 0x0134 }
            boolean r9 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0134 }
            if (r9 != 0) goto L_0x0127
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r9 = r9.getPackageName()     // Catch:{ Throwable -> 0x0134 }
            boolean r9 = r5.equals(r9)     // Catch:{ Throwable -> 0x0134 }
            if (r9 == 0) goto L_0x0127
            android.content.Context r9 = mContext     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r10 = mAppkey     // Catch:{ Throwable -> 0x0134 }
            java.lang.String r0 = mTtid     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.IAppReceiver r1 = mAppReceiver     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.ACCSManager.startInAppConnection(r9, r10, r0, r1)     // Catch:{ Throwable -> 0x0134 }
        L_0x0127:
            com.taobao.accs.init.Launcher_InitAccs$1 r9 = new com.taobao.accs.init.Launcher_InitAccs$1     // Catch:{ Throwable -> 0x0134 }
            r9.<init>()     // Catch:{ Throwable -> 0x0134 }
            r0 = 10
            java.util.concurrent.TimeUnit r10 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ Throwable -> 0x0134 }
            com.taobao.accs.common.ThreadPoolExecutorFactory.schedule(r9, r0, r10)     // Catch:{ Throwable -> 0x0134 }
            goto L_0x013e
        L_0x0134:
            r9 = move-exception
            java.lang.String r10 = "Launcher_InitAccs"
            java.lang.String r0 = "init"
            java.lang.Object[] r1 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r10, r0, r9, r1)
        L_0x013e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.init.Launcher_InitAccs.init(android.app.Application, java.util.HashMap):void");
    }

    static class AccsLoginInfo implements ILoginInfo {
        public boolean getCommentUsed() {
            return false;
        }

        public String getEcode() {
            return null;
        }

        public String getHeadPicLink() {
            return null;
        }

        public String getNick() {
            return null;
        }

        public String getSsoToken() {
            return null;
        }

        AccsLoginInfo() {
        }

        public String getSid() {
            return Launcher_InitAccs.mSid;
        }

        public String getUserId() {
            return Launcher_InitAccs.mUserId;
        }
    }

    static {
        SERVICES.put("im", "com.taobao.tao.amp.remote.AccsReceiverCallback");
        SERVICES.put("powermsg", "com.taobao.appfrmbundle.mkt.AccsReceiverService");
        SERVICES.put("pmmonitor", "com.taobao.appfrmbundle.mkt.AccsReceiverService");
        SERVICES.put("cloudsync", "com.taobao.datasync.network.accs.AccsCloudSyncService");
        SERVICES.put("acds", "com.taobao.acds.compact.AccsACDSService");
        SERVICES.put(GlobalClientInfo.AGOO_SERVICE_ID, "org.android.agoo.accs.AgooService");
        SERVICES.put(AgooConstants.AGOO_SERVICE_AGOOACK, "org.android.agoo.accs.AgooService");
        SERVICES.put("agooTokenReport", "org.android.agoo.accs.AgooService");
        SERVICES.put("AliLive", "com.taobao.playbudyy.gameplugin.danmu.DanmuCallbackService");
        SERVICES.put("orange", "com.taobao.orange.accssupport.OrangeAccsService");
        SERVICES.put("tsla", "com.taobao.android.festival.accs.HomepageAccsMassService");
        SERVICES.put("taobaoWaimaiAccsService", "com.taobao.takeout.order.detail.service.TakeoutOrderDetailACCSService");
        SERVICES.put("login", "com.taobao.android.sso.v2.service.LoginAccsService");
        SERVICES.put("ranger_abtest", "com.taobao.ranger3.RangerACCSService");
        SERVICES.put("accs_poplayer", "com.taobao.tbpoplayer.AccsPopLayerService");
        SERVICES.put("dm_abtest", "com.tmall.wireless.ant.accs.AntAccsService");
        SERVICES.put("family", "com.taobao.family.FamilyAccsService");
        SERVICES.put("taobao-dingtalk", "com.laiwang.protocol.android.LwpAccsService");
        SERVICES.put("amp-sync", "com.taobao.message.init.accs.AccsReceiverCallback");
        SERVICES.put("friend_invite_msg", "com.taobao.message.init.accs.TaoFriendAccsReceiverCallback");
        SERVICES.put(WXBasicComponentType.SLIDER, "com.taobao.slide.accs.SlideAccsService");
    }
}
