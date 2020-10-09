package com.alibaba.ut.abtest.pipeline;

import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.ut.abtest.UTABEnvironment;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.IOUtils;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.pipeline.encoder.ProtocolEncoder;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

public class PipelineServiceImpl implements PipelineService {
    private static final String TAG = "PipelineServiceImpl";
    private ProtocolEncoder protocolEncoder = new ProtocolEncoder();
    private SslSocketFactory sslSocketFactory;

    public Response executeRequest(Request request) {
        Response response;
        try {
            response = sendRequest(request);
            try {
                LogUtils.logD(TAG, "executeRequest complete, response=" + response);
                return response;
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            response = null;
            LogUtils.logE(TAG, "executeRequest failure." + th.getMessage(), th);
            if (response != null) {
                return response;
            }
            Response response2 = new Response();
            response2.setSuccess(false);
            response2.setCode(40000);
            response2.setMessage(th.getMessage());
            return response2;
        }
    }

    private Response sendRequest(Request request) throws Exception {
        InputStream inputStream;
        DataOutputStream dataOutputStream;
        long currentTimeMillis = System.currentTimeMillis();
        Response response = new Response();
        DataOutputStream dataOutputStream2 = null;
        try {
            URL url = new URL(getHost() + request.getUrl());
            LogUtils.logD(TAG, "sendRequest. request=" + request + ", requestUrl=" + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                if (this.sslSocketFactory == null && !TextUtils.isEmpty(url.getHost())) {
                    this.sslSocketFactory = new SslSocketFactory(url.getHost());
                }
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(this.sslSocketFactory);
            }
            httpURLConnection.setRequestProperty("Charset", ABConstants.BasicConstants.DEFAULT_CHARSET.name());
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            if (request.getHeaders() != null) {
                for (Map.Entry next : request.getHeaders().entrySet()) {
                    if (next.getValue() == null) {
                        httpURLConnection.setRequestProperty((String) next.getKey(), "");
                    } else {
                        httpURLConnection.setRequestProperty((String) next.getKey(), (String) next.getValue());
                    }
                }
            }
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            String encode = this.protocolEncoder.encode(httpURLConnection, request);
            if (encode == null || encode.length() <= 0) {
                dataOutputStream = null;
            } else {
                String str = "requestBody=" + encode;
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(str.length()));
                dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                try {
                    dataOutputStream.writeBytes(str);
                    dataOutputStream.flush();
                } catch (Throwable th) {
                    th = th;
                    inputStream = null;
                    dataOutputStream2 = dataOutputStream;
                    IOUtils.closeIO(dataOutputStream2);
                    IOUtils.closeIO(inputStream);
                    throw th;
                }
            }
            response.setHttpResponseCode(httpURLConnection.getResponseCode());
            if (response.getHttpResponseCode() != 200) {
                LogUtils.logE(TAG, "request returned http code " + response.getHttpResponseCode());
                IOUtils.closeIO(dataOutputStream);
                IOUtils.closeIO(null);
                return null;
            }
            inputStream = httpURLConnection.getInputStream();
            try {
                response.setByteData(IOUtils.toByteArray(inputStream));
                String str2 = new String(response.getByteData(), "UTF-8");
                if (LogUtils.isLogDebugEnable()) {
                    LogUtils.logD(TAG, "responseString=" + str2 + ", request=" + request);
                }
                JSONObject jSONObject = new JSONObject(str2);
                if (TextUtils.equals("sm", jSONObject.optString("rgv587_flag"))) {
                    response.setSuccess(false);
                    response.setCode(StatusCode.INTERFACE_LIMIT);
                    response.setMessage("rgv587_flag");
                } else {
                    response.setSuccess(jSONObject.optBoolean("success"));
                    response.setCode(jSONObject.optInt("code"));
                    response.setMessage(jSONObject.optString(ApiConstants.ApiField.INFO));
                    response.setDataJsonObject(jSONObject.optJSONObject("data"));
                    if (response.getDataJsonObject() != null) {
                        if (request.getResponseClass() != null) {
                            response.setData(JsonUtil.fromJson(response.getDataJsonObject().toString(), request.getResponseClass()));
                        } else if (request.getResponseType() != null) {
                            response.setData(JsonUtil.fromJson(response.getDataJsonObject().toString(), request.getResponseType()));
                        }
                    }
                }
                IOUtils.closeIO(dataOutputStream);
                IOUtils.closeIO(inputStream);
                if (LogUtils.isLogDebugEnable()) {
                    LogUtils.logD(TAG, "The request ended and it took " + (System.currentTimeMillis() - currentTimeMillis) + " milliseconds. request=" + request);
                } else {
                    LogUtils.logD(TAG, "The request ended and it took " + (System.currentTimeMillis() - currentTimeMillis) + " milliseconds.");
                }
                return response;
            } catch (Throwable th2) {
                th = th2;
                dataOutputStream2 = dataOutputStream;
                IOUtils.closeIO(dataOutputStream2);
                IOUtils.closeIO(inputStream);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStream = null;
            IOUtils.closeIO(dataOutputStream2);
            IOUtils.closeIO(inputStream);
            throw th;
        }
    }

    private String getHost() {
        UTABEnvironment environment = ABContext.getInstance().getEnvironment();
        if (environment == null || environment == UTABEnvironment.Product) {
            return ABConstants.Pipeline.HOST_PRODUCT;
        }
        if (environment == UTABEnvironment.Prepare) {
            return ABConstants.Pipeline.HOST_PREPARE;
        }
        return environment == UTABEnvironment.Daily ? ABConstants.Pipeline.HOST_DAILY : ABConstants.Pipeline.HOST_PRODUCT;
    }
}
