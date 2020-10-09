package com.taobao.monitor.procedure;

import java.util.Map;

/* compiled from: IProcedure */
class Default implements IProcedure {
    public IProcedure addBiz(String str, Map<String, Object> map) {
        return this;
    }

    public IProcedure addBizAbTest(String str, Map<String, Object> map) {
        return this;
    }

    public IProcedure addBizStage(String str, Map<String, Object> map) {
        return this;
    }

    public IProcedure addProperty(String str, Object obj) {
        return this;
    }

    public IProcedure addStatistic(String str, Object obj) {
        return this;
    }

    public IProcedure begin() {
        return this;
    }

    public IProcedure end() {
        return this;
    }

    public IProcedure end(boolean z) {
        return this;
    }

    public IProcedure event(String str, Map<String, Object> map) {
        return this;
    }

    public boolean isAlive() {
        return false;
    }

    public IProcedure parent() {
        return this;
    }

    public IProcedure stage(String str, long j) {
        return this;
    }

    public String topic() {
        return "default";
    }

    public String topicSession() {
        return "no-session";
    }

    Default() {
    }
}
