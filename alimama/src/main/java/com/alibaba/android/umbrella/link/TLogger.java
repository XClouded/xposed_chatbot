package com.alibaba.android.umbrella.link;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.util.UMLinkLogUtils;
import com.taobao.android.AliLogInterface;
import com.taobao.android.AliLogServiceFetcher;
import java.io.Serializable;
import java.util.Map;
import java.util.Random;

final class TLogger {
    private static final int MAX_TLOG_TEXT_LENGTH = 10240;
    private static final String MODULE_UMBRELLA = "umbrella";
    private static final String SEPARATOR_MESSAGE = "↕︎";
    private static volatile AliLogInterface logService;
    private static final Random random = new Random();

    @Keep
    public static final class LinkLogTLogDO implements Serializable {
        public String umb1;
        public String umb10;
        public String umb11;
        public String umb12;
        public String umb13;
        public String umb14;
        public String umb15;
        public String umb16;
        public String umb17;
        public String umb18;
        public String umb19;
        public String umb2;
        public Object umb20;
        public String umb21;
        public String umb22;
        public String umb23;
        public String umb24;
        public String umb25;
        public String umb26;
        public String umb27;
        public String umb28;
        public String umb29;
        public String umb3;
        public String umb30;
        public String umb31;
        public String umb32;
        public String umb33;
        public String umb34;
        public String umb35;
        public String umb4;
        public String umb5;
        public String umb6;
        public String umb7;
        public String umb8;
        public String umb9;
    }

    TLogger() {
    }

    static void logTLog(LinkLogEntity linkLogEntity) {
        AliLogInterface aliLogInterface = get();
        if (aliLogInterface != null) {
            String unifiedString = UMLinkLogUtils.toUnifiedString(buildLinkLogDO(linkLogEntity));
            int nextInt = random.nextInt(Integer.MAX_VALUE);
            int length = unifiedString.length();
            if (length < 10240) {
                writeTLogChunk(aliLogInterface, linkLogEntity, unifiedString, nextInt, "-1");
                return;
            }
            int i = 0;
            int i2 = 0;
            while (i < length) {
                int i3 = i + 10240;
                writeTLogChunk(aliLogInterface, linkLogEntity, unifiedString.substring(i, Math.min(i3, length)), nextInt, String.valueOf(i2));
                i2++;
                i = i3;
            }
        }
    }

    private static void writeTLogChunk(@NonNull AliLogInterface aliLogInterface, LinkLogEntity linkLogEntity, String str, int i, String str2) {
        aliLogInterface.loge(MODULE_UMBRELLA, MODULE_UMBRELLA, linkLogEntity.getMainBizName() + SEPARATOR_MESSAGE + linkLogEntity.getChildBizName() + SEPARATOR_MESSAGE + linkLogEntity.getFeatureType() + SEPARATOR_MESSAGE + i + SEPARATOR_MESSAGE + str2 + SEPARATOR_MESSAGE + str);
    }

    private static LinkLogTLogDO buildLinkLogDO(LinkLogEntity linkLogEntity) {
        LinkLogTLogDO linkLogTLogDO = new LinkLogTLogDO();
        linkLogTLogDO.umb1 = linkLogEntity.getMainBizName();
        linkLogTLogDO.umb2 = linkLogEntity.getChildBizName();
        linkLogTLogDO.umb3 = linkLogEntity.getLaunchId();
        linkLogTLogDO.umb4 = linkLogEntity.getLinkId();
        linkLogTLogDO.umb5 = linkLogEntity.getPageName();
        linkLogTLogDO.umb6 = linkLogEntity.getThreadId();
        linkLogTLogDO.umb7 = linkLogEntity.getFeatureType();
        linkLogTLogDO.umb8 = linkLogEntity.getErrorCode();
        linkLogTLogDO.umb9 = linkLogEntity.getErrorMsg();
        linkLogTLogDO.umb10 = String.valueOf(linkLogEntity.getLogLevel());
        linkLogTLogDO.umb11 = String.valueOf(linkLogEntity.getLogStage());
        linkLogTLogDO.umb12 = linkLogEntity.getTimestamp();
        linkLogTLogDO.umb20 = linkLogEntity.getExtData() == null ? "" : linkLogEntity.getExtData().getExtMap();
        Map<UMDimKey, Object> dimMap = linkLogEntity.getDimMap();
        if (dimMap == null) {
            return linkLogTLogDO;
        }
        linkLogTLogDO.umb21 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_1));
        linkLogTLogDO.umb22 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_2));
        linkLogTLogDO.umb23 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_3));
        linkLogTLogDO.umb24 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_4));
        linkLogTLogDO.umb25 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_5));
        linkLogTLogDO.umb26 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_6));
        linkLogTLogDO.umb27 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_7));
        linkLogTLogDO.umb28 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_8));
        linkLogTLogDO.umb29 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_9));
        linkLogTLogDO.umb30 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.DIM_10));
        linkLogTLogDO.umb31 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.TAG_1));
        linkLogTLogDO.umb32 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.TAG_2));
        linkLogTLogDO.umb33 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.TAG_3));
        linkLogTLogDO.umb34 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.TAG_4));
        linkLogTLogDO.umb35 = UMLinkLogUtils.toUnifiedString(dimMap.get(UMDimKey.TAG_5));
        return linkLogTLogDO;
    }

    @Nullable
    private static AliLogInterface get() {
        if (logService == null) {
            synchronized (TLogger.class) {
                if (logService == null) {
                    logService = AliLogServiceFetcher.getLogService();
                }
            }
        }
        return logService;
    }
}
