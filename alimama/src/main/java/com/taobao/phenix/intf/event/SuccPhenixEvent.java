package com.taobao.phenix.intf.event;

import android.graphics.drawable.BitmapDrawable;
import com.taobao.phenix.intf.PhenixTicket;

public class SuccPhenixEvent extends PhenixEvent {
    BitmapDrawable drawable;
    private boolean fromDisk;
    @Deprecated
    boolean fromMCache;
    private boolean fromSecondary;
    boolean immediate;
    private boolean intermediate;

    public SuccPhenixEvent(PhenixTicket phenixTicket) {
        super(phenixTicket);
    }

    public BitmapDrawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(BitmapDrawable bitmapDrawable) {
        this.drawable = bitmapDrawable;
    }

    @Deprecated
    public boolean isFromMCache() {
        return this.fromMCache;
    }

    @Deprecated
    public void setFromMCache(boolean z) {
        this.fromMCache = z;
    }

    public void setImmediate(boolean z) {
        this.immediate = z;
    }

    public void setIntermediate(boolean z) {
        this.intermediate = z;
    }

    public void fromDisk(boolean z) {
        this.fromDisk = z;
    }

    public void fromSecondary(boolean z) {
        this.fromSecondary = z;
    }

    public boolean isImmediate() {
        return this.immediate;
    }

    public boolean isFromDisk() {
        return this.fromDisk;
    }

    public boolean isFromSecondary() {
        return this.fromSecondary;
    }

    public boolean isIntermediate() {
        return this.intermediate;
    }
}
