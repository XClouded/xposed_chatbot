package com.coloros.mcssdk.a;

import android.content.Context;
import android.content.Intent;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.c.b;
import com.coloros.mcssdk.c.d;
import com.coloros.mcssdk.mode.Message;
import java.util.ArrayList;
import java.util.List;

public abstract class c implements d {
    public static List<Message> a(Context context, Intent intent) {
        Message a;
        if (intent == null) {
            return null;
        }
        int i = 4096;
        try {
            i = Integer.parseInt(b.a(intent.getStringExtra("type")));
        } catch (Exception e) {
            d.b("MessageParser--getMessageByIntent--Exception:" + e.getMessage());
        }
        d.a("MessageParser--getMessageByIntent--type:" + i);
        ArrayList arrayList = new ArrayList();
        for (d next : PushManager.getInstance().getParsers()) {
            if (!(next == null || (a = next.a(context, i, intent)) == null)) {
                arrayList.add(a);
            }
        }
        return arrayList;
    }

    public abstract Message a(Intent intent);
}
