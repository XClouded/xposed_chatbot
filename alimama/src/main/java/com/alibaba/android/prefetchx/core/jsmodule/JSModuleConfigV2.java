package com.alibaba.android.prefetchx.core.jsmodule;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import mtopsdk.common.util.SymbolExpUtil;

public class JSModuleConfigV2 {
    public int a = 1;
    public String k = null;
    public int t = 0;
    public String u = null;

    @Nullable
    public JSModulePojo toJSModulePojo() {
        if (this.k == null || TextUtils.isEmpty(this.k)) {
            return null;
        }
        if (this.a == 1 && this.t == 0 && (this.u == null || TextUtils.isEmpty(this.u))) {
            return null;
        }
        JSModulePojo jSModulePojo = new JSModulePojo();
        String[] split = this.k.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
        if (split == null || split.length != 2) {
            return null;
        }
        jSModulePojo.name = split[0];
        jSModulePojo.version = split[1];
        jSModulePojo.key = this.k;
        if (this.a == 1) {
            jSModulePojo.action = "load";
        } else if (this.a == 2) {
            jSModulePojo.action = JSModulePojo.UNLOAD;
        }
        if (this.u != null && !TextUtils.isEmpty(this.u)) {
            jSModulePojo.jsModuleUrl = this.u;
            if (this.u.contains("g.alicdn.com/")) {
                jSModulePojo.cdnComobPrefix = "//g.alicdn.com/";
                jSModulePojo.cdnComobUrl = this.u.substring(this.u.indexOf("g.alicdn.com/") + "g.alicdn.com/".length());
            }
        } else if (this.t == 1 && !TextUtils.isEmpty(jSModulePojo.name)) {
            String[] split2 = jSModulePojo.name.split("-");
            if (split2 == null || split2.length != 2) {
                return null;
            }
            jSModulePojo.jsModuleUrl = "//g.alicdn.com/code/npm/" + jSModulePojo.name + "/" + jSModulePojo.version + "/dist/" + split2[1] + ".service.min.js";
            jSModulePojo.cdnComobPrefix = "//g.alicdn.com/code/npm/";
            jSModulePojo.cdnComobUrl = jSModulePojo.name + "/" + jSModulePojo.version + "/dist/" + split2[1] + ".service.min.js";
        } else if (this.t == 2 && !TextUtils.isEmpty(jSModulePojo.name)) {
            jSModulePojo.jsModuleUrl = "//g.alicdn.com/abs-mod/" + jSModulePojo.name + "/" + jSModulePojo.version + "/index.service.js";
            jSModulePojo.cdnComobPrefix = "//g.alicdn.com/abs-mod/";
            jSModulePojo.cdnComobUrl = jSModulePojo.name + "/" + jSModulePojo.version + "/index.service.js";
        }
        return jSModulePojo;
    }
}
