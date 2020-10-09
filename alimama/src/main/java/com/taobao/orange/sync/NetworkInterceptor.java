package com.taobao.orange.sync;

import android.text.TextUtils;
import anet.channel.bytes.ByteArray;
import anet.channel.request.Request;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class NetworkInterceptor implements Interceptor {
    static final String ORANGE_REQ_HEADER = "a-orange-q";
    static final String ORANGE_RES_HEADER = "a-orange-p";
    static final String TAG = "NetworkInterceptor";

    public Future intercept(final Interceptor.Chain chain) {
        boolean z;
        Request request = chain.request();
        Callback callback = chain.callback();
        if (GlobalOrange.indexUpdMode != OConstant.UPDMODE.O_EVENT && !TextUtils.isEmpty(request.getHost()) && !GlobalOrange.probeHosts.isEmpty()) {
            Iterator<String> it = GlobalOrange.probeHosts.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (request.getHost().contains(it.next())) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (z) {
            if (!TextUtils.isEmpty(GlobalOrange.reqOrangeHeader)) {
                request = chain.request().newBuilder().addHeader(ORANGE_REQ_HEADER, GlobalOrange.reqOrangeHeader).build();
            }
            callback = new Callback() {
                public void onResponseCode(int i, final Map<String, List<String>> map) {
                    if (map != null && map.containsKey(NetworkInterceptor.ORANGE_RES_HEADER)) {
                        OThreadFactory.execute(new Runnable() {
                            public void run() {
                                try {
                                    AndroidUtil.setThreadPriority();
                                    IndexUpdateHandler.updateIndex(NetworkInterceptor.getOrangeFromKey(map, NetworkInterceptor.ORANGE_RES_HEADER), false);
                                } catch (Throwable th) {
                                    OLog.e(NetworkInterceptor.TAG, "intercept", th, new Object[0]);
                                }
                            }
                        });
                    }
                    chain.callback().onResponseCode(i, map);
                }

                public void onDataReceiveSize(int i, int i2, ByteArray byteArray) {
                    chain.callback().onDataReceiveSize(i, i2, byteArray);
                }

                public void onFinish(DefaultFinishEvent defaultFinishEvent) {
                    chain.callback().onFinish(defaultFinishEvent);
                }
            };
        }
        return chain.proceed(request, callback);
    }

    static String getOrangeFromKey(Map<String, List<String>> map, String str) {
        List<String> list;
        Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                list = null;
                break;
            }
            Map.Entry next = it.next();
            if (str.equalsIgnoreCase((String) next.getKey())) {
                list = (List) next.getValue();
                break;
            }
        }
        if (list == null || list.isEmpty()) {
            OLog.w(TAG, "getOrangeFromKey fail", "not exist a-orange-p");
            return null;
        }
        for (String str2 : list) {
            if (str2 != null && str2.startsWith("resourceId")) {
                if (OLog.isPrintLog(1)) {
                    OLog.d(TAG, "getOrangeFromKey", "value", str2);
                }
                try {
                    return URLDecoder.decode(str2, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    OLog.w(TAG, "getOrangeFromKey", e, new Object[0]);
                    return null;
                }
            }
        }
        OLog.w(TAG, "getOrangeFromKey fail", "parseValue no resourceId");
        return null;
    }
}
