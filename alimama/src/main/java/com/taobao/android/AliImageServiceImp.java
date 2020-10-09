package com.taobao.android;

import android.content.Context;
import android.view.View;
import com.taobao.phenix.intf.Phenix;

public class AliImageServiceImp implements AliImageServiceInterface {
    private static final AliImageServiceImp sInstance = new AliImageServiceImp();

    public static AliImageServiceImp getInstance() {
        return sInstance;
    }

    public AliImageInterface getAliImageInterface(Context context) {
        return new PhenixAdapter(Phenix.instance().with(context));
    }

    public <T extends View & AliUrlImageViewInterface> T newUrlImageView(Context context) {
        return new AliUrlImageView(context);
    }
}
