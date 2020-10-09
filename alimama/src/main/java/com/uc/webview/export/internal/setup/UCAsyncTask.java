package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/* compiled from: U4Source */
public class UCAsyncTask<RETURN_TYPE extends UCAsyncTask, CALLBACK_TYPE extends UCAsyncTask> implements Runnable {
    /* access modifiers changed from: private */
    public static final Boolean p = false;
    /* access modifiers changed from: private */
    public UCAsyncTask a;
    /* access modifiers changed from: private */
    public ConcurrentLinkedQueue<UCAsyncTask> b;
    private int c;
    /* access modifiers changed from: private */
    public final Object d;
    private final Integer e;
    private final Boolean f;
    /* access modifiers changed from: private */
    public boolean g;
    /* access modifiers changed from: private */
    public boolean h;
    /* access modifiers changed from: private */
    public final bo i;
    private HandlerThread j;
    private Looper k;
    /* access modifiers changed from: private */
    public Handler l;
    private String m;
    public ConcurrentHashMap<String, ValueCallback<CALLBACK_TYPE>> mCallbacks;
    protected UCSetupException mException;
    protected UCSetupException mExtraException;
    protected boolean mHasStarted;
    protected int mPercent;
    /* access modifiers changed from: private */
    public long n;
    private Runnable o;
    /* access modifiers changed from: private */
    public Vector<Pair<String, Pair<Long, Long>>> q;

    static /* synthetic */ int e(UCAsyncTask uCAsyncTask) {
        int i2 = uCAsyncTask.c + 1;
        uCAsyncTask.c = i2;
        return i2;
    }

    public UCAsyncTask(Integer num) {
        this(num, true);
    }

    public UCAsyncTask(Integer num, Boolean bool) {
        this.c = 0;
        this.d = new Object();
        this.g = false;
        this.h = false;
        this.i = new bo();
        this.n = 0;
        this.mHasStarted = false;
        this.q = p.booleanValue() ? new Vector<>() : null;
        this.e = num;
        this.f = bool;
    }

    public UCAsyncTask(Runnable runnable) {
        this((Integer) 0);
        this.o = runnable;
    }

    public UCAsyncTask(UCAsyncTask uCAsyncTask) {
        this((Runnable) null);
        setParent(uCAsyncTask);
    }

    public final RETURN_TYPE setParent(UCAsyncTask uCAsyncTask) {
        this.a = uCAsyncTask;
        return this;
    }

    public final RETURN_TYPE setCallbacks(ConcurrentHashMap<String, ValueCallback<CALLBACK_TYPE>> concurrentHashMap) {
        for (Map.Entry next : concurrentHashMap.entrySet()) {
            onEvent((String) next.getKey(), (ValueCallback) next.getValue());
        }
        return this;
    }

    public final void setPriority(int i2) {
        Process.setThreadPriority(i2);
    }

    /* access modifiers changed from: protected */
    public final RETURN_TYPE post(UCAsyncTask uCAsyncTask) {
        if (uCAsyncTask.a == this) {
            synchronized (this.d) {
                if (this.b == null) {
                    this.b = new ConcurrentLinkedQueue<>();
                }
                this.b.add(uCAsyncTask);
            }
            return this;
        }
        throw new RuntimeException("Please use \"new UCAsyncTask(parentTask).start()\" instead of \"post(new UCAsyncTask())\" to add sub task.");
    }

    /* access modifiers changed from: protected */
    public UCAsyncTask getParent() {
        return this.a;
    }

    public final Vector<Pair<String, Pair<Long, Long>>> getCosts() {
        return this.q;
    }

    public final ValueCallback<CALLBACK_TYPE> getCallback(String str) {
        if (this.mCallbacks == null) {
            return null;
        }
        return this.mCallbacks.get(str);
    }

    public final String getEvent() {
        return this.m;
    }

    private int a() {
        int i2 = 0;
        for (UCAsyncTask parent = getParent(); parent != null; parent = parent.getParent()) {
            i2++;
        }
        return i2;
    }

    private static String a(int i2) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                return sb.toString();
            }
            sb.append("    ");
            i2 = i3;
        }
    }

    /* access modifiers changed from: protected */
    public final int getPriority() {
        return this.e.intValue();
    }

    private UCAsyncTask b() {
        UCAsyncTask uCAsyncTask = this;
        while (uCAsyncTask.getParent() != null) {
            uCAsyncTask = uCAsyncTask.getParent();
        }
        return uCAsyncTask;
    }

    /* access modifiers changed from: protected */
    public boolean inThread() {
        return Thread.currentThread() == this.j;
    }

    public int getPercent() {
        return this.mPercent;
    }

    public boolean isPaused() {
        boolean a2;
        UCAsyncTask b2 = b();
        synchronized (b2.i) {
            a2 = b2.i.a();
        }
        return a2;
    }

    /* access modifiers changed from: protected */
    public void callback(String str) {
        UCLogger create;
        UCLogger create2;
        StringBuilder sb;
        String str2;
        this.m = str;
        try {
            UCSetupException exception = getException();
            if (!UCCore.EVENT_STAT.equals(str) && (create2 = UCLogger.create("d", "UCAsyncTask")) != null) {
                if (!"cost".equals(str)) {
                    sb = new StringBuilder("callback: ");
                    sb.append(a(a()));
                    sb.append(getClass().getSimpleName());
                    sb.append(".");
                    sb.append(str);
                    sb.append(Operators.SPACE_STR);
                    sb.append("progress".equals(str) ? Integer.valueOf(getPercent()) : "");
                    str2 = (!UCCore.EVENT_EXCEPTION.equals(str) || exception == null) ? "" : exception.toString();
                } else if (p.booleanValue()) {
                    Pair lastElement = this.q.lastElement();
                    sb = new StringBuilder("callback: ");
                    sb.append(a(a()));
                    sb.append(getClass().getSimpleName());
                    sb.append(".");
                    sb.append(str);
                    sb.append(" cost:");
                    sb.append(String.format("%5s", new Object[]{((Pair) lastElement.second).first}));
                    sb.append(" cost_cpu:");
                    sb.append(String.format("%5s", new Object[]{((Pair) lastElement.second).second}));
                    sb.append(" task:");
                    str2 = (String) lastElement.first;
                }
                sb.append(str2);
                create2.print(sb.toString(), new Throwable[0]);
            }
            if (UCCore.EVENT_EXCEPTION.equals(str) && (this instanceof UCSetupTask) && exception != null && (create = UCLogger.create("e", "UCAsyncTask")) != null) {
                create.print("callback: exception: ", exception);
                if (exception != exception.getRootCause()) {
                    create.print("callback: rootCause: ", exception.getRootCause());
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        Object callback = getCallback(str);
        if (callback instanceof WeakReference) {
            callback = ((WeakReference) callback).get();
        }
        if (callback instanceof ValueCallback) {
            try {
                ((ValueCallback) callback).onReceiveValue(this);
            } catch (Throwable th2) {
                Log.e("UCAsyncTask", "callback exception", th2);
            }
        }
    }

    /* access modifiers changed from: private */
    public Handler a(Looper looper) {
        com.uc.webview.export.internal.uc.startup.b.a(506);
        this.l = new bp(this, looper);
        this.l.post(this);
        com.uc.webview.export.internal.uc.startup.b.a(507);
        return this.l;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public RETURN_TYPE start() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.d
            monitor-enter(r0)
            boolean r1 = r4.mHasStarted     // Catch:{ all -> 0x00cb }
            if (r1 == 0) goto L_0x000f
            com.uc.webview.export.internal.setup.UCAsyncTask r1 = r4.a     // Catch:{ all -> 0x00cb }
            if (r1 != 0) goto L_0x00c9
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            if (r1 != 0) goto L_0x00c9
        L_0x000f:
            r1 = 1
            r4.mHasStarted = r1     // Catch:{ all -> 0x00cb }
            com.uc.webview.export.internal.setup.UCAsyncTask r1 = r4.a     // Catch:{ all -> 0x00cb }
            if (r1 == 0) goto L_0x001d
            com.uc.webview.export.internal.setup.UCAsyncTask r1 = r4.a     // Catch:{ all -> 0x00cb }
            r1.post(r4)     // Catch:{ all -> 0x00cb }
            goto L_0x00c9
        L_0x001d:
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            r2 = 225(0xe1, float:3.15E-43)
            if (r1 != 0) goto L_0x00bd
            com.uc.webview.export.internal.uc.startup.b.a(r2)     // Catch:{ all -> 0x00cb }
            android.os.Looper r1 = android.os.Looper.myLooper()     // Catch:{ all -> 0x00cb }
            android.os.Looper r2 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x00cb }
            if (r1 == r2) goto L_0x008d
            java.lang.Boolean r1 = r4.f     // Catch:{ all -> 0x00cb }
            boolean r1 = r1.booleanValue()     // Catch:{ all -> 0x00cb }
            if (r1 == 0) goto L_0x0039
            goto L_0x008d
        L_0x0039:
            java.lang.String r1 = "UCAsyncTask"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cb }
            java.lang.String r3 = "createThreadIfNeed myLooper "
            r2.<init>(r3)     // Catch:{ all -> 0x00cb }
            android.os.Looper r3 = android.os.Looper.myLooper()     // Catch:{ all -> 0x00cb }
            r2.append(r3)     // Catch:{ all -> 0x00cb }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00cb }
            com.uc.webview.export.internal.utility.Log.i(r1, r2)     // Catch:{ all -> 0x00cb }
            android.os.Looper r1 = android.os.Looper.myLooper()     // Catch:{ all -> 0x00cb }
            if (r1 != 0) goto L_0x0084
            android.os.Looper.prepare()     // Catch:{ all -> 0x00cb }
            android.os.Looper r1 = android.os.Looper.myLooper()     // Catch:{ all -> 0x00cb }
            r4.k = r1     // Catch:{ all -> 0x00cb }
            java.lang.String r1 = "UCAsyncTask"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cb }
            java.lang.String r3 = "createThreadIfNeed new myLooper "
            r2.<init>(r3)     // Catch:{ all -> 0x00cb }
            android.os.Looper r3 = r4.k     // Catch:{ all -> 0x00cb }
            r2.append(r3)     // Catch:{ all -> 0x00cb }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00cb }
            com.uc.webview.export.internal.utility.Log.i(r1, r2)     // Catch:{ all -> 0x00cb }
            android.os.Looper r1 = r4.k     // Catch:{ all -> 0x00cb }
            r4.a((android.os.Looper) r1)     // Catch:{ all -> 0x00cb }
            android.os.Looper.loop()     // Catch:{ all -> 0x00cb }
            java.lang.String r1 = "UCAsyncTask"
            java.lang.String r2 = "createThreadIfNeed Looper.loop after"
            com.uc.webview.export.internal.utility.Log.i(r1, r2)     // Catch:{ all -> 0x00cb }
            goto L_0x008b
        L_0x0084:
            android.os.Looper r1 = android.os.Looper.myLooper()     // Catch:{ all -> 0x00cb }
            r4.a((android.os.Looper) r1)     // Catch:{ all -> 0x00cb }
        L_0x008b:
            r1 = 0
            goto L_0x00b1
        L_0x008d:
            java.lang.String r1 = "UCAsyncTask"
            java.lang.String r2 = "createThreadIfNeed Looper.myLooper() == Looper.getMainLooper "
            com.uc.webview.export.internal.utility.Log.i(r1, r2)     // Catch:{ all -> 0x00cb }
            com.uc.webview.export.internal.setup.bq r1 = new com.uc.webview.export.internal.setup.bq     // Catch:{ all -> 0x00cb }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cb }
            java.lang.String r3 = "UCAsyncTask_"
            r2.<init>(r3)     // Catch:{ all -> 0x00cb }
            int r3 = r4.hashCode()     // Catch:{ all -> 0x00cb }
            r2.append(r3)     // Catch:{ all -> 0x00cb }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00cb }
            java.lang.Integer r3 = r4.e     // Catch:{ all -> 0x00cb }
            int r3 = r3.intValue()     // Catch:{ all -> 0x00cb }
            r1.<init>(r4, r2, r3)     // Catch:{ all -> 0x00cb }
        L_0x00b1:
            r4.j = r1     // Catch:{ all -> 0x00cb }
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            if (r1 == 0) goto L_0x00c9
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            r1.start()     // Catch:{ all -> 0x00cb }
            goto L_0x00c9
        L_0x00bd:
            com.uc.webview.export.internal.uc.startup.b.a(r2)     // Catch:{ all -> 0x00cb }
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            if (r1 == 0) goto L_0x00c9
            android.os.HandlerThread r1 = r4.j     // Catch:{ all -> 0x00cb }
            r1.start()     // Catch:{ all -> 0x00cb }
        L_0x00c9:
            monitor-exit(r0)     // Catch:{ all -> 0x00cb }
            return r4
        L_0x00cb:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00cb }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.UCAsyncTask.start():com.uc.webview.export.internal.setup.UCAsyncTask");
    }

    public final RETURN_TYPE start(long j2) {
        this.n = j2;
        return start();
    }

    public RETURN_TYPE stop() {
        synchronized (this.i) {
            resume();
            this.h = true;
        }
        return this;
    }

    public boolean isStopped() {
        boolean z;
        synchronized (this.i) {
            z = this.h;
        }
        return z;
    }

    public RETURN_TYPE pause() {
        UCAsyncTask b2 = b();
        synchronized (b2.i) {
            if (!b2.i.a()) {
                b2.g = true;
            }
        }
        return this;
    }

    public RETURN_TYPE resume() {
        UCAsyncTask b2 = b();
        synchronized (b2.i) {
            b2.g = false;
            if (b2.i.a()) {
                b2.i.a(0, (Object) null);
            }
        }
        return this;
    }

    public void run() {
        if (this.o != null) {
            this.o.run();
        }
    }

    public RETURN_TYPE onEvent(String str, ValueCallback<CALLBACK_TYPE> valueCallback) {
        if (str != null) {
            if (this.mCallbacks == null) {
                synchronized (this) {
                    if (this.mCallbacks == null) {
                        this.mCallbacks = new ConcurrentHashMap<>();
                    }
                }
            }
            if (valueCallback == null) {
                this.mCallbacks.remove(str);
            } else {
                this.mCallbacks.put(str, valueCallback);
            }
        }
        return this;
    }

    @Reflection
    public UCSetupException getException() {
        return this.mException;
    }

    public void setException(UCSetupException uCSetupException) {
        this.mException = uCSetupException;
    }

    @Reflection
    public UCSetupException getExtraException() {
        return this.mExtraException;
    }

    public void setExtraException(UCSetupException uCSetupException) {
        this.mExtraException = uCSetupException;
    }

    /* compiled from: U4Source */
    public class b<CB_TYPE extends UCAsyncTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public b() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCAsyncTask.this.stop();
        }
    }

    /* compiled from: U4Source */
    public class a<CB_TYPE extends UCAsyncTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public a() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCAsyncTask.this.callback(((UCAsyncTask) obj).getEvent());
        }
    }

    static /* synthetic */ int f(UCAsyncTask uCAsyncTask) {
        if (uCAsyncTask.b == null) {
            return 1;
        }
        return uCAsyncTask.b.size() + uCAsyncTask.c;
    }

    static /* synthetic */ void j(UCAsyncTask uCAsyncTask) {
        if (uCAsyncTask.b != null) {
            uCAsyncTask.b.clear();
        }
        com.uc.webview.export.internal.utility.Log.d("UCAsyncTask", "clearSubTasks");
    }

    static /* synthetic */ void n(UCAsyncTask uCAsyncTask) {
        uCAsyncTask.b = null;
        com.uc.webview.export.internal.utility.Log.i("UCAsyncTask", "cleanThread mLooper " + uCAsyncTask.k);
        if (uCAsyncTask.k != null) {
            uCAsyncTask.k.quit();
            uCAsyncTask.k = null;
        }
        uCAsyncTask.l = null;
        if (uCAsyncTask.j != null) {
            uCAsyncTask.j.quit();
            uCAsyncTask.j = null;
        }
        com.uc.webview.export.internal.utility.Log.d("UCAsyncTask", "cleanThread");
    }
}
