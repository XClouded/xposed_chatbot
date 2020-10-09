package com.ali.telescope.internal.plugins.resourceleak;

import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;

public class ResourceLeakReportBean implements IReportErrorBean {
    public Throwable mThrowable;
    public long time;

    public String getBody() {
        return null;
    }

    public String getErrorType() {
        return ErrorConstants.HA_RESOURCE_LEAK;
    }

    public String getKey() {
        return null;
    }

    public ResourceLeakReportBean(long j, Throwable th) {
        this.time = j;
        this.mThrowable = th;
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_RESOURCE_LEAK;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }
}
