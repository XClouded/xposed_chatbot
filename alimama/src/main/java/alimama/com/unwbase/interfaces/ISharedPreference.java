package alimama.com.unwbase.interfaces;

import android.content.SharedPreferences;

public interface ISharedPreference extends IInitAction {
    boolean contains(String str, String str2);

    boolean getBoolean(String str, String str2, boolean z);

    int getInt(String str, String str2, int i);

    long getLong(String str, String str2, long j);

    String getString(String str, String str2, String str3);

    SharedPreferences.Editor putBoolean(String str, String str2, boolean z);

    SharedPreferences.Editor putInt(String str, String str2, int i);

    SharedPreferences.Editor putLong(String str, String str2, long j);

    SharedPreferences.Editor putString(String str, String str2, String str3);
}
