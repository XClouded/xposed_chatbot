package com.taobao.accs.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import anet.channel.SessionCenter;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.accs.ErrorCode;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.IConnectionService;
import com.taobao.accs.ILoginInfo;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.data.Message;
import com.taobao.accs.data.MsgDistribute;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.ForeBackManager;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.constant.Constants;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ACCSManagerImpl implements IACCSManager {
    /* access modifiers changed from: private */
    public static String TAG = "ACCSMgrImpl_";
    private int baseDataId = 0;
    /* access modifiers changed from: private */
    public IConnectionService connectionService;
    /* access modifiers changed from: private */
    public String mConfigTag;

    public String getUserUnit() {
        return null;
    }

    public class StateReceiver extends BroadcastReceiver {
        public StateReceiver() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:26:0x0058 A[Catch:{ Exception -> 0x0223 }] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x005a A[Catch:{ Exception -> 0x0223 }] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x00bd A[Catch:{ Exception -> 0x0223 }] */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x00fe A[Catch:{ Exception -> 0x0223 }] */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x0169 A[Catch:{ Exception -> 0x0223 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                r0 = 0
                com.taobao.accs.internal.ACCSManagerImpl r1 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.String r1 = r1.mConfigTag     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.AccsClientConfig r1 = com.taobao.accs.AccsClientConfig.getConfigByTag(r1)     // Catch:{ Exception -> 0x0223 }
                java.lang.String r2 = r10.getAction()     // Catch:{ Exception -> 0x0223 }
                r3 = -1
                int r4 = r2.hashCode()     // Catch:{ Exception -> 0x0223 }
                r5 = -1939419492(0xffffffff8c66ce9c, float:-1.778073E-31)
                r6 = 2
                r7 = 1
                if (r4 == r5) goto L_0x0049
                r5 = -1322929215(0xffffffffb125b3c1, float:-2.4112838E-9)
                if (r4 == r5) goto L_0x003f
                r5 = -1034470443(0xffffffffc2573bd5, float:-53.80843)
                if (r4 == r5) goto L_0x0035
                r5 = -1034337366(0xffffffffc25943aa, float:-54.31608)
                if (r4 == r5) goto L_0x002b
                goto L_0x0053
            L_0x002b:
                java.lang.String r4 = "com.taobao.accs.ACTION_STATE_FORE"
                boolean r2 = r2.equals(r4)     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x0053
                r2 = 1
                goto L_0x0054
            L_0x0035:
                java.lang.String r4 = "com.taobao.accs.ACTION_STATE_BACK"
                boolean r2 = r2.equals(r4)     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x0053
                r2 = 2
                goto L_0x0054
            L_0x003f:
                java.lang.String r4 = "com.taobao.accs.ACTION_STATE_DEEPBACK"
                boolean r2 = r2.equals(r4)     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x0053
                r2 = 3
                goto L_0x0054
            L_0x0049:
                java.lang.String r4 = "com.taobao.aipc.DISCONNECT"
                boolean r2 = r2.equals(r4)     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x0053
                r2 = 0
                goto L_0x0054
            L_0x0053:
                r2 = -1
            L_0x0054:
                r3 = 0
                switch(r2) {
                    case 0: goto L_0x0169;
                    case 1: goto L_0x00fe;
                    case 2: goto L_0x00bd;
                    case 3: goto L_0x005a;
                    default: goto L_0x0058;
                }     // Catch:{ Exception -> 0x0223 }
            L_0x0058:
                goto L_0x022f
            L_0x005a:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 != 0) goto L_0x0073
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.Class<com.taobao.accs.IConnectionService> r10 = com.taobao.accs.IConnectionService.class
                java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0223 }
                r2[r0] = r1     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r10 = com.taobao.aipc.AIPC.getService(r10, r2)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r10 = (com.taobao.accs.IConnectionService) r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
            L_0x0073:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 == 0) goto L_0x00a8
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                r9.setForeBackState(r6)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r10 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r10 = r10.connectionService     // Catch:{ Exception -> 0x0223 }
                java.lang.String r10 = r10.getHost(r3)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.data.Message r10 = com.taobao.accs.data.Message.buildBackground(r10)     // Catch:{ Exception -> 0x0223 }
                r9.sendMessage(r10)     // Catch:{ Exception -> 0x0223 }
                java.lang.String r9 = com.taobao.accs.internal.ACCSManagerImpl.TAG     // Catch:{ Exception -> 0x0223 }
                java.lang.String r10 = "send background state frame"
                java.lang.Object[] r1 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.utl.ALog.i(r9, r10, r1)     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x00a8:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ConnectionServiceImpl r10 = new com.taobao.accs.internal.ConnectionServiceImpl     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r1 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.String r1 = r1.mConfigTag     // Catch:{ Exception -> 0x0223 }
                r10.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.utl.OrangeAdapter.resetChannelModeEnable()     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x00bd:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 != 0) goto L_0x00d6
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.Class<com.taobao.accs.IConnectionService> r10 = com.taobao.accs.IConnectionService.class
                java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0223 }
                r2[r0] = r1     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r10 = com.taobao.aipc.AIPC.getService(r10, r2)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r10 = (com.taobao.accs.IConnectionService) r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
            L_0x00d6:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 == 0) goto L_0x00e9
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                r9.setForeBackState(r0)     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x00e9:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ConnectionServiceImpl r10 = new com.taobao.accs.internal.ConnectionServiceImpl     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r1 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.String r1 = r1.mConfigTag     // Catch:{ Exception -> 0x0223 }
                r10.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.utl.OrangeAdapter.resetChannelModeEnable()     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x00fe:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 != 0) goto L_0x0117
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.Class<com.taobao.accs.IConnectionService> r2 = com.taobao.accs.IConnectionService.class
                java.lang.Object[] r4 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0223 }
                r4[r0] = r1     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r1 = com.taobao.aipc.AIPC.getService(r2, r4)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r1 = (com.taobao.accs.IConnectionService) r1     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r1     // Catch:{ Exception -> 0x0223 }
            L_0x0117:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 == 0) goto L_0x0154
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                r9.setForeBackState(r7)     // Catch:{ Exception -> 0x0223 }
                java.lang.String r9 = "state"
                boolean r9 = r10.getBooleanExtra(r9, r7)     // Catch:{ Exception -> 0x0223 }
                if (r9 == 0) goto L_0x022f
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r10 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r10 = r10.connectionService     // Catch:{ Exception -> 0x0223 }
                java.lang.String r10 = r10.getHost(r3)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.data.Message r10 = com.taobao.accs.data.Message.buildForeground(r10)     // Catch:{ Exception -> 0x0223 }
                r9.sendMessage(r10)     // Catch:{ Exception -> 0x0223 }
                java.lang.String r9 = com.taobao.accs.internal.ACCSManagerImpl.TAG     // Catch:{ Exception -> 0x0223 }
                java.lang.String r10 = "send foreground state frame"
                java.lang.Object[] r1 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.utl.ALog.i(r9, r10, r1)     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x0154:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ConnectionServiceImpl r10 = new com.taobao.accs.internal.ConnectionServiceImpl     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r1 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.String r1 = r1.mConfigTag     // Catch:{ Exception -> 0x0223 }
                r10.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.utl.OrangeAdapter.resetChannelModeEnable()     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x0169:
                java.lang.Class<com.taobao.accs.IGlobalClientInfoService> r10 = com.taobao.accs.IGlobalClientInfoService.class
                java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0223 }
                r2[r0] = r9     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r10 = com.taobao.aipc.AIPC.getInstance(r10, r2)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IGlobalClientInfoService r10 = (com.taobao.accs.IGlobalClientInfoService) r10     // Catch:{ Exception -> 0x0223 }
                if (r10 == 0) goto L_0x01ec
                com.taobao.accs.IAgooAppReceiver r2 = com.taobao.accs.client.GlobalClientInfo.mAgooAppReceiver     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x0180
                com.taobao.accs.IAgooAppReceiver r2 = com.taobao.accs.client.GlobalClientInfo.mAgooAppReceiver     // Catch:{ Exception -> 0x0223 }
                r10.setRemoteAgooAppReceiver(r2)     // Catch:{ Exception -> 0x0223 }
            L_0x0180:
                com.taobao.accs.client.GlobalClientInfo r2 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Exception -> 0x0223 }
                java.util.Map r2 = r2.getAppReceiver()     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x01b6
                com.taobao.accs.client.GlobalClientInfo r2 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Exception -> 0x0223 }
                java.util.Map r2 = r2.getAppReceiver()     // Catch:{ Exception -> 0x0223 }
                java.util.Set r2 = r2.entrySet()     // Catch:{ Exception -> 0x0223 }
                java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x0223 }
            L_0x019a:
                boolean r3 = r2.hasNext()     // Catch:{ Exception -> 0x0223 }
                if (r3 == 0) goto L_0x01b6
                java.lang.Object r3 = r2.next()     // Catch:{ Exception -> 0x0223 }
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r4 = r3.getKey()     // Catch:{ Exception -> 0x0223 }
                java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r3 = r3.getValue()     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IAppReceiver r3 = (com.taobao.accs.IAppReceiver) r3     // Catch:{ Exception -> 0x0223 }
                r10.setRemoteAppReceiver(r4, r3)     // Catch:{ Exception -> 0x0223 }
                goto L_0x019a
            L_0x01b6:
                com.taobao.accs.client.GlobalClientInfo r2 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Exception -> 0x0223 }
                java.util.Map r2 = r2.getListener()     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x01ec
                com.taobao.accs.client.GlobalClientInfo r9 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Exception -> 0x0223 }
                java.util.Map r9 = r9.getListener()     // Catch:{ Exception -> 0x0223 }
                java.util.Set r9 = r9.entrySet()     // Catch:{ Exception -> 0x0223 }
                java.util.Iterator r9 = r9.iterator()     // Catch:{ Exception -> 0x0223 }
            L_0x01d0:
                boolean r2 = r9.hasNext()     // Catch:{ Exception -> 0x0223 }
                if (r2 == 0) goto L_0x01ec
                java.lang.Object r2 = r9.next()     // Catch:{ Exception -> 0x0223 }
                java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r3 = r2.getKey()     // Catch:{ Exception -> 0x0223 }
                java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r2 = r2.getValue()     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.base.AccsDataListener r2 = (com.taobao.accs.base.AccsDataListener) r2     // Catch:{ Exception -> 0x0223 }
                r10.registerRemoteListener(r3, r2)     // Catch:{ Exception -> 0x0223 }
                goto L_0x01d0
            L_0x01ec:
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.Class<com.taobao.accs.IConnectionService> r10 = com.taobao.accs.IConnectionService.class
                java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0223 }
                r2[r0] = r1     // Catch:{ Exception -> 0x0223 }
                java.lang.Object r10 = com.taobao.aipc.AIPC.getService(r10, r2)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r10 = (com.taobao.accs.IConnectionService) r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                if (r9 == 0) goto L_0x020f
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService r9 = r9.connectionService     // Catch:{ Exception -> 0x0223 }
                r9.start()     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x020f:
                com.taobao.accs.utl.OrangeAdapter.resetChannelModeEnable()     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r9 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ConnectionServiceImpl r10 = new com.taobao.accs.internal.ConnectionServiceImpl     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.internal.ACCSManagerImpl r1 = com.taobao.accs.internal.ACCSManagerImpl.this     // Catch:{ Exception -> 0x0223 }
                java.lang.String r1 = r1.mConfigTag     // Catch:{ Exception -> 0x0223 }
                r10.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x0223 }
                com.taobao.accs.IConnectionService unused = r9.connectionService = r10     // Catch:{ Exception -> 0x0223 }
                goto L_0x022f
            L_0x0223:
                r9 = move-exception
                java.lang.String r10 = com.taobao.accs.internal.ACCSManagerImpl.TAG
                java.lang.String r1 = "on receive action error, Error:"
                java.lang.Object[] r0 = new java.lang.Object[r0]
                com.taobao.accs.utl.ALog.e(r10, r1, r9, r0)
            L_0x022f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ACCSManagerImpl.StateReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    public ACCSManagerImpl(Context context, String str) {
        GlobalClientInfo.mContext = context.getApplicationContext();
        this.mConfigTag = str;
        if (OrangeAdapter.isChannelModeEnable()) {
            AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(this.mConfigTag);
            if (configByTag == null) {
                try {
                    configByTag = new AccsClientConfig.Builder().setAppKey(ACCSManager.getDefaultAppkey(context)).setTag(str).build();
                } catch (AccsException e) {
                    ALog.e(TAG, "ACCSManagerImpl build config", e, new Object[0]);
                }
            }
            this.connectionService = (IConnectionService) AIPC.getService(IConnectionService.class, configByTag);
            if (this.connectionService == null) {
                OrangeAdapter.resetChannelModeEnable();
                this.connectionService = new ConnectionServiceImpl(str);
            } else if (Utils.isMainProcess(context)) {
                StateReceiver stateReceiver = new StateReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Constants.ACTION_DISCONNECT);
                intentFilter.addAction(ForeBackManager.ACTION_STATE_FORE);
                intentFilter.addAction(ForeBackManager.ACTION_STATE_BACK);
                intentFilter.addAction(ForeBackManager.ACTION_STATE_DEEPBACK);
                this.connectionService.setForeBackState(ForeBackManager.getManager().getState());
                LocalBroadcastManager.getInstance(context).registerReceiver(stateReceiver, intentFilter);
            }
        } else {
            this.connectionService = new ConnectionServiceImpl(str);
        }
        TAG += this.mConfigTag;
    }

    public void bindApp(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        bindApp(context, str, "accs", str2, iAppReceiver);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:44|45|51) */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        com.taobao.accs.utl.ALog.w(TAG, "no orange sdk", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x011a */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0085 A[Catch:{ Throwable -> 0x0124 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ae A[Catch:{ Throwable -> 0x0124 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bd A[Catch:{ Throwable -> 0x0124 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00f3 A[Catch:{ Throwable -> 0x0124 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0105 A[Catch:{ Throwable -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindApp(android.content.Context r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, com.taobao.accs.IAppReceiver r13) {
        /*
            r8 = this;
            if (r9 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = TAG
            java.lang.String r1 = "bindApp"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r3 = "appKey"
            r4 = 0
            r2[r4] = r3
            r3 = 1
            r2[r3] = r10
            com.taobao.accs.utl.ALog.i(r0, r1, r2)
            java.lang.String r0 = r9.getPackageName()
            com.taobao.accs.data.Message r0 = com.taobao.accs.data.Message.buildParameterError(r0, r3)
            boolean r1 = com.taobao.accs.utl.UtilityImpl.getFocusDisableStatus(r9)
            if (r1 == 0) goto L_0x002f
            java.lang.String r1 = TAG
            java.lang.String r2 = "accs disabled, try enable"
            java.lang.Object[] r5 = new java.lang.Object[r4]
            com.taobao.accs.utl.ALog.e(r1, r2, r5)
            com.taobao.accs.utl.UtilityImpl.focusEnableService(r9)
        L_0x002f:
            boolean r1 = android.text.TextUtils.isEmpty(r10)
            if (r1 == 0) goto L_0x003d
            com.taobao.accs.IConnectionService r9 = r8.connectionService
            r10 = -14
            r9.onResult(r0, r10)
            return
        L_0x003d:
            com.taobao.accs.IConnectionService r0 = r8.connectionService
            r0.setTTid(r12)
            com.taobao.accs.IConnectionService r0 = r8.connectionService
            r0.setAppkey(r10)
            com.taobao.accs.utl.UtilityImpl.saveAppKey(r9, r10)
            if (r13 == 0) goto L_0x0055
            com.taobao.accs.client.GlobalClientInfo r0 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)
            java.lang.String r1 = r8.mConfigTag
            r0.setAppReceiver(r1, r13)
        L_0x0055:
            com.taobao.accs.utl.UtilityImpl.enableService(r9)
            android.content.Intent r13 = r8.getIntent(r9, r3)
            if (r13 != 0) goto L_0x005f
            return
        L_0x005f:
            com.taobao.accs.client.GlobalClientInfo r0 = com.taobao.accs.client.GlobalClientInfo.getInstance(r9)     // Catch:{ Throwable -> 0x0124 }
            android.content.pm.PackageInfo r0 = r0.getPackageInfo()     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r0 = r0.versionName     // Catch:{ Throwable -> 0x0124 }
            boolean r1 = com.taobao.accs.utl.UtilityImpl.appVersionChanged(r9)     // Catch:{ Throwable -> 0x0124 }
            if (r1 != 0) goto L_0x0082
            java.lang.String r1 = "ACCS_SDK"
            boolean r1 = com.taobao.accs.utl.UtilityImpl.utdidChanged(r1, r9)     // Catch:{ Throwable -> 0x0124 }
            if (r1 != 0) goto L_0x0082
            java.lang.String r1 = "ACCS_SDK"
            boolean r1 = com.taobao.accs.utl.UtilityImpl.notificationStateChanged(r1, r9)     // Catch:{ Throwable -> 0x0124 }
            if (r1 == 0) goto L_0x0080
            goto L_0x0082
        L_0x0080:
            r1 = 0
            goto L_0x0083
        L_0x0082:
            r1 = 1
        L_0x0083:
            if (r1 == 0) goto L_0x0097
            java.lang.String r2 = TAG     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r5 = "bindApp"
            java.lang.Object[] r6 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r7 = "need force bind"
            r6[r4] = r7     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.utl.ALog.d(r2, r5, r6)     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r2 = "fouce_bind"
            r13.putExtra(r2, r3)     // Catch:{ Throwable -> 0x0124 }
        L_0x0097:
            java.lang.String r2 = "appKey"
            r13.putExtra(r2, r10)     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r10 = "ttid"
            r13.putExtra(r10, r12)     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r10 = "appVersion"
            r13.putExtra(r10, r0)     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r10 = "app_sercet"
            boolean r12 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Throwable -> 0x0124 }
            if (r12 == 0) goto L_0x00b4
            com.taobao.accs.IConnectionService r11 = r8.connectionService     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r11 = r11.getAppSecret()     // Catch:{ Throwable -> 0x0124 }
        L_0x00b4:
            r13.putExtra(r10, r11)     // Catch:{ Throwable -> 0x0124 }
            boolean r10 = com.taobao.accs.utl.UtilityImpl.isMainProcess(r9)     // Catch:{ Throwable -> 0x0124 }
            if (r10 == 0) goto L_0x00f3
            com.taobao.accs.IConnectionService r10 = r8.connectionService     // Catch:{ Throwable -> 0x0124 }
            r11 = 0
            java.lang.String r10 = r10.getHost(r11)     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r11 = r8.mConfigTag     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.data.Message r10 = com.taobao.accs.data.Message.buildBindApp(r10, r11, r9, r13)     // Catch:{ Throwable -> 0x0124 }
            if (r10 == 0) goto L_0x00ef
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r11 = r10.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0124 }
            if (r11 == 0) goto L_0x00ef
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r11 = r10.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r12 = r10.dataId     // Catch:{ Throwable -> 0x0124 }
            r11.setDataId(r12)     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r11 = r10.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0124 }
            r11.setMsgType(r3)     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r11 = r10.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0124 }
            java.net.URL r12 = r10.host     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r12 = r12.toString()     // Catch:{ Throwable -> 0x0124 }
            r11.setHost(r12)     // Catch:{ Throwable -> 0x0124 }
        L_0x00ef:
            r8.sendControlMessage(r9, r10, r3, r1)     // Catch:{ Throwable -> 0x0124 }
            goto L_0x00fc
        L_0x00f3:
            java.lang.String r9 = TAG     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r10 = "bindApp only allow in main process"
            java.lang.Object[] r11 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.utl.ALog.w(r9, r10, r11)     // Catch:{ Throwable -> 0x0124 }
        L_0x00fc:
            com.taobao.accs.IConnectionService r9 = r8.connectionService     // Catch:{ Throwable -> 0x0124 }
            r9.startChannelService()     // Catch:{ Throwable -> 0x0124 }
            boolean r9 = com.taobao.accs.utl.OrangeAdapter.mOrangeValid     // Catch:{ Throwable -> 0x011a }
            if (r9 == 0) goto L_0x012e
            java.lang.String[] r9 = new java.lang.String[r3]     // Catch:{ Throwable -> 0x011a }
            java.lang.String r10 = "accs"
            r9[r4] = r10     // Catch:{ Throwable -> 0x011a }
            com.taobao.accs.utl.OrangeAdapter$OrangeListener r10 = new com.taobao.accs.utl.OrangeAdapter$OrangeListener     // Catch:{ Throwable -> 0x011a }
            r10.<init>()     // Catch:{ Throwable -> 0x011a }
            com.taobao.accs.utl.OrangeAdapter.registerListener(r9, r10)     // Catch:{ Throwable -> 0x011a }
            com.taobao.accs.utl.OrangeAdapter.checkAccsEnabled()     // Catch:{ Throwable -> 0x011a }
            com.taobao.accs.utl.OrangeAdapter.getConfigForAccs()     // Catch:{ Throwable -> 0x011a }
            goto L_0x012e
        L_0x011a:
            java.lang.String r9 = TAG     // Catch:{ Throwable -> 0x0124 }
            java.lang.String r10 = "no orange sdk"
            java.lang.Object[] r11 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0124 }
            com.taobao.accs.utl.ALog.w(r9, r10, r11)     // Catch:{ Throwable -> 0x0124 }
            goto L_0x012e
        L_0x0124:
            r9 = move-exception
            java.lang.String r10 = TAG
            java.lang.String r11 = "bindApp exception"
            java.lang.Object[] r12 = new java.lang.Object[r4]
            com.taobao.accs.utl.ALog.e(r10, r11, r9, r12)
        L_0x012e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ACCSManagerImpl.bindApp(android.content.Context, java.lang.String, java.lang.String, java.lang.String, com.taobao.accs.IAppReceiver):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x009d, code lost:
        r8 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00c2, code lost:
        r8 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00c3, code lost:
        if (r8 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00c5, code lost:
        com.taobao.accs.utl.ALog.i(TAG, "sendControlMessage", "command", java.lang.Integer.valueOf(r10));
        r7.connectionService.send(r9, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendControlMessage(android.content.Context r8, com.taobao.accs.data.Message r9, int r10, boolean r11) {
        /*
            r7 = this;
            com.taobao.accs.IConnectionService r0 = r7.connectionService
            r0.start()
            r0 = 0
            if (r9 != 0) goto L_0x0021
            java.lang.String r9 = TAG
            java.lang.String r11 = "message is null"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r9, r11, r0)
            java.lang.String r8 = r8.getPackageName()
            com.taobao.accs.data.Message r8 = com.taobao.accs.data.Message.buildParameterError(r8, r10)
            com.taobao.accs.IConnectionService r9 = r7.connectionService
            r10 = -2
            r9.onResult(r8, r10)
            goto L_0x00dd
        L_0x0021:
            r8 = 200(0xc8, float:2.8E-43)
            r1 = 2
            r2 = 1
            switch(r10) {
                case 1: goto L_0x009f;
                case 2: goto L_0x0070;
                case 3: goto L_0x002a;
                default: goto L_0x0028;
            }
        L_0x0028:
            goto L_0x00c2
        L_0x002a:
            com.taobao.accs.IConnectionService r3 = r7.connectionService
            java.lang.String r4 = r9.getPackageName()
            java.lang.String r5 = r9.userinfo
            boolean r3 = r3.isUserBinded(r4, r5)
            if (r3 == 0) goto L_0x00c2
            if (r11 != 0) goto L_0x00c2
            java.lang.String r3 = TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = r9.getPackageName()
            r4.append(r5)
            java.lang.String r5 = "/"
            r4.append(r5)
            java.lang.String r5 = r9.userinfo
            r4.append(r5)
            java.lang.String r5 = " isUserBinded"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.Object[] r5 = new java.lang.Object[r1]
            java.lang.String r6 = "isForceBind"
            r5[r0] = r6
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)
            r5[r2] = r11
            com.taobao.accs.utl.ALog.i(r3, r4, r5)
            com.taobao.accs.IConnectionService r11 = r7.connectionService
            r11.onResult(r9, r8)
            goto L_0x009d
        L_0x0070:
            com.taobao.accs.IConnectionService r11 = r7.connectionService
            java.lang.String r3 = r9.getPackageName()
            boolean r11 = r11.isAppUnbinded(r3)
            if (r11 == 0) goto L_0x00c2
            java.lang.String r11 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r9.getPackageName()
            r3.append(r4)
            java.lang.String r4 = " isAppUnbinded"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.Object[] r4 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.i(r11, r3, r4)
            com.taobao.accs.IConnectionService r11 = r7.connectionService
            r11.onResult(r9, r8)
        L_0x009d:
            r8 = 0
            goto L_0x00c3
        L_0x009f:
            java.lang.String r3 = r9.getPackageName()
            com.taobao.accs.IConnectionService r4 = r7.connectionService
            boolean r4 = r4.isAppBinded(r3)
            if (r4 == 0) goto L_0x00c2
            if (r11 != 0) goto L_0x00c2
            java.lang.String r11 = TAG
            java.lang.String r4 = "isAppBinded"
            java.lang.Object[] r5 = new java.lang.Object[r1]
            java.lang.String r6 = "package"
            r5[r0] = r6
            r5[r2] = r3
            com.taobao.accs.utl.ALog.i(r11, r4, r5)
            com.taobao.accs.IConnectionService r11 = r7.connectionService
            r11.onResult(r9, r8)
            goto L_0x009d
        L_0x00c2:
            r8 = 1
        L_0x00c3:
            if (r8 == 0) goto L_0x00dd
            java.lang.String r8 = TAG
            java.lang.String r11 = "sendControlMessage"
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r3 = "command"
            r1[r0] = r3
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r1[r2] = r10
            com.taobao.accs.utl.ALog.i(r8, r11, r1)
            com.taobao.accs.IConnectionService r8 = r7.connectionService
            r8.send(r9, r2)
        L_0x00dd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ACCSManagerImpl.sendControlMessage(android.content.Context, com.taobao.accs.data.Message, int, boolean):void");
    }

    public void unbindApp(Context context) {
        String str = TAG;
        ALog.e(str, "unbindApp" + UtilityImpl.getStackMsg(new Exception()), new Object[0]);
        if (!UtilityImpl.getFocusDisableStatus(context)) {
            Intent intent = getIntent(context, 2);
            if (intent == null) {
                sendAppNotBind(context, 2, (String) null, (String) null);
            } else if (UtilityImpl.isMainProcess(context)) {
                sendControlMessage(context, Message.buildUnbindApp(this.connectionService.getHost((String) null), intent), 2, false);
            }
        }
    }

    public void bindUser(Context context, String str) {
        bindUser(context, str, false);
    }

    public void bindUser(Context context, String str, boolean z) {
        try {
            ALog.i(TAG, "bindUser", "userId", str);
            if (UtilityImpl.getFocusDisableStatus(context)) {
                ALog.e(TAG, "accs disabled", new Object[0]);
                return;
            }
            Intent intent = getIntent(context, 3);
            if (intent == null) {
                ALog.e(TAG, "intent null", new Object[0]);
                sendAppNotBind(context, 3, (String) null, (String) null);
                return;
            }
            String appkey = this.connectionService.getAppkey();
            if (TextUtils.isEmpty(appkey)) {
                ALog.e(TAG, "appKey null", new Object[0]);
                return;
            }
            if (UtilityImpl.appVersionChanged(context) || z) {
                ALog.i(TAG, "force bind User", new Object[0]);
                intent.putExtra(com.taobao.accs.common.Constants.KEY_FOUCE_BIND, true);
                z = true;
            }
            intent.putExtra("appKey", appkey);
            intent.putExtra("userInfo", str);
            if (UtilityImpl.isMainProcess(context)) {
                Message buildBindUser = Message.buildBindUser(this.connectionService.getHost((String) null), this.mConfigTag, intent);
                if (!(buildBindUser == null || buildBindUser.getNetPermanceMonitor() == null)) {
                    buildBindUser.getNetPermanceMonitor().setDataId(buildBindUser.dataId);
                    buildBindUser.getNetPermanceMonitor().setMsgType(2);
                    buildBindUser.getNetPermanceMonitor().setHost(buildBindUser.host.toString());
                }
                sendControlMessage(context, buildBindUser, 3, z);
            }
            this.connectionService.startChannelService();
        } catch (Throwable th) {
            ALog.e(TAG, "bindUser", th, new Object[0]);
        }
    }

    public void unbindUser(Context context) {
        if (!UtilityImpl.getFocusDisableStatus(context) && !UtilityImpl.getFocusDisableStatus(context)) {
            Intent intent = getIntent(context, 4);
            if (intent == null) {
                sendAppNotBind(context, 4, (String) null, (String) null);
                return;
            }
            String appkey = this.connectionService.getAppkey();
            if (!TextUtils.isEmpty(appkey)) {
                intent.putExtra("appKey", appkey);
                if (UtilityImpl.isMainProcess(context)) {
                    sendControlMessage(context, Message.buildUnbindUser(this.connectionService.getHost((String) null), this.mConfigTag, intent), 4, false);
                }
            }
        }
    }

    public void bindService(Context context, String str) {
        if (!UtilityImpl.getFocusDisableStatus(context) && !UtilityImpl.getFocusDisableStatus(context)) {
            Intent intent = getIntent(context, 5);
            if (intent == null) {
                sendAppNotBind(context, 5, str, (String) null);
                return;
            }
            String appkey = this.connectionService.getAppkey();
            if (!TextUtils.isEmpty(appkey)) {
                intent.putExtra("appKey", appkey);
                intent.putExtra("serviceId", str);
                if (UtilityImpl.isMainProcess(context)) {
                    Message buildBindService = Message.buildBindService(this.connectionService.getHost((String) null), this.mConfigTag, intent);
                    if (!(buildBindService == null || buildBindService.getNetPermanceMonitor() == null)) {
                        buildBindService.getNetPermanceMonitor().setDataId(buildBindService.dataId);
                        buildBindService.getNetPermanceMonitor().setMsgType(3);
                        buildBindService.getNetPermanceMonitor().setHost(buildBindService.host.toString());
                    }
                    sendControlMessage(context, buildBindService, 5, false);
                }
                this.connectionService.startChannelService();
            }
        }
    }

    public void unbindService(Context context, String str) {
        if (!UtilityImpl.getFocusDisableStatus(context)) {
            Intent intent = getIntent(context, 6);
            if (intent == null) {
                sendAppNotBind(context, 6, str, (String) null);
                return;
            }
            String appkey = this.connectionService.getAppkey();
            if (!TextUtils.isEmpty(appkey)) {
                intent.putExtra("appKey", appkey);
                intent.putExtra("serviceId", str);
                if (UtilityImpl.isMainProcess(context)) {
                    sendControlMessage(context, Message.buildUnbindService(this.connectionService.getHost((String) null), this.mConfigTag, intent), 6, false);
                }
            }
        }
    }

    public String sendData(Context context, String str, String str2, byte[] bArr, String str3) {
        return sendData(context, str, str2, bArr, str3, (String) null);
    }

    public String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return sendData(context, str, str2, bArr, str3, str4, (URL) null);
    }

    public String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        Context context2 = context;
        return sendData(context, new ACCSManager.AccsRequest(str, str2, bArr, str3, str4, url, (String) null));
    }

    public String sendData(Context context, ACCSManager.AccsRequest accsRequest) {
        try {
            boolean focusDisableStatus = UtilityImpl.getFocusDisableStatus(context);
            if (!UtilityImpl.isMainProcess(context)) {
                ALog.e(TAG, "sendData not in mainprocess", new Object[0]);
                return null;
            }
            if (!focusDisableStatus) {
                if (accsRequest != null) {
                    if (TextUtils.isEmpty(accsRequest.dataId)) {
                        synchronized (ACCSManagerImpl.class) {
                            this.baseDataId++;
                            accsRequest.dataId = this.baseDataId + "";
                        }
                    }
                    if (TextUtils.isEmpty(this.connectionService.getAppkey())) {
                        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "data appkey null");
                        ALog.e(TAG, "sendData appkey null", com.taobao.accs.common.Constants.KEY_DATA_ID, accsRequest.dataId);
                        return null;
                    }
                    this.connectionService.start();
                    Message buildSendData = Message.buildSendData(this.connectionService.getHost((String) null), this.mConfigTag, this.connectionService.getStoreId(), context, context.getPackageName(), accsRequest);
                    if (!(buildSendData == null || buildSendData.getNetPermanceMonitor() == null)) {
                        buildSendData.getNetPermanceMonitor().onSend();
                    }
                    this.connectionService.send(buildSendData, true);
                    return accsRequest.dataId;
                }
            }
            if (focusDisableStatus) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "accs disable");
            } else {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "", "1", "data null");
            }
            ALog.e(TAG, "sendData dataInfo null or disable:" + focusDisableStatus, new Object[0]);
            return null;
        } catch (Throwable th) {
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "data " + th.toString());
            ALog.e(TAG, "sendData", th, "dataid", accsRequest.dataId);
        }
    }

    public String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return sendRequest(context, str, str2, bArr, str3, str4, (URL) null);
    }

    public String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        Context context2 = context;
        return sendRequest(context, new ACCSManager.AccsRequest(str, str2, bArr, str3, str4, url, (String) null));
    }

    public String sendRequest(Context context, ACCSManager.AccsRequest accsRequest, String str, boolean z) {
        if (accsRequest == null) {
            try {
                ALog.e(TAG, "sendRequest request null", new Object[0]);
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, (String) null, "1", "request null");
                return null;
            } catch (Throwable th) {
                if (accsRequest != null) {
                    AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "request " + th.toString());
                    ALog.e(TAG, "sendRequest", th, com.taobao.accs.common.Constants.KEY_DATA_ID, accsRequest.dataId);
                }
            }
        } else if (!UtilityImpl.isMainProcess(context)) {
            ALog.e(TAG, "sendRequest not in mainprocess", new Object[0]);
            return null;
        } else if (UtilityImpl.getFocusDisableStatus(context)) {
            ALog.e(TAG, "sendRequest disable", new Object[0]);
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "accs disable");
            return null;
        } else {
            if (TextUtils.isEmpty(accsRequest.dataId)) {
                synchronized (ACCSManagerImpl.class) {
                    this.baseDataId++;
                    accsRequest.dataId = this.baseDataId + "";
                }
            }
            if (TextUtils.isEmpty(this.connectionService.getAppkey())) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "request appkey null");
                ALog.e(TAG, "sendRequest appkey null", com.taobao.accs.common.Constants.KEY_DATA_ID, accsRequest.dataId);
                return null;
            }
            this.connectionService.start();
            if (str == null) {
                str = context.getPackageName();
            }
            Message buildRequest = Message.buildRequest(context, this.connectionService.getHost((String) null), this.mConfigTag, "", str, com.taobao.accs.common.Constants.TARGET_SERVICE_PRE, accsRequest, z);
            if (!(buildRequest == null || buildRequest.getNetPermanceMonitor() == null)) {
                buildRequest.getNetPermanceMonitor().onSend();
            }
            this.connectionService.send(buildRequest, true);
            return accsRequest.dataId;
        }
    }

    public String sendRequest(Context context, ACCSManager.AccsRequest accsRequest) {
        return sendRequest(context, accsRequest, (String) null, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00f0 A[Catch:{ Throwable -> 0x022f }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0153 A[Catch:{ Throwable -> 0x022f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String sendPushResponse(android.content.Context r18, com.taobao.accs.ACCSManager.AccsRequest r19, com.taobao.accs.base.TaoBaseService.ExtraInfo r20) {
        /*
            r17 = this;
            r1 = r17
            r0 = r18
            r2 = r19
            r4 = 5
            r5 = 4
            r6 = 3
            r7 = 6
            r8 = 2
            r9 = 0
            r10 = 1
            r11 = 0
            if (r0 == 0) goto L_0x0203
            if (r2 != 0) goto L_0x0014
            goto L_0x0203
        L_0x0014:
            java.lang.String r12 = "accs"
            java.lang.String r13 = "send_fail"
            java.lang.String r14 = "push response total"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmSuccess(r12, r13, r14)     // Catch:{ Throwable -> 0x022f }
            boolean r12 = com.taobao.accs.utl.UtilityImpl.getFocusDisableStatus(r18)     // Catch:{ Throwable -> 0x022f }
            if (r12 == 0) goto L_0x0031
            java.lang.String r0 = "accs"
            java.lang.String r3 = "send_fail"
            java.lang.String r4 = r2.serviceId     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "1"
            java.lang.String r6 = "sendPushResponse accs disable"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r0, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x022f }
            return r9
        L_0x0031:
            com.taobao.accs.IConnectionService r12 = r1.connectionService     // Catch:{ Throwable -> 0x022f }
            java.lang.String r12 = r12.getAppkey()     // Catch:{ Throwable -> 0x022f }
            boolean r13 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Throwable -> 0x022f }
            if (r13 == 0) goto L_0x0065
            java.lang.String r0 = "accs"
            java.lang.String r3 = "send_fail"
            java.lang.String r4 = r2.serviceId     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "1"
            java.lang.String r6 = "sendPushResponse appkey null"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r0, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x022f }
            r3.<init>()     // Catch:{ Throwable -> 0x022f }
            java.lang.String r4 = "sendPushResponse appkey null dataid:"
            r3.append(r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r4 = r2.dataId     // Catch:{ Throwable -> 0x022f }
            r3.append(r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x022f }
            java.lang.Object[] r4 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.e(r0, r3, r4)     // Catch:{ Throwable -> 0x022f }
            return r9
        L_0x0065:
            java.lang.String r13 = r2.dataId     // Catch:{ Throwable -> 0x022f }
            boolean r13 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Throwable -> 0x022f }
            if (r13 == 0) goto L_0x008f
            java.lang.Class<com.taobao.accs.internal.ACCSManagerImpl> r13 = com.taobao.accs.internal.ACCSManagerImpl.class
            monitor-enter(r13)     // Catch:{ Throwable -> 0x022f }
            int r14 = r1.baseDataId     // Catch:{ all -> 0x008c }
            int r14 = r14 + r10
            r1.baseDataId = r14     // Catch:{ all -> 0x008c }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x008c }
            r14.<init>()     // Catch:{ all -> 0x008c }
            int r15 = r1.baseDataId     // Catch:{ all -> 0x008c }
            r14.append(r15)     // Catch:{ all -> 0x008c }
            java.lang.String r15 = ""
            r14.append(r15)     // Catch:{ all -> 0x008c }
            java.lang.String r14 = r14.toString()     // Catch:{ all -> 0x008c }
            r2.dataId = r14     // Catch:{ all -> 0x008c }
            monitor-exit(r13)     // Catch:{ all -> 0x008c }
            goto L_0x008f
        L_0x008c:
            r0 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x008c }
            throw r0     // Catch:{ Throwable -> 0x022f }
        L_0x008f:
            if (r20 != 0) goto L_0x0097
            com.taobao.accs.base.TaoBaseService$ExtraInfo r3 = new com.taobao.accs.base.TaoBaseService$ExtraInfo     // Catch:{ Throwable -> 0x022f }
            r3.<init>()     // Catch:{ Throwable -> 0x022f }
            goto L_0x0099
        L_0x0097:
            r3 = r20
        L_0x0099:
            r2.host = r9     // Catch:{ Throwable -> 0x022f }
            java.lang.String r13 = r18.getPackageName()     // Catch:{ Throwable -> 0x022f }
            r3.fromPackage = r13     // Catch:{ Throwable -> 0x022f }
            int r13 = r3.connType     // Catch:{ Throwable -> 0x022f }
            if (r13 == 0) goto L_0x00ac
            java.lang.String r13 = r3.fromHost     // Catch:{ Throwable -> 0x022f }
            if (r13 != 0) goto L_0x00aa
            goto L_0x00ac
        L_0x00aa:
            r9 = 1
            goto L_0x00c0
        L_0x00ac:
            r3.connType = r11     // Catch:{ Throwable -> 0x022f }
            java.lang.String r13 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r14 = "pushresponse use channel"
            java.lang.Object[] r15 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r16 = "host"
            r15[r11] = r16     // Catch:{ Throwable -> 0x022f }
            java.lang.String r9 = r3.fromHost     // Catch:{ Throwable -> 0x022f }
            r15[r10] = r9     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.w(r13, r14, r15)     // Catch:{ Throwable -> 0x022f }
            r9 = 0
        L_0x00c0:
            java.lang.String r13 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r14 = "sendPushResponse"
            r15 = 8
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r16 = "sendbyInapp"
            r15[r11] = r16     // Catch:{ Throwable -> 0x022f }
            java.lang.Boolean r16 = java.lang.Boolean.valueOf(r9)     // Catch:{ Throwable -> 0x022f }
            r15[r10] = r16     // Catch:{ Throwable -> 0x022f }
            java.lang.String r16 = "host"
            r15[r8] = r16     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = r3.fromHost     // Catch:{ Throwable -> 0x022f }
            r15[r6] = r10     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = "pkg"
            r15[r5] = r10     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            r15[r4] = r10     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = "dataId"
            r15[r7] = r10     // Catch:{ Throwable -> 0x022f }
            r10 = 7
            java.lang.String r4 = r2.dataId     // Catch:{ Throwable -> 0x022f }
            r15[r10] = r4     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.i(r13, r14, r15)     // Catch:{ Throwable -> 0x022f }
            if (r9 == 0) goto L_0x0153
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "sendPushResponse inapp by"
            java.lang.Object[] r6 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r7 = "app"
            r6[r11] = r7     // Catch:{ Throwable -> 0x022f }
            java.lang.String r7 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            r8 = 1
            r6[r8] = r7     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.i(r4, r5, r6)     // Catch:{ Throwable -> 0x022f }
            java.net.URL r4 = new java.net.URL     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = r3.fromHost     // Catch:{ Throwable -> 0x022f }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x022f }
            r2.host = r4     // Catch:{ Throwable -> 0x022f }
            java.lang.String r4 = r18.getPackageName()     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x022f }
            if (r4 == 0) goto L_0x0126
            boolean r4 = com.taobao.accs.utl.UtilityImpl.isMainProcess(r18)     // Catch:{ Throwable -> 0x022f }
            if (r4 == 0) goto L_0x0126
            java.lang.String r3 = r18.getPackageName()     // Catch:{ Throwable -> 0x022f }
            r1.sendRequest(r0, r2, r3, r11)     // Catch:{ Throwable -> 0x022f }
            goto L_0x026a
        L_0x0126:
            android.content.Intent r4 = new android.content.Intent     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "com.taobao.accs.intent.action.SEND"
            r4.<init>(r5)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "com.taobao.accs.data.MsgDistributeService"
            r4.setClassName(r3, r5)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "packageName"
            java.lang.String r5 = r18.getPackageName()     // Catch:{ Throwable -> 0x022f }
            r4.putExtra(r3, r5)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "reqdata"
            r4.putExtra(r3, r2)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "appKey"
            r4.putExtra(r3, r12)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "configTag"
            java.lang.String r5 = r1.mConfigTag     // Catch:{ Throwable -> 0x022f }
            r4.putExtra(r3, r5)     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.dispatch.IntentDispatch.dispatchIntent(r0, r4)     // Catch:{ Throwable -> 0x022f }
            goto L_0x026a
        L_0x0153:
            r4 = 100
            android.content.Intent r9 = r1.getIntent(r0, r4)     // Catch:{ Throwable -> 0x022f }
            if (r9 != 0) goto L_0x018e
            java.lang.String r9 = "accs"
            java.lang.String r10 = "send_fail"
            java.lang.String r12 = r2.serviceId     // Catch:{ Throwable -> 0x022f }
            java.lang.String r13 = "1"
            java.lang.String r14 = "push response intent null"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r9, r10, r12, r13, r14)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r9 = r2.serviceId     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = r2.dataId     // Catch:{ Throwable -> 0x022f }
            r1.sendAppNotBind(r0, r4, r9, r10)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r9 = "sendPushResponse input null"
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = "context"
            r7[r11] = r10     // Catch:{ Throwable -> 0x022f }
            r10 = 1
            r7[r10] = r0     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = "response"
            r7[r8] = r0     // Catch:{ Throwable -> 0x022f }
            r7[r6] = r2     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = "extraInfo"
            r7[r5] = r0     // Catch:{ Throwable -> 0x022f }
            r0 = 5
            r7[r0] = r3     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.e(r4, r9, r7)     // Catch:{ Throwable -> 0x022f }
            r3 = 0
            return r3
        L_0x018e:
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r5 = "sendPushResponse channel by"
            java.lang.Object[] r6 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r7 = "app"
            r6[r11] = r7     // Catch:{ Throwable -> 0x022f }
            java.lang.String r7 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            r8 = 1
            r6[r8] = r7     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.i(r4, r5, r6)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = r3.fromPackage     // Catch:{ Throwable -> 0x022f }
            java.lang.String r4 = "com.taobao.accs.ChannelService"
            r9.setClassName(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "send_type"
            com.taobao.accs.data.Message$ReqType r4 = com.taobao.accs.data.Message.ReqType.REQ     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "appKey"
            r9.putExtra(r3, r12)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "userInfo"
            java.lang.String r4 = r2.userId     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "serviceId"
            java.lang.String r4 = r2.serviceId     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "data"
            byte[] r4 = r2.data     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "dataId"
            java.lang.String r4 = r2.dataId     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = "configTag"
            java.lang.String r4 = r1.mConfigTag     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r3 = r2.businessId     // Catch:{ Throwable -> 0x022f }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x022f }
            if (r3 != 0) goto L_0x01e5
            java.lang.String r3 = "businessId"
            java.lang.String r4 = r2.businessId     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
        L_0x01e5:
            java.lang.String r3 = r2.tag     // Catch:{ Throwable -> 0x022f }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x022f }
            if (r3 != 0) goto L_0x01f4
            java.lang.String r3 = "extTag"
            java.lang.String r4 = r2.tag     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
        L_0x01f4:
            java.lang.String r3 = r2.target     // Catch:{ Throwable -> 0x022f }
            if (r3 == 0) goto L_0x01ff
            java.lang.String r3 = "target"
            java.lang.String r4 = r2.target     // Catch:{ Throwable -> 0x022f }
            r9.putExtra(r3, r4)     // Catch:{ Throwable -> 0x022f }
        L_0x01ff:
            com.taobao.accs.dispatch.IntentDispatch.dispatchIntent(r0, r9)     // Catch:{ Throwable -> 0x022f }
            goto L_0x026a
        L_0x0203:
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x022f }
            java.lang.String r9 = "sendPushResponse input null"
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x022f }
            java.lang.String r10 = "context"
            r7[r11] = r10     // Catch:{ Throwable -> 0x022f }
            r10 = 1
            r7[r10] = r0     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = "response"
            r7[r8] = r0     // Catch:{ Throwable -> 0x022f }
            r7[r6] = r2     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = "extraInfo"
            r7[r5] = r0     // Catch:{ Throwable -> 0x022f }
            r0 = 5
            r7[r0] = r20     // Catch:{ Throwable -> 0x022f }
            com.taobao.accs.utl.ALog.e(r4, r9, r7)     // Catch:{ Throwable -> 0x022f }
            java.lang.String r0 = "accs"
            java.lang.String r3 = "send_fail"
            java.lang.String r4 = ""
            java.lang.String r5 = "1"
            java.lang.String r6 = "sendPushResponse null"
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r0, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x022f }
            r3 = 0
            return r3
        L_0x022f:
            r0 = move-exception
            java.lang.String r3 = "accs"
            java.lang.String r4 = "send_fail"
            java.lang.String r5 = r2.serviceId
            java.lang.String r6 = "1"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "push response "
            r7.append(r8)
            java.lang.String r8 = r0.toString()
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r3, r4, r5, r6, r7)
            java.lang.String r3 = TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "sendPushResponse dataid:"
            r4.append(r5)
            java.lang.String r2 = r2.dataId
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            java.lang.Object[] r4 = new java.lang.Object[r11]
            com.taobao.accs.utl.ALog.e(r3, r2, r0, r4)
        L_0x026a:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ACCSManagerImpl.sendPushResponse(android.content.Context, com.taobao.accs.ACCSManager$AccsRequest, com.taobao.accs.base.TaoBaseService$ExtraInfo):java.lang.String");
    }

    public boolean isNetworkReachable(Context context) {
        return UtilityImpl.isNetworkConnected(context);
    }

    private Intent getIntent(Context context, int i) {
        if (i == 1 || !UtilityImpl.getFocusDisableStatus(context)) {
            Intent intent = new Intent();
            intent.setAction(com.taobao.accs.common.Constants.ACTION_COMMAND);
            intent.setClassName(context.getPackageName(), AdapterUtilityImpl.channelService);
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("command", i);
            intent.putExtra("appKey", this.connectionService.getAppkey());
            intent.putExtra(com.taobao.accs.common.Constants.KEY_CONFIG_TAG, this.mConfigTag);
            return intent;
        }
        ALog.e(TAG, "getIntent null command:" + i + " accs enabled:" + UtilityImpl.getFocusDisableStatus(context), new Object[0]);
        return null;
    }

    public void forceDisableService(Context context) {
        UtilityImpl.focusDisableService(context);
    }

    public void forceEnableService(Context context) {
        UtilityImpl.focusEnableService(context);
    }

    @Deprecated
    public void setMode(Context context, int i) {
        ACCSClient.setEnvironment(context, i);
    }

    private void sendAppNotBind(Context context, int i, String str, String str2) {
        Intent intent = new Intent(com.taobao.accs.common.Constants.ACTION_RECEIVE);
        intent.setPackage(context.getPackageName());
        intent.putExtra("command", i);
        intent.putExtra("serviceId", str);
        intent.putExtra(com.taobao.accs.common.Constants.KEY_DATA_ID, str2);
        intent.putExtra("appKey", this.connectionService.getAppkey());
        intent.putExtra(com.taobao.accs.common.Constants.KEY_CONFIG_TAG, this.mConfigTag);
        intent.putExtra("errorCode", i == 2 ? 200 : 300);
        MsgDistribute.distribMessage(context, intent);
    }

    public void setProxy(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(com.taobao.accs.common.Constants.SP_FILE_NAME, 0).edit();
        if (!TextUtils.isEmpty(str)) {
            edit.putString(com.taobao.accs.common.Constants.KEY_PROXY_HOST, str);
        }
        edit.putInt(com.taobao.accs.common.Constants.KEY_PROXY_PORT, i);
        edit.apply();
    }

    public void startInAppConnection(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        startInAppConnection(context, str, (String) null, str2, iAppReceiver);
    }

    public void startInAppConnection(Context context, String str, String str2, String str3, IAppReceiver iAppReceiver) {
        GlobalClientInfo.getInstance(context).setAppReceiver(this.mConfigTag, iAppReceiver);
        if (!UtilityImpl.isMainProcess(context)) {
            ALog.d(TAG, "inapp only init in main process!", new Object[0]);
            return;
        }
        String str4 = TAG;
        ALog.d(str4, "startInAppConnection APPKEY:" + str, new Object[0]);
        if (!TextUtils.isEmpty(str)) {
            if (!TextUtils.equals(this.connectionService.getAppkey(), str)) {
                this.connectionService.setTTid(str3);
                this.connectionService.setAppkey(str);
                UtilityImpl.saveAppKey(context, str);
            }
            this.connectionService.start();
        }
    }

    public void setLoginInfo(Context context, ILoginInfo iLoginInfo) {
        GlobalClientInfo.getInstance(context).setLoginInfoImpl(this.mConfigTag, iLoginInfo);
    }

    public void clearLoginInfo(Context context) {
        GlobalClientInfo.getInstance(context).clearLoginInfoImpl();
    }

    public boolean cancel(Context context, String str) {
        return this.connectionService.cancel(str);
    }

    public Map<String, Boolean> getChannelState() throws Exception {
        String host = this.connectionService.getHost((String) null);
        HashMap hashMap = new HashMap();
        hashMap.put(host, false);
        if (SessionCenter.getInstance(this.connectionService.getAppkey()).getThrowsException(host, 60000) != null) {
            hashMap.put(host, true);
        }
        String str = TAG;
        ALog.d(str, "getChannelState " + hashMap.toString(), new Object[0]);
        return hashMap;
    }

    public Map<String, Boolean> forceReConnectChannel() throws Exception {
        SessionCenter.getInstance(this.connectionService.getAppkey()).forceRecreateAccsSession();
        return getChannelState();
    }

    public boolean isChannelError(int i) {
        return ErrorCode.isChannelError(i);
    }

    public void registerSerivce(Context context, String str, String str2) {
        GlobalClientInfo.getInstance(context).registerService(str, str2);
    }

    public void unRegisterSerivce(Context context, String str) {
        GlobalClientInfo.getInstance(context).unRegisterService(str);
    }

    public void registerDataListener(Context context, String str, AccsAbstractDataListener accsAbstractDataListener) {
        GlobalClientInfo.getInstance(context).registerListener(str, accsAbstractDataListener);
    }

    public void unRegisterDataListener(Context context, String str) {
        GlobalClientInfo.getInstance(context).unregisterListener(str);
    }

    public void sendBusinessAck(String str, String str2, String str3, short s, String str4, Map<Integer, String> map) {
        this.connectionService.send(Message.buildPushAck(this.connectionService.getHost((String) null), this.mConfigTag, str, str2, str3, true, s, str4, map), true);
    }

    public void updateConfig(AccsClientConfig accsClientConfig) {
        this.connectionService.updateConfig(accsClientConfig);
    }
}
