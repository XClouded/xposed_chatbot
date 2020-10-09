package com.uploader.export;

import androidx.annotation.NonNull;

public class EnvironmentElement {
    public final String appKey;
    public final String authCode;
    public final int environment;
    public final String host;
    public final String ipAddress;

    public EnvironmentElement(int i, @NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4) {
        this.environment = i;
        this.appKey = str;
        this.host = str2;
        this.ipAddress = str3;
        this.authCode = str4;
    }

    public EnvironmentElement(int i, @NonNull String str, @NonNull String str2, @NonNull String str3) {
        this.environment = i;
        this.appKey = str;
        this.host = str2;
        this.ipAddress = str3;
        this.authCode = "";
    }
}
