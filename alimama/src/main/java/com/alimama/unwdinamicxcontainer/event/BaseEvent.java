package com.alimama.unwdinamicxcontainer.event;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEvent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;

public class BaseEvent implements IDXEvent {
    public JSONObject fields;
    public String type;

    public void executeEvent(String str) {
        try {
            JSONObject parseObject = JSON.parseObject(str);
            this.type = parseObject.getString("type");
            this.fields = parseObject.getJSONObject(ProtocolConst.KEY_FIELDS);
            IEvent iEvent = (IEvent) UNWManager.getInstance().getService(IEvent.class);
            if (iEvent != null) {
                iEvent.execute(this.type, adapterProtocol(), null);
            }
        } catch (Exception e) {
            UNWManager.getInstance().getLogger().error("BaseEvent", "initDXContainerEngine", e.toString());
        }
    }

    public JSONObject adapterProtocol() {
        return this.fields;
    }
}
