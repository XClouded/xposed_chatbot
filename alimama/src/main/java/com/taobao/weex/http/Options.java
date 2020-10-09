package com.taobao.weex.http;

import java.util.HashMap;
import java.util.Map;

class Options {
    private String body;
    private Map<String, String> headers;
    private String method;
    private int timeout;
    private Type type;
    private String url;

    public enum Type {
        json,
        text,
        jsonp
    }

    private Options(String str, String str2, Map<String, String> map, String str3, Type type2, int i) {
        this.type = Type.text;
        this.timeout = 3000;
        this.method = str;
        this.url = str2;
        this.headers = map;
        this.body = str3;
        this.type = type2;
        this.timeout = i == 0 ? 3000 : i;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    public Type getType() {
        return this.type;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public static class Builder {
        private String body;
        private Map<String, String> headers = new HashMap();
        private String method;
        private int timeout;
        private Type type;
        private String url;

        public Builder setMethod(String str) {
            this.method = str;
            return this;
        }

        public Builder setUrl(String str) {
            this.url = str;
            return this;
        }

        public Builder putHeader(String str, String str2) {
            this.headers.put(str, str2);
            return this;
        }

        public Builder setBody(String str) {
            this.body = str;
            return this;
        }

        public Builder setType(String str) {
            if (Type.json.name().equals(str)) {
                this.type = Type.json;
            } else if (Type.jsonp.name().equals(str)) {
                this.type = Type.jsonp;
            } else {
                this.type = Type.text;
            }
            return this;
        }

        public Builder setType(Type type2) {
            this.type = type2;
            return this;
        }

        public Builder setTimeout(int i) {
            this.timeout = i;
            return this;
        }

        public Options createOptions() {
            return new Options(this.method, this.url, this.headers, this.body, this.type, this.timeout);
        }
    }
}
