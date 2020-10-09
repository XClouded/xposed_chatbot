package com.alibaba.android.anynetwork.core.common;

public class ANNetRes {

    public class BaseNetRes {
        public static final int ERROR_BASE_NONE = 0;
        public static final int ERROR_CODE_BASE = -1000;
        public static final int ERROR_CODE_NONSUPPORT_BASE_TYPE = -1003;
        public static final int ERROR_CODE_NO_SUPPORT_SERVICE = -1002;
        public static final int ERROR_CODE_REQUEST_IS_NULL = -1004;
        public static final int ERROR_CODE_UNKNOWN_BASE_CODE = -1001;

        public BaseNetRes() {
        }
    }

    public class DownloadResponseRes {
        public static final String CANCEL = "cancel";
        public static final String ERROR = "error";
        public static final String SUCCESS = "success";

        public DownloadResponseRes() {
        }
    }
}
