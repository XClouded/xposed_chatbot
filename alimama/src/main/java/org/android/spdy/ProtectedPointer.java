package org.android.spdy;

import java.util.concurrent.atomic.AtomicLong;

class ProtectedPointer {
    private static final long CLOSED = 3;
    private static final long INIT = 1;
    private static final long WAIT_CLOSE = 2;
    private Object data2protected;
    protected ProtectedPointerOnClose how2close;
    private AtomicLong referAndstatus = new AtomicLong(1);

    interface ProtectedPointerOnClose {
        void close(Object obj);
    }

    ProtectedPointer(Object obj) {
        this.data2protected = obj;
    }

    /* access modifiers changed from: package-private */
    public void setHow2close(ProtectedPointerOnClose protectedPointerOnClose) {
        this.how2close = protectedPointerOnClose;
    }

    /* access modifiers changed from: package-private */
    public boolean enter() {
        long j;
        do {
            j = this.referAndstatus.get();
            if (j == 3) {
                return false;
            }
        } while (!this.referAndstatus.compareAndSet(j, 16 + j));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void exit() {
        this.referAndstatus.addAndGet(-16);
        if (this.referAndstatus.compareAndSet(2, 3)) {
            if (this.how2close != null) {
                this.how2close.close(this.data2protected);
            }
            this.data2protected = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        this.referAndstatus.incrementAndGet();
        if (this.referAndstatus.compareAndSet(2, 3)) {
            if (this.how2close != null) {
                this.how2close.close(this.data2protected);
            }
            this.data2protected = null;
        }
    }

    /* access modifiers changed from: package-private */
    public Object getData() {
        return this.data2protected;
    }
}
