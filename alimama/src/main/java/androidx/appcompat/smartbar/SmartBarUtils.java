package androidx.appcompat.smartbar;

import android.app.ActionBar;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Deprecated
public class SmartBarUtils {
    private static final int NEED_INIT = -1;
    private static final int NOT_SUPPORT_SMARTBAR = 0;
    private static final int SUPPORT_SMARTBAR = 1;
    private static boolean sChangeState = false;
    private static int sSupportSamrtBar = -1;

    public static boolean isSupportSmartBar() {
        return false;
    }

    private static void checkSmartBar() {
        try {
            sSupportSamrtBar = ((Boolean) Class.forName("android.os.Build").getMethod("hasSmartBar", new Class[0]).invoke((Object) null, new Object[0])).booleanValue() ? 1 : 0;
        } catch (Exception unused) {
            if (!Build.DEVICE.equals("mx2") || !Build.DISPLAY.contains("Flyme")) {
                sSupportSamrtBar = 0;
            } else {
                sSupportSamrtBar = 1;
            }
        }
    }

    public static void setActionBarViewCollapsable(ActionBar actionBar, boolean z) {
        try {
            Method method = Class.forName("android.app.ActionBar").getMethod("setActionBarViewCollapsable", new Class[]{Boolean.TYPE});
            try {
                method.invoke(actionBar, new Object[]{Boolean.valueOf(z)});
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        } catch (SecurityException e4) {
            e4.printStackTrace();
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        } catch (ClassNotFoundException e6) {
            e6.printStackTrace();
        }
    }

    public static void setChangeState(boolean z) {
        sChangeState = z;
    }

    public static boolean getChangeState() {
        return sChangeState;
    }
}
