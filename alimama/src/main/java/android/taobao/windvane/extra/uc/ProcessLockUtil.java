package android.taobao.windvane.extra.uc;

import android.taobao.windvane.util.TaoLog;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ProcessLockUtil {
    private static final String TAG = "ProcessLockUtil";
    private FileLock cacheLock;
    private FileChannel lockChannel;
    private File lockFile;
    private RandomAccessFile lockRaf;

    public ProcessLockUtil(String str) {
        this.lockFile = new File(str);
    }

    public ProcessLockUtil(File file) {
        this.lockFile = file;
    }

    public void lock() {
        try {
            this.lockRaf = new RandomAccessFile(this.lockFile, "rw");
            if (this.lockRaf == null || this.lockFile == null) {
                TaoLog.e(TAG, "lock error lockRaf = " + this.lockRaf + " lockFile = " + this.lockFile);
                return;
            }
            this.lockChannel = this.lockRaf.getChannel();
            TaoLog.d(TAG, "Blocking on lock " + this.lockFile.getPath());
            try {
                this.cacheLock = this.lockChannel.lock();
                TaoLog.d(TAG, this.lockFile.getPath() + " locked");
            } catch (IOException e) {
                TaoLog.e(TAG, "lock error ", e, new Object[0]);
            }
        } catch (FileNotFoundException e2) {
            TaoLog.e(TAG, "ProcessLock error", e2, new Object[0]);
        }
    }

    public void unlock() {
        if (this.cacheLock != null) {
            try {
                this.cacheLock.release();
            } catch (IOException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to release lock on ");
                sb.append(this.lockFile != null ? this.lockFile.getPath() : "");
                TaoLog.e(TAG, sb.toString());
            }
        }
        if (this.lockChannel != null) {
            closeQuietly(this.lockChannel);
        }
        closeQuietly(this.lockRaf);
        if (this.lockFile != null) {
            TaoLog.d(TAG, this.lockFile.getPath() + " unlocked");
        }
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                TaoLog.e(TAG, "Failed to close resource", e, new Object[0]);
            }
        }
    }
}
