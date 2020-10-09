package com.xiaomi.push;

import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import com.xiaomi.channel.commonutils.logger.b;
import java.io.File;

public class cc {
    public static long a(String str) {
        if (Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            if (new File(str).exists()) {
                return Os.stat(str).st_size;
            }
            return 0;
        } catch (ErrnoException e) {
            b.a((Throwable) e);
            return 0;
        }
    }
}
