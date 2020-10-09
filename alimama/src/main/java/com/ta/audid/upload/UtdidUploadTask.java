package com.ta.audid.upload;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.device.AppUtdid;
import com.ta.audid.store.UtdidContent;
import com.ta.audid.store.UtdidContentBuilder;
import com.ta.audid.store.UtdidContentSqliteStore;
import com.ta.audid.store.UtdidContentUtil;
import com.ta.audid.utils.UtdidLogger;
import java.util.List;

public class UtdidUploadTask implements Runnable {
    private static final String POST_HTTP_URL = "https://audid-api.taobao.com/v2.0/a/audid/req/";
    private static volatile boolean bRunning = false;
    private Context mContext = null;

    public UtdidUploadTask(Context context) {
        this.mContext = context;
    }

    public void run() {
        try {
            upload();
        } catch (Throwable th) {
            UtdidLogger.e("", th, new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003e, code lost:
        bRunning = false;
        com.ta.audid.utils.MutiProcessLock.releaseUpload();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0043, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003d A[ExcHandler: all (r0v4 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:6:0x0014] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void upload() {
        /*
            r5 = this;
            com.ta.audid.utils.UtdidLogger.d()
            android.content.Context r0 = r5.mContext
            boolean r0 = com.ta.audid.utils.NetworkInfoUtils.isConnectInternet(r0)
            if (r0 != 0) goto L_0x000c
            return
        L_0x000c:
            boolean r0 = bRunning
            if (r0 != 0) goto L_0x004a
            r0 = 1
            bRunning = r0
            r1 = 0
            boolean r2 = com.ta.audid.utils.MutiProcessLock.trylockUpload()     // Catch:{ Throwable -> 0x0044, all -> 0x003d }
            if (r2 != 0) goto L_0x002b
            java.lang.String r2 = ""
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0044, all -> 0x003d }
            java.lang.String r3 = "Other Process is Uploading"
            r0[r1] = r3     // Catch:{ Throwable -> 0x0044, all -> 0x003d }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r2, (java.lang.Object[]) r0)     // Catch:{ Throwable -> 0x0044, all -> 0x003d }
            bRunning = r1
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            return
        L_0x002b:
            r2 = 0
        L_0x002c:
            if (r2 >= r0) goto L_0x0044
            boolean r3 = r5.uploadFromDataBase()     // Catch:{ Throwable -> 0x003a, all -> 0x003d }
            if (r3 == 0) goto L_0x0035
            goto L_0x0044
        L_0x0035:
            r3 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r3)     // Catch:{ Throwable -> 0x003a, all -> 0x003d }
        L_0x003a:
            int r2 = r2 + 1
            goto L_0x002c
        L_0x003d:
            r0 = move-exception
            bRunning = r1
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            throw r0
        L_0x0044:
            bRunning = r1
            com.ta.audid.utils.MutiProcessLock.releaseUpload()
            return
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.upload.UtdidUploadTask.upload():void");
    }

    private boolean uploadFromDataBase() {
        UtdidLogger.d();
        List<UtdidContent> list = UtdidContentSqliteStore.getInstance().get(4);
        if (list == null || list.size() == 0) {
            UtdidLogger.d("log is empty", new Object[0]);
            return true;
        }
        String buildPostDataFromDB = buildPostDataFromDB(list);
        if (TextUtils.isEmpty(buildPostDataFromDB)) {
            UtdidLogger.d("postData is empty", new Object[0]);
            return true;
        }
        if (reqServer(buildPostDataFromDB)) {
            UtdidContentSqliteStore.getInstance().delete(list);
            UtdidLogger.d("", "upload success");
        } else {
            UtdidLogger.d("", "upload fail");
        }
        return false;
    }

    private boolean reqServer(String str) {
        HttpResponse sendRequest = HttpUtils.sendRequest(POST_HTTP_URL, str, true);
        if (sendRequest == null) {
            return false;
        }
        String str2 = "";
        try {
            str2 = new String(sendRequest.data, "UTF-8");
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        if (HttpResponse.checkSignature(str2, sendRequest.signature)) {
            return BizResponse.isSuccess(BizResponse.parseResult(str2).code);
        }
        return false;
    }

    private String buildPostDataFromDB(List<UtdidContent> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        String currentAppUtdid = AppUtdid.getInstance().getCurrentAppUtdid();
        if (TextUtils.isEmpty(currentAppUtdid)) {
            return null;
        }
        String buildUDID = UtdidContentBuilder.buildUDID(currentAppUtdid);
        StringBuilder sb = new StringBuilder();
        sb.append(buildUDID);
        for (int i = 0; i < list.size(); i++) {
            String decodedContent = list.get(i).getDecodedContent();
            sb.append("\n");
            sb.append(decodedContent);
        }
        if (UtdidLogger.isDebug()) {
            UtdidLogger.sd("", sb.toString());
        }
        return UtdidContentUtil.getEncodedContent(sb.toString());
    }
}
