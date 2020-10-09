package com.ali.telescope.internal.plugins.appevent;

import com.ali.telescope.base.report.IReportBean;
import com.ali.telescope.internal.report.ProtocolConstants;

public class AppEventBean implements IReportBean {
    public long time;
    public short type;

    public AppEventBean(int i, long j) {
        if (i == 1) {
            this.type = ProtocolConstants.EVENT_APP_BACKGROUND;
        } else if (i == 2) {
            this.type = ProtocolConstants.EVENT_APP_FOREGROUND;
        }
        this.time = j;
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return this.type;
    }
}
