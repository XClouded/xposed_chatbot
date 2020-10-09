package io.flutter.plugin.external_adapter_image;

import android.graphics.Bitmap;
import androidx.annotation.Keep;
import io.flutter.embedding.engine.FlutterJNI;
import io.flutter.plugin.external_adapter_image.ExternalAdapterImageProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

@Keep
public class ExternalAdapterImageBridge {
    private static Map<String, Task> allTasks = new ConcurrentHashMap();

    private static native void notifyExternalImageFinish(String str, ExternalAdapterImageProvider.Image image, Bitmap[] bitmapArr, int i, int i2, double d, boolean z, boolean z2);

    private static class Response implements ExternalAdapterImageProvider.Response {
        final String id;

        Response(String str) {
            this.id = str;
        }

        public void finish(ExternalAdapterImageProvider.Image image) {
            ExternalAdapterImageBridge.finish(this.id, image);
        }
    }

    private static class Task {
        final ExternalAdapterImageProvider.Request request;
        final Response response;

        Task(ExternalAdapterImageProvider.Request request2, Response response2) {
            this.request = request2;
            this.response = response2;
        }
    }

    public static boolean request(String str, String str2, int i, int i2, String str3, String str4) {
        ExternalAdapterImageProvider externalAdapterImageProvider = FlutterJNI.getExternalAdapterImageProvider();
        if (externalAdapterImageProvider == null) {
            return false;
        }
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str3);
            JSONArray names = jSONObject.names();
            if (names != null) {
                for (int i3 = 0; i3 < names.length(); i3++) {
                    String string = names.getString(i3);
                    hashMap.put(string, jSONObject.getString(string));
                }
            }
            JSONObject jSONObject2 = new JSONObject(str4);
            JSONArray names2 = jSONObject2.names();
            if (names2 != null) {
                for (int i4 = 0; i4 < names2.length(); i4++) {
                    String string2 = names2.getString(i4);
                    hashMap2.put(string2, jSONObject2.getString(string2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = new Response(str);
        allTasks.put(str, new Task(externalAdapterImageProvider.request(str2, i, i2, hashMap, hashMap2, response), response));
        return true;
    }

    public static void cancel(String str) {
        Task remove = allTasks.remove(str);
        if (remove != null && remove.request != null) {
            remove.request.cancel();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0009, code lost:
        r1 = r12.getBitmap();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void finish(java.lang.String r11, io.flutter.plugin.external_adapter_image.ExternalAdapterImageProvider.Image r12) {
        /*
            r0 = 0
            if (r12 == 0) goto L_0x0015
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 17
            if (r1 < r2) goto L_0x0015
            android.graphics.Bitmap r1 = r12.getBitmap()
            if (r1 == 0) goto L_0x0015
            boolean r1 = r1.isPremultiplied()
            r10 = r1
            goto L_0x0016
        L_0x0015:
            r10 = 0
        L_0x0016:
            if (r12 == 0) goto L_0x001e
            android.graphics.Bitmap[] r1 = r12.getBitmaps()
        L_0x001c:
            r4 = r1
            goto L_0x0020
        L_0x001e:
            r1 = 0
            goto L_0x001c
        L_0x0020:
            if (r12 == 0) goto L_0x0028
            int r1 = r12.getBitmapCount()
            r5 = r1
            goto L_0x0029
        L_0x0028:
            r5 = 0
        L_0x0029:
            if (r12 == 0) goto L_0x0031
            int r1 = r12.getFrameCount()
            r6 = r1
            goto L_0x0033
        L_0x0031:
            r1 = 1
            r6 = 1
        L_0x0033:
            if (r12 == 0) goto L_0x003b
            double r1 = r12.getDuration()
        L_0x0039:
            r7 = r1
            goto L_0x003e
        L_0x003b:
            r1 = 0
            goto L_0x0039
        L_0x003e:
            if (r12 == 0) goto L_0x0046
            boolean r0 = r12.isSingleBitmapAnimated()
            r9 = r0
            goto L_0x0047
        L_0x0046:
            r9 = 0
        L_0x0047:
            r2 = r11
            r3 = r12
            notifyExternalImageFinish(r2, r3, r4, r5, r6, r7, r9, r10)
            java.util.Map<java.lang.String, io.flutter.plugin.external_adapter_image.ExternalAdapterImageBridge$Task> r12 = allTasks
            r12.remove(r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.flutter.plugin.external_adapter_image.ExternalAdapterImageBridge.finish(java.lang.String, io.flutter.plugin.external_adapter_image.ExternalAdapterImageProvider$Image):void");
    }
}
