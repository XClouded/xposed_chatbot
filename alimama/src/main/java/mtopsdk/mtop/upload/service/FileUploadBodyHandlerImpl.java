package mtopsdk.mtop.upload.service;

import anetwork.channel.IBodyHandler;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import mtopsdk.common.util.TBSdkLog;

class FileUploadBodyHandlerImpl implements IBodyHandler {
    private static final String TAG = "mtopsdk.FileUploadBodyHandlerImpl";
    private File file;
    private boolean isCompleted = false;
    private long offset = 0;
    private long patchSize = 0;
    private int postedLength = 0;
    private RandomAccessFile raf = null;

    public FileUploadBodyHandlerImpl(File file2, long j, long j2) {
        this.file = file2;
        this.offset = j;
        this.patchSize = j2;
    }

    /* JADX INFO: finally extract failed */
    public int read(byte[] bArr) {
        int i = 0;
        if (bArr == null || bArr.length == 0 || this.file == null) {
            TBSdkLog.e(TAG, "[read(byte[] buffer)]parameter buffer or file is null");
            this.isCompleted = true;
            return 0;
        } else if (((long) this.postedLength) >= this.patchSize) {
            this.isCompleted = true;
            return 0;
        } else {
            try {
                if (this.raf == null) {
                    this.raf = new RandomAccessFile(this.file, UploadQueueMgr.MSGTYPE_REALTIME);
                }
                long length = this.raf.length();
                if (this.offset < length) {
                    if (((long) this.postedLength) < length) {
                        this.raf.seek(this.offset);
                        int read = this.raf.read(bArr);
                        if (read != -1) {
                            if (((long) (this.postedLength + read)) > this.patchSize) {
                                read = (int) (this.patchSize - ((long) this.postedLength));
                            }
                            i = read;
                            this.postedLength += i;
                            this.offset += (long) i;
                            if (((long) this.postedLength) >= this.patchSize || this.offset >= length) {
                                this.isCompleted = true;
                            }
                        }
                        if (this.raf != null && this.isCompleted) {
                            try {
                                this.raf.close();
                            } catch (IOException e) {
                                TBSdkLog.e(TAG, "close RandomAccessFile error", (Throwable) e);
                            }
                        }
                        return i;
                    }
                }
                this.isCompleted = true;
                if (this.raf != null && this.isCompleted) {
                    try {
                        this.raf.close();
                    } catch (IOException e2) {
                        TBSdkLog.e(TAG, "close RandomAccessFile error", (Throwable) e2);
                    }
                }
                return 0;
            } catch (Exception e3) {
                TBSdkLog.e(TAG, "[read]write Body error", (Throwable) e3);
                this.isCompleted = true;
                if (this.raf != null && this.isCompleted) {
                    this.raf.close();
                }
            } catch (Throwable th) {
                if (this.raf != null && this.isCompleted) {
                    try {
                        this.raf.close();
                    } catch (IOException e4) {
                        TBSdkLog.e(TAG, "close RandomAccessFile error", (Throwable) e4);
                    }
                }
                throw th;
            }
        }
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }
}
