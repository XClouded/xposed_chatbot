package com.nostra13.universalimageloader.cache.disc.impl;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import java.io.File;

public class UnlimitedDiscCache extends BaseDiscCache {
    public UnlimitedDiscCache(File file) {
        super(file);
    }

    public UnlimitedDiscCache(File file, File file2) {
        super(file, file2);
    }

    public UnlimitedDiscCache(File file, File file2, FileNameGenerator fileNameGenerator) {
        super(file, file2, fileNameGenerator);
    }
}
