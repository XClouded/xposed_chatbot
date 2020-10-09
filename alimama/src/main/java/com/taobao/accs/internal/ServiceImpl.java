package com.taobao.accs.internal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.data.MsgDistribute;
import com.taobao.accs.eudemon.EudemonManager;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.net.SpdyConnection;
import com.taobao.accs.ut.statistics.MonitorStatistic;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.LoadSoFailUtil;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.weex.BuildConfig;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.android.agoo.common.Config;
import org.android.agoo.service.IMessageService;

public class ServiceImpl extends ElectionServiceImpl {
    private static final String TAG = "ServiceImpl";
    private static EudemonManager eudemonManager;
    private Service mBaseService = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private String mLastNetWorkType = "unknown";
    private final IMessageService.Stub messageServiceBinder = new IMessageService.Stub() {
        public boolean ping() throws RemoteException {
            return true;
        }

        public void probe() throws RemoteException {
            ALog.d(ServiceImpl.TAG, "ReceiverImpl probeTaoBao begin......messageServiceBinder [probe]", new Object[0]);
            ThreadPoolExecutorFactory.execute(new Runnable() {
                public void run() {
                    try {
                        if (ServiceImpl.this.mContext == null || !UtilityImpl.getServiceEnabled(ServiceImpl.this.mContext)) {
                            Process.killProcess(Process.myPid());
                        } else {
                            Intent intent = new Intent();
                            intent.setAction("org.agoo.android.intent.action.PING_V4");
                            intent.setClassName(ServiceImpl.this.mContext.getPackageName(), AdapterUtilityImpl.channelService);
                            ServiceImpl.this.mContext.startService(intent);
                            UTMini.getInstance().commitEvent(66001, "probeServiceEnabled", UtilityImpl.getDeviceId(ServiceImpl.this.mContext));
                            ALog.d(ServiceImpl.TAG, "ReceiverImpl probeTaoBao........mContext.startService(intent) [probe][successfully]", new Object[0]);
                        }
                        ALog.d(ServiceImpl.TAG, "ReceiverImpl probeTaoBao........messageServiceBinder [probe][end]", new Object[0]);
                    } catch (Throwable th) {
                        ALog.d(ServiceImpl.TAG, "ReceiverImpl probeTaoBao error........e=" + th, new Object[0]);
                    }
                }
            });
        }
    };
    private long startTime;

    private void initUt() {
    }

    private final void onPingIpp(Context context) {
    }

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public ServiceImpl(Service service) {
        super(service);
        this.mBaseService = service;
        this.mContext = service.getApplicationContext();
    }

    public void onCreate() {
        super.onCreate();
        init();
    }

    public int onHostStartCommand(Intent intent, int i, int i2) {
        String str;
        Bundle extras;
        int i3 = 1;
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "onHostStartCommand", "intent", intent);
        }
        try {
            if (!(!ALog.isPrintLog(ALog.Level.D) || intent == null || (extras = intent.getExtras()) == null)) {
                for (String str2 : extras.keySet()) {
                    ALog.d(TAG, "onHostStartCommand", "key", str2, " value", extras.get(str2));
                }
            }
            int soFailTimes = LoadSoFailUtil.getSoFailTimes();
            if (soFailTimes > 3) {
                try {
                    ALog.e(TAG, "onHostStartCommand load SO fail 4 times, don't auto restart", new Object[0]);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_SOFAIL, UtilityImpl.int2String(soFailTimes), 0.0d);
                    i3 = 2;
                } catch (Throwable th) {
                    th = th;
                    i3 = 2;
                    try {
                        ALog.e(TAG, "onHostStartCommand", th, new Object[0]);
                        AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
                        return i3;
                    } catch (Throwable th2) {
                        AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
                        throw th2;
                    }
                }
            }
            if (intent == null) {
                str = null;
            } else {
                str = intent.getAction();
            }
            if (TextUtils.isEmpty(str)) {
                tryConnect();
                pingOnAllConns(false, false);
                AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
                return i3;
            }
            handleAction(intent, str);
            AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
            return i3;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void init() {
        ALog.d(TAG, "init start", new Object[0]);
        GlobalClientInfo.getInstance(this.mContext);
        AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
        this.startTime = System.currentTimeMillis();
        this.mLastNetWorkType = UtilityImpl.getNetworkTypeExt(this.mContext);
        if (Config.isEnableDaemonServer(this.mContext)) {
            EudemonManager.checkAndRenewPidFile(this.mContext);
            eudemonManager = EudemonManager.getInstance(this.mContext, 600, false);
            if (eudemonManager != null) {
                eudemonManager.start();
            }
        }
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "init", "sdkVersion", Integer.valueOf(Constants.SDK_VERSION_CODE), "procStart", Integer.valueOf(AdapterGlobalClientInfo.mStartServiceTimes.intValue()));
        }
        initUt();
        onPingIpp(this.mContext);
        UTMini.getInstance().commitEvent(66001, "START", UtilityImpl.getProxy(), "PROXY");
        long serviceAliveTime = UtilityImpl.getServiceAliveTime(this.mContext);
        ALog.d(TAG, "getServiceAliveTime", "aliveTime", Long.valueOf(serviceAliveTime));
        if (serviceAliveTime > UmbrellaConstants.PERFORMANCE_DATA_ALIVE) {
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_SERVICE_ALIVE, "", (double) (serviceAliveTime / 1000));
        }
        UtilityImpl.setServiceTime(this.mContext, Constants.SP_KEY_SERVICE_START, System.currentTimeMillis());
        UTMini.getInstance().commitEvent(66001, "NOTIFY", UtilityImpl.isNotificationEnabled(this.mContext));
    }

    private void handleAction(Intent intent, String str) {
        ALog.d(TAG, "handleAction", "action", str);
        try {
            if (!TextUtils.isEmpty(str) && "org.agoo.android.intent.action.PING_V4".equals(str)) {
                String stringExtra = intent.getStringExtra("source");
                ALog.i(TAG, "org.agoo.android.intent.action.PING_V4,start channel by brothers", "serviceStart", Integer.valueOf(AdapterGlobalClientInfo.mStartServiceTimes.intValue()), "source" + stringExtra);
                AppMonitorAdapter.commitCount("accs", "startChannel", stringExtra, 0.0d);
                if (AdapterGlobalClientInfo.isFirstStartProc()) {
                    AppMonitorAdapter.commitCount("accs", "createChannel", stringExtra, 0.0d);
                }
            }
            tryConnect();
            if (!TextUtils.equals(str, "android.intent.action.PACKAGE_REMOVED")) {
                if (TextUtils.equals(str, "android.net.conn.CONNECTIVITY_CHANGE")) {
                    String networkTypeExt = UtilityImpl.getNetworkTypeExt(this.mContext);
                    boolean isNetworkConnected = UtilityImpl.isNetworkConnected(this.mContext);
                    String str2 = "network change:" + this.mLastNetWorkType + " to " + networkTypeExt;
                    ALog.i(TAG, str2, new Object[0]);
                    if (isNetworkConnected) {
                        this.mLastNetWorkType = networkTypeExt;
                        notifyNetChangeOnAllConns(str2);
                        pingOnAllConns(true, false);
                        UTMini.getInstance().commitEvent(66001, "CONNECTIVITY_CHANGE", networkTypeExt, UtilityImpl.getProxy(), "0");
                    }
                    if (networkTypeExt.equals("unknown")) {
                        notifyNetChangeOnAllConns(str2);
                        this.mLastNetWorkType = networkTypeExt;
                    }
                } else if (TextUtils.equals(str, "android.intent.action.BOOT_COMPLETED")) {
                    pingOnAllConns(true, false);
                } else if (TextUtils.equals(str, "android.intent.action.USER_PRESENT")) {
                    ALog.d(TAG, "action android.intent.action.USER_PRESENT", new Object[0]);
                    pingOnAllConns(true, false);
                } else if (str.equals(Constants.ACTION_COMMAND)) {
                    handleCommand(intent);
                } else if (str.equals(Constants.ACTION_START_FROM_AGOO)) {
                    ALog.i(TAG, "ACTION_START_FROM_AGOO", new Object[0]);
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "handleAction", th, new Object[0]);
        }
    }

    private void handleCommand(Intent intent) {
        Message message;
        BaseConnection baseConnection;
        Message message2;
        Message.ReqType reqType;
        URL url;
        Message buildRequest;
        URL url2;
        Intent intent2 = intent;
        int intExtra = intent2.getIntExtra("command", -1);
        ALog.i(TAG, "handleCommand", "command", Integer.valueOf(intExtra));
        String stringExtra = intent2.getStringExtra("packageName");
        String stringExtra2 = intent2.getStringExtra("serviceId");
        String stringExtra3 = intent2.getStringExtra("userInfo");
        String stringExtra4 = intent2.getStringExtra("appKey");
        String stringExtra5 = intent2.getStringExtra(Constants.KEY_CONFIG_TAG);
        String stringExtra6 = intent2.getStringExtra("ttid");
        intent2.getStringExtra("sid");
        intent2.getStringExtra(Constants.KEY_ANTI_BRUSH_COOKIE);
        if (intExtra == 201) {
            sendOnAllConnections(Message.BuildPing(true, 0), true);
            updateMonitorInfoOnAllConns();
        }
        if (intExtra > 0 && !TextUtils.isEmpty(stringExtra)) {
            BaseConnection connection = getConnection(this.mContext, stringExtra5, true, intExtra);
            if (connection != null) {
                connection.start();
                if (intExtra != 1) {
                    baseConnection = connection;
                    if (intExtra == 2) {
                        ALog.e(TAG, "onHostStartCommand COMMAND_UNBIND_APP", new Object[0]);
                        if (baseConnection.getClientManager().isAppUnbinded(stringExtra)) {
                            Message buildUnbindApp = Message.buildUnbindApp(baseConnection.getHost((String) null), stringExtra);
                            ALog.i(TAG, stringExtra + " isAppUnbinded", new Object[0]);
                            baseConnection.onResult(buildUnbindApp, 200);
                            return;
                        }
                    } else if (intExtra == 5) {
                        message = Message.buildBindService(baseConnection.getHost((String) null), stringExtra2);
                    } else if (intExtra == 6) {
                        message = Message.buildUnbindService(stringExtra, stringExtra2);
                    } else if (intExtra == 3) {
                        message = Message.buildBindUser(stringExtra, stringExtra3);
                        if (baseConnection.getClientManager().isUserBinded(stringExtra, stringExtra3) && !intent2.getBooleanExtra(Constants.KEY_FOUCE_BIND, false)) {
                            ALog.i(TAG, stringExtra + "/" + stringExtra3 + " isUserBinded", new Object[0]);
                            if (message != null) {
                                baseConnection.onResult(message, 200);
                                return;
                            }
                            return;
                        }
                    } else if (intExtra == 4) {
                        message = Message.buildUnbindUser(stringExtra);
                    } else {
                        if (intExtra == 100) {
                            byte[] byteArrayExtra = intent2.getByteArrayExtra("data");
                            String stringExtra7 = intent2.getStringExtra(Constants.KEY_DATA_ID);
                            String stringExtra8 = intent2.getStringExtra(Constants.KEY_TARGET);
                            String stringExtra9 = intent2.getStringExtra(Constants.KEY_BUSINESSID);
                            String stringExtra10 = intent2.getStringExtra(Constants.KEY_EXT_TAG);
                            try {
                                reqType = (Message.ReqType) intent2.getSerializableExtra(Constants.KEY_SEND_TYPE);
                            } catch (Exception unused) {
                                reqType = null;
                            }
                            if (byteArrayExtra != null) {
                                try {
                                    if (!OrangeAdapter.isChannelModeEnable()) {
                                        url2 = new URL("https://" + ((SpdyConnection) baseConnection).getChannelHost());
                                    } else {
                                        url2 = new URL(baseConnection.getHost((String) null));
                                    }
                                    url = url2;
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    url = null;
                                }
                                message2 = null;
                                ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest(stringExtra3, stringExtra2, byteArrayExtra, stringExtra7, stringExtra8, url, stringExtra9);
                                accsRequest.setTag(stringExtra10);
                                if (reqType == null) {
                                    buildRequest = Message.buildSendData(baseConnection.getHost((String) null), stringExtra5, baseConnection.mConfig.getStoreId(), this.mContext, stringExtra, accsRequest, false);
                                } else if (reqType == Message.ReqType.REQ) {
                                    buildRequest = Message.buildRequest(this.mContext, baseConnection.getHost((String) null), stringExtra5, baseConnection.mConfig.getStoreId(), stringExtra, Constants.TARGET_SERVICE_PRE, accsRequest, false);
                                }
                                message = buildRequest;
                            }
                        } else {
                            message2 = null;
                            if (intExtra == 106) {
                                intent2.setAction(Constants.ACTION_RECEIVE);
                                intent2.putExtra("command", -1);
                                MsgDistribute.distribMessage(this.mContext, intent2);
                                return;
                            }
                        }
                        message = message2;
                    }
                    message2 = null;
                    message = message2;
                } else if (!stringExtra.equals(this.mContext.getPackageName())) {
                    ALog.e(TAG, "handleCommand bindapp pkg error", new Object[0]);
                    return;
                } else {
                    BaseConnection baseConnection2 = connection;
                    message = Message.buildBindApp(this.mContext, stringExtra5, stringExtra4, intent2.getStringExtra("app_sercet"), stringExtra, stringExtra6, intent2.getStringExtra("appVersion"));
                    baseConnection2.mTtid = stringExtra6;
                    UtilityImpl.saveAppKey(this.mContext, stringExtra4);
                    if (!baseConnection2.getClientManager().isAppBinded(stringExtra) || intent2.getBooleanExtra(Constants.KEY_FOUCE_BIND, false)) {
                        baseConnection = baseConnection2;
                    } else {
                        ALog.i(TAG, stringExtra + " isAppBinded", new Object[0]);
                        baseConnection2.onResult(message, 200);
                        return;
                    }
                }
                if (message != null) {
                    ALog.d(TAG, "try send message", new Object[0]);
                    if (message.getNetPermanceMonitor() != null) {
                        message.getNetPermanceMonitor().onSend();
                    }
                    baseConnection.send(message, true);
                    return;
                }
                ALog.e(TAG, "message is null", new Object[0]);
                baseConnection.onResult(Message.buildParameterError(stringExtra, intExtra), -2);
                return;
            }
            ALog.e(TAG, "no connection", Constants.KEY_CONFIG_TAG, stringExtra5, "command", Integer.valueOf(intExtra));
        }
    }

    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        ALog.d(TAG, "accs probeTaoBao begin......action=" + action, new Object[0]);
        if (TextUtils.isEmpty(action) || !TextUtils.equals(action, "org.agoo.android.intent.action.PING_V4")) {
            return null;
        }
        UTMini.getInstance().commitEvent(66001, "probeChannelService", UtilityImpl.getDeviceId(this.mContext), intent.getStringExtra("source"));
        return this.messageServiceBinder;
    }

    public void onDestroy() {
        super.onDestroy();
        ALog.e(TAG, "Service onDestroy", new Object[0]);
        UtilityImpl.setServiceTime(this.mContext, Constants.SP_KEY_SERVICE_END, System.currentTimeMillis());
        this.mBaseService = null;
        this.mContext = null;
        shutdownAllConns();
        Process.killProcess(Process.myPid());
    }

    private void shouldStopSelf(boolean z) {
        ALog.e(TAG, "shouldStopSelf, kill:" + z, new Object[0]);
        if (this.mBaseService != null) {
            this.mBaseService.stopSelf();
        }
        if (z) {
            Process.killProcess(Process.myPid());
        }
    }

    private synchronized void tryConnect() {
        if (mConnections != null) {
            if (mConnections.size() != 0) {
                for (Map.Entry entry : mConnections.entrySet()) {
                    BaseConnection baseConnection = (BaseConnection) entry.getValue();
                    if (baseConnection == null) {
                        ALog.e(TAG, "tryConnect connection null", "appkey", baseConnection.getAppkey());
                        return;
                    }
                    ALog.i(TAG, "tryConnect", "appkey", baseConnection.getAppkey(), Constants.KEY_CONFIG_TAG, entry.getKey());
                    if (!baseConnection.isSecurityOff() || !TextUtils.isEmpty(baseConnection.mConfig.getAppSecret())) {
                        baseConnection.start();
                    } else {
                        ALog.e(TAG, "tryConnect secret is null", new Object[0]);
                    }
                }
                return;
            }
        }
        ALog.w(TAG, "tryConnect no connections", new Object[0]);
    }

    private void sendOnAllConnections(Message message, boolean z) {
        if (mConnections != null && mConnections.size() != 0) {
            for (Map.Entry value : mConnections.entrySet()) {
                ((BaseConnection) value.getValue()).send(message, z);
            }
        }
    }

    private void pingOnAllConns(boolean z, boolean z2) {
        if (mConnections != null && mConnections.size() != 0) {
            for (Map.Entry value : mConnections.entrySet()) {
                BaseConnection baseConnection = (BaseConnection) value.getValue();
                baseConnection.ping(z, z2);
                ALog.i(TAG, "ping connection", "appkey", baseConnection.getAppkey());
            }
        }
    }

    private void notifyNetChangeOnAllConns(String str) {
        if (mConnections != null && mConnections.size() != 0) {
            for (Map.Entry value : mConnections.entrySet()) {
                ((BaseConnection) value.getValue()).notifyNetWorkChange(str);
            }
        }
    }

    private void updateMonitorInfoOnAllConns() {
        if (mConnections != null && mConnections.size() != 0) {
            for (Map.Entry value : mConnections.entrySet()) {
                MonitorStatistic updateMonitorInfo = ((BaseConnection) value.getValue()).updateMonitorInfo();
                if (updateMonitorInfo != null) {
                    updateMonitorInfo.startServiceTime = this.startTime;
                    updateMonitorInfo.commitUT();
                }
            }
        }
    }

    private void shutdownAllConns() {
        if (mConnections != null && mConnections.size() != 0) {
            for (Map.Entry value : mConnections.entrySet()) {
                ((BaseConnection) value.getValue()).shutdown();
            }
        }
    }

    private String getVersion(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return BuildConfig.buildJavascriptFrameworkVersion;
            }
            String str2 = this.mContext.getPackageManager().getPackageInfo(str, 0).versionName;
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(TAG, "getVersion###版本号为 : " + str2, new Object[0]);
            }
            return str2;
        } catch (Throwable unused) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }

    public void startConnect() {
        ALog.i(TAG, "startConnect", new Object[0]);
        try {
            tryConnect();
            pingOnAllConns(false, false);
        } catch (Throwable th) {
            ALog.e(TAG, "tryConnect is error,e=" + th, new Object[0]);
        }
    }

    public void onVotedHost() {
        startConnect();
    }
}
