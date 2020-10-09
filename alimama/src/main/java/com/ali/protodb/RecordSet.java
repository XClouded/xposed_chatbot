package com.ali.protodb;

import androidx.annotation.Keep;

public class RecordSet extends NativeBridgedObject {
    @Keep
    private native long nativeGetRecord();

    @Keep
    private native boolean nativeNext();

    public RecordSet(long j) {
        super(j);
    }

    public boolean next() {
        return nativeNext();
    }

    public Record getRecord() {
        long nativeGetRecord = nativeGetRecord();
        if (nativeGetRecord > 0) {
            return new Record(nativeGetRecord);
        }
        return null;
    }
}
