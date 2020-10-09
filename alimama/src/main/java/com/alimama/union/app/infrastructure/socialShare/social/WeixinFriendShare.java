package com.alimama.union.app.infrastructure.socialShare.social;

import android.content.Context;
import android.net.Uri;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.alimama.union.app.infrastructure.socialShare.social.SocialShare;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinFriendShare implements SocialShare {
    public static final String CLS = "com.tencent.mm.ui.tools.ShareImgUI";
    public static final String PKG = "com.tencent.mm";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) WeixinFriendShare.class);
    private final Context activityContext;
    private final IImageDownloader imageDownloader;

    public WeixinFriendShare(Context context, IImageDownloader iImageDownloader) {
        this.activityContext = context;
        this.imageDownloader = iImageDownloader;
    }

    public void doShare(final ShareObj shareObj, String str, List<String> list, String str2, final SocialShare.ShareCallback shareCallback) {
        this.imageDownloader.batchDownload(list, new IImageDownloader.BatchImageDownloadCallback() {
            public void onProgressUpdate(List<Uri> list, List<String> list2) {
            }

            public void onSuccess(List<Uri> list) {
                WeixinFriendShare.this.processImageDownloadSuccess(shareObj, list);
                shareCallback.onSuccess();
            }

            public void onFailure(String str) {
                shareCallback.onFailure(SocialShare.ErrorCode.IMAGE_PROCESS_ERROR);
            }
        });
    }

    /* access modifiers changed from: private */
    public void processImageDownloadSuccess(ShareObj shareObj, List<Uri> list) {
        ShareUtils.processShareImages(this.activityContext, shareObj, list);
        openWeixin();
    }

    private void openWeixin() {
        try {
            this.activityContext.startActivity(this.activityContext.getPackageManager().getLaunchIntentForPackage("com.tencent.mm"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
