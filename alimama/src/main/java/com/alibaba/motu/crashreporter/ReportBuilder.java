package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import com.alibaba.motu.crashreporter.CatcherManager;
import com.alibaba.motu.tbrest.utils.AppUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ReportBuilder {
    Configuration mConfiguration;
    Context mContext;
    ReporterContext mReporterContext;
    StorageManager mStorageManager;

    public ReportBuilder(Context context, ReporterContext reporterContext, Configuration configuration, StorageManager storageManager) {
        this.mContext = context;
        this.mReporterContext = reporterContext;
        this.mConfiguration = configuration;
        this.mStorageManager = storageManager;
    }

    public CrashReport buildUncaughtExceptionReport(Throwable th, Thread thread, Map<String, Object> map) {
        clearCrashRepoterFile();
        long currentTimeMillis = System.currentTimeMillis();
        String str = "catch";
        if ("true".equals(map.get(Constants.REPORT_IGNORE))) {
            str = ABConstants.Operator.NAV_LOOPBACK_VALUE_IGNORE;
        }
        String buildReportName = CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty("APP_KEY"), this.mReporterContext.getProperty(Constants.APP_VERSION), currentTimeMillis, str, "java");
        File processTombstoneFile = this.mStorageManager.getProcessTombstoneFile(buildReportName);
        new UncaughtExceptionReportPrintWrite(this, this.mContext, this.mReporterContext, this.mConfiguration, buildReportName, currentTimeMillis, processTombstoneFile, th, thread, map).print();
        return CrashReport.buildCrashReport(this.mContext, processTombstoneFile, this.mReporterContext, true);
    }

    public CrashReport buildNativeExceptionReport(File file, Map<String, String> map) {
        clearCrashRepoterFile();
        File processTombstoneFile = this.mStorageManager.getProcessTombstoneFile(CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty("APP_KEY"), this.mReporterContext.getProperty(Constants.APP_VERSION), System.currentTimeMillis(), "scan", "native"));
        file.renameTo(processTombstoneFile);
        return CrashReport.buildCrashReport(this.mContext, processTombstoneFile, this.mReporterContext, false);
    }

    public CrashReport buildANRReport(CatcherManager.ANRCatcher.TracesFinder tracesFinder, Map<String, String> map) {
        clearCrashRepoterFile();
        long currentTimeMillis = System.currentTimeMillis();
        String buildReportName = CrashReport.buildReportName(this.mReporterContext.getPropertyAndSet(Constants.UTDID), this.mReporterContext.getProperty("APP_KEY"), this.mReporterContext.getProperty(Constants.APP_VERSION), currentTimeMillis, "scan", CrashReport.TYPE_ANR);
        File processTombstoneFile = this.mStorageManager.getProcessTombstoneFile(buildReportName);
        new ANRReportPrintWrite(this, this.mContext, this.mReporterContext, this.mConfiguration, buildReportName, currentTimeMillis, processTombstoneFile, tracesFinder).print();
        return CrashReport.buildCrashReport(this.mContext, processTombstoneFile, this.mReporterContext, false);
    }

    public CrashReport[] listProcessCrashReport() {
        File[] listProcessCrashReportFile = listProcessCrashReportFile();
        if (listProcessCrashReportFile == null || listProcessCrashReportFile.length <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (File buildCrashReport : listProcessCrashReportFile) {
            arrayList.add(CrashReport.buildCrashReport(this.mContext, buildCrashReport, this.mReporterContext, false));
        }
        return (CrashReport[]) arrayList.toArray(new CrashReport[0]);
    }

    private File[] listProcessCrashReportFile() {
        return this.mStorageManager.listProcessTombstoneFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith("java.log") || file.getName().endsWith("native.log") || file.getName().endsWith("anr.log");
            }
        });
    }

    public void clearCrashRepoterFile() {
        try {
            File[] listProcessCrashReportFile = listProcessCrashReportFile();
            if (listProcessCrashReportFile != null && listProcessCrashReportFile.length > 20) {
                List<File> asList = Arrays.asList(listProcessCrashReportFile);
                Collections.sort(asList, new Comparator<File>() {
                    public int compare(File file, File file2) {
                        if (file.lastModified() > file2.lastModified()) {
                            return -1;
                        }
                        return file.lastModified() == file2.lastModified() ? 0 : 1;
                    }
                });
                for (File file : asList) {
                }
            }
        } catch (Exception e) {
            LogUtil.e("clear crashReport file", e);
        }
    }

    public abstract class ReportPrintWrite {
        Configuration mConfiguration;
        Context mContext;
        Map<String, Object> mExtraInfo;
        long mFull;
        long mLimit;
        OutputStream mOutputStream;
        long mReject;
        String mReportName;
        String mReportType;
        ReporterContext mReporterContext;
        long mTimestamp;
        long mWrite;

        /* access modifiers changed from: protected */
        public abstract void printContent();

        public ReportPrintWrite() {
        }

        public void print() {
            printBanner();
            printContent();
            printDone();
        }

        /* access modifiers changed from: protected */
        public void close() {
            AppUtils.closeQuietly(this.mOutputStream);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x001c */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void write(java.lang.String r6) {
            /*
                r5 = this;
                r0 = 0
                byte[] r0 = new byte[r0]
                java.lang.String r1 = "UTF-8"
                byte[] r1 = r6.getBytes(r1)     // Catch:{ Exception -> 0x000b }
                r0 = r1
                goto L_0x0012
            L_0x000b:
                r1 = move-exception
                java.lang.String r2 = "write."
                com.alibaba.motu.crashreporter.LogUtil.e(r2, r1)
            L_0x0012:
                long r1 = r5.mFull
                int r3 = r0.length
                long r3 = (long) r3
                long r1 = r1 + r3
                r5.mFull = r1
                com.alibaba.motu.crashreporter.LogUtil.i(r6)     // Catch:{ Exception -> 0x001c }
            L_0x001c:
                java.io.OutputStream r1 = r5.mOutputStream     // Catch:{ Exception -> 0x0034 }
                java.lang.String r2 = "UTF-8"
                byte[] r6 = r6.getBytes(r2)     // Catch:{ Exception -> 0x0034 }
                r1.write(r6)     // Catch:{ Exception -> 0x0034 }
                long r1 = r5.mWrite     // Catch:{ Exception -> 0x0034 }
                int r6 = r0.length     // Catch:{ Exception -> 0x0034 }
                long r3 = (long) r6     // Catch:{ Exception -> 0x0034 }
                long r1 = r1 + r3
                r5.mWrite = r1     // Catch:{ Exception -> 0x0034 }
                java.io.OutputStream r6 = r5.mOutputStream     // Catch:{ Exception -> 0x0034 }
                r6.flush()     // Catch:{ Exception -> 0x0034 }
                goto L_0x003b
            L_0x0034:
                r6 = move-exception
                java.lang.String r0 = "write."
                com.alibaba.motu.crashreporter.LogUtil.e(r0, r6)
            L_0x003b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ReportBuilder.ReportPrintWrite.write(java.lang.String):void");
        }

        /* access modifiers changed from: protected */
        public void printDone() {
            write(String.format("Full: %d bytes, write: %d bytes, limit: %d bytes, reject: %d bytes.\n", new Object[]{Long.valueOf(this.mFull), Long.valueOf(this.mWrite), Long.valueOf(this.mLimit), Long.valueOf(this.mReject)}));
            write(String.format("log end: %s\n", new Object[]{AppUtils.getGMT8Time(System.currentTimeMillis())}));
        }

        /* access modifiers changed from: protected */
        public void printEnd() {
            write("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n");
        }

        /* access modifiers changed from: protected */
        public void printBanner() {
            write("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\n");
            write(String.format("Basic Information: 'pid: %d/tid: %d/logver: 2/time: %s/cpu: %s/cpu hardware: %s'\n", new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Long.valueOf(this.mTimestamp), Build.CPU_ABI, Build.HARDWARE}));
            write(String.format("Mobile Information: 'model: %s/version: %s/sdk: %d'\n", new Object[]{Build.MODEL, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT)}));
            write(String.format("Build fingerprint: '" + Build.FINGERPRINT + "'\n", new Object[0]));
            write(String.format("Runtime Information: 'start: %s/maxheap: %s'\n", new Object[]{this.mReporterContext.getProperty(Constants.STARTUP_TIME), Long.valueOf(Runtime.getRuntime().maxMemory())}));
            write(String.format("Application Information: 'version: %s/subversion: %s/buildseq: %s'\n", new Object[]{this.mReporterContext.getProperty(Constants.APP_VERSION), this.mReporterContext.getProperty(Constants.APP_SUBVERSION), this.mReporterContext.getProperty(Constants.APP_BUILD)}));
            write(String.format("%s Information: 'version: %s/nativeseq: %s/javaseq: %s/target: %s'\n", new Object[]{CrashReporter._MAGIC, "1.0.0.0", "160509105620", "", "beta"}));
            write("Report Name: " + this.mReportName + "\n");
            write("UUID: " + UUID.randomUUID().toString().toLowerCase() + "\n");
            write("Log Type: " + this.mReportType + "\n");
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printStatus() {
            try {
                write("meminfo:\n");
                write(StringUtils.defaultString(AppUtils.getMeminfo(), "") + "\n");
                printEnd();
            } catch (Exception e) {
                LogUtil.e("write meminfo.", e);
            }
            try {
                write("status:\n");
                write(StringUtils.defaultString(AppUtils.getMyStatus(), "") + "\n");
                printEnd();
            } catch (Exception e2) {
                LogUtil.e("write status.", e2);
            }
            try {
                write("virtual machine:\nMaxMemory: " + Runtime.getRuntime().maxMemory() + " TotalMemory: " + Runtime.getRuntime().totalMemory() + " FreeMemory: " + Runtime.getRuntime().freeMemory() + "\n");
            } catch (Exception e3) {
                LogUtil.e("write virtual machine info.", e3);
            }
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printStorageinfo() {
            write("storageinfo:\n");
            write(AppUtils.dumpStorage(this.mContext));
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printApplictionMeminfo() {
            write("appliction meminfo:\n");
            write(AppUtils.dumpMeminfo(this.mContext));
            printEnd();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0047 A[SYNTHETIC, Splitter:B:14:0x0047] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void printFileDescriptor() {
            /*
                r8 = this;
                com.alibaba.motu.crashreporter.Configuration r0 = r8.mConfiguration
                java.lang.String r1 = "Configuration.fileDescriptorLimit"
                r2 = 900(0x384, float:1.261E-42)
                int r0 = r0.getInt(r1, r2)
                r1 = 0
                r2 = 0
                java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x003c }
                java.lang.String r4 = "/proc/self/fd"
                r3.<init>(r4)     // Catch:{ Exception -> 0x003c }
                java.io.File[] r3 = r3.listFiles()     // Catch:{ Exception -> 0x003c }
                if (r3 == 0) goto L_0x0036
                java.lang.String r2 = "opened file count: %d, write limit: %d.\n"
                r4 = 2
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0034 }
                int r5 = r3.length     // Catch:{ Exception -> 0x0034 }
                java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x0034 }
                r4[r1] = r5     // Catch:{ Exception -> 0x0034 }
                java.lang.Integer r5 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x0034 }
                r6 = 1
                r4[r6] = r5     // Catch:{ Exception -> 0x0034 }
                java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ Exception -> 0x0034 }
                r8.write(r2)     // Catch:{ Exception -> 0x0034 }
                goto L_0x0045
            L_0x0034:
                r2 = move-exception
                goto L_0x0040
            L_0x0036:
                java.lang.String r2 = "[DEBUG] listFiles failed!\n"
                r8.write(r2)     // Catch:{ Exception -> 0x0034 }
                goto L_0x0045
            L_0x003c:
                r3 = move-exception
                r7 = r3
                r3 = r2
                r2 = r7
            L_0x0040:
                java.lang.String r4 = "print file descriptor."
                com.alibaba.motu.crashreporter.LogUtil.e(r4, r2)
            L_0x0045:
                if (r3 == 0) goto L_0x0088
                int r2 = r3.length     // Catch:{ Exception -> 0x0082 }
                if (r2 < r0) goto L_0x0088
                java.lang.String r0 = "opened files:\n"
                r8.write(r0)     // Catch:{ Exception -> 0x0082 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0082 }
                r0.<init>()     // Catch:{ Exception -> 0x0082 }
                int r2 = r3.length     // Catch:{ Exception -> 0x0074 }
            L_0x0055:
                if (r1 >= r2) goto L_0x007a
                r4 = r3[r1]     // Catch:{ Exception -> 0x0074 }
                java.lang.String r5 = r4.getName()     // Catch:{ Exception -> 0x0074 }
                r0.append(r5)     // Catch:{ Exception -> 0x0074 }
                java.lang.String r5 = " -> "
                r0.append(r5)     // Catch:{ Exception -> 0x0074 }
                java.lang.String r4 = r4.getCanonicalPath()     // Catch:{ Exception -> 0x0074 }
                r0.append(r4)     // Catch:{ Exception -> 0x0074 }
                java.lang.String r4 = "\n"
                r0.append(r4)     // Catch:{ Exception -> 0x0074 }
                int r1 = r1 + 1
                goto L_0x0055
            L_0x0074:
                r1 = move-exception
                java.lang.String r2 = "print file descriptor."
                com.alibaba.motu.crashreporter.LogUtil.e(r2, r1)     // Catch:{ Exception -> 0x0082 }
            L_0x007a:
                java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0082 }
                r8.write(r0)     // Catch:{ Exception -> 0x0082 }
                goto L_0x0088
            L_0x0082:
                r0 = move-exception
                java.lang.String r1 = "print file descriptor."
                com.alibaba.motu.crashreporter.LogUtil.e(r1, r0)
            L_0x0088:
                r8.printEnd()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ReportBuilder.ReportPrintWrite.printFileDescriptor():void");
        }

        /* access modifiers changed from: protected */
        public void printLogcat() {
            int i = this.mConfiguration.getInt(Configuration.mainLogLineLimit, 2000);
            int i2 = this.mConfiguration.getInt(Configuration.eventsLogLineLimit, 200);
            printLogcat((String) null, i);
            printLogcat(ProtocolConst.KEY_EVENTS, i2);
        }

        private void printLogcat(String str, int i) {
            Process process;
            Throwable th;
            int i2;
            int i3;
            int i4;
            ArrayList arrayList = new ArrayList();
            arrayList.add("logcat");
            arrayList.add("-d");
            if (StringUtils.isBlank(str)) {
                write("logcat main: \n");
            } else {
                write("logcat " + str + ": \n");
                arrayList.add("-b");
                arrayList.add(str);
            }
            arrayList.add("-v");
            arrayList.add("threadtime");
            if (i < 0) {
                write("[DEBUG] custom java logcat lines count is 0!\n");
            } else {
                arrayList.add("-t");
                arrayList.add(String.valueOf(i));
                BufferedReader bufferedReader = null;
                try {
                    process = new ProcessBuilder(new String[0]).command(arrayList).redirectErrorStream(true).start();
                } catch (Exception e) {
                    LogUtil.e("exec logcat", e);
                    process = null;
                }
                if (process == null) {
                    write("[DEBUG] exec logcat failed!\n");
                } else {
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
                        i2 = 0;
                        i3 = 0;
                        while (true) {
                            try {
                                String readLine = bufferedReader2.readLine();
                                if (readLine == null) {
                                    break;
                                }
                                i2++;
                                if (i3 < i) {
                                    write(readLine + "\n");
                                    i3++;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                BufferedReader bufferedReader3 = bufferedReader2;
                                i4 = i2;
                                bufferedReader = bufferedReader3;
                                try {
                                    LogUtil.e("print log.", e);
                                    AppUtils.closeQuietly(bufferedReader);
                                    i2 = i4;
                                    write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                                    printEnd();
                                } catch (Throwable th2) {
                                    bufferedReader2 = bufferedReader;
                                    th = th2;
                                    AppUtils.closeQuietly(bufferedReader2);
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                AppUtils.closeQuietly(bufferedReader2);
                                throw th;
                            }
                        }
                        AppUtils.closeQuietly(bufferedReader2);
                    } catch (Exception e3) {
                        e = e3;
                        i3 = 0;
                        i4 = 0;
                        LogUtil.e("print log.", e);
                        AppUtils.closeQuietly(bufferedReader);
                        i2 = i4;
                        write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                        printEnd();
                    }
                    write(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                }
            }
            printEnd();
        }

        /* access modifiers changed from: protected */
        public void printExtraInfo() {
            if (this.mExtraInfo != null && !this.mExtraInfo.isEmpty()) {
                try {
                    write("extrainfo:\n");
                    for (String next : this.mExtraInfo.keySet()) {
                        write(String.format("%s: %s\n", new Object[]{next, this.mExtraInfo.get(next)}));
                    }
                } catch (Exception e) {
                    LogUtil.e("write extral info", e);
                }
                printEnd();
            }
        }
    }

    public abstract class FileReportPrintWrite extends ReportPrintWrite {
        File mReportFile;

        public FileReportPrintWrite(Context context, ReporterContext reporterContext, Configuration configuration, String str, String str2, long j, File file, Map<String, Object> map) {
            super();
            this.mContext = context;
            this.mReporterContext = reporterContext;
            this.mConfiguration = configuration;
            this.mReportName = str;
            this.mReportType = str2;
            this.mTimestamp = j;
            this.mReportFile = file;
            this.mExtraInfo = map;
            if (file.exists()) {
                file.delete();
            }
            try {
                this.mOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                LogUtil.e("create fileOutputStream.", e);
            }
        }
    }

    final class UncaughtExceptionReportPrintWrite extends FileReportPrintWrite {
        Thread mThread;
        Throwable mThrowable;
        final /* synthetic */ ReportBuilder this$0;

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        UncaughtExceptionReportPrintWrite(com.alibaba.motu.crashreporter.ReportBuilder r13, android.content.Context r14, com.alibaba.motu.crashreporter.ReporterContext r15, com.alibaba.motu.crashreporter.Configuration r16, java.lang.String r17, long r18, java.io.File r20, java.lang.Throwable r21, java.lang.Thread r22, java.util.Map<java.lang.String, java.lang.Object> r23) {
            /*
                r12 = this;
                r11 = r12
                r1 = r13
                r11.this$0 = r1
                java.lang.String r6 = "java"
                r0 = r12
                r2 = r14
                r3 = r15
                r4 = r16
                r5 = r17
                r7 = r18
                r9 = r20
                r10 = r23
                r0.<init>(r2, r3, r4, r5, r6, r7, r9, r10)
                r0 = r21
                r11.mThrowable = r0
                r0 = r22
                r11.mThread = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ReportBuilder.UncaughtExceptionReportPrintWrite.<init>(com.alibaba.motu.crashreporter.ReportBuilder, android.content.Context, com.alibaba.motu.crashreporter.ReporterContext, com.alibaba.motu.crashreporter.Configuration, java.lang.String, long, java.io.File, java.lang.Throwable, java.lang.Thread, java.util.Map):void");
        }

        /* access modifiers changed from: protected */
        public void printContent() {
            printThrowable();
            printExtraInfo();
            printStatus();
            printStorageinfo();
            printFileDescriptor();
            if (this.mThrowable instanceof OutOfMemoryError) {
                printApplictionMeminfo();
            }
            printLogcat();
        }

        private void printThrowable() {
            ByteArrayOutputStream byteArrayOutputStream;
            Exception e;
            try {
                write(String.format("Process Name: '%s' \n", new Object[]{this.mReporterContext.getProperty("PROCESS_NAME")}));
                write(String.format("Thread Name: '%s' \n", new Object[]{this.mThread.getName()}));
                write("Back traces starts.\n");
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        this.mThrowable.printStackTrace(new PrintStream(byteArrayOutputStream));
                        write(byteArrayOutputStream.toString());
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            LogUtil.e("print throwable", e);
                            AppUtils.closeQuietly(byteArrayOutputStream);
                            write("Back traces end.\n");
                            printEnd();
                            write(AppUtils.dumpThread(this.mThread));
                            printEnd();
                        } catch (Throwable th) {
                            th = th;
                        }
                    }
                } catch (Exception e3) {
                    Exception exc = e3;
                    byteArrayOutputStream = null;
                    e = exc;
                    LogUtil.e("print throwable", e);
                    AppUtils.closeQuietly(byteArrayOutputStream);
                    write("Back traces end.\n");
                    printEnd();
                    write(AppUtils.dumpThread(this.mThread));
                    printEnd();
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    byteArrayOutputStream = null;
                    th = th3;
                    AppUtils.closeQuietly(byteArrayOutputStream);
                    throw th;
                }
                AppUtils.closeQuietly(byteArrayOutputStream);
                write("Back traces end.\n");
                printEnd();
            } catch (Exception e4) {
                LogUtil.e("write throwable", e4);
            }
            try {
                write(AppUtils.dumpThread(this.mThread));
            } catch (Exception e5) {
                LogUtil.e("write thread", e5);
            }
            printEnd();
        }
    }

    final class ANRReportPrintWrite extends FileReportPrintWrite {
        CatcherManager.ANRCatcher.TracesFinder mTracesFinder;
        final /* synthetic */ ReportBuilder this$0;

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        ANRReportPrintWrite(com.alibaba.motu.crashreporter.ReportBuilder r13, android.content.Context r14, com.alibaba.motu.crashreporter.ReporterContext r15, com.alibaba.motu.crashreporter.Configuration r16, java.lang.String r17, long r18, java.io.File r20, com.alibaba.motu.crashreporter.CatcherManager.ANRCatcher.TracesFinder r21) {
            /*
                r12 = this;
                r11 = r12
                r1 = r13
                r11.this$0 = r1
                java.lang.String r6 = "anr"
                r10 = 0
                r0 = r12
                r2 = r14
                r3 = r15
                r4 = r16
                r5 = r17
                r7 = r18
                r9 = r20
                r0.<init>(r2, r3, r4, r5, r6, r7, r9, r10)
                r0 = r21
                r11.mTracesFinder = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ReportBuilder.ANRReportPrintWrite.<init>(com.alibaba.motu.crashreporter.ReportBuilder, android.content.Context, com.alibaba.motu.crashreporter.ReporterContext, com.alibaba.motu.crashreporter.Configuration, java.lang.String, long, java.io.File, com.alibaba.motu.crashreporter.CatcherManager$ANRCatcher$TracesFinder):void");
        }

        /* access modifiers changed from: protected */
        public void printContent() {
            printTraces();
        }

        private void printTraces() {
            BufferedReader bufferedReader;
            IOException e;
            try {
                write("traces starts.\n");
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.mTracesFinder.mSystemTraceFile)));
                    int i = 0;
                    boolean z = false;
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine != null) {
                                i++;
                                if (!this.mTracesFinder.strStartFlag.equals(readLine)) {
                                    z = true;
                                }
                                if (!z) {
                                    if (i > 5) {
                                        break;
                                    }
                                } else {
                                    write(readLine + "\n");
                                }
                            }
                            
                        }
