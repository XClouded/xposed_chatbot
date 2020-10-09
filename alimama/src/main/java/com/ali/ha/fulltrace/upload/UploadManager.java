package com.ali.ha.fulltrace.upload;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.ali.ha.fulltrace.FTHeader;
import com.ali.ha.fulltrace.FileUtils;
import com.ali.ha.fulltrace.FulltraceGlobal;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.logger.Logger;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UploadManager {
    public static final String HOTDATA = "hotdata";
    private static final long ONE_DAY = 86400000;
    private static final String TAG = "UploadManager";
    private static final String TRACE_END = ".trace";
    private long BG_TO_FG_CHECK_DELAY;
    private int BOOT_DELAY_CHECK;
    private long FG_TO_BG_CHECK_DELAY;
    private long MAX_CACHE_DAY;
    private long MAX_CACHE_SIZE;
    private long MAX_CHECK_INTERVAL;
    private long MAX_LOAD_FILE_SIZE;
    private long MAX_MOBILE_TRAFFIC;
    private long currentSize;
    private volatile boolean isFinished;
    private volatile boolean isForeground;
    private volatile boolean isUploading;
    private Application mApplication;
    private SharedPreferences sharedPreferences;

    public static class SP {
        public static final String KEY_DATE = "date";
        public static final String KEY_SIZE = "size";
        public static final String SP_NAME = "com.ali.fulltrace";
    }

    private UploadManager() {
        this.currentSize = 0;
        this.isForeground = true;
        this.isFinished = false;
        this.isUploading = false;
        this.BOOT_DELAY_CHECK = 20000;
        this.MAX_MOBILE_TRAFFIC = 1048576;
        this.MAX_CACHE_DAY = DateUtils.WEEK;
        this.MAX_CHECK_INTERVAL = 300000;
        this.BG_TO_FG_CHECK_DELAY = 10000;
        this.FG_TO_BG_CHECK_DELAY = TBToast.Duration.MEDIUM;
        this.MAX_LOAD_FILE_SIZE = 256000;
        this.MAX_CACHE_SIZE = 52428800;
    }

    public static final UploadManager getInstance() {
        return SingleInstanceHolder.sInstance;
    }

    private static final class SingleInstanceHolder {
        /* access modifiers changed from: private */
        public static final UploadManager sInstance = new UploadManager();

        private SingleInstanceHolder() {
        }
    }

    public void init(Application application) {
        this.mApplication = application;
        loadDefaultData();
        FulltraceGlobal.instance().uploadHandler().postDelayed(new Runnable() {
            public void run() {
                try {
                    UploadManager.this.upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) this.BOOT_DELAY_CHECK);
    }

    private void loadDefaultData() {
        this.sharedPreferences = UploadSharedPreferences.instance().getProcessSP(this.mApplication, SP.SP_NAME);
        long j = this.sharedPreferences.getLong("date", 0);
        this.currentSize = this.sharedPreferences.getLong("size", 0);
        long currentTimeMillis = System.currentTimeMillis() / 86400000;
        if (currentTimeMillis != j) {
            this.sharedPreferences.edit().putLong("date", currentTimeMillis).putLong("size", 0).apply();
            this.currentSize = 0;
        }
        this.BOOT_DELAY_CHECK = 30000;
        this.MAX_MOBILE_TRAFFIC = 1048576;
        this.MAX_CACHE_DAY = DateUtils.WEEK;
        this.MAX_CHECK_INTERVAL = 300000;
        this.BG_TO_FG_CHECK_DELAY = 10000;
        this.FG_TO_BG_CHECK_DELAY = TBToast.Duration.MEDIUM;
        this.MAX_LOAD_FILE_SIZE = 256000;
        this.MAX_CACHE_SIZE = 52428800;
    }

    /* access modifiers changed from: private */
    public void upload() {
        if (!this.isUploading && !this.isFinished) {
            this.isUploading = true;
            trimAllFiles();
            List<File> uploadDir = getUploadDir();
            if (uploadDir == null || uploadDir.size() <= 0) {
                Logger.i(TAG, "upload dir is empty !");
                this.isFinished = true;
                this.isUploading = false;
                return;
            }
            clearInvalidLog(uploadDir, trimLogFile(uploadDir));
            Logger.d("start upload", new Object[0]);
            this.isFinished = uploadLogFiles(uploadDir);
            if (!this.isFinished && this.isForeground) {
                FulltraceGlobal.instance().uploadHandler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            UploadManager.this.upload();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, this.MAX_CHECK_INTERVAL);
            }
            this.isUploading = false;
            Logger.d("finish upload", new Object[0]);
        }
    }

    private boolean uploadLogFiles(List<File> list) {
        for (File next : list) {
            if (next.isDirectory()) {
                File[] listFiles = next.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        if (!file.isFile() || UploadManager.this.getFileNameTime(file, UploadManager.TRACE_END) <= 0) {
                            return false;
                        }
                        return true;
                    }
                });
                if (listFiles == null || listFiles.length <= 0) {
                    Logger.i(TAG, "upload dir is empty=" + next.getAbsolutePath());
                    FileUtils.deleteFile(next);
                } else {
                    List asList = Arrays.asList(listFiles);
                    if (asList.size() > 1) {
                        Collections.sort(asList, new Comparator<File>() {
                            public int compare(File file, File file2) {
                                long access$300 = UploadManager.this.getFileNameTime(file, UploadManager.TRACE_END) - UploadManager.this.getFileNameTime(file2, UploadManager.TRACE_END);
                                if (access$300 == 0) {
                                    return 0;
                                }
                                return access$300 > 0 ? 1 : -1;
                            }
                        });
                    }
                    String substring = next.getAbsolutePath().substring(next.getAbsolutePath().lastIndexOf("/") + 1);
                    String num = Integer.toString((substring + FTHeader.utdid).hashCode());
                    int size = asList.size();
                    int i = 0;
                    boolean z = false;
                    while (true) {
                        if (i >= size) {
                            break;
                        }
                        File file = (File) asList.get(i);
                        StringBuilder sb = new StringBuilder();
                        sb.append(num);
                        sb.append("#");
                        i++;
                        sb.append(i);
                        sb.append("#");
                        sb.append(size);
                        boolean uploadFile = uploadFile(file, sb.toString());
                        Logger.i(TAG, "upload file=" + file.getAbsolutePath() + Operators.SPACE_STR + uploadFile + Operators.SPACE_STR + file.length());
                        if (!uploadFile) {
                            z = uploadFile;
                            break;
                        }
                        file.delete();
                        z = uploadFile;
                    }
                    if (!z) {
                        return false;
                    }
                    FileUtils.deleteFile(next);
                }
            }
        }
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r6v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0104, code lost:
        r13 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0106, code lost:
        r5 = null;
        r8 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0160 A[SYNTHETIC, Splitter:B:101:0x0160] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0170 A[SYNTHETIC, Splitter:B:108:0x0170] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0104 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:21:0x0096] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x011c A[SYNTHETIC, Splitter:B:79:0x011c] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x012c A[SYNTHETIC, Splitter:B:86:0x012c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean uploadFile(java.io.File r12, java.lang.String r13) {
        /*
            r11 = this;
            long r0 = r12.length()
            r2 = 0
            r4 = 1
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 != 0) goto L_0x000c
            return r4
        L_0x000c:
            r0 = 0
            boolean r1 = r11.isMobileConnected()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            if (r1 == 0) goto L_0x005f
            long r2 = r11.currentSize     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            long r5 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            double r5 = (double) r5
            r7 = 4600877379321698714(0x3fd999999999999a, double:0.4)
            java.lang.Double.isNaN(r5)
            double r5 = r5 * r7
            long r5 = (long) r5
            long r2 = r2 + r5
            long r5 = r11.MAX_MOBILE_TRAFFIC     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 < 0) goto L_0x005f
            java.lang.String r13 = "UploadManager"
            java.lang.Object[] r1 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r5.<init>()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r6 = "upload size larger than MAX_MOBILE_TRAFFIC! "
            r5.append(r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r6 = r12.getAbsolutePath()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r5.append(r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r6 = " "
            r5.append(r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            long r6 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r5.append(r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r6 = " "
            r5.append(r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r5.append(r2)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r2 = r5.toString()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r1[r0] = r2     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.w(r13, r1)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            return r0
        L_0x005f:
            byte[] r5 = com.ali.ha.fulltrace.FileUtils.readFileToBytes(r12)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            if (r5 != 0) goto L_0x0090
            java.lang.String r13 = "UploadManager"
            java.lang.Object[] r1 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r2.<init>()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r3 = "read file failed! "
            r2.append(r3)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r3 = r12.getAbsolutePath()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r2.append(r3)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r3 = " "
            r2.append(r3)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            long r5 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r2.append(r5)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r2 = r2.toString()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r1[r0] = r2     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r13, r1)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            return r4
        L_0x0090:
            r6 = 0
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x010d, all -> 0x010a }
            r7.<init>()     // Catch:{ Throwable -> 0x010d, all -> 0x010a }
            java.util.zip.GZIPOutputStream r8 = new java.util.zip.GZIPOutputStream     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            r8.<init>(r7)     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            r8.write(r5)     // Catch:{ Throwable -> 0x0102, all -> 0x0100 }
            r8.flush()     // Catch:{ Throwable -> 0x0102, all -> 0x0100 }
            r8.close()     // Catch:{ Throwable -> 0x0102, all -> 0x0100 }
            byte[] r5 = r7.toByteArray()     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            if (r5 == 0) goto L_0x00e6
            int r8 = r5.length     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            if (r8 != 0) goto L_0x00ae
            goto L_0x00e6
        L_0x00ae:
            r8 = 2
            byte[] r5 = android.util.Base64.encode(r5, r8)     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            if (r5 == 0) goto L_0x00cc
            int r8 = r5.length     // Catch:{ Throwable -> 0x00ca, all -> 0x0104 }
            if (r8 != 0) goto L_0x00b9
            goto L_0x00cc
        L_0x00b9:
            r7.close()     // Catch:{ IOException -> 0x00be }
            goto L_0x013a
        L_0x00be:
            r6 = move-exception
            java.lang.String r7 = "baOS close failed"
            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r8[r0] = r6     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r7, r8)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            goto L_0x013a
        L_0x00ca:
            r8 = r6
            goto L_0x0108
        L_0x00cc:
            java.lang.String r8 = "UploadManager"
            java.lang.Object[] r9 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x00ca, all -> 0x0104 }
            java.lang.String r10 = "base64 failed!"
            r9[r0] = r10     // Catch:{ Throwable -> 0x00ca, all -> 0x0104 }
            com.ali.ha.fulltrace.logger.Logger.e(r8, r9)     // Catch:{ Throwable -> 0x00ca, all -> 0x0104 }
            r7.close()     // Catch:{ IOException -> 0x00db }
            goto L_0x00e5
        L_0x00db:
            r13 = move-exception
            java.lang.String r1 = "baOS close failed"
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r2[r0] = r13     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r1, r2)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x00e5:
            return r4
        L_0x00e6:
            java.lang.String r5 = "UploadManager"
            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            java.lang.String r9 = "gzip failed!"
            r8[r0] = r9     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            com.ali.ha.fulltrace.logger.Logger.e(r5, r8)     // Catch:{ Throwable -> 0x0106, all -> 0x0104 }
            r7.close()     // Catch:{ IOException -> 0x00f5 }
            goto L_0x00ff
        L_0x00f5:
            r13 = move-exception
            java.lang.String r1 = "baOS close failed"
            java.lang.Object[] r2 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r2[r0] = r13     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r1, r2)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x00ff:
            return r4
        L_0x0100:
            r13 = move-exception
            goto L_0x015d
        L_0x0102:
            r5 = r6
            goto L_0x0108
        L_0x0104:
            r13 = move-exception
            goto L_0x015e
        L_0x0106:
            r5 = r6
            r8 = r5
        L_0x0108:
            r6 = r7
            goto L_0x010f
        L_0x010a:
            r13 = move-exception
            r7 = r6
            goto L_0x015e
        L_0x010d:
            r5 = r6
            r8 = r5
        L_0x010f:
            java.lang.String r7 = "UploadManager"
            java.lang.Object[] r9 = new java.lang.Object[r4]     // Catch:{ all -> 0x015b }
            java.lang.String r10 = "gzip and base64 error!"
            r9[r0] = r10     // Catch:{ all -> 0x015b }
            com.ali.ha.fulltrace.logger.Logger.e(r7, r9)     // Catch:{ all -> 0x015b }
            if (r6 == 0) goto L_0x012a
            r6.close()     // Catch:{ IOException -> 0x0120 }
            goto L_0x012a
        L_0x0120:
            r6 = move-exception
            java.lang.String r7 = "baOS close failed"
            java.lang.Object[] r9 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r9[r0] = r6     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r7, r9)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x012a:
            if (r8 == 0) goto L_0x013a
            r8.close()     // Catch:{ IOException -> 0x0130 }
            goto L_0x013a
        L_0x0130:
            r6 = move-exception
            java.lang.String r7 = "gzipOS close failed"
            java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r8[r0] = r6     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r7, r8)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x013a:
            java.lang.String r6 = new java.lang.String     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r6.<init>(r5)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            boolean r13 = com.ali.ha.fulltrace.SendManager.send(r13, r6)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            if (r13 == 0) goto L_0x015a
            if (r1 == 0) goto L_0x015a
            r11.currentSize = r2     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            android.content.SharedPreferences r1 = r11.sharedPreferences     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            android.content.SharedPreferences$Editor r1 = r1.edit()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            java.lang.String r2 = "size"
            long r5 = r11.currentSize     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            android.content.SharedPreferences$Editor r1 = r1.putLong(r2, r5)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r1.apply()     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x015a:
            return r13
        L_0x015b:
            r13 = move-exception
            r7 = r6
        L_0x015d:
            r6 = r8
        L_0x015e:
            if (r7 == 0) goto L_0x016e
            r7.close()     // Catch:{ IOException -> 0x0164 }
            goto L_0x016e
        L_0x0164:
            r1 = move-exception
            java.lang.String r2 = "baOS close failed"
            java.lang.Object[] r3 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r3[r0] = r1     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r2, r3)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x016e:
            if (r6 == 0) goto L_0x017e
            r6.close()     // Catch:{ IOException -> 0x0174 }
            goto L_0x017e
        L_0x0174:
            r1 = move-exception
            java.lang.String r2 = "gzipOS close failed"
            java.lang.Object[] r3 = new java.lang.Object[r4]     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            r3[r0] = r1     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
            com.ali.ha.fulltrace.logger.Logger.e(r2, r3)     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x017e:
            throw r13     // Catch:{ OutOfMemoryError -> 0x0184, Throwable -> 0x017f }
        L_0x017f:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x01b1
        L_0x0184:
            r12.delete()
            java.lang.String r13 = "UploadManager"
            java.lang.Object[] r1 = new java.lang.Object[r4]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "read file oom! "
            r2.append(r3)
            java.lang.String r3 = r12.getAbsolutePath()
            r2.append(r3)
            java.lang.String r3 = " "
            r2.append(r3)
            long r3 = r12.length()
            r2.append(r3)
            java.lang.String r12 = r2.toString()
            r1[r0] = r12
            com.ali.ha.fulltrace.logger.Logger.e(r13, r1)
        L_0x01b1:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.ha.fulltrace.upload.UploadManager.uploadFile(java.io.File, java.lang.String):boolean");
    }

    private void clearInvalidLog(List<File> list, long j) {
        File[] listFiles;
        File[] fileArr;
        long j2 = j;
        int size = list.size();
        long j3 = j2 - this.MAX_CACHE_SIZE;
        long currentTimeMillis = System.currentTimeMillis();
        int i = 1;
        int i2 = size - 1;
        while (i2 > -1) {
            File file = list.get(i2);
            if (file.isDirectory() && (listFiles = file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().endsWith(UploadManager.TRACE_END);
                }
            })) != null) {
                int length = listFiles.length;
                long j4 = j3;
                int i3 = 0;
                while (i3 < length) {
                    File file2 = listFiles[i3];
                    if (file2.isFile()) {
                        if (j4 > 0) {
                            Object[] objArr = new Object[i];
                            objArr[0] = "total size large than MAX_CACHE_SIZE! " + j2 + " remove=" + file2.getAbsolutePath() + "  " + file2.length() + " outSize=" + j4;
                            Logger.i(TAG, objArr);
                            j4 -= file2.length();
                            file2.delete();
                        } else {
                            if (file2.length() >= this.MAX_LOAD_FILE_SIZE) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("file size is to large! ");
                                sb.append(file2.getAbsolutePath());
                                sb.append(Operators.SPACE_STR);
                                fileArr = listFiles;
                                sb.append(file2.length());
                                Logger.e(TAG, sb.toString());
                                file2.delete();
                            } else {
                                fileArr = listFiles;
                                long fileNameTime = getFileNameTime(file2, TRACE_END);
                                if (fileNameTime > 0 && currentTimeMillis - fileNameTime > this.MAX_CACHE_DAY) {
                                    Logger.i(TAG, "file date is expired! " + file2.getAbsolutePath());
                                    file2.delete();
                                    i3++;
                                    listFiles = fileArr;
                                    j2 = j;
                                    i = 1;
                                    List<File> list2 = list;
                                }
                            }
                            i3++;
                            listFiles = fileArr;
                            j2 = j;
                            i = 1;
                            List<File> list22 = list;
                        }
                    }
                    fileArr = listFiles;
                    i3++;
                    listFiles = fileArr;
                    j2 = j;
                    i = 1;
                    List<File> list222 = list;
                }
                j3 = j4;
            }
            i2--;
            j2 = j;
            i = 1;
        }
    }

    private long trimLogFile(List<File> list) {
        File[] listFiles;
        long j = 0;
        for (File next : list) {
            if (next.isDirectory() && (listFiles = next.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().endsWith(UploadManager.TRACE_END);
                }
            })) != null) {
                for (File file : listFiles) {
                    if (file.isFile()) {
                        j += file.length();
                    }
                }
            }
        }
        return j;
    }

    private List<File> getUploadDir() {
        File[] listFiles;
        File file = new File(DumpManager.getPathPrefix(this.mApplication));
        if (!file.exists() || (listFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (!file.isDirectory() || UploadManager.this.getLongValue(file.getName()) <= 0) {
                    return false;
                }
                return true;
            }
        })) == null || listFiles.length <= 1) {
            return null;
        }
        List asList = Arrays.asList(listFiles);
        if (asList.size() > 1) {
            Collections.sort(asList, new Comparator<File>() {
                public int compare(File file, File file2) {
                    long access$400 = UploadManager.this.getLongValue(file2.getName()) - UploadManager.this.getLongValue(file.getName());
                    if (access$400 == 0) {
                        return 0;
                    }
                    return access$400 > 0 ? 1 : -1;
                }
            });
        }
        ArrayList arrayList = new ArrayList(asList);
        arrayList.remove(0);
        return arrayList;
    }

    private void trimAllFiles() {
        File[] listFiles;
        String pathCachPrefix = DumpManager.getPathCachPrefix(this.mApplication);
        String pathPrefix = DumpManager.getPathPrefix(this.mApplication);
        File file = new File(pathCachPrefix);
        if (file.exists() && (listFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (!file.isDirectory() || UploadManager.this.getLongValue(file.getName()) <= 0 || file.getName().equals(String.valueOf(DumpManager.session))) {
                    return false;
                }
                return true;
            }
        })) != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                String str = pathPrefix + File.separator + file2.getName();
                if (new File(file2.getAbsolutePath() + File.separator + "hotdata").exists()) {
                    DumpManager.getInstance().trimHotdataBeforeUpload(file2.getAbsolutePath(), str);
                }
                FileUtils.deleteFile(file2);
            }
        }
    }

    /* access modifiers changed from: private */
    public long getFileNameTime(File file, String str) {
        String name = file.getName();
        if (TextUtils.isEmpty(str)) {
            return getLongValue(name);
        }
        int indexOf = name.indexOf(str);
        if (indexOf > 0) {
            return getLongValue(name.substring(0, indexOf));
        }
        return -1;
    }

    public boolean isMobileConnected() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mApplication.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || activeNetworkInfo.getType() != 0) {
                return false;
            }
            return activeNetworkInfo.isAvailable();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: private */
    public long getLongValue(String str) {
        try {
            return Long.parseLong(str);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }
}
