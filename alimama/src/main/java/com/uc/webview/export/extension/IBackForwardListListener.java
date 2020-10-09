package com.uc.webview.export.extension;

import com.uc.webview.export.WebHistoryItem;
import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: U4Source */
public interface IBackForwardListListener {
    void onIndexChanged(WebHistoryItem webHistoryItem, int i);

    void onNewHistoryItem(WebHistoryItem webHistoryItem);
}
