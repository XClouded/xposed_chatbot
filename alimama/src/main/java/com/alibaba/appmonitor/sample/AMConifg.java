package com.alibaba.appmonitor.sample;

import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.utils.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AMConifg extends Entity implements Cloneable {
    @Column("module")
    protected String module;
    @Column("mp")
    protected String monitorPoint;
    @Column("offline")
    protected String offline;
    @Ingore
    private HashMap<String, AMConifg> relationMap;
    @Column("cp")
    private int sampling;

    public boolean isSampled(int i, String str, String str2, Map<String, String> map) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(str);
        arrayList.add(str2);
        return sampling(i, arrayList);
    }

    private boolean sampling(int i, ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return checkSelfSampling(i);
        }
        String remove = arrayList.remove(0);
        if (isContains(remove)) {
            return this.relationMap.get(remove).sampling(i, arrayList);
        }
        return checkSelfSampling(i);
    }

    /* access modifiers changed from: protected */
    public boolean checkSelfSampling(int i) {
        Logger.d("sampling", "module", this.module, "monitorPoint", this.monitorPoint, "samplingSeed", Integer.valueOf(i), "sampling", Integer.valueOf(this.sampling));
        if (i < this.sampling) {
            return true;
        }
        return false;
    }

    public synchronized void add(String str, AMConifg aMConifg) {
        if (this.relationMap == null) {
            this.relationMap = new HashMap<>();
        }
        if (isContains(str)) {
            AMConifg aMConifg2 = this.relationMap.get(str);
            if (!(aMConifg2 == null || aMConifg2.relationMap == null || aMConifg.relationMap == null)) {
                aMConifg.relationMap.putAll(aMConifg2.relationMap);
            }
            Logger.w("config object order errror", "config:", aMConifg + "");
        }
        this.relationMap.put(str, aMConifg);
    }

    /* access modifiers changed from: protected */
    public synchronized boolean isContains(String str) {
        if (this.relationMap == null) {
            return false;
        }
        return this.relationMap.containsKey(str);
    }

    public synchronized AMConifg getOrBulidNext(String str) {
        AMConifg next;
        next = getNext(str);
        if (next == null) {
            try {
                AMConifg aMConifg = (AMConifg) clone();
                try {
                    aMConifg.module = str;
                    next = aMConifg;
                } catch (CloneNotSupportedException e) {
                    AMConifg aMConifg2 = aMConifg;
                    e = e;
                    next = aMConifg2;
                    e.printStackTrace();
                    this.relationMap.put(str, next);
                    return next;
                }
            } catch (CloneNotSupportedException e2) {
                e = e2;
                e.printStackTrace();
                this.relationMap.put(str, next);
                return next;
            }
        }
        this.relationMap.put(str, next);
        return next;
    }

    public synchronized AMConifg getNext(String str) {
        if (this.relationMap == null) {
            this.relationMap = new HashMap<>();
        }
        return this.relationMap.get(str);
    }

    public String getModule() {
        return this.module;
    }

    public void setSampling(int i) {
        this.sampling = i;
    }

    public boolean isOffline(String str, String str2) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(str);
        arrayList.add(str2);
        return isOffline(arrayList);
    }

    private boolean isOffline(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return checkSelfOffline();
        }
        String remove = arrayList.remove(0);
        if (isContains(remove)) {
            return this.relationMap.get(remove).isOffline(arrayList);
        }
        return checkSelfOffline();
    }

    private boolean checkSelfOffline() {
        return "1".equalsIgnoreCase(this.offline);
    }

    public void enableOffline() {
        this.offline = "1";
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Deprecated
    public void enableOffline(boolean z) {
        if (z) {
            this.offline = "1";
        } else {
            this.offline = null;
        }
    }
}
