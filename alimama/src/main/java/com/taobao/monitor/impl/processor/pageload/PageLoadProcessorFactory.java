package com.taobao.monitor.impl.processor.pageload;

import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.processor.IProcessorFactory;

class PageLoadProcessorFactory implements IProcessorFactory<PageLoadProcessor> {
    PageLoadProcessorFactory() {
    }

    public PageLoadProcessor createProcessor() {
        if (DynamicConstants.needPageLoad) {
            return new PageLoadProcessor();
        }
        return null;
    }
}
