package com.alimamaunion.base.safejson;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u000f\b\u0016\u0012\u0006\u0010\u0006\u001a\u00020\u0001¢\u0006\u0002\u0010\u0007J\u0006\u0010\t\u001a\u00020\u0001J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u000bH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\u000bH\u0016J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000bH\u0016J\b\u0010\u0011\u001a\u00020\u0004H\u0016R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/alimamaunion/base/safejson/SafeJSONArray;", "Lorg/json/JSONArray;", "()V", "data", "", "(Ljava/lang/String;)V", "array", "(Lorg/json/JSONArray;)V", "mJSONArray", "getWrappedJsonArray", "length", "", "optJSONArray", "index", "optJSONObject", "Lcom/alimamaunion/base/safejson/SafeJSONObject;", "optString", "toString", "safejson_release"}, k = 1, mv = {1, 1, 11})
/* compiled from: SafeJSONArray.kt */
public final class SafeJSONArray extends JSONArray {
    private JSONArray mJSONArray;

    public SafeJSONArray() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SafeJSONArray(@NotNull String str) throws JSONException {
        super(str);
        Intrinsics.checkParameterIsNotNull(str, "data");
    }

    public SafeJSONArray(@NotNull JSONArray jSONArray) {
        Intrinsics.checkParameterIsNotNull(jSONArray, "array");
        this.mJSONArray = jSONArray;
    }

    public int length() {
        JSONArray jSONArray = this.mJSONArray;
        return jSONArray != null ? jSONArray.length() : super.length();
    }

    @NotNull
    public String optString(int i) {
        String optString;
        JSONArray jSONArray = this.mJSONArray;
        if (jSONArray != null && (optString = jSONArray.optString(i)) != null) {
            return optString;
        }
        String optString2 = super.optString(i);
        Intrinsics.checkExpressionValueIsNotNull(optString2, "super.optString(index)");
        return optString2;
    }

    @NotNull
    public SafeJSONArray optJSONArray(int i) {
        JSONArray jSONArray;
        JSONArray jSONArray2 = this.mJSONArray;
        if (jSONArray2 == null || (jSONArray = jSONArray2.optJSONArray(i)) == null) {
            jSONArray = super.optJSONArray(i);
        }
        return jSONArray != null ? new SafeJSONArray(jSONArray) : new SafeJSONArray();
    }

    @NotNull
    public SafeJSONObject optJSONObject(int i) {
        JSONObject jSONObject;
        JSONArray jSONArray = this.mJSONArray;
        if (jSONArray == null || (jSONObject = jSONArray.optJSONObject(i)) == null) {
            jSONObject = super.optJSONObject(i);
        }
        return jSONObject != null ? new SafeJSONObject(jSONObject) : new SafeJSONObject();
    }

    @NotNull
    public String toString() {
        String jSONArray;
        JSONArray jSONArray2 = this.mJSONArray;
        if (jSONArray2 != null && (jSONArray = jSONArray2.toString()) != null) {
            return jSONArray;
        }
        String jSONArray3 = super.toString();
        Intrinsics.checkExpressionValueIsNotNull(jSONArray3, "super.toString()");
        return jSONArray3;
    }

    @NotNull
    public final JSONArray getWrappedJsonArray() {
        JSONArray jSONArray = this.mJSONArray;
        return jSONArray != null ? jSONArray : new SafeJSONArray();
    }
}
