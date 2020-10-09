package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ErrorConstant;
import anetwork.channel.NetworkEvent;
import anetwork.channel.statist.StatisticData;
import com.taobao.weex.el.parse.Operators;

public class DefaultFinishEvent implements NetworkEvent.FinishEvent, Parcelable {
    public static final Parcelable.Creator<DefaultFinishEvent> CREATOR = new Parcelable.Creator<DefaultFinishEvent>() {
        public DefaultFinishEvent createFromParcel(Parcel parcel) {
            return DefaultFinishEvent.readFromParcel(parcel);
        }

        public DefaultFinishEvent[] newArray(int i) {
            return new DefaultFinishEvent[i];
        }
    };
    int code;
    Object context;
    String desc;
    public final Request request;
    public final RequestStatistic rs;
    StatisticData statisticData;

    public int describeContents() {
        return 0;
    }

    public Object getContext() {
        return this.context;
    }

    public void setContext(Object obj) {
        this.context = obj;
    }

    public int getHttpCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public StatisticData getStatisticData() {
        return this.statisticData;
    }

    public DefaultFinishEvent(int i) {
        this(i, (String) null, (Request) null, (RequestStatistic) null);
    }

    public DefaultFinishEvent(int i, String str, RequestStatistic requestStatistic) {
        this(i, str, (Request) null, requestStatistic);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public DefaultFinishEvent(int i, String str, Request request2) {
        this(i, str, request2, request2 != null ? request2.rs : null);
    }

    private DefaultFinishEvent(int i, String str, Request request2, RequestStatistic requestStatistic) {
        this.statisticData = new StatisticData();
        this.code = i;
        this.desc = str == null ? ErrorConstant.getErrMsg(i) : str;
        this.request = request2;
        this.rs = requestStatistic;
    }

    public String toString() {
        return "DefaultFinishEvent [" + "code=" + this.code + ", desc=" + this.desc + ", context=" + this.context + ", statisticData=" + this.statisticData + Operators.ARRAY_END_STR;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.code);
        parcel.writeString(this.desc);
        if (this.statisticData != null) {
            parcel.writeSerializable(this.statisticData);
        }
    }

    static DefaultFinishEvent readFromParcel(Parcel parcel) {
        DefaultFinishEvent defaultFinishEvent = new DefaultFinishEvent(0);
        try {
            defaultFinishEvent.code = parcel.readInt();
            defaultFinishEvent.desc = parcel.readString();
            defaultFinishEvent.statisticData = (StatisticData) parcel.readSerializable();
        } catch (Throwable unused) {
        }
        return defaultFinishEvent;
    }
}
