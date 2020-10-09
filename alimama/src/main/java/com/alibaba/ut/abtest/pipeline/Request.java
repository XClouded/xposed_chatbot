package com.alibaba.ut.abtest.pipeline;

import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.util.PreconditionUtils;
import com.alibaba.ut.abtest.pipeline.request.RequestParam;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Request {
    /* access modifiers changed from: private */
    public Map<String, String> headers;
    /* access modifiers changed from: private */
    public RequestMethod method = RequestMethod.GET;
    /* access modifiers changed from: private */
    public RequestParam params;
    /* access modifiers changed from: private */
    public Object requestContext;
    /* access modifiers changed from: private */
    public Class responseClass;
    /* access modifiers changed from: private */
    public Type responseType;
    /* access modifiers changed from: private */
    public String url;

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public RequestParam getParams() {
        return this.params;
    }

    public String getUrl() {
        return this.url;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public Object getRequestContext() {
        return this.requestContext;
    }

    public Class getResponseClass() {
        return this.responseClass;
    }

    public Type getResponseType() {
        return this.responseType;
    }

    public static final class Builder {
        private Request request = new Request();

        public Builder(String str) {
            PreconditionUtils.checkArgument(!TextUtils.isEmpty(str), "Url cannot be empty");
            String unused = this.request.url = str;
        }

        public Builder setParams(RequestParam requestParam) {
            RequestParam unused = this.request.params = requestParam;
            return this;
        }

        public Builder setHeaders(Map<String, String> map) {
            if (this.request.headers == null) {
                Map unused = this.request.headers = new HashMap();
            } else {
                this.request.headers.clear();
            }
            this.request.headers.putAll(map);
            return this;
        }

        public Builder setMethod(RequestMethod requestMethod) {
            RequestMethod unused = this.request.method = requestMethod;
            return this;
        }

        public Builder setRequestContext(Object obj) {
            Object unused = this.request.requestContext = obj;
            return this;
        }

        public Builder setResponseClass(Class cls) {
            Class unused = this.request.responseClass = cls;
            return this;
        }

        public Builder setResponseType(Type type) {
            Type unused = this.request.responseType = type;
            return this;
        }

        public Request build() {
            return this.request;
        }
    }

    public String toString() {
        return super.toString() + " { url=" + getUrl() + ", method=" + getMethod() + ", headers=" + getHeaders() + ", params=" + getParams() + ", requestContext=" + getRequestContext() + "}";
    }
}
