package android.taobao.windvane.webview;

import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;

import java.lang.reflect.Method;

public class WVRenderPolicy {
    public static boolean shouldDisableHardwareRenderInLayer() {
        return (Build.MODEL != null && Build.MODEL.contains("GT-I95") && Build.MANUFACTURER != null && Build.MANUFACTURER.equals("samsung")) && (Build.VERSION.SDK_INT == 18);
    }

    public static void disableAccessibility(Context context) {
        if (Build.VERSION.SDK_INT == 17 && context != null) {
            try {
                AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
                if (accessibilityManager.isEnabled()) {
                    Method declaredMethod = accessibilityManager.getClass().getDeclaredMethod("setState", new Class[]{Integer.TYPE});
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(accessibilityManager, new Object[]{0});
                }
            } catch (Throwable unused) {
            }
        }
    }
}
