package com.alimama.union.app.infrastructure.image.capture;

import android.content.Context;
import java.io.File;

public interface Capture {

    public interface CaptureCallback {
        void onSuccess(File file);
    }

    void captureHtml(Context context, String str, int i, int i2, CaptureCallback captureCallback);
}
