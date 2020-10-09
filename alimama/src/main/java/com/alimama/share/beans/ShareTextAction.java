package com.alimama.share.beans;

import com.alimama.share.SocialCamp;
import com.alimama.share.listeners.Action;

public class ShareTextAction extends Action {
    private String desc = "";
    private boolean isToCircle = false;
    private Platform platform = Platform.WEIXIN;
    private String title = "";

    public ShareTextAction(Platform platform2) {
        this.platform = platform2;
    }

    public ShareTextAction withTitle(String str) {
        this.title = str;
        return this;
    }

    public ShareTextAction withDesc(String str) {
        this.desc = str;
        return this;
    }

    public ShareTextAction isCirCle(boolean z) {
        this.isToCircle = z;
        return this;
    }

    public void execute() {
        SocialCamp.getInstance().dispatchFunction(this.platform).shareText(this.desc, this.isToCircle, this.title);
    }
}
