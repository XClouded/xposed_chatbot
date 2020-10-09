package com.uploader.implement.a.a;

import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.alibaba.android.update.UpdateUtils;
import com.taobao.weex.el.parse.Operators;
import com.uploader.implement.a;
import com.uploader.implement.a.c.b;
import com.uploader.implement.a.e;
import com.uploader.implement.a.h;
import com.uploader.implement.b.a.f;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.commons.cli.HelpFormatter;
import org.json.JSONObject;

/* compiled from: FileUploadActionRequest */
public class c implements e {
    private boolean a;
    private b b;
    private f c;
    private long d;
    private final Pair<Boolean, Pair<String, Integer>> e;
    private final int f = hashCode();
    private final h g;
    private final String h;
    private final com.uploader.implement.c i;

    public c(com.uploader.implement.c cVar, b bVar, String str, long j, long j2, boolean z) throws Exception {
        com.uploader.implement.c cVar2 = cVar;
        b bVar2 = bVar;
        this.i = cVar2;
        this.b = bVar2;
        this.a = z;
        this.e = cVar2.a.d();
        if (this.e != null) {
            this.h = (String) cVar2.a.a().first;
            this.d = cVar2.a.f();
            this.g = new h(bVar2.b, bVar2.k, j, j2, (Map<String, String>) null, a(j, j2, str).getBytes("UTF-8"), j < bVar2.g ? "\r\n\r\n".getBytes() : null, bVar2.l);
            return;
        }
        throw new RuntimeException("currentUploadTarget is null");
    }

    /* renamed from: c */
    public f a() {
        if (this.c != null) {
            return this.c;
        }
        f fVar = new f((String) ((Pair) this.e.second).first, ((Integer) ((Pair) this.e.second).second).intValue(), true, ((Boolean) this.e.first).booleanValue());
        this.c = fVar;
        return fVar;
    }

    public h b() {
        return this.g;
    }

    @NonNull
    public Pair<com.uploader.implement.a.f, Integer> a(Map<String, String> map, byte[] bArr, int i2, int i3) {
        int i4;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, i2, i3);
        loop0:
        while (true) {
            int i5 = 0;
            while (true) {
                try {
                    int read = byteArrayInputStream.read();
                    if (read == -1) {
                        i4 = -1;
                        break loop0;
                    }
                    if (read == 13) {
                        if (i5 == 0 || i5 == 2) {
                            i5++;
                        }
                    } else if (read == 10 && (i5 == 1 || i5 == 3)) {
                        i5++;
                        if (i5 == 4) {
                            i4 = i3 - byteArrayInputStream.available();
                            break loop0;
                        }
                    }
                    if (i5 != 0) {
                    }
                } catch (Exception e2) {
                    if (a.a(16)) {
                        a.a(16, "FileUploadActionRequest", this.f + " divide, exception", e2);
                    }
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e3) {
                        if (a.a(8)) {
                            a.a(8, "FileUploadActionRequest", this.f + " divide", e3);
                        }
                    }
                    i4 = -1;
                } catch (Throwable th) {
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e4) {
                        if (a.a(8)) {
                            a.a(8, "FileUploadActionRequest", this.f + " divide", e4);
                        }
                    }
                    throw th;
                }
            }
        }
        try {
            byteArrayInputStream.close();
        } catch (IOException e5) {
            if (a.a(8)) {
                a.a(8, "FileUploadActionRequest", this.f + " divide", e5);
            }
        }
        if (i4 <= -1) {
            return new Pair<>((Object) null, 0);
        }
        com.uploader.implement.a.f b2 = b(map, bArr, i2, i4);
        if (b2 == null) {
            i4 = -1;
        }
        return new Pair<>(b2, Integer.valueOf(i4));
    }

    private com.uploader.implement.a.f b(Map<String, String> map, byte[] bArr, int i2, int i3) {
        com.uploader.implement.a.b.a aVar;
        if (map == null) {
            map = new HashMap<>();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bArr, i2, i3)));
        map.put("divided_length", Integer.toString(i3));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    try {
                        break;
                    } catch (IOException unused) {
                    }
                } else if (!TextUtils.isEmpty(readLine)) {
                    int length = readLine.length();
                    int indexOf = readLine.indexOf(":");
                    if (indexOf > 1) {
                        String substring = readLine.substring(0, indexOf);
                        if (indexOf < length) {
                            map.put(substring.trim(), b.a(readLine.substring(indexOf + 1, length)));
                        }
                    } else {
                        int indexOf2 = readLine.indexOf(Operators.SPACE_STR);
                        if (indexOf2 > 1) {
                            String substring2 = readLine.substring(0, indexOf2);
                            if (indexOf2 < length) {
                                String substring3 = readLine.substring(indexOf2 + 1, length);
                                map.put("response_code", substring2);
                                map.put("response_msg", substring3);
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                if (a.a(16)) {
                    a.a(16, "FileUploadActionRequest", this.f + " decode response header failed", e2);
                }
                try {
                    bufferedReader.close();
                } catch (IOException unused2) {
                }
                return null;
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (IOException unused3) {
                }
                throw th;
            }
        }
        bufferedReader.close();
        if (map.get("x-arup-process") != null) {
            aVar = new com.uploader.implement.a.b.a(2, map);
        } else if (map.get("x-arup-offset") != null) {
            aVar = new com.uploader.implement.a.b.a(3, map);
        } else if (map.get("x-arup-file-status") != null) {
            aVar = new com.uploader.implement.a.b.a(4, map);
        } else if (map.get("x-arup-error-code") != null) {
            aVar = new com.uploader.implement.a.b.a(5, map);
        } else if (map.get("x-arup-session-status") == null) {
            return null;
        } else {
            aVar = new com.uploader.implement.a.b.a(6, map);
        }
        map.put("divided_length", Integer.toString(i3));
        if (a.a(4)) {
            a.a(4, "FileUploadActionRequest", this.f + " decode actionResponse header:" + map.toString());
        }
        return aVar;
    }

    private final String a(long j, long j2, String str) throws Exception {
        StringBuilder sb;
        long j3 = j;
        String str2 = str;
        int i2 = 0;
        if (this.a) {
            String str3 = this.i.b.getCurrentElement().appKey;
            String utdid = this.i.b.getUtdid();
            String appVersion = this.i.b.getAppVersion();
            String valueOf = String.valueOf(this.d + (System.currentTimeMillis() / 1000));
            HashMap hashMap = new HashMap();
            hashMap.put("host", "arup.alibaba.com");
            hashMap.put("content-type", "application/offset+octet-stream");
            hashMap.put("x-arup-version", "2.1");
            hashMap.put("x-arup-device-id", utdid);
            hashMap.put("x-arup-appkey", str3);
            hashMap.put("x-arup-appversion", appVersion);
            hashMap.put("x-arup-file-count", Integer.toString(1));
            String userId = this.i.b.getUserId();
            if (!TextUtils.isEmpty(userId)) {
                hashMap.put("x-arup-userinfo", userId);
            }
            hashMap.put("x-arup-timestamp", valueOf);
            if (UpdateUtils.SUFFIX_PATCH.equals(str2)) {
                StringBuilder sb2 = new StringBuilder(36);
                sb2.append(this.b.e);
                sb2.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb2.append(j3);
                hashMap.put("x-arup-req-offset", sb2.toString());
                sb2.setLength(0);
                sb2.append(this.b.e);
                sb2.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb2.append(String.valueOf(this.b.g));
                hashMap.put("x-arup-req-offset-file-length", sb2.toString());
            }
            StringBuilder sb3 = new StringBuilder(128);
            sb3.append(this.b.f);
            sb3.append("&");
            sb3.append(this.h);
            sb3.append("&");
            sb3.append(str3);
            sb3.append("&");
            sb3.append(appVersion);
            sb3.append("&");
            sb3.append(utdid);
            sb3.append("&");
            sb3.append(valueOf);
            String signature = this.i.b.signature(sb3.toString());
            if (TextUtils.isEmpty(signature)) {
                if (a.a(16)) {
                    a.a(16, "FileUploadActionRequest", this.f + " compute upload sign failed.");
                }
                throw new Exception("compute api sign failed.");
            }
            hashMap.put("x-arup-sign", signature);
            sb = new StringBuilder(128);
            sb.append(str2);
            sb.append(Operators.SPACE_STR);
            sb.append("/");
            sb.append("f");
            sb.append("/");
            sb.append(this.b.f);
            sb.append("/");
            sb.append(this.h);
            sb.append(Operators.SPACE_STR);
            sb.append("HTTP/1.1");
            sb.append("\r\n");
            for (Map.Entry entry : hashMap.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append(":");
                sb.append(b.b((String) entry.getValue()));
                sb.append("\r\n");
            }
            sb.append("\r\n");
        } else {
            sb = null;
        }
        if (j3 < this.b.g) {
            if (sb == null) {
                sb = new StringBuilder(128);
            }
            sb.append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
            sb.append(this.b.e);
            sb.append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
            sb.append("\r\n");
            if (this.b.i != null && this.b.i.size() > 0) {
                JSONObject jSONObject = new JSONObject();
                for (Map.Entry next : this.b.i.entrySet()) {
                    String str4 = (String) next.getKey();
                    if (!TextUtils.isEmpty(str4)) {
                        jSONObject.put(str4, next.getValue());
                    }
                }
                sb.append("x-arup-meta");
                sb.append(":");
                sb.append(b.b(jSONObject.toString()));
                sb.append("\r\n");
            }
            sb.append("x-arup-file-md5");
            sb.append(":");
            sb.append(b.b(this.b.h));
            sb.append("\r\n");
            sb.append("x-arup-file-name");
            sb.append(":");
            sb.append(b.b(this.b.d));
            sb.append("\r\n");
            sb.append("x-arup-range");
            sb.append(":");
            sb.append(j3);
            sb.append(",");
            sb.append(j2);
            sb.append("\r\n");
            sb.append("x-arup-file-length");
            sb.append(":");
            sb.append(String.valueOf(this.b.g));
            sb.append("\r\n\r\n");
        }
        if (sb == null) {
            return null;
        }
        if (a.a(2)) {
            String sb4 = sb.toString();
            String str5 = "";
            while (true) {
                int indexOf = sb4.indexOf("\r\n", i2);
                if (indexOf == -1) {
                    break;
                }
                str5 = str5 + sb4.substring(i2, indexOf) + "\n";
                i2 = "\r\n".length() + indexOf;
            }
            a.a(2, "FileUploadActionRequest", this.f + " create upload header:" + str5);
        }
        return sb.toString();
    }
}
