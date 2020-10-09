package com.uc.webview.export.utility.download;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.alipay.sdk.util.e;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.k;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

@Api
/* compiled from: U4Source */
public class UpdateTask {
    public static final String START_FLG_FILE_NAME = "299772b0fd1634653ae3c31f366de3f8";
    public static final String STOP_FLG_FILE_NAME = "2e67cdbeb4ec133dcc8204d930aa7145";
    /* access modifiers changed from: private */
    public static final String a = "UpdateTask";
    private static final ConcurrentHashMap<Long, Long> b = new ConcurrentHashMap<>();
    private final String[] c = new String[3];
    /* access modifiers changed from: private */
    public final long[] d = new long[6];
    private final ValueCallback<UpdateTask>[] e = new ValueCallback[12];
    /* access modifiers changed from: private */
    public final Object[] f = new Object[3];
    /* access modifiers changed from: private */
    public int g = 0;
    /* access modifiers changed from: private */
    public int h = 0;
    /* access modifiers changed from: private */
    public String i = br.SDK_SHELL_DEX_FILE;
    /* access modifiers changed from: private */
    public ValueCallback<Object[]> j;

    public UpdateTask(Context context, String str, String str2, String str3, ValueCallback<Object[]> valueCallback, Long l, Long l2) {
        long j2;
        l = l == null ? 60000L : l;
        l2 = l2 == null ? Long.valueOf(DateUtils.WEEK) : l2;
        int hashCode = str.hashCode();
        synchronized (b) {
            j2 = (long) hashCode;
            if (!b.containsKey(Long.valueOf(j2))) {
                b.put(Long.valueOf(j2), Long.valueOf(j2));
            } else {
                throw new RuntimeException("Duplicate task.");
            }
        }
        IWaStat.WaStat.stat(IWaStat.UCM);
        this.j = valueCallback;
        this.d[0] = j2;
        this.d[4] = l.longValue();
        this.d[5] = l2.longValue();
        this.c[0] = str;
        this.c[1] = str2;
        this.c[2] = UCCyclone.getSourceHash(str);
        this.f[0] = context;
        this.f[2] = new DownloadTask(context, str, valueCallback);
        this.i = str3;
    }

    public static final File getUCPlayerRoot(Context context) throws UCSetupException {
        if (d.a().a(UCCore.OPTION_UC_PLAYER_ROOT) != null) {
            return new File((String) d.a().a(UCCore.OPTION_UC_PLAYER_ROOT));
        }
        return k.a(context, "ucplayer");
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            synchronized (b) {
                b.remove(Long.valueOf(this.d[0]));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public UpdateTask onEvent(String str, ValueCallback<UpdateTask> valueCallback) {
        if (str.equals("success")) {
            this.e[0] = valueCallback;
        } else if (str.equals(e.a)) {
            this.e[1] = valueCallback;
        } else if (str.equals("recovered")) {
            this.e[2] = valueCallback;
        } else if (str.equals("progress")) {
            this.e[3] = valueCallback;
        } else if (str.equals(UCCore.EVENT_EXCEPTION)) {
            this.e[4] = valueCallback;
        } else if (str.equals("check")) {
            this.e[5] = valueCallback;
        } else if (str.equals("exists")) {
            this.e[6] = valueCallback;
        } else if (str.equals("beginDownload")) {
            this.e[7] = valueCallback;
        } else if (str.equals("beginUnZip")) {
            this.e[8] = valueCallback;
        } else if (str.equals("unzipSuccess")) {
            this.e[9] = valueCallback;
        } else if (str.equals(UCCore.EVENT_DOWNLOAD_EXCEPTION)) {
            this.e[10] = valueCallback;
        } else if (str.equals("deleteDownFile")) {
            this.e[11] = valueCallback;
        } else {
            throw new RuntimeException("The given event:" + str + " is unknown.");
        }
        return this;
    }

    public final File getUpdateDir() throws UCSetupException {
        if (this.d[1] <= 0) {
            Pair<Long, Long> a2 = k.a(this.c[0], (URL) null);
            this.d[1] = ((Long) a2.first).longValue();
            this.d[2] = ((Long) a2.second).longValue();
        }
        String str = String.valueOf(this.d[1]) + "_" + this.d[2];
        return new File(this.c[1] + "/" + this.c[2] + "/" + str);
    }

    public Throwable getException() {
        return (Throwable) this.f[1];
    }

    public UpdateTask start() {
        ValueCallback<UpdateTask> valueCallback = this.e[0];
        ValueCallback<UpdateTask> valueCallback2 = this.e[1];
        ValueCallback<UpdateTask> valueCallback3 = this.e[2];
        ValueCallback<UpdateTask> valueCallback4 = this.e[3];
        ValueCallback<UpdateTask> valueCallback5 = this.e[4];
        ValueCallback<UpdateTask> valueCallback6 = this.e[5];
        ValueCallback<UpdateTask> valueCallback7 = this.e[6];
        ValueCallback<UpdateTask> valueCallback8 = this.e[7];
        ValueCallback<UpdateTask> valueCallback9 = this.e[8];
        ValueCallback<UpdateTask> valueCallback10 = this.e[9];
        ValueCallback<UpdateTask> valueCallback11 = this.e[10];
        ValueCallback<UpdateTask> valueCallback12 = this.e[11];
        String str = this.c[0];
        this.g = 0;
        ValueCallback<UpdateTask> valueCallback13 = valueCallback3;
        d dVar = r0;
        DownloadTask downloadTask = (DownloadTask) this.f[2];
        ValueCallback<UpdateTask> valueCallback14 = valueCallback4;
        ValueCallback<UpdateTask> valueCallback15 = valueCallback4;
        d dVar2 = new d(this, valueCallback14, str, downloadTask, valueCallback2, valueCallback9, valueCallback10, valueCallback, valueCallback5);
        DownloadTask downloadTask2 = downloadTask;
        ValueCallback<UpdateTask> valueCallback16 = valueCallback15;
        downloadTask2.onEvent("check", new e(this, valueCallback6)).onEvent("success", new o(this, dVar)).onEvent("exists", new n(this, dVar)).onEvent("delete", new m(this, valueCallback12)).onEvent(e.a, new l(this, valueCallback2)).onEvent("progress", new k(this, valueCallback16)).onEvent(UCCore.EVENT_EXCEPTION, new i(this, valueCallback11, valueCallback5)).onEvent("header", new h(this, valueCallback7, valueCallback13, valueCallback16, valueCallback5)).onEvent("beginDownload", new g(this, valueCallback8)).start();
        return this;
    }

    public UpdateTask stop() {
        ((DownloadTask) this.f[2]).stop();
        return this;
    }

    public UpdateTask startDownload() {
        ((DownloadTask) this.f[2]).start();
        return this;
    }

    public UpdateTask delete() {
        ((DownloadTask) this.f[2]).stopWith(new f(this));
        return this;
    }

    public static boolean isFinished(File file, String str) {
        if (!file.exists() || !new File(file, str).exists()) {
            return false;
        }
        return (!new File(file, START_FLG_FILE_NAME).exists() && !new File(file, "c34d62af061f389f7e4c9f0e835f7a54").exists()) || new File(file, STOP_FLG_FILE_NAME).exists() || new File(file, "95b70b3ec9f6407a92becf890996088d").exists();
    }

    public static File getUpdateRoot(Context context) throws Exception {
        return k.a(context, "updates");
    }

    public int getPercent() {
        return this.h;
    }

    public String getFilePath() {
        return ((DownloadTask) this.f[2]).getFilePath();
    }

    static /* synthetic */ void a(File file, boolean z) throws Exception {
        if (z) {
            new File(file, STOP_FLG_FILE_NAME).createNewFile();
        } else {
            new File(file, START_FLG_FILE_NAME).createNewFile();
        }
    }
}
