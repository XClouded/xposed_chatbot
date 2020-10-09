package com.huawei.updatesdk.support.b;

import java.io.Serializable;
import java.util.Comparator;

public class a implements Serializable, Comparator<a> {
    private String a = null;
    private long b = 0;
    private long c = 0;
    private C0018a d = C0018a.SYSTEM_STORAGE;

    /* renamed from: com.huawei.updatesdk.support.b.a$a  reason: collision with other inner class name */
    public enum C0018a {
        SYSTEM_STORAGE,
        INNER_SDCARD,
        EXTERNAL_SDCARD,
        UNKNOWN_TYPE
    }

    /* renamed from: a */
    public int compare(a aVar, a aVar2) {
        if (aVar.b > aVar2.b) {
            return 1;
        }
        return aVar.b < aVar2.b ? -1 : 0;
    }

    public String a() {
        return this.a;
    }

    public void a(long j) {
        this.c = j;
    }

    public void a(C0018a aVar) {
        this.d = aVar;
    }

    public void a(String str) {
        this.a = str;
    }

    public long b() {
        return this.b;
    }

    public void b(long j) {
        this.b = j;
    }

    public String toString() {
        return "StorageInfo[ storagePath = " + this.a + ", totalSpace = " + this.c + ", freeSpace = " + this.b + ", storageType = " + this.d;
    }
}
