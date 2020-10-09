package com.taobao.aipc.utils;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.logs.IPCLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;

public final class SerializeUtils {
    private static final String TAG = "SerializeUtils";

    private SerializeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static byte[] encode(Object obj) throws IPCException {
        if (obj == null) {
            return new byte[0];
        }
        try {
            if (obj instanceof Serializable) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(obj);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                objectOutputStream.close();
                byteArrayOutputStream.close();
                return byteArray;
            } else if (!(obj instanceof Parcelable)) {
                return JSON.toJSONString(obj).getBytes();
            } else {
                Parcel obtain = Parcel.obtain();
                ((Parcelable) obj).writeToParcel(obtain, 0);
                byte[] marshall = obtain.marshall();
                obtain.recycle();
                return marshall;
            }
        } catch (Exception e) {
            IPCLog.eTag(TAG, "encode Object Error: ", e);
            throw new IPCException(6, "Error occurs when encodes Object " + obj + " to String.");
        }
    }

    public static <T> T decode(byte[] bArr, Class<T> cls) throws IPCException {
        try {
            if (Serializable.class.isAssignableFrom(cls)) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                T readObject = objectInputStream.readObject();
                objectInputStream.close();
                byteArrayInputStream.close();
                return readObject;
            } else if (Parcelable.class.isAssignableFrom(cls)) {
                Parcel obtain = Parcel.obtain();
                obtain.unmarshall(bArr, 0, bArr.length);
                obtain.setDataPosition(0);
                T createFromParcel = ((Parcelable.Creator) cls.getDeclaredField("CREATOR").get(cls)).createFromParcel(obtain);
                obtain.recycle();
                return createFromParcel;
            } else {
                return JSON.parseObject(bArr, (Type) cls, Feature.SupportNonPublicField);
            }
        } catch (Exception e) {
            IPCLog.eTag(TAG, "decode data Error: ", e);
            throw new IPCException(7, "Error occurs when decodes data of the Class " + cls.getName());
        }
    }
}
