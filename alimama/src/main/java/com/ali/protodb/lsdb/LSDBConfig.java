package com.ali.protodb.lsdb;

import androidx.annotation.Keep;

public class LSDBConfig {
    @Keep
    private int openFlag;
    @Keep
    private long timeToLive;
    @Keep
    private int walSize;

    public int getWalSize() {
        return this.walSize;
    }

    public void setWalSize(int i) {
        this.walSize = i;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public void setTimeToLive(long j) {
        this.timeToLive = j;
    }

    public int getOpenFlag() {
        return this.openFlag;
    }

    public void setOpenFlag(int i) {
        this.openFlag = i;
    }
}
