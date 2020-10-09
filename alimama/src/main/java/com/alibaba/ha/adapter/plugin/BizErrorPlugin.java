package com.alibaba.ha.adapter.plugin;

import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import java.util.concurrent.atomic.AtomicBoolean;

public class BizErrorPlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.bizErrorReporter.name();
    }

    public void start(AliHaParam aliHaParam) {
        this.enabling.compareAndSet(false, true);
    }
}
