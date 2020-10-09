package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.ImageTool;
import android.view.View;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ScreenCaptureUtil {
    private static final int IMAGE_SAVE_REQUEST_CODE = 1553;

    private ScreenCaptureUtil() {
    }

    public static long capture(View view, boolean z, long j, long j2, long j3, boolean z2) throws IOException {
        Bitmap bitmapFromView = getBitmapFromView(view);
        if (bitmapFromView != null) {
            WVCacheManager.getInstance().getCacheDir(true);
            long saveBitmapToCache = WVUtils.saveBitmapToCache(z2 ? ImageTool.zoomBitmap(bitmapFromView, j2, j3) : bitmapFromView);
            if (!z) {
                return saveBitmapToCache;
            }
            ImageTool.saveImage(view.getContext(), bitmapFromView);
            return saveBitmapToCache;
        }
        throw new RuntimeException("can't get bitmap from the view");
    }

    public static long captureByActivty(Activity activity, boolean z, long j, long j2, long j3, boolean z2) throws IOException {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap drawingCache = activity.getWindow().getDecorView().getDrawingCache();
        if (drawingCache != null) {
            WVCacheManager.getInstance().getCacheDir(true);
            if (z2) {
                drawingCache = ImageTool.zoomBitmap(drawingCache, j2, j3);
            }
            long saveBitmapToCache = WVUtils.saveBitmapToCache(drawingCache);
            if (!z) {
                return saveBitmapToCache;
            }
            ImageTool.saveImage(activity, drawingCache);
            return saveBitmapToCache;
        }
        throw new RuntimeException("can't get bitmap from the view");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBitmapToPath(android.content.Context r2, android.graphics.Bitmap r3, android.net.Uri r4) throws java.io.IOException {
        /*
            r0 = 0
            android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            java.lang.String r1 = "w"
            android.os.ParcelFileDescriptor r2 = r2.openFileDescriptor(r4, r1)     // Catch:{ Throwable -> 0x002f, all -> 0x002c }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x002a }
            java.io.FileDescriptor r1 = r2.getFileDescriptor()     // Catch:{ Throwable -> 0x002a }
            r4.<init>(r1)     // Catch:{ Throwable -> 0x002a }
            android.graphics.Bitmap$CompressFormat r0 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            r1 = 100
            r3.compress(r0, r1, r4)     // Catch:{ Throwable -> 0x0027, all -> 0x0024 }
            r4.flush()
            r4.close()
            if (r2 == 0) goto L_0x0041
            goto L_0x003e
        L_0x0024:
            r3 = move-exception
            r0 = r4
            goto L_0x0043
        L_0x0027:
            r3 = move-exception
            r0 = r4
            goto L_0x0031
        L_0x002a:
            r3 = move-exception
            goto L_0x0031
        L_0x002c:
            r3 = move-exception
            r2 = r0
            goto L_0x0043
        L_0x002f:
            r3 = move-exception
            r2 = r0
        L_0x0031:
            r3.printStackTrace()     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x003c
            r0.flush()
            r0.close()
        L_0x003c:
            if (r2 == 0) goto L_0x0041
        L_0x003e:
            r2.close()
        L_0x0041:
            return
        L_0x0042:
            r3 = move-exception
        L_0x0043:
            if (r0 == 0) goto L_0x004b
            r0.flush()
            r0.close()
        L_0x004b:
            if (r2 == 0) goto L_0x0050
            r2.close()
        L_0x0050:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.ScreenCaptureUtil.saveBitmapToPath(android.content.Context, android.graphics.Bitmap, android.net.Uri):void");
    }

    private static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private static String generateFileName() {
        return "SHOUTAO_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date()) + ".jpg";
    }

    /* access modifiers changed from: private */
    public static void notifyNewMedia(Uri uri, Context context) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static final class BackFragment extends Fragment {
        private Bitmap bmp;

        public BackFragment() {
        }

        public BackFragment(Bitmap bitmap) {
            this.bmp = bitmap;
        }

        public void updateBitmap(Bitmap bitmap) {
            this.bmp = bitmap;
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            if (i == ScreenCaptureUtil.IMAGE_SAVE_REQUEST_CODE && i2 == -1) {
                try {
                    if (this.bmp != null) {
                        ScreenCaptureUtil.saveBitmapToPath(getActivity(), this.bmp, intent.getData());
                        ScreenCaptureUtil.notifyNewMedia(intent.getData(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
