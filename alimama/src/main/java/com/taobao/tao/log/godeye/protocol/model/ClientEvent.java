package com.taobao.tao.log.godeye.protocol.model;

public class ClientEvent {
    private String event;
    private Long timestamp;
    private Object value;

    public ClientEvent() {
    }

    public ClientEvent(Long l, String str, Object obj) {
        this.timestamp = l;
        this.event = str;
        this.value = obj;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long l) {
        this.timestamp = l;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String str) {
        this.event = str;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object obj) {
        this.value = obj;
    }
}
