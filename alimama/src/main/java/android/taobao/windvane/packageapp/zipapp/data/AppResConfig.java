package android.taobao.windvane.packageapp.zipapp.data;

import org.json.JSONObject;

import java.util.Hashtable;

public class AppResConfig {
    public ZipAppInfo mAppinfo;
    public Hashtable<String, FileInfo> mResfileMap = new Hashtable<>();
    public String tk;

    public FileInfo getResfileInfo(String str) {
        if (this.mResfileMap == null || !this.mResfileMap.containsKey(str)) {
            return null;
        }
        return this.mResfileMap.get(str);
    }

    public void setAppInfo(ZipAppInfo zipAppInfo) {
        this.mAppinfo = zipAppInfo;
    }

    public ZipAppInfo getAppInfo() {
        return this.mAppinfo;
    }

    public class FileInfo {
        public JSONObject headers;
        public String path;
        public String url;
        public String v;

        public FileInfo() {
        }
    }
}
