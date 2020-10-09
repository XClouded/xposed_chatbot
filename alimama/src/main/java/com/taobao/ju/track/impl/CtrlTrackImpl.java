package com.taobao.ju.track.impl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.CsvFileUtil;
import com.taobao.ju.track.impl.interfaces.ICtrlTrack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CtrlTrackImpl extends TrackImpl implements ICtrlTrack {
    private static final String DEFAULT_FILE_NAME = "ut_ctrl.csv";
    private static final String KEY_PAGE = "_page";

    public CtrlTrackImpl(Context context) {
        super(context, DEFAULT_FILE_NAME);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CtrlTrackImpl(Context context, String str) {
        super(context, !CsvFileUtil.isCSV(str) ? DEFAULT_FILE_NAME : str);
    }

    public String getSpm(String str) {
        return getSpm((String) null, str);
    }

    public String getSpm(Activity activity, String str) {
        return getSpm(activity != null ? activity.getClass().getSimpleName() : null, str);
    }

    public String getSpm(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> paramMap = getParamMap(str2);
        if (paramMap != null) {
            PageTrackImpl track = JTrack.Page.getTrack();
            if (str == null) {
                str = paramMap.get(KEY_PAGE);
            }
            String spmAB = track.getSpmAB(str);
            if (Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE.equals(spmAB)) {
                spmAB = track.getSpmAB(getTopActivityName());
            }
            stringBuffer.append(spmAB);
            stringBuffer.append(".");
            stringBuffer.append(getParamValue(paramMap, "_spmc", "0"));
            stringBuffer.append(".");
            stringBuffer.append(getParamValue(paramMap, "_spmd", "0"));
        }
        return stringBuffer.length() > 0 ? stringBuffer.toString() : Constants.PARAM_OUTER_SPM_NONE;
    }

    public Map<String, String> getParamSpm(String str) {
        Map<String, String> paramSpmInternal = getParamSpmInternal(str);
        paramSpmInternal.put("spm", getSpm(str));
        return paramSpmInternal;
    }

    public Map<String, String> getParamSpm(Activity activity, String str) {
        Map<String, String> paramSpmInternal = getParamSpmInternal(str);
        paramSpmInternal.put("spm", getSpm(activity, str));
        return paramSpmInternal;
    }

    public Map<String, String> getParamSpm(String str, String str2) {
        Map<String, String> paramSpmInternal = getParamSpmInternal(str2);
        paramSpmInternal.put("spm", getSpm(str, str2));
        return paramSpmInternal;
    }

    private Map<String, String> getParamSpmInternal(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("autosend", getParamValue(getParamMap(str), "_autosend", (String) null));
        return hashMap;
    }

    private String getTopActivityName() {
        ActivityManager activityManager;
        ComponentName componentName;
        if (!(this.mContext == null || (activityManager = (ActivityManager) this.mContext.getSystemService("activity")) == null)) {
            try {
                List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                if (!(runningTasks == null || runningTasks.get(0) == null || (componentName = runningTasks.get(0).topActivity) == null)) {
                    String className = componentName.getClassName();
                    return className.substring(Math.max(0, className.lastIndexOf(".") + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
