package com.xiaomi.push;

import java.io.File;
import java.io.FileFilter;

final class z implements FileFilter {
    z() {
    }

    public boolean accept(File file) {
        return file.isDirectory();
    }
}
