package com.taobao.android.ultron.common.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import java.lang.reflect.Field;

public class WriteRenderDataSwitch {
    public static final String KEY_WRITE_RENDER_DATA = "writeRenderData";

    public static String processSwitch(Context context, Uri uri) {
        String queryParameter;
        if (context == null || uri == null || (queryParameter = uri.getQueryParameter(KEY_WRITE_RENDER_DATA)) == null) {
            return null;
        }
        boolean equals = Boolean.TRUE.toString().equals(queryParameter);
        String str = "开关状态: writeRenderData is " + queryParameter;
        try {
            Field declaredField = Class.forName("com.alibaba.android.ultron.vfw.core.ViewEngine").getDeclaredField("sWriteRenderData");
            declaredField.setAccessible(true);
            declaredField.setBoolean((Object) null, equals);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        Toast.makeText(context.getApplicationContext(), str, 0).show();
        return str;
    }
}
