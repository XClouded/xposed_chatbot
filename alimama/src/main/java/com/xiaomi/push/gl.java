package com.xiaomi.push;

import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.gb;
import com.xiaomi.push.gf;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.au;
import com.xiaomi.push.service.e;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class gl {
    private static XmlPullParser a;

    public static ga a(String str, String str2, XmlPullParser xmlPullParser) {
        Object a2 = gk.a().a("all", "xm:chat");
        if (a2 == null || !(a2 instanceof e)) {
            return null;
        }
        return ((e) a2).b(xmlPullParser);
    }

    public static gb a(XmlPullParser xmlPullParser, fm fmVar) {
        String attributeValue = xmlPullParser.getAttributeValue("", "id");
        String attributeValue2 = xmlPullParser.getAttributeValue("", "to");
        String attributeValue3 = xmlPullParser.getAttributeValue("", WeexDevOptions.EXTRA_FROM);
        String attributeValue4 = xmlPullParser.getAttributeValue("", "chid");
        gb.a a2 = gb.a.a(xmlPullParser.getAttributeValue("", "type"));
        HashMap hashMap = new HashMap();
        boolean z = false;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            hashMap.put(attributeName, xmlPullParser.getAttributeValue("", attributeName));
        }
        gb gbVar = null;
        gh ghVar = null;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("error")) {
                    ghVar = a(xmlPullParser);
                } else {
                    gbVar = new gb();
                    gbVar.a(a(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("iq")) {
                z = true;
            }
        }
        if (gbVar == null) {
            if (gb.a.a == a2 || gb.a.b == a2) {
                gm gmVar = new gm();
                gmVar.k(attributeValue);
                gmVar.m(attributeValue3);
                gmVar.n(attributeValue2);
                gmVar.a(gb.a.d);
                gmVar.l(attributeValue4);
                gmVar.a(new gh(gh.a.e));
                fmVar.a((gd) gmVar);
                b.d("iq usage error. send packet in packet parser.");
                return null;
            }
            gbVar = new gn();
        }
        gbVar.k(attributeValue);
        gbVar.m(attributeValue2);
        gbVar.l(attributeValue4);
        gbVar.n(attributeValue3);
        gbVar.a(a2);
        gbVar.a(ghVar);
        gbVar.a((Map<String, String>) hashMap);
        return gbVar;
    }

    public static gd a(XmlPullParser xmlPullParser) {
        String str;
        boolean z = false;
        String str2 = null;
        if ("1".equals(xmlPullParser.getAttributeValue("", "s"))) {
            String attributeValue = xmlPullParser.getAttributeValue("", "chid");
            String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
            String attributeValue3 = xmlPullParser.getAttributeValue("", WeexDevOptions.EXTRA_FROM);
            String attributeValue4 = xmlPullParser.getAttributeValue("", "to");
            String attributeValue5 = xmlPullParser.getAttributeValue("", "type");
            al.b a2 = al.a().a(attributeValue, attributeValue4);
            if (a2 == null) {
                a2 = al.a().a(attributeValue, attributeValue3);
            }
            if (a2 != null) {
                gd gdVar = null;
                while (!z) {
                    int next = xmlPullParser.next();
                    if (next == 2) {
                        if (!"s".equals(xmlPullParser.getName())) {
                            throw new fx("error while receiving a encrypted message with wrong format");
                        } else if (xmlPullParser.next() == 4) {
                            String text = xmlPullParser.getText();
                            if ("5".equals(attributeValue) || Constants.LogTransferLevel.L6.equals(attributeValue)) {
                                gc gcVar = new gc();
                                gcVar.l(attributeValue);
                                gcVar.b(true);
                                gcVar.n(attributeValue3);
                                gcVar.m(attributeValue4);
                                gcVar.k(attributeValue2);
                                gcVar.f(attributeValue5);
                                String[] strArr = null;
                                ga gaVar = new ga("s", (String) null, strArr, strArr);
                                gaVar.a(text);
                                gcVar.a(gaVar);
                                return gcVar;
                            }
                            a(au.a(au.a(a2.h, attributeValue2), text));
                            a.next();
                            gdVar = a(a);
                        } else {
                            throw new fx("error while receiving a encrypted message with wrong format");
                        }
                    } else if (next == 3 && xmlPullParser.getName().equals("message")) {
                        z = true;
                    }
                }
                if (gdVar != null) {
                    return gdVar;
                }
                throw new fx("error while receiving a encrypted message with wrong format");
            }
            throw new fx("the channel id is wrong while receiving a encrypted message");
        }
        gc gcVar2 = new gc();
        String attributeValue6 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue6 == null) {
            attributeValue6 = "ID_NOT_AVAILABLE";
        }
        gcVar2.k(attributeValue6);
        gcVar2.m(xmlPullParser.getAttributeValue("", "to"));
        gcVar2.n(xmlPullParser.getAttributeValue("", WeexDevOptions.EXTRA_FROM));
        gcVar2.l(xmlPullParser.getAttributeValue("", "chid"));
        gcVar2.a(xmlPullParser.getAttributeValue("", "appid"));
        try {
            str = xmlPullParser.getAttributeValue("", "transient");
        } catch (Exception unused) {
            str = null;
        }
        try {
            String attributeValue7 = xmlPullParser.getAttributeValue("", "seq");
            if (!TextUtils.isEmpty(attributeValue7)) {
                gcVar2.b(attributeValue7);
            }
        } catch (Exception unused2) {
        }
        try {
            String attributeValue8 = xmlPullParser.getAttributeValue("", "mseq");
            if (!TextUtils.isEmpty(attributeValue8)) {
                gcVar2.c(attributeValue8);
            }
        } catch (Exception unused3) {
        }
        try {
            String attributeValue9 = xmlPullParser.getAttributeValue("", "fseq");
            if (!TextUtils.isEmpty(attributeValue9)) {
                gcVar2.d(attributeValue9);
            }
        } catch (Exception unused4) {
        }
        try {
            String attributeValue10 = xmlPullParser.getAttributeValue("", "status");
            if (!TextUtils.isEmpty(attributeValue10)) {
                gcVar2.e(attributeValue10);
            }
        } catch (Exception unused5) {
        }
        gcVar2.a(!TextUtils.isEmpty(str) && str.equalsIgnoreCase("true"));
        gcVar2.f(xmlPullParser.getAttributeValue("", "type"));
        String b = b(xmlPullParser);
        if (b == null || "".equals(b.trim())) {
            gd.q();
        } else {
            gcVar2.j(b);
        }
        while (!z) {
            int next2 = xmlPullParser.next();
            if (next2 == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (TextUtils.isEmpty(namespace)) {
                    namespace = "xm";
                }
                if (name.equals("subject")) {
                    String b2 = b(xmlPullParser);
                    gcVar2.g(a(xmlPullParser));
                } else if (name.equals("body")) {
                    String attributeValue11 = xmlPullParser.getAttributeValue("", "encode");
                    String a3 = a(xmlPullParser);
                    if (!TextUtils.isEmpty(attributeValue11)) {
                        gcVar2.a(a3, attributeValue11);
                    } else {
                        gcVar2.h(a3);
                    }
                } else if (name.equals("thread")) {
                    if (str2 == null) {
                        str2 = xmlPullParser.nextText();
                    }
                } else if (name.equals("error")) {
                    gcVar2.a(a(xmlPullParser));
                } else {
                    gcVar2.a(a(name, namespace, xmlPullParser));
                }
            } else if (next2 == 3 && xmlPullParser.getName().equals("message")) {
                z = true;
            }
        }
        gcVar2.i(str2);
        return gcVar2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gf m343a(XmlPullParser xmlPullParser) {
        gf.b bVar = gf.b.available;
        String attributeValue = xmlPullParser.getAttributeValue("", "type");
        if (attributeValue != null && !attributeValue.equals("")) {
            try {
                bVar = gf.b.valueOf(attributeValue);
            } catch (IllegalArgumentException unused) {
                PrintStream printStream = System.err;
                printStream.println("Found invalid presence type " + attributeValue);
            }
        }
        gf gfVar = new gf(bVar);
        gfVar.m(xmlPullParser.getAttributeValue("", "to"));
        gfVar.n(xmlPullParser.getAttributeValue("", WeexDevOptions.EXTRA_FROM));
        gfVar.l(xmlPullParser.getAttributeValue("", "chid"));
        String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue2 == null) {
            attributeValue2 = "ID_NOT_AVAILABLE";
        }
        gfVar.k(attributeValue2);
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("status")) {
                    gfVar.a(xmlPullParser.nextText());
                } else if (name.equals("priority")) {
                    try {
                        gfVar.a(Integer.parseInt(xmlPullParser.nextText()));
                    } catch (NumberFormatException unused2) {
                    } catch (IllegalArgumentException unused3) {
                        gfVar.a(0);
                    }
                } else if (name.equals("show")) {
                    String nextText = xmlPullParser.nextText();
                    try {
                        gfVar.a(gf.a.valueOf(nextText));
                    } catch (IllegalArgumentException unused4) {
                        PrintStream printStream2 = System.err;
                        printStream2.println("Found invalid presence mode " + nextText);
                    }
                } else if (name.equals("error")) {
                    gfVar.a(a(xmlPullParser));
                } else {
                    gfVar.a(a(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("presence")) {
                z = true;
            }
        }
        return gfVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gg m344a(XmlPullParser xmlPullParser) {
        gg ggVar = null;
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                ggVar = new gg(xmlPullParser.getName());
            } else if (next == 3 && xmlPullParser.getName().equals("error")) {
                z = true;
            }
        }
        return ggVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gh m345a(XmlPullParser xmlPullParser) {
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        String str = "-1";
        String str2 = null;
        String str3 = null;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            if (xmlPullParser.getAttributeName(i).equals("code")) {
                str = xmlPullParser.getAttributeValue("", "code");
            }
            if (xmlPullParser.getAttributeName(i).equals("type")) {
                str2 = xmlPullParser.getAttributeValue("", "type");
            }
            if (xmlPullParser.getAttributeName(i).equals("reason")) {
                str3 = xmlPullParser.getAttributeValue("", "reason");
            }
        }
        String str4 = null;
        String str5 = null;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("text")) {
                    str5 = xmlPullParser.nextText();
                } else {
                    String name = xmlPullParser.getName();
                    String namespace = xmlPullParser.getNamespace();
                    if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(namespace)) {
                        str4 = name;
                    } else {
                        arrayList.add(a(name, namespace, xmlPullParser));
                    }
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("error")) {
                    z = true;
                }
            } else if (next == 4) {
                str5 = xmlPullParser.getText();
            }
        }
        return new gh(Integer.parseInt(str), str2 == null ? "cancel" : str2, str3, str4, str5, arrayList);
    }

    /* renamed from: a  reason: collision with other method in class */
    private static String m346a(XmlPullParser xmlPullParser) {
        String str = "";
        int depth = xmlPullParser.getDepth();
        while (true) {
            if (xmlPullParser.next() == 3 && xmlPullParser.getDepth() == depth) {
                return str;
            }
            str = str + xmlPullParser.getText();
        }
    }

    private static void a(byte[] bArr) {
        if (a == null) {
            try {
                a = XmlPullParserFactory.newInstance().newPullParser();
                a.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        a.setInput(new InputStreamReader(new ByteArrayInputStream(bArr)));
    }

    private static String b(XmlPullParser xmlPullParser) {
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(xmlPullParser.getAttributePrefix(i)))) {
                return xmlPullParser.getAttributeValue(i);
            }
        }
        return null;
    }
}
