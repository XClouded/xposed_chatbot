package com.ali.telescope.base.report;

public class AbstractReportBean implements IReportRawByteBean {
    public byte[] body;
    public long time;
    public short type;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return this.type;
    }

    public byte[] getBody() {
        return this.body;
    }
}
