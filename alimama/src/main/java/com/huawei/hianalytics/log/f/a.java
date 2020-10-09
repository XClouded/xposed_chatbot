package com.huawei.hianalytics.log.f;

import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.e.f;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public final class a {

    /* renamed from: com.huawei.hianalytics.log.f.a$a  reason: collision with other inner class name */
    public static class C0007a implements Serializable, Comparator<File> {
        /* renamed from: a */
        public int compare(File file, File file2) {
            long lastModified = file.lastModified() - file2.lastModified();
            if (lastModified > 0) {
                return 1;
            }
            return lastModified < 0 ? -1 : 0;
        }
    }

    public static File a(String str, String str2, int i) {
        C0007a aVar;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            b.d("AppLogManager", "createLogFile Exc, not have file path or name");
            return null;
        }
        File file = new File(str, str2);
        try {
            if (file.createNewFile()) {
                b.b("HiAnalytics/logServer", "log file createNewFile");
            }
        } catch (IOException unused) {
            b.d("AppLogManager", "createNewFile Exception,log File creation failure!");
        }
        File[] a = a(str);
        int b = f.b(a);
        int i2 = i;
        boolean z = false;
        for (File name : a) {
            if (name.getName().equals("eventinfo.log")) {
                i2++;
                z = true;
            }
        }
        if (b > i2) {
            if (!z) {
                aVar = new C0007a();
            } else {
                a = a(a);
                aVar = new C0007a();
            }
            Arrays.sort(a, aVar);
            a(a, i);
        }
        return file;
    }

    public static void a(String str, int i) {
        String str2;
        String str3;
        String str4;
        String str5;
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                str4 = "AppLogManager";
                str5 = "Failed to create file";
            } else if (listFiles.length > i) {
                com.huawei.hianalytics.log.e.a.a(file);
                str4 = "AppLogManager";
                str5 = "zips number anomaly ,Delete the file ";
            } else {
                Arrays.sort(listFiles, new C0007a());
                long j = 0;
                for (File file2 : listFiles) {
                    if (((double) file2.length()) <= 1887436.8d || !file2.delete()) {
                        j += file2.length();
                        if (((double) j) >= 1887436.8d) {
                            if (f.a(listFiles)) {
                                str2 = "HiAnalytics/logServer";
                                str3 = "delFullFile() true";
                            } else {
                                if (listFiles[0].delete()) {
                                    str2 = "HiAnalytics/logServer";
                                    str3 = "delFullFile() Crash file deletion success";
                                }
                                a(str, i);
                            }
                            b.b(str2, str3);
                            a(str, i);
                        }
                    } else {
                        b.c("HiAnalytics/logServer", "Delete a file with a length greater than 1.8M ");
                        j = 0;
                    }
                }
                return;
            }
            b.c(str4, str5);
        }
    }

    public static boolean a(File file) {
        return file.length() <= ((long) 204800);
    }

    public static boolean a(File[] fileArr, int i) {
        if (fileArr == null || fileArr.length < i) {
            b.b("AppLogManager", "files is empty or files size too much");
            return false;
        }
        int i2 = 0;
        boolean z = true;
        for (int i3 = 0; i3 < fileArr.length; i3++) {
            if (i3 < (fileArr.length - i) + i2) {
                if (fileArr[i3].getName().contains("Crash")) {
                    i2++;
                } else if (!fileArr[i3].delete()) {
                    b.c("AppLogManager", "delete failed:");
                    z = false;
                } else {
                    b.c("AppLogManager", "delete success:");
                }
            }
            if (i2 >= 5) {
                if (!fileArr[0].delete()) {
                    b.c("AppLogManager", "delete failed:");
                    z = false;
                } else {
                    b.c("AppLogManager", "delete success:");
                }
            }
        }
        return z;
    }

    public static File[] a(String str) {
        return new File(str).listFiles();
    }

    public static File[] a(File[] fileArr) {
        ArrayList arrayList = new ArrayList();
        for (File file : fileArr) {
            if (!"eventinfo.log".equals(file.getName())) {
                arrayList.add(file);
            }
        }
        return (File[]) arrayList.toArray(new File[arrayList.size()]);
    }
}
