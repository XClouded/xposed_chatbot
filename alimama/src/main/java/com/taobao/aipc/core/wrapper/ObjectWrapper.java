package com.taobao.aipc.core.wrapper;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.aipc.annotation.type.ClassName;
import com.taobao.aipc.utils.TimeStampGenerator;
import com.taobao.aipc.utils.TypeUtils;

public class ObjectWrapper extends BaseWrapper implements Parcelable {
    public static final Parcelable.Creator<ObjectWrapper> CREATOR = new Parcelable.Creator<ObjectWrapper>() {
        public ObjectWrapper createFromParcel(Parcel parcel) {
            ObjectWrapper objectWrapper = new ObjectWrapper();
            objectWrapper.readFromParcel(parcel);
            return objectWrapper;
        }

        public ObjectWrapper[] newArray(int i) {
            return new ObjectWrapper[i];
        }
    };
    public static final int TYPE_CLASS = 4;
    public static final int TYPE_CLASS_TO_GET = 2;
    public static final int TYPE_OBJECT = 3;
    public static final int TYPE_OBJECT_TO_GET = 1;
    public static final int TYPE_OBJECT_TO_NEW = 0;
    private Class<?> mClass;
    private String mTimeStamp;
    private int mType;

    public int describeContents() {
        return 0;
    }

    private ObjectWrapper() {
    }

    public ObjectWrapper(Class<?> cls, int i) {
        setName(!cls.isAnnotationPresent(ClassName.class), TypeUtils.getClassId(cls));
        this.mClass = cls;
        this.mTimeStamp = TimeStampGenerator.getTimeStamp();
        this.mType = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mTimeStamp);
        parcel.writeInt(this.mType);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        this.mTimeStamp = parcel.readString();
        this.mType = parcel.readInt();
    }

    public String getTimeStamp() {
        return this.mTimeStamp;
    }

    public Class<?> getObjectClass() {
        return this.mClass;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public int getType() {
        return this.mType;
    }
}
