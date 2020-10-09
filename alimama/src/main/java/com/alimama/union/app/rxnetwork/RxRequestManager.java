package com.alimama.union.app.rxnetwork;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IMtop;
import com.alimama.moon.App;
import com.alimama.moon.BuildConfig;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.taobao.login4android.Login;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.MtopBuilder;
import org.json.JSONObject;

public class RxRequestManager {
    private static final String TAG = "RxRequestManager";
    private static RxRequestManager sRequestManager;

    private RxRequestManager() {
        App.getAppComponent().inject(this);
    }

    public static RxRequestManager getInstance() {
        if (sRequestManager == null) {
            sRequestManager = new RxRequestManager();
        }
        return sRequestManager;
    }

    public RxResponse doSyncRequest(RxMtopRequest rxMtopRequest) {
        RxResponse rxResponse = new RxResponse();
        ApiInfo apiInfo = rxMtopRequest.getApiInfo();
        if (apiInfo == null) {
            rxResponse.isReqSuccess = false;
            return rxResponse;
        }
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        if (iMtop == null) {
            return rxResponse;
        }
        MtopRequest mtopRequest = new MtopRequest();
        MtopBuilder build = iMtop.getMtop().build(mtopRequest, BuildConfig.TTID);
        if (rxMtopRequest.isEnablePost()) {
            build.reqMethod(MethodEnum.POST);
        }
        if (apiInfo.useWua()) {
            build.useWua();
        }
        mtopRequest.setApiName(apiInfo.getAPIName());
        mtopRequest.setVersion(apiInfo.getVersion());
        mtopRequest.setNeedSession(apiInfo.needSession());
        mtopRequest.setNeedEcode(apiInfo.needECode());
        String jSONObject = new JSONObject(rxMtopRequest.getParams()).toString();
        mtopRequest.setData(jSONObject);
        build.retryTime(1);
        build.setConnectionTimeoutMilliSecond(5000);
        build.setSocketTimeoutMilliSecond(5000);
        MtopResponse syncRequest = build.syncRequest();
        String retMsg = syncRequest.getRetMsg();
        String str = "";
        Map<String, List<String>> headerFields = syncRequest.getHeaderFields();
        if (headerFields != null) {
            List list = headerFields.get(HttpHeaderConstant.EAGLE_TRACE_ID);
            if (list == null) {
                list = headerFields.get("EagleEye-TraceId");
            }
            if (list != null && list.size() > 0) {
                str = (String) list.get(0);
            }
        }
        if (syncRequest.isApiSuccess()) {
            rxResponse.data = syncRequest.getBytedata();
            rxResponse.isReqSuccess = true;
            rxResponse.retCode = syncRequest.getRetCode();
            rxResponse.msg = retMsg;
            NewMonitorLogger.MTOP.afterMtopSuccess(TAG, apiInfo.getAPIName(), apiInfo.getVersion(), jSONObject, syncRequest.getRetMsg(), str);
            return rxResponse;
        }
        if (!syncRequest.isNetworkError()) {
            NewMonitorLogger.MTOP.afterMtopFailed(TAG, apiInfo.getAPIName(), apiInfo.getVersion(), jSONObject, str, syncRequest.getRetMsg(), syncRequest.getRetCode(), String.valueOf(syncRequest.getResponseCode()));
        }
        if (syncRequest.isSessionInvalid()) {
            Login.logout();
        } else if (!syncRequest.isNetworkError() && !syncRequest.isSystemError()) {
            syncRequest.isExpiredRequest();
        }
        rxResponse.data = syncRequest.getBytedata();
        rxResponse.isReqSuccess = false;
        rxResponse.result = "";
        rxResponse.msg = retMsg;
        rxResponse.retCode = syncRequest.getRetCode();
        return rxResponse;
    }

    public RxResponse doSyncHttpRequest(RxHttpRequest rxHttpRequest) {
        if (!rxHttpRequest.isPost()) {
            return doHttpGetRequest(rxHttpRequest);
        }
        return null;
    }

    private RxResponse doHttpGetRequest(RxHttpRequest rxHttpRequest) {
        RxResponse rxResponse = new RxResponse();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(rxHttpRequest.getUrl()).openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            byte[] streamData = getStreamData(new BufferedInputStream(httpURLConnection.getInputStream()));
            rxResponse.isReqSuccess = true;
            rxResponse.data = streamData;
            return rxResponse;
        } catch (Exception | MalformedURLException unused) {
            rxResponse.isReqSuccess = false;
            return rxResponse;
        }
    }

    private byte[] getStreamData(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
            return new byte[0];
        }
    }
}
