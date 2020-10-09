package com.taobao.monitor.impl.processor;

import com.taobao.monitor.impl.processor.IProcessor;

public interface IProcessorFactory<T extends IProcessor> {
    T createProcessor();
}
