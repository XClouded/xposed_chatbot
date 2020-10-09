package com.alimama.share.listeners;

import com.alimama.share.beans.Platform;
import com.alimama.share.beans.State;

public abstract class Action {
    private ShareCallBack shareCallBack;

    public abstract void execute();

    public void setShareCallBack(ShareCallBack shareCallBack2) {
        this.shareCallBack = shareCallBack2;
    }

    public ShareCallBack getShareCallBack() {
        return this.shareCallBack == null ? new ShareCallBack() {
            public void end(Platform platform, State state, String str) {
            }

            public void start() {
            }
        } : this.shareCallBack;
    }
}
