package com.taobao.aipc.core.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.core.wrapper.TypeWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.SerializeUtils;
import com.taobao.aipc.utils.TypeCenter;
import java.util.concurrent.ArrayBlockingQueue;

public class Reply implements Parcelable {
    public static final Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator<Reply>() {
        public Reply createFromParcel(Parcel parcel) {
            Reply reply = new Reply();
            reply.readFromParcel(parcel);
            return reply;
        }

        public Reply[] newArray(int i) {
            return new Reply[i];
        }
    };
    private static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();
    private static final ArrayBlockingQueue<Reply> replyPool = new ArrayBlockingQueue<>(20);
    private static final Object sPoolSync = new Object();
    private TypeWrapper mClass;
    private ParameterWrapper[] mDataFlowParameters;
    private int mErrorCode;
    private String mErrorMessage;
    private Object mResult;

    public int describeContents() {
        return 0;
    }

    public static Reply obtain(ParameterWrapper parameterWrapper) {
        Reply poll;
        synchronized (sPoolSync) {
            poll = replyPool.poll();
            if (poll == null) {
                return new Reply(parameterWrapper);
            }
            try {
                Class<?> classType = TYPE_CENTER.getClassType(parameterWrapper);
                poll.setResult(SerializeUtils.decode(parameterWrapper.getData(), classType));
                poll.setErrorCode(0);
                poll.setErrorMessage((String) null);
                poll.setClass(TypeWrapper.obtain(classType));
            } catch (IPCException e) {
                poll.setResult((Object) null);
                poll.setErrorCode(e.getErrorCode());
                poll.setErrorMessage(e.getMessage());
                poll.setClass((TypeWrapper) null);
            }
        }
        return poll;
    }

    public static Reply obtain(int i, String str) {
        synchronized (sPoolSync) {
            Reply poll = replyPool.poll();
            if (poll == null) {
                return new Reply(i, str);
            }
            poll.setResult((Object) null);
            poll.setErrorCode(i);
            poll.setErrorMessage(str);
            poll.setClass((TypeWrapper) null);
            return poll;
        }
    }

    public void recycle() {
        if (this.mClass != null && !this.mClass.recycle()) {
            this.mClass = null;
        }
        this.mErrorMessage = null;
        this.mResult = null;
        this.mDataFlowParameters = null;
        synchronized (sPoolSync) {
            replyPool.offer(this);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mErrorCode);
        parcel.writeString(this.mErrorMessage);
        try {
            parcel.writeParcelable(this.mClass, i);
            byte[] encode = SerializeUtils.encode(this.mResult);
            parcel.writeInt(encode.length);
            parcel.writeByteArray(encode);
        } catch (IPCException e) {
            e.printStackTrace();
        }
        parcel.writeParcelableArray(this.mDataFlowParameters, i);
    }

    /* access modifiers changed from: private */
    public void readFromParcel(Parcel parcel) {
        this.mErrorCode = parcel.readInt();
        this.mErrorMessage = parcel.readString();
        ClassLoader classLoader = Reply.class.getClassLoader();
        this.mClass = (TypeWrapper) parcel.readParcelable(classLoader);
        try {
            if (this.mClass != null) {
                Class<?> classType = TYPE_CENTER.getClassType(this.mClass);
                byte[] bArr = new byte[parcel.readInt()];
                parcel.readByteArray(bArr);
                this.mResult = SerializeUtils.decode(bArr, classType);
            } else {
                parcel.readInt();
                parcel.readByteArray(new byte[0]);
            }
        } catch (IPCException e) {
            e.printStackTrace();
        }
        Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
        if (readParcelableArray == null || readParcelableArray.length == 0) {
            this.mDataFlowParameters = new ParameterWrapper[0];
            return;
        }
        int length = readParcelableArray.length;
        this.mDataFlowParameters = new ParameterWrapper[length];
        for (int i = 0; i < length; i++) {
            this.mDataFlowParameters[i] = (ParameterWrapper) readParcelableArray[i];
        }
    }

    private Reply() {
    }

    private Reply(ParameterWrapper parameterWrapper) {
        try {
            Class<?> classType = TYPE_CENTER.getClassType(parameterWrapper);
            this.mResult = SerializeUtils.decode(parameterWrapper.getData(), classType);
            this.mErrorCode = 0;
            this.mErrorMessage = null;
            this.mClass = TypeWrapper.obtain(classType);
        } catch (IPCException e) {
            e.printStackTrace();
            this.mErrorCode = e.getErrorCode();
            this.mErrorMessage = e.getMessage();
            this.mResult = null;
            this.mClass = null;
            this.mDataFlowParameters = null;
        }
    }

    private Reply(int i, String str) {
        this.mErrorCode = i;
        this.mErrorMessage = str;
        this.mResult = null;
        this.mClass = null;
        this.mDataFlowParameters = null;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public boolean success() {
        return this.mErrorCode == 0 || this.mErrorCode == -1;
    }

    public String getMessage() {
        return this.mErrorMessage;
    }

    public Object getResult() {
        return this.mResult;
    }

    public void setErrorCode(int i) {
        this.mErrorCode = i;
    }

    public void setErrorMessage(String str) {
        this.mErrorMessage = str;
    }

    public void setClass(TypeWrapper typeWrapper) {
        this.mClass = typeWrapper;
    }

    public void setResult(Object obj) {
        this.mResult = obj;
    }

    public ParameterWrapper[] getDataFlowParameter() {
        return this.mDataFlowParameters;
    }

    public void setDataFlowParameters(ParameterWrapper[] parameterWrapperArr) {
        this.mDataFlowParameters = parameterWrapperArr;
    }
}
