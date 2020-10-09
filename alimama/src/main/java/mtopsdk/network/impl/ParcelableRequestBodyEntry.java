package mtopsdk.network.impl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.request.BodyEntry;
import java.io.IOException;
import java.io.OutputStream;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;

public class ParcelableRequestBodyEntry implements BodyEntry {
    public static final Parcelable.Creator<ParcelableRequestBodyEntry> CREATOR = new Parcelable.Creator<ParcelableRequestBodyEntry>() {
        public ParcelableRequestBodyEntry createFromParcel(Parcel parcel) {
            return new ParcelableRequestBodyEntry(parcel);
        }

        public ParcelableRequestBodyEntry[] newArray(int i) {
            return new ParcelableRequestBodyEntry[i];
        }
    };
    ParcelableRequestBodyImpl requestBody;

    public int describeContents() {
        return 0;
    }

    public ParcelableRequestBodyEntry(ParcelableRequestBodyImpl parcelableRequestBodyImpl) {
        this.requestBody = parcelableRequestBodyImpl;
    }

    public String getContentType() {
        return this.requestBody.contentType();
    }

    public int writeTo(OutputStream outputStream) throws IOException {
        this.requestBody.writeTo(outputStream);
        return (int) this.requestBody.contentLength();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.requestBody, i);
    }

    protected ParcelableRequestBodyEntry(Parcel parcel) {
        this.requestBody = (ParcelableRequestBodyImpl) parcel.readParcelable(ParcelableRequestBodyImpl.class.getClassLoader());
    }
}
