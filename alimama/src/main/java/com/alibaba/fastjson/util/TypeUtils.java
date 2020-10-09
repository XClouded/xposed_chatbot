package com.alibaba.fastjson.util;

import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeUtils {
    public static boolean compatibleWithJavaBean = false;
    private static volatile Map<Class, String[]> kotlinIgnores = null;
    private static volatile boolean kotlinIgnores_error = false;
    private static volatile boolean kotlin_class_klass_error = false;
    private static volatile boolean kotlin_error = false;
    private static volatile Constructor kotlin_kclass_constructor = null;
    private static volatile Method kotlin_kclass_getConstructors = null;
    private static volatile Method kotlin_kfunction_getParameters = null;
    private static volatile Method kotlin_kparameter_getName = null;
    private static volatile Class kotlin_metadata = null;
    private static volatile boolean kotlin_metadata_error = false;
    private static final ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap(36, 0.75f, 1);
    private static boolean setAccessibleEnable = true;

    public static boolean isKotlin(Class cls) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable unused) {
                kotlin_metadata_error = true;
            }
        }
        if (kotlin_metadata == null) {
            return false;
        }
        return cls.isAnnotationPresent(kotlin_metadata);
    }

    private static boolean isKotlinIgnore(Class cls, String str) {
        String[] strArr;
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.put(Class.forName("kotlin.ranges.CharRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.IntRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.LongRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedFloatRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedDoubleRange"), new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = hashMap;
            } catch (Throwable unused) {
                kotlinIgnores_error = true;
            }
        }
        if (kotlinIgnores == null || (strArr = kotlinIgnores.get(cls)) == null || Arrays.binarySearch(strArr, str) < 0) {
            return false;
        }
        return true;
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        if (kotlin_kclass_constructor == null && !kotlin_class_klass_error) {
            try {
                Class<?> cls2 = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlin_kclass_constructor = cls2.getConstructor(new Class[]{Class.class});
                kotlin_kclass_getConstructors = cls2.getMethod("getConstructors", new Class[0]);
                kotlin_kfunction_getParameters = Class.forName("kotlin.reflect.KFunction").getMethod("getParameters", new Class[0]);
                kotlin_kparameter_getName = Class.forName("kotlin.reflect.KParameter").getMethod("getName", new Class[0]);
            } catch (Throwable unused) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null || kotlin_error) {
            return null;
        }
        try {
            Iterator it = ((Iterable) kotlin_kclass_getConstructors.invoke(kotlin_kclass_constructor.newInstance(new Object[]{cls}), new Object[0])).iterator();
            Object obj = null;
            while (it.hasNext()) {
                Object next = it.next();
                List list = (List) kotlin_kfunction_getParameters.invoke(next, new Object[0]);
                if (obj == null || list.size() != 0) {
                    obj = next;
                }
                it.hasNext();
            }
            List list2 = (List) kotlin_kfunction_getParameters.invoke(obj, new Object[0]);
            String[] strArr = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                strArr[i] = (String) kotlin_kparameter_getName.invoke(list2.get(i), new Object[0]);
            }
            return strArr;
        } catch (Throwable unused2) {
            kotlin_error = true;
            return null;
        }
    }

    public static final String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static final Byte castToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        }
        throw new JSONException("can not cast to byte, value : " + obj);
    }

    public static final Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0) {
                return null;
            }
            if (str.length() == 1) {
                return Character.valueOf(str.charAt(0));
            }
            throw new JSONException("can not cast to byte, value : " + obj);
        }
        throw new JSONException("can not cast to byte, value : " + obj);
    }

    public static final Short castToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        }
        throw new JSONException("can not cast to short, value : " + obj);
    }

    public static final BigDecimal castToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(obj2)) {
            return null;
        }
        return new BigDecimal(obj2);
    }

    public static final BigInteger castToBigInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(obj2)) {
            return null;
        }
        return new BigInteger(obj2);
    }

    public static final Float castToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(obj2)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(obj2));
        }
        throw new JSONException("can not cast to float, value : " + obj);
    }

    public static final Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(obj2) || "NULL".equals(obj2)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(obj2));
        }
        throw new JSONException("can not cast to double, value : " + obj);
    }

    public static final Date castToDate(Object obj) {
        String str;
        long j;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        long j2 = -1;
        if (obj instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            int scale = bigDecimal.scale();
            if (scale < -100 || scale > 100) {
                j = bigDecimal.longValueExact();
            } else {
                j = bigDecimal.longValue();
            }
            j2 = j;
        } else if (obj instanceof Number) {
            j2 = ((Number) obj).longValue();
        } else if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.indexOf(45) != -1) {
                if (str2.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                    str = JSON.DEFFAULT_DATE_FORMAT;
                } else if (str2.length() == 10) {
                    str = "yyyy-MM-dd";
                } else if (str2.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                    str = "yyyy-MM-dd HH:mm:ss";
                } else {
                    str = (str2.length() == 29 && str2.charAt(26) == ':' && str2.charAt(28) == '0') ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : "yyyy-MM-dd HH:mm:ss.SSS";
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, JSON.defaultLocale);
                simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
                try {
                    return simpleDateFormat.parse(str2);
                } catch (ParseException unused) {
                    throw new JSONException("can not cast to Date, value : " + str2);
                }
            } else if (str2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str2)) {
                return null;
            } else {
                j2 = Long.parseLong(str2);
            }
        }
        if (j2 >= 0) {
            return new Date(j2);
        }
        throw new JSONException("can not cast to Date, value : " + obj);
    }

    public static final Long castToLong(Object obj) {
        Calendar calendar = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            int scale = bigDecimal.scale();
            if (scale < -100 || scale > 100) {
                return Long.valueOf(bigDecimal.longValueExact());
            }
            return Long.valueOf(bigDecimal.longValue());
        } else if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                    return null;
                }
                try {
                    return Long.valueOf(Long.parseLong(str));
                } catch (NumberFormatException unused) {
                    JSONLexer jSONLexer = new JSONLexer(str);
                    if (jSONLexer.scanISO8601DateIfMatch(false)) {
                        calendar = jSONLexer.calendar;
                    }
                    jSONLexer.close();
                    if (calendar != null) {
                        return Long.valueOf(calendar.getTimeInMillis());
                    }
                }
            }
            throw new JSONException("can not cast to long, value : " + obj);
        }
    }

    public static final Integer castToInt(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            int scale = bigDecimal.scale();
            if (scale < -100 || scale > 100) {
                return Integer.valueOf(bigDecimal.intValueExact());
            }
            return Integer.valueOf(bigDecimal.intValue());
        } else if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                    return null;
                }
                return Integer.valueOf(Integer.parseInt(str));
            }
            throw new JSONException("can not cast to int, value : " + obj);
        }
    }

    public static final byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return JSONLexer.decodeFast(str, 0, str.length());
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static final Boolean castToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        boolean z = false;
        if (obj instanceof BigDecimal) {
            if (((BigDecimal) obj).intValueExact() == 1) {
                z = true;
            }
            return Boolean.valueOf(z);
        } else if (obj instanceof Number) {
            if (((Number) obj).intValue() == 1) {
                z = true;
            }
            return Boolean.valueOf(z);
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                    return null;
                }
                if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                    return Boolean.TRUE;
                }
                if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                    return Boolean.FALSE;
                }
            }
            throw new JSONException("can not cast to int, value : " + obj);
        }
    }

    public static final <T> T castToJavaBean(Object obj, Class<T> cls) {
        return cast(obj, cls, ParserConfig.global);
    }

    public static final <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) {
        return cast(obj, cls, parserConfig, 0);
    }

    public static final <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig, int i) {
        T t;
        if (obj == null) {
            return null;
        }
        if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        } else if (cls == obj.getClass()) {
            return obj;
        } else {
            if (!(obj instanceof Map)) {
                int i2 = 0;
                if (cls.isArray()) {
                    if (obj instanceof Collection) {
                        Collection<Object> collection = (Collection) obj;
                        T newInstance = Array.newInstance(cls.getComponentType(), collection.size());
                        for (Object cast : collection) {
                            Array.set(newInstance, i2, cast(cast, cls.getComponentType(), parserConfig));
                            i2++;
                        }
                        return newInstance;
                    } else if (cls == byte[].class) {
                        return castToBytes(obj);
                    }
                }
                if (cls.isAssignableFrom(obj.getClass())) {
                    return obj;
                }
                if (cls == Boolean.TYPE || cls == Boolean.class) {
                    return castToBoolean(obj);
                }
                if (cls == Byte.TYPE || cls == Byte.class) {
                    return castToByte(obj);
                }
                if ((cls == Character.TYPE || cls == Character.class) && (obj instanceof String)) {
                    String str = (String) obj;
                    if (str.length() == 1) {
                        return Character.valueOf(str.charAt(0));
                    }
                }
                if (cls == Short.TYPE || cls == Short.class) {
                    return castToShort(obj);
                }
                if (cls == Integer.TYPE || cls == Integer.class) {
                    return castToInt(obj);
                }
                if (cls == Long.TYPE || cls == Long.class) {
                    return castToLong(obj);
                }
                if (cls == Float.TYPE || cls == Float.class) {
                    return castToFloat(obj);
                }
                if (cls == Double.TYPE || cls == Double.class) {
                    return castToDouble(obj);
                }
                if (cls == String.class) {
                    return castToString(obj);
                }
                if (cls == BigDecimal.class) {
                    return castToBigDecimal(obj);
                }
                if (cls == BigInteger.class) {
                    return castToBigInteger(obj);
                }
                if (cls == Date.class) {
                    return castToDate(obj);
                }
                if (cls.isEnum()) {
                    return castToEnum(obj, cls, parserConfig);
                }
                if (Calendar.class.isAssignableFrom(cls)) {
                    Date castToDate = castToDate(obj);
                    if (cls == Calendar.class) {
                        t = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    } else {
                        try {
                            t = (Calendar) cls.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + cls.getName(), e);
                        }
                    }
                    t.setTime(castToDate);
                    return t;
                }
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (str2.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str2)) {
                        return null;
                    }
                    if (cls == Currency.class) {
                        return Currency.getInstance(str2);
                    }
                }
                throw new JSONException("can not cast to : " + cls.getName());
            } else if (cls == Map.class) {
                return obj;
            } else {
                Map map = (Map) obj;
                if (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                    return castToJavaBean(map, cls, parserConfig, i);
                }
                return obj;
            }
        }
    }

    public static final <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                return Enum.valueOf(cls, str);
            }
            if ((obj instanceof Integer) || (obj instanceof Long)) {
                int intValue = ((Number) obj).intValue();
                T[] enumConstants = cls.getEnumConstants();
                if (intValue < enumConstants.length) {
                    return enumConstants[intValue];
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    public static final <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return cast(obj, (Class) type, parserConfig, 0);
        }
        if (type instanceof ParameterizedType) {
            return cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static final <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        T t;
        Object obj2;
        Object obj3;
        Type rawType = parameterizedType.getRawType();
        if (rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof List) {
                List list = (List) obj;
                int size = list.size();
                T arrayList = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    Object obj4 = list.get(i);
                    if (!(type instanceof Class)) {
                        obj3 = cast(obj4, type, parserConfig);
                    } else if (obj4 == null || obj4.getClass() != JSONObject.class) {
                        obj3 = cast(obj4, (Class) type, parserConfig, 0);
                    } else {
                        obj3 = ((JSONObject) obj4).toJavaObject((Class) type, parserConfig, 0);
                    }
                    arrayList.add(obj3);
                }
                return arrayList;
            }
        }
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == List.class || rawType == ArrayList.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    t = new HashSet();
                } else if (rawType == TreeSet.class) {
                    t = new TreeSet();
                } else {
                    t = new ArrayList();
                }
                for (Object next : (Iterable) obj) {
                    if (!(type2 instanceof Class)) {
                        obj2 = cast(next, type2, parserConfig);
                    } else if (next == null || next.getClass() != JSONObject.class) {
                        obj2 = cast(next, (Class) type2, parserConfig, 0);
                    } else {
                        obj2 = ((JSONObject) next).toJavaObject((Class) type2, parserConfig, 0);
                    }
                    t.add(obj2);
                }
                return t;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type3 = parameterizedType.getActualTypeArguments()[0];
            Type type4 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                T hashMap = new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    hashMap.put(cast(entry.getKey(), type3, parserConfig), cast(entry.getValue(), type4, parserConfig));
                }
                return hashMap;
            }
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
                return null;
            }
        }
        if (parameterizedType.getActualTypeArguments().length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return cast(obj, rawType, parserConfig);
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    public static final <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig) {
        return castToJavaBean(map, cls, parserConfig, 0);
    }

    /* JADX WARNING: type inference failed for: r5v1, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> T castToJavaBean(java.util.Map<java.lang.String, java.lang.Object> r4, java.lang.Class<T> r5, com.alibaba.fastjson.parser.ParserConfig r6, int r7) {
        /*
            java.lang.Class<java.lang.StackTraceElement> r0 = java.lang.StackTraceElement.class
            r1 = 0
            if (r5 != r0) goto L_0x003d
            java.lang.String r5 = "className"
            java.lang.Object r5 = r4.get(r5)     // Catch:{ Exception -> 0x00db }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x00db }
            java.lang.String r6 = "methodName"
            java.lang.Object r6 = r4.get(r6)     // Catch:{ Exception -> 0x00db }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x00db }
            java.lang.String r7 = "fileName"
            java.lang.Object r7 = r4.get(r7)     // Catch:{ Exception -> 0x00db }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Exception -> 0x00db }
            java.lang.String r0 = "lineNumber"
            java.lang.Object r4 = r4.get(r0)     // Catch:{ Exception -> 0x00db }
            java.lang.Number r4 = (java.lang.Number) r4     // Catch:{ Exception -> 0x00db }
            if (r4 != 0) goto L_0x0028
            goto L_0x0037
        L_0x0028:
            boolean r0 = r4 instanceof java.math.BigDecimal     // Catch:{ Exception -> 0x00db }
            if (r0 == 0) goto L_0x0033
            java.math.BigDecimal r4 = (java.math.BigDecimal) r4     // Catch:{ Exception -> 0x00db }
            int r1 = r4.intValueExact()     // Catch:{ Exception -> 0x00db }
            goto L_0x0037
        L_0x0033:
            int r1 = r4.intValue()     // Catch:{ Exception -> 0x00db }
        L_0x0037:
            java.lang.StackTraceElement r4 = new java.lang.StackTraceElement     // Catch:{ Exception -> 0x00db }
            r4.<init>(r5, r6, r7, r1)     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x003d:
            java.lang.String r0 = "@type"
            java.lang.Object r0 = r4.get(r0)     // Catch:{ Exception -> 0x00db }
            boolean r2 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x00db }
            r3 = 0
            if (r2 == 0) goto L_0x0076
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x00db }
            if (r6 != 0) goto L_0x004e
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.global     // Catch:{ Exception -> 0x00db }
        L_0x004e:
            java.lang.Class r2 = r6.checkAutoType(r0, r3, r7)     // Catch:{ Exception -> 0x00db }
            if (r2 == 0) goto L_0x005f
            boolean r0 = r2.equals(r5)     // Catch:{ Exception -> 0x00db }
            if (r0 != 0) goto L_0x0076
            java.lang.Object r4 = castToJavaBean(r4, r2, r6, r7)     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x005f:
            java.lang.ClassNotFoundException r4 = new java.lang.ClassNotFoundException     // Catch:{ Exception -> 0x00db }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00db }
            r5.<init>()     // Catch:{ Exception -> 0x00db }
            r5.append(r0)     // Catch:{ Exception -> 0x00db }
            java.lang.String r6 = " not found"
            r5.append(r6)     // Catch:{ Exception -> 0x00db }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x00db }
            r4.<init>(r5)     // Catch:{ Exception -> 0x00db }
            throw r4     // Catch:{ Exception -> 0x00db }
        L_0x0076:
            boolean r7 = r5.isInterface()     // Catch:{ Exception -> 0x00db }
            if (r7 == 0) goto L_0x00b0
            boolean r7 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x00db }
            if (r7 == 0) goto L_0x0083
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4     // Catch:{ Exception -> 0x00db }
            goto L_0x0089
        L_0x0083:
            com.alibaba.fastjson.JSONObject r7 = new com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x00db }
            r7.<init>((java.util.Map<java.lang.String, java.lang.Object>) r4)     // Catch:{ Exception -> 0x00db }
            r4 = r7
        L_0x0089:
            if (r6 != 0) goto L_0x008f
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance()     // Catch:{ Exception -> 0x00db }
        L_0x008f:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r6 = r6.getDeserializer(r5)     // Catch:{ Exception -> 0x00db }
            if (r6 == 0) goto L_0x009e
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r4)     // Catch:{ Exception -> 0x00db }
            java.lang.Object r4 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r4, r5)     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x009e:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x00db }
            java.lang.ClassLoader r6 = r6.getContextClassLoader()     // Catch:{ Exception -> 0x00db }
            r7 = 1
            java.lang.Class[] r7 = new java.lang.Class[r7]     // Catch:{ Exception -> 0x00db }
            r7[r1] = r5     // Catch:{ Exception -> 0x00db }
            java.lang.Object r4 = java.lang.reflect.Proxy.newProxyInstance(r6, r7, r4)     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x00b0:
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            if (r5 != r7) goto L_0x00bd
            boolean r7 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x00db }
            if (r7 == 0) goto L_0x00bd
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x00bd:
            if (r6 != 0) goto L_0x00c1
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.global     // Catch:{ Exception -> 0x00db }
        L_0x00c1:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r6.getDeserializer(r5)     // Catch:{ Exception -> 0x00db }
            boolean r7 = r5 instanceof com.alibaba.fastjson.parser.JavaBeanDeserializer     // Catch:{ Exception -> 0x00db }
            if (r7 == 0) goto L_0x00cc
            r3 = r5
            com.alibaba.fastjson.parser.JavaBeanDeserializer r3 = (com.alibaba.fastjson.parser.JavaBeanDeserializer) r3     // Catch:{ Exception -> 0x00db }
        L_0x00cc:
            if (r3 == 0) goto L_0x00d3
            java.lang.Object r4 = r3.createInstance((java.util.Map<java.lang.String, java.lang.Object>) r4, (com.alibaba.fastjson.parser.ParserConfig) r6)     // Catch:{ Exception -> 0x00db }
            return r4
        L_0x00d3:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ Exception -> 0x00db }
            java.lang.String r5 = "can not get javaBeanDeserializer"
            r4.<init>(r5)     // Catch:{ Exception -> 0x00db }
            throw r4     // Catch:{ Exception -> 0x00db }
        L_0x00db:
            r4 = move-exception
            com.alibaba.fastjson.JSONException r5 = new com.alibaba.fastjson.JSONException
            java.lang.String r6 = r4.getMessage()
            r5.<init>(r6, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.castToJavaBean(java.util.Map, java.lang.Class, com.alibaba.fastjson.parser.ParserConfig, int):java.lang.Object");
    }

    static {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        mappings.put("java.util.HashMap", HashMap.class);
        mappings.put("java.util.TreeMap", TreeMap.class);
        mappings.put("java.util.Date", Date.class);
        mappings.put("com.alibaba.fastjson.JSONObject", JSONObject.class);
        mappings.put("java.util.concurrent.ConcurrentHashMap", ConcurrentHashMap.class);
        mappings.put("java.text.SimpleDateFormat", SimpleDateFormat.class);
        mappings.put("java.lang.StackTraceElement", StackTraceElement.class);
        mappings.put("java.lang.RuntimeException", RuntimeException.class);
    }

    public static Class<?> getClassFromMapping(String str) {
        return (Class) mappings.get(str);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader) {
        return loadClass(str, classLoader, false);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:(4:23|24|(2:26|27)|30)|31|32|(3:35|(2:37|38)|41)|42|43|(3:44|45|46)) */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        return r1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x006b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x0086 */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x007d A[SYNTHETIC, Splitter:B:37:0x007d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Class<?> loadClass(java.lang.String r6, java.lang.ClassLoader r7, boolean r8) {
        /*
            r0 = 0
            if (r6 == 0) goto L_0x00a9
            int r1 = r6.length()
            if (r1 != 0) goto L_0x000b
            goto L_0x00a9
        L_0x000b:
            int r1 = r6.length()
            r2 = 256(0x100, float:3.59E-43)
            if (r1 >= r2) goto L_0x0092
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r1 = mappings
            java.lang.Object r1 = r1.get(r6)
            java.lang.Class r1 = (java.lang.Class) r1
            if (r1 == 0) goto L_0x001e
            return r1
        L_0x001e:
            r2 = 0
            char r3 = r6.charAt(r2)
            r4 = 91
            r5 = 1
            if (r3 != r4) goto L_0x003c
            java.lang.String r6 = r6.substring(r5)
            java.lang.Class r6 = loadClass(r6, r7, r2)
            if (r6 != 0) goto L_0x0033
            return r0
        L_0x0033:
            java.lang.Object r6 = java.lang.reflect.Array.newInstance(r6, r2)
            java.lang.Class r6 = r6.getClass()
            return r6
        L_0x003c:
            java.lang.String r0 = "L"
            boolean r0 = r6.startsWith(r0)
            if (r0 == 0) goto L_0x005a
            java.lang.String r0 = ";"
            boolean r0 = r6.endsWith(r0)
            if (r0 == 0) goto L_0x005a
            int r8 = r6.length()
            int r8 = r8 - r5
            java.lang.String r6 = r6.substring(r5, r8)
            java.lang.Class r6 = loadClass(r6, r7, r2)
            return r6
        L_0x005a:
            if (r7 == 0) goto L_0x006b
            java.lang.Class r0 = r7.loadClass(r6)     // Catch:{ Exception -> 0x006b }
            if (r8 == 0) goto L_0x006a
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r1 = mappings     // Catch:{ Exception -> 0x0068 }
            r1.put(r6, r0)     // Catch:{ Exception -> 0x0068 }
            goto L_0x006a
        L_0x0068:
            r1 = r0
            goto L_0x006b
        L_0x006a:
            return r0
        L_0x006b:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0086 }
            java.lang.ClassLoader r0 = r0.getContextClassLoader()     // Catch:{ Exception -> 0x0086 }
            if (r0 == 0) goto L_0x0086
            if (r0 == r7) goto L_0x0086
            java.lang.Class r7 = r0.loadClass(r6)     // Catch:{ Exception -> 0x0086 }
            if (r8 == 0) goto L_0x0085
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r8 = mappings     // Catch:{ Exception -> 0x0083 }
            r8.put(r6, r7)     // Catch:{ Exception -> 0x0083 }
            goto L_0x0085
        L_0x0083:
            r1 = r7
            goto L_0x0086
        L_0x0085:
            return r7
        L_0x0086:
            java.lang.Class r7 = java.lang.Class.forName(r6)     // Catch:{ Exception -> 0x0090 }
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r8 = mappings     // Catch:{ Exception -> 0x0091 }
            r8.put(r6, r7)     // Catch:{ Exception -> 0x0091 }
            return r7
        L_0x0090:
            r7 = r1
        L_0x0091:
            return r7
        L_0x0092:
            com.alibaba.fastjson.JSONException r7 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r0 = "className too long. "
            r8.append(r0)
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            r7.<init>(r6)
            throw r7
        L_0x00a9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.loadClass(java.lang.String, java.lang.ClassLoader, boolean):java.lang.Class");
    }

    /* JADX WARNING: type inference failed for: r46v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x02ff, code lost:
        if (r0 == null) goto L_0x0301;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x03fd, code lost:
        if (r0 == null) goto L_0x042e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b7, code lost:
        if (r6.getReturnType().getName().equals("groovy.lang.MetaClass") != false) goto L_0x00d9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02f9  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x0306  */
    /* JADX WARNING: Removed duplicated region for block: B:295:0x0183 A[EDGE_INSN: B:295:0x0183->B:85:0x0183 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x014b  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x015f  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x016d  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r46, int r47, boolean r48, com.alibaba.fastjson.annotation.JSONType r49, java.util.Map<java.lang.String, java.lang.String> r50, boolean r51, boolean r52, boolean r53, com.alibaba.fastjson.PropertyNamingStrategy r54) {
        /*
            r11 = r46
            r12 = r47
            r13 = r49
            r14 = r50
            r10 = r54
            java.util.LinkedHashMap r9 = new java.util.LinkedHashMap
            r9.<init>()
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.lang.reflect.Field[] r7 = r46.getDeclaredFields()
            r16 = 0
            if (r48 != 0) goto L_0x0474
            boolean r17 = isKotlin(r46)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r11
        L_0x0026:
            if (r1 == 0) goto L_0x0081
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            if (r1 == r2) goto L_0x0081
            java.lang.reflect.Method[] r2 = r1.getDeclaredMethods()
            int r3 = r2.length
            r4 = 0
        L_0x0032:
            if (r4 >= r3) goto L_0x007c
            r5 = r2[r4]
            int r6 = r5.getModifiers()
            r20 = r6 & 8
            if (r20 != 0) goto L_0x0075
            r20 = r6 & 2
            if (r20 != 0) goto L_0x0075
            r21 = r2
            r2 = r6 & 256(0x100, float:3.59E-43)
            if (r2 != 0) goto L_0x0077
            r2 = r6 & 4
            if (r2 == 0) goto L_0x004d
            goto L_0x0077
        L_0x004d:
            java.lang.Class r2 = r5.getReturnType()
            java.lang.Class r6 = java.lang.Void.TYPE
            boolean r2 = r2.equals(r6)
            if (r2 != 0) goto L_0x0077
            java.lang.Class[] r2 = r5.getParameterTypes()
            int r2 = r2.length
            if (r2 != 0) goto L_0x0077
            java.lang.Class r2 = r5.getReturnType()
            java.lang.Class<java.lang.ClassLoader> r6 = java.lang.ClassLoader.class
            if (r2 == r6) goto L_0x0077
            java.lang.Class r2 = r5.getDeclaringClass()
            java.lang.Class<java.lang.Object> r6 = java.lang.Object.class
            if (r2 != r6) goto L_0x0071
            goto L_0x0077
        L_0x0071:
            r0.add(r5)
            goto L_0x0077
        L_0x0075:
            r21 = r2
        L_0x0077:
            int r4 = r4 + 1
            r2 = r21
            goto L_0x0032
        L_0x007c:
            java.lang.Class r1 = r1.getSuperclass()
            goto L_0x0026
        L_0x0081:
            r1 = r16
            java.lang.annotation.Annotation[][] r1 = (java.lang.annotation.Annotation[][]) r1
            java.util.Iterator r20 = r0.iterator()
            r3 = r1
            r0 = r16
            r1 = r0
            r2 = r1
        L_0x008e:
            boolean r4 = r20.hasNext()
            if (r4 == 0) goto L_0x0474
            java.lang.Object r4 = r20.next()
            r6 = r4
            java.lang.reflect.Method r6 = (java.lang.reflect.Method) r6
            java.lang.String r5 = r6.getName()
            java.lang.String r4 = "getMetaClass"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x00ba
            java.lang.Class r4 = r6.getReturnType()
            java.lang.String r4 = r4.getName()
            r22 = r1
            java.lang.String r1 = "groovy.lang.MetaClass"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00bc
            goto L_0x00d9
        L_0x00ba:
            r22 = r1
        L_0x00bc:
            if (r52 == 0) goto L_0x00c7
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r6.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            goto L_0x00c9
        L_0x00c7:
            r1 = r16
        L_0x00c9:
            if (r1 != 0) goto L_0x00d1
            if (r52 == 0) goto L_0x00d1
            com.alibaba.fastjson.annotation.JSONField r1 = getSupperMethodAnnotation(r11, r6)
        L_0x00d1:
            if (r17 == 0) goto L_0x00dc
            boolean r4 = isKotlinIgnore(r11, r5)
            if (r4 == 0) goto L_0x00dc
        L_0x00d9:
            r1 = r22
            goto L_0x008e
        L_0x00dc:
            if (r1 != 0) goto L_0x0188
            if (r17 == 0) goto L_0x0188
            if (r0 != 0) goto L_0x0127
            java.lang.reflect.Constructor[] r0 = r46.getDeclaredConstructors()
            int r4 = r0.length
            r24 = r3
            r3 = 1
            if (r4 != r3) goto L_0x0122
            r4 = 0
            r18 = r0[r4]
            java.lang.annotation.Annotation[][] r18 = r18.getParameterAnnotations()
            java.lang.String[] r3 = getKoltinConstructorParameters(r46)
            if (r3 == 0) goto L_0x0119
            int r2 = r3.length
            java.lang.String[] r2 = new java.lang.String[r2]
            r26 = r0
            int r0 = r3.length
            java.lang.System.arraycopy(r3, r4, r2, r4, r0)
            java.util.Arrays.sort(r2)
            int r0 = r3.length
            short[] r0 = new short[r0]
            r28 = r1
        L_0x010a:
            int r1 = r3.length
            if (r4 >= r1) goto L_0x011f
            r1 = r3[r4]
            int r1 = java.util.Arrays.binarySearch(r2, r1)
            r0[r1] = r4
            int r4 = r4 + 1
            short r4 = (short) r4
            goto L_0x010a
        L_0x0119:
            r26 = r0
            r28 = r1
            r0 = r2
            r2 = r3
        L_0x011f:
            r24 = r18
            goto L_0x0130
        L_0x0122:
            r26 = r0
            r28 = r1
            goto L_0x012d
        L_0x0127:
            r28 = r1
            r24 = r3
            r26 = r0
        L_0x012d:
            r0 = r2
            r2 = r22
        L_0x0130:
            if (r2 == 0) goto L_0x0183
            if (r0 == 0) goto L_0x0183
            java.lang.String r1 = "get"
            boolean r1 = r5.startsWith(r1)
            if (r1 == 0) goto L_0x0183
            r1 = 3
            java.lang.String r3 = r5.substring(r1)
            java.lang.String r1 = decapitalize(r3)
            int r3 = java.util.Arrays.binarySearch(r2, r1)
            if (r3 >= 0) goto L_0x015f
            r29 = r3
            r4 = 0
        L_0x014e:
            int r3 = r2.length
            if (r4 >= r3) goto L_0x0161
            r3 = r2[r4]
            boolean r3 = r1.equalsIgnoreCase(r3)
            if (r3 == 0) goto L_0x015c
            r29 = r4
            goto L_0x0161
        L_0x015c:
            int r4 = r4 + 1
            goto L_0x014e
        L_0x015f:
            r29 = r3
        L_0x0161:
            if (r29 < 0) goto L_0x0183
            short r1 = r0[r29]
            r1 = r24[r1]
            if (r1 == 0) goto L_0x0183
            int r3 = r1.length
            r4 = 0
        L_0x016b:
            if (r4 >= r3) goto L_0x0183
            r30 = r0
            r0 = r1[r4]
            r31 = r1
            boolean r1 = r0 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r1 == 0) goto L_0x017c
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            r28 = r0
            goto L_0x0185
        L_0x017c:
            int r4 = r4 + 1
            r0 = r30
            r1 = r31
            goto L_0x016b
        L_0x0183:
            r30 = r0
        L_0x0185:
            r22 = r2
            goto L_0x0190
        L_0x0188:
            r28 = r1
            r24 = r3
            r26 = r0
            r30 = r2
        L_0x0190:
            if (r28 == 0) goto L_0x020f
            boolean r0 = r28.serialize()
            if (r0 != 0) goto L_0x01a3
        L_0x0198:
            r11 = r7
            r43 = r8
            r15 = r9
            r13 = r12
            r21 = 1
            r25 = 0
            goto L_0x045d
        L_0x01a3:
            int r18 = r28.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r28.serialzeFeatures()
            int r19 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            java.lang.String r0 = r28.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x0204
            java.lang.String r0 = r28.name()
            if (r14 == 0) goto L_0x01c8
            java.lang.Object r0 = r14.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x01c8
            goto L_0x0198
        L_0x01c8:
            r5 = r0
            setAccessible(r11, r6, r12)
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r21 = 0
            r23 = 0
            r29 = 1
            r0 = r4
            r1 = r5
            r2 = r6
            r6 = 1
            r32 = r4
            r25 = 0
            r4 = r46
            r33 = r5
            r5 = r21
            r21 = 1
            r6 = r18
            r34 = r7
            r7 = r19
            r35 = r8
            r8 = r28
            r36 = r9
            r9 = r23
            r12 = r10
            r10 = r29
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r1 = r32
            r0 = r33
            r10 = r36
            r10.put(r0, r1)
            goto L_0x033f
        L_0x0204:
            r34 = r7
            r35 = r8
            r12 = r10
            r21 = 1
            r25 = 0
            r10 = r9
            goto L_0x021d
        L_0x020f:
            r34 = r7
            r35 = r8
            r12 = r10
            r21 = 1
            r25 = 0
            r10 = r9
            r18 = 0
            r19 = 0
        L_0x021d:
            java.lang.String r0 = "get"
            boolean r0 = r5.startsWith(r0)
            r9 = 102(0x66, float:1.43E-43)
            r8 = 95
            if (r0 == 0) goto L_0x0348
            int r0 = r5.length()
            r1 = 4
            if (r0 < r1) goto L_0x033f
            java.lang.String r0 = "getClass"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x023a
            goto L_0x033f
        L_0x023a:
            r4 = 3
            char r0 = r5.charAt(r4)
            boolean r2 = java.lang.Character.isUpperCase(r0)
            if (r2 == 0) goto L_0x026e
            boolean r0 = compatibleWithJavaBean
            if (r0 == 0) goto L_0x0252
            java.lang.String r0 = r5.substring(r4)
            java.lang.String r0 = decapitalize(r0)
            goto L_0x0295
        L_0x0252:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            char r2 = r5.charAt(r4)
            char r2 = java.lang.Character.toLowerCase(r2)
            r0.append(r2)
            java.lang.String r1 = r5.substring(r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            goto L_0x0295
        L_0x026e:
            if (r0 != r8) goto L_0x0275
            java.lang.String r0 = r5.substring(r1)
            goto L_0x0295
        L_0x0275:
            if (r0 != r9) goto L_0x027c
            java.lang.String r0 = r5.substring(r4)
            goto L_0x0295
        L_0x027c:
            int r0 = r5.length()
            r2 = 5
            if (r0 < r2) goto L_0x033f
            char r0 = r5.charAt(r1)
            boolean r0 = java.lang.Character.isUpperCase(r0)
            if (r0 == 0) goto L_0x033f
            java.lang.String r0 = r5.substring(r4)
            java.lang.String r0 = decapitalize(r0)
        L_0x0295:
            boolean r1 = isJSONTypeIgnore(r11, r13, r0)
            if (r1 == 0) goto L_0x029d
            goto L_0x033f
        L_0x029d:
            r3 = r34
            r7 = r35
            java.lang.reflect.Field r2 = getField(r11, r0, r3, r7)
            if (r2 == 0) goto L_0x02eb
            if (r52 == 0) goto L_0x02b2
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r2.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            goto L_0x02b4
        L_0x02b2:
            r1 = r16
        L_0x02b4:
            if (r1 == 0) goto L_0x02e7
            boolean r18 = r1.serialize()
            if (r18 != 0) goto L_0x02bd
            goto L_0x0301
        L_0x02bd:
            int r18 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r19 = r1.serialzeFeatures()
            int r19 = com.alibaba.fastjson.serializer.SerializerFeature.of(r19)
            java.lang.String r23 = r1.name()
            int r23 = r23.length()
            if (r23 == 0) goto L_0x02e7
            java.lang.String r0 = r1.name()
            if (r14 == 0) goto L_0x02e2
            java.lang.Object r0 = r14.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x02e2
            goto L_0x0301
        L_0x02e2:
            r23 = r1
            r1 = r0
            r0 = 1
            goto L_0x02ef
        L_0x02e7:
            r23 = r1
            r1 = r0
            goto L_0x02ee
        L_0x02eb:
            r1 = r0
            r23 = r16
        L_0x02ee:
            r0 = 0
        L_0x02ef:
            if (r12 == 0) goto L_0x02f7
            if (r0 != 0) goto L_0x02f7
            java.lang.String r1 = r12.translate(r1)
        L_0x02f7:
            if (r14 == 0) goto L_0x0306
            java.lang.Object r0 = r14.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0307
        L_0x0301:
            r11 = r3
            r43 = r7
            r15 = r10
            goto L_0x0344
        L_0x0306:
            r0 = r1
        L_0x0307:
            r1 = r12
            r12 = r47
            setAccessible(r11, r6, r12)
            r37 = r5
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r27 = 0
            r38 = r0
            r0 = r5
            r1 = r38
            r29 = r2
            r2 = r6
            r39 = r3
            r3 = r29
            r12 = 3
            r4 = r46
            r14 = r5
            r12 = r37
            r5 = r27
            r40 = r6
            r6 = r18
            r41 = r7
            r7 = r19
            r8 = r28
            r9 = r23
            r15 = r10
            r10 = r53
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r38
            r15.put(r0, r14)
            goto L_0x0350
        L_0x033f:
            r15 = r10
            r11 = r34
            r43 = r35
        L_0x0344:
            r13 = r47
            goto L_0x045d
        L_0x0348:
            r12 = r5
            r40 = r6
            r15 = r10
            r39 = r34
            r41 = r35
        L_0x0350:
            java.lang.String r0 = "is"
            boolean r0 = r12.startsWith(r0)
            if (r0 == 0) goto L_0x035f
            int r0 = r12.length()
            r1 = 3
            if (r0 >= r1) goto L_0x0364
        L_0x035f:
            r11 = r39
            r43 = r41
            goto L_0x0344
        L_0x0364:
            r0 = 2
            char r1 = r12.charAt(r0)
            boolean r2 = java.lang.Character.isUpperCase(r1)
            if (r2 == 0) goto L_0x0399
            boolean r1 = compatibleWithJavaBean
            if (r1 == 0) goto L_0x037c
            java.lang.String r0 = r12.substring(r0)
            java.lang.String r0 = decapitalize(r0)
            goto L_0x03ab
        L_0x037c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            char r0 = r12.charAt(r0)
            char r0 = java.lang.Character.toLowerCase(r0)
            r1.append(r0)
            r2 = 3
            java.lang.String r0 = r12.substring(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            goto L_0x03ab
        L_0x0399:
            r2 = 3
            r3 = 95
            if (r1 != r3) goto L_0x03a3
            java.lang.String r0 = r12.substring(r2)
            goto L_0x03ab
        L_0x03a3:
            r2 = 102(0x66, float:1.43E-43)
            if (r1 != r2) goto L_0x035f
            java.lang.String r0 = r12.substring(r0)
        L_0x03ab:
            boolean r1 = isJSONTypeIgnore(r11, r13, r0)
            if (r1 == 0) goto L_0x03b2
            goto L_0x035f
        L_0x03b2:
            r10 = r39
            r14 = r41
            java.lang.reflect.Field r1 = getField(r11, r0, r10, r14)
            if (r1 != 0) goto L_0x03c0
            java.lang.reflect.Field r1 = getField(r11, r12, r10, r14)
        L_0x03c0:
            r3 = r1
            if (r3 == 0) goto L_0x0414
            if (r52 == 0) goto L_0x03ce
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            goto L_0x03d0
        L_0x03ce:
            r1 = r16
        L_0x03d0:
            if (r1 == 0) goto L_0x0409
            boolean r2 = r1.serialize()
            if (r2 != 0) goto L_0x03d9
            goto L_0x042e
        L_0x03d9:
            int r2 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r1.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            java.lang.String r5 = r1.name()
            int r5 = r5.length()
            if (r5 == 0) goto L_0x0400
            java.lang.String r0 = r1.name()
            r9 = r50
            if (r9 == 0) goto L_0x0402
            java.lang.Object r0 = r9.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0402
            goto L_0x042e
        L_0x0400:
            r9 = r50
        L_0x0402:
            r18 = r1
            r6 = r2
            r7 = r4
            r8 = r54
            goto L_0x041e
        L_0x0409:
            r9 = r50
            r6 = r18
            r7 = r19
            r8 = r54
            r18 = r1
            goto L_0x041e
        L_0x0414:
            r9 = r50
            r6 = r18
            r7 = r19
            r8 = r54
            r18 = r16
        L_0x041e:
            if (r8 == 0) goto L_0x0424
            java.lang.String r0 = r8.translate(r0)
        L_0x0424:
            if (r9 == 0) goto L_0x0433
            java.lang.Object r0 = r9.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0433
        L_0x042e:
            r11 = r10
            r43 = r14
            goto L_0x0344
        L_0x0433:
            r4 = r0
            r5 = r47
            setAccessible(r11, r3, r5)
            r2 = r40
            setAccessible(r11, r2, r5)
            com.alibaba.fastjson.util.FieldInfo r1 = new com.alibaba.fastjson.util.FieldInfo
            r19 = 0
            r0 = r1
            r42 = r1
            r1 = r4
            r43 = r14
            r14 = r4
            r4 = r46
            r13 = r5
            r5 = r19
            r8 = r28
            r9 = r18
            r11 = r10
            r10 = r53
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r42
            r15.put(r14, r0)
        L_0x045d:
            r7 = r11
            r12 = r13
            r9 = r15
            r1 = r22
            r3 = r24
            r0 = r26
            r2 = r30
            r8 = r43
            r10 = r54
            r11 = r46
            r13 = r49
            r14 = r50
            goto L_0x008e
        L_0x0474:
            r11 = r7
            r15 = r9
            r13 = r12
            r21 = 1
            r25 = 0
            java.util.ArrayList r0 = new java.util.ArrayList
            int r1 = r11.length
            r0.<init>(r1)
            int r1 = r11.length
            r2 = 0
        L_0x0483:
            if (r2 >= r1) goto L_0x04ab
            r3 = r11[r2]
            int r4 = r3.getModifiers()
            r4 = r4 & 8
            if (r4 == 0) goto L_0x0490
            goto L_0x04a8
        L_0x0490:
            java.lang.String r4 = r3.getName()
            java.lang.String r5 = "this$0"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x049d
            goto L_0x04a8
        L_0x049d:
            int r4 = r3.getModifiers()
            r4 = r4 & 1
            if (r4 == 0) goto L_0x04a8
            r0.add(r3)
        L_0x04a8:
            int r2 = r2 + 1
            goto L_0x0483
        L_0x04ab:
            java.lang.Class r1 = r46.getSuperclass()
        L_0x04af:
            if (r1 == 0) goto L_0x04db
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            if (r1 == r2) goto L_0x04db
            java.lang.reflect.Field[] r2 = r1.getDeclaredFields()
            int r3 = r2.length
            r4 = 0
        L_0x04bb:
            if (r4 >= r3) goto L_0x04d6
            r5 = r2[r4]
            int r6 = r5.getModifiers()
            r6 = r6 & 8
            if (r6 == 0) goto L_0x04c8
            goto L_0x04d3
        L_0x04c8:
            int r6 = r5.getModifiers()
            r6 = r6 & 1
            if (r6 == 0) goto L_0x04d3
            r0.add(r5)
        L_0x04d3:
            int r4 = r4 + 1
            goto L_0x04bb
        L_0x04d6:
            java.lang.Class r1 = r1.getSuperclass()
            goto L_0x04af
        L_0x04db:
            java.util.Iterator r11 = r0.iterator()
        L_0x04df:
            boolean r0 = r11.hasNext()
            if (r0 == 0) goto L_0x0568
            java.lang.Object r0 = r11.next()
            r3 = r0
            java.lang.reflect.Field r3 = (java.lang.reflect.Field) r3
            if (r52 == 0) goto L_0x04f8
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = r3.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            r9 = r0
            goto L_0x04fa
        L_0x04f8:
            r9 = r16
        L_0x04fa:
            java.lang.String r0 = r3.getName()
            if (r9 == 0) goto L_0x0524
            boolean r1 = r9.serialize()
            if (r1 != 0) goto L_0x0507
            goto L_0x04df
        L_0x0507:
            int r1 = r9.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r2 = r9.serialzeFeatures()
            int r2 = com.alibaba.fastjson.serializer.SerializerFeature.of(r2)
            java.lang.String r4 = r9.name()
            int r4 = r4.length()
            if (r4 == 0) goto L_0x0521
            java.lang.String r0 = r9.name()
        L_0x0521:
            r6 = r1
            r7 = r2
            goto L_0x0526
        L_0x0524:
            r6 = 0
            r7 = 0
        L_0x0526:
            r14 = r50
            if (r14 == 0) goto L_0x0533
            java.lang.Object r0 = r14.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0533
            goto L_0x04df
        L_0x0533:
            r10 = r54
            if (r10 == 0) goto L_0x053b
            java.lang.String r0 = r10.translate(r0)
        L_0x053b:
            r8 = r0
            boolean r0 = r15.containsKey(r8)
            if (r0 != 0) goto L_0x04df
            r5 = r46
            setAccessible(r5, r3, r13)
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r2 = 0
            r17 = 0
            r18 = 0
            r0 = r4
            r1 = r8
            r44 = r4
            r4 = r46
            r5 = r17
            r45 = r8
            r8 = r18
            r10 = r53
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r1 = r44
            r0 = r45
            r15.put(r0, r1)
            goto L_0x04df
        L_0x0568:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r49
            if (r1 == 0) goto L_0x0590
            java.lang.String[] r1 = r49.orders()
            if (r1 == 0) goto L_0x0592
            int r2 = r1.length
            int r3 = r15.size()
            if (r2 != r3) goto L_0x0592
            int r2 = r1.length
            r3 = 0
        L_0x0580:
            if (r3 >= r2) goto L_0x058e
            r4 = r1[r3]
            boolean r4 = r15.containsKey(r4)
            if (r4 != 0) goto L_0x058b
            goto L_0x0592
        L_0x058b:
            int r3 = r3 + 1
            goto L_0x0580
        L_0x058e:
            r5 = 1
            goto L_0x0593
        L_0x0590:
            r1 = r16
        L_0x0592:
            r5 = 0
        L_0x0593:
            if (r5 == 0) goto L_0x05a7
            int r2 = r1.length
            r3 = 0
        L_0x0597:
            if (r3 >= r2) goto L_0x05c4
            r4 = r1[r3]
            java.lang.Object r4 = r15.get(r4)
            com.alibaba.fastjson.util.FieldInfo r4 = (com.alibaba.fastjson.util.FieldInfo) r4
            r0.add(r4)
            int r3 = r3 + 1
            goto L_0x0597
        L_0x05a7:
            java.util.Collection r1 = r15.values()
            java.util.Iterator r1 = r1.iterator()
        L_0x05af:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x05bf
            java.lang.Object r2 = r1.next()
            com.alibaba.fastjson.util.FieldInfo r2 = (com.alibaba.fastjson.util.FieldInfo) r2
            r0.add(r2)
            goto L_0x05af
        L_0x05bf:
            if (r51 == 0) goto L_0x05c4
            java.util.Collections.sort(r0)
        L_0x05c4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, int, boolean, com.alibaba.fastjson.annotation.JSONType, java.util.Map, boolean, boolean, boolean, com.alibaba.fastjson.PropertyNamingStrategy):java.util.List");
    }

    public static JSONField getSupperMethodAnnotation(Class<?> cls, Method method) {
        boolean z;
        JSONField jSONField;
        boolean z2;
        JSONField jSONField2;
        for (Class methods : cls.getInterfaces()) {
            for (Method method2 : methods.getMethods()) {
                if (method2.getName().equals(method.getName())) {
                    Class[] parameterTypes = method2.getParameterTypes();
                    Class[] parameterTypes2 = method.getParameterTypes();
                    if (parameterTypes.length != parameterTypes2.length) {
                        continue;
                    } else {
                        int i = 0;
                        while (true) {
                            if (i >= parameterTypes.length) {
                                z2 = true;
                                break;
                            } else if (!parameterTypes[i].equals(parameterTypes2[i])) {
                                z2 = false;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (z2 && (jSONField2 = (JSONField) method2.getAnnotation(JSONField.class)) != null) {
                            return jSONField2;
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            Class[] parameterTypes3 = method.getParameterTypes();
            for (Method method3 : superclass.getMethods()) {
                Class[] parameterTypes4 = method3.getParameterTypes();
                if (parameterTypes4.length == parameterTypes3.length && method3.getName().equals(method.getName())) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= parameterTypes3.length) {
                            z = true;
                            break;
                        } else if (!parameterTypes4[i2].equals(parameterTypes3[i2])) {
                            z = false;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z && (jSONField = (JSONField) method3.getAnnotation(JSONField.class)) != null) {
                        return jSONField;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, JSONType jSONType, String str) {
        if (!(jSONType == null || jSONType.ignores() == null)) {
            for (String equalsIgnoreCase : jSONType.ignores()) {
                if (str.equalsIgnoreCase(equalsIgnoreCase)) {
                    return true;
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == Object.class || superclass == null || !isJSONTypeIgnore(superclass, (JSONType) superclass.getAnnotation(JSONType.class), str)) {
            return false;
        }
        return true;
    }

    public static boolean isGenericParamType(Type type) {
        Type genericSuperclass;
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class) || (genericSuperclass = ((Class) type).getGenericSuperclass()) == Object.class || !isGenericParamType(genericSuperclass)) {
            return false;
        }
        return true;
    }

    public static Type getGenericParamType(Type type) {
        return type instanceof Class ? getGenericParamType(((Class) type).getGenericSuperclass()) : type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            return (Class) ((TypeVariable) type).getBounds()[0];
        }
        if (!(type instanceof WildcardType)) {
            return Object.class;
        }
        Type[] upperBounds = ((WildcardType) type).getUpperBounds();
        if (upperBounds.length == 1) {
            return getClass(upperBounds[0]);
        }
        return Object.class;
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0 || (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0)))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static boolean setAccessible(Class<?> cls, Member member, int i) {
        if (member == null || !setAccessibleEnable) {
            return false;
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if ((superclass == null || superclass == Object.class) && (member.getModifiers() & 1) != 0 && (i & 1) != 0) {
            return false;
        }
        try {
            ((AccessibleObject) member).setAccessible(true);
            return true;
        } catch (AccessControlException unused) {
            setAccessibleEnable = false;
            return false;
        }
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr) {
        return getField(cls, str, fieldArr, (Map<Class<?>, Field[]>) null);
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr, Map<Class<?>, Field[]> map) {
        Field field0 = getField0(cls, str, fieldArr, map);
        if (field0 == null) {
            field0 = getField0(cls, "_" + str, fieldArr, map);
        }
        if (field0 == null) {
            field0 = getField0(cls, "m_" + str, fieldArr, map);
        }
        if (field0 != null) {
            return field0;
        }
        return getField0(cls, WXComponent.PROP_FS_MATCH_PARENT + str.substring(0, 1).toUpperCase() + str.substring(1), fieldArr, map);
    }

    private static Field getField0(Class<?> cls, String str, Field[] fieldArr, Map<Class<?>, Field[]> map) {
        char charAt;
        char charAt2;
        for (Field field : fieldArr) {
            String name = field.getName();
            if (str.equals(name)) {
                return field;
            }
            if (str.length() > 2 && (charAt = str.charAt(0)) >= 'a' && charAt <= 'z' && (charAt2 = str.charAt(1)) >= 'A' && charAt2 <= 'Z' && str.equalsIgnoreCase(name)) {
                return field;
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        Field[] fieldArr2 = null;
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        if (map != null) {
            fieldArr2 = map.get(superclass);
        }
        if (fieldArr2 == null) {
            fieldArr2 = superclass.getDeclaredFields();
            if (map != null) {
                map.put(superclass, fieldArr2);
            }
        }
        return getField(superclass, str, fieldArr2, map);
    }

    public static Type getCollectionItemType(Type type) {
        Type type2;
        if (type instanceof ParameterizedType) {
            type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type2 instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) type2).getUpperBounds();
                if (upperBounds.length == 1) {
                    type2 = upperBounds[0];
                }
            }
        } else {
            if (type instanceof Class) {
                Class cls = (Class) type;
                if (!cls.getName().startsWith("java.")) {
                    type2 = getCollectionItemType(cls.getGenericSuperclass());
                }
            }
            type2 = null;
        }
        return type2 == null ? Object.class : type2;
    }

    public static Object defaultValue(Class<?> cls) {
        if (cls == Byte.TYPE) {
            return (byte) 0;
        }
        if (cls == Short.TYPE) {
            return (short) 0;
        }
        if (cls == Integer.TYPE) {
            return 0;
        }
        if (cls == Long.TYPE) {
            return 0L;
        }
        if (cls == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (cls == Double.TYPE) {
            return Double.valueOf(0.0d);
        }
        if (cls == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        return cls == Character.TYPE ? '0' : null;
    }

    public static boolean getArgument(Type[] typeArr, TypeVariable[] typeVariableArr, Type[] typeArr2) {
        if (typeArr2 == null || typeVariableArr.length == 0) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < typeArr.length; i++) {
            ParameterizedType parameterizedType = typeArr[i];
            if (parameterizedType instanceof ParameterizedType) {
                ParameterizedType parameterizedType2 = parameterizedType;
                Type[] actualTypeArguments = parameterizedType2.getActualTypeArguments();
                if (getArgument(actualTypeArguments, typeVariableArr, typeArr2)) {
                    typeArr[i] = new ParameterizedTypeImpl(actualTypeArguments, parameterizedType2.getOwnerType(), parameterizedType2.getRawType());
                    z = true;
                }
            } else if (parameterizedType instanceof TypeVariable) {
                boolean z2 = z;
                for (int i2 = 0; i2 < typeVariableArr.length; i2++) {
                    if (parameterizedType.equals(typeVariableArr[i2])) {
                        typeArr[i] = typeArr2[i2];
                        z2 = true;
                    }
                }
                z = z2;
            }
        }
        return z;
    }

    public static double parseDouble(String str) {
        int length = str.length();
        if (length > 10) {
            return Double.parseDouble(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '-' && i2 == 0) {
                z = true;
            } else if (charAt == '.') {
                if (i != 0) {
                    return Double.parseDouble(str);
                }
                i = (length - i2) - 1;
            } else if (charAt < '0' || charAt > '9') {
                return Double.parseDouble(str);
            } else {
                j = (j * 10) + ((long) (charAt - '0'));
            }
        }
        if (z) {
            j = -j;
        }
        switch (i) {
            case 0:
                return (double) j;
            case 1:
                double d = (double) j;
                Double.isNaN(d);
                return d / 10.0d;
            case 2:
                double d2 = (double) j;
                Double.isNaN(d2);
                return d2 / 100.0d;
            case 3:
                double d3 = (double) j;
                Double.isNaN(d3);
                return d3 / 1000.0d;
            case 4:
                double d4 = (double) j;
                Double.isNaN(d4);
                return d4 / 10000.0d;
            case 5:
                double d5 = (double) j;
                Double.isNaN(d5);
                return d5 / 100000.0d;
            case 6:
                double d6 = (double) j;
                Double.isNaN(d6);
                return d6 / 1000000.0d;
            case 7:
                double d7 = (double) j;
                Double.isNaN(d7);
                return d7 / 1.0E7d;
            case 8:
                double d8 = (double) j;
                Double.isNaN(d8);
                return d8 / 1.0E8d;
            case 9:
                double d9 = (double) j;
                Double.isNaN(d9);
                return d9 / 1.0E9d;
            default:
                return Double.parseDouble(str);
        }
    }

    public static float parseFloat(String str) {
        int length = str.length();
        if (length >= 10) {
            return Float.parseFloat(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '-' && i2 == 0) {
                z = true;
            } else if (charAt == '.') {
                if (i != 0) {
                    return Float.parseFloat(str);
                }
                i = (length - i2) - 1;
            } else if (charAt < '0' || charAt > '9') {
                return Float.parseFloat(str);
            } else {
                j = (j * 10) + ((long) (charAt - '0'));
            }
        }
        if (z) {
            j = -j;
        }
        switch (i) {
            case 0:
                return (float) j;
            case 1:
                return ((float) j) / 10.0f;
            case 2:
                return ((float) j) / 100.0f;
            case 3:
                return ((float) j) / 1000.0f;
            case 4:
                return ((float) j) / 10000.0f;
            case 5:
                return ((float) j) / 100000.0f;
            case 6:
                return ((float) j) / 1000000.0f;
            case 7:
                return ((float) j) / 1.0E7f;
            case 8:
                return ((float) j) / 1.0E8f;
            case 9:
                return ((float) j) / 1.0E9f;
            default:
                return Float.parseFloat(str);
        }
    }

    public static long fnv_64_lower(String str) {
        if (str == null) {
            return 0;
        }
        long j = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (!(charAt == '_' || charAt == '-')) {
                if (charAt >= 'A' && charAt <= 'Z') {
                    charAt = (char) (charAt + ' ');
                }
                j = (j ^ ((long) charAt)) * 1099511628211L;
            }
        }
        return j;
    }

    public static void addMapping(String str, Class<?> cls) {
        mappings.put(str, cls);
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Class<?> cls;
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String str = Operators.ARRAY_START_STR;
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            str = str + str;
        }
        if (!(genericComponentType instanceof Class)) {
            return genericArrayType;
        }
        Class cls2 = (Class) genericComponentType;
        if (!cls2.isPrimitive()) {
            return genericArrayType;
        }
        try {
            if (cls2 == Boolean.TYPE) {
                cls = Class.forName(str + "Z");
            } else if (cls2 == Character.TYPE) {
                cls = Class.forName(str + "C");
            } else if (cls2 == Byte.TYPE) {
                cls = Class.forName(str + "B");
            } else if (cls2 == Short.TYPE) {
                cls = Class.forName(str + "S");
            } else if (cls2 == Integer.TYPE) {
                cls = Class.forName(str + "I");
            } else if (cls2 == Long.TYPE) {
                cls = Class.forName(str + "J");
            } else if (cls2 == Float.TYPE) {
                cls = Class.forName(str + ApiConstants.UTConstants.UT_SUCCESS_F);
            } else if (cls2 != Double.TYPE) {
                return genericArrayType;
            } else {
                cls = Class.forName(str + "D");
            }
            return cls;
        } catch (ClassNotFoundException unused) {
            return genericArrayType;
        }
    }
}
