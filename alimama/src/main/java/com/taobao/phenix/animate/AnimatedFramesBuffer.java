package com.taobao.phenix.animate;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.animate.AnimatedDrawableFrameInfo;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.pexode.common.AshmemBitmapFactory;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AnimatedFramesBuffer {
    private static final int MAX_CACHE_SIZE = 5242880;
    private static final int MAX_FREE_SIZE = 2097152;
    private final SparseArray<CachedEntity> mCachedEntities;
    private SparseArray<Runnable> mDecodeFlights;
    private boolean mDecodeRunning;
    private final Scheduler mDecodeScheduler;
    private String mDrawableId;
    private AnimatedFrameCompositor mFrameCompositor;
    private final int mFrameCount;
    private List<Bitmap> mFreeBitmaps;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final int mImageHeight;
    private final int mImageWidth;
    private final int mMaxCacheCount;
    private final int mMaxFreeCount;
    private final ScheduledAction mWeakDecodeAction = new WeakDecodeAction(this);

    private boolean isInRange(int i, int i2, int i3) {
        return (i2 > i && i3 >= i && i3 < i2) || (i2 <= i && (i3 >= i || i3 < i2));
    }

    public static class CachedEntity {
        /* access modifiers changed from: private */
        public Bitmap bitmap;
        /* access modifiers changed from: private */
        public int references = 0;

        static /* synthetic */ int access$008(CachedEntity cachedEntity) {
            int i = cachedEntity.references;
            cachedEntity.references = i + 1;
            return i;
        }

        static /* synthetic */ int access$010(CachedEntity cachedEntity) {
            int i = cachedEntity.references;
            cachedEntity.references = i - 1;
            return i;
        }

        public CachedEntity(Bitmap bitmap2) {
            this.bitmap = bitmap2;
        }
    }

    public AnimatedFramesBuffer(AnimatedImage animatedImage, Scheduler scheduler, String str) {
        this.mDrawableId = str;
        this.mImageWidth = animatedImage.getWidth();
        this.mImageHeight = animatedImage.getHeight();
        this.mFrameCount = animatedImage.getFrameCount();
        this.mMaxCacheCount = Math.min(6, Math.max(1, 5242880 / ((this.mImageWidth * this.mImageHeight) * 4)));
        this.mMaxFreeCount = Math.min(3, Math.max(1, 2097152 / ((this.mImageWidth * this.mImageHeight) * 4)));
        this.mDecodeScheduler = scheduler;
        this.mCachedEntities = new SparseArray<>(this.mMaxCacheCount);
        this.mFreeBitmaps = new ArrayList(this.mMaxFreeCount);
        this.mDecodeFlights = new SparseArray<>(this.mMaxCacheCount);
        this.mFrameCompositor = new AnimatedFrameCompositor(animatedImage, this, str);
    }

    public synchronized Bitmap getCachedBitmapAt(int i) {
        CachedEntity cachedEntity = this.mCachedEntities.get(i);
        if (cachedEntity == null) {
            return null;
        }
        CachedEntity.access$008(cachedEntity);
        return cachedEntity.bitmap;
    }

    private CachedEntity newCachedEntityAt(int i) {
        Bitmap remove;
        synchronized (this) {
            remove = this.mFreeBitmaps.size() > 0 ? this.mFreeBitmaps.remove(0) : null;
        }
        if (remove == null && Pexode.isAshmemSupported()) {
            remove = AshmemBitmapFactory.instance().newBitmapWithPin(this.mImageWidth, this.mImageHeight, Bitmap.Config.ARGB_8888);
        }
        if (remove == null) {
            remove = Bitmap.createBitmap(this.mImageWidth, this.mImageHeight, Bitmap.Config.ARGB_8888);
        }
        this.mFrameCompositor.renderFrame(i, remove);
        return new CachedEntity(remove);
    }

    public synchronized void startCacheFrom(int i, int i2, Runnable runnable) {
        Preconditions.checkArgument(i >= 0);
        Preconditions.checkArgument(i2 > 0);
        if (i2 > this.mMaxCacheCount) {
            i2 = this.mMaxCacheCount;
        }
        int max = this.mFrameCompositor.getFrameInfoAt(i).mDisposalMode == AnimatedDrawableFrameInfo.DisposalMode.DISPOSE_TO_PREVIOUS ? Math.max(0, i - 1) : i;
        int i3 = max;
        while (true) {
            if (i3 < 0) {
                i3 = -1;
                break;
            } else if (this.mCachedEntities.get(i3) != null) {
                break;
            } else {
                i3--;
            }
        }
        int i4 = (max + i2) % this.mFrameCount;
        int i5 = 0;
        while (i5 < this.mCachedEntities.size()) {
            int keyAt = this.mCachedEntities.keyAt(i5);
            if (keyAt != i3) {
                if (!isInRange(max, i4, keyAt)) {
                    CachedEntity valueAt = this.mCachedEntities.valueAt(i5);
                    this.mCachedEntities.removeAt(i5);
                    if (valueAt != null && valueAt.references <= 0) {
                        recycleAsFreeBitmap(valueAt.bitmap);
                    }
                }
            }
            i5++;
        }
        int i6 = 0;
        while (i6 < this.mDecodeFlights.size()) {
            if (isInRange(max, i2, this.mDecodeFlights.keyAt(i6))) {
                i6++;
            } else {
                this.mDecodeFlights.removeAt(i6);
            }
        }
        for (int i7 = 0; i7 < i2; i7++) {
            int i8 = (max + i7) % this.mFrameCount;
            if (this.mCachedEntities.get(i8) != null) {
                if (i == i8) {
                    this.mHandler.post(runnable);
                }
            } else if (i == i8) {
                this.mDecodeFlights.put(i8, runnable);
            } else {
                this.mDecodeFlights.put(i8, (Object) null);
            }
        }
        if (!this.mDecodeRunning) {
            this.mDecodeRunning = true;
            this.mDecodeScheduler.schedule(this.mWeakDecodeAction);
        }
    }

    public void startCacheFrom(int i, Runnable runnable) {
        startCacheFrom(i, this.mMaxCacheCount, runnable);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r4.mCachedEntities.get(r0) != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        if (r1 == false) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        r1 = newCachedEntityAt(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r4.mCachedEntities.put(r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
        if (r2 == null) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0040, code lost:
        r4.mHandler.post(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0022, code lost:
        monitor-enter(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDecodeActionRun() {
        /*
            r4 = this;
        L_0x0000:
            monitor-enter(r4)
            android.util.SparseArray<java.lang.Runnable> r0 = r4.mDecodeFlights     // Catch:{ all -> 0x0049 }
            int r0 = r0.size()     // Catch:{ all -> 0x0049 }
            r1 = 0
            if (r0 > 0) goto L_0x000e
            r4.mDecodeRunning = r1     // Catch:{ all -> 0x0049 }
            monitor-exit(r4)     // Catch:{ all -> 0x0049 }
            return
        L_0x000e:
            android.util.SparseArray<java.lang.Runnable> r0 = r4.mDecodeFlights     // Catch:{ all -> 0x0049 }
            int r0 = r0.keyAt(r1)     // Catch:{ all -> 0x0049 }
            android.util.SparseArray<java.lang.Runnable> r2 = r4.mDecodeFlights     // Catch:{ all -> 0x0049 }
            java.lang.Object r2 = r2.valueAt(r1)     // Catch:{ all -> 0x0049 }
            java.lang.Runnable r2 = (java.lang.Runnable) r2     // Catch:{ all -> 0x0049 }
            android.util.SparseArray<java.lang.Runnable> r3 = r4.mDecodeFlights     // Catch:{ all -> 0x0049 }
            r3.removeAt(r1)     // Catch:{ all -> 0x0049 }
            monitor-exit(r4)     // Catch:{ all -> 0x0049 }
            monitor-enter(r4)
            android.util.SparseArray<com.taobao.phenix.animate.AnimatedFramesBuffer$CachedEntity> r3 = r4.mCachedEntities     // Catch:{ all -> 0x0046 }
            java.lang.Object r3 = r3.get(r0)     // Catch:{ all -> 0x0046 }
            if (r3 != 0) goto L_0x002c
            r1 = 1
        L_0x002c:
            monitor-exit(r4)     // Catch:{ all -> 0x0046 }
            if (r1 == 0) goto L_0x003e
            com.taobao.phenix.animate.AnimatedFramesBuffer$CachedEntity r1 = r4.newCachedEntityAt(r0)
            monitor-enter(r4)
            android.util.SparseArray<com.taobao.phenix.animate.AnimatedFramesBuffer$CachedEntity> r3 = r4.mCachedEntities     // Catch:{ all -> 0x003b }
            r3.put(r0, r1)     // Catch:{ all -> 0x003b }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            goto L_0x003e
        L_0x003b:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r0
        L_0x003e:
            if (r2 == 0) goto L_0x0000
            android.os.Handler r0 = r4.mHandler
            r0.post(r2)
            goto L_0x0000
        L_0x0046:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0046 }
            throw r0
        L_0x0049:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0049 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.animate.AnimatedFramesBuffer.onDecodeActionRun():void");
    }

    public synchronized void freeBitmap(Bitmap bitmap) {
        int size = this.mCachedEntities.size();
        int i = 0;
        while (true) {
            if (i < size) {
                CachedEntity valueAt = this.mCachedEntities.valueAt(i);
                if (valueAt != null && valueAt.bitmap == bitmap) {
                    CachedEntity.access$010(valueAt);
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (i == size) {
            recycleAsFreeBitmap(bitmap);
        }
    }

    public synchronized void dropCaches() {
        this.mFrameCompositor.dropCaches();
        this.mDecodeFlights.clear();
        this.mCachedEntities.clear();
        this.mFreeBitmaps.clear();
        UnitedLog.d("AnimatedImage", "%s dropped frame caches", this.mDrawableId);
    }

    private void recycleAsFreeBitmap(Bitmap bitmap) {
        if (this.mFreeBitmaps.size() < this.mMaxFreeCount && bitmap != null && bitmap.isMutable() && bitmap.getWidth() == this.mImageWidth && bitmap.getHeight() == this.mImageHeight && !this.mFreeBitmaps.contains(bitmap)) {
            this.mFreeBitmaps.add(bitmap);
        }
    }

    private static class WeakDecodeAction extends ScheduledAction {
        private WeakReference<AnimatedFramesBuffer> frameBufferRef;

        public WeakDecodeAction(AnimatedFramesBuffer animatedFramesBuffer) {
            super(1, (Consumer<?, ? extends RequestContext>) null, (ScheduleResultWrapper) null, false);
            this.frameBufferRef = new WeakReference<>(animatedFramesBuffer);
        }

        public void run(Consumer consumer, ScheduleResultWrapper scheduleResultWrapper) {
            AnimatedFramesBuffer animatedFramesBuffer = (AnimatedFramesBuffer) this.frameBufferRef.get();
            if (animatedFramesBuffer != null) {
                animatedFramesBuffer.onDecodeActionRun();
            }
        }
    }
}
