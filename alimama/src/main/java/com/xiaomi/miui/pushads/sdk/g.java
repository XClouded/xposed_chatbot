package com.xiaomi.miui.pushads.sdk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class g {
    private File a;

    /* renamed from: a  reason: collision with other field name */
    private StringBuilder f80a;

    public void a() {
        try {
            FileWriter fileWriter = new FileWriter(this.a, true);
            fileWriter.write(this.f80a.toString());
            fileWriter.flush();
            fileWriter.close();
            this.f80a.delete(0, this.f80a.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void a(String str, long j, int i) {
        StringBuilder sb = this.f80a;
        sb.append(str + "\t" + j + "\t" + i);
        this.f80a.append("\r\n");
    }
}
