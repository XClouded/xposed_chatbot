package com.alipay.literpc.android.phone.mrpc.core;

import com.alipay.literpc.android.phone.mrpc.core.gwprotocol.Deserializer;
import com.alipay.literpc.android.phone.mrpc.core.gwprotocol.JsonDeserializer;
import com.alipay.literpc.android.phone.mrpc.core.gwprotocol.JsonSerializer;
import com.alipay.literpc.android.phone.mrpc.core.gwprotocol.Serializer;
import com.alipay.literpc.mobile.framework.service.annotation.OperationType;
import com.alipay.literpc.mobile.framework.service.annotation.ResetCookie;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcInvoker {
    private static final ThreadLocal<Map<String, Object>> EXT_PARAM = new ThreadLocal<>();
    private static final byte MODE_BATCH = 1;
    private static final byte MODE_DEFAULT = 0;
    private static final ThreadLocal<Object> RETURN_VALUE = new ThreadLocal<>();
    private byte mMode = 0;
    private RpcFactory mRpcFactory;
    private AtomicInteger rpcSequence;

    private void postHandle(Object obj, byte[] bArr, Class<?> cls, Method method, Object[] objArr, Annotation[] annotationArr) throws RpcException {
    }

    private void preHandle(Object obj, Class<?> cls, Method method, Object[] objArr, Annotation[] annotationArr) throws RpcException {
    }

    public RpcInvoker(RpcFactory rpcFactory) {
        this.mRpcFactory = rpcFactory;
        this.rpcSequence = new AtomicInteger();
    }

    public Object invoke(Object obj, Class<?> cls, Method method, Object[] objArr) throws RpcException {
        byte[] bArr;
        RpcException rpcException;
        Method method2 = method;
        if (!ThreadUtil.checkMainThread()) {
            OperationType operationType = (OperationType) method2.getAnnotation(OperationType.class);
            boolean z = method2.getAnnotation(ResetCookie.class) != null;
            Type genericReturnType = method.getGenericReturnType();
            Annotation[] annotations = method.getAnnotations();
            byte[] bArr2 = null;
            RETURN_VALUE.set((Object) null);
            EXT_PARAM.set((Object) null);
            if (operationType != null) {
                String value = operationType.value();
                int incrementAndGet = this.rpcSequence.incrementAndGet();
                preHandle(obj, cls, method, objArr, annotations);
                try {
                    if (this.mMode == 0) {
                        byte[] singleCall = singleCall(method, objArr, value, incrementAndGet, z);
                        try {
                            Object parser = getDeserializer(genericReturnType, singleCall).parser();
                            if (genericReturnType != Void.TYPE) {
                                RETURN_VALUE.set(parser);
                            }
                            bArr2 = singleCall;
                        } catch (RpcException e) {
                            rpcException = e;
                            bArr2 = singleCall;
                            rpcException.setOperationType(value);
                            bArr = bArr2;
                            exceptionHandle(obj, bArr, cls, method, objArr, annotations, rpcException);
                            postHandle(obj, bArr, cls, method, objArr, annotations);
                            return RETURN_VALUE.get();
                        }
                    }
                    bArr = bArr2;
                } catch (RpcException e2) {
                    rpcException = e2;
                    rpcException.setOperationType(value);
                    bArr = bArr2;
                    exceptionHandle(obj, bArr, cls, method, objArr, annotations, rpcException);
                    postHandle(obj, bArr, cls, method, objArr, annotations);
                    return RETURN_VALUE.get();
                }
                postHandle(obj, bArr, cls, method, objArr, annotations);
                return RETURN_VALUE.get();
            }
            throw new IllegalStateException("OperationType must be set.");
        }
        throw new IllegalThreadStateException("can't in main thread call rpc .");
    }

    private void exceptionHandle(Object obj, byte[] bArr, Class<?> cls, Method method, Object[] objArr, Annotation[] annotationArr, RpcException rpcException) throws RpcException {
        throw rpcException;
    }

    private byte[] singleCall(Method method, Object[] objArr, String str, int i, boolean z) throws RpcException {
        Serializer serializer = getSerializer(i, str, objArr);
        if (EXT_PARAM.get() != null) {
            serializer.setExtParam(EXT_PARAM.get());
        }
        byte[] bArr = (byte[]) getTransport(method, i, str, serializer.packet(), z).call();
        EXT_PARAM.set((Object) null);
        return bArr;
    }

    public void batchBegin() {
        this.mMode = 1;
    }

    public FutureTask<?> batchCommit() {
        this.mMode = 0;
        return null;
    }

    public static void addProtocolArgs(String str, Object obj) {
        if (EXT_PARAM.get() == null) {
            EXT_PARAM.set(new HashMap());
        }
        EXT_PARAM.get().put(str, obj);
    }

    public RpcCaller getTransport(Method method, int i, String str, byte[] bArr, boolean z) {
        return new HttpCaller(this.mRpcFactory.getConfig(), method, i, str, bArr, z);
    }

    public Serializer getSerializer(int i, String str, Object[] objArr) {
        return new JsonSerializer(i, str, objArr);
    }

    public Deserializer getDeserializer(Type type, byte[] bArr) {
        return new JsonDeserializer(type, bArr);
    }
}
