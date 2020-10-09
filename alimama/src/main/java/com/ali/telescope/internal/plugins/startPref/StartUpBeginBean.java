package com.ali.telescope.internal.plugins.startPref;

import com.ali.telescope.base.report.IReportBean;
import com.ali.telescope.internal.report.ProtocolConstants;

public class StartUpBeginBean implements IReportBean {
    private long startTime;

    public StartUpBeginBean(long j) {
        this.startTime = j;
    }

    public long getTime() {
        return this.startTime;
    }

    public short getType() {
        return ProtocolConstants.EVENT_APP_START_UP_BEGIN;
    }
}
