package com.xiaomi.push;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ci {
    private File a;

    /* renamed from: a  reason: collision with other field name */
    private StringBuilder f179a;

    public void a() {
        try {
            FileWriter fileWriter = new FileWriter(this.a, true);
            fileWriter.write(this.f179a.toString());
            fileWriter.flush();
            fileWriter.close();
            this.f179a.delete(0, this.f179a.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void a(ch chVar) {
        StringBuilder sb = this.f179a;
        sb.append(chVar.a + "\t");
        StringBuilder sb2 = this.f179a;
        sb2.append(chVar.f178a + "\t" + chVar.b);
        this.f179a.append("\r\n");
    }
}
