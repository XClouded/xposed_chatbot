package com.taobao.android.dinamic.model;

public class DinamicInfo {
    private String interpreterVersion;
    private byte[] option1;
    private byte[] padding1;

    public String getInterpreterVersion() {
        return this.interpreterVersion;
    }

    public void setInterpreterVersion(String str) {
        this.interpreterVersion = str;
    }

    public byte[] getPadding1() {
        return this.padding1;
    }

    public void setPadding1(byte[] bArr) {
        this.padding1 = bArr;
    }

    public byte[] getOption1() {
        return this.option1;
    }

    public void setOption1(byte[] bArr) {
        this.option1 = bArr;
    }
}
