package com.alimama.union.app.share.flutter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.share.EtaoShare;
import com.alimama.share.beans.Platform;
import com.alimama.share.beans.ShareImageAction;
import com.alimama.share.beans.ShareMultipleImageAction;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import java.util.ArrayList;

public class ShareChannelClickHandler {
    private final Context context;

    public ShareChannelClickHandler(Context context2) {
        this.context = context2;
    }

    public void immediateShareToWechat(@Nullable ArrayList<Uri> arrayList) {
        if (!ShareUtils.isWechatInstalled(this.context)) {
            ToastUtil.showToast(this.context, (int) R.string.wechat_not_installed);
            return;
        }
        ShareMultipleImageAction shareMultipleImageAction = new ShareMultipleImageAction(Platform.WEIXIN);
        if (arrayList != null) {
            shareMultipleImageAction.withUri(arrayList);
        }
        shareMultipleImageAction.isCirCle(false);
        EtaoShare.getInstance().exec(shareMultipleImageAction);
    }

    public boolean shareToWechat() {
        if (ShareUtils.openWechat(this.context)) {
            return true;
        }
        ToastUtil.showToast(this.context, (int) R.string.wechat_not_installed);
        return false;
    }

    public void shareToWechatMoments(@Nullable ArrayList<Uri> arrayList) {
        if (!ShareUtils.isWechatInstalled(this.context)) {
            ToastUtil.showToast(this.context, (int) R.string.wechat_not_installed);
            return;
        }
        ShareImageAction shareImageAction = new ShareImageAction(Platform.WEIXIN);
        if (arrayList != null && !arrayList.isEmpty()) {
            shareImageAction.withUri(arrayList.get(0));
        }
        shareImageAction.isCirCle(true);
        EtaoShare.getInstance().exec(shareImageAction);
    }

    public void immediateShareToQq(@Nullable ArrayList<Uri> arrayList) {
        if (!ShareUtils.isQqInstalled(this.context)) {
            ToastUtil.showToast(this.context, (int) R.string.qq_not_installed);
            return;
        }
        ShareMultipleImageAction shareMultipleImageAction = new ShareMultipleImageAction(Platform.QQ);
        if (arrayList != null) {
            shareMultipleImageAction.withUri(arrayList);
        }
        shareMultipleImageAction.isCirCle(false);
        EtaoShare.getInstance().exec(shareMultipleImageAction);
    }

    public void shareToQQ() {
        if (!ShareUtils.openQq(this.context)) {
            ToastUtil.showToast(this.context, (int) R.string.qq_not_installed);
        }
    }

    public void shareToWeibo(String str, ArrayList<Uri> arrayList) {
        if (!ShareUtils.shareWeibo(this.context, str, arrayList)) {
            ToastUtil.showToast(this.context, (int) R.string.weibo_not_installed);
        }
    }
}
