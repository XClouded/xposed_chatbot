package com.taobao.weex;

import android.net.Uri;
import android.text.TextUtils;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.LogDetail;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXHttpListener implements IWXHttpAdapter.OnHttpListener {
    private WXRenderStrategy flag;
    private WXSDKInstance instance;
    private boolean isInstanceReady;
    public boolean isPreDownLoadMode;
    private boolean isResponseHasWait;
    private String jsonInitData;
    private WXInstanceApm mApmForInstance;
    private String mBundleUrl;
    private LogDetail mLogDetail;
    private WXResponse mResponse;
    private IWXUserTrackAdapter mUserTrackAdapter;
    private WXPerformance mWXPerformance;
    private Map<String, Object> options;
    private String pageName;
    private long startRequestTime;
    private int traceId;

    public void onFail(WXResponse wXResponse) {
    }

    public void onHttpUploadProgress(int i) {
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance) {
        this.isPreDownLoadMode = false;
        this.isInstanceReady = false;
        this.isResponseHasWait = false;
        if (wXSDKInstance != null) {
            this.mLogDetail = wXSDKInstance.mTimeCalculator.createLogDetail("downloadBundleJS");
        }
        this.instance = wXSDKInstance;
        this.traceId = WXTracing.nextId();
        this.mWXPerformance = wXSDKInstance.getWXPerformance();
        this.mApmForInstance = wXSDKInstance.getApmForInstance();
        this.mUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
        if (WXTracing.isAvailable()) {
            WXTracing.TraceEvent newEvent = WXTracing.newEvent("downloadBundleJS", wXSDKInstance.getInstanceId(), -1);
            newEvent.iid = wXSDKInstance.getInstanceId();
            newEvent.tname = "Network";
            newEvent.ph = "B";
            newEvent.traceId = this.traceId;
            newEvent.submit();
        }
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance, String str) {
        this(wXSDKInstance);
        this.startRequestTime = System.currentTimeMillis();
        this.mBundleUrl = str;
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy, long j) {
        this(wXSDKInstance);
        this.pageName = str;
        this.options = map;
        this.jsonInitData = str2;
        this.flag = wXRenderStrategy;
        this.startRequestTime = j;
        this.mBundleUrl = wXSDKInstance.getBundleUrl();
    }

    public void setSDKInstance(WXSDKInstance wXSDKInstance) {
        this.instance = wXSDKInstance;
    }

    /* access modifiers changed from: protected */
    public WXSDKInstance getInstance() {
        return this.instance;
    }

    public void onHttpStart() {
        if (this.instance != null && this.instance.getWXStatisticsListener() != null) {
            this.instance.getWXStatisticsListener().onHttpStart();
            if (this.mLogDetail != null) {
                this.mLogDetail.taskStart();
            }
        }
    }

    public void onHeadersReceived(int i, Map<String, List<String>> map) {
        if (!(this.instance == null || this.instance.getWXStatisticsListener() == null)) {
            this.instance.getWXStatisticsListener().onHeadersReceived();
            this.instance.onHttpStart();
        }
        if (this.instance != null && this.instance.responseHeaders != null && map != null) {
            this.instance.responseHeaders.putAll(map);
        }
    }

    public void onHttpResponseProgress(int i) {
        this.instance.getApmForInstance().extInfo.put(WXInstanceApm.VALUE_BUNDLE_LOAD_LENGTH, Integer.valueOf(i));
    }

    public void onHttpFinish(WXResponse wXResponse) {
        if (this.mLogDetail != null) {
            this.mLogDetail.taskEnd();
        }
        if (!(this.instance == null || this.instance.getWXStatisticsListener() == null)) {
            this.instance.getWXStatisticsListener().onHttpFinish();
        }
        if (WXTracing.isAvailable()) {
            WXTracing.TraceEvent newEvent = WXTracing.newEvent("downloadBundleJS", this.instance.getInstanceId(), -1);
            newEvent.traceId = this.traceId;
            newEvent.tname = "Network";
            newEvent.ph = "E";
            newEvent.extParams = new HashMap();
            if (!(wXResponse == null || wXResponse.originalData == null)) {
                newEvent.extParams.put("BundleSize", Integer.valueOf(wXResponse.originalData.length));
            }
            newEvent.submit();
        }
        this.mWXPerformance.networkTime = System.currentTimeMillis() - this.startRequestTime;
        if (!(wXResponse == null || wXResponse.extendParams == null)) {
            this.mApmForInstance.updateRecordInfo(wXResponse.extendParams);
            Object obj = wXResponse.extendParams.get("actualNetworkTime");
            long j = 0;
            this.mWXPerformance.actualNetworkTime = obj instanceof Long ? ((Long) obj).longValue() : 0;
            Object obj2 = wXResponse.extendParams.get("pureNetworkTime");
            this.mWXPerformance.pureNetworkTime = obj2 instanceof Long ? ((Long) obj2).longValue() : 0;
            Object obj3 = wXResponse.extendParams.get("connectionType");
            this.mWXPerformance.connectionType = obj3 instanceof String ? (String) obj3 : "";
            Object obj4 = wXResponse.extendParams.get("packageSpendTime");
            this.mWXPerformance.packageSpendTime = obj4 instanceof Long ? ((Long) obj4).longValue() : 0;
            Object obj5 = wXResponse.extendParams.get("syncTaskTime");
            WXPerformance wXPerformance = this.mWXPerformance;
            if (obj5 instanceof Long) {
                j = ((Long) obj5).longValue();
            }
            wXPerformance.syncTaskTime = j;
            Object obj6 = wXResponse.extendParams.get("requestType");
            this.mWXPerformance.requestType = obj6 instanceof String ? (String) obj6 : "none";
            Object obj7 = wXResponse.extendParams.get(WXPerformance.Dimension.cacheType.toString());
            if (obj7 instanceof String) {
                this.mWXPerformance.cacheType = (String) obj7;
            }
            Object obj8 = wXResponse.extendParams.get("zCacheInfo");
            this.mWXPerformance.zCacheInfo = obj8 instanceof String ? (String) obj8 : "";
            if (isNet(this.mWXPerformance.requestType) && this.mUserTrackAdapter != null) {
                WXPerformance wXPerformance2 = new WXPerformance(this.instance.getInstanceId());
                if (!TextUtils.isEmpty(this.mBundleUrl)) {
                    try {
                        wXPerformance2.args = Uri.parse(this.mBundleUrl).buildUpon().clearQuery().toString();
                    } catch (Exception unused) {
                        wXPerformance2.args = this.pageName;
                    }
                }
                if (!AlipayAuthConstant.LoginResult.SUCCESS.equals(wXResponse.statusCode)) {
                    wXPerformance2.errCode = WXErrorCode.WX_ERR_JSBUNDLE_DOWNLOAD.getErrorCode();
                    wXPerformance2.appendErrMsg(wXResponse.errorCode);
                    wXPerformance2.appendErrMsg("|");
                    wXPerformance2.appendErrMsg(wXResponse.errorMsg);
                } else if (!AlipayAuthConstant.LoginResult.SUCCESS.equals(wXResponse.statusCode) || (wXResponse.originalData != null && wXResponse.originalData.length > 0)) {
                    wXPerformance2.errCode = WXErrorCode.WX_SUCCESS.getErrorCode();
                } else {
                    wXPerformance2.errCode = WXErrorCode.WX_ERR_JSBUNDLE_DOWNLOAD.getErrorCode();
                    wXPerformance2.appendErrMsg(wXResponse.statusCode);
                    wXPerformance2.appendErrMsg("|template is null!");
                }
                if (this.mUserTrackAdapter != null) {
                    this.mUserTrackAdapter.commit(this.instance.getContext(), (String) null, IWXUserTrackAdapter.JS_DOWNLOAD, wXPerformance2, (Map<String, Serializable>) null);
                }
            }
        }
        if (!this.isPreDownLoadMode) {
            didHttpFinish(wXResponse);
        } else if (this.isInstanceReady) {
            WXLogUtils.d("test->", "DownLoad didHttpFinish on http");
            didHttpFinish(wXResponse);
        } else {
            WXLogUtils.d("test->", "DownLoad end before activity created");
            this.mResponse = wXResponse;
            this.isResponseHasWait = true;
        }
    }

    public void onInstanceReady() {
        if (this.isPreDownLoadMode) {
            this.isInstanceReady = true;
            if (this.isResponseHasWait) {
                WXLogUtils.d("test->", "preDownLoad didHttpFinish on ready");
                didHttpFinish(this.mResponse);
            }
        }
    }

    private void didHttpFinish(WXResponse wXResponse) {
        String str = "0";
        if (wXResponse != null && wXResponse.originalData != null && TextUtils.equals(AlipayAuthConstant.LoginResult.SUCCESS, wXResponse.statusCode)) {
            this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_END);
            onSuccess(wXResponse);
        } else if (TextUtils.equals(WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode(), wXResponse.statusCode)) {
            WXLogUtils.e("user intercept: WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR");
            str = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
            WXSDKInstance wXSDKInstance = this.instance;
            wXSDKInstance.onRenderError(str, "|response.errorMsg==" + wXResponse.errorMsg + "|instance bundleUrl = \n" + this.instance.getBundleUrl() + "|instance requestUrl = \n" + Uri.decode(WXSDKInstance.requestUrl));
            onFail(wXResponse);
        } else if (wXResponse == null || wXResponse.originalData == null || !TextUtils.equals("-206", wXResponse.statusCode)) {
            str = WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode();
            this.instance.onRenderError(str, wXResponse.errorMsg);
            onFail(wXResponse);
        } else {
            WXLogUtils.e("user intercept: WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED");
            str = WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorCode();
            WXSDKInstance wXSDKInstance2 = this.instance;
            wXSDKInstance2.onRenderError(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorCode() + "|response.errorMsg==" + wXResponse.errorMsg);
            onFail(wXResponse);
        }
        if (!"0".equals(str)) {
            this.mApmForInstance.addProperty(WXInstanceApm.KEY_PROPERTIES_ERROR_CODE, str);
        }
    }

    private boolean isNet(String str) {
        return "network".equals(str) || "2g".equals(str) || "3g".equals(str) || "4g".equals(str) || "wifi".equals(str) || "other".equals(str) || "unknown".equals(str);
    }

    public void onSuccess(WXResponse wXResponse) {
        if (this.flag == WXRenderStrategy.DATA_RENDER_BINARY) {
            this.instance.render(this.pageName, wXResponse.originalData, this.options, this.jsonInitData);
            return;
        }
        this.instance.render(this.pageName, new String(wXResponse.originalData), this.options, this.jsonInitData, this.flag);
    }
}
