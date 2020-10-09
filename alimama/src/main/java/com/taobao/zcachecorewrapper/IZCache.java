package com.taobao.zcachecorewrapper;

import com.taobao.zcachecorewrapper.model.AppConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.Error;
import com.taobao.zcachecorewrapper.model.ZConfigUpdateInfo;
import com.taobao.zcachecorewrapper.model.ZProxyRequest;
import java.util.HashMap;
import java.util.List;

public interface IZCache {
    void commitMonitor(String str, HashMap<String, String> hashMap, HashMap<String, Double> hashMap2);

    String initTempFolder();

    String initZCacheFolder();

    int networkStatus();

    void onFirstUpdateQueueFinished(int i);

    void requestAppConfig(AppConfigUpdateInfo appConfigUpdateInfo, long j);

    void requestConfig(String str, long j);

    void requestZConfig(ZConfigUpdateInfo zConfigUpdateInfo, long j);

    void requestZIP(String str, long j);

    void sendLog(int i, String str);

    void sendRequest(ZProxyRequest zProxyRequest, long j);

    void subscribePushMessageByGroup(List<String> list, String str, long j);

    Error unzip(String str, String str2);

    boolean verifySign(byte[] bArr, byte[] bArr2);
}
