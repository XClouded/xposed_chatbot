package com.alibaba.aliweex.plugin;

import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.taobao.android.tlog.protocol.model.joint.point.TimerJointPoint;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.util.ReflectUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class MtopHandler {
    private static final String MSG_FAILED = "MSG_FAILED";
    private static final String MSG_PARAM_ERR = "MSG_PARAM_ERR";
    private static final String MSG_SUCCESS = "WX_SUCCESS";

    public interface MtopFinshCallback {
        void onError(String str);

        void onSuccess(String str);
    }

    public static void send(String str, final MtopFinshCallback mtopFinshCallback) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("sendMtop >>> " + str);
        }
        if (mtopFinshCallback != null) {
            MtopServerParams parseParams = parseParams(str);
            if (parseParams == null) {
                mtopFinshCallback.onError("MSG_PARAM_ERR");
            } else {
                buildRemoteBusiness(buildRequest(parseParams), parseParams).registeListener((IRemoteListener) new IRemoteBaseListener() {
                    public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                        mtopFinshCallback.onError(MtopHandler.MSG_FAILED);
                    }

                    public void onError(int i, MtopResponse mtopResponse, Object obj) {
                        mtopFinshCallback.onError(MtopHandler.MSG_FAILED);
                    }

                    public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                        try {
                            mtopFinshCallback.onSuccess(mtopResponse.getBytedata() == null ? "{}" : new String(mtopResponse.getBytedata()));
                        } catch (Exception e) {
                            mtopFinshCallback.onError(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }).startRequest();
            }
        }
    }

    private static MtopRequest buildRequest(MtopServerParams mtopServerParams) {
        MtopRequest mtopRequest = new MtopRequest();
        mtopRequest.setApiName(mtopServerParams.api);
        mtopRequest.setVersion(mtopServerParams.v);
        mtopRequest.setNeedEcode(mtopServerParams.ecode);
        mtopRequest.dataParams = mtopServerParams.getData();
        mtopRequest.setData(ReflectUtil.converMapToDataStr(mtopRequest.dataParams));
        return mtopRequest;
    }

    private static RemoteBusiness buildRemoteBusiness(MtopRequest mtopRequest, MtopServerParams mtopServerParams) {
        RemoteBusiness build = RemoteBusiness.build(mtopRequest, mtopServerParams.ttid);
        if (mtopServerParams.isHttps) {
            build.protocol(ProtocolEnum.HTTPSECURE);
        } else {
            build.protocol(ProtocolEnum.HTTP);
        }
        if ("true".equals(WXInitConfigManager.getInstance().getConfigKVFirstValue(WXInitConfigManager.getInstance().c_enable_mtop_cache))) {
            build.useCache();
        }
        if (mtopServerParams.timer > 0) {
            build.setConnectionTimeoutMilliSecond(mtopServerParams.timer);
        }
        if (mtopServerParams.isSec) {
            build.useWua();
        }
        build.reqMethod(mtopServerParams.post ? MethodEnum.POST : MethodEnum.GET);
        return build;
    }

    private static MtopServerParams parseParams(String str) {
        try {
            MtopServerParams mtopServerParams = new MtopServerParams();
            JSONObject jSONObject = new JSONObject(str);
            mtopServerParams.api = jSONObject.getString("api");
            mtopServerParams.v = jSONObject.optString("v", "*");
            boolean z = false;
            mtopServerParams.post = jSONObject.optInt("post", 0) != 0;
            mtopServerParams.ecode = jSONObject.optInt("ecode", 0) != 0;
            mtopServerParams.isSec = jSONObject.optInt("isSec", 1) != 0;
            if (jSONObject.optInt("isHttps", 0) != 0) {
                z = true;
            }
            mtopServerParams.isHttps = z;
            mtopServerParams.ttid = jSONObject.optString("ttid");
            mtopServerParams.timer = jSONObject.optInt(TimerJointPoint.TYPE, -1);
            JSONObject optJSONObject = jSONObject.optJSONObject("param");
            if (optJSONObject != null) {
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    mtopServerParams.addData(next, optJSONObject.getString(next));
                }
            }
            return mtopServerParams;
        } catch (JSONException unused) {
            WXLogUtils.e("parseParams error, param=" + str);
            return null;
        }
    }

    private static class MtopServerParams {
        public String api;
        private Map<String, String> data;
        public boolean ecode;
        public boolean isHttps;
        public boolean isSec;
        public boolean post;
        public int timer;
        public String ttid;
        public String v;

        private MtopServerParams() {
            this.data = new HashMap();
        }

        public Map<String, String> getData() {
            return this.data;
        }

        public void addData(String str, String str2) {
            this.data.put(str, str2);
        }
    }
}
