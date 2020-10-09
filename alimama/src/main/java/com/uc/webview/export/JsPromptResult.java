package com.uc.webview.export;

import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: U4Source */
public interface JsPromptResult extends JsResult {
    void cancel();

    void confirm();

    void confirm(String str);
}
