package com.alibaba.android.anynetwork.plugin.allinone;

public class AllInOneRequestIdWrapper {
    public int id;
    public String type;

    public AllInOneRequestIdWrapper() {
    }

    public AllInOneRequestIdWrapper(String str, int i) {
        this.type = str;
        this.id = i;
    }
}
