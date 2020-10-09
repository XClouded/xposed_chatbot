package android.taobao.windvane.webview;

public class WindVaneError extends Error {
    private static final long serialVersionUID = 8736004749630607428L;

    public WindVaneError() {
    }

    public WindVaneError(String str) {
        super(str);
    }

    public WindVaneError(String str, Throwable th) {
        super(str, th);
    }

    public WindVaneError(Throwable th) {
        super(th);
    }
}
