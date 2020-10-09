package com.taobao.weex.devtools.inspector.elements;

import com.taobao.weex.devtools.common.ThreadBound;

public interface DocumentProviderFactory extends ThreadBound {
    DocumentProvider create();
}
