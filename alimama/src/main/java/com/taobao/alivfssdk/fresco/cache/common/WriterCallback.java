package com.taobao.alivfssdk.fresco.cache.common;

import java.io.IOException;
import java.io.OutputStream;

public interface WriterCallback {
    OutputStream write(OutputStream outputStream) throws IOException;
}
