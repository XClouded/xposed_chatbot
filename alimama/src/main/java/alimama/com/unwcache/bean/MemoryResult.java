package alimama.com.unwcache.bean;

public class MemoryResult {
    public byte[] data;
    public String extra;

    public MemoryResult(byte[] bArr, String str) {
        this.data = bArr;
        this.extra = str;
    }
}
