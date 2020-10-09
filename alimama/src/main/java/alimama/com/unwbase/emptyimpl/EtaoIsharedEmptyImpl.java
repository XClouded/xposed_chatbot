package alimama.com.unwbase.emptyimpl;

import alimama.com.unwbase.interfaces.ISharedPreference;
import alimama.com.unwbase.tools.UNWLog;
import android.content.SharedPreferences;

public class EtaoIsharedEmptyImpl implements ISharedPreference {
    public boolean contains(String str, String str2) {
        return false;
    }

    public boolean getBoolean(String str, String str2, boolean z) {
        return false;
    }

    public int getInt(String str, String str2, int i) {
        return 0;
    }

    public long getLong(String str, String str2, long j) {
        return 0;
    }

    public String getString(String str, String str2, String str3) {
        return null;
    }

    public SharedPreferences.Editor putBoolean(String str, String str2, boolean z) {
        return null;
    }

    public SharedPreferences.Editor putInt(String str, String str2, int i) {
        return null;
    }

    public SharedPreferences.Editor putLong(String str, String str2, long j) {
        return null;
    }

    public SharedPreferences.Editor putString(String str, String str2, String str3) {
        return null;
    }

    public void init() {
        UNWLog.error("请注入sharedpreference实现 registerService(ISharedPreference.class,new UNWSharePreference());");
    }
}
