package com.facebook.stetho.dumpapp;

import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;

class UnexpectedFrameException extends DumpappFramingException {
    public UnexpectedFrameException(byte b, byte b2) {
        super("Expected '" + b + "', got: '" + b2 + DXBindingXConstant.SINGLE_QUOTE);
    }
}
