package com.taobao.android.dxcontainer.utils;

import android.util.SparseArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.render.LoadMoreRender;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;

public class DXContainerLoadMoreModelUtils {
    public static final String LOAD_MORE_VIEW_MODEL_ID_PRE = "container_load_more_";

    public static DXContainerModel getLoadMoreModel(int i) {
        DXContainerModel dXContainerModel = new DXContainerModel();
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("text", (Object) "");
        jSONObject2.put("state", (Object) 0);
        jSONObject.put(ProtocolConst.KEY_FIELDS, (Object) jSONObject2);
        jSONObject.put("id", (Object) LOAD_MORE_VIEW_MODEL_ID_PRE + i);
        dXContainerModel.setData(jSONObject);
        dXContainerModel.setRenderType(LoadMoreRender.RENDER_TYPE);
        dXContainerModel.setId(LOAD_MORE_VIEW_MODEL_ID_PRE + i);
        return dXContainerModel;
    }

    public static void updateState(DXContainerModel dXContainerModel, SparseArray<String> sparseArray, int i) {
        if (dXContainerModel != null && sparseArray != null) {
            dXContainerModel.getFields().put("text", (Object) sparseArray.get(i));
            dXContainerModel.getFields().put("state", (Object) Integer.valueOf(i));
        }
    }

    public static int getState(DXContainerModel dXContainerModel) {
        return dXContainerModel.getFields().getIntValue("state");
    }

    public static String getText(DXContainerModel dXContainerModel) {
        return dXContainerModel.getFields().getString("text");
    }
}
