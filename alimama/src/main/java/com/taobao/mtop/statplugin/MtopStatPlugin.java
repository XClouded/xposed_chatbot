package com.taobao.mtop.statplugin;

import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unwrouter.UNWRouter;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WindVaneInterface;
import anetwork.channel.stat.NetworkStat;
import com.taobao.mtop.UTAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import org.json.JSONException;
import org.json.JSONObject;

public class MtopStatPlugin extends WVApiPlugin {
    public static final String API_SERVER_NAME = "MtopStatPlugin";
    private static final String TAG = "mtopsdk.MtopStatPlugin";

    public static void register() {
        WVPluginManager.registerPlugin(API_SERVER_NAME, (Class<? extends WVApiPlugin>) MtopStatPlugin.class);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "register MtopStatPlugin succeed!");
        }
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("get".equals(str)) {
            getStat(wVCallBackContext, str2);
            return true;
        } else if (!"commitUT".equals(str)) {
            return false;
        } else {
            commitUT(wVCallBackContext, str2);
            return true;
        }
    }

    @WindVaneInterface
    public void getStat(WVCallBackContext wVCallBackContext, String str) {
        try {
            String string = new JSONObject(str).getString("url");
            String str2 = NetworkStat.getNetworkStat().get(string);
            if (TBSdkLog.isPrintLog()) {
                TBSdkLog.d(TAG, "[getStat] url=" + string + " stat=" + str2);
            }
            if (StringUtils.isNotBlank(str)) {
                wVCallBackContext.success(str2);
            } else {
                wVCallBackContext.error(str2);
            }
        } catch (JSONException e) {
            TBSdkLog.e(TAG, "JSON解析失败", (Throwable) e);
            wVCallBackContext.error("JSON解析失败");
        } catch (Exception e2) {
            TBSdkLog.e(TAG, "发生异常", (Throwable) e2);
            wVCallBackContext.error("发生异常");
        }
    }

    @WindVaneInterface
    public void commitUT(WVCallBackContext wVCallBackContext, String str) {
        try {
            if (TBSdkLog.isPrintLog()) {
                TBSdkLog.d(TAG, "[commitUT] params=" + str);
            }
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString(UNWRouter.PAGE_NAME);
            int i = jSONObject.getInt(EventConstants.EVENTID);
            String optString = jSONObject.optString(EventConstants.UT.ARG1);
            String optString2 = jSONObject.optString("arg2");
            String optString3 = jSONObject.optString("arg3");
            JSONObject optJSONObject = jSONObject.optJSONObject("args");
            HashMap hashMap = null;
            if (optJSONObject != null) {
                hashMap = new HashMap();
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    String obj = keys.next().toString();
                    hashMap.put(obj, optJSONObject.optString(obj));
                }
            }
            UTAdapter.commit(string, i, optString, optString2, optString3, (Map<String, String>) hashMap);
            wVCallBackContext.success();
        } catch (JSONException e) {
            TBSdkLog.e(TAG, "JSON解析失败", (Throwable) e);
            wVCallBackContext.error("JSON解析失败");
        } catch (Exception e2) {
            TBSdkLog.e(TAG, "发生异常", (Throwable) e2);
            wVCallBackContext.error("发生异常");
        }
    }
}
