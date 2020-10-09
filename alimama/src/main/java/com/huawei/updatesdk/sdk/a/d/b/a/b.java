package com.huawei.updatesdk.sdk.a.d.b.a;

import com.huawei.updatesdk.sdk.a.d.b.a.a;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class b {
    private static a.C0015a a = a.C0015a.MODE_SUPPORT_UNKNOWN;
    private static a b;

    public static a a() {
        b();
        b = a == a.C0015a.MODE_SUPPORT_MTK_GEMINI ? d.b() : c.b();
        return b;
    }

    public static boolean b() {
        a.C0015a aVar;
        if (a == a.C0015a.MODE_SUPPORT_UNKNOWN) {
            if (d()) {
                aVar = a.C0015a.MODE_SUPPORT_MTK_GEMINI;
            } else if (c()) {
                aVar = a.C0015a.MODE_SUPPORT_HW_GEMINI;
            } else {
                a = a.C0015a.MODE_NOT_SUPPORT_GEMINI;
            }
            a = aVar;
            return true;
        } else if (a == a.C0015a.MODE_SUPPORT_HW_GEMINI || a == a.C0015a.MODE_SUPPORT_MTK_GEMINI) {
            return true;
        }
        return false;
    }

    public static boolean c() {
        StringBuilder sb;
        String str;
        String str2;
        boolean z = false;
        try {
            Object c = c.c();
            if (c != null) {
                z = ((Boolean) c.getClass().getMethod("isMultiSimEnabled", new Class[0]).invoke(c, new Object[0])).booleanValue();
            }
        } catch (NoSuchMethodException e) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("MSimTelephonyManager.getDefault().isMultiSimEnabled()?");
            str2 = e.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isHwGeminiSupport1 " + z);
            return z;
        } catch (IllegalAccessException e2) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("MSimTelephonyManager.getDefault().isMultiSimEnabled()");
            str2 = e2.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isHwGeminiSupport1 " + z);
            return z;
        } catch (InvocationTargetException e3) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("MSimTelephonyManager.getDefault().isMultiSimEnabled()");
            str2 = e3.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, sb.toString());
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isHwGeminiSupport1 " + z);
            return z;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isHwGeminiSupport1 " + z);
        return z;
    }

    private static boolean d() {
        boolean z;
        StringBuilder sb;
        String str;
        String str2;
        try {
            Field declaredField = Class.forName("com.mediatek.common.featureoption.FeatureOption").getDeclaredField("MTK_GEMINI_SUPPORT");
            declaredField.setAccessible(true);
            z = declaredField.getBoolean((Object) null);
        } catch (ClassNotFoundException e) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("FeatureOption.MTK_GEMINI_SUPPORT");
            str2 = e.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str, sb.toString());
            z = false;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isMtkGeminiSupport " + z);
            return z;
        } catch (NoSuchFieldException e2) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("FeatureOption.MTK_GEMINI_SUPPORT");
            str2 = e2.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str, sb.toString());
            z = false;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isMtkGeminiSupport " + z);
            return z;
        } catch (IllegalAccessException e3) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("FeatureOption.MTK_GEMINI_SUPPORT");
            str2 = e3.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str, sb.toString());
            z = false;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isMtkGeminiSupport " + z);
            return z;
        } catch (Exception e4) {
            str = "mutiCardFactory";
            sb = new StringBuilder();
            sb.append("FeatureOption.MTK_GEMINI_SUPPORT");
            str2 = e4.toString();
            sb.append(str2);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str, sb.toString());
            z = false;
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isMtkGeminiSupport " + z);
            return z;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("mutiCardFactory", "isMtkGeminiSupport " + z);
        return z;
    }
}
