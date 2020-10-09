package com.taobao.phenix.intf;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public abstract class AbsPhenixCreator {
    public AbsPhenixCreator error(int i) {
        return this;
    }

    public AbsPhenixCreator error(Drawable drawable) {
        return this;
    }

    public abstract PhenixTicket fetch();

    public abstract PhenixTicket into(ImageView imageView);

    public AbsPhenixCreator onlyCache() {
        return this;
    }

    public AbsPhenixCreator placeholder(int i) {
        return this;
    }

    public AbsPhenixCreator placeholder(Drawable drawable) {
        return this;
    }

    public String url() {
        return "";
    }
}
