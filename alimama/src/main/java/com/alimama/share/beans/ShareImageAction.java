package com.alimama.share.beans;

import android.net.Uri;
import com.alimama.share.SocialCamp;
import com.alimama.share.interfaces.SocialFunction;
import com.alimama.share.listeners.Action;

public class ShareImageAction extends Action {
    private String desc = "";
    private boolean isToCircle = false;
    private Platform platform = Platform.WEIXIN;
    private String title = "";
    private Uri uri;

    public ShareImageAction(Platform platform2) {
        this.platform = platform2;
    }

    public ShareImageAction withUri(Uri uri2) {
        this.uri = uri2;
        return this;
    }

    public ShareImageAction withCircle(boolean z) {
        this.isToCircle = z;
        return this;
    }

    public ShareImageAction withTitle(String str) {
        this.title = str;
        return this;
    }

    public ShareImageAction withDesc(String str) {
        this.desc = str;
        return this;
    }

    public ShareImageAction isCirCle(boolean z) {
        this.isToCircle = z;
        return this;
    }

    public void execute() {
        SocialFunction dispatchFunction = SocialCamp.getInstance().dispatchFunction(this.platform);
        if (this.uri != null) {
            dispatchFunction.shareImage(this.uri, Boolean.valueOf(this.isToCircle), this.title, this.desc);
        } else {
            getShareCallBack().end(this.platform, State.FAIL, "has no uri");
        }
    }
}
