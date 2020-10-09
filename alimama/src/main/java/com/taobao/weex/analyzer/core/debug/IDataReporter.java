package com.taobao.weex.analyzer.core.debug;

import androidx.annotation.NonNull;

public interface IDataReporter<T> {
    public static final String API_VERSION = "1";

    boolean isEnabled();

    void report(@NonNull ProcessedData<T> processedData);

    public static class ProcessedData<T> {
        public T data;
        public String deviceId;
        public int sequenceId;
        public long timestamp;
        public String type;
        public String version;

        public int getSequenceId() {
            return this.sequenceId;
        }

        public void setSequenceId(int i) {
            this.sequenceId = i;
        }

        public String getDeviceId() {
            return this.deviceId;
        }

        public void setDeviceId(String str) {
            this.deviceId = str;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String str) {
            this.version = str;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(long j) {
            this.timestamp = j;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public T getData() {
            return this.data;
        }

        public void setData(T t) {
            this.data = t;
        }
    }

    public static final class ProcessedDataBuilder<T> {
        private T mData;
        private String mDeviceId;
        private int mSequenceId;
        private long mTimestamp = System.currentTimeMillis();
        private String mType;
        private String mVersion = "1";

        public ProcessedDataBuilder<T> sequenceId(int i) {
            this.mSequenceId = i;
            return this;
        }

        public ProcessedDataBuilder<T> deviceId(String str) {
            this.mDeviceId = str;
            return this;
        }

        public ProcessedDataBuilder<T> type(String str) {
            this.mType = str;
            return this;
        }

        public ProcessedDataBuilder<T> data(T t) {
            this.mData = t;
            return this;
        }

        public ProcessedData<T> build() {
            ProcessedData<T> processedData = new ProcessedData<>();
            processedData.sequenceId = this.mSequenceId;
            processedData.deviceId = this.mDeviceId;
            processedData.timestamp = this.mTimestamp;
            processedData.data = this.mData;
            processedData.type = this.mType;
            processedData.version = this.mVersion;
            return processedData;
        }
    }
}
