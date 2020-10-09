package com.taobao.aipc.core.sender;

import android.content.Context;
import com.taobao.aipc.annotation.parameter.Background;
import com.taobao.aipc.annotation.parameter.InOut;
import com.taobao.aipc.annotation.parameter.Out;
import com.taobao.aipc.annotation.parameter.WeakRef;
import com.taobao.aipc.core.channel.Channel;
import com.taobao.aipc.core.entity.Message;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.intf.IIPcDataFlow;
import com.taobao.aipc.utils.CallbackManager;
import com.taobao.aipc.utils.TimeStampGenerator;
import com.taobao.aipc.utils.TypeCenter;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class Sender implements ISender {
    private static final CallbackManager CALLBACK_MANAGER = CallbackManager.getInstance();
    protected static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();
    private static final CopyOnWriteArraySet<String> methodRegisterCache = new CopyOnWriteArraySet<>();
    protected ObjectWrapper mObject;
    protected String mTimeStamp;

    public Sender(ObjectWrapper objectWrapper) {
        this.mObject = objectWrapper;
    }

    private void registerCallbackMethodParameterTypes(Class<?> cls) {
        for (Method parameterTypes : cls.getMethods()) {
            for (Class register : parameterTypes.getParameterTypes()) {
                TYPE_CENTER.register(register);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void registerClass(Method method) {
        if (method != null) {
            String genericString = method.toGenericString();
            if (!methodRegisterCache.contains(genericString)) {
                for (Class cls : method.getParameterTypes()) {
                    if (cls.isInterface()) {
                        TYPE_CENTER.register(cls);
                        registerCallbackMethodParameterTypes(cls);
                    }
                }
                TYPE_CENTER.register(method.getReturnType());
                methodRegisterCache.add(genericString);
            }
        }
    }

    /* access modifiers changed from: protected */
    public ParameterWrapper[] getParameterWrappers(Method method, Object[] objArr) throws IPCException {
        ParameterWrapper[] parameterWrapperArr;
        int length = objArr.length;
        if (method != null) {
            parameterWrapperArr = new ParameterWrapper[length];
            Class[] parameterTypes = method.getParameterTypes();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < length; i++) {
                if (parameterTypes[i].isInterface()) {
                    Object obj = objArr[i];
                    if (obj != null) {
                        parameterWrapperArr[i] = new ParameterWrapper((Class<?>) parameterTypes[i], (Object) null);
                    } else {
                        parameterWrapperArr[i] = new ParameterWrapper((Object) null);
                    }
                    if (!(parameterAnnotations[i] == null || obj == null)) {
                        CALLBACK_MANAGER.addCallback(this.mTimeStamp, i, obj, TypeUtils.arrayContainsAnnotation(parameterAnnotations[i], WeakRef.class), !TypeUtils.arrayContainsAnnotation(parameterAnnotations[i], Background.class));
                    }
                } else if (Context.class.isAssignableFrom(parameterTypes[i])) {
                    parameterWrapperArr[i] = new ParameterWrapper(TypeUtils.getContextClass(parameterTypes[i]), (Object) null);
                } else {
                    boolean arrayContainsAnnotation = TypeUtils.arrayContainsAnnotation(parameterAnnotations[i], Out.class);
                    boolean arrayContainsAnnotation2 = TypeUtils.arrayContainsAnnotation(parameterAnnotations[i], InOut.class);
                    if ((arrayContainsAnnotation || arrayContainsAnnotation2) && IIPcDataFlow.class.isAssignableFrom(parameterTypes[i])) {
                        if (arrayContainsAnnotation2) {
                            parameterWrapperArr[i] = new ParameterWrapper(objArr[i], 2);
                        } else {
                            try {
                                if (parameterTypes[i].getConstructor(new Class[0]) != null) {
                                    parameterWrapperArr[i] = new ParameterWrapper(objArr[i], 1);
                                }
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    parameterWrapperArr[i] = new ParameterWrapper(objArr[i]);
                }
            }
        } else {
            parameterWrapperArr = new ParameterWrapper[length];
            for (int i2 = 0; i2 < length; i2++) {
                if (objArr[i2] instanceof Context) {
                    parameterWrapperArr[i2] = new ParameterWrapper(TypeUtils.getContextClass(Context.class), (Object) null);
                } else {
                    parameterWrapperArr[i2] = new ParameterWrapper(objArr[i2]);
                }
            }
        }
        return parameterWrapperArr;
    }

    public final Reply send(Method method, Object[] objArr) throws IPCException {
        this.mTimeStamp = TimeStampGenerator.getTimeStamp();
        if (objArr == null) {
            objArr = new Object[0];
        }
        ParameterWrapper[] parameterWrappers = getParameterWrappers(method, objArr);
        MethodWrapper methodWrapper = getMethodWrapper(method, (String) null, parameterWrappers);
        registerClass(method);
        return Channel.getInstance().send(Message.obtain(this.mTimeStamp, this.mObject, methodWrapper, parameterWrappers));
    }

    public ObjectWrapper getObject() {
        return this.mObject;
    }
}
