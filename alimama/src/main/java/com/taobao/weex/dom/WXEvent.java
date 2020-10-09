package com.taobao.weex.dom;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.dom.binding.JSONUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WXEvent extends ArrayList<String> implements Serializable, Cloneable {
    public static final String EVENT_KEY_ARGS = "params";
    public static final String EVENT_KEY_TYPE = "type";
    private static final long serialVersionUID = -8186587029452440107L;
    private ArrayMap mEventBindingArgs;
    private ArrayMap<String, List<Object>> mEventBindingArgsValues;

    public void clear() {
        if (this.mEventBindingArgs != null) {
            this.mEventBindingArgs.clear();
        }
        if (this.mEventBindingArgsValues != null) {
            this.mEventBindingArgsValues.clear();
        }
        super.clear();
    }

    public boolean remove(String str) {
        if (this.mEventBindingArgs != null) {
            this.mEventBindingArgs.remove(str);
        }
        if (this.mEventBindingArgsValues != null) {
            this.mEventBindingArgsValues.remove(str);
        }
        return super.remove(str);
    }

    public ArrayMap getEventBindingArgs() {
        return this.mEventBindingArgs;
    }

    public ArrayMap<String, List<Object>> getEventBindingArgsValues() {
        return this.mEventBindingArgsValues;
    }

    public void addEvent(Object obj) {
        if (obj instanceof CharSequence) {
            if (JSONUtils.isJSON(obj.toString())) {
                addEvent(JSONUtils.toJSON(obj.toString()));
                return;
            }
            String obj2 = obj.toString();
            if (!contains(obj2)) {
                add(obj2);
            }
        } else if (obj instanceof JSONObject) {
            addBindingEvent((JSONObject) obj);
        }
    }

    public static String getEventName(Object obj) {
        if (obj instanceof CharSequence) {
            return obj.toString();
        }
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).getString("type");
        }
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public void parseStatements() {
        if (!isEmpty()) {
            for (int i = 0; i < size(); i++) {
                String str = (String) get(i);
                if (JSONUtils.isJSON(str)) {
                    set(i, addBindingEvent(JSONUtils.toJSON(str)));
                }
            }
        }
    }

    private String addBindingEvent(JSONObject jSONObject) {
        String string = jSONObject.getString("type");
        Object obj = jSONObject.get("params");
        if (string != null) {
            addBindingArgsEvent(string, obj);
        }
        return string;
    }

    private void addBindingArgsEvent(String str, Object obj) {
        if (!contains(str)) {
            add(str);
        }
        if (obj != null) {
            if (this.mEventBindingArgs == null) {
                this.mEventBindingArgs = new ArrayMap();
            }
            this.mEventBindingArgs.put(str, ELUtils.bindingBlock(obj));
        }
    }

    public void putEventBindingArgsValue(String str, List<Object> list) {
        if (this.mEventBindingArgsValues == null) {
            this.mEventBindingArgsValues = new ArrayMap<>();
        }
        if (list == null) {
            this.mEventBindingArgsValues.remove(str);
        } else {
            this.mEventBindingArgsValues.put(str, list);
        }
    }

    public WXEvent clone() {
        WXEvent wXEvent = new WXEvent();
        wXEvent.addAll(this);
        if (this.mEventBindingArgs != null) {
            wXEvent.mEventBindingArgs = new ArrayMap((SimpleArrayMap) this.mEventBindingArgs);
        }
        wXEvent.mEventBindingArgsValues = null;
        return wXEvent;
    }
}
