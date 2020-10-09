package com.alimama.union.app.infrastructure.image.save;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;

public interface IImageSaver {
    File getBaseFileDir();

    File saveBitmap(Bitmap bitmap, String str);

    File saveBitmapAs(Bitmap bitmap, String str);

    File saveBitmapToMedia(Context context, Bitmap bitmap, String str);
}
