package com.alimama.union.app.infrastructure.image.request;

import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.io.File;

public class UniversalImageRequestCreator implements TaoImageRequestCreator {
    private static final long MAX_IMAGE_CACHE_AGE = 432000;

    public UniversalImageRequestCreator(Context context, File file) {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).threadPriority(3).tasksProcessingOrder(QueueProcessingType.LIFO).diskCache(new LimitedAgeDiscCache(file, (File) null, new HashCodeFileNameGenerator(), MAX_IMAGE_CACHE_AGE)).writeDebugLogs().build());
    }

    public TaoImageRequest createImageRequest(String str, int i) {
        return new UniversalImageRequest(ImageLoader.getInstance(), str, i);
    }
}
