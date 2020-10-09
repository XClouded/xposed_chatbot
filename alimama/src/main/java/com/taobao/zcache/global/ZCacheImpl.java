package com.taobao.zcache.global;

import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.config.ConfigUtil;
import com.taobao.zcache.config.IZConfigRequest;
import com.taobao.zcache.config.ZCacheAdapterManager;
import com.taobao.zcache.intelligent.ZIntelligentManger;
import com.taobao.zcache.log.ZLog;
import com.taobao.zcache.monitor.ZMonitorManager;
import com.taobao.zcache.network.ConnectManager;
import com.taobao.zcache.network.HttpConnectListener;
import com.taobao.zcache.network.HttpRequest;
import com.taobao.zcache.network.HttpResponse;
import com.taobao.zcache.slide.ZCacheSlideManager;
import com.taobao.zcache.util.FileUtils;
import com.taobao.zcache.util.NetWorkUtils;
import com.taobao.zcache.util.RsaUtil;
import com.taobao.zcache.zipdownload.DownLoadListener;
import com.taobao.zcache.zipdownload.InstanceZipDownloader;
import com.taobao.zcachecorewrapper.IZCache;
import com.taobao.zcachecorewrapper.IZCacheCore;
import com.taobao.zcachecorewrapper.model.AppConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.Error;
import com.taobao.zcachecorewrapper.model.ZConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.ZProxyRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ZCacheImpl implements IZCache {
    public static final String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr4QTVnTHJ/W1hfBkEfTdWMMAxsQHW22gK0JProk3hmdwwal+Up7Ty/8NUXs+8SKufik2ASXQLFkqeoZu60sXmtlQGZJ+kAezC8pS9MboHZWywO9VJwxRUQuXI/Hn0jjZsA8tZPpN6Ty9wkz80GrQJrRuhjEjT0JAjElhpZUxTXMKIIPqM+ndgcfF55f9wWYFKW+o/Z0Nil0yP1crvLryq3sbSbDTnz7+j4zUE7aCGb0ECyS/ii1o53C08YKyhzpSTICSzILvHMdHFHGeuH1LfrinuLYdyORlC0f6qoSODBSaXO7UI+uHxhb6K3e1YzUYsMRuEjyDUTETeT/b07LIgwIDAQAB";
    InstanceZipDownloader downloader;
    /* access modifiers changed from: private */
    public IZCacheCore izCacheCore;

    public ZCacheImpl(IZCacheCore iZCacheCore) {
        this.izCacheCore = iZCacheCore;
    }

    public String initZCacheFolder() {
        return ZCacheGlobal.instance().context() != null ? ZCacheGlobal.instance().context().getDir("zcache", 0).getAbsolutePath() : "";
    }

    public String initTempFolder() {
        if (ZCacheGlobal.instance().context() == null) {
            return "";
        }
        File dir = ZCacheGlobal.instance().context().getDir("zcache", 0);
        File file = new File(dir.getAbsolutePath() + File.separator + "cache" + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public void requestAppConfig(AppConfigUpdateInfo appConfigUpdateInfo, long j) {
        if (TextUtils.isEmpty(appConfigUpdateInfo.url)) {
            IZCacheCore iZCacheCore = this.izCacheCore;
            iZCacheCore.onRequestAppConfigCallback(j, "{}", 1001, appConfigUpdateInfo.url + " invalid");
            ZLog.e("request app config=[" + appConfigUpdateInfo.url + "], code=[" + 1001 + "]; msg=[" + appConfigUpdateInfo.url + " invalid");
            return;
        }
        String str = appConfigUpdateInfo.url;
        if (ZCacheAdapterManager.getInstance().getRequest() != null) {
            str = getAppConfigUrl(appConfigUpdateInfo.appName);
        }
        final AppConfigUpdateInfo appConfigUpdateInfo2 = appConfigUpdateInfo;
        final long j2 = j;
        final String str2 = str;
        ConnectManager.getInstance().connect(str, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                String str;
                int i2;
                String str2;
                if (httpResponse == null || httpResponse.getData() == null || httpResponse.getData().length == 0) {
                    String str3 = appConfigUpdateInfo2.url + " no data";
                    ZCacheImpl.this.izCacheCore.onRequestAppConfigCallback(j2, "{}", 1006, str3);
                    ZLog.e("request app config=[" + str2 + "], code=[" + 1006 + "]; msg=[" + str3 + Operators.ARRAY_END_STR);
                    return;
                }
                try {
                    str = "SUCCESS";
                    str2 = new String(httpResponse.getData(), "utf-8");
                    i2 = 0;
                } catch (UnsupportedEncodingException e) {
                    String str4 = "{}";
                    e.printStackTrace();
                    str = appConfigUpdateInfo2.url + " deserialization failed:" + e.getMessage();
                    str2 = str4;
                    i2 = 1007;
                }
                ZLog.i("request app config:[" + appConfigUpdateInfo2.url + "], code:[" + i2 + "]; msg:[" + str + Operators.ARRAY_END_STR);
                ZCacheImpl.this.izCacheCore.onRequestAppConfigCallback(j2, str2, i2, str);
            }

            public void onError(int i, String str) {
                ZLog.e("request app config:[" + appConfigUpdateInfo2.url + "], code:[" + i + "]; msg:[" + str + Operators.ARRAY_END_STR);
                IZCacheCore access$000 = ZCacheImpl.this.izCacheCore;
                long j = j2;
                StringBuilder sb = new StringBuilder();
                sb.append(appConfigUpdateInfo2.url);
                sb.append(" download failed: ");
                sb.append(str);
                access$000.onRequestAppConfigCallback(j, "{}", 1003, sb.toString());
            }
        });
    }

    public void commitMonitor(String str, HashMap<String, String> hashMap, HashMap<String, Double> hashMap2) {
        if (TextUtils.isEmpty(str) || !str.contains(".")) {
            ZLog.e("module=[" + str + Operators.ARRAY_END_STR);
            return;
        }
        String[] split = str.split("\\.");
        String str2 = split[0];
        String str3 = split[1];
        if (ZMonitorManager.getInstance().getMonitorProxy() != null) {
            ZMonitorManager.getInstance().getMonitorProxy().commitStat(str2, str3, hashMap, hashMap2);
        }
        if (TextUtils.equals("AppUpdate", str3) && ZIntelligentManger.getInstance().getIntelligentImpl() != null) {
            ZIntelligentManger.getInstance().getIntelligentImpl().commitUpdate(hashMap, hashMap2);
        }
    }

    public void subscribePushMessageByGroup(List<String> list, String str, long j) {
        if (ZCacheSlideManager.getInstance().getSlideSubscribe() != null) {
            ZLog.i("ZCache 3.0 slide 分组注册");
            ZCacheSlideManager.getInstance().getSlideSubscribe().subscribeSlideByGroup(list, str, j);
        }
    }

    public void onFirstUpdateQueueFinished(int i) {
        if (ZCacheAdapterManager.getInstance().getUpdateImpl() != null) {
            ZCacheAdapterManager.getInstance().getUpdateImpl().firstUpdateCount(i);
        }
    }

    public void requestZConfig(final ZConfigUpdateInfo zConfigUpdateInfo, final long j) {
        if (ZCacheAdapterManager.getInstance().getRequest() != null) {
            ZCacheAdapterManager.getInstance().getRequest().requestZConfig(new IZConfigRequest.ZConfigCallback() {
                public void configBack(String str, int i, String str2) {
                    ZLog.i("zcache 3.0 config by resembled, content=[" + str + Operators.ARRAY_END_STR);
                    ZCacheImpl.this.izCacheCore.onRequestZConfigCallback(j, ConfigUtil.resembleConfig(str), i, str2);
                }
            });
        } else if (TextUtils.isEmpty(zConfigUpdateInfo.url)) {
            ZLog.i("zcache 3.0 config by url=[" + zConfigUpdateInfo.url + "], content=[]");
            IZCacheCore iZCacheCore = this.izCacheCore;
            iZCacheCore.onRequestZConfigCallback(j, "{}", 1001, zConfigUpdateInfo.url + " invalid");
        } else {
            ConnectManager.getInstance().connect(zConfigUpdateInfo.url, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse httpResponse, int i) {
                    String str;
                    int i2;
                    String str2;
                    if (httpResponse == null || httpResponse.getData() == null || httpResponse.getData().length == 0) {
                        String str3 = zConfigUpdateInfo.url + " no data";
                        ZCacheImpl.this.izCacheCore.onRequestZConfigCallback(j, "{}", 1006, str3);
                        ZLog.e("zcache 3.0 config by url=[" + zConfigUpdateInfo.url + "], content=[" + "{}" + "], code=[" + 1006 + "]; msg=[" + str3 + Operators.ARRAY_END_STR);
                        return;
                    }
                    try {
                        str = "SUCCESS";
                        str2 = new String(httpResponse.getData(), "utf-8");
                        i2 = 0;
                    } catch (UnsupportedEncodingException e) {
                        String str4 = "{}";
                        e.printStackTrace();
                        str = zConfigUpdateInfo.url + " deserialization failed:" + e.getMessage();
                        str2 = str4;
                        i2 = 1007;
                    }
                    ZLog.i("zcache 3.0 config by url=[" + zConfigUpdateInfo.url + "], content=[" + str2 + "], code=[" + i2 + "]; msg=[" + str + Operators.ARRAY_END_STR);
                    ZCacheImpl.this.izCacheCore.onRequestZConfigCallback(j, str2, i2, str);
                }

                public void onError(int i, String str) {
                    super.onError(i, str);
                    ZLog.e("zcache 3.0 config by url=[" + zConfigUpdateInfo.url + "], content=[], code=[" + i + "]; msg=[" + str + Operators.ARRAY_END_STR);
                    IZCacheCore access$000 = ZCacheImpl.this.izCacheCore;
                    long j = j;
                    StringBuilder sb = new StringBuilder();
                    sb.append(zConfigUpdateInfo.url);
                    sb.append(" download failed: ");
                    sb.append(str);
                    access$000.onRequestZConfigCallback(j, "{}", 1003, sb.toString());
                }
            });
        }
    }

    public void requestZIP(String str, final long j) {
        new InstanceZipDownloader(str, new DownLoadListener() {
            public void callback(String str, String str2, Map<String, String> map, int i, int i2, String str3) {
                ZLog.i("request zip callback, url=[" + str + "];" + " file=[" + str2 + Operators.ARRAY_END_STR);
                ZCacheImpl.this.izCacheCore.onRequestZIPCallback(j, str2, i, i2, str3);
            }
        }).start();
    }

    public void requestConfig(final String str, final long j) {
        if (TextUtils.isEmpty(str)) {
            ZLog.i("zcache 3.0 config by url=[" + str + "], content=[]");
            IZCacheCore iZCacheCore = this.izCacheCore;
            iZCacheCore.onRequestZConfigCallback(j, "{}", 1001, str + " invalid");
            return;
        }
        ConnectManager.getInstance().connect(str, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                String str;
                int i2;
                String str2;
                if (httpResponse == null || httpResponse.getData() == null || httpResponse.getData().length == 0) {
                    String str3 = str + " no data";
                    ZCacheImpl.this.izCacheCore.onRequestConfigCallback(j, "{}", -1, 1006, str3);
                    ZLog.e("zcache 3.0 config by url=[" + str + "], content=[" + "{}" + "], code=[" + 1006 + "]; msg=[" + str3 + Operators.ARRAY_END_STR);
                    return;
                }
                try {
                    str = "SUCCESS";
                    str2 = new String(httpResponse.getData(), "utf-8");
                    i2 = 0;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    str2 = "{}";
                    str = str + " deserialization failed:" + e.getMessage();
                    i2 = 1007;
                }
                ZLog.i("zcache 3.0 config by url=[" + str + "], content=[" + str2 + "], code=[" + i2 + "]; msg=[" + str + Operators.ARRAY_END_STR);
                ZCacheImpl.this.izCacheCore.onRequestConfigCallback(j, str2, httpResponse.getHttpCode(), i2, str);
            }

            public void onError(int i, String str) {
                super.onError(i, str);
                ZLog.e("zcache 3.0 config by url=[" + str + "], content=[], code=[" + i + "]; msg=[" + str + Operators.ARRAY_END_STR);
                IZCacheCore access$000 = ZCacheImpl.this.izCacheCore;
                long j = j;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" download failed: ");
                sb.append(str);
                access$000.onRequestConfigCallback(j, "{}", i, 1003, sb.toString());
            }
        });
    }

    public void sendRequest(ZProxyRequest zProxyRequest, final long j) {
        final String str = zProxyRequest.url;
        int i = zProxyRequest.timeout;
        HashMap<String, String> hashMap = zProxyRequest.headers;
        if (!TextUtils.isEmpty(zProxyRequest.tempFilePath)) {
            InstanceZipDownloader instanceZipDownloader = new InstanceZipDownloader(str, new DownLoadListener() {
                public void callback(String str, String str2, Map<String, String> map, int i, int i2, String str3) {
                    ZLog.i("[NEW] request zip callback, url=[" + str + "];" + " file=[" + str2 + Operators.ARRAY_END_STR);
                    ZCacheImpl.this.izCacheCore.onSendRequestCallback(j, "", i, i2, str3);
                }
            });
            instanceZipDownloader.setHeader(hashMap);
            instanceZipDownloader.setTimeout(i * 1000);
            instanceZipDownloader.setUrl(str);
            instanceZipDownloader.setDestFile(zProxyRequest.tempFilePath);
            instanceZipDownloader.start();
        } else if (TextUtils.isEmpty(str)) {
            ZLog.i("[NEW] zcache 3.0 config by url=[" + str + "], content=[]");
            IZCacheCore iZCacheCore = this.izCacheCore;
            iZCacheCore.onSendRequestCallback(j, "{}", -1, 1001, "invalid url = [" + str + Operators.ARRAY_END_STR);
        } else {
            HttpRequest httpRequest = new HttpRequest(str);
            httpRequest.setConnectTimeout(i * 1000);
            httpRequest.setHeaders(hashMap);
            ConnectManager.getInstance().connect(httpRequest, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse httpResponse, int i) {
                    String str;
                    int i2;
                    int i3;
                    String str2;
                    String str3 = "";
                    if (httpResponse == null || httpResponse.getData() == null) {
                        str2 = str3;
                        str = "";
                        i3 = 0;
                    } else {
                        try {
                            String str4 = new String(httpResponse.getData(), "utf-8");
                            try {
                                i3 = httpResponse.getHttpCode();
                                str = "SUCCESS";
                                str2 = str4;
                            } catch (UnsupportedEncodingException e) {
                                e = e;
                                str3 = str4;
                                e.printStackTrace();
                                str = str + " deserialization failed:" + e.getMessage();
                                str2 = str3;
                                i3 = 0;
                                i2 = 1007;
                                ZLog.i("[NEW] zcache 3.0 config by url=[" + str + "], content=[" + str2 + "], code=[" + i2 + "]; msg=[" + str + Operators.ARRAY_END_STR);
                                ZCacheImpl.this.izCacheCore.onSendRequestCallback(j, str2, i3, i2, str);
                            }
                        } catch (UnsupportedEncodingException e2) {
                            e = e2;
                            e.printStackTrace();
                            str = str + " deserialization failed:" + e.getMessage();
                            str2 = str3;
                            i3 = 0;
                            i2 = 1007;
                            ZLog.i("[NEW] zcache 3.0 config by url=[" + str + "], content=[" + str2 + "], code=[" + i2 + "]; msg=[" + str + Operators.ARRAY_END_STR);
                            ZCacheImpl.this.izCacheCore.onSendRequestCallback(j, str2, i3, i2, str);
                        }
                    }
                    i2 = 0;
                    ZLog.i("[NEW] zcache 3.0 config by url=[" + str + "], content=[" + str2 + "], code=[" + i2 + "]; msg=[" + str + Operators.ARRAY_END_STR);
                    ZCacheImpl.this.izCacheCore.onSendRequestCallback(j, str2, i3, i2, str);
                }

                public void onError(int i, String str) {
                    super.onError(i, str);
                    ZLog.e("[NEW] zcache 3.0 config by url=[" + str + "], content=[], code=[" + i + "]; msg=[" + str + Operators.ARRAY_END_STR);
                    ZCacheImpl.this.izCacheCore.onSendRequestCallback(j, "", 0, i, str);
                }
            });
        }
    }

    public int networkStatus() {
        return NetWorkUtils.getNetworkType();
    }

    public Error unzip(String str, String str2) {
        String str3;
        IOException e;
        InputStream inputStream;
        Error error = new Error();
        if (str == null || str2 == null) {
            error.errCode = 2206;
            error.errMsg = str + " read failed: [zipFilePath]=null or [targetFolder]=null";
            ZLog.e("unzip error, errCode=[" + error.errCode + "]; errMsg=[" + error.errMsg + Operators.ARRAY_END_STR);
            return error;
        }
        try {
            if (str.startsWith("/assets")) {
                str3 = str.substring(str.lastIndexOf("/") + 1);
                try {
                    inputStream = getPreloadInputStream(str3);
                } catch (IOException e2) {
                    e = e2;
                    error.errCode = 2206;
                    error.errMsg = str3 + " read failed: " + e.getMessage();
                    ZLog.i("unzip [" + str3 + "] to [" + str2 + "], errCode:[" + error.errCode + "]; errMsg:[" + error.errMsg + Operators.ARRAY_END_STR);
                    return error;
                }
            } else {
                str3 = str;
                inputStream = new FileInputStream(str);
            }
            if (inputStream != null) {
                if (inputStream.available() != 0) {
                    String unzip = FileUtils.unzip(inputStream, str2);
                    if ("SUCCESS".equals(unzip)) {
                        error.errCode = 0;
                        error.errMsg = "unzip success";
                    } else {
                        error.errCode = 2207;
                        error.errMsg = str3 + " unzip failed: " + unzip;
                    }
                    ZLog.i("unzip [" + str3 + "] to [" + str2 + "], errCode:[" + error.errCode + "]; errMsg:[" + error.errMsg + Operators.ARRAY_END_STR);
                    return error;
                }
            }
            error.errCode = 2205;
            error.errMsg = str3 + " no data";
        } catch (IOException e3) {
            IOException iOException = e3;
            str3 = str;
            e = iOException;
            error.errCode = 2206;
            error.errMsg = str3 + " read failed: " + e.getMessage();
            ZLog.i("unzip [" + str3 + "] to [" + str2 + "], errCode:[" + error.errCode + "]; errMsg:[" + error.errMsg + Operators.ARRAY_END_STR);
            return error;
        }
        ZLog.i("unzip [" + str3 + "] to [" + str2 + "], errCode:[" + error.errCode + "]; errMsg:[" + error.errMsg + Operators.ARRAY_END_STR);
        return error;
    }

    private InputStream getPreloadInputStream(String str) {
        try {
            return ZCacheGlobal.instance().context().getResources().getAssets().open(str);
        } catch (Throwable unused) {
            ZLog.e("preload package not exists");
            return null;
        }
    }

    public boolean verifySign(byte[] bArr, byte[] bArr2) {
        try {
            if (!validConfigFile(new String(bArr, "utf-8"), bArr2)) {
                return false;
            }
            ZLog.i("verify success");
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ZLog.e("verify failed, " + e.getMessage());
            return false;
        }
    }

    private boolean validConfigFile(String str, byte[] bArr) {
        try {
            String str2 = new String(RsaUtil.decryptData(bArr, (Key) RsaUtil.getPublicKey(key)));
            boolean equals = str2.equals(str);
            if (!equals) {
                ZLog.e("verify failed, realSign=[" + str + "], requireSign=[" + str2 + Operators.ARRAY_END_STR);
            }
            return equals;
        } catch (Exception e) {
            ZLog.e("decrypt fail: " + e.getMessage());
            return false;
        }
    }

    public void sendLog(int i, String str) {
        switch (i) {
            case 1:
                ZLog.e(str);
                return;
            case 2:
                ZLog.w(str);
                return;
            case 3:
                ZLog.i(str);
                return;
            case 4:
                ZLog.d(str);
                return;
            case 5:
                ZLog.v(str);
                return;
            default:
                return;
        }
    }

    private String getAppConfigUrl(String str) {
        StringBuilder sb = new StringBuilder();
        switch (ZCacheGlobal.instance().env()) {
            case 0:
                sb.append("https://wvcfg.alicdn.com");
                break;
            case 1:
                sb.append("http://h5.wapa.taobao.com");
                break;
            case 2:
                sb.append("https://h5.waptest.taobao.com");
                break;
            default:
                sb.append("https://wvcfg.alicdn.com");
                break;
        }
        sb.append("/app/");
        sb.append(str);
        sb.append("/config/app.json");
        return sb.toString();
    }
}
