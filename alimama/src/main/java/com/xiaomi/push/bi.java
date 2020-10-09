package com.xiaomi.push;

import com.taobao.weex.el.parse.Operators;
import com.xiaomi.channel.commonutils.logger.b;
import java.text.SimpleDateFormat;
import java.util.Date;

public class bi implements fy {
    public static boolean a;

    /* renamed from: a  reason: collision with other field name */
    private a f138a = null;

    /* renamed from: a  reason: collision with other field name */
    private fm f139a = null;

    /* renamed from: a  reason: collision with other field name */
    private fp f140a = null;

    /* renamed from: a  reason: collision with other field name */
    private final String f141a = "[Slim] ";

    /* renamed from: a  reason: collision with other field name */
    private SimpleDateFormat f142a = new SimpleDateFormat("hh:mm:ss aaa");
    private a b = null;

    class a implements fr, fz {

        /* renamed from: a  reason: collision with other field name */
        String f143a;

        a(boolean z) {
            this.f143a = z ? " RCV " : " Sent ";
        }

        public void a(ff ffVar) {
            StringBuilder sb;
            String str;
            if (bi.a) {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(bi.a(bi.this).format(new Date()));
                sb.append(this.f143a);
                str = ffVar.toString();
            } else {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(bi.a(bi.this).format(new Date()));
                sb.append(this.f143a);
                sb.append(" Blob [");
                sb.append(ffVar.a());
                sb.append(",");
                sb.append(ffVar.a());
                sb.append(",");
                sb.append(ffVar.e());
                str = Operators.ARRAY_END_STR;
            }
            sb.append(str);
            b.c(sb.toString());
        }

        public void a(gd gdVar) {
            StringBuilder sb;
            String str;
            if (bi.a) {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(bi.a(bi.this).format(new Date()));
                sb.append(this.f143a);
                sb.append(" PKT ");
                str = gdVar.a();
            } else {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(bi.a(bi.this).format(new Date()));
                sb.append(this.f143a);
                sb.append(" PKT [");
                sb.append(gdVar.k());
                sb.append(",");
                sb.append(gdVar.j());
                str = Operators.ARRAY_END_STR;
            }
            sb.append(str);
            b.c(sb.toString());
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m121a(gd gdVar) {
            return true;
        }
    }

    static {
        boolean z = true;
        if (t.a() != 1) {
            z = false;
        }
        a = z;
    }

    public bi(fm fmVar) {
        this.f139a = fmVar;
        a();
    }

    private void a() {
        this.f138a = new a(true);
        this.b = new a(false);
        this.f139a.a((fr) this.f138a, (fz) this.f138a);
        this.f139a.b((fr) this.b, (fz) this.b);
        this.f140a = new bj(this);
    }
}
