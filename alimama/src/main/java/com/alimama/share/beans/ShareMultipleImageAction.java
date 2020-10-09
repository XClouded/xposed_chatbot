package com.alimama.share.beans;

import android.net.Uri;
import com.alimama.share.SocialCamp;
import com.alimama.share.interfaces.SocialFunction;
import com.alimama.share.listeners.Action;
import java.util.ArrayList;

public class ShareMultipleImageAction extends Action {
    private String desc = "";
    private boolean isToCircle = false;
    private Platform platform = Platform.WEIXIN;
    private String title = "";
    private ArrayList<Uri> uris;

    public ShareMultipleImageAction(Platform platform2) {
        this.platform = platform2;
    }

    public ShareMultipleImageAction withUri(ArrayList<Uri> arrayList) {
        this.uris = arrayList;
        return this;
    }

    public ShareMultipleImageAction withCircle(boolean z) {
        this.isToCircle = z;
        return this;
    }

    public ShareMultipleImageAction withTitle(String str) {
        this.title = str;
        return this;
    }

    public ShareMultipleImageAction withDesc(String str) {
        this.desc = str;
        return this;
    }

    public ShareMultipleImageAction isCirCle(boolean z) {
        this.isToCircle = z;
        return this;
    }

    public void execute() {
        SocialFunction dispatchFunction = SocialCamp.getInstance().dispatchFunction(this.platform);
        if (this.uris == null || this.uris.size() == 0) {
            getShareCallBack().end(this.platform, State.FAIL, "has no uri");
        } else {
            dispatchFunction.shareImages(this.uris, Boolean.valueOf(this.isToCircle), this.title, this.desc);
        }
    }
}
