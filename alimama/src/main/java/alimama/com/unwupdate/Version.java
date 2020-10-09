package alimama.com.unwupdate;

import android.os.Build;
import android.os.StrictMode;

public class Version {
    private Version() {
    }

    public static void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        if (hasJellyBean()) {
            builder.detectLeakedRegistrationObjects();
        }
        if (Build.VERSION.SDK_INT >= 18) {
            builder.detectFileUriExposure();
        }
        builder.detectLeakedClosableObjects().detectLeakedSqlLiteObjects().penaltyLog();
        StrictMode.setVmPolicy(builder.build());
    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;
    }

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= 15;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean hasJellyBean_17() {
        return Build.VERSION.SDK_INT >= 17;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean hasPie() {
        return Build.VERSION.SDK_INT >= 29;
    }
}
