package com.uc.webview.export.extension;

import android.support.v4.media.session.PlaybackStateCompat;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: U4Source */
final class g implements ValueCallback<Object[]> {
    g() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Object[] objArr = (Object[]) obj;
        switch (((Integer) objArr[0]).intValue()) {
            case 1:
                SDKFactory.a((Long) 1024L);
                return;
            case 2:
                SDKFactory.a((Long) 2048L);
                return;
            case 3:
                SDKFactory.a(Long.valueOf(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM));
                return;
            case 4:
                SDKFactory.a(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE));
                return;
            case 5:
                SDKFactory.a(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH));
                return;
            case 6:
                SDKFactory.q.put(IWaStat.VIDEO_ERROR_CODE_UPDATE_CHECK_REQUEST, (Integer) objArr[1]);
                return;
            case 7:
                SDKFactory.q.put(IWaStat.VIDEO_ERROR_CODE_DOWNLOAD, (Integer) objArr[1]);
                return;
            case 8:
                SDKFactory.q.put(IWaStat.VIDEO_ERROR_CODE_VERIFY, (Integer) objArr[1]);
                return;
            case 9:
                SDKFactory.q.put(IWaStat.VIDEO_ERROR_CODE_UNZIP, (Integer) objArr[1]);
                return;
            default:
                return;
        }
    }
}
