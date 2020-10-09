package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class u {
    private static final Set<String> a = Collections.synchronizedSet(new HashSet());

    /* renamed from: a  reason: collision with other field name */
    private Context f941a;

    /* renamed from: a  reason: collision with other field name */
    private RandomAccessFile f942a;

    /* renamed from: a  reason: collision with other field name */
    private String f943a;

    /* renamed from: a  reason: collision with other field name */
    private FileLock f944a;

    private u(Context context) {
        this.f941a = context;
    }

    /* JADX INFO: finally extract failed */
    public static u a(Context context, File file) {
        b.c("Locking: " + file.getAbsolutePath());
        String str = file.getAbsolutePath() + ".LOCK";
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.getParentFile().mkdirs();
            file2.createNewFile();
        }
        if (a.add(str)) {
            u uVar = new u(context);
            uVar.f943a = str;
            try {
                uVar.f942a = new RandomAccessFile(file2, "rw");
                uVar.f944a = uVar.f942a.getChannel().lock();
                b.c("Locked: " + str + " :" + uVar.f944a);
                if (uVar.f944a == null) {
                    if (uVar.f942a != null) {
                        y.a((Closeable) uVar.f942a);
                    }
                    a.remove(uVar.f943a);
                }
                return uVar;
            } catch (Throwable th) {
                if (uVar.f944a == null) {
                    if (uVar.f942a != null) {
                        y.a((Closeable) uVar.f942a);
                    }
                    a.remove(uVar.f943a);
                }
                throw th;
            }
        } else {
            throw new IOException("abtain lock failure");
        }
    }

    public void a() {
        b.c("unLock: " + this.f944a);
        if (this.f944a != null && this.f944a.isValid()) {
            try {
                this.f944a.release();
            } catch (IOException unused) {
            }
            this.f944a = null;
        }
        if (this.f942a != null) {
            y.a((Closeable) this.f942a);
        }
        a.remove(this.f943a);
    }
}
