package com.taobao.weex.ui;

import android.util.Pair;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.bridge.MethodInvoker;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SimpleComponentHolder implements IFComponentHolder {
    public static final String TAG = "SimpleComponentHolder";
    private final Class<? extends WXComponent> mClz;
    private ComponentCreator mCreator;
    private Map<String, Invoker> mMethodInvokers;
    private Map<String, Invoker> mPropertyInvokers;

    public static class ClazzComponentCreator implements ComponentCreator {
        private final Class<? extends WXComponent> mCompClz;
        private Constructor<? extends WXComponent> mConstructor;

        public ClazzComponentCreator(Class<? extends WXComponent> cls) {
            this.mCompClz = cls;
        }

        private void loadConstructor() {
            Constructor<? extends WXComponent> constructor;
            Class<? extends WXComponent> cls = this.mCompClz;
            try {
                constructor = cls.getConstructor(new Class[]{WXSDKInstance.class, WXVContainer.class, BasicComponentData.class});
            } catch (NoSuchMethodException unused) {
                WXLogUtils.d("ClazzComponentCreator", "Use deprecated component constructor");
                try {
                    constructor = cls.getConstructor(new Class[]{WXSDKInstance.class, WXVContainer.class, Boolean.TYPE, BasicComponentData.class});
                } catch (NoSuchMethodException unused2) {
                    try {
                        constructor = cls.getConstructor(new Class[]{WXSDKInstance.class, WXVContainer.class, String.class, Boolean.TYPE, BasicComponentData.class});
                    } catch (NoSuchMethodException unused3) {
                        throw new WXRuntimeException("Can't find constructor of component.");
                    }
                }
            }
            this.mConstructor = constructor;
        }

        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            if (this.mConstructor == null) {
                loadConstructor();
            }
            int length = this.mConstructor.getParameterTypes().length;
            if (length == 3) {
                return (WXComponent) this.mConstructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, basicComponentData});
            } else if (length == 4) {
                return (WXComponent) this.mConstructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, false, basicComponentData});
            } else {
                return (WXComponent) this.mConstructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, wXSDKInstance.getInstanceId(), Boolean.valueOf(wXVContainer.isLazy())});
            }
        }
    }

    public SimpleComponentHolder(Class<? extends WXComponent> cls) {
        this(cls, new ClazzComponentCreator(cls));
    }

    public SimpleComponentHolder(Class<? extends WXComponent> cls, ComponentCreator componentCreator) {
        this.mClz = cls;
        this.mCreator = componentCreator;
    }

    public void loadIfNonLazy() {
        Annotation[] declaredAnnotations = this.mClz.getDeclaredAnnotations();
        int length = declaredAnnotations.length;
        int i = 0;
        while (i < length) {
            Annotation annotation = declaredAnnotations[i];
            if (!(annotation instanceof Component)) {
                i++;
            } else if (!((Component) annotation).lazyload() && this.mMethodInvokers == null) {
                generate();
                return;
            } else {
                return;
            }
        }
    }

    private synchronized void generate() {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("SimpleComponentHolder", "Generate Component:" + this.mClz.getSimpleName());
        }
        Pair<Map<String, Invoker>, Map<String, Invoker>> methods = getMethods(this.mClz);
        this.mPropertyInvokers = (Map) methods.first;
        this.mMethodInvokers = (Map) methods.second;
    }

    public static Pair<Map<String, Invoker>, Map<String, Invoker>> getMethods(Class cls) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        try {
            for (Method method : cls.getMethods()) {
                try {
                    Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                    int length = declaredAnnotations.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        Annotation annotation = declaredAnnotations[i];
                        if (annotation != null) {
                            if (annotation instanceof WXComponentProp) {
                                hashMap.put(((WXComponentProp) annotation).name(), new MethodInvoker(method, true));
                                break;
                            } else if (annotation instanceof JSMethod) {
                                JSMethod jSMethod = (JSMethod) annotation;
                                String alias = jSMethod.alias();
                                if ("_".equals(alias)) {
                                    alias = method.getName();
                                }
                                hashMap2.put(alias, new MethodInvoker(method, jSMethod.uiThread()));
                            }
                        }
                        i++;
                    }
                } catch (ArrayIndexOutOfBoundsException | IncompatibleClassChangeError unused) {
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            WXLogUtils.e("SimpleComponentHolder", (Throwable) e2);
        }
        return new Pair<>(hashMap, hashMap2);
    }

    public synchronized WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WXComponent createInstance;
        createInstance = this.mCreator.createInstance(wXSDKInstance, wXVContainer, basicComponentData);
        createInstance.bindHolder(this);
        return createInstance;
    }

    public synchronized Invoker getPropertyInvoker(String str) {
        if (this.mPropertyInvokers == null) {
            generate();
        }
        return this.mPropertyInvokers.get(str);
    }

    public Invoker getMethodInvoker(String str) {
        if (this.mMethodInvokers == null) {
            generate();
        }
        return this.mMethodInvokers.get(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:10|11|(1:13)|14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r4.mClz != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        r0 = r4.mClz.getName();
        com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT((java.lang.String) null, com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT, com.taobao.weex.bridge.WXBridgeManager.METHOD_REGISTER_COMPONENTS, r0 + ": gen methods failed", (java.util.Map<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0043, code lost:
        return new java.lang.String[1];
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String[] getMethods() {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker> r0 = r4.mMethodInvokers     // Catch:{ all -> 0x0044 }
            if (r0 != 0) goto L_0x0008
            r4.generate()     // Catch:{ all -> 0x0044 }
        L_0x0008:
            java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker> r0 = r4.mMethodInvokers     // Catch:{ all -> 0x0044 }
            java.util.Set r0 = r0.keySet()     // Catch:{ all -> 0x0044 }
            int r1 = r0.size()     // Catch:{ Throwable -> 0x001c }
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ Throwable -> 0x001c }
            java.lang.Object[] r0 = r0.toArray(r1)     // Catch:{ Throwable -> 0x001c }
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Throwable -> 0x001c }
            monitor-exit(r4)
            return r0
        L_0x001c:
            java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent> r0 = r4.mClz     // Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x003f
            java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent> r0 = r4.mClz     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r0.getName()     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0044 }
            r1.<init>()     // Catch:{ all -> 0x0044 }
            r1.append(r0)     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = ": gen methods failed"
            r1.append(r0)     // Catch:{ all -> 0x0044 }
            java.lang.String r0 = r1.toString()     // Catch:{ all -> 0x0044 }
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT     // Catch:{ all -> 0x0044 }
            java.lang.String r2 = "registerComponents"
            r3 = 0
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r3, r1, r2, r0, r3)     // Catch:{ all -> 0x0044 }
        L_0x003f:
            r0 = 1
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ all -> 0x0044 }
            monitor-exit(r4)
            return r0
        L_0x0044:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.SimpleComponentHolder.getMethods():java.lang.String[]");
    }
}
