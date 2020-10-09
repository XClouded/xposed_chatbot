package com.taobao.monitor.procedure.model;

import java.util.HashMap;
import java.util.Map;

public class Biz {
    private Map<String, Object> abTest;
    private final String bizID;
    private Map<String, Object> properties;
    private Map<String, Object> stages;

    public Biz(String str, Map<String, Object> map) {
        this.bizID = str;
        this.properties = map;
    }

    public String bizID() {
        return this.bizID;
    }

    public Map<String, Object> stages() {
        return this.stages;
    }

    public Map<String, Object> abTest() {
        return this.abTest;
    }

    public Map<String, Object> properties() {
        return this.properties;
    }

    public Biz addProperties(Map<String, Object> map) {
        if (map == null) {
            return this;
        }
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        this.properties.putAll(map);
        return this;
    }

    public Biz addProperties(String str, Object obj) {
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        this.properties.put(str, obj);
        return this;
    }

    public Biz addAbTest(Map<String, Object> map) {
        if (map == null) {
            return this;
        }
        if (this.abTest == null) {
            this.abTest = new HashMap();
        }
        this.abTest.putAll(map);
        return this;
    }

    public Biz addStage(Map<String, Object> map) {
        if (map == null) {
            return this;
        }
        if (this.stages == null) {
            this.stages = new HashMap();
        }
        this.stages.putAll(map);
        return this;
    }

    public String toString() {
        return this.bizID;
    }
}
