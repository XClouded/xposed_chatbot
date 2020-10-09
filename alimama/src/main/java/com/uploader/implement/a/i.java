package com.uploader.implement.a;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.analytics.core.Constants;
import com.alibaba.android.update.UpdateUtils;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import com.uploader.export.ITaskListener;
import com.uploader.export.ITaskResult;
import com.uploader.export.IUploaderTask;
import com.uploader.implement.a.c.a;
import com.uploader.implement.b.a.f;
import com.uploader.implement.b.a.g;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.android.agoo.message.MessageService;
import org.json.JSONException;

/* compiled from: UploaderAction */
public class i extends a {
    private ArrayList<Pair<Integer, Integer>> d = new ArrayList<>();
    private long e;
    private int f;
    private int g;
    private e h;
    private String i;
    private c j;
    private volatile com.uploader.implement.a.a.b k;
    private final IUploaderTask l;
    private final ITaskListener m;
    private final Handler n;
    private final int o;
    private final com.uploader.implement.c p;

    /* compiled from: UploaderAction */
    static final class a implements Handler.Callback {
        static final int a = a.class.hashCode();
        private final WeakReference<com.uploader.implement.d.b> b;
        private final WeakReference<a> c;

        a(a aVar, com.uploader.implement.d.b bVar) {
            this.c = new WeakReference<>(aVar);
            this.b = new WeakReference<>(bVar);
        }

        public boolean handleMessage(Message message) {
            com.uploader.implement.d.b bVar;
            a aVar;
            if (message.what != a || (bVar = (com.uploader.implement.d.b) this.b.get()) == null || (aVar = (a) this.c.get()) == null) {
                return false;
            }
            aVar.a(bVar, (com.uploader.implement.c.a) message.obj);
            return true;
        }
    }

    /* compiled from: UploaderAction */
    private static final class c implements Runnable {
        final Handler.Callback a;
        private final WeakReference<i> b;
        private final WeakReference<Looper> c = new WeakReference<>(Looper.myLooper());

        c(i iVar, Handler.Callback callback) {
            this.b = new WeakReference<>(iVar);
            this.a = callback;
        }

        public void run() {
            i iVar = (i) this.b.get();
            Looper looper = (Looper) this.c.get();
            if (looper != null && iVar != null) {
                new Handler(looper, this.a).obtainMessage(a.a, iVar.e()).sendToTarget();
            }
        }
    }

    public i(com.uploader.implement.c cVar, IUploaderTask iUploaderTask, int i2, ITaskListener iTaskListener, Handler handler) {
        super(cVar.c);
        this.p = cVar;
        this.l = iUploaderTask;
        this.m = iTaskListener;
        this.n = handler;
        this.o = i2;
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.h = null;
        this.d.clear();
    }

    /* access modifiers changed from: package-private */
    public boolean b() {
        Pair<String, Long> a2 = this.p.a.a();
        if (a2 != null && this.p.a.f() + (System.currentTimeMillis() / 1000) < ((Long) a2.second).longValue()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a e() {
        long currentTimeMillis = System.currentTimeMillis();
        Pair<com.uploader.implement.c.a, com.uploader.implement.a.a.b> a2 = com.uploader.implement.a.c.b.a(this.l);
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (a2.second != null) {
            ((com.uploader.implement.a.a.b) a2.second).j = currentTimeMillis2;
            this.k = (com.uploader.implement.a.a.b) a2.second;
        }
        if (com.uploader.implement.a.a(8)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.a);
            sb.append(" createFileDescription, elapsed:");
            sb.append(currentTimeMillis2);
            sb.append(" error:");
            sb.append(a2.first == null ? "" : ((com.uploader.implement.c.a) a2.first).toString());
            com.uploader.implement.a.a(8, "UploaderAction", sb.toString());
        }
        return (com.uploader.implement.c.a) a2.first;
    }

    /* access modifiers changed from: package-private */
    public boolean a(com.uploader.implement.d.b bVar) {
        boolean z = this.k == null;
        if (z) {
            com.uploader.implement.e.b.a(new c(this, new a(this, bVar)));
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a a(com.uploader.implement.d.b bVar, @Nullable e eVar, boolean z) {
        if (c() == 2) {
            return c(bVar, eVar, z);
        }
        return b(bVar, eVar, z);
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a b(com.uploader.implement.d.b bVar, @Nullable e eVar, boolean z) {
        try {
            com.uploader.implement.a.a.a aVar = new com.uploader.implement.a.a.a(this.p);
            if (eVar == null) {
                bVar.a((e) aVar);
            } else {
                bVar.a(eVar, aVar, z);
            }
            g c2 = aVar.a();
            this.j = new c(true, this.j);
            this.j.d = this.k.f;
            this.j.p = this.k.e;
            this.j.e = c2.a;
            this.j.f = c2.b;
            this.j.k = this.k.g;
            this.j.s = this.k.j;
            if (!com.uploader.implement.a.a(8)) {
                return null;
            }
            com.uploader.implement.a.a(8, "UploaderAction", this.a + " beginDeclare statistics create:" + this.j.hashCode());
            return null;
        } catch (JSONException e2) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionBegin", e2);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "1", e2.toString(), false);
        } catch (Exception e3) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionBegin", e3);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "5", e3.toString(), false);
        }
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a c(com.uploader.implement.d.b bVar, @Nullable e eVar, boolean z) {
        long j2;
        long j3;
        com.uploader.implement.d.b bVar2 = bVar;
        e eVar2 = eVar;
        long j4 = this.e;
        long j5 = this.k.g - this.e;
        if (j5 < 0) {
            j3 = this.k.g;
            j2 = 0;
        } else {
            j3 = j4;
            j2 = j5;
        }
        try {
            com.uploader.implement.a.a.c cVar = new com.uploader.implement.a.a.c(this.p, this.k, j3 == 0 ? "put" : UpdateUtils.SUFFIX_PATCH, j3, j2, true);
            if (eVar2 == null) {
                bVar2.a((e) cVar);
            } else {
                bVar2.a(eVar2, cVar, z);
            }
            f c2 = cVar.a();
            this.j = new c(false, this.j);
            this.j.d = this.k.f;
            this.j.p = this.k.e;
            this.j.q = (String) this.p.a.a().first;
            this.j.e = c2.a;
            this.j.f = c2.b;
            this.j.k = this.k.g;
            this.j.t = c2.f ? 1 : 0;
            if (!com.uploader.implement.a.a(8)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.a);
            sb.append(" beginFile, request:");
            sb.append(eVar2 == null ? "" : Integer.valueOf(eVar.hashCode()));
            sb.append(" newRequest:");
            sb.append(Integer.valueOf(cVar.hashCode()));
            sb.append(" statistics:");
            sb.append(this.j.hashCode());
            com.uploader.implement.a.a(8, "UploaderAction", sb.toString());
            return null;
        } catch (UnsupportedEncodingException e2) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionStartFile", e2);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "1", e2.toString(), false);
        } catch (Exception e3) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionStartFile", e3);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "5", e3.toString(), false);
        }
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a a(com.uploader.implement.d.b bVar, e eVar, com.uploader.implement.c.a aVar) {
        if (this.j != null) {
            this.j.m = System.currentTimeMillis();
        }
        if (com.uploader.implement.a.a(8)) {
            com.uploader.implement.a.a(8, "UploaderAction", this.a + " onActionRetry, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        if (c() == 2) {
            return d(bVar, eVar, aVar);
        }
        return c(bVar, eVar, aVar);
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a c(com.uploader.implement.d.b bVar, e eVar, com.uploader.implement.c.a aVar) {
        if (this.f >= 4) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderAction", this.a + " retryDeclare, retry failed, request:" + eVar.hashCode() + " error:" + aVar + " declareRetryCounter:" + this.f);
            }
            return aVar;
        }
        if (MessageService.MSG_DB_COMPLETE.equalsIgnoreCase(aVar.code) || "400".equalsIgnoreCase(aVar.code)) {
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " onActionRetry, try to connect next, request:" + eVar.hashCode());
            }
            this.p.a.c();
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " ConnectionStrategy, after nextDeclareTarget:" + this.p.a.toString());
            }
        }
        com.uploader.implement.c.a b2 = b(bVar, eVar, false);
        if (b2 == null) {
            this.f++;
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderAction", this.a + " onActionRetry, retry, request:" + eVar.hashCode() + " declareRetryCounter:" + this.f);
            }
            if (this.j != null) {
                this.j.r = this.f;
            }
        }
        return b2;
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a d(com.uploader.implement.d.b bVar, e eVar, com.uploader.implement.c.a aVar) {
        if (this.g >= 5) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderAction", this.a + " retryFile, retry failed, request:" + eVar.hashCode() + " error:" + aVar + " fileRetryCounter:" + this.g);
            }
            return aVar;
        }
        if (MessageService.MSG_DB_COMPLETE.equalsIgnoreCase(aVar.code)) {
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " retryFile, try to connect next, request:" + eVar.hashCode());
            }
            this.p.a.e();
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " ConnectionStrategy, after nextUploadTarget:" + this.p.a.toString());
            }
        }
        com.uploader.implement.c.a c2 = c(bVar, eVar, false);
        if (c2 == null) {
            this.g++;
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "UploaderAction", this.a + " retryFile, request:" + eVar.hashCode() + " fileRetryCounter:" + this.g);
            }
            if (this.j != null) {
                this.j.r = this.g;
            }
        }
        return c2;
    }

    /* access modifiers changed from: package-private */
    public com.uploader.implement.c.a a(com.uploader.implement.d.b bVar, e eVar, Pair<Integer, Integer> pair) {
        Pair<Integer, Integer> pair2 = pair;
        if (this.h != null) {
            this.d.add(pair2);
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " onActionContinue, add offset, session:" + bVar.hashCode());
            }
            return null;
        }
        try {
            com.uploader.implement.a.a.c cVar = new com.uploader.implement.a.a.c(this.p, this.k, UpdateUtils.SUFFIX_PATCH, (long) ((Integer) pair2.first).intValue(), (long) ((Integer) pair2.second).intValue(), false);
            bVar.a(eVar, cVar, true);
            if (com.uploader.implement.a.a(4)) {
                com.uploader.implement.a.a(4, "UploaderAction", this.a + " onActionContinue, session:" + bVar.hashCode() + " send request:" + cVar.hashCode());
            }
            return null;
        } catch (UnsupportedEncodingException e2) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionContinue", e2);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "1", e2.toString(), false);
        } catch (Exception e3) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " onActionContinue", e3);
            }
            return new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "5", e3.toString(), false);
        }
    }

    public void c(com.uploader.implement.d.b bVar, e eVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " onConnectBegin, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        if (this.j != null) {
            this.j.n = System.currentTimeMillis();
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " onConnectBegin statistics:" + this.j.hashCode() + " connectedTimeMillisStart:" + this.j.n);
            }
        }
    }

    public void d(com.uploader.implement.d.b bVar, e eVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " onConnect, session:" + bVar.hashCode() + " request:" + eVar.hashCode());
        }
        if (this.j != null) {
            this.j.o = System.currentTimeMillis();
        }
    }

    public void a(com.uploader.implement.d.b bVar, e eVar, int i2) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " onUploading, session:" + bVar.hashCode() + " request:" + eVar.hashCode() + " fileSizeSent:" + i2 + ", sendOffset=" + this.e);
        }
        this.e = ((long) i2) + eVar.b().c;
        if (this.j != null) {
            this.j.b = this.e;
        }
    }

    public void e(com.uploader.implement.d.b bVar, e eVar) {
        long j2;
        String str;
        if (this.j == null || this.j.l != 0) {
            j2 = 0;
        } else {
            j2 = System.currentTimeMillis();
            this.j.l = j2;
        }
        this.h = eVar;
        if (com.uploader.implement.a.a(2)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.a);
            sb.append(" onSendBegin, session:");
            sb.append(bVar.hashCode());
            sb.append(" request and set current:");
            sb.append(eVar.hashCode());
            if (j2 == 0) {
                str = "";
            } else {
                str = " statistics:" + this.j.hashCode() + " costTimeMillisStart:" + j2;
            }
            sb.append(str);
            com.uploader.implement.a.a(2, "UploaderAction", sb.toString());
        }
    }

    /* access modifiers changed from: package-private */
    public Pair<Integer, Integer> a(com.uploader.implement.d.b bVar, e eVar) {
        if (com.uploader.implement.a.a(4)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.a);
            sb.append(" onActionDeliver, session:");
            sb.append(bVar.hashCode());
            sb.append(" request:");
            sb.append(eVar.hashCode());
            sb.append(" currentRequest:");
            sb.append(this.h == null ? BuildConfig.buildJavascriptFrameworkVersion : Integer.valueOf(this.h.hashCode()));
            com.uploader.implement.a.a(4, "UploaderAction", sb.toString());
        }
        if (this.j != null) {
            h b2 = eVar.b();
            this.j.b = b2.d + ((long) (b2.f == null ? 0 : b2.f.length)) + ((long) (b2.g == null ? 0 : b2.g.length));
        }
        if (this.h != eVar) {
            return null;
        }
        this.h = null;
        if (this.d.size() > 0) {
            return this.d.remove(0);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void a(int i2, Object obj) {
        b.a(this.n, i2, this.l, this.m, obj);
        if (this.j != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " onActionNotify, notifyType:" + i2 + " statistics:" + this.j.hashCode() + " costTimeMillisEnd:" + currentTimeMillis);
            }
            if (i2 != 7) {
                switch (i2) {
                    case 1:
                        this.j.g = 2;
                        this.j.m = currentTimeMillis;
                        this.j.a();
                        this.j = null;
                        return;
                    case 2:
                        com.uploader.implement.c.a aVar = (com.uploader.implement.c.a) obj;
                        this.j.g = 0;
                        this.j.h = aVar.code;
                        this.j.i = aVar.subcode;
                        this.j.j = aVar.info;
                        this.j.m = currentTimeMillis;
                        this.j.a();
                        this.j = null;
                        return;
                    default:
                        return;
                }
            } else {
                this.j.m = currentTimeMillis;
            }
        }
    }

    @NonNull
    public final IUploaderTask f() {
        return this.l;
    }

    public final int g() {
        return this.o;
    }

    /* compiled from: UploaderAction */
    private static class b implements ITaskResult {
        private Map<String, String> a;
        private String b;
        private String c;

        public b(Map<String, String> map, String str, String str2) {
            this.a = map;
            this.c = str;
            this.b = str2;
        }

        public String getBizResult() {
            return this.b;
        }

        public String getFileUrl() {
            return this.c;
        }

        public Map<String, String> getResult() {
            return this.a;
        }
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> a(com.uploader.implement.d.b bVar, e eVar, com.uploader.implement.a.b.a aVar) {
        String a2;
        if (!(this.j == null || (a2 = aVar.a("divided_length")) == null)) {
            try {
                this.j.c += (long) Integer.parseInt(a2);
            } catch (Exception e2) {
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "UploaderAction", this.a + e2.toString());
                }
            }
        }
        switch (aVar.a()) {
            case 1:
                return e(aVar);
            case 2:
                return d(aVar);
            case 3:
                return b(bVar, eVar, aVar);
            case 4:
                return c(aVar);
            case 5:
                return b(aVar);
            case 6:
                return a(aVar);
            default:
                return null;
        }
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> a(com.uploader.implement.a.b.a aVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " retrieveStatus ,response=" + aVar);
        }
        String a2 = aVar.a("x-arup-session-status");
        if (!TextUtils.isEmpty(a2)) {
            return new Pair<>((Object) null, a2);
        }
        return new Pair<>((Object) null, (Object) null);
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> b(com.uploader.implement.a.b.a aVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " onReceiveError ,response=" + aVar);
        }
        String a2 = aVar.a("x-arup-error-code");
        String a3 = aVar.a("x-arup-error-msg");
        String a4 = aVar.a("x-arup-server-timestamp");
        if (!TextUtils.isEmpty(a4)) {
            try {
                this.p.a.a(Long.parseLong(a4));
            } catch (Exception e2) {
                if (com.uploader.implement.a.a(2)) {
                    com.uploader.implement.a.a(2, "UploaderAction", this.a + " retrieveError " + e2);
                }
                a3 = a3 + Operators.SPACE_STR + e2.toString();
            }
        }
        if (a.C0027a.C0028a.a.contains(a2)) {
            return new Pair<>(new com.uploader.implement.c.a("300", a2, a3, true), (Object) null);
        }
        if ("20021".equalsIgnoreCase(a2) || "20022".equalsIgnoreCase(a2) || "20020".equalsIgnoreCase(a2)) {
            return new Pair<>(new com.uploader.implement.c.a("300", "2", a3, true), (Object) null);
        }
        return new Pair<>(new com.uploader.implement.c.a("300", a2, a3, false), (Object) null);
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> c(com.uploader.implement.a.b.a aVar) {
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "UploaderAction", this.a + " onReceiveResult ,response=" + aVar.b());
        }
        if (!this.k.e.equals(aVar.a("x-arup-file-id"))) {
            return new Pair<>(new com.uploader.implement.c.a("300", "1", "fileId!=", true), (Object) null);
        }
        b bVar = new b(aVar.b(), aVar.a("x-arup-file-url"), aVar.a("x-arup-biz-ret"));
        if (this.j != null) {
            this.j.g = 1;
            this.j.m = System.currentTimeMillis();
            this.i += ", File" + this.j.a();
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " retrieveResult, statistics:" + this.j.hashCode() + " costTimeMillisEnd:" + this.j.m);
            }
        }
        Map<String, String> result = bVar.getResult();
        if (result != null) {
            result.put("", this.i);
        }
        return new Pair<>((Object) null, bVar);
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> d(com.uploader.implement.a.b.a aVar) {
        int i2;
        String a2 = aVar.a("x-arup-process");
        if (com.uploader.implement.a.a(4)) {
            com.uploader.implement.a.a(4, "UploaderAction", this.a + " progress :" + a2);
        }
        try {
            i2 = Integer.parseInt(a2);
        } catch (Exception e2) {
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + "", e2);
            }
            i2 = 0;
        }
        return new Pair<>((Object) null, Integer.valueOf(i2));
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> e(com.uploader.implement.a.b.a aVar) {
        try {
            Object[] objArr = aVar.c;
            this.p.a.a((String) objArr[0], ((Long) objArr[1]).longValue(), (List) objArr[2], (List) objArr[3]);
            if (com.uploader.implement.a.a(8)) {
                com.uploader.implement.a.a(8, "UploaderAction", this.a + " ConnectionStrategy update:" + this.p.a.toString());
            }
            if (this.j != null) {
                this.j.g = 1;
                this.j.q = (String) this.p.a.a().first;
                this.j.m = System.currentTimeMillis();
                this.i = "Declare" + this.j.a();
                if (com.uploader.implement.a.a(8)) {
                    com.uploader.implement.a.a(8, "UploaderAction", this.a + " retrieveDeclare, statistics:" + this.j.hashCode() + " costTimeMillisEnd:" + this.j.m);
                }
            }
            return new Pair<>((Object) null, (Object) null);
        } catch (Exception e2) {
            if (com.uploader.implement.a.a(4)) {
                com.uploader.implement.a.a(4, "UploaderAction", e2.toString());
            }
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, "8", e2.toString(), true), (Object) null);
        }
    }

    /* access modifiers changed from: package-private */
    public Pair<com.uploader.implement.c.a, ? extends Object> b(com.uploader.implement.d.b bVar, e eVar, com.uploader.implement.a.b.a aVar) {
        String a2 = aVar.a("x-arup-offset");
        if (TextUtils.isEmpty(a2)) {
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, Constants.LogTransferLevel.L7, "onReceiveOffset:1", true), (Object) null);
        }
        int indexOf = a2.indexOf(SymbolExpUtil.SYMBOL_EQUAL);
        if (indexOf == -1) {
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, Constants.LogTransferLevel.L7, "onReceiveOffset:2", true), (Object) null);
        }
        if (!this.k.e.equals(a2.substring(0, indexOf))) {
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, Constants.LogTransferLevel.L7, "onReceiveOffset:3", true), (Object) null);
        }
        int indexOf2 = a2.indexOf(",");
        int i2 = indexOf + 1;
        if (indexOf2 <= i2 || indexOf2 >= a2.length()) {
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, Constants.LogTransferLevel.L7, "onReceiveOffset:4", true), (Object) null);
        }
        try {
            return new Pair<>((Object) null, new Pair(Integer.valueOf(Integer.parseInt(a2.substring(i2, indexOf2))), Integer.valueOf(Integer.parseInt(a2.substring(indexOf2 + 1, a2.length())))));
        } catch (Exception e2) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "UploaderAction", this.a + " parse offset error.", e2);
            }
            return new Pair<>(new com.uploader.implement.c.a(AlipayAuthConstant.LoginResult.SUCCESS, Constants.LogTransferLevel.L7, e2.toString(), true), (Object) null);
        }
    }
}
