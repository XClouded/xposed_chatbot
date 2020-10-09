package com.taobao.weex.devtools;

import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;

public interface InspectorModulesProvider {
    Iterable<ChromeDevtoolsDomain> get();
}
