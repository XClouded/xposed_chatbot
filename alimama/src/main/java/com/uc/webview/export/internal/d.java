package com.uc.webview.export.internal;

import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.utility.Log;
import java.util.Arrays;

/* compiled from: U4Source */
public class d implements InvokeObject {
    private static String a = "d";
    private static d b;
    private InvokeObject c;

    private d(InvokeObject invokeObject) {
        this.c = invokeObject;
        this.c.invoke(101, new Object[]{this});
    }

    public static void a(InvokeObject invokeObject) {
        if (b == null) {
            b = new d(invokeObject);
        }
    }

    public Object invoke(int i, Object[] objArr) {
        String str = a;
        Log.d(str, "invoke.case.id: " + i + " params: " + Arrays.toString(objArr));
        switch (i) {
            case 201:
                if (objArr != null && objArr.length == 1) {
                    int intValue = objArr[0].intValue();
                    String str2 = a;
                    Log.d(str2, "onCoreClearRecord value: " + intValue);
                }
                return null;
            case 202:
                if (objArr != null && objArr.length == 1) {
                    boolean booleanValue = objArr[0].booleanValue();
                    String str3 = a;
                    Log.d(str3, "onProxySettingChanged value: " + booleanValue);
                }
                return null;
            default:
                return null;
        }
    }
}
