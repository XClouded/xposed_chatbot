package com.taobao.zcachecorewrapper.log;

public interface IZCLog {
    public static final int DEBUG = 2;
    public static final int ERROR = 5;
    public static final int INFO = 3;
    public static final int NONE = 6;
    public static final int VERBOSE = 1;
    public static final int WARN = 4;

    void d(String str, String str2);

    void e(String str, String str2);

    void i(String str, String str2);

    boolean isLogLevelEnabled(int i);

    void v(String str, String str2);

    void w(String str, String str2);
}
