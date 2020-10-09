package com.uc.crashsdk.a;

import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.crashsdk.CrashLogFilesUploader;
import com.uc.crashsdk.a;
import com.uc.crashsdk.b;
import com.uc.crashsdk.f;

/* compiled from: ProGuard */
public class e implements Runnable {
    static final /* synthetic */ boolean a = (!e.class.desiredAssertionStatus());
    private final int b;
    private final Object[] c;

    public e(int i) {
        this.b = i;
        this.c = null;
    }

    public e(int i, Object[] objArr) {
        this.b = i;
        this.c = objArr;
    }

    public void run() {
        int i = this.b;
        if (i == 10) {
            f.a(this.b, this.c);
        } else if (i == 500) {
            d.a(this.b);
        } else if (i == 700) {
            f.b(this.b);
        } else if (i != 800) {
            switch (i) {
                case 100:
                case 101:
                    b.a(this.b);
                    return;
                default:
                    switch (i) {
                        case 201:
                        case 202:
                            a.a(this.b);
                            return;
                        default:
                            switch (i) {
                                case 301:
                                case 302:
                                case 303:
                                    h.a(this.b);
                                    return;
                                default:
                                    switch (i) {
                                        case 401:
                                        case 402:
                                        case 403:
                                        case 404:
                                        case 405:
                                        case 406:
                                        case 407:
                                        case 408:
                                        case 409:
                                        case 410:
                                        case 411:
                                        case 412:
                                            com.uc.crashsdk.e.a(this.b, this.c);
                                            return;
                                        default:
                                            switch (i) {
                                                case 600:
                                                case SecExceptionCode.SEC_ERROR_SIGNATRUE_INVALID_INPUT:
                                                    CrashLogFilesUploader.a(this.b, this.c);
                                                    return;
                                                default:
                                                    a.c("crashsdk", "Unknown async runnable: " + toString());
                                                    if (!a) {
                                                        throw new AssertionError();
                                                    }
                                                    return;
                                            }
                                    }
                            }
                    }
            }
        } else {
            g.a(this.b);
        }
    }

    public final boolean a() {
        int i = this.b;
        switch (i) {
            case 351:
            case 352:
            case 353:
            case 354:
                return h.a(this.b, this.c);
            default:
                switch (i) {
                    case 451:
                    case 452:
                        return com.uc.crashsdk.e.b(this.b, this.c);
                    default:
                        switch (i) {
                            case 751:
                            case 752:
                            case 753:
                                return f.a(this.b, this.c);
                            default:
                                a.c("crashsdk", "Unknown sync runnable: " + toString());
                                if (a) {
                                    return false;
                                }
                                throw new AssertionError();
                        }
                }
        }
    }

    public String toString() {
        return super.toString() + "@action_" + this.b;
    }
}
