package com.xiaomi.push;

import android.os.AsyncTask;

class ck extends AsyncTask<String, Integer, Integer> {
    ch a;

    /* renamed from: a  reason: collision with other field name */
    co f187a;

    /* renamed from: a  reason: collision with other field name */
    String f188a;
    String b;

    public ck(co coVar, String str, String str2, ch chVar) {
        this.f188a = str;
        this.b = str2;
        this.f187a = coVar;
        this.a = chVar;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public Integer doInBackground(String... strArr) {
        return Integer.valueOf(cl.a(this.f188a, this.b, this.a));
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(Integer num) {
        super.onPostExecute(num);
        if (this.f187a != null) {
            this.f187a.a(num, this.a);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        super.onCancelled();
        if (this.f187a != null) {
            this.f187a.a(1, this.a);
        }
    }
}
