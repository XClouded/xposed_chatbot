package com.taobao.phenix.cache.memory;

import android.content.res.Resources;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.weex.el.parse.Operators;
import java.util.HashSet;
import java.util.Set;

public abstract class CachedRootImage implements ReleasableReferenceListener {
    private final int mDiskCacheCatalog;
    private final String mDiskCacheKey;
    private final int mDiskPriority;
    private boolean mIsReferenceDirty;
    private final String mMemoryCacheKey;
    private boolean mRecycled;
    private final Set<Integer> mReleasableReferenceSet = new HashSet(2);
    private boolean mReleasedFromCache;

    public abstract int getSize();

    /* access modifiers changed from: protected */
    public abstract PassableBitmapDrawable newBitmapDrawable(String str, String str2, int i, int i2, boolean z, Resources resources);

    /* access modifiers changed from: protected */
    public void onCanBeRecycled() {
    }

    /* access modifiers changed from: protected */
    public void onChange2NotRecycled() {
    }

    public CachedRootImage(String str, String str2, int i, int i2) {
        this.mMemoryCacheKey = str;
        this.mDiskCacheKey = str2;
        this.mDiskCacheCatalog = i;
        this.mDiskPriority = i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0056, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void addReference(com.taobao.phenix.cache.memory.PassableBitmapDrawable r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 != 0) goto L_0x0005
            monitor-exit(r6)
            return
        L_0x0005:
            boolean r0 = r6.mRecycled     // Catch:{ all -> 0x0057 }
            r1 = 0
            if (r0 == 0) goto L_0x000f
            r6.mRecycled = r1     // Catch:{ all -> 0x0057 }
            r6.onChange2NotRecycled()     // Catch:{ all -> 0x0057 }
        L_0x000f:
            boolean r0 = r6.mIsReferenceDirty     // Catch:{ all -> 0x0057 }
            if (r0 == 0) goto L_0x0015
            monitor-exit(r6)
            return
        L_0x0015:
            boolean r0 = r7 instanceof com.taobao.phenix.cache.memory.ReleasableBitmapDrawable     // Catch:{ all -> 0x0057 }
            r2 = 1
            if (r0 == 0) goto L_0x0053
            java.util.Set<java.lang.Integer> r0 = r6.mReleasableReferenceSet     // Catch:{ all -> 0x0057 }
            int r3 = r7.hashCode()     // Catch:{ all -> 0x0057 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0057 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x0057 }
            if (r0 == 0) goto L_0x0048
            r6.mIsReferenceDirty = r2     // Catch:{ all -> 0x0057 }
            java.lang.String r0 = "ImageRecycle"
            java.lang.String r3 = "references dirty now(last releasable drawable same with the hash is lost), refer=%d, image=%s, drawable=%s"
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0057 }
            java.util.Set<java.lang.Integer> r5 = r6.mReleasableReferenceSet     // Catch:{ all -> 0x0057 }
            int r5 = r5.size()     // Catch:{ all -> 0x0057 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0057 }
            r4[r1] = r5     // Catch:{ all -> 0x0057 }
            r4[r2] = r6     // Catch:{ all -> 0x0057 }
            r1 = 2
            r4[r1] = r7     // Catch:{ all -> 0x0057 }
            com.taobao.phenix.common.UnitedLog.w(r0, r3, r4)     // Catch:{ all -> 0x0057 }
            goto L_0x0055
        L_0x0048:
            java.util.Set<java.lang.Integer> r0 = r6.mReleasableReferenceSet     // Catch:{ all -> 0x0057 }
            r0.add(r3)     // Catch:{ all -> 0x0057 }
            com.taobao.phenix.cache.memory.ReleasableBitmapDrawable r7 = (com.taobao.phenix.cache.memory.ReleasableBitmapDrawable) r7     // Catch:{ all -> 0x0057 }
            r7.setReleasableReferenceListener(r6)     // Catch:{ all -> 0x0057 }
            goto L_0x0055
        L_0x0053:
            r6.mIsReferenceDirty = r2     // Catch:{ all -> 0x0057 }
        L_0x0055:
            monitor-exit(r6)
            return
        L_0x0057:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.cache.memory.CachedRootImage.addReference(com.taobao.phenix.cache.memory.PassableBitmapDrawable):void");
    }

    public synchronized void removeFromCache() {
        this.mIsReferenceDirty = true;
    }

    public synchronized void releaseFromCache(boolean z) {
        if (this.mRecycled && !z) {
            this.mRecycled = false;
            onChange2NotRecycled();
        }
        this.mReleasedFromCache = z;
        UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "release from cache, result=%b, isDirty=%b, refer=%d, image=%s", Boolean.valueOf(z), Boolean.valueOf(this.mIsReferenceDirty), Integer.valueOf(this.mReleasableReferenceSet.size()), this);
        recycleIfPossible();
    }

    public synchronized void onReferenceReleased(ReleasableBitmapDrawable releasableBitmapDrawable) {
        if (releasableBitmapDrawable != null) {
            this.mReleasableReferenceSet.remove(Integer.valueOf(releasableBitmapDrawable.hashCode()));
            UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "image reference released, isDirty=%b, refer=%d, image=%s, drawable=%s", Boolean.valueOf(this.mIsReferenceDirty), Integer.valueOf(this.mReleasableReferenceSet.size()), this, releasableBitmapDrawable);
            recycleIfPossible();
        }
    }

    public synchronized void onReferenceDowngrade2Passable(ReleasableBitmapDrawable releasableBitmapDrawable) {
        if (releasableBitmapDrawable != null) {
            this.mIsReferenceDirty = true;
            releasableBitmapDrawable.setReleasableReferenceListener((ReleasableReferenceListener) null);
            this.mReleasableReferenceSet.remove(Integer.valueOf(releasableBitmapDrawable.hashCode()));
            UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "image reference downgraded to passable, isDirty=%b, refer=%d, image=%s, drawable=%s", Boolean.valueOf(this.mIsReferenceDirty), Integer.valueOf(this.mReleasableReferenceSet.size()), this, releasableBitmapDrawable);
        }
    }

    private void recycleIfPossible() {
        if (!this.mRecycled && !this.mIsReferenceDirty && this.mReleasedFromCache && this.mReleasableReferenceSet.size() == 0) {
            onCanBeRecycled();
            this.mRecycled = true;
        }
    }

    public String getMemoryCacheKey() {
        return this.mMemoryCacheKey;
    }

    public PassableBitmapDrawable newImageDrawableWith(boolean z, Resources resources) {
        PassableBitmapDrawable newBitmapDrawable = newBitmapDrawable(this.mMemoryCacheKey, this.mDiskCacheKey, this.mDiskCacheCatalog, this.mDiskPriority, z, resources);
        addReference(newBitmapDrawable);
        return newBitmapDrawable;
    }

    public String toString() {
        return getClass().getSimpleName() + Operators.BRACKET_START_STR + Integer.toHexString(hashCode()) + ", key@" + this.mMemoryCacheKey + Operators.BRACKET_END_STR;
    }
}
