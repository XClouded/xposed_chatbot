package com.taobao.android.ultron.datamodel.imp;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.datamodel.ISubmitModule;
import java.util.List;

public class DMEngine {
    private static final String EMPTY_STRING = "";
    private static final String ENCODE_UTF8 = "utf-8";
    private static final String TAG = "DMEngine";
    boolean mGzip = true;
    private ParseModule mParseModule = new ParseModule();
    private ISubmitModule mSubmitModule = new SubmitModule();

    public DMEngine(boolean z) {
        this.mGzip = z;
    }

    public void setSubmitModule(ISubmitModule iSubmitModule) {
        if (iSubmitModule != null) {
            this.mSubmitModule = iSubmitModule;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean parseProcess(DMContext dMContext, JSONObject jSONObject) {
        return this.mParseModule.parseComponents(dMContext, jSONObject);
    }

    /* access modifiers changed from: package-private */
    public List<IDMComponent> getComponentsByRoot(DMContext dMContext, String str) {
        return this.mParseModule.getComponentsByRoot(dMContext, str);
    }

    /* access modifiers changed from: package-private */
    public IDMComponent getRootComponent() {
        return this.mParseModule.getRootComponent();
    }

    public String asyncRequestData(DMContext dMContext, IDMComponent iDMComponent) {
        String jSONString = JSONObject.toJSONString(this.mSubmitModule.asyncRequestData(dMContext, iDMComponent));
        UnifyLog.e(TAG, "asyncRequestData: " + jSONString);
        return this.mGzip ? compress(jSONString) : jSONString;
    }

    /* access modifiers changed from: package-private */
    public String submitRequestData(DMContext dMContext) {
        String jSONString = JSONObject.toJSONString(this.mSubmitModule.submitRequestData(dMContext));
        UnifyLog.e(TAG, "submitRequestData: " + jSONString);
        return this.mGzip ? compress(jSONString) : jSONString;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:11|(0)|17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0038, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0052, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0030 */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A[SYNTHETIC, Splitter:B:15:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0034 A[SYNTHETIC, Splitter:B:23:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0043 A[SYNTHETIC, Splitter:B:34:0x0043] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String compress(java.lang.String r5) {
        /*
            if (r5 == 0) goto L_0x0053
            boolean r0 = r5.isEmpty()
            if (r0 == 0) goto L_0x0009
            goto L_0x0053
        L_0x0009:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            int r1 = r5.length()
            r0.<init>(r1)
            r1 = 1
            r2 = 0
            r3 = 0
            java.util.zip.GZIPOutputStream r4 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x0031, all -> 0x0029 }
            r4.<init>(r0)     // Catch:{ IOException -> 0x0031, all -> 0x0029 }
            java.lang.String r2 = "utf-8"
            byte[] r5 = r5.getBytes(r2)     // Catch:{ IOException -> 0x0032, all -> 0x0027 }
            r4.write(r5)     // Catch:{ IOException -> 0x0032, all -> 0x0027 }
            r4.close()     // Catch:{ IOException -> 0x003a }
            goto L_0x003b
        L_0x0027:
            r5 = move-exception
            goto L_0x002b
        L_0x0029:
            r5 = move-exception
            r4 = r2
        L_0x002b:
            if (r4 == 0) goto L_0x0030
            r4.close()     // Catch:{ IOException -> 0x0030 }
        L_0x0030:
            throw r5     // Catch:{ all -> 0x0038 }
        L_0x0031:
            r4 = r2
        L_0x0032:
            if (r4 == 0) goto L_0x003a
            r4.close()     // Catch:{ IOException -> 0x003a }
            goto L_0x003a
        L_0x0038:
            r5 = move-exception
            goto L_0x004f
        L_0x003a:
            r1 = 0
        L_0x003b:
            if (r1 != 0) goto L_0x0043
            java.lang.String r5 = ""
            r0.close()     // Catch:{ IOException -> 0x0042 }
        L_0x0042:
            return r5
        L_0x0043:
            byte[] r5 = r0.toByteArray()     // Catch:{ all -> 0x0038 }
            r0.close()     // Catch:{ IOException -> 0x004a }
        L_0x004a:
            java.lang.String r5 = android.util.Base64.encodeToString(r5, r3)
            return r5
        L_0x004f:
            r0.close()     // Catch:{ IOException -> 0x0052 }
        L_0x0052:
            throw r5
        L_0x0053:
            java.lang.String r5 = ""
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.imp.DMEngine.compress(java.lang.String):java.lang.String");
    }
}
