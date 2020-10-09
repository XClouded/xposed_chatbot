package com.huawei.hms.update.a;

import java.io.File;
import java.io.IOException;

/* compiled from: UpdateDownload */
class i extends b {
    final /* synthetic */ int a;
    final /* synthetic */ String b;
    final /* synthetic */ h c;
    private long d = 0;
    private int e = this.c.c.b();

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    i(h hVar, File file, int i, int i2, String str) {
        super(file, i);
        this.c = hVar;
        this.a = i2;
        this.b = str;
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        super.write(bArr, i, i2);
        this.e += i2;
        if (this.e <= 209715200) {
            long currentTimeMillis = System.currentTimeMillis();
            if (Math.abs(currentTimeMillis - this.d) > 1000) {
                this.d = currentTimeMillis;
                a(this.e);
            }
            if (this.e == this.a) {
                a(this.e);
            }
        }
    }

    private void a(int i) {
        this.c.c.a(this.c.b(), i, this.b);
        this.c.a(2100, i, this.a);
    }
}
