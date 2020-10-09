package com.taobao.phenix.decode;

import com.taobao.pexode.PexodeOptions;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.DecodedImage;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.network.IncompleteResponseException;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.BaseChainProducer;
import com.taobao.rxm.request.RequestCancelListener;
import com.taobao.tcommon.core.Preconditions;

public class DecodeProducer extends BaseChainProducer<DecodedImage, EncodedImage, ImageRequest> implements RequestCancelListener<ImageRequest> {
    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<DecodedImage, ImageRequest> consumer) {
        return false;
    }

    public DecodeProducer() {
        super(0, 1);
    }

    private EncodedImage inspectEncodedImage(EncodedImage encodedImage) {
        EncodedDataInspector encodedDataInspector = Phenix.instance().getEncodedDataInspector();
        if (encodedDataInspector == null) {
            return encodedImage;
        }
        EncodedData inspectEncodedData = encodedDataInspector.inspectEncodedData(encodedImage.path, encodedImage);
        Preconditions.checkArgument(inspectEncodedData != null && inspectEncodedData.isAvailable(), "inspected data cannot be null or not available!");
        if (inspectEncodedData == encodedImage) {
            return encodedImage;
        }
        return encodedImage.cloneExcept(inspectEncodedData, encodedImage.sizeLevel).forceNoCache(true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:126:0x02a7, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x02a9, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x02ce, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x02ee, code lost:
        r0 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x02a7 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:117:0x0291] */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x02a9 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:117:0x0291] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0191 A[Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01aa A[SYNTHETIC, Splitter:B:67:0x01aa] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void consumeNewResult(com.taobao.rxm.consume.Consumer<com.taobao.phenix.entity.DecodedImage, com.taobao.phenix.request.ImageRequest> r19, boolean r20, com.taobao.phenix.entity.EncodedImage r21) {
        /*
            r18 = this;
            r1 = r18
            r2 = r19
            r3 = r20
            r4 = r21
            java.lang.Object r5 = r19.getContext()
            com.taobao.phenix.request.ImageRequest r5 = (com.taobao.phenix.request.ImageRequest) r5
            r7 = 0
            r8 = 1
            if (r4 == 0) goto L_0x0290
            boolean r9 = r21.isAvailable()     // Catch:{ OutOfMemoryError -> 0x028a, UnsatisfiedLinkError -> 0x0284, Throwable -> 0x027e, all -> 0x0276 }
            if (r9 != 0) goto L_0x001a
            goto L_0x0290
        L_0x001a:
            r18.onConsumeStart(r19, r20)     // Catch:{ OutOfMemoryError -> 0x028a, UnsatisfiedLinkError -> 0x0284, Throwable -> 0x027e, all -> 0x0276 }
            java.lang.String r9 = "Phenix"
            java.lang.String r10 = "Decode Started."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r9, (java.lang.String) r10, (com.taobao.phenix.request.ImageRequest) r5)     // Catch:{ OutOfMemoryError -> 0x028a, UnsatisfiedLinkError -> 0x0284, Throwable -> 0x027e, all -> 0x0276 }
            com.taobao.phenix.entity.EncodedImage r9 = r1.inspectEncodedImage(r4)     // Catch:{ OutOfMemoryError -> 0x028a, UnsatisfiedLinkError -> 0x0284, Throwable -> 0x027e, all -> 0x0276 }
            boolean r10 = r9.fromDisk     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r10 == 0) goto L_0x0036
            com.taobao.phenix.request.ImageStatistics r10 = r5.getStatistics()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r10.mRspProcessStart = r11     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x0036:
            boolean r10 = com.taobao.pexode.Pexode.isAshmemSupported()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.PexodeOptions r11 = new com.taobao.pexode.PexodeOptions     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.<init>()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.justDecodeBounds = r8     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.allowDegrade2System = r8     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.util.TypedValue r12 = r9.resourceValue     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.resourceValue = r12     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r12 = r5.isForcedAnimationStatic()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.forceStaticIfAnimation = r12     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r12 = r9.type     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r12 != r8) goto L_0x005e
            com.taobao.pexode.entity.RewindableByteArrayStream r12 = new com.taobao.pexode.entity.RewindableByteArrayStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            byte[] r13 = r9.bytes     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r14 = r9.offset     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r15 = r9.length     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12.<init>(r13, r14, r15)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13 = r12
            goto L_0x0073
        L_0x005e:
            java.io.InputStream r12 = r9.inputStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r13 = r12 instanceof java.io.FileInputStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r14 = 1048576(0x100000, float:1.469368E-39)
            if (r13 == 0) goto L_0x006e
            com.taobao.pexode.entity.RewindableFileInputStream r13 = new com.taobao.pexode.entity.RewindableFileInputStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.io.FileInputStream r12 = (java.io.FileInputStream) r12     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13.<init>(r12, r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            goto L_0x0073
        L_0x006e:
            com.taobao.pexode.entity.RewindableInputStream r13 = new com.taobao.pexode.entity.RewindableInputStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13.<init>(r12, r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x0073:
            com.taobao.pexode.Pexode.decode((java.io.InputStream) r13, (com.taobao.pexode.PexodeOptions) r11)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13.rewind()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.mimetype.MimeType r12 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r9.setMimeType(r12)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r3 == 0) goto L_0x008b
            com.taobao.phenix.request.ImageStatistics r12 = r5.getStatistics()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.mimetype.MimeType r14 = r9.getMimeType()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12.setCompressFormat(r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x008b:
            com.taobao.pexode.mimetype.MimeType r12 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r12 = com.taobao.pexode.Pexode.preferInputType(r13, r12, r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r14 = r13.getInputType()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r15 = 3
            if (r12 == r14) goto L_0x00c0
            if (r12 != r15) goto L_0x009d
            r13.back2StreamType()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x009d:
            if (r12 != r8) goto L_0x00c0
            com.taobao.phenix.intf.Phenix r12 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.builder.BytesPoolBuilder r12 = r12.bytesPoolBuilder()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.tcommon.core.BytesPool r12 = r12.build()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int[] r14 = new int[r8]     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r6 = r9.length     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r14[r7] = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.entity.EncodedData r6 = com.taobao.phenix.common.StreamUtil.readBytes((java.io.InputStream) r13, (com.taobao.tcommon.core.BytesPool) r12, (int[]) r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.entity.RewindableByteArrayStream r13 = new com.taobao.pexode.entity.RewindableByteArrayStream     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            byte[] r12 = r6.bytes     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r14 = r6.offset     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r6 = r6.length     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13.<init>(r12, r14, r6)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x00c0:
            com.taobao.pexode.mimetype.MimeType r6 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 == 0) goto L_0x00d0
            com.taobao.pexode.mimetype.MimeType r6 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r6 = r6.isAnimation()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 != 0) goto L_0x00cd
            goto L_0x00d0
        L_0x00cd:
            r6 = 0
            goto L_0x0189
        L_0x00d0:
            int r6 = r5.getMaxViewWidth()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r12 = r5.getMaxViewHeight()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.util.TypedValue r15 = r11.resourceValue     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r15 != 0) goto L_0x0134
            boolean r15 = r11.isSizeAvailable()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r15 == 0) goto L_0x0134
            if (r6 != 0) goto L_0x00e6
            if (r12 == 0) goto L_0x0134
        L_0x00e6:
            int r15 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r14 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r14 = com.taobao.phenix.common.SizeUtil.getResizedDimension(r6, r12, r15, r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r15 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r8 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r8 = com.taobao.phenix.common.SizeUtil.getResizedDimension(r12, r6, r15, r8)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r15 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r7 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r7 = com.taobao.phenix.common.SizeUtil.findBestSampleSize(r15, r7, r14, r8)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.sampleSize = r7     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.String r7 = "Decoder"
            java.lang.String r8 = "limit with maxSize, sampleSize=%d, maxSize=%dx%d, actualSize=%dx%d"
            r14 = 5
            java.lang.Object[] r14 = new java.lang.Object[r14]     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r15 = r11.sampleSize     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Integer r15 = java.lang.Integer.valueOf(r15)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r17 = 0
            r14[r17] = r15     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r15 = 1
            r14[r15] = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r12)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12 = 2
            r14[r12] = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r6 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12 = 3
            r14[r12] = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r6 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12 = 4
            r14[r12] = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.common.UnitedLog.d(r7, r5, r8, r14)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x0134:
            int r6 = r9.type     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r7 = 1
            if (r6 == r7) goto L_0x0140
            android.graphics.Rect r6 = new android.graphics.Rect     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r6.<init>()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.outPadding = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x0140:
            if (r10 == 0) goto L_0x0147
            r6 = 1
            r11.enableAshmem = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.allowDegrade2NoAshmem = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x0147:
            android.util.TypedValue r6 = r11.resourceValue     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 != 0) goto L_0x00cd
            boolean r6 = com.taobao.pexode.Pexode.isInBitmapSupported()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 == 0) goto L_0x00cd
            com.taobao.phenix.intf.Phenix r6 = com.taobao.phenix.intf.Phenix.instance()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.builder.BitmapPoolBuilder r6 = r6.bitmapPoolBuilder()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.bitmap.BitmapPool r6 = r6.build()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 == 0) goto L_0x00cd
            int r7 = r11.sampleSize     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r8 = 1
            if (r7 <= r8) goto L_0x016a
            com.taobao.pexode.Pexode.decode((java.io.InputStream) r13, (com.taobao.pexode.PexodeOptions) r11)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r13.rewind()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
        L_0x016a:
            int r7 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r8 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.graphics.Bitmap$Config r10 = com.taobao.pexode.PexodeOptions.CONFIG     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.graphics.Bitmap r6 = r6.getFromPool(r7, r8, r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r11.inBitmap = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r6 = 1
            r11.allowDegrade2NoInBitmap = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.request.ImageStatistics r6 = r5.getStatistics()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.graphics.Bitmap r7 = r11.inBitmap     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r7 == 0) goto L_0x0183
            r7 = 1
            goto L_0x0184
        L_0x0183:
            r7 = 0
        L_0x0184:
            r6.onBitmapPoolLookedUp(r7)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            goto L_0x00cd
        L_0x0189:
            r11.justDecodeBounds = r6     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r7 = r5.isCancelled()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r7 == 0) goto L_0x01aa
            java.lang.String r7 = "Decoder"
            java.lang.String r8 = "request is cancelled before image decoding"
            java.lang.Object[] r10 = new java.lang.Object[r6]     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.common.UnitedLog.i(r7, r5, r8, r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r19.onCancellation()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r9 == 0) goto L_0x01a2
            r9.release()
        L_0x01a2:
            if (r4 == 0) goto L_0x01a9
            if (r4 == r9) goto L_0x01a9
            r21.release()
        L_0x01a9:
            return
        L_0x01aa:
            r5.setPexodeOptions(r11)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r5.registerCancelListener(r1)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.PexodeResult r6 = com.taobao.pexode.Pexode.decode((java.io.InputStream) r13, (com.taobao.pexode.PexodeOptions) r11)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r5.unregisterCancelListener(r1)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r7 = 0
            r5.setPexodeOptions(r7)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 == 0) goto L_0x01cd
            com.taobao.phenix.entity.DecodedImage r7 = new com.taobao.phenix.entity.DecodedImage     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.graphics.Bitmap r8 = r6.bitmap     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.animate.AnimatedImage r6 = r6.animated     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            android.graphics.Rect r10 = r11.outPadding     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r7.<init>(r9, r8, r6, r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r6 = r7.isAvailable()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            goto L_0x01ce
        L_0x01cd:
            r6 = 0
        L_0x01ce:
            r1.onConsumeFinish(r2, r6, r3)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.String r8 = "Phenix"
            java.lang.String r10 = "Decode Finished."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r8, (java.lang.String) r10, (com.taobao.phenix.request.ImageRequest) r5)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r6 != 0) goto L_0x0228
            java.lang.String r6 = "Decoder"
            java.lang.String r7 = "decoded image not available, cancelled=%b, mimeType=%s"
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            boolean r10 = r5.isCancelled()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12 = 0
            r8[r12] = r10     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.pexode.mimetype.MimeType r10 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r12 = 1
            r8[r12] = r10     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.common.UnitedLog.e(r6, r5, r7, r8)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.decode.DecodeException r6 = new com.taobao.phenix.decode.DecodeException     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            com.taobao.phenix.decode.DecodeException$DecodedError r7 = com.taobao.phenix.decode.DecodeException.DecodedError.UNAVAILABLE_OUTPUT_ERROR     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r8.<init>()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.String r10 = "result image null, WxH="
            r8.append(r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r10 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r8.append(r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.String r10 = "x"
            r8.append(r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            int r10 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r8.append(r10)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            java.lang.String r8 = r8.toString()     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r6.<init>((com.taobao.phenix.decode.DecodeException.DecodedError) r7, (java.lang.String) r8)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            r1.dispatchError(r2, r3, r9, r6)     // Catch:{ OutOfMemoryError -> 0x0273, UnsatisfiedLinkError -> 0x0270, Throwable -> 0x026d, all -> 0x026a }
            if (r9 == 0) goto L_0x0220
            r9.release()
        L_0x0220:
            if (r4 == 0) goto L_0x0227
            if (r4 == r9) goto L_0x0227
            r21.release()
        L_0x0227:
            return
        L_0x0228:
            r2.onNewResult(r7, r3)     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            java.lang.String r6 = "Decoder"
            java.lang.String r8 = "decode complete, result=%s, WxH=%dx%d, mimeType=%s"
            r10 = 4
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            r12 = 0
            r10[r12] = r7     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            int r7 = r11.outWidth     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            r12 = 1
            r10[r12] = r7     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            int r7 = r11.outHeight     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            r12 = 2
            r10[r12] = r7     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            com.taobao.pexode.mimetype.MimeType r7 = r11.outMimeType     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            r11 = 3
            r10[r11] = r7     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            com.taobao.phenix.common.UnitedLog.d(r6, r5, r8, r10)     // Catch:{ OutOfMemoryError -> 0x0265, UnsatisfiedLinkError -> 0x0260, Throwable -> 0x025b, all -> 0x0255 }
            if (r4 == 0) goto L_0x0310
            if (r4 == r9) goto L_0x0310
            goto L_0x030d
        L_0x0255:
            r0 = move-exception
            r2 = r0
            r16 = 0
            goto L_0x0315
        L_0x025b:
            r0 = move-exception
            r6 = r0
            r7 = 0
            goto L_0x02ab
        L_0x0260:
            r0 = move-exception
            r6 = r0
            r7 = 0
            goto L_0x02d1
        L_0x0265:
            r0 = move-exception
            r6 = r0
            r7 = 0
            goto L_0x02f1
        L_0x026a:
            r0 = move-exception
            r2 = r0
            goto L_0x027a
        L_0x026d:
            r0 = move-exception
            r6 = r0
            goto L_0x0282
        L_0x0270:
            r0 = move-exception
            r6 = r0
            goto L_0x0288
        L_0x0273:
            r0 = move-exception
            r6 = r0
            goto L_0x028e
        L_0x0276:
            r0 = move-exception
            r7 = 0
        L_0x0278:
            r2 = r0
            r9 = r7
        L_0x027a:
            r16 = 1
            goto L_0x0315
        L_0x027e:
            r0 = move-exception
            r7 = 0
        L_0x0280:
            r6 = r0
            r9 = r7
        L_0x0282:
            r7 = 1
            goto L_0x02ab
        L_0x0284:
            r0 = move-exception
            r7 = 0
        L_0x0286:
            r6 = r0
            r9 = r7
        L_0x0288:
            r7 = 1
            goto L_0x02d1
        L_0x028a:
            r0 = move-exception
            r7 = 0
        L_0x028c:
            r6 = r0
            r9 = r7
        L_0x028e:
            r7 = 1
            goto L_0x02f1
        L_0x0290:
            r7 = 0
            com.taobao.phenix.decode.DecodeException r6 = new com.taobao.phenix.decode.DecodeException     // Catch:{ OutOfMemoryError -> 0x02ee, UnsatisfiedLinkError -> 0x02ce, Throwable -> 0x02a9, all -> 0x02a7 }
            com.taobao.phenix.decode.DecodeException$DecodedError r8 = com.taobao.phenix.decode.DecodeException.DecodedError.UNAVAILABLE_INPUT_ERROR     // Catch:{ OutOfMemoryError -> 0x02a5, UnsatisfiedLinkError -> 0x02a3, Throwable -> 0x02a9, all -> 0x02a7 }
            r6.<init>(r8)     // Catch:{ OutOfMemoryError -> 0x02a5, UnsatisfiedLinkError -> 0x02a3, Throwable -> 0x02a9, all -> 0x02a7 }
            r1.dispatchError(r2, r3, r4, r6)     // Catch:{ OutOfMemoryError -> 0x02a5, UnsatisfiedLinkError -> 0x02a3, Throwable -> 0x02a9, all -> 0x02a7 }
            if (r4 == 0) goto L_0x02a2
            if (r4 == 0) goto L_0x02a2
            r21.release()
        L_0x02a2:
            return
        L_0x02a3:
            r0 = move-exception
            goto L_0x0286
        L_0x02a5:
            r0 = move-exception
            goto L_0x028c
        L_0x02a7:
            r0 = move-exception
            goto L_0x0278
        L_0x02a9:
            r0 = move-exception
            goto L_0x0280
        L_0x02ab:
            java.lang.String r8 = "Decoder"
            java.lang.String r10 = "unknown error, throwable=%s"
            r11 = 1
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x0311 }
            r12 = 0
            r11[r12] = r6     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.common.UnitedLog.e(r8, r5, r10, r11)     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException r5 = new com.taobao.phenix.decode.DecodeException     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException$DecodedError r8 = com.taobao.phenix.decode.DecodeException.DecodedError.UNKNOWN_ERROR     // Catch:{ all -> 0x0311 }
            r5.<init>((com.taobao.phenix.decode.DecodeException.DecodedError) r8, (java.lang.Throwable) r6)     // Catch:{ all -> 0x0311 }
            r1.dispatchError(r2, r3, r9, r5)     // Catch:{ all -> 0x0311 }
            if (r7 == 0) goto L_0x02c9
            if (r9 == 0) goto L_0x02c9
            r9.release()
        L_0x02c9:
            if (r4 == 0) goto L_0x0310
            if (r4 == r9) goto L_0x0310
            goto L_0x030d
        L_0x02ce:
            r0 = move-exception
            r11 = 1
            goto L_0x0286
        L_0x02d1:
            java.lang.String r8 = "Decoder"
            java.lang.String r10 = "Decode UnsatisfiedLinkError"
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r8, (java.lang.String) r10, (com.taobao.phenix.request.ImageRequest) r5)     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException r5 = new com.taobao.phenix.decode.DecodeException     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException$DecodedError r8 = com.taobao.phenix.decode.DecodeException.DecodedError.UNLINK_SO_ERROR     // Catch:{ all -> 0x0311 }
            r5.<init>((com.taobao.phenix.decode.DecodeException.DecodedError) r8, (java.lang.Throwable) r6)     // Catch:{ all -> 0x0311 }
            r1.dispatchError(r2, r3, r9, r5)     // Catch:{ all -> 0x0311 }
            if (r7 == 0) goto L_0x02e9
            if (r9 == 0) goto L_0x02e9
            r9.release()
        L_0x02e9:
            if (r4 == 0) goto L_0x0310
            if (r4 == r9) goto L_0x0310
            goto L_0x030d
        L_0x02ee:
            r0 = move-exception
            r11 = 1
            goto L_0x028c
        L_0x02f1:
            java.lang.String r8 = "Decoder"
            java.lang.String r10 = "Decode OutOfMemoryError"
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r8, (java.lang.String) r10, (com.taobao.phenix.request.ImageRequest) r5)     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException r5 = new com.taobao.phenix.decode.DecodeException     // Catch:{ all -> 0x0311 }
            com.taobao.phenix.decode.DecodeException$DecodedError r8 = com.taobao.phenix.decode.DecodeException.DecodedError.OOM_ERROR     // Catch:{ all -> 0x0311 }
            r5.<init>((com.taobao.phenix.decode.DecodeException.DecodedError) r8, (java.lang.Throwable) r6)     // Catch:{ all -> 0x0311 }
            r1.dispatchError(r2, r3, r9, r5)     // Catch:{ all -> 0x0311 }
            if (r7 == 0) goto L_0x0309
            if (r9 == 0) goto L_0x0309
            r9.release()
        L_0x0309:
            if (r4 == 0) goto L_0x0310
            if (r4 == r9) goto L_0x0310
        L_0x030d:
            r21.release()
        L_0x0310:
            return
        L_0x0311:
            r0 = move-exception
            r2 = r0
            r16 = r7
        L_0x0315:
            if (r16 == 0) goto L_0x031c
            if (r9 == 0) goto L_0x031c
            r9.release()
        L_0x031c:
            if (r4 == 0) goto L_0x0323
            if (r4 == r9) goto L_0x0323
            r21.release()
        L_0x0323:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.decode.DecodeProducer.consumeNewResult(com.taobao.rxm.consume.Consumer, boolean, com.taobao.phenix.entity.EncodedImage):void");
    }

    public void onCancel(ImageRequest imageRequest) {
        UnitedLog.e("Phenix", "Decode Cancel.", imageRequest);
        PexodeOptions pexodeOptions = imageRequest.getPexodeOptions();
        if (pexodeOptions != null) {
            imageRequest.setPexodeOptions((PexodeOptions) null);
            UnitedLog.d("Decoder", imageRequest, "cancelled image decoding, result=%b", Boolean.valueOf(pexodeOptions.requestCancel()));
        }
    }

    private void dispatchError(Consumer<DecodedImage, ImageRequest> consumer, boolean z, EncodedImage encodedImage, DecodeException decodeException) {
        if (z) {
            decodeException.setLocalUri(consumer.getContext().getImageUriInfo().isLocalUri());
            Throwable th = decodeException;
            if (encodedImage != null) {
                decodeException.dataFromDisk(encodedImage.fromDisk);
                th = decodeException;
                if (!encodedImage.fromDisk) {
                    th = decodeException;
                    if (!encodedImage.completed) {
                        th = decodeException;
                        if (encodedImage.isAvailable()) {
                            UnitedLog.w("Decoder", consumer.getContext(), "actual decode error=%s, convert to error=IncompleteContentError", decodeException);
                            th = new IncompleteResponseException();
                        }
                    }
                }
            }
            consumer.onFailure(th);
            return;
        }
        UnitedLog.e("Decoder", consumer.getContext(), "intermediate result decode error=%s, request not failed yet", decodeException);
    }
}
