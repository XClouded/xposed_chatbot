package com.taobao.android;

import android.content.Context;
import android.view.View;

public interface AliImageServiceInterface {
    AliImageInterface getAliImageInterface(Context context);

    <T extends View & AliUrlImageViewInterface> T newUrlImageView(Context context);
}
