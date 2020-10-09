package com.alibaba.appmonitor.offline;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;

@TableName("counter_temp")
public class TempCounter extends TempEvent {
    @Column("arg")
    public String arg;
    @Column("value")
    public double value;

    public TempCounter(String str, String str2, String str3, double d, String str4, String str5) {
        super(str, str2, str4, str5);
        this.arg = str3;
        this.value = d;
    }

    public TempCounter() {
    }

    public String toString() {
        return "TempCounter{" + "arg='" + this.arg + '\'' + ", value=" + this.value + '}';
    }
}
