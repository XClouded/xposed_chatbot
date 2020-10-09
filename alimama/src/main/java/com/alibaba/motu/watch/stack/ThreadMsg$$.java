package com.alibaba.motu.watch.stack;

/* compiled from: ThreadMsg */
class ThreadMsg$$ {
    /* access modifiers changed from: private */
    public final String _name;
    /* access modifiers changed from: private */
    public final StackTraceElement[] _stackTrace;

    /* compiled from: ThreadMsg */
    private class _Thread extends Throwable {
        private _Thread(_Thread _thread) {
            super(ThreadMsg$$.this._name, _thread);
        }

        public Throwable fillInStackTrace() {
            setStackTrace(ThreadMsg$$.this._stackTrace);
            return this;
        }
    }

    private ThreadMsg$$(String str, StackTraceElement[] stackTraceElementArr) {
        this._name = str;
        this._stackTrace = stackTraceElementArr;
    }
}
