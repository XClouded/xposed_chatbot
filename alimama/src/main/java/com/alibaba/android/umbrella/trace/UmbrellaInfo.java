package com.alibaba.android.umbrella.trace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UmbrellaInfo implements Serializable {
    public Map<String, String> args;
    @JSONField(name = "sceneName")
    public String childBizName;
    public String featureType;
    public String invokePage;
    public String invokePageUrl;
    @JSONField(name = "bizName")
    public String mainBizName;
    public String samplingRate;
    @JSONField(name = "serviceId")
    public String tagId;
    @JSONField(name = "version")
    public String tagVersion;
    public String umbVersion;

    private UmbrellaInfo() {
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public static final class UmbrellaBuilder {
        private Map<String, String> args;
        private String childBizName;
        private String featureType;
        private String invokePage;
        private String invokePageUrl;
        private String mainBizName;
        private String samplingRate;
        private String tagId;
        private String tagVersion;
        private String umbVersion;

        public UmbrellaBuilder(String str, String str2, String str3, String str4, String str5) {
            this.tagId = str;
            this.tagVersion = str2;
            this.featureType = str3;
            this.mainBizName = str4;
            this.childBizName = str5;
        }

        public UmbrellaBuilder setVersion(String str) {
            this.tagVersion = str;
            return this;
        }

        public UmbrellaBuilder setUmbVersion(String str) {
            this.umbVersion = str;
            return this;
        }

        public UmbrellaBuilder setSamplingRate(String str) {
            this.samplingRate = str;
            return this;
        }

        public UmbrellaBuilder setInvokePage(String str) {
            this.invokePage = str;
            return this;
        }

        public UmbrellaBuilder setInvokePageUrl(String str) {
            this.invokePageUrl = str;
            return this;
        }

        public UmbrellaBuilder setParams(Map<String, String> map) {
            if (map == null) {
                return this;
            }
            if (this.args == null) {
                this.args = new HashMap();
            }
            this.args.putAll(map);
            return this;
        }

        public UmbrellaInfo build() {
            UmbrellaInfo umbrellaInfo = new UmbrellaInfo();
            umbrellaInfo.tagId = this.tagId;
            umbrellaInfo.tagVersion = this.tagVersion;
            umbrellaInfo.mainBizName = this.mainBizName;
            umbrellaInfo.childBizName = this.childBizName;
            umbrellaInfo.featureType = this.featureType;
            umbrellaInfo.samplingRate = this.samplingRate;
            umbrellaInfo.invokePage = this.invokePage;
            umbrellaInfo.invokePageUrl = this.invokePageUrl;
            umbrellaInfo.args = this.args;
            umbrellaInfo.umbVersion = this.umbVersion;
            return umbrellaInfo;
        }
    }
}
