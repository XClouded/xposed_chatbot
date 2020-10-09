package com.taobao.weex.devtools.inspector.console;

public interface RuntimeRepl {
    Object evaluate(String str) throws Throwable;
}
