package com.taobao.weex.analyzer.core.logcat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LogcatDumper implements Handler.Callback {
    private static final int DEFAULT_CACHE_LIMIT = 1000;
    private static final int MESSAGE_DUMP_LOG = 1;
    private static final int MESSAGE_FILTER_LOG = 2;
    /* access modifiers changed from: private */
    public boolean isCacheEnabled = true;
    private int mCacheLimit;
    private volatile LinkedList<LogInfo> mCachedLogList;
    private DumpLogRunnable mDumpLogRunnable;
    private ExecutorService mExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "wx_analyzer_logcat_dumper");
        }
    });
    /* access modifiers changed from: private */
    public Handler mHandler;
    private int mLevel;
    private OnLogReceivedListener mListener;
    private List<Rule> mRules;

    public interface OnLogReceivedListener {
        void onReceived(@NonNull List<LogInfo> list);
    }

    LogcatDumper(@Nullable OnLogReceivedListener onLogReceivedListener) {
        this.mListener = onLogReceivedListener;
        this.mCacheLimit = 1000;
        this.mRules = new LinkedList();
        this.mCachedLogList = new LinkedList<>();
    }

    public void addRule(@Nullable Rule rule) {
        if (rule != null) {
            this.mRules.add(rule);
        }
    }

    public void setLevel(int i) {
        this.mLevel = i;
    }

    public boolean removeRule(@Nullable Rule rule) {
        if (rule == null) {
            return false;
        }
        return this.mRules.remove(rule);
    }

    public boolean removeRule(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.mRules.remove(new Rule(str, ""));
    }

    public void removeAllRule() {
        this.mRules.clear();
    }

    public void setCacheLimit(int i) {
        if (i > 0) {
            this.mCacheLimit = i;
        }
    }

    public int getCacheLimit() {
        return this.mCacheLimit;
    }

    public void setCacheEnabled(boolean z) {
        this.isCacheEnabled = z;
    }

    public boolean isCacheEnabled() {
        return this.isCacheEnabled;
    }

    public void beginDump() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.mDumpLogRunnable = new DumpLogRunnable(true);
        execute(this.mDumpLogRunnable);
    }

    public void findCachedLogByNewFilters() {
        if (this.mHandler != null && this.isCacheEnabled) {
            execute(new Runnable() {
                public void run() {
                    List access$000 = LogcatDumper.this.filterCachedLog();
                    Message obtain = Message.obtain();
                    obtain.what = 2;
                    obtain.obj = access$000;
                    if (LogcatDumper.this.mHandler != null) {
                        LogcatDumper.this.mHandler.sendMessage(obtain);
                    }
                }
            });
        }
    }

    public synchronized void clearCachedLog() {
        if (this.mCachedLogList != null) {
            this.mCachedLogList.clear();
        }
        execute(new Runnable() {
            public void run() {
                LogcatDumper.this.clearLog();
            }
        });
    }

    @VisibleForTesting
    @Nullable
    public Handler getHandler() {
        return this.mHandler;
    }

    public void destroy() {
        if (this.mDumpLogRunnable != null) {
            this.mDumpLogRunnable.destroy();
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mExecutor != null) {
            this.mExecutor.shutdown();
        }
        this.mHandler = null;
        this.mExecutor = null;
        this.mDumpLogRunnable = null;
        this.mCachedLogList.clear();
        this.mCachedLogList = null;
    }

    /* access modifiers changed from: private */
    public synchronized void cacheLog(@NonNull LogInfo logInfo) {
        if (this.mCachedLogList != null) {
            try {
                if (this.mCachedLogList.size() >= this.mCacheLimit) {
                    this.mCachedLogList.removeFirst();
                }
                this.mCachedLogList.add(logInfo);
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized List<LogInfo> filterCachedLog() {
        if (this.mCachedLogList != null) {
            if (!this.mCachedLogList.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                Iterator it = this.mCachedLogList.iterator();
                while (it.hasNext()) {
                    LogInfo logInfo = (LogInfo) it.next();
                    int i = logInfo.level;
                    String str = logInfo.message;
                    if (checkLevel(i) && checkRule(str)) {
                        arrayList.add(logInfo);
                    }
                }
                return arrayList;
            }
        }
        return Collections.emptyList();
    }

    public boolean handleMessage(Message message) {
        if (this.mListener == null) {
            return false;
        }
        if (message.what == 1) {
            this.mListener.onReceived(Collections.singletonList((LogInfo) message.obj));
        } else if (message.what == 2) {
            this.mListener.onReceived((List) message.obj);
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clearLog() {
        /*
            r5 = this;
            r0 = 0
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x0022 }
            java.lang.String r2 = "logcat -c"
            java.lang.Process r1 = r1.exec(r2)     // Catch:{ Exception -> 0x0022 }
            r2 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r2)     // Catch:{ Exception -> 0x001b, all -> 0x0016 }
            if (r1 == 0) goto L_0x0031
            r1.destroy()
            goto L_0x0031
        L_0x0016:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0032
        L_0x001b:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0023
        L_0x0020:
            r1 = move-exception
            goto L_0x0032
        L_0x0022:
            r1 = move-exception
        L_0x0023:
            java.lang.String r2 = "weex-analyzer"
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x0020 }
            android.util.Log.d(r2, r1)     // Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x0031
            r0.destroy()
        L_0x0031:
            return
        L_0x0032:
            if (r0 == 0) goto L_0x0037
            r0.destroy()
        L_0x0037:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.logcat.LogcatDumper.clearLog():void");
    }

    /* access modifiers changed from: private */
    public boolean checkRule(String str) {
        if (this.mRules.isEmpty()) {
            return true;
        }
        for (Rule accept : this.mRules) {
            if (!accept.accept(str)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean checkLevel(int i) {
        if (this.mLevel == 0 || this.mLevel == 2 || i == this.mLevel) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public int getLevel(@NonNull String str) {
        if (str.length() < 20) {
            return 86;
        }
        switch (str.charAt(19)) {
            case 'D':
                return 3;
            case 'E':
                return 6;
            case 'I':
                return 4;
            case 'V':
                return 2;
            case 'W':
                return 5;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: private */
    public void dumpLog(@NonNull String str, int i) {
        if (this.mHandler != null) {
            try {
                LogInfo logInfo = new LogInfo();
                logInfo.message = str;
                logInfo.level = i;
                Message obtain = Message.obtain();
                obtain.what = 1;
                obtain.obj = logInfo;
                this.mHandler.sendMessage(obtain);
            } catch (Throwable unused) {
            }
        }
    }

    private void execute(Runnable runnable) {
        if (this.mExecutor != null) {
            this.mExecutor.execute(runnable);
        }
    }

    public static class LogInfo {
        public int level;
        public String message;

        public LogInfo() {
        }

        public LogInfo(String str, int i) {
            this.message = str;
            this.level = i;
        }
    }

    private class DumpLogRunnable implements Runnable {
        private boolean isAllowClear;
        private Process logProcess;

        DumpLogRunnable(boolean z) {
            this.isAllowClear = z;
        }

        public void run() {
            try {
                if (this.isAllowClear) {
                    LogcatDumper.this.clearLog();
                }
                this.logProcess = Runtime.getRuntime().exec("logcat -v time");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.logProcess.getInputStream()));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        int access$300 = LogcatDumper.this.getLevel(readLine);
                        if (LogcatDumper.this.checkLevel(access$300) && LogcatDumper.this.checkRule(readLine)) {
                            LogcatDumper.this.dumpLog(readLine, access$300);
                        }
                        if (LogcatDumper.this.isCacheEnabled) {
                            LogcatDumper.this.cacheLog(new LogInfo(readLine, access$300));
                        }
                    } else {
                        return;
                    }
                }
            } catch (IOException e) {
                Log.e("weex-analyzer", e.getMessage());
            }
        }

        /* access modifiers changed from: package-private */
        public void destroy() {
            try {
                if (this.logProcess != null) {
                    this.logProcess.destroy();
                }
            } catch (Exception e) {
                Log.e("weex-analyzer", e.getMessage());
            }
        }
    }

    public static class Rule {
        private String filter;
        private String name;

        public Rule(@Nullable String str, @Nullable String str2) {
            this.name = str;
            this.filter = str2;
        }

        public boolean accept(@NonNull String str) {
            if (TextUtils.isEmpty(this.filter)) {
                return true;
            }
            return str.contains(this.filter);
        }

        public String getName() {
            return this.name;
        }

        public String getFilter() {
            return this.filter;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Rule rule = (Rule) obj;
            if (this.name != null) {
                return this.name.equals(rule.name);
            }
            if (rule.name == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.name != null) {
                return this.name.hashCode();
            }
            return 0;
        }
    }
}
