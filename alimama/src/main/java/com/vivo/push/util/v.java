package com.vivo.push.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

/* compiled from: SettingsCache */
final class v implements c {
    private ContentResolver a;

    v() {
    }

    public final boolean a(Context context) {
        if (!k.b()) {
            return false;
        }
        this.a = context.getContentResolver();
        return true;
    }

    public final String a(String str, String str2) {
        try {
            return Settings.System.getString(this.a, str);
        } catch (Exception e) {
            e.printStackTrace();
            p.b("SettingsCache", "getString error by " + str);
            return str2;
        }
    }

    public final void b(String str, String str2) {
        try {
            Settings.System.putString(this.a, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            p.b("SettingsCache", "putString error by " + str);
        }
    }
}
