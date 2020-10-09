package mtopsdk.mtop.upload.service;

import anetwork.channel.IBodyHandler;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import mtopsdk.common.util.TBSdkLog;

class FileStreamUploadBodyHandlerImpl implements IBodyHandler {
    private static final String TAG = "mtopsdk.FileStreamUploadBodyHandlerImpl";
    private BufferedInputStream bis = null;
    private long fileSize = 0;
    private InputStream fileStream;
    private boolean isCompleted = false;
    private long offset = 0;
    private long patchSize = 0;
    private int postedLength = 0;

    public FileStreamUploadBodyHandlerImpl(InputStream inputStream, long j, long j2, long j3) {
        this.fileStream = inputStream;
        this.fileSize = j;
        this.offset = j2;
        this.patchSize = j3;
    }

    /* JADX INFO: finally extract failed */
    public int read(byte[] bArr) {
        int i = 0;
        if (bArr == null || bArr.length == 0 || this.fileStream == null) {
            TBSdkLog.e(TAG, "[read(byte[] buffer)]parameter buffer or fileStream is null");
            this.isCompleted = true;
            return 0;
        } else if (((long) this.postedLength) >= this.patchSize) {
            this.isCompleted = true;
            return 0;
        } else {
            try {
                if (this.offset < this.fileSize) {
                    if (((long) this.postedLength) < this.fileSize) {
                        if (this.bis == null) {
                            this.bis = new BufferedInputStream(this.fileStream);
                        }
                        int read = this.bis.read(bArr);
                        if (read != -1) {
                            if (((long) (this.postedLength + read)) > this.patchSize) {
                                read = (int) (this.patchSize - ((long) this.postedLength));
                            }
                            i = read;
                            this.postedLength += i;
                            this.offset += (long) i;
                            if (((long) this.postedLength) >= this.patchSize || this.offset >= this.fileSize) {
                                this.isCompleted = true;
                            }
                            if (!this.isCompleted) {
                                this.bis.mark((int) this.fileSize);
                                this.bis.reset();
                            }
                        }
                        if (this.bis != null && this.isCompleted) {
                            try {
                                this.bis.close();
                            } catch (IOException e) {
                                TBSdkLog.e(TAG, "close inputStream error", (Throwable) e);
                            }
                        }
                        return i;
                    }
                }
                this.isCompleted = true;
                if (this.bis != null && this.isCompleted) {
                    try {
                        this.bis.close();
                    } catch (IOException e2) {
                        TBSdkLog.e(TAG, "close inputStream error", (Throwable) e2);
                    }
                }
                return 0;
            } catch (Exception e3) {
                TBSdkLog.e(TAG, "[read]write Body error", (Throwable) e3);
                this.isCompleted = true;
                if (this.bis != null && this.isCompleted) {
                    this.bis.close();
                }
            } catch (Throwable th) {
                if (this.bis != null && this.isCompleted) {
                    try {
                        this.bis.close();
                    } catch (IOException e4) {
                        TBSdkLog.e(TAG, "close inputStream error", (Throwable) e4);
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
