package com.alibaba.android.umbrella.link.export;

public enum UMTagKey {
    TAG_1(UMDimKey.TAG_1),
    TAG_2(UMDimKey.TAG_2),
    TAG_3(UMDimKey.TAG_3),
    TAG_4(UMDimKey.TAG_4),
    TAG_5(UMDimKey.TAG_5);
    
    private final UMDimKey dimKey;

    private UMTagKey(UMDimKey uMDimKey) {
        this.dimKey = uMDimKey;
    }

    public UMDimKey getDimKey() {
        return this.dimKey;
    }
}
