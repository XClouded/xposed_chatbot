package android.taobao.windvane.packageapp.zipapp.data;

import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;

import com.taobao.ju.track.constants.Constants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZipGlobalConfig {
    private String TAG = "ZipGlobalConfig";
    public String i = "0";
    private Map<String, ZipAppInfo> mAppsMap = new ConcurrentHashMap();
    private Hashtable<String, ArrayList<String>> mZcacheResConfig = new Hashtable<>();
    public String online_v = null;
    public String v = "0";

    public static class CacheFileData {
        public String appName;
        public String errorCode;
        public String path;
        public long seq;
        public String v;
    }

    public Map<String, ZipAppInfo> getAppsTable() {
        return this.mAppsMap;
    }

    public ZipAppInfo getAppInfo(String str) {
        if (!isAvailableData()) {
            return null;
        }
        return this.mAppsMap.get(str);
    }

    public void putAppInfo2Table(String str, ZipAppInfo zipAppInfo) {
        if (str != null && zipAppInfo != null && zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_REACT && zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN && this.mAppsMap != null) {
            if (this.mAppsMap.containsKey(str)) {
                ZipAppInfo zipAppInfo2 = this.mAppsMap.get(str);
                if (zipAppInfo.getInfo() != ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                    zipAppInfo2.f = zipAppInfo.f;
                    if (zipAppInfo2.s <= zipAppInfo.s) {
                        zipAppInfo2.s = zipAppInfo.s;
                        zipAppInfo2.v = zipAppInfo.v;
                        zipAppInfo2.t = zipAppInfo.t;
                        zipAppInfo2.z = zipAppInfo.z;
                        zipAppInfo2.isOptional = zipAppInfo.isOptional;
                        zipAppInfo2.isPreViewApp = zipAppInfo.isPreViewApp;
                        if (zipAppInfo.folders != null && zipAppInfo.folders.size() > 0) {
                            TaoLog.e(this.TAG + "-Folders", "Before replace: " + zipAppInfo2.name + " [" + (zipAppInfo2.folders == null ? -1 : zipAppInfo2.folders.size()) + "] ");
                            zipAppInfo2.folders = zipAppInfo.folders;
                            StringBuilder sb = new StringBuilder();
                            sb.append(this.TAG);
                            sb.append("-Folders");
                            TaoLog.e(sb.toString(), "Replace " + zipAppInfo2.name + " folders to [" + zipAppInfo.folders.size() + "] ");
                        }
                        if (!TextUtils.isEmpty(zipAppInfo.mappingUrl)) {
                            zipAppInfo2.mappingUrl = zipAppInfo.mappingUrl;
                        }
                        if (zipAppInfo.installedSeq > 0) {
                            zipAppInfo2.installedSeq = zipAppInfo.installedSeq;
                        }
                        if (!zipAppInfo.installedVersion.equals(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE)) {
                            zipAppInfo2.installedVersion = zipAppInfo.installedVersion;
                        }
                    }
                } else if (zipAppInfo2.isOptional || zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                    zipAppInfo2.status = ZipAppConstants.ZIP_REMOVED;
                    zipAppInfo2.f = zipAppInfo.f;
                } else {
                    zipAppInfo2.isOptional = true;
                }
            } else {
                this.mAppsMap.put(str, zipAppInfo);
            }
        }
    }

    public void removeAppInfoFromTable(String str) {
        if (str != null && this.mAppsMap != null) {
            this.mAppsMap.remove(str);
        }
    }

    public boolean isAvailableData() {
        return this.mAppsMap != null && !this.mAppsMap.isEmpty();
    }

    public void reset() {
        this.v = "0";
        this.i = "0";
        if (isAvailableData()) {
            this.mAppsMap.clear();
        }
        if (this.mZcacheResConfig != null) {
            this.mZcacheResConfig.clear();
        }
    }

    public boolean isAllAppUpdated() {
        if (!isAvailableData()) {
            return true;
        }
        try {
            for (Map.Entry<String, ZipAppInfo> value : this.mAppsMap.entrySet()) {
                ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                if (zipAppInfo.status != ZipAppConstants.ZIP_REMOVED) {
                    if (zipAppInfo.s != zipAppInfo.installedSeq) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public Hashtable<String, ArrayList<String>> getZcacheResConfig() {
        return this.mZcacheResConfig;
    }

    public void setZcacheResConfig(Hashtable<String, ArrayList<String>> hashtable) {
        if (this.mZcacheResConfig != null) {
            this.mZcacheResConfig.putAll(hashtable);
            if (TaoLog.getLogStatus()) {
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("ZcacheforDebug 设置Zcache 的url map size:");
                sb.append(hashtable != null ? hashtable.size() : 0);
                TaoLog.d(str, sb.toString());
            }
        }
    }

    public void addZcacheResConfig(String str, ArrayList<String> arrayList) {
        if (str != null && arrayList != null && !arrayList.isEmpty()) {
            this.mZcacheResConfig.put(str, arrayList);
            String str2 = this.TAG;
            TaoLog.d(str2, "ZcacheforDebug 新增zcache name:" + str);
        }
    }

    public void removeZcacheRes(String str) {
        if (str != null) {
            this.mZcacheResConfig.remove(str);
            String str2 = this.TAG;
            TaoLog.d(str2, "ZcacheforDebug 删除zcache name:" + str);
        }
    }

    public CacheFileData isZcacheUrl(String str) {
        Exception e;
        String str2;
        if (this.mZcacheResConfig == null) {
            return null;
        }
        try {
            str2 = WVUrlUtil.removeQueryParam(str);
            try {
                String md5ToHex = DigestUtils.md5ToHex(str2);
                for (Map.Entry next : this.mZcacheResConfig.entrySet()) {
                    ArrayList arrayList = (ArrayList) next.getValue();
                    String str3 = (String) next.getKey();
                    if (arrayList != null && arrayList.contains(md5ToHex)) {
                        ZipAppInfo zipAppInfo = this.mAppsMap.get(str3);
                        if (!(this.mAppsMap == null || zipAppInfo == null)) {
                            CacheFileData cacheFileData = new CacheFileData();
                            cacheFileData.appName = zipAppInfo.name;
                            cacheFileData.v = zipAppInfo.v;
                            cacheFileData.path = ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, md5ToHex, false);
                            cacheFileData.seq = zipAppInfo.s;
                            return cacheFileData;
                        }
                    }
                }
                return null;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                String str4 = this.TAG;
                TaoLog.d(str4, "ZcacheforDebug 资源url 解析匹配异常，url=" + str2);
                return null;
            }
        } catch (Exception e3) {
            Exception exc = e3;
            str2 = str;
            e = exc;
            e.printStackTrace();
            String str42 = this.TAG;
            TaoLog.d(str42, "ZcacheforDebug 资源url 解析匹配异常，url=" + str2);
            return null;
        }
    }
}
