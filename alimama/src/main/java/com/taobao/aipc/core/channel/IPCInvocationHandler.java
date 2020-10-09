package com.taobao.aipc.core.channel;

import android.content.Context;
import com.taobao.aipc.annotation.method.OneWay;
import com.taobao.aipc.annotation.parameter.InOut;
import com.taobao.aipc.annotation.parameter.Out;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.sender.impl.SenderDesignator;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.intf.IIPcDataFlow;
import com.taobao.aipc.logs.IPCLog;
import com.taobao.aipc.utils.IPCThreadPool;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IPCInvocationHandler implements InvocationHandler {
    /* access modifiers changed from: private */
    public static final String TAG = "IPCInvocationHandler";
    /* access modifiers changed from: private */
    public Sender mSender;

    public IPCInvocationHandler(ObjectWrapper objectWrapper) {
        this.mSender = SenderDesignator.getPostOffice(3, objectWrapper);
    }

    public Object invoke(Object obj, final Method method, final Object[] objArr) {
        Reply reply;
        OneWay oneWay = (OneWay) method.getAnnotation(OneWay.class);
        Class<?> returnType = method.getReturnType();
        if (oneWay == null || !returnType.getName().equals("void")) {
            try {
                reply = this.mSender.send(method, objArr);
            } catch (IPCException e) {
                IPCLog.eTag(TAG, "sync invoke Error:", e);
                reply = null;
            }
            return processReply(reply, method, objArr);
        }
        IPCThreadPool.getIPCExecutor().submit(new Runnable() {
            public void run() {
                Reply reply;
                try {
                    reply = IPCInvocationHandler.this.mSender.send(method, objArr);
                } catch (IPCException e) {
                    IPCLog.eTag(IPCInvocationHandler.TAG, "oneway invoke Error:", e);
                    reply = null;
                }
                Object unused = IPCInvocationHandler.this.processReply(reply, method, objArr);
            }
        });
        return null;
    }

    /* access modifiers changed from: private */
    public Object processReply(Reply reply, Method method, Object[] objArr) {
        if (reply == null) {
            return null;
        }
        if (reply.getDataFlowParameter().length != 0) {
            Class[] parameterTypes = method.getParameterTypes();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            int i = 0;
            for (int i2 = 0; i2 < objArr.length; i2++) {
                try {
                    if (!parameterTypes[i2].isInterface() && !Context.class.isAssignableFrom(parameterTypes[i2]) && (objArr[i2] instanceof IIPcDataFlow)) {
                        if (!TypeUtils.arrayContainsAnnotation(parameterAnnotations[i2], Out.class) || parameterTypes[i2].getConstructor(new Class[0]) != null) {
                            if (TypeUtils.arrayContainsAnnotation(parameterAnnotations[i2], Out.class) || TypeUtils.arrayContainsAnnotation(parameterAnnotations[i2], InOut.class)) {
                                objArr[i2].copyRemoteProperties(SerializeUtils.decode(reply.getDataFlowParameter()[i].getData(), reply.getDataFlowParameter()[i].getClassType()));
                                i++;
                            }
                        }
                    }
                } catch (Exception e) {
                    IPCLog.eTag(TAG, "get data flow Error:", e);
                    e.printStackTrace();
                }
            }
        }
        if (reply.success()) {
            return reply.getResult();
        }
        String str = TAG;
        IPCLog.e(str, "Error occurs. Error " + reply.getErrorCode() + ": " + reply.getMessage());
        return null;
    }
}
