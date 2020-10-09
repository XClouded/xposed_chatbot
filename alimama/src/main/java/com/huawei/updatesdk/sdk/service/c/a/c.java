package com.huawei.updatesdk.sdk.service.c.a;

import com.ali.user.mobile.rpc.ApiConstants;
import com.huawei.updatesdk.sdk.a.d.d;
import com.huawei.updatesdk.sdk.a.d.e;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class c {
    public static final String CLIENT_API = "tlsApis";
    private static final String END_FLAG = "_";
    private String method_;
    private String ver_ = ApiConstants.ApiField.VERSION_1_1;

    private String a(Field field) throws IllegalAccessException, IllegalArgumentException {
        Object obj = field.get(this);
        if (obj != null && (obj instanceof b)) {
            return ((b) obj).toJson();
        }
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;
    }

    public String d() throws IllegalAccessException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
        e();
        Map<String, Field> i = i();
        String[] strArr = new String[i.size()];
        i.keySet().toArray(strArr);
        Arrays.sort(strArr);
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        do {
            String a = a(i.get(strArr[i2]));
            if (a != null) {
                String b = e.b(a);
                sb.append(strArr[i2]);
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append(b);
                sb.append("&");
            }
            i2++;
        } while (i2 < strArr.length);
        int length = sb.length();
        if (length > 0) {
            int i3 = length - 1;
            if (sb.charAt(i3) == '&') {
                sb.deleteCharAt(i3);
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void e() {
    }

    public String g() {
        return this.method_;
    }

    public String h() {
        return "https://store.hispace.hicloud.com/hwmarket/api/tlsApis";
    }

    /* access modifiers changed from: protected */
    public Map<String, Field> i() {
        HashMap hashMap = new HashMap();
        for (Field field : d.a((Class) getClass())) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.endsWith("_")) {
                hashMap.put(name.substring(0, name.length() - "_".length()), field);
            }
        }
        return hashMap;
    }

    public void u(String str) {
        this.method_ = str;
    }

    public void v(String str) {
        this.ver_ = str;
    }
}
