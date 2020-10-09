package com.taobao.aipc.core.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import java.util.concurrent.ArrayBlockingQueue;

public class CallbackMessage implements Parcelable {
    public static final Parcelable.Creator<CallbackMessage> CREATOR = new Parcelable.Creator<CallbackMessage>() {
        public CallbackMessage createFromParcel(Parcel parcel) {
            CallbackMessage callbackMessage = new CallbackMessage();
            callbackMessage.readFromParcel(parcel);
            return callbackMessage;
        }

        public CallbackMessage[] newArray(int i) {
            return new CallbackMessage[i];
        }
    };
    private static final ArrayBlockingQueue<CallbackMessage> callbackMessagePool = new ArrayBlockingQueue<>(10);
    private static final Object sPoolSync = new Object();
    private int mIndex;
    private MethodWrapper mMethod;
    private ParameterWrapper[] mParameters;
    private String mTimeStamp;

    public int describeContents() {
        return 0;
    }

    public static CallbackMessage obtain(String str, int i, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) {
        synchronized (sPoolSync) {
            CallbackMessage poll = callbackMessagePool.poll();
            if (poll == null) {
                return new CallbackMessage(str, i, methodWrapper, parameterWrapperArr);
            }
            poll.setIndex(i);
            poll.setMethod(methodWrapper);
            poll.setTimeStamp(str);
            poll.setParameters(parameterWrapperArr);
            return poll;
        }
    }

    public void recycle() {
        if (this.mMethod != null && !this.mMethod.recycle()) {
            this.mMethod = null;
        }
        this.mTimeStamp = null;
        this.mParameters = null;
        synchronized (sPoolSync) {
            callbackMessagePool.offer(this);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mTimeStamp);
        parcel.writeInt(this.mIndex);
        parcel.writeParcelable(this.mMethod, i);
        parcel.writeParcelableArray(this.mParameters, i);
    }

    /* access modifiers changed from: private */
    public void readFromParcel(Parcel parcel) {
        this.mTimeStamp = parcel.readString();
        this.mIndex = parcel.readInt();
        ClassLoader classLoader = CallbackMessage.class.getClassLoader();
        this.mMethod = (MethodWrapper) parcel.readParcelable(classLoader);
        Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
        if (readParcelableArray == null || readParcelableArray.length == 0) {
            this.mParameters = new ParameterWrapper[0];
            return;
        }
        int length = readParcelableArray.length;
        this.mParameters = new ParameterWrapper[length];
        for (int i = 0; i < length; i++) {
            this.mParameters[i] = (ParameterWrapper) readParcelableArray[i];
        }
    }

    private CallbackMessage() {
    }

    private CallbackMessage(String str, int i, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) {
        this.mTimeStamp = str;
        this.mIndex = i;
        this.mMethod = methodWrapper;
        this.mParameters = parameterWrapperArr;
    }

    public ParameterWrapper[] getParameters() {
        return this.mParameters;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public MethodWrapper getMethod() {
        return this.mMethod;
    }

    public String getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(String str) {
        this.mTimeStamp = str;
    }

    public void setIndex(int i) {
        this.mIndex = i;
    }

    public void setMethod(MethodWrapper methodWrapper) {
        this.mMethod = methodWrapper;
    }

    public void setParameters(ParameterWrapper[] parameterWrapperArr) {
        this.mParameters = parameterWrapperArr;
    }
}
