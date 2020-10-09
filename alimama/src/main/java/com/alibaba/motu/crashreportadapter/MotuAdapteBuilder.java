package com.alibaba.motu.crashreportadapter;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import com.alibaba.motu.crashreportadapter.constants.AdapterConstants;
import com.alibaba.motu.crashreportadapter.module.AdapterBase;
import com.alibaba.motu.crashreportadapter.module.AdapterSendModule;
import com.alibaba.motu.crashreporter.Constants;
import com.alibaba.motu.crashreporter.CrashReport;
import com.alibaba.motu.crashreporter.CrashReporter;
import com.alibaba.motu.crashreporter.Utils;
import com.alibaba.motu.crashreporter.utils.Base64;
import com.alibaba.motu.crashreporter.utils.GZipUtils;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MotuAdapteBuilder {
    public AdapterSendModule build(Context context, AdapterBase adapterBase) {
        if (!(adapterBase instanceof AdapterExceptionModule)) {
            return null;
        }
        AdapterSendModule adapterSendModule = new AdapterSendModule();
        AdapterExceptionModule adapterExceptionModule = (AdapterExceptionModule) adapterBase;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            adapterSendModule.sendContent = Base64.encodeBase64String(GZipUtils.compress(new JavaExceptionReportBuilder(adapterExceptionModule, context, CrashReport.buildReportName(CrashReporter.getInstance().getPropertyAndSet(Constants.UTDID), CrashReporter.getInstance().getProperty("APP_KEY"), CrashReporter.getInstance().getProperty(Constants.APP_VERSION), currentTimeMillis, "catch", "BUSINESS"), currentTimeMillis, "BUSINESS").builder().getBytes()));
            adapterSendModule.aggregationType = String.valueOf(adapterExceptionModule.aggregationType);
            adapterSendModule.businessType = String.valueOf(adapterExceptionModule.businessType);
            adapterSendModule.eventId = AdapterConstants.EVENTID_61005;
            adapterSendModule.sendFlag = "MOTU_REPORTER_SDK_3.0.0_PRIVATE_COMPRESS";
            return adapterSendModule;
        } catch (Exception e) {
            Log.e("MotuCrashAdapter", "base64 and gzip err", e);
            return null;
        }
    }

    public abstract class ReportBuilder {
        Context mContext;
        Map<String, Object> mExtraInfo;
        long mFull;
        long mLimit;
        long mReject;
        String mReportName;
        String mReportType;
        long mTimestamp;
        long mWrite;

        /* access modifiers changed from: protected */
        public abstract String buildContent();

        public ReportBuilder() {
        }

        public String builder() {
            return buildBanner() + buildContent() + buildDone();
        }

        /* access modifiers changed from: protected */
        public String buildDone() {
            return String.format("Full: %d bytes, write: %d bytes, limit: %d bytes, reject: %d bytes.\n", new Object[]{Long.valueOf(this.mFull), Long.valueOf(this.mWrite), Long.valueOf(this.mLimit), Long.valueOf(this.mReject)}) + String.format("log end: %s\n", new Object[]{Utils.getGMT8Time(System.currentTimeMillis())});
        }

        /* access modifiers changed from: protected */
        public String buildEnd() {
            return "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n";
        }

        /* access modifiers changed from: protected */
        public String buildBanner() {
            StringBuilder sb = new StringBuilder();
            sb.append("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\n");
            sb.append(String.format("Basic Information: 'pid: %d/tid: %d/logver: 2/time: %s/cpu: %s/cpu hardware: %s'\n", new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), Long.valueOf(this.mTimestamp), Build.CPU_ABI, Build.HARDWARE}));
            sb.append(String.format("Mobile Information: 'model: %s/version: %s/sdk: %d'\n", new Object[]{Build.MODEL, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT)}));
            sb.append(String.format("Build fingerprint: '" + Build.FINGERPRINT + "'\n", new Object[0]));
            sb.append(String.format("Runtime Information: 'start: %s/maxheap: %s'\n", new Object[]{CrashReporter.getInstance().getProperty(Constants.STARTUP_TIME), Long.valueOf(Runtime.getRuntime().maxMemory())}));
            sb.append(String.format("Application Information: 'version: %s/subversion: %s/buildseq: %s'\n", new Object[]{CrashReporter.getInstance().getProperty(Constants.APP_VERSION), CrashReporter.getInstance().getProperty(Constants.APP_SUBVERSION), CrashReporter.getInstance().getProperty(Constants.APP_BUILD)}));
            sb.append(String.format("%s Information: 'version: %s/nativeseq: %s/javaseq: %s/target: %s'\n", new Object[]{CrashReporter._MAGIC, "1.0.0.0", "160509105620", "", "beta"}));
            sb.append("Report Name: " + this.mReportName + "\n");
            sb.append("UUID: " + UUID.randomUUID().toString().toLowerCase() + "\n");
            sb.append("Log Type: " + this.mReportType + "\n");
            sb.append(buildEnd());
            return sb.toString();
        }

        /* access modifiers changed from: protected */
        public String buildStatus() {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append("meminfo:\n");
                sb.append(StringUtils.defaultString(Utils.getMeminfo(), "") + "\n");
                sb.append(buildEnd());
            } catch (Exception e) {
                Log.e("MotuCrashAdapter", "write meminfo.", e);
            }
            try {
                sb.append("status:\n");
                sb.append(StringUtils.defaultString(Utils.getMyStatus(), "") + "\n");
                sb.append(buildEnd());
            } catch (Exception e2) {
                Log.e("MotuCrashAdapter", "adapter write status.", e2);
            }
            try {
                sb.append("virtual machine:\nMaxMemory: " + Runtime.getRuntime().maxMemory() + " TotalMemory: " + Runtime.getRuntime().totalMemory() + " FreeMemory: " + Runtime.getRuntime().freeMemory() + "\n");
            } catch (Exception e3) {
                Log.e("MotuCrashAdapter", "adapter write virtual machine info.", e3);
            }
            sb.append(buildEnd());
            return sb.toString();
        }

        /* access modifiers changed from: protected */
        public String buildStorageinfo() {
            return "storageinfo:\n" + Utils.dumpStorage(this.mContext) + buildEnd();
        }

        /* access modifiers changed from: protected */
        public String buildApplictionMeminfo() {
            return "appliction meminfo:\n" + Utils.dumpMeminfo(this.mContext) + buildEnd();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0046 A[SYNTHETIC, Splitter:B:14:0x0046] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String buildFileDescriptor() {
            /*
                r9 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                r1 = 0
                r2 = 1024(0x400, float:1.435E-42)
                r3 = 0
                java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x0039 }
                java.lang.String r5 = "/proc/self/fd"
                r4.<init>(r5)     // Catch:{ Exception -> 0x0039 }
                java.io.File[] r4 = r4.listFiles()     // Catch:{ Exception -> 0x0039 }
                if (r4 == 0) goto L_0x0033
                java.lang.String r3 = "opened file count: %d, write limit: %d.\n"
                r5 = 2
                java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0031 }
                int r6 = r4.length     // Catch:{ Exception -> 0x0031 }
                java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x0031 }
                r5[r1] = r6     // Catch:{ Exception -> 0x0031 }
                java.lang.Integer r6 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x0031 }
                r7 = 1
                r5[r7] = r6     // Catch:{ Exception -> 0x0031 }
                java.lang.String r3 = java.lang.String.format(r3, r5)     // Catch:{ Exception -> 0x0031 }
                r0.append(r3)     // Catch:{ Exception -> 0x0031 }
                goto L_0x0044
            L_0x0031:
                r3 = move-exception
                goto L_0x003d
            L_0x0033:
                java.lang.String r3 = "[DEBUG] listFiles failed!\n"
                r0.append(r3)     // Catch:{ Exception -> 0x0031 }
                goto L_0x0044
            L_0x0039:
                r4 = move-exception
                r8 = r4
                r4 = r3
                r3 = r8
            L_0x003d:
                java.lang.String r5 = "MotuCrashAdapter"
                java.lang.String r6 = "print file descriptor."
                android.util.Log.e(r5, r6, r3)
            L_0x0044:
                if (r4 == 0) goto L_0x008b
                int r3 = r4.length     // Catch:{ Exception -> 0x0083 }
                if (r3 < r2) goto L_0x008b
                java.lang.String r2 = "opened files:\n"
                r0.append(r2)     // Catch:{ Exception -> 0x0083 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0083 }
                r2.<init>()     // Catch:{ Exception -> 0x0083 }
                int r3 = r4.length     // Catch:{ Exception -> 0x0073 }
            L_0x0054:
                if (r1 >= r3) goto L_0x007b
                r5 = r4[r1]     // Catch:{ Exception -> 0x0073 }
                java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x0073 }
                r2.append(r6)     // Catch:{ Exception -> 0x0073 }
                java.lang.String r6 = " -> "
                r2.append(r6)     // Catch:{ Exception -> 0x0073 }
                java.lang.String r5 = r5.getCanonicalPath()     // Catch:{ Exception -> 0x0073 }
                r2.append(r5)     // Catch:{ Exception -> 0x0073 }
                java.lang.String r5 = "\n"
                r2.append(r5)     // Catch:{ Exception -> 0x0073 }
                int r1 = r1 + 1
                goto L_0x0054
            L_0x0073:
                r1 = move-exception
                java.lang.String r3 = "MotuCrashAdapter"
                java.lang.String r4 = "print file descriptor."
                android.util.Log.e(r3, r4, r1)     // Catch:{ Exception -> 0x0083 }
            L_0x007b:
                java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x0083 }
                r0.append(r1)     // Catch:{ Exception -> 0x0083 }
                goto L_0x008b
            L_0x0083:
                r1 = move-exception
                java.lang.String r2 = "MotuCrashAdapter"
                java.lang.String r3 = "print file descriptor."
                android.util.Log.e(r2, r3, r1)
            L_0x008b:
                java.lang.String r1 = r9.buildEnd()
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreportadapter.MotuAdapteBuilder.ReportBuilder.buildFileDescriptor():java.lang.String");
        }

        /* access modifiers changed from: protected */
        public String buildLogcat() {
            return buildLogcat((String) null, 500) + buildLogcat(ProtocolConst.KEY_EVENTS, 100);
        }

        private String buildLogcat(String str, int i) {
            Process process;
            Throwable th;
            int i2;
            int i3;
            int i4;
            int i5;
            StringBuilder sb = new StringBuilder();
            ArrayList arrayList = new ArrayList();
            arrayList.add("logcat");
            arrayList.add("-d");
            if (StringUtils.isBlank(str)) {
                sb.append("logcat main: \n");
            } else {
                sb.append("logcat " + str + ": \n");
                arrayList.add("-b");
                arrayList.add(str);
            }
            arrayList.add("-v");
            arrayList.add("threadtime");
            if (i < 0) {
                sb.append("[DEBUG] custom java logcat lines count is 0!\n");
            } else {
                arrayList.add("-t");
                arrayList.add(String.valueOf(i));
                BufferedReader bufferedReader = null;
                try {
                    process = new ProcessBuilder(new String[0]).command(arrayList).redirectErrorStream(true).start();
                } catch (Exception e) {
                    Log.e("MotuCrashAdapter", "exec logcat", e);
                    process = null;
                }
                if (process == null) {
                    sb.append("[DEBUG] exec logcat failed!\n");
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
                                    sb.append(readLine + "\n");
                                    i3++;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                int i6 = i3;
                                i5 = i2;
                                bufferedReader = bufferedReader2;
                                i4 = i6;
                                try {
                                    Log.e("MotuCrashAdapter", "print log.", e);
                                    Utils.closeQuietly(bufferedReader);
                                    i2 = i5;
                                    i3 = i4;
                                    sb.append(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                                    sb.append(buildEnd());
                                    return sb.toString();
                                } catch (Throwable th2) {
                                    bufferedReader2 = bufferedReader;
                                    th = th2;
                                    Utils.closeQuietly(bufferedReader2);
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                Utils.closeQuietly(bufferedReader2);
                                throw th;
                            }
                        }
                        Utils.closeQuietly(bufferedReader2);
                    } catch (Exception e3) {
                        e = e3;
                        i5 = 0;
                        i4 = 0;
                        Log.e("MotuCrashAdapter", "print log.", e);
                        Utils.closeQuietly(bufferedReader);
                        i2 = i5;
                        i3 = i4;
                        sb.append(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                        sb.append(buildEnd());
                        return sb.toString();
                    }
                    sb.append(String.format("[DEBUG] Read %d lines, wrote %d lines.\n", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}));
                }
            }
            sb.append(buildEnd());
            return sb.toString();
        }

        /* access modifiers changed from: protected */
        public String buildExtraInfo() {
            StringBuilder sb = new StringBuilder();
            if (this.mExtraInfo != null && !this.mExtraInfo.isEmpty()) {
                try {
                    sb.append("extrainfo:\n");
                    for (String next : this.mExtraInfo.keySet()) {
                        sb.append(String.format("%s: %s\n", new Object[]{next, this.mExtraInfo.get(next)}));
                    }
                } catch (Exception e) {
                    Log.e("MotuCrashAdapter", "write extral info", e);
                }
                sb.append(buildEnd());
            }
            return sb.toString();
        }
    }

    final class JavaExceptionReportBuilder extends ReportBuilder {
        String mExceptionContent;
        AdapterExceptionModule mExceptionModule;
        Thread mThread;
        Throwable mThrowable;

        JavaExceptionReportBuilder(AdapterExceptionModule adapterExceptionModule, Context context, String str, long j, String str2) {
            super();
            this.mExceptionModule = adapterExceptionModule;
            this.mThrowable = adapterExceptionModule.throwable;
            this.mThread = adapterExceptionModule.thread;
            this.mExceptionContent = adapterExceptionModule.exceptionDetail;
            if (this.mExtraInfo == null) {
                this.mExtraInfo = new HashMap();
            }
            String str3 = adapterExceptionModule.exceptionId;
            if (str3 != null) {
                this.mExtraInfo.put("exceptionId", str3);
            }
            String str4 = adapterExceptionModule.exceptionCode;
            if (str4 != null) {
                this.mExtraInfo.put("exceptionCode", str4);
            }
            String str5 = adapterExceptionModule.exceptionVersion;
            if (str5 != null) {
                this.mExtraInfo.put("exceptionVersion", str5);
            }
            String str6 = adapterExceptionModule.exceptionArg1;
            if (str6 != null) {
                this.mExtraInfo.put("exceptionArg1", str6);
            }
            String str7 = adapterExceptionModule.exceptionArg2;
            if (str7 != null) {
                this.mExtraInfo.put("exceptionArg2", str7);
            }
            String str8 = adapterExceptionModule.exceptionArg3;
            if (str8 != null) {
                this.mExtraInfo.put("exceptionArg3", str8);
            }
            Map<String, Object> map = adapterExceptionModule.exceptionArgs;
            if (map != null && map.size() > 0) {
                this.mExtraInfo.putAll(map);
            }
            this.mContext = context;
            this.mReportName = str;
            this.mTimestamp = j;
            this.mReportType = str2;
        }

        /* access modifiers changed from: protected */
        public String buildContent() {
            StringBuilder sb = new StringBuilder();
            sb.append(buildThrowable());
            sb.append(buildExtraInfo());
            sb.append(buildStatus());
            sb.append(buildStorageinfo());
            sb.append(buildFileDescriptor());
            if (this.mThrowable instanceof OutOfMemoryError) {
                sb.append(buildApplictionMeminfo());
            }
            sb.append(buildLogcat());
            return sb.toString();
        }

        /* JADX WARNING: Removed duplicated region for block: B:40:0x00b3 A[SYNTHETIC, Splitter:B:40:0x00b3] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.String buildThrowable() {
            /*
                r7 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "Process Name: '%s' \n"
                r2 = 1
                java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x00a6 }
                com.alibaba.motu.crashreporter.CrashReporter r4 = com.alibaba.motu.crashreporter.CrashReporter.getInstance()     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r5 = "PROCESS_NAME"
                java.lang.String r4 = r4.getProperty(r5)     // Catch:{ Exception -> 0x00a6 }
                r5 = 0
                r3[r5] = r4     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = java.lang.String.format(r1, r3)     // Catch:{ Exception -> 0x00a6 }
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
                java.lang.Thread r1 = r7.mThread     // Catch:{ Exception -> 0x00a6 }
                if (r1 == 0) goto L_0x0036
                java.lang.String r1 = "Thread Name: '%s' \n"
                java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x00a6 }
                java.lang.Thread r3 = r7.mThread     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r3 = r3.getName()     // Catch:{ Exception -> 0x00a6 }
                r2[r5] = r3     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = java.lang.String.format(r1, r2)     // Catch:{ Exception -> 0x00a6 }
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
                goto L_0x0045
            L_0x0036:
                java.lang.String r1 = "Thread Name: '%s' \n"
                java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r3 = "adapter no thread name"
                r2[r5] = r3     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = java.lang.String.format(r1, r2)     // Catch:{ Exception -> 0x00a6 }
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
            L_0x0045:
                java.lang.String r1 = "Back traces starts.\n"
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
                r1 = 0
                java.lang.Throwable r2 = r7.mThrowable     // Catch:{ Exception -> 0x008c }
                if (r2 == 0) goto L_0x0071
                java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x008c }
                r2.<init>()     // Catch:{ Exception -> 0x008c }
                java.lang.Throwable r1 = r7.mThrowable     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                java.io.PrintStream r3 = new java.io.PrintStream     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                r3.<init>(r2)     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                r1.printStackTrace(r3)     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                r0.append(r1)     // Catch:{ Exception -> 0x006c, all -> 0x0067 }
                r1 = r2
                goto L_0x0086
            L_0x0067:
                r1 = move-exception
                r6 = r2
                r2 = r1
                r1 = r6
                goto L_0x00a2
            L_0x006c:
                r1 = move-exception
                r6 = r2
                r2 = r1
                r1 = r6
                goto L_0x008d
            L_0x0071:
                java.lang.String r2 = r7.mExceptionContent     // Catch:{ Exception -> 0x008c }
                if (r2 == 0) goto L_0x0080
                java.lang.String r2 = r7.mExceptionContent     // Catch:{ Exception -> 0x008c }
                r0.append(r2)     // Catch:{ Exception -> 0x008c }
                java.lang.String r2 = "\n"
                r0.append(r2)     // Catch:{ Exception -> 0x008c }
                goto L_0x0086
            L_0x0080:
                java.lang.String r2 = "无内容"
                r0.append(r2)     // Catch:{ Exception -> 0x008c }
            L_0x0086:
                com.alibaba.motu.crashreporter.Utils.closeQuietly(r1)     // Catch:{ Exception -> 0x00a6 }
                goto L_0x0095
            L_0x008a:
                r2 = move-exception
                goto L_0x00a2
            L_0x008c:
                r2 = move-exception
            L_0x008d:
                java.lang.String r3 = "MotuCrashAdapter"
                java.lang.String r4 = "print throwable"
                android.util.Log.e(r3, r4, r2)     // Catch:{ all -> 0x008a }
                goto L_0x0086
            L_0x0095:
                java.lang.String r1 = "Back traces end.\n"
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
                java.lang.String r1 = r7.buildEnd()     // Catch:{ Exception -> 0x00a6 }
                r0.append(r1)     // Catch:{ Exception -> 0x00a6 }
                goto L_0x00af
            L_0x00a2:
                com.alibaba.motu.crashreporter.Utils.closeQuietly(r1)     // Catch:{ Exception -> 0x00a6 }
                throw r2     // Catch:{ Exception -> 0x00a6 }
            L_0x00a6:
                r1 = move-exception
                java.lang.String r2 = "MotuCrashAdapter"
                java.lang.String r3 = "write throwable"
                android.util.Log.e(r2, r3, r1)
            L_0x00af:
                java.lang.Thread r1 = r7.mThread
                if (r1 == 0) goto L_0x00c6
                java.lang.Thread r1 = r7.mThread     // Catch:{ Exception -> 0x00bd }
                java.lang.String r1 = r7.buildThread(r1)     // Catch:{ Exception -> 0x00bd }
                r0.append(r1)     // Catch:{ Exception -> 0x00bd }
                goto L_0x00c6
            L_0x00bd:
                r1 = move-exception
                java.lang.String r2 = "MotuCrashAdapter"
                java.lang.String r3 = "write thread"
                android.util.Log.e(r2, r3, r1)
            L_0x00c6:
                java.lang.String r1 = r7.buildEnd()
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreportadapter.MotuAdapteBuilder.JavaExceptionReportBuilder.buildThrowable():java.lang.String");
        }

        /* access modifiers changed from: protected */
        public String buildThread(Thread thread) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(String.format("Thread Name: '%s'\n", new Object[]{thread.getName()}));
                sb.append(String.format("\"%s\" prio=%d tid=%d %s\n", new Object[]{thread.getName(), Integer.valueOf(thread.getPriority()), Long.valueOf(thread.getId()), thread.getState()}));
                StackTraceElement[] stackTrace = thread.getStackTrace();
                int length = stackTrace.length;
                for (int i = 0; i < length; i++) {
                    sb.append(String.format("\tat %s\n", new Object[]{stackTrace[i].toString()}));
                }
            } catch (Exception e) {
                Log.e("MotuCrashAdapter", "dumpThread", e);
            }
            return sb.toString();
        }
    }
}
