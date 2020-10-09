package org.android.spdy;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.UByte;
import kotlin.text.Typography;

public final class SpdyAgent {
    public static final int ACCS_ONLINE_SERVER = 1;
    public static final int ACCS_TEST_SERVER = 0;
    private static final boolean HAVE_CLOSE = false;
    private static final int KB32 = 32768;
    private static final int KB8 = 8192;
    private static final int MAX_SPDY_SESSION_COUNT = 50;
    private static final int MB5 = 5242880;
    static final int MODE_QUIC = 256;
    static final int SPDY_CUSTOM_CONTROL_FRAME_RECV = 4106;
    static final int SPDY_DATA_CHUNK_RECV = 4097;
    static final int SPDY_DATA_RECV = 4098;
    static final int SPDY_DATA_SEND = 4099;
    static final int SPDY_PING_RECV = 4101;
    static final int SPDY_REQUEST_RECV = 4102;
    static final int SPDY_SESSION_CLOSE = 4103;
    static final int SPDY_SESSION_CREATE = 4096;
    static final int SPDY_SESSION_FAILED_ERROR = 4105;
    static final int SPDY_STREAM_CLOSE = 4100;
    static final int SPDY_STREAM_RESPONSE_RECV = 4104;
    private static final String TNET_SO_VERSION = "tnet-4.0.0";
    private static Object domainHashLock = new Object();
    private static HashMap<String, Integer> domainHashMap = new HashMap<>();
    public static volatile boolean enableDebug = false;
    public static volatile boolean enableTimeGaurd = false;
    private static volatile SpdyAgent gSingleInstance = null;
    private static volatile boolean loadSucc = false;
    private static Object lock = new Object();
    private static final Lock r = rwLock.readLock();
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static int totalDomain = 0;
    private static final Lock w = rwLock.writeLock();
    private AccsSSLCallback accsSSLCallback;
    private long agentNativePtr;
    private final QuicCacher cacher = new SecurityGuardCacherImp();
    private AtomicBoolean closed = new AtomicBoolean();
    private Context context = null;
    private volatile boolean enable_header_cache = true;
    private String proxyPassword = null;
    private String proxyUsername = null;
    private HashMap<String, SpdySession> sessionMgr = new HashMap<>(5);
    private LinkedList<SpdySession> sessionQueue = new LinkedList<>();

    private native int closeSessionN(long j);

    private static native int configIpStackModeN(int i);

    private native int configLogFileN(String str, int i, int i2);

    private native int configLogFileN(String str, int i, int i2, int i3);

    private static void crashReporter(int i) {
    }

    private native long createSessionN(long j, SpdySession spdySession, int i, byte[] bArr, char c, byte[] bArr2, char c2, byte[] bArr3, byte[] bArr4, Object obj, int i2, int i3, int i4, byte[] bArr5);

    private native int freeAgent(long j);

    private void getPerformance(SpdySession spdySession, SslPermData sslPermData) {
    }

    private native long getSession(long j, byte[] bArr, char c);

    private native long initAgent(int i, int i2, int i3);

    @Deprecated
    public static void inspect(String str) {
    }

    private native void logFileCloseN();

    private native void logFileFlushN();

    private native int setConTimeout(long j, int i);

    private native int setSessionKind(long j, int i);

    public native String ResolveHost(String str, String str2, int i);

    public void close() {
    }

    @Deprecated
    public void switchAccsServer(int i) {
    }

    public int VerifyProof(String str, int i, String str2, String str3, String[] strArr, String str4) {
        return QuicProofVerifier.VerifyProof(str, i, str2, 36, str3, strArr, (String) null, str4);
    }

    /* access modifiers changed from: package-private */
    public void clearSpdySession(String str, String str2, int i) {
        if (str != null) {
            w.lock();
            if (str != null) {
                try {
                    HashMap<String, SpdySession> hashMap = this.sessionMgr;
                    hashMap.remove(str + str2 + i);
                } catch (Throwable th) {
                    w.unlock();
                    throw th;
                }
            }
            w.unlock();
        }
    }

    public static SpdyAgent getInstance(Context context2, SpdyVersion spdyVersion, SpdySessionKind spdySessionKind) throws UnsatisfiedLinkError, SpdyErrorException {
        if (gSingleInstance == null) {
            synchronized (lock) {
                if (gSingleInstance == null) {
                    gSingleInstance = new SpdyAgent(context2, spdyVersion, spdySessionKind, (AccsSSLCallback) null);
                }
            }
        }
        return gSingleInstance;
    }

    public static boolean checkLoadSucc() {
        return loadSucc;
    }

    @Deprecated
    public static SpdyAgent getInstance(Context context2, SpdyVersion spdyVersion, SpdySessionKind spdySessionKind, AccsSSLCallback accsSSLCallback2) throws UnsatisfiedLinkError, SpdyErrorException {
        if (gSingleInstance == null) {
            synchronized (lock) {
                if (gSingleInstance == null) {
                    gSingleInstance = new SpdyAgent(context2, spdyVersion, spdySessionKind, accsSSLCallback2);
                }
            }
        }
        return gSingleInstance;
    }

    private int getDomainHashIndex(String str) {
        Integer num;
        synchronized (domainHashLock) {
            num = domainHashMap.get(str);
            if (num == null) {
                HashMap<String, Integer> hashMap = domainHashMap;
                int i = totalDomain + 1;
                totalDomain = i;
                hashMap.put(str, Integer.valueOf(i));
                num = Integer.valueOf(totalDomain);
            }
        }
        return num.intValue();
    }

    private SpdyAgent(Context context2, SpdyVersion spdyVersion, SpdySessionKind spdySessionKind, AccsSSLCallback accsSSLCallback2) throws UnsatisfiedLinkError {
        try {
            SoInstallMgrSdk.init(context2);
            loadSucc = SoInstallMgrSdk.initSo(TNET_SO_VERSION, 1);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            this.agentNativePtr = initAgent(spdyVersion.getInt(), spdySessionKind.getint(), SslVersion.SLIGHT_VERSION_V1.getint());
            this.accsSSLCallback = accsSSLCallback2;
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
        this.context = context2;
        this.closed.set(false);
    }

    @Deprecated
    public static void InitializeCerts() {
        AndroidTrustAnchors.getInstance().Initialize();
    }

    public void InitializeSecurityStuff() {
        this.cacher.init(this.context);
        AndroidTrustAnchors.getInstance().Initialize();
    }

    private void checkLoadSo() throws SpdyErrorException {
        if (!loadSucc) {
            try {
                synchronized (lock) {
                    if (!loadSucc) {
                        loadSucc = SoInstallMgrSdk.initSo(TNET_SO_VERSION, 1);
                        this.agentNativePtr = initAgent(0, 0, 0);
                    } else {
                        return;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } else {
            return;
        }
        if (!loadSucc) {
            throw new SpdyErrorException("TNET_JNI_ERR_LOAD_SO_FAIL", (int) TnetStatusCode.TNET_JNI_ERR_LOAD_SO_FAIL);
        }
    }

    public void setProxyUsernamePassword(String str, String str2) {
        this.proxyUsername = str;
        this.proxyPassword = str2;
    }

    static void securityCheck(int i, int i2) {
        if (i >= 32768) {
            throw new SpdyErrorException("SPDY_JNI_ERR_INVALID_PARAM:total=" + i, (int) TnetStatusCode.TNET_JNI_ERR_INVLID_PARAM);
        } else if (i2 >= 8192) {
            throw new SpdyErrorException("SPDY_JNI_ERR_INVALID_PARAM:value=" + i2, (int) TnetStatusCode.TNET_JNI_ERR_INVLID_PARAM);
        }
    }

    static void tableListJudge(int i) {
        if (i >= 5242880) {
            throw new SpdyErrorException("SPDY_JNI_ERR_INVALID_PARAM:total=" + i, (int) TnetStatusCode.TNET_JNI_ERR_INVLID_PARAM);
        }
    }

    static void InvlidCharJudge(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            if ((bArr[i] & UByte.MAX_VALUE) < 32 || (bArr[i] & UByte.MAX_VALUE) > 126) {
                bArr[i] = 63;
            }
        }
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            if ((bArr2[i2] & UByte.MAX_VALUE) < 32 || (bArr2[i2] & UByte.MAX_VALUE) > 126) {
                bArr2[i2] = 63;
            }
        }
    }

    static void headJudge(Map<String, String> map) {
        if (map != null) {
            int i = 0;
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                InvlidCharJudge(str.getBytes(), str2.getBytes());
                i += str.length() + 1 + str2.length();
                securityCheck(i, str2.length());
            }
        }
    }

    static String mapBodyToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (map == null) {
            return null;
        }
        int i = 0;
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            String str2 = (String) next.getValue();
            sb.append(str);
            sb.append('=');
            sb.append(str2);
            sb.append(Typography.amp);
            i += str.length() + 1 + str2.length();
            tableListJudge(i);
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    static byte[] dataproviderToByteArray(SpdyRequest spdyRequest, SpdyDataProvider spdyDataProvider) {
        byte[] bArr;
        headJudge(spdyRequest.getHeaders());
        if (spdyDataProvider == null) {
            return null;
        }
        String mapBodyToString = mapBodyToString(spdyDataProvider.postBody);
        if (mapBodyToString != null) {
            bArr = mapBodyToString.getBytes();
        } else {
            bArr = spdyDataProvider.data;
        }
        if (bArr == null || bArr.length < 5242880) {
            return bArr;
        }
        throw new SpdyErrorException("SPDY_JNI_ERR_INVALID_PARAM:total=" + bArr.length, (int) TnetStatusCode.TNET_JNI_ERR_INVLID_PARAM);
    }

    @Deprecated
    public SpdySession createSession(String str, Object obj, SessionCb sessionCb, int i) throws SpdyErrorException {
        return createSession(str, "", obj, sessionCb, (SslCertcb) null, i, 0);
    }

    @Deprecated
    public SpdySession createSession(String str, String str2, Object obj, SessionCb sessionCb, int i) throws SpdyErrorException {
        return createSession(str, str2, obj, sessionCb, (SslCertcb) null, i, 0);
    }

    @Deprecated
    public SpdySession createSession(String str, Object obj, SessionCb sessionCb, SslCertcb sslCertcb, int i) throws SpdyErrorException {
        return createSession(str, "", obj, sessionCb, sslCertcb, i, 0);
    }

    public SpdySession createSession(SessionInfo sessionInfo) throws SpdyErrorException {
        return createSession(sessionInfo.getAuthority(), sessionInfo.getDomain(), sessionInfo.getSessonUserData(), sessionInfo.getSessionCb(), (SslCertcb) null, sessionInfo.getMode(), sessionInfo.getPubKeySeqNum(), sessionInfo.getConnectionTimeoutMs(), sessionInfo.getCertHost());
    }

    @Deprecated
    public SpdySession createSession(String str, String str2, Object obj, SessionCb sessionCb, SslCertcb sslCertcb, int i, int i2) throws SpdyErrorException {
        return createSession(str, str2, obj, sessionCb, sslCertcb, i, i2, -1);
    }

    public SpdySession createSession(String str, String str2, Object obj, SessionCb sessionCb, SslCertcb sslCertcb, int i, int i2, int i3) throws SpdyErrorException {
        return createSession(str, str2, obj, sessionCb, sslCertcb, i, i2, i3, (String) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x0186 A[Catch:{ all -> 0x0147 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x018b A[Catch:{ all -> 0x0147 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0190 A[Catch:{ all -> 0x0147 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.android.spdy.SpdySession createSession(java.lang.String r27, java.lang.String r28, java.lang.Object r29, org.android.spdy.SessionCb r30, org.android.spdy.SslCertcb r31, int r32, int r33, int r34, java.lang.String r35) throws org.android.spdy.SpdyErrorException {
        /*
            r26 = this;
            r15 = r26
            r0 = r27
            r14 = r28
            r11 = r32
            if (r0 == 0) goto L_0x01fa
            java.lang.String r1 = "/"
            java.lang.String[] r1 = r0.split(r1)
            r13 = 0
            r2 = r1[r13]
            r3 = 58
            int r2 = r2.lastIndexOf(r3)
            r3 = r1[r13]
            java.lang.String r12 = r3.substring(r13, r2)
            r3 = r1[r13]
            r10 = 1
            int r2 = r2 + r10
            java.lang.String r16 = r3.substring(r2)
            java.lang.String r2 = "0.0.0.0"
            byte[] r2 = r2.getBytes()
            int r3 = r1.length
            if (r3 == r10) goto L_0x004b
            r1 = r1[r10]
            java.lang.String r2 = ":"
            java.lang.String[] r1 = r1.split(r2)
            r2 = r1[r13]
            byte[] r2 = r2.getBytes()
            r1 = r1[r10]
            int r1 = java.lang.Integer.parseInt(r1)
            char r1 = (char) r1
            r9 = r0
            r18 = r1
            r17 = r2
            goto L_0x0061
        L_0x004b:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = "/0.0.0.0:0"
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            r9 = r1
            r17 = r2
            r18 = 0
        L_0x0061:
            r26.agentIsOpen()
            java.util.concurrent.locks.Lock r1 = r
            r1.lock()
            java.util.HashMap<java.lang.String, org.android.spdy.SpdySession> r1 = r15.sessionMgr     // Catch:{ all -> 0x01f2 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01f2 }
            r2.<init>()     // Catch:{ all -> 0x01f2 }
            r2.append(r9)     // Catch:{ all -> 0x01f2 }
            r2.append(r14)     // Catch:{ all -> 0x01f2 }
            r2.append(r11)     // Catch:{ all -> 0x01f2 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x01f2 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x01f2 }
            org.android.spdy.SpdySession r1 = (org.android.spdy.SpdySession) r1     // Catch:{ all -> 0x01f2 }
            java.util.HashMap<java.lang.String, org.android.spdy.SpdySession> r2 = r15.sessionMgr     // Catch:{ all -> 0x01f2 }
            int r2 = r2.size()     // Catch:{ all -> 0x01f2 }
            r3 = 50
            if (r2 < r3) goto L_0x008f
            r2 = 1
            goto L_0x0090
        L_0x008f:
            r2 = 0
        L_0x0090:
            java.util.concurrent.locks.Lock r3 = r
            r3.unlock()
            if (r2 != 0) goto L_0x01e7
            if (r1 == 0) goto L_0x009d
            r1.increRefCount()
            return r1
        L_0x009d:
            java.util.concurrent.locks.Lock r1 = w
            r1.lock()
            r19 = 0
            java.util.HashMap<java.lang.String, org.android.spdy.SpdySession> r1 = r15.sessionMgr     // Catch:{ Throwable -> 0x00bf }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00bf }
            r2.<init>()     // Catch:{ Throwable -> 0x00bf }
            r2.append(r9)     // Catch:{ Throwable -> 0x00bf }
            r2.append(r14)     // Catch:{ Throwable -> 0x00bf }
            r2.append(r11)     // Catch:{ Throwable -> 0x00bf }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00bf }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ Throwable -> 0x00bf }
            org.android.spdy.SpdySession r1 = (org.android.spdy.SpdySession) r1     // Catch:{ Throwable -> 0x00bf }
            goto L_0x00c1
        L_0x00bf:
            r1 = r19
        L_0x00c1:
            if (r1 == 0) goto L_0x00cc
            java.util.concurrent.locks.Lock r0 = w
            r0.unlock()
            r1.increRefCount()
            return r1
        L_0x00cc:
            org.android.spdy.SpdySession r8 = new org.android.spdy.SpdySession     // Catch:{ all -> 0x01df }
            r2 = 0
            r1 = r8
            r4 = r26
            r5 = r9
            r6 = r28
            r7 = r30
            r20 = r8
            r8 = r32
            r21 = r9
            r9 = r33
            r22 = 1
            r10 = r29
            r1.<init>(r2, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x01df }
            if (r35 != 0) goto L_0x00ec
            r23 = r19
            goto L_0x00f2
        L_0x00ec:
            byte[] r1 = r35.getBytes()     // Catch:{ all -> 0x01df }
            r23 = r1
        L_0x00f2:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x01df }
            r1.<init>()     // Catch:{ all -> 0x01df }
            r1.append(r14)     // Catch:{ all -> 0x01df }
            r1.append(r11)     // Catch:{ all -> 0x01df }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01df }
            int r5 = r15.getDomainHashIndex(r1)     // Catch:{ all -> 0x01df }
            r1 = r11 & 4
            if (r1 == 0) goto L_0x010c
            r1 = r11 | 8
            r11 = r1
        L_0x010c:
            java.lang.String r1 = r15.proxyUsername     // Catch:{ all -> 0x01df }
            if (r1 == 0) goto L_0x014c
            java.lang.String r1 = r15.proxyPassword     // Catch:{ all -> 0x0147 }
            if (r1 == 0) goto L_0x014c
            long r2 = r15.agentNativePtr     // Catch:{ all -> 0x0147 }
            byte[] r6 = r12.getBytes()     // Catch:{ all -> 0x0147 }
            int r1 = java.lang.Integer.parseInt(r16)     // Catch:{ all -> 0x0147 }
            char r7 = (char) r1     // Catch:{ all -> 0x0147 }
            java.lang.String r1 = r15.proxyUsername     // Catch:{ all -> 0x0147 }
            byte[] r10 = r1.getBytes()     // Catch:{ all -> 0x0147 }
            java.lang.String r1 = r15.proxyPassword     // Catch:{ all -> 0x0147 }
            byte[] r12 = r1.getBytes()     // Catch:{ all -> 0x0147 }
            r1 = r26
            r4 = r20
            r8 = r17
            r9 = r18
            r24 = r11
            r11 = r12
            r12 = r29
            r25 = 0
            r13 = r24
            r14 = r33
            r15 = r34
            r16 = r23
            long r1 = r1.createSessionN(r2, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)     // Catch:{ all -> 0x0147 }
            goto L_0x0175
        L_0x0147:
            r0 = move-exception
            r1 = r26
            goto L_0x01e1
        L_0x014c:
            r24 = r11
            r25 = 0
            r15 = r26
            long r2 = r15.agentNativePtr     // Catch:{ all -> 0x01df }
            byte[] r6 = r12.getBytes()     // Catch:{ all -> 0x01df }
            int r1 = java.lang.Integer.parseInt(r16)     // Catch:{ all -> 0x01df }
            char r7 = (char) r1
            r10 = 0
            r11 = 0
            r1 = r26
            r4 = r20
            r8 = r17
            r9 = r18
            r12 = r29
            r13 = r24
            r14 = r33
            r15 = r34
            r16 = r23
            long r1 = r1.createSessionN(r2, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)     // Catch:{ all -> 0x0147 }
        L_0x0175:
            java.lang.String r3 = "tnet-jni"
            java.lang.String r4 = " create new session: "
            org.android.spdy.spduLog.Logi((java.lang.String) r3, (java.lang.String) r4, (java.lang.Object) r0)     // Catch:{ all -> 0x0147 }
            r3 = 1
            long r5 = r1 & r3
            r7 = 0
            int r0 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x018b
            long r0 = r1 >> r22
            int r13 = (int) r0     // Catch:{ all -> 0x0147 }
            r1 = r7
            goto L_0x018c
        L_0x018b:
            r13 = 0
        L_0x018c:
            int r0 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r0 == 0) goto L_0x01bc
            r0 = r20
            r0.setSessionNativePtr(r1)     // Catch:{ all -> 0x0147 }
            r1 = r26
            java.util.HashMap<java.lang.String, org.android.spdy.SpdySession> r2 = r1.sessionMgr     // Catch:{ all -> 0x01ba }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01ba }
            r3.<init>()     // Catch:{ all -> 0x01ba }
            r4 = r21
            r3.append(r4)     // Catch:{ all -> 0x01ba }
            r4 = r28
            r3.append(r4)     // Catch:{ all -> 0x01ba }
            r11 = r24
            r3.append(r11)     // Catch:{ all -> 0x01ba }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01ba }
            r2.put(r3, r0)     // Catch:{ all -> 0x01ba }
            java.util.LinkedList<org.android.spdy.SpdySession> r2 = r1.sessionQueue     // Catch:{ all -> 0x01ba }
            r2.add(r0)     // Catch:{ all -> 0x01ba }
            goto L_0x01c2
        L_0x01ba:
            r0 = move-exception
            goto L_0x01e1
        L_0x01bc:
            r1 = r26
            if (r13 != 0) goto L_0x01c8
            r0 = r19
        L_0x01c2:
            java.util.concurrent.locks.Lock r2 = w
            r2.unlock()
            return r0
        L_0x01c8:
            org.android.spdy.SpdyErrorException r0 = new org.android.spdy.SpdyErrorException     // Catch:{ all -> 0x01ba }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x01ba }
            r2.<init>()     // Catch:{ all -> 0x01ba }
            java.lang.String r3 = "create session error: "
            r2.append(r3)     // Catch:{ all -> 0x01ba }
            r2.append(r13)     // Catch:{ all -> 0x01ba }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x01ba }
            r0.<init>((java.lang.String) r2, (int) r13)     // Catch:{ all -> 0x01ba }
            throw r0     // Catch:{ all -> 0x01ba }
        L_0x01df:
            r0 = move-exception
            r1 = r15
        L_0x01e1:
            java.util.concurrent.locks.Lock r2 = w
            r2.unlock()
            throw r0
        L_0x01e7:
            r1 = r15
            org.android.spdy.SpdyErrorException r0 = new org.android.spdy.SpdyErrorException
            r2 = -1105(0xfffffffffffffbaf, float:NaN)
            java.lang.String r3 = "SPDY_SESSION_EXCEED_MAXED: session count exceed max"
            r0.<init>((java.lang.String) r3, (int) r2)
            throw r0
        L_0x01f2:
            r0 = move-exception
            r1 = r15
            java.util.concurrent.locks.Lock r2 = r
            r2.unlock()
            throw r0
        L_0x01fa:
            r1 = r15
            org.android.spdy.SpdyErrorException r0 = new org.android.spdy.SpdyErrorException
            r2 = -1102(0xfffffffffffffbb2, float:NaN)
            java.lang.String r3 = "SPDY_JNI_ERR_INVALID_PARAM"
            r0.<init>((java.lang.String) r3, (int) r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.spdy.SpdyAgent.createSession(java.lang.String, java.lang.String, java.lang.Object, org.android.spdy.SessionCb, org.android.spdy.SslCertcb, int, int, int, java.lang.String):org.android.spdy.SpdySession");
    }

    @Deprecated
    public SpdySession submitRequest(SpdyRequest spdyRequest, SpdyDataProvider spdyDataProvider, Object obj, Object obj2, Spdycb spdycb, SessionCb sessionCb, SslCertcb sslCertcb, int i) throws SpdyErrorException {
        SpdySession createSession = createSession(spdyRequest.getAuthority(), spdyRequest.getDomain(), obj, sessionCb, sslCertcb, i, 0, spdyRequest.getConnectionTimeoutMs());
        SpdyRequest spdyRequest2 = spdyRequest;
        SpdyDataProvider spdyDataProvider2 = spdyDataProvider;
        Object obj3 = obj2;
        Spdycb spdycb2 = spdycb;
        createSession.submitRequest(spdyRequest, spdyDataProvider, obj2, spdycb);
        return createSession;
    }

    @Deprecated
    public SpdySession submitRequest(SpdyRequest spdyRequest, SpdyDataProvider spdyDataProvider, Object obj, Object obj2, Spdycb spdycb, SessionCb sessionCb, SslCertcb sslCertcb, int i, int i2) throws SpdyErrorException {
        SpdySession createSession = createSession(spdyRequest.getAuthority(), spdyRequest.getDomain(), obj, sessionCb, sslCertcb, i, i2, spdyRequest.getConnectionTimeoutMs());
        SpdyRequest spdyRequest2 = spdyRequest;
        SpdyDataProvider spdyDataProvider2 = spdyDataProvider;
        Object obj3 = obj2;
        Spdycb spdycb2 = spdycb;
        createSession.submitRequest(spdyRequest, spdyDataProvider, obj2, spdycb);
        return createSession;
    }

    public SpdySession submitRequest(SpdyRequest spdyRequest, SpdyDataProvider spdyDataProvider, Object obj, Object obj2, Spdycb spdycb, SessionCb sessionCb, int i, int i2) throws SpdyErrorException {
        return submitRequest(spdyRequest, spdyDataProvider, obj, obj2, spdycb, sessionCb, (SslCertcb) null, i, i2);
    }

    @Deprecated
    public SpdySession submitRequest(SpdyRequest spdyRequest, SpdyDataProvider spdyDataProvider, Object obj, Object obj2, Spdycb spdycb, SessionCb sessionCb, int i) throws SpdyErrorException {
        return submitRequest(spdyRequest, spdyDataProvider, obj, obj2, spdycb, sessionCb, (SslCertcb) null, i);
    }

    private void agentIsOpen() {
        if (!this.closed.get()) {
            checkLoadSo();
            return;
        }
        throw new SpdyErrorException("SPDY_JNI_ERR_ASYNC_CLOSE", (int) TnetStatusCode.TNET_JNI_ERR_ASYNC_CLOSE);
    }

    /* access modifiers changed from: package-private */
    public void removeSession(SpdySession spdySession) {
        w.lock();
        try {
            this.sessionQueue.remove(spdySession);
        } finally {
            w.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public int closeSession(long j) {
        return closeSessionN(j);
    }

    static String[] mapToByteArray(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        String[] strArr = new String[(map.size() * 2)];
        int i = 0;
        for (Map.Entry next : map.entrySet()) {
            strArr[i] = (String) next.getKey();
            strArr[i + 1] = (String) next.getValue();
            i += 2;
        }
        return strArr;
    }

    static Map<String, List<String>> stringArrayToMap(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        HashMap hashMap = new HashMap(5);
        int i = 0;
        while (true) {
            int i2 = i + 2;
            if (i2 <= strArr.length) {
                if (strArr[i] == null) {
                    break;
                }
                int i3 = i + 1;
                if (strArr[i3] == null) {
                    break;
                }
                List list = (List) hashMap.get(strArr[i]);
                if (list == null) {
                    list = new ArrayList(1);
                    hashMap.put(strArr[i], list);
                }
                list.add(strArr[i3]);
                i = i2;
            } else {
                return hashMap;
            }
        }
        return null;
    }

    @Deprecated
    public int setSessionKind(SpdySessionKind spdySessionKind) {
        agentIsOpen();
        try {
            return setSessionKind(this.agentNativePtr, spdySessionKind.getint());
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Deprecated
    public int setConnectTimeOut(int i) {
        agentIsOpen();
        try {
            return setConTimeout(this.agentNativePtr, i);
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setAccsSslCallback(AccsSSLCallback accsSSLCallback2) {
        spduLog.Logi("tnet-jni", "[setAccsSslCallback] - ", (Object) accsSSLCallback2.getClass());
        this.accsSSLCallback = accsSSLCallback2;
    }

    private void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        spduLog.Logi("tnet-jni", "[spdySessionConnectCB] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdySessionConnectCB] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdySessionConnectCB] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdySessionConnectCB(spdySession, superviseConnectInfo);
        }
        if (spdySession != null) {
            spdySession.setQuicConnectionID(superviseConnectInfo.quicConnectionID);
        }
    }

    private void spdyDataChunkRecvCB(SpdySession spdySession, boolean z, int i, SpdyByteArray spdyByteArray, int i2) {
        spduLog.Logi("tnet-jni", "[spdyDataChunkRecvCB] - ");
        long j = 4294967295L & ((long) i);
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyDataChunkRecvCB] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyDataChunkRecvCB] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyDataChunkRecvCB(spdySession, z, j, spdyByteArray, i2);
        }
    }

    private void spdyDataRecvCallback(SpdySession spdySession, boolean z, int i, int i2, int i3) {
        spduLog.Logi("tnet-jni", "[spdyDataRecvCallback] - ");
        long j = 4294967295L & ((long) i);
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyDataRecvCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyDataRecvCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyDataRecvCallback(spdySession, z, j, i2, i3);
        }
    }

    private void spdyDataSendCallback(SpdySession spdySession, boolean z, int i, int i2, int i3) {
        long j = 4294967295L & ((long) i);
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyDataSendCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyDataSendCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyDataSendCallback(spdySession, z, j, i2, i3);
        }
    }

    private void spdyStreamCloseCallback(SpdySession spdySession, int i, int i2, int i3, SuperviseData superviseData) {
        spduLog.Logi("tnet-jni", "[spdyStreamCloseCallback] - ");
        long j = ((long) i) & 4294967295L;
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyStreamCloseCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyStreamCloseCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyStreamCloseCallback(spdySession, j, i2, i3, superviseData);
        }
    }

    private void spdyPingRecvCallback(SpdySession spdySession, int i, Object obj) {
        spduLog.Logi("tnet-jni", "[spdyPingRecvCallback] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyPingRecvCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyPingRecvCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyPingRecvCallback(spdySession, (long) i, obj);
        }
    }

    private void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
        spduLog.Logi("tnet-jni", "[spdyCustomControlFrameRecvCallback] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyCustomControlFrameRecvCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyCustomControlFrameRecvCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyCustomControlFrameRecvCallback(spdySession, obj, i, i2, i3, i4, bArr);
        }
    }

    private void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        spduLog.Logi("tnet-jni", "[spdyCustomControlFrameFailCallback] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyCustomControlFrameFailCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyCustomControlFrameFailCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyCustomControlFrameFailCallback(spdySession, obj, i, i2);
        }
    }

    private void bioPingRecvCallback(SpdySession spdySession, int i) {
        spduLog.Logi("tnet-jni", "[bioPingRecvCallback] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[bioPingRecvCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[bioPingRecvCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.bioPingRecvCallback(spdySession, i);
        }
    }

    private void spdyRequestRecvCallback(SpdySession spdySession, int i, int i2) {
        long j = ((long) i) & 4294967295L;
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyRequestRecvCallback] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyRequestRecvCallback] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyRequestRecvCallback(spdySession, j, i2);
        }
    }

    private void spdyStreamResponseRecv(SpdySession spdySession, int i, byte[] bArr, int[] iArr, int i2) {
        spduLog.Logi("tnet-jni", "[spdyStreamResponseRecv] - ");
        String[] strArr = new String[iArr.length];
        int i3 = 0;
        if (this.enable_header_cache) {
            HTTPHeaderPool instance = HTTPHeaderPool.getInstance();
            int i4 = 0;
            while (i3 < iArr.length) {
                strArr[i3] = instance.GetValueString(ByteBuffer.wrap(bArr, i4, iArr[i3]));
                int i5 = i4 + iArr[i3];
                int i6 = i3 + 1;
                if (iArr[i6] > 32) {
                    strArr[i6] = new String(bArr, i5, iArr[i6]);
                } else {
                    strArr[i6] = instance.GetValueString(ByteBuffer.wrap(bArr, i5, iArr[i6]));
                }
                i4 = i5 + iArr[i6];
                i3 += 2;
            }
        } else {
            int i7 = 0;
            while (i3 < iArr.length) {
                try {
                    strArr[i3] = new String(bArr, i7, iArr[i3], "utf-8");
                    i7 += iArr[i3];
                    int i8 = i3 + 1;
                    strArr[i8] = new String(bArr, i7, iArr[i8], "utf-8");
                    i7 += iArr[i8];
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i3 += 2;
            }
        }
        Map<String, List<String>> stringArrayToMap = stringArrayToMap(strArr);
        long j = ((long) i) & 4294967295L;
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdyStreamResponseRecv] - session is null");
        } else if (spdySession.intenalcb == null) {
            spduLog.Logi("tnet-jni", "[spdyStreamResponseRecv] - session.intenalcb is null");
        } else {
            spdySession.intenalcb.spdyOnStreamResponse(spdySession, j, stringArrayToMap, i2);
        }
    }

    private void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
        spduLog.Logi("tnet-jni", "[spdySessionCloseCallback] - errorCode = ", (long) i);
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdySessionCloseCallback] - session is null");
        } else {
            try {
                if (spdySession.intenalcb == null) {
                    spduLog.Logi("tnet-jni", "[spdySessionCloseCallback] - session.intenalcb is null");
                } else {
                    spdySession.intenalcb.spdySessionCloseCallback(spdySession, obj, superviseConnectInfo, i);
                }
            } finally {
                spdySession.cleanUp();
            }
        }
        spdySession.releasePptr();
    }

    private void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
        spduLog.Logi("tnet-jni", "[spdySessionFailedError] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdySessionFailedError] - session is null");
        } else {
            try {
                if (spdySession.intenalcb == null) {
                    spduLog.Logi("tnet-jni", "[spdySessionFailedError] - session.intenalcb is null");
                } else {
                    spdySession.intenalcb.spdySessionFailedError(spdySession, i, obj);
                }
            } finally {
                spdySession.cleanUp();
            }
        }
        spdySession.releasePptr();
    }

    private void spdySessionOnWritable(SpdySession spdySession, Object obj, int i) {
        spduLog.Logi("tnet-jni", "[spdySessionOnWritable] - ");
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[spdySessionOnWritable] - session is null");
            return;
        }
        try {
            if (spdySession.intenalcb == null) {
                spduLog.Logi("tnet-jni", "[spdySessionOnWritable] - session.intenalcb is null");
            } else {
                spdySession.intenalcb.spdySessionOnWritable(spdySession, obj, i);
            }
        } catch (Throwable th) {
            spduLog.Loge("tnet-jni", "[spdySessionOnWritable] - exception:", th);
        }
    }

    private byte[] getSSLPublicKey(int i, byte[] bArr) {
        if (this.accsSSLCallback != null) {
            return this.accsSSLCallback.getSSLPublicKey(i, bArr);
        }
        spduLog.Logd("tnet-jni", "[getSSLPublicKey] - accsSSLCallback is null.");
        return null;
    }

    private int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[putSSLMeta] - session is null");
            return -1;
        } else if (spdySession.intenalcb != null) {
            return spdySession.intenalcb.putSSLMeta(spdySession, bArr);
        } else {
            spduLog.Logi("tnet-jni", "[putSSLMeta] - session.intenalcb is null");
            return -1;
        }
    }

    private byte[] getSSLMeta(SpdySession spdySession) {
        if (spdySession == null) {
            spduLog.Logi("tnet-jni", "[getSSLMeta] - session is null");
            return null;
        } else if (spdySession.intenalcb != null) {
            return spdySession.intenalcb.getSSLMeta(spdySession);
        } else {
            spduLog.Logi("tnet-jni", "[getSSLMeta] - session.intenalcb is null");
            return null;
        }
    }

    public HashMap<String, SpdySession> getAllSession() {
        return this.sessionMgr;
    }

    public int configLogFile(String str, int i, int i2) {
        if (loadSucc) {
            return configLogFileN(str, i, i2);
        }
        return -1;
    }

    public int configLogFile(String str, int i, int i2, int i3) {
        if (loadSucc) {
            return configLogFileN(str, i, i2, i3);
        }
        return -1;
    }

    public void logFileFlush() {
        if (loadSucc) {
            logFileFlushN();
        }
    }

    public void logFileClose() {
        if (loadSucc) {
            logFileFlushN();
            logFileCloseN();
        }
    }

    public boolean cacher_store(String str, String str2) {
        if (this.cacher != null) {
            return this.cacher.store(str, str2);
        }
        return false;
    }

    public byte[] cacher_load(String str) {
        if (this.cacher != null) {
            return this.cacher.load(str);
        }
        return null;
    }

    public static int configIpStackMode(int i) {
        spduLog.Logi("tnet-jni", "[configIpStackMode] - ", (long) i);
        return configIpStackModeN(i);
    }

    public void disableHeaderCache() {
        this.enable_header_cache = false;
    }
}
