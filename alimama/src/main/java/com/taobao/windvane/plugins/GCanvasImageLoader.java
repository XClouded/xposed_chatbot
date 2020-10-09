package com.taobao.windvane.plugins;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.taobao.gcanvas.util.GCanvasBase64;
import com.taobao.gcanvas.util.GLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;

public class GCanvasImageLoader {
    private static final String TAG = "GCanvasImageLoader";
    /* access modifiers changed from: private */
    public HashMap<String, ArrayList<WVCallBackContext>> mCallbacks = new HashMap<>();
    private double mDpr = 0.0d;
    /* access modifiers changed from: private */
    public HashMap<String, ImageInfo> mImageIdCache = new HashMap<>();

    public HashMap<String, ImageInfo> allCache() {
        return this.mImageIdCache;
    }

    public ImageInfo getCache(String str) {
        return this.mImageIdCache.get(str);
    }

    static class ImageInfo {
        static final int IDLE = -1;
        static final int LOADED = 512;
        static final int LOADING = 256;
        public int height;
        public int id;
        public AtomicInteger status = new AtomicInteger(-1);
        public int width;

        ImageInfo() {
        }
    }

    public void setDpr(double d) {
        this.mDpr = d;
    }

    public void loadImage(final String str, int i, final WVCallBackContext wVCallBackContext) {
        try {
            final JSONObject jSONObject = new JSONObject();
            if (str.startsWith("http://") || str.startsWith("https://")) {
                ImageInfo imageInfo = this.mImageIdCache.get(str);
                if (imageInfo == null) {
                    imageInfo = new ImageInfo();
                    this.mImageIdCache.put(str, imageInfo);
                }
                if (imageInfo.status.get() == -1) {
                    imageInfo.status.set(256);
                    imageInfo.id = i;
                    ArrayList arrayList = this.mCallbacks.get(str);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        this.mCallbacks.put(str, arrayList);
                    }
                    arrayList.add(wVCallBackContext);
                    final String str2 = str;
                    final JSONObject jSONObject2 = jSONObject;
                    final int i2 = i;
                    final WVCallBackContext wVCallBackContext2 = wVCallBackContext;
                    Phenix.instance().load(str).succListener(new IPhenixListener<SuccPhenixEvent>() {
                        public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                            Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                            if (bitmap != null) {
                                ImageInfo imageInfo = (ImageInfo) GCanvasImageLoader.this.mImageIdCache.get(str2);
                                imageInfo.width = bitmap.getWidth();
                                imageInfo.height = bitmap.getHeight();
                                try {
                                    jSONObject2.put("id", i2);
                                    jSONObject2.put("url", str2);
                                    jSONObject2.put("width", imageInfo.width);
                                    jSONObject2.put("height", imageInfo.height);
                                } catch (JSONException unused) {
                                    wVCallBackContext2.success(jSONObject2.toString());
                                }
                                imageInfo.status.set(512);
                                try {
                                    ArrayList arrayList = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str2);
                                    if (arrayList == null) {
                                        return true;
                                    }
                                    Iterator it = arrayList.iterator();
                                    while (it.hasNext()) {
                                        ((WVCallBackContext) it.next()).success(jSONObject2.toString());
                                    }
                                    return true;
                                } catch (Throwable unused2) {
                                    imageInfo.status.set(-1);
                                    wVCallBackContext2.success(jSONObject2.toString());
                                    return true;
                                }
                            } else {
                                try {
                                    GCanvasImageLoader.this.mImageIdCache.remove(str2);
                                    jSONObject2.put("error", "bitmap load failed");
                                } catch (JSONException unused3) {
                                    wVCallBackContext2.error(jSONObject2.toString());
                                }
                                try {
                                    ArrayList arrayList2 = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str2);
                                    if (arrayList2 == null) {
                                        return true;
                                    }
                                    Iterator it2 = arrayList2.iterator();
                                    while (it2.hasNext()) {
                                        ((WVCallBackContext) it2.next()).error(jSONObject2.toString());
                                    }
                                    return true;
                                } catch (Throwable unused4) {
                                    wVCallBackContext2.error(jSONObject2.toString());
                                    return true;
                                }
                            }
                        }
                    }).failListener(new IPhenixListener<FailPhenixEvent>() {
                        public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                            try {
                                GCanvasImageLoader.this.mImageIdCache.remove(str);
                                jSONObject.put("error", "bitmap load failed");
                            } catch (JSONException unused) {
                                wVCallBackContext.error();
                            }
                            try {
                                ArrayList arrayList = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str);
                                if (arrayList == null) {
                                    return true;
                                }
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    ((WVCallBackContext) it.next()).error(jSONObject.toString());
                                }
                                return true;
                            } catch (Throwable unused2) {
                                wVCallBackContext.error(jSONObject.toString());
                                return true;
                            }
                        }
                    }).fetch();
                } else if (256 == imageInfo.status.get()) {
                    ArrayList arrayList2 = this.mCallbacks.get(str);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        this.mCallbacks.put(str, arrayList2);
                    }
                    arrayList2.add(wVCallBackContext);
                } else if (512 == imageInfo.status.get()) {
                    jSONObject.put("id", i);
                    jSONObject.put("url", str);
                    jSONObject.put("width", imageInfo.width);
                    jSONObject.put("height", imageInfo.height);
                    ArrayList remove = this.mCallbacks.remove(str);
                    if (remove != null) {
                        Iterator it = remove.iterator();
                        while (it.hasNext()) {
                            ((WVCallBackContext) it.next()).success(jSONObject.toString());
                        }
                    }
                    wVCallBackContext.success(jSONObject.toString());
                }
            } else if (str.startsWith("data:image")) {
                Bitmap handleBase64Texture = handleBase64Texture(str.substring(str.indexOf("base64,") + "base64,".length()));
                if (handleBase64Texture != null) {
                    jSONObject.put("id", i);
                    jSONObject.put("url", str);
                    jSONObject.put("width", handleBase64Texture.getWidth());
                    jSONObject.put("height", handleBase64Texture.getHeight());
                    wVCallBackContext.success(jSONObject.toString());
                    return;
                }
                jSONObject.put("error", "process base64 failed,url=" + str);
                wVCallBackContext.error(jSONObject.toString());
            } else {
                jSONObject.put("error", "bad url address,url=" + str);
                wVCallBackContext.error(jSONObject.toString());
            }
        } catch (Throwable th) {
            GLog.e(TAG, th.getMessage(), th);
        }
    }

    public Bitmap handleBase64Texture(String str) {
        try {
            byte[] decode = GCanvasBase64.decode(str.getBytes());
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Throwable th) {
            GLog.d("error in processing base64Texture,error=" + th);
            return null;
        }
    }
}
