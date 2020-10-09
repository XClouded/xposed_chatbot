package com.ali.user.mobile.exception;

import com.taobao.weex.el.parse.Operators;

public class RpcException extends RuntimeException {
    private static final long serialVersionUID = -2875437994101380406L;
    private int mCode;
    private String mExtCode;
    private String mMsg;

    public interface ErrorCode {
        public static final int ANTI_REFRESH = 401;
        public static final int CLIENT_DESERIALIZER_ERROR = 10;
        public static final int CLIENT_HANDLE_ERROR = 9;
        public static final int CLIENT_INTERUPTED_ERROR = 13;
        public static final int CLIENT_LOGIN_FAIL_ERROR = 11;
        public static final int CLIENT_NETWORK_CONNECTION_ERROR = 4;
        public static final int CLIENT_NETWORK_ERROR = 7;
        public static final int CLIENT_NETWORK_SCHEDULE_ERROR = 8;
        public static final int CLIENT_NETWORK_SERVER_ERROR = 6;
        public static final int CLIENT_NETWORK_SOCKET_ERROR = 5;
        public static final int CLIENT_NETWORK_SSL_ERROR = 3;
        public static final int CLIENT_NETWORK_UNAVAILABLE_ERROR = 2;
        public static final int CLIENT_USER_CHANGE_ERROR = 12;
        public static final int ERROR_UNKNOWN = 405;
        public static final int EXPIRED_REQUEST = 402;
        public static final int ILLEGEL_SIGN = 403;
        public static final int LIMIT_ERROR = 400;
        public static final int SESSION_INVALID = 407;
        public static final int SYSTEM_ERROR = 406;
    }

    public static boolean isSystemError(int i) {
        return i == 7;
    }

    public String getExtCode() {
        return this.mExtCode;
    }

    public RpcException(Integer num, String str, String str2) {
        super(format(num, str));
        this.mCode = num.intValue();
        this.mMsg = str;
        this.mExtCode = str2;
    }

    public RpcException(Integer num, String str) {
        super(format(num, str));
        this.mCode = num.intValue();
        this.mMsg = str;
    }

    public RpcException(Integer num, Throwable th) {
        super(th);
        this.mCode = num.intValue();
    }

    public RpcException(String str) {
        super(str);
        this.mCode = 0;
        this.mMsg = str;
    }

    public int getCode() {
        return this.mCode;
    }

    public String getMsg() {
        return this.mMsg;
    }

    protected static String format(Integer num, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("RPCException: ");
        if (num != null) {
            sb.append(Operators.ARRAY_START_STR);
            sb.append(num);
            sb.append(Operators.ARRAY_END_STR);
        }
        sb.append(" : ");
        if (str != null) {
            sb.append(str);
        }
        return sb.toString();
    }
}
