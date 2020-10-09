package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.IUnionLens;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.el.parse.Operators;
import com.ut.device.UTDevice;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class UWNUnionLensImpl implements IUnionLens {
    private static final Pattern REGEX_UNION_LENS_RECOVERY_ID = Pattern.compile("(?<=recoveryid:)[^;]+", 32);
    private static final Pattern REGEX_UNION_LENS_TRAFFIC_FLAG = Pattern.compile("(?<=traffic_flag:)[^;]+", 32);
    private static final String TAG = "UWNUnionLensImpl";
    private static final String UNIONLENS_REPORT_SWITCH = "enable_unionlens_report";
    private static final String UNION_LENS_LOG = "union_lens";
    private static final String UNION_LENS_RECOVERY_ID = "recoveryid";
    private static final String UNION_LENS_TRAFFIC_FLAG = "traffic_flag";
    private static boolean isReport;
    private static String recoveryId;
    private String appKey;
    private boolean isInitialized = false;
    private Context mContext;

    public void init() {
        this.mContext = UNWManager.getInstance().application;
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        if (iSecurity != null) {
            this.appKey = iSecurity.getAppkey();
            generateRecoveryId(this.mContext);
            this.isInitialized = true;
        }
    }

    public void setReportSwitch(boolean z) {
        isReport = z;
    }

    private void generateRecoveryId(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        String replace = UTDevice.getUtdid(context).replace("+", ".");
        int nextInt = new Random(System.nanoTime()).nextInt(Integer.MAX_VALUE);
        recoveryId = replace + "_" + currentTimeMillis + "_" + nextInt + "_" + this.appKey;
    }

    public String getUrlParameter(String str, String str2) {
        try {
            return Uri.parse(str).getQueryParameter(str2);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    public String appendUrlUnionLens(String str) {
        if (!isInit() || !isUnionLensReport() || TextUtils.isEmpty(str)) {
            return str;
        }
        if (!str.startsWith(CommonUtils.HTTP_PRE) && !str.startsWith(Utils.HTTPS_SCHEMA)) {
            return str;
        }
        try {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            String urlParameter = getUrlParameter(str, "union_lens");
            if (Boolean.valueOf(isTrafficFlag(urlParameter)).booleanValue()) {
                generateRecoveryId(this.mContext);
            } else {
                updateRecoveryId(urlParameter);
            }
            if (!TextUtils.isEmpty(urlParameter)) {
                return replaceUrl(str, "union_lens", updateUnionLes(urlParameter));
            }
            buildUpon.appendQueryParameter("union_lens", appendUtUnionLens());
            return buildUpon.toString();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return str;
        }
    }

    public String appendNaUnionLens(Map<String, String> map) {
        if (!isInit() || !isUnionLensReport()) {
            return null;
        }
        if (map == null || map.isEmpty()) {
            return appendUtUnionLens();
        }
        if (!map.containsKey("union_lens")) {
            return appendUtUnionLens();
        }
        if (Boolean.valueOf(isTrafficFlag(map.get("union_lens"))).booleanValue()) {
            generateRecoveryId(this.mContext);
        } else {
            updateRecoveryId(map.get("union_lens"));
        }
        return updateUnionLes(map.get("union_lens"));
    }

    public String appendUtUnionLens() {
        if (!isInit()) {
            return null;
        }
        return "recoveryid:" + recoveryId;
    }

    private void updateRecoveryId(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher matcher = REGEX_UNION_LENS_RECOVERY_ID.matcher(str);
            if (matcher.find()) {
                recoveryId = matcher.group().replace("+", ".");
            }
        }
    }

    private boolean isTrafficFlag(String str) {
        if (!TextUtils.isEmpty(str) && REGEX_UNION_LENS_TRAFFIC_FLAG.matcher(str).find()) {
            return true;
        }
        return false;
    }

    public String updateUnionLes(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.contains("recoveryid")) {
            return str.replaceAll("(?<=recoveryid:)[^;]+", recoveryId);
        }
        return str + ";" + "recoveryid:" + recoveryId;
    }

    public String replaceUrl(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return str;
        }
        return str.replaceAll(Operators.BRACKET_START_STR + str2 + "=[^&#]*)", str2 + SymbolExpUtil.SYMBOL_EQUAL + str3);
    }

    public boolean isUnionLensReport() {
        return isReport;
    }

    public boolean isInit() {
        return this.isInitialized;
    }

    public String getRecoveryId() {
        if (!isInit()) {
            return null;
        }
        return recoveryId;
    }
}
