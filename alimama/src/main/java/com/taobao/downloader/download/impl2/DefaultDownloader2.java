package com.taobao.downloader.download.impl2;

import com.taobao.downloader.download.IDownloader;
import com.taobao.downloader.download.IListener;
import com.taobao.downloader.download.impl2.OutputContext;
import com.taobao.downloader.download.protocol.DLConnection;
import com.taobao.downloader.download.protocol.DLInputStream;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.util.FileUtil;
import com.taobao.downloader.util.LogUtil;
import com.taobao.downloader.util.Switcher;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DefaultDownloader2 implements IDownloader {
    public static final int ST_CANCELED = 2;
    public static final int ST_PAUSED = 1;
    private static final String TAG = "Downloader";
    private InputContext inputContext;
    private int mStatus;
    private OutputContext outputContext;

    public void cancel() {
        this.mStatus |= 2;
    }

    public void pause() {
        this.mStatus |= 1;
    }

    public void download(SingleTask singleTask, IListener iListener) {
        this.inputContext = new InputContext(singleTask);
        this.outputContext = new OutputContext(iListener);
        try {
            int status = getStatus();
            if (status > 0) {
                this.outputContext.errorInfo.addErrorInfo(-20, status, "");
            } else {
                this.inputContext.prepareDownload();
                if (this.inputContext.hitFileCache()) {
                    this.outputContext.successCode = 11;
                } else if (!this.inputContext.hitTmpCache()) {
                    doNetworkDownload();
                    this.outputContext.callback(this.inputContext);
                    return;
                } else if (FileUtil.mvFile(this.inputContext.tempFile, this.inputContext.downloadFile)) {
                    this.outputContext.successCode = 11;
                } else {
                    this.outputContext.errorInfo.addErrorInfo(-11, this.inputContext.downloadFile.getParentFile().canWrite() ? 104 : 105, "rename tmp file error").ioError = true;
                }
            }
        } catch (Throwable th) {
            try {
                LogUtil.error("Downloader", "do download exception", th);
                this.outputContext.errorInfo.addErrorInfo(-19, 301, getErrorMsg(th));
            } catch (Throwable th2) {
                this.outputContext.callback(this.inputContext);
                throw th2;
            }
        }
        this.outputContext.callback(this.inputContext);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v11, resolved type: com.taobao.downloader.download.protocol.DLInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v17, resolved type: com.taobao.downloader.download.protocol.DLInputStream} */
    /* JADX WARNING: type inference failed for: r6v0, types: [com.taobao.downloader.download.protocol.DLInputStream] */
    /* JADX WARNING: type inference failed for: r6v10 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x018a A[SYNTHETIC, Splitter:B:113:0x018a] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x0199 A[SYNTHETIC, Splitter:B:118:0x0199] */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x01a8 A[SYNTHETIC, Splitter:B:123:0x01a8] */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x01dd A[SYNTHETIC, Splitter:B:138:0x01dd] */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x01ec A[SYNTHETIC, Splitter:B:143:0x01ec] */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0207 A[SYNTHETIC, Splitter:B:157:0x0207] */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x0216 A[SYNTHETIC, Splitter:B:162:0x0216] */
    /* JADX WARNING: Removed duplicated region for block: B:167:0x0225 A[SYNTHETIC, Splitter:B:167:0x0225] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean doNetworkDownload() {
        /*
            r16 = this;
            r1 = r16
            r2 = 0
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0200 }
            com.taobao.downloader.download.protocol.DLConnection r5 = r16.getConnection()     // Catch:{ all -> 0x0200 }
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x01fd }
            com.taobao.downloader.util.MonitorUtil$DownloadStat r0 = r0.downloadStat     // Catch:{ all -> 0x01fd }
            long r6 = r0.connectTime     // Catch:{ all -> 0x01fd }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01fd }
            r10 = 0
            long r8 = r8 - r3
            long r6 = r6 + r8
            r0.connectTime = r6     // Catch:{ all -> 0x01fd }
            r3 = 0
            if (r5 != 0) goto L_0x002d
            if (r5 == 0) goto L_0x002c
            r5.disConnect()     // Catch:{ Throwable -> 0x0023 }
            goto L_0x002c
        L_0x0023:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x002c:
            return r3
        L_0x002d:
            r4 = 1
            com.taobao.downloader.download.protocol.DLInputStream r6 = r5.getInputStream()     // Catch:{ IOException -> 0x01c0 }
            if (r6 == 0) goto L_0x01b6
            r7 = -11
            com.taobao.downloader.download.impl2.InputContext r0 = r1.inputContext     // Catch:{ FileNotFoundException -> 0x0170 }
            java.io.RandomAccessFile r8 = r0.getRandomAccessFile()     // Catch:{ FileNotFoundException -> 0x0170 }
            if (r8 == 0) goto L_0x0162
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x016a }
            boolean r0 = r1.saveData(r6, r8)     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.OutputContext r2 = r1.outputContext     // Catch:{ all -> 0x016a }
            com.taobao.downloader.util.MonitorUtil$DownloadStat r2 = r2.downloadStat     // Catch:{ all -> 0x016a }
            long r11 = r2.downloadTime     // Catch:{ all -> 0x016a }
            long r13 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x016a }
            r15 = 0
            long r13 = r13 - r9
            long r11 = r11 + r13
            r2.downloadTime = r11     // Catch:{ all -> 0x016a }
            if (r0 != 0) goto L_0x0085
            if (r8 == 0) goto L_0x0066
            r8.close()     // Catch:{ IOException -> 0x005d }
            goto L_0x0066
        L_0x005d:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0066:
            if (r6 == 0) goto L_0x0075
            r6.close()     // Catch:{ Throwable -> 0x006c }
            goto L_0x0075
        L_0x006c:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0075:
            if (r5 == 0) goto L_0x0084
            r5.disConnect()     // Catch:{ Throwable -> 0x007b }
            goto L_0x0084
        L_0x007b:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0084:
            return r3
        L_0x0085:
            com.taobao.downloader.download.impl2.InputContext r0 = r1.inputContext     // Catch:{ all -> 0x016a }
            boolean r0 = r0.hitTmpCache()     // Catch:{ all -> 0x016a }
            if (r0 == 0) goto L_0x011c
            com.taobao.downloader.download.impl2.InputContext r0 = r1.inputContext     // Catch:{ all -> 0x016a }
            java.io.File r0 = r0.tempFile     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.InputContext r2 = r1.inputContext     // Catch:{ all -> 0x016a }
            java.io.File r2 = r2.downloadFile     // Catch:{ all -> 0x016a }
            boolean r0 = com.taobao.downloader.util.FileUtil.mvFile(r0, r2)     // Catch:{ all -> 0x016a }
            if (r0 == 0) goto L_0x00cf
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x016a }
            r2 = 10
            r0.successCode = r2     // Catch:{ all -> 0x016a }
            if (r8 == 0) goto L_0x00b0
            r8.close()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x00b0
        L_0x00a7:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r3 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r3, (java.lang.Throwable) r2)
        L_0x00b0:
            if (r6 == 0) goto L_0x00bf
            r6.close()     // Catch:{ Throwable -> 0x00b6 }
            goto L_0x00bf
        L_0x00b6:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r3 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r3, (java.lang.Throwable) r2)
        L_0x00bf:
            if (r5 == 0) goto L_0x00ce
            r5.disConnect()     // Catch:{ Throwable -> 0x00c5 }
            goto L_0x00ce
        L_0x00c5:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r3 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r3, (java.lang.Throwable) r2)
        L_0x00ce:
            return r4
        L_0x00cf:
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.errorInfo     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.InputContext r2 = r1.inputContext     // Catch:{ all -> 0x016a }
            java.io.File r2 = r2.downloadFile     // Catch:{ all -> 0x016a }
            java.io.File r2 = r2.getParentFile()     // Catch:{ all -> 0x016a }
            boolean r2 = r2.canWrite()     // Catch:{ all -> 0x016a }
            if (r2 == 0) goto L_0x00e4
            r2 = 104(0x68, float:1.46E-43)
            goto L_0x00e6
        L_0x00e4:
            r2 = 105(0x69, float:1.47E-43)
        L_0x00e6:
            java.lang.String r9 = "rename tmp file error"
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.addErrorInfo(r7, r2, r9)     // Catch:{ all -> 0x016a }
            r0.ioError = r4     // Catch:{ all -> 0x016a }
            if (r8 == 0) goto L_0x00fd
            r8.close()     // Catch:{ IOException -> 0x00f4 }
            goto L_0x00fd
        L_0x00f4:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x00fd:
            if (r6 == 0) goto L_0x010c
            r6.close()     // Catch:{ Throwable -> 0x0103 }
            goto L_0x010c
        L_0x0103:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x010c:
            if (r5 == 0) goto L_0x011b
            r5.disConnect()     // Catch:{ Throwable -> 0x0112 }
            goto L_0x011b
        L_0x0112:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x011b:
            return r3
        L_0x011c:
            com.taobao.downloader.download.impl2.InputContext r0 = r1.inputContext     // Catch:{ all -> 0x016a }
            java.io.File r0 = r0.tempFile     // Catch:{ all -> 0x016a }
            r0.delete()     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.errorInfo     // Catch:{ all -> 0x016a }
            com.taobao.downloader.download.impl2.InputContext r2 = r1.inputContext     // Catch:{ all -> 0x016a }
            int r2 = r2.getFileErrorCode()     // Catch:{ all -> 0x016a }
            r4 = 106(0x6a, float:1.49E-43)
            java.lang.String r7 = "download invalid"
            r0.addErrorInfo(r2, r4, r7)     // Catch:{ all -> 0x016a }
            if (r8 == 0) goto L_0x0143
            r8.close()     // Catch:{ IOException -> 0x013a }
            goto L_0x0143
        L_0x013a:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0143:
            if (r6 == 0) goto L_0x0152
            r6.close()     // Catch:{ Throwable -> 0x0149 }
            goto L_0x0152
        L_0x0149:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0152:
            if (r5 == 0) goto L_0x0161
            r5.disConnect()     // Catch:{ Throwable -> 0x0158 }
            goto L_0x0161
        L_0x0158:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0161:
            return r3
        L_0x0162:
            java.io.FileNotFoundException r0 = new java.io.FileNotFoundException     // Catch:{ FileNotFoundException -> 0x016d }
            java.lang.String r2 = "outputStream is null"
            r0.<init>(r2)     // Catch:{ FileNotFoundException -> 0x016d }
            throw r0     // Catch:{ FileNotFoundException -> 0x016d }
        L_0x016a:
            r0 = move-exception
            goto L_0x0204
        L_0x016d:
            r0 = move-exception
            r2 = r8
            goto L_0x0171
        L_0x0170:
            r0 = move-exception
        L_0x0171:
            java.lang.String r8 = "Downloader"
            java.lang.String r9 = "getRandomAccessFile"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r8, (java.lang.String) r9, (java.lang.Throwable) r0)     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.errorInfo     // Catch:{ all -> 0x01fa }
            r8 = 103(0x67, float:1.44E-43)
            java.lang.String r9 = r5.getErrorMsg()     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.addErrorInfo(r7, r8, r9)     // Catch:{ all -> 0x01fa }
            r0.ioError = r4     // Catch:{ all -> 0x01fa }
            if (r2 == 0) goto L_0x0197
            r2.close()     // Catch:{ IOException -> 0x018e }
            goto L_0x0197
        L_0x018e:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x0197:
            if (r6 == 0) goto L_0x01a6
            r6.close()     // Catch:{ Throwable -> 0x019d }
            goto L_0x01a6
        L_0x019d:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x01a6:
            if (r5 == 0) goto L_0x01b5
            r5.disConnect()     // Catch:{ Throwable -> 0x01ac }
            goto L_0x01b5
        L_0x01ac:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x01b5:
            return r3
        L_0x01b6:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ IOException -> 0x01be }
            java.lang.String r7 = "inputstream is null"
            r0.<init>(r7)     // Catch:{ IOException -> 0x01be }
            throw r0     // Catch:{ IOException -> 0x01be }
        L_0x01be:
            r0 = move-exception
            goto L_0x01c2
        L_0x01c0:
            r0 = move-exception
            r6 = r2
        L_0x01c2:
            java.lang.String r7 = "Downloader"
            java.lang.String r8 = "conn.getinputstream exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r7, (java.lang.String) r8, (java.lang.Throwable) r0)     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext r0 = r1.outputContext     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.errorInfo     // Catch:{ all -> 0x01fa }
            r7 = -12
            r8 = 205(0xcd, float:2.87E-43)
            java.lang.String r9 = r5.getErrorMsg()     // Catch:{ all -> 0x01fa }
            com.taobao.downloader.download.impl2.OutputContext$ErrorInfo r0 = r0.addErrorInfo(r7, r8, r9)     // Catch:{ all -> 0x01fa }
            r0.connectError = r4     // Catch:{ all -> 0x01fa }
            if (r6 == 0) goto L_0x01ea
            r6.close()     // Catch:{ Throwable -> 0x01e1 }
            goto L_0x01ea
        L_0x01e1:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x01ea:
            if (r5 == 0) goto L_0x01f9
            r5.disConnect()     // Catch:{ Throwable -> 0x01f0 }
            goto L_0x01f9
        L_0x01f0:
            r0 = move-exception
            r2 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x01f9:
            return r3
        L_0x01fa:
            r0 = move-exception
            r8 = r2
            goto L_0x0204
        L_0x01fd:
            r0 = move-exception
            r6 = r2
            goto L_0x0203
        L_0x0200:
            r0 = move-exception
            r5 = r2
            r6 = r5
        L_0x0203:
            r8 = r6
        L_0x0204:
            r2 = r0
            if (r8 == 0) goto L_0x0214
            r8.close()     // Catch:{ IOException -> 0x020b }
            goto L_0x0214
        L_0x020b:
            r0 = move-exception
            r3 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r3)
        L_0x0214:
            if (r6 == 0) goto L_0x0223
            r6.close()     // Catch:{ Throwable -> 0x021a }
            goto L_0x0223
        L_0x021a:
            r0 = move-exception
            r3 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r3)
        L_0x0223:
            if (r5 == 0) goto L_0x0232
            r5.disConnect()     // Catch:{ Throwable -> 0x0229 }
            goto L_0x0232
        L_0x0229:
            r0 = move-exception
            r3 = r0
            java.lang.String r0 = "Downloader"
            java.lang.String r4 = "on exception"
            com.taobao.downloader.util.LogUtil.error((java.lang.String) r0, (java.lang.String) r4, (java.lang.Throwable) r3)
        L_0x0232:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.downloader.download.impl2.DefaultDownloader2.doNetworkDownload():boolean");
    }

    private DLConnection getConnection() {
        DLConnection connection = Switcher.getConnection(this.inputContext.mTask.item, this.inputContext.mConfig);
        LogUtil.error("Downloader", "use connction {}", this.inputContext.mTask.item.url, connection.getClass().getSimpleName());
        try {
            connection.openConnection(this.inputContext.url, this.inputContext.mConfig);
            connection.addRequestProperty("f-refer", "download_" + this.inputContext.mTask.param.bizId);
            long previousFileSize = this.inputContext.getPreviousFileSize();
            if (0 != previousFileSize) {
                String str = "bytes=" + previousFileSize + "-";
                LogUtil.debug("Downloader", "add request property range {}", str);
                connection.addRequestProperty("Range", str);
                this.outputContext.downloadStat.range = true;
            } else {
                this.outputContext.downloadStat.range = false;
            }
            try {
                connection.connect();
                try {
                    int statusCode = connection.getStatusCode();
                    if (this.inputContext.isHttpStatusCodeOk(connection.getDownloadLength(), statusCode)) {
                        return connection;
                    }
                    this.outputContext.errorInfo.addErrorInfo(-12, statusCode, connection.getErrorMsg()).connectError = true;
                    return null;
                } catch (Exception e) {
                    LogUtil.error("Downloader", "conn.getstatuscode exception", (Throwable) e);
                    this.outputContext.errorInfo.addErrorInfo(-12, 204, connection.getErrorMsg()).connectError = true;
                    return null;
                }
            } catch (IOException e2) {
                LogUtil.error("Downloader", "conn.conn exception", (Throwable) e2);
                this.outputContext.errorInfo.addErrorInfo(-12, 203, connection.getErrorMsg()).connectError = true;
                return null;
            }
        } catch (IOException e3) {
            LogUtil.error("Downloader", "conn.open exception", (Throwable) e3);
            this.outputContext.errorInfo.addErrorInfo(-12, 202, connection.getErrorMsg()).connectError = true;
            return null;
        }
    }

    private boolean saveData(DLInputStream dLInputStream, RandomAccessFile randomAccessFile) {
        boolean z = true;
        if (dLInputStream == null || randomAccessFile == null) {
            OutputContext.ErrorInfo errorInfo = this.outputContext.errorInfo;
            StringBuilder sb = new StringBuilder();
            sb.append("savedataParam:");
            sb.append(dLInputStream == null);
            sb.append("|");
            if (randomAccessFile != null) {
                z = false;
            }
            sb.append(z);
            errorInfo.addErrorInfo(-19, 0, sb.toString());
            return false;
        }
        this.outputContext.mDownloadSize = this.inputContext.tempFile.length();
        FileChannel channel = randomAccessFile.getChannel();
        try {
            channel.position(randomAccessFile.length());
            byte[] bArr = new byte[Switcher.getDLReadBufferSize()];
            while (true) {
                int status = getStatus();
                if (status > 0) {
                    this.outputContext.errorInfo.addErrorInfo(-20, status, "");
                    return false;
                }
                try {
                    int read = dLInputStream.read(bArr);
                    if (-1 == read) {
                        return true;
                    }
                    this.outputContext.hasReadData = true;
                    try {
                        channel.write(ByteBuffer.wrap(bArr, 0, read));
                        long j = (long) read;
                        this.outputContext.mDownloadSize += j;
                        this.outputContext.downloadStat.traffic += j;
                        this.outputContext.updateProgress();
                    } catch (IOException e) {
                        LogUtil.error("Downloader", "fc.write exception", (Throwable) e);
                        this.outputContext.errorInfo.addErrorInfo(-11, 102, getErrorMsg(e)).ioError = true;
                        return false;
                    }
                } catch (Exception e2) {
                    LogUtil.error("Downloader", "input.read exception", (Throwable) e2);
                    this.outputContext.errorInfo.addErrorInfo(-12, 201, getErrorMsg(e2)).readStreamError = true;
                    return false;
                }
            }
        } catch (IOException e3) {
            LogUtil.error("Downloader", "fc.position exception", (Throwable) e3);
            this.outputContext.errorInfo.addErrorInfo(-11, 101, getErrorMsg(e3)).ioError = true;
            return false;
        }
    }

    private String getErrorMsg(Throwable th) {
        if (!(th instanceof RuntimeException) || th.getMessage() == null || th.getMessage().length() >= 20) {
            return th.getClass().getSimpleName();
        }
        return th.getClass().getSimpleName() + ":" + th.getMessage();
    }

    private int getStatus() {
        if (this.mStatus <= 0) {
            return 0;
        }
        if ((this.mStatus & 1) == 1) {
            return 1;
        }
        if ((this.mStatus & 2) != 2) {
            return 0;
        }
        if (this.inputContext.tempFile.exists()) {
            this.inputContext.tempFile.delete();
        }
        return 2;
    }
}
