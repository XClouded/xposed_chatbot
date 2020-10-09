package com.taobao.ju.track.spm;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.param.JParamBuilder;
import com.taobao.ju.track.util.IntentUtil;
import com.taobao.ju.track.util.ParamUtil;
import java.util.HashMap;
import java.util.Map;

public class SpmUtil {
    private static final String PARAM_SPM = "spm";
    private static String sPreSpm = null;
    private static boolean sUsePreSpm = false;

    public static void setPreSpm(String str) {
        sPreSpm = str;
    }

    public static void setUsePreSpm() {
        sUsePreSpm = true;
    }

    public static String getPreSpm() {
        if (!sUsePreSpm) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        sUsePreSpm = false;
        return !TextUtils.isEmpty(sPreSpm) ? sPreSpm : Constants.PARAM_OUTER_SPM_NONE;
    }

    public static void clearPreSpm() {
        sPreSpm = null;
    }

    public static boolean isSpmValid(String str) {
        return str != null && !str.equals(Constants.PARAM_OUTER_SPM_NONE) && !str.equals(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE);
    }

    public static String getSPM(JParamBuilder jParamBuilder) {
        if (jParamBuilder == null) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        String paramValue = jParamBuilder.getParamValue("spm");
        return !TextUtils.isEmpty(paramValue) ? paramValue : Constants.PARAM_OUTER_SPM_NONE;
    }

    public static String getSpmFromUrl(String str) {
        if (str == null) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        Uri parse = Uri.parse(str);
        return (!parse.toString().contains("spm") || TextUtils.isEmpty(parse.getQueryParameter("spm"))) ? Constants.PARAM_OUTER_SPM_NONE : parse.getQueryParameter("spm");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000e, code lost:
        r1 = (r0 = android.net.Uri.parse(r3)).getQueryParameter("spm");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String addSpm(java.lang.String r3, java.lang.String r4) {
        /*
            if (r3 == 0) goto L_0x0032
            android.net.Uri r0 = android.net.Uri.parse(r3)
            if (r0 == 0) goto L_0x0032
            boolean r1 = r0.isHierarchical()
            if (r1 == 0) goto L_0x0032
            java.lang.String r1 = "spm"
            java.lang.String r1 = r0.getQueryParameter(r1)
            if (r1 == 0) goto L_0x001f
            java.lang.String r2 = "0.0.0.0"
            int r1 = r1.indexOf(r2)
            r2 = -1
            if (r1 == r2) goto L_0x0032
        L_0x001f:
            android.net.Uri$Builder r3 = r0.buildUpon()
            java.lang.String r0 = "spm"
            android.net.Uri$Builder r3 = r3.appendQueryParameter(r0, r4)
            android.net.Uri r3 = r3.build()
            java.lang.String r3 = r3.toString()
            return r3
        L_0x0032:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.spm.SpmUtil.addSpm(java.lang.String, java.lang.String):java.lang.String");
    }

    public static String addSpm(String str, Intent intent) {
        return ParamUtil.hasExtra(intent, "spm") ? addSpm(str, ParamUtil.getStringExtra(intent, "spm")) : str;
    }

    public static Map addSpm(Map map, JParamBuilder jParamBuilder) {
        if (jParamBuilder != null) {
            if (map == null) {
                map = new HashMap();
            }
            String spm = getSPM(jParamBuilder);
            if (isSpmValid(spm)) {
                map.put("spm", spm);
            }
        }
        return map;
    }

    public static Intent addSpm(Intent intent, JParamBuilder jParamBuilder) {
        if (jParamBuilder != null) {
            String spm = jParamBuilder.getSpm();
            if (isSpmValid(spm)) {
                intent.putExtra("spm", spm);
            }
        }
        return intent;
    }

    public static Intent addSpm(Intent intent, String str) {
        if (intent != null) {
            String spm = JTrack.Ctrl.getSpm(IntentUtil.getComponentSimpleClassName(intent), str);
            if (isSpmValid(spm)) {
                intent.putExtra("spm", spm);
            } else if (isSpmValid(str)) {
                intent.putExtra("spm", str);
            }
        }
        return intent;
    }

    private static String getABOfSpm(String str) {
        int indexOf;
        int indexOf2;
        if (str == null || (indexOf = str.indexOf(46)) == -1 || (indexOf2 = str.indexOf(46, indexOf)) == -1) {
            return null;
        }
        str.substring(0, indexOf2);
        return null;
    }
}
