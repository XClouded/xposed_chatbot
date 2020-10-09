package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.io.File;
import java.io.FileFilter;

final class StorageManager {
    final Context mContext;
    final String mProcessName;
    final File mProcessTombstoneDir;
    final String mProcessTombstoneDirPath;
    final File mTombstoneDir;
    final String mTombstoneDirPath;

    StorageManager(Context context, String str) {
        this.mContext = context;
        this.mTombstoneDir = this.mContext.getDir("tombstone", 0);
        this.mTombstoneDirPath = this.mTombstoneDir.getAbsolutePath();
        this.mProcessTombstoneDirPath = this.mTombstoneDirPath + File.separator + str;
        this.mProcessTombstoneDir = new File(this.mProcessTombstoneDirPath);
        this.mProcessName = str;
        if (this.mProcessTombstoneDir.exists() && this.mProcessTombstoneDir.isFile()) {
            this.mProcessTombstoneDir.delete();
        }
        this.mProcessTombstoneDir.mkdirs();
    }

    StorageManager(Context context, String str, String str2) {
        this.mContext = context;
        this.mTombstoneDirPath = str;
        this.mTombstoneDir = new File(this.mTombstoneDirPath);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(File.separator);
        sb.append(StringUtils.isNotBlank(str2) ? str2 : "DEFAULT");
        this.mProcessTombstoneDirPath = sb.toString();
        this.mProcessTombstoneDir = new File(this.mProcessTombstoneDirPath);
        if (this.mProcessTombstoneDir.exists() && this.mProcessTombstoneDir.isFile()) {
            this.mProcessTombstoneDir.delete();
        }
        this.mProcessTombstoneDir.mkdirs();
        this.mProcessName = str2;
    }

    public File getProcessTombstoneFile(String str) {
        if (StringUtils.isBlank(str) || str.contains(File.separator)) {
            throw new IllegalArgumentException("file name can't not empty or contains " + File.separator);
        }
        return new File(this.mProcessTombstoneDirPath + File.separator + str);
    }

    public File[] listProcessTombstoneFiles(FileFilter fileFilter) {
        return this.mProcessTombstoneDir.listFiles(fileFilter);
    }
}
