package com.taobao.weex.module;

import com.taobao.orange.OrangeConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import com.taobao.weex.utils.TBWXInterNetUtil;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONException;

public class WXTBUtils extends WXModule {
    public static final String ORANGE_GROUP = "weex_adaper_url_intercept";
    public static final String ORANGE_KEY = "weex_prefetch_mtop_switch";
    /* access modifiers changed from: private */
    public static long mTimeoffset;

    @WXModuleAnno
    public void getServerTimestamp(final JSCallback jSCallback) {
        if (jSCallback != null) {
            final HashMap hashMap = new HashMap();
            if (mTimeoffset != 0) {
                hashMap.put("data", (System.currentTimeMillis() + mTimeoffset) + "");
                jSCallback.invoke(hashMap);
                return;
            }
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName("mtop.common.getTimestamp");
            mtopRequest.setVersion("*");
            mtopRequest.setNeedEcode(false);
            Mtop.instance(WXEnvironment.getApplication()).build(mtopRequest, (String) null).addListener(new MtopCallback.MtopFinishListener() {
                public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
                    MtopResponse mtopResponse = mtopFinishEvent.getMtopResponse();
                    long currentTimeMillis = System.currentTimeMillis();
                    if (mtopResponse.isApiSuccess()) {
                        try {
                            String str = (String) mtopResponse.getDataJsonObject().get("t");
                            long unused = WXTBUtils.mTimeoffset = Long.parseLong(str) - currentTimeMillis;
                            hashMap.put("data", str);
                        } catch (JSONException unused2) {
                            Map map = hashMap;
                            map.put("data", currentTimeMillis + "");
                        }
                    } else {
                        Map map2 = hashMap;
                        map2.put("data", currentTimeMillis + "");
                    }
                    jSCallback.invoke(hashMap);
                }
            }).asyncRequest();
        }
    }

    @WXModuleAnno
    public void getNetworkStatus(JSCallback jSCallback) {
        if (jSCallback != null) {
            jSCallback.invoke(TBWXInterNetUtil.getNetworkState(this.mWXSDKInstance.getContext()));
        }
    }

    @WXModuleAnno
    public void prefetchMtopSwitchOn(JSCallback jSCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("mswitch", Boolean.valueOf(OrangeConfig.getInstance().getConfig(ORANGE_GROUP, ORANGE_KEY, "false").equals("true")));
        jSCallback.invoke(hashMap);
    }
}
