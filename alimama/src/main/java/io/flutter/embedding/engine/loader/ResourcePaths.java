package io.flutter.embedding.engine.loader;

import android.content.Context;
import java.io.File;
import java.io.IOException;

class ResourcePaths {
    public static final String TEMPORARY_RESOURCE_PREFIX = ".org.chromium.Chromium.";

    ResourcePaths() {
    }

    public static File createTempFile(Context context, String str) throws IOException {
        return File.createTempFile(TEMPORARY_RESOURCE_PREFIX, "_" + str, context.getCacheDir());
    }
}
