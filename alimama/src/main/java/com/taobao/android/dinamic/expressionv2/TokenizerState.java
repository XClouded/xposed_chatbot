package com.taobao.android.dinamic.expressionv2;

public class TokenizerState {
    public static final int TokenizerStateBranchBegin = 11;
    public static final int TokenizerStateBranchEnd = 12;
    public static final int TokenizerStateBranchSep = 10;
    public static final int TokenizerStateConstBegin = 6;
    public static final int TokenizerStateConstEnd = 8;
    public static final int TokenizerStateConstName = 7;
    public static final int TokenizerStateDone = 13;
    public static final int TokenizerStateEnd = 15;
    public static final int TokenizerStateError = 14;
    public static final int TokenizerStateInit = 0;
    public static final int TokenizerStateMethodBegin = 1;
    public static final int TokenizerStateMethodBodyBegin = 3;
    public static final int TokenizerStateMethodBodyEnd = 5;
    public static final int TokenizerStateMethodName = 2;
    public static final int TokenizerStateSerialSep = 9;
    public static final int TokenizerStateVarName = 4;
}
