package com.taobao.rxm.consume;

public class ConsumeType {
    public static final int ALL = 29;
    public static final int CANCELLATION = 8;
    public static final int FAILURE = 16;
    public static final int LAST_RESULT = 2;
    public static final int NEW_RESULT = 1;
    public static final int PROGRESS_UPDATE = 4;
    public static final int SKIP = 0;
    private final int mConsumeType;

    public ConsumeType(int i) {
        this.mConsumeType = i;
    }

    public boolean activeOn(int i) {
        return (i & this.mConsumeType) > 0;
    }
}
