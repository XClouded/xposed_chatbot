package com.alibaba.android.prefetchx.core.file;

import androidx.annotation.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IConnection {
    public static final String TYPE_MOBILE_2G = "2g";
    public static final String TYPE_MOBILE_3G = "3g";
    public static final String TYPE_MOBILE_4G = "4g";
    public static final String TYPE_NONE = "none";
    public static final String TYPE_OTHER = "other";
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_WIFI = "wifi";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectionType {
    }

    @NonNull
    String getConnectionType();
}
