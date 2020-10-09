package com.alimama.union.app.infrastructure.image.piccollage;

import android.graphics.Bitmap;
import com.alimama.union.app.infrastructure.socialShare.ShareObj;

public interface ICollage {
    Bitmap collage(ShareObj shareObj, Bitmap bitmap, Bitmap bitmap2) throws Exception;
}
