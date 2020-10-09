package com.ali.ha.datahub;

import java.util.HashMap;

public class KVBuilder {
    private boolean hasBuild = false;
    private HashMap<String, String> values = new HashMap<>();

    public static KVBuilder obtain() {
        return new KVBuilder();
    }

    public KVBuilder putKV(String str, String str2) {
        if (!this.hasBuild) {
            this.values.put(str, str2);
        }
        return this;
    }

    public HashMap<String, String> build() {
        this.hasBuild = true;
        return this.values;
    }
}
