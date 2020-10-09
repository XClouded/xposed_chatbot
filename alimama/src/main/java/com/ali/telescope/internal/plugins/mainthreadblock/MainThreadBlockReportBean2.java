package com.ali.telescope.internal.plugins.mainthreadblock;

import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;

public class MainThreadBlockReportBean2 implements IReportErrorBean {
    private String body;
    private String key;
    private long time;

    public String getErrorType() {
        return ErrorConstants.HA_MAIN_THREAD_BLOCK;
    }

    public Throwable getThrowable() {
        return null;
    }

    public MainThreadBlockReportBean2(long j, String str, String str2) {
        this.time = j;
        this.body = str;
        this.key = str2;
    }

    public short getType() {
        return ProtocolConstants.EVENT_MAINTHREAD_BLOCK;
    }

    public long getTime() {
        return this.time;
    }

    public String getBody() {
        return this.body;
    }

    public String getKey() {
        return this.key;
    }
}
