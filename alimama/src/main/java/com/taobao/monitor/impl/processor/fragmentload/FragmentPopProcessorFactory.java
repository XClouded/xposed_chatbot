package com.taobao.monitor.impl.processor.fragmentload;

import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.processor.IProcessorFactory;

class FragmentPopProcessorFactory implements IProcessorFactory<FragmentPopProcessor> {
    FragmentPopProcessorFactory() {
    }

    public FragmentPopProcessor createProcessor() {
        if (DynamicConstants.needFragmentPop) {
            return new FragmentPopProcessor();
        }
        return null;
    }
}
