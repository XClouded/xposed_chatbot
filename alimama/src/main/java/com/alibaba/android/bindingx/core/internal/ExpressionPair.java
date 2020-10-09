package com.alibaba.android.bindingx.core.internal;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import org.json.JSONObject;

public class ExpressionPair {
    public final JSONObject compiledTransformed;
    public final String origin;
    public final String transformed;

    public ExpressionPair(String str, String str2, JSONObject jSONObject) {
        this.origin = str;
        this.transformed = str2;
        this.compiledTransformed = jSONObject;
    }

    public static ExpressionPair createCompiled(@Nullable String str, @Nullable JSONObject jSONObject) {
        return new ExpressionPair(str, (String) null, jSONObject);
    }

    public static ExpressionPair create(@Nullable String str, @Nullable String str2) {
        return new ExpressionPair(str, str2, (JSONObject) null);
    }

    public static boolean isValid(@Nullable ExpressionPair expressionPair) {
        return expressionPair != null && ((!TextUtils.isEmpty(expressionPair.transformed) && !"{}".equals(expressionPair.transformed)) || expressionPair.compiledTransformed != null);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExpressionPair expressionPair = (ExpressionPair) obj;
        if (this.origin == null ? expressionPair.origin != null : !this.origin.equals(expressionPair.origin)) {
            return false;
        }
        if (this.transformed != null) {
            return this.transformed.equals(expressionPair.transformed);
        }
        if (expressionPair.transformed == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (this.origin != null ? this.origin.hashCode() : 0) * 31;
        if (this.transformed != null) {
            i = this.transformed.hashCode();
        }
        return hashCode + i;
    }
}
