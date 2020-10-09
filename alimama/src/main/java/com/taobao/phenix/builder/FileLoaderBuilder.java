package com.taobao.phenix.builder;

import com.taobao.phenix.loader.file.DefaultFileLoader;
import com.taobao.phenix.loader.file.FileLoader;
import com.taobao.tcommon.core.Preconditions;

public class FileLoaderBuilder implements Builder<FileLoader> {
    private FileLoader mFileLoader;
    private boolean mHaveBuilt;

    public FileLoaderBuilder with(FileLoader fileLoader) {
        Preconditions.checkState(!this.mHaveBuilt, "FileLoaderBuilder has been built, not allow with() now");
        this.mFileLoader = fileLoader;
        return this;
    }

    public synchronized FileLoader build() {
        if (this.mHaveBuilt) {
            return this.mFileLoader;
        }
        this.mHaveBuilt = true;
        if (this.mFileLoader == null) {
            this.mFileLoader = new DefaultFileLoader();
        }
        return this.mFileLoader;
    }
}
