package com.alibaba.ut.abtest.internal.bucketing.model;

public enum ExperimentType {
    AbComponent(2),
    AbUri(3),
    Redirect(4);
    
    private final int value;

    private ExperimentType(int i) {
        this.value = i;
    }

    public static ExperimentType valueOf(int i) {
        for (ExperimentType experimentType : values()) {
            if (experimentType.getValue() == i) {
                return experimentType;
            }
        }
        return AbComponent;
    }

    public int getValue() {
        return this.value;
    }
}
