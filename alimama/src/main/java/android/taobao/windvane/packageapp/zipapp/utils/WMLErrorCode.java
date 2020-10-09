package android.taobao.windvane.packageapp.zipapp.utils;

public enum WMLErrorCode {
    ERROR_FETCH_APP_CONFIG("1003", "ERROR_FETCH_APP_CONFIG"),
    ERROR_PARSE_APP_CONFIG("1007", "ERROR_PARSE_APP_CONFIG"),
    ERROR_EMPTY_APP_CONFIG("1006", "ERROR_EMPTY_APP_CONFIG"),
    ERROR_INVALID_APP_VERSION("1008", "ERROR_INVALID_APP_VERSION"),
    ERROR_INSTALL_APP("3203", "ERROR_INSTALL_APP"),
    ERROR_UNZIP_APP("2207", "ERROR_UNZIP_APP"),
    ERROR_FILE_NOT_EXISTED("3109", "ERROR_FILE_NOT_EXISTED"),
    ERROR_APP_DELETED("3106", "ERROR_APP_DELETED"),
    ERROR_ZCACHE_NOT_INIT("3107", "ERROR_ZCACHE_NOT_INIT");
    
    private String code;
    private String msg;

    private WMLErrorCode(String str, String str2) {
        this.code = str;
        this.msg = str2;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.msg;
    }
}
