package com.ali.protodb.migration.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.ali.protodb.lsdb.Iterator;
import com.ali.protodb.lsdb.Key;
import com.ali.protodb.lsdb.LSDB;
import com.ali.protodb.lsdb.LSDBConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesDelegate implements SharedPreferences {
    static final String SP_DELEGATE_MODULE_NAME = "sp_delegate";
    private static volatile LSDB lsdb;
    private final String name;

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    public SharedPreferencesDelegate(Context context, String str) {
        this.name = str;
        if (lsdb == null) {
            synchronized (SharedPreferencesDelegate.class) {
                if (lsdb == null) {
                    lsdb = LSDB.open(context, SP_DELEGATE_MODULE_NAME, (LSDBConfig) null);
                }
            }
        }
    }

    public static LSDB getLsdb() {
        return lsdb;
    }

    public Map<String, ?> getAll() {
        LSDB lsdb2 = lsdb;
        Key key = new Key(this.name + "-");
        Iterator<Key> keyIterator = lsdb2.keyIterator(key, new Key(this.name + "."));
        HashMap hashMap = new HashMap();
        if (keyIterator != null) {
            while (true) {
                Key next = keyIterator.next();
                if (next == null) {
                    break;
                } else if (next.getStringKey().startsWith(this.name)) {
                    hashMap.put(unwrapKey(this.name, next), lsdb.getString(next));
                }
            }
        }
        return hashMap;
    }

    @Nullable
    public String getString(String str, @Nullable String str2) {
        String string = lsdb.getString(wrapKey(this.name, str));
        return string != null ? string : str2;
    }

    @Nullable
    public Set<String> getStringSet(String str, @Nullable Set<String> set) {
        String string = lsdb.getString(wrapKey(this.name, str));
        if (string == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        hashSet.addAll(Arrays.asList(string.split(",")));
        return hashSet;
    }

    public int getInt(String str, int i) {
        try {
            return Integer.parseInt(lsdb.getString(wrapKey(this.name, str)));
        } catch (Exception unused) {
            return i;
        }
    }

    public long getLong(String str, long j) {
        try {
            return Long.parseLong(lsdb.getString(wrapKey(this.name, str)));
        } catch (Exception unused) {
            return j;
        }
    }

    public float getFloat(String str, float f) {
        try {
            return Float.parseFloat(lsdb.getString(wrapKey(this.name, str)));
        } catch (Exception unused) {
            return f;
        }
    }

    public boolean getBoolean(String str, boolean z) {
        try {
            return Boolean.parseBoolean(lsdb.getString(wrapKey(this.name, str)));
        } catch (Exception unused) {
            return z;
        }
    }

    public boolean contains(String str) {
        return lsdb.contains(wrapKey(this.name, str));
    }

    public SharedPreferences.Editor edit() {
        return new EditorDelegate(lsdb, this.name);
    }

    static Key wrapKey(String str, String str2) {
        return new Key(str + "-" + str2);
    }

    static String unwrapKey(String str, Key key) {
        String stringKey = key.getStringKey();
        return stringKey.replace(str + "-", "");
    }
}
