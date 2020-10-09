package com.alimama.union.app.infrastructure.image.request;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import androidx.annotation.MainThread;
import com.alimama.moon.App;
import com.alimama.moon.R;
import java.io.File;

public class TaoImageLoader {
    private static final String IMAGE_CACHE_DIR = "image";
    private static TaoImageRequestCreator imageRequesterCreator;

    @MainThread
    public static void init(Context context) {
        imageRequesterCreator = createImageRequestLoader(context);
    }

    @MainThread
    public static TaoImageRequest load(String str) {
        if (imageRequesterCreator == null) {
            init(App.sApplication);
        }
        if (TextUtils.isEmpty(str)) {
            return imageRequesterCreator.createImageRequest((String) null, R.drawable.img_loading_bg);
        }
        Uri parse = Uri.parse(str);
        if (TextUtils.isEmpty(parse.getScheme())) {
            parse = parse.buildUpon().scheme("https").build();
        }
        return imageRequesterCreator.createImageRequest(parse.toString(), 0);
    }

    private static TaoImageRequestCreator createImageRequestLoader(Context context) {
        File file;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            file = new File(context.getExternalCacheDir(), "image");
        } else {
            file = new File(context.getCacheDir(), "image");
        }
        return new UniversalImageRequestCreator(App.sApplication, file);
    }
}
