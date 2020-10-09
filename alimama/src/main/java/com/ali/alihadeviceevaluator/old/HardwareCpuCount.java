package com.ali.alihadeviceevaluator.old;

public class HardwareCpuCount implements CalScore {
    public int getScore(HardWareInfo hardWareInfo) {
        if (hardWareInfo == null) {
            return 0;
        }
        if (hardWareInfo.mCpuCount >= 16) {
            return 10;
        }
        if (hardWareInfo.mCpuCount >= 8) {
            return 9;
        }
        if (hardWareInfo.mCpuCount >= 6) {
            return 8;
        }
        if (hardWareInfo.mCpuCount >= 4) {
            return 6;
        }
        if (hardWareInfo.mCpuCount >= 2) {
            return 4;
        }
        return 2;
    }
}
