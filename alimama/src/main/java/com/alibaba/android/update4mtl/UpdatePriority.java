package com.alibaba.android.update4mtl;

public enum UpdatePriority {
    PRI_NORMAL_TYPE("0"),
    FORCE_TYPE("1"),
    SILENT_TYPE("2"),
    NOT_DISTURB_A_TYPE("3"),
    NOT_DISTURB_B_TYPE("13"),
    SILENT_A_TYPE("10"),
    SILENT_B_TYPE("12"),
    FORCE_WHEN_WIFI_TYPE("21");
    
    private String mValue;

    private UpdatePriority(String str) {
        this.mValue = str;
    }

    public String getValue() {
        return this.mValue;
    }

    public String toString() {
        return String.valueOf(this.mValue);
    }
}
