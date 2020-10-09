package alimama.com.unwviewbase.tool;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

public class DensityUtil {
    private static WindowManager mWindowManager;
    private static int screenHeight;
    private static int screenWidth;

    public static int dip2px(Context context, float f) {
        if (context == null) {
            return 0;
        }
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp2px(Context context, float f) {
        return (int) (TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics()) + 0.5f);
    }
}
