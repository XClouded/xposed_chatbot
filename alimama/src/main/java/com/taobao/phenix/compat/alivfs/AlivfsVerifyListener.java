package com.taobao.phenix.compat.alivfs;

public interface AlivfsVerifyListener {
    long getCurrentTime();

    boolean isExpectedTime(long j);

    boolean isTTLDomain(String str);
}
