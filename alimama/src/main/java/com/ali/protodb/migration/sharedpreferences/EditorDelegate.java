package com.ali.protodb.migration.sharedpreferences;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.ali.protodb.lsdb.Iterator;
import com.ali.protodb.lsdb.Key;
import com.ali.protodb.lsdb.LSDB;
import java.util.Set;

public class EditorDelegate implements SharedPreferences.Editor {
    private final LSDB lsdb;
    private final String name;

    public void apply() {
    }

    public boolean commit() {
        return true;
    }

    public EditorDelegate(LSDB lsdb2, String str) {
        this.lsdb = lsdb2;
        this.name = str;
    }

    public SharedPreferences.Editor putString(String str, @Nullable String str2) {
        this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), str2);
        return this;
    }

    public SharedPreferences.Editor putStringSet(String str, @Nullable Set<String> set) {
        if (set != null) {
            StringBuilder sb = new StringBuilder();
            for (String append : set) {
                sb.append(append);
                sb.append(",");
            }
            this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), sb.toString());
        }
        return this;
    }

    public SharedPreferences.Editor putInt(String str, int i) {
        this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), String.valueOf(i));
        return this;
    }

    public SharedPreferences.Editor putLong(String str, long j) {
        this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), String.valueOf(j));
        return this;
    }

    public SharedPreferences.Editor putFloat(String str, float f) {
        this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), String.valueOf(f));
        return this;
    }

    public SharedPreferences.Editor putBoolean(String str, boolean z) {
        this.lsdb.insertString(SharedPreferencesDelegate.wrapKey(this.name, str), String.valueOf(z));
        return this;
    }

    public SharedPreferences.Editor remove(String str) {
        this.lsdb.delete(SharedPreferencesDelegate.wrapKey(this.name, str));
        return this;
    }

    public SharedPreferences.Editor clear() {
        Iterator<Key> keyIterator = this.lsdb.keyIterator();
        while (true) {
            Key next = keyIterator.next();
            if (next == null) {
                return this;
            }
            if (next.getStringKey().startsWith(this.name)) {
                this.lsdb.delete(next);
            }
        }
    }
}
