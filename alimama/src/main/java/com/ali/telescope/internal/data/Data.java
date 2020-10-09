package com.ali.telescope.internal.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    private boolean isChanged = false;
    private final String mID;
    private Map<String, Object> mMap;
    private final Data mParent;

    public Data(String str, Data data) {
        this.mID = str;
        this.mParent = data;
    }

    public String getID() {
        return this.mID;
    }

    public Map<String, Object> getMap() {
        return this.mMap;
    }

    public void putData(String str, Object obj) {
        if (this.mMap == null) {
            this.mMap = new ConcurrentHashMap();
        }
        Object obj2 = this.mMap.get(str);
        if (obj2 == null || !obj2.equals(obj)) {
            this.mMap.put(str, obj);
            changed();
        }
    }

    public Object getData(String str) {
        if (this.mMap == null) {
            return null;
        }
        return this.mMap.get(str);
    }

    /* access modifiers changed from: protected */
    public void changed() {
        this.isChanged = true;
        if (this.mParent != null) {
            this.mParent.changed();
        }
    }

    public Data getParent() {
        return this.mParent;
    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public void setChanged(boolean z) {
        this.isChanged = z;
    }

    public String toString() {
        return this.mID;
    }
}
