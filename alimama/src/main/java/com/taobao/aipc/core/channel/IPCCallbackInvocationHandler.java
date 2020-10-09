package com.taobao.aipc.core.channel;

import com.taobao.aipc.core.entity.CallbackMessage;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.logs.IPCLog;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IPCCallbackInvocationHandler implements InvocationHandler {
    private static final String TAG = "IPCCallbackInvocationHandler";
    private int mIndex;
    private String mTimeStamp;

    public IPCCallbackInvocationHandler(String str, int i) {
        this.mTimeStamp = str;
        this.mIndex = i;
    }

    public Object invoke(Object obj, Method method, Object[] objArr) {
        try {
            Reply callback = Channel.getInstance().callback(CallbackMessage.obtain(this.mTimeStamp, this.mIndex, MethodWrapper.obtain(method), TypeUtils.objectToWrapper(objArr)));
            if (callback == null) {
                return null;
            }
            if (callback.success()) {
                return callback.getResult();
            }
            String str = TAG;
            IPCLog.e(str, "Error occurs: " + callback.getMessage());
            return null;
        } catch (IPCException e) {
            IPCLog.eTag(TAG, "Error occurs but does not crash the app.", e);
            return null;
        }
    }
}
