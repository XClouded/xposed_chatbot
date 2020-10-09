package com.vivo.push.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import com.vivo.push.model.InsideNotificationItem;

/* compiled from: DefaultNotifyDataAdapter */
public final class i implements BaseNotifyDataAdapter {
    private Resources a;
    private String b;
    private String c;
    private String d;

    public final void init(Context context) {
        String str;
        this.b = context.getPackageName();
        this.a = context.getResources();
        this.c = k.a();
        String str2 = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(str2)) {
            str = null;
        } else {
            str = str2.replace(".", "");
        }
        this.d = str;
    }

    public final int getDefaultNotifyIcon() {
        int i;
        String str = this.d;
        while (true) {
            if (Build.VERSION.SDK_INT < 26) {
                break;
            } else if (TextUtils.isEmpty(str)) {
                p.d("DefaultNotifyDataAdapter", "systemVersion is not suit ");
                break;
            } else {
                String str2 = "vivo_push_ard" + str + "_notifyicon";
                i = this.a.getIdentifier(str2, "drawable", this.b);
                if (i > 0) {
                    p.d("DefaultNotifyDataAdapter", "get notify icon : " + str2);
                    break;
                }
                p.d("DefaultNotifyDataAdapter", "get notify error icon : " + str2);
                str = str.substring(0, str.length() + -1);
            }
        }
        i = -1;
        if (i != -1) {
            return i;
        }
        return a(this.c);
    }

    public final int getDefaultSmallIconId() {
        int i;
        String str = this.d;
        while (true) {
            if (Build.VERSION.SDK_INT < 26) {
                break;
            } else if (TextUtils.isEmpty(str)) {
                p.d("DefaultNotifyDataAdapter", "systemVersion is not suit ");
                break;
            } else {
                String str2 = "vivo_push_ard" + str + "_icon";
                i = this.a.getIdentifier(str2, "drawable", this.b);
                if (i > 0) {
                    p.d("DefaultNotifyDataAdapter", "get small icon : " + str2);
                    break;
                }
                p.d("DefaultNotifyDataAdapter", "get small error icon : " + str2);
                str = str.substring(0, str.length() + -1);
            }
        }
        i = -1;
        if (i != -1) {
            return i;
        }
        return b(this.c);
    }

    private int a(String str) {
        while (!TextUtils.isEmpty(str)) {
            Resources resources = this.a;
            int identifier = resources.getIdentifier("vivo_push_rom" + str + "_notifyicon", "drawable", this.b);
            if (identifier > 0) {
                return identifier;
            }
            str = str.substring(0, str.length() - 1);
        }
        return this.a.getIdentifier("vivo_push_notifyicon", "drawable", this.b);
    }

    private int b(String str) {
        while (!TextUtils.isEmpty(str)) {
            Resources resources = this.a;
            int identifier = resources.getIdentifier("vivo_push_rom" + str + "_icon", "drawable", this.b);
            if (identifier > 0) {
                return identifier;
            }
            str = str.substring(0, str.length() - 1);
        }
        return this.a.getIdentifier("vivo_push_icon", "drawable", this.b);
    }

    public final int getNotifyMode(InsideNotificationItem insideNotificationItem) {
        return Build.VERSION.SDK_INT >= 21 ? 2 : 1;
    }
}
