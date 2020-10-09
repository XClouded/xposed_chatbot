package com.huawei.hianalytics.log.f;

import alimama.com.unweventparse.constants.EventConstants;
import android.taobao.windvane.jsbridge.api.WVFile;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.alipay.literpc.android.phone.mrpc.core.Headers;
import com.huawei.hianalytics.log.e.d;
import com.huawei.hianalytics.log.f.a.a;
import com.huawei.hianalytics.log.f.a.b;
import com.huawei.hianalytics.log.f.a.c;
import com.taobao.weex.adapter.URIAdapter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

class f implements e {
    f() {
    }

    public b a(String str, String str2) {
        String str3;
        String str4;
        b bVar = new b();
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null || listFiles.length < 1) {
            str3 = "SendManagerImpl";
            str4 = "not has send file";
        } else {
            File file = listFiles[0];
            if (file.length() > WVFile.FILE_MAX_SIZE) {
                str3 = "SendManagerImpl";
                str4 = "send file ,file length full！";
            } else {
                String a = a(file, MessageDigestAlgorithms.MD5);
                String a2 = a(file, MessageDigestAlgorithms.SHA_256);
                String valueOf = String.valueOf(file.length());
                a aVar = new a(a, valueOf, a2);
                ArrayList arrayList = new ArrayList();
                arrayList.add(aVar);
                bVar.a("0");
                bVar.b(file.getName());
                bVar.a((List<a>) arrayList);
                bVar.c(valueOf);
                bVar.d(str2);
                bVar.e(valueOf);
                bVar.f("1");
                bVar.g("0");
                bVar.h(URIAdapter.OTHERS);
                return bVar;
            }
        }
        com.huawei.hianalytics.g.b.d(str3, str4);
        return null;
    }

    public String a(File file, String str) {
        String str2;
        String str3;
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                MappedByteBuffer map = fileInputStream2.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest instance = MessageDigest.getInstance(str);
                instance.update(map);
                String bigInteger = new BigInteger(1, instance.digest()).toString(16);
                d.a(1, (Closeable) fileInputStream2);
                return bigInteger;
            } catch (IOException unused) {
                fileInputStream = fileInputStream2;
                str2 = "SendManagerImpl";
                str3 = "getMd5ByFile : file input stream Exception!";
                try {
                    com.huawei.hianalytics.g.b.d(str2, str3);
                    d.a(1, (Closeable) fileInputStream);
                    return "";
                } catch (Throwable th) {
                    th = th;
                    fileInputStream2 = fileInputStream;
                    d.a(1, (Closeable) fileInputStream2);
                    throw th;
                }
            } catch (NoSuchAlgorithmException unused2) {
                fileInputStream = fileInputStream2;
                str2 = "SendManagerImpl";
                str3 = "getMd5ByFile : No such algorithm!";
                com.huawei.hianalytics.g.b.d(str2, str3);
                d.a(1, (Closeable) fileInputStream);
                return "";
            } catch (Throwable th2) {
                th = th2;
                d.a(1, (Closeable) fileInputStream2);
                throw th;
            }
        } catch (IOException unused3) {
            str2 = "SendManagerImpl";
            str3 = "getMd5ByFile : file input stream Exception!";
            com.huawei.hianalytics.g.b.d(str2, str3);
            d.a(1, (Closeable) fileInputStream);
            return "";
        } catch (NoSuchAlgorithmException unused4) {
            str2 = "SendManagerImpl";
            str3 = "getMd5ByFile : No such algorithm!";
            com.huawei.hianalytics.g.b.d(str2, str3);
            d.a(1, (Closeable) fileInputStream);
            return "";
        }
    }

    public String a(String str, b bVar) {
        if (bVar == null || TextUtils.isEmpty(str)) {
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "commonbody is empty or fileinfo is null!");
            return "";
        }
        List<a> c = bVar.c();
        JSONArray jSONArray = new JSONArray();
        for (a next : c) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("fileMd5", next.c());
            jSONObject.put("fileSha256", next.b());
            jSONObject.put("fileSize", next.a());
            jSONArray.put(jSONObject);
        }
        Locale locale = Locale.getDefault();
        return String.format(locale, "%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s", new Object[]{str + "&" + "logType", bVar.a(), "fileName", bVar.b(), "fileHashList", jSONArray, "fileSize", bVar.d(), "encryptKey", bVar.e(), "patchSize", bVar.f(), "patchNum", bVar.g(), "patchVer", bVar.h(), URIAdapter.OTHERS, bVar.i()});
    }

    public List<c> a(JSONArray jSONArray, String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String optString = jSONObject.optString("method");
            String optString2 = jSONObject.optString("uploadUrl");
            JSONObject optJSONObject = jSONObject.optJSONObject(EventConstants.Mtop.HEADERS);
            c cVar = new c(optString2, optString, str);
            cVar.a(HttpConstant.AUTHORIZATION, optJSONObject.optString(HttpConstant.AUTHORIZATION));
            cVar.a("x-amz-content-sha256", optJSONObject.optString("x-amz-content-sha256"));
            cVar.a("x-amz-date", optJSONObject.optString("x-amz-date"));
            cVar.a(Headers.CONN_DIRECTIVE, optJSONObject.optString(Headers.CONN_DIRECTIVE));
            cVar.a(HttpConstant.HOST, optJSONObject.optString(HttpConstant.HOST));
            cVar.a("Content-MD5", optJSONObject.optString("Content-MD5"));
            cVar.a("Content-Type", optJSONObject.optString("Content-Type"));
            cVar.a("user-agent", optJSONObject.optString("user-agent"));
            arrayList.add(cVar);
        }
        return arrayList;
    }
}
