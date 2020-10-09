package com.taobao.aipc.exception;

public class ErrorCodes {
    public static final int ACCESS_DENIED = 5;
    public static final int CALLBACK_NOT_ALIVE = 22;
    public static final int CLASS_NOT_FOUND = 16;
    public static final int CLASS_WITH_PROCESS = 19;
    public static final int CONSTRUCTOR_NOT_FOUND = 15;
    public static final int DECODE_EXCEPTION = 7;
    public static final int ENCODE_EXCEPTION = 6;
    public static final int FILE_DESCRIPTOR_TRANSFER_FAIL = 23;
    public static final int GETTING_INSTANCE_METHOD_NOT_FOUND = 13;
    public static final int GETTING_INSTANCE_RETURN_TYPE_ERROR = 12;
    public static final int ILLEGAL_PARAMETER_EXCEPTION = 4;
    public static final int METHOD_GET_INSTANCE_NOT_STATIC = 21;
    public static final int METHOD_INVOCATION_EXCEPTION = 18;
    public static final int METHOD_NOT_FOUND = 17;
    public static final int METHOD_PARAMETER_NOT_MATCHING = 9;
    public static final int METHOD_RETURN_TYPE_NOT_MATCHING = 10;
    public static final int METHOD_WITH_PROCESS = 20;
    public static final int NULL_POINTER_EXCEPTION = 3;
    public static final int REMOTE_EXCEPTION = 1;
    public static final int SERVICE_UNAVAILABLE = 2;
    public static final int SUCCESS = 0;
    public static final int TOO_MANY_MATCHING_CONSTRUCTORS_FOR_CREATING_INSTANCE = 14;
    public static final int TOO_MANY_MATCHING_METHODS = 8;
    public static final int TOO_MANY_MATCHING_METHODS_FOR_GETTING_INSTANCE = 11;
    public static final int VOID = -1;

    private ErrorCodes() {
    }
}
