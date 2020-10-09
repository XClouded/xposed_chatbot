package android.taobao.windvane.packageapp.zipdownload;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;

import com.taobao.downloader.Downloader;
import com.taobao.downloader.TbDownloader;
import com.taobao.downloader.request.DownloadListener;
import com.taobao.downloader.request.DownloadRequest;
import com.taobao.downloader.request.Item;
import com.taobao.downloader.request.Param;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownLoadManager {
    private static final String TAG = "DownLoadManager";
    /* access modifiers changed from: private */
    public ZipAppInfo appinfo;
    private boolean isInstantTask;
    public boolean isTBDownloaderEnabled = true;
    /* access modifiers changed from: private */
    public DownLoadListener listener;
    /* access modifiers changed from: private */
    public Object obj;
    private DownloadRequest request;
    private DownloadListener tbListener;
    /* access modifiers changed from: private */
    public int token;
    /* access modifiers changed from: private */
    public String zipUrl;

    public DownLoadManager(String str, DownLoadListener downLoadListener, int i, Object obj2, boolean z) {
        this.listener = downLoadListener;
        this.token = i;
        this.zipUrl = str;
        this.obj = obj2;
        if (obj2 instanceof ZipAppInfo) {
            this.appinfo = (ZipAppInfo) obj2;
        }
        this.isInstantTask = z;
    }

    public void updateParam(String str, int i, Object obj2, boolean z) {
        this.token = i;
        this.zipUrl = str;
        this.obj = obj2;
        if (obj2 instanceof ZipAppInfo) {
            this.appinfo = (ZipAppInfo) obj2;
        }
        this.isInstantTask = z;
    }

    public boolean doTask() {
        if (this.appinfo != null) {
            if (this.token == 4) {
                AppInfoMonitor.start(this.appinfo.getNameandVersion(), 1);
            }
            if (this.token == 2) {
                AppInfoMonitor.start(this.appinfo.getNameandVersion(), 2);
            }
        }
        if (!WVCommonConfig.commonConfig.isUseTBDownloader || !this.isTBDownloaderEnabled || !doTBDownloadTask()) {
            return doDefaultTask();
        }
        return true;
    }

    private boolean doTBDownloadTask() {
        try {
            if (this.request == null) {
                this.request = new DownloadRequest(this.zipUrl);
                this.request.downloadParam.bizId = "windvane";
                this.request.downloadParam.callbackCondition = 0;
                this.tbListener = new DownloadListener() {
                    public void onDownloadStateChange(String str, boolean z) {
                    }

                    public void onFinish(boolean z) {
                    }

                    public void onNetworkLimit(int i, Param param, DownloadListener.NetworkLimitCallback networkLimitCallback) {
                    }

                    public void onDownloadProgress(int i) {
                        if (DownLoadManager.this.appinfo != null && DownLoadManager.this.appinfo.isPreViewApp) {
                            WVEventService.getInstance().onEvent(6004, Integer.valueOf(i), DownLoadManager.this.appinfo.name);
                        }
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d(DownLoadManager.TAG, "onDownloadProgress pro : " + i);
                        }
                    }

                    public void onDownloadError(String str, int i, String str2) {
                        if (DownLoadManager.this.appinfo != null && DownLoadManager.this.appinfo.isPreViewApp) {
                            WVEventService.getInstance().onEvent(6010);
                        }
                        DownLoadManager.this.checkInstanceTask();
                        DownLoadManager.this.listener.callback(DownLoadManager.this.zipUrl, (String) null, (Map<String, String>) null, DownLoadManager.this.token, DownLoadManager.this.obj);
                        String str3 = str2 + str;
                        AppInfoMonitor.error(DownLoadManager.this.appinfo, ZipAppResultCode.ERR_DOWN_ZIP, DownLoadManager.this.appinfo.v.equals(DownLoadManager.this.appinfo.installedVersion) + ":" + DownLoadManager.this.appinfo.s + " errorCode =" + i + "doTBDownloadTask ErrorMsg=" + str3);
                        if (TaoLog.getLogStatus()) {
                            TaoLog.e(DownLoadManager.TAG, "doTBDownloadTask Exception : " + str3);
                        }
                    }

                    public void onDownloadFinish(String str, String str2) {
                        if (DownLoadManager.this.appinfo != null && DownLoadManager.this.appinfo.isPreViewApp) {
                            WVEventService.getInstance().onEvent(6010);
                        }
                        DownLoadManager.this.checkInstanceTask();
                        try {
                            DownLoadManager.this.listener.callback(DownLoadManager.this.zipUrl, str2, new HashMap(), DownLoadManager.this.token, DownLoadManager.this.obj);
                        } catch (Exception e) {
                            DownLoadManager.this.listener.callback(DownLoadManager.this.zipUrl, (String) null, (Map<String, String>) null, DownLoadManager.this.token, DownLoadManager.this.obj);
                            ZipAppInfo access$000 = DownLoadManager.this.appinfo;
                            int i = ZipAppResultCode.ERR_DOWN_ZIP;
                            AppInfoMonitor.error(access$000, i, DownLoadManager.this.appinfo.v.equals(DownLoadManager.this.appinfo.installedVersion) + ":" + DownLoadManager.this.appinfo.s + " doTBDownloadTask ErrorMsg=" + e.getMessage());
                            if (TaoLog.getLogStatus()) {
                                TaoLog.e(DownLoadManager.TAG, "doTBDownloadTask Exception : " + e.getMessage());
                            }
                        }
                    }
                };
            } else {
                this.request.downloadList.clear();
                this.request.downloadList.add(new Item(this.zipUrl));
            }
            File file = new File(GlobalConfig.context.getCacheDir().getAbsolutePath());
            if (!file.exists()) {
                file.mkdir();
                TaoLog.d(TAG, "TMP 目录不存在，新建一个tmp目录");
            }
            this.request.downloadParam.fileStorePath = file + File.separator + DigestUtils.md5ToHex(this.zipUrl);
            if (hasTbDownloader()) {
                TbDownloader.getInstance().download(this.request, this.tbListener);
                TaoLog.d(TAG, "download by TbDownloader");
                return true;
            }
            Downloader.getInstance().download(this.request, this.tbListener);
            TaoLog.d(TAG, "download by Downloader");
            return true;
        } catch (Throwable th) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "doTBDownloadTask Exception : " + th.getMessage());
            }
            if (th instanceof ClassNotFoundException) {
                this.isTBDownloaderEnabled = false;
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void checkInstanceTask() {
        if (!this.isInstantTask && ZipAppDownloaderQueue.getInstance().instantTaskName != null) {
            if (this.appinfo.name.equals(ZipAppDownloaderQueue.getInstance().instantTaskName)) {
                ZipAppDownloaderQueue.getInstance().updateFinshCount(true);
                ZipAppDownloaderQueue.getInstance().updateState();
                return;
            }
            try {
                synchronized (ZipAppDownloaderQueue.getInstance().lock) {
                    ZipAppDownloaderQueue.getInstance().lock.wait(TBToast.Duration.MEDIUM);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasTbDownloader() {
        Class<TbDownloader> cls = TbDownloader.class;
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v0, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v1, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v6, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v10, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v5, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v11, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v12, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v6, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v13, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v7, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v15, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v8, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v19, resolved type: java.io.FileOutputStream} */
    /* JADX WARNING: type inference failed for: r15v0, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r15v1, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r15v2 */
    /* JADX WARNING: type inference failed for: r15v3 */
    /* JADX WARNING: type inference failed for: r0v16, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r15v4, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r14v18 */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x0299, code lost:
        if (r8 == null) goto L_0x036d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x02a9, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x02aa, code lost:
        r7 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x02ad, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x02ae, code lost:
        r7 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x02cd, code lost:
        r1.listener.callback(r1.zipUrl, (java.lang.String) null, (java.util.Map<java.lang.String, java.lang.String>) null, r1.token, r1.obj);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x02ee, code lost:
        monitor-enter(android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance().lock);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:?, code lost:
        android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance().lock.notify();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:?, code lost:
        android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance().removeByName(r1.appinfo.name);
        android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance().instantTaskName = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0338, code lost:
        android.taobao.windvane.util.TaoLog.e(TAG, "WVZipBPDownloader  Exception : " + r0.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:?, code lost:
        r15.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0358, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x035c, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0361, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0365, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0368, code lost:
        if (r8 == null) goto L_0x036d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x036a, code lost:
        r8.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x036d, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:?, code lost:
        r15.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x0377, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x037b, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x0380, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x0384, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x0389, code lost:
        r8.disconnect();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x0283  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0287 A[SYNTHETIC, Splitter:B:113:0x0287] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0292 A[Catch:{ Exception -> 0x028b }] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x02ad A[ExcHandler: all (th java.lang.Throwable), Splitter:B:47:0x0188] */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02cd A[Catch:{ all -> 0x036e }] */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02e8 A[Catch:{ all -> 0x036e }] */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0338 A[Catch:{ all -> 0x036e }] */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0354 A[SYNTHETIC, Splitter:B:165:0x0354] */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x035c A[Catch:{ Exception -> 0x0358 }] */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x0361 A[Catch:{ Exception -> 0x0358 }] */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0373 A[SYNTHETIC, Splitter:B:180:0x0373] */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x037b A[Catch:{ Exception -> 0x0377 }] */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x0380 A[Catch:{ Exception -> 0x0377 }] */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0389  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01cc  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01da A[EDGE_INSN: B:72:0x01da->B:73:? ?: BREAK  , SYNTHETIC, Splitter:B:72:0x01da] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01de A[SYNTHETIC, Splitter:B:75:0x01de] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0201 A[Catch:{ Exception -> 0x02a4, all -> 0x029d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean doDefaultTask() {
        /*
            r22 = this;
            r1 = r22
            long r2 = java.lang.System.currentTimeMillis()
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x0040
            java.lang.String r0 = "DownLoadManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "appinfoName=【"
            r4.append(r5)
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r5 = r1.appinfo
            java.lang.String r5 = r5.name
            r4.append(r5)
            java.lang.String r5 = "】 url="
            r4.append(r5)
            java.lang.String r5 = r1.zipUrl
            r4.append(r5)
            java.lang.String r5 = "线程ID="
            r4.append(r5)
            java.lang.Thread r5 = java.lang.Thread.currentThread()
            long r5 = r5.getId()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r4)
        L_0x0040:
            r4 = 1
            r5 = 0
            r6 = 0
            android.app.Application r0 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.io.File r0 = r0.getCacheDir()     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            r7.<init>(r0)     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            boolean r0 = r7.exists()     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            if (r0 != 0) goto L_0x0062
            r7.mkdir()     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.lang.String r0 = "DownLoadManager"
            java.lang.String r8 = "TMP 目录不存在，新建一个tmp目录"
            android.taobao.windvane.util.TaoLog.d(r0, r8)     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
        L_0x0062:
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.lang.String r8 = r1.zipUrl     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            r0.<init>(r8)     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            r8 = r0
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ Exception -> 0x02c4, all -> 0x02bd }
            r0 = 5000(0x1388, float:7.006E-42)
            r8.setConnectTimeout(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r0 = "GET"
            r8.setRequestMethod(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.<init>()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.append(r7)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r7 = java.io.File.separator     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.append(r7)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r7 = r1.zipUrl     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r7 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r7)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.append(r7)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r7.<init>(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            boolean r9 = r7.exists()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            if (r9 != 0) goto L_0x00a2
            r7.mkdir()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
        L_0x00a2:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r7.<init>()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r7.append(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r0 = java.io.File.separator     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r7.append(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r0 = r1.appinfo     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r0 = r0.name     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r7.append(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r0 = ".zip"
            r7.append(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r11 = r7.toString()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.<init>(r11)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            boolean r7 = r0.exists()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            if (r7 == 0) goto L_0x00cd
            r0.delete()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
        L_0x00cd:
            r0.createNewFile()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            int r7 = r8.getContentLength()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            long r9 = (long) r7     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r12 = 500000(0x7a120, double:2.47033E-318)
            int r7 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1))
            if (r7 >= 0) goto L_0x0101
            boolean r7 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            if (r7 == 0) goto L_0x00ff
            java.lang.String r7 = "DownLoadManager"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r9.<init>()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r10 = "isBPDownLoad = false  zipUrl=【"
            r9.append(r10)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r10 = r1.zipUrl     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r9.append(r10)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r10 = "】"
            r9.append(r10)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            android.taobao.windvane.util.TaoLog.d(r7, r9)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
        L_0x00ff:
            r7 = 0
            goto L_0x0102
        L_0x0101:
            r7 = 1
        L_0x0102:
            if (r7 == 0) goto L_0x0175
            java.io.RandomAccessFile r9 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            java.lang.String r10 = "rwd"
            r9.<init>(r0, r10)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            long r12 = r9.length()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r14 = 0
            int r0 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r0 <= 0) goto L_0x0119
            r16 = 1
            long r12 = r12 - r16
        L_0x0119:
            r9.seek(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            int r0 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r0 <= 0) goto L_0x0143
            java.lang.String r0 = "Range"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r10.<init>()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r14 = "bytes="
            r10.append(r14)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r12 = "-"
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r12 = ""
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r8.setRequestProperty(r0, r10)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r8.getContentLength()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
        L_0x0143:
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            if (r0 == 0) goto L_0x0166
            java.lang.String r0 = "DownLoadManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r10.<init>()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r12 = "isBPDownLoad = true  zipUrl=【"
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r12 = r1.zipUrl     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r12 = "】"
            r10.append(r12)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
            android.taobao.windvane.util.TaoLog.d(r0, r10)     // Catch:{ Exception -> 0x016f, all -> 0x0169 }
        L_0x0166:
            r14 = r6
            r15 = r9
            goto L_0x0188
        L_0x0169:
            r0 = move-exception
            r2 = r0
            r14 = r6
            r15 = r9
            goto L_0x0371
        L_0x016f:
            r0 = move-exception
            r13 = r6
            r14 = r13
            r15 = r9
            goto L_0x02c9
        L_0x0175:
            boolean r9 = r0.isDirectory()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            if (r9 == 0) goto L_0x0181
            r0.delete()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r0.createNewFile()     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
        L_0x0181:
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r9.<init>(r0)     // Catch:{ Exception -> 0x02ba, all -> 0x02b6 }
            r15 = r6
            r14 = r9
        L_0x0188:
            java.io.InputStream r0 = r8.getInputStream()     // Catch:{ Exception -> 0x02b2, all -> 0x02ad }
            java.lang.String r9 = r8.getContentEncoding()     // Catch:{ Exception -> 0x02b2, all -> 0x02ad }
            int r16 = r8.getResponseCode()     // Catch:{ Exception -> 0x02b2, all -> 0x02ad }
            if (r9 == 0) goto L_0x01b3
            java.lang.String r10 = "gzip"
            boolean r9 = r10.equals(r9)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
            if (r9 == 0) goto L_0x01b3
            java.util.zip.GZIPInputStream r9 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
            r9.<init>(r0)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
            java.io.DataInputStream r0 = new java.io.DataInputStream     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
            r0.<init>(r9)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
            r13 = r0
            goto L_0x01b9
        L_0x01aa:
            r0 = move-exception
            goto L_0x02af
        L_0x01ad:
            r0 = move-exception
        L_0x01ae:
            r13 = r6
        L_0x01af:
            r5 = r16
            goto L_0x02c9
        L_0x01b3:
            java.io.DataInputStream r9 = new java.io.DataInputStream     // Catch:{ Exception -> 0x02a9, all -> 0x02ad }
            r9.<init>(r0)     // Catch:{ Exception -> 0x02a9, all -> 0x02ad }
            r13 = r9
        L_0x01b9:
            android.taobao.windvane.thread.WVFixedThreadPool r0 = android.taobao.windvane.thread.WVFixedThreadPool.getInstance()     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            android.taobao.windvane.thread.WVFixedThreadPool$BufferWrapper r0 = r0.getTempBuffer()     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
        L_0x01c1:
            byte[] r9 = r0.tempBuffer     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            int r10 = android.taobao.windvane.thread.WVFixedThreadPool.bufferSize     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            int r9 = r13.read(r9, r5, r10)     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            r10 = -1
            if (r9 == r10) goto L_0x01da
            if (r7 == 0) goto L_0x01d4
            byte[] r10 = r0.tempBuffer     // Catch:{ Exception -> 0x01f8 }
            r15.write(r10, r5, r9)     // Catch:{ Exception -> 0x01f8 }
            goto L_0x01c1
        L_0x01d4:
            byte[] r10 = r0.tempBuffer     // Catch:{ Exception -> 0x01f8 }
            r14.write(r10, r5, r9)     // Catch:{ Exception -> 0x01f8 }
            goto L_0x01c1
        L_0x01da:
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r9 = r1.appinfo     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            if (r9 == 0) goto L_0x01fa
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r9 = r1.appinfo     // Catch:{ Exception -> 0x01f8 }
            boolean r9 = r9.isInstantApp     // Catch:{ Exception -> 0x01f8 }
            r10 = 6010(0x177a, float:8.422E-42)
            if (r9 == 0) goto L_0x01f0
            android.taobao.windvane.service.WVEventService r9 = android.taobao.windvane.service.WVEventService.getInstance()     // Catch:{ Exception -> 0x01f8 }
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x01f8 }
            r9.onInstantEvent(r10, r5)     // Catch:{ Exception -> 0x01f8 }
            goto L_0x01fa
        L_0x01f0:
            android.taobao.windvane.service.WVEventService r5 = android.taobao.windvane.service.WVEventService.getInstance()     // Catch:{ Exception -> 0x01f8 }
            r5.onEvent(r10)     // Catch:{ Exception -> 0x01f8 }
            goto L_0x01fa
        L_0x01f8:
            r0 = move-exception
            goto L_0x01af
        L_0x01fa:
            r0.setIsFree(r4)     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            android.taobao.windvane.packageapp.zipdownload.DownLoadListener r0 = r1.listener     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            if (r0 == 0) goto L_0x0283
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            if (r0 == 0) goto L_0x023a
            java.lang.String r0 = "DownLoadManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f8 }
            r5.<init>()     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r9 = "zipUrl =【"
            r5.append(r9)     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r9 = r1.zipUrl     // Catch:{ Exception -> 0x01f8 }
            r5.append(r9)     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r9 = "】  下载耗时=【"
            r5.append(r9)     // Catch:{ Exception -> 0x01f8 }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01f8 }
            r12 = 0
            long r9 = r9 - r2
            r5.append(r9)     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r2 = "】isBPDownLoad  =【"
            r5.append(r2)     // Catch:{ Exception -> 0x01f8 }
            r5.append(r7)     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r2 = "】"
            r5.append(r2)     // Catch:{ Exception -> 0x01f8 }
            java.lang.String r2 = r5.toString()     // Catch:{ Exception -> 0x01f8 }
            android.taobao.windvane.util.TaoLog.d(r0, r2)     // Catch:{ Exception -> 0x01f8 }
        L_0x023a:
            android.taobao.windvane.packageapp.zipdownload.DownLoadListener r9 = r1.listener     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            java.lang.String r10 = r1.zipUrl     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            java.util.HashMap r12 = new java.util.HashMap     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            r12.<init>()     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            int r0 = r1.token     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            java.lang.Object r2 = r1.obj     // Catch:{ Exception -> 0x02a4, all -> 0x029d }
            r3 = r13
            r13 = r0
            r7 = r14
            r14 = r2
            r9.callback(r10, r11, r12, r13, r14)     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            boolean r0 = r1.isInstantTask     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            if (r0 == 0) goto L_0x0285
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r0 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            java.lang.Object r2 = r0.lock     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            monitor-enter(r2)     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r0 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ all -> 0x0275 }
            java.lang.Object r0 = r0.lock     // Catch:{ all -> 0x0275 }
            r0.notify()     // Catch:{ all -> 0x0275 }
            monitor-exit(r2)     // Catch:{ all -> 0x0275 }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r0 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r2 = r1.appinfo     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            java.lang.String r2 = r2.name     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            r0.removeByName(r2)     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r0 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            r0.instantTaskName = r6     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
            goto L_0x0285
        L_0x0275:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0275 }
            throw r0     // Catch:{ Exception -> 0x027e, all -> 0x0278 }
        L_0x0278:
            r0 = move-exception
            r2 = r0
            r6 = r3
            r14 = r7
            goto L_0x0371
        L_0x027e:
            r0 = move-exception
            r13 = r3
            r14 = r7
            goto L_0x01af
        L_0x0283:
            r3 = r13
            r7 = r14
        L_0x0285:
            if (r15 == 0) goto L_0x028d
            r15.close()     // Catch:{ Exception -> 0x028b }
            goto L_0x028d
        L_0x028b:
            r0 = move-exception
            goto L_0x0296
        L_0x028d:
            r3.close()     // Catch:{ Exception -> 0x028b }
            if (r7 == 0) goto L_0x0299
            r7.close()     // Catch:{ Exception -> 0x028b }
            goto L_0x0299
        L_0x0296:
            r0.printStackTrace()
        L_0x0299:
            if (r8 == 0) goto L_0x036d
            goto L_0x036a
        L_0x029d:
            r0 = move-exception
            r3 = r13
            r7 = r14
            r2 = r0
            r6 = r3
            goto L_0x0371
        L_0x02a4:
            r0 = move-exception
            r3 = r13
            r7 = r14
            goto L_0x01af
        L_0x02a9:
            r0 = move-exception
            r7 = r14
            goto L_0x01ae
        L_0x02ad:
            r0 = move-exception
            r7 = r14
        L_0x02af:
            r2 = r0
            goto L_0x0371
        L_0x02b2:
            r0 = move-exception
            r7 = r14
            r13 = r6
            goto L_0x02c9
        L_0x02b6:
            r0 = move-exception
            r2 = r0
            r14 = r6
            goto L_0x02c1
        L_0x02ba:
            r0 = move-exception
            r13 = r6
            goto L_0x02c7
        L_0x02bd:
            r0 = move-exception
            r2 = r0
            r8 = r6
            r14 = r8
        L_0x02c1:
            r15 = r14
            goto L_0x0371
        L_0x02c4:
            r0 = move-exception
            r8 = r6
            r13 = r8
        L_0x02c7:
            r14 = r13
            r15 = r14
        L_0x02c9:
            android.taobao.windvane.packageapp.zipdownload.DownLoadListener r2 = r1.listener     // Catch:{ all -> 0x036e }
            if (r2 == 0) goto L_0x02e4
            android.taobao.windvane.packageapp.zipdownload.DownLoadListener r2 = r1.listener     // Catch:{ all -> 0x036e }
            java.lang.String r3 = r1.zipUrl     // Catch:{ all -> 0x036e }
            r18 = 0
            r19 = 0
            int r7 = r1.token     // Catch:{ all -> 0x036e }
            java.lang.Object r9 = r1.obj     // Catch:{ all -> 0x036e }
            r16 = r2
            r17 = r3
            r20 = r7
            r21 = r9
            r16.callback(r17, r18, r19, r20, r21)     // Catch:{ all -> 0x036e }
        L_0x02e4:
            boolean r2 = r1.isInstantTask     // Catch:{ all -> 0x036e }
            if (r2 == 0) goto L_0x030e
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r2 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ all -> 0x036e }
            java.lang.Object r2 = r2.lock     // Catch:{ all -> 0x036e }
            monitor-enter(r2)     // Catch:{ all -> 0x036e }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r3 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ all -> 0x030b }
            java.lang.Object r3 = r3.lock     // Catch:{ all -> 0x030b }
            r3.notify()     // Catch:{ all -> 0x030b }
            monitor-exit(r2)     // Catch:{ all -> 0x030b }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r2 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ all -> 0x036e }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r3 = r1.appinfo     // Catch:{ all -> 0x036e }
            java.lang.String r3 = r3.name     // Catch:{ all -> 0x036e }
            r2.removeByName(r3)     // Catch:{ all -> 0x036e }
            android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue r2 = android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue.getInstance()     // Catch:{ all -> 0x036e }
            r2.instantTaskName = r6     // Catch:{ all -> 0x036e }
            goto L_0x030e
        L_0x030b:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x030b }
            throw r0     // Catch:{ all -> 0x036e }
        L_0x030e:
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r2 = r1.appinfo     // Catch:{ all -> 0x036e }
            int r3 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_DOWN_ZIP     // Catch:{ all -> 0x036e }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x036e }
            r6.<init>()     // Catch:{ all -> 0x036e }
            java.lang.String r7 = "httpcode = "
            r6.append(r7)     // Catch:{ all -> 0x036e }
            r6.append(r5)     // Catch:{ all -> 0x036e }
            java.lang.String r5 = " ErrorMsg = ErrorMsg : "
            r6.append(r5)     // Catch:{ all -> 0x036e }
            java.lang.String r5 = r0.getMessage()     // Catch:{ all -> 0x036e }
            r6.append(r5)     // Catch:{ all -> 0x036e }
            java.lang.String r5 = r6.toString()     // Catch:{ all -> 0x036e }
            android.taobao.windvane.packageapp.monitor.AppInfoMonitor.error(r2, r3, r5)     // Catch:{ all -> 0x036e }
            boolean r2 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x036e }
            if (r2 == 0) goto L_0x0352
            java.lang.String r2 = "DownLoadManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x036e }
            r3.<init>()     // Catch:{ all -> 0x036e }
            java.lang.String r5 = "WVZipBPDownloader  Exception : "
            r3.append(r5)     // Catch:{ all -> 0x036e }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x036e }
            r3.append(r0)     // Catch:{ all -> 0x036e }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x036e }
            android.taobao.windvane.util.TaoLog.e(r2, r0)     // Catch:{ all -> 0x036e }
        L_0x0352:
            if (r15 == 0) goto L_0x035a
            r15.close()     // Catch:{ Exception -> 0x0358 }
            goto L_0x035a
        L_0x0358:
            r0 = move-exception
            goto L_0x0365
        L_0x035a:
            if (r13 == 0) goto L_0x035f
            r13.close()     // Catch:{ Exception -> 0x0358 }
        L_0x035f:
            if (r14 == 0) goto L_0x0368
            r14.close()     // Catch:{ Exception -> 0x0358 }
            goto L_0x0368
        L_0x0365:
            r0.printStackTrace()
        L_0x0368:
            if (r8 == 0) goto L_0x036d
        L_0x036a:
            r8.disconnect()
        L_0x036d:
            return r4
        L_0x036e:
            r0 = move-exception
            r2 = r0
            r6 = r13
        L_0x0371:
            if (r15 == 0) goto L_0x0379
            r15.close()     // Catch:{ Exception -> 0x0377 }
            goto L_0x0379
        L_0x0377:
            r0 = move-exception
            goto L_0x0384
        L_0x0379:
            if (r6 == 0) goto L_0x037e
            r6.close()     // Catch:{ Exception -> 0x0377 }
        L_0x037e:
            if (r14 == 0) goto L_0x0387
            r14.close()     // Catch:{ Exception -> 0x0377 }
            goto L_0x0387
        L_0x0384:
            r0.printStackTrace()
        L_0x0387:
            if (r8 == 0) goto L_0x038c
            r8.disconnect()
        L_0x038c:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipdownload.DownLoadManager.doDefaultTask():boolean");
    }
}
