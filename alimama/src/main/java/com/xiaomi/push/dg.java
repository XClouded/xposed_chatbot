package com.xiaomi.push;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;

public class dg implements LoggerInterface {
    private LoggerInterface a = null;
    private LoggerInterface b = null;

    public dg(LoggerInterface loggerInterface, LoggerInterface loggerInterface2) {
        this.a = loggerInterface;
        this.b = loggerInterface2;
    }

    public void log(String str) {
        if (this.a != null) {
            this.a.log(str);
        }
        if (this.b != null) {
            this.b.log(str);
        }
    }

    public void log(String str, Throwable th) {
        if (this.a != null) {
            this.a.log(str, th);
        }
        if (this.b != null) {
            this.b.log(str, th);
        }
    }

    public void setTag(String str) {
    }
}
