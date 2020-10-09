package com.taobao.phenix.cache.disk;

import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.request.ImageRequest;

public class DiskCacheReader extends BaseDiskCacheProducer<EncodedImage, EncodedImage> {
    private DiskCacheKeyValueStore mDiskKV;

    public DiskCacheReader(DiskCacheSupplier diskCacheSupplier) {
        super(1, 0, diskCacheSupplier);
    }

    public DiskCacheReader(DiskCacheSupplier diskCacheSupplier, DiskCacheKeyValueStore diskCacheKeyValueStore) {
        super(1, 0, diskCacheSupplier);
        this.mDiskKV = diskCacheKeyValueStore;
    }

    private boolean isTTLValid(ImageRequest imageRequest) {
        return this.mDiskKV != null && this.mDiskKV.isTTLDomain(imageRequest.getPath());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bc  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0196 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean conductResult(com.taobao.rxm.consume.Consumer<com.taobao.phenix.entity.EncodedImage, com.taobao.phenix.request.ImageRequest> r20) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            java.lang.Object r0 = r20.getContext()
            r3 = r0
            com.taobao.phenix.request.ImageRequest r3 = (com.taobao.phenix.request.ImageRequest) r3
            boolean r0 = r3.isSkipCache()
            r4 = 0
            if (r0 == 0) goto L_0x0013
            return r4
        L_0x0013:
            r19.onConductStart(r20)
            java.lang.String r0 = "Phenix"
            java.lang.String r5 = "DiskCacheRead Started."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r0, (java.lang.String) r5, (com.taobao.phenix.request.ImageRequest) r3)
            com.taobao.phenix.request.ImageUriInfo r5 = r3.getImageUriInfo()
            java.lang.String r0 = r3.getDiskCacheKey()
            int r6 = r3.getDiskCacheCatalog()
            r7 = 1
            int[] r8 = new int[r7]
            boolean r9 = r5.containsCdnSize()
            if (r9 == 0) goto L_0x0037
            int r9 = r3.getAllowedSizeLevel()
            goto L_0x0038
        L_0x0037:
            r9 = 1
        L_0x0038:
            r8[r4] = r9
            com.taobao.phenix.entity.EncodedData r9 = r1.getCacheResult(r3, r0, r6, r8)
            if (r9 == 0) goto L_0x0048
            boolean r10 = r9.isAvailable()
            if (r10 == 0) goto L_0x0048
            r10 = 1
            goto L_0x0049
        L_0x0048:
            r10 = 0
        L_0x0049:
            if (r10 == 0) goto L_0x00b1
            boolean r11 = r1.isTTLValid(r3)
            if (r11 == 0) goto L_0x00b1
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00a5 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a5 }
            r13.<init>()     // Catch:{ Exception -> 0x00a5 }
            r13.append(r0)     // Catch:{ Exception -> 0x00a5 }
            r13.append(r6)     // Catch:{ Exception -> 0x00a5 }
            java.lang.String r0 = r13.toString()     // Catch:{ Exception -> 0x00a5 }
            com.taobao.phenix.cache.disk.DiskCacheKeyValueStore r6 = r1.mDiskKV     // Catch:{ Exception -> 0x00a5 }
            java.lang.String r0 = r6.get(r0)     // Catch:{ Exception -> 0x00a5 }
            boolean r6 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00a5 }
            if (r6 != 0) goto L_0x0098
            boolean r6 = android.text.TextUtils.isDigitsOnly(r0)     // Catch:{ Exception -> 0x00a5 }
            if (r6 == 0) goto L_0x0098
            com.taobao.phenix.cache.disk.DiskCacheKeyValueStore r6 = r1.mDiskKV     // Catch:{ Exception -> 0x00a5 }
            long r13 = r6.getCurrentTime()     // Catch:{ Exception -> 0x00a5 }
            java.lang.Long r0 = java.lang.Long.valueOf(r0)     // Catch:{ Exception -> 0x00a5 }
            long r15 = r0.longValue()     // Catch:{ Exception -> 0x00a5 }
            r0 = 0
            long r13 = r13 - r15
            r15 = 0
            int r0 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r0 <= 0) goto L_0x0098
            java.lang.String r0 = "Phenix"
            java.lang.String r6 = "DiskCacheRead TTL Expired"
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r0, (java.lang.String) r6, (com.taobao.phenix.request.ImageRequest) r3)     // Catch:{ Exception -> 0x0095 }
            r10 = 0
            goto L_0x0098
        L_0x0095:
            r0 = move-exception
            r10 = 0
            goto L_0x00a6
        L_0x0098:
            com.taobao.phenix.request.ImageStatistics r0 = r3.getStatistics()     // Catch:{ Exception -> 0x00a5 }
            long r13 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00a5 }
            r6 = 0
            long r13 = r13 - r11
            r0.mTTLGetTime = r13     // Catch:{ Exception -> 0x00a5 }
            goto L_0x00b1
        L_0x00a5:
            r0 = move-exception
        L_0x00a6:
            java.lang.String r6 = "TTL"
            java.lang.String r11 = "ttl get error=%s"
            java.lang.Object[] r12 = new java.lang.Object[r7]
            r12[r4] = r0
            com.taobao.tcommon.log.FLog.e((java.lang.String) r6, (java.lang.String) r11, (java.lang.Object[]) r12)
        L_0x00b1:
            java.lang.String r0 = r3.getPath()
            r6 = 2
            if (r10 == 0) goto L_0x00be
            r11 = r8[r4]
            if (r11 == r6) goto L_0x00be
            r11 = 1
            goto L_0x00bf
        L_0x00be:
            r11 = 0
        L_0x00bf:
            if (r11 == 0) goto L_0x00cc
            int r12 = r3.getProgressUpdateStep()
            if (r12 <= 0) goto L_0x00cc
            r12 = 1065353216(0x3f800000, float:1.0)
            r2.onProgressUpdate(r12)
        L_0x00cc:
            com.taobao.phenix.request.ImageStatistics r12 = r3.getStatistics()
            r12.onDiskCacheLookedUp(r10)
            if (r10 != 0) goto L_0x0136
            com.taobao.phenix.request.ImageUriInfo r12 = r3.getSecondaryUriInfo()
            if (r12 == 0) goto L_0x0136
            com.taobao.phenix.request.ImageUriInfo r0 = r3.getSecondaryUriInfo()
            java.lang.String r0 = r0.getPath()
            com.taobao.phenix.request.ImageUriInfo r9 = r3.getSecondaryUriInfo()
            java.lang.String r9 = r9.getDiskCacheKey()
            com.taobao.phenix.request.ImageUriInfo r10 = r3.getSecondaryUriInfo()
            int r10 = r10.getDiskCacheCatalog()
            r8[r4] = r7
            com.taobao.phenix.entity.EncodedData r12 = r1.getCacheResult(r3, r9, r10, r8)
            if (r12 == 0) goto L_0x0103
            boolean r13 = r12.isAvailable()
            if (r13 == 0) goto L_0x0103
            r13 = 1
            goto L_0x0104
        L_0x0103:
            r13 = 0
        L_0x0104:
            if (r13 == 0) goto L_0x010b
            r3.disableSecondary()
            r14 = 1
            goto L_0x010c
        L_0x010b:
            r14 = 0
        L_0x010c:
            com.taobao.phenix.request.ImageStatistics r15 = r3.getStatistics()
            r15.onDiskCacheLookedUp(r13)
            java.lang.String r15 = "DiskCache"
            java.lang.String r6 = "secondary read result=%B, isLast=false, secondaryKey=%s, secondaryCatalog=%d"
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.Boolean r16 = java.lang.Boolean.valueOf(r13)
            r7[r4] = r16
            r16 = 1
            r7[r16] = r9
            java.lang.Integer r9 = java.lang.Integer.valueOf(r10)
            r10 = 2
            r7[r10] = r9
            com.taobao.phenix.common.UnitedLog.d(r15, r3, r6, r7)
            r10 = r13
            r13 = r12
            r18 = r14
            r14 = r0
            r0 = r18
            goto L_0x0139
        L_0x0136:
            r14 = r0
            r13 = r9
            r0 = 0
        L_0x0139:
            r1.onConductFinish(r2, r11)
            java.lang.String r6 = "Phenix"
            java.lang.String r7 = "DiskCacheReader Finished."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r6, (java.lang.String) r7, (com.taobao.phenix.request.ImageRequest) r3)
            if (r10 == 0) goto L_0x0182
            if (r11 == 0) goto L_0x0163
            com.taobao.phenix.request.ImageStatistics r6 = r3.getStatistics()
            int r7 = r13.length
            r6.setSize(r7)
            com.taobao.phenix.request.ImageStatistics r6 = r3.getStatistics()
            long r9 = java.lang.System.currentTimeMillis()
            r6.mRspProcessStart = r9
            com.taobao.phenix.request.ImageStatistics r6 = r3.getStatistics()
            int r7 = r13.length
            long r9 = (long) r7
            r6.mRspDeflateSize = r9
        L_0x0163:
            com.taobao.phenix.entity.EncodedImage r6 = new com.taobao.phenix.entity.EncodedImage
            r15 = r8[r4]
            r16 = 1
            java.lang.String r17 = r5.getImageExtension()
            r12 = r6
            r12.<init>(r13, r14, r15, r16, r17)
            r6.isSecondary = r0
            int r0 = r5.getWidth()
            r6.targetWidth = r0
            int r0 = r5.getHeight()
            r6.targetHeight = r0
            r2.onNewResult(r6, r11)
        L_0x0182:
            if (r11 != 0) goto L_0x0196
            boolean r0 = r3.isOnlyCache()
            if (r0 == 0) goto L_0x0196
            com.taobao.phenix.cache.disk.OnlyCacheFailedException r0 = new com.taobao.phenix.cache.disk.OnlyCacheFailedException
            java.lang.String r3 = "DiskCache"
            r0.<init>(r3)
            r2.onFailure(r0)
            r2 = 1
            return r2
        L_0x0196:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.cache.disk.DiskCacheReader.conductResult(com.taobao.rxm.consume.Consumer):boolean");
    }
}
