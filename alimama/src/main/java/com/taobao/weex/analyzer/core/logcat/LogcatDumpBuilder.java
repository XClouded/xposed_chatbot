package com.taobao.weex.analyzer.core.logcat;

import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.core.logcat.LogcatDumper;
import java.util.LinkedList;
import java.util.List;

public class LogcatDumpBuilder {
    private int cacheLimit;
    private boolean isCacheEnabled;
    private int level = 0;
    private LogcatDumper.OnLogReceivedListener logReceivedListener;
    private List<LogcatDumper.Rule> ruleList;

    public LogcatDumpBuilder listener(LogcatDumper.OnLogReceivedListener onLogReceivedListener) {
        this.logReceivedListener = onLogReceivedListener;
        return this;
    }

    public LogcatDumpBuilder rule(@Nullable LogcatDumper.Rule rule) {
        if (rule == null) {
            return this;
        }
        if (this.ruleList == null) {
            this.ruleList = new LinkedList();
        }
        this.ruleList.add(rule);
        return this;
    }

    public LogcatDumpBuilder level(int i) {
        this.level = i;
        return this;
    }

    public LogcatDumpBuilder enableCache(boolean z) {
        this.isCacheEnabled = z;
        return this;
    }

    public LogcatDumpBuilder cacheLimit(int i) {
        this.cacheLimit = i;
        return this;
    }

    public LogcatDumper build() {
        LogcatDumper logcatDumper = new LogcatDumper(this.logReceivedListener);
        logcatDumper.setLevel(this.level);
        logcatDumper.setCacheLimit(this.cacheLimit);
        logcatDumper.setCacheEnabled(this.isCacheEnabled);
        if (this.ruleList != null) {
            for (LogcatDumper.Rule addRule : this.ruleList) {
                logcatDumper.addRule(addRule);
            }
        }
        return logcatDumper;
    }
}
