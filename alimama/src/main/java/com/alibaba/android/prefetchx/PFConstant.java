package com.alibaba.android.prefetchx;

public class PFConstant {
    public static final String PF_ORANGE_CONFIG_ERROR = "-10001";
    public static final String TAG = "PrefetchX";
    public static final String USELESS_CODE = "-101";

    public static class Data {
        public static final String PF_DATA_DEFAULT_MAP_URL = "https://h5.m.taobao.com/app/prefetchmap/prefetch.js";
        public static final String PF_DATA_ENABLE = "data_prefetch";
        public static final String PF_DATA_ENABLE_2 = "mtop_prefetch_enable";
        public static final String PF_DATA_ERROR = "-1";
        public static final String PF_DATA_FLAG = "wh_prefetch";
        public static final String PF_DATA_GEO_LATITUDE = "$_geo_latitude_$";
        public static final String PF_DATA_GEO_LONGITUDE = "$_geo_longitude_$";
        public static final String PF_DATA_JSON_PRASE_FAILED_ERROR = "-30004";
        public static final String PF_DATA_MERGE_ERROR = "-30008";
        public static final String PF_DATA_MODULE_NAME = "PrefetchX";
        public static final String PF_DATA_MONITOR_NAME = "PrefetchX_Data";
        public static final String PF_DATA_MTOP_QUERY_ERROR = "-30003";
        public static final String PF_DATA_NEED_LOGIN = "wh_needlogin";
        public static final String PF_DATA_NEED_LOGIN_ERROR = "-30005";
        public static final String PF_DATA_NONE = "";
        public static final String PF_DATA_REFRESH_GEO = "wh_refresh_geo";
        public static final String PF_DATA_SAVE_STATUS_STORAGE_ERROR = "-30002";
        public static final String PF_DATA_SAVE_STORAGE_ERROR = "-30007";
        public static final String PF_DATA_URL_ID = "wh_prefetch_id";
        public static final String PF_DATA_ZIP_PACKAGE_CACHE = "-30006";
        public static final String STATUS_GOT_RESPONSE = "got_response";
        public static final String STATUS_GOT_RESPONSE_FAIL = "got_response_fail";
        public static final String STATUS_INIT = "init";
        public static final String STATUS_QUEUE = "resquesting";
        public static final String STATUS_SAVED_TO_STORAGE = "saved_to_storage";
        public static final String STATUS_SAVED_TO_STORAGE_FAIL = "saved_to_storage_fail";
        public static final String TAG = "PrefetchX_Data";
    }

    public static class File {
        public static final String PF_FILE_ALREADY_REQUESTED = "-40104";
        public static final String PF_FILE_ALREADY_REQUESTED_MSG = "already_requested";
        public static final String PF_FILE_BEEN_CACHED = "-40103";
        public static final String PF_FILE_BEEN_CACHED_MSG = "been_cached";
        public static final String PF_FILE_ERROR_CONNECTION = "-40102";
        public static final String PF_FILE_ERROR_CONNECTION_MSG = "error_connection_type";
        public static final String PF_FILE_EXCEED = "-40101";
        public static final String PF_FILE_EXCEED_MSG = "exceed";
        public static final String PF_FILE_EXCEPTION_ADD_TASK = "-40002";
        public static final String PF_FILE_FIND_A_LIKE_URL = "-40004";
        public static final String PF_FILE_GET_CONNENCT_TYPE = "-40001";
        public static final String PF_FILE_INTERNAL_ERROR = "-40106";
        public static final String PF_FILE_INTERNAL_ERROR_MSG = "internal_error";
        public static final String PF_FILE_MODULE_NAME = "PrefetchX";
        public static final String PF_FILE_MONITOR_NAME = "PrefetchX_File";
        public static final String PF_FILE_NETWORK_FAILED = "-40105";
        public static final String PF_FILE_NETWORK_FAILED_MSG = "network_failed";
        public static final String PF_FILE_PROCESS_BUDNEL_URL = "-40003";
        public static final String PF_FILE_REGISTER_TO_NETWORK = "-40005";
        public static final String TAG = "PrefetchX_File";
    }

    public static class Image {
        public static final String PF_IMAGE_CONFIG_EXCEPTION = "-166053001";
        public static final String PF_IMAGE_FIND_IMAGE_URL = "-166053002";
        public static final String PF_IMAGE_MODULE_NAME = "PrefetchX";
        public static final String PF_IMAGE_MONITOR_NAME = "PrefetchX_Image";
        public static final String PF_IMAGE_TRANS_TO_JSON_FIND_IMAGE_URL = "-166053003";
        public static final String TAG = "PrefetchX_Image";
        public static final String TAG_BUNDLE = "PrefetchX_Image";
    }

    public static class JSModule {
        public static final String PF_JSMODULE_CONFIG_INPROCESS = "-51001";
        public static final String PF_JSMODULE_DEGENERATE = "-50005";
        public static final String PF_JSMODULE_DEGENERATE_EXCEPTION = "-50007";
        public static final String PF_JSMODULE_ERROR = "-1";
        public static final String PF_JSMODULE_ERROR_PREFETCHX = "-50003";
        public static final String PF_JSMODULE_ERROR_WEEX = "-50002";
        public static final String PF_JSMODULE_EVOLVE = "-50004";
        public static final String PF_JSMODULE_EXCEPTION = "-50099";
        public static final String PF_JSMODULE_LOW_MEMORY = "-51002";
        public static final String PF_JSMODULE_MODULE_NAME = "PrefetchX";
        public static final String PF_JSMODULE_MONITOR_NAME = "PrefetchX_JSModule";
        public static final String PF_JSMODULE_NONE = "";
        public static final String PF_JSMODULE_NOT_READY = "-50006";
        public static final String PF_JSMODULE_NOT_RUN_ON_LOW_DEVICES = "-50010";
        public static final String PF_JSMODULE_TOO_MANY_JSMODULE = "-50001";
        public static final String TAG = "PrefetchX_JSModule";
        public static final String TAG_BUNDLE = "PrefetchX_JSModule_In_Bundle";
    }

    public static class Resource {
        public static final String PF_RESOURCE_MODULE_NAME = "PrefetchX";
        public static final String PF_RESOURCE_MONITOR_NAME = "PrefetchX_Resource";
        public static final String PF_RESOURCE_TOO_MANY_JSMODULE = "-60001";
        public static final String TAG = "PrefetchX_Resource";
    }
}
