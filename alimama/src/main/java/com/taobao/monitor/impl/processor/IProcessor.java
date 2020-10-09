package com.taobao.monitor.impl.processor;

public interface IProcessor {

    public interface IProcessorLifeCycle {
        void processorOnEnd(IProcessor iProcessor);

        void processorOnStart(IProcessor iProcessor);
    }
}
