package com.uc.webview.export.extension;

import android.support.v4.media.session.PlaybackStateCompat;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class e implements ValueCallback<UpdateTask> {
    e() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_DOWNLOAD);
        SDKFactory.a(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID));
    }
}
