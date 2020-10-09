package com.taobao.phenix.compat;

import com.taobao.phenix.compat.mtop.MtopCertificateException;
import com.taobao.phenix.compat.mtop.MtopConnectTimeoutException;
import com.taobao.phenix.compat.mtop.MtopHttpLoader;
import com.taobao.phenix.compat.mtop.MtopIndifferentException;
import com.taobao.phenix.compat.mtop.MtopInvalidHostException;
import com.taobao.phenix.compat.mtop.MtopInvalidUrlException;
import com.taobao.phenix.compat.mtop.MtopReadTimeoutException;
import com.taobao.phenix.compat.stat.NetworkAnalyzer;

public class TBNetworkAnalyzer implements NetworkAnalyzer {
    public String keyOfCdnIpPort() {
        return MtopHttpLoader.MTOP_EXTRA_CDN_IP_PORT;
    }

    public String keyOfConnectType() {
        return MtopHttpLoader.MTOP_EXTRA_CONNECT_TYPE;
    }

    public String keyOfFirstData() {
        return MtopHttpLoader.MTOP_EXTRA_FIRST_DATA;
    }

    public String keyOfHitCdnCache() {
        return MtopHttpLoader.MTOP_EXTRA_HIT_CDN_CACHE;
    }

    public String keyOfResponseCode() {
        return MtopHttpLoader.MTOP_EXTRA_RESPONSE_CODE;
    }

    public String keyOfSendBefore() {
        return MtopHttpLoader.MTOP_EXTRA_SEND_BEFORE;
    }

    public String keyOfServerRt() {
        return MtopHttpLoader.MTOP_EXTRA_SERVER_RT;
    }

    public boolean isNoNetworkException(Throwable th) {
        return (th instanceof MtopIndifferentException) && ((MtopIndifferentException) th).getExtraCode() == -200;
    }

    public boolean isReadTimeoutException(Throwable th) {
        return th instanceof MtopReadTimeoutException;
    }

    public boolean isCertificateException(Throwable th) {
        return th instanceof MtopCertificateException;
    }

    public boolean isInvalidHostException(Throwable th) {
        return th instanceof MtopInvalidHostException;
    }

    public boolean isConnectTimeoutException(Throwable th) {
        return th instanceof MtopConnectTimeoutException;
    }

    public boolean isInvalidUrlException(Throwable th) {
        return th instanceof MtopInvalidUrlException;
    }

    public boolean isIndifferentException(Throwable th) {
        return th instanceof MtopIndifferentException;
    }
}
