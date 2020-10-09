package com.taobao.downloader.download.impl2;

import android.os.Environment;
import android.text.TextUtils;
import com.taobao.downloader.Configuration;
import com.taobao.downloader.download.protocol.DLConfig;
import com.taobao.downloader.request.task.SingleTask;
import com.taobao.downloader.util.FileUtil;
import com.taobao.downloader.util.Md5Util;
import com.taobao.downloader.util.Switcher;
import com.taobao.tao.log.TLogConstant;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

public class InputContext {
    public File downloadFile;
    public DLConfig mConfig;
    public SingleTask mTask;
    public File tempFile;
    public URL url;

    public InputContext(SingleTask singleTask) {
        this.mTask = singleTask;
        this.mConfig = new DLConfig(singleTask);
    }

    public void prepareDownload() throws MalformedURLException {
        if (this.url == null) {
            this.url = new URL(this.mTask.item.url);
            this.downloadFile = new File(this.mTask.storeDir, TextUtils.isEmpty(this.mTask.item.name) ? new File(this.url.getFile()).getName() : this.mTask.item.name);
            if (Switcher.useUniformTmpDir()) {
                String storePath = FileUtil.getStorePath(Configuration.sContext, TLogConstant.RUBBISH_DIR, !this.mTask.storeDir.startsWith(Environment.getDataDirectory().getPath()));
                this.tempFile = new File(storePath, Md5Util.getTextMd5(this.mTask.item.url + this.mTask.storeDir));
            } else {
                this.tempFile = new File(this.mTask.storeDir, Md5Util.getTextMd5(this.mTask.item.url));
                if (!this.tempFile.getParentFile().exists()) {
                    this.tempFile.getParentFile().mkdirs();
                }
            }
            if (!this.tempFile.getParentFile().canWrite()) {
                this.tempFile.getParentFile().setWritable(true);
            }
            if (!this.mTask.param.useCache && TextUtils.isEmpty(this.mTask.item.md5)) {
                this.downloadFile.delete();
                this.tempFile.delete();
            }
        }
    }

    public boolean hitFileCache() {
        return this.downloadFile.exists() && (0 == this.mTask.item.size || this.mTask.item.size == this.downloadFile.length()) && Md5Util.isMd5Same(this.mTask.item.md5, this.downloadFile.getAbsolutePath());
    }

    public boolean hitTmpCache() {
        if ((0 == this.mTask.item.size && TextUtils.isEmpty(this.mTask.item.md5)) || !this.tempFile.exists()) {
            return false;
        }
        if ((0 == this.mTask.item.size || this.mTask.item.size == this.tempFile.length()) && Md5Util.isMd5Same(this.mTask.item.md5, this.tempFile.getAbsolutePath())) {
            return true;
        }
        return false;
    }

    public long getPreviousFileSize() {
        if (!this.tempFile.exists()) {
            return 0;
        }
        long length = this.tempFile.length();
        if (0 == this.mTask.item.size || length < this.mTask.item.size) {
            return length;
        }
        this.tempFile.delete();
        return 0;
    }

    public RandomAccessFile getRandomAccessFile() throws FileNotFoundException {
        return new RandomAccessFile(this.tempFile, "rw");
    }

    public boolean isHttpStatusCodeOk(long j, int i) {
        if (200 != i && 206 != i) {
            return false;
        }
        if (j <= 0) {
            return true;
        }
        if (206 == i) {
            j += this.tempFile.length();
        } else if (200 != i) {
            j = 0;
        }
        if (j != 0 && this.mTask.item.size != 0 && j != this.mTask.item.size) {
            return false;
        }
        if (0 != this.mTask.item.size) {
            return true;
        }
        this.mTask.item.size = j;
        return true;
    }

    public int getFileErrorCode() {
        if (0 == this.mTask.item.size || this.mTask.item.size == this.tempFile.length()) {
            return !Md5Util.isMd5Same(this.mTask.item.md5, this.tempFile.getAbsolutePath()) ? -15 : -14;
        }
        return -18;
    }
}
