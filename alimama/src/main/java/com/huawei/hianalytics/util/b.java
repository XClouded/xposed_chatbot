package com.huawei.hianalytics.util;

import java.io.File;

public class b {
    public static void a(File file) {
        if (file != null && file.exists()) {
            if (!file.isFile()) {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    com.huawei.hianalytics.g.b.c("HianalyticsSDK", "not have file remove!");
                    return;
                }
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        a(file2);
                    } else if (!file2.delete()) {
                        com.huawei.hianalytics.g.b.d("HianalyticsSDK", "remover file fail!");
                    }
                }
            } else if (!file.delete()) {
                com.huawei.hianalytics.g.b.c("HianalyticsSDK", "remover file fail!");
            }
        }
    }
}
