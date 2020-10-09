package com.alibaba.android.bindingx.core.internal;

import java.util.Collections;
import java.util.Map;

final class ExpressionHolder {
    Map<String, Object> config;
    String eventType;
    ExpressionPair expressionPair;
    String prop;
    String targetInstanceId;
    String targetRef;

    ExpressionHolder(String str, String str2, ExpressionPair expressionPair2, String str3, String str4, Map<String, Object> map) {
        this.targetRef = str;
        this.targetInstanceId = str2;
        this.expressionPair = expressionPair2;
        this.prop = str3;
        this.eventType = str4;
        if (map == null) {
            this.config = Collections.emptyMap();
        } else {
            this.config = map;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExpressionHolder expressionHolder = (ExpressionHolder) obj;
        if (this.targetRef == null ? expressionHolder.targetRef != null : !this.targetRef.equals(expressionHolder.targetRef)) {
            return false;
        }
        if (this.expressionPair == null ? expressionHolder.expressionPair != null : !this.expressionPair.equals(expressionHolder.expressionPair)) {
            return false;
        }
        if (this.prop == null ? expressionHolder.prop != null : !this.prop.equals(expressionHolder.prop)) {
            return false;
        }
        if (this.eventType == null ? expressionHolder.eventType != null : !this.eventType.equals(expressionHolder.eventType)) {
            return false;
        }
        if (this.config != null) {
            return this.config.equals(expressionHolder.config);
        }
        if (expressionHolder.config == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((((((this.targetRef != null ? this.targetRef.hashCode() : 0) * 31) + (this.expressionPair != null ? this.expressionPair.hashCode() : 0)) * 31) + (this.prop != null ? this.prop.hashCode() : 0)) * 31) + (this.eventType != null ? this.eventType.hashCode() : 0)) * 31;
        if (this.config != null) {
            i = this.config.hashCode();
        }
        return hashCode + i;
    }
}
