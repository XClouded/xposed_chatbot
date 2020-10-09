package alimama.com.unwbase.tools;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MediaStoreUtil {
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String insertImage(android.content.Context r4, android.graphics.Bitmap r5, java.lang.String r6, java.lang.String r7) {
        /*
            android.content.ContentResolver r0 = r4.getContentResolver()
            android.content.ContentValues r1 = new android.content.ContentValues
            r1.<init>()
            java.lang.String r2 = "title"
            r1.put(r2, r6)
            java.lang.String r2 = "_display_name"
            r1.put(r2, r6)
            java.lang.String r6 = "description"
            r1.put(r6, r7)
            java.lang.String r6 = "mime_type"
            java.lang.String r7 = "image/jpeg"
            r1.put(r6, r7)
            java.lang.String r6 = "date_added"
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.Long r7 = java.lang.Long.valueOf(r2)
            r1.put(r6, r7)
            java.lang.String r6 = "datetaken"
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.Long r7 = java.lang.Long.valueOf(r2)
            r1.put(r6, r7)
            r6 = 0
            android.net.Uri r7 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch:{ Exception -> 0x005c }
            android.net.Uri r7 = r0.insert(r7, r1)     // Catch:{ Exception -> 0x005c }
            if (r5 == 0) goto L_0x0058
            java.io.OutputStream r1 = r0.openOutputStream(r7)     // Catch:{ Exception -> 0x0056 }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ all -> 0x0051 }
            r3 = 100
            r5.compress(r2, r3, r1)     // Catch:{ all -> 0x0051 }
            r1.close()     // Catch:{ Exception -> 0x0056 }
            goto L_0x0063
        L_0x0051:
            r5 = move-exception
            r1.close()     // Catch:{ Exception -> 0x0056 }
            throw r5     // Catch:{ Exception -> 0x0056 }
        L_0x0056:
            goto L_0x005d
        L_0x0058:
            r0.delete(r7, r6, r6)     // Catch:{ Exception -> 0x0056 }
            goto L_0x0062
        L_0x005c:
            r7 = r6
        L_0x005d:
            if (r7 == 0) goto L_0x0063
            r0.delete(r7, r6, r6)
        L_0x0062:
            r7 = r6
        L_0x0063:
            if (r7 == 0) goto L_0x0069
            java.lang.String r6 = r7.toString()
        L_0x0069:
            java.lang.String r5 = getFilePathByContentResolver(r4, r7)
            android.content.Intent r7 = new android.content.Intent
            java.lang.String r0 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "file://"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            android.net.Uri r5 = android.net.Uri.parse(r5)
            r7.<init>(r0, r5)
            r4.sendBroadcast(r7)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwbase.tools.MediaStoreUtil.insertImage(android.content.Context, android.graphics.Bitmap, java.lang.String, java.lang.String):java.lang.String");
    }

    public static String getFilePathByContentResolver(Context context, Uri uri) {
        String str = null;
        if (uri == null) {
            return null;
        }
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query != null) {
            try {
                if (query.getCount() == 1) {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndexOrThrow("_data"));
                    }
                }
                return str;
            } finally {
                query.close();
            }
        } else {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
    }
}
