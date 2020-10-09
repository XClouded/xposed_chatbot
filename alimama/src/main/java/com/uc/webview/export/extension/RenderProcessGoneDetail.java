package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: U4Source */
public abstract class RenderProcessGoneDetail {
    public abstract boolean didCrash();

    public abstract int rendererPriorityAtExit();
}
