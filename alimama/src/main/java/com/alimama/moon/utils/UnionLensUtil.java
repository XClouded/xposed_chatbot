package com.alimama.moon.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.unionwl.utils.CommonUtils;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.el.parse.Operators;
import com.ut.device.UTDevice;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class UnionLensUtil {
    public static final String MTOP_UNION_LENS_RECOVERY_ID = "recoveryId";
    private static final Pattern REGEX_UNION_LENS_PREPVID = Pattern.compile("(?<=prepvid:)[^;]+", 32);
    private static final Pattern REGEX_UNION_LENS_RECOVERY_ID = Pattern.compile("(?<=recoveryid:)[^;]+", 32);
    private static final Pattern REGEX_UNION_LENS_TRAFFIC_FLAG = Pattern.compile("(?<=traffic_flag:)[^;]+", 32);
    private static final String TAG = "UnionLensUtil";
    public static final String UNION_LENS_LOG = "union_lens";
    public static final String UNION_LENS_PREPVID = "prepvid";
    public static final String UNION_LENS_RECOVERY_ID = "recoveryid";
    private static final String UNION_LENS_REPORT_SWITCH = "enable_unionlens_report";
    private static String appKey = "";
    public static String lastPrePvid;
    private static Context mContext;
    public static String prePvid;
    public static String recoveryId;

    public static void init(Context context, String str) {
        mContext = context;
        appKey = str;
        generateRecoveryId();
        generatePrepvid();
        lastPrePvid = prePvid;
    }

    public static void generateRecoveryId() {
        String replace = UTDevice.getUtdid(mContext).replace("+", ".");
        Random random = new Random(System.nanoTime());
        int nextInt = random.nextInt(Integer.MAX_VALUE);
        recoveryId = replace + "_" + (System.currentTimeMillis() + ((long) random.nextInt(OnLineMonitor.TASK_TYPE_FROM_BOOT))) + "_" + nextInt + "_" + appKey;
    }

    public static String generatePrepvid() {
        lastPrePvid = prePvid;
        String replace = UTDevice.getUtdid(mContext).replace("+", ".");
        Random random = new Random(System.nanoTime());
        int nextInt = random.nextInt(Integer.MAX_VALUE);
        prePvid = replace + "_" + (System.currentTimeMillis() + ((long) random.nextInt(OnLineMonitor.TASK_TYPE_FROM_BOOT))) + "_" + nextInt + "_" + appKey;
        return prePvid;
    }

    public static void updatePvid(String str) {
        prePvid = str;
    }

    public static String geneRidPvidJsonStr() {
        SafeJSONObject safeJSONObject = new SafeJSONObject();
        safeJSONObject.put(MTOP_UNION_LENS_RECOVERY_ID, recoveryId);
        safeJSONObject.put(UNION_LENS_PREPVID, prePvid);
        return safeJSONObject.toString();
    }

    public static String getUrlParameter(String str, String str2) {
        try {
            return Uri.parse(str).getQueryParameter(str2);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    public static String appendUrlUnionLens(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (!str.startsWith(CommonUtils.HTTP_PRE) && !str.startsWith(Utils.HTTPS_SCHEMA)) {
            return str;
        }
        try {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            String urlParameter = getUrlParameter(str, UNION_LENS_LOG);
            if (Boolean.valueOf(isTrafficFlag(urlParameter)).booleanValue()) {
                generateRecoveryId();
                generatePrepvid();
            } else {
                updateRecoveryId(urlParameter);
                updatePrepvid(urlParameter);
            }
            if (!TextUtils.isEmpty(urlParameter)) {
                return replaceUrl(str, UNION_LENS_LOG, URLEncoder.encode(updateUnionLes(urlParameter), "UTF-8"));
            }
            buildUpon.appendQueryParameter(UNION_LENS_LOG, appendUtUnionLens());
            return buildUpon.toString();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return str;
        }
    }

    public static String appendNaUnionLens(Map<String, String> map) {
        if (!map.containsKey(UNION_LENS_LOG)) {
            return appendUtUnionLens();
        }
        if (Boolean.valueOf(isTrafficFlag(map.get(UNION_LENS_LOG))).booleanValue()) {
            generateRecoveryId();
        } else {
            updateRecoveryId(map.get(UNION_LENS_LOG));
        }
        return updateUnionLes(map.get(UNION_LENS_LOG));
    }

    public static String appendUtUnionLens() {
        return "recoveryid:" + recoveryId + ";" + UNION_LENS_PREPVID + ":" + prePvid;
    }

    private static void updateRecoveryId(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher matcher = REGEX_UNION_LENS_RECOVERY_ID.matcher(str);
            if (matcher.find()) {
                recoveryId = matcher.group().replace("+", ".");
            }
        }
    }

    private static void updatePrepvid(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher matcher = REGEX_UNION_LENS_PREPVID.matcher(str);
            if (matcher.find()) {
                prePvid = matcher.group().replace("+", ".");
            }
        }
    }

    private static boolean isTrafficFlag(String str) {
        if (!TextUtils.isEmpty(str) && REGEX_UNION_LENS_TRAFFIC_FLAG.matcher(str).find()) {
            return true;
        }
        return false;
    }

    private static String updateUnionLes(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.contains(UNION_LENS_RECOVERY_ID)) {
            str2 = str.replaceAll("(?<=recoveryid:)[^;]+", recoveryId);
        } else {
            str2 = str + ";" + "recoveryid:" + recoveryId;
        }
        if (str2.contains(UNION_LENS_PREPVID)) {
            return str2.replaceAll("(?<=prepvid:)[^;]+", prePvid);
        }
        return str2 + ";" + "prepvid:" + prePvid;
    }

    public static String replaceUrl(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return str;
        }
        return str.replaceAll(Operators.BRACKET_START_STR + str2 + "=[^&]*)", str2 + SymbolExpUtil.SYMBOL_EQUAL + str3);
    }

    public static boolean isUnionLensReport() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, UNION_LENS_REPORT_SWITCH, true);
    }
}
