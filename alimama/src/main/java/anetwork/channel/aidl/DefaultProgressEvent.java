package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anetwork.channel.NetworkEvent;
import com.taobao.weex.el.parse.Operators;

public class DefaultProgressEvent implements NetworkEvent.ProgressEvent, Parcelable {
    public static final Parcelable.Creator<DefaultProgressEvent> CREATOR = new Parcelable.Creator<DefaultProgressEvent>() {
        public DefaultProgressEvent createFromParcel(Parcel parcel) {
            return DefaultProgressEvent.readFromParcel(parcel);
        }

        public DefaultProgressEvent[] newArray(int i) {
            return new DefaultProgressEvent[i];
        }
    };
    private static final String TAG = "anet.DefaultProgressEvent";
    Object context;
    int index;
    byte[] out;
    int size;
    int total;

    public int describeContents() {
        return 0;
    }

    public String getDesc() {
        return "";
    }

    public DefaultProgressEvent() {
    }

    public DefaultProgressEvent(int i, int i2, int i3, byte[] bArr) {
        this.index = i;
        this.size = i2;
        this.total = i3;
        this.out = bArr;
    }

    public int getSize() {
        return this.size;
    }

    public int getTotal() {
        return this.total;
    }

    public Object getContext() {
        return this.context;
    }

    public void setContext(Object obj) {
        this.context = obj;
    }

    public byte[] getBytedata() {
        return this.out;
    }

    public int getIndex() {
        return this.index;
    }

    public String toString() {
        return "DefaultProgressEvent [index=" + this.index + ", size=" + this.size + ", total=" + this.total + Operators.ARRAY_END_STR;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.index);
        parcel.writeInt(this.size);
        parcel.writeInt(this.total);
        parcel.writeInt(this.out != null ? this.out.length : 0);
        parcel.writeByteArray(this.out);
    }

    public static DefaultProgressEvent readFromParcel(Parcel parcel) {
        DefaultProgressEvent defaultProgressEvent = new DefaultProgressEvent();
        try {
            defaultProgressEvent.index = parcel.readInt();
            defaultProgressEvent.size = parcel.readInt();
            defaultProgressEvent.total = parcel.readInt();
            int readInt = parcel.readInt();
            if (readInt > 0) {
                byte[] bArr = new byte[readInt];
                parcel.readByteArray(bArr);
                defaultProgressEvent.out = bArr;
            }
        } catch (Exception unused) {
        }
        return defaultProgressEvent;
    }
}
