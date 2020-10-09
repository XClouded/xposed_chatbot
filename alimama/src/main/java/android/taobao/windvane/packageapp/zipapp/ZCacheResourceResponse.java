package android.taobao.windvane.packageapp.zipapp;

import android.text.TextUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ZCacheResourceResponse {
    public static final String ZCACHE_INFO = "X-ZCache-Info";
    public String encoding;
    public Map<String, String> headers;
    public InputStream inputStream;
    public boolean isSuccess = false;
    public String mimeType;

    public void insertZCacheInfo(String str, long j, String str2) {
        if (!TextUtils.isEmpty(str)) {
            if (TextUtils.isEmpty(str2)) {
                str2 = "0";
            }
            if (this.headers == null) {
                this.headers = new HashMap();
            }
            this.headers.put("X-ZCache-Info", str + "_" + j + "_" + str2);
        }
    }
}
