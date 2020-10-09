package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.ByteUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.WuaHelper;
import com.alibaba.analytics.utils.ZipDictUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.android.spdy.SessionCb;
import org.android.spdy.SessionExtraCb;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdySession;
import org.android.spdy.SuperviseConnectInfo;

public class TnetUtil {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int GCRY_CIPHER_AES128 = 16;
    private static final int HEAD_LENGTH = 8;
    private static final Object Lock_Event = new Object();
    /* access modifiers changed from: private */
    public static final Object Lock_Object = new Object();
    private static final int PROTOCOL_MAX_LENGTH = 131072;
    private static final String TAG = "TnetUtil";
    private static final int WAIT_TIMEOUT = 60000;
    private static boolean bFirstSpdySession = true;
    /* access modifiers changed from: private */
    public static int errorCode = -1;
    private static boolean isGetWuaBeforeSpdySession = false;
    public static int mErrorCode = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    /* access modifiers changed from: private */
    public static ByteArrayOutputStream mResponseCache = null;
    /* access modifiers changed from: private */
    public static long mResponseLen = 0;
    /* access modifiers changed from: private */
    public static long mResponseReceiveLen = 0;
    private static byte[] protocolBytes = null;
    private static int sendBytes = 0;
    /* access modifiers changed from: private */
    public static SpdySession spdySessionUT = null;

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
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    static com.alibaba.analytics.core.sync.BizResponse sendRequest(byte[] r25) {
        /*
            com.alibaba.analytics.utils.Logger.d()
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r0 = mMonitor
            int r1 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_REQUEST_SEND
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r4 = java.lang.Double.valueOf(r2)
            r5 = 0
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r1 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r1, r5, r4)
            r0.onEvent(r1)
            com.alibaba.analytics.core.sync.BizResponse r1 = new com.alibaba.analytics.core.sync.BizResponse
            r1.<init>()
            java.lang.Object r4 = Lock_Object
            monitor-enter(r4)
            protocolBytes = r25     // Catch:{ all -> 0x0219 }
            r6 = 0
            sendBytes = r6     // Catch:{ all -> 0x0219 }
            monitor-exit(r4)     // Catch:{ all -> 0x0219 }
            java.lang.Object r7 = Lock_Event
            monitor-enter(r7)
            java.io.ByteArrayOutputStream r0 = mResponseCache     // Catch:{ all -> 0x0216 }
            if (r0 == 0) goto L_0x002f
            java.io.ByteArrayOutputStream r0 = mResponseCache     // Catch:{ IOException -> 0x002f }
            r0.close()     // Catch:{ IOException -> 0x002f }
        L_0x002f:
            mResponseCache = r5     // Catch:{ all -> 0x0216 }
            r8 = 0
            mResponseReceiveLen = r8     // Catch:{ all -> 0x0216 }
            mResponseLen = r8     // Catch:{ all -> 0x0216 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0216 }
            r0 = -1
            errorCode = r0     // Catch:{ all -> 0x0216 }
            r4 = 4
            r10 = 3
            r13 = 2
            r14 = 1
            org.android.spdy.SpdySession r0 = spdySessionUT     // Catch:{ Exception -> 0x017e }
            if (r0 != 0) goto L_0x015a
            boolean r0 = bFirstSpdySession     // Catch:{ Exception -> 0x017e }
            if (r0 != 0) goto L_0x0054
            com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r0 = r0.isGzipUpload()     // Catch:{ Exception -> 0x017e }
            if (r0 == 0) goto L_0x015a
        L_0x0054:
            com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r0 = r0.isSelfMonitorTurnOn()     // Catch:{ Exception -> 0x017e }
            if (r0 == 0) goto L_0x006d
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r0 = mMonitor     // Catch:{ Exception -> 0x017e }
            int r15 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_CREATE_SESSION     // Catch:{ Exception -> 0x017e }
            java.lang.Double r11 = java.lang.Double.valueOf(r2)     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r11 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r15, r5, r11)     // Catch:{ Exception -> 0x017e }
            r0.onEvent(r11)     // Catch:{ Exception -> 0x017e }
        L_0x006d:
            com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x017e }
            android.content.Context r0 = r0.getContext()     // Catch:{ Exception -> 0x017e }
            org.android.spdy.SpdyVersion r11 = org.android.spdy.SpdyVersion.SPDY3     // Catch:{ Exception -> 0x017e }
            org.android.spdy.SpdySessionKind r12 = org.android.spdy.SpdySessionKind.NONE_SESSION     // Catch:{ Exception -> 0x017e }
            org.android.spdy.SpdyAgent r0 = org.android.spdy.SpdyAgent.getInstance(r0, r11, r12)     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.sync.TnetSecuritySDK r11 = com.alibaba.analytics.core.sync.TnetSecuritySDK.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r11 = r11.getInitSecurityCheck()     // Catch:{ Exception -> 0x017e }
            if (r11 == 0) goto L_0x008f
            com.alibaba.analytics.core.sync.TnetUtil$1 r11 = new com.alibaba.analytics.core.sync.TnetUtil$1     // Catch:{ Exception -> 0x017e }
            r11.<init>()     // Catch:{ Exception -> 0x017e }
            r0.setAccsSslCallback(r11)     // Catch:{ Exception -> 0x017e }
        L_0x008f:
            com.alibaba.analytics.core.ipv6.TnetIpv6Manager r11 = com.alibaba.analytics.core.ipv6.TnetIpv6Manager.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r11 = r11.isIpv6Connection()     // Catch:{ Exception -> 0x017e }
            if (r11 == 0) goto L_0x00b4
            com.alibaba.analytics.core.ipv6.TnetIpv6Manager r11 = com.alibaba.analytics.core.ipv6.TnetIpv6Manager.getInstance()     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.ipv6.TnetIpv6HostListener$TnetIpv6HostPort r11 = r11.getHostPortEntity()     // Catch:{ Exception -> 0x017e }
            if (r11 == 0) goto L_0x00b4
            com.alibaba.analytics.core.ipv6.TnetIpv6Manager r11 = com.alibaba.analytics.core.ipv6.TnetIpv6Manager.getInstance()     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.ipv6.TnetIpv6HostListener$TnetIpv6HostPort r11 = r11.getHostPortEntity()     // Catch:{ Exception -> 0x017e }
            java.lang.String r12 = r11.getHost()     // Catch:{ Exception -> 0x017e }
            int r11 = r11.getPort()     // Catch:{ Exception -> 0x017e }
            goto L_0x00c4
        L_0x00b4:
            com.alibaba.analytics.core.sync.TnetHostPortMgr r11 = com.alibaba.analytics.core.sync.TnetHostPortMgr.getInstance()     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r11 = r11.getEntity()     // Catch:{ Exception -> 0x017e }
            java.lang.String r12 = r11.getHost()     // Catch:{ Exception -> 0x017e }
            int r11 = r11.getPort()     // Catch:{ Exception -> 0x017e }
        L_0x00c4:
            r18 = r11
            java.lang.String r11 = "TnetUtil"
            java.lang.Object[] r15 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x017e }
            java.lang.String r16 = "host"
            r15[r6] = r16     // Catch:{ Exception -> 0x017e }
            r15[r14] = r12     // Catch:{ Exception -> 0x017e }
            java.lang.String r16 = "port"
            r15[r13] = r16     // Catch:{ Exception -> 0x017e }
            java.lang.Integer r16 = java.lang.Integer.valueOf(r18)     // Catch:{ Exception -> 0x017e }
            r15[r10] = r16     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r11, (java.lang.Object[]) r15)     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.sync.TnetUtil$UTSessionCb r11 = new com.alibaba.analytics.core.sync.TnetUtil$UTSessionCb     // Catch:{ Exception -> 0x017e }
            r11.<init>(r12)     // Catch:{ Exception -> 0x017e }
            org.android.spdy.SessionInfo r15 = new org.android.spdy.SessionInfo     // Catch:{ Exception -> 0x017e }
            r19 = 0
            r20 = 0
            r21 = 0
            r22 = 0
            r24 = 4240(0x1090, float:5.942E-42)
            r16 = r15
            r17 = r12
            r23 = r11
            r16.<init>(r17, r18, r19, r20, r21, r22, r23, r24)     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.core.sync.TnetSecuritySDK r11 = com.alibaba.analytics.core.sync.TnetSecuritySDK.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r11 = r11.getInitSecurityCheck()     // Catch:{ Exception -> 0x017e }
            if (r11 == 0) goto L_0x0107
            r11 = 8
            r15.setPubKeySeqNum(r11)     // Catch:{ Exception -> 0x017e }
            goto L_0x010c
        L_0x0107:
            r11 = 9
            r15.setPubKeySeqNum(r11)     // Catch:{ Exception -> 0x017e }
        L_0x010c:
            r11 = 10000(0x2710, float:1.4013E-41)
            r15.setConnectionTimeoutMs(r11)     // Catch:{ Exception -> 0x017e }
            java.lang.Object r11 = Lock_Object     // Catch:{ Exception -> 0x017e }
            monitor-enter(r11)     // Catch:{ Exception -> 0x017e }
            org.android.spdy.SpdySession r0 = r0.createSession(r15)     // Catch:{ all -> 0x0157 }
            spdySessionUT = r0     // Catch:{ all -> 0x0157 }
            boolean r0 = isGetWuaBeforeSpdySession     // Catch:{ all -> 0x0157 }
            if (r0 != 0) goto L_0x0140
            java.lang.String r0 = com.alibaba.analytics.utils.WuaHelper.getMiniWua()     // Catch:{ all -> 0x0157 }
            com.alibaba.analytics.core.sync.BizRequest.mMiniWua = r0     // Catch:{ all -> 0x0157 }
            java.lang.String r0 = "TnetUtil"
            java.lang.Object[] r12 = new java.lang.Object[r14]     // Catch:{ all -> 0x0157 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x0157 }
            r15.<init>()     // Catch:{ all -> 0x0157 }
            java.lang.String r4 = "GetWua by createSession:"
            r15.append(r4)     // Catch:{ all -> 0x0157 }
            java.lang.String r4 = com.alibaba.analytics.core.sync.BizRequest.mMiniWua     // Catch:{ all -> 0x0157 }
            r15.append(r4)     // Catch:{ all -> 0x0157 }
            java.lang.String r4 = r15.toString()     // Catch:{ all -> 0x0157 }
            r12[r6] = r4     // Catch:{ all -> 0x0157 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r12)     // Catch:{ all -> 0x0157 }
        L_0x0140:
            isGetWuaBeforeSpdySession = r6     // Catch:{ all -> 0x0157 }
            monitor-exit(r11)     // Catch:{ all -> 0x0157 }
            java.lang.String r0 = "TnetUtil"
            java.lang.Object[] r4 = new java.lang.Object[r14]     // Catch:{ Exception -> 0x017e }
            java.lang.String r11 = "createSession"
            r4[r6] = r11     // Catch:{ Exception -> 0x017e }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r4)     // Catch:{ Exception -> 0x017e }
            java.lang.Object r0 = Lock_Event     // Catch:{ Exception -> 0x017e }
            r11 = 60000(0xea60, double:2.9644E-319)
            r0.wait(r11)     // Catch:{ Exception -> 0x017e }
            goto L_0x018f
        L_0x0157:
            r0 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0157 }
            throw r0     // Catch:{ Exception -> 0x017e }
        L_0x015a:
            org.android.spdy.SpdySession r0 = spdySessionUT     // Catch:{ Exception -> 0x017e }
            if (r0 == 0) goto L_0x017a
            boolean r0 = bFirstSpdySession     // Catch:{ Exception -> 0x017e }
            if (r0 == 0) goto L_0x016c
            com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x017e }
            boolean r0 = r0.isGzipUpload()     // Catch:{ Exception -> 0x017e }
            if (r0 == 0) goto L_0x017a
        L_0x016c:
            org.android.spdy.SpdySession r0 = spdySessionUT     // Catch:{ Exception -> 0x017e }
            sendCustomControlFrame(r0)     // Catch:{ Exception -> 0x017e }
            java.lang.Object r0 = Lock_Event     // Catch:{ Exception -> 0x017e }
            r11 = 60000(0xea60, double:2.9644E-319)
            r0.wait(r11)     // Catch:{ Exception -> 0x017e }
            goto L_0x018f
        L_0x017a:
            closeSession()     // Catch:{ Exception -> 0x017e }
            goto L_0x018f
        L_0x017e:
            r0 = move-exception
            closeSession()     // Catch:{ all -> 0x0216 }
            java.lang.String r4 = "TnetUtil"
            java.lang.Object[] r11 = new java.lang.Object[r13]     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = "CreateSession Exception"
            r11[r6] = r12     // Catch:{ all -> 0x0216 }
            r11[r14] = r0     // Catch:{ all -> 0x0216 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r4, (java.lang.Object[]) r11)     // Catch:{ all -> 0x0216 }
        L_0x018f:
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0216 }
            r0 = 0
            long r11 = r11 - r8
            r8 = 60000(0xea60, double:2.9644E-319)
            int r0 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1))
            if (r0 < 0) goto L_0x01c3
            com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ all -> 0x0216 }
            boolean r0 = r0.isSelfMonitorTurnOn()     // Catch:{ all -> 0x0216 }
            if (r0 == 0) goto L_0x01b5
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r0 = mMonitor     // Catch:{ all -> 0x0216 }
            int r4 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_REQUEST_TIMEOUT     // Catch:{ all -> 0x0216 }
            java.lang.Double r2 = java.lang.Double.valueOf(r2)     // Catch:{ all -> 0x0216 }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r2 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r4, r5, r2)     // Catch:{ all -> 0x0216 }
            r0.onEvent(r2)     // Catch:{ all -> 0x0216 }
        L_0x01b5:
            closeSession()     // Catch:{ all -> 0x0216 }
            java.lang.String r0 = "TnetUtil"
            java.lang.Object[] r2 = new java.lang.Object[r14]     // Catch:{ all -> 0x0216 }
            java.lang.String r3 = "WAIT_TIMEOUT"
            r2[r6] = r3     // Catch:{ all -> 0x0216 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r0, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0216 }
        L_0x01c3:
            monitor-exit(r7)     // Catch:{ all -> 0x0216 }
            int r0 = sendBytes
            long r2 = (long) r0
            com.alibaba.analytics.core.sync.BizRequest.recordTraffic(r2)
            java.lang.Object r2 = Lock_Object
            monitor-enter(r2)
            protocolBytes = r5     // Catch:{ all -> 0x0213 }
            sendBytes = r6     // Catch:{ all -> 0x0213 }
            monitor-exit(r2)     // Catch:{ all -> 0x0213 }
            int r0 = errorCode
            r1.errCode = r0
            r1.rt = r11
            java.lang.String r0 = com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData
            r1.data = r0
            com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData = r5
            int r0 = errorCode
            mErrorCode = r0
            java.lang.String r0 = "TnetUtil"
            r2 = 6
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r3 = "PostData isSuccess"
            r2[r6] = r3
            boolean r3 = r1.isSuccess()
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            r2[r14] = r3
            java.lang.String r3 = "errCode"
            r2[r13] = r3
            int r3 = r1.errCode
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r2[r10] = r3
            java.lang.String r3 = "rt"
            r4 = 4
            r2[r4] = r3
            r3 = 5
            long r4 = r1.rt
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r2[r3] = r4
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r2)
            return r1
        L_0x0213:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0213 }
            throw r0
        L_0x0216:
            r0 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0216 }
            throw r0
        L_0x0219:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0219 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.TnetUtil.sendRequest(byte[]):com.alibaba.analytics.core.sync.BizResponse");
    }

    /* access modifiers changed from: private */
    public static void sendCustomControlFrame(SpdySession spdySession) {
        synchronized (Lock_Object) {
            while (spdySession == spdySessionUT && spdySessionUT != null && protocolBytes != null && protocolBytes.length > sendBytes) {
                try {
                    if (protocolBytes.length - sendBytes > 131072) {
                        spdySession.sendCustomControlFrame(-1, -1, -1, 131072, ByteUtils.subBytes(protocolBytes, sendBytes, 131072));
                        sendBytes += 131072;
                    } else {
                        int length = protocolBytes.length - sendBytes;
                        if (length > 0) {
                            spdySession.sendCustomControlFrame(-1, -1, -1, length, ByteUtils.subBytes(protocolBytes, sendBytes, length));
                            sendBytes += length;
                        }
                    }
                } catch (SpdyErrorException e) {
                    Logger.e(TAG, "SpdyErrorException", e);
                    if (e.SpdyErrorGetCode() != -3848) {
                        errorCode = e.SpdyErrorGetCode();
                        closeSession();
                    }
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void closeSession() {
        Logger.d();
        synchronized (Lock_Object) {
            if (spdySessionUT != null) {
                spdySessionUT.closeSession();
            }
            spdySessionUT = null;
            BizRequest.closeOutputStream();
            ZipDictUtils.clear();
        }
        sendCallbackNotify();
    }

    /* access modifiers changed from: private */
    public static void sendCallbackNotify() {
        synchronized (Lock_Event) {
            Lock_Event.notifyAll();
        }
    }

    private static class UTSessionCb implements SessionCb, SessionExtraCb {
        private static final String SSL_TIKET_KEY2 = "accs_ssl_key2_";
        private String mSecurityGuardHost;
        private byte[] sslMeta;

        public void bioPingRecvCallback(SpdySession spdySession, int i) {
        }

        public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        }

        public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
        }

        public UTSessionCb(String str) {
            this.mSecurityGuardHost = SSL_TIKET_KEY2 + str;
        }

        public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
            if (spdySession == TnetUtil.spdySessionUT) {
                TnetUtil.sendCustomControlFrame(spdySession);
            }
        }

        public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
            if (spdySession == TnetUtil.spdySessionUT) {
                if (TnetUtil.mResponseCache == null) {
                    ByteArrayOutputStream unused = TnetUtil.mResponseCache = new ByteArrayOutputStream(1024);
                    long unused2 = TnetUtil.mResponseLen = TnetUtil.getResponseBodyLen(bArr);
                }
                if (TnetUtil.mResponseLen != -1) {
                    try {
                        TnetUtil.mResponseCache.write(bArr);
                    } catch (IOException unused3) {
                    }
                    long unused4 = TnetUtil.mResponseReceiveLen = TnetUtil.mResponseReceiveLen + ((long) bArr.length);
                    if (TnetUtil.mResponseLen == TnetUtil.mResponseReceiveLen - 8) {
                        try {
                            TnetUtil.mResponseCache.flush();
                        } catch (IOException unused5) {
                        }
                        byte[] byteArray = TnetUtil.mResponseCache.toByteArray();
                        try {
                            TnetUtil.mResponseCache.close();
                        } catch (IOException unused6) {
                        }
                        int unused7 = TnetUtil.errorCode = BizRequest.parseResult(byteArray);
                        if (TnetUtil.errorCode != 0) {
                            TnetUtil.closeSession();
                        }
                        TnetUtil.sendCallbackNotify();
                        return;
                    }
                    return;
                }
                int unused8 = TnetUtil.errorCode = -1;
                TnetUtil.closeSession();
                TnetUtil.sendCallbackNotify();
                return;
            }
            Logger.w(TnetUtil.TAG, "[spdyCustomControlFrameRecvCallback] session != spdySessionUT");
        }

        public void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
            if (Variables.getInstance().isSelfMonitorTurnOn()) {
                TnetUtil.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.TNET_REQUEST_ERROR, (String) null, Double.valueOf(1.0d)));
            }
            if (spdySession == TnetUtil.spdySessionUT) {
                int unused = TnetUtil.errorCode = i;
                TnetUtil.closeSession();
            }
        }

        public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
            if (spdySession == TnetUtil.spdySessionUT) {
                int unused = TnetUtil.errorCode = i;
                synchronized (TnetUtil.Lock_Object) {
                    SpdySession unused2 = TnetUtil.spdySessionUT = null;
                }
            }
        }

        public byte[] getSSLMeta(SpdySession spdySession) {
            if (!TnetSecuritySDK.getInstance().getInitSecurityCheck()) {
                return this.sslMeta;
            }
            byte[] byteArray = TnetSecuritySDK.getInstance().getByteArray(this.mSecurityGuardHost);
            if (byteArray != null) {
                return byteArray;
            }
            return new byte[0];
        }

        public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
            if (TnetSecuritySDK.getInstance().getInitSecurityCheck()) {
                return securityGuardPutSslTicket2(bArr);
            }
            this.sslMeta = bArr;
            return 0;
        }

        public void spdySessionOnWritable(SpdySession spdySession, Object obj, int i) {
            if (spdySession == TnetUtil.spdySessionUT) {
                TnetUtil.sendCustomControlFrame(spdySession);
            }
        }

        private int securityGuardPutSslTicket2(byte[] bArr) {
            return (bArr == null || TnetSecuritySDK.getInstance().putByteArray(this.mSecurityGuardHost, bArr) == 0) ? -1 : 0;
        }
    }

    /* access modifiers changed from: private */
    public static long getResponseBodyLen(byte[] bArr) {
        if (bArr == null || bArr.length < 12) {
            return -1;
        }
        return (long) ByteUtils.bytesToInt(bArr, 1, 3);
    }

    static void initTnetStream() {
        synchronized (Lock_Object) {
            if (spdySessionUT == null) {
                ZipDictUtils.clear();
                BizRequest.initOutputStream();
                bFirstSpdySession = true;
            } else {
                bFirstSpdySession = false;
            }
        }
    }

    static void refreshMiniWua() {
        if (spdySessionUT == null) {
            BizRequest.mMiniWua = WuaHelper.getMiniWua();
            isGetWuaBeforeSpdySession = true;
        }
    }
}
