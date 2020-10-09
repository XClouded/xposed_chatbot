package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.content.Intent;

public class PictureBrowserManager {
    public static PictureBrowserManager sInstance;

    private PictureBrowserManager() {
    }

    public static PictureBrowserManager getInstance() {
        if (sInstance == null) {
            sInstance = new PictureBrowserManager();
        }
        return sInstance;
    }

    public void goToPictureManager(Context context, String str) {
        Intent intent = new Intent(context, PictureBrowserActivity.class);
        intent.putExtra(PictureBrowserActivity.PIC_BROWSER_JSON_DATA_KEY, str);
        context.startActivity(intent);
    }
}
