package com.alimama.unionwl.utils;

import android.app.Application;
import android.content.SharedPreferences;

public class ISSharedPreferences {
    private static Application sApplication;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public static void init(Application application) {
        sApplication = application;
    }

    public ISSharedPreferences(String str) {
        if (sApplication != null) {
            this.mPreferences = sApplication.getSharedPreferences(str, 0);
            return;
        }
        throw new IllegalArgumentException("Application to be null");
    }

    public boolean contains(String str) {
        return this.mPreferences.contains(str);
    }

    public SharedPreferences.Editor edit() {
        if (this.mEditor == null) {
            this.mEditor = this.mPreferences.edit();
        }
        return this.mEditor;
    }

    public boolean getBoolean(String str, boolean z) {
        return this.mPreferences.getBoolean(str, z);
    }

    public int getInt(String str, int i) {
        return this.mPreferences.getInt(str, i);
    }

    public String getString(String str, String str2) {
        return this.mPreferences.getString(str, str2);
    }

    public SharedPreferences.Editor putBoolean(String str, boolean z) {
        if (this.mEditor == null) {
            edit();
        }
        return this.mEditor.putBoolean(str, z);
    }

    public SharedPreferences.Editor putInt(String str, int i) {
        if (this.mEditor == null) {
            edit();
        }
        return this.mEditor.putInt(str, i);
    }

    public SharedPreferences.Editor putString(String str, String str2) {
        if (this.mEditor == null) {
            edit();
        }
        return this.mEditor.putString(str, str2);
    }

    public void apply() {
        if (this.mEditor == null) {
            edit();
        }
        this.mEditor.apply();
    }
}
