package com.alibaba.android.prefetchx.core.jsmodule;

import android.text.TextUtils;

public class JSModuleConfig {
    public int a = 1;
    public String k = null;
    public String n = null;
    public String u = null;
    public String v = null;

    public JSModulePojo toJSModulePojo() {
        JSModulePojo jSModulePojo = new JSModulePojo();
        jSModulePojo.name = this.n;
        jSModulePojo.version = this.v;
        if (this.a == 1) {
            jSModulePojo.action = "load";
        } else if (this.a == 2) {
            jSModulePojo.action = JSModulePojo.UNLOAD;
        }
        if (this.u != null && !TextUtils.isEmpty(this.u)) {
            jSModulePojo.jsModuleUrl = this.u;
        } else if (this.k != null) {
            jSModulePojo.jsModuleUrl = "//g.alicdn.com/code/npm/" + this.k;
        }
        return jSModulePojo;
    }
}
