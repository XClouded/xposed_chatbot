package com.alibaba.motu.tbrest.rest;

import android.content.Context;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.data.RestOrangeConfigure;
import com.alibaba.motu.tbrest.request.BizRequest;
import com.alibaba.motu.tbrest.request.UrlWrapper;
import com.alibaba.motu.tbrest.utils.LogUtil;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RestReqSend {
    private static final RestBlockHandler REST_BLOCK_HANDLER = new RestBlockHandler();

    public static boolean sendLog(Context context, String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return sendLog(SendService.getInstance().appKey, context, str, j, str2, i, obj, obj2, obj3, map);
    }

    public static boolean sendLog(String str, Context context, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return innerSendLog(str, context, str2, j, str3, i, obj, obj2, obj3, map, false);
    }

    public static boolean sendLogByUrl(String str, Context context, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return innerSendLog(str, context, str2, j, str3, i, obj, obj2, obj3, map, true);
    }

    @Deprecated
    public static String sendLogByUrl(String str, Context context, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return sendLogByUrl(str, SendService.getInstance().appKey, context, j, str2, i, obj, obj2, obj3, map);
    }

    @Deprecated
    public static String sendLogByUrl(String str, String str2, Context context, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        try {
            LogUtil.i("sendLogByUrl RestAPI start send log!");
            RestReqDataBuildResult buildMonkeyPostReqDataObj = RestReqDataBuilder.buildMonkeyPostReqDataObj(str2, str, context, j, str3, i, obj, obj2, obj3, map);
            if (buildMonkeyPostReqDataObj != null) {
                LogUtil.i("sendLogByUrl RestAPI build data succ!");
                Map<String, Object> postReqData = buildMonkeyPostReqDataObj.getPostReqData();
                if (postReqData == null) {
                    LogUtil.i("sendLogByUrl postReqData is null!");
                    return null;
                }
                String reqUrl = buildMonkeyPostReqDataObj.getReqUrl();
                if (StringUtils.isEmpty(reqUrl)) {
                    LogUtil.i("sendLogByUrl reqUrl is null!");
                    return null;
                }
                byte[] sendRequest = RestHttpUtils.sendRequest(2, reqUrl, postReqData, true);
                if (sendRequest != null) {
                    String str4 = new String(sendRequest, "UTF-8");
                    if (!StringUtils.isEmpty(str4)) {
                        return str4;
                    }
                }
            } else {
                LogUtil.i("sendLogByUrl UTRestAPI build data failure!");
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("sendLogByUrl result encoding UTF-8 error!", e);
        } catch (Throwable th) {
            LogUtil.e("sendLogByUrl system error!", th);
        }
        return null;
    }

    private static boolean innerSendLog(String str, Context context, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map, boolean z) {
        int i2 = i;
        try {
            LogUtil.i("RestAPI start send log by url!");
            String buildRequestData = RestReqDataBuilder.buildRequestData(str, j, str3, i, obj, obj2, obj3, map);
            if (StringUtils.isNotBlank(buildRequestData)) {
                LogUtil.i("RestAPI build data succ by url!");
                if (!(i2 == 61006 || i2 == 1)) {
                    if (!RestOrangeConfigure.instance().isUseOldLogic()) {
                        return REST_BLOCK_HANDLER.sendAsyncInfo(i, str, context, str2, buildRequestData);
                    }
                }
                String str4 = str;
                Context context2 = context;
                String str5 = str2;
                try {
                    return sendBlockInfo(i2, str, context, str2, buildRequestData);
                } catch (Exception unused) {
                    return false;
                }
            } else {
                LogUtil.i("UTRestAPI build data failure by url!");
                return false;
            }
        } catch (Throwable th) {
            LogUtil.e("system error by url!", th);
        }
    }

    private static boolean sendBlockInfo(int i, String str, Context context, String str2, String str3) {
        try {
            LogUtil.i("RestAPI start send log!");
            if (StringUtils.isNotBlank(str3)) {
                LogUtil.i("RestAPI build data succ!");
                HashMap hashMap = new HashMap(1);
                hashMap.put(String.valueOf(i), str3);
                byte[] bArr = null;
                bArr = packageRequest(str, context, hashMap);
                if (bArr == null) {
                    return false;
                }
                LogUtil.i("packRequest success!");
                return UrlWrapper.sendRequest(str2, bArr).isSuccess();
            }
            LogUtil.i("UTRestAPI build data failure!");
            return false;
        } catch (Exception e) {
            LogUtil.e(e.toString());
        } catch (Throwable th) {
            LogUtil.e("system error!", th);
            return false;
        }
    }

    private static byte[] packageRequest(String str, Context context, Map<String, String> map) {
        try {
            return BizRequest.getPackRequest(str, context, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendAllCacheData() {
        REST_BLOCK_HANDLER.sendAllCacheData();
    }
}
