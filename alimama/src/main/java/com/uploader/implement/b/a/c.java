package com.uploader.implement.b.a;

import android.content.Context;
import java.util.LinkedList;
import java.util.List;
import org.android.spdy.AccsSSLCallback;
import org.android.spdy.SessionCb;
import org.android.spdy.SessionExtraCb;
import org.android.spdy.SessionInfo;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdyProtocol;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.SuperviseConnectInfo;

/* compiled from: CustomizedSession */
public class c implements SessionCb, SessionExtraCb {
    /* access modifiers changed from: private */
    public com.uploader.implement.c a;
    private SpdyAgent b;
    private SpdySession c;
    /* access modifiers changed from: private */
    public final Context d;
    private final f e;
    private volatile a f;
    private volatile String g = "DISCONNECTED";
    private List<b> h = new LinkedList();
    private final int i;

    /* compiled from: CustomizedSession */
    interface a {
        void a();

        void a(int i);

        void a(int i, int i2);

        void a(byte[] bArr, int i);

        void b(int i);
    }

    public void bioPingRecvCallback(SpdySession spdySession, int i2) {
    }

    public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
    }

    /* compiled from: CustomizedSession */
    private static class b {
        byte[] a;
        int b;
        int c;

        public b(byte[] bArr, int i, int i2) {
            this.a = bArr;
            this.b = i;
            this.c = i2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(32);
            sb.append(hashCode());
            sb.append(" WaitingData{ length=");
            sb.append(this.b);
            sb.append(", sendSequence=");
            sb.append(this.c);
            sb.append("}");
            return sb.toString();
        }
    }

    public c(com.uploader.implement.c cVar, f fVar) {
        this.a = cVar;
        this.d = cVar.c;
        this.e = fVar;
        this.i = hashCode();
    }

    public void a(a aVar) {
        this.f = aVar;
    }

    public void a() {
        if (c()) {
            try {
                this.g = "CONNECTING";
                SessionInfo sessionInfo = new SessionInfo(this.e.a, this.e.b, Integer.toString(this.i), (String) null, 0, (Object) null, this, this.e.f ? SpdyProtocol.SSSL_0RTT_CUSTOM : 16);
                sessionInfo.setConnectionTimeoutMs(10000);
                if (this.e.f) {
                    if (2 == this.a.b.getCurrentElement().environment) {
                        sessionInfo.setPubKeySeqNum(0);
                    } else {
                        sessionInfo.setPubKeySeqNum(6);
                    }
                }
                if (this.b == null) {
                    d();
                }
                this.c = this.b.createSession(sessionInfo);
                if (com.uploader.implement.a.a(4)) {
                    com.uploader.implement.a.a(4, "CustomizedSession", this.i + " CustomizedSession createSession,mSession:" + this.c.hashCode() + " getRefCount:" + this.c.getRefCount());
                }
            } catch (SpdyErrorException e2) {
                this.g = "CONNECTFAILED";
                if (com.uploader.implement.a.a(16)) {
                    com.uploader.implement.a.a(16, "CustomizedSession", this.i + "CustomizedSession connect failed", e2);
                }
                a(e2.SpdyErrorGetCode());
            }
        } else if (com.uploader.implement.a.a(8)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.i);
            sb.append(" CustomizedSession already connected,mSession:");
            sb.append(this.c != null ? Integer.valueOf(this.c.hashCode()) : "");
            com.uploader.implement.a.a(8, "CustomizedSession", sb.toString());
        }
    }

    public void b() {
        if (this.c != null) {
            this.c.closeSession();
            if (com.uploader.implement.a.a(4)) {
                com.uploader.implement.a.a(4, "CustomizedSession", this.i + " CustomizedSession closeSession,session:" + this.c.hashCode());
            }
        }
        this.g = "DISCONNECTED";
    }

    public void a(int i2, byte[] bArr, int i3) {
        try {
            if (!c()) {
                this.c.sendCustomControlFrame(i2, -1, -1, i3, bArr);
                if (com.uploader.implement.a.a(4)) {
                    StringBuilder sb = new StringBuilder(64);
                    sb.append(this.i);
                    sb.append(" send sendCustomControlFrame. sequence=");
                    sb.append(i2);
                    sb.append(", length=");
                    sb.append(i3);
                    sb.append(", mSession:");
                    sb.append(this.c.hashCode());
                    com.uploader.implement.a.a(4, "CustomizedSession", sb.toString());
                }
                if (this.f != null) {
                    this.f.b(i2);
                }
            } else if (com.uploader.implement.a.a(16)) {
                StringBuilder sb2 = new StringBuilder(64);
                sb2.append(this.i);
                sb2.append(" send failed, needConnect and return, sequence:");
                sb2.append(i2);
                sb2.append(", length=");
                sb2.append(i3);
                sb2.append(", mSession:");
                sb2.append(this.c != null ? Integer.valueOf(this.c.hashCode()) : "");
                com.uploader.implement.a.a(16, "CustomizedSession", sb2.toString());
            }
        } catch (SpdyErrorException e2) {
            int SpdyErrorGetCode = e2.SpdyErrorGetCode();
            if (-3848 == SpdyErrorGetCode) {
                a(new b(bArr, i3, i2));
                return;
            }
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "CustomizedSession", this.i + " send sendCustomControlFrame failed", e2);
            }
            if (this.f != null) {
                this.f.a(i2, SpdyErrorGetCode);
            }
        }
    }

    private void d() {
        try {
            SpdyAgent.enableDebug = false;
            this.b = SpdyAgent.getInstance(this.d, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
            if (this.e.f) {
                this.b.setAccsSslCallback(new AccsSSLCallback() {
                    public byte[] getSSLPublicKey(int i, byte[] bArr) {
                        try {
                            return c.this.a.b.decrypt(c.this.d, SpdyProtocol.TNET_PUBKEY_SG_KEY, bArr);
                        } catch (Exception e) {
                            if (!com.uploader.implement.a.a(16)) {
                                return null;
                            }
                            com.uploader.implement.a.a(16, "CustomizedSession", "call config.decrypt error.", e);
                            return null;
                        }
                    }
                });
            }
            if (com.uploader.implement.a.a(4)) {
                com.uploader.implement.a.a(4, "CustomizedSession", this.i + " initSpdyAgent");
            }
        } catch (Exception e2) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "CustomizedSession", this.i + " init SpdyAgent failed.", e2);
            }
        }
    }

    public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        this.g = "CONNECTED";
        if (com.uploader.implement.a.a(4)) {
            com.uploader.implement.a.a(4, "CustomizedSession", this.i + " CustomizedSession spdySessionConnectCB,session:" + spdySession.hashCode());
        }
        if (this.f != null) {
            this.f.a();
        }
        e();
    }

    public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i2, int i3, int i4, int i5, byte[] bArr) {
        if (this.f != null) {
            this.f.a(bArr, i5);
        }
    }

    public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i2, int i3) {
        if (com.uploader.implement.a.a(4)) {
            com.uploader.implement.a.a(4, "CustomizedSession", this.i + " CustomizedSession spdyCustomControlFrameFailCallback, session:" + spdySession.hashCode() + ", id:" + i2 + ", error:" + i3);
        }
    }

    public void spdySessionFailedError(SpdySession spdySession, int i2, Object obj) {
        if (spdySession != null) {
            spdySession.cleanUp();
        }
        this.g = "CONNECTFAILED";
        if (com.uploader.implement.a.a(4)) {
            com.uploader.implement.a.a(4, "CustomizedSession", this.i + " CustomizedSession spdySessionFailedError,session:" + spdySession + ", error:" + i2);
        }
        a(i2);
    }

    public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i2) {
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
                spdySession.clearAllStreamCb();
            } catch (Throwable unused) {
            }
        }
        this.g = "DISCONNECTED";
        if (com.uploader.implement.a.a(2)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.i);
            sb.append(" CustomizedSession spdySessionCloseCallback,session:");
            sb.append(spdySession != null ? Integer.valueOf(spdySession.hashCode()) : "");
            sb.append(", error:");
            sb.append(i2);
            com.uploader.implement.a.a(2, "CustomizedSession", sb.toString());
        }
        a(i2);
    }

    public byte[] getSSLMeta(SpdySession spdySession) {
        try {
            return this.a.b.getSslTicket(this.d, "ARUP_SSL_TICKET_KEY");
        } catch (Exception e2) {
            if (!com.uploader.implement.a.a(16)) {
                return null;
            }
            com.uploader.implement.a.a(16, "CustomizedSession", "CustomizedSession call config.getSslTicket error.", e2);
            return null;
        }
    }

    public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        try {
            return this.a.b.putSslTicket(this.d, "ARUP_SSL_TICKET_KEY", bArr);
        } catch (Exception e2) {
            if (!com.uploader.implement.a.a(16)) {
                return -1;
            }
            com.uploader.implement.a.a(16, "CustomizedSession", "CustomizedSession call config.putSslTicket error.", e2);
            return -1;
        }
    }

    public void spdySessionOnWritable(SpdySession spdySession, Object obj, int i2) {
        if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "CustomizedSession", this.i + " CustomizedSession spdySessionOnWritable session:" + spdySession.hashCode() + ",size:" + i2);
        }
        e();
    }

    public boolean c() {
        String str = this.g;
        return !"CONNECTED".equals(str) && !"CONNECTING".equals(str);
    }

    private void a(int i2) {
        synchronized (this.h) {
            this.h.clear();
        }
        if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "CustomizedSession", this.i + " CustomizedSession onClose, error:" + i2);
        }
        if (this.f != null) {
            this.f.a(i2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
        com.uploader.implement.e.b.a(new com.uploader.implement.b.a.c.AnonymousClass2(r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        if (r1 == null) goto L_?;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void e() {
        /*
            r3 = this;
            java.util.List<com.uploader.implement.b.a.c$b> r0 = r3.h
            monitor-enter(r0)
            java.util.List<com.uploader.implement.b.a.c$b> r1 = r3.h     // Catch:{ all -> 0x0022 }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x0022 }
            return
        L_0x000d:
            java.util.List<com.uploader.implement.b.a.c$b> r1 = r3.h     // Catch:{ all -> 0x0022 }
            r2 = 0
            java.lang.Object r1 = r1.remove(r2)     // Catch:{ all -> 0x0022 }
            com.uploader.implement.b.a.c$b r1 = (com.uploader.implement.b.a.c.b) r1     // Catch:{ all -> 0x0022 }
            monitor-exit(r0)     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x0021
            com.uploader.implement.b.a.c$2 r0 = new com.uploader.implement.b.a.c$2
            r0.<init>(r1)
            com.uploader.implement.e.b.a(r0)
        L_0x0021:
            return
        L_0x0022:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0022 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.b.a.c.e():void");
    }

    private void a(b bVar) {
        synchronized (this.h) {
            this.h.add(bVar);
            if (com.uploader.implement.a.a(8)) {
                StringBuilder sb = new StringBuilder(64);
                sb.append(this.i);
                sb.append(" [addWaitingData] ");
                sb.append(bVar);
                sb.append(", mSession:");
                sb.append(this.c != null ? Integer.valueOf(this.c.hashCode()) : "");
                com.uploader.implement.a.a(8, "CustomizedSession", sb.toString());
            }
        }
    }
}
