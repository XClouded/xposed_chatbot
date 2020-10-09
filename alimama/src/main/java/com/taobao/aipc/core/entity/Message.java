package com.taobao.aipc.core.entity;

import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Process;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.utils.SerializeUtils;
import java.io.FileDescriptor;
import java.util.concurrent.ArrayBlockingQueue;

public class Message implements Parcelable {
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel parcel) {
            Message message = new Message();
            message.readFromParcel(parcel);
            return message;
        }

        public Message[] newArray(int i) {
            return new Message[i];
        }
    };
    private static final long MAX_MESSAGE_SIZE = 921600;
    private static final ArrayBlockingQueue<Message> messagePool = new ArrayBlockingQueue<>(10);
    private static final Object sPoolSync = new Object();
    private MethodWrapper mMethod;
    private ObjectWrapper mObject;
    private ParameterWrapper[] mParameters;
    private Parcelable mPfd;
    private int mPid;
    private String mTimeStamp;

    public int describeContents() {
        return 0;
    }

    public static Message obtain(String str, ObjectWrapper objectWrapper, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) {
        synchronized (sPoolSync) {
            Message poll = messagePool.poll();
            if (poll == null) {
                return new Message(str, objectWrapper, methodWrapper, parameterWrapperArr);
            }
            poll.setPid(Process.myPid());
            poll.setMethod(methodWrapper);
            poll.setObject(objectWrapper);
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
        this.mPfd = null;
        this.mObject = null;
        this.mParameters = null;
        synchronized (sPoolSync) {
            messagePool.offer(this);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mTimeStamp);
        parcel.writeInt(this.mPid);
        parcel.writeParcelable(this.mObject, i);
        parcel.writeParcelable(this.mMethod, i);
        parcel.writeParcelable(this.mPfd, i);
        parcel.writeParcelableArray(this.mParameters, i);
    }

    /* access modifiers changed from: private */
    public void readFromParcel(Parcel parcel) {
        this.mTimeStamp = parcel.readString();
        this.mPid = parcel.readInt();
        ClassLoader classLoader = Message.class.getClassLoader();
        this.mObject = (ObjectWrapper) parcel.readParcelable(classLoader);
        this.mMethod = (MethodWrapper) parcel.readParcelable(classLoader);
        this.mPfd = parcel.readParcelable(Parcelable.class.getClassLoader());
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

    private Message() {
    }

    private Message(String str, ObjectWrapper objectWrapper, MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) {
        this.mTimeStamp = str;
        this.mPid = Process.myPid();
        this.mObject = objectWrapper;
        this.mMethod = methodWrapper;
        this.mParameters = parameterWrapperArr;
        if (getMessageSize() > MAX_MESSAGE_SIZE) {
            reBuildMessageByFileDescriptor();
        }
    }

    private long getMessageSize() {
        if (this.mParameters == null) {
            return 0;
        }
        long j = 0;
        for (ParameterWrapper parameterWrapper : this.mParameters) {
            j += parameterWrapper.getData() != null ? (long) parameterWrapper.getData().length : 0;
        }
        return j;
    }

    private void reBuildMessageByFileDescriptor() {
        try {
            byte[] encode = SerializeUtils.encode(this.mParameters);
            MemoryFile memoryFile = new MemoryFile(String.valueOf(this.mTimeStamp), encode.length);
            memoryFile.allowPurging(true);
            memoryFile.writeBytes(encode, 0, 0, encode.length);
            this.mPfd = ParcelFileDescriptor.dup((FileDescriptor) MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]).invoke(memoryFile, new Object[0]));
            this.mParameters = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPid() {
        return this.mPid;
    }

    public ParameterWrapper[] getParameters() {
        return this.mParameters;
    }

    public ObjectWrapper getObject() {
        return this.mObject;
    }

    public MethodWrapper getMethod() {
        return this.mMethod;
    }

    public String getTimeStamp() {
        return this.mTimeStamp;
    }

    public Parcelable getPfd() {
        return this.mPfd;
    }

    public void setParameters(ParameterWrapper[] parameterWrapperArr) {
        this.mParameters = parameterWrapperArr;
    }

    public void setTimeStamp(String str) {
        this.mTimeStamp = str;
    }

    public void setPid(int i) {
        this.mPid = i;
    }

    public void setObject(ObjectWrapper objectWrapper) {
        this.mObject = objectWrapper;
    }

    public void setMethod(MethodWrapper methodWrapper) {
        this.mMethod = methodWrapper;
    }
}
