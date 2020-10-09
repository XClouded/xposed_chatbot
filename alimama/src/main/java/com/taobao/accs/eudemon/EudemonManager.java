package com.taobao.accs.eudemon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

public class EudemonManager implements Handler.Callback {
    public static final int ACT_START = 0;
    public static final int ACT_STOP = -1;
    public static final String AGOO_PID = "agoo.pid";
    public static String AMPARAMS = "startservice -n {packname}/com.taobao.accs.ChannelService";
    private static final int BUF_SIZE = 100;
    private static final String DAEMON_STAT_FILE = "eudemon";
    public static final String EX_FILE_NAME = "DaemonServer";
    private static final String PID = "daemonserver.pid";
    private static final String PKG_INSTALL_DIR = "/data/data/";
    public static final String PROCESS_NAME = "runServer";
    private static final String TAG = "com.taobao.accs.eudemon.EudemonManager";
    private static final ReentrantLock lock = new ReentrantLock();
    private static EudemonManager soManager = null;
    private static int timeoutAlarmDay = 2500;
    private static int timeoutAlarmNight = 7200;
    private String abi;
    private String appKey = "21646297";
    private String checkPackagePath = "";
    public boolean debugMode = false;
    private HandlerThread handerThread = null;
    private Handler hanlder = null;
    private boolean isTransparentProxy = true;
    private Context mContext = null;
    private String reportKey = "100001";
    private String reportLoc = "http://100.69.165.28/agoo/report";
    private int sdkVersion = 0;
    private String serverIp = "100.69.165.28";
    private int serverPort = 80;
    private int timeout = 1800;
    private String ua = "tb_accs_eudemon_1.1.3";

    public EudemonManager(Context context, int i, boolean z) {
        initHandler();
        AMPARAMS = "startservice -n {packname}/com.taobao.accs.ChannelService";
        this.mContext = context;
        this.timeout = i;
        this.debugMode = z;
        this.abi = getFieldReflectively(new Build(), "CPU_ABI");
        this.checkPackagePath = PKG_INSTALL_DIR + context.getPackageName();
        this.sdkVersion = Constants.SDK_VERSION_CODE;
        String[] appkey = UtilityImpl.getAppkey(this.mContext);
        this.appKey = (appkey == null || appkey.length == 0) ? "" : appkey[0];
        if (Utils.getMode(context) == 0) {
            this.serverIp = "agoodm.m.taobao.com";
            this.serverPort = 80;
            this.reportLoc = "http://agoodm.m.taobao.com/agoo/report";
            this.reportKey = "1009527";
        } else if (Utils.getMode(context) == 1) {
            this.serverIp = "110.75.98.154";
            this.serverPort = 80;
            this.reportLoc = "http://agoodm.wapa.taobao.com/agoo/report";
            this.reportKey = "1009527";
        } else {
            this.serverIp = "100.69.168.33";
            this.serverPort = 80;
            this.reportLoc = "http://100.69.168.33/agoo/report";
            this.timeout = 60;
            this.reportKey = "9527";
        }
    }

    private void initHandler() {
        Log.d(TAG, "start handler init");
        this.handerThread = new HandlerThread("soManager-threads");
        this.handerThread.setPriority(5);
        this.handerThread.start();
        this.hanlder = new Handler(this.handerThread.getLooper(), this);
    }

    private String getAbiPath() {
        if (this.abi.startsWith("arm")) {
            return "armeabi/";
        }
        return this.abi + "/";
    }

    private static String getFieldReflectively(Build build, String str) {
        try {
            return Build.class.getField(str).get(build).toString();
        } catch (Throwable unused) {
            return "Unknown";
        }
    }

    public static EudemonManager getInstance(Context context, int i, boolean z) {
        try {
            lock.lock();
            if (soManager == null) {
                soManager = new EudemonManager(context, i, z);
            }
        } catch (Exception e) {
            ALog.e(TAG, "getInstance", e, new Object[0]);
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
        lock.unlock();
        return soManager;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a9 A[SYNTHETIC, Splitter:B:35:0x00a9] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c1 A[SYNTHETIC, Splitter:B:44:0x00c1] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:39:0x00b7=Splitter:B:39:0x00b7, B:26:0x008c=Splitter:B:26:0x008c} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String copyBinToFiles() throws java.io.IOException {
        /*
            r9 = this;
            java.io.File r0 = new java.io.File
            android.content.Context r1 = r9.mContext
            java.io.File r1 = r1.getFilesDir()
            java.lang.String r2 = "DaemonServer"
            r0.<init>(r1, r2)
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x0016
            r0.delete()
        L_0x0016:
            java.lang.String r1 = TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "open assets from = "
            r2.append(r3)
            java.lang.String r3 = r9.getAbiPath()
            r2.append(r3)
            java.lang.String r3 = "DaemonServer"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.w(r1, r2, r4)
            java.io.FileOutputStream r1 = new java.io.FileOutputStream
            r1.<init>(r0)
            r2 = 0
            boolean r4 = r9.debugMode     // Catch:{ Exception -> 0x009d }
            if (r4 == 0) goto L_0x0079
            android.content.Context r4 = r9.mContext     // Catch:{ Exception -> 0x009d }
            android.content.res.AssetManager r4 = r4.getAssets()     // Catch:{ Exception -> 0x009d }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x009d }
            r5.<init>()     // Catch:{ Exception -> 0x009d }
            java.lang.String r6 = r9.getAbiPath()     // Catch:{ Exception -> 0x009d }
            r5.append(r6)     // Catch:{ Exception -> 0x009d }
            java.lang.String r6 = "DaemonServer"
            r5.append(r6)     // Catch:{ Exception -> 0x009d }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x009d }
            java.io.InputStream r4 = r4.open(r5)     // Catch:{ Exception -> 0x009d }
            r2 = 100
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0074, all -> 0x0071 }
        L_0x0065:
            int r5 = r4.read(r2)     // Catch:{ Exception -> 0x0074, all -> 0x0071 }
            if (r5 <= 0) goto L_0x006f
            r1.write(r2, r3, r5)     // Catch:{ Exception -> 0x0074, all -> 0x0071 }
            goto L_0x0065
        L_0x006f:
            r2 = r4
            goto L_0x007c
        L_0x0071:
            r0 = move-exception
            r2 = r4
            goto L_0x00bf
        L_0x0074:
            r2 = move-exception
            r8 = r4
            r4 = r2
            r2 = r8
            goto L_0x009e
        L_0x0079:
            r9.writeFileInRelease(r1, r0)     // Catch:{ Exception -> 0x009d }
        L_0x007c:
            if (r2 == 0) goto L_0x008c
            r2.close()     // Catch:{ IOException -> 0x0082 }
            goto L_0x008c
        L_0x0082:
            r2 = move-exception
            java.lang.String r4 = TAG
            java.lang.String r5 = "error in close input file"
            java.lang.Object[] r6 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.e(r4, r5, r2, r6)
        L_0x008c:
            r1.close()     // Catch:{ IOException -> 0x0090 }
            goto L_0x00ba
        L_0x0090:
            r1 = move-exception
            java.lang.String r2 = TAG
            java.lang.String r4 = "error in close io"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.e(r2, r4, r1, r3)
            goto L_0x00ba
        L_0x009b:
            r0 = move-exception
            goto L_0x00bf
        L_0x009d:
            r4 = move-exception
        L_0x009e:
            java.lang.String r5 = TAG     // Catch:{ all -> 0x009b }
            java.lang.String r6 = "error in copy daemon files"
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ all -> 0x009b }
            com.taobao.accs.utl.ALog.e(r5, r6, r4, r7)     // Catch:{ all -> 0x009b }
            if (r2 == 0) goto L_0x00b7
            r2.close()     // Catch:{ IOException -> 0x00ad }
            goto L_0x00b7
        L_0x00ad:
            r2 = move-exception
            java.lang.String r4 = TAG
            java.lang.String r5 = "error in close input file"
            java.lang.Object[] r6 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.e(r4, r5, r2, r6)
        L_0x00b7:
            r1.close()     // Catch:{ IOException -> 0x0090 }
        L_0x00ba:
            java.lang.String r0 = r0.getCanonicalPath()
            return r0
        L_0x00bf:
            if (r2 == 0) goto L_0x00cf
            r2.close()     // Catch:{ IOException -> 0x00c5 }
            goto L_0x00cf
        L_0x00c5:
            r2 = move-exception
            java.lang.String r4 = TAG
            java.lang.Object[] r5 = new java.lang.Object[r3]
            java.lang.String r6 = "error in close input file"
            com.taobao.accs.utl.ALog.e(r4, r6, r2, r5)
        L_0x00cf:
            r1.close()     // Catch:{ IOException -> 0x00d3 }
            goto L_0x00dd
        L_0x00d3:
            r1 = move-exception
            java.lang.String r2 = TAG
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r4 = "error in close io"
            com.taobao.accs.utl.ALog.e(r2, r4, r1, r3)
        L_0x00dd:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.eudemon.EudemonManager.copyBinToFiles():java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a8 A[SYNTHETIC, Splitter:B:30:0x00a8] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00ba A[SYNTHETIC, Splitter:B:36:0x00ba] */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeFileInRelease(java.io.FileOutputStream r9, java.io.File r10) throws java.io.IOException {
        /*
            r8 = this;
            java.lang.String r0 = r8.abi
            java.lang.String r0 = com.taobao.accs.eudemon.SoData.getData(r0)
            java.lang.String r1 = TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ">>>soDataSize:datasize:"
            r2.append(r3)
            int r3 = r0.length()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.d(r1, r2, r4)
            byte[] r0 = r0.getBytes()
            byte[] r0 = android.util.Base64.decode(r0, r3)
            java.lang.String r1 = TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = ">>>soDataSize:"
            r2.append(r4)
            int r4 = r0.length
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            java.lang.Object[] r4 = new java.lang.Object[r3]
            com.taobao.accs.utl.ALog.d(r1, r2, r4)
            int r1 = r0.length
            if (r1 > 0) goto L_0x0048
            return
        L_0x0048:
            if (r9 != 0) goto L_0x004b
            return
        L_0x004b:
            r1 = 0
            android.os.StatFs r2 = new android.os.StatFs
            java.lang.String r10 = r10.getCanonicalPath()
            r2.<init>(r10)
            int r10 = r2.getBlockSize()
            int r2 = r2.getAvailableBlocks()
            long r4 = (long) r2
            long r6 = (long) r10
            long r6 = r6 * r4
            int r10 = r0.length
            long r4 = (long) r10
            int r10 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r10 >= 0) goto L_0x006f
            java.lang.String r9 = TAG
            java.lang.String r10 = "Disk is not enough for writing this file"
            android.util.Log.e(r9, r10)
            return
        L_0x006f:
            java.io.ByteArrayInputStream r10 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0095 }
            r10.<init>(r0)     // Catch:{ IOException -> 0x0095 }
            r0 = 100
            byte[] r1 = new byte[r0]     // Catch:{ IOException -> 0x0090, all -> 0x008d }
        L_0x0078:
            int r2 = r10.read(r1, r3, r0)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            if (r2 < 0) goto L_0x0082
            r9.write(r1, r3, r2)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            goto L_0x0078
        L_0x0082:
            java.io.FileDescriptor r9 = r9.getFD()
            r9.sync()
            r10.close()     // Catch:{ IOException -> 0x00ac }
            goto L_0x00b0
        L_0x008d:
            r0 = move-exception
            r1 = r10
            goto L_0x00b1
        L_0x0090:
            r0 = move-exception
            r1 = r10
            goto L_0x0096
        L_0x0093:
            r0 = move-exception
            goto L_0x00b1
        L_0x0095:
            r0 = move-exception
        L_0x0096:
            java.lang.String r10 = TAG     // Catch:{ all -> 0x0093 }
            java.lang.String r2 = "error in write files"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0093 }
            com.taobao.accs.utl.ALog.e(r10, r2, r0, r3)     // Catch:{ all -> 0x0093 }
            java.io.FileDescriptor r9 = r9.getFD()
            r9.sync()
            if (r1 == 0) goto L_0x00b0
            r1.close()     // Catch:{ IOException -> 0x00ac }
            goto L_0x00b0
        L_0x00ac:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00b0:
            return
        L_0x00b1:
            java.io.FileDescriptor r9 = r9.getFD()
            r9.sync()
            if (r1 == 0) goto L_0x00c2
            r1.close()     // Catch:{ IOException -> 0x00be }
            goto L_0x00c2
        L_0x00be:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00c2:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.eudemon.EudemonManager.writeFileInRelease(java.io.FileOutputStream, java.io.File):void");
    }

    private void lauchIt(String str) {
        StringBuilder sb = new StringBuilder();
        execShell("", "chmod 500 " + str, sb);
        execShell("", str + Operators.SPACE_STR + getBinParam(), sb);
        String str2 = TAG;
        ALog.w(str2, str + Operators.SPACE_STR + getBinParam(), new Object[0]);
    }

    private String getBinParam() {
        StringBuilder sb = new StringBuilder();
        String str = PKG_INSTALL_DIR + this.mContext.getPackageName();
        sb.append("-s \"");
        sb.append(str);
        sb.append("/lib/");
        sb.append("\" ");
        sb.append("-n \"runServer\" ");
        sb.append("-p \"");
        sb.append(getAmParams());
        sb.append("\" ");
        sb.append("-f \"");
        sb.append(str);
        sb.append("\" ");
        sb.append("-t \"");
        sb.append(this.timeout);
        sb.append("\" ");
        sb.append("-c \"agoo.pid\" ");
        if (this.checkPackagePath != null) {
            sb.append("-P ");
            sb.append(this.checkPackagePath);
            sb.append(Operators.SPACE_STR);
        }
        if (this.reportKey != null) {
            sb.append("-K ");
            sb.append(this.reportKey);
            sb.append(Operators.SPACE_STR);
        }
        if (this.ua != null) {
            sb.append("-U ");
            sb.append(this.ua);
            sb.append(Operators.SPACE_STR);
        }
        if (this.reportLoc != null) {
            sb.append("-L ");
            sb.append(this.reportLoc);
            sb.append(Operators.SPACE_STR);
        }
        sb.append("-D ");
        sb.append(getReportData());
        sb.append(Operators.SPACE_STR);
        if (this.serverIp != null) {
            sb.append("-I ");
            sb.append(this.serverIp);
            sb.append(Operators.SPACE_STR);
        }
        if (this.serverPort > 0) {
            sb.append("-O ");
            sb.append(this.serverPort);
            sb.append(Operators.SPACE_STR);
        }
        String proxyHost = UtilityImpl.getProxyHost(this.mContext);
        int proxyPort = UtilityImpl.getProxyPort(this.mContext);
        if (proxyHost != null && proxyPort > 0) {
            sb.append("-X ");
            sb.append(proxyHost);
            sb.append(Operators.SPACE_STR);
            sb.append("-Y ");
            sb.append(proxyPort);
            sb.append(Operators.SPACE_STR);
        }
        if (this.isTransparentProxy) {
            sb.append("-T ");
        }
        sb.append("-Z ");
        return sb.toString();
    }

    private String getReportData() {
        String deviceId = UtilityImpl.getDeviceId(this.mContext);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = BuildConfig.buildJavascriptFrameworkVersion;
        }
        String str = "{\"package\":\"" + this.mContext.getPackageName() + "\",\"appKey\":\"" + this.appKey + "\",\"utdid\":\"" + deviceId + "\",\"sdkVersion\":\"" + this.sdkVersion + "\"}";
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable unused) {
            ALog.e(TAG, "getReportData failed for url encode, data:" + str, new Object[0]);
            return str;
        }
    }

    private String getAmParams() {
        StringBuilder sb = new StringBuilder();
        sb.append(AMPARAMS.replaceAll("\\{packname\\}", this.mContext.getApplicationContext().getPackageName()));
        if (Build.VERSION.SDK_INT > 15) {
            sb.append(" --user 0");
        }
        return sb.toString();
    }

    private void doReportDaemonStat(String str, int i, int i2, String str2, String str3, int i3) {
        String str4 = "AndroidVer=" + Build.VERSION.RELEASE + "&Model=" + Build.MODEL + "&AndroidSdk=" + Build.VERSION.SDK_INT + "&AccsVer=" + Constants.SDK_VERSION_CODE + "&Appkey=" + this.appKey + "&PullCount=" + str2 + "&Pid=" + str + "&StartTime=" + i + "&EndTime=" + i2 + "&ExitCode=" + str3 + "&AliveTime=" + i3;
        Log.d(TAG, "EUDEMON_ENDSTAT doReportDaemonStat:" + str4);
        UTMini.getInstance().commitEvent(66001, "EUDEMON_ENDSTAT", str4);
    }

    /* JADX WARNING: Removed duplicated region for block: B:102:0x01b8 A[SYNTHETIC, Splitter:B:102:0x01b8] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01c7 A[SYNTHETIC, Splitter:B:107:0x01c7] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x01d6 A[SYNTHETIC, Splitter:B:112:0x01d6] */
    /* JADX WARNING: Removed duplicated region for block: B:122:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0169 A[SYNTHETIC, Splitter:B:75:0x0169] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0178 A[SYNTHETIC, Splitter:B:80:0x0178] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0187 A[SYNTHETIC, Splitter:B:85:0x0187] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0196 A[SYNTHETIC, Splitter:B:90:0x0196] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01a9 A[SYNTHETIC, Splitter:B:97:0x01a9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reportDaemonStat() {
        /*
            r18 = this;
            r8 = r18
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "/data/data/"
            r0.append(r1)
            android.content.Context r1 = r8.mContext
            java.lang.String r1 = r1.getPackageName()
            r0.append(r1)
            java.lang.String r1 = "/"
            r0.append(r1)
            java.lang.String r1 = "eudemon"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r2 = r1.exists()
            if (r2 != 0) goto L_0x002f
            return
        L_0x002f:
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0159, all -> 0x0150 }
            r10.<init>(r1)     // Catch:{ Exception -> 0x0159, all -> 0x0150 }
            java.io.InputStreamReader r11 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0148, all -> 0x013e }
            r11.<init>(r10)     // Catch:{ Exception -> 0x0148, all -> 0x013e }
            java.io.BufferedReader r12 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0139, all -> 0x0134 }
            r12.<init>(r11)     // Catch:{ Exception -> 0x0139, all -> 0x0134 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r13.<init>()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
        L_0x0043:
            java.lang.String r1 = r12.readLine()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            if (r1 == 0) goto L_0x00d5
            java.lang.String r2 = "\\|"
            java.lang.String[] r2 = r1.split(r2)     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            int r3 = r2.length     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r4 = 5
            if (r3 == r4) goto L_0x0054
            goto L_0x0043
        L_0x0054:
            r3 = 0
            r3 = r2[r3]     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r3 = r3.trim()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r4 = 1
            r4 = r2[r4]     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r4 = r4.trim()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r5 = 2
            r6 = r2[r5]     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r6 = r6.trim()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            int r6 = r6.intValue()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            int r7 = r6 - r4
            r14 = 3
            r14 = r2[r14]     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r14 = r14.trim()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r15 = 4
            r2 = r2[r15]     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r15 = r2.trim()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r2 = "0"
            boolean r2 = r15.equals(r2)     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            if (r2 == 0) goto L_0x00c5
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r9 = "/proc"
            r2.<init>(r9, r3)     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.String r9 = TAG     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r5.<init>()     // Catch:{ Exception -> 0x012f, all -> 0x012a }
            r17 = r10
            java.lang.String r10 = "pidfile:"
            r5.append(r10)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            r5.append(r2)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            android.util.Log.e(r9, r5)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            if (r2 == 0) goto L_0x00bf
            r13.append(r1)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            java.lang.String r1 = "\n"
            r13.append(r1)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            goto L_0x00d1
        L_0x00bf:
            int r1 = r8.timeout     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            r2 = 2
            int r1 = r1 / r2
            int r7 = r7 + r1
            goto L_0x00c7
        L_0x00c5:
            r17 = r10
        L_0x00c7:
            r1 = r18
            r2 = r3
            r3 = r4
            r4 = r6
            r5 = r14
            r6 = r15
            r1.doReportDaemonStat(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
        L_0x00d1:
            r10 = r17
            goto L_0x0043
        L_0x00d5:
            r17 = r10
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x0128, all -> 0x0126 }
            java.lang.String r0 = r13.toString()     // Catch:{ Exception -> 0x0121, all -> 0x011b }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x0121, all -> 0x011b }
            r9.write(r0)     // Catch:{ Exception -> 0x0121, all -> 0x011b }
            r12.close()     // Catch:{ Exception -> 0x0121, all -> 0x011b }
            r12.close()     // Catch:{ Throwable -> 0x00f3 }
            goto L_0x00fc
        L_0x00f3:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close buffreader stream"
            android.util.Log.e(r0, r2, r1)
        L_0x00fc:
            r11.close()     // Catch:{ Throwable -> 0x0100 }
            goto L_0x0109
        L_0x0100:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close reader stream"
            android.util.Log.e(r0, r2, r1)
        L_0x0109:
            r17.close()     // Catch:{ IOException -> 0x010d }
            goto L_0x0116
        L_0x010d:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close input file"
            android.util.Log.e(r0, r2, r1)
        L_0x0116:
            r9.close()     // Catch:{ IOException -> 0x019a }
            goto L_0x01a3
        L_0x011b:
            r0 = move-exception
            r1 = r0
            r16 = r9
            goto L_0x01a7
        L_0x0121:
            r0 = move-exception
            r16 = r9
            r9 = r12
            goto L_0x0160
        L_0x0126:
            r0 = move-exception
            goto L_0x012d
        L_0x0128:
            r0 = move-exception
            goto L_0x0132
        L_0x012a:
            r0 = move-exception
            r17 = r10
        L_0x012d:
            r1 = r0
            goto L_0x0144
        L_0x012f:
            r0 = move-exception
            r17 = r10
        L_0x0132:
            r9 = r12
            goto L_0x014d
        L_0x0134:
            r0 = move-exception
            r17 = r10
            r1 = r0
            goto L_0x0143
        L_0x0139:
            r0 = move-exception
            r17 = r10
            r9 = 0
            goto L_0x014d
        L_0x013e:
            r0 = move-exception
            r17 = r10
            r1 = r0
            r11 = 0
        L_0x0143:
            r12 = 0
        L_0x0144:
            r16 = 0
            goto L_0x01a7
        L_0x0148:
            r0 = move-exception
            r17 = r10
            r9 = 0
            r11 = 0
        L_0x014d:
            r16 = 0
            goto L_0x0160
        L_0x0150:
            r0 = move-exception
            r1 = r0
            r11 = 0
            r12 = 0
            r16 = 0
            r17 = 0
            goto L_0x01a7
        L_0x0159:
            r0 = move-exception
            r9 = 0
            r11 = 0
            r16 = 0
            r17 = 0
        L_0x0160:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x01a4 }
            java.lang.String r2 = "report daemon stat exp:"
            android.util.Log.e(r1, r2, r0)     // Catch:{ all -> 0x01a4 }
            if (r9 == 0) goto L_0x0176
            r9.close()     // Catch:{ Throwable -> 0x016d }
            goto L_0x0176
        L_0x016d:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close buffreader stream"
            android.util.Log.e(r0, r2, r1)
        L_0x0176:
            if (r11 == 0) goto L_0x0185
            r11.close()     // Catch:{ Throwable -> 0x017c }
            goto L_0x0185
        L_0x017c:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close reader stream"
            android.util.Log.e(r0, r2, r1)
        L_0x0185:
            if (r17 == 0) goto L_0x0194
            r17.close()     // Catch:{ IOException -> 0x018b }
            goto L_0x0194
        L_0x018b:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close input file"
            android.util.Log.e(r0, r2, r1)
        L_0x0194:
            if (r16 == 0) goto L_0x01a3
            r16.close()     // Catch:{ IOException -> 0x019a }
            goto L_0x01a3
        L_0x019a:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = TAG
            java.lang.String r2 = "error in close input file"
            android.util.Log.e(r0, r2, r1)
        L_0x01a3:
            return
        L_0x01a4:
            r0 = move-exception
            r1 = r0
            r12 = r9
        L_0x01a7:
            if (r12 == 0) goto L_0x01b6
            r12.close()     // Catch:{ Throwable -> 0x01ad }
            goto L_0x01b6
        L_0x01ad:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = TAG
            java.lang.String r3 = "error in close buffreader stream"
            android.util.Log.e(r0, r3, r2)
        L_0x01b6:
            if (r11 == 0) goto L_0x01c5
            r11.close()     // Catch:{ Throwable -> 0x01bc }
            goto L_0x01c5
        L_0x01bc:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = TAG
            java.lang.String r3 = "error in close reader stream"
            android.util.Log.e(r0, r3, r2)
        L_0x01c5:
            if (r17 == 0) goto L_0x01d4
            r17.close()     // Catch:{ IOException -> 0x01cb }
            goto L_0x01d4
        L_0x01cb:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = TAG
            java.lang.String r3 = "error in close input file"
            android.util.Log.e(r0, r3, r2)
        L_0x01d4:
            if (r16 == 0) goto L_0x01e3
            r16.close()     // Catch:{ IOException -> 0x01da }
            goto L_0x01e3
        L_0x01da:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = TAG
            java.lang.String r3 = "error in close input file"
            android.util.Log.e(r0, r3, r2)
        L_0x01e3:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.eudemon.EudemonManager.reportDaemonStat():void");
    }

    public void start() {
        Log.d(TAG, "start SoManager");
        Message obtain = Message.obtain();
        obtain.what = 0;
        this.hanlder.sendMessage(obtain);
    }

    private void startInternal() {
        String str = TAG;
        ALog.d(str, "api level is:" + Build.VERSION.SDK_INT, new Object[0]);
        createAlarm(this.mContext);
        if (Build.VERSION.SDK_INT < 20) {
            try {
                String copyBinToFiles = copyBinToFiles();
                reportDaemonStat();
                lauchIt(copyBinToFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UTMini.getInstance().commitEvent(66001, "EUDEMON_START", "");
    }

    public void stop() {
        Log.d(TAG, "stop SoManager");
        Message obtain = Message.obtain();
        obtain.what = -1;
        this.hanlder.sendMessage(obtain);
    }

    private void stopInternal() {
        File file = new File(PKG_INSTALL_DIR + this.mContext.getPackageName(), PID);
        if (file.exists()) {
            file.delete();
        }
    }

    public static final PendingIntent getIntentForWakeup(Context context) {
        Intent intent = new Intent();
        intent.setAction(context.getApplicationContext().getPackageName() + ".intent.action.COCKROACH");
        intent.putExtra("cockroach", "cockroach-PPreotect");
        intent.putExtra("pack", context.getApplicationContext().getPackageName());
        return PendingIntent.getService(context, 0, intent, 134217728);
    }

    public static void createAlarm(Context context) {
        int i = Calendar.getInstance().get(11);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager != null) {
            PendingIntent intentForWakeup = getIntentForWakeup(context);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (i > 23 || i < 6) {
                ALog.w(TAG, "time is night, do not wakeup cpu", new Object[0]);
                createNightAlarm(context, alarmManager, intentForWakeup, elapsedRealtime);
                return;
            }
            ALog.w(TAG, "time is daytime, wakeup cpu for keeping connecntion", new Object[0]);
            createDayAlarm(context, alarmManager, intentForWakeup, elapsedRealtime);
        }
    }

    private static void createDayAlarm(Context context, AlarmManager alarmManager, PendingIntent pendingIntent, long j) {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(2, ((long) (timeoutAlarmDay * 1000)) + j, (long) (timeoutAlarmDay * 1000), pendingIntent);
        }
    }

    private static void createNightAlarm(Context context, AlarmManager alarmManager, PendingIntent pendingIntent, long j) {
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(3, j + ((long) (timeoutAlarmNight * 1000)), (long) (timeoutAlarmNight * 1000), pendingIntent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0061 A[SYNTHETIC, Splitter:B:21:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0073 A[SYNTHETIC, Splitter:B:27:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void checkAndRenewPidFile(android.content.Context r5) {
        /*
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0082 }
            java.io.File r5 = r5.getFilesDir()     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r2 = "agoo.pid"
            r1.<init>(r5, r2)     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r5 = TAG     // Catch:{ Throwable -> 0x0082 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0082 }
            r2.<init>()     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r3 = "pid path:"
            r2.append(r3)     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r3 = r1.getAbsolutePath()     // Catch:{ Throwable -> 0x0082 }
            r2.append(r3)     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0082 }
            java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0082 }
            com.taobao.accs.utl.ALog.d(r5, r2, r3)     // Catch:{ Throwable -> 0x0082 }
            boolean r5 = r1.exists()     // Catch:{ Throwable -> 0x0082 }
            if (r5 == 0) goto L_0x0031
            r1.delete()     // Catch:{ Throwable -> 0x0082 }
        L_0x0031:
            r1.createNewFile()     // Catch:{ Throwable -> 0x0082 }
            r5 = 0
            int r2 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0053, all -> 0x004f }
            java.io.FileWriter r3 = new java.io.FileWriter     // Catch:{ Throwable -> 0x0053, all -> 0x004f }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x0053, all -> 0x004f }
            java.lang.String r5 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x004d }
            char[] r5 = r5.toCharArray()     // Catch:{ Throwable -> 0x004d }
            r3.write(r5)     // Catch:{ Throwable -> 0x004d }
            r3.close()     // Catch:{ Throwable -> 0x0065 }
            goto L_0x006f
        L_0x004d:
            r5 = move-exception
            goto L_0x0056
        L_0x004f:
            r1 = move-exception
            r3 = r5
            r5 = r1
            goto L_0x0071
        L_0x0053:
            r1 = move-exception
            r3 = r5
            r5 = r1
        L_0x0056:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x0070 }
            java.lang.String r2 = "save pid error"
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ all -> 0x0070 }
            com.taobao.accs.utl.ALog.e(r1, r2, r5, r4)     // Catch:{ all -> 0x0070 }
            if (r3 == 0) goto L_0x006f
            r3.close()     // Catch:{ Throwable -> 0x0065 }
            goto L_0x006f
        L_0x0065:
            r5 = move-exception
            java.lang.String r1 = TAG
            java.lang.String r2 = "error"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r1, r2, r5, r0)
        L_0x006f:
            return
        L_0x0070:
            r5 = move-exception
        L_0x0071:
            if (r3 == 0) goto L_0x0081
            r3.close()     // Catch:{ Throwable -> 0x0077 }
            goto L_0x0081
        L_0x0077:
            r1 = move-exception
            java.lang.String r2 = TAG
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r3 = "error"
            com.taobao.accs.utl.ALog.e(r2, r3, r1, r0)
        L_0x0081:
            throw r5
        L_0x0082:
            r5 = move-exception
            java.lang.String r1 = TAG
            java.lang.String r2 = "error in create file"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r1, r2, r5, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.eudemon.EudemonManager.checkAndRenewPidFile(android.content.Context):void");
    }

    public boolean handleMessage(Message message) {
        try {
            if (message.what == 0) {
                startInternal();
                return true;
            } else if (message.what != -1) {
                return true;
            } else {
                stopInternal();
                return true;
            }
        } catch (Throwable th) {
            ALog.e(TAG, "handleMessage error", th, new Object[0]);
            return true;
        }
    }

    public static boolean execShell(String str, String str2, StringBuilder sb) {
        Log.w("TAG.", str2);
        try {
            Process exec = Runtime.getRuntime().exec("sh");
            DataInputStream dataInputStream = new DataInputStream(exec.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
            if (str != null && !"".equals(str.trim())) {
                dataOutputStream.writeBytes("cd " + str + "\n");
            }
            dataOutputStream.writeBytes(str2 + " &\n");
            dataOutputStream.writeBytes("exit \n");
            dataOutputStream.flush();
            exec.waitFor();
            byte[] bArr = new byte[dataInputStream.available()];
            dataInputStream.read(bArr);
            String str3 = new String(bArr);
            if (str3.length() == 0) {
                return true;
            }
            sb.append(str3);
            return true;
        } catch (Exception e) {
            sb.append("Exception:");
            sb.append(e.getMessage());
            return false;
        }
    }
}
