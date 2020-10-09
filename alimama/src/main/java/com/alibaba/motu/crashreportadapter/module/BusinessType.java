package com.alibaba.motu.crashreportadapter.module;

public enum BusinessType {
    WEEX_ERROR("weex js error"),
    WINDVANE_ERROR("windvane error"),
    IMAGE_ERROR("图片库错误"),
    ATLAS_ERROR("Atlas错误"),
    OTHER_ERROR("其他错误");
    
    private String name;

    private BusinessType(String str) {
        this.name = str;
    }
}
