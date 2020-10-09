package com.taobao.weex.analyzer.core.lint;

import java.util.List;
import java.util.Map;

public class VDOMInfo {
    public Map<String, Object> attrs;
    public List<VDOMInfo> children;
    public String realName;
    public String simpleName;

    public String toString() {
        return "VDOMInfo{simpleName='" + this.simpleName + '\'' + ", realName='" + this.realName + '\'' + ", attrs=" + this.attrs + ", children=" + this.children + '}';
    }
}
