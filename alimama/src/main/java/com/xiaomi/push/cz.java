package com.xiaomi.push;

import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

class cz implements Comparable<cz> {
    protected int a;

    /* renamed from: a  reason: collision with other field name */
    private long f209a;

    /* renamed from: a  reason: collision with other field name */
    String f210a;

    /* renamed from: a  reason: collision with other field name */
    private final LinkedList<cp> f211a;

    public cz() {
        this((String) null, 0);
    }

    public cz(String str) {
        this(str, 0);
    }

    public cz(String str, int i) {
        this.f211a = new LinkedList<>();
        this.f209a = 0;
        this.f210a = str;
        this.a = i;
    }

    /* renamed from: a */
    public int compareTo(cz czVar) {
        if (czVar == null) {
            return 1;
        }
        return czVar.a - this.a;
    }

    public synchronized cz a(JSONObject jSONObject) {
        this.f209a = jSONObject.getLong("tt");
        this.a = jSONObject.getInt("wt");
        this.f210a = jSONObject.getString("host");
        JSONArray jSONArray = jSONObject.getJSONArray("ah");
        for (int i = 0; i < jSONArray.length(); i++) {
            this.f211a.add(new cp().a(jSONArray.getJSONObject(i)));
        }
        return this;
    }

    public synchronized JSONObject a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put("tt", this.f209a);
        jSONObject.put("wt", this.a);
        jSONObject.put("host", this.f210a);
        JSONArray jSONArray = new JSONArray();
        Iterator it = this.f211a.iterator();
        while (it.hasNext()) {
            jSONArray.put(((cp) it.next()).a());
        }
        jSONObject.put("ah", jSONArray);
        return jSONObject;
    }

    /* access modifiers changed from: protected */
    public synchronized void a(cp cpVar) {
        if (cpVar != null) {
            this.f211a.add(cpVar);
            int a2 = cpVar.a();
            if (a2 > 0) {
                this.a += cpVar.a();
            } else {
                int i = 0;
                int size = this.f211a.size() - 1;
                while (size >= 0 && this.f211a.get(size).a() < 0) {
                    i++;
                    size--;
                }
                this.a += a2 * i;
            }
            if (this.f211a.size() > 30) {
                this.a -= this.f211a.remove().a();
            }
        }
    }

    public String toString() {
        return this.f210a + ":" + this.a;
    }
}
