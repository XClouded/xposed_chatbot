package com.huawei.updatesdk.service.a;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.updatesdk.sdk.a.c.a.a.a;

public class b {
    protected SharedPreferences a;

    public b(SharedPreferences sharedPreferences) {
        this.a = sharedPreferences;
    }

    public static b a(String str, Context context) {
        return new b(context.getSharedPreferences(str, 0));
    }

    public void a(String str, long j) {
        try {
            SharedPreferences.Editor edit = this.a.edit();
            edit.putLong(str, j);
            edit.commit();
        } catch (Exception e) {
            a.a("SharedPreferencesWrapper", "putLong error!!key:" + str, e);
        }
    }

    public void a(String str, String str2) {
        try {
            SharedPreferences.Editor edit = this.a.edit();
            edit.putString(str, str2);
            edit.commit();
        } catch (Exception e) {
            a.a("SharedPreferencesWrapper", "putString error!!key:" + str, e);
        }
    }

    public long b(String str, long j) {
        try {
            return this.a.getLong(str, j);
        } catch (Exception unused) {
            this.a.edit().remove(str).commit();
            return j;
        }
    }

    public String b(String str, String str2) {
        try {
            return this.a.getString(str, str2);
        } catch (Exception unused) {
            this.a.edit().remove(str).commit();
            return str2;
        }
    }
}
