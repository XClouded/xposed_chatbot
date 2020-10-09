package com.huawei.hms.update.a.a;

import com.alibaba.wireless.security.SecExceptionCode;

/* compiled from: UpdateStatus */
public final class d {
    public static String a(int i) {
        if (i == 1000) {
            return "CHECK_OK";
        }
        if (i == 2000) {
            return "DOWNLOAD_SUCCESS";
        }
        switch (i) {
            case 1201:
                return "CHECK_FAILURE";
            case 1202:
                return "CHECK_NO_UPDATE";
            case 1203:
                return "CHECK_NO_SUPPORTED";
            default:
                switch (i) {
                    case 2100:
                        return "DOWNLOADING";
                    case 2101:
                        return "DOWNLOAD_CANCELED";
                    default:
                        switch (i) {
                            case SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM:
                                return "DOWNLOAD_FAILURE";
                            case SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_LOCATION_SET:
                                return "DOWNLOAD_HASH_ERROR";
                            case SecExceptionCode.SEC_ERROR_LBSRISK_NO_MEMORY:
                                return "DOWNLOAD_NO_SPACE";
                            case SecExceptionCode.SEC_ERROR_LBSRISK_NOT_INIT:
                                return "DOWNLOAD_NO_STORAGE";
                            default:
                                return "UNKNOWN - " + Integer.toString(i);
                        }
                }
        }
    }
}
