package com.taobao.aipc.core.channel;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import androidx.core.util.Pair;
import com.taobao.aipc.constant.Constants;
import com.taobao.aipc.core.entity.CallbackMessage;
import com.taobao.aipc.core.entity.Message;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.receiver.ReceiverDesignator;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.logs.IPCLog;
import com.taobao.aipc.utils.CallbackManager;
import com.taobao.aipc.utils.ObjectCenter;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.aipc.utils.TypeCenter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class DuplexIPCProvider extends ContentProvider {
    private static final CallbackManager CALLBACK_MANAGER = CallbackManager.getInstance();
    private static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();
    /* access modifiers changed from: private */
    public static final String TAG = "DuplexIPCProvider";
    private static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();
    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        char c = 65535;
        try {
            int hashCode = str.hashCode();
            if (hashCode != -418254875) {
                if (hashCode != -172220347) {
                    if (hashCode != 3526536) {
                        if (hashCode == 1930954610) {
                            if (str.equals(Constants.METHOD_RECYCLE_REMOTE)) {
                                c = 3;
                            }
                        }
                    } else if (str.equals("send")) {
                        c = 2;
                    }
                } else if (str.equals("callback")) {
                    c = 0;
                }
            } else if (str.equals(Constants.METHOD_RECYCLE_MAIN)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    bundle.setClassLoader(CallbackMessage.class.getClassLoader());
                    Reply callback = callback((CallbackMessage) bundle.getParcelable(Constants.PARAM_CALLBACK_MESSAGE));
                    if (callback != null) {
                        bundle2.putByteArray(Constants.PARAM_REPLY, SerializeUtils.encode(callback));
                        callback.recycle();
                    }
                    return bundle2;
                case 1:
                    recycle(bundle.getStringArrayList(Constants.PARAM_TIMESTAMPS), bundle.getIntegerArrayList(Constants.PARAM_INDEX));
                    break;
                case 2:
                    bundle.setClassLoader(Message.class.getClassLoader());
                    Reply receive = receive((Message) bundle.getParcelable("message"));
                    if (receive != null) {
                        bundle2.putByteArray(Constants.PARAM_REPLY, SerializeUtils.encode(receive));
                        receive.recycle();
                    }
                    return bundle2;
                case 3:
                    recycle(bundle.getStringArrayList(Constants.PARAM_TIMESTAMPS));
                    break;
            }
        } catch (Exception e) {
            IPCLog.eTag(TAG, "Error occurs during call. Error: ", e);
        }
        return bundle2;
    }

    private Object[] getParameters(ParameterWrapper[] parameterWrapperArr) throws IPCException {
        if (parameterWrapperArr == null) {
            parameterWrapperArr = new ParameterWrapper[0];
        }
        int length = parameterWrapperArr.length;
        Object[] objArr = new Object[length];
        for (int i = 0; i < length; i++) {
            ParameterWrapper parameterWrapper = parameterWrapperArr[i];
            if (parameterWrapper == null) {
                objArr[i] = null;
            } else {
                Class<?> classType = TYPE_CENTER.getClassType(parameterWrapper);
                byte[] data = parameterWrapper.getData();
                if (data == null) {
                    objArr[i] = null;
                } else {
                    objArr[i] = SerializeUtils.decode(data, classType);
                }
            }
        }
        return objArr;
    }

    public Reply callback(CallbackMessage callbackMessage) {
        Object obj;
        Object obj2;
        Exception exc;
        Pair<Boolean, Object> callback = CALLBACK_MANAGER.getCallback(callbackMessage.getTimeStamp(), callbackMessage.getIndex());
        if (callback == null) {
            return null;
        }
        S s = callback.second;
        if (s == null) {
            return Reply.obtain(22, "");
        }
        boolean booleanValue = ((Boolean) callback.first).booleanValue();
        try {
            Method method = TYPE_CENTER.getMethod(s.getClass(), callbackMessage.getMethod());
            Object[] parameters = getParameters(callbackMessage.getParameters());
            if (booleanValue) {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    try {
                        obj2 = method.invoke(s, parameters);
                        exc = null;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        exc = e;
                        obj2 = null;
                    }
                    Exception exc2 = exc;
                    obj = obj2;
                    e = exc2;
                } else {
                    try {
                        CountDownLatch countDownLatch = new CountDownLatch(1);
                        MainRunnable mainRunnable = new MainRunnable(countDownLatch, method, s, parameters);
                        this.mUiHandler.post(mainRunnable);
                        countDownLatch.await(5000, TimeUnit.MILLISECONDS);
                        if (mainRunnable.getMainResult() == null) {
                            return null;
                        }
                        return Reply.obtain(new ParameterWrapper(mainRunnable.getMainResult()));
                    } catch (Exception unused) {
                        return null;
                    }
                }
            } else {
                try {
                    obj = method.invoke(s, parameters);
                    e = null;
                } catch (IllegalAccessException | InvocationTargetException e2) {
                    e = e2;
                    obj = null;
                }
            }
            if (e != null) {
                e.printStackTrace();
                throw new IPCException(18, "Error occurs when invoking method " + method + " on " + s, e);
            } else if (obj == null) {
                return null;
            } else {
                return Reply.obtain(new ParameterWrapper(obj));
            }
        } catch (IPCException e3) {
            IPCLog.eTag(TAG, "callback to main Error: ", e3);
            return Reply.obtain(e3.getErrorCode(), e3.getMessage());
        }
    }

    public void recycle(List<String> list, List<Integer> list2) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CALLBACK_MANAGER.removeCallback(list.get(i), list2.get(i).intValue());
        }
    }

    private class MainRunnable implements Runnable {
        private Object callback;
        private CountDownLatch countDownLatch;
        private Object mainResult;
        private Method method;
        private Object[] parameters;

        MainRunnable(CountDownLatch countDownLatch2, Method method2, Object obj, Object[] objArr) {
            this.countDownLatch = countDownLatch2;
            this.method = method2;
            this.callback = obj;
            this.parameters = objArr;
        }

        public void run() {
            try {
                this.mainResult = this.method.invoke(this.callback, this.parameters);
            } catch (Exception e) {
                IPCLog.eTag(DuplexIPCProvider.TAG, "main runnable invoke Error: ", e);
            } catch (Throwable th) {
                this.countDownLatch.countDown();
                throw th;
            }
            this.countDownLatch.countDown();
        }

        /* access modifiers changed from: package-private */
        public Object getMainResult() {
            return this.mainResult;
        }
    }

    public Reply receive(Message message) {
        try {
            if (message.getPfd() != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor) message.getPfd()).getFileDescriptor());
                    FileChannel channel = fileInputStream.getChannel();
                    ByteBuffer allocate = ByteBuffer.allocate(1024);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while (channel.read(allocate) != -1) {
                        byteArrayOutputStream.write(allocate.array());
                        allocate.clear();
                    }
                    message.setParameters((ParameterWrapper[]) SerializeUtils.decode(byteArrayOutputStream.toByteArray(), ParameterWrapper[].class));
                    byteArrayOutputStream.close();
                    fileInputStream.close();
                } catch (IOException e) {
                    IPCLog.eTag(TAG, "read from ParcelFileDescriptor Error:", e);
                    return Reply.obtain(23, "Error occurs when inputStream read from ParcelFileDescriptor");
                }
            }
            return ReceiverDesignator.getReceiver(message.getObject()).action(message.getTimeStamp(), message.getMethod(), message.getParameters());
        } catch (IPCException e2) {
            IPCLog.eTag(TAG, "receive Error: ", e2);
            return Reply.obtain(e2.getErrorCode(), e2.getMessage());
        }
    }

    public void recycle(ArrayList<String> arrayList) {
        OBJECT_CENTER.deleteObjects(arrayList);
    }
}
