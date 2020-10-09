package alimama.com.unwviewbase.pullandrefrsh;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ViewCompat {
    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            SDK16.postOnAnimation(view, runnable);
        } else if (view != null && runnable != null) {
            view.postDelayed(runnable, 16);
        }
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            SDK16.setBackground(view, drawable);
        } else if (view != null && drawable != null) {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setLayerType(View view, int i) {
        if (Build.VERSION.SDK_INT >= 11) {
            SDK11.setLayerType(view, i);
        }
    }

    @TargetApi(11)
    static class SDK11 {
        SDK11() {
        }

        public static void setLayerType(View view, int i) {
            if (view != null) {
                view.setLayerType(i, (Paint) null);
            }
        }
    }

    @TargetApi(16)
    static class SDK16 {
        SDK16() {
        }

        public static void postOnAnimation(View view, Runnable runnable) {
            if (view != null && runnable != null) {
                view.postOnAnimation(runnable);
            }
        }

        public static void setBackground(View view, Drawable drawable) {
            if (view != null && drawable != null) {
                view.setBackground(drawable);
            }
        }
    }
}
