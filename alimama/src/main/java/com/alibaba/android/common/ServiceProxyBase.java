package com.alibaba.android.common;

import android.content.Context;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class ServiceProxyBase implements ServiceProxy {
    protected final String TEMP_SERVICE_PREFIX;
    protected Context applicationContext;
    protected ServiceProxy parentProxy;
    private HashMap<String, Object> serviceMap;
    private HashMap<String, WeakReference> temporaryServiceMap;

    /* access modifiers changed from: protected */
    public abstract Object createServiceDelegate(String str);

    public ServiceProxyBase(ServiceProxy serviceProxy) {
        this(serviceProxy, (Context) null);
    }

    public ServiceProxyBase(ServiceProxy serviceProxy, Context context) {
        this.TEMP_SERVICE_PREFIX = "temp_";
        this.serviceMap = new HashMap<>();
        this.temporaryServiceMap = new HashMap<>();
        this.parentProxy = serviceProxy;
        this.applicationContext = context;
    }

    public ServiceProxy getParent() {
        return this.parentProxy;
    }

    /* access modifiers changed from: protected */
    public boolean isTemporaryService(String str) {
        return str != null && str.startsWith("temp_");
    }

    public Object getService(String str) {
        Object obj;
        Object obj2 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (isTemporaryService(str)) {
            WeakReference weakReference = this.temporaryServiceMap.get(str);
            if (weakReference == null || weakReference.get() == null) {
                synchronized (this.temporaryServiceMap) {
                    if (weakReference != null) {
                        try {
                            if (weakReference.get() == null) {
                            }
                            obj = obj2;
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    obj2 = createServiceDelegate(str);
                    if (obj2 != null) {
                        this.temporaryServiceMap.put(str, new WeakReference(obj2));
                    }
                    obj = obj2;
                }
            } else {
                obj = weakReference.get();
            }
        } else {
            obj = this.serviceMap.get(str);
            if (obj == null) {
                synchronized (this.serviceMap) {
                    if (obj == null) {
                        try {
                            obj = createServiceDelegate(str);
                            if (obj != null) {
                                this.serviceMap.put(str, obj);
                            }
                        } catch (Throwable th2) {
                            throw th2;
                        }
                    }
                }
            }
        }
        return (obj != null || this.parentProxy == null) ? obj : this.parentProxy.getService(str);
    }

    public void setApplicationContext(Context context) {
        this.applicationContext = context;
    }

    public Context getApplicationContext() {
        if (this.applicationContext != null || this.parentProxy == null) {
            return this.applicationContext;
        }
        return this.parentProxy.getApplicationContext();
    }
}
