package com.taobao.uikit.extend.component.unify.Toast;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.feature.view.TIconFontTextView;
import com.taobao.uikit.extend.utils.DeviceUtils;
import com.taobao.uikit.extend.utils.PermissionHelper;

public class TBToast {
    private static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_FADE_DURATION = 230;
    private static final String ERROR_CONTEXTNULL = " - You cannot use a null context.";
    private static final String ERROR_DURATIONTOOLONG = " - You should NEVER specify a duration greater than four and a half seconds for a TBToast.";
    private static final String TAG = "TBToast";
    protected View mAnimView;
    protected Context mContext;
    private long mDuration = Duration.MEDIUM;
    private int mGravity = 81;
    private TIconFontTextView mIconView;
    protected boolean mLongClicked = false;
    private TextView mMessage2TextView;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private int mXOffset = 0;
    private int mYOffset = 0;

    public static class Duration {
        public static final long EXTRA_LONG = 4500;
        public static final long LONG = 3500;
        public static final long MEDIUM = 3000;
        public static final long SHORT = 2000;
        public static final long VERY_SHORT = 1500;
    }

    public interface OnClickListener {
        void onClick(View view, Parcelable parcelable);
    }

    public TBToast(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mToastView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.uik_toast, (ViewGroup) null);
            this.mToastView.setClickable(false);
            this.mWindowManager = (WindowManager) this.mToastView.getContext().getApplicationContext().getSystemService("window");
            this.mAnimView = this.mToastView.findViewById(R.id.uik_toast);
            this.mMessageTextView = (TextView) this.mToastView.findViewById(R.id.uik_toast_message);
            this.mMessage2TextView = (TextView) this.mToastView.findViewById(R.id.uik_toast_message2);
            this.mIconView = (TIconFontTextView) this.mToastView.findViewById(R.id.uik_toast_icon);
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            this.mYOffset = (int) (((float) point.y) * 0.1f);
            return;
        }
        throw new IllegalArgumentException("TBToast - You cannot use a null context.");
    }

    public void show() {
        this.mWindowManagerParams = new WindowManager.LayoutParams();
        this.mWindowManagerParams.height = -2;
        this.mWindowManagerParams.width = -2;
        this.mWindowManagerParams.flags = 262312;
        this.mWindowManagerParams.format = -3;
        this.mWindowManagerParams.windowAnimations = R.style.uik_toastAnim;
        this.mWindowManagerParams.type = 2005;
        this.mWindowManagerParams.gravity = this.mGravity;
        this.mWindowManagerParams.x = this.mXOffset;
        this.mWindowManagerParams.y = this.mYOffset;
        if (DeviceUtils.isMeizuDevice() && !PermissionHelper.isMezuFloatWindowOpAllowed(this.mContext)) {
            Toast.makeText(this.mContext, getText(), 0).show();
        } else if (DeviceUtils.isMIUIDevice() && !PermissionHelper.isMiuiFloatWindowOpAllowed(this.mContext)) {
            Toast.makeText(this.mContext, getText(), 0).show();
        } else if (Build.VERSION.SDK_INT >= 23) {
            Toast.makeText(this.mContext, getText(), 0).show();
        } else {
            TBToastManager.getInstance().add(this);
        }
    }

    public void setText(CharSequence charSequence) {
        if (this.mMessageTextView != null) {
            this.mMessageTextView.setText(charSequence);
        }
    }

    public TBToast setText2(CharSequence charSequence) {
        if (this.mMessage2TextView != null && !TextUtils.isEmpty(charSequence)) {
            this.mMessage2TextView.setText(charSequence);
            this.mMessage2TextView.setVisibility(0);
        }
        return this;
    }

    public TBToast setToastIcon(int i) {
        if (this.mContext != null) {
            setToastIcon(this.mContext.getText(i).toString());
        }
        return this;
    }

    public TBToast setToastIcon(String str) {
        if (!(this.mIconView == null || this.mContext == null || TextUtils.isEmpty(str))) {
            this.mIconView.setText(str);
            this.mIconView.setVisibility(0);
        }
        return this;
    }

    public TBToast setToastIconColor(int i) {
        if (this.mIconView != null) {
            this.mIconView.setTextColor(i);
        }
        return this;
    }

    public CharSequence getText() {
        return this.mMessageTextView.getText();
    }

    public void setTextColor(int i) {
        this.mMessageTextView.setTextColor(i);
    }

    public int getTextColor() {
        return this.mMessageTextView.getCurrentTextColor();
    }

    public void setTextSize(int i) {
        this.mMessageTextView.setTextSize((float) i);
    }

    public float getTextSize() {
        return this.mMessageTextView.getTextSize();
    }

    public TBToast setDuration(long j) {
        if (j > Duration.EXTRA_LONG) {
            Log.e(TAG, "TBToast - You should NEVER specify a duration greater than four and a half seconds for a TBToast.");
            this.mDuration = Duration.EXTRA_LONG;
        } else {
            this.mDuration = j;
        }
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setGravity(int i, int i2, int i3) {
        this.mGravity = i;
        this.mXOffset = i2;
        this.mYOffset = i3;
    }

    public void dismiss() {
        TBToastManager.getInstance().removeTBToast(this);
    }

    public TextView getTextView() {
        return this.mMessageTextView;
    }

    public View getView() {
        return this.mToastView;
    }

    public boolean isShowing() {
        return this.mToastView != null && this.mToastView.isShown();
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public WindowManager.LayoutParams getWindowManagerParams() {
        return this.mWindowManagerParams;
    }

    public static TBToast makeText(@NonNull Context context, CharSequence charSequence) {
        return makeText(context, charSequence, Duration.MEDIUM);
    }

    public static TBToast makeText(@NonNull Context context, CharSequence charSequence, long j) {
        if (!(context instanceof Activity) || !DeviceUtils.isHUWEIDevice() || Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT >= 29 || NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            TBToast tBToast = new TBToast(context);
            tBToast.setText(charSequence);
            tBToast.setDuration(j);
            return tBToast;
        }
        TBActivityToast tBActivityToast = new TBActivityToast((Activity) context);
        tBActivityToast.setText(charSequence);
        tBActivityToast.setDuration(Duration.MEDIUM);
        return tBActivityToast;
    }

    public static void cancelAllTBToast() {
        TBToastManager.getInstance().cancelAllTBToasts();
    }
}
