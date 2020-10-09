package com.uc.webview.export.internal.android;

import com.uc.webview.export.WebBackForwardList;
import com.uc.webview.export.WebHistoryItem;

/* compiled from: U4Source */
final class h extends WebBackForwardList {

    /* compiled from: U4Source */
    class a extends WebHistoryItem {
        a(android.webkit.WebHistoryItem webHistoryItem) {
            this.mItem = webHistoryItem;
        }
    }

    h(android.webkit.WebBackForwardList webBackForwardList) {
        this.mList = webBackForwardList;
    }

    /* access modifiers changed from: protected */
    public final WebHistoryItem createItem(android.webkit.WebHistoryItem webHistoryItem) {
        return new a(webHistoryItem);
    }
}
