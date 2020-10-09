package com.taobao.tao.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TLogController implements ITLogController {
    private static final String TAG = "TLogController";
    private LogLevel logLevel;
    private Map<String, LogLevel> moduleFilter;

    public boolean checkLogLength(String str) {
        return false;
    }

    public String compress(String str) {
        return null;
    }

    public void destroyLog(boolean z) {
    }

    public boolean isOpenLog() {
        return true;
    }

    public void openLog(boolean z) {
    }

    @Deprecated
    public void setEndTime(long j) {
    }

    private TLogController() {
        this.logLevel = LogLevel.E;
        this.moduleFilter = new ConcurrentHashMap();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final TLogController INSTANCE = new TLogController();

        private SingletonHolder() {
        }
    }

    public static final TLogController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addModuleFilter(Map<String, LogLevel> map) {
        if (map != null && map.size() > 0) {
            for (String next : map.keySet()) {
                getInstance().addModuleFilter(next, map.get(next));
            }
        }
    }

    public void addModuleFilter(String str, LogLevel logLevel2) {
        this.moduleFilter.put(str, logLevel2);
        if (TLogInitializer.getInstance().getInitState() == 2 && TLogNative.isSoOpen()) {
            TLogNative.addModuleFilter(str, logLevel2.getIndex());
        }
    }

    public void cleanModuleFilter() {
        this.moduleFilter.clear();
    }

    public void setLogLevel(LogLevel logLevel2) {
        this.logLevel = logLevel2;
        if (TLogInitializer.getInstance().getInitState() == 2 && TLogNative.isSoOpen()) {
            TLogNative.setLogLevel(logLevel2.getIndex());
        }
    }

    public void closeLog() {
        if (TLogInitializer.getInstance().getInitState() == 2 && TLogNative.isSoOpen()) {
            TLogNative.setLogLevel(LogLevel.L.getIndex());
            TLogNative.appenderClose();
        }
    }

    public void resumeLog(Context context, LogLevel logLevel2) {
        if (TLogInitializer.getInstance().getInitState() == 2) {
            try {
                String str = context.getFilesDir().getAbsolutePath() + File.separator + TLogInitializer.DEFAULT_DIR;
                File file = TLogInitializer.getInstance().logDir;
                String appkey = TLogInitializer.getInstance().getAppkey();
                String nameprefix = TLogInitializer.getInstance().getNameprefix();
                Log.e(TAG, "resumeLog. logLevel:" + logLevel2 + " logDir:" + file.getAbsolutePath() + " namePrefix:" + nameprefix + " appKey:" + appkey + " cacheDir:" + str);
                TLogNative.appenderOpen(logLevel2.getIndex(), str, file.getAbsolutePath(), nameprefix, appkey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateLogLevel(LogLevel logLevel2) {
        this.logLevel = logLevel2;
    }

    /* access modifiers changed from: protected */
    public void updateAsyncConfig() {
        if (this.moduleFilter != null && this.logLevel != null && TLogNative.isSoOpen()) {
            try {
                for (Map.Entry next : this.moduleFilter.entrySet()) {
                    TLogNative.addModuleFilter((String) next.getKey(), ((LogLevel) next.getValue()).getIndex());
                }
                TLogNative.setLogLevel(this.logLevel.getIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFilter(LogLevel logLevel2, String str) {
        if (this.logLevel == null || logLevel2 == null || str == null) {
            return false;
        }
        if (this.logLevel.getIndex() <= logLevel2.getIndex()) {
            return true;
        }
        int indexOf = str.indexOf(".");
        if (indexOf < 0 || indexOf >= str.length()) {
            return false;
        }
        String substring = str.substring(0, indexOf);
        if (!TextUtils.isEmpty(substring) && this.moduleFilter.size() > 0 && this.moduleFilter.get(substring) != null && this.moduleFilter.get(substring).getIndex() <= logLevel2.getIndex()) {
            return true;
        }
        return false;
    }

    public byte[] ecrypted(byte[] bArr, int i, int i2) {
        return new byte[0];
    }

    public byte[] ecrypted(byte[] bArr) {
        return new byte[0];
    }

    public LogLevel getLogLevel(String str) {
        if (TextUtils.isEmpty(str)) {
            return this.logLevel;
        }
        return this.moduleFilter.get(str) == null ? this.logLevel : this.moduleFilter.get(str);
    }

    public void setLogLevel(String str) {
        updateLogLevel(TLogUtils.convertLogLevel(str));
    }

    public void setModuleFilter(Map<String, LogLevel> map) {
        addModuleFilter(map);
    }
}
