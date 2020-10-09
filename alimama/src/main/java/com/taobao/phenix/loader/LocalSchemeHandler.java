package com.taobao.phenix.loader;

import com.taobao.phenix.entity.ResponseData;
import java.io.IOException;

public interface LocalSchemeHandler {
    ResponseData handleScheme(String str) throws IOException;

    boolean isSupported(String str);
}
