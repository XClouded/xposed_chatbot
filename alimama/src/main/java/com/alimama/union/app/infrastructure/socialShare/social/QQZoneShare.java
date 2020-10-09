package com.alimama.union.app.infrastructure.socialShare.social;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;
import com.alimama.union.app.infrastructure.socialShare.social.SocialShare;
import java.util.ArrayList;
import java.util.List;

public class QQZoneShare implements SocialShare {
    public static final String CLS = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";
    public static final String PKG = "com.qzone";
    private final Context activityContext;
    private final IImageDownloader imageDownloader;

    public QQZoneShare(Context context, IImageDownloader iImageDownloader) {
        this.activityContext = context;
        this.imageDownloader = iImageDownloader;
    }

    public void doShare(final ShareObj shareObj, final String str, List<String> list, String str2, final SocialShare.ShareCallback shareCallback) {
        this.imageDownloader.batchDownload(list, new IImageDownloader.BatchImageDownloadCallback() {
            public void onProgressUpdate(List<Uri> list, List<String> list2) {
            }

            public void onSuccess(List<Uri> list) {
                QQZoneShare.this.processImageDownloadSuccess(shareObj, str, list);
                shareCallback.onSuccess();
            }

            public void onFailure(String str) {
                shareCallback.onFailure(SocialShare.ErrorCode.IMAGE_PROCESS_ERROR);
            }
        });
    }

    /* access modifiers changed from: private */
    public void processImageDownloadSuccess(ShareObj shareObj, String str, List<Uri> list) {
        ArrayList<Uri> processShareImages = ShareUtils.processShareImages(this.activityContext, shareObj, list);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.STREAM", processShareImages);
        intent.setComponent(new ComponentName(PKG, CLS));
        intent.addFlags(134217728);
        intent.addFlags(268435456);
        this.activityContext.startActivity(intent);
    }
}
