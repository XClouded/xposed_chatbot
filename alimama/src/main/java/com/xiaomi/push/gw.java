package com.xiaomi.push;

import java.net.UnknownHostException;

final class gw {

    static class a {
        fb a;

        /* renamed from: a  reason: collision with other field name */
        String f431a;

        a() {
        }
    }

    static a a(Exception exc) {
        a(exc);
        boolean z = exc instanceof fx;
        Throwable th = exc;
        if (z) {
            fx fxVar = (fx) exc;
            th = exc;
            if (fxVar.a() != null) {
                th = fxVar.a();
            }
        }
        a aVar = new a();
        String message = th.getMessage();
        if (th.getCause() != null) {
            message = th.getCause().getMessage();
        }
        String str = th.getClass().getSimpleName() + ":" + message;
        int a2 = fo.a(th);
        if (a2 != 0) {
            aVar.a = fb.a(fb.GSLB_REQUEST_SUCCESS.a() + a2);
        }
        if (aVar.a == null) {
            aVar.a = fb.GSLB_TCP_ERR_OTHER;
        }
        if (aVar.a == fb.GSLB_TCP_ERR_OTHER) {
            aVar.f431a = str;
        }
        return aVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static void m352a(Exception exc) {
        if (exc == null) {
            throw new NullPointerException();
        }
    }

    static a b(Exception exc) {
        fb fbVar;
        Throwable cause;
        a(exc);
        boolean z = exc instanceof fx;
        Throwable th = exc;
        if (z) {
            fx fxVar = (fx) exc;
            th = exc;
            if (fxVar.a() != null) {
                th = fxVar.a();
            }
        }
        a aVar = new a();
        String message = th.getMessage();
        if (th.getCause() != null) {
            message = th.getCause().getMessage();
        }
        int a2 = fo.a(th);
        String str = th.getClass().getSimpleName() + ":" + message;
        if (a2 != 0) {
            aVar.a = fb.a(fb.CONN_SUCCESS.a() + a2);
            if (aVar.a == fb.CONN_BOSH_ERR && (cause = th.getCause()) != null && (cause instanceof UnknownHostException)) {
                fbVar = fb.CONN_BOSH_UNKNOWNHOST;
            }
            if (aVar.a == fb.CONN_TCP_ERR_OTHER || aVar.a == fb.CONN_XMPP_ERR || aVar.a == fb.CONN_BOSH_ERR) {
                aVar.f431a = str;
            }
            return aVar;
        }
        fbVar = fb.CONN_XMPP_ERR;
        aVar.a = fbVar;
        aVar.f431a = str;
        return aVar;
    }

    static a c(Exception exc) {
        fb fbVar;
        a(exc);
        boolean z = exc instanceof fx;
        Throwable th = exc;
        if (z) {
            fx fxVar = (fx) exc;
            th = exc;
            if (fxVar.a() != null) {
                th = fxVar.a();
            }
        }
        a aVar = new a();
        String message = th.getMessage();
        if (th.getCause() != null) {
            message = th.getCause().getMessage();
        }
        int a2 = fo.a(th);
        String str = th.getClass().getSimpleName() + ":" + message;
        if (a2 == 105) {
            fbVar = fb.BIND_TCP_READ_TIMEOUT;
        } else if (a2 == 199) {
            fbVar = fb.BIND_TCP_ERR;
        } else if (a2 != 499) {
            switch (a2) {
                case 109:
                    fbVar = fb.BIND_TCP_CONNRESET;
                    break;
                case 110:
                    fbVar = fb.BIND_TCP_BROKEN_PIPE;
                    break;
                default:
                    fbVar = fb.BIND_XMPP_ERR;
                    break;
            }
        } else {
            aVar.a = fb.BIND_BOSH_ERR;
            if (message.startsWith("Terminal binding condition encountered: item-not-found")) {
                fbVar = fb.BIND_BOSH_ITEM_NOT_FOUND;
            }
            if (aVar.a == fb.BIND_TCP_ERR || aVar.a == fb.BIND_XMPP_ERR || aVar.a == fb.BIND_BOSH_ERR) {
                aVar.f431a = str;
            }
            return aVar;
        }
        aVar.a = fbVar;
        aVar.f431a = str;
        return aVar;
    }

    static a d(Exception exc) {
        fb fbVar;
        a(exc);
        boolean z = exc instanceof fx;
        Throwable th = exc;
        if (z) {
            fx fxVar = (fx) exc;
            th = exc;
            if (fxVar.a() != null) {
                th = fxVar.a();
            }
        }
        a aVar = new a();
        String message = th.getMessage();
        int a2 = fo.a(th);
        String str = th.getClass().getSimpleName() + ":" + message;
        if (a2 == 105) {
            fbVar = fb.CHANNEL_TCP_READTIMEOUT;
        } else if (a2 == 199) {
            fbVar = fb.CHANNEL_TCP_ERR;
        } else if (a2 != 499) {
            switch (a2) {
                case 109:
                    fbVar = fb.CHANNEL_TCP_CONNRESET;
                    break;
                case 110:
                    fbVar = fb.CHANNEL_TCP_BROKEN_PIPE;
                    break;
                default:
                    fbVar = fb.CHANNEL_XMPPEXCEPTION;
                    break;
            }
        } else {
            aVar.a = fb.CHANNEL_BOSH_EXCEPTION;
            if (message.startsWith("Terminal binding condition encountered: item-not-found")) {
                fbVar = fb.CHANNEL_BOSH_ITEMNOTFIND;
            }
            if (aVar.a == fb.CHANNEL_TCP_ERR || aVar.a == fb.CHANNEL_XMPPEXCEPTION || aVar.a == fb.CHANNEL_BOSH_EXCEPTION) {
                aVar.f431a = str;
            }
            return aVar;
        }
        aVar.a = fbVar;
        aVar.f431a = str;
        return aVar;
    }
}
