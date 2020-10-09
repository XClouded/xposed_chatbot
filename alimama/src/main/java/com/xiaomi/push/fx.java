package com.xiaomi.push;

import java.io.PrintStream;
import java.io.PrintWriter;

public class fx extends Exception {
    private gg a = null;

    /* renamed from: a  reason: collision with other field name */
    private gh f392a = null;

    /* renamed from: a  reason: collision with other field name */
    private Throwable f393a = null;

    public fx() {
    }

    public fx(gg ggVar) {
        this.a = ggVar;
    }

    public fx(String str) {
        super(str);
    }

    public fx(String str, Throwable th) {
        super(str);
        this.f393a = th;
    }

    public fx(Throwable th) {
        this.f393a = th;
    }

    public Throwable a() {
        return this.f393a;
    }

    public String getMessage() {
        String message = super.getMessage();
        return (message != null || this.f392a == null) ? (message != null || this.a == null) ? message : this.a.toString() : this.f392a.toString();
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (this.f393a != null) {
            printStream.println("Nested Exception: ");
            this.f393a.printStackTrace(printStream);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (this.f393a != null) {
            printWriter.println("Nested Exception: ");
            this.f393a.printStackTrace(printWriter);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String message = super.getMessage();
        if (message != null) {
            sb.append(message);
            sb.append(": ");
        }
        if (this.f392a != null) {
            sb.append(this.f392a);
        }
        if (this.a != null) {
            sb.append(this.a);
        }
        if (this.f393a != null) {
            sb.append("\n  -- caused by: ");
            sb.append(this.f393a);
        }
        return sb.toString();
    }
}
