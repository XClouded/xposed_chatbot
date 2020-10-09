package com.huawei.hms.c;

import android.content.Context;
import com.huawei.hms.support.log.a;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/* compiled from: FileUtil */
public abstract class b {
    private static boolean a = false;
    private static ScheduledExecutorService b = Executors.newSingleThreadScheduledExecutor();

    public static void a(File file, String str, long j) {
        b.execute(new c(file, j, str));
    }

    public static void a(Context context, File file, File file2, String str, long j, int i) {
        if (file != null && file.isFile() && file.exists()) {
            if (!a) {
                if (file2 != null && file2.exists() && !file2.delete()) {
                    a.d("FileUtil", "file delete failed.");
                }
                a = true;
            }
            a(file2, str + "|" + j + "|" + i, 10240);
        }
    }
}
