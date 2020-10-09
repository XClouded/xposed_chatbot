package com.taobao.phenix.loader;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.Consumer;

public class StreamResultHandler {
    public final int contentLength;
    private byte[] data;
    private boolean mCancelled;
    private final Consumer<?, ImageRequest> mConsumer;
    private final int mProgressStep;
    private int mProgressStepCount;
    private int readLength;

    public StreamResultHandler(Consumer<?, ImageRequest> consumer, int i, int i2) {
        this.mConsumer = consumer;
        this.contentLength = i;
        this.mProgressStep = i2;
    }

    public boolean inLimit(int i) {
        return this.contentLength <= 0 || this.readLength + i <= this.contentLength;
    }

    public synchronized boolean onProgressUpdate(int i) {
        this.readLength += i;
        if (this.mConsumer == null) {
            return true;
        }
        if (this.contentLength > 0 && this.mProgressStep > 0) {
            float f = ((float) this.readLength) / ((float) this.contentLength);
            int i2 = (int) ((100.0f * f) / ((float) this.mProgressStep));
            if (i2 > this.mProgressStepCount || this.readLength == this.contentLength) {
                this.mProgressStepCount = i2;
                this.mConsumer.onProgressUpdate(f);
            }
        }
        if (!this.mConsumer.getContext().isCancelled()) {
            return true;
        }
        UnitedLog.i("Stream", this.mConsumer.getContext(), "Request is cancelled while reading stream", new Object[0]);
        this.mConsumer.onCancellation();
        this.mCancelled = true;
        return false;
    }

    public boolean isDataIncomplete() {
        return this.data == null || (this.contentLength > 0 && this.readLength != this.contentLength);
    }

    public boolean isCancellationCalled() {
        return this.mCancelled;
    }

    public int getReadLength() {
        return this.readLength;
    }

    public void setupData(byte[] bArr) {
        this.data = bArr;
    }

    public EncodedData getEncodeData() {
        return new EncodedData(!isDataIncomplete(), this.data, 0, this.readLength);
    }
}
