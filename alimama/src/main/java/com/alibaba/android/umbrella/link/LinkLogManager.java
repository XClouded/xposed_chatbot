package com.alibaba.android.umbrella.link;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.LinkLogWorker;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UmTypeKey;
import com.alibaba.android.umbrella.link.util.UMLaunchId;
import com.alibaba.android.umbrella.link.util.UMLinkLogUtils;
import com.alibaba.android.umbrella.trace.UmbrellaUtils;
import java.util.Map;

class LinkLogManager {
    private static final String TAG_VERSION_UMBRELLA_2_0 = "umbrella2";
    @NonNull
    private final LinkLogSwitcher linkLogSwitcher = new LinkLogSwitcher();
    private final LinkLogWorker linkLogWorker = new LinkLogWorker();

    LinkLogManager() {
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public LinkLogSwitcher getLinkLogSwitcher() {
        return this.linkLogSwitcher;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public UMRefContext createAndLog(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, int i, @Nullable String str4, @Nullable String str5, @Nullable Map<UMDimKey, Object> map, @Nullable LinkLogExtData linkLogExtData) {
        if (this.linkLogSwitcher.isSkipLog(str, str2, str3, str4)) {
            return null;
        }
        LinkLogEntity createEntity = createEntity(str, str2, str3, uMRefContext, i, str4, str5, map, linkLogExtData);
        triggerLogEntity(createEntity);
        return createEntity.getRefContext();
    }

    private LinkLogEntity createEntity(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, int i, @Nullable String str4, @Nullable String str5, @Nullable Map<UMDimKey, Object> map, @Nullable LinkLogExtData linkLogExtData) {
        LinkLogEntity extData = new LinkLogEntity().setBizName(str, str2).setFeatureType(str3).setLogLevel(UMStringUtils.isEmpty(str4) ^ true ? 1 : 0).setLogStage(i).setErrorCode(str4, str5).setDimMap(map).setExtData(linkLogExtData);
        if (uMRefContext == null) {
            uMRefContext = new UMRefContext(UMLaunchId.createLinkId(""));
        }
        return extData.setRefContext(uMRefContext).setLaunchId(UMLaunchId.getLaunchId()).setPageName(UMLinkLogUtils.getPageName()).setThreadId(UMLinkLogUtils.getThreadId());
    }

    private void triggerLogEntity(LinkLogEntity linkLogEntity) {
        if (linkLogEntity.getRefContext() != null) {
            String mainBizName = linkLogEntity.getMainBizName();
            final String childBizName = linkLogEntity.getChildBizName();
            String featureType = linkLogEntity.getFeatureType();
            String errorCode = linkLogEntity.getErrorCode();
            final String str = mainBizName;
            final String str2 = featureType;
            final String str3 = errorCode;
            final LinkLogEntity linkLogEntity2 = linkLogEntity;
            this.linkLogWorker.runNonBlocking(new LinkLogWorker.SafetyRunnable() {
                public void runSafety() {
                    fillExceptionArgs("exception_log", str, childBizName, str2, str3);
                    TLogger.logTLog(linkLogEntity2);
                }
            });
            printLogcatIfEnabled("triggerLogEntity", mainBizName, featureType, errorCode);
        }
    }

    /* access modifiers changed from: package-private */
    public void triggerCommitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        if (!this.linkLogSwitcher.isSkipCommit(str4, str5, str, "")) {
            AppMonitorAlarm.commitSuccessStability(str, str2, replaceTagVersion(str4, str5, str, str3), str4, str5, map);
            printLogcatIfEnabled("triggerCommitSuccess", str4, str, "");
        }
    }

    /* access modifiers changed from: package-private */
    public void triggerCommitFailure(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable Map<String, String> map, @NonNull String str6, @NonNull String str7) {
        String str8 = str;
        String str9 = str4;
        String str10 = str5;
        String str11 = str6;
        if (!this.linkLogSwitcher.isSkipCommit(str9, str10, str, str11) && !UmbrellaUtils.isFlowControl(str6)) {
            String str12 = str3;
            AppMonitorAlarm.commitFailureStability(str, str2, replaceTagVersion(str9, str10, str, str3), str4, str5, map, str6, str7);
            printLogcatIfEnabled("triggerCommitFailure", str9, str, str11);
        }
    }

    /* access modifiers changed from: package-private */
    public void triggerCommitFeedback(@NonNull String str, @NonNull String str2, @Nullable UmTypeKey umTypeKey, @NonNull String str3, @NonNull String str4) {
        if (this.linkLogSwitcher.isFeedbackEnabled()) {
            AppMonitorAlarm.commitFeedback(str, str2, umTypeKey, str3, str4);
            printLogcatIfEnabled("triggerCommitFeedback", str, umTypeKey.getKey(), str3);
        }
    }

    private void printLogcatIfEnabled(String str, String str2, String str3, String str4) {
        if (!this.linkLogSwitcher.isLogcatEnabled()) {
            return;
        }
        if (UMStringUtils.isEmpty(str4)) {
            Log.v(TAG_VERSION_UMBRELLA_2_0, str + ", mainBizName=" + str2 + " featureType=" + str3);
            return;
        }
        Log.e(TAG_VERSION_UMBRELLA_2_0, str + ", mainBizName=" + str2 + " featureType=" + str3 + " errorCode=" + str4);
    }

    private String replaceTagVersion(String str, String str2, String str3, String str4) {
        return this.linkLogSwitcher.isNeedDoubleCheckCommit(str, str2, str3) ? TAG_VERSION_UMBRELLA_2_0 : str4;
    }
}
