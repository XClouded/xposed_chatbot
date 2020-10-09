package alimama.com.unwupdate;

public class DownloadTask extends SimpleTask {
    private static final String LOG_TAG = "cube-update";
    public static final int RESULT_DOWNLOAD_ERROR = 3;
    public static final int RESULT_NO_ENOUGH_SPACE = 4;
    public static final int RESULT_OK = 1;
    public static final int RESULT_URL_ERROR = 2;
    private String mDestMd5Str;
    private DownLoadListener mDownLoadListener;
    private String mFileName;
    private int mResult = 1;
    private String mUrl;

    public DownloadTask(DownLoadListener downLoadListener, String str, String str2, String str3) {
        this.mDownLoadListener = downLoadListener;
        this.mUrl = str;
        this.mFileName = str2;
        this.mDestMd5Str = str3;
        if (this.mDestMd5Str == null) {
            this.mDestMd5Str = "";
        }
    }

    private void setResult(int i) {
        this.mResult = i;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:33|(2:34|(1:61)(2:36|(2:62|38)(3:39|(2:41|64)(1:63)|60)))|(2:43|44)|45|46|(2:49|50)|51|(1:53)(1:(2:55|66)(2:56|65))) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00be */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00c3 A[SYNTHETIC, Splitter:B:49:0x00c3] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00cc A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00cd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doInBackground() {
        /*
            r11 = this;
            java.lang.String r0 = r11.mUrl
            boolean r0 = android.webkit.URLUtil.isNetworkUrl(r0)
            if (r0 != 0) goto L_0x000d
            r0 = 2
            r11.setResult(r0)
            return
        L_0x000d:
            java.lang.String r0 = r11.mUrl
            r1 = 3
            java.net.URL r2 = new java.net.URL     // Catch:{ Exception -> 0x00de }
            r2.<init>(r0)     // Catch:{ Exception -> 0x00de }
            java.net.URLConnection r0 = r2.openConnection()     // Catch:{ Exception -> 0x00de }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x00de }
            r2 = 30000(0x7530, float:4.2039E-41)
            r0.setConnectTimeout(r2)     // Catch:{ Exception -> 0x00de }
            r0.connect()     // Catch:{ Exception -> 0x00de }
            int r2 = r0.getContentLength()     // Catch:{ Exception -> 0x00de }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x00de }
            java.lang.String r4 = r11.mFileName     // Catch:{ Exception -> 0x00de }
            r3.<init>(r4)     // Catch:{ Exception -> 0x00de }
            boolean r4 = r3.exists()     // Catch:{ Exception -> 0x00de }
            r5 = 1
            if (r4 == 0) goto L_0x0053
            long r6 = (long) r2     // Catch:{ Exception -> 0x00de }
            long r8 = r3.length()     // Catch:{ Exception -> 0x00de }
            int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r4 != 0) goto L_0x0053
            java.lang.String r4 = r11.mDestMd5Str     // Catch:{ Exception -> 0x00de }
            java.lang.String r6 = alimama.com.unwupdate.UpdateController.getFileMD5Impl(r3)     // Catch:{ Exception -> 0x00de }
            boolean r4 = r4.equals(r6)     // Catch:{ Exception -> 0x00de }
            if (r4 == 0) goto L_0x0053
            if (r0 == 0) goto L_0x004f
            r0.disconnect()     // Catch:{ Exception -> 0x00de }
        L_0x004f:
            r11.setResult(r5)     // Catch:{ Exception -> 0x00de }
            return
        L_0x0053:
            boolean r4 = r3.exists()     // Catch:{ Exception -> 0x00de }
            if (r4 == 0) goto L_0x005c
            r3.delete()     // Catch:{ Exception -> 0x00de }
        L_0x005c:
            java.io.File r4 = r3.getParentFile()     // Catch:{ Exception -> 0x00de }
            boolean r6 = r4.exists()     // Catch:{ Exception -> 0x00de }
            if (r6 != 0) goto L_0x0070
            boolean r6 = r4.mkdirs()     // Catch:{ Exception -> 0x00de }
            if (r6 != 0) goto L_0x0070
            r11.setResult(r1)     // Catch:{ Exception -> 0x00de }
            return
        L_0x0070:
            long r6 = alimama.com.unwupdate.DiskFileUtils.getUsableSpace(r4)     // Catch:{ Exception -> 0x00de }
            long r8 = (long) r2     // Catch:{ Exception -> 0x00de }
            int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r4 >= 0) goto L_0x007e
            r0 = 4
            r11.setResult(r0)     // Catch:{ Exception -> 0x00de }
            return
        L_0x007e:
            java.io.InputStream r4 = r0.getInputStream()     // Catch:{ Exception -> 0x00de }
            if (r4 != 0) goto L_0x0088
            r11.setResult(r1)     // Catch:{ Exception -> 0x00de }
            return
        L_0x0088:
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00de }
            r6.<init>(r3)     // Catch:{ Exception -> 0x00de }
            r3 = 409200(0x63e70, float:5.73411E-40)
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x00de }
            r7 = 0
            r8 = 0
        L_0x0094:
            boolean r9 = r11.isCancelled()     // Catch:{ Exception -> 0x00de }
            if (r9 != 0) goto L_0x00b9
            int r9 = r4.read(r3)     // Catch:{ Exception -> 0x00de }
            if (r9 > 0) goto L_0x00a1
            goto L_0x00b9
        L_0x00a1:
            r6.write(r3, r7, r9)     // Catch:{ Exception -> 0x00de }
            int r8 = r8 + r9
            r9 = 1120403456(0x42c80000, float:100.0)
            float r10 = (float) r8     // Catch:{ Exception -> 0x00de }
            float r10 = r10 * r9
            float r9 = (float) r2     // Catch:{ Exception -> 0x00de }
            float r10 = r10 / r9
            int r9 = (int) r10     // Catch:{ Exception -> 0x00de }
            boolean r10 = r11.isCancelled()     // Catch:{ Exception -> 0x00de }
            if (r10 != 0) goto L_0x0094
            alimama.com.unwupdate.DownLoadListener r10 = r11.mDownLoadListener     // Catch:{ Exception -> 0x00de }
            r10.onPercentUpdate(r9)     // Catch:{ Exception -> 0x00de }
            goto L_0x0094
        L_0x00b9:
            if (r4 == 0) goto L_0x00be
            r4.close()     // Catch:{ Exception -> 0x00be }
        L_0x00be:
            r6.close()     // Catch:{ Exception -> 0x00c1 }
        L_0x00c1:
            if (r0 == 0) goto L_0x00c6
            r0.disconnect()     // Catch:{ Exception -> 0x00de }
        L_0x00c6:
            boolean r0 = r11.isCancelled()
            if (r0 == 0) goto L_0x00cd
            return
        L_0x00cd:
            if (r8 == r2) goto L_0x00d3
            r11.setResult(r1)
            goto L_0x00dd
        L_0x00d3:
            java.lang.String r0 = "666"
            java.lang.String r1 = r11.mFileName
            alimama.com.unwupdate.FileUtils.chmod(r0, r1)
            r11.setResult(r5)
        L_0x00dd:
            return
        L_0x00de:
            r11.setResult(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwupdate.DownloadTask.doInBackground():void");
    }

    /* access modifiers changed from: protected */
    public void onCancel() {
        this.mDownLoadListener.onCancel();
    }

    public void onFinish(boolean z) {
        this.mDownLoadListener.onDone(z, this.mResult);
    }
}
