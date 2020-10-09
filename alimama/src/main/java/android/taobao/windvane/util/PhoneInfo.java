package android.taobao.windvane.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;

import com.taobao.weex.el.parse.Operators;

import java.util.Random;

public class PhoneInfo {
    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";
    public static final String MACADDRESS = "mac_address";

    private static String generateImei() {
        try {
            StringBuilder sb = new StringBuilder();
            long currentTimeMillis = System.currentTimeMillis();
            String l = Long.toString(currentTimeMillis);
            sb.append(l.substring(l.length() - 5));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Build.MODEL.replaceAll(Operators.SPACE_STR, ""));
            while (sb2.length() < 6) {
                sb2.append('0');
            }
            sb.append(sb2.substring(0, 6));
            Random random = new Random(currentTimeMillis);
            long j = 0;
            while (j < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
                j = random.nextLong();
            }
            sb.append(Long.toHexString(j).substring(0, 4));
            return sb.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String getImei(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("imei", 0);
            String string = sharedPreferences.getString("imei", (String) null);
            if (string == null || string.length() == 0) {
                string = generateImei().replaceAll(Operators.SPACE_STR, "").trim();
                while (string.length() < 15) {
                    string = "0" + string;
                }
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("imei", string);
                edit.commit();
            }
            return string.trim();
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String getImsi(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("imei", 0);
        String string = sharedPreferences.getString("imsi", (String) null);
        if (string == null || string.length() == 0) {
            string = generateImei().replaceAll(Operators.SPACE_STR, "").trim();
            while (string.length() < 15) {
                string = "0" + string;
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("imsi", string);
            edit.commit();
        }
        return string;
    }

    public static String getLocalMacAddress(Context context) {
        String macAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        if (macAddress == null || "".equals(macAddress)) {
            return context.getSharedPreferences(MACADDRESS, 0).getString(MACADDRESS, "");
        }
        SharedPreferences.Editor edit = context.getSharedPreferences(MACADDRESS, 0).edit();
        edit.putString(MACADDRESS, macAddress);
        edit.commit();
        return macAddress;
    }

    public static String getOriginalImei(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        return deviceId != null ? deviceId.trim() : deviceId;
    }

    public static String getOriginalImsi(Context context) {
        String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        return subscriberId != null ? subscriberId.trim() : subscriberId;
    }

    public static String getSerialNum() {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{"ro.serialno", "unknown"});
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }
}
