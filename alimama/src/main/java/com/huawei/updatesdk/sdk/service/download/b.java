package com.huawei.updatesdk.sdk.service.download;

import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.taobao.weex.el.parse.Operators;

public abstract class b {

    public static class a {
        private boolean a;
        private long b;
        private long c;
        private String d;

        public void a(long j) {
            this.b = j;
        }

        public void a(String str) {
            this.d = str;
        }

        public void a(boolean z) {
            this.a = z;
        }

        public boolean a() {
            return this.a;
        }

        public long b() {
            return this.b;
        }

        public String c() {
            return this.d;
        }

        public String toString() {
            return "DiskInfo [isEnough=" + this.a + ", internalStorageSpace=" + this.b + ", externalStorageSpace=" + this.c + ", availableStoragePath=" + this.d + Operators.ARRAY_END_STR;
        }
    }

    public abstract a a(DownloadTask downloadTask);

    public void a(DownloadTask downloadTask, a aVar) {
    }

    public void a(DownloadTask downloadTask, String str) {
    }
}
