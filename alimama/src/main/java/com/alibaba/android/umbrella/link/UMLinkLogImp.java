package com.alibaba.android.umbrella.link;

import android.util.Log;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.alibaba.android.umbrella.link.export.UMTagKey;
import com.alibaba.android.umbrella.link.export.UMUserData;
import com.alibaba.android.umbrella.link.export.UmTypeKey;
import com.alibaba.android.umbrella.link.util.UMLaunchId;
import java.util.Map;
import mtopsdk.mtop.domain.MtopResponse;

@Keep
public final class UMLinkLogImp implements UMLinkLogInterface {
    private static final String TAG = "umbrella";

    public UMRefContext createLinkId(String str) {
        Log.e(TAG, "createLinkId");
        return new UMRefContext(UMLaunchId.createLinkId(str));
    }

    public void logSimpleInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable String str4) {
        try {
            Log.v(TAG, str + " logSimpleInfo");
            UMLinkLog.logSimpleInfo(str, str2, str3, str4);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, "");
        }
    }

    public void logInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        try {
            Log.v(TAG, str + " logInfo");
            UMLinkLog.logInfo(str, str2, str3, uMRefContext, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, "");
        }
    }

    public void logInfoRawDim(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<String, Object> map, @Nullable UMUserData uMUserData) {
        try {
            Log.v(TAG, str + " logInfoRawDim");
            UMLinkLog.logInfo(str, str2, str3, uMRefContext, UMDimKey.convertRawMap(map), uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, "");
        }
    }

    public void logError(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        try {
            Log.e(TAG, str + " logError");
            UMLinkLog.logError(str, str2, str3, uMRefContext, str4, str5, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, str4);
        }
    }

    public void logErrorRawDim(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<String, Object> map, @Nullable UMUserData uMUserData) {
        try {
            StringBuilder sb = new StringBuilder();
            String str6 = str;
            try {
                sb.append(str);
                sb.append(" logErrorRawDim");
                Log.e(TAG, sb.toString());
                UMLinkLog.logError(str, str2, str3, uMRefContext, str4, str5, UMDimKey.convertRawMap(map), uMUserData);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            String str7 = str;
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, str4);
        }
    }

    public void logBegin(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        try {
            Log.v(TAG, str + " logBegin");
            UMLinkLog.logBegin(str, str2, str3, uMRefContext, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, "");
        }
    }

    public void logEnd(@NonNull String str, @NonNull String str2, @NonNull String str3, @Nullable UMRefContext uMRefContext, @NonNull String str4, @NonNull String str5, @Nullable Map<UMDimKey, Object> map, @Nullable UMUserData uMUserData) {
        try {
            Log.v(TAG, str + " logEnd");
            UMLinkLog.logEnd(str, str2, str3, uMRefContext, str4, str5, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, str3, str4);
        }
    }

    public void logPageLifecycle(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, int i2, @NonNull String str3, @NonNull String str4, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            StringBuilder sb = new StringBuilder();
            String str5 = str;
            try {
                sb.append(str);
                sb.append(" logPageLifecycle");
                Log.i(TAG, sb.toString());
                UMLinkLog.logPageLifecycle(str, str2, uMRefContext, String.valueOf(i), String.valueOf(i2), str3, str4, map, uMUserData);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            String str6 = str;
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_PAGE, str3);
        }
    }

    public void logPageLifecycle2(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            Log.i(TAG, str + " logPageLifecycle2");
            UMLinkLog.logPageLifecycle(str, str2, uMRefContext, str3, str4, str5, str6, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_PAGE, str5);
        }
    }

    public void logUIAction(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, @NonNull String str3, @NonNull String str4, @Nullable String str5, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            StringBuilder sb = new StringBuilder();
            String str6 = str;
            try {
                sb.append(str);
                sb.append(" logUIAction");
                Log.i(TAG, sb.toString());
                UMLinkLog.logUIAction2(str, str2, uMRefContext, String.valueOf(i), "", str3, str4, str5, map, uMUserData);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            String str7 = str;
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_UI_ACTION, "");
        }
    }

    public void logUIAction2(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, int i, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable String str6, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            StringBuilder sb = new StringBuilder();
            String str7 = str;
            try {
                sb.append(str);
                sb.append(" logUIAction2");
                Log.i(TAG, sb.toString());
                UMLinkLog.logUIAction2(str, str2, uMRefContext, String.valueOf(i), str3, str4, str5, str6, map, uMUserData);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            String str8 = str;
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_UI_ACTION, "");
        }
    }

    public void logMtopReq(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull String str3, @NonNull String str4, @NonNull String str5, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            Log.i(TAG, str + " logMtopReq");
            UMLinkLog.logMtopReq(str, str2, uMRefContext, str3, str4, str5, map, uMUserData);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_MTOP, "");
        }
    }

    public void logMtopResponse(@NonNull String str, @NonNull String str2, @Nullable UMRefContext uMRefContext, @NonNull Object obj, @NonNull String str3, @Nullable Map<UMTagKey, String> map, @Nullable UMUserData uMUserData) {
        try {
            Log.i(TAG, str + " logMtopResponse");
            if (obj instanceof MtopResponse) {
                UMLinkLog.logMtopResponse(str, str2, uMRefContext, (MtopResponse) obj, str3, map, uMUserData);
            }
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_log", str, str2, UMLLCons.FEATURE_TYPE_MTOP, "");
        }
    }

    public void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        try {
            Log.v(TAG, str4 + " commitSuccess");
            UMLinkLog.commitSuccess(str, str2, str3, str4, str5, map);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_success", str4, str5, str, "");
        }
    }

    public void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        try {
            Log.e(TAG, str4 + " commitFailure");
            UMLinkLog.commitFailure(str, str2, str3, str4, str5, map, str6, str7);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_failure", str4, str5, str, str6);
        }
    }

    public void commitFeedback(String str, String str2, UmTypeKey umTypeKey, String str3, String str4) {
        try {
            Log.e(TAG, str + " commitFeedback");
            UMLinkLog.commitFeedback(str, str2, umTypeKey, str3, str4);
        } catch (Throwable th) {
            AppMonitorAlarm.commitInnerException(th, "exception_failure", str, (String) null, (String) null, (String) null);
        }
    }
}
