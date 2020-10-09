package anet.channel.strategy;

import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.StrategyStatObject;
import anet.channel.util.ALog;
import anet.channel.util.SerializeHelper;
import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

class StrategySerializeHelper {
    private static final String DEFAULT_STRATEGY_FOLDER_NAME = "awcn_strategy";
    private static final long MAX_AVAILABLE_PERIOD = 172800000;
    private static final long MAX_FILE_NUM = 10;
    private static final String TAG = "awcn.StrategySerializeHelper";
    private static Comparator<File> comparator = new Comparator<File>() {
        public int compare(File file, File file2) {
            return (int) (file2.lastModified() - file.lastModified());
        }
    };
    private static File strategyFolder = null;
    private static volatile boolean toDelete = false;

    StrategySerializeHelper() {
    }

    public static void initialize(Context context) {
        if (context != null) {
            try {
                strategyFolder = new File(context.getFilesDir(), DEFAULT_STRATEGY_FOLDER_NAME);
                if (!checkFolderExistOrCreate(strategyFolder)) {
                    ALog.e(TAG, "create directory failed!!!", (String) null, "dir", strategyFolder.getAbsolutePath());
                }
                if (!GlobalAppRuntimeInfo.isTargetProcess()) {
                    String currentProcess = GlobalAppRuntimeInfo.getCurrentProcess();
                    strategyFolder = new File(strategyFolder, currentProcess.substring(currentProcess.indexOf(58) + 1));
                    if (!checkFolderExistOrCreate(strategyFolder)) {
                        ALog.e(TAG, "create directory failed!!!", (String) null, "dir", strategyFolder.getAbsolutePath());
                    }
                }
                ALog.i(TAG, "StrateyFolder", (String) null, "path", strategyFolder.getAbsolutePath());
                if (toDelete) {
                    clearStrategyFolder();
                    toDelete = false;
                    return;
                }
                removeInvalidFile();
            } catch (Throwable th) {
                ALog.e(TAG, "StrategySerializeHelper initialize failed!!!", (String) null, th, new Object[0]);
            }
        }
    }

    private static boolean checkFolderExistOrCreate(File file) {
        if (file == null || file.exists()) {
            return true;
        }
        return file.mkdir();
    }

    public static File getStrategyFile(String str) {
        checkFolderExistOrCreate(strategyFolder);
        return new File(strategyFolder, str);
    }

    static synchronized void clearStrategyFolder() {
        synchronized (StrategySerializeHelper.class) {
            ALog.i(TAG, "clear start.", (String) null, new Object[0]);
            if (strategyFolder == null) {
                ALog.w(TAG, "folder path not initialized, wait to clear", (String) null, new Object[0]);
                toDelete = true;
                return;
            }
            File[] listFiles = strategyFolder.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
                ALog.i(TAG, "clear end.", (String) null, new Object[0]);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0018, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static synchronized java.io.File[] getSortedFiles() {
        /*
            java.lang.Class<anet.channel.strategy.StrategySerializeHelper> r0 = anet.channel.strategy.StrategySerializeHelper.class
            monitor-enter(r0)
            java.io.File r1 = strategyFolder     // Catch:{ all -> 0x0019 }
            if (r1 != 0) goto L_0x000a
            r1 = 0
            monitor-exit(r0)
            return r1
        L_0x000a:
            java.io.File r1 = strategyFolder     // Catch:{ all -> 0x0019 }
            java.io.File[] r1 = r1.listFiles()     // Catch:{ all -> 0x0019 }
            if (r1 == 0) goto L_0x0017
            java.util.Comparator<java.io.File> r2 = comparator     // Catch:{ all -> 0x0019 }
            java.util.Arrays.sort(r1, r2)     // Catch:{ all -> 0x0019 }
        L_0x0017:
            monitor-exit(r0)
            return r1
        L_0x0019:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategySerializeHelper.getSortedFiles():java.io.File[]");
    }

    static synchronized void removeInvalidFile() {
        synchronized (StrategySerializeHelper.class) {
            File[] sortedFiles = getSortedFiles();
            if (sortedFiles != null) {
                int i = 0;
                for (File file : sortedFiles) {
                    if (!file.isDirectory()) {
                        if (System.currentTimeMillis() - file.lastModified() > MAX_AVAILABLE_PERIOD) {
                            file.delete();
                        } else if (file.getName().startsWith("WIFI")) {
                            int i2 = i + 1;
                            if (((long) i) > MAX_FILE_NUM) {
                                file.delete();
                            }
                            i = i2;
                        }
                    }
                }
            }
        }
    }

    static synchronized void persist(Serializable serializable, String str, StrategyStatObject strategyStatObject) {
        synchronized (StrategySerializeHelper.class) {
            SerializeHelper.persist(serializable, getStrategyFile(str), strategyStatObject);
        }
    }

    static synchronized <T> T restore(String str, StrategyStatObject strategyStatObject) {
        T restore;
        synchronized (StrategySerializeHelper.class) {
            restore = SerializeHelper.restore(getStrategyFile(str), strategyStatObject);
        }
        return restore;
    }
}
