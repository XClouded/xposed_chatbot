package com.taobao.phenix.decode;

public class DecodeException extends Throwable {
    private boolean mDataFromDisk;
    private DecodedError mDecodedError;
    private boolean misLocalUri;

    public enum DecodedError {
        SUCCESS,
        OOM_ERROR,
        UNAVAILABLE_INPUT_ERROR,
        UNAVAILABLE_OUTPUT_ERROR,
        EMPTY_BYTES_ERROR,
        UNLINK_SO_ERROR,
        UNKNOWN_ERROR,
        WEBP_FORMAT_ERROR
    }

    public DecodeException(DecodedError decodedError) {
        this.mDecodedError = decodedError;
    }

    public DecodeException(DecodedError decodedError, String str) {
        super(str);
        this.mDecodedError = decodedError;
    }

    public DecodeException(DecodedError decodedError, Throwable th) {
        super(th);
        this.mDecodedError = decodedError;
    }

    public DecodedError getDecodedError() {
        return this.mDecodedError;
    }

    public boolean isDataFromDisk() {
        return this.mDataFromDisk;
    }

    public void dataFromDisk(boolean z) {
        this.mDataFromDisk = z;
    }

    public boolean isLocalUri() {
        return this.misLocalUri;
    }

    public void setLocalUri(boolean z) {
        this.misLocalUri = z;
    }

    public String toString() {
        return "DecodeException:type=" + this.mDecodedError + " localUri=" + this.misLocalUri + " fromDisk=" + this.mDataFromDisk + " throwable=" + getMessage();
    }
}
