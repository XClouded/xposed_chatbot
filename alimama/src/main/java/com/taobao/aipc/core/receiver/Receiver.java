package com.taobao.aipc.core.receiver;

import android.content.Context;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.core.channel.IPCCallbackInvocationHandler;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.IPCCallbackRecycle;
import com.taobao.aipc.utils.ObjectCenter;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.aipc.utils.TypeCenter;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Receiver implements IReceiver {
    protected static final IPCCallbackRecycle IPC_CALLBACK_RECYCLE = IPCCallbackRecycle.getInstance();
    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();
    protected static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();
    private String mObjectTimeStamp;
    private Object[] mParameters;

    public Receiver(ObjectWrapper objectWrapper) {
        this.mObjectTimeStamp = objectWrapper.getTimeStamp();
    }

    /* access modifiers changed from: protected */
    public String getObjectTimeStamp() {
        return this.mObjectTimeStamp;
    }

    /* access modifiers changed from: protected */
    public Object[] getParameters() {
        return this.mParameters;
    }

    private Object getProxy(Class<?> cls, int i, String str) {
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new IPCCallbackInvocationHandler(str, i));
    }

    private static void registerCallbackReturnTypes(Class<?> cls) {
        for (Method returnType : cls.getMethods()) {
            TYPE_CENTER.register(returnType.getReturnType());
        }
    }

    private void setParameters(String str, ParameterWrapper[] parameterWrapperArr, ArrayList<Integer> arrayList) throws IPCException {
        if (parameterWrapperArr == null) {
            this.mParameters = null;
            return;
        }
        int length = parameterWrapperArr.length;
        this.mParameters = new Object[length];
        for (int i = 0; i < length; i++) {
            ParameterWrapper parameterWrapper = parameterWrapperArr[i];
            if (parameterWrapper == null) {
                this.mParameters[i] = null;
            } else {
                Class<?> classType = TYPE_CENTER.getClassType(parameterWrapper);
                if (classType != null && classType.isInterface()) {
                    registerCallbackReturnTypes(classType);
                    this.mParameters[i] = getProxy(classType, i, str);
                    IPC_CALLBACK_RECYCLE.register(this.mParameters[i], str, i);
                } else if (classType == null || !Context.class.isAssignableFrom(classType)) {
                    byte[] data = parameterWrapper.getData();
                    if (data == null) {
                        this.mParameters[i] = null;
                    } else {
                        if (parameterWrapper.getFlowFlag() != 0) {
                            arrayList.add(Integer.valueOf(i));
                        }
                        this.mParameters[i] = SerializeUtils.decode(data, classType);
                    }
                } else {
                    this.mParameters[i] = AIPC.getContext();
                }
            }
        }
    }

    public final Reply action(String str, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException {
        Reply reply;
        ArrayList arrayList = new ArrayList();
        setMethod(methodWrapper, parameterWrapperArr);
        setParameters(str, parameterWrapperArr, arrayList);
        Object invokeMethod = invokeMethod();
        if (invokeMethod == null) {
            reply = Reply.obtain(-1, "void");
        } else {
            reply = Reply.obtain(new ParameterWrapper(invokeMethod));
        }
        if (!arrayList.isEmpty()) {
            ParameterWrapper[] parameterWrapperArr2 = new ParameterWrapper[arrayList.size()];
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                parameterWrapperArr2[intValue] = new ParameterWrapper(this.mParameters[((Integer) arrayList.get(intValue)).intValue()]);
            }
            reply.setDataFlowParameters(parameterWrapperArr2);
        }
        return reply;
    }
}
