package mtopsdk.mtop.util;

import com.alibaba.fastjson.JSON;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;

public class ReflectUtil {
    private static final String API_NAME = "API_NAME";
    private static final String NEED_ECODE = "NEED_ECODE";
    private static final String NEED_SESSION = "NEED_SESSION";
    private static final String ORIGINALJSON = "ORIGINALJSON";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    private static final String TAG = "mtopsdk.ReflectUtil";
    private static final String VERSION = "VERSION";

    public static MtopRequest convertToMtopRequest(Object obj) {
        MtopRequest mtopRequest = new MtopRequest();
        if (obj != null) {
            parseParams(mtopRequest, obj);
        }
        return mtopRequest;
    }

    public static MtopRequest convertToMtopRequest(IMTOPDataObject iMTOPDataObject) {
        MtopRequest mtopRequest = new MtopRequest();
        if (iMTOPDataObject != null) {
            parseParams(mtopRequest, iMTOPDataObject);
        }
        return mtopRequest;
    }

    private static void parseParams(MtopRequest mtopRequest, Object obj) {
        try {
            HashMap hashMap = new HashMap();
            Class<?> cls = obj.getClass();
            HashSet hashSet = new HashSet();
            hashSet.addAll(Arrays.asList(cls.getFields()));
            hashSet.addAll(Arrays.asList(cls.getDeclaredFields()));
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                Field field = (Field) it.next();
                String name = field.getName();
                if (!name.contains("$") && !name.equals(SERIAL_VERSION_UID) && !name.equals(ORIGINALJSON)) {
                    boolean z = true;
                    field.setAccessible(true);
                    char c = 65535;
                    int hashCode = name.hashCode();
                    if (hashCode != -513196083) {
                        if (hashCode != 397645513) {
                            if (hashCode != 1069590712) {
                                if (hashCode == 1779423664) {
                                    if (name.equals(API_NAME)) {
                                        c = 0;
                                    }
                                }
                            } else if (name.equals(VERSION)) {
                                c = 1;
                            }
                        } else if (name.equals(NEED_ECODE)) {
                            c = 2;
                        }
                    } else if (name.equals(NEED_SESSION)) {
                        c = 3;
                    }
                    switch (c) {
                        case 0:
                            Object obj2 = field.get(obj);
                            if (obj2 == null) {
                                break;
                            } else {
                                mtopRequest.setApiName(obj2.toString());
                                break;
                            }
                        case 1:
                            Object obj3 = field.get(obj);
                            if (obj3 == null) {
                                break;
                            } else {
                                mtopRequest.setVersion(obj3.toString());
                                break;
                            }
                        case 2:
                            Boolean valueOf = Boolean.valueOf(field.getBoolean(obj));
                            if (valueOf == null || !valueOf.booleanValue()) {
                                z = false;
                            }
                            mtopRequest.setNeedEcode(z);
                            break;
                        case 3:
                            Boolean valueOf2 = Boolean.valueOf(field.getBoolean(obj));
                            if (valueOf2 == null || !valueOf2.booleanValue()) {
                                z = false;
                            }
                            mtopRequest.setNeedSession(z);
                            break;
                        default:
                            Object obj4 = field.get(obj);
                            if (obj4 != null) {
                                if (!(obj4 instanceof String)) {
                                    hashMap.put(name, JSON.toJSONString(obj4));
                                    break;
                                } else {
                                    hashMap.put(name, obj4.toString());
                                    break;
                                }
                            } else {
                                break;
                            }
                    }
                }
            }
            mtopRequest.dataParams = hashMap;
            mtopRequest.setData(convertMapToDataStr(hashMap));
        } catch (Exception e) {
            TBSdkLog.e(TAG, "parseParams failed.", (Throwable) e);
        }
    }

    public static String convertMapToDataStr(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(Operators.BLOCK_START_STR);
        if (map != null && !map.isEmpty()) {
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                if (!(str == null || str2 == null)) {
                    try {
                        sb.append(JSON.toJSONString(str));
                        sb.append(":");
                        sb.append(JSON.toJSONString(str2));
                        sb.append(",");
                    } catch (Throwable th) {
                        StringBuilder sb2 = new StringBuilder(64);
                        sb2.append("[convertMapToDataStr] convert key=");
                        sb2.append(str);
                        sb2.append(",value=");
                        sb2.append(str2);
                        sb2.append(" to dataStr error.");
                        TBSdkLog.e(TAG, sb2.toString(), th);
                    }
                }
            }
            int length = sb.length();
            if (length > 1) {
                sb.deleteCharAt(length - 1);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Deprecated
    public static String converMapToDataStr(Map<String, String> map) {
        return convertMapToDataStr(map);
    }

    @Deprecated
    public static Map<String, String> parseDataParams(IMTOPDataObject iMTOPDataObject) {
        if (iMTOPDataObject == null) {
            return new HashMap();
        }
        return parseFields(iMTOPDataObject, iMTOPDataObject.getClass());
    }

    @Deprecated
    private static Map<String, String> parseDataParams(Object obj) {
        if (obj == null) {
            return new HashMap();
        }
        return parseFields(obj, obj.getClass());
    }

    @Deprecated
    private static Map<String, String> parseFields(Object obj, Class<?> cls) {
        HashMap hashMap = new HashMap();
        parseFieldsToMap(obj, cls.getDeclaredFields(), hashMap, false);
        parseFieldsToMap(obj, cls.getFields(), hashMap, true);
        return hashMap;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0045 A[SYNTHETIC, Splitter:B:18:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0074 A[SYNTHETIC] */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void parseFieldsToMap(java.lang.Object r8, java.lang.reflect.Field[] r9, java.util.HashMap<java.lang.String, java.lang.String> r10, boolean r11) {
        /*
            if (r9 == 0) goto L_0x0079
            int r0 = r9.length
            if (r0 != 0) goto L_0x0007
            goto L_0x0079
        L_0x0007:
            int r0 = r9.length
            r1 = 0
            r2 = 0
            r3 = r2
        L_0x000b:
            if (r1 >= r0) goto L_0x0078
            r4 = r9[r1]
            java.lang.String r5 = r4.getName()     // Catch:{ Throwable -> 0x0025 }
            boolean r3 = excludeField(r5, r10, r11)     // Catch:{ Throwable -> 0x0023 }
            if (r3 == 0) goto L_0x001a
            goto L_0x0074
        L_0x001a:
            r3 = 1
            r4.setAccessible(r3)     // Catch:{ Throwable -> 0x0023 }
            java.lang.Object r3 = r4.get(r8)     // Catch:{ Throwable -> 0x0023 }
            goto L_0x0043
        L_0x0023:
            r3 = move-exception
            goto L_0x0028
        L_0x0025:
            r4 = move-exception
            r5 = r3
            r3 = r4
        L_0x0028:
            java.lang.String r4 = "mtopsdk.ReflectUtil"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "[parseFieldsToMap]get biz param error through reflection.---"
            r6.append(r7)
            java.lang.String r3 = r3.toString()
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            mtopsdk.common.util.TBSdkLog.e(r4, r3)
            r3 = r2
        L_0x0043:
            if (r3 == 0) goto L_0x0074
            boolean r4 = r3 instanceof java.lang.String     // Catch:{ Throwable -> 0x0059 }
            if (r4 == 0) goto L_0x0051
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0059 }
            r10.put(r5, r3)     // Catch:{ Throwable -> 0x0059 }
            goto L_0x0074
        L_0x0051:
            java.lang.String r3 = com.alibaba.fastjson.JSON.toJSONString(r3)     // Catch:{ Throwable -> 0x0059 }
            r10.put(r5, r3)     // Catch:{ Throwable -> 0x0059 }
            goto L_0x0074
        L_0x0059:
            r3 = move-exception
            java.lang.String r4 = "mtopsdk.ReflectUtil"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "[parseFieldsToMap]transform biz param to json string error.---"
            r6.append(r7)
            java.lang.String r3 = r3.toString()
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            mtopsdk.common.util.TBSdkLog.e(r4, r3)
        L_0x0074:
            r3 = r5
            int r1 = r1 + 1
            goto L_0x000b
        L_0x0078:
            return
        L_0x0079:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.util.ReflectUtil.parseFieldsToMap(java.lang.Object, java.lang.reflect.Field[], java.util.HashMap, boolean):void");
    }

    @Deprecated
    private static boolean excludeField(String str, HashMap<String, String> hashMap, boolean z) {
        if (str.contains("$") || API_NAME.equals(str) || VERSION.equals(str) || NEED_ECODE.equals(str) || NEED_SESSION.equals(str) || SERIAL_VERSION_UID.equalsIgnoreCase(str) || ORIGINALJSON.equalsIgnoreCase(str)) {
            return true;
        }
        if (z) {
            return hashMap.containsKey(str);
        }
        return false;
    }

    @Deprecated
    public static void parseUrlParams(MtopRequest mtopRequest, Object obj) {
        if (obj != null) {
            Object fieldValueByName = getFieldValueByName(API_NAME, obj);
            if (fieldValueByName != null) {
                mtopRequest.setApiName(fieldValueByName.toString());
            }
            Object fieldValueByName2 = getFieldValueByName(VERSION, obj);
            if (fieldValueByName2 != null) {
                mtopRequest.setVersion(fieldValueByName2.toString());
            }
            if (needEcode(obj)) {
                mtopRequest.setNeedEcode(true);
            }
            if (needSession(obj)) {
                mtopRequest.setNeedSession(true);
            }
        }
    }

    @Deprecated
    public static Object getFieldValueByName(String str, Object obj) {
        if (obj == null || str == null) {
            return null;
        }
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.getName().equals(str)) {
                try {
                    return field.get(obj);
                } catch (IllegalArgumentException e) {
                    TBSdkLog.e(TAG, e.toString());
                } catch (IllegalAccessException e2) {
                    TBSdkLog.e(TAG, e2.toString());
                }
            } else {
                i++;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean needEcode(Object obj) {
        Object fieldValueByName = getFieldValueByName(NEED_ECODE, obj);
        Boolean bool = false;
        if (fieldValueByName != null) {
            bool = (Boolean) fieldValueByName;
        }
        return bool.booleanValue();
    }

    @Deprecated
    public static boolean needSession(Object obj) {
        Object fieldValueByName = getFieldValueByName(NEED_SESSION, obj);
        Boolean bool = false;
        if (fieldValueByName != null) {
            bool = (Boolean) fieldValueByName;
        }
        return bool.booleanValue();
    }

    @Deprecated
    public static boolean needJsonType(Object obj) {
        Object fieldValueByName = getFieldValueByName(ORIGINALJSON, obj);
        Boolean bool = false;
        if (fieldValueByName != null) {
            bool = (Boolean) fieldValueByName;
        }
        return bool.booleanValue();
    }
}
