package com.taobao.weex;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.uc.webview.export.media.MessageID;

public final class WXAnalyzerDelegate {
    private Object mWXAnalyzer;

    public WXAnalyzerDelegate(@Nullable Context context) {
        if (context != null) {
            try {
                this.mWXAnalyzer = Class.forName("com.taobao.weex.analyzer.WeexDevOptions").getDeclaredConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            } catch (Exception unused) {
            }
        }
    }

    public void registerExtraOption(String str, int i, Runnable runnable) {
        if (this.mWXAnalyzer != null && !TextUtils.isEmpty(str) && runnable != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod("registerExtraOption", new Class[]{String.class, Integer.TYPE, Runnable.class}).invoke(this.mWXAnalyzer, new Object[]{str, Integer.valueOf(i), runnable});
            } catch (Exception unused) {
            }
        }
    }

    public void onReceiveTouchEvent(MotionEvent motionEvent) {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod("onReceiveTouchEvent", new Class[]{MotionEvent.class}).invoke(this.mWXAnalyzer, new Object[]{motionEvent});
            } catch (Exception unused) {
            }
        }
    }

    public void onCreate() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod(UmbrellaConstants.LIFECYCLE_CREATE, new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onStart() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod(UmbrellaConstants.LIFECYCLE_START, new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onResume() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod(UmbrellaConstants.LIFECYCLE_RESUME, new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onPause() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod(MessageID.onPause, new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onStop() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod(MessageID.onStop, new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onDestroy() {
        if (this.mWXAnalyzer != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod("onDestroy", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception unused) {
            }
        }
    }

    public void onWeexRenderSuccess(@Nullable WXSDKInstance wXSDKInstance) {
        if (this.mWXAnalyzer != null && wXSDKInstance != null) {
            try {
                this.mWXAnalyzer.getClass().getDeclaredMethod("onWeexRenderSuccess", new Class[]{WXSDKInstance.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance});
            } catch (Exception unused) {
            }
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mWXAnalyzer == null) {
            return false;
        }
        try {
            return ((Boolean) this.mWXAnalyzer.getClass().getDeclaredMethod("onKeyUp", new Class[]{Integer.TYPE, KeyEvent.class}).invoke(this.mWXAnalyzer, new Object[]{Integer.valueOf(i), keyEvent})).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.mWXAnalyzer != null) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
                try {
                    this.mWXAnalyzer.getClass().getDeclaredMethod("onException", new Class[]{WXSDKInstance.class, String.class, String.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance, str, str2});
                } catch (Exception unused) {
                }
            }
        }
    }

    public View onWeexViewCreated(WXSDKInstance wXSDKInstance, View view) {
        if (this.mWXAnalyzer == null || wXSDKInstance == null || view == null) {
            return null;
        }
        try {
            return (View) this.mWXAnalyzer.getClass().getDeclaredMethod("onWeexViewCreated", new Class[]{WXSDKInstance.class, View.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance, view});
        } catch (Exception unused) {
            return view;
        }
    }
}
