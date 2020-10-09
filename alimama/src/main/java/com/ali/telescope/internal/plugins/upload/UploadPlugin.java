package com.ali.telescope.internal.plugins.upload;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.Constants;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.report.ReportManager;
import com.ali.telescope.util.FileUtils;
import com.ali.telescope.util.TeleScopeSharePreferences;
import com.ali.telescope.util.TelescopeLog;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class UploadPlugin extends Plugin {
    public static final String HOTDATA = "hotdata";
    private static final long ONE_DAY = 86400000;
    private static final String TAG = "UploadPlugin";
    private static final String TRACE_END = ".trace";
    private long BG_TO_FG_CHECK_DELAY = 10000;
    private int BOOT_DELAY_CHECK = 30000;
    private long FG_TO_BG_CHECK_DELAY = TBToast.Duration.MEDIUM;
    private long MAX_CACHE_DAY = DateUtils.WEEK;
    private long MAX_CACHE_SIZE = 52428800;
    /* access modifiers changed from: private */
    public long MAX_CHECK_INTERVAL = 300000;
    private long MAX_LOAD_FILE_SIZE = 256000;
    private long MAX_MOBILE_TRAFFIC = 1048576;
    private long currentSize = 0;
    /* access modifiers changed from: private */
    public volatile boolean isFinished = false;
    /* access modifiers changed from: private */
    public volatile boolean isForeground = true;
    /* access modifiers changed from: private */
    public volatile boolean isUploading = false;
    private Application mApplication;
    /* access modifiers changed from: private */
    public UploadHandler mUploadHandler;
    private SharedPreferences sharedPreferences;

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.mApplication = application;
        if (jSONObject == null) {
            loadDefaultData();
        } else {
            loadData(jSONObject);
        }
        this.mUploadHandler = new UploadHandler(Loopers.getTelescopeLooper(), this);
        iTelescopeContext.registerBroadcast(2, this.pluginID);
        this.mUploadHandler.sendEmptyMessageDelayed(1, (long) this.BOOT_DELAY_CHECK);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void loadDefaultData() {
        this.sharedPreferences = TeleScopeSharePreferences.getInstance().getProcessSP(this.mApplication, Constants.SP.SP_NAME);
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

    private void loadData(JSONObject jSONObject) {
        this.sharedPreferences = TeleScopeSharePreferences.getInstance().getProcessSP(this.mApplication, Constants.SP.SP_NAME);
        long j = this.sharedPreferences.getLong("date", 0);
        this.currentSize = this.sharedPreferences.getLong("size", 0);
        long currentTimeMillis = System.currentTimeMillis() / 86400000;
        if (currentTimeMillis != j) {
            this.sharedPreferences.edit().putLong("date", currentTimeMillis).putLong("size", 0).apply();
            this.currentSize = 0;
        }
        this.BOOT_DELAY_CHECK = jSONObject.optInt("boot_delay_check", 30000);
        this.MAX_MOBILE_TRAFFIC = jSONObject.optLong("max_mobile_traffic", 1048576);
        this.MAX_CACHE_DAY = jSONObject.optLong("max_cache_day", DateUtils.WEEK);
        this.MAX_CHECK_INTERVAL = jSONObject.optLong("max_check_interval", 300000);
        this.BG_TO_FG_CHECK_DELAY = jSONObject.optLong("bg_to_fg_check_delay", 10000);
        this.FG_TO_BG_CHECK_DELAY = jSONObject.optLong("fg_to_bg_check_delay", TBToast.Duration.MEDIUM);
        this.MAX_LOAD_FILE_SIZE = jSONObject.optLong("max_load_file_size", 256000);
        this.MAX_CACHE_SIZE = jSONObject.optLong("max_cache_size", 52428800);
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        if (!this.isFinished && i == 2) {
            AppEvent appEvent = (AppEvent) event;
            if (appEvent.subEvent == 1) {
                this.isForeground = false;
                this.mUploadHandler.removeMessages(1);
                this.mUploadHandler.sendEmptyMessageDelayed(1, this.FG_TO_BG_CHECK_DELAY);
            } else if (appEvent.subEvent == 2) {
                this.isForeground = true;
                if (!this.mUploadHandler.hasMessages(1)) {
                    this.mUploadHandler.sendEmptyMessageDelayed(1, this.BG_TO_FG_CHECK_DELAY);
                }
            }
        }
    }

    static class UploadHandler extends Handler {
        static final int MSG_UPLOAD = 1;
        private UploadPlugin uploadPlugin;

        public UploadHandler(Looper looper, UploadPlugin uploadPlugin2) {
            super(looper);
            this.uploadPlugin = uploadPlugin2;
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                this.uploadPlugin.upload();
            }
        }
    }

    /* access modifiers changed from: private */
    public void upload() {
        if (!this.isUploading && !this.isFinished) {
            this.isUploading = true;
            trimAllFiles();
            final List<File> uploadDir = getUploadDir();
            if (uploadDir == null || uploadDir.size() <= 0) {
                TelescopeLog.i("UploadPlugin", "upload dir is empty !");
                this.isFinished = true;
                this.isUploading = false;
                return;
            }
            clearInvalidLog(uploadDir, trimLogFile(uploadDir));
            new Thread("telescope upload") {
                public void run() {
                    super.run();
                    TelescopeLog.d("start upload", new Object[0]);
                    boolean unused = UploadPlugin.this.isFinished = UploadPlugin.this.uploadLogFiles(uploadDir);
                    if (!UploadPlugin.this.isFinished && UploadPlugin.this.isForeground) {
                        UploadPlugin.this.mUploadHandler.sendEmptyMessageDelayed(1, UploadPlugin.this.MAX_CHECK_INTERVAL);
                    }
                    boolean unused2 = UploadPlugin.this.isUploading = false;
                    TelescopeLog.d("finish upload", new Object[0]);
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public boolean uploadLogFiles(List<File> list) {
        for (File next : list) {
            if (next.isDirectory()) {
                File[] listFiles = next.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        if (!file.isFile() || UploadPlugin.this.getFileNameTime(file, UploadPlugin.TRACE_END) <= 0) {
                            return false;
                        }
                        return true;
                    }
                });
                if (listFiles == null || listFiles.length <= 0) {
                    TelescopeLog.i("UploadPlugin", "upload dir is empty=" + next.getAbsolutePath());
                    FileUtils.deleteFile(next);
                } else {
                    List asList = Arrays.asList(listFiles);
                    if (asList.size() > 1) {
                        Collections.sort(asList, new Comparator<File>() {
                            public int compare(File file, File file2) {
                                long access$700 = UploadPlugin.this.getFileNameTime(file, UploadPlugin.TRACE_END) - UploadPlugin.this.getFileNameTime(file2, UploadPlugin.TRACE_END);
                                if (access$700 == 0) {
                                    return 0;
                                }
                                return access$700 > 0 ? 1 : -1;
                            }
                        });
                    }
                    Iterator it = asList.iterator();
                    boolean z = false;
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        File file = (File) it.next();
                        boolean uploadFile = uploadFile(file);
                        TelescopeLog.i("UploadPlugin", "upload file=" + file.getAbsolutePath() + Operators.SPACE_STR + uploadFile + Operators.SPACE_STR + file.length());
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v3, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v10, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v12, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v14, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v15, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ea, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ec, code lost:
        r5 = null;
        r8 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ea A[ExcHandler: all (th java.lang.Throwable), Splitter:B:21:0x0096] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean uploadFile(java.io.File r12) {
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
            boolean r1 = r11.isMobileConnected()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            if (r1 == 0) goto L_0x005f
            long r2 = r11.currentSize     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            long r5 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            double r5 = (double) r5
            r7 = 4600877379321698714(0x3fd999999999999a, double:0.4)
            java.lang.Double.isNaN(r5)
            double r5 = r5 * r7
            long r5 = (long) r5
            long r2 = r2 + r5
            long r5 = r11.MAX_MOBILE_TRAFFIC     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 < 0) goto L_0x005f
            java.lang.String r1 = "UploadPlugin"
            java.lang.String[] r5 = new java.lang.String[r4]     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r6.<init>()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r7 = "upload size larger than MAX_MOBILE_TRAFFIC! "
            r6.append(r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r7 = r12.getAbsolutePath()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r6.append(r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r7 = " "
            r6.append(r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            long r7 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r6.append(r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r7 = " "
            r6.append(r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r6.append(r2)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r2 = r6.toString()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r5[r0] = r2     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.TelescopeLog.w(r1, r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            return r0
        L_0x005f:
            byte[] r5 = com.ali.telescope.util.FileUtils.readFileToBytes(r12)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            if (r5 != 0) goto L_0x0090
            java.lang.String r1 = "UploadPlugin"
            java.lang.String[] r2 = new java.lang.String[r4]     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r3.<init>()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r5 = "read file failed! "
            r3.append(r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r5 = r12.getAbsolutePath()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r3.append(r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r5 = " "
            r3.append(r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            long r5 = r12.length()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r3.append(r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r3 = r3.toString()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r2[r0] = r3     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.TelescopeLog.e(r1, r2)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            return r4
        L_0x0090:
            r6 = 0
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x00f3, all -> 0x00f0 }
            r7.<init>()     // Catch:{ Throwable -> 0x00f3, all -> 0x00f0 }
            java.util.zip.GZIPOutputStream r8 = new java.util.zip.GZIPOutputStream     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            r8.<init>(r7)     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            r8.write(r5)     // Catch:{ Throwable -> 0x00e8, all -> 0x00e6 }
            r8.flush()     // Catch:{ Throwable -> 0x00e8, all -> 0x00e6 }
            r8.close()     // Catch:{ Throwable -> 0x00e8, all -> 0x00e6 }
            byte[] r5 = r7.toByteArray()     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            if (r5 == 0) goto L_0x00d4
            int r8 = r5.length     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            if (r8 != 0) goto L_0x00ae
            goto L_0x00d4
        L_0x00ae:
            r8 = 2
            byte[] r5 = android.util.Base64.encode(r5, r8)     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            if (r5 == 0) goto L_0x00c2
            int r8 = r5.length     // Catch:{ Throwable -> 0x00c0, all -> 0x00ea }
            if (r8 != 0) goto L_0x00b9
            goto L_0x00c2
        L_0x00b9:
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            goto L_0x0106
        L_0x00c0:
            r8 = r6
            goto L_0x00ee
        L_0x00c2:
            java.lang.String r8 = "UploadPlugin"
            java.lang.String[] r9 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x00c0, all -> 0x00ea }
            java.lang.String r10 = "base64 failed!"
            r9[r0] = r10     // Catch:{ Throwable -> 0x00c0, all -> 0x00ea }
            com.ali.telescope.util.TelescopeLog.e(r8, r9)     // Catch:{ Throwable -> 0x00c0, all -> 0x00ea }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            return r4
        L_0x00d4:
            java.lang.String r5 = "UploadPlugin"
            java.lang.String[] r8 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            java.lang.String r9 = "gzip failed!"
            r8[r0] = r9     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            com.ali.telescope.util.TelescopeLog.e(r5, r8)     // Catch:{ Throwable -> 0x00ec, all -> 0x00ea }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            return r4
        L_0x00e6:
            r1 = move-exception
            goto L_0x0129
        L_0x00e8:
            r5 = r6
            goto L_0x00ee
        L_0x00ea:
            r1 = move-exception
            goto L_0x012a
        L_0x00ec:
            r5 = r6
            r8 = r5
        L_0x00ee:
            r6 = r7
            goto L_0x00f5
        L_0x00f0:
            r1 = move-exception
            r7 = r6
            goto L_0x012a
        L_0x00f3:
            r5 = r6
            r8 = r5
        L_0x00f5:
            java.lang.String r7 = "UploadPlugin"
            java.lang.String[] r9 = new java.lang.String[r4]     // Catch:{ all -> 0x0127 }
            java.lang.String r10 = "gzip and base64 error!"
            r9[r0] = r10     // Catch:{ all -> 0x0127 }
            com.ali.telescope.util.TelescopeLog.e(r7, r9)     // Catch:{ all -> 0x0127 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r8)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
        L_0x0106:
            java.lang.String r6 = new java.lang.String     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r6.<init>(r5)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            boolean r5 = com.ali.telescope.internal.report.SendManager.sendReport(r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            if (r5 == 0) goto L_0x0126
            if (r1 == 0) goto L_0x0126
            r11.currentSize = r2     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            android.content.SharedPreferences r1 = r11.sharedPreferences     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            android.content.SharedPreferences$Editor r1 = r1.edit()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            java.lang.String r2 = "size"
            long r6 = r11.currentSize     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            android.content.SharedPreferences$Editor r1 = r1.putLong(r2, r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            r1.apply()     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
        L_0x0126:
            return r5
        L_0x0127:
            r1 = move-exception
            r7 = r6
        L_0x0129:
            r6 = r8
        L_0x012a:
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r7)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r6)     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
            throw r1     // Catch:{ OutOfMemoryError -> 0x0136, Throwable -> 0x0131 }
        L_0x0131:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0163
        L_0x0136:
            r12.delete()
            java.lang.String r1 = "UploadPlugin"
            java.lang.String[] r2 = new java.lang.String[r4]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "read file oom! "
            r3.append(r4)
            java.lang.String r4 = r12.getAbsolutePath()
            r3.append(r4)
            java.lang.String r4 = " "
            r3.append(r4)
            long r4 = r12.length()
            r3.append(r4)
            java.lang.String r12 = r3.toString()
            r2[r0] = r12
            com.ali.telescope.util.TelescopeLog.e(r1, r2)
        L_0x0163:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.upload.UploadPlugin.uploadFile(java.io.File):boolean");
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
                    return file.getName().endsWith(UploadPlugin.TRACE_END);
                }
            })) != null) {
                int length = listFiles.length;
                long j4 = j3;
                int i3 = 0;
                while (i3 < length) {
                    File file2 = listFiles[i3];
                    if (file2.isFile()) {
                        if (j4 > 0) {
                            String[] strArr = new String[i];
                            strArr[0] = "total size large than MAX_CACHE_SIZE! " + j2 + " remove=" + file2.getAbsolutePath() + "  " + file2.length() + " outSize=" + j4;
                            TelescopeLog.i("UploadPlugin", strArr);
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
                                TelescopeLog.e("UploadPlugin", sb.toString());
                                file2.delete();
                            } else {
                                fileArr = listFiles;
                                long fileNameTime = getFileNameTime(file2, TRACE_END);
                                if (fileNameTime > 0 && currentTimeMillis - fileNameTime > this.MAX_CACHE_DAY) {
                                    TelescopeLog.i("UploadPlugin", "file date is expired! " + file2.getAbsolutePath());
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
                    return file.getName().endsWith(UploadPlugin.TRACE_END);
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
        File file = new File(ReportManager.getPathPrefix(this.mApplication));
        if (!file.exists() || (listFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (!file.isDirectory() || UploadPlugin.this.getLongValue(file.getName()) <= 0) {
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
                    long access$800 = UploadPlugin.this.getLongValue(file2.getName()) - UploadPlugin.this.getLongValue(file.getName());
                    if (access$800 == 0) {
                        return 0;
                    }
                    return access$800 > 0 ? 1 : -1;
                }
            });
        }
        ArrayList arrayList = new ArrayList(asList);
        arrayList.remove(0);
        return arrayList;
    }

    private void trimAllFiles() {
        File[] listFiles;
        String pathCachPrefix = ReportManager.getPathCachPrefix(this.mApplication);
        String pathPrefix = ReportManager.getPathPrefix(this.mApplication);
        File file = new File(pathCachPrefix);
        if (file.exists() && (listFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (!file.isDirectory() || UploadPlugin.this.getLongValue(file.getName()) <= 0 || file.getName().equals(String.valueOf(ReportManager.session))) {
                    return false;
                }
                return true;
            }
        })) != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                String str = pathPrefix + File.separator + file2.getName();
                if (new File(file2.getAbsolutePath() + File.separator + "hotdata").exists()) {
                    ReportManager.getInstance().trimHotdataBeforeUpload(file2.getAbsolutePath(), str);
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
