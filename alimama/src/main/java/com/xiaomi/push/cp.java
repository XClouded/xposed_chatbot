package com.xiaomi.push;

import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import org.json.JSONObject;

public class cp {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private long f189a;

    /* renamed from: a  reason: collision with other field name */
    private String f190a;
    private long b;
    private long c;

    public cp() {
        this(0, 0, 0, (Exception) null);
    }

    public cp(int i, long j, long j2, Exception exc) {
        this.a = i;
        this.f189a = j;
        this.c = j2;
        this.b = System.currentTimeMillis();
        if (exc != null) {
            this.f190a = exc.getClass().getSimpleName();
        }
    }

    public int a() {
        return this.a;
    }

    public cp a(JSONObject jSONObject) {
        this.f189a = jSONObject.getLong("cost");
        this.c = jSONObject.getLong("size");
        this.b = jSONObject.getLong("ts");
        this.a = jSONObject.getInt("wt");
        this.f190a = jSONObject.optString(ExperimentGroupPO.TYPE_AB_EXPERIMENT);
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public JSONObject m152a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("cost", this.f189a);
        jSONObject.put("size", this.c);
        jSONObject.put("ts", this.b);
        jSONObject.put("wt", this.a);
        jSONObject.put(ExperimentGroupPO.TYPE_AB_EXPERIMENT, this.f190a);
        return jSONObject;
    }
}
