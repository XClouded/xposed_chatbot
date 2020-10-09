package com.taobao.tao.image;

public interface IImageStrategySupport {
    String getConfigString(String str, String str2, String str3);

    boolean isNetworkSlow();

    boolean isSupportWebP();
}
