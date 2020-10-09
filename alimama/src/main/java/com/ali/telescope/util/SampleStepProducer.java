package com.ali.telescope.util;

public class SampleStepProducer {
    private static final int FIRST_LEVEL = 20;
    private static final int FORTH_LEVEL = 300;
    private static final int SECOND_LEVEL = 60;
    private static final int THIRD_LEVEL = 140;
    private int currentSampleTime;

    public int nextStep() {
        this.currentSampleTime++;
        if (this.currentSampleTime <= 20) {
            return 2;
        }
        if (this.currentSampleTime <= 60) {
            return 4;
        }
        if (this.currentSampleTime <= THIRD_LEVEL) {
            return 8;
        }
        return this.currentSampleTime <= 300 ? 16 : Integer.MAX_VALUE;
    }

    public int getSampleTimes() {
        return this.currentSampleTime;
    }

    public void reset() {
        this.currentSampleTime = 0;
    }
}
