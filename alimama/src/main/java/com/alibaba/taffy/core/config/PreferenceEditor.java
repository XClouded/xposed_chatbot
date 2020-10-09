package com.alibaba.taffy.core.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.taffy.core.util.TLog;
import java.util.Set;

public class PreferenceEditor {
    private static final String GLOBAL_SETTING_STORE = "GLOBAL_SETTING_STORE";
    private static final String TAG = "PreferenceEditor";
    private SharedPreferences sharedPreferences;

    public PreferenceEditor(Context context) {
        this(context, GLOBAL_SETTING_STORE, 0);
    }

    public PreferenceEditor(Context context, String str) {
        this(context, str, 0);
    }

    public PreferenceEditor(Context context, String str, int i) {
        if (context == null) {
            TLog.e(TAG, "new PreferenceEditor failed");
        } else {
            this.sharedPreferences = context.getSharedPreferences(str, i);
        }
    }

    public String getString(String str) {
        return getString(str, (String) null);
    }

    public String getString(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    public void putString(String str, String str2) {
        putString(str, str2, false);
    }

    public void putString(String str, String str2, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putString(str, str2);
        commitOrApply(edit, z);
    }

    public boolean getBoolean(String str) {
        return getBoolean(str, false);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.sharedPreferences.getBoolean(str, z);
    }

    public void putBoolean(String str, boolean z) {
        putBoolean(str, z, false);
    }

    public void putBoolean(String str, boolean z, boolean z2) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putBoolean(str, z);
        commitOrApply(edit, z2);
    }

    public int getInt(String str) {
        return getInt(str, 0);
    }

    public int getInt(String str, int i) {
        return this.sharedPreferences.getInt(str, i);
    }

    public void putInt(String str, int i) {
        putInt(str, i, false);
    }

    public void putInt(String str, int i, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putInt(str, i);
        commitOrApply(edit, z);
    }

    public float getFloat(String str) {
        return getFloat(str, 0.0f);
    }

    public float getFloat(String str, float f) {
        return this.sharedPreferences.getFloat(str, f);
    }

    public void putFloat(String str, float f) {
        putFloat(str, f, false);
    }

    public void putFloat(String str, float f, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putFloat(str, f);
        commitOrApply(edit, z);
    }

    public void putLong(String str, long j) {
        putLong(str, j, false);
    }

    public void putLong(String str, long j, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putLong(str, j);
        commitOrApply(edit, z);
    }

    public long getLong(String str) {
        return getLong(str, 0);
    }

    public long getLong(String str, long j) {
        return this.sharedPreferences.getLong(str, j);
    }

    public Set<String> getStringSet(String str) {
        return getStringSet(str, (Set<String>) null);
    }

    public Set<String> getStringSet(String str, Set<String> set) {
        return this.sharedPreferences.getStringSet(str, set);
    }

    public void putStringSet(String str, Set<String> set) {
        putStringSet(str, set, false);
    }

    public void putStringSet(String str, Set<String> set, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putStringSet(str, set);
        commitOrApply(edit, z);
    }

    public void clear(String str) {
        clear(str, false);
    }

    public void clear(String str, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.remove(str);
        commitOrApply(edit, z);
    }

    public void clearAll() {
        clearAll(false);
    }

    public void clearAll(boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.clear();
        commitOrApply(edit, z);
    }

    private void commitOrApply(SharedPreferences.Editor editor, boolean z) {
        if (z) {
            editor.commit();
        } else {
            editor.apply();
        }
    }
}
