package android.taobao.windvane.connect;

import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.util.CommonUtils;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IResponse {
    private int StatusCode;
    private byte[] data;
    private String desc;
    private String encoding;
    private Map<String, String> headersMap = new HashMap();
    public WVPerformanceMonitorInterface.NetStat mNetstat = new WVPerformanceMonitorInterface.NetStat();
    private String mimeType;

    public String getMimeType() {
        return this.mimeType;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public int getStatusCode() {
        return this.StatusCode;
    }

    public void setStatusCode(int i) {
        this.StatusCode = i;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public Map<String, String> getHeadersMap() {
        return this.headersMap;
    }

    public void setHeadMap(Map<String, List<String>> map) {
        if (map != null) {
            for (String next : map.keySet()) {
                List list = map.get(next);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        this.headersMap.put(next, list.get(i));
                    }
                }
            }
            String str = this.headersMap.get("content-type");
            if (str != null) {
                this.mimeType = CommonUtils.parseMimeType(str);
                this.encoding = CommonUtils.parseCharset(str);
                this.encoding = TextUtils.isEmpty(this.encoding) ? "utf-8" : this.encoding;
            }
        }
    }
}
