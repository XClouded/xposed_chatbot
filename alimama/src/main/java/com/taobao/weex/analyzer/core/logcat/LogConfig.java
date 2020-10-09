package com.taobao.weex.analyzer.core.logcat;

import java.util.List;

public class LogConfig {
    private List<String> mCustomRule;
    private boolean showLogFilterPanel = true;
    private boolean showLogLevelPanel = true;
    private boolean showSearchPanel = true;
    private int viewSize = -1;

    public boolean isShowLogLevelPanel() {
        return this.showLogLevelPanel;
    }

    public void setShowLogLevelPanel(boolean z) {
        this.showLogLevelPanel = z;
    }

    public boolean isShowLogFilterPanel() {
        return this.showLogFilterPanel;
    }

    public void setShowLogFilterPanel(boolean z) {
        this.showLogFilterPanel = z;
    }

    public boolean isShowSearchPanel() {
        return this.showSearchPanel;
    }

    public void setShowSearchPanel(boolean z) {
        this.showSearchPanel = z;
    }

    public List<String> getCustomRule() {
        return this.mCustomRule;
    }

    public void setCustomRule(List<String> list) {
        this.mCustomRule = list;
    }

    public int getViewSize() {
        return this.viewSize;
    }

    public void setViewSize(int i) {
        this.viewSize = i;
    }
}
