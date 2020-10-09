package com.alibaba.weex.plugin.gcanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.taobao.gcanvas.util.GCanvasBase64;
import com.taobao.gcanvas.util.GLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.bridge.JSCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class GCanvasImageLoader {
    private static final String TAG = "GCanvasImageLoader";
    /* access modifiers changed from: private */
    public HashMap<String, ArrayList<JSCallback>> mCallbacks = new HashMap<>();
    /* access modifiers changed from: private */
    public HashMap<String, ImageInfo> mImageIdCache = new HashMap<>();

    public static class ImageInfo {
        static final int IDLE = -1;
        static final int LOADED = 512;
        static final int LOADING = 256;
        public int height;
        public int id;
        public AtomicInteger status = new AtomicInteger(-1);
        public int width;
    }

    public HashMap<String, ImageInfo> allCache() {
        return this.mImageIdCache;
    }

    public ImageInfo getCache(String str) {
        return this.mImageIdCache.get(str);
    }

    public void loadImage(final String str, int i, final JSCallback jSCallback) {
        try {
            final HashMap hashMap = new HashMap();
            if (str.startsWith("data:image")) {
                Bitmap handleBase64Texture = handleBase64Texture(str.substring(str.indexOf("base64,") + "base64,".length()));
                if (handleBase64Texture != null) {
                    hashMap.put("id", Integer.valueOf(i));
                    hashMap.put("url", str);
                    hashMap.put("width", Integer.valueOf(handleBase64Texture.getWidth()));
                    hashMap.put("height", Integer.valueOf(handleBase64Texture.getHeight()));
                } else {
                    hashMap.put("error", "process base64 failed,url=" + str);
                }
                jSCallback.invoke(hashMap);
                return;
            }
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
                arrayList.add(jSCallback);
                final String str2 = str;
                final HashMap hashMap2 = hashMap;
                final int i2 = i;
                final JSCallback jSCallback2 = jSCallback;
                Phenix.instance().load(str).succListener(new IPhenixListener<SuccPhenixEvent>() {
                    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                        Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                        if (bitmap != null) {
                            ImageInfo imageInfo = (ImageInfo) GCanvasImageLoader.this.mImageIdCache.get(str2);
                            imageInfo.width = bitmap.getWidth();
                            imageInfo.height = bitmap.getHeight();
                            hashMap2.put("id", Integer.valueOf(i2));
                            hashMap2.put("url", str2);
                            hashMap2.put("width", Integer.valueOf(imageInfo.width));
                            hashMap2.put("height", Integer.valueOf(imageInfo.height));
                            imageInfo.status.set(512);
                            try {
                                ArrayList arrayList = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str2);
                                if (arrayList == null) {
                                    return true;
                                }
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    ((JSCallback) it.next()).invoke(hashMap2);
                                }
                                return true;
                            } catch (Throwable unused) {
                                imageInfo.status.set(-1);
                                jSCallback2.invoke(hashMap2);
                                return true;
                            }
                        } else {
                            hashMap2.put("error", "bitmap load failed");
                            try {
                                ArrayList arrayList2 = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str2);
                                if (arrayList2 == null) {
                                    return true;
                                }
                                Iterator it2 = arrayList2.iterator();
                                while (it2.hasNext()) {
                                    ((JSCallback) it2.next()).invoke(hashMap2);
                                }
                                return true;
                            } catch (Throwable unused2) {
                                jSCallback2.invoke(hashMap2);
                                return true;
                            }
                        }
                    }
                }).failListener(new IPhenixListener<FailPhenixEvent>() {
                    public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                        hashMap.put("error", "bitmap load failed");
                        try {
                            ArrayList arrayList = (ArrayList) GCanvasImageLoader.this.mCallbacks.remove(str);
                            if (arrayList == null) {
                                return true;
                            }
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                ((JSCallback) it.next()).invoke(hashMap);
                            }
                            return true;
                        } catch (Throwable unused) {
                            jSCallback.invoke(hashMap);
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
                arrayList2.add(jSCallback);
            } else if (512 == imageInfo.status.get()) {
                hashMap.put("id", Integer.valueOf(i));
                hashMap.put("url", str);
                hashMap.put("width", Integer.valueOf(imageInfo.width));
                hashMap.put("height", Integer.valueOf(imageInfo.height));
                ArrayList remove = this.mCallbacks.remove(str);
                if (remove != null) {
                    Iterator it = remove.iterator();
                    while (it.hasNext()) {
                        ((JSCallback) it.next()).invoke(hashMap);
                    }
                }
                jSCallback.invoke(hashMap);
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
