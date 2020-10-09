package com.taobao.monitor.impl.logger;

public class DataLoggerUtils {
    private static IDataLogger dataLogger;

    public static void log(String str, Object... objArr) {
        if (dataLogger != null) {
            dataLogger.log(str, objArr);
        }
    }

    public static void setDataLogger(IDataLogger iDataLogger) {
        dataLogger = iDataLogger;
    }
}
