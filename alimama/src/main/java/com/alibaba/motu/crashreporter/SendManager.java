package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.rest.RestConstants;
import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import com.alibaba.motu.tbrest.utils.Base64;
import com.alibaba.motu.tbrest.utils.GzipUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

final class SendManager {
    Configuration mConfiguration;
    Context mContext;
    ReportBuilder mReportBuilder;
    ReportSender mReportSender;
    ReporterContext mReporterContext;
    AtomicBoolean mSending = new AtomicBoolean(false);
    Map<String, CrashReport> mWaitingSend = new ConcurrentHashMap();
    Map<String, ICrashReportSendListener> sendListenerMap = new ConcurrentHashMap();

    interface ReportSender {
        boolean sendReport(CrashReport crashReport);
    }

    public SendManager(Context context, ReporterContext reporterContext, Configuration configuration, ReportBuilder reportBuilder) {
        this.mContext = context;
        this.mReporterContext = reporterContext;
        this.mConfiguration = configuration;
        this.mReportBuilder = reportBuilder;
        this.mReportSender = new DefaultSender(context, reporterContext, configuration);
    }

    public void addListener(ICrashReportSendListener iCrashReportSendListener) {
        if (iCrashReportSendListener != null && StringUtils.isNotBlank(iCrashReportSendListener.getName())) {
            this.sendListenerMap.put(iCrashReportSendListener.getName(), iCrashReportSendListener);
        }
    }

    public void removeListener(ICrashReportSendListener iCrashReportSendListener) {
        if (iCrashReportSendListener != null && StringUtils.isNotBlank(iCrashReportSendListener.getName())) {
            this.sendListenerMap.remove(iCrashReportSendListener.getName());
        }
    }

    public void sendAllReport() {
        sendReports(this.mReportBuilder.listProcessCrashReport());
    }

    public void sendReport(CrashReport crashReport) {
        sendReports(new CrashReport[]{crashReport});
    }

    public void sendReports(CrashReport[] crashReportArr) {
        if (crashReportArr != null) {
            for (CrashReport crashReport : crashReportArr) {
                if (crashReport != null && StringUtils.isNotBlank(crashReport.mReportPath)) {
                    this.mWaitingSend.put(crashReport.mReportPath, crashReport);
                }
            }
            if (!this.mWaitingSend.isEmpty() && this.mSending.compareAndSet(false, true)) {
                MotuCrashReporter.getInstance().asyncTaskThread.start(new Runnable() {
                    public void run() {
                        try {
                            Iterator<Map.Entry<String, CrashReport>> it = SendManager.this.mWaitingSend.entrySet().iterator();
                            if (it != null) {
                                while (it.hasNext()) {
                                    Map.Entry next = it.next();
                                    if (next != null) {
                                        try {
                                            CrashReport crashReport = (CrashReport) next.getValue();
                                            if (crashReport != null) {
                                                if (StringUtils.isBlank(crashReport.mReportPath) || StringUtils.isBlank(crashReport.mReportName) || StringUtils.isBlank(crashReport.mReportType)) {
                                                    try {
                                                        crashReport.deleteReportFile();
                                                    } catch (Exception e) {
                                                        LogUtil.e("remote invalid crash report.", e);
                                                    }
                                                } else if (crashReport.isComplete()) {
                                                    crashReport.extractPropertys();
                                                    for (ICrashReportSendListener beforeSend : SendManager.this.sendListenerMap.values()) {
                                                        try {
                                                            beforeSend.beforeSend(crashReport);
                                                        } catch (Exception e2) {
                                                            LogUtil.e("beforeSend", e2);
                                                        }
                                                    }
                                                    boolean sendReport = SendManager.this.mReportSender.sendReport(crashReport);
                                                    for (ICrashReportSendListener afterSend : SendManager.this.sendListenerMap.values()) {
                                                        try {
                                                            afterSend.afterSend(sendReport, crashReport);
                                                        } catch (Exception e3) {
                                                            LogUtil.e("beforeSend", e3);
                                                        }
                                                    }
                                                    if (sendReport) {
                                                        crashReport.deleteReportFile();
                                                    }
                                                } else if (!crashReport.mCurrentTrigger) {
                                                    crashReport.deleteReportFile();
                                                }
                                            }
                                        } catch (Exception e4) {
                                            LogUtil.e("send and del crash report.", e4);
                                        } catch (Throwable th) {
                                            it.remove();
                                            throw th;
                                        }
                                    }
                                    it.remove();
                                }
                            }
                        } finally {
                            SendManager.this.mSending.set(false);
                        }
                    }
                });
            }
        }
    }

    class DefaultSender implements ReportSender {
        Configuration mConfiguration;
        Context mContext;
        ReporterContext mReporterContext;

        public DefaultSender(Context context, ReporterContext reporterContext, Configuration configuration) {
            this.mContext = context;
            this.mReporterContext = reporterContext;
            this.mConfiguration = configuration;
            if (this.mConfiguration.getBoolean(Configuration.enableSecuritySDK, true)) {
                RestUrlWrapper.enableSecuritySDK();
                RestUrlWrapper.setContext(this.mContext);
            }
        }

        public boolean sendReport(CrashReport crashReport) {
            int i;
            String str;
            Object obj;
            if (crashReport == null) {
                return true;
            }
            if ("java".equals(crashReport.mReportType)) {
                i = 1;
            } else if ("native".equals(crashReport.mReportType) || CrashReport.TYPE_ANR.equals(crashReport.mReportType)) {
                i = 61006;
            } else {
                LogUtil.i(String.format("unsupport report type:%s path:%s", new Object[]{crashReport.mReportType, crashReport.mReportPath}));
                return true;
            }
            crashReport.mPropertys.copyTo(new HashMap());
            String string = this.mConfiguration.getString(Configuration.adashxServerHost, RestConstants.G_DEFAULT_ADASHX_HOST);
            String reportContent = crashReport.getReportContent();
            if (Configuration.getInstance().getBoolean(Configuration.enableReportContentCompress, true)) {
                str = Base64.encodeBase64String(GzipUtils.gzip(reportContent.getBytes()));
                obj = "MOTU_REPORTER_SDK_3.0.0_PRIVATE_COMPRESS";
            } else {
                str = reportContent;
                obj = "MOTU_REPORTER_SDK_3.0.0_PRIVATE";
            }
            return SendService.getInstance().sendRequest(string, System.currentTimeMillis(), "-", i, obj, str, "-", (Map<String, String>) null).booleanValue();
        }
    }
}
