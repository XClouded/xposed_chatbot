package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.Response;
import anetwork.channel.statist.StatisticData;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Map;

public class NetworkResponse implements Response, Parcelable {
    public static final Parcelable.Creator<NetworkResponse> CREATOR = new Parcelable.Creator<NetworkResponse>() {
        public NetworkResponse createFromParcel(Parcel parcel) {
            return NetworkResponse.readFromParcel(parcel);
        }

        public NetworkResponse[] newArray(int i) {
            return new NetworkResponse[i];
        }
    };
    private static final String TAG = "anet.NetworkResponse";
    byte[] bytedata;
    private Map<String, List<String>> connHeadFields;
    private String desc;
    private Throwable error;
    private StatisticData statisticData;
    int statusCode;

    public int describeContents() {
        return 0;
    }

    public void setStatusCode(int i) {
        this.statusCode = i;
        this.desc = ErrorConstant.getErrMsg(i);
    }

    public byte[] getBytedata() {
        return this.bytedata;
    }

    public void setBytedata(byte[] bArr) {
        this.bytedata = bArr;
    }

    public void setConnHeadFields(Map<String, List<String>> map) {
        this.connHeadFields = map;
    }

    public Map<String, List<String>> getConnHeadFields() {
        return this.connHeadFields;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NetworkResponse [");
        sb.append("statusCode=");
        sb.append(this.statusCode);
        sb.append(", desc=");
        sb.append(this.desc);
        sb.append(", connHeadFields=");
        sb.append(this.connHeadFields);
        sb.append(", bytedata=");
        sb.append(this.bytedata != null ? new String(this.bytedata) : "");
        sb.append(", error=");
        sb.append(this.error);
        sb.append(", statisticData=");
        sb.append(this.statisticData);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    public NetworkResponse() {
    }

    public NetworkResponse(int i) {
        this.statusCode = i;
        this.desc = ErrorConstant.getErrMsg(i);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Throwable getError() {
        return this.error;
    }

    public void setError(Throwable th) {
        this.error = th;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.statusCode);
        parcel.writeString(this.desc);
        int length = this.bytedata != null ? this.bytedata.length : 0;
        parcel.writeInt(length);
        if (length > 0) {
            parcel.writeByteArray(this.bytedata);
        }
        parcel.writeMap(this.connHeadFields);
        if (this.statisticData != null) {
            parcel.writeSerializable(this.statisticData);
        }
    }

    public static NetworkResponse readFromParcel(Parcel parcel) {
        NetworkResponse networkResponse = new NetworkResponse();
        try {
            networkResponse.statusCode = parcel.readInt();
            networkResponse.desc = parcel.readString();
            int readInt = parcel.readInt();
            if (readInt > 0) {
                networkResponse.bytedata = new byte[readInt];
                parcel.readByteArray(networkResponse.bytedata);
            }
            networkResponse.connHeadFields = parcel.readHashMap(NetworkResponse.class.getClassLoader());
            try {
                networkResponse.statisticData = (StatisticData) parcel.readSerializable();
            } catch (Throwable unused) {
                ALog.i(TAG, "[readFromParcel] source.readSerializable() error", (String) null, new Object[0]);
            }
        } catch (Exception e) {
            ALog.w(TAG, "[readFromParcel]", (String) null, e, new Object[0]);
        }
        return networkResponse;
    }

    public void setStatisticData(StatisticData statisticData2) {
        this.statisticData = statisticData2;
    }

    public StatisticData getStatisticData() {
        return this.statisticData;
    }
}
