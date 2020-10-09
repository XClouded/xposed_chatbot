package com.uc.webview.export.extension;

import android.support.v4.media.session.PlaybackStateCompat;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class d implements ValueCallback<UpdateTask> {
    d() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_UNZIP);
        SDKFactory.a(Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_URI));
    }
}
