package com.taobao.aipc.core.wrapper;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.taobao.aipc.annotation.method.MethodName;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;

public class MethodWrapper extends BaseWrapper implements Parcelable {
    public static final Parcelable.Creator<MethodWrapper> CREATOR = new Parcelable.Creator<MethodWrapper>() {
        public MethodWrapper createFromParcel(Parcel parcel) {
            MethodWrapper methodWrapper = new MethodWrapper();
            methodWrapper.readFromParcel(parcel);
            return methodWrapper;
        }

        public MethodWrapper[] newArray(int i) {
            return new MethodWrapper[i];
        }
    };
    private static final ArrayBlockingQueue<MethodWrapper> methodPool = new ArrayBlockingQueue<>(20);
    private static final Object sPoolSync = new Object();
    private TypeWrapper[] mParameterTypes;
    private TypeWrapper mReturnType;

    public int describeContents() {
        return 0;
    }

    public static MethodWrapper obtain(Method method) {
        synchronized (sPoolSync) {
            MethodWrapper poll = methodPool.poll();
            if (poll != null) {
                poll.setName(!method.isAnnotationPresent(MethodName.class), TypeUtils.getMethodId(method));
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes == null) {
                    parameterTypes = new Class[0];
                }
                int length = parameterTypes.length;
                TypeWrapper[] typeWrapperArr = new TypeWrapper[length];
                for (int i = 0; i < length; i++) {
                    typeWrapperArr[i] = TypeWrapper.obtain(parameterTypes[i]);
                }
                poll.setReturnType(TypeWrapper.obtain(method.getReturnType()));
                poll.setParameterTypes(typeWrapperArr);
                return poll;
            }
            MethodWrapper methodWrapper = new MethodWrapper(method);
            return methodWrapper;
        }
    }

    public static MethodWrapper obtain(String str, Class<?>[] clsArr) {
        synchronized (sPoolSync) {
            MethodWrapper poll = methodPool.poll();
            if (poll != null) {
                poll.setName(!TextUtils.isEmpty(str), str);
                if (clsArr == null) {
                    clsArr = new Class[0];
                }
                int length = clsArr.length;
                TypeWrapper[] typeWrapperArr = new TypeWrapper[length];
                for (int i = 0; i < length; i++) {
                    typeWrapperArr[i] = TypeWrapper.obtain(clsArr[i]);
                }
                poll.setReturnType((TypeWrapper) null);
                poll.setParameterTypes(typeWrapperArr);
                return poll;
            }
            MethodWrapper methodWrapper = new MethodWrapper(str, clsArr);
            return methodWrapper;
        }
    }

    public static MethodWrapper obtain(Class<?>[] clsArr) {
        return obtain("", clsArr);
    }

    public boolean recycle() {
        boolean offer;
        if (this.mReturnType != null && !this.mReturnType.recycle()) {
            this.mReturnType = null;
        }
        if (this.mParameterTypes != null && this.mParameterTypes.length > 0) {
            for (TypeWrapper recycle : this.mParameterTypes) {
                recycle.recycle();
            }
        }
        setName(false, (String) null);
        synchronized (sPoolSync) {
            offer = methodPool.offer(this);
        }
        return offer;
    }

    private MethodWrapper() {
    }

    private MethodWrapper(Method method) {
        setName(!method.isAnnotationPresent(MethodName.class), TypeUtils.getMethodId(method));
        Class[] parameterTypes = method.getParameterTypes();
        parameterTypes = parameterTypes == null ? new Class[0] : parameterTypes;
        int length = parameterTypes.length;
        this.mParameterTypes = new TypeWrapper[length];
        for (int i = 0; i < length; i++) {
            this.mParameterTypes[i] = TypeWrapper.obtain(parameterTypes[i]);
        }
        this.mReturnType = TypeWrapper.obtain(method.getReturnType());
    }

    private MethodWrapper(String str, Class<?>[] clsArr) {
        setName(true, str);
        clsArr = clsArr == null ? new Class[0] : clsArr;
        int length = clsArr.length;
        this.mParameterTypes = new TypeWrapper[length];
        for (int i = 0; i < length; i++) {
            this.mParameterTypes[i] = TypeWrapper.obtain(clsArr[i]);
        }
        this.mReturnType = null;
    }

    private MethodWrapper(Class<?>[] clsArr) {
        int i = 0;
        setName(false, "");
        clsArr = clsArr == null ? new Class[0] : clsArr;
        int length = clsArr.length;
        this.mParameterTypes = new TypeWrapper[length];
        while (true) {
            TypeWrapper typeWrapper = null;
            if (i < length) {
                TypeWrapper[] typeWrapperArr = this.mParameterTypes;
                if (clsArr[i] != null) {
                    typeWrapper = TypeWrapper.obtain(clsArr[i]);
                }
                typeWrapperArr[i] = typeWrapper;
                i++;
            } else {
                this.mReturnType = null;
                return;
            }
        }
    }

    public TypeWrapper[] getParameterTypes() {
        return this.mParameterTypes;
    }

    public TypeWrapper getReturnType() {
        return this.mReturnType;
    }

    public void setParameterTypes(TypeWrapper[] typeWrapperArr) {
        this.mParameterTypes = typeWrapperArr;
    }

    public void setReturnType(TypeWrapper typeWrapper) {
        this.mReturnType = typeWrapper;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelableArray(this.mParameterTypes, i);
        parcel.writeParcelable(this.mReturnType, i);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        ClassLoader classLoader = MethodWrapper.class.getClassLoader();
        Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
        if (readParcelableArray == null) {
            this.mParameterTypes = null;
        } else {
            int length = readParcelableArray.length;
            this.mParameterTypes = new TypeWrapper[length];
            for (int i = 0; i < length; i++) {
                this.mParameterTypes[i] = (TypeWrapper) readParcelableArray[i];
            }
        }
        this.mReturnType = (TypeWrapper) parcel.readParcelable(classLoader);
    }
}
