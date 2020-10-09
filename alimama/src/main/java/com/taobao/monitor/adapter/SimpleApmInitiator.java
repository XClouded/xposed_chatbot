package com.taobao.monitor.adapter;

import android.app.Application;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import com.ali.ha.datahub.BizSubscriber;
import com.ali.ha.datahub.DataHub;
import com.ali.ha.fulltrace.FulltraceLauncher;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.monitor.APMLauncher;
import com.taobao.monitor.ProcedureGlobal;
import com.taobao.monitor.ProcedureLauncher;
import com.taobao.monitor.adapter.common.TBAPMConstants;
import com.taobao.monitor.adapter.network.TBRestSender;
import com.taobao.monitor.common.ThreadUtils;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.AbsWebView;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.WebViewProxy;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.processor.pageload.IProcedureManager;
import com.taobao.monitor.impl.processor.pageload.ProcedureManagerSetter;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.impl.util.TopicUtils;
import com.taobao.monitor.network.NetworkSenderProxy;
import com.taobao.monitor.procedure.Header;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import com.uc.webview.export.WebView;
import java.io.Serializable;
import java.util.HashMap;

public class SimpleApmInitiator implements Serializable {
    private static final String TAG = "TBAPMAdapterLaunchers";
    private long apmStartTime = TimeUtils.currentTimeMillis();
    private long cpuStartTime = SystemClock.currentThreadTimeMillis();

    public void init(Application application, HashMap<String, Object> hashMap) {
        if (!TBAPMConstants.init) {
            Logger.i(TAG, "init start");
            TBAPMConstants.open = true;
            initAPMFunction(application, hashMap);
            Logger.i(TAG, "init end");
            TBAPMConstants.init = true;
        }
        Logger.i(TAG, "apmStartTime:", Long.valueOf(TimeUtils.currentTimeMillis() - this.apmStartTime));
    }

    private void initAPMFunction(Application application, HashMap<String, Object> hashMap) {
        Global.instance().setHandler(ProcedureGlobal.instance().handler());
        initAPMLauncher(application, hashMap);
        initTbRest(application);
        initFulltrace(application);
        initDataHub();
        initLauncherProcedure();
        initWebView();
    }

    private void initTbRest(Application application) {
        NetworkSenderProxy.instance().setSender(new TBRestSender());
    }

    private void initDataHub() {
        DataHub.getInstance().init(new BizSubscriber() {
            public void pub(final String str, final HashMap<String, String> hashMap) {
                if ("splash".equals(str)) {
                    GlobalStats.hasSplash = true;
                }
                async(new Runnable() {
                    public void run() {
                        IProcedure currentProcedures = DataHubProcedureGroupHelper.getCurrentProcedures();
                        if (currentProcedures != null) {
                            currentProcedures.addBiz(str, hashMap);
                        }
                    }
                });
            }

            public void pubAB(final String str, final HashMap<String, String> hashMap) {
                async(new Runnable() {
                    public void run() {
                        IProcedure currentProcedures = DataHubProcedureGroupHelper.getCurrentProcedures();
                        if (currentProcedures != null) {
                            currentProcedures.addBizAbTest(str, hashMap);
                        }
                    }
                });
            }

            public void onStage(String str, String str2, long j) {
                final long currentTimeMillis = TimeUtils.currentTimeMillis();
                final String str3 = str2;
                final String str4 = str;
                async(new Runnable() {
                    public void run() {
                        IProcedure currentProcedures = DataHubProcedureGroupHelper.getCurrentProcedures();
                        if (currentProcedures != null) {
                            HashMap hashMap = new HashMap();
                            hashMap.put(str3, Long.valueOf(currentTimeMillis));
                            currentProcedures.addBizStage(str4, hashMap);
                        }
                    }
                });
            }

            public void setMainBiz(final String str, final String str2) {
                async(new Runnable() {
                    public void run() {
                        IProcedure currentProcedures = DataHubProcedureGroupHelper.getCurrentProcedures();
                        if (currentProcedures != null) {
                            currentProcedures.addProperty("bizID", str);
                            if (!TextUtils.isEmpty(str2)) {
                                currentProcedures.addProperty("bizCode", str2);
                            }
                        }
                    }
                });
            }

            public void onBizDataReadyStage() {
                IProcedure currentProcedures = DataHubProcedureGroupHelper.getCurrentProcedures();
                if (currentProcedures != null) {
                    currentProcedures.stage("onBizDataReadyTime", TimeUtils.currentTimeMillis());
                }
            }

            private void async(Runnable runnable) {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    Global.instance().handler().post(runnable);
                } else {
                    runnable.run();
                }
            }
        });
    }

    private void initWebView() {
        WebViewProxy.INSTANCE.setReal(new AbsWebView() {
            public boolean isWebView(View view) {
                return view instanceof WebView;
            }

            public int getProgress(View view) {
                return ((WebView) view).getProgress();
            }
        });
    }

    private void initLauncherProcedure() {
        boolean z = false;
        IProcedure createProcedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/startup"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(false).setParent((IProcedure) null).build());
        createProcedure.begin();
        ProcedureGlobal.PROCEDURE_MANAGER.setLauncherProcedure(createProcedure);
        IProcedure createProcedure2 = ProcedureFactoryProxy.PROXY.createProcedure("/APMSelf", new ProcedureConfig.Builder().setIndependent(false).setUpload(false).setParentNeedStats(false).setParent(createProcedure).build());
        createProcedure2.begin();
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            z = true;
        }
        createProcedure2.addProperty("isMainThread", Boolean.valueOf(z));
        createProcedure2.addProperty("threadName", Thread.currentThread().getName());
        createProcedure2.stage("taskStart", this.apmStartTime);
        createProcedure2.stage("cpuStartTime", this.cpuStartTime);
        TBAPMAdapterSubTaskManager.transferPendingTasks();
        createProcedure2.stage("taskEnd", TimeUtils.currentTimeMillis());
        createProcedure2.stage("cpuEndTime", SystemClock.currentThreadTimeMillis());
        createProcedure2.end();
    }

    private void initAPMLauncher(Application application, HashMap<String, Object> hashMap) {
        ProcedureLauncher.init(application, hashMap);
        APMLauncher.init(application, hashMap);
        ProcedureManagerSetter.instance().setProxy(new IProcedureManager() {
            public void setCurrentActivityProcedure(IProcedure iProcedure) {
                ProcedureGlobal.PROCEDURE_MANAGER.setCurrentActivityProcedure(iProcedure);
            }

            public void setCurrentFragmentProcedure(IProcedure iProcedure) {
                ProcedureGlobal.PROCEDURE_MANAGER.setCurrentFragmentProcedure(iProcedure);
            }

            public void setCurrentLauncherProcedure(IProcedure iProcedure) {
                ProcedureGlobal.PROCEDURE_MANAGER.setLauncherProcedure(iProcedure);
            }
        });
    }

    private void initFulltrace(final Application application) {
        ThreadUtils.start(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("appVersion", Header.appVersion);
                hashMap.put("session", Header.session);
                hashMap.put("apmVersion", Header.apmVersion);
                hashMap.put("ttid", Header.ttid);
                hashMap.put("userNick", Header.userNick);
                hashMap.put("userId", Header.userId);
                hashMap.put(WXDebugConstants.ENV_OS_VERSION, Header.osVersion);
                hashMap.put("os", Header.os);
                hashMap.put("appChannelVersion", Header.channel);
                hashMap.put(WXDebugConstants.ENV_DEVICE_MODEL, Header.deviceModel);
                hashMap.put("brand", Header.brand);
                hashMap.put("utdid", Header.utdid);
                hashMap.put("appKey", Header.appKey);
                hashMap.put("appId", Header.appId);
                hashMap.put(Constants.KEY_APP_BUILD, Header.appBuild);
                hashMap.put("processName", Header.processName);
                FulltraceLauncher.init(application, hashMap);
            }
        });
    }
}
