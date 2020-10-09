package com.taobao.mtop.wvplugin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import anet.channel.request.ByteArrayEntry;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.SDKConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ANetBridge {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String ERR_BYTEARRAY_2_STRING = "ByteArray -> String Error";
    private static final String ERR_PARSE_PARAM = "parseParams error, param=";
    private static final String HTTP_STATUS = "status";
    private static final String HTTP_STATUS_CODE = "status_code";
    public static final String KEY_DATA = "data";
    public static final String KEY_HEADER = "header";
    public static final String KEY_METHOD = "method";
    public static final String KEY_URL = "url";
    public static final String METHOD_POST = "POST";
    public static final int MSG_ERR = -1;
    public static final int MSG_OK = 1;
    public static final String RESULT_CONTENT = "content";
    public static final String RESULT_KEY = "ret";
    private static final String TAG = "ANetBridge";
    private Context mContext = SDKConfig.getInstance().getGlobalContext();
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle data = message.getData();
            WVResult wVResult = new WVResult();
            wVResult.addData(ANetBridge.HTTP_STATUS_CODE, String.valueOf(data.getInt(ANetBridge.HTTP_STATUS_CODE, 0)));
            String string = data.getString("status");
            if (TextUtils.isEmpty(string)) {
                wVResult.addData("status", string);
            }
            if (message.what == -1) {
                wVResult.addData("ret", new JSONArray().put("HY_FAILED"));
                ANetBridge.this.mJsContext.error(wVResult);
            } else if (message.what == 1) {
                wVResult.addData("ret", new JSONArray().put(WVResult.SUCCESS));
                wVResult.addData("content", data.getString("content"));
                ANetBridge.this.mJsContext.success(wVResult);
            }
        }
    };
    /* access modifiers changed from: private */
    public WVCallBackContext mJsContext;

    private class NetworkListener implements NetworkCallBack.FinishListener, NetworkCallBack.ProgressListener {
        private ByteArrayOutputStream outStream;

        private NetworkListener() {
            this.outStream = new ByteArrayOutputStream();
        }

        public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
            Message obtainMessage = ANetBridge.this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            try {
                if (finishEvent.getHttpCode() > 0) {
                    obtainMessage.what = 1;
                    bundle.putString("content", new String(this.outStream.toByteArray(), "UTF-8"));
                } else {
                    obtainMessage.what = -1;
                }
            } catch (UnsupportedEncodingException unused) {
                TBSdkLog.e(ANetBridge.TAG, ANetBridge.ERR_BYTEARRAY_2_STRING);
            }
            bundle.putInt(ANetBridge.HTTP_STATUS_CODE, finishEvent.getHttpCode());
            bundle.putString("status", finishEvent.getDesc());
            obtainMessage.setData(bundle);
            ANetBridge.this.mHandler.sendMessage(obtainMessage);
        }

        public void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj) {
            this.outStream.write(progressEvent.getBytedata(), 0, progressEvent.getSize());
        }
    }

    public void sendRequest(WVCallBackContext wVCallBackContext, String str) {
        if (TBSdkLog.isPrintLog()) {
            TBSdkLog.d(TAG, "Send Params: " + str);
        }
        this.mJsContext = wVCallBackContext;
        RequestImpl parseParams = parseParams(str);
        if (parseParams == null) {
            this.mHandler.sendEmptyMessage(-1);
        }
        new DegradableNetwork(this.mContext).asyncSend(parseParams, (Object) null, (Handler) null, new NetworkListener());
    }

    private RequestImpl parseParams(String str) {
        byte[] bArr;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("url");
            String string2 = jSONObject.getString("method");
            RequestImpl requestImpl = new RequestImpl(string);
            requestImpl.setMethod(string2);
            JSONObject optJSONObject = jSONObject.optJSONObject("header");
            if (optJSONObject != null) {
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    String string3 = optJSONObject.getString(next);
                    if (!TextUtils.isEmpty(next)) {
                        if (!TextUtils.isEmpty(string3)) {
                            requestImpl.addHeader(next, string3);
                        }
                    }
                }
            }
            if ("POST".equals(string2)) {
                String string4 = jSONObject.getString("data");
                if (!StringUtils.isBlank(string4)) {
                    try {
                        bArr = string4.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        bArr = null;
                    }
                    if (bArr != null) {
                        requestImpl.setBodyEntry(new ByteArrayEntry(bArr));
                    }
                }
            }
            return requestImpl;
        } catch (JSONException unused) {
            TBSdkLog.e(TAG, ERR_PARSE_PARAM + str);
            return null;
        }
    }
}
