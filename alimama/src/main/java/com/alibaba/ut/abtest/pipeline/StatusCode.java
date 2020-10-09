package com.alibaba.ut.abtest.pipeline;

public interface StatusCode {
    public static final int INTERFACE_LIMIT = 20001;
    public static final int INVALID_REQUEST = 30000;
    public static final int PULL_DISABLE = 20003;
    public static final int UNKNOW_ERROR = 40000;
    public static final int UTDID_IS_BLANK = 10002;
    public static final int VERSION_IS_NEWEST = 20002;
}
