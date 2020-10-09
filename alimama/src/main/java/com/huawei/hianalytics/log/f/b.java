package com.huawei.hianalytics.log.f;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.h.c;
import com.huawei.hianalytics.log.a.a;
import com.huawei.hianalytics.log.e.d;
import com.huawei.hianalytics.log.e.e;
import com.huawei.hianalytics.util.f;
import com.taobao.tao.log.TLogInitializer;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class b implements c {
    private String a(String str) {
        return "https://" + str;
    }

    private static void a(ZipInputStream zipInputStream, File file) {
        String str;
        String str2;
        FileOutputStream fileOutputStream = null;
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                d.a(0, (Closeable) null);
                return;
            }
            String c = c(nextEntry.getName());
            if (TextUtils.isEmpty(c)) {
                com.huawei.hianalytics.g.b.c("SendManagerImpl", "File name exception, stop unzip");
                d.a(0, (Closeable) null);
                return;
            }
            File file2 = new File(file.getCanonicalPath() + File.separator + c);
            if (nextEntry.isDirectory()) {
                if (!file2.mkdirs()) {
                    str = "HiAnalytics/logServer";
                    str2 = "fileUnZip() Unzip file creation failure";
                } else {
                    a(zipInputStream, file);
                    d.a(0, (Closeable) fileOutputStream);
                }
            } else if (!file2.createNewFile()) {
                str = "HiAnalytics/logServer";
                str2 = "fileUnZip() Failure to create new files";
            } else {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
                try {
                    byte[] bArr = new byte[2048];
                    int i = 0;
                    while (true) {
                        int read = zipInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        i += read;
                        if (!a(i, file2)) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                    }
                    a(zipInputStream, file);
                    fileOutputStream = fileOutputStream2;
                } catch (IOException unused) {
                    fileOutputStream = fileOutputStream2;
                    try {
                        com.huawei.hianalytics.g.b.d("SendManagerImpl", "fileUnZip() File creation failure or Stream Exception!");
                        d.a(0, (Closeable) fileOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        d.a(0, (Closeable) fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = fileOutputStream2;
                    d.a(0, (Closeable) fileOutputStream);
                    throw th;
                }
                d.a(0, (Closeable) fileOutputStream);
            }
            com.huawei.hianalytics.g.b.b(str, str2);
            d.a(0, (Closeable) fileOutputStream);
        } catch (IOException unused2) {
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "fileUnZip() File creation failure or Stream Exception!");
            d.a(0, (Closeable) fileOutputStream);
        }
    }

    private static boolean a(int i, File file) {
        if (i <= 5242880) {
            return true;
        }
        com.huawei.hianalytics.g.b.d("SendManagerImpl", "Single File being unzipped is too big.");
        if (!file.exists() || !file.delete()) {
            return false;
        }
        com.huawei.hianalytics.g.b.b("SendManagerImpl", "Delete large files successfully");
        return false;
    }

    private String b(String str, String str2, String str3) {
        return a.a(str, str2, str3);
    }

    private void b(String str) {
        String[] split = str.split("/");
        if (TLogInitializer.DEFAULT_DIR.equals(split[split.length - 2])) {
            a(str.replace(split[split.length - 1], "logzips"), str);
        } else if (new File(str).delete()) {
            com.huawei.hianalytics.g.b.c("SendManagerImpl", "doUnzip() delete srcFilePath file");
        }
    }

    private static String c(String str) {
        try {
            String canonicalPath = new File(str).getCanonicalPath();
            if (canonicalPath.startsWith(new File(".").getCanonicalPath())) {
                return canonicalPath;
            }
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "File is outside extraction target directory.");
            return "";
        } catch (IOException unused) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x0097 A[SYNTHETIC, Splitter:B:55:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:63:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:64:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            r0 = 0
            r1 = 4
            java.util.zip.ZipFile r2 = new java.util.zip.ZipFile     // Catch:{ FileNotFoundException -> 0x007b, IOException -> 0x006a, all -> 0x0067 }
            r2.<init>(r7)     // Catch:{ FileNotFoundException -> 0x007b, IOException -> 0x006a, all -> 0x0067 }
            int r3 = r2.size()     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            r4 = 10
            if (r3 <= r4) goto L_0x0025
            java.lang.String r6 = "SendManagerImpl"
            java.lang.String r7 = "The number of unzip files is too much.MaxSize: 10"
            com.huawei.hianalytics.g.b.c(r6, r7)     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            com.huawei.hianalytics.log.e.d.a((int) r1, (java.io.Closeable) r0)
            r2.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0024
        L_0x001d:
            java.lang.String r6 = "SendManagerImpl"
            java.lang.String r7 = "ZipFile. Exception when closing the closeable"
            com.huawei.hianalytics.g.b.c(r6, r7)
        L_0x0024:
            return
        L_0x0025:
            java.util.zip.ZipInputStream r3 = new java.util.zip.ZipInputStream     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            r4.<init>(r7)     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            r3.<init>(r4)     // Catch:{ FileNotFoundException -> 0x007c, IOException -> 0x006b }
            java.io.File r0 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            r0.<init>(r6)     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            boolean r6 = r0.exists()     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            if (r6 != 0) goto L_0x0047
            boolean r6 = r0.mkdirs()     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            if (r6 != 0) goto L_0x0047
            java.lang.String r6 = "HiAnalytics/logServer"
            java.lang.String r4 = "Zips directory creation failure"
            com.huawei.hianalytics.g.b.b(r6, r4)     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
        L_0x0047:
            a((java.util.zip.ZipInputStream) r3, (java.io.File) r0)     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            java.io.File r6 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            r6.<init>(r7)     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            boolean r6 = r6.delete()     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
            if (r6 == 0) goto L_0x005c
            java.lang.String r6 = "HiAnalytics/logServer"
            java.lang.String r7 = "Delete unzip file"
            com.huawei.hianalytics.g.b.b(r6, r7)     // Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x0063, all -> 0x0060 }
        L_0x005c:
            com.huawei.hianalytics.log.e.d.a((int) r1, (java.io.Closeable) r3)
            goto L_0x0077
        L_0x0060:
            r6 = move-exception
            r0 = r3
            goto L_0x0092
        L_0x0063:
            r0 = r3
            goto L_0x006b
        L_0x0065:
            r0 = r3
            goto L_0x007c
        L_0x0067:
            r6 = move-exception
            r2 = r0
            goto L_0x0092
        L_0x006a:
            r2 = r0
        L_0x006b:
            java.lang.String r6 = "SendManagerImpl"
            java.lang.String r7 = "This file is not a compressed file"
            com.huawei.hianalytics.g.b.d(r6, r7)     // Catch:{ all -> 0x0091 }
            com.huawei.hianalytics.log.e.d.a((int) r1, (java.io.Closeable) r0)
            if (r2 == 0) goto L_0x0090
        L_0x0077:
            r2.close()     // Catch:{ IOException -> 0x0089 }
            goto L_0x0090
        L_0x007b:
            r2 = r0
        L_0x007c:
            java.lang.String r6 = "SendManagerImpl"
            java.lang.String r7 = " unZip() There is no document!"
            com.huawei.hianalytics.g.b.d(r6, r7)     // Catch:{ all -> 0x0091 }
            com.huawei.hianalytics.log.e.d.a((int) r1, (java.io.Closeable) r0)
            if (r2 == 0) goto L_0x0090
            goto L_0x0077
        L_0x0089:
            java.lang.String r6 = "SendManagerImpl"
            java.lang.String r7 = "ZipFile. Exception when closing the closeable"
            com.huawei.hianalytics.g.b.c(r6, r7)
        L_0x0090:
            return
        L_0x0091:
            r6 = move-exception
        L_0x0092:
            com.huawei.hianalytics.log.e.d.a((int) r1, (java.io.Closeable) r0)
            if (r2 == 0) goto L_0x00a2
            r2.close()     // Catch:{ IOException -> 0x009b }
            goto L_0x00a2
        L_0x009b:
            java.lang.String r7 = "SendManagerImpl"
            java.lang.String r0 = "ZipFile. Exception when closing the closeable"
            com.huawei.hianalytics.g.b.c(r7, r0)
        L_0x00a2:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.log.f.b.a(java.lang.String, java.lang.String):void");
    }

    public void a(String str, String str2, Key key) {
        FileOutputStream fileOutputStream;
        byte[] a = com.huawei.hianalytics.log.e.b.a();
        Cipher a2 = com.huawei.hianalytics.log.e.b.a(1, key, a);
        if (a2 == null) {
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "get cipher is null!");
            b(str);
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(str);
            try {
                fileOutputStream = new FileOutputStream(str2);
                try {
                    fileOutputStream.write(a);
                    byte[] bArr = new byte[2048];
                    while (fileInputStream2.read(bArr) != -1) {
                        fileOutputStream.write(a2.doFinal(bArr));
                    }
                    d.a(1, (Closeable) fileInputStream2);
                } catch (FileNotFoundException unused) {
                    fileInputStream = fileInputStream2;
                    com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): There is no document!");
                    d.a(1, (Closeable) fileInputStream);
                    d.a(0, (Closeable) fileOutputStream);
                } catch (BadPaddingException unused2) {
                    fileInputStream = fileInputStream2;
                    com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): diFinal - False filling parameters!");
                    b(str);
                    d.a(1, (Closeable) fileInputStream);
                    d.a(0, (Closeable) fileOutputStream);
                } catch (IllegalBlockSizeException unused3) {
                    fileInputStream = fileInputStream2;
                    com.huawei.hianalytics.g.b.d("SendManagerImpl", "encrypt(): doFinal - The provided block is not filled with !");
                    b(str);
                    d.a(1, (Closeable) fileInputStream);
                    d.a(0, (Closeable) fileOutputStream);
                } catch (IOException unused4) {
                    fileInputStream = fileInputStream2;
                    com.huawei.hianalytics.g.b.d("SendManagerImpl", "Exception by stream read or write in the encrypt()!");
                    b(str);
                    d.a(1, (Closeable) fileInputStream);
                    d.a(0, (Closeable) fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    d.a(1, (Closeable) fileInputStream);
                    d.a(0, (Closeable) fileOutputStream);
                    throw th;
                }
            } catch (FileNotFoundException unused5) {
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): There is no document!");
                d.a(1, (Closeable) fileInputStream);
                d.a(0, (Closeable) fileOutputStream);
            } catch (BadPaddingException unused6) {
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): diFinal - False filling parameters!");
                b(str);
                d.a(1, (Closeable) fileInputStream);
                d.a(0, (Closeable) fileOutputStream);
            } catch (IllegalBlockSizeException unused7) {
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                com.huawei.hianalytics.g.b.d("SendManagerImpl", "encrypt(): doFinal - The provided block is not filled with !");
                b(str);
                d.a(1, (Closeable) fileInputStream);
                d.a(0, (Closeable) fileOutputStream);
            } catch (IOException unused8) {
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                com.huawei.hianalytics.g.b.d("SendManagerImpl", "Exception by stream read or write in the encrypt()!");
                b(str);
                d.a(1, (Closeable) fileInputStream);
                d.a(0, (Closeable) fileOutputStream);
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                d.a(1, (Closeable) fileInputStream);
                d.a(0, (Closeable) fileOutputStream);
                throw th;
            }
        } catch (FileNotFoundException unused9) {
            fileOutputStream = null;
            com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): There is no document!");
            d.a(1, (Closeable) fileInputStream);
            d.a(0, (Closeable) fileOutputStream);
        } catch (BadPaddingException unused10) {
            fileOutputStream = null;
            com.huawei.hianalytics.g.b.d("SendManagerImpl", " encrypt(): diFinal - False filling parameters!");
            b(str);
            d.a(1, (Closeable) fileInputStream);
            d.a(0, (Closeable) fileOutputStream);
        } catch (IllegalBlockSizeException unused11) {
            fileOutputStream = null;
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "encrypt(): doFinal - The provided block is not filled with !");
            b(str);
            d.a(1, (Closeable) fileInputStream);
            d.a(0, (Closeable) fileOutputStream);
        } catch (IOException unused12) {
            fileOutputStream = null;
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "Exception by stream read or write in the encrypt()!");
            b(str);
            d.a(1, (Closeable) fileInputStream);
            d.a(0, (Closeable) fileOutputStream);
        } catch (Throwable th3) {
            th = th3;
            d.a(1, (Closeable) fileInputStream);
            d.a(0, (Closeable) fileOutputStream);
            throw th;
        }
        d.a(0, (Closeable) fileOutputStream);
    }

    public boolean a(String str, String str2, Context context) {
        String str3;
        String str4;
        String str5;
        String str6;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || context == null) {
            str3 = "SendManagerImpl";
            str4 = "send log path or key is empty!";
        } else {
            String c = com.huawei.hianalytics.a.d.c();
            String b = com.huawei.hianalytics.a.d.b();
            if (TextUtils.isEmpty(b) || TextUtils.isEmpty(c)) {
                str3 = "SendManagerImpl";
                str4 = "logClintID or logClintKey is empty! Do not send file!";
            } else {
                f fVar = new f();
                String concat = "{url}/v2/getServerDomain".replace("{url}", com.huawei.hianalytics.a.d.i()).concat("?appID=").concat(b);
                String a = e.a(context);
                try {
                    JSONObject jSONObject = new JSONObject(c.a(concat, a, b(concat, a, c)).b());
                    String string = jSONObject.getString("resCode");
                    com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "logserver first request resCode :" + string);
                    if ("0".equals(string)) {
                        String optString = jSONObject.optString("serverDomain", "");
                        String optString2 = jSONObject.optString(XStateConstants.KEY_ACCESS_TOKEN, "");
                        String a2 = a(optString);
                        if (!f.a(a2, "(https://)[a-zA-Z0-9-_]+[\\.a-zA-Z0-9_-]*(\\.hicloud\\.com)(:(\\d){2,5})?(\\\\|\\/)?")) {
                            com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "url non conformity");
                            return false;
                        }
                        String concat2 = "{url}/v2/getUploadInfo".replace("{url}", a2).concat("?appID=").concat(b);
                        String a3 = fVar.a(a, fVar.a(str, str2));
                        JSONObject jSONObject2 = new JSONObject(c.a(concat2, a3, a.a(concat2, a3, optString2)).b());
                        String string2 = jSONObject2.getString("resCode");
                        com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "Request file to report URL interface serverResCode :" + string2);
                        if ("0".equals(string2)) {
                            com.huawei.hianalytics.log.e.f.a(context, jSONObject2.getString("policy"));
                            String optString3 = jSONObject2.optString("fileUniqueFlag");
                            String optString4 = jSONObject2.optString("currentTime");
                            int a4 = d.a(fVar.a(jSONObject2.getJSONArray("uploadInfoList"), str));
                            com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "upLoadLogPut response code: " + a4);
                            if (200 == a4) {
                                com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "upLoadLogPut success");
                                com.huawei.hianalytics.log.e.c.a(com.huawei.hianalytics.log.e.c.b(context), "autocheck_starttime", Long.valueOf(System.currentTimeMillis()));
                                String concat3 = "{url}/v2/notifyUploadSucc".replace("{url}", a2).concat("?appID=").concat(b);
                                String concat4 = a.concat("&fileUniqueFlag=").concat(optString3).concat("&uploadTime=").concat(optString4);
                                String string3 = new JSONObject(c.a(concat3, concat4, a.a(concat3, concat4, optString2)).b()).getString("resCode");
                                com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "upload_notify_succ: " + string3);
                                return true;
                            }
                            com.huawei.hianalytics.g.b.c("SendManagerImpl", "File upload failure");
                            return false;
                        }
                    }
                } catch (IOException unused) {
                    str6 = "SendManagerImpl";
                    str5 = "sendLog get logServerUrl Exception,The Exception is IO!";
                    com.huawei.hianalytics.g.b.d(str6, str5);
                    return false;
                } catch (JSONException unused2) {
                    str6 = "SendManagerImpl";
                    str5 = "sendLog(reauest) get logServerUrl Exception,The Exception is json!";
                    com.huawei.hianalytics.g.b.d(str6, str5);
                    return false;
                }
                return false;
            }
        }
        com.huawei.hianalytics.g.b.c(str3, str4);
        return false;
    }

    public boolean a(String str, String str2, String str3) {
        File[] a = a.a(str);
        if (a.length < 1) {
            com.huawei.hianalytics.g.b.d("SendManagerImpl", "There is no log file when creating a compressed package");
            return false;
        }
        File file = new File(str3 + str2);
        for (int i = 0; i < 2; i++) {
            if (com.huawei.hianalytics.log.e.f.a(a, file)) {
                com.huawei.hianalytics.log.e.a.a(new File(str));
                return true;
            }
            if (i == 1) {
                com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "Packaging failure!");
                if (file.exists() && file.delete()) {
                    com.huawei.hianalytics.g.b.b("HiAnalytics/logServer", "Bad zip file delete ok");
                }
            }
        }
        return false;
    }
}
