package com.alibaba.aliweex.interceptor.phenix;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.alibaba.aliweex.interceptor.IWeexAnalyzerInspector;
import com.alibaba.aliweex.interceptor.InspectRequest;
import com.alibaba.aliweex.interceptor.InspectResponse;
import com.alibaba.aliweex.interceptor.NetworkEventReporterProxy;
import com.alibaba.aliweex.interceptor.TrackerManager;
import com.alibaba.aliweex.interceptor.WeexAnalyzerInspectorImpl;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.ui.module.WXModalUIModule;
import com.taobao.weex.utils.WXLogUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;

public class PhenixTracker {
    private static final int REPORT_IMAGE_QUALITY = 100;
    private static final String TAG = "PhenixTracker";
    private static boolean enabled = true;
    private boolean isDevToolEnabled = false;
    /* access modifiers changed from: private */
    public IWeexAnalyzerInspector mAnalyzerInspector;
    /* access modifiers changed from: private */
    public NetworkEventReporterProxy mEventReporter;
    private ExecutorService mExecutor = null;
    private final int mRequestId = TrackerManager.nextRequestId();
    @Nullable
    private String mRequestIdString;

    private PhenixTracker() {
        if (WXEnvironment.isApkDebugable()) {
            this.mEventReporter = NetworkEventReporterProxy.getInstance();
            this.mAnalyzerInspector = WeexAnalyzerInspectorImpl.createDefault();
            this.mExecutor = Executors.newSingleThreadExecutor();
            this.isDevToolEnabled = this.mEventReporter.isEnabled();
            WXLogUtils.d(TAG, "Create new instance " + toString());
        }
    }

    public static PhenixTracker newInstance() {
        return new PhenixTracker();
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public void preRequest(final PhenixCreator phenixCreator, final Map<String, String> map) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    InspectRequest inspectRequest = new InspectRequest();
                    inspectRequest.setUrl(phenixCreator.url());
                    inspectRequest.setRequestId(PhenixTracker.this.getRequestId());
                    inspectRequest.setMethod("GET");
                    inspectRequest.setFriendlyName("Phenix");
                    for (Map.Entry entry : map.entrySet()) {
                        inspectRequest.addHeader((String) entry.getKey(), (String) entry.getValue());
                    }
                    PhenixTracker.this.mEventReporter.requestWillBeSent(inspectRequest);
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled()) {
            try {
                this.mAnalyzerInspector.onRequest("image", new IWeexAnalyzerInspector.InspectorRequest(TextUtils.isEmpty(phenixCreator.url()) ? "unknown" : phenixCreator.url(), "GET", map));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void onSuccess(final SuccPhenixEvent succPhenixEvent) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    InspectResponse inspectResponse = new InspectResponse();
                    inspectResponse.setRequestId(PhenixTracker.this.getRequestId());
                    inspectResponse.setFromDiskCache(succPhenixEvent.isFromDisk());
                    inspectResponse.setStatusCode(succPhenixEvent.isFromDisk() ? 304 : 200);
                    inspectResponse.setReasonPhrase(succPhenixEvent.isFromDisk() ? "FROM DISK CACHE" : WXModalUIModule.OK);
                    inspectResponse.setUrl(succPhenixEvent.getUrl());
                    Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                    if (bitmap == null) {
                        PhenixTracker.this.mEventReporter.responseReadFailed(PhenixTracker.this.getRequestId(), "event getbitmap obj is null");
                        return;
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap.CompressFormat access$200 = PhenixTracker.this.decideFormat(succPhenixEvent.getUrl());
                    bitmap.compress(access$200, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    inspectResponse.addHeader("Content-Type", PhenixTracker.this.decideContentType(access$200));
                    inspectResponse.addHeader("Content-Length", byteArray.length + "");
                    PhenixTracker.this.mEventReporter.responseHeadersReceived(inspectResponse);
                    PhenixTracker.this.mEventReporter.interpretResponseStream(PhenixTracker.this.getRequestId(), PhenixTracker.this.decideContentType(access$200), (String) null, new ByteArrayInputStream(byteArray), false);
                    PhenixTracker.this.mEventReporter.responseReadFinished(PhenixTracker.this.getRequestId());
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled() && this.mExecutor != null && !this.mExecutor.isShutdown()) {
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    int i;
                    try {
                        Bitmap bitmap = succPhenixEvent.getDrawable().getBitmap();
                        if (bitmap == null) {
                            i = 0;
                        } else {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(PhenixTracker.this.decideFormat(succPhenixEvent.getUrl()), 100, byteArrayOutputStream);
                            i = byteArrayOutputStream.toByteArray().length;
                        }
                        IWeexAnalyzerInspector access$400 = PhenixTracker.this.mAnalyzerInspector;
                        String url = TextUtils.isEmpty(succPhenixEvent.getUrl()) ? "unknown" : succPhenixEvent.getUrl();
                        access$400.onResponse("image", new IWeexAnalyzerInspector.InspectorResponse(url, Collections.singletonMap("Content-Length", i + "").toString(), succPhenixEvent.isFromDisk() ? 304 : 200, (Map<String, List<String>>) null));
                    } catch (Exception e) {
                        WXLogUtils.e(PhenixTracker.TAG, e.getMessage());
                    }
                }
            });
        }
    }

    public void onFail(final FailPhenixEvent failPhenixEvent) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    NetworkEventReporterProxy access$100 = PhenixTracker.this.mEventReporter;
                    String access$000 = PhenixTracker.this.getRequestId();
                    access$100.httpExchangeFailed(access$000, "Error code: " + failPhenixEvent.getResultCode());
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled()) {
            try {
                this.mAnalyzerInspector.onResponse("image", new IWeexAnalyzerInspector.InspectorResponse(TextUtils.isEmpty(failPhenixEvent.getUrl()) ? "unknown" : failPhenixEvent.getUrl(), "download failed", 200, (Map<String, List<String>>) null));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public Bitmap.CompressFormat decideFormat(String str) {
        if (str != null) {
            if (str.endsWith(".webp") || str.endsWith(".WEBP")) {
                return Bitmap.CompressFormat.WEBP;
            }
            if (str.endsWith(".png") || str.endsWith(".PNG")) {
                return Bitmap.CompressFormat.PNG;
            }
        }
        return Bitmap.CompressFormat.JPEG;
    }

    /* renamed from: com.alibaba.aliweex.interceptor.phenix.PhenixTracker$5  reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat = new int[Bitmap.CompressFormat.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                android.graphics.Bitmap$CompressFormat[] r0 = android.graphics.Bitmap.CompressFormat.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$graphics$Bitmap$CompressFormat = r0
                int[] r0 = $SwitchMap$android$graphics$Bitmap$CompressFormat     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.WEBP     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$CompressFormat     // Catch:{ NoSuchFieldError -> 0x001f }
                android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.interceptor.phenix.PhenixTracker.AnonymousClass5.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public String decideContentType(Bitmap.CompressFormat compressFormat) {
        switch (AnonymousClass5.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()]) {
            case 1:
                return "image/webp";
            case 2:
                return "image/png";
            default:
                return "image/jpeg";
        }
    }

    private boolean canReport() {
        return enabled && WXEnvironment.isApkDebugable() && this.mEventReporter != null && this.isDevToolEnabled;
    }

    /* access modifiers changed from: private */
    public String getRequestId() {
        if (this.mRequestIdString == null) {
            this.mRequestIdString = String.valueOf(this.mRequestId);
        }
        return this.mRequestIdString;
    }
}
