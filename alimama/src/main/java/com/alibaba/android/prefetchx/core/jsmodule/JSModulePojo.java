package com.alibaba.android.prefetchx.core.jsmodule;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.lang.ref.WeakReference;

public class JSModulePojo implements Serializable {
    public static final String LOAD = "load";
    public static final String UNLOAD = "unload";
    private static final long serialVersionUID = -8515422880909618195L;
    public String action = "load";
    @Nullable
    public transient WeakReference<Callback> callback;
    public transient String cdnComobPrefix;
    public transient String cdnComobUrl;
    @Nullable
    public String jsModule = null;
    @Nullable
    public String jsModuleUrl = null;
    public String key = null;
    public Long lastModified = null;
    public String name = null;
    public String url = "https://h5.m.taobao.com/prefetchx_mock";
    public String version = null;

    interface Callback {
        void done(Object obj);
    }

    public String getKey() {
        if (!TextUtils.isEmpty(this.key)) {
            return this.key;
        }
        return this.name + DinamicConstant.DINAMIC_PREFIX_AT + this.version;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JSModulePojo [name:");
        sb.append(this.name);
        sb.append(" version:");
        sb.append(this.version);
        sb.append("] action:");
        sb.append(this.action);
        sb.append(Operators.SPACE_STR);
        if (!TextUtils.isEmpty(this.jsModuleUrl)) {
            sb.append(" jsModuleUrl:");
            sb.append(this.jsModuleUrl);
        }
        return sb.toString();
    }
}
