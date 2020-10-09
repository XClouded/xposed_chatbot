package com.taobao.android.ultron.datamodel.imp;

import android.content.Context;
import com.alibaba.android.umbrella.trace.UmbrellaTracker;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.common.utils.TimeProfileUtil;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.datamodel.AbsRequestCallback;
import com.taobao.android.ultron.datamodel.DMRequestBuilder;
import com.taobao.android.ultron.datamodel.IDMContext;
import com.taobao.android.ultron.datamodel.IDMRequester;
import com.taobao.android.ultron.datamodel.IRequestCallback;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public class DMRequester implements IDMRequester {
    private static final int CACHE_REQUEST_TYPE = 10000;
    public static final String HEADER_FEATURE_KEY = "feature";
    public static final String HEADER_FEATURE_VAL = "{\"gzip\":\"true\"}";
    static final int INVALID_BIZID = -1;
    public static final String KEY_FEATURE_DATA_PARSE = "dataProcess";
    public static final String KEY_FEATURE_REQUEST_ERROR = "netRequest";
    public static final String KEY_FEATURE_VERSION = "1.0";
    public static final String KEY_IS_CACHE_DATA = "isCachaData";
    public static final String KEY_SP_FILE_NAME = "ultornSdkSpName";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";
    private static final String TAG = "DMRequester";
    public static boolean uploadUltronData = false;
    String bizName = "default";
    boolean mAsync = false;
    int mBizId = -1;
    Context mContext;
    IDMContext mDMContext;
    String mDomain;
    boolean mGzip = true;
    Map<String, String> mParams;
    boolean mPostMethod = true;
    MtopRequest mRequest;
    Map<String, String> mRequestHeaders;
    Class<?> mResponseClazz;
    boolean mSubmit = false;
    IDMComponent mTriggerComponent;
    String mUnitStrategy;
    boolean mUseWua = false;

    public DMRequester(DMRequestBuilder dMRequestBuilder) {
        if (dMRequestBuilder != null) {
            this.mGzip = dMRequestBuilder.isGzip();
            if (dMRequestBuilder.getDMContext() != null) {
                this.mDMContext = dMRequestBuilder.getDMContext();
            } else {
                this.mDMContext = new DMContext(this.mGzip);
            }
            this.mRequestHeaders = dMRequestBuilder.getHeaders();
            this.mDomain = dMRequestBuilder.getDomain();
            this.mUnitStrategy = dMRequestBuilder.getUnitStrategy();
            this.mAsync = dMRequestBuilder.isAsync();
            this.mSubmit = dMRequestBuilder.isSubmit();
            this.mUseWua = dMRequestBuilder.isUseWua();
            this.mPostMethod = dMRequestBuilder.isPostMethod();
            this.mBizId = dMRequestBuilder.getBizId();
            this.mTriggerComponent = dMRequestBuilder.getTriggerComponent();
            this.mParams = dMRequestBuilder.getParams();
            this.mResponseClazz = dMRequestBuilder.getResponseClazz();
            this.bizName = dMRequestBuilder.getBizName();
            this.mRequest = new MtopRequest();
            this.mRequest.setApiName(dMRequestBuilder.getApi());
            this.mRequest.setVersion(dMRequestBuilder.getVersion());
            this.mRequest.setNeedSession(dMRequestBuilder.isNeedSession());
            this.mRequest.setNeedEcode(dMRequestBuilder.isNeedEcode());
            this.mContext = dMRequestBuilder.getContext();
            this.mDMContext.setBizName(this.bizName);
            ((DMContext) this.mDMContext).setContext(dMRequestBuilder.getContext());
        }
    }

    /* access modifiers changed from: private */
    public void uploadDataForTest(MtopBusiness mtopBusiness) {
        if (uploadUltronData) {
            try {
                doRealUpload(mtopBusiness);
            } catch (Throwable th) {
                UnifyLog.e(TAG, "uploadDataForTest exception: " + th.getMessage());
            }
        }
    }

    private void doRealUpload(MtopBusiness mtopBusiness) {
        if (mtopBusiness != null && mtopBusiness.getMtopContext() != null) {
            MtopRequest mtopRequest = mtopBusiness.getMtopContext().mtopRequest;
            MtopResponse mtopResponse = mtopBusiness.getMtopContext().mtopResponse;
            String str = "default_ttid";
            if (mtopBusiness.getMtopContext().property != null) {
                str = mtopBusiness.getMtopContext().property.ttid;
            }
            if (mtopRequest != null && mtopResponse != null) {
                Map<String, List<String>> headerFields = mtopResponse.getHeaderFields();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("userId", (Object) getSpUserId(this.mContext));
                jSONObject.put("userNick", (Object) getSpUserName(this.mContext));
                jSONObject.put("api", (Object) mtopRequest.getApiName());
                jSONObject.put("request", (Object) mtopRequest.getData());
                jSONObject.put(NetworkEventSender.TYPE_RESPONSE, (Object) new String(mtopResponse.getBytedata()));
                jSONObject.put("ttid", (Object) str);
                if (headerFields != null) {
                    jSONObject.put("responseHeaders", (Object) JSON.toJSON(headerFields).toString());
                }
                MtopRequest mtopRequest2 = new MtopRequest();
                mtopRequest2.setApiName("mtop.taobao.ultron.upload");
                mtopRequest2.setVersion("1.0");
                mtopRequest2.setData(jSONObject.toJSONString());
                MtopBusiness build = MtopBusiness.build(mtopRequest2);
                build.useWua().reqMethod(MethodEnum.POST).addListener((MtopListener) new IRemoteBaseListener() {
                    public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                        UnifyLog.e(DMRequester.TAG, "uploadDataForTest onSystemError: " + mtopResponse.getRetMsg());
                    }

                    public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                        UnifyLog.e(DMRequester.TAG, "uploadDataForTest onSuccess: " + mtopResponse.getRetMsg());
                    }

                    public void onError(int i, MtopResponse mtopResponse, Object obj) {
                        UnifyLog.e(DMRequester.TAG, "uploadDataForTest onError: " + mtopResponse.getRetMsg());
                    }
                });
                build.startRequest();
            }
        }
    }

    private String getSpUserName(Context context) {
        return context.getSharedPreferences(KEY_SP_FILE_NAME, 0).getString(KEY_USER_NAME, "");
    }

    private String getSpUserId(Context context) {
        return context.getSharedPreferences(KEY_SP_FILE_NAME, 0).getString("userId", "");
    }

    public static void updateSpUserInfo(Context context, String str, String str2) {
        if (context != null && str2 != null && str != null) {
            context.getSharedPreferences(KEY_SP_FILE_NAME, 0).edit().putString(KEY_USER_NAME, str).putString("userId", str2).apply();
        }
    }

    class Response implements IRemoteBaseListener, IRemoteCacheListener {
        DMContext mDMContext;
        MtopBusiness mMtopBusiness;
        AbsRequestCallback mOuterCallback;

        Response(AbsRequestCallback absRequestCallback, DMContext dMContext, MtopBusiness mtopBusiness) {
            this.mDMContext = dMContext;
            this.mOuterCallback = absRequestCallback;
            this.mMtopBusiness = mtopBusiness;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:29:0x014d, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x01a4, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            r0.printStackTrace();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x01a9, code lost:
            com.taobao.android.ultron.common.utils.UnifyLog.e(com.taobao.android.ultron.datamodel.imp.DMRequester.TAG, "submit onSuccess callback error", r0.getMessage());
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [B:27:0x013a, B:37:0x0173] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSuccess(int r20, mtopsdk.mtop.domain.MtopResponse r21, mtopsdk.mtop.domain.BaseOutDo r22, java.lang.Object r23) {
            /*
                r19 = this;
                r1 = r19
                r0 = r21
                com.taobao.android.ultron.datamodel.imp.DMRequester r2 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                com.taobao.tao.remotebusiness.MtopBusiness r3 = r1.mMtopBusiness
                r2.uploadDataForTest(r3)
                java.lang.String r2 = "DMRequester"
                r8 = 1
                java.lang.String[] r3 = new java.lang.String[r8]
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "onSuccess, request: "
                r4.append(r5)
                com.taobao.android.ultron.datamodel.imp.DMRequester r5 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                mtopsdk.mtop.domain.MtopRequest r5 = r5.mRequest
                java.lang.String r5 = r5.toString()
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                r9 = 0
                r3[r9] = r4
                com.taobao.android.ultron.common.utils.UnifyLog.e(r2, r3)
                if (r0 == 0) goto L_0x004f
                java.lang.String r2 = "DMRequester"
                java.lang.String[] r3 = new java.lang.String[r8]
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "onSuccess, response: "
                r4.append(r5)
                org.json.JSONObject r5 = r21.getDataJsonObject()
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                r3[r9] = r4
                com.taobao.android.ultron.common.utils.UnifyLog.e(r2, r3)
            L_0x004f:
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "DMRequester-"
                r2.append(r3)
                com.taobao.android.ultron.datamodel.imp.DMRequester r3 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                mtopsdk.mtop.domain.MtopRequest r3 = r3.mRequest
                java.lang.String r3 = r3.getApiName()
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "DMRequester onSuccess: "
                r3.append(r4)
                com.taobao.android.ultron.datamodel.imp.DMRequester r4 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                mtopsdk.mtop.domain.MtopRequest r4 = r4.mRequest
                java.lang.String r4 = r4.getApiName()
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                com.taobao.android.ultron.common.utils.TimeProfileUtil.end(r2, r3)
                java.lang.String r2 = "ultronProfile"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "onSucess: "
                r3.append(r4)
                com.taobao.android.ultron.datamodel.imp.DMRequester r4 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                mtopsdk.mtop.domain.MtopRequest r4 = r4.mRequest
                java.lang.String r4 = r4.getApiName()
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                com.taobao.android.ultron.common.utils.TimeProfileUtil.stage(r2, r3)
                com.taobao.android.ultron.datamodel.imp.DMContext r2 = r1.mDMContext
                boolean r2 = r2.isCacheData()
                if (r2 == 0) goto L_0x00b4
                com.taobao.android.ultron.datamodel.imp.DMContext r2 = r1.mDMContext
                r2.reset()
                com.taobao.android.ultron.datamodel.imp.DMContext r2 = r1.mDMContext
                r2.setCacheData(r9)
            L_0x00b4:
                com.taobao.android.ultron.datamodel.AbsRequestCallback r2 = r1.mOuterCallback     // Catch:{ Throwable -> 0x00c1 }
                r3 = r20
                r5 = r23
                boolean r2 = r2.isDealDataOuter(r3, r0, r5)     // Catch:{ Throwable -> 0x00c5 }
                if (r2 == 0) goto L_0x00c5
                return
            L_0x00c1:
                r3 = r20
                r5 = r23
            L_0x00c5:
                java.lang.String r2 = "ultronProfile"
                java.lang.String r4 = "isDealDataOuter"
                com.taobao.android.ultron.common.utils.TimeProfileUtil.stage(r2, r4)
                com.taobao.android.ultron.datamodel.imp.DMRequester r2 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                boolean r2 = r2.mSubmit
                r10 = 2
                if (r2 == 0) goto L_0x00f5
                com.taobao.android.ultron.datamodel.AbsRequestCallback r2 = r1.mOuterCallback     // Catch:{ Throwable -> 0x00e2 }
                com.taobao.android.ultron.datamodel.imp.DMContext r6 = r1.mDMContext     // Catch:{ Throwable -> 0x00e2 }
                r7 = 0
                r3 = r20
                r4 = r21
                r5 = r23
                r2.onSuccess(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x00e2 }
                goto L_0x00f4
            L_0x00e2:
                r0 = move-exception
                java.lang.String r2 = "DMRequester"
                java.lang.String[] r3 = new java.lang.String[r10]
                java.lang.String r4 = "submit onSuccess callback error"
                r3[r9] = r4
                java.lang.String r0 = r0.getMessage()
                r3[r8] = r0
                com.taobao.android.ultron.common.utils.UnifyLog.e(r2, r3)
            L_0x00f4:
                return
            L_0x00f5:
                com.taobao.android.ultron.datamodel.imp.ParseResponseHelper r7 = new com.taobao.android.ultron.datamodel.imp.ParseResponseHelper
                com.taobao.android.ultron.datamodel.imp.DMContext r2 = r1.mDMContext
                r7.<init>(r2)
                byte[] r2 = r21.getBytedata()
                java.lang.Class<com.alibaba.fastjson.JSONObject> r4 = com.alibaba.fastjson.JSONObject.class
                com.alibaba.fastjson.parser.Feature[] r6 = new com.alibaba.fastjson.parser.Feature[r9]
                java.lang.Object r2 = com.alibaba.fastjson.JSON.parseObject((byte[]) r2, (java.lang.reflect.Type) r4, (com.alibaba.fastjson.parser.Feature[]) r6)
                com.alibaba.fastjson.JSONObject r2 = (com.alibaba.fastjson.JSONObject) r2
                java.lang.String r4 = "data"
                com.alibaba.fastjson.JSONObject r14 = r2.getJSONObject(r4)
                r7.parseProtocolFeatures(r14)
                java.math.BigInteger r4 = com.taobao.android.ultron.datamodel.imp.ProtocolFeatures.FEATURE_CONTAINER_CACHE
                boolean r4 = r7.hasFeature(r4)
                if (r4 == 0) goto L_0x012a
                com.taobao.android.ultron.datamodel.imp.DMRequester r4 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                android.content.Context r12 = r4.mContext
                com.taobao.android.ultron.datamodel.imp.DMRequester r4 = com.taobao.android.ultron.datamodel.imp.DMRequester.this
                java.lang.String r13 = r4.bizName
                r15 = 1
                r16 = 1
                r11 = r7
                r11.processCache(r12, r13, r14, r15, r16)
            L_0x012a:
                r7.parseResponse((com.alibaba.fastjson.JSONObject) r2)
                java.lang.String r2 = "ultronProfile"
                java.lang.String r4 = "parse complete"
                com.taobao.android.ultron.common.utils.TimeProfileUtil.stage(r2, r4)
                boolean r2 = r7.isSuccess()
                if (r2 == 0) goto L_0x014f
                com.taobao.android.ultron.datamodel.AbsRequestCallback r2 = r1.mOuterCallback     // Catch:{ Throwable -> 0x014d }
                com.taobao.android.ultron.datamodel.imp.DMContext r6 = r1.mDMContext     // Catch:{ Throwable -> 0x014d }
                java.util.Map r7 = r7.getExtraData()     // Catch:{ Throwable -> 0x014d }
                r3 = r20
                r4 = r21
                r5 = r23
                r2.onSuccess(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x014d }
                goto L_0x01ba
            L_0x014d:
                r0 = move-exception
                goto L_0x01a9
            L_0x014f:
                com.taobao.android.ultron.datamodel.AbsRequestCallback r2 = r1.mOuterCallback     // Catch:{ Throwable -> 0x014d }
                r6 = 1
                java.util.Map r11 = r7.getExtraData()     // Catch:{ Throwable -> 0x014d }
                r3 = r20
                r4 = r21
                r5 = r23
                r0 = r7
                r7 = r11
                r2.onError(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x014d }
                java.util.Map r0 = r0.getExtraData()     // Catch:{ Throwable -> 0x014d }
                if (r0 != 0) goto L_0x0168
                return
            L_0x0168:
                java.lang.String r2 = "protocolVersion"
                java.lang.Object r0 = r0.get(r2)     // Catch:{ Throwable -> 0x014d }
                boolean r2 = r0 instanceof java.lang.String     // Catch:{ Throwable -> 0x014d }
                if (r2 != 0) goto L_0x0173
                return
            L_0x0173:
                java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x01a4 }
                float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ Exception -> 0x01a4 }
                double r2 = (double) r0     // Catch:{ Exception -> 0x01a4 }
                r4 = 4611911198408756429(0x4000cccccccccccd, double:2.1)
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r0 <= 0) goto L_0x01ba
                java.lang.String r11 = "dataProcess"
                com.taobao.android.ultron.datamodel.imp.DMRequester r0 = com.taobao.android.ultron.datamodel.imp.DMRequester.this     // Catch:{ Exception -> 0x01a4 }
                mtopsdk.mtop.domain.MtopRequest r0 = r0.mRequest     // Catch:{ Exception -> 0x01a4 }
                java.lang.String r12 = r0.getApiName()     // Catch:{ Exception -> 0x01a4 }
                com.taobao.android.ultron.datamodel.imp.DMRequester r0 = com.taobao.android.ultron.datamodel.imp.DMRequester.this     // Catch:{ Exception -> 0x01a4 }
                mtopsdk.mtop.domain.MtopRequest r0 = r0.mRequest     // Catch:{ Exception -> 0x01a4 }
                java.lang.String r13 = r0.getVersion()     // Catch:{ Exception -> 0x01a4 }
                com.taobao.android.ultron.datamodel.imp.DMRequester r0 = com.taobao.android.ultron.datamodel.imp.DMRequester.this     // Catch:{ Exception -> 0x01a4 }
                java.lang.String r14 = r0.bizName     // Catch:{ Exception -> 0x01a4 }
                r15 = 0
                r16 = 0
                java.lang.String r17 = "parse response error"
                java.lang.String r18 = "error msg"
                com.alibaba.android.umbrella.trace.UmbrellaTracker.commitFailureStability(r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x01a4 }
                goto L_0x01ba
            L_0x01a4:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ Throwable -> 0x014d }
                goto L_0x01ba
            L_0x01a9:
                java.lang.String r2 = "DMRequester"
                java.lang.String[] r3 = new java.lang.String[r10]
                java.lang.String r4 = "submit onSuccess callback error"
                r3[r9] = r4
                java.lang.String r0 = r0.getMessage()
                r3[r8] = r0
                com.taobao.android.ultron.common.utils.UnifyLog.e(r2, r3)
            L_0x01ba:
                java.lang.String r0 = "ultronProfile"
                java.lang.String r2 = "callback complete"
                com.taobao.android.ultron.common.utils.TimeProfileUtil.stage(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.imp.DMRequester.Response.onSuccess(int, mtopsdk.mtop.domain.MtopResponse, mtopsdk.mtop.domain.BaseOutDo, java.lang.Object):void");
        }

        public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
            DMRequester.this.uploadDataForTest(this.mMtopBusiness);
            UnifyLog.e(DMRequester.TAG, "onSystemError: errorCode:" + mtopResponse.getRetCode() + ",errorMsg:" + mtopResponse.getRetMsg() + ",request: " + DMRequester.this.mRequest.toString());
            StringBuilder sb = new StringBuilder();
            sb.append("DMRequester-");
            sb.append(DMRequester.this.mRequest.getApiName());
            String sb2 = sb.toString();
            TimeProfileUtil.end(sb2, "onSystemError: " + DMRequester.this.mRequest.getApiName());
            this.mDMContext.setCacheData(false);
            try {
                if (this.mOuterCallback != null && !this.mOuterCallback.isDealDataOuter(i, mtopResponse, obj)) {
                    this.mOuterCallback.onError(i, mtopResponse, obj, false, (Map<String, ? extends Object>) null);
                }
            } catch (Exception e) {
                UnifyLog.e(DMRequester.TAG, "onSystemError 节点onError回调处理错误出错", e.getMessage());
            }
            UmbrellaTracker.commitFailureStability("netRequest", DMRequester.this.mRequest.getApiName(), DMRequester.this.mRequest.getVersion(), DMRequester.this.bizName, (String) null, (Map<String, String>) null, mtopResponse.getRetCode(), mtopResponse.getRetMsg());
        }

        public void onError(int i, MtopResponse mtopResponse, Object obj) {
            DMRequester.this.uploadDataForTest(this.mMtopBusiness);
            UnifyLog.e(DMRequester.TAG, "onError: errorCode:" + mtopResponse.getRetCode() + ",errorMsg:" + mtopResponse.getRetMsg() + ",request: " + DMRequester.this.mRequest.toString());
            StringBuilder sb = new StringBuilder();
            sb.append("DMRequester-");
            sb.append(DMRequester.this.mRequest.getApiName());
            String sb2 = sb.toString();
            TimeProfileUtil.end(sb2, "onError: " + DMRequester.this.mRequest.getApiName());
            this.mDMContext.setCacheData(false);
            try {
                if (this.mOuterCallback != null && !this.mOuterCallback.isDealDataOuter(i, mtopResponse, obj)) {
                    this.mOuterCallback.onError(i, mtopResponse, obj, false, (Map<String, ? extends Object>) null);
                }
            } catch (Exception e) {
                UnifyLog.e(DMRequester.TAG, "onError 节点onError回调处理错误出错", e.getMessage());
            }
            UmbrellaTracker.commitFailureStability("netRequest", DMRequester.this.mRequest.getApiName(), DMRequester.this.mRequest.getVersion(), DMRequester.this.bizName, (String) null, (Map<String, String>) null, mtopResponse.getRetCode(), mtopResponse.getRetMsg());
        }

        public void onCached(MtopCacheEvent mtopCacheEvent, BaseOutDo baseOutDo, Object obj) {
            UnifyLog.e(DMRequester.TAG, "onCached,request: " + DMRequester.this.mRequest.toString());
            if (mtopCacheEvent.getMtopResponse() != null) {
                UnifyLog.e(DMRequester.TAG, "onCached, response: " + mtopCacheEvent.getMtopResponse().getDataJsonObject());
            }
            TimeProfileUtil.stageFromStart("DMRequester-" + DMRequester.this.mRequest.getApiName(), "onCached: " + DMRequester.this.mRequest.getApiName());
            this.mDMContext.setCacheData(true);
            MtopResponse mtopResponse = mtopCacheEvent.getMtopResponse();
            try {
                if (this.mOuterCallback.isDealDataOuter(10000, mtopResponse, obj)) {
                    return;
                }
            } catch (Throwable unused) {
            }
            if (DMRequester.this.mSubmit) {
                try {
                    this.mOuterCallback.onSuccess(10000, mtopResponse, obj, this.mDMContext, (Map<String, ? extends Object>) null);
                } catch (Throwable th) {
                    UnifyLog.e(DMRequester.TAG, "submit onSuccess callback error", th.getMessage());
                }
            } else {
                ParseResponseHelper parseResponseHelper = new ParseResponseHelper(this.mDMContext);
                parseResponseHelper.parseResponse(mtopResponse);
                if (parseResponseHelper.isSuccess()) {
                    try {
                        this.mOuterCallback.onSuccess(10000, mtopResponse, obj, this.mDMContext, parseResponseHelper.getExtraData());
                    } catch (Throwable th2) {
                        UnifyLog.e(DMRequester.TAG, "submit onSuccess callback error", th2.getMessage());
                    }
                } else {
                    parseResponseHelper.addExtraData(DMRequester.KEY_IS_CACHE_DATA, "true");
                    this.mOuterCallback.onError(10000, mtopResponse, obj, true, parseResponseHelper.getExtraData());
                }
            }
        }
    }

    class DelegateIRequestCallback extends AbsRequestCallback {
        private IRequestCallback mCallback;

        public DelegateIRequestCallback(IRequestCallback iRequestCallback) {
            this.mCallback = iRequestCallback;
        }

        public void onError(int i, MtopResponse mtopResponse, Object obj, boolean z, Map<String, ?> map) {
            if (this.mCallback != null) {
                this.mCallback.onError(i, mtopResponse, obj, false, map);
            }
        }

        public void onSuccess(int i, MtopResponse mtopResponse, Object obj, IDMContext iDMContext, Map<String, ?> map) {
            if (this.mCallback != null) {
                this.mCallback.onSuccess(i, mtopResponse, obj, iDMContext, map);
            }
        }
    }

    public boolean execute(AbsRequestCallback absRequestCallback) {
        return doExecute((Object) null, absRequestCallback);
    }

    public boolean execute(Object obj, AbsRequestCallback absRequestCallback) {
        return doExecute(obj, absRequestCallback);
    }

    public boolean execute(IRequestCallback iRequestCallback) {
        return doExecute((Object) null, new DelegateIRequestCallback(iRequestCallback));
    }

    public String getFeature() {
        return this.mGzip ? HEADER_FEATURE_VAL : "";
    }

    public String getAsyncData() {
        if (!(this.mDMContext instanceof DMContext)) {
            return "";
        }
        DMContext dMContext = (DMContext) this.mDMContext;
        return dMContext.getEngine().asyncRequestData(dMContext, this.mTriggerComponent);
    }

    public String getAsyncData(IDMComponent iDMComponent) {
        if (!(this.mDMContext instanceof DMContext)) {
            return "";
        }
        DMContext dMContext = (DMContext) this.mDMContext;
        return dMContext.getEngine().asyncRequestData(dMContext, iDMComponent);
    }

    public String getSubmitData() {
        if (!(this.mDMContext instanceof DMContext)) {
            return "";
        }
        DMContext dMContext = (DMContext) this.mDMContext;
        return dMContext.getEngine().submitRequestData(dMContext);
    }

    private void updateUltronUploadParams() {
        JSONObject parseObject = JSON.parseObject(this.mParams.get("exParams"));
        if (parseObject == null) {
            parseObject = new JSONObject();
        }
        parseObject.put("ultron_data_record", (Object) "true");
        if (parseObject.containsKey("tradeProtocolFeatures")) {
            BigInteger bigInteger = null;
            try {
                bigInteger = new BigInteger(parseObject.getString("tradeProtocolFeatures"));
            } catch (Exception unused) {
            }
            if (ProtocolFeatures.hasFeature(bigInteger, ProtocolFeatures.FEATURE_CONTAINER_CACHE)) {
                bigInteger = ProtocolFeatures.removeFeature(bigInteger, ProtocolFeatures.FEATURE_CONTAINER_CACHE);
            }
            parseObject.put("tradeProtocolFeatures", (Object) bigInteger.toString());
        }
        this.mParams.put("exParams", parseObject.toJSONString());
    }

    private boolean doExecute(Object obj, AbsRequestCallback absRequestCallback) {
        if (!(this.mDMContext instanceof DMContext)) {
            return false;
        }
        DMContext dMContext = (DMContext) this.mDMContext;
        if (this.mParams == null) {
            this.mParams = new HashMap();
        }
        if (uploadUltronData) {
            try {
                updateUltronUploadParams();
            } catch (Throwable th) {
                UnifyLog.e(TAG, "doExecute ultron params error: " + th.getMessage());
            }
        }
        if (this.mAsync) {
            if (this.mGzip) {
                this.mParams.put(HEADER_FEATURE_KEY, HEADER_FEATURE_VAL);
            }
            this.mParams.put("params", dMContext.getEngine().asyncRequestData(dMContext, this.mTriggerComponent));
            JSONObject jSONObject = new JSONObject();
            jSONObject.putAll(this.mParams);
            this.mRequest.setData(jSONObject.toJSONString());
        } else if (this.mSubmit) {
            if (this.mGzip) {
                this.mParams.put(HEADER_FEATURE_KEY, HEADER_FEATURE_VAL);
            }
            this.mParams.put("params", dMContext.getEngine().submitRequestData(dMContext));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putAll(this.mParams);
            this.mRequest.setData(jSONObject2.toJSONString());
        } else {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.putAll(this.mParams);
            this.mRequest.setData(jSONObject3.toJSONString());
        }
        MtopBusiness build = MtopBusiness.build(this.mRequest);
        if (this.mUseWua) {
            build.useWua();
        }
        if (this.mPostMethod) {
            build.reqMethod(MethodEnum.POST);
        }
        if (this.mDomain != null) {
            build.setCustomDomain(this.mDomain);
        }
        if (-1 != this.mBizId) {
            build.setBizId(this.mBizId);
        }
        if (this.mUnitStrategy != null) {
            build.setUnitStrategy(this.mUnitStrategy);
        }
        if (this.mRequestHeaders != null) {
            build.mtopProp.setRequestHeaders(this.mRequestHeaders);
        }
        if (obj != null) {
            build.reqContext(obj);
        }
        build.setErrorNotifyAfterCache(true);
        Response response = new Response(absRequestCallback, dMContext, build);
        if (this.mResponseClazz == null) {
            build.addListener((MtopListener) response).startRequest();
        } else {
            build.addListener((MtopListener) response).startRequest(this.mResponseClazz);
        }
        UnifyLog.e(TAG, "send request: " + this.mRequest.toString());
        TimeProfileUtil.start("DMRequester-" + this.mRequest.getApiName(), "begin request: " + this.mRequest.getApiName());
        return true;
    }
}
