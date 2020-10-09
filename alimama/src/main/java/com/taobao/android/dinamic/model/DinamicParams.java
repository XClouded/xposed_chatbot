package com.taobao.android.dinamic.model;

import com.taobao.android.dinamic.view.ViewResult;

public class DinamicParams {
    private Object currentData;
    private Object dinamicContext;
    private String module;
    private Object originalData;
    private ViewResult viewResult;

    public Object getCurrentData() {
        return this.currentData;
    }

    public void setCurrentData(Object obj) {
        this.currentData = obj;
    }

    public String getModule() {
        return this.module;
    }

    public ViewResult getViewResult() {
        return this.viewResult;
    }

    public Object getDinamicContext() {
        return this.dinamicContext;
    }

    public Object getOriginalData() {
        return this.originalData;
    }

    private DinamicParams(Builder builder) {
        this.module = "default";
        this.module = builder.module;
        this.currentData = builder.currentData;
        this.dinamicContext = builder.dinamicContext;
        this.originalData = builder.originalData;
        this.viewResult = builder.viewResult;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Object currentData;
        /* access modifiers changed from: private */
        public Object dinamicContext;
        /* access modifiers changed from: private */
        public String module = "default";
        /* access modifiers changed from: private */
        public Object originalData;
        /* access modifiers changed from: private */
        public ViewResult viewResult;

        public Builder withModule(String str) {
            this.module = str;
            return this;
        }

        public Builder withViewResult(ViewResult viewResult2) {
            this.viewResult = viewResult2;
            return this;
        }

        public Builder withDinamicContext(Object obj) {
            this.dinamicContext = obj;
            return this;
        }

        public Builder withOriginalData(Object obj) {
            this.originalData = obj;
            return this;
        }

        public Builder withCurrentData(Object obj) {
            this.currentData = obj;
            return this;
        }

        public DinamicParams build() {
            return new DinamicParams(this);
        }
    }
}
