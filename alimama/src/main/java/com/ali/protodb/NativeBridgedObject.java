package com.ali.protodb;

import androidx.annotation.Keep;

public class NativeBridgedObject {
    protected static boolean sNativeLibraryLoaded;
    @Keep
    private final long mNativePointer;

    @Keep
    private native void freeNativeObject();

    static {
        try {
            System.loadLibrary("ProtoDB");
            sNativeLibraryLoaded = true;
        } catch (Throwable unused) {
            sNativeLibraryLoaded = false;
        }
    }

    protected NativeBridgedObject(long j) {
        this.mNativePointer = j;
    }

    public long getNativePointer() {
        return this.mNativePointer;
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (sNativeLibraryLoaded) {
            freeNativeObject();
        }
    }
}
