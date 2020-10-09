package mtopsdk.mtop.upload.util;

import android.util.SparseArray;
import mtopsdk.common.util.RemoteConfig;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.xstate.NetworkClassEnum;

public final class FileUploadSetting {
    private static int DEFAULT_SEGMENT_RETRY_TIMES = 2;
    private static int DEFAULT_SEGMENT_SIZE = 131072;
    private static int DEFAULT_UPLOAD_THREAD_NUMS = 2;
    private static final int MAX_SEGMENT_RETRY_TIMES = 10;
    private static final int MAX_UPLOAD_THREAD_NUMS = 10;
    private static final int MIN_SEGMENT_RETRY_TIMES = 0;
    private static final int MIN_UPLOAD_THREAD_NUMS = 1;
    private static final String TAG = "mtopsdk.FileUploadSetting";
    public static final SparseArray<String> uploadDomainMap = new SparseArray<>(3);

    private FileUploadSetting() {
    }

    static {
        uploadDomainMap.put(EnvModeEnum.ONLINE.getEnvMode(), "upload.m.taobao.com");
        uploadDomainMap.put(EnvModeEnum.PREPARE.getEnvMode(), "upload.wapa.taobao.com");
        uploadDomainMap.put(EnvModeEnum.TEST.getEnvMode(), "upload.waptest.taobao.net");
    }

    public static int getSegmentRetryTimes() {
        int i = RemoteConfig.getInstance().segmentRetryTimes;
        if (i < 0 || i > 10) {
            return DEFAULT_SEGMENT_RETRY_TIMES;
        }
        return i;
    }

    public static void setSegmentRetryTimes(int i) {
        if (i >= 0 && i <= 10) {
            DEFAULT_SEGMENT_RETRY_TIMES = i;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, String.format("[setSegmentRetryTimes] setSegmentRetryTimes succeed, segmentRetryTimes=%d", new Object[]{Integer.valueOf(i)}));
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, String.format("[setSegmentRetryTimes] invalid parameter,range[%d,%d], segmentRetryTimes=%d", new Object[]{0, 10, Integer.valueOf(i)}));
        }
    }

    public static int getSegmentSize(NetworkClassEnum networkClassEnum) {
        if (networkClassEnum == null) {
            return DEFAULT_SEGMENT_SIZE;
        }
        Integer segmentSize = RemoteConfig.getInstance().getSegmentSize(networkClassEnum.getNetClass());
        if (segmentSize == null || segmentSize.intValue() <= 0) {
            return DEFAULT_SEGMENT_SIZE;
        }
        return segmentSize.intValue();
    }

    public static int getSegmentSize(String str, String str2) {
        Integer segmentSize;
        if (StringUtils.isBlank(str)) {
            return DEFAULT_SEGMENT_SIZE;
        }
        if (StringUtils.isNotBlank(str2) && (segmentSize = RemoteConfig.getInstance().getSegmentSize(StringUtils.concatStr(str, str2))) != null && segmentSize.intValue() > 0) {
            return segmentSize.intValue();
        }
        Integer segmentSize2 = RemoteConfig.getInstance().getSegmentSize(str);
        if (segmentSize2 == null || segmentSize2.intValue() <= 0) {
            return DEFAULT_SEGMENT_SIZE;
        }
        return segmentSize2.intValue();
    }

    public static void setSegmentSize(NetworkClassEnum networkClassEnum, int i) {
        if (networkClassEnum != null && i > 0) {
            RemoteConfig.getInstance().setSegmentSize(networkClassEnum.getNetClass(), i);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.w(TAG, String.format("[setSegmentSize] setSegmentSize succeed,networkType=%s,segmentSize=%d", new Object[]{networkClassEnum, Integer.valueOf(i)}));
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, String.format("[setSegmentSize] invalid parameter,networkType=%s,segmentSize=%d", new Object[]{networkClassEnum, Integer.valueOf(i)}));
        }
    }

    public static int getUploadThreadsNums() {
        int i = RemoteConfig.getInstance().uploadThreadNums;
        if (i < 1 || i > 10) {
            return DEFAULT_UPLOAD_THREAD_NUMS;
        }
        return i;
    }

    public static void setUploadThreadsNums(int i) {
        if (i >= 1 && i <= 10) {
            DEFAULT_UPLOAD_THREAD_NUMS = i;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, String.format("[setUploadThreadsNums] setUploadThreadsNums succeed, uploadThreadsNums=%d", new Object[]{Integer.valueOf(i)}));
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            TBSdkLog.w(TAG, String.format("[setUploadThreadsNums] invalid parameter,range[%d,%d], uploadThreadsNums=%d", new Object[]{1, 10, Integer.valueOf(i)}));
        }
    }

    public static boolean useHttps(String str) {
        return RemoteConfig.getInstance().useHttpsBizcodeSets.contains(str);
    }
}
