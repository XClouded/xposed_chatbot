package com.alibaba.ut.abtest.bucketing.expression;

public class ExpressionException extends RuntimeException {
    private static final long serialVersionUID = -1051325072049880536L;

    public ExpressionException() {
    }

    public ExpressionException(String str) {
        super(str);
    }
}
