package com.alibaba.ha.protocol;

public interface AliHaPlugin {
    String getName();

    void start(AliHaParam aliHaParam);
}
