package com.taobao.weex.utils.tools;

import com.alibaba.fastjson.annotation.JSONField;

public class Time {
    @JSONField(name = "constructor")
    public long constructor;
    @JSONField(name = "destructor")
    public long destructor;
    @JSONField(name = "execTime")
    public long execTime;
    @JSONField(name = "taskEnd")
    public long taskEnd;
    @JSONField(name = "taskStart")
    public long taskStart;
    @JSONField(name = "waitTime")
    public long waitTime;

    public String toString() {
        return "time : {constructor = '" + this.constructor + '\'' + ",taskStart = '" + this.taskStart + '\'' + ",execTime = '" + this.execTime + '\'' + ",waitTime = '" + this.waitTime + '\'' + ",destructor = '" + this.destructor + '\'' + ",taskEnd = '" + this.taskEnd + '\'' + "}";
    }

    /* access modifiers changed from: protected */
    public void constructor() {
        this.constructor = System.currentTimeMillis();
    }

    public void execTime() {
        this.execTime = this.taskEnd - this.taskStart;
    }

    public void taskStart() {
        this.taskStart = System.currentTimeMillis();
    }

    public void taskEnd() {
        this.taskEnd = System.currentTimeMillis();
        execTime();
        destructor();
    }

    private void destructor() {
        waitTime();
        this.destructor = System.currentTimeMillis();
    }

    public void waitTime() {
        this.waitTime = this.taskStart - this.constructor;
    }
}
