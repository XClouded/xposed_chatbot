package com.taobao.android.dxcontainer;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.life.EngineModelExchangeListener;
import com.taobao.android.dxcontainer.render.NativeXRender;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.datamodel.imp.DMContext;
import com.taobao.android.ultron.datamodel.imp.ParseResponseHelper;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.ArrayList;

public class AliDXContainerDataChange {
    public static DXContainerModel exchange(JSONObject jSONObject, DMContext dMContext) {
        ParseResponseHelper parseResponseHelper = new ParseResponseHelper(dMContext);
        parseResponseHelper.parseResponse(jSONObject);
        if (parseResponseHelper.isSuccess()) {
            return exchange(dMContext.getRootComponent());
        }
        return null;
    }

    public static DXContainerModel exchange(JSONObject jSONObject) {
        return exchange(jSONObject, new DMContext(true));
    }

    public static IDMComponent exchange2DMComponent(JSONObject jSONObject, DMContext dMContext) {
        if (jSONObject == null || dMContext == null) {
            return null;
        }
        ParseResponseHelper parseResponseHelper = new ParseResponseHelper(dMContext);
        parseResponseHelper.parseResponse(jSONObject);
        if (parseResponseHelper.isSuccess()) {
            return dMContext.getRootComponent();
        }
        return null;
    }

    public static IDMComponent exchange2DMComponent(JSONObject jSONObject) {
        return exchange2DMComponent(jSONObject, new DMContext(true));
    }

    public static DXContainerModel exchange(IDMComponent iDMComponent) {
        return exchange(iDMComponent, (EngineModelExchangeListener) null);
    }

    public static DXContainerModel exchange(IDMComponent iDMComponent, EngineModelExchangeListener engineModelExchangeListener) {
        if (iDMComponent == null) {
            return null;
        }
        DXContainerModel dXContainerModel = new DXContainerModel();
        dXContainerModel.setData(iDMComponent.getData());
        dXContainerModel.setRenderType(iDMComponent.getContainerType());
        dXContainerModel.setLayoutType(iDMComponent.getLayoutType());
        dXContainerModel.setStyleModel(iDMComponent.getLayoutStyle());
        dXContainerModel.setId(iDMComponent.getKey());
        dXContainerModel.setTag(iDMComponent.getTag());
        DXTemplateItem buildDXTemplate = buildDXTemplate(iDMComponent.getContainerInfo());
        dXContainerModel.setTemplateItem(buildDXTemplate);
        if (engineModelExchangeListener != null) {
            engineModelExchangeListener.onTemplateCreated(buildDXTemplate);
        }
        if (iDMComponent.getChildren() != null) {
            ArrayList arrayList = new ArrayList();
            dXContainerModel.setChildren(arrayList);
            for (IDMComponent exchange : iDMComponent.getChildren()) {
                DXContainerModel exchange2 = exchange(exchange, engineModelExchangeListener);
                if (exchange2 != null) {
                    exchange2.setParent(dXContainerModel);
                    arrayList.add(exchange2);
                }
            }
        }
        if (engineModelExchangeListener != null) {
            engineModelExchangeListener.onDXCModelCreated(dXContainerModel);
        }
        return dXContainerModel;
    }

    public static DXTemplateItem buildDXTemplate(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        String string = jSONObject.getString(ProtocolConst.KEY_CONTAINER_TYPE);
        if ("dinamicx".equals(string)) {
            DXTemplateItem dXTemplateItem = new DXTemplateItem();
            dXTemplateItem.name = jSONObject.getString("name");
            dXTemplateItem.version = -1;
            String string2 = jSONObject.getString("version");
            if (!TextUtils.isEmpty(string2)) {
                try {
                    dXTemplateItem.version = Long.valueOf(string2).longValue();
                } catch (NumberFormatException unused) {
                }
            }
            if (dXTemplateItem.version == -1) {
                return null;
            }
            dXTemplateItem.templateUrl = jSONObject.getString("url");
            return dXTemplateItem;
        } else if (!NativeXRender.DEFAULT_RENDER_TYPE.equals(string)) {
            return null;
        } else {
            DXTemplateItem dXTemplateItem2 = new DXTemplateItem();
            dXTemplateItem2.name = jSONObject.getString("name");
            return dXTemplateItem2;
        }
    }
}
