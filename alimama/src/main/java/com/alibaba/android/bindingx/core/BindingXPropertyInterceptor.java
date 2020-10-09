package com.alibaba.android.bindingx.core;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.PlatformManager;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BindingXPropertyInterceptor {
    private static BindingXPropertyInterceptor sInstance = new BindingXPropertyInterceptor();
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<IPropertyUpdateInterceptor> mPropertyInterceptors = new CopyOnWriteArrayList<>();
    private final Handler sUIHandler = new Handler(Looper.getMainLooper());

    public interface IPropertyUpdateInterceptor {
        boolean updateView(@Nullable View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr);
    }

    private BindingXPropertyInterceptor() {
    }

    @NonNull
    public static BindingXPropertyInterceptor getInstance() {
        return sInstance;
    }

    public void addInterceptor(@Nullable IPropertyUpdateInterceptor iPropertyUpdateInterceptor) {
        if (iPropertyUpdateInterceptor != null) {
            this.mPropertyInterceptors.add(iPropertyUpdateInterceptor);
        }
    }

    public boolean removeInterceptor(@Nullable IPropertyUpdateInterceptor iPropertyUpdateInterceptor) {
        if (iPropertyUpdateInterceptor != null) {
            return this.mPropertyInterceptors.remove(iPropertyUpdateInterceptor);
        }
        return false;
    }

    public void clear() {
        this.mPropertyInterceptors.clear();
    }

    public void performIntercept(@Nullable View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr) {
        if (!this.mPropertyInterceptors.isEmpty()) {
            final View view2 = view;
            final String str2 = str;
            final Object obj2 = obj;
            final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
            final Map<String, Object> map2 = map;
            final Object[] objArr2 = objArr;
            this.sUIHandler.post(new WeakRunnable(new Runnable() {
                public void run() {
                    Iterator it = BindingXPropertyInterceptor.this.mPropertyInterceptors.iterator();
                    while (it.hasNext()) {
                        ((IPropertyUpdateInterceptor) it.next()).updateView(view2, str2, obj2, iDeviceResolutionTranslator2, map2, objArr2);
                    }
                }
            }));
        }
    }

    public void clearCallbacks() {
        this.sUIHandler.removeCallbacksAndMessages((Object) null);
    }

    @NonNull
    public List<IPropertyUpdateInterceptor> getInterceptors() {
        return Collections.unmodifiableList(this.mPropertyInterceptors);
    }
}
