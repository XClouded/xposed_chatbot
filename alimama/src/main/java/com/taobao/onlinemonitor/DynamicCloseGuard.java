package com.taobao.onlinemonitor;

import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DynamicCloseGuard extends BaseDynamicProxy {
    public DynamicCloseGuard(OnLineMonitor onLineMonitor) {
        super(onLineMonitor);
    }

    public void doProxy() {
        try {
            Class<?> cls = Class.forName("dalvik.system.CloseGuard");
            Field declaredField = cls.getDeclaredField("REPORTER");
            Field declaredField2 = cls.getDeclaredField("ENABLED");
            declaredField.setAccessible(true);
            declaredField2.setAccessible(true);
            declaredField2.setBoolean(cls, true);
            declaredField.set(cls, newProxyInstance(declaredField.get(cls)));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        String str;
        try {
            if (this.mOnLineMonitor == null) {
                return null;
            }
            if (objArr[1] != null) {
                OnLineMonitor onLineMonitor = this.mOnLineMonitor;
                str = OnLineMonitor.getStackTraceElement(objArr[1].getStackTrace(), 2, 15);
            } else {
                str = null;
            }
            if (this.mOnLineMonitor.mOnlineStatistics != null) {
                int size = this.mOnLineMonitor.mOnlineStatistics.size();
                for (int i = 0; i < size; i++) {
                    OnlineStatistics onlineStatistics = this.mOnLineMonitor.mOnlineStatistics.get(i);
                    if (onlineStatistics != null) {
                        onlineStatistics.onBlockOrCloseGuard(this.mOnLineMonitor.mOnLineStat, 1, "Close", this.mOnLineMonitor.mActivityName, Thread.currentThread().getName(), str, 0);
                    }
                }
            }
            if (OnLineMonitor.sIsTraceDetail && str != null) {
                Log.e("OnLineMonitor", "未及时关闭的句柄：\n" + str.replace("</br>", "\n"));
            }
            if (OnLineMonitor.sIsTraceDetail && this.mOnLineMonitor.mTraceDetail.mCloseGuardInfo != null && !TextUtils.isEmpty(str)) {
                Integer num = this.mOnLineMonitor.mTraceDetail.mCloseGuardInfo.get(str);
                if (num != null) {
                    this.mOnLineMonitor.mTraceDetail.mCloseGuardInfo.put(str, Integer.valueOf(num.intValue() + 1));
                } else {
                    this.mOnLineMonitor.mTraceDetail.mCloseGuardInfo.put(str, 1);
                }
            }
            return method.invoke(this.mTargetObject, objArr);
        } catch (Throwable unused) {
            return null;
        }
    }
}
