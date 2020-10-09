package org.slf4j.event;

import com.alibaba.android.umbrella.link.export.UMLLCons;

public enum Level {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, UMLLCons.FEATURE_TYPE_DEBUG),
    TRACE(0, "TRACE");
    
    private int levelInt;
    private String levelStr;

    private Level(int i, String str) {
        this.levelInt = i;
        this.levelStr = str;
    }

    public int toInt() {
        return this.levelInt;
    }

    public String toString() {
        return this.levelStr;
    }
}
