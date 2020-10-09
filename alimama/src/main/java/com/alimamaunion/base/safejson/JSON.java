package com.alimamaunion.base.safejson;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0001¢\u0006\u0002\u0010\tJ\u0015\u0010\n\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\u0001¢\u0006\u0002\u0010\u000bJ\u0015\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\b\u001a\u00020\u0001¢\u0006\u0002\u0010\u000eJ\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\b\u001a\u00020\u0001¢\u0006\u0002\u0010\u0011J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\b\u001a\u0004\u0018\u00010\u0001J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00012\b\u0010\u0017\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0018\u001a\u00020\u0013J\u0018\u0010\u0014\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0018\u001a\u00020\u0013¨\u0006\u0019"}, d2 = {"Lcom/alimamaunion/base/safejson/JSON;", "", "()V", "checkDouble", "", "d", "toBoolean", "", "value", "(Ljava/lang/Object;)Ljava/lang/Boolean;", "toDouble", "(Ljava/lang/Object;)Ljava/lang/Double;", "toInteger", "", "(Ljava/lang/Object;)Ljava/lang/Integer;", "toLong", "", "(Ljava/lang/Object;)Ljava/lang/Long;", "toString", "", "typeMismatch", "Lorg/json/JSONException;", "indexOrName", "actual", "requiredType", "safejson_release"}, k = 1, mv = {1, 1, 11})
/* compiled from: JSON.kt */
public final class JSON {
    public static final JSON INSTANCE = new JSON();

    private JSON() {
    }

    public final double checkDouble(double d) throws JSONException {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            return d;
        }
        throw new JSONException("Forbidden numeric value: " + d);
    }

    @Nullable
    public final Boolean toBoolean(@NotNull Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "value");
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (!(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        if (StringsKt.equals("true", str, true)) {
            return true;
        }
        return StringsKt.equals("false", str, true) ? false : null;
    }

    @Nullable
    public final Double toDouble(@NotNull Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "value");
        if (obj instanceof Double) {
            return (Double) obj;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (!(obj instanceof String)) {
            return null;
        }
        try {
            return Double.valueOf((String) obj);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    @Nullable
    public final Integer toInteger(@NotNull Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "value");
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (!(obj instanceof String)) {
            return null;
        }
        try {
            return Integer.valueOf((int) Double.parseDouble((String) obj));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    @Nullable
    public final Long toLong(@NotNull Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "value");
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (!(obj instanceof String)) {
            return null;
        }
        try {
            return Long.valueOf((long) Double.parseDouble((String) obj));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    @Nullable
    public final String toString(@Nullable Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    @NotNull
    public final JSONException typeMismatch(@NotNull Object obj, @Nullable Object obj2, @NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(obj, "indexOrName");
        Intrinsics.checkParameterIsNotNull(str, "requiredType");
        if (obj2 == null) {
            throw new JSONException("Value at " + obj + " is null.");
        }
        throw new JSONException("Value " + obj2 + " at " + obj + " of type " + obj2.getClass().getName() + " cannot be converted to " + str);
    }

    @NotNull
    public final JSONException typeMismatch(@Nullable Object obj, @NotNull String str) throws JSONException {
        Intrinsics.checkParameterIsNotNull(str, "requiredType");
        if (obj == null) {
            throw new JSONException("Value is null.");
        }
        throw new JSONException("Value " + obj + " of type " + obj.getClass().getName() + " cannot be converted to " + str);
    }
}
