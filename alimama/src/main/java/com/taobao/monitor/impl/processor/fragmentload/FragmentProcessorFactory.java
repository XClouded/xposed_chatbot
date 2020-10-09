package com.taobao.monitor.impl.processor.fragmentload;

import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.processor.IProcessorFactory;

class FragmentProcessorFactory implements IProcessorFactory<FragmentProcessor> {
    FragmentProcessorFactory() {
    }

    public FragmentProcessor createProcessor() {
        if (DynamicConstants.needFragment) {
            return new FragmentProcessor();
        }
        return null;
    }
}
