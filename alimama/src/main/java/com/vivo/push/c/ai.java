package com.vivo.push.c;

import com.uc.webview.export.cyclone.ErrorCode;
import com.vivo.push.v;
import com.vivo.push.y;

/* compiled from: PushClientTaskFactory */
public final class ai {
    public static v a(y yVar) {
        int b = yVar.b();
        if (b == 20) {
            return new ah(yVar);
        }
        switch (b) {
            case 0:
                break;
            case 1:
                return new ac(yVar);
            case 2:
                return new h(yVar);
            case 3:
                return new p(yVar);
            case 4:
                return new r(yVar);
            case 5:
                return new t(yVar);
            case 6:
                return new z(yVar);
            case 7:
                return new n(yVar);
            case 8:
                return new l(yVar);
            case 9:
                return new g(yVar);
            case 10:
                return new d(yVar);
            case 11:
                return new af(yVar);
            case 12:
                return new f(yVar);
            default:
                switch (b) {
                    case 100:
                        return new b(yVar);
                    case 101:
                        return new c(yVar);
                    default:
                        switch (b) {
                            case 2000:
                            case 2001:
                            case 2002:
                            case 2003:
                            case 2004:
                            case 2005:
                            case 2008:
                            case 2009:
                            case 2010:
                            case 2011:
                            case 2012:
                            case 2013:
                            case 2014:
                            case ErrorCode.UCSERVICE_IMPL_UNSEVENZIP_IMPL_NOT_FOUND:
                                break;
                            case 2006:
                                return new a(yVar);
                            case 2007:
                                return new ak(yVar);
                            default:
                                return null;
                        }
                }
        }
        return new aj(yVar);
    }

    public static ab b(y yVar) {
        int b = yVar.b();
        if (b == 20) {
            return new ah(yVar);
        }
        if (b == 2016) {
            return new k(yVar);
        }
        switch (b) {
            case 1:
                return new ac(yVar);
            case 2:
                return new h(yVar);
            case 3:
                return new p(yVar);
            case 4:
                return new r(yVar);
            case 5:
                return new t(yVar);
            case 6:
                return new z(yVar);
            case 7:
                return new n(yVar);
            case 8:
                return new l(yVar);
            case 9:
                return new g(yVar);
            case 10:
                return new d(yVar);
            case 11:
                return new af(yVar);
            default:
                return null;
        }
    }
}
