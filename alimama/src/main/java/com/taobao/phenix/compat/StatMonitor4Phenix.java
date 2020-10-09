package com.taobao.phenix.compat;

import android.app.Application;
import android.content.Context;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.taobao.pexode.Pexode;
import com.taobao.phenix.chain.ImageDecodingListener;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.stat.NavigationLifecycleObserver;
import com.taobao.phenix.compat.stat.NetworkAnalyzer;
import com.taobao.phenix.compat.stat.TBImageFlowMonitor;
import com.taobao.phenix.compat.stat.TBImageRetrieveABListener;
import com.taobao.phenix.compat.stat.TBNonCriticalErrorReporter;
import com.taobao.phenix.intf.Phenix;
import com.taobao.rxm.schedule.PairingThrottlingScheduler;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatMonitor4Phenix {
    /* access modifiers changed from: private */
    public static final StatImageDecodingListener IMAGE_DECODING_LISTENER = new StatImageDecodingListener();
    public static long mInitTime;
    public static boolean mIsFullTrackValid;
    public static boolean mIsUseNewThreadModel;

    private static class StatImageDecodingListener implements ImageDecodingListener {
        final Map<Long, String> mLatestDecodingUrls;

        private StatImageDecodingListener() {
            this.mLatestDecodingUrls = new LinkedHashMap(2);
        }

        public void onDecodeStart(long j, String str) {
            synchronized (this) {
                if (this.mLatestDecodingUrls.size() > 5) {
                    this.mLatestDecodingUrls.clear();
                }
                this.mLatestDecodingUrls.put(Long.valueOf(j), str);
            }
            MotuCrashReporter.getInstance().addNativeHeaderInfo("PHENIX_LATEST_DECODING", getLatestDecodingUrlText());
        }

        public synchronized void onDecodeFinish(long j, String str) {
            this.mLatestDecodingUrls.remove(Long.valueOf(j));
        }

        public String getLatestDecodingUrlText() {
            String str;
            synchronized (this) {
                str = null;
                if (this.mLatestDecodingUrls.size() > 0) {
                    for (Map.Entry next : this.mLatestDecodingUrls.entrySet()) {
                        if (str == null) {
                            str = (String) next.getValue();
                        } else {
                            str = str + "," + ((String) next.getValue());
                        }
                    }
                }
            }
            return str;
        }
    }

    public static void setupFlowMonitor(Context context, NetworkAnalyzer networkAnalyzer, int i) {
        setupFlowMonitor(context, networkAnalyzer, i, 100, 0, (TBImageRetrieveABListener) null);
    }

    public static void setupFlowMonitor(Context context, NetworkAnalyzer networkAnalyzer, int i, int i2) {
        setupFlowMonitor(context, networkAnalyzer, i, 100, i2, (TBImageRetrieveABListener) null);
    }

    public static void setupFlowMonitor(Context context, NetworkAnalyzer networkAnalyzer, int i, int i2, int i3, TBImageRetrieveABListener tBImageRetrieveABListener) {
        TBImageFlowMonitor tBImageFlowMonitor = new TBImageFlowMonitor(i, i2, networkAnalyzer, tBImageRetrieveABListener);
        mInitTime = System.currentTimeMillis();
        tBImageFlowMonitor.setNavigationInfoObtainer(NavigationLifecycleObserver.getInstance());
        ((Application) context).registerActivityLifecycleCallbacks(NavigationLifecycleObserver.getInstance());
        tBImageFlowMonitor.setNonCriticalErrorReporter(new TBNonCriticalErrorReporter(context));
        if (i3 > 0) {
            tBImageFlowMonitor.setImageWarningSize(i3);
        }
        Pexode.setForcedDegradationListener(tBImageFlowMonitor);
        Phenix.instance().setImageFlowMonitor(tBImageFlowMonitor);
        SchedulerSupplier build = Phenix.instance().schedulerBuilder().build();
        if (build != null) {
            Scheduler forNetwork = build.forNetwork();
            if (forNetwork instanceof PairingThrottlingScheduler) {
                ((PairingThrottlingScheduler) forNetwork).setDegradationListener(tBImageFlowMonitor);
            }
        }
        Phenix.instance().setImageDecodingListener(IMAGE_DECODING_LISTENER);
        MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new IUTCrashCaughtListener() {
            public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
                String latestDecodingUrlText = StatMonitor4Phenix.IMAGE_DECODING_LISTENER.getLatestDecodingUrlText();
                if (latestDecodingUrlText == null) {
                    UnitedLog.w("StatMonitor4Phenix", "crash happened, collect latest decoding failed", new Object[0]);
                    return null;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("PHENIX_LATEST_DECODING", latestDecodingUrlText);
                UnitedLog.i("StatMonitor4Phenix", "crash happened, collect latest decoding urls=%s", latestDecodingUrlText);
                return hashMap;
            }
        });
        UnitedLog.i("StatMonitor4Phenix", "init stat monitor with sampling=%d", Integer.valueOf(i));
        try {
            Class.forName("com.taobao.analysis.fulltrace.FullTraceAnalysis");
            mIsFullTrackValid = true;
        } catch (Exception unused) {
            mIsFullTrackValid = false;
        }
    }
}
