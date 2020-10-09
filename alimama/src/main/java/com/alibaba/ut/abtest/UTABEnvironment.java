package com.alibaba.ut.abtest;

public enum UTABEnvironment {
    Product(0),
    Prepare(1),
    Daily(2);
    
    private final int value;

    private UTABEnvironment(int i) {
        this.value = i;
    }

    public static UTABEnvironment valueOf(int i) {
        for (UTABEnvironment uTABEnvironment : values()) {
            if (uTABEnvironment.getValue() == i) {
                return uTABEnvironment;
            }
        }
        return Product;
    }

    public int getValue() {
        return this.value;
    }
}
