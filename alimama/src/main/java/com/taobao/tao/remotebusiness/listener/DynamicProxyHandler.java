package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.MtopBusiness;
import java.lang.reflect.InvocationHandler;
import mtopsdk.mtop.common.MtopListener;

class DynamicProxyHandler implements InvocationHandler {
    private MtopCacheListenerImpl cacheListener;
    private MtopFinishListenerImpl finishListener;
    private MtopListener listener;
    private MtopBusiness mtopBusiness;
    private MtopProgressListenerImpl progressListener;

    public DynamicProxyHandler(MtopBusiness mtopBusiness2, MtopListener mtopListener) {
        this.finishListener = new MtopFinishListenerImpl(mtopBusiness2, mtopListener);
        this.mtopBusiness = mtopBusiness2;
        this.listener = mtopListener;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0049 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0077  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object invoke(java.lang.Object r3, java.lang.reflect.Method r4, java.lang.Object[] r5) throws java.lang.Throwable {
        /*
            r2 = this;
            java.lang.String r3 = r4.getName()
            int r0 = r3.hashCode()
            r1 = -1809154262(0xffffffff942a7f2a, float:-8.607885E-27)
            if (r0 == r1) goto L_0x003b
            r1 = 1030363105(0x3d6a17e1, float:0.05715168)
            if (r0 == r1) goto L_0x0031
            r1 = 1177139532(0x4629b94c, float:10862.324)
            if (r0 == r1) goto L_0x0027
            r1 = 2096292721(0x7cf2e371, float:1.0089191E37)
            if (r0 == r1) goto L_0x001d
            goto L_0x0045
        L_0x001d:
            java.lang.String r0 = "onFinished"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0045
            r3 = 0
            goto L_0x0046
        L_0x0027:
            java.lang.String r0 = "onHeader"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0045
            r3 = 2
            goto L_0x0046
        L_0x0031:
            java.lang.String r0 = "onCached"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0045
            r3 = 3
            goto L_0x0046
        L_0x003b:
            java.lang.String r0 = "onDataReceived"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0045
            r3 = 1
            goto L_0x0046
        L_0x0045:
            r3 = -1
        L_0x0046:
            switch(r3) {
                case 0: goto L_0x0077;
                case 1: goto L_0x0061;
                case 2: goto L_0x0061;
                case 3: goto L_0x004b;
                default: goto L_0x0049;
            }
        L_0x0049:
            r3 = 0
            return r3
        L_0x004b:
            com.taobao.tao.remotebusiness.listener.MtopCacheListenerImpl r3 = r2.cacheListener
            if (r3 != 0) goto L_0x005a
            com.taobao.tao.remotebusiness.listener.MtopCacheListenerImpl r3 = new com.taobao.tao.remotebusiness.listener.MtopCacheListenerImpl
            com.taobao.tao.remotebusiness.MtopBusiness r0 = r2.mtopBusiness
            mtopsdk.mtop.common.MtopListener r1 = r2.listener
            r3.<init>(r0, r1)
            r2.cacheListener = r3
        L_0x005a:
            com.taobao.tao.remotebusiness.listener.MtopCacheListenerImpl r3 = r2.cacheListener
            java.lang.Object r3 = r4.invoke(r3, r5)
            return r3
        L_0x0061:
            com.taobao.tao.remotebusiness.listener.MtopProgressListenerImpl r3 = r2.progressListener
            if (r3 != 0) goto L_0x0070
            com.taobao.tao.remotebusiness.listener.MtopProgressListenerImpl r3 = new com.taobao.tao.remotebusiness.listener.MtopProgressListenerImpl
            com.taobao.tao.remotebusiness.MtopBusiness r0 = r2.mtopBusiness
            mtopsdk.mtop.common.MtopListener r1 = r2.listener
            r3.<init>(r0, r1)
            r2.progressListener = r3
        L_0x0070:
            com.taobao.tao.remotebusiness.listener.MtopProgressListenerImpl r3 = r2.progressListener
            java.lang.Object r3 = r4.invoke(r3, r5)
            return r3
        L_0x0077:
            com.taobao.tao.remotebusiness.listener.MtopFinishListenerImpl r3 = r2.finishListener
            java.lang.Object r3 = r4.invoke(r3, r5)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.remotebusiness.listener.DynamicProxyHandler.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }
}
