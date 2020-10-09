package android.taobao.windvane.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.util.Base64;
import android.webkit.URLUtil;

import com.facebook.imagepipeline.common.RotationOptions;
import com.taobao.weex.el.parse.Operators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageTool {
    private static final int MAX_LENGTH = 10240;

    public interface ImageSaveCallback {
        void error(String str);

        void success();
    }

    public static void saveImageToDCIM(final Context context, String str, final ImageSaveCallback imageSaveCallback) {
        if (URLUtil.isHttpUrl(str) || URLUtil.isHttpsUrl(str)) {
            ConnectManager.getInstance().connect(str, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse httpResponse, int i) {
                    try {
                        if (httpResponse.isSuccess() && "mounted".equals(Environment.getExternalStorageState())) {
                            if (ImageTool.saveImage(context, BitmapFactory.decodeStream(new ByteArrayInputStream(httpResponse.getData())))) {
                                if (imageSaveCallback != null) {
                                    imageSaveCallback.success();
                                    return;
                                }
                                return;
                            }
                        }
                        if (imageSaveCallback != null) {
                            imageSaveCallback.error("bad resource");
                        }
                    } catch (Exception e) {
                        if (imageSaveCallback != null) {
                            imageSaveCallback.error(e.getMessage());
                        }
                    } catch (OutOfMemoryError e2) {
                        if (imageSaveCallback != null) {
                            imageSaveCallback.error(e2.getMessage());
                        }
                    }
                }

                public void onError(int i, String str) {
                    if (imageSaveCallback != null) {
                        ImageSaveCallback imageSaveCallback = imageSaveCallback;
                        imageSaveCallback.error("error get resource, code=[" + i + "],msg=[" + str + Operators.ARRAY_END_STR);
                    }
                }
            });
        } else if (str.startsWith("data:")) {
            try {
                byte[] decode = Base64.decode(str.substring(str.indexOf(",") + 1).getBytes(), 0);
                if (saveImage(context, BitmapFactory.decodeByteArray(decode, 0, decode.length)) && imageSaveCallback != null) {
                    imageSaveCallback.success();
                }
            } catch (Throwable th) {
                if (imageSaveCallback != null) {
                    imageSaveCallback.error(th.getMessage());
                }
            }
        }
    }

    public static void saveImageToDCIM(Context context, String str, final Handler handler) {
        saveImageToDCIM(context, str, (ImageSaveCallback) new ImageSaveCallback() {
            public void success() {
                if (handler != null) {
                    handler.sendEmptyMessage(404);
                }
            }

            public void error(String str) {
                if (handler != null) {
                    handler.sendEmptyMessage(405);
                }
            }
        });
    }

    public static boolean saveImage(Context context, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < 29) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), String.valueOf(System.currentTimeMillis()));
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, new String[]{"image/jpeg"}, (MediaScannerConnection.OnScanCompletedListener) null);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        } else if (saveImageQ(context, bitmap)) {
            return true;
        } else {
            TaoLog.e("ImageTool", "save image failed");
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c9 A[SYNTHETIC, Splitter:B:40:0x00c9] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00d4 A[Catch:{ IOException -> 0x00d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00df A[SYNTHETIC, Splitter:B:50:0x00df] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ea A[Catch:{ IOException -> 0x00e6 }] */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean saveImageQ(android.content.Context r9, android.graphics.Bitmap r10) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = android.os.Environment.DIRECTORY_PICTURES
            r0.append(r1)
            java.lang.String r1 = java.io.File.separator
            r0.append(r1)
            java.lang.String r1 = "wv_save_image"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.content.ContentValues r1 = new android.content.ContentValues
            r1.<init>()
            java.lang.String r2 = "_display_name"
            long r3 = java.lang.System.currentTimeMillis()
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r1.put(r2, r3)
            java.lang.String r2 = "mime_type"
            java.lang.String r3 = "image/jpeg"
            r1.put(r2, r3)
            java.lang.String r2 = "is_pending"
            r3 = 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r3)
            r1.put(r2, r4)
            java.lang.String r2 = "relative_path"
            r1.put(r2, r0)
            java.lang.String r0 = "external_primary"
            android.net.Uri r0 = android.provider.MediaStore.Images.Media.getContentUri(r0)
            android.content.ContentResolver r9 = r9.getContentResolver()
            android.net.Uri r0 = r9.insert(r0, r1)
            r2 = 0
            r4 = 0
            if (r0 != 0) goto L_0x0066
            android.util.AndroidRuntimeException r10 = new android.util.AndroidRuntimeException     // Catch:{ IOException -> 0x0062, all -> 0x005d }
            java.lang.String r1 = "android Q: save image error for uri is null"
            r10.<init>(r1)     // Catch:{ IOException -> 0x0062, all -> 0x005d }
            r10.printStackTrace()     // Catch:{ IOException -> 0x0062, all -> 0x005d }
            return r2
        L_0x005d:
            r9 = move-exception
            r5 = r4
            r6 = r5
            goto L_0x00dd
        L_0x0062:
            r10 = move-exception
            r5 = r4
            r6 = r5
            goto L_0x00c1
        L_0x0066:
            java.lang.String r5 = "w"
            android.os.ParcelFileDescriptor r5 = r9.openFileDescriptor(r0, r5, r4)     // Catch:{ IOException -> 0x0062, all -> 0x005d }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00bf, all -> 0x00bc }
            java.io.FileDescriptor r7 = r5.getFileDescriptor()     // Catch:{ IOException -> 0x00bf, all -> 0x00bc }
            r6.<init>(r7)     // Catch:{ IOException -> 0x00bf, all -> 0x00bc }
            android.graphics.Bitmap$CompressFormat r7 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x00ba }
            r8 = 100
            boolean r10 = r10.compress(r7, r8, r6)     // Catch:{ IOException -> 0x00ba }
            if (r10 != 0) goto L_0x009a
            android.util.AndroidRuntimeException r10 = new android.util.AndroidRuntimeException     // Catch:{ IOException -> 0x00ba }
            java.lang.String r1 = "android Q: save image failed"
            r10.<init>(r1)     // Catch:{ IOException -> 0x00ba }
            r10.printStackTrace()     // Catch:{ IOException -> 0x00ba }
            r6.flush()     // Catch:{ IOException -> 0x0095 }
            r6.close()     // Catch:{ IOException -> 0x0095 }
            if (r5 == 0) goto L_0x0099
            r5.close()     // Catch:{ IOException -> 0x0095 }
            goto L_0x0099
        L_0x0095:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0099:
            return r2
        L_0x009a:
            r1.clear()     // Catch:{ IOException -> 0x00ba }
            java.lang.String r10 = "is_pending"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)     // Catch:{ IOException -> 0x00ba }
            r1.put(r10, r7)     // Catch:{ IOException -> 0x00ba }
            r9.update(r0, r1, r4, r4)     // Catch:{ IOException -> 0x00ba }
            r6.flush()     // Catch:{ IOException -> 0x00b5 }
            r6.close()     // Catch:{ IOException -> 0x00b5 }
            if (r5 == 0) goto L_0x00b9
            r5.close()     // Catch:{ IOException -> 0x00b5 }
            goto L_0x00b9
        L_0x00b5:
            r9 = move-exception
            r9.printStackTrace()
        L_0x00b9:
            return r3
        L_0x00ba:
            r10 = move-exception
            goto L_0x00c1
        L_0x00bc:
            r9 = move-exception
            r6 = r4
            goto L_0x00dd
        L_0x00bf:
            r10 = move-exception
            r6 = r4
        L_0x00c1:
            r10.printStackTrace()     // Catch:{ all -> 0x00dc }
            r9.delete(r0, r4, r4)     // Catch:{ all -> 0x00dc }
            if (r6 == 0) goto L_0x00d2
            r6.flush()     // Catch:{ IOException -> 0x00d0 }
            r6.close()     // Catch:{ IOException -> 0x00d0 }
            goto L_0x00d2
        L_0x00d0:
            r9 = move-exception
            goto L_0x00d8
        L_0x00d2:
            if (r5 == 0) goto L_0x00db
            r5.close()     // Catch:{ IOException -> 0x00d0 }
            goto L_0x00db
        L_0x00d8:
            r9.printStackTrace()
        L_0x00db:
            return r2
        L_0x00dc:
            r9 = move-exception
        L_0x00dd:
            if (r6 == 0) goto L_0x00e8
            r6.flush()     // Catch:{ IOException -> 0x00e6 }
            r6.close()     // Catch:{ IOException -> 0x00e6 }
            goto L_0x00e8
        L_0x00e6:
            r10 = move-exception
            goto L_0x00ee
        L_0x00e8:
            if (r5 == 0) goto L_0x00f1
            r5.close()     // Catch:{ IOException -> 0x00e6 }
            goto L_0x00f1
        L_0x00ee:
            r10.printStackTrace()
        L_0x00f1:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.ImageTool.saveImageQ(android.content.Context, android.graphics.Bitmap):boolean");
    }

    public static Bitmap readZoomImage(String str, int i) {
        if (i > 10240) {
            i = 10240;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int round = Math.round(((float) (options.outHeight > options.outWidth ? options.outHeight : options.outWidth)) / ((float) i));
        if (round < 1) {
            round = 1;
        }
        options.inSampleSize = round;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i2 = width > height ? width : height;
        if (i2 <= i) {
            return bitmap;
        }
        float f = ((float) i) / ((float) i2);
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, long j, long j2) {
        int i;
        int i2;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            i2 = width;
            i = height;
        } else {
            i = width;
            i2 = height;
        }
        if (((long) i2) <= j && ((long) i) <= j2) {
            return bitmap;
        }
        float f = ((float) j) / ((float) i2);
        float f2 = ((float) j2) / ((float) i);
        if (f >= f2) {
            f = f2;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Drawable toDrawable(Resources resources, String str) {
        if (str == null) {
            return null;
        }
        try {
            try {
                return new BitmapDrawable(resources, BitmapFactory.decodeStream(new ByteArrayInputStream(Base64.decode(str, 0))));
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public static int readRotationDegree(String str) {
        if (str == null) {
            return 0;
        }
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                return RotationOptions.ROTATE_180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return RotationOptions.ROTATE_270;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        if (i == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap != createBitmap ? createBitmap : bitmap;
        } catch (OutOfMemoryError unused) {
            return bitmap;
        }
    }
}
