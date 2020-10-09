package com.ali.telescope.internal.pluginengine.model;

import org.json.JSONObject;

public class PluginData {
    public boolean enable;
    public String name;
    public JSONObject params;

    public PluginData(String str, boolean z, JSONObject jSONObject) {
        this.name = str;
        this.enable = z;
        this.params = jSONObject;
    }

    public PluginData(String str, boolean z) {
        this(str, z, (JSONObject) null);
    }
}
