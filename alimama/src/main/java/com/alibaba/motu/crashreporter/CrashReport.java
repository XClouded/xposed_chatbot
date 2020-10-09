package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.utils.AppUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.io.File;

public final class CrashReport {
    private static final String TAG = "CrashReport";
    public static final String TYPE_ANR = "anr";
    public static final String TYPE_JAVA = "java";
    public static final String TYPE_NATIVE = "native";
    Context mContext;
    boolean mCurrentTrigger;
    Propertys mPropertys = new Propertys();
    String mReportContent;
    File mReportFile;
    String mReportName;
    String mReportPath;
    String mReportType;
    ReporterContext mReporterContext;

    public static String buildReportName(String str, String str2, String str3, long j, String str4, String str5) {
        String replaceUnderscore = replaceUnderscore(str3);
        String replaceUnderscore2 = replaceUnderscore(str4);
        return "CrashSDK_1.0.0.0__df_df_df_" + str2 + "_" + replaceUnderscore + "_" + String.valueOf(j) + "_" + AppUtils.getGMT8Time(j) + "_" + StringUtils.defaultString(replaceUnderscore2, "df") + "_" + str5 + ".log";
    }

    public static String replaceUnderscore(String str) {
        return str != null ? str.replace("_", "&#95;") : "";
    }

    public static String revertUnderscore(String str) {
        return str != null ? str.replace("&#95;", "_") : "";
    }

    public static String[] parseReportName(String str) {
        if (!StringUtils.isNotBlank(str) || !str.endsWith(".log")) {
            return null;
        }
        String[] split = str.split("_");
        if (split.length != 12) {
            return null;
        }
        split[11] = split[11].replace(".log", "");
        if ("java".equals(split[11]) || "native".equals(split[11]) || TYPE_ANR.equals(split[11])) {
            return split;
        }
        return null;
    }

    private CrashReport() {
    }

    public static CrashReport buildCrashReport(Context context, File file, ReporterContext reporterContext, boolean z) {
        String str;
        String name = file.getName();
        String absolutePath = file.getAbsolutePath();
        String[] parseReportName = parseReportName(name);
        if (parseReportName == null) {
            return null;
        }
        CrashReport crashReport = new CrashReport();
        crashReport.mContext = context;
        crashReport.mReporterContext = reporterContext;
        crashReport.mReportFile = file;
        crashReport.mReportName = name;
        crashReport.mReportPath = absolutePath;
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_NAME, parseReportName[0]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_VERSION, parseReportName[1]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.CRASH_SDK_BUILD, parseReportName[2]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.BRAND, parseReportName[3]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.DEVICE_MODEL, parseReportName[4]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.UTDID, parseReportName[5]));
        crashReport.mPropertys.add(new Propertys.Property("APP_KEY", parseReportName[6]));
        String revertUnderscore = revertUnderscore(parseReportName[7]);
        try {
            str = Utils.getContextAppVersion(context);
            if (revertUnderscore != null && str != null && str.length() > 0 && !revertUnderscore.equals(str)) {
                try {
                    SendService.getInstance().updateAppVersion(str);
                    LogUtil.d("crashreporter update appversion:" + str);
                } catch (Exception unused) {
                }
                crashReport.mPropertys.add(new Propertys.Property(Constants.APP_VERSION, str));
                crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIMESTAMP, parseReportName[8]));
                crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIME, parseReportName[9]));
                crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TAG, revertUnderscore(parseReportName[10])));
                crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TYPE, parseReportName[11]));
                crashReport.mReportType = parseReportName[11];
                crashReport.mCurrentTrigger = z;
                return crashReport;
            }
        } catch (Exception unused2) {
        }
        str = revertUnderscore;
        crashReport.mPropertys.add(new Propertys.Property(Constants.APP_VERSION, str));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIMESTAMP, parseReportName[8]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_CREATE_TIME, parseReportName[9]));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TAG, revertUnderscore(parseReportName[10])));
        crashReport.mPropertys.add(new Propertys.Property(Constants.REPORT_TYPE, parseReportName[11]));
        crashReport.mReportType = parseReportName[11];
        crashReport.mCurrentTrigger = z;
        return crashReport;
    }

    public void extractPropertys() {
        extractPropertys(this.mReporterContext);
    }

    public void extractPropertys(ReporterContext reporterContext) {
        this.mPropertys.add(new Propertys.Property(Constants.USERNICK, reporterContext.getPropertyAndSet(Constants.USERNICK)));
        this.mPropertys.add(new Propertys.Property(Constants.BRAND, Build.BOARD));
        this.mPropertys.add(new Propertys.Property(Constants.DEVICE_MODEL, Build.MODEL));
        this.mPropertys.add(new Propertys.Property(Constants.UTDID, reporterContext.getPropertyAndSet(Constants.UTDID)));
        this.mPropertys.add(new Propertys.Property("IMEI", reporterContext.getPropertyAndSet("IMEI")));
        this.mPropertys.add(new Propertys.Property("IMSI", reporterContext.getPropertyAndSet("IMSI")));
        this.mPropertys.add(new Propertys.Property(Constants.DEVICE_ID, reporterContext.getPropertyAndSet(Constants.DEVICE_ID)));
        this.mPropertys.add(new Propertys.Property(Constants.CHANNEL, reporterContext.getProperty(Constants.CHANNEL)));
        this.mPropertys.add(new Propertys.Property(Constants.APP_ID, reporterContext.getProperty(Constants.APP_ID)));
        boolean z = this.mCurrentTrigger;
    }

    public void deleteReportFile() {
        if (this.mReportFile != null) {
            this.mReportFile.delete();
        }
    }

    public String getProperty(String str) {
        return this.mPropertys.getValue(str);
    }

    public String getReportContent() {
        if (StringUtils.isBlank(this.mReportContent)) {
            this.mReportContent = AppUtils.readFully(this.mReportFile);
            try {
                TLogAdapter.log(TAG, this.mReportType, "crash happened last time");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mReportContent;
    }

    public boolean isComplete() {
        if (StringUtils.isBlank(this.mReportContent)) {
            this.mReportContent = getReportContent();
        }
        if (StringUtils.isNotBlank(this.mReportContent)) {
            return this.mReportContent.trim().contains("log end:");
        }
        return false;
    }
}
