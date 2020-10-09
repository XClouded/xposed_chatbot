package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;

public class UMRefContext {
    private final String linkId;

    UMRefContext(@NonNull String str) {
        this.linkId = str;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public String getLinkId() {
        return this.linkId;
    }

    public String toString() {
        return this.linkId;
    }
}
