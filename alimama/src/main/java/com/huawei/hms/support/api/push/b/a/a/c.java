package com.huawei.hms.support.api.push.b.a.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.HashMap;
import java.util.Map;

/* compiled from: PushPreferences */
public class c {
    protected SharedPreferences a;

    public c(Context context, String str) {
        if (context != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                Context createDeviceProtectedStorageContext = context.createDeviceProtectedStorageContext();
                SharedPreferences sharedPreferences = createDeviceProtectedStorageContext.getSharedPreferences("move_to_de_records", 0);
                if (!sharedPreferences.getBoolean(str, false)) {
                    if (createDeviceProtectedStorageContext.moveSharedPreferencesFrom(context, str)) {
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean(str, true);
                        edit.apply();
                    }
                }
                context = createDeviceProtectedStorageContext;
            }
            this.a = context.getSharedPreferences(str, 0);
            return;
        }
        throw new NullPointerException("context is null!");
    }

    public boolean a(String str) {
        return this.a != null && this.a.getBoolean(str, false);
    }

    public String b(String str) {
        return this.a != null ? this.a.getString(str, "") : "";
    }

    public boolean a(String str, Object obj) {
        SharedPreferences.Editor edit = this.a.edit();
        if (obj instanceof String) {
            edit.putString(str, String.valueOf(obj));
        } else if (obj instanceof Integer) {
            edit.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Short) {
            edit.putInt(str, ((Short) obj).shortValue());
        } else if (obj instanceof Byte) {
            edit.putInt(str, ((Byte) obj).byteValue());
        } else if (obj instanceof Long) {
            edit.putLong(str, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            edit.putFloat(str, ((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            edit.putFloat(str, (float) ((Double) obj).doubleValue());
        } else if (obj instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue());
        }
        return edit.commit();
    }

    public boolean a(String str, String str2) {
        SharedPreferences.Editor edit;
        if (this.a == null || (edit = this.a.edit()) == null) {
            return false;
        }
        return edit.putString(str, str2).commit();
    }

    public void a(String str, Long l) {
        SharedPreferences.Editor edit;
        if (this.a != null && (edit = this.a.edit()) != null) {
            edit.putLong(str, l.longValue()).commit();
        }
    }

    public void a(String str, boolean z) {
        SharedPreferences.Editor edit;
        if (this.a != null && (edit = this.a.edit()) != null) {
            edit.putBoolean(str, z).commit();
        }
    }

    public boolean c(String str) {
        return this.a != null && this.a.contains(str);
    }

    public boolean d(String str) {
        SharedPreferences.Editor edit;
        if (this.a == null || !this.a.contains(str) || (edit = this.a.edit()) == null) {
            return false;
        }
        return edit.remove(str).commit();
    }

    public Map<String, ?> a() {
        if (this.a != null) {
            return this.a.getAll();
        }
        return new HashMap();
    }
}
