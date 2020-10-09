package com.taobao.fresco.disk.common;

import java.io.IOException;
import java.io.InputStream;

public interface BinaryResource {
    InputStream openStream() throws IOException;

    byte[] read() throws IOException;

    long size();
}
