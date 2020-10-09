package alimama.com.unwbase.tools;

import android.os.Bundle;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class UNWLog {
    private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final char HORIZONTAL_LINE = '│';
    public static final String LOGTAG = "UNWLog";
    private static final String MIDDLE_BORDER = "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final char MIDDLE_CORNER = '├';
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final char TOP_LEFT_CORNER = '┌';
    public static boolean logable = true;

    public static void message(String str) {
        message(LOGTAG, str);
    }

    public static void message(String str, String str2) {
        if (logable) {
            Log.d("UNWLog_" + str, str2);
        }
    }

    public static void error(String str) {
        error(LOGTAG, str);
    }

    public static void error(String str, String str2) {
        if (logable) {
            Log.e("UNWLog_" + str, str2);
        }
    }

    public static void printJsonObject(JSONObject jSONObject) {
        printJsonObject(LOGTAG, jSONObject);
    }

    public static void printJsonObject(String str, JSONObject jSONObject) {
        if (logable && jSONObject != null) {
            try {
                message(str, jSONObject.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printBundle(String str, Bundle bundle) {
        if (logable) {
            if (bundle == null) {
                message(str, "");
                return;
            }
            String[] strArr = new String[bundle.keySet().size()];
            int i = 0;
            for (String str2 : bundle.keySet()) {
                strArr[i] = "key:" + str2 + "   " + "value:" + bundle.get(str2).toString();
                i++;
            }
            array(str, strArr);
        }
    }

    public static void printBundle(Bundle bundle) {
        printBundle(LOGTAG, bundle);
    }

    public static void array(String... strArr) {
        array(LOGTAG, strArr);
    }

    public static void array(String str, String... strArr) {
        if (logable) {
            message(str, TOP_BORDER);
            for (int i = 0; i < strArr.length; i++) {
                message(str, HORIZONTAL_LINE + strArr[i]);
                if (i != strArr.length - 1) {
                    message(str, MIDDLE_BORDER);
                }
            }
            message(str, BOTTOM_BORDER);
        }
    }
}
