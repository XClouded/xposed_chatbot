package com.taobao.weex;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.taobao.orange.OConstant;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WXSecurityGuardPageTrack {
    private static final String TAG = "WXSecurityGuardPageTrack";
    private static final int TASK_LIMIT = 200;
    /* access modifiers changed from: private */
    public static Object mPageTrack;
    /* access modifiers changed from: private */
    public static Executor mPageTrackHandler = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public static volatile AtomicInteger taskCount = new AtomicInteger();

    public static WXSecurityGuardPageTrack createInstance(Context context) {
        Method method;
        Class<?> cls;
        try {
            Class<?> cls2 = Class.forName(OConstant.REFLECT_SECURITYGUARD);
            if (!(cls2 == null || (method = cls2.getMethod("getInstance", new Class[]{Context.class})) == null || (cls = Class.forName("com.alibaba.wireless.security.open.securitybodysdk.ISecurityBodyPageTrack")) == null)) {
                Object invoke = method.invoke((Object) null, new Object[]{context});
                Method method2 = cls2.getMethod("getInterface", new Class[]{Class.class});
                if (!(invoke == null || method2 == null)) {
                    WXSecurityGuardPageTrack wXSecurityGuardPageTrack = new WXSecurityGuardPageTrack();
                    mPageTrack = method2.invoke(invoke, new Object[]{cls});
                    return wXSecurityGuardPageTrack;
                }
            }
        } catch (Exception unused) {
            WXLogUtils.e(TAG, "[PageTrackLog]createInstance error");
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static Class getPageTrackClass() {
        try {
            return Class.forName("com.alibaba.wireless.security.open.securitybodysdk.ISecurityBodyPageTrack");
        } catch (Exception unused) {
            WXLogUtils.e(TAG, "[PageTrackLog]getPageTrackClass error");
            return null;
        }
    }

    public void addTouchEventReflection(final String str, final MotionEvent motionEvent) {
        if ((motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 1) && taskCount.get() <= 200 && mPageTrackHandler != null) {
            mPageTrackHandler.execute(new Runnable() {
                public void run() {
                    Class access$100;
                    if (!(WXSecurityGuardPageTrack.mPageTrack == null || (access$100 = WXSecurityGuardPageTrack.getPageTrackClass()) == null)) {
                        try {
                            Method method = access$100.getMethod("addTouchEvent", new Class[]{String.class, MotionEvent.class});
                            if (method != null) {
                                method.invoke(WXSecurityGuardPageTrack.mPageTrack, new Object[]{str, motionEvent});
                            }
                        } catch (Exception unused) {
                            WXLogUtils.e(WXSecurityGuardPageTrack.TAG, "[PageTrackLog]addTouchEventReflection error");
                        }
                    }
                    WXSecurityGuardPageTrack.taskCount.getAndDecrement();
                }
            });
            taskCount.getAndIncrement();
        }
    }

    public void onPageStartReflection(String str) {
        Class pageTrackClass;
        if (mPageTrack != null && (pageTrackClass = getPageTrackClass()) != null) {
            try {
                Method method = pageTrackClass.getMethod("onPageStart", new Class[]{String.class});
                if (method != null) {
                    method.invoke(mPageTrack, new Object[]{str});
                }
            } catch (Exception unused) {
                WXLogUtils.e(TAG, "[PageTrackLog]onPageStartReflection error");
            }
        }
    }

    public void onPageDestroyReflection(String str) {
        Class pageTrackClass;
        if (mPageTrack != null && (pageTrackClass = getPageTrackClass()) != null) {
            try {
                Method method = pageTrackClass.getMethod("onPageDestroy", new Class[]{String.class});
                if (method != null) {
                    method.invoke(mPageTrack, new Object[]{str});
                }
            } catch (Exception unused) {
                WXLogUtils.e(TAG, "[PageTrackLog]oonPageDestroyReflection error");
            }
        }
    }

    public void addKeyEventReflection(String str, KeyEvent keyEvent) {
        Class pageTrackClass;
        if (mPageTrack != null && (pageTrackClass = getPageTrackClass()) != null) {
            try {
                Method method = pageTrackClass.getMethod("addKeyEvent", new Class[]{String.class, KeyEvent.class});
                if (method != null) {
                    method.invoke(mPageTrack, new Object[]{str, keyEvent});
                }
            } catch (Exception unused) {
                WXLogUtils.e(TAG, "[PageTrackLog]addKeyEventReflection error");
            }
        }
    }

    public static class OnWXScrollListenerEX implements OnWXScrollListener {
        Context mContext = null;
        int mLastState = 0;
        int mLastY = 0;
        int mSumX = 0;
        int mSumY = 0;
        String mUrl = null;

        public OnWXScrollListenerEX(String str, Context context) {
            this.mUrl = str;
            this.mContext = context;
        }

        /* access modifiers changed from: private */
        public void addScrollEventReflection(Object obj, String str, int i, int i2, int i3, int i4) {
            Class access$100 = WXSecurityGuardPageTrack.getPageTrackClass();
            if (access$100 != null) {
                try {
                    Method method = access$100.getMethod("addScrollEvent", new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
                    if (method != null) {
                        Object obj2 = obj;
                        method.invoke(obj, new Object[]{str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)});
                    }
                } catch (Exception unused) {
                    WXLogUtils.e(WXSecurityGuardPageTrack.TAG, "[PageTrackLog]addScrollEventReflection error");
                }
            }
        }

        public void onScrolled(View view, int i, int i2) {
            this.mSumX += i;
            this.mSumY += i2;
        }

        public void onScrollStateChanged(View view, int i, int i2, final int i3) {
            if (WXSecurityGuardPageTrack.mPageTrackHandler != null && WXSecurityGuardPageTrack.taskCount.get() <= 200) {
                WXSecurityGuardPageTrack.mPageTrackHandler.execute(new Runnable() {
                    public void run() {
                        if (i3 == 1) {
                            OnWXScrollListenerEX.this.mLastState = i3;
                        }
                        if (i3 == 0 && OnWXScrollListenerEX.this.mLastState == 1) {
                            try {
                                if (WXSecurityGuardPageTrack.mPageTrack != null) {
                                    int i = OnWXScrollListenerEX.this.mLastY;
                                    int i2 = OnWXScrollListenerEX.this.mSumX + 0;
                                    int i3 = OnWXScrollListenerEX.this.mSumY + i;
                                    OnWXScrollListenerEX.this.addScrollEventReflection(WXSecurityGuardPageTrack.mPageTrack, OnWXScrollListenerEX.this.mUrl, i2, i3, 0, i);
                                    OnWXScrollListenerEX.this.mLastY = i3;
                                    OnWXScrollListenerEX.this.mSumX = 0;
                                    OnWXScrollListenerEX.this.mSumY = 0;
                                    OnWXScrollListenerEX.this.mLastState = i3;
                                }
                            } catch (Exception unused) {
                                WXLogUtils.e(WXSecurityGuardPageTrack.TAG, "[PageTrackLog]onScrollStateChanged error");
                            }
                        }
                        WXSecurityGuardPageTrack.taskCount.getAndDecrement();
                    }
                });
                WXSecurityGuardPageTrack.taskCount.getAndIncrement();
            }
        }
    }
}
