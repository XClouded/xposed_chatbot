package com.alimama.unwdinamicxcontainer.utils;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alimama.unwdinamicxcontainer.model.dxcengine.GlobalModel;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;

public class GlobalModelUtil {
    private static final String TAG = "GlobalModelUtil";

    public static void updateGlobalModel(String str, GlobalModel globalModel) {
        if (globalModel == null) {
            try {
                globalModel = new GlobalModel();
            } catch (Exception e) {
                IEtaoLogger logger = UNWManager.getInstance().getLogger();
                logger.error(TAG, "updateGlobalModel", "Exception: " + e.toString());
                return;
            }
        }
        JSONObject jSONObject = JSON.parseObject(str).getJSONObject("data").getJSONObject(ProtocolConst.KEY_GLOBAL);
        if (jSONObject != null) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("unwEvents");
            if (jSONObject2 != null) {
                globalModel.setUnwEvents(jSONObject2);
            }
            JSONObject jSONObject3 = jSONObject.getJSONObject("extendParams");
            if (jSONObject3 != null && jSONObject3.containsKey("unwSuccess")) {
                globalModel.getExtendParams().setUnwSuccess(jSONObject3.getString("unwSuccess"));
                if (TextUtils.equals(globalModel.getExtendParams().getUnwSuccess(), "true")) {
                    globalModel.setExtendParamsJsonData(jSONObject3.toJSONString());
                    if (jSONObject3.containsKey("loadMore")) {
                        globalModel.getExtendParams().setLoadMore(jSONObject3.getString("loadMore"));
                    }
                    if (jSONObject3.containsKey("emptyList")) {
                        globalModel.getExtendParams().setEmptyList(jSONObject3.getString("emptyList"));
                    }
                    if (jSONObject3.containsKey("forceUpdate")) {
                        globalModel.getExtendParams().setForceUpdate(jSONObject3.getString("forceUpdate"));
                    }
                }
            }
        }
    }
}
