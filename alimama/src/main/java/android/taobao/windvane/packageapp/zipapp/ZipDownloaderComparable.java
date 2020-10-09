package android.taobao.windvane.packageapp.zipapp;

public class ZipDownloaderComparable implements Comparable<ZipDownloaderComparable> {
    private String name = "";
    private int priority = 0;

    public ZipDownloaderComparable(String str, int i) {
        this.name = str;
        this.priority = i;
    }

    public String getAppName() {
        return this.name;
    }

    public int compareTo(ZipDownloaderComparable zipDownloaderComparable) {
        return zipDownloaderComparable.priority - this.priority;
    }
}
