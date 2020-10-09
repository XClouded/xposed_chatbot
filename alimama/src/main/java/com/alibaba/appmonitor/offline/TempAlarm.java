package com.alibaba.appmonitor.offline;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;

@TableName("alarm_temp")
public class TempAlarm extends TempEvent {
    @Column("arg")
    public String arg;
    @Column("err_code")
    public String errCode;
    @Column("err_msg")
    public String errMsg;
    @Column("success")
    public String success;

    public TempAlarm() {
    }

    public TempAlarm(String str, String str2, String str3, String str4, String str5, boolean z, String str6, String str7) {
        super(str, str2, str6, str7);
        this.arg = str3;
        this.errCode = str4;
        this.errMsg = str5;
        this.success = z ? "1" : "0";
    }

    public boolean isSuccessEvent() {
        return "1".equalsIgnoreCase(this.success);
    }

    public String toString() {
        return "TempAlarm{" + " module='" + this.module + '\'' + ", monitorPoint='" + this.monitorPoint + '\'' + ", commitTime=" + this.commitTime + ", access='" + this.access + '\'' + ", accessSubType='" + this.accessSubType + '\'' + ", arg='" + this.arg + '\'' + ", errCode='" + this.errCode + '\'' + ", errMsg='" + this.errMsg + '\'' + ", success='" + this.success + '\'' + '}';
    }
}
