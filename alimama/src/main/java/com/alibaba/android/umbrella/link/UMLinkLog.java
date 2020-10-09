package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.alibaba.android.umbrella.link.export.UMTagKey;
import com.alibaba.android.umbrella.link.export.UMUserData;
import com.alibaba.android.umbrella.link.export.UmTypeKey;
import com.alibaba.android.umbrella.link.util.UMLinkLogUtils;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.mtop.domain.MtopResponse;
import org.json.JSONObject;

public final class UMLinkLog {
    private static volatile LinkLogManager linkLogManager;

    private static LinkLogManager getLinkLogManager() {
        if (linkLogManager == null) {
            synchronized (UMLinkLog.class) {
                if (linkLogManager == null) {
                    linkLogManager = new LinkLogManager();
                }
            }
        }
        return linkLogManager;
    }

    public static UMRefContext logSimpleInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable String str4) {
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, str3)) {
            return null;
        }
        return getLinkLogManager().createAndLog(str, str2, str3, (UMRefContext) null, 0, (String) null, (String) null, (Map<UMDimKey, Object>) null, LinkLogExtData.fromUserData(UMUserData.fromMsg(str4)));
    }

    public static UMRefContext logInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, str3)) {
            return null;
        }
        return getLinkLogManager().createAndLog(str, str2, str3, uMRefContext, 0, (String) null, (String) null, map, LinkLogExtData.fromUserData(uMUserData));
    }

    public static UMRefContext logError(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        String str6 = str;
        String str7 = str3;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, str3)) {
            return null;
        }
        return getLinkLogManager().createAndLog(str, str2, str3, uMRefContext, 0, str4, str5, map, LinkLogExtData.fromUserData(uMUserData));
    }

    public static UMRefContext logBegin(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, str3)) {
            return null;
        }
        return getLinkLogManager().createAndLog(str, str2, str3, uMRefContext, 1, (String) null, (String) null, map, LinkLogExtData.fromUserData(uMUserData));
    }

    public static UMRefContext logEnd(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        String str6 = str;
        String str7 = str3;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, str3)) {
            return null;
        }
        return getLinkLogManager().createAndLog(str, str2, str3, uMRefContext, 2, str4, str5, map, LinkLogExtData.fromUserData(uMUserData));
    }

    private static void fillTagToDimMap(@Nullable Map<UMDimKey, Object> map, @Nullable Map<UMTagKey, String> map2) {
        UMTagKey uMTagKey;
        if (map != null && map2 != null && !map2.isEmpty()) {
            for (Map.Entry next : map2.entrySet()) {
                if (!(next == null || (uMTagKey = (UMTagKey) next.getKey()) == null)) {
                    if (next.getValue() == null) {
                        map.put(uMTagKey.getDimKey(), "empty value");
                    } else {
                        map.put(uMTagKey.getDimKey(), next.getValue());
                    }
                }
            }
        }
    }

    public static UMRefContext logPageLifecycle(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        String str7 = str;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, UMLLCons.FEATURE_TYPE_PAGE)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str8 = str3;
        hashMap.put(UMDimKey.DIM_1, str3);
        hashMap.put(UMDimKey.DIM_2, str4);
        fillTagToDimMap(hashMap, map);
        return getLinkLogManager().createAndLog(str, str2, UMLLCons.FEATURE_TYPE_PAGE, uMRefContext, 0, str5, str6, hashMap, LinkLogExtData.fromUserData(uMUserData));
    }

    @Deprecated
    public static UMRefContext logUIAction(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        return logUIAction2(str, str2, uMRefContext, str3, "", str4, str5, str6, map, uMUserData);
    }

    public static UMRefContext logUIAction2(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @Nullable String str7, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        String str8 = str;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, UMLLCons.FEATURE_TYPE_UI_ACTION)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str9 = str3;
        hashMap.put(UMDimKey.DIM_1, str3);
        hashMap.put(UMDimKey.DIM_2, str4);
        hashMap.put(UMDimKey.DIM_3, str5);
        hashMap.put(UMDimKey.DIM_4, str6);
        fillTagToDimMap(hashMap, map);
        return getLinkLogManager().createAndLog(str, str2, UMLLCons.FEATURE_TYPE_UI_ACTION, uMRefContext, 0, (String) null, (String) null, hashMap, LinkLogExtData.fromUserData(uMUserData).putKV("args", str7));
    }

    public static UMRefContext logMtopReq(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        String str6 = str;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, UMLLCons.FEATURE_TYPE_MTOP)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str7 = str3;
        hashMap.put(UMDimKey.DIM_1, str3);
        hashMap.put(UMDimKey.DIM_2, str4);
        fillTagToDimMap(hashMap, map);
        return getLinkLogManager().createAndLog(str, str2, UMLLCons.FEATURE_TYPE_MTOP, uMRefContext, 1, (String) null, (String) null, hashMap, LinkLogExtData.fromUserData(uMUserData).putKV("requestParams", str5));
    }

    public static UMRefContext logMtopResponse(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull MtopResponse mtopResponse, @NonNull String str3, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        String str4 = str;
        if (getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, UMLLCons.FEATURE_TYPE_MTOP) || mtopResponse == null) {
            return null;
        }
        String traceIds = UMLinkLogUtils.getTraceIds(mtopResponse);
        HashMap hashMap = new HashMap();
        hashMap.put(UMDimKey.DIM_1, mtopResponse.getApi());
        hashMap.put(UMDimKey.DIM_2, mtopResponse.getV());
        hashMap.put(UMDimKey.DIM_3, UMLinkLogUtils.ensureText(traceIds));
        hashMap.put(UMDimKey.DIM_4, mtopResponse.getRetCode());
        hashMap.put(UMDimKey.DIM_5, Integer.valueOf(mtopResponse.getResponseCode()));
        fillTagToDimMap(hashMap, map);
        String str5 = "";
        String str6 = "";
        if (!mtopResponse.isApiSuccess()) {
            str5 = mtopResponse.getRetCode();
            str6 = mtopResponse.getRetMsg();
        }
        String str7 = str5;
        String str8 = str6;
        String str9 = "";
        JSONObject dataJsonObject = mtopResponse.getDataJsonObject();
        if (dataJsonObject != null) {
            str9 = dataJsonObject.toString();
        }
        return getLinkLogManager().createAndLog(str, str2, UMLLCons.FEATURE_TYPE_MTOP, uMRefContext, 2, str7, str8, hashMap, LinkLogExtData.fromUserData(uMUserData).putKV("responseString", str9).putKV("responseHeaders", mtopResponse.getHeaderFields()).putKV("requestParams", str3));
    }

    public static void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        if (!getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str4, str)) {
            getLinkLogManager().triggerCommitSuccess(str, str2, str3, str4, str5, map);
        }
    }

    public static void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        String str8 = str;
        String str9 = str4;
        if (!getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str4, str)) {
            getLinkLogManager().triggerCommitFailure(str, str2, str3, str4, str5, map, str6, str7);
        }
    }

    public static void commitFeedback(String str, String str2, UmTypeKey umTypeKey, String str3, String str4) {
        if (!getLinkLogManager().getLinkLogSwitcher().isSkipAllPoint(str, umTypeKey.getKey())) {
            getLinkLogManager().triggerCommitFeedback(str, str2, umTypeKey, str3, str4);
        }
    }
}
