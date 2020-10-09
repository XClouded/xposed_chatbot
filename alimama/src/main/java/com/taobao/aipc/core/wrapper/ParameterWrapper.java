package com.taobao.aipc.core.wrapper;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.aipc.annotation.type.ClassName;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.aipc.utils.TypeUtils;

public class ParameterWrapper extends BaseWrapper implements Parcelable {
    public static final Parcelable.Creator<ParameterWrapper> CREATOR = new Parcelable.Creator<ParameterWrapper>() {
        public ParameterWrapper createFromParcel(Parcel parcel) {
            ParameterWrapper parameterWrapper = new ParameterWrapper();
            parameterWrapper.readFromParcel(parcel);
            return parameterWrapper;
        }

        public ParameterWrapper[] newArray(int i) {
            return new ParameterWrapper[i];
        }
    };
    private int flowFlag;
    private Class<?> mClass;
    private byte[] mData;

    public interface DataFlow {
        public static final int IN = 0;
        public static final int INT_OUT = 2;
        public static final int OUT = 1;
    }

    public int describeContents() {
        return 0;
    }

    private ParameterWrapper() {
        this.flowFlag = 0;
    }

    public ParameterWrapper(Class<?> cls, Object obj) throws IPCException {
        this.flowFlag = 0;
        this.mClass = cls;
        setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
        this.mData = SerializeUtils.encode(obj);
    }

    public ParameterWrapper(Object obj) throws IPCException {
        this.flowFlag = 0;
        if (obj == null) {
            setName(false, "");
            this.mData = null;
            this.mClass = null;
            return;
        }
        Class<?> cls = obj.getClass();
        this.mClass = cls;
        setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
        this.mData = SerializeUtils.encode(obj);
    }

    public ParameterWrapper(Object obj, int i) throws IPCException {
        this.flowFlag = 0;
        if (obj == null) {
            setName(false, "");
            this.mData = null;
            this.mClass = null;
            return;
        }
        Class<?> cls = obj.getClass();
        this.mClass = cls;
        setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
        this.flowFlag = i;
        if (this.flowFlag != 1) {
            this.mData = SerializeUtils.encode(obj);
            return;
        }
        try {
            this.mData = SerializeUtils.encode(this.mClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.flowFlag);
        parcel.writeSerializable(this.mClass);
        if (this.mData != null) {
            parcel.writeInt(this.mData.length);
            parcel.writeByteArray(this.mData);
        }
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        this.flowFlag = parcel.readInt();
        this.mClass = (Class) parcel.readSerializable();
        int readInt = parcel.readInt();
        if (readInt != 0) {
            this.mData = new byte[readInt];
            parcel.readByteArray(this.mData);
        }
    }

    public byte[] getData() {
        return this.mData;
    }

    public boolean isNull() {
        return this.mData == null;
    }

    public Class<?> getClassType() {
        return this.mClass;
    }

    public int getFlowFlag() {
        return this.flowFlag;
    }
}
