package com.taobao.android;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface AliImageCreatorInterface {
    AliImageCreatorInterface error(int i);

    AliImageCreatorInterface error(Drawable drawable);

    AliImageCreatorInterface failListener(AliImageListener<AliImageFailEvent> aliImageListener);

    AliImageTicketInterface fetch();

    AliImageTicketInterface into(ImageView imageView);

    AliImageTicketInterface into(ImageView imageView, float f);

    AliImageTicketInterface into(ImageView imageView, int i, int i2);

    AliImageCreatorInterface placeholder(int i);

    AliImageCreatorInterface placeholder(Drawable drawable);

    AliImageCreatorInterface succListener(AliImageListener<AliImageSuccEvent> aliImageListener);
}
