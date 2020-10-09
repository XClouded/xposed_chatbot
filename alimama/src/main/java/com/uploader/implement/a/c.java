package com.uploader.implement.a;

import android.text.TextUtils;
import com.uploader.implement.a;
import com.uploader.implement.b;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import mtopsdk.mtop.upload.domain.UploadConstants;
import org.android.agoo.common.AgooConstants;

/* compiled from: ActionStatistics */
public class c {
    private static volatile boolean u = false;
    private static volatile boolean v = false;
    private static final byte[] w = new byte[0];
    final boolean a;
    long b;
    long c;
    String d;
    String e;
    int f;
    int g;
    String h;
    String i;
    String j;
    long k;
    long l;
    long m;
    long n;
    long o;
    String p;
    String q;
    int r;
    long s;
    int t;
    private c x;

    c(boolean z, c cVar) {
        this.a = z;
        this.x = cVar;
    }

    public String a() {
        c cVar;
        long j2;
        long j3;
        long j4;
        long j5;
        HashMap hashMap;
        HashMap hashMap2 = new HashMap();
        c cVar2 = this.x;
        long j6 = 0;
        long j7 = 0;
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        int i2 = 0;
        while (cVar2 != null && !cVar2.a) {
            HashMap hashMap3 = hashMap2;
            if (cVar2.o > j6 && cVar2.n > j6) {
                j7 += cVar2.o - cVar2.n;
                i2++;
            }
            if (cVar2.m > 0 && cVar2.l > 0) {
                j8 += cVar2.m - cVar2.l;
            }
            j9 += cVar2.b;
            j10 += cVar2.c;
            cVar2 = cVar2.x;
            hashMap2 = hashMap3;
            j6 = 0;
        }
        HashMap hashMap4 = hashMap2;
        if (a.a(4)) {
            StringBuilder sb = new StringBuilder();
            sb.append(hashCode());
            sb.append(" result:");
            cVar = this;
            sb.append(cVar.g);
            sb.append(" connectCountPrevious:");
            sb.append(i2);
            sb.append(" connectTimePrevious:");
            sb.append(j7);
            sb.append(" connectedTimeCurrent:");
            sb.append(cVar.o - cVar.n);
            sb.append(" costTimePrevious:");
            sb.append(j8);
            sb.append(" costTimeCurrent:");
            sb.append(cVar.m - cVar.l);
            sb.append(" upstreamPrevious:");
            sb.append(j9);
            sb.append(" downstreamPrevious:");
            sb.append(j10);
            a.a(4, "ActionStatistics", sb.toString());
        } else {
            cVar = this;
        }
        if (cVar.o <= 0 || cVar.n <= 0) {
            j3 = 0;
            j2 = 0;
        } else {
            long j11 = cVar.o - cVar.n;
            if (j7 > 0) {
                j11 += j7;
            }
            j2 = j11 + 0;
            j3 = j11 / ((long) (i2 + 1));
        }
        if (cVar.m <= 0 || cVar.l <= 0) {
            j4 = j9;
            j5 = 0;
        } else {
            j4 = j9;
            j5 = cVar.m - cVar.l;
            if (j8 > 0) {
                j5 += j8;
            }
            j2 += j5;
        }
        if (j3 > 0) {
            hashMap = hashMap4;
            hashMap.put("connecttime", Double.valueOf((double) j3));
        } else {
            hashMap = hashMap4;
        }
        if (j5 > 0) {
            hashMap.put("costtime", Double.valueOf((double) j5));
        }
        if (cVar.k > 0) {
            hashMap.put("size", Double.valueOf((double) cVar.k));
        }
        if (cVar.s > 0) {
            hashMap.put("md5time", Double.valueOf((double) cVar.s));
        }
        if (j2 > 0) {
            hashMap.put("totaltime", Double.valueOf((double) j2));
            if (cVar.k > 0) {
                hashMap.put("speed", Double.valueOf((double) ((cVar.k * 125) / (j2 * 128))));
            }
        }
        if (cVar.c + j10 > 0) {
            hashMap.put("downstream", Double.valueOf((double) (cVar.c + j10)));
        }
        if (cVar.b + j4 > 0) {
            hashMap.put("upstream", Double.valueOf((double) (cVar.b + j4)));
        }
        HashMap hashMap5 = new HashMap();
        hashMap5.put(com.alipay.sdk.app.statistic.c.b, cVar.d);
        hashMap5.put("port", String.valueOf(cVar.f));
        hashMap5.put("ip", cVar.e);
        hashMap5.put("ret", String.valueOf(cVar.g));
        if (!TextUtils.isEmpty(cVar.h)) {
            hashMap5.put("errorcode", cVar.h);
        }
        if (!TextUtils.isEmpty(cVar.i)) {
            hashMap5.put("subcode", cVar.i);
        }
        if (!TextUtils.isEmpty(cVar.j)) {
            hashMap5.put("errormsg", cVar.j);
        }
        if (!TextUtils.isEmpty(cVar.p)) {
            hashMap5.put(UploadConstants.FILE_ID, cVar.p);
        }
        if (!TextUtils.isEmpty(cVar.q)) {
            hashMap5.put("token", cVar.q);
        }
        hashMap5.put(UploadConstants.RETRY_TIMES, String.valueOf(cVar.r));
        if (!cVar.a) {
            hashMap5.put(AgooConstants.MESSAGE_ENCRYPTED, String.valueOf(cVar.t));
        }
        if (a.a(2)) {
            a.a(2, "ActionStatistics", "commit statistic,isDeclarationStatistics=" + cVar.a + ", dimensions:" + hashMap5 + ", measures:" + hashMap + " upStreamTotal:" + (cVar.b + j4) + " downstream:" + (cVar.c + j10));
        }
        if (cVar.a) {
            if (!u) {
                synchronized (w) {
                    if (!u) {
                        b.a("ARUP", "RequestUpload", c(), b(), false);
                        u = true;
                    }
                }
            }
            b.a("ARUP", "RequestUpload", hashMap, hashMap5);
            return hashMap.toString();
        }
        if (!v) {
            synchronized (w) {
                if (!v) {
                    b.a("ARUP", "FileUpload", c(), b(), false);
                    v = true;
                }
            }
        }
        b.a("ARUP", "FileUpload", hashMap, hashMap5);
        return hashMap.toString();
    }

    private Set<String> b() {
        HashSet hashSet = new HashSet();
        hashSet.add(com.alipay.sdk.app.statistic.c.b);
        hashSet.add("token");
        hashSet.add(UploadConstants.FILE_ID);
        hashSet.add("ip");
        hashSet.add("port");
        hashSet.add("ret");
        hashSet.add("errorcode");
        hashSet.add("subcode");
        hashSet.add("errormsg");
        hashSet.add(UploadConstants.RETRY_TIMES);
        if (!this.a) {
            hashSet.add(AgooConstants.MESSAGE_ENCRYPTED);
        }
        return hashSet;
    }

    private Set<String> c() {
        HashSet hashSet = new HashSet();
        hashSet.add("connecttime");
        hashSet.add("costtime");
        hashSet.add("size");
        hashSet.add("speed");
        hashSet.add("totaltime");
        hashSet.add("md5time");
        hashSet.add("upstream");
        hashSet.add("downstream");
        return hashSet;
    }
}
