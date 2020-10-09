package com.alimama.unionwl.base.etaodrawee;

import android.net.Uri;
import androidx.annotation.Nullable;

public interface AImageViewBasicAction {
    void setAnyImageURI(Uri uri);

    void setAnyImageURI(Uri uri, @Nullable Object obj);

    void setRoundedCorners(float f);

    void setRoundedCorners(float f, float f2, float f3, float f4);
}
