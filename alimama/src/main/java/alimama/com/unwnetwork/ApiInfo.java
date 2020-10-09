package alimama.com.unwnetwork;

public class ApiInfo {
    private final String mAPI;
    private final boolean mNeedECode;
    private final boolean mNeedSession;
    private boolean mUseWua;
    private final String mVersion;

    public ApiInfo(String str, String str2, boolean z, boolean z2) {
        this.mAPI = str;
        this.mVersion = str2;
        this.mNeedECode = z;
        this.mNeedSession = z2;
    }

    public ApiInfo(String str, String str2) {
        this(str, str2, false, true);
    }

    public String getAPIName() {
        return this.mAPI;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public boolean needSession() {
        return this.mNeedSession;
    }

    public boolean needECode() {
        return this.mNeedECode;
    }

    public boolean useWua() {
        return this.mUseWua;
    }

    public ApiInfo setUseWua(boolean z) {
        this.mUseWua = z;
        return this;
    }

    public String toString() {
        return "ApiInfo{mVersion='" + this.mVersion + '\'' + ", mAPI='" + this.mAPI + '\'' + '}';
    }
}
