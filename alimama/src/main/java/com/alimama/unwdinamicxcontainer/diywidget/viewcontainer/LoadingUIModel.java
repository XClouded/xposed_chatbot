package com.alimama.unwdinamicxcontainer.diywidget.viewcontainer;

public class LoadingUIModel {
    private static LoadingUIModel sInstance;
    private int mCount;
    private int mTimeAverage = -1;

    public static LoadingUIModel getInstance() {
        if (sInstance == null) {
            sInstance = new LoadingUIModel();
        }
        return sInstance;
    }

    public boolean shouldShowLoading() {
        return this.mTimeAverage < 0 || this.mTimeAverage >= 500;
    }

    public void addStat(long j) {
        long j2 = ((long) (this.mCount * this.mTimeAverage)) + j;
        this.mCount++;
        this.mTimeAverage = (int) (j2 / ((long) this.mCount));
    }
}
