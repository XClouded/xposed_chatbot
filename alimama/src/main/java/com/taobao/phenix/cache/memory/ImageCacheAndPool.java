package com.taobao.phenix.cache.memory;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import com.taobao.phenix.bitmap.BitmapPool;
import com.taobao.phenix.cache.HotEndLruCache;
import com.taobao.phenix.common.UnitedLog;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@TargetApi(19)
public class ImageCacheAndPool extends HotEndLruCache<String, CachedRootImage> implements BitmapPool {
    public static final int CEILING_SIZE_MAX_MULTIPLE = 6;
    public static final String TAG = "ImageCachePool";
    public static final String TAG_POOL = "BitmapPool";
    public static final String TAG_RECYCLE = "ImageRecycle";
    private final Object POOL_LOCK = new Object();
    private int mAvailableSize;
    private int mCurrCount;
    private int mHitCount;
    private int mMissCount;
    private NavigableMap<Integer, List<String>> mSizeGroup = new TreeMap();

    public ImageCacheAndPool(int i, float f) {
        super(i, f);
        UnitedLog.d(TAG, "init with maxSize=%K, hotPercent=%.1f%%", Integer.valueOf(i), Float.valueOf(f * 100.0f));
    }

    /* access modifiers changed from: protected */
    public int getSize(CachedRootImage cachedRootImage) {
        if (cachedRootImage == null) {
            return 0;
        }
        return cachedRootImage.getSize();
    }

    /* renamed from: com.taobao.phenix.cache.memory.ImageCacheAndPool$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                android.graphics.Bitmap$Config[] r0 = android.graphics.Bitmap.Config.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$graphics$Bitmap$Config = r0
                int[] r0 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ALPHA_8     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x001f }
                android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.RGB_565     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x002a }
                android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_4444     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x0035 }
                android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.cache.memory.ImageCacheAndPool.AnonymousClass1.<clinit>():void");
        }
    }

    private int getByteCountFromConfig(Bitmap.Config config) {
        if (config == null) {
            return 0;
        }
        switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    private int getStaticImageSize4Pool(CachedRootImage cachedRootImage) {
        Bitmap bitmap;
        if (!(cachedRootImage instanceof StaticCachedImage) || (bitmap = ((StaticCachedImage) cachedRootImage).bitmap) == null || bitmap.isRecycled() || !bitmap.isMutable()) {
            return 0;
        }
        return cachedRootImage.getSize();
    }

    /* access modifiers changed from: protected */
    public void onPreEvictedStateChange(boolean z, String str, CachedRootImage cachedRootImage, boolean z2) {
        List list;
        if (z2) {
            cachedRootImage.removeFromCache();
        } else {
            cachedRootImage.releaseFromCache(z);
        }
        synchronized (this.POOL_LOCK) {
            if (!z) {
                try {
                    int staticImageSize4Pool = getStaticImageSize4Pool(cachedRootImage);
                    if (staticImageSize4Pool > 0 && (list = (List) this.mSizeGroup.get(Integer.valueOf(staticImageSize4Pool))) != null) {
                        if (list.remove(str)) {
                            this.mAvailableSize -= staticImageSize4Pool;
                            this.mCurrCount--;
                            UnitedLog.d(TAG_POOL, "remove from bitmap pool when not pre-evicted(cache removed=%b), image=%s", Boolean.valueOf(z2), cachedRootImage);
                        }
                        if (list.isEmpty()) {
                            this.mSizeGroup.remove(Integer.valueOf(staticImageSize4Pool));
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void maxPoolSize(int i) {
        setPreEvictedMaxSize(i);
        UnitedLog.d(TAG_POOL, "set preEvictedMaxSize(maxPoolSize=%K) in ImageCacheAndPool", Integer.valueOf(i));
    }

    public void trimPool(int i) {
        trimTo(i);
    }

    /* access modifiers changed from: protected */
    public void boardPool() {
        if (UnitedLog.isLoggable(3)) {
            UnitedLog.d(TAG_POOL, "%K/%K, rate:%.1f%%, hits:%d, misses:%d, count:%d", Integer.valueOf(this.mAvailableSize), Integer.valueOf(maxPreEvictedSize()), Float.valueOf((((float) this.mHitCount) * 100.0f) / ((float) (this.mHitCount + this.mMissCount))), Integer.valueOf(this.mHitCount), Integer.valueOf(this.mMissCount), Integer.valueOf(this.mCurrCount));
        }
    }

    public Bitmap getFromPool(int i, int i2, Bitmap.Config config) {
        Bitmap bitmap;
        String str;
        int i3;
        CachedRootImage cachedRootImage;
        Bitmap bitmap2;
        int byteCountFromConfig = i * i2 * getByteCountFromConfig(config);
        synchronized (this.POOL_LOCK) {
            bitmap = null;
            if (byteCountFromConfig > 0) {
                try {
                    Map.Entry<Integer, List<String>> ceilingEntry = this.mSizeGroup.ceilingEntry(Integer.valueOf(byteCountFromConfig));
                    if (ceilingEntry != null) {
                        i3 = ceilingEntry.getKey().intValue();
                        if (i3 <= byteCountFromConfig * 6) {
                            List value = ceilingEntry.getValue();
                            if (!value.isEmpty()) {
                                str = (String) value.remove(0);
                                this.mAvailableSize -= i3;
                                this.mCurrCount--;
                            } else {
                                str = null;
                            }
                            if (value.isEmpty()) {
                                this.mSizeGroup.remove(Integer.valueOf(i3));
                            }
                        } else {
                            str = null;
                        }
                    }
                } catch (Throwable th) {
                    while (true) {
                        throw th;
                    }
                }
            }
            str = null;
            i3 = 0;
        }
        if (str != null) {
            cachedRootImage = (CachedRootImage) remove(str, false);
            if ((cachedRootImage instanceof StaticCachedImage) && (bitmap2 = ((StaticCachedImage) cachedRootImage).bitmap) != null && bitmap2.isMutable() && !bitmap2.isRecycled()) {
                try {
                    bitmap2.reconfigure(i, i2, config);
                    bitmap2.setHasAlpha(true);
                    bitmap2.eraseColor(0);
                    bitmap = bitmap2;
                } catch (Throwable th2) {
                    UnitedLog.e(TAG_POOL, "reconfigure error, bitmap=%s, throwable=%s", bitmap2, th2);
                }
            }
        } else {
            cachedRootImage = null;
        }
        if (bitmap != null) {
            this.mHitCount++;
            UnitedLog.d(TAG_POOL, "get from bitmap pool, width=%d, height=%d, config=%s, redundant=%.1f, image=%s", Integer.valueOf(i), Integer.valueOf(i2), config, Float.valueOf(((float) i3) / ((float) byteCountFromConfig)), cachedRootImage);
        } else {
            this.mMissCount++;
        }
        boardPool();
        return bitmap;
    }

    public boolean putIntoPool(CachedRootImage cachedRootImage) {
        boolean add;
        if (!contains(cachedRootImage.getMemoryCacheKey())) {
            UnitedLog.d(TAG_POOL, "cannot put into bitmap pool(cache removed), image=%s", cachedRootImage);
            return false;
        }
        int staticImageSize4Pool = getStaticImageSize4Pool(cachedRootImage);
        if (staticImageSize4Pool <= 0) {
            return false;
        }
        synchronized (this.POOL_LOCK) {
            List list = (List) this.mSizeGroup.get(Integer.valueOf(staticImageSize4Pool));
            if (list == null) {
                list = new LinkedList();
                this.mSizeGroup.put(Integer.valueOf(staticImageSize4Pool), list);
            }
            this.mAvailableSize += staticImageSize4Pool;
            this.mCurrCount++;
            UnitedLog.d(TAG_POOL, "put into bitmap pool, size=%d, image=%s", Integer.valueOf(staticImageSize4Pool), cachedRootImage);
            add = list.add(cachedRootImage.getMemoryCacheKey());
        }
        return add;
    }

    public int available() {
        return this.mAvailableSize;
    }

    public final synchronized void clear() {
        super.clear();
        synchronized (this.POOL_LOCK) {
            this.mAvailableSize = 0;
            this.mCurrCount = 0;
            this.mSizeGroup.clear();
        }
    }

    public final CachedRootImage get(String str) {
        CachedRootImage cachedRootImage = (CachedRootImage) super.get(str);
        board(TAG);
        return cachedRootImage;
    }
}
