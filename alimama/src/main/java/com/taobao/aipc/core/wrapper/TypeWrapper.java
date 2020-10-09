package com.taobao.aipc.core.wrapper;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.aipc.annotation.type.ClassName;
import com.taobao.aipc.utils.TypeUtils;
import java.util.concurrent.ArrayBlockingQueue;

public class TypeWrapper extends BaseWrapper implements Parcelable {
    public static final Parcelable.Creator<TypeWrapper> CREATOR = new Parcelable.Creator<TypeWrapper>() {
        public TypeWrapper createFromParcel(Parcel parcel) {
            TypeWrapper typeWrapper = new TypeWrapper();
            typeWrapper.readFromParcel(parcel);
            return typeWrapper;
        }

        public TypeWrapper[] newArray(int i) {
            return new TypeWrapper[i];
        }
    };
    private static final Object sPoolSync = new Object();
    private static final ArrayBlockingQueue<TypeWrapper> typePool = new ArrayBlockingQueue<>(20);

    public int describeContents() {
        return 0;
    }

    public static TypeWrapper obtain(Class<?> cls) {
        synchronized (sPoolSync) {
            TypeWrapper poll = typePool.poll();
            if (poll != null) {
                poll.setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
                return poll;
            }
            TypeWrapper typeWrapper = new TypeWrapper(cls);
            return typeWrapper;
        }
    }

    public boolean recycle() {
        boolean offer;
        setName(false, (String) null);
        synchronized (sPoolSync) {
            offer = typePool.offer(this);
        }
        return offer;
    }

    private TypeWrapper() {
    }

    private TypeWrapper(Class<?> cls) {
        setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
    }
}
