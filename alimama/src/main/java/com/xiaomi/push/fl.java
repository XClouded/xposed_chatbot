package com.xiaomi.push;

class fl extends Thread {
    final /* synthetic */ fk a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    fl(fk fkVar, String str) {
        super(str);
        this.a = fkVar;
    }

    public void run() {
        try {
            this.a.a.a();
        } catch (Exception e) {
            this.a.c(9, e);
        }
    }
}
