package com.taobao.android.preview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import java.net.URLDecoder;
import mtopsdk.common.util.SymbolExpUtil;

public class DXPreviewUtil {
    public static void showPreview(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            String str2 = "";
            try {
                Log.e(DXTemplatePreviewActivity.PREVIEW_TAG, "url--->" + str);
                if (!TextUtils.isEmpty(str)) {
                    str2 = URLDecoder.decode(str, "UTF-8");
                }
                Log.e(DXTemplatePreviewActivity.PREVIEW_TAG, "result--->" + str2);
                if (!TextUtils.isEmpty(str2) && str2.contains("DinamicXTemplateDebug=")) {
                    Class<?> cls = Class.forName("com.taobao.android.dinamicx.dinamicx_debug_plugin.DXDebugController");
                    if (cls != null) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("DXUrl", (Object) str);
                        cls.getDeclaredMethod("show", new Class[]{Context.class, JSONObject.class}).invoke((Object) null, new Object[]{context, jSONObject});
                    }
                } else if (!TextUtils.isEmpty(str2) && str2.contains("templateMock=")) {
                    String queryParameter = Uri.parse(str2).getQueryParameter("previewParam");
                    String substring = queryParameter.substring(queryParameter.indexOf(SymbolExpUtil.SYMBOL_EQUAL) + 1);
                    Log.e(DXTemplatePreviewActivity.PREVIEW_TAG, "info-->:" + substring);
                    Intent intent = new Intent(context, DXTemplatePreviewActivity.class);
                    if (!(context instanceof Activity)) {
                        intent.addFlags(268435456);
                    }
                    intent.putExtra(DXTemplatePreviewActivity.PREVIEW_INFO, substring);
                    context.startActivity(intent);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
