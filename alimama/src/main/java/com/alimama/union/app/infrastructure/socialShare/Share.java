package com.alimama.union.app.infrastructure.socialShare;

import android.content.Context;

public interface Share {

    public interface Callback {
        void onFailure();

        void onSuccess();
    }

    void doShare(Context context, ShareObj shareObj, Callback callback);
}
