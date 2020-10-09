package com.taobao.phenix.compat.mtop;

import android.os.RemoteException;
import android.text.TextUtils;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.statist.StatisticData;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.loader.network.HttpCodeResponseException;
import com.taobao.phenix.loader.network.HttpLoader;
import com.taobao.phenix.loader.network.IncompleteResponseException;
import com.taobao.phenix.loader.network.NetworkResponseException;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class MtopResponseListener implements NetworkCallBack.ResponseCodeListener, NetworkCallBack.InputStreamListener, NetworkCallBack.FinishListener {
    private final HttpLoader.FinishCallback mFinishCallback;
    private final Map<String, String> mLoadExtras;
    private boolean mOnceCalled;

    public MtopResponseListener(HttpLoader.FinishCallback finishCallback, Map<String, String> map) {
        this.mFinishCallback = finishCallback;
        this.mLoadExtras = map;
    }

    private NetworkResponseException classifyException(NetworkEvent.FinishEvent finishEvent) {
        int i;
        if (finishEvent != null) {
            i = finishEvent.getHttpCode();
        } else {
            i = 0;
        }
        if (i != -405) {
            if (i != -202) {
                if (i == -102) {
                    return new MtopInvalidUrlException(i);
                }
                if (i == 200) {
                    return new IncompleteResponseException();
                }
                switch (i) {
                    case ErrorConstant.ERROR_HOST_NOT_VERIFY_ERROR:
                        break;
                    case ErrorConstant.ERROR_SSL_ERROR:
                        return new MtopCertificateException(i);
                    case ErrorConstant.ERROR_SOCKET_TIME_OUT:
                    case -400:
                        break;
                    default:
                        return new MtopIndifferentException(i, finishEvent != null ? finishEvent.getDesc() : "unknown");
                }
            }
            return new MtopConnectTimeoutException(i);
        }
        return new MtopInvalidHostException(i);
    }

    public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
        StatisticData statisticData;
        if (!(this.mLoadExtras == null || finishEvent == null || (statisticData = finishEvent.getStatisticData()) == null)) {
            this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_CONNECT_TYPE, statisticData.connectionType);
            this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_CDN_IP_PORT, statisticData.ip_port);
            this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_FIRST_DATA, String.valueOf(statisticData.firstDataTime));
            this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_SEND_BEFORE, String.valueOf(statisticData.sendBeforeTime));
            this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_SERVER_RT, String.valueOf(statisticData.serverRT));
        }
        if (!this.mOnceCalled) {
            this.mOnceCalled = true;
            this.mFinishCallback.onError(classifyException(finishEvent));
        }
    }

    public void onInputStreamGet(ParcelableInputStream parcelableInputStream, Object obj) {
        int i;
        if (!this.mOnceCalled && parcelableInputStream != null) {
            MtopResponseInputStream mtopResponseInputStream = new MtopResponseInputStream(parcelableInputStream);
            try {
                i = parcelableInputStream.length();
                try {
                    UnitedLog.d("Network", "%s get content length(%d) from stream success", MtopHttpLoader.MTOP_PREFIX, Integer.valueOf(i));
                } catch (RemoteException unused) {
                }
            } catch (RemoteException unused2) {
                i = 0;
                UnitedLog.e("Network", "%s get content length from stream failed", MtopHttpLoader.MTOP_PREFIX);
                this.mOnceCalled = true;
                this.mFinishCallback.onFinished(new ResponseData(mtopResponseInputStream, i));
            }
            this.mOnceCalled = true;
            this.mFinishCallback.onFinished(new ResponseData(mtopResponseInputStream, i));
        }
    }

    private String getHeaderValue(Map<String, List<String>> map, String str) {
        List list = map.get(str);
        String lowerCase = str.toLowerCase();
        if (list == null && !str.equals(lowerCase)) {
            list = map.get(lowerCase);
        }
        if (list == null || list.size() <= 0) {
            return null;
        }
        return (String) list.get(0);
    }

    private String getMaxAge(String str) {
        String[] split;
        String[] split2;
        if (TextUtils.isEmpty(str) || (split = str.replace(Operators.SPACE_STR, "").split(",")) == null || split.length <= 0) {
            return "";
        }
        for (int i = 0; i < split.length; i++) {
            if (!TextUtils.isEmpty(split[i]) && (split2 = split[i].split(SymbolExpUtil.SYMBOL_EQUAL)) != null && split2.length > 1 && "max-age".equals(split2[0])) {
                return split2[1];
            }
        }
        return "";
    }

    public boolean onResponseCode(int i, Map<String, List<String>> map, Object obj) {
        String str;
        String str2;
        String str3 = null;
        if (map != null) {
            str3 = getHeaderValue(map, "X-Cache");
            str2 = getHeaderValue(map, "eagleid");
            str = getHeaderValue(map, HttpConstant.CACHE_CONTROL);
        } else {
            str = null;
            str2 = null;
        }
        if (this.mLoadExtras != null) {
            if (!TextUtils.isEmpty(str3)) {
                this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_HIT_CDN_CACHE, str3.startsWith("HIT") ? "1" : "0");
            }
            if (!TextUtils.isEmpty(str2)) {
                this.mLoadExtras.put("eagleid", str2);
            }
            String maxAge = getMaxAge(str);
            if (!TextUtils.isEmpty(maxAge)) {
                this.mLoadExtras.put("max-age", maxAge);
            }
            String str4 = this.mLoadExtras.get(Constant.INNER_EXTRA_NETWORK_START_TIME);
            if (str4 != null) {
                this.mLoadExtras.put(MtopHttpLoader.MTOP_EXTRA_RESPONSE_CODE, String.valueOf(System.currentTimeMillis() - Long.parseLong(str4)));
            }
        }
        if (!this.mOnceCalled && i != 200) {
            this.mOnceCalled = true;
            this.mFinishCallback.onError(new HttpCodeResponseException(i));
        }
        return true;
    }
}
