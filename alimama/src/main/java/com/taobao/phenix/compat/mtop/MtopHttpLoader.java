package com.taobao.phenix.compat.mtop;

import android.content.Context;
import android.os.Handler;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.util.RequestConstant;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.loader.network.HttpLoader;
import java.util.Map;
import java.util.concurrent.Future;

public class MtopHttpLoader implements HttpLoader {
    public static final String MTOP_EXTRA_CDN_IP_PORT = "mtop_extra_ip_port";
    public static final String MTOP_EXTRA_CONNECT_TYPE = "mtop_extra_connect_type";
    public static final String MTOP_EXTRA_FIRST_DATA = "mtop_extra_first_data";
    public static final String MTOP_EXTRA_HIT_CDN_CACHE = "mtop_extra_hit_cdn_cache";
    public static final String MTOP_EXTRA_RESPONSE_CODE = "mtop_extra_response_code";
    public static final String MTOP_EXTRA_SEND_BEFORE = "mtop_extra_send_before";
    public static final String MTOP_EXTRA_SERVER_RT = "mtop_extra_server_rt";
    public static final String MTOP_PREFIX = "MtopHttpLoader";
    private int mConnectTimeout;
    private final Context mContext;
    private int mReadTimeout;

    public MtopHttpLoader(Context context) {
        this.mContext = context;
    }

    public void connectTimeout(int i) {
        this.mConnectTimeout = i;
    }

    public void readTimeout(int i) {
        this.mReadTimeout = i;
    }

    public Future<?> load(String str, Map<String, String> map, HttpLoader.FinishCallback finishCallback) {
        String str2;
        String str3;
        UnitedLog.dp("Network", str, "%s async download image", MTOP_PREFIX);
        RequestImpl requestImpl = new RequestImpl(str);
        requestImpl.setCookieEnabled(false);
        requestImpl.setFollowRedirects(true);
        requestImpl.setConnectTimeout(this.mConnectTimeout);
        requestImpl.setReadTimeout(this.mReadTimeout);
        requestImpl.addHeader("f-refer", "picture");
        requestImpl.addHeader("User-Agent", "TBAndroid/Native");
        if (!(map == null || (str3 = map.get(RequestConstant.KEY_TRACE_ID)) == null)) {
            requestImpl.setExtProperty(RequestConstant.KEY_TRACE_ID, str3);
        }
        if (!(map == null || (str2 = map.get(Constant.BUNDLE_BIZ_CODE)) == null)) {
            try {
                requestImpl.setBizId(Integer.parseInt(str2));
            } catch (NumberFormatException e) {
                UnitedLog.dp("Network", str, "%s get biz code from extras error=%s", MTOP_PREFIX, e);
            }
        }
        return new DegradableNetwork(this.mContext).asyncSend(requestImpl, (Object) null, (Handler) null, new MtopResponseListener(finishCallback, map));
    }
}
