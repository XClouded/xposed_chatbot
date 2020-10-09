package com.xiaomi.push;

import com.taobao.alivfsadapter.MonitorCacheEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class fi {
    private XmlPullParser a;

    fi() {
        try {
            this.a = XmlPullParserFactory.newInstance().newPullParser();
            this.a.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        } catch (XmlPullParserException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public gd a(byte[] bArr, fm fmVar) {
        this.a.setInput(new InputStreamReader(new ByteArrayInputStream(bArr)));
        this.a.next();
        int eventType = this.a.getEventType();
        String name = this.a.getName();
        if (eventType != 2) {
            return null;
        }
        if (name.equals("message")) {
            return gl.a(this.a);
        }
        if (name.equals("iq")) {
            return gl.a(this.a, fmVar);
        }
        if (name.equals("presence")) {
            return gl.a(this.a);
        }
        if (this.a.getName().equals(MonitorCacheEvent.RESOURCE_STREAM)) {
            return null;
        }
        if (this.a.getName().equals("error")) {
            throw new fx(gl.a(this.a));
        } else if (this.a.getName().equals("warning")) {
            this.a.next();
            boolean equals = this.a.getName().equals("multi-login");
            return null;
        } else {
            this.a.getName().equals("bind");
            return null;
        }
    }
}
