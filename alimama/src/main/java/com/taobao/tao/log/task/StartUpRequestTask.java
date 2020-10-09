package com.taobao.tao.log.task;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.tlog.protocol.model.request.StartupRequest;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartUpRequestTask {
    /* access modifiers changed from: private */
    public static String TAG = "TLOG.StartUpRequestTask";
    private static String dirName = "logStartUp";
    private static String fileName = "adapter.config";
    private static Integer localSampling = 10000;
    private static OutputStream mOutputStream = null;
    private static Integer max = 10000;
    private static Integer min = 0;

    public static void execute() {
        try {
            final Long valueOf = Long.valueOf(System.currentTimeMillis());
            if (!isSendStartUp(TLogInitializer.getInstance().getContext(), TLogInitializer.getInstance().getAppVersion()).booleanValue()) {
                TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_PULL, TAG, "启动事件：不发送启动事件");
                return;
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_PULL, StartUpRequestTask.TAG, "启动事件：发送启动事件");
                    UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
                    StartupRequest startupRequest = new StartupRequest();
                    startupRequest.user = TLogInitializer.getInstance().getUserNick();
                    startupRequest.appVersion = TLogInitializer.getInstance().getAppVersion();
                    startupRequest.appKey = TLogInitializer.getInstance().getAppkey();
                    startupRequest.appId = TLogInitializer.getInstance().getAppId();
                    startupRequest.utdid = TLogInitializer.getUTDID();
                    startupRequest.opCode = OpCode.STARTUP;
                    UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
                    startupRequest.tokenType = uploadInfo.type;
                    if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
                        uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
                    }
                    startupRequest.tokenInfo = uploadTokenInfo;
                    startupRequest.osPlatform = "android";
                    startupRequest.osVersion = Build.VERSION.RELEASE;
                    startupRequest.brand = Build.BRAND;
                    startupRequest.deviceModel = Build.MODEL;
                    startupRequest.ip = StartUpRequestTask.getLocalIpAddress();
                    startupRequest.clientTime = valueOf;
                    try {
                        RequestResult build = startupRequest.build();
                        if (build != null) {
                            SendMessage.send(TLogInitializer.getInstance().getContext(), build, true);
                        }
                    } catch (Exception e) {
                        Log.e(StartUpRequestTask.TAG, "start up request build error", e);
                        TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, StartUpRequestTask.TAG, (Throwable) e);
                    }
                }
            }, 5000);
        } catch (Exception e) {
            Log.e(TAG, "send startUpRequest error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }

    public static void updateSample(Integer num) {
        if (num != null) {
            localSampling = num;
            TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
            String str = TLogStage.MSG_PULL;
            String str2 = TAG;
            tLogMonitor.stageInfo(str, str2, "启动事件：收到服务端采样配置,更新采样：" + num);
            updateConfig(TLogInitializer.getInstance().getAppVersion(), TLogInitializer.getInstance().getContext(), serverConfig(num));
        }
    }

    private static Boolean isSendStartUp(Context context, String str) {
        String localConfig = getLocalConfig(context);
        if (localConfig == null) {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_PULL, TAG, "启动事件：文件内容不存在，执行随机采样");
            return updateConfig(str, context, defaultConfig());
        }
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str2 = TLogStage.MSG_PULL;
        String str3 = TAG;
        tLogMonitor.stageInfo(str2, str3, "启动事件：采样内容存在：" + localConfig);
        String[] split = localConfig.split("\\^");
        if (split.length <= 1) {
            return false;
        }
        String str4 = split[0];
        try {
            Boolean valueOf = Boolean.valueOf(split[1]);
            if (str4.equals(str)) {
                return valueOf;
            }
            TLogMonitor tLogMonitor2 = TLogInitializer.getInstance().gettLogMonitor();
            String str5 = TLogStage.MSG_PULL;
            String str6 = TAG;
            tLogMonitor2.stageInfo(str5, str6, "启动事件：版本号变更了，更新采样：" + localConfig);
            return updateConfig(str, context, defaultConfig());
        } catch (Exception unused) {
            return false;
        }
    }

    private static synchronized Boolean updateConfig(String str, Context context, Boolean bool) {
        synchronized (StartUpRequestTask.class) {
            writeToLocalConfig(context, str + "^" + bool);
        }
        return bool;
    }

    /* access modifiers changed from: private */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (true) {
                    if (inetAddresses.hasMoreElements()) {
                        InetAddress nextElement = inetAddresses.nextElement();
                        if (!nextElement.isLoopbackAddress()) {
                            return nextElement.getHostAddress().toString();
                        }
                    }
                }
            }
            return "-";
        } catch (SocketException e) {
            Log.e(TAG, "WifiPreference IpAddress", e);
            return "-";
        }
    }

    private static Boolean serverConfig(Integer num) {
        Integer random = getRandom();
        if (num == null) {
            return defaultConfig();
        }
        if (num.equals(0)) {
            return false;
        }
        if (random.intValue() <= 0 || random.intValue() > num.intValue()) {
            return false;
        }
        return true;
    }

    private static Boolean defaultConfig() {
        int intValue = getRandom().intValue();
        if (intValue <= 0 || intValue > localSampling.intValue()) {
            return false;
        }
        return true;
    }

    private static Integer getRandom() {
        return Integer.valueOf((new Random().nextInt(max.intValue()) % ((max.intValue() - min.intValue()) + 1)) + min.intValue());
    }

    private static String getStorePath(Context context) {
        String str = context.getDir("tombstone", 0).getAbsolutePath() + File.separator + dirName;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return str;
    }

    private static void writeToLocalConfig(Context context, String str) {
        String storePath = getStorePath(context);
        write(storePath + File.separator + fileName, str);
    }

    private static String getLocalConfig(Context context) {
        String storePath = getStorePath(context);
        String read = read(storePath + File.separator + fileName);
        if (read == null || read.length() <= 0) {
            return null;
        }
        return read;
    }

    private static void write(String str, String str2) {
        if (mOutputStream == null) {
            try {
                mOutputStream = new FileOutputStream(str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            mOutputStream.write(str2.getBytes("UTF-8"));
            mOutputStream.flush();
            if (mOutputStream == null) {
                return;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (mOutputStream == null) {
                return;
            }
        } catch (Throwable th) {
            if (mOutputStream != null) {
                try {
                    mOutputStream.close();
                } catch (Exception unused) {
                }
            }
            throw th;
        }
        try {
            mOutputStream.close();
        } catch (Exception unused2) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0040 A[SYNTHETIC, Splitter:B:21:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0048 A[SYNTHETIC, Splitter:B:28:0x0048] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String read(java.lang.String r5) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r5 = r0.exists()
            r1 = 0
            if (r5 != 0) goto L_0x000d
            return r1
        L_0x000d:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            r5.<init>()     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            r4.<init>(r0)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            java.lang.String r0 = "utf-8"
            r3.<init>(r4, r0)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
        L_0x0023:
            java.lang.String r0 = r2.readLine()     // Catch:{ Exception -> 0x0035 }
            if (r0 == 0) goto L_0x002d
            r5.append(r0)     // Catch:{ Exception -> 0x0035 }
            goto L_0x0023
        L_0x002d:
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0035 }
            r2.close()     // Catch:{ Exception -> 0x0034 }
        L_0x0034:
            return r5
        L_0x0035:
            r5 = move-exception
            goto L_0x003b
        L_0x0037:
            r5 = move-exception
            goto L_0x0046
        L_0x0039:
            r5 = move-exception
            r2 = r1
        L_0x003b:
            r5.printStackTrace()     // Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x0043
            r2.close()     // Catch:{ Exception -> 0x0043 }
        L_0x0043:
            return r1
        L_0x0044:
            r5 = move-exception
            r1 = r2
        L_0x0046:
            if (r1 == 0) goto L_0x004b
            r1.close()     // Catch:{ Exception -> 0x004b }
        L_0x004b:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.log.task.StartUpRequestTask.read(java.lang.String):java.lang.String");
    }
}
