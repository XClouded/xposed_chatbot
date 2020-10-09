package mtopsdk.network.domain;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;
import java.io.OutputStream;

public class ParcelableRequestBodyImpl extends RequestBody implements Parcelable {
    public static final Parcelable.Creator<ParcelableRequestBodyImpl> CREATOR = new Parcelable.Creator<ParcelableRequestBodyImpl>() {
        public ParcelableRequestBodyImpl createFromParcel(Parcel parcel) {
            return new ParcelableRequestBodyImpl(parcel);
        }

        public ParcelableRequestBodyImpl[] newArray(int i) {
            return new ParcelableRequestBodyImpl[i];
        }
    };
    private byte[] content;
    private String contentType;

    public int describeContents() {
        return 0;
    }

    public ParcelableRequestBodyImpl(String str, byte[] bArr) {
        this.content = bArr;
        this.contentType = str;
    }

    public String contentType() {
        return this.contentType;
    }

    public long contentLength() {
        return this.content != null ? (long) this.content.length : super.contentLength();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.content);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.contentType);
        parcel.writeByteArray(this.content);
    }

    protected ParcelableRequestBodyImpl(Parcel parcel) {
        this.contentType = parcel.readString();
        this.content = parcel.createByteArray();
    }
}
