package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import java.util.Map;

final class LinkLogEntity {
    static final int LOG_LEVEL_DEBUG = 9999;
    static final int LOG_LEVEL_ERROR = 1;
    static final int LOG_LEVEL_INFO = 0;
    static final int LOG_STAGE_BEGIN = 1;
    static final int LOG_STAGE_DEFAULT = 0;
    static final int LOG_STAGE_END = 2;
    private String childBizName = "";
    private Map<UMDimKey, Object> dimMap = null;
    private String errorCode = "";
    private String errorMsg = "";
    private LinkLogExtData extData = null;
    private String featureType = "";
    private String launchId = "";
    private int logLevel = 0;
    private int logStage = 0;
    private String mainBizName = "";
    private String pageName = "";
    private UMRefContext refContext = null;
    private String threadId = "";
    private String timestamp = String.valueOf(System.currentTimeMillis());

    LinkLogEntity() {
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    @NonNull
    public String getMainBizName() {
        return this.mainBizName;
    }

    @NonNull
    public String getChildBizName() {
        return this.childBizName;
    }

    @NonNull
    public String getLaunchId() {
        return this.launchId;
    }

    @Nullable
    public UMRefContext getRefContext() {
        return this.refContext;
    }

    @NonNull
    public String getLinkId() {
        return this.refContext == null ? "" : this.refContext.getLinkId();
    }

    @NonNull
    public String getPageName() {
        return this.pageName;
    }

    @NonNull
    public String getThreadId() {
        return this.threadId;
    }

    @NonNull
    public String getFeatureType() {
        return this.featureType;
    }

    @NonNull
    public String getErrorCode() {
        return this.errorCode;
    }

    @NonNull
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public int getLogStage() {
        return this.logStage;
    }

    @Nullable
    public Map<UMDimKey, Object> getDimMap() {
        return this.dimMap;
    }

    @Nullable
    public LinkLogExtData getExtData() {
        return this.extData;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setBizName(@NonNull String str, @Nullable String str2) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.mainBizName = str;
        if (str2 == null) {
            return this;
        }
        this.childBizName = str2;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setLaunchId(String str) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.launchId = str;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setRefContext(@Nullable UMRefContext uMRefContext) {
        if (uMRefContext == null) {
            return this;
        }
        this.refContext = uMRefContext;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setPageName(String str) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.pageName = str;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setThreadId(String str) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.threadId = str;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setFeatureType(String str) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.featureType = str;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setErrorCode(@Nullable String str, @Nullable String str2) {
        if (UMStringUtils.isEmpty(str)) {
            return this;
        }
        this.errorCode = str;
        if (str2 == null) {
            return this;
        }
        this.errorMsg = str2;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setLogLevel(int i) {
        this.logLevel = i;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setLogStage(int i) {
        this.logStage = i;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setDimMap(@Nullable Map<UMDimKey, Object> map) {
        if (map == null || map.isEmpty()) {
            return this;
        }
        this.dimMap = map;
        return this;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogEntity setExtData(@Nullable LinkLogExtData linkLogExtData) {
        if (linkLogExtData == null || linkLogExtData.isEmpty()) {
            return this;
        }
        this.extData = linkLogExtData;
        return this;
    }
}
