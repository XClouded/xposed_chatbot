package anet.channel.fulltrace;

import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;

public class AnalysisFactory {
    private static final String TAG = "anet.AnalysisFactory";
    private static volatile IFullTraceAnalysis analysis = new AnalysisProxy((IFullTraceAnalysis) null);

    public static IFullTraceAnalysis getInstance() {
        return analysis;
    }

    public static void setInstance(IFullTraceAnalysis iFullTraceAnalysis) {
        analysis = new AnalysisProxy(iFullTraceAnalysis);
    }

    private static class AnalysisProxy implements IFullTraceAnalysis {
        private IFullTraceAnalysis analysis;

        AnalysisProxy(IFullTraceAnalysis iFullTraceAnalysis) {
            this.analysis = iFullTraceAnalysis;
        }

        public String createRequest() {
            if (this.analysis == null) {
                return null;
            }
            try {
                return this.analysis.createRequest();
            } catch (Throwable th) {
                ALog.e(AnalysisFactory.TAG, "createRequest fail.", (String) null, th, new Object[0]);
                return null;
            }
        }

        public void commitRequest(String str, RequestStatistic requestStatistic) {
            if (this.analysis != null) {
                try {
                    this.analysis.commitRequest(str, requestStatistic);
                } catch (Throwable th) {
                    ALog.e(AnalysisFactory.TAG, "fulltrace commit fail.", (String) null, th, new Object[0]);
                }
            }
        }

        public SceneInfo getSceneInfo() {
            if (this.analysis == null) {
                return null;
            }
            try {
                return this.analysis.getSceneInfo();
            } catch (Throwable th) {
                ALog.e(AnalysisFactory.TAG, "getSceneInfo fail", (String) null, th, new Object[0]);
                return null;
            }
        }
    }
}
