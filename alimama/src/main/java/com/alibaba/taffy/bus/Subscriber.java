package com.alibaba.taffy.bus;

import com.alibaba.taffy.bus.listener.EventListener;

public class Subscriber {
    public static final String GROUP_DEFAULT = "";
    public static final int STATUS_DISABLED = 1;
    public static final int STATUS_HOLD = 2;
    public static final int STATUS_NORMAL = 0;
    private String filter;
    private String group;
    private long id = -1;
    private EventListener listener;
    private int priority = 3;
    private int status;
    private int thread;
    private String topic;

    public Subscriber() {
    }

    public Subscriber(String str, int i, EventListener eventListener) {
        this.topic = str;
        this.thread = i;
        this.listener = eventListener;
    }

    public Subscriber(long j, String str, String str2, int i, int i2, int i3, String str3, EventListener eventListener) {
        this.id = j;
        this.topic = str;
        this.filter = str2;
        this.priority = i;
        this.thread = i2;
        this.status = i3;
        this.group = str3;
        this.listener = eventListener;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String str) {
        this.topic = str;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String str) {
        this.filter = str;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public int getThread() {
        return this.thread;
    }

    public void setThread(int i) {
        this.thread = i;
    }

    public EventListener getListener() {
        return this.listener;
    }

    public void setListener(EventListener eventListener) {
        this.listener = eventListener;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }
}
