package com.taobao.tao.log.godeye.api.command;

public enum TraceProgress {
    NOT_STARTED,
    EXCEPTION_ON_TRY_TIME_OUT,
    RUNNING,
    COMPLETE,
    EXCEPTION_ON_UPLOAD,
    UPLOADED;

    public static TraceProgress fromName(String str) {
        for (TraceProgress traceProgress : values()) {
            if (traceProgress.name().equalsIgnoreCase(str)) {
                return traceProgress;
            }
        }
        return NOT_STARTED;
    }
}
