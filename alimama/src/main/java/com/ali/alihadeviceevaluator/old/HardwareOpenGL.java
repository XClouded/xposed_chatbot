package com.ali.alihadeviceevaluator.old;

public class HardwareOpenGL implements CalScore {
    public float mOpenglv = 2.0f;

    public int getScore(HardWareInfo hardWareInfo) {
        if (((double) this.mOpenglv) > 4.0d) {
            return 10;
        }
        if (((double) this.mOpenglv) >= 4.0d) {
            return 9;
        }
        if (((double) this.mOpenglv) >= 3.2d) {
            return 8;
        }
        if (((double) this.mOpenglv) >= 3.1d) {
            return 7;
        }
        if (((double) this.mOpenglv) >= 3.0d) {
            return 6;
        }
        if (((double) this.mOpenglv) >= 2.0d) {
            return 3;
        }
        return 8;
    }
}
