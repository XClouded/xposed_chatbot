package com.alimamaunion.base.safejson;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u000f\b\u0016\u0012\u0006\u0010\u0006\u001a\u00020\u0001¢\u0006\u0002\u0010\u0007J\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004H\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0006\u0010\u0018\u001a\u00020\u0001J\u0010\u0010\u0019\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0012\u0010\u001a\u001a\u00020\r2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0004H\u0016J\u000e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00040\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0011H\u0016J\u0010\u0010\u001e\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0018\u0010\u001e\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\rH\u0016J\u0010\u0010 \u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0018\u0010 \u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u000fH\u0016J\u0010\u0010!\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0018\u0010!\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0011H\u0016J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010$\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010%\u001a\u00020\u00162\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0018\u0010%\u001a\u00020\u00162\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0016H\u0016J\u0010\u0010&\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0018\u0010&\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0004H\u0016J\u0016\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010)\u001a\u00020#J\u0016\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0000J\u0018\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\rH\u0016J\u0018\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u000fH\u0016J\u0018\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0011H\u0016J\u0018\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0016H\u0016J\u0016\u0010'\u001a\u00020\u00002\u0006\u0010(\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0004J\b\u0010+\u001a\u00020\u0004H\u0016R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lcom/alimamaunion/base/safejson/SafeJSONObject;", "Lorg/json/JSONObject;", "()V", "data", "", "(Ljava/lang/String;)V", "obj", "(Lorg/json/JSONObject;)V", "mJSONObject", "get", "", "name", "getBoolean", "", "getDouble", "", "getInt", "", "getJSONArray", "Lorg/json/JSONArray;", "getJSONObject", "getLong", "", "getString", "getWrappedJsonObject", "has", "isNull", "keys", "", "length", "optBoolean", "fallback", "optDouble", "optInt", "optJSONArray", "Lcom/alimamaunion/base/safejson/SafeJSONArray;", "optJSONObject", "optLong", "optString", "put", "key", "array", "value", "toString", "safejson_release"}, k = 1, mv = {1, 1, 11})
/* compiled from: SafeJSONObject.kt */
public final class SafeJSONObject extends JSONObject {
    private JSONObject mJSONObject;

    public SafeJSONObject() {
        this.mJSONObject = new JSONObject();
    }

    public SafeJSONObject(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "data");
        this.mJSONObject = new JSONObject(str);
    }

    public SafeJSONObject(@NotNull JSONObject jSONObject) {
        Intrinsics.checkParameterIsNotNull(jSONObject, "obj");
        this.mJSONObject = jSONObject;
    }

    @NotNull
    public final SafeJSONObject put(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, "value");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, str2) == null) {
                super.put(str, str2);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public SafeJSONObject put(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, i) == null) {
                super.put(str, i);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public SafeJSONObject put(@NotNull String str, double d) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, d) == null) {
                super.put(str, d);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public final SafeJSONObject put(@NotNull String str, @NotNull SafeJSONArray safeJSONArray) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(safeJSONArray, "array");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, safeJSONArray) == null) {
                super.put(str, safeJSONArray);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public SafeJSONObject put(@NotNull String str, long j) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, j) == null) {
                super.put(str, j);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public SafeJSONObject put(@NotNull String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, z) == null) {
                super.put(str, z);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public final SafeJSONObject put(@NotNull String str, @NotNull SafeJSONObject safeJSONObject) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(safeJSONObject, "obj");
        try {
            JSONObject jSONObject = this.mJSONObject;
            if (jSONObject == null || jSONObject.put(str, safeJSONObject) == null) {
                super.put(str, safeJSONObject);
            }
        } catch (JSONException unused) {
        }
        return this;
    }

    @NotNull
    public Object get(@NotNull String str) throws JSONException {
        Object obj;
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        if (jSONObject == null || (obj = jSONObject.get(str)) == null) {
            obj = super.get(str);
        }
        if (obj != null) {
            return obj;
        }
        throw new JSONException("No value for " + str);
    }

    public int optInt(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return optInt(str, 0);
    }

    public int optInt(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.optInt(str, i) : super.optInt(str, i);
    }

    public int getInt(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        Integer integer = obj != null ? JSON.INSTANCE.toInteger(obj) : null;
        if (integer != null) {
            integer.intValue();
            return integer.intValue();
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "int");
    }

    public long optLong(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return optLong(str, 0);
    }

    public long optLong(@NotNull String str, long j) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.optLong(str, j) : super.optLong(str, j);
    }

    public long getLong(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        Long l = obj != null ? JSON.INSTANCE.toLong(obj) : null;
        if (l != null) {
            l.longValue();
            return l.longValue();
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "long");
    }

    @NotNull
    public String optString(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return optString(str, "");
    }

    @NotNull
    public String optString(@NotNull String str, @NotNull String str2) {
        String optString;
        Intrinsics.checkParameterIsNotNull(str, "name");
        Intrinsics.checkParameterIsNotNull(str2, "fallback");
        JSONObject jSONObject = this.mJSONObject;
        if (jSONObject != null && jSONObject.isNull(str)) {
            return str2;
        }
        JSONObject jSONObject2 = this.mJSONObject;
        if (jSONObject2 != null && (optString = jSONObject2.optString(str, str2)) != null) {
            return optString;
        }
        String optString2 = super.optString(str, str2);
        Intrinsics.checkExpressionValueIsNotNull(optString2, "super.optString(name, fallback)");
        return optString2;
    }

    @NotNull
    public String getString(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        String json = obj != null ? JSON.INSTANCE.toString(obj) : null;
        if (json != null) {
            return json;
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "string");
    }

    @NotNull
    public SafeJSONArray optJSONArray(@NotNull String str) {
        JSONArray jSONArray;
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        if (jSONObject == null || (jSONArray = jSONObject.optJSONArray(str)) == null) {
            jSONArray = super.optJSONArray(str);
        }
        return jSONArray != null ? new SafeJSONArray(jSONArray) : new SafeJSONArray();
    }

    @NotNull
    public JSONArray getJSONArray(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "JSONArray");
    }

    @NotNull
    public SafeJSONObject optJSONObject(@NotNull String str) {
        JSONObject jSONObject;
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject2 = this.mJSONObject;
        if (jSONObject2 == null || (jSONObject = jSONObject2.optJSONObject(str)) == null) {
            jSONObject = super.optJSONObject(str);
        }
        return jSONObject != null ? new SafeJSONObject(jSONObject) : new SafeJSONObject();
    }

    @NotNull
    public JSONObject getJSONObject(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "JSONObject");
    }

    public boolean optBoolean(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return optBoolean(str, false);
    }

    public boolean optBoolean(@NotNull String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.optBoolean(str, z) : super.optBoolean(str, z);
    }

    public boolean getBoolean(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        Boolean bool = obj != null ? JSON.INSTANCE.toBoolean(obj) : null;
        if (bool != null) {
            bool.booleanValue();
            return bool.booleanValue();
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "boolean");
    }

    public double optDouble(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return optDouble(str, DoubleCompanionObject.INSTANCE.getNaN());
    }

    public double optDouble(@NotNull String str, double d) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.optDouble(str, d) : super.optDouble(str, d);
    }

    public double getDouble(@NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Object obj = get(str);
        Double d = obj != null ? JSON.INSTANCE.toDouble(obj) : null;
        if (d != null) {
            d.doubleValue();
            return d.doubleValue();
        }
        throw JSON.INSTANCE.typeMismatch(str, obj, "double");
    }

    public boolean isNull(@Nullable String str) {
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.isNull(str) : super.isNull(str);
    }

    public boolean has(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.has(str) : super.has(str);
    }

    @NotNull
    public Iterator<String> keys() {
        Iterator<String> keys;
        JSONObject jSONObject = this.mJSONObject;
        if (jSONObject != null && (keys = jSONObject.keys()) != null) {
            return keys;
        }
        Iterator<String> keys2 = super.keys();
        Intrinsics.checkExpressionValueIsNotNull(keys2, "super.keys()");
        return keys2;
    }

    @NotNull
    public String toString() {
        String jSONObject;
        JSONObject jSONObject2 = this.mJSONObject;
        if (jSONObject2 != null && (jSONObject = jSONObject2.toString()) != null) {
            return jSONObject;
        }
        String jSONObject3 = super.toString();
        Intrinsics.checkExpressionValueIsNotNull(jSONObject3, "super.toString()");
        return jSONObject3;
    }

    public int length() {
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject.length() : super.length();
    }

    @NotNull
    public final JSONObject getWrappedJsonObject() {
        JSONObject jSONObject = this.mJSONObject;
        return jSONObject != null ? jSONObject : new JSONObject();
    }
}
