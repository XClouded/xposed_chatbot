package anet.channel;

import alimama.com.unwetaologger.base.UNWLogger;
import android.content.Context;
import android.preference.PreferenceManager;
import anet.channel.Config;
import anet.channel.analysis.DefaultFullTraceAnalysis;
import anet.channel.analysis.DefaultNetworkAnalysis;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.appmonitor.DefaultAppMonitor;
import anet.channel.cache.AVFSCacheImpl;
import anet.channel.config.OrangeConfigImpl;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.fulltrace.AnalysisFactory;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.log.TLogAdapter;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import anetwork.channel.aidl.adapter.RemoteGetterHelper;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.cache.CachePrediction;
import anetwork.channel.config.NetworkConfigCenter;
import com.taobao.orange.OConstant;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.mtop.intf.MtopUnitStrategy;
import org.android.agoo.common.AgooConstants;

public class TaobaoNetworkAdapter implements Serializable {
    private static final String TAG = "awcn.TaobaoNetworkAdapter";
    public static AtomicBoolean isInited = new AtomicBoolean();

    public static void init(Context context, HashMap<String, Object> hashMap) {
        boolean z;
        boolean z2;
        if (isInited.compareAndSet(false, true)) {
            if (hashMap != null && AgooConstants.TAOBAO_PACKAGE.equals(hashMap.get(UNWLogger.LOG_VALUE_TYPE_PROCESS))) {
                AwcnConfig.setAccsSessionCreateForbiddenInBg(true);
            }
            ALog.setLog(new TLogAdapter());
            NetworkConfigCenter.setRemoteConfig(new OrangeConfigImpl());
            AppMonitor.setInstance(new DefaultAppMonitor());
            NetworkAnalysis.setInstance(new DefaultNetworkAnalysis());
            AnalysisFactory.setInstance(new DefaultFullTraceAnalysis());
            ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                public void run() {
                    try {
                        AVFSCacheImpl aVFSCacheImpl = new AVFSCacheImpl();
                        aVFSCacheImpl.initialize();
                        CacheManager.addCache(aVFSCacheImpl, new CachePrediction() {
                            public boolean handleCache(String str, Map<String, String> map) {
                                return "weex".equals(map.get("f-refer"));
                            }
                        }, 1);
                    } catch (Exception unused) {
                    }
                }
            }, ThreadPoolExecutorFactory.Priority.NORMAL);
            if (hashMap != null) {
                try {
                    if (AgooConstants.TAOBAO_PACKAGE.equals(hashMap.get(UNWLogger.LOG_VALUE_TYPE_PROCESS)) && ((Boolean) hashMap.get("isDebuggable")).booleanValue()) {
                        Utils.invokeStaticMethodThrowException("com.taobao.android.request.analysis.RequestRecorder", "init", new Class[]{Context.class}, context);
                    }
                } catch (Exception e) {
                    ALog.e(TAG, "RequestRecorder error.", (String) null, e, new Object[0]);
                }
            }
            if (hashMap != null) {
                try {
                    Integer num = (Integer) hashMap.get("outline");
                    if (num != null && num.intValue() == -1) {
                        ALog.e(TAG, "taobao speed mode enable.", (String) null, new Object[0]);
                        GlobalAppRuntimeInfo.addBucketInfo("tbspeed", "speed");
                        if (NetworkConfigCenter.isRemoteNetworkServiceEnable()) {
                            RemoteGetterHelper.initRemoteGetterAndWait(context, false);
                        }
                        AwcnConfig.setAsyncLoadStrategyEnable(true);
                    }
                    if (!hashMap.containsKey("isNextLaunch") || PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AwcnConfig.NEXT_LAUNCH_FORBID, false)) {
                        z = false;
                    } else {
                        GlobalAppRuntimeInfo.addBucketInfo("isNextLaunch", "true");
                        z = true;
                    }
                    AwcnConfig.setTbNextLaunch(z);
                    try {
                        z2 = ((Boolean) Utils.invokeStaticMethodThrowException("com.taobao.android.speed.TBSpeed", "isSpeedEdition", new Class[]{Context.class, String.class}, context, "NWServiceB")).booleanValue();
                    } catch (Exception unused) {
                        z2 = false;
                    }
                    if (z2) {
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NetworkConfigCenter.SERVICE_OPTIMIZE, true)) {
                            GlobalAppRuntimeInfo.addBucketInfo("tbspeed", "speed");
                            NetworkConfigCenter.setBindServiceOptimize(true);
                            ALog.i(TAG, "bindservice optimize enabled.", (String) null, new Object[0]);
                        }
                    }
                } catch (Exception unused2) {
                }
            }
            if (hashMap != null) {
                try {
                    if (AgooConstants.TAOBAO_PACKAGE.equals((String) hashMap.get(UNWLogger.LOG_VALUE_TYPE_PROCESS))) {
                        String str = (String) hashMap.get(OConstant.LAUNCH_ONLINEAPPKEY);
                        registerPresetSession(MtopUnitStrategy.GUIDE_ONLINE_DOMAIN, str, ConnProtocol.valueOf(ConnType.HTTP2, ConnType.RTT_0, ConnType.PK_ACS), true);
                        ConnProtocol valueOf = ConnProtocol.valueOf(ConnType.HTTP2, ConnType.RTT_0, ConnType.PK_CDN);
                        registerPresetSession("gw.alicdn.com", str, valueOf, false);
                        registerPresetSession("dorangesource.alicdn.com", str, valueOf, false);
                        registerPresetSession("ossgw.alicdn.com", str, valueOf, false);
                    }
                } catch (Exception unused3) {
                }
            }
        }
    }

    private static void registerPresetSession(String str, String str2, ConnProtocol connProtocol, boolean z) {
        StrategyTemplate.getInstance().registerConnProtocol(str, connProtocol);
        if (z) {
            SessionCenter.getInstance(new Config.Builder().setAppkey(str2).setEnv(ENV.ONLINE).build()).registerSessionInfo(SessionInfo.create(str, z, false, (IAuth) null, (IHeartbeat) null, (DataFrameCb) null));
        }
    }
}
