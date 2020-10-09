package com.uploader.implement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.uploader.export.ITaskListener;
import com.uploader.export.IUploaderManager;
import com.uploader.export.IUploaderTask;
import com.uploader.implement.a.d;
import com.uploader.implement.a.g;
import com.uploader.implement.a.i;
import com.uploader.implement.d.c;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class UploaderManager implements IUploaderManager, d {
    private int a;
    private ArrayList<i> b;
    private ArrayList<Pair<Integer, String>> c;
    private SparseArray<ArrayList<Pair<i, com.uploader.implement.d.b>>> d;
    private ArrayList<Pair<i, com.uploader.implement.d.b>> e;
    private com.uploader.implement.b.a.b f;
    private BroadcastReceiver g;
    private volatile Handler h;
    private volatile boolean i;
    private Runnable j;
    private boolean k;
    private String l;
    private final int m;
    private c n;
    private final byte[] o;
    private final int p;

    private static class a implements Runnable {
        final UploaderManager a;
        final int b;
        final Object[] c;

        a(int i, @NonNull UploaderManager uploaderManager, Object... objArr) {
            this.b = i;
            this.a = uploaderManager;
            this.c = objArr;
        }

        public void run() {
            switch (this.b) {
                case 1:
                    this.a.a((IUploaderTask) this.c[0], (ITaskListener) this.c[1], (Handler) this.c[2]);
                    return;
                case 2:
                    this.a.a((IUploaderTask) this.c[0]);
                    return;
                case 3:
                    this.a.a();
                    return;
                case 4:
                    this.a.b((g) this.c[0]);
                    return;
                case 5:
                    this.a.c();
                    return;
                default:
                    return;
            }
        }
    }

    UploaderManager() {
        this(0);
    }

    UploaderManager(int i2) {
        this.a = 0;
        this.i = false;
        this.o = new byte[0];
        this.d = new SparseArray<>(2);
        this.e = new ArrayList<>();
        this.c = new ArrayList<>();
        this.b = new ArrayList<>();
        this.m = hashCode();
        this.p = i2;
    }

    public boolean isInitialized() {
        return this.i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009a, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c4, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean initialize(@androidx.annotation.NonNull android.content.Context r7, @androidx.annotation.NonNull com.uploader.export.IUploaderDependency r8) {
        /*
            r6 = this;
            r0 = 16
            r1 = 0
            if (r7 != 0) goto L_0x0024
            boolean r7 = com.uploader.implement.a.a((int) r0)
            if (r7 == 0) goto L_0x0023
            java.lang.String r7 = "UploaderManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            int r2 = r6.m
            r8.append(r2)
            java.lang.String r2 = " initialize fail, context null"
            r8.append(r2)
            java.lang.String r8 = r8.toString()
            com.uploader.implement.a.a(r0, r7, r8)
        L_0x0023:
            return r1
        L_0x0024:
            boolean r2 = r6.i
            r3 = 4
            if (r2 == 0) goto L_0x0048
            boolean r7 = com.uploader.implement.a.a((int) r3)
            if (r7 == 0) goto L_0x0047
            java.lang.String r7 = "UploaderManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            int r0 = r6.m
            r8.append(r0)
            java.lang.String r0 = " initialize, is initialized !"
            r8.append(r0)
            java.lang.String r8 = r8.toString()
            com.uploader.implement.a.a(r3, r7, r8)
        L_0x0047:
            return r1
        L_0x0048:
            byte[] r2 = r6.o
            monitor-enter(r2)
            boolean r4 = r6.i     // Catch:{ all -> 0x00c5 }
            if (r4 == 0) goto L_0x006f
            boolean r7 = com.uploader.implement.a.a((int) r3)     // Catch:{ all -> 0x00c5 }
            if (r7 == 0) goto L_0x006d
            java.lang.String r7 = "UploaderManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c5 }
            r8.<init>()     // Catch:{ all -> 0x00c5 }
            int r0 = r6.m     // Catch:{ all -> 0x00c5 }
            r8.append(r0)     // Catch:{ all -> 0x00c5 }
            java.lang.String r0 = " initialize, is initialized !"
            r8.append(r0)     // Catch:{ all -> 0x00c5 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00c5 }
            com.uploader.implement.a.a(r3, r7, r8)     // Catch:{ all -> 0x00c5 }
        L_0x006d:
            monitor-exit(r2)     // Catch:{ all -> 0x00c5 }
            return r1
        L_0x006f:
            int r4 = r6.p     // Catch:{ all -> 0x00c5 }
            com.uploader.export.IUploaderEnvironment r5 = r8.getEnvironment()     // Catch:{ all -> 0x00c5 }
            int r5 = r5.getInstanceType()     // Catch:{ all -> 0x00c5 }
            if (r4 == r5) goto L_0x009b
            boolean r7 = com.uploader.implement.a.a((int) r0)     // Catch:{ all -> 0x00c5 }
            if (r7 == 0) goto L_0x0099
            java.lang.String r7 = "UploaderManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c5 }
            r8.<init>()     // Catch:{ all -> 0x00c5 }
            int r3 = r6.m     // Catch:{ all -> 0x00c5 }
            r8.append(r3)     // Catch:{ all -> 0x00c5 }
            java.lang.String r3 = " initialize, FAILED! environment not equals instance in instanceType"
            r8.append(r3)     // Catch:{ all -> 0x00c5 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00c5 }
            com.uploader.implement.a.a(r0, r7, r8)     // Catch:{ all -> 0x00c5 }
        L_0x0099:
            monitor-exit(r2)     // Catch:{ all -> 0x00c5 }
            return r1
        L_0x009b:
            com.uploader.implement.c r0 = new com.uploader.implement.c     // Catch:{ all -> 0x00c5 }
            r0.<init>(r7, r8)     // Catch:{ all -> 0x00c5 }
            r6.n = r0     // Catch:{ all -> 0x00c5 }
            r7 = 1
            r6.i = r7     // Catch:{ all -> 0x00c5 }
            boolean r8 = com.uploader.implement.a.a((int) r3)     // Catch:{ all -> 0x00c5 }
            if (r8 == 0) goto L_0x00c3
            java.lang.String r8 = "UploaderManager"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c5 }
            r0.<init>()     // Catch:{ all -> 0x00c5 }
            int r1 = r6.m     // Catch:{ all -> 0x00c5 }
            r0.append(r1)     // Catch:{ all -> 0x00c5 }
            java.lang.String r1 = " initialize !!!"
            r0.append(r1)     // Catch:{ all -> 0x00c5 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00c5 }
            com.uploader.implement.a.a(r3, r8, r0)     // Catch:{ all -> 0x00c5 }
        L_0x00c3:
            monitor-exit(r2)     // Catch:{ all -> 0x00c5 }
            return r7
        L_0x00c5:
            r7 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00c5 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.UploaderManager.initialize(android.content.Context, com.uploader.export.IUploaderDependency):boolean");
    }

    public boolean uploadAsync(@NonNull IUploaderTask iUploaderTask, @NonNull ITaskListener iTaskListener, Handler handler) {
        if (iUploaderTask == null) {
            if (a.a(8)) {
                a.a(8, "UploaderManager", this.m + " uploadAsync fail,task null");
            }
            return false;
        }
        synchronized (this.o) {
            if (!this.i) {
                return false;
            }
            boolean post = b().post(new a(1, this, iUploaderTask, iTaskListener, handler));
            return post;
        }
    }

    private static class b extends BroadcastReceiver {
        private final WeakReference<UploaderManager> a;

        b(UploaderManager uploaderManager) {
            this.a = new WeakReference<>(uploaderManager);
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && intent != null) {
                try {
                    intent.getBooleanExtra("noConnectivity", false);
                    intent.getStringExtra("extraInfo");
                    UploaderManager uploaderManager = (UploaderManager) this.a.get();
                    if (uploaderManager != null) {
                        uploaderManager.d();
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    private Handler b() {
        Handler handler = this.h;
        if (handler != null) {
            return handler;
        }
        if (a.a(2)) {
            a.a(2, "UploaderManager", this.m + " doRetrieve and register");
        }
        HandlerThread handlerThread = new HandlerThread("[aus]");
        handlerThread.start();
        Handler handler2 = new Handler(handlerThread.getLooper());
        this.h = handler2;
        return handler2;
    }

    /* access modifiers changed from: package-private */
    public void a() {
        Handler handler = this.h;
        if (handler != null) {
            Context applicationContext = this.n.c.getApplicationContext();
            if (this.g != null) {
                try {
                    applicationContext.unregisterReceiver(this.g);
                } catch (Exception e2) {
                    if (a.a(16)) {
                        a.a(16, "UploaderManager", "doClean unregisterReceiver", e2);
                    }
                } catch (Throwable th) {
                    this.g = null;
                    throw th;
                }
                this.g = null;
            }
            handler.removeCallbacksAndMessages((Object) null);
            handler.getLooper().quit();
            this.j = null;
            this.h = null;
            this.d = new SparseArray<>(2);
            this.e.trimToSize();
            this.c.trimToSize();
            this.b.trimToSize();
            if (this.f != null) {
                this.f.a();
                this.f = null;
            }
            if (a.a(2)) {
                a.a(2, "UploaderManager", this.m + " doClean and release");
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    void a(com.uploader.export.IUploaderTask r9, com.uploader.export.ITaskListener r10, android.os.Handler r11) {
        /*
            r8 = this;
            android.content.BroadcastReceiver r0 = r8.g
            if (r0 != 0) goto L_0x0035
            com.uploader.implement.c r0 = r8.n
            android.content.Context r0 = r0.c
            android.content.Context r0 = r0.getApplicationContext()
            com.uploader.implement.UploaderManager$b r1 = new com.uploader.implement.UploaderManager$b
            r1.<init>(r8)
            r8.g = r1
            com.uploader.implement.c r1 = r8.n
            android.content.Context r1 = r1.c
            android.content.BroadcastReceiver r2 = r8.g
            android.content.IntentFilter r3 = new android.content.IntentFilter
            java.lang.String r4 = "android.net.conn.CONNECTIVITY_CHANGE"
            r3.<init>(r4)
            r1.registerReceiver(r2, r3)
            android.net.NetworkInfo r0 = com.uploader.implement.e.a.b(r0)
            if (r0 == 0) goto L_0x0035
            boolean r1 = r0.isConnected()
            r8.k = r1
            java.lang.String r0 = r0.getExtraInfo()
            r8.l = r0
        L_0x0035:
            java.lang.String r0 = r9.getBizType()
            int r4 = r8.a((java.lang.String) r0)
            android.util.SparseArray<java.util.ArrayList<android.util.Pair<com.uploader.implement.a.i, com.uploader.implement.d.b>>> r0 = r8.d
            java.lang.Object r0 = r0.get(r4)
            java.util.ArrayList r0 = (java.util.ArrayList) r0
            com.uploader.implement.a.i r7 = new com.uploader.implement.a.i
            com.uploader.implement.c r2 = r8.n
            r1 = r7
            r3 = r9
            r5 = r10
            r6 = r11
            r1.<init>(r2, r3, r4, r5, r6)
            r10 = 1
            r11 = 2
            r1 = 0
            if (r0 == 0) goto L_0x005e
            int r2 = r0.size()
            if (r2 != r11) goto L_0x0068
            r10 = 0
            r11 = 1
            goto L_0x007e
        L_0x005e:
            android.util.SparseArray<java.util.ArrayList<android.util.Pair<com.uploader.implement.a.i, com.uploader.implement.d.b>>> r2 = r8.d
            int r2 = r2.size()
            if (r2 != r11) goto L_0x0068
            r10 = 0
            goto L_0x007e
        L_0x0068:
            com.uploader.implement.c r11 = r8.n
            android.content.Context r11 = r11.c
            android.content.Context r11 = r11.getApplicationContext()
            boolean r11 = com.uploader.implement.e.a.a(r11)
            if (r11 != 0) goto L_0x007a
            r10 = 3
            r10 = 0
            r11 = 3
            goto L_0x007e
        L_0x007a:
            r8.a(r7, r0)
            r11 = 0
        L_0x007e:
            byte[] r0 = r8.o
            monitor-enter(r0)
            android.os.Handler r2 = r8.h     // Catch:{ all -> 0x00ec }
            if (r2 != 0) goto L_0x0087
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return
        L_0x0087:
            java.lang.Runnable r3 = r8.j     // Catch:{ all -> 0x00ec }
            r2.removeCallbacks(r3)     // Catch:{ all -> 0x00ec }
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            r0 = 4
            if (r10 == 0) goto L_0x00b6
            boolean r10 = com.uploader.implement.a.a((int) r0)
            if (r10 == 0) goto L_0x00b5
            java.lang.String r10 = "UploaderManager"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            int r1 = r8.m
            r11.append(r1)
            java.lang.String r1 = " doUpload, remove count down, start, task:"
            r11.append(r1)
            int r9 = r9.hashCode()
            r11.append(r9)
            java.lang.String r9 = r11.toString()
            com.uploader.implement.a.a(r0, r10, r9)
        L_0x00b5:
            return
        L_0x00b6:
            java.util.ArrayList<com.uploader.implement.a.i> r10 = r8.b
            r10.add(r1, r7)
            r7.d()
            boolean r10 = com.uploader.implement.a.a((int) r0)
            if (r10 == 0) goto L_0x00eb
            java.lang.String r10 = "UploaderManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r2 = r8.m
            r1.append(r2)
            java.lang.String r2 = " doUpload, remove count down, wait, stopReason:"
            r1.append(r2)
            r1.append(r11)
            java.lang.String r11 = " task:"
            r1.append(r11)
            int r9 = r9.hashCode()
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            com.uploader.implement.a.a(r0, r10, r9)
        L_0x00eb:
            return
        L_0x00ec:
            r9 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.UploaderManager.a(com.uploader.export.IUploaderTask, com.uploader.export.ITaskListener, android.os.Handler):void");
    }

    private void a(i iVar, @Nullable ArrayList<Pair<i, com.uploader.implement.d.b>> arrayList) {
        int g2 = iVar.g();
        if (this.f == null) {
            this.f = new com.uploader.implement.b.a.b(this.n, this.h.getLooper());
        }
        c cVar = new c(this.n, this.f, this.h.getLooper());
        if (arrayList == null) {
            arrayList = new ArrayList<>(2);
            this.d.append(g2, arrayList);
        }
        Pair create = Pair.create(iVar, cVar);
        arrayList.add(create);
        this.e.add(create);
        iVar.a((d) this);
        iVar.b(cVar);
        if (a.a(4)) {
            a.a(4, "UploaderManager", this.m + " startAction task:" + iVar.f().hashCode());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0049, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancelAsync(@androidx.annotation.NonNull com.uploader.export.IUploaderTask r8) {
        /*
            r7 = this;
            r0 = 0
            if (r8 != 0) goto L_0x0024
            r8 = 8
            boolean r1 = com.uploader.implement.a.a((int) r8)
            if (r1 == 0) goto L_0x0023
            java.lang.String r1 = "UploaderManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            int r3 = r7.m
            r2.append(r3)
            java.lang.String r3 = " cancelAsync fail,task null"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.uploader.implement.a.a(r8, r1, r2)
        L_0x0023:
            return r0
        L_0x0024:
            boolean r1 = r7.i
            if (r1 != 0) goto L_0x0029
            return r0
        L_0x0029:
            byte[] r1 = r7.o
            monitor-enter(r1)
            boolean r2 = r7.i     // Catch:{ all -> 0x004a }
            if (r2 != 0) goto L_0x0032
            monitor-exit(r1)     // Catch:{ all -> 0x004a }
            return r0
        L_0x0032:
            android.os.Handler r2 = r7.h     // Catch:{ all -> 0x004a }
            r3 = 1
            if (r2 == 0) goto L_0x0048
            com.uploader.implement.UploaderManager$a r4 = new com.uploader.implement.UploaderManager$a     // Catch:{ all -> 0x004a }
            r5 = 2
            java.lang.Object[] r6 = new java.lang.Object[r3]     // Catch:{ all -> 0x004a }
            r6[r0] = r8     // Catch:{ all -> 0x004a }
            r4.<init>(r5, r7, r6)     // Catch:{ all -> 0x004a }
            boolean r8 = r2.post(r4)     // Catch:{ all -> 0x004a }
            if (r8 == 0) goto L_0x0048
            r0 = 1
        L_0x0048:
            monitor-exit(r1)     // Catch:{ all -> 0x004a }
            return r0
        L_0x004a:
            r8 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x004a }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.UploaderManager.cancelAsync(com.uploader.export.IUploaderTask):boolean");
    }

    private int a(String str) {
        if (str == null) {
            str = "";
        }
        int size = this.c.size() - 1;
        while (true) {
            if (size < 0) {
                size = -1;
                break;
            } else if (((String) this.c.get(size).second).equals(str)) {
                break;
            } else {
                size--;
            }
        }
        if (size < 0) {
            ArrayList<Pair<Integer, String>> arrayList = this.c;
            int i2 = this.a + 1;
            this.a = i2;
            arrayList.add(new Pair(Integer.valueOf(i2), str));
            size = this.c.size() - 1;
        }
        return ((Integer) this.c.get(size).first).intValue();
    }

    /* access modifiers changed from: package-private */
    public void a(IUploaderTask iUploaderTask) {
        boolean z;
        int a2 = a(iUploaderTask.getBizType());
        int size = this.b.size() - 1;
        while (true) {
            if (size < 0) {
                z = false;
                break;
            } else if (this.b.get(size).f().equals(iUploaderTask)) {
                this.b.remove(size).c((com.uploader.implement.d.b) null);
                z = true;
                break;
            } else {
                size--;
            }
        }
        if (!z) {
            ArrayList arrayList = this.d.get(a2);
            if (arrayList != null) {
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    if (((i) ((Pair) arrayList.get(size2)).first).f().equals(iUploaderTask)) {
                        Pair pair = (Pair) arrayList.get(size2);
                        ((i) pair.first).c((com.uploader.implement.d.b) pair.second);
                        if (a.a(4)) {
                            a.a(4, "UploaderManager", this.m + " doCancel cancel concurrent task:" + iUploaderTask);
                            return;
                        }
                        return;
                    }
                }
            }
        } else if (a.a(4)) {
            a.a(4, "UploaderManager", this.m + " doCancel cancel waiting task:" + iUploaderTask);
        }
    }

    public void a(g gVar) {
        synchronized (this.o) {
            Handler handler = this.h;
            if (handler != null) {
                handler.post(new a(4, this, gVar));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void b(g gVar) {
        boolean z;
        i iVar = (i) gVar;
        int g2 = iVar.g();
        ArrayList arrayList = this.d.get(g2);
        if (arrayList != null) {
            int size = arrayList.size();
            while (true) {
                size--;
                if (size < 0) {
                    z = false;
                    break;
                } else if (((i) ((Pair) arrayList.get(size)).first).equals(iVar)) {
                    z = this.e.remove(arrayList.remove(size));
                    break;
                }
            }
            if (z) {
                if (arrayList.size() == 0) {
                    this.d.remove(g2);
                    if (a.a(4)) {
                        a.a(4, "UploaderManager", this.m + " onFinish remove concurrent task:" + iVar.f().hashCode());
                    }
                }
                if (com.uploader.implement.e.a.a(this.n.c.getApplicationContext())) {
                    e();
                    if (this.d.size() == 0 && this.b.size() == 0) {
                        synchronized (this.o) {
                            Handler handler = this.h;
                            if (a.a(8)) {
                                a.a(8, "UploaderManager", this.m + " start count down:" + ILocatable.ErrorCode.SUCCESS);
                            }
                            if (handler != null) {
                                this.j = new a(3, this, new Object[0]);
                                handler.postDelayed(this.j, 90000);
                            }
                        }
                    } else if (a.a(8)) {
                        a.a(8, "UploaderManager", this.m + " doFinish has more data");
                    }
                } else if (a.a(8)) {
                    a.a(8, "UploaderManager", this.m + " doFinish no network");
                }
            } else if (a.a(8)) {
                a.a(8, "UploaderManager", this.m + " doFinish !removed");
            }
        } else if (a.a(8)) {
            a.a(8, "UploaderManager", this.m + " doFinish no concurrent");
        }
    }

    /* access modifiers changed from: private */
    public void c() {
        boolean z;
        String str;
        boolean equals;
        NetworkInfo b2 = com.uploader.implement.e.a.b(this.n.c.getApplicationContext());
        if (b2 != null) {
            z = b2.isConnected();
            str = b2.getExtraInfo();
        } else {
            str = null;
            z = false;
        }
        boolean z2 = this.k;
        String str2 = this.l;
        boolean z3 = true;
        if (z2 == z) {
            if (str2 != null) {
                equals = str2.equals(str);
            } else if (str != null) {
                equals = str.equals(str2);
            } else {
                z3 = false;
            }
            z3 = true ^ equals;
        }
        if (a.a(8)) {
            a.a(8, "UploaderManager", this.m + " doNetworkChanged, extraInfo(new|old):" + str + "|" + str2 + " isConnected(new|old):" + z + "|" + z2 + " changed:" + z3);
        }
        if (z3) {
            this.k = z;
            this.l = str;
            if (z) {
                int size = this.e.size();
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    Pair pair = this.e.get(i3);
                    ((i) pair.first).b((com.uploader.implement.d.b) pair.second);
                    i2++;
                }
                int e2 = e();
                if (a.a(2)) {
                    a.a(2, "UploaderManager", this.m + " restartedCount:" + i2 + " suppliedCount:" + e2);
                }
            } else if (this.f != null) {
                this.f.a();
            }
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        synchronized (this.o) {
            Handler handler = this.h;
            if (handler != null) {
                handler.post(new a(5, this, new Object[0]));
            }
        }
    }

    private int e() {
        int i2 = 0;
        for (int size = this.b.size() - 1; size >= 0; size--) {
            i iVar = this.b.get(size);
            ArrayList arrayList = this.d.get(iVar.g());
            if (arrayList == null) {
                if (this.d.size() < 2) {
                    this.b.remove(size);
                    a(iVar, arrayList);
                    i2++;
                }
            } else if (arrayList.size() < 2) {
                this.b.remove(size);
                a(iVar, arrayList);
                i2++;
            }
        }
        if (a.a(2)) {
            a.a(2, "UploaderManager", this.m + " suppliedCount:" + i2);
        }
        return i2;
    }
}
