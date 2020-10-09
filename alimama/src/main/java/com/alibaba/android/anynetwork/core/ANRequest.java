package com.alibaba.android.anynetwork.core;

import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.common.ANConstants;
import com.alibaba.android.anynetwork.core.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class ANRequest {
    public static final String BASE_TYPE = "base_type";
    public static final String NETWORK_ASYNC_CALLBACK = "network_async_callback";
    public static final String NETWORK_ASYNC_PROGRESS_CALLBACK = "network_async_progress_callback";
    public static final String NETWORK_CONNECT_TIMEOUT = "network_connect_timeout";
    public static final String NETWORK_DOWNLOAD_PATH = "network_download_path";
    public static final String NETWORK_HEADER = "network_base_header";
    public static final String NETWORK_HTTP_METHOD = "network_mtop_http_method";
    public static final String NETWORK_MTOP_API_NAME = "network_mtop_api_name";
    public static final String NETWORK_MTOP_API_VERSION = "network_mtop_api_version";
    public static final String NETWORK_MTOP_DATA_JSON_STRING = "network_mtop_data_json_string";
    public static final String NETWORK_MTOP_LOCATION = "network_mtop_location";
    public static final String NETWORK_MTOP_NEED_ECODE = "network_mtop_need_ecode";
    public static final String NETWORK_MTOP_NETWORK_QUALITY = "network_mtop_network_quality";
    public static final String NETWORK_MTOP_USE_HTTPS = "network_mtop_use_https";
    public static final String NETWORK_MTOP_USE_WUA = "network_mtop_use_wua";
    public static final String NETWORK_POST_BODY = "network_post_body";
    public static final String NETWORK_SOCKET_TIMEOUT = "network_socket_timeout";
    public static final String NETWORK_URL = "network_http_url";
    public static final String SERVICE_KEY = "service_key";
    private static HashMap<String, Class> mTypeCheckMap = new HashMap<>();
    private HashMap<String, Object> mPropertyMap = new HashMap<>();

    public interface Method {
        public static final int DEPRECATED_GET_OR_POST = -1;
        public static final int GET = 1;
        public static final int POST = 2;
    }

    static {
        boolean z = ANConstants.DEBUG;
    }

    public static void setTypeCheckEntry(String str, Class cls) {
        if (ANConstants.DEBUG && !TextUtils.isEmpty(str) && cls != null) {
            mTypeCheckMap.put(str, cls);
        }
    }

    public ANRequest setProperty(String str, Object obj) {
        Class cls;
        if (str == null) {
            throw new IllegalArgumentException("Property key can't be null");
        } else if (obj == null) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else if (!ANConstants.DEBUG || (cls = mTypeCheckMap.get(str)) == null || cls.isInstance(obj)) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else {
            throw new IllegalArgumentException("Excepted " + cls + " for " + str + " but is " + obj.getClass());
        }
    }

    public Object getProperty(String str) {
        return this.mPropertyMap.get(str);
    }

    public String toString() {
        return "ANRequest{" + this.mPropertyMap.toString() + "}";
    }

    public ANRequest setServiceKey(String str) {
        setProperty(SERVICE_KEY, str);
        return this;
    }

    public String getServiceKey() {
        return (String) getProperty(SERVICE_KEY);
    }

    public ANRequest setBaseType(String str) {
        setProperty("base_type", str);
        return this;
    }

    public String getBaseType() {
        return (String) getProperty("base_type");
    }

    public ANRequest setNetworkHeader(Map<String, String> map) {
        setProperty(NETWORK_HEADER, map);
        return this;
    }

    public Map<String, String> getNetworkHeader() {
        return (Map) getProperty(NETWORK_HEADER);
    }

    public ANRequest setNetworkUrl(String str) {
        setProperty(NETWORK_URL, str);
        return this;
    }

    public String getNetworkUrl() {
        return (String) getProperty(NETWORK_URL);
    }

    public ANRequest setNetworkPostBody(String str) {
        setProperty(NETWORK_POST_BODY, str);
        return this;
    }

    public String getNetworkPostBody() {
        return (String) getProperty(NETWORK_POST_BODY);
    }

    public ANRequest setNetworkConnectTimeout(int i) {
        setProperty(NETWORK_CONNECT_TIMEOUT, Integer.valueOf(i));
        return this;
    }

    public int getNetworkConnectTimeout() {
        return Utils.getInt(getProperty(NETWORK_CONNECT_TIMEOUT), -1);
    }

    public ANRequest setNetworkSocketTimeout(int i) {
        setProperty(NETWORK_SOCKET_TIMEOUT, Integer.valueOf(i));
        return this;
    }

    public int getNetworkSocketTimeout() {
        return Utils.getInt(getProperty(NETWORK_SOCKET_TIMEOUT), -1);
    }

    public ANRequest setNetworkAsyncCallback(IANAsyncCallback iANAsyncCallback) {
        setProperty(NETWORK_ASYNC_CALLBACK, iANAsyncCallback);
        return this;
    }

    public IANAsyncCallback getNetworkAsyncCallback() {
        return (IANAsyncCallback) getProperty(NETWORK_ASYNC_CALLBACK);
    }

    public ANRequest setNetworkAsyncProgressCallback(IANAsyncProgressCallback iANAsyncProgressCallback) {
        setProperty(NETWORK_ASYNC_PROGRESS_CALLBACK, iANAsyncProgressCallback);
        return this;
    }

    public IANAsyncProgressCallback getNetworkAsyncProgressCallback() {
        return (IANAsyncProgressCallback) getProperty(NETWORK_ASYNC_PROGRESS_CALLBACK);
    }

    public ANRequest setNetworkMtopApiName(String str) {
        setProperty(NETWORK_MTOP_API_NAME, str);
        return this;
    }

    public String getNetworkMtopApiName() {
        return (String) getProperty(NETWORK_MTOP_API_NAME);
    }

    public ANRequest setNetworkMtopApiVersion(String str) {
        setProperty(NETWORK_MTOP_API_VERSION, str);
        return this;
    }

    public String getNetworkMtopApiVersion() {
        return (String) getProperty(NETWORK_MTOP_API_VERSION);
    }

    public ANRequest setNetworkMtopDataJsonString(String str) {
        setProperty(NETWORK_MTOP_DATA_JSON_STRING, str);
        return this;
    }

    public String getNetworkMtopDataJsonString() {
        return (String) getProperty(NETWORK_MTOP_DATA_JSON_STRING);
    }

    public ANRequest setNetworkHttpMethod(int i) {
        setProperty(NETWORK_HTTP_METHOD, Integer.valueOf(i));
        return this;
    }

    public int getNetworkHttpMethod() {
        return Utils.getInt(getProperty(NETWORK_HTTP_METHOD), 1);
    }

    public ANRequest setNetworkMtopNeedEcode(boolean z) {
        setProperty(NETWORK_MTOP_NEED_ECODE, Boolean.valueOf(z));
        return this;
    }

    public boolean getNetworkMtopNeedEcode() {
        return Utils.getBoolean(getProperty(NETWORK_MTOP_NEED_ECODE), false);
    }

    public ANRequest setNetworkMtopUseHttps(boolean z) {
        setProperty(NETWORK_MTOP_USE_HTTPS, Boolean.valueOf(z));
        return this;
    }

    public boolean getNetworkMtopUseHttps() {
        return Utils.getBoolean(getProperty(NETWORK_MTOP_USE_HTTPS), false);
    }

    public ANRequest setNetworkMtopLocation(String str) {
        setProperty(NETWORK_MTOP_LOCATION, str);
        return this;
    }

    public String getNetworkMtopLocation() {
        return (String) getProperty(NETWORK_MTOP_LOCATION);
    }

    public ANRequest setNetworkMtopNetworkQuality(String str) {
        setProperty(NETWORK_MTOP_NETWORK_QUALITY, str);
        return this;
    }

    public String getNetworkMtopNetworkQuality() {
        return (String) getProperty(NETWORK_MTOP_NETWORK_QUALITY);
    }

    public ANRequest setNetworkDownloadPath(String str) {
        setProperty(NETWORK_DOWNLOAD_PATH, str);
        return this;
    }

    public String getNetworkDownloadPath() {
        return (String) getProperty(NETWORK_DOWNLOAD_PATH);
    }

    public ANRequest setNetworkMtopUseWua(boolean z) {
        setProperty(NETWORK_MTOP_USE_WUA, Boolean.valueOf(z));
        return this;
    }

    public boolean getNetworkMtopUseWua() {
        return Utils.getBoolean(getProperty(NETWORK_MTOP_USE_WUA), false);
    }
}
