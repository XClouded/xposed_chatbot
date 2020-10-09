package com.taobao.phenix.loader.file;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.loader.StreamResultHandler;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.SchemeInfo;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.BaseChainProducer;

public class LocalImageProducer extends BaseChainProducer<EncodedImage, EncodedImage, ImageRequest> {
    private static final int LOCAL_TYPE = 1;
    private static final int NETWORK_TYPE = 0;
    private static final int SECONDARY_LOCAL_TYPE = 2;
    private final FileLoader mFileLoader;

    public LocalImageProducer(FileLoader fileLoader) {
        super(1, 0);
        this.mFileLoader = fileLoader;
    }

    private EncodedData readLocalData(Consumer<EncodedImage, ImageRequest> consumer, boolean z, SchemeInfo schemeInfo, String str) throws Exception {
        ImageRequest context = consumer.getContext();
        ResponseData load = this.mFileLoader.load(schemeInfo, str, context.getLoaderExtras());
        int i = 0;
        if (context.isCancelled()) {
            UnitedLog.i("LocalFile", context, "Request is cancelled before reading file", new Object[0]);
            consumer.onCancellation();
            load.release();
            return null;
        }
        int i2 = load.length;
        if (!z) {
            i = context.getProgressUpdateStep();
        }
        StreamResultHandler streamResultHandler = new StreamResultHandler(consumer, i2, i);
        EncodedData transformFrom = EncodedData.transformFrom(load, streamResultHandler);
        if (streamResultHandler.isCancellationCalled()) {
            return null;
        }
        return transformFrom;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00d3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean conductResult(com.taobao.rxm.consume.Consumer<com.taobao.phenix.entity.EncodedImage, com.taobao.phenix.request.ImageRequest> r17) {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            java.lang.Object r0 = r17.getContext()
            r3 = r0
            com.taobao.phenix.request.ImageRequest r3 = (com.taobao.phenix.request.ImageRequest) r3
            com.taobao.phenix.request.ImageUriInfo r4 = r3.getImageUriInfo()
            com.taobao.phenix.request.ImageUriInfo r0 = r3.getSecondaryUriInfo()
            com.taobao.phenix.request.SchemeInfo r5 = r4.getSchemeInfo()
            boolean r6 = r5.isLocalUri()
            r7 = 2
            r8 = 1
            r9 = 0
            if (r6 == 0) goto L_0x0022
            r6 = 1
            goto L_0x0031
        L_0x0022:
            if (r0 == 0) goto L_0x0030
            com.taobao.phenix.request.SchemeInfo r6 = r0.getSchemeInfo()
            boolean r6 = r6.isLocalUri()
            if (r6 == 0) goto L_0x0030
            r6 = 2
            goto L_0x0031
        L_0x0030:
            r6 = 0
        L_0x0031:
            if (r6 != 0) goto L_0x0034
            return r9
        L_0x0034:
            r10 = 0
            java.lang.String r11 = r4.getPath()
            r16.onConductStart(r17)
            java.lang.String r12 = "Phenix"
            java.lang.String r13 = "LocalImage started."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r12, (java.lang.String) r13, (com.taobao.phenix.request.ImageRequest) r3)
            switch(r6) {
                case 1: goto L_0x0090;
                case 2: goto L_0x004b;
                default: goto L_0x0046;
            }
        L_0x0046:
            r12 = r11
            r0 = 0
        L_0x0048:
            r11 = r10
            goto L_0x00c7
        L_0x004b:
            java.lang.String r5 = r0.getPath()     // Catch:{ Exception -> 0x0083 }
            com.taobao.phenix.request.SchemeInfo r0 = r0.getSchemeInfo()     // Catch:{ Exception -> 0x0080 }
            com.taobao.phenix.entity.EncodedData r11 = r1.readLocalData(r2, r8, r0, r5)     // Catch:{ Exception -> 0x0080 }
            java.lang.Object r0 = r17.getContext()     // Catch:{ Exception -> 0x007d }
            com.taobao.phenix.request.ImageRequest r0 = (com.taobao.phenix.request.ImageRequest) r0     // Catch:{ Exception -> 0x007d }
            r0.disableSecondary()     // Catch:{ Exception -> 0x007d }
            java.lang.String r0 = "LocalFile"
            java.lang.String r10 = "load file(secondary) result=%B"
            java.lang.Object[] r12 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x007d }
            if (r11 == 0) goto L_0x0070
            boolean r13 = r11.isAvailable()     // Catch:{ Exception -> 0x007d }
            if (r13 == 0) goto L_0x0070
            r13 = 1
            goto L_0x0071
        L_0x0070:
            r13 = 0
        L_0x0071:
            java.lang.Boolean r13 = java.lang.Boolean.valueOf(r13)     // Catch:{ Exception -> 0x007d }
            r12[r9] = r13     // Catch:{ Exception -> 0x007d }
            com.taobao.phenix.common.UnitedLog.dp(r0, r5, r10, r12)     // Catch:{ Exception -> 0x007d }
            r12 = r5
            r0 = 0
            goto L_0x00c7
        L_0x007d:
            r0 = move-exception
            r10 = r11
            goto L_0x0081
        L_0x0080:
            r0 = move-exception
        L_0x0081:
            r11 = r5
            goto L_0x0084
        L_0x0083:
            r0 = move-exception
        L_0x0084:
            java.lang.String r5 = "LocalFile"
            java.lang.String r12 = "load file(secondary) error=%s"
            java.lang.Object[] r13 = new java.lang.Object[r8]
            r13[r9] = r0
            com.taobao.phenix.common.UnitedLog.ep(r5, r11, r12, r13)
            goto L_0x0046
        L_0x0090:
            com.taobao.phenix.entity.EncodedData r5 = r1.readLocalData(r2, r9, r5, r11)     // Catch:{ Exception -> 0x00b5 }
            java.lang.String r0 = "LocalFile"
            java.lang.String r10 = "load file result=%B"
            java.lang.Object[] r12 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x00b2 }
            if (r5 == 0) goto L_0x00a4
            boolean r13 = r5.isAvailable()     // Catch:{ Exception -> 0x00b2 }
            if (r13 == 0) goto L_0x00a4
            r13 = 1
            goto L_0x00a5
        L_0x00a4:
            r13 = 0
        L_0x00a5:
            java.lang.Boolean r13 = java.lang.Boolean.valueOf(r13)     // Catch:{ Exception -> 0x00b2 }
            r12[r9] = r13     // Catch:{ Exception -> 0x00b2 }
            com.taobao.phenix.common.UnitedLog.dp(r0, r11, r10, r12)     // Catch:{ Exception -> 0x00b2 }
            r12 = r11
            r0 = 1
            r11 = r5
            goto L_0x00c7
        L_0x00b2:
            r0 = move-exception
            r10 = r5
            goto L_0x00b6
        L_0x00b5:
            r0 = move-exception
        L_0x00b6:
            java.lang.String r5 = "LocalFile"
            java.lang.String r12 = "load file error=%s"
            java.lang.Object[] r13 = new java.lang.Object[r8]
            r13[r9] = r0
            com.taobao.phenix.common.UnitedLog.ep(r5, r11, r12, r13)
            r2.onFailure(r0)
            r12 = r11
            r0 = 1
            goto L_0x0048
        L_0x00c7:
            r1.onConductFinish(r2, r0)
            java.lang.String r5 = "Phenix"
            java.lang.String r10 = "LocalImage Finished."
            com.taobao.phenix.common.UnitedLog.e((java.lang.String) r5, (java.lang.String) r10, (com.taobao.phenix.request.ImageRequest) r3)
            if (r11 == 0) goto L_0x0106
            if (r0 == 0) goto L_0x00f1
            com.taobao.phenix.request.ImageStatistics r5 = r3.getStatistics()
            int r10 = r11.length
            r5.setSize(r10)
            com.taobao.phenix.request.ImageStatistics r5 = r3.getStatistics()
            long r13 = java.lang.System.currentTimeMillis()
            r5.mRspProcessStart = r13
            com.taobao.phenix.request.ImageStatistics r3 = r3.getStatistics()
            int r5 = r11.length
            long r13 = (long) r5
            r3.mRspDeflateSize = r13
        L_0x00f1:
            com.taobao.phenix.entity.EncodedImage r3 = new com.taobao.phenix.entity.EncodedImage
            r13 = 1
            r14 = 1
            java.lang.String r15 = r4.getImageExtension()
            r10 = r3
            r10.<init>(r11, r12, r13, r14, r15)
            if (r6 != r7) goto L_0x0100
            goto L_0x0101
        L_0x0100:
            r8 = 0
        L_0x0101:
            r3.isSecondary = r8
            r2.onNewResult(r3, r0)
        L_0x0106:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.loader.file.LocalImageProducer.conductResult(com.taobao.rxm.consume.Consumer):boolean");
    }
}
