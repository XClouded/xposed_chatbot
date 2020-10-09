package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.CatcherManager;
import com.alibaba.motu.crashreporter.Options;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.crashreporter.async.AsyncThreadPool;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.rest.RestConstants;
import com.alibaba.motu.tbrest.utils.StringUtils;
import com.ut.mini.crashhandler.UTCrashHandlerWapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.AgooConstants;

public class MotuCrashReporter {
    static final MotuCrashReporter INSTANCE = new MotuCrashReporter();
    static List dataListenerList = new ArrayList();
    static List listenerList = new ArrayList();
    static List senderListenerList = new ArrayList();
    public AsyncThreadPool asyncTaskThread = new AsyncThreadPool();
    AtomicBoolean enabling = new AtomicBoolean(false);
    CrashReporter mCrashReporter = CrashReporter.getInstance();

    public int getCrashReporterState() {
        return 0;
    }

    public String getStrExtraInfo() {
        return "";
    }

    public void setCrashReportDataListener(ICrashReportDataListener iCrashReportDataListener) {
    }

    public void setCrashReporterState(int i) {
    }

    public void setExtraInfo(String str) {
    }

    public static MotuCrashReporter getInstance() {
        return INSTANCE;
    }

    public void setCrashCaughtListener(final UTCrashHandlerWapper uTCrashHandlerWapper) {
        this.mCrashReporter.addUncaughtExceptionLinster(new CatcherManager.UncaughtExceptionLinster() {
            public boolean originalEquals(Object obj) {
                if (uTCrashHandlerWapper == null || obj == null) {
                    return false;
                }
                return uTCrashHandlerWapper.equals(obj);
            }

            public Map<String, Object> onUncaughtException(Thread thread, Throwable th) {
                return uTCrashHandlerWapper.onCrashCaught(thread, th);
            }
        });
    }

    public void setCrashCaughtListener(final IUTCrashCaughtListener iUTCrashCaughtListener) {
        this.mCrashReporter.addUncaughtExceptionLinster(new CatcherManager.UncaughtExceptionLinster() {
            public boolean originalEquals(Object obj) {
                if (iUTCrashCaughtListener == null || obj == null) {
                    return false;
                }
                return iUTCrashCaughtListener.equals(obj);
            }

            public Map<String, Object> onUncaughtException(Thread thread, Throwable th) {
                return iUTCrashCaughtListener.onCrashCaught(thread, th);
            }
        });
    }

    public void addCrashReportSendListener(ICrashReportSendListener iCrashReportSendListener) {
        this.mCrashReporter.addSendLinster(iCrashReportSendListener);
    }

    public void removeCrashReportSendListener(ICrashReportSendListener iCrashReportSendListener) {
        this.mCrashReporter.removeSendLinster(iCrashReportSendListener);
    }

    public boolean isTaobaoApplication(Context context) {
        return StringUtils.isNotBlank(this.mCrashReporter.mProcessName) && this.mCrashReporter.mProcessName.startsWith(AgooConstants.TAOBAO_PACKAGE);
    }

    @Deprecated
    public boolean enable(Context context, String str, String str2, String str3, String str4, ReporterConfigure reporterConfigure) {
        String str5 = "";
        if (StringUtils.isBlank(str) || "12278902".equals(str) || "21646297".equals(str)) {
            str = "21646297";
            str5 = "12278902@android";
        }
        return enable(context, str5, str, str2, str3, str4, (ReporterConfigure) null);
    }

    public boolean enable(Context context, String str, String str2, String str3, String str4, String str5, ReporterConfigure reporterConfigure) {
        ReporterConfigure reporterConfigure2 = reporterConfigure;
        if (this.enabling.compareAndSet(false, true)) {
            try {
                LogUtil.d("CrashSDK RestApi initialize start ");
                SendService.getInstance().init(context, str, str2, str3, str4, str5);
                LogUtil.d("CrashSDK RestApi initialize success! ");
                Configuration instance = Configuration.getInstance();
                if (reporterConfigure2 != null) {
                    instance.add(new Options.Option(Configuration.enableUncaughtExceptionIgnore, Boolean.valueOf(reporterConfigure2.enableUncaughtExceptionIgnore)));
                    instance.add(new Options.Option(Configuration.enableExternalLinster, Boolean.valueOf(reporterConfigure2.enableExternalLinster)));
                    instance.add(new Options.Option(Configuration.enableFinalizeFake, Boolean.valueOf(reporterConfigure2.enableFinalizeFake)));
                    instance.add(new Options.Option(Configuration.enableUIProcessSafeGuard, Boolean.valueOf(reporterConfigure2.enableUIProcessSafeGuard)));
                    instance.add(new Options.Option(Configuration.enableSecuritySDK, Boolean.valueOf(reporterConfigure2.enableSecuritySDK)));
                    instance.add(new Options.Option(Configuration.enableANRCatch, Boolean.valueOf(reporterConfigure2.enableCatchANRException)));
                    if (!RestConstants.G_DEFAULT_ADASHX_HOST.equals(reporterConfigure2.adashxServerHost)) {
                        instance.add(new Options.Option(Configuration.adashxServerHost, reporterConfigure2.adashxServerHost));
                        SendService.getInstance().changeHost(reporterConfigure2.adashxServerHost);
                    }
                }
                this.mCrashReporter.initialize(context, str, str2, str3, str4, instance);
                this.mCrashReporter.enable();
                setUserNick(str5);
                return true;
            } catch (Exception e) {
                LogUtil.e("enable", e);
            }
        }
        return false;
    }

    public void registerLifeCallbacks(Context context) {
        this.mCrashReporter.registerLifeCallbacks(context);
    }

    public void setAppVersion(String str) {
        this.mCrashReporter.refreshAppVersion(str);
        SendService.getInstance().updateAppVersion(str);
    }

    public void addNativeHeaderInfo(String str, String str2) {
        this.mCrashReporter.addNativeHeaderInfo(str, str2);
    }

    public void closeNativeSignalTerm() {
        this.mCrashReporter.closeNativeSignalTerm();
    }

    public void changeHost(String str) {
        Configuration.getInstance().add(new Options.Option(Configuration.adashxServerHost, str));
        SendService.getInstance().changeHost(str);
    }

    public void setUserNick(String str) {
        if (!StringUtils.isBlank(str)) {
            this.mCrashReporter.setProperty(new Propertys.Property(Constants.USERNICK, str));
            SendService.getInstance().updateUserNick(str);
        }
    }

    public void setTTid(String str) {
        if (!StringUtils.isBlank(str)) {
            this.mCrashReporter.setProperty(new Propertys.Property(Constants.CHANNEL, str));
            SendService.getInstance().updateChannel(str);
        }
    }

    public List getMyListenerList() {
        return this.mCrashReporter.getAllUncaughtExceptionLinster();
    }

    public List getMyDataListenerList() {
        return dataListenerList;
    }

    public List getMySenderListenerList() {
        return senderListenerList;
    }
}
