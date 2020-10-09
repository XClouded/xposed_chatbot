package com.taobao.android;

public interface AliImageStrategyConfigBuilderInterface {

    public enum AliSizeLimitType {
        WIDTH_LIMIT,
        HEIGHT_LIMIT,
        ALL_LIMIT
    }

    Object build();

    AliImageStrategyConfigBuilderInterface setSizeLimitType(AliSizeLimitType aliSizeLimitType);
}
