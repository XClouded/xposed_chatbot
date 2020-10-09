package com.ali.telescope.internal.plugins.startPref;

import com.ali.telescope.base.report.IReportStringBean;
import com.ali.telescope.internal.report.ProtocolConstants;

public class StartPrefTaskBean implements IReportStringBean {
    private boolean isStart;
    private String taskName;
    private long time;

    public StartPrefTaskBean(String str, long j, boolean z) {
        this.taskName = str;
        this.time = j;
        this.isStart = z;
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return this.isStart ? ProtocolConstants.EVENT_LAUNCH_TASK_START : ProtocolConstants.EVENT_LAUNCH_TASK_END;
    }

    public String getBody() {
        return this.taskName;
    }
}
