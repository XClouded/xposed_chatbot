package com.taobao.phenix.request;

import com.taobao.pexode.mimetype.MimeType;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.weex.el.parse.Operators;
import java.util.Map;

public class ImageStatistics {
    public static final String KEY_BITMAP_DECODE = "decode";
    public static final String KEY_BITMAP_PROCESS = "bitmapProcess";
    public static final String KEY_BITMAP_SCALE = "scaleTime";
    public static final String KEY_DECODE_WAIT_SIZE = "decodeWaitSize";
    public static final String KEY_MASTER_WAIT_SIZE = "masterWaitSize";
    public static final String KEY_NETWORK_CONNECT = "connect";
    public static final String KEY_NETWORK_DOWNLOAD = "download";
    public static final String KEY_NETWORK_WAIT_SIZE = "networkWaitSize";
    public static final String KEY_READ_DISK_CACHE = "cacheLookup";
    public static final String KEY_READ_LOCAL_FILE = "localFile";
    public static final String KEY_READ_MEMORY_CACHE = "memoryLookup";
    public static final String KEY_SCHEDULE_TIME = "scheduleTime";
    public static final String KEY_TOTAL_TIME = "totalTime";
    public static final String KEY_WAIT_FOR_MAIN = "waitForMain";
    private int mBitmapPoolHitCount;
    private int mBitmapPoolMissCount;
    public String mBizId;
    private Map<String, Integer> mDetailCost;
    private int mDiskCacheHitCount;
    private int mDiskCacheMissCount;
    private int mDiskCachePriority;
    private Map<String, String> mExtras;
    private MimeType mFormat;
    private FromType mFromType;
    public String mFullTraceId;
    private boolean mIsDuplicated;
    public boolean mIsOnlyFullTrack;
    private final boolean mIsRetrying;
    private int mMemCacheHitCount;
    private int mMemCacheMissCount;
    public boolean mReportTTLException;
    public long mReqProcessStart;
    private long mRequestStartTime;
    public long mRspCbDispatch;
    public long mRspCbEnd;
    public long mRspCbStart;
    public long mRspDeflateSize;
    public long mRspProcessStart;
    private int mSize;
    public long mTTLGetTime;
    public long mTTLPutTime;
    private final ImageUriInfo mUriInfo;

    public enum FromType {
        FROM_UNKNOWN(-1),
        FROM_NETWORK(0),
        FROM_MEMORY_CACHE(1),
        FROM_DISK_CACHE(2),
        FROM_LARGE_SCALE(3),
        FROM_LOCAL_FILE(4);
        
        public final int value;

        private FromType(int i) {
            this.value = i;
        }
    }

    public ImageStatistics(ImageUriInfo imageUriInfo, boolean z) {
        this.mFromType = FromType.FROM_UNKNOWN;
        this.mIsOnlyFullTrack = false;
        this.mUriInfo = imageUriInfo;
        this.mIsRetrying = z;
    }

    public ImageStatistics(ImageUriInfo imageUriInfo) {
        this(imageUriInfo, false);
    }

    public long getRequestStartTime() {
        return this.mRequestStartTime;
    }

    public void setRequestStartTime(long j) {
        this.mRequestStartTime = j;
    }

    public FromType getFromType() {
        return this.mFromType;
    }

    public int getSize() {
        return this.mSize;
    }

    public void fromType(FromType fromType) {
        this.mFromType = fromType;
    }

    public void setDiskCachePriority(int i) {
        this.mDiskCachePriority = i;
    }

    public int getDiskCachePriority() {
        return this.mDiskCachePriority;
    }

    public int getDiskCacheHitCount() {
        return this.mDiskCacheHitCount;
    }

    public int getDiskCacheMissCount() {
        return this.mDiskCacheMissCount;
    }

    public int getBitmapPoolHitCount() {
        return this.mBitmapPoolHitCount;
    }

    public int getBitmapPoolMissCount() {
        return this.mBitmapPoolMissCount;
    }

    public int getMemCacheHitCount() {
        return this.mDiskCacheHitCount;
    }

    public int getMemCacheMissCount() {
        return this.mDiskCacheMissCount;
    }

    public void onDiskCacheLookedUp(boolean z) {
        if (z) {
            this.mDiskCacheHitCount++;
        } else {
            this.mDiskCacheMissCount++;
        }
    }

    public void onBitmapPoolLookedUp(boolean z) {
        if (z) {
            this.mBitmapPoolHitCount++;
        } else {
            this.mBitmapPoolMissCount++;
        }
    }

    public void onMemCacheLookedUp(boolean z) {
        if (z) {
            this.mMemCacheHitCount++;
        } else {
            this.mMemCacheMissCount++;
        }
    }

    public void setSize(int i) {
        this.mSize = i;
    }

    public Map<String, String> getExtras() {
        return this.mExtras;
    }

    public void setExtras(Map<String, String> map) {
        this.mExtras = map;
    }

    public MimeType getFormat() {
        if (this.mFormat == null) {
            this.mFormat = EncodedImage.getMimeTypeByExtension(this.mUriInfo.getImageExtension());
        }
        return this.mFormat;
    }

    public void setCompressFormat(MimeType mimeType) {
        this.mFormat = mimeType;
    }

    public Map<String, Integer> getDetailCost() {
        return this.mDetailCost;
    }

    public void setDetailCost(Map<String, Integer> map) {
        this.mDetailCost = map;
    }

    public ImageUriInfo getUriInfo() {
        return this.mUriInfo;
    }

    public boolean isDuplicated() {
        return this.mIsDuplicated;
    }

    public boolean isRetrying() {
        return this.mIsRetrying;
    }

    public void duplicate(boolean z) {
        this.mIsDuplicated = z;
    }

    public String toString() {
        return "ImageStatistics(FromType=" + this.mFromType + ", Duplicated=" + this.mIsDuplicated + ", Retrying=" + this.mIsRetrying + ", Size=" + this.mSize + ", Format=" + this.mFormat + ", DetailCost=" + this.mDetailCost + Operators.BRACKET_END_STR;
    }
}
