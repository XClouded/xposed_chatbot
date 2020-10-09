package com.alibaba.taffy.core.cache;

import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;
import kotlin.UByte;

public class DiskCache implements Closeable {
    private static final int BH_CHECKSUM = 8;
    private static final int BH_KEY = 0;
    private static final int BH_LENGTH = 16;
    private static final int BH_OFFSET = 12;
    private static final int BLOB_HEADER_SIZE = 20;
    private static final int DATA_HEADER_SIZE = 4;
    private static final int IH_ACTIVE_BYTES = 20;
    private static final int IH_ACTIVE_ENTRIES = 16;
    private static final int IH_ACTIVE_REGION = 12;
    private static final int IH_CHECKSUM = 28;
    private static final int IH_MAGIC = 0;
    private static final int IH_MAX_BYTES = 8;
    private static final int IH_MAX_ENTRIES = 4;
    private static final int IH_VERSION = 24;
    private static final int INDEX_HEADER_SIZE = 32;
    private static final int MAGIC_DATA_FILE = -1121680112;
    private static final int MAGIC_INDEX_FILE = -1289277392;
    private static final String TAG = "DiskCache";
    private int mActiveBytes;
    private RandomAccessFile mActiveDataFile;
    private int mActiveEntries;
    private int mActiveHashStart;
    private int mActiveRegion;
    private Adler32 mAdler32;
    private byte[] mBlobHeader;
    private RandomAccessFile mDataFile0;
    private RandomAccessFile mDataFile1;
    private int mFileOffset;
    private RandomAccessFile mInactiveDataFile;
    private int mInactiveHashStart;
    private MappedByteBuffer mIndexBuffer;
    private FileChannel mIndexChannel;
    private RandomAccessFile mIndexFile;
    private byte[] mIndexHeader;
    private LookupRequest mLookupRequest;
    private int mMaxBytes;
    private int mMaxEntries;
    private String mPath;
    private int mSlotOffset;
    private int mVersion;

    public static class LookupRequest {
        public byte[] buffer;
        public long key;
        public int length;
    }

    public DiskCache(String str, int i, int i2, boolean z) throws IOException {
        this(str, i, i2, z, 0);
    }

    public DiskCache(String str, int i, int i2, boolean z, int i3) throws IOException {
        this.mIndexHeader = new byte[32];
        this.mBlobHeader = new byte[20];
        this.mAdler32 = new Adler32();
        this.mLookupRequest = new LookupRequest();
        File file = new File(str);
        if (file.exists() || file.mkdirs()) {
            this.mPath = str;
            this.mIndexFile = new RandomAccessFile(str + ".idx", "rw");
            this.mDataFile0 = new RandomAccessFile(str + ".0", "rw");
            this.mDataFile1 = new RandomAccessFile(str + ".1", "rw");
            this.mVersion = i3;
            if (z || !loadIndex()) {
                resetCache(i, i2);
                if (!loadIndex()) {
                    closeAll();
                    throw new IOException("unable to load index");
                }
                return;
            }
            return;
        }
        throw new IOException("unable to make dirs");
    }

    public void delete() {
        deleteFileSilently(this.mPath + ".idx");
        deleteFileSilently(this.mPath + ".0");
        deleteFileSilently(this.mPath + ".1");
    }

    private static void deleteFileSilently(String str) {
        try {
            new File(str).delete();
        } catch (Throwable unused) {
        }
    }

    public void close() {
        syncAll();
        closeAll();
    }

    private void closeAll() {
        closeSilently(this.mIndexChannel);
        closeSilently(this.mIndexFile);
        closeSilently(this.mDataFile0);
        closeSilently(this.mDataFile1);
    }

    private boolean loadIndex() {
        try {
            this.mIndexFile.seek(0);
            this.mDataFile0.seek(0);
            this.mDataFile1.seek(0);
            byte[] bArr = this.mIndexHeader;
            if (this.mIndexFile.read(bArr) != 32) {
                Log.w(TAG, "cannot read header");
                return false;
            } else if (readInt(bArr, 0) != MAGIC_INDEX_FILE) {
                Log.w(TAG, "cannot read header magic");
                return false;
            } else if (readInt(bArr, 24) != this.mVersion) {
                Log.w(TAG, "version mismatch");
                return false;
            } else {
                this.mMaxEntries = readInt(bArr, 4);
                this.mMaxBytes = readInt(bArr, 8);
                this.mActiveRegion = readInt(bArr, 12);
                this.mActiveEntries = readInt(bArr, 16);
                this.mActiveBytes = readInt(bArr, 20);
                if (checkSum(bArr, 0, 28) != readInt(bArr, 28)) {
                    Log.w(TAG, "header checksum does not match");
                    return false;
                } else if (this.mMaxEntries <= 0) {
                    Log.w(TAG, "invalid max entries");
                    return false;
                } else if (this.mMaxBytes <= 0) {
                    Log.w(TAG, "invalid max bytes");
                    return false;
                } else if (this.mActiveRegion == 0 || this.mActiveRegion == 1) {
                    if (this.mActiveEntries >= 0) {
                        if (this.mActiveEntries <= this.mMaxEntries) {
                            if (this.mActiveBytes >= 4) {
                                if (this.mActiveBytes <= this.mMaxBytes) {
                                    if (this.mIndexFile.length() != ((long) ((this.mMaxEntries * 12 * 2) + 32))) {
                                        Log.w(TAG, "invalid index file length");
                                        return false;
                                    }
                                    byte[] bArr2 = new byte[4];
                                    if (this.mDataFile0.read(bArr2) != 4) {
                                        Log.w(TAG, "cannot read data file magic");
                                        return false;
                                    } else if (readInt(bArr2, 0) != MAGIC_DATA_FILE) {
                                        Log.w(TAG, "invalid data file magic");
                                        return false;
                                    } else if (this.mDataFile1.read(bArr2) != 4) {
                                        Log.w(TAG, "cannot read data file magic");
                                        return false;
                                    } else if (readInt(bArr2, 0) != MAGIC_DATA_FILE) {
                                        Log.w(TAG, "invalid data file magic");
                                        return false;
                                    } else {
                                        this.mIndexChannel = this.mIndexFile.getChannel();
                                        this.mIndexBuffer = this.mIndexChannel.map(FileChannel.MapMode.READ_WRITE, 0, this.mIndexFile.length());
                                        this.mIndexBuffer.order(ByteOrder.LITTLE_ENDIAN);
                                        setActiveVariables();
                                        return true;
                                    }
                                }
                            }
                            Log.w(TAG, "invalid active bytes");
                            return false;
                        }
                    }
                    Log.w(TAG, "invalid active entries");
                    return false;
                } else {
                    Log.w(TAG, "invalid active region");
                    return false;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "loadIndex failed.", e);
            return false;
        }
    }

    private void setActiveVariables() throws IOException {
        this.mActiveDataFile = this.mActiveRegion == 0 ? this.mDataFile0 : this.mDataFile1;
        this.mInactiveDataFile = this.mActiveRegion == 1 ? this.mDataFile0 : this.mDataFile1;
        this.mActiveDataFile.setLength((long) this.mActiveBytes);
        this.mActiveDataFile.seek((long) this.mActiveBytes);
        this.mActiveHashStart = 32;
        this.mInactiveHashStart = 32;
        if (this.mActiveRegion == 0) {
            this.mInactiveHashStart += this.mMaxEntries * 12;
        } else {
            this.mActiveHashStart += this.mMaxEntries * 12;
        }
    }

    private void resetCache(int i, int i2) throws IOException {
        this.mIndexFile.setLength(0);
        this.mIndexFile.setLength((long) ((i * 12 * 2) + 32));
        this.mIndexFile.seek(0);
        byte[] bArr = this.mIndexHeader;
        writeInt(bArr, 0, MAGIC_INDEX_FILE);
        writeInt(bArr, 4, i);
        writeInt(bArr, 8, i2);
        writeInt(bArr, 12, 0);
        writeInt(bArr, 16, 0);
        writeInt(bArr, 20, 4);
        writeInt(bArr, 24, this.mVersion);
        writeInt(bArr, 28, checkSum(bArr, 0, 28));
        this.mIndexFile.write(bArr);
        this.mDataFile0.setLength(0);
        this.mDataFile1.setLength(0);
        this.mDataFile0.seek(0);
        this.mDataFile1.seek(0);
        writeInt(bArr, 0, MAGIC_DATA_FILE);
        this.mDataFile0.write(bArr, 0, 4);
        this.mDataFile1.write(bArr, 0, 4);
    }

    private void flipRegion() throws IOException {
        this.mActiveRegion = 1 - this.mActiveRegion;
        this.mActiveEntries = 0;
        this.mActiveBytes = 4;
        writeInt(this.mIndexHeader, 12, this.mActiveRegion);
        writeInt(this.mIndexHeader, 16, this.mActiveEntries);
        writeInt(this.mIndexHeader, 20, this.mActiveBytes);
        updateIndexHeader();
        setActiveVariables();
        clearHash(this.mActiveHashStart);
        syncIndex();
    }

    private void updateIndexHeader() {
        writeInt(this.mIndexHeader, 28, checkSum(this.mIndexHeader, 0, 28));
        this.mIndexBuffer.position(0);
        this.mIndexBuffer.put(this.mIndexHeader);
    }

    private void clearHash(int i) {
        byte[] bArr = new byte[1024];
        this.mIndexBuffer.position(i);
        int i2 = this.mMaxEntries * 12;
        while (i2 > 0) {
            int min = Math.min(i2, 1024);
            this.mIndexBuffer.put(bArr, 0, min);
            i2 -= min;
        }
    }

    public void insert(long j, byte[] bArr) throws IOException {
        if (bArr.length + 24 <= this.mMaxBytes) {
            if (this.mActiveBytes + 20 + bArr.length > this.mMaxBytes || this.mActiveEntries * 2 >= this.mMaxEntries) {
                flipRegion();
            }
            if (!lookupInternal(j, this.mActiveHashStart)) {
                this.mActiveEntries++;
                writeInt(this.mIndexHeader, 16, this.mActiveEntries);
            }
            insertInternal(j, bArr, bArr.length);
            updateIndexHeader();
            return;
        }
        throw new RuntimeException("blob is too large!");
    }

    private void insertInternal(long j, byte[] bArr, int i) throws IOException {
        byte[] bArr2 = this.mBlobHeader;
        int checkSum = checkSum(bArr);
        writeLong(bArr2, 0, j);
        writeInt(bArr2, 8, checkSum);
        writeInt(bArr2, 12, this.mActiveBytes);
        writeInt(bArr2, 16, i);
        this.mActiveDataFile.write(bArr2);
        this.mActiveDataFile.write(bArr, 0, i);
        this.mIndexBuffer.putLong(this.mSlotOffset, j);
        this.mIndexBuffer.putInt(this.mSlotOffset + 8, this.mActiveBytes);
        this.mActiveBytes += i + 20;
        writeInt(this.mIndexHeader, 20, this.mActiveBytes);
    }

    public byte[] lookup(long j) throws IOException {
        this.mLookupRequest.key = j;
        this.mLookupRequest.buffer = null;
        if (lookup(this.mLookupRequest)) {
            return this.mLookupRequest.buffer;
        }
        return null;
    }

    public boolean lookup(LookupRequest lookupRequest) throws IOException {
        if (lookupInternal(lookupRequest.key, this.mActiveHashStart) && getBlob(this.mActiveDataFile, this.mFileOffset, lookupRequest)) {
            return true;
        }
        int i = this.mSlotOffset;
        if (!lookupInternal(lookupRequest.key, this.mInactiveHashStart) || !getBlob(this.mInactiveDataFile, this.mFileOffset, lookupRequest)) {
            return false;
        }
        if (this.mActiveBytes + 20 + lookupRequest.length > this.mMaxBytes || this.mActiveEntries * 2 >= this.mMaxEntries) {
            return true;
        }
        this.mSlotOffset = i;
        try {
            insertInternal(lookupRequest.key, lookupRequest.buffer, lookupRequest.length);
            this.mActiveEntries++;
            writeInt(this.mIndexHeader, 16, this.mActiveEntries);
            updateIndexHeader();
        } catch (Throwable unused) {
            Log.e(TAG, "cannot copy over");
        }
        return true;
    }

    private boolean getBlob(RandomAccessFile randomAccessFile, int i, LookupRequest lookupRequest) throws IOException {
        byte[] bArr = this.mBlobHeader;
        long filePointer = randomAccessFile.getFilePointer();
        try {
            randomAccessFile.seek((long) i);
            if (randomAccessFile.read(bArr) != 20) {
                Log.w(TAG, "cannot read blob header");
                return false;
            }
            long readLong = readLong(bArr, 0);
            if (readLong != lookupRequest.key) {
                String str = TAG;
                Log.w(str, "blob key does not match: " + readLong);
                randomAccessFile.seek(filePointer);
                return false;
            }
            int readInt = readInt(bArr, 8);
            int readInt2 = readInt(bArr, 12);
            if (readInt2 != i) {
                String str2 = TAG;
                Log.w(str2, "blob offset does not match: " + readInt2);
                randomAccessFile.seek(filePointer);
                return false;
            }
            int readInt3 = readInt(bArr, 16);
            if (readInt3 >= 0) {
                if (readInt3 <= (this.mMaxBytes - i) - 20) {
                    if (lookupRequest.buffer == null || lookupRequest.buffer.length < readInt3) {
                        lookupRequest.buffer = new byte[readInt3];
                    }
                    byte[] bArr2 = lookupRequest.buffer;
                    lookupRequest.length = readInt3;
                    if (randomAccessFile.read(bArr2, 0, readInt3) != readInt3) {
                        Log.w(TAG, "cannot read blob data");
                        randomAccessFile.seek(filePointer);
                        return false;
                    } else if (checkSum(bArr2, 0, readInt3) != readInt) {
                        String str3 = TAG;
                        Log.w(str3, "blob checksum does not match: " + readInt);
                        randomAccessFile.seek(filePointer);
                        return false;
                    } else {
                        randomAccessFile.seek(filePointer);
                        return true;
                    }
                }
            }
            String str4 = TAG;
            Log.w(str4, "invalid blob length: " + readInt3);
            randomAccessFile.seek(filePointer);
            return false;
        } catch (Throwable th) {
            Log.e(TAG, "getBlob failed.", th);
            return false;
        } finally {
            randomAccessFile.seek(filePointer);
        }
    }

    private boolean lookupInternal(long j, int i) {
        int i2 = (int) (j % ((long) this.mMaxEntries));
        if (i2 < 0) {
            i2 += this.mMaxEntries;
        }
        int i3 = i2;
        while (true) {
            int i4 = (i3 * 12) + i;
            long j2 = this.mIndexBuffer.getLong(i4);
            int i5 = this.mIndexBuffer.getInt(i4 + 8);
            if (i5 == 0) {
                this.mSlotOffset = i4;
                return false;
            } else if (j2 == j) {
                this.mSlotOffset = i4;
                this.mFileOffset = i5;
                return true;
            } else {
                i3++;
                if (i3 >= this.mMaxEntries) {
                    i3 = 0;
                }
                if (i3 == i2) {
                    Log.w(TAG, "corrupted index: clear the slot.");
                    this.mIndexBuffer.putInt((i3 * 12) + i + 8, 0);
                }
            }
        }
    }

    public void syncIndex() {
        try {
            this.mIndexBuffer.force();
        } catch (Throwable th) {
            Log.w(TAG, "sync index failed", th);
        }
    }

    public void syncAll() {
        syncIndex();
        try {
            this.mDataFile0.getFD().sync();
        } catch (Throwable th) {
            Log.w(TAG, "sync data file 0 failed", th);
        }
        try {
            this.mDataFile1.getFD().sync();
        } catch (Throwable th2) {
            Log.w(TAG, "sync data file 1 failed", th2);
        }
    }

    /* access modifiers changed from: package-private */
    public int getActiveCount() {
        int i = 0;
        for (int i2 = 0; i2 < this.mMaxEntries; i2++) {
            if (this.mIndexBuffer.getInt(this.mActiveHashStart + (i2 * 12) + 8) != 0) {
                i++;
            }
        }
        if (i == this.mActiveEntries) {
            return i;
        }
        String str = TAG;
        Log.e(str, "wrong active count: " + this.mActiveEntries + " vs " + i);
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int checkSum(byte[] bArr) {
        this.mAdler32.reset();
        this.mAdler32.update(bArr);
        return (int) this.mAdler32.getValue();
    }

    /* access modifiers changed from: package-private */
    public int checkSum(byte[] bArr, int i, int i2) {
        this.mAdler32.reset();
        this.mAdler32.update(bArr, i, i2);
        return (int) this.mAdler32.getValue();
    }

    static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    static int readInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & UByte.MAX_VALUE) << 24) | (bArr[i] & UByte.MAX_VALUE) | ((bArr[i + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i + 2] & UByte.MAX_VALUE) << 16);
    }

    static long readLong(byte[] bArr, int i) {
        long j = (long) (bArr[i + 7] & UByte.MAX_VALUE);
        for (int i2 = 6; i2 >= 0; i2--) {
            j = (j << 8) | ((long) (bArr[i + i2] & UByte.MAX_VALUE));
        }
        return j;
    }

    static void writeInt(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i + i3] = (byte) (i2 & 255);
            i2 >>= 8;
        }
    }

    static void writeLong(byte[] bArr, int i, long j) {
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i + i2] = (byte) ((int) (255 & j));
            j >>= 8;
        }
    }
}
