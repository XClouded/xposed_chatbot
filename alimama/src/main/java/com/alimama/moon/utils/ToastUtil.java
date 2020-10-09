package com.alimama.moon.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.alimama.moon.R;

public class ToastUtil {
    public static void toastForNoNet(Context context) {
        toast(context, (int) R.string.no_net);
    }

    public static void toast(Context context, int i) {
        if (context != null) {
            toast(context, (CharSequence) context.getString(i));
        }
    }

    @AnyThread
    public static void toast(final Context context, final CharSequence charSequence) {
        if (context != null) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                Toast makeText = Toast.makeText(context, charSequence, 0);
                makeText.setGravity(17, 0, 0);
                makeText.show();
                return;
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Toast makeText = Toast.makeText(context, charSequence, 0);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                }
            });
        }
    }

    @MainThread
    public static void showToast(@NonNull Context context, @StringRes int i) {
        showToast(context, context.getString(i));
    }

    @MainThread
    public static void showToast(@NonNull Context context, String str) {
        Toast makeText = Toast.makeText(context, str, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }
}
