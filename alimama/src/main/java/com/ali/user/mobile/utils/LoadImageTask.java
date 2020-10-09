package com.ali.user.mobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.File;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private String mImageUrl;
    private ImageView mImageView;
    private int mLimitSize = 160;
    private String mTargetDir;

    public LoadImageTask(Context context, ImageView imageView, String str, int i) {
        this.mContext = context;
        this.mImageView = imageView;
        this.mTargetDir = str;
        this.mLimitSize = i;
    }

    /* access modifiers changed from: protected */
    public Bitmap doInBackground(String... strArr) {
        this.mImageUrl = strArr[0];
        Bitmap bitmapFromMemoryCache = ImageUtil.getBitmapFromMemoryCache(MD5Util.getMD5(this.mImageUrl));
        return bitmapFromMemoryCache == null ? loadImage(this.mImageUrl) : bitmapFromMemoryCache;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Bitmap bitmap) {
        if (bitmap != null && this.mImageView != null) {
            this.mImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap loadImage(String str) {
        Bitmap decodeSampledBitmapFromResource;
        File file = new File(getImagePath(str));
        if (!file.exists()) {
            downloadImage(str);
        }
        if (str == null || (decodeSampledBitmapFromResource = ImageUtil.decodeSampledBitmapFromResource(file.getPath(), this.mLimitSize)) == null) {
            return null;
        }
        ImageUtil.addBitmapToMemoryCache(MD5Util.getMD5(str), decodeSampledBitmapFromResource);
        return decodeSampledBitmapFromResource;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v10, resolved type: java.io.BufferedOutputStream} */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r0v8, types: [java.io.BufferedInputStream] */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r0v10, types: [java.io.BufferedInputStream] */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0084, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0087, code lost:
        r4 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0088, code lost:
        r3 = null;
        r5 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0084 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:12:0x0046] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00b8 A[SYNTHETIC, Splitter:B:61:0x00b8] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00bd A[Catch:{ IOException -> 0x0094 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00c2 A[Catch:{ IOException -> 0x0094 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00e0 A[SYNTHETIC, Splitter:B:75:0x00e0] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00e8 A[Catch:{ IOException -> 0x00e4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x00ed A[Catch:{ IOException -> 0x00e4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:88:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void downloadImage(java.lang.String r8) {
        /*
            r7 = this;
            java.lang.String r0 = android.os.Environment.getExternalStorageState()
            java.lang.String r1 = "mounted"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0014
            java.lang.String r0 = "TAG"
            java.lang.String r1 = "monted sdcard"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r0, (java.lang.String) r1)
            goto L_0x001b
        L_0x0014:
            java.lang.String r0 = "TAG"
            java.lang.String r1 = "has no sdcard"
            com.ali.user.mobile.log.TLogAdapter.e((java.lang.String) r0, (java.lang.String) r1)
        L_0x001b:
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch:{ Exception -> 0x00af, all -> 0x00ab }
            r1.<init>(r8)     // Catch:{ Exception -> 0x00af, all -> 0x00ab }
            java.net.URLConnection r1 = r1.openConnection()     // Catch:{ Exception -> 0x00af, all -> 0x00ab }
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ Exception -> 0x00af, all -> 0x00ab }
            r2 = 5000(0x1388, float:7.006E-42)
            r1.setConnectTimeout(r2)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            r2 = 15000(0x3a98, float:2.102E-41)
            r1.setReadTimeout(r2)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            r2 = 1
            r1.setDoInput(r2)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            int r2 = r1.getResponseCode()     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto L_0x008c
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            java.io.InputStream r3 = r1.getInputStream()     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r4 = r7.getImagePath(r8)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            boolean r4 = r3.exists()     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
            if (r4 != 0) goto L_0x005d
            r3.createNewFile()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005d
        L_0x0059:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
        L_0x005d:
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
            java.io.BufferedOutputStream r5 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x0081, all -> 0x0084 }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x007f, all -> 0x007c }
        L_0x006b:
            int r4 = r2.read(r0)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            r6 = -1
            if (r4 == r6) goto L_0x007a
            r6 = 0
            r5.write(r0, r6, r4)     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            r5.flush()     // Catch:{ Exception -> 0x007f, all -> 0x007c }
            goto L_0x006b
        L_0x007a:
            r0 = r2
            goto L_0x008e
        L_0x007c:
            r8 = move-exception
            goto L_0x00dd
        L_0x007f:
            r4 = move-exception
            goto L_0x008a
        L_0x0081:
            r4 = move-exception
            r5 = r0
            goto L_0x008a
        L_0x0084:
            r8 = move-exception
            goto L_0x00de
        L_0x0087:
            r4 = move-exception
            r3 = r0
            r5 = r3
        L_0x008a:
            r0 = r2
            goto L_0x00b3
        L_0x008c:
            r3 = r0
            r5 = r3
        L_0x008e:
            if (r0 == 0) goto L_0x0096
            r0.close()     // Catch:{ IOException -> 0x0094 }
            goto L_0x0096
        L_0x0094:
            r0 = move-exception
            goto L_0x00a1
        L_0x0096:
            if (r5 == 0) goto L_0x009b
            r5.close()     // Catch:{ IOException -> 0x0094 }
        L_0x009b:
            if (r1 == 0) goto L_0x00c5
            r1.disconnect()     // Catch:{ IOException -> 0x0094 }
            goto L_0x00c5
        L_0x00a1:
            r0.printStackTrace()
            goto L_0x00c5
        L_0x00a5:
            r8 = move-exception
            r2 = r0
            goto L_0x00de
        L_0x00a8:
            r4 = move-exception
            r3 = r0
            goto L_0x00b2
        L_0x00ab:
            r8 = move-exception
            r1 = r0
            r2 = r1
            goto L_0x00de
        L_0x00af:
            r4 = move-exception
            r1 = r0
            r3 = r1
        L_0x00b2:
            r5 = r3
        L_0x00b3:
            r4.printStackTrace()     // Catch:{ all -> 0x00db }
            if (r0 == 0) goto L_0x00bb
            r0.close()     // Catch:{ IOException -> 0x0094 }
        L_0x00bb:
            if (r5 == 0) goto L_0x00c0
            r5.close()     // Catch:{ IOException -> 0x0094 }
        L_0x00c0:
            if (r1 == 0) goto L_0x00c5
            r1.disconnect()     // Catch:{ IOException -> 0x0094 }
        L_0x00c5:
            if (r3 == 0) goto L_0x00da
            java.lang.String r0 = r3.getPath()
            int r1 = r7.mLimitSize
            android.graphics.Bitmap r0 = com.ali.user.mobile.utils.ImageUtil.decodeSampledBitmapFromResource(r0, r1)
            if (r0 == 0) goto L_0x00da
            java.lang.String r8 = com.ali.user.mobile.utils.MD5Util.getMD5(r8)
            com.ali.user.mobile.utils.ImageUtil.addBitmapToMemoryCache(r8, r0)
        L_0x00da:
            return
        L_0x00db:
            r8 = move-exception
            r2 = r0
        L_0x00dd:
            r0 = r5
        L_0x00de:
            if (r2 == 0) goto L_0x00e6
            r2.close()     // Catch:{ IOException -> 0x00e4 }
            goto L_0x00e6
        L_0x00e4:
            r0 = move-exception
            goto L_0x00f1
        L_0x00e6:
            if (r0 == 0) goto L_0x00eb
            r0.close()     // Catch:{ IOException -> 0x00e4 }
        L_0x00eb:
            if (r1 == 0) goto L_0x00f4
            r1.disconnect()     // Catch:{ IOException -> 0x00e4 }
            goto L_0x00f4
        L_0x00f1:
            r0.printStackTrace()
        L_0x00f4:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.utils.LoadImageTask.downloadImage(java.lang.String):void");
    }

    private String getImagePath(String str) {
        String str2 = this.mContext.getCacheDir().getPath() + "/" + this.mTargetDir + "/";
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str2 + MD5Util.getMD5(str);
    }
}
