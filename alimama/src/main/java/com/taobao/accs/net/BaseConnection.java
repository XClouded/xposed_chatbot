package com.taobao.accs.net;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.Config;
import anet.channel.SessionCenter;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.ClientManager;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.data.MessageHandler;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.ut.statistics.MonitorStatistic;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.aipc.AIPC;
import com.uc.webview.export.cyclone.StatAction;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.android.agoo.common.AgooConstants;

public abstract class BaseConnection {
    public static final int ACCS_RECEIVE_TIMEOUT = 40000;
    protected static final int CONNECTED = 1;
    protected static final int CONNECTING = 2;
    protected static final int DISCONNECTED = 3;
    protected static final int DISCONNECTING = 4;
    protected static final int FRAME_TYPE = 200;
    public static final int INAPP = 1;
    protected static final int MAX_DATA_SIZE = 16384;
    protected static final int MAX_QUEUE_SIZE = 1000;
    protected static final int RESEND_ACK_DELAY = 5000;
    protected static final int RESEND_DELAY = 2000;
    public static final int SERVICE = 0;
    protected static int state = 1;
    protected LinkedHashMap<Integer, Message> mAckMessage = new LinkedHashMap<Integer, Message>() {
        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<Integer, Message> entry) {
            return size() > 10;
        }
    };
    public String mAppkey = "";
    protected volatile boolean mAwcnInited = false;
    public ClientManager mClientManager;
    public AccsClientConfig mConfig;
    public String mConfigTag;
    protected String mConnToken = null;
    protected int mConnectionType;
    protected Context mContext;
    private long mExpectSendTime = 0;
    protected MessageHandler mMessageHandler;
    private ScheduledFuture<?> mPingTimeoutFuture;
    private Runnable mPingTimeoutRunnable;
    protected int mTimeoutMsgNum = 0;
    public String mTtid;
    protected String mUtdid;

    public abstract boolean cancel(String str);

    public abstract void close();

    public abstract int getChannelState();

    /* access modifiers changed from: protected */
    public int getMaxTimeOutData() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public String getStatus(int i) {
        switch (i) {
            case 1:
                return "CONNECTED";
            case 2:
                return "CONNECTING";
            case 3:
                return "DISCONNECTED";
            case 4:
                return "DISCONNECTING";
            default:
                return "DISCONNECTED";
        }
    }

    public abstract String getTag();

    public abstract boolean isAlive();

    /* access modifiers changed from: protected */
    public boolean isKeepAlive() {
        return true;
    }

    public abstract void notifyNetWorkChange(String str);

    public abstract void onTimeOut(String str, boolean z, String str2);

    public abstract void ping(boolean z, boolean z2);

    public abstract void sendMessage(Message message, boolean z);

    public void shutdown() {
    }

    public abstract void start();

    public abstract MonitorStatistic updateMonitorInfo();

    protected BaseConnection(Context context, int i, String str) {
        this.mConnectionType = i;
        this.mContext = context.getApplicationContext();
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag == null) {
            ALog.e(getTag(), "BaseConnection config null!!", new Object[0]);
            try {
                configByTag = new AccsClientConfig.Builder().setAppKey(ACCSManager.getDefaultAppkey(context)).setTag(str).build();
            } catch (AccsException e) {
                ALog.e(getTag(), "BaseConnection build config", e, new Object[0]);
            }
        }
        this.mConfigTag = configByTag.getTag();
        this.mAppkey = configByTag.getAppKey();
        this.mConfig = configByTag;
        this.mMessageHandler = new MessageHandler(context, this);
        this.mMessageHandler.mConnectType = this.mConnectionType;
        ALog.d(getTag(), "new connection", new Object[0]);
    }

    public void send(Message message, boolean z) {
        Message message2 = message;
        if (message2.isAck || UtilityImpl.isNetworkConnected(this.mContext)) {
            long flowCtrlDelay = message.getType() != 2 ? this.mMessageHandler.mFlowControl.getFlowCtrlDelay(message2.serviceId, message2.bizId) : 0;
            if (flowCtrlDelay == -1) {
                ALog.e(getTag(), "sendMessage ready server limit high", Constants.KEY_DATA_ID, message2.dataId);
                this.mMessageHandler.onResult(message2, 70021);
            } else if (flowCtrlDelay == -1000) {
                ALog.e(getTag(), "sendMessage ready server limit high for brush", Constants.KEY_DATA_ID, message2.dataId);
                this.mMessageHandler.onResult(message2, 70023);
            } else {
                if (flowCtrlDelay > 0) {
                    if (System.currentTimeMillis() > this.mExpectSendTime) {
                        message2.delyTime = flowCtrlDelay;
                    } else {
                        message2.delyTime = (this.mExpectSendTime + flowCtrlDelay) - System.currentTimeMillis();
                    }
                    this.mExpectSendTime = System.currentTimeMillis() + message2.delyTime;
                    ALog.e(getTag(), "sendMessage ready", Constants.KEY_DATA_ID, message2.dataId, "type", Message.MsgType.name(message.getType()), "delay", Long.valueOf(message2.delyTime));
                } else if ("accs".equals(message2.serviceId)) {
                    ALog.e(getTag(), "sendMessage ready", Constants.KEY_DATA_ID, message2.dataId, "type", Message.MsgType.name(message.getType()), "delay", Long.valueOf(message2.delyTime));
                } else if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(getTag(), "sendMessage ready", Constants.KEY_DATA_ID, message2.dataId, "type", Message.MsgType.name(message.getType()), "delay", Long.valueOf(message2.delyTime));
                }
                try {
                    if (TextUtils.isEmpty(this.mUtdid)) {
                        this.mUtdid = UtilityImpl.getDeviceId(this.mContext);
                    }
                    if (!message.isTimeOut()) {
                        sendMessage(message, z);
                    } else {
                        this.mMessageHandler.onResult(message2, -9);
                    }
                } catch (RejectedExecutionException unused) {
                    this.mMessageHandler.onResult(message2, 70008);
                    ALog.e(getTag(), "sendMessage ready queue full", "size", Integer.valueOf(ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size()));
                }
            }
        } else {
            ALog.e(getTag(), "sendMessage ready no network", Constants.KEY_DATA_ID, message2.dataId);
            this.mMessageHandler.onResult(message2, -13);
        }
    }

    /* access modifiers changed from: protected */
    public void setTimeOut(final String str, final boolean z, long j) {
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new Runnable() {
            public void run() {
                Message unhandledMessage = BaseConnection.this.mMessageHandler.getUnhandledMessage(str);
                if (unhandledMessage != null) {
                    BaseConnection.this.mMessageHandler.onResult(unhandledMessage, -9);
                    BaseConnection.this.onTimeOut(str, z, "receive data time out");
                    String tag = BaseConnection.this.getTag();
                    ALog.e(tag, str + "-> receive data time out!", new Object[0]);
                }
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: protected */
    public boolean reSend(Message message, int i) {
        boolean z = true;
        try {
            if (message.retryTimes > 3) {
                return false;
            }
            message.retryTimes++;
            message.delyTime = (long) i;
            ALog.e(getTag(), "reSend dataid:" + message.dataId + " retryTimes:" + message.retryTimes, new Object[0]);
            send(message, true);
            try {
                if (message.getNetPermanceMonitor() != null) {
                    message.getNetPermanceMonitor().take_date = 0;
                    message.getNetPermanceMonitor().to_tnet_date = 0;
                    message.getNetPermanceMonitor().retry_times = message.retryTimes;
                    if (message.retryTimes == 1) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, StatAction.KEY_TOTAL, 0.0d);
                    }
                }
            } catch (Throwable th) {
                th = th;
                this.mMessageHandler.onResult(message, -8);
                ALog.e(getTag(), "reSend error", th, new Object[0]);
                return z;
            }
            return z;
        } catch (Throwable th2) {
            th = th2;
            z = false;
            this.mMessageHandler.onResult(message, -8);
            ALog.e(getTag(), "reSend error", th, new Object[0]);
            return z;
        }
    }

    /* access modifiers changed from: protected */
    public void reSendAck(int i) {
        if (i < 0) {
            ALog.e(getTag(), "reSendAck", Constants.KEY_DATA_ID, Integer.valueOf(i));
            Message message = this.mAckMessage.get(Integer.valueOf(i));
            if (message != null) {
                reSend(message, 5000);
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, BaseMonitor.COUNT_ACK, 0.0d);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setPingTimeOut() {
        if (this.mPingTimeoutRunnable == null) {
            this.mPingTimeoutRunnable = new Runnable() {
                public void run() {
                    if (BaseConnection.this.mMessageHandler.getUnrcvPing()) {
                        ALog.e(BaseConnection.this.getTag(), "receive ping time out! ", new Object[0]);
                        HeartbeatManager.getInstance(BaseConnection.this.mContext).onNetworkTimeout();
                        BaseConnection.this.onTimeOut("", false, "receive ping timeout");
                        BaseConnection.this.mMessageHandler.onNetworkFail(-12);
                    }
                }
            };
        }
        cancelPingTimeOut();
        this.mPingTimeoutFuture = ThreadPoolExecutorFactory.getScheduledExecutor().schedule(this.mPingTimeoutRunnable, anet.channel.Constants.RECV_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: protected */
    public void cancelPingTimeOut() {
        if (this.mPingTimeoutFuture != null) {
            this.mPingTimeoutFuture.cancel(true);
        }
    }

    public String getHost(String str) {
        String inappHost = this.mConfig.getInappHost();
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(TextUtils.isEmpty(str) ? "" : str);
        sb.append(inappHost);
        String sb2 = sb.toString();
        try {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("https://");
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            sb3.append(str);
            sb3.append(inappHost);
            return sb3.toString();
        } catch (Throwable th) {
            ALog.e("InAppConnection", "getHost", th, new Object[0]);
            return sb2;
        }
    }

    /* access modifiers changed from: protected */
    public void initAwcn(Context context) {
        try {
            ENV env = ENV.ONLINE;
            if (AccsClientConfig.mEnv == 2) {
                env = ENV.TEST;
                SessionCenter.switchEnvironment(env);
            } else if (AccsClientConfig.mEnv == 1) {
                env = ENV.PREPARE;
                SessionCenter.switchEnvironment(env);
            }
            SessionCenter.init(context, new Config.Builder().setAppkey(this.mAppkey).setAppSecret(this.mConfig.getAppSecret()).setAuthCode(this.mConfig.getAuthCode()).setEnv(env).setTag(this.mConfig.getAppKey()).build());
            String str = ConnType.PK_ACS;
            if (this.mConfig.getInappPubKey() == 10 || this.mConfig.getInappPubKey() == 11) {
                str = ConnType.PK_OPEN;
            }
            ALog.i(getTag(), "init awcn register new conn protocol host:", this.mConfig.getInappHost());
            StrategyTemplate.getInstance().registerConnProtocol(this.mConfig.getInappHost(), ConnProtocol.valueOf(ConnType.HTTP2, ConnType.RTT_0, str, false));
        } catch (Throwable th) {
            ALog.e(getTag(), "initAwcn", th, new Object[0]);
        }
    }

    public void onResult(Message message, int i) {
        this.mMessageHandler.onResult(message, i);
    }

    public String getAppkey() {
        return this.mAppkey;
    }

    public ClientManager getClientManager() {
        if (this.mClientManager == null) {
            ALog.d(getTag(), "new ClientManager", Constants.KEY_CONFIG_TAG, this.mConfigTag);
            this.mClientManager = new ClientManager(this.mContext, this.mConfigTag);
        }
        return this.mClientManager;
    }

    public void startChannelService() {
        try {
            ThreadPoolExecutorFactory.schedule(new Runnable() {
                public void run() {
                    ALog.d(BaseConnection.this.getTag(), "startChannelService", new Object[0]);
                    Intent intent = new Intent(Constants.ACTION_START_SERVICE);
                    intent.putExtra("appKey", BaseConnection.this.getAppkey());
                    intent.putExtra("ttid", BaseConnection.this.mTtid);
                    intent.putExtra("packageName", GlobalClientInfo.getContext().getPackageName());
                    intent.putExtra("app_sercet", BaseConnection.this.mConfig.getAppSecret());
                    intent.putExtra(Constants.KEY_MODE, AccsClientConfig.mEnv);
                    intent.putExtra(org.android.agoo.common.Config.PROPERTY_APP_KEY, org.android.agoo.common.Config.getAgooAppKey(GlobalClientInfo.getContext()));
                    intent.putExtra(Constants.KEY_CONFIG_TAG, BaseConnection.this.mConfigTag);
                    intent.setClassName(GlobalClientInfo.getContext().getPackageName(), AdapterUtilityImpl.channelService);
                    IntentDispatch.dispatchIntent(GlobalClientInfo.getContext(), intent);
                    Intent intent2 = new Intent();
                    intent2.setAction(AgooConstants.INTENT_FROM_AGOO_REPORT);
                    intent2.setPackage(GlobalClientInfo.getContext().getPackageName());
                    intent2.setClassName(GlobalClientInfo.getContext().getPackageName(), AdapterGlobalClientInfo.getAgooCustomServiceName(AIPC.getContext().getPackageName()));
                    IntentDispatch.dispatchIntent(GlobalClientInfo.getContext(), intent2);
                }
            }, 10000, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            ALog.w(getTag(), "startChannelService", th, new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public String buildAuthUrl(String str) {
        String str2;
        String deviceId = UtilityImpl.getDeviceId(this.mContext);
        try {
            str2 = URLEncoder.encode(deviceId);
        } catch (Throwable th) {
            ALog.e(getTag(), "buildAuthUrl", th, new Object[0]);
            str2 = deviceId;
        }
        String appsign = UtilityImpl.getAppsign(this.mContext, getAppkey(), this.mConfig.getAppSecret(), deviceId, this.mConfigTag);
        StringBuilder sb = new StringBuilder(256);
        sb.append(str);
        sb.append("auth?1=");
        sb.append(str2);
        sb.append("&2=");
        sb.append(appsign);
        sb.append("&3=");
        sb.append(getAppkey());
        if (this.mConnToken != null) {
            sb.append("&4=");
            sb.append(this.mConnToken);
        }
        sb.append("&5=");
        sb.append(this.mConnectionType);
        sb.append("&6=");
        sb.append(UtilityImpl.getNetworkType(this.mContext));
        sb.append("&7=");
        sb.append(UtilityImpl.getOperator(this.mContext));
        sb.append("&8=");
        sb.append(this.mConnectionType == 1 ? "1.1.2" : Integer.valueOf(Constants.SDK_VERSION_CODE));
        sb.append("&9=");
        sb.append(System.currentTimeMillis());
        sb.append("&10=");
        sb.append(1);
        sb.append("&11=");
        sb.append(Build.VERSION.SDK_INT);
        sb.append("&12=");
        sb.append(this.mContext.getPackageName());
        sb.append("&13=");
        sb.append(UtilityImpl.getAppVersion(this.mContext));
        sb.append("&14=");
        sb.append(this.mTtid);
        sb.append("&15=");
        sb.append(UtilityImpl.urlEncode(Build.MODEL));
        sb.append("&16=");
        sb.append(UtilityImpl.urlEncode(Build.BRAND));
        sb.append("&17=");
        sb.append(Constants.SDK_VERSION_CODE);
        sb.append("&19=");
        sb.append(isSecurityOff() ^ true ? 1 : 0);
        sb.append("&20=");
        sb.append(this.mConfig.getStoreId());
        if (OrangeAdapter.isChannelModeEnable()) {
            sb.append("&21=");
            sb.append(state);
        }
        return sb.toString();
    }

    public boolean isSecurityOff() {
        return 2 == this.mConfig.getSecurity();
    }

    public void setForeBackState(int i) {
        state = i;
    }
}
