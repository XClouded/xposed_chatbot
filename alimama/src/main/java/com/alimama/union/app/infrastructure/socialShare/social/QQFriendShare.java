package com.alimama.union.app.infrastructure.socialShare.social;

import android.content.Context;
import android.net.Uri;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.alimama.union.app.infrastructure.socialShare.social.SocialShare;
import java.util.List;

public class QQFriendShare implements SocialShare {
    public static final String CLS = "com.tencent.mobileqq.activity.JumpActivity";
    public static final String PKG = "com.tencent.mobileqq";
    /* access modifiers changed from: private */
    public final Context activityContext;
    private final IImageDownloader imageDownloader;

    public QQFriendShare(Context context, IImageDownloader iImageDownloader) {
        this.activityContext = context;
        this.imageDownloader = iImageDownloader;
    }

    public void doShare(final ShareObj shareObj, String str, List<String> list, String str2, final SocialShare.ShareCallback shareCallback) {
        this.imageDownloader.batchDownload(list, new IImageDownloader.BatchImageDownloadCallback() {
            public void onProgressUpdate(List<Uri> list, List<String> list2) {
            }

            public void onSuccess(List<Uri> list) {
                ShareUtils.processShareImages(QQFriendShare.this.activityContext, shareObj, list);
                QQFriendShare.this.openQQ();
                shareCallback.onSuccess();
            }

            public void onFailure(String str) {
                shareCallback.onFailure(SocialShare.ErrorCode.IMAGE_PROCESS_ERROR);
            }
        });
    }

    /* access modifiers changed from: private */
    public void openQQ() {
        try {
            this.activityContext.startActivity(this.activityContext.getPackageManager().getLaunchIntentForPackage(PKG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
