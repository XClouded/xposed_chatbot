package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.ISharedPreference;
import android.content.SharedPreferences;

public class UNWSharePreference implements ISharedPreference {
    private SharedPreferences.Editor mEditor;

    public void init() {
    }

    public boolean contains(String str, String str2) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).contains(str2);
    }

    public boolean getBoolean(String str, String str2, boolean z) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).getBoolean(str2, z);
    }

    public int getInt(String str, String str2, int i) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).getInt(str2, i);
    }

    public long getLong(String str, String str2, long j) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).getLong(str2, j);
    }

    public String getString(String str, String str2, String str3) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).getString(str2, str3);
    }

    public SharedPreferences.Editor putBoolean(String str, String str2, boolean z) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).edit().putBoolean(str2, z);
    }

    public SharedPreferences.Editor putInt(String str, String str2, int i) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).edit().putInt(str2, i);
    }

    public SharedPreferences.Editor putLong(String str, String str2, long j) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).edit().putLong(str2, j);
    }

    public SharedPreferences.Editor putString(String str, String str2, String str3) {
        return UNWManager.getInstance().application.getSharedPreferences(str, 0).edit().putString(str2, str3);
    }
}
