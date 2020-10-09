package android.taobao.windvane.extra.jsbridge;

import java.util.HashMap;
import java.util.Map;

public class ServerParams {
    public String api;
    private Map<String, String> data = new HashMap();
    public boolean ecode;
    public boolean isSec;
    public boolean post;
    public String v;

    public Map<String, String> getData() {
        return this.data;
    }

    public void addData(String str, String str2) {
        this.data.put(str, str2);
    }
}
