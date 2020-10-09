package com.taobao.weex.analyzer.core.memory;

import android.content.Context;

public class PSSMemoryInfoSampler {
    public static PssInfo getAppPssInfo(Context context) {
        PssInfo pssInfo = new PssInfo();
        pssInfo.totalPss = (double) MemoryTracker.getTotalPss(context);
        pssInfo.dalvikPss = (double) MemoryTracker.getDalvikPss(context);
        pssInfo.nativePss = (double) MemoryTracker.getNativePss(context);
        return pssInfo;
    }

    public static class PssInfo {
        public double dalvikPss;
        public double nativePss;
        public double otherPss;
        public double totalPss;

        public String toString() {
            return "PssInfo{totalPss=" + this.totalPss + ", dalvikPss=" + this.dalvikPss + ", nativePss=" + this.nativePss + ", otherPss=" + this.otherPss + '}';
        }
    }
}
