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

public class WeiboShare implements SocialShare {
    public static final String CLS = "com.sina.weibo.composerinde.ComposerDispatchActivity";
    public static final String PKG = "com.sina.weibo";
    private final Context activityContext;
    private final IImageDownloader imageDownloader;

    public WeiboShare(Context context, IImageDownloader iImageDownloader) {
        this.activityContext = context;
        this.imageDownloader = iImageDownloader;
    }

    public void doShare(ShareObj shareObj, final String str, List<String> list, final String str2, final SocialShare.ShareCallback shareCallback) {
        this.imageDownloader.batchDownload(list, new IImageDownloader.BatchImageDownloadCallback() {
            public void onProgressUpdate(List<Uri> list, List<String> list2) {
            }

            public void onSuccess(List<Uri> list) {
                WeiboShare.this.processImageDownloadSuccess(str, str2, list);
                shareCallback.onSuccess();
            }

            public void onFailure(String str) {
                shareCallback.onFailure(SocialShare.ErrorCode.IMAGE_PROCESS_ERROR);
            }
        });
    }

    /* access modifiers changed from: private */
    public void processImageDownloadSuccess(String str, String str2, List<Uri> list) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.STREAM", (ArrayList) list);
        intent.setComponent(new ComponentName(PKG, CLS));
        intent.addFlags(134217728);
        intent.addFlags(268435456);
        this.activityContext.startActivity(intent);
    }
}
