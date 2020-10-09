package com.xiaomi.push;

import android.content.ContentValues;
import android.content.Context;
import com.xiaomi.push.bw;

public class bt extends bw.e {
    private String a = "MessageInsertJob";

    public bt(String str, ContentValues contentValues, String str2) {
        super(str, contentValues);
        this.a = str2;
    }

    public static bt a(Context context, String str, hk hkVar) {
        byte[] a2 = iq.a(hkVar);
        if (a2 == null || a2.length <= 0) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        contentValues.put("messageId", "");
        contentValues.put("messageItemId", hkVar.d());
        contentValues.put("messageItem", a2);
        contentValues.put("appId", bk.a(context).b());
        contentValues.put("packageName", bk.a(context).a());
        contentValues.put("createTimeStamp", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("uploadTimestamp", 0);
        return new bt(str, contentValues, "a job build to insert message to db");
    }
}
