package com.taobao.android.dinamic.model;

public class DinamicTailer {
    private byte[] checksum;
    private String complierVersion;
    private String magicWord;
    private long offset;
    private byte[] reserved;

    public String getMagicWord() {
        return this.magicWord;
    }

    public void setMagicWord(String str) {
        this.magicWord = str;
    }

    public String getComplierVersion() {
        return this.complierVersion;
    }

    public void setComplierVersion(String str) {
        this.complierVersion = str;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long j) {
        this.offset = j;
    }

    public byte[] getReserved() {
        return this.reserved;
    }

    public void setReserved(byte[] bArr) {
        this.reserved = bArr;
    }

    public byte[] getChecksum() {
        return this.checksum;
    }

    public void setChecksum(byte[] bArr) {
        this.checksum = bArr;
    }
}
