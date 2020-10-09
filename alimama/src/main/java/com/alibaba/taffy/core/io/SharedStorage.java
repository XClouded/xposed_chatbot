package com.alibaba.taffy.core.io;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedStorage {
    private SharedPreferences storage;

    public SharedStorage(Context context, String str) {
        this.storage = context.getSharedPreferences(str, 0);
    }

    public void put(String str, String str2) {
        SharedPreferences.Editor edit = this.storage.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public String get(String str) {
        return this.storage.getString(str, (String) null);
    }

    public String get(String str, String str2) {
        return this.storage.getString(str, str2);
    }
}
