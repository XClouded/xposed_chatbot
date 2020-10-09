package com.taobao.android.dinamicx.bindingx;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXMsgConstant;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.HashMap;

public class DXBindingXEventHandler extends DXAbsEventHandler {
    public static final long DX_EVENT_BINDINGX = 1454898448112604731L;

    public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        DXRootView rootView = dXRuntimeContext.getRootView();
        if (rootView != null && objArr != null && objArr.length != 0) {
            DXWidgetNode widgetNode = dXRuntimeContext.getWidgetNode();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", (Object) DXMsgConstant.DX_MSG_TYPE_BNDX);
            JSONObject jSONObject2 = new JSONObject();
            processArgs(jSONObject2, objArr);
            jSONObject2.put(DXMsgConstant.DX_MSG_WIDGET, (Object) widgetNode);
            jSONObject.put("params", (Object) jSONObject2);
            if (dXRuntimeContext != null && dXRuntimeContext.getEngineContext() != null) {
                dXRuntimeContext.getEngineContext().postMessage(rootView, jSONObject);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void processArgs(JSONObject jSONObject, Object[] objArr) {
        if (objArr != null && jSONObject != null) {
            int length = objArr.length;
            HashMap hashMap = null;
            if (objArr.length >= 2) {
                JSONArray jSONArray = new JSONArray();
                if (objArr[0] instanceof String) {
                    if (!TextUtils.isEmpty(objArr[0])) {
                        jSONArray.add(objArr[0]);
                    }
                } else if (objArr[0] instanceof JSONArray) {
                    jSONArray.addAll(objArr[0]);
                } else if (objArr[0] != null) {
                    jSONArray.add(objArr[0]);
                }
                jSONObject.put(DXMsgConstant.DX_MSG_SPEC, (Object) jSONArray);
                String str = objArr[1];
                if ("start".equalsIgnoreCase(str)) {
                    jSONObject.put("action", (Object) "start");
                } else if ("stop".equalsIgnoreCase(str)) {
                    jSONObject.put("action", (Object) "stop");
                }
            }
            for (int i = 2; i < length; i += 2) {
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                int i2 = i + 1;
                if (i2 >= length) {
                    break;
                }
                hashMap.put(objArr[i], objArr[i2]);
            }
            if (hashMap != null) {
                jSONObject.put("args", (Object) hashMap);
            }
        }
    }
}
