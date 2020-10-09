package com.uc.crashsdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.e;
import com.uc.crashsdk.a.f;
import com.uc.crashsdk.a.g;
import java.io.File;

/* compiled from: ProGuard */
public class CrashLogFilesUploader extends Service {
    static final /* synthetic */ boolean a = (!CrashLogFilesUploader.class.desiredAssertionStatus());
    private boolean b = false;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!h.a()) {
            throw new RuntimeException("Must initialize crashsdk for current process (" + e.g() + AVFSCacheConstants.COMMA_SEP + Process.myPid() + "), or set CustomInfo.mUploadUcebuCrashLog as false!");
        } else if (this.b) {
            return 2;
        } else {
            this.b = true;
            f.a(1, (Runnable) new e(600, new Object[]{this}));
            return 2;
        }
    }

    public static void a(int i, Object[] objArr) {
        switch (i) {
            case 600:
                try {
                    a.b("Service uploading logs ...");
                    e.a(e.i(), true, true);
                } catch (Throwable th) {
                    g.a(th);
                }
                f.a(2, (Runnable) new e(SecExceptionCode.SEC_ERROR_SIGNATRUE_INVALID_INPUT, objArr));
                return;
            case SecExceptionCode.SEC_ERROR_SIGNATRUE_INVALID_INPUT:
                if (a || objArr != null) {
                    a.b("Crash log upload service done, exiting");
                    objArr[0].stopSelf();
                    Process.killProcess(Process.myPid());
                    return;
                }
                throw new AssertionError();
            default:
                if (!a) {
                    throw new AssertionError();
                }
                return;
        }
    }

    public static void a(Context context) {
        String T = h.T();
        File file = new File(T);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                a.c("Ucebu can not list folder: " + T);
                return;
            }
            int length = listFiles.length;
            int i = 0;
            while (i < length) {
                File file2 = listFiles[i];
                if (!file2.isFile() || !file2.getName().contains("ucebu")) {
                    i++;
                } else {
                    try {
                        context.startService(new Intent(context, CrashLogFilesUploader.class));
                        return;
                    } catch (Exception e) {
                        g.a((Throwable) e);
                        return;
                    }
                }
            }
        }
    }
}
