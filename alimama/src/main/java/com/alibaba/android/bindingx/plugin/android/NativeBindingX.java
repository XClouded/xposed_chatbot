package com.alibaba.android.bindingx.plugin.android;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.alibaba.android.bindingx.core.internal.Utils;
import com.alibaba.android.bindingx.plugin.android.model.BindingXPropSpec;
import com.alibaba.android.bindingx.plugin.android.model.BindingXSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NativeBindingX {
    private BindingXCore mBindingXCore;

    public static NativeBindingX create() {
        return new NativeBindingX((NativeViewFinder) null, (PlatformManager.IDeviceResolutionTranslator) null, (INativeViewUpdater) null, (PlatformManager.IScrollFactory) null);
    }

    public static NativeBindingX create(@Nullable NativeViewFinder nativeViewFinder) {
        return new NativeBindingX(nativeViewFinder, (PlatformManager.IDeviceResolutionTranslator) null, (INativeViewUpdater) null, (PlatformManager.IScrollFactory) null);
    }

    public static NativeBindingX create(@Nullable NativeViewFinder nativeViewFinder, @Nullable PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        return new NativeBindingX(nativeViewFinder, iDeviceResolutionTranslator, (INativeViewUpdater) null, (PlatformManager.IScrollFactory) null);
    }

    public static NativeBindingX create(@Nullable NativeViewFinder nativeViewFinder, @Nullable PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @Nullable INativeViewUpdater iNativeViewUpdater) {
        return new NativeBindingX(nativeViewFinder, iDeviceResolutionTranslator, iNativeViewUpdater, (PlatformManager.IScrollFactory) null);
    }

    public static NativeBindingX create(@Nullable NativeViewFinder nativeViewFinder, @Nullable PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @Nullable INativeViewUpdater iNativeViewUpdater, @Nullable PlatformManager.IScrollFactory iScrollFactory) {
        return new NativeBindingX(nativeViewFinder, iDeviceResolutionTranslator, iNativeViewUpdater, iScrollFactory);
    }

    private NativeBindingX(@Nullable NativeViewFinder nativeViewFinder, @Nullable PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @Nullable INativeViewUpdater iNativeViewUpdater, @Nullable PlatformManager.IScrollFactory iScrollFactory) {
        this.mBindingXCore = new BindingXCore(createPlatformManager(new ViewFinderProxy(nativeViewFinder == null ? new NativeViewFinder() {
            public View findViewBy(View view, String str) {
                int i;
                if (view == null || TextUtils.isEmpty(str)) {
                    return null;
                }
                try {
                    i = Integer.parseInt(str);
                } catch (Throwable unused) {
                    Context context = view.getContext();
                    i = context.getResources().getIdentifier(str, "id", context.getPackageName());
                }
                if (i > 0) {
                    return view.findViewById(i);
                }
                return null;
            }
        } : nativeViewFinder), iDeviceResolutionTranslator == null ? new PlatformManager.IDeviceResolutionTranslator() {
            public double nativeToWeb(double d, Object... objArr) {
                return d;
            }

            public double webToNative(double d, Object... objArr) {
                return d;
            }
        } : iDeviceResolutionTranslator, iNativeViewUpdater == null ? new PlatformManager.IViewUpdater() {
            public void synchronouslyUpdateViewOnUIThread(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr) {
                NativeViewUpdateService.findUpdater(str).update(view, str, obj, iDeviceResolutionTranslator, map);
            }
        } : new ViewUpdaterProxy(iNativeViewUpdater), iScrollFactory));
        this.mBindingXCore.registerEventHandler("scroll", new BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXScrollHandler(context, platformManager, objArr);
            }
        });
    }

    public boolean registerEventHandler(String str, BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager> objectCreator) {
        if (this.mBindingXCore == null) {
            return false;
        }
        this.mBindingXCore.registerEventHandler(str, objectCreator);
        return true;
    }

    public Map<String, Object> bind(View view, BindingXSpec bindingXSpec, NativeCallback nativeCallback) {
        if (bindingXSpec != null) {
            return bind(view, resolveParams(bindingXSpec), nativeCallback);
        }
        LogProxy.e("params invalid. bindingX spec is null");
        return Collections.emptyMap();
    }

    public Map<String, Object> bind(View view, Map<String, Object> map, final NativeCallback nativeCallback) {
        if (view == null) {
            LogProxy.e("params invalid. view is null");
            return Collections.emptyMap();
        }
        if (map == null) {
            map = Collections.emptyMap();
        }
        String doBind = this.mBindingXCore.doBind(view.getContext(), (String) null, map, new BindingXCore.JavaScriptCallback() {
            public void callback(Object obj) {
                if (nativeCallback != null && (obj instanceof Map)) {
                    nativeCallback.callback((Map) obj);
                }
            }
        }, view);
        HashMap hashMap = new HashMap(2);
        hashMap.put("token", doBind);
        hashMap.put(BindingXConstants.KEY_EVENT_TYPE, Utils.getStringValue(map, BindingXConstants.KEY_EVENT_TYPE));
        return hashMap;
    }

    public void unbind(Map<String, Object> map) {
        if (this.mBindingXCore != null) {
            this.mBindingXCore.doUnbind(map);
        }
    }

    public void unbindAll() {
        if (this.mBindingXCore != null) {
            this.mBindingXCore.doRelease();
        }
    }

    public void onActivityPause() {
        if (this.mBindingXCore != null) {
            this.mBindingXCore.onActivityPause();
        }
    }

    public void onActivityResume() {
        if (this.mBindingXCore != null) {
            this.mBindingXCore.onActivityResume();
        }
    }

    public void onDestroy() {
        if (this.mBindingXCore != null) {
            this.mBindingXCore.doRelease();
            this.mBindingXCore = null;
            NativeViewUpdateService.clearCallbacks();
        }
    }

    private PlatformManager createPlatformManager(@NonNull PlatformManager.IViewFinder iViewFinder, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull PlatformManager.IViewUpdater iViewUpdater, @Nullable PlatformManager.IScrollFactory iScrollFactory) {
        return new PlatformManager.Builder().withViewFinder(iViewFinder).withDeviceResolutionTranslator(iDeviceResolutionTranslator).withViewUpdater(iViewUpdater).withScrollFactory(iScrollFactory).build();
    }

    private Map<String, Object> resolveParams(BindingXSpec bindingXSpec) {
        HashMap hashMap = new HashMap();
        hashMap.put(BindingXConstants.KEY_EVENT_TYPE, bindingXSpec.eventType);
        hashMap.put(BindingXConstants.KEY_ANCHOR, bindingXSpec.anchor);
        hashMap.put("options", bindingXSpec.options);
        if (bindingXSpec.exitExpression != null && ExpressionPair.isValid(bindingXSpec.exitExpression)) {
            HashMap hashMap2 = new HashMap(2);
            hashMap2.put(BindingXConstants.KEY_ORIGIN, bindingXSpec.exitExpression.origin);
            hashMap2.put("transformed", bindingXSpec.exitExpression.transformed);
            hashMap.put("exitExpression", hashMap2);
        }
        LinkedList linkedList = new LinkedList();
        for (BindingXPropSpec next : bindingXSpec.expressionProps) {
            HashMap hashMap3 = new HashMap();
            hashMap3.put("property", next.property);
            hashMap3.put("element", next.element);
            HashMap hashMap4 = new HashMap(2);
            if (next.expressionPair != null && ExpressionPair.isValid(next.expressionPair)) {
                hashMap4.put(BindingXConstants.KEY_ORIGIN, next.expressionPair.origin);
                hashMap4.put("transformed", next.expressionPair.transformed);
            }
            hashMap3.put("expression", hashMap4);
            linkedList.add(hashMap3);
        }
        hashMap.put("props", linkedList);
        return hashMap;
    }

    static class ViewFinderProxy implements PlatformManager.IViewFinder {
        private NativeViewFinder mNativeViewFinder;

        ViewFinderProxy(@NonNull NativeViewFinder nativeViewFinder) {
            this.mNativeViewFinder = nativeViewFinder;
        }

        @Nullable
        public View findViewBy(String str, Object... objArr) {
            if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof View)) {
                return null;
            }
            return this.mNativeViewFinder.findViewBy(objArr[0], str);
        }
    }

    static class ViewUpdaterProxy implements PlatformManager.IViewUpdater {
        private INativeViewUpdater mNativeViewUpdater;

        ViewUpdaterProxy(@NonNull INativeViewUpdater iNativeViewUpdater) {
            this.mNativeViewUpdater = iNativeViewUpdater;
        }

        public void synchronouslyUpdateViewOnUIThread(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr) {
            if (this.mNativeViewUpdater != null) {
                this.mNativeViewUpdater.update(view, str, obj, iDeviceResolutionTranslator, map);
            }
        }
    }
}
