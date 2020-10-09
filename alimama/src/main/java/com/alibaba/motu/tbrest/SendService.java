package com.alibaba.motu.tbrest;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.alibaba.motu.tbrest.rest.RestConstants;
import com.alibaba.motu.tbrest.rest.RestReqSend;
import com.alibaba.motu.tbrest.utils.LogUtil;
import java.util.Map;

public class SendService {
    static final SendService instance = new SendService();
    public String appId = null;
    public String appKey = null;
    public String appSecret = null;
    public String appVersion = null;
    public String buildId = null;
    public String channel = null;
    public Context context = null;
    public String country = null;
    public String host = null;
    public Boolean openHttp = false;
    private SendAsyncExecutor sendAsyncExecutor = new SendAsyncExecutor();
    public String userNick = null;

    public static SendService getInstance() {
        return instance;
    }

    public void init(Context context2, String str, String str2, String str3, String str4, String str5) {
        String str6;
        this.context = context2;
        this.appId = str;
        this.appKey = str2;
        this.appVersion = str3;
        this.channel = str4;
        this.userNick = str5;
        Resources resources = context2.getResources();
        int identifier = resources.getIdentifier("build_id", "string", context2.getPackageName());
        if (identifier > 0) {
            try {
                str6 = resources.getString(identifier);
            } catch (Exception e) {
                Log.e("SendService", e.getMessage());
            }
            this.buildId = str6;
        }
        str6 = "unknown";
        this.buildId = str6;
    }

    public void updateAppVersion(String str) {
        if (str != null) {
            this.appVersion = str;
        }
    }

    public void updateUserNick(String str) {
        if (str != null) {
            this.userNick = str;
        }
    }

    public void updateChannel(String str) {
        if (str != null) {
            this.channel = str;
        }
    }

    public void changeHost(String str) {
        if (str != null) {
            this.host = str;
        }
    }

    public String getChangeHost() {
        return this.host;
    }

    public Boolean sendRequest(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str3;
        if (!canSend().booleanValue()) {
            return false;
        }
        if (str == null) {
            str3 = this.host != null ? this.host : RestConstants.G_DEFAULT_ADASHX_HOST;
        } else {
            str3 = str;
        }
        return Boolean.valueOf(RestReqSend.sendLog(this.appKey, this.context, str3, j, str2, i, obj, obj2, obj3, map));
    }

    public void sendRequestAsyn(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        String str3;
        if (canSend().booleanValue()) {
            if (str == null) {
                str3 = this.host != null ? this.host : RestConstants.G_DEFAULT_ADASHX_HOST;
            } else {
                str3 = str;
            }
            RestThread restThread = r0;
            RestThread restThread2 = new RestThread("rest thread", this.appKey, this.context, str3, j, str2, i, obj, obj2, obj3, map, (Boolean) null);
            this.sendAsyncExecutor.start(restThread);
        }
    }

    public void sendRequestAsynByAppkeyAndUrl(String str, String str2, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        if (!canSend().booleanValue()) {
            return;
        }
        if (str == null) {
            Log.e(LogUtil.TAG, "need set url");
            return;
        }
        RestThread restThread = r0;
        RestThread restThread2 = new RestThread("rest thread", str2 == null ? this.appKey : str2, this.context, str, j, str3, i, obj, obj2, obj3, map, 1);
        this.sendAsyncExecutor.start(restThread);
    }

    @Deprecated
    public String sendRequestByUrl(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        if (!canSend().booleanValue()) {
            return null;
        }
        return RestReqSend.sendLogByUrl(str, this.appKey, this.context, j, str2, i, obj, obj2, obj3, map);
    }

    private Boolean canSend() {
        if (this.appId != null && this.appVersion != null && this.appKey != null && this.context != null) {
            return true;
        }
        LogUtil.e("have send args is null，you must init first. appId " + this.appId + " appVersion " + this.appVersion + " appKey " + this.appKey);
        return false;
    }

    public class RestThread implements Runnable {
        private Object aArg1;
        private Object aArg2;
        private Object aArg3;
        private int aEventId;
        private Map<String, String> aExtData;
        private String aPage;
        private long aTimestamp;
        private String adashxServerHost;
        private String appKey;
        private Context context;
        private Boolean isUrl = false;

        public RestThread() {
        }

        public RestThread(String str, String str2, Context context2, String str3, long j, String str4, int i, Object obj, Object obj2, Object obj3, Map<String, String> map, Boolean bool) {
            this.context = context2;
            this.adashxServerHost = str3;
            this.aTimestamp = j;
            this.aPage = str4;
            this.aEventId = i;
            this.aArg1 = obj;
            this.aArg2 = obj2;
            this.aArg3 = obj3;
            this.aExtData = map;
            this.appKey = str2;
            this.isUrl = bool;
        }

        public void run() {
            try {
                if (this.isUrl.booleanValue()) {
                    RestReqSend.sendLogByUrl(this.appKey, this.context, this.adashxServerHost, this.aTimestamp, this.aPage, this.aEventId, this.aArg1, this.aArg2, this.aArg3, this.aExtData);
                } else {
                    RestReqSend.sendLog(this.appKey, this.context, this.adashxServerHost, this.aTimestamp, this.aPage, this.aEventId, this.aArg1, this.aArg2, this.aArg3, this.aExtData);
                }
            } catch (Exception e) {
                LogUtil.e("send log asyn error ", e);
            }
        }
    }
}
