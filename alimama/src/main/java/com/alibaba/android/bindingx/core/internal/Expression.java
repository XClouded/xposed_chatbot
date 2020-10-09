package com.alibaba.android.bindingx.core.internal;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.LogProxy;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class Expression {
    private static final Map<String, Object> sFunctionCache = new HashMap(32);
    private JSONObject root;

    @Nullable
    static Expression createFrom(ExpressionPair expressionPair) {
        if (expressionPair == null) {
            return null;
        }
        if (!TextUtils.isEmpty(expressionPair.transformed)) {
            return new Expression(expressionPair.transformed);
        }
        if (expressionPair.compiledTransformed != null) {
            return new Expression(expressionPair.compiledTransformed);
        }
        return null;
    }

    Expression(String str) {
        try {
            this.root = (JSONObject) new JSONTokener(str).nextValue();
        } catch (Throwable th) {
            LogProxy.e("[Expression] expression is illegal. \n ", th);
        }
    }

    Expression(JSONObject jSONObject) {
        this.root = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public Object execute(Map<String, Object> map) throws IllegalArgumentException, JSONException {
        return execute(this.root, map);
    }

    private double toNumber(Object obj) {
        if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1.0d : 0.0d;
        }
        return ((Double) obj).doubleValue();
    }

    private boolean toBoolean(Object obj) {
        if (obj instanceof String) {
            return "".equals(obj);
        }
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue() != 0.0d;
        }
        return ((Boolean) obj).booleanValue();
    }

    private String toString(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? "true" : "false";
        }
        if (obj instanceof Double) {
            return Double.toString(((Double) obj).doubleValue());
        }
        return (String) obj;
    }

    private boolean equal(Object obj, Object obj2) {
        if ((obj instanceof JSObjectInterface) && (obj2 instanceof JSObjectInterface)) {
            return obj == obj2;
        }
        if ((obj instanceof String) && (obj2 instanceof String)) {
            return obj.equals(obj2);
        }
        if (!(obj instanceof Boolean) || !(obj2 instanceof Boolean)) {
            if (toNumber(obj) == toNumber(obj2)) {
                return true;
            }
            return false;
        } else if (toBoolean(obj) == toBoolean(obj2)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean strictlyEqual(Object obj, Object obj2) {
        if ((obj instanceof JSObjectInterface) && !(obj2 instanceof JSObjectInterface)) {
            return false;
        }
        if ((obj instanceof Boolean) && !(obj2 instanceof Boolean)) {
            return false;
        }
        if ((obj instanceof Double) && !(obj2 instanceof Double)) {
            return false;
        }
        if ((!(obj instanceof String) || (obj2 instanceof String)) && obj == obj2) {
            return true;
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r4v0 */
    /* JADX WARNING: type inference failed for: r4v1, types: [int] */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: type inference failed for: r4v11 */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object execute(org.json.JSONObject r7, java.util.Map<java.lang.String, java.lang.Object> r8) throws java.lang.IllegalArgumentException, org.json.JSONException {
        /*
            r6 = this;
            java.lang.String r0 = "type"
            java.lang.String r0 = r7.getString(r0)
            java.lang.String r1 = "children"
            org.json.JSONArray r1 = r7.optJSONArray(r1)
            int r2 = r0.hashCode()
            r3 = 2
            r4 = 0
            r5 = 1
            switch(r2) {
                case -1746151498: goto L_0x0111;
                case 33: goto L_0x0106;
                case 37: goto L_0x00fb;
                case 42: goto L_0x00f0;
                case 43: goto L_0x00e6;
                case 45: goto L_0x00dc;
                case 47: goto L_0x00d1;
                case 60: goto L_0x00c6;
                case 62: goto L_0x00bb;
                case 63: goto L_0x00b1;
                case 1084: goto L_0x00a5;
                case 1216: goto L_0x0099;
                case 1344: goto L_0x008d;
                case 1921: goto L_0x0081;
                case 1952: goto L_0x0075;
                case 1983: goto L_0x0069;
                case 3968: goto L_0x005c;
                case 33665: goto L_0x0050;
                case 60573: goto L_0x0044;
                case 189157634: goto L_0x0039;
                case 375032009: goto L_0x002e;
                case 1074430782: goto L_0x0023;
                case 1816238983: goto L_0x0018;
                default: goto L_0x0016;
            }
        L_0x0016:
            goto L_0x011b
        L_0x0018:
            java.lang.String r2 = "BooleanLiteral"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 2
            goto L_0x011c
        L_0x0023:
            java.lang.String r2 = "StringLiteral"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 0
            goto L_0x011c
        L_0x002e:
            java.lang.String r2 = "Identifier"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 3
            goto L_0x011c
        L_0x0039:
            java.lang.String r2 = "NumericLiteral"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 1
            goto L_0x011c
        L_0x0044:
            java.lang.String r2 = "==="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 17
            goto L_0x011c
        L_0x0050:
            java.lang.String r2 = "!=="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 19
            goto L_0x011c
        L_0x005c:
            java.lang.String r2 = "||"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 21
            goto L_0x011c
        L_0x0069:
            java.lang.String r2 = ">="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 14
            goto L_0x011c
        L_0x0075:
            java.lang.String r2 = "=="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 16
            goto L_0x011c
        L_0x0081:
            java.lang.String r2 = "<="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 15
            goto L_0x011c
        L_0x008d:
            java.lang.String r2 = "**"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 11
            goto L_0x011c
        L_0x0099:
            java.lang.String r2 = "&&"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 20
            goto L_0x011c
        L_0x00a5:
            java.lang.String r2 = "!="
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 18
            goto L_0x011c
        L_0x00b1:
            java.lang.String r2 = "?"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 5
            goto L_0x011c
        L_0x00bb:
            java.lang.String r2 = ">"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 12
            goto L_0x011c
        L_0x00c6:
            java.lang.String r2 = "<"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 13
            goto L_0x011c
        L_0x00d1:
            java.lang.String r2 = "/"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 9
            goto L_0x011c
        L_0x00dc:
            java.lang.String r2 = "-"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 7
            goto L_0x011c
        L_0x00e6:
            java.lang.String r2 = "+"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 6
            goto L_0x011c
        L_0x00f0:
            java.lang.String r2 = "*"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 8
            goto L_0x011c
        L_0x00fb:
            java.lang.String r2 = "%"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 10
            goto L_0x011c
        L_0x0106:
            java.lang.String r2 = "!"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 22
            goto L_0x011c
        L_0x0111:
            java.lang.String r2 = "CallExpression"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x011b
            r0 = 4
            goto L_0x011c
        L_0x011b:
            r0 = -1
        L_0x011c:
            switch(r0) {
                case 0: goto L_0x0384;
                case 1: goto L_0x0379;
                case 2: goto L_0x036e;
                case 3: goto L_0x035d;
                case 4: goto L_0x032b;
                case 5: goto L_0x0309;
                case 6: goto L_0x02eb;
                case 7: goto L_0x02cd;
                case 8: goto L_0x02ae;
                case 9: goto L_0x0290;
                case 10: goto L_0x0272;
                case 11: goto L_0x0251;
                case 12: goto L_0x022f;
                case 13: goto L_0x020d;
                case 14: goto L_0x01eb;
                case 15: goto L_0x01c9;
                case 16: goto L_0x01b0;
                case 17: goto L_0x0197;
                case 18: goto L_0x017d;
                case 19: goto L_0x0163;
                case 20: goto L_0x014b;
                case 21: goto L_0x0133;
                case 22: goto L_0x0121;
                default: goto L_0x011f;
            }
        L_0x011f:
            r7 = 0
            return r7
        L_0x0121:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            boolean r7 = r6.toBoolean(r7)
            r7 = r7 ^ r5
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x0133:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            boolean r0 = r6.toBoolean(r7)
            if (r0 == 0) goto L_0x0142
            return r7
        L_0x0142:
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            return r7
        L_0x014b:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            boolean r0 = r6.toBoolean(r7)
            if (r0 != 0) goto L_0x015a
            return r7
        L_0x015a:
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            return r7
        L_0x0163:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            org.json.JSONObject r0 = r1.getJSONObject(r5)
            java.lang.Object r8 = r6.execute(r0, r8)
            boolean r7 = r6.strictlyEqual(r7, r8)
            r7 = r7 ^ r5
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x017d:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            org.json.JSONObject r0 = r1.getJSONObject(r5)
            java.lang.Object r8 = r6.execute(r0, r8)
            boolean r7 = r6.equal(r7, r8)
            r7 = r7 ^ r5
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x0197:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            org.json.JSONObject r0 = r1.getJSONObject(r5)
            java.lang.Object r8 = r6.execute(r0, r8)
            boolean r7 = r6.strictlyEqual(r7, r8)
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x01b0:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            org.json.JSONObject r0 = r1.getJSONObject(r5)
            java.lang.Object r8 = r6.execute(r0, r8)
            boolean r7 = r6.equal(r7, r8)
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x01c9:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            int r0 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r0 > 0) goto L_0x01e6
            r4 = 1
        L_0x01e6:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r4)
            return r7
        L_0x01eb:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            int r0 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r0 < 0) goto L_0x0208
            r4 = 1
        L_0x0208:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r4)
            return r7
        L_0x020d:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            int r0 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r0 >= 0) goto L_0x022a
            r4 = 1
        L_0x022a:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r4)
            return r7
        L_0x022f:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            int r0 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r0 <= 0) goto L_0x024c
            r4 = 1
        L_0x024c:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r4)
            return r7
        L_0x0251:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r7 = java.lang.Math.pow(r2, r7)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            return r7
        L_0x0272:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r2 = r2 % r7
            java.lang.Double r7 = java.lang.Double.valueOf(r2)
            return r7
        L_0x0290:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r2 = r2 / r7
            java.lang.Double r7 = java.lang.Double.valueOf(r2)
            return r7
        L_0x02ae:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r2 = r2 * r7
            java.lang.Double r7 = java.lang.Double.valueOf(r2)
            return r7
        L_0x02cd:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r2 = r2 - r7
            java.lang.Double r7 = java.lang.Double.valueOf(r2)
            return r7
        L_0x02eb:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r2 = r6.toNumber(r7)
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            double r7 = r6.toNumber(r7)
            double r2 = r2 + r7
            java.lang.Double r7 = java.lang.Double.valueOf(r2)
            return r7
        L_0x0309:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            boolean r7 = r7.booleanValue()
            if (r7 == 0) goto L_0x0322
            org.json.JSONObject r7 = r1.getJSONObject(r5)
            java.lang.Object r7 = r6.execute(r7, r8)
            return r7
        L_0x0322:
            org.json.JSONObject r7 = r1.getJSONObject(r3)
            java.lang.Object r7 = r6.execute(r7, r8)
            return r7
        L_0x032b:
            org.json.JSONObject r7 = r1.getJSONObject(r4)
            java.lang.Object r7 = r6.execute(r7, r8)
            com.alibaba.android.bindingx.core.internal.JSFunctionInterface r7 = (com.alibaba.android.bindingx.core.internal.JSFunctionInterface) r7
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            org.json.JSONObject r1 = r1.getJSONObject(r5)
            java.lang.String r2 = "children"
            org.json.JSONArray r1 = r1.getJSONArray(r2)
        L_0x0344:
            int r2 = r1.length()
            if (r4 >= r2) goto L_0x0358
            org.json.JSONObject r2 = r1.getJSONObject(r4)
            java.lang.Object r2 = r6.execute(r2, r8)     // Catch:{ Throwable -> 0x038b }
            r0.add(r2)
            int r4 = r4 + 1
            goto L_0x0344
        L_0x0358:
            java.lang.Object r7 = r7.execute(r0)
            return r7
        L_0x035d:
            java.lang.String r0 = "value"
            java.lang.String r7 = r7.getString(r0)
            java.lang.Object r8 = r8.get(r7)
            if (r8 != 0) goto L_0x036d
            java.lang.Object r8 = r6.findIdentifier(r7)
        L_0x036d:
            return r8
        L_0x036e:
            java.lang.String r8 = "value"
            boolean r7 = r7.getBoolean(r8)
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x0379:
            java.lang.String r8 = "value"
            double r7 = r7.getDouble(r8)
            java.lang.Double r7 = java.lang.Double.valueOf(r7)
            return r7
        L_0x0384:
            java.lang.String r8 = "value"
            java.lang.String r7 = r7.getString(r8)
            return r7
        L_0x038b:
            r7 = move-exception
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.internal.Expression.execute(org.json.JSONObject, java.util.Map):java.lang.Object");
    }

    @Nullable
    private Object findIdentifier(@NonNull String str) {
        Object findIdentifier = findIdentifier(JSMath.class, str);
        if (findIdentifier == null) {
            findIdentifier = findIdentifier(TimingFunctions.class, str);
        }
        if (findIdentifier == null && LogProxy.sEnableLog) {
            LogProxy.e("can not find inentifier: " + str);
        }
        return findIdentifier;
    }

    @Nullable
    private static Object findIdentifier(@NonNull Class<?> cls, @NonNull String str) {
        try {
            Object obj = sFunctionCache.get(str);
            if (obj == null) {
                obj = cls.getMethod(str, new Class[0]).invoke((Object) null, new Object[0]);
            }
            if (obj != null) {
                sFunctionCache.put(str, obj);
            }
            return obj;
        } catch (Throwable unused) {
            return null;
        }
    }
}
