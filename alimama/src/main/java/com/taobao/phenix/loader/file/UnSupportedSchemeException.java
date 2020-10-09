package com.taobao.phenix.loader.file;

public class UnSupportedSchemeException extends Exception {
    public UnSupportedSchemeException(int i) {
        super("SchemeType(" + i + ") cannot be supported now");
    }
}
