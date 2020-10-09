package com.uc.webview.export.cyclone.service;

import android.content.Context;
import com.taobao.weex.ui.component.WXComponent;
import com.uc.webview.export.cyclone.Constant;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCKnownException;
import com.uc.webview.export.cyclone.UCLibrary;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.cyclone.UCService;

@Constant
/* compiled from: U4Source */
public class UCVmsizeImpl implements UCVmsize {
    private static final String LOG_TAG = "UCVmsizeImpl";
    private static boolean mSoIsLoaded = false;
    private static UCKnownException mSoIsLoadedException;

    private static native int nativeSaveChromiumReservedSpace(long j);

    public int getServiceVersion() {
        return 0;
    }

    static {
        try {
            UCService.registerImpl(UCVmsize.class, new UCVmsizeImpl());
        } catch (Throwable th) {
            UCLogger create = UCLogger.create(WXComponent.PROP_FS_WRAP_CONTENT, LOG_TAG);
            if (create != null) {
                create.print("UCVmsizeImpl register exception:" + th, new Throwable[0]);
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
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004d  */
    public long saveChromiumReservedSpace(android.content.Context r10) throws java.lang.Exception {
        /*
            r9 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r3 = 21
            if (r0 < r3) goto L_0x00e2
            int r0 = android.os.Build.VERSION.SDK_INT
            r3 = 27
            if (r0 <= r3) goto L_0x0010
            goto L_0x00e2
        L_0x0010:
            java.lang.String r0 = "android.webkit.WebViewFactory"
            java.lang.Class r0 = java.lang.Class.forName(r0)
            if (r0 == 0) goto L_0x00e1
            java.lang.String r3 = "sAddressSpaceReserved"
            java.lang.reflect.Field r3 = r0.getDeclaredField(r3)
            if (r3 == 0) goto L_0x00e1
            boolean r4 = r3.isAccessible()
            r5 = 1
            if (r4 != 0) goto L_0x002a
            r3.setAccessible(r5)
        L_0x002a:
            r4 = 0
            boolean r6 = r3.getBoolean(r4)
            if (r6 == 0) goto L_0x00e1
            java.lang.String r1 = "sProviderLock"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r1)     // Catch:{ Throwable -> 0x0048 }
            if (r0 != 0) goto L_0x003a
            goto L_0x0048
        L_0x003a:
            boolean r1 = r0.isAccessible()     // Catch:{ Throwable -> 0x0048 }
            if (r1 != 0) goto L_0x0043
            r0.setAccessible(r5)     // Catch:{ Throwable -> 0x0048 }
        L_0x0043:
            java.lang.Object r0 = r0.get(r4)     // Catch:{ Throwable -> 0x0048 }
            goto L_0x0049
        L_0x0048:
            r0 = r9
        L_0x0049:
            boolean r1 = mSoIsLoaded
            if (r1 != 0) goto L_0x0050
            loadSo(r10)
        L_0x0050:
            monitor-enter(r0)
            r10 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r10)     // Catch:{ all -> 0x00de }
            r3.set(r4, r1)     // Catch:{ all -> 0x00de }
            java.lang.String r1 = "android.os.SystemProperties"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ Exception -> 0x00d5 }
            if (r1 == 0) goto L_0x00cd
            java.lang.String r2 = "getLong"
            r6 = 2
            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00d5 }
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r7[r10] = r8     // Catch:{ Exception -> 0x00d5 }
            java.lang.Class r8 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x00d5 }
            r7[r5] = r8     // Catch:{ Exception -> 0x00d5 }
            java.lang.reflect.Method r1 = r1.getDeclaredMethod(r2, r7)     // Catch:{ Exception -> 0x00d5 }
            if (r1 == 0) goto L_0x00c5
            boolean r2 = r1.isAccessible()     // Catch:{ Exception -> 0x00d5 }
            if (r2 != 0) goto L_0x007d
            r1.setAccessible(r5)     // Catch:{ Exception -> 0x00d5 }
        L_0x007d:
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r6 = "persist.sys.webview.vmsize"
            r2[r10] = r6     // Catch:{ Exception -> 0x00d5 }
            r6 = 104857600(0x6400000, double:5.1806538E-316)
            java.lang.Long r10 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x00d5 }
            r2[r5] = r10     // Catch:{ Exception -> 0x00d5 }
            java.lang.Object r10 = r1.invoke(r4, r2)     // Catch:{ Exception -> 0x00d5 }
            java.lang.Long r10 = (java.lang.Long) r10     // Catch:{ Exception -> 0x00d5 }
            if (r10 == 0) goto L_0x00bd
            long r1 = r10.longValue()     // Catch:{ Exception -> 0x00d5 }
            int r1 = nativeSaveChromiumReservedSpace(r1)     // Catch:{ Exception -> 0x00d5 }
            if (r1 != 0) goto L_0x00a4
            long r1 = r10.longValue()     // Catch:{ Exception -> 0x00d5 }
            monitor-exit(r0)     // Catch:{ all -> 0x00de }
            goto L_0x00e1
        L_0x00a4:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00d5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r6 = "Error:"
            r2.<init>(r6)     // Catch:{ Exception -> 0x00d5 }
            r2.append(r1)     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r1 = " on nativeSaveChromiumReservedSpace"
            r2.append(r1)     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x00d5 }
            r10.<init>(r1)     // Catch:{ Exception -> 0x00d5 }
            throw r10     // Catch:{ Exception -> 0x00d5 }
        L_0x00bd:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r1 = "SystemProperties.getLong invoke return null."
            r10.<init>(r1)     // Catch:{ Exception -> 0x00d5 }
            throw r10     // Catch:{ Exception -> 0x00d5 }
        L_0x00c5:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r1 = "SystemProperties.getLong not found."
            r10.<init>(r1)     // Catch:{ Exception -> 0x00d5 }
            throw r10     // Catch:{ Exception -> 0x00d5 }
        L_0x00cd:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00d5 }
            java.lang.String r1 = "SystemProperties not found."
            r10.<init>(r1)     // Catch:{ Exception -> 0x00d5 }
            throw r10     // Catch:{ Exception -> 0x00d5 }
        L_0x00d5:
            r10 = move-exception
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r5)     // Catch:{ all -> 0x00de }
            r3.set(r4, r1)     // Catch:{ all -> 0x00de }
            throw r10     // Catch:{ all -> 0x00de }
        L_0x00de:
            r10 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00de }
            throw r10
        L_0x00e1:
            return r1
        L_0x00e2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.service.UCVmsizeImpl.saveChromiumReservedSpace(android.content.Context):long");
    }

    private static synchronized void loadSo(Context context) {
        synchronized (UCVmsizeImpl.class) {
            if (!mSoIsLoaded) {
                if (mSoIsLoadedException == null) {
                    try {
                        UCLibrary.load(context, UCCyclone.genFile(context, (String) null, "libvmsize", ".so", 24713491, "e3d7b7107d5f402c1dde1a3930f7d7da", UCVmsizeImplConstant.genCodes(), new Object[0]).getAbsolutePath(), (ClassLoader) null);
                        mSoIsLoaded = true;
                    } catch (Throwable th) {
                        UCKnownException uCKnownException = new UCKnownException(th);
                        mSoIsLoadedException = uCKnownException;
                        throw uCKnownException;
                    }
                } else {
                    throw mSoIsLoadedException;
                }
            }
        }
    }

    public String toString() {
        return "UCVmsizeImpl." + getServiceVersion();
    }
}
