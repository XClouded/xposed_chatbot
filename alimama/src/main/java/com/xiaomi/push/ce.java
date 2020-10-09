package com.xiaomi.push;

import android.os.Bundle;
import org.json.JSONObject;

public class ce {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public long f173a;

    /* renamed from: a  reason: collision with other field name */
    private String f174a;
    public int b;

    /* renamed from: b  reason: collision with other field name */
    public long f175b;
    public int c;
    public int d;
    public int e;
    public String h;

    public ce() {
    }

    public ce(ce ceVar) {
        this.f173a = ceVar.f173a;
        this.a = ceVar.a;
        this.h = ceVar.h;
        this.b = ceVar.b;
        this.c = ceVar.c;
        this.f175b = ceVar.f175b;
        this.d = ceVar.d;
        this.f174a = ceVar.f174a;
        this.e = ceVar.e;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        bundle.putLong("id", this.f173a);
        bundle.putInt("showType", this.a);
        bundle.putInt("nonsense", this.b);
        bundle.putInt("receiveUpperBound", this.c);
        bundle.putLong("lastShowTime", this.f175b);
        bundle.putInt("multi", this.e);
        return bundle;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m148a() {
        return this.f174a;
    }

    public void a(String str) {
        this.f174a = str;
    }

    public void a(JSONObject jSONObject) {
        this.f173a = jSONObject.optLong("id");
        this.a = jSONObject.optInt("showType");
        this.b = jSONObject.optInt("nonsense");
        this.c = jSONObject.optInt("receiveUpperBound");
        this.f175b = jSONObject.optLong("lastShowTime");
        this.e = jSONObject.optInt("multi");
    }

    public String toString() {
        return "";
    }
}
