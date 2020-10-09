package com.taobao.uikit.extend.component.unify.Toast;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.taobao.uikit.extend.utils.DeviceUtils;

class TBActivityToast extends TBToast {
    private WindowManager.LayoutParams mWindowManagerParams;

    TBActivityToast(@NonNull Activity activity) {
        super(activity);
    }

    public WindowManager.LayoutParams getWindowManagerParams() {
        return this.mWindowManagerParams;
    }

    /* access modifiers changed from: package-private */
    public Activity getActivity() {
        return (Activity) this.mContext;
    }

    public WindowManager getWindowManager() {
        return getActivity().getWindowManager();
    }

    public void show() {
        try {
            if (!DeviceUtils.isHUWEIDevice() || Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT >= 29 || NotificationManagerCompat.from(getActivity()).areNotificationsEnabled()) {
                Toast.makeText(this.mContext, getText(), 0).show();
                return;
            }
            this.mWindowManagerParams = new WindowManager.LayoutParams();
            this.mWindowManagerParams.flags = 8;
            this.mWindowManagerParams.format = -3;
            this.mWindowManagerParams.height = -2;
            this.mWindowManagerParams.width = -2;
            this.mWindowManagerParams.windowAnimations = 16973828;
            this.mWindowManagerParams.gravity = 81;
            this.mWindowManagerParams.x = 0;
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            this.mWindowManagerParams.y = (int) (((float) point.y) * 0.1f);
            getTextView().setMaxLines(20);
            TBToastManager.getInstance().add(this);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
