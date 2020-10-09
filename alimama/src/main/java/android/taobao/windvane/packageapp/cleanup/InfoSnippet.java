package android.taobao.windvane.packageapp.cleanup;

public class InfoSnippet {
    public double count = 0.0d;
    public int failCount = 0;
    public long lastAccessTime = 0;
    public String name = "";
    public boolean needReinstall = false;

    public InfoSnippet() {
    }

    public InfoSnippet(String str, long j, long j2, int i, int i2) {
        this.name = str;
        this.count = (double) j;
        this.lastAccessTime = j2;
        this.failCount = i2;
    }

    public String toString() {
        return "InfoSnippet{name='" + this.name + '\'' + ", lastAccessTime=" + this.lastAccessTime + ", needReinstall=" + this.needReinstall + ", failCount=" + this.failCount + ", count=" + this.count + '}';
    }
}
