package com.taobao.android.dxcontainer.layout;

import android.content.Context;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper;

public class DXContainerLayoutStyleUtils {
    public static void bindMarginLayoutStyle(Context context, MarginLayoutHelper marginLayoutHelper, JSONObject jSONObject) {
        if (jSONObject != null && marginLayoutHelper != null) {
            JSONArray jSONArray = jSONObject.getJSONArray("margin");
            if (jSONArray != null) {
                String[] strArr = new String[4];
                int i = 0;
                while (i < 4 && i < jSONArray.size()) {
                    strArr[i] = jSONArray.getString(i);
                    i++;
                }
                int[] iArr = new int[4];
                for (int i2 = 0; i2 < 4; i2++) {
                    iArr[i2] = DXScreenTool.getPx(context, strArr[i2], 0);
                }
                marginLayoutHelper.setMargin(iArr[3], iArr[0], iArr[1], iArr[2]);
            }
            JSONArray jSONArray2 = jSONObject.getJSONArray("padding");
            if (jSONArray2 != null) {
                String[] strArr2 = new String[4];
                int i3 = 0;
                while (i3 < 4 && i3 < jSONArray2.size()) {
                    strArr2[i3] = jSONArray2.getString(i3);
                    i3++;
                }
                int[] iArr2 = new int[4];
                for (int i4 = 0; i4 < 4; i4++) {
                    iArr2[i4] = DXScreenTool.getPx(context, strArr2[i4], 0);
                }
                marginLayoutHelper.setPadding(iArr2[3], iArr2[0], iArr2[1], iArr2[2]);
            }
        }
    }
}
