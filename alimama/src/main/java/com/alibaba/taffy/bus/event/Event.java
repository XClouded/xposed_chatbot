package com.alibaba.taffy.bus.event;

public class Event {
    protected long createTimestamp;
    protected Object data;
    protected long sendTimestamp;
    protected String tag;
    protected String topic;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String str) {
        this.topic = str;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public long getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setCreateTimestamp(long j) {
        this.createTimestamp = j;
    }

    public long getSendTimestamp() {
        return this.sendTimestamp;
    }

    public void setSendTimestamp(long j) {
        this.sendTimestamp = j;
    }
}
