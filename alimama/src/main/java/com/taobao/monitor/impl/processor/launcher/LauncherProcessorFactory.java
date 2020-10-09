package com.taobao.monitor.impl.processor.launcher;

import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.processor.IProcessorFactory;

class LauncherProcessorFactory implements IProcessorFactory<LauncherProcessor> {
    LauncherProcessorFactory() {
    }

    public LauncherProcessor createProcessor() {
        if (DynamicConstants.needLauncher) {
            return new LauncherProcessor();
        }
        return null;
    }

    public LauncherProcessor createLinkManagerProcessor() {
        if (DynamicConstants.needLauncher) {
            return new LinkManagerProcessor();
        }
        return null;
    }

    public LauncherProcessor createLinkB2FManagerProcessor() {
        if (DynamicConstants.needLauncher) {
            return new LinkManagerProcessor("B2F");
        }
        return null;
    }
}
