package com.taobao.mtop;

import java.lang.reflect.Method;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;

public class UTAdapter {
    private static final String COMMIT_EVENT_METHOD = "commitEvent";
    private static final String TAG = "MtopWVPlugin.UTAdapter";
    private static final String TBS_EXT_CLASS_NAME = "com.taobao.statistic.TBS$Ext";
    private static Class<?> cls;
    private static Method utCommitMethod;

    public static void commit(String str, int i, Object obj, Object obj2, Object obj3, String... strArr) {
        try {
            if (getUtCommitMethod() != null && cls != null) {
                utCommitMethod.invoke(cls, new Object[]{str, Integer.valueOf(i), obj, obj2, obj3, strArr});
            }
        } catch (Exception e) {
            TBSdkLog.e(TAG, "UTAdapter commit(String pageName,int eventId, Object arg1,Object arg2,Object arg3,String ... kvs) failed ---" + e.toString());
        }
    }

    public static void commit(String str, int i, String str2, String str3, String str4, Map<String, String> map) {
        try {
            commit(str, i, (Object) str2, (Object) str3, (Object) str4, mapToArray(map));
        } catch (Throwable unused) {
        }
    }

    public static Method getUtCommitMethod() {
        if (utCommitMethod != null) {
            return utCommitMethod;
        }
        try {
            cls = UTAdapter.class.getClassLoader().loadClass(TBS_EXT_CLASS_NAME);
            if (cls != null) {
                utCommitMethod = cls.getDeclaredMethod(COMMIT_EVENT_METHOD, new Class[]{String.class, Integer.TYPE, Object.class, Object.class, Object.class, String[].class});
            }
        } catch (Exception e) {
            TBSdkLog.e(TAG, "Connot Found \"TBS.Ext.commitEvent(String, int, Object, Object, Object, String...)\" Method ---" + e.toString());
        }
        return utCommitMethod;
    }

    private static String[] mapToArray(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return new String[0];
        }
        String[] strArr = new String[map.size()];
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            String str2 = (String) next.getValue();
            if (str == null) {
                strArr[0] = "";
            } else {
                strArr[0] = str + SymbolExpUtil.SYMBOL_EQUAL + str2;
            }
        }
        return strArr;
    }
}
