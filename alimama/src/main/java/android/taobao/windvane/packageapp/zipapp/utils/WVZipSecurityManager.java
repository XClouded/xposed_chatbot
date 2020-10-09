package android.taobao.windvane.packageapp.zipapp.utils;

import android.annotation.TargetApi;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import android.util.LruCache;

import com.taobao.ju.track.constants.Constants;
import com.taobao.weex.el.parse.Operators;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class WVZipSecurityManager {
    private static int MAX_LRU_CACHE_SIZE = 1000;
    private static WVZipSecurityManager mSecTokenCache;
    private String TAG = WVZipSecurityManager.class.getSimpleName();
    private final Object lock = new Object();
    private LruCache<String, AppResInfo> mLruCache = new LruCache<>(MAX_LRU_CACHE_SIZE);
    private HashMap<String, String> mSampleMap = new HashMap<>();

    public void setSampleMap(HashMap<String, String> hashMap) {
        this.mSampleMap = hashMap;
    }

    public void parseSampleMap(String str) {
        try {
            if (TaoLog.getLogStatus()) {
                String str2 = this.TAG;
                TaoLog.d(str2, "每个app的采样率配置信息  data = " + str);
            }
            this.mSampleMap = new HashMap<>();
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                this.mSampleMap.put(next, jSONObject.getString(next));
            }
        } catch (Exception e) {
            String str3 = this.TAG;
            TaoLog.e(str3, "app的采样率配置信息  error = " + e.getMessage());
        }
    }

    public static synchronized WVZipSecurityManager getInstance() {
        WVZipSecurityManager wVZipSecurityManager;
        synchronized (WVZipSecurityManager.class) {
            if (mSecTokenCache == null) {
                mSecTokenCache = new WVZipSecurityManager();
            }
            wVZipSecurityManager = mSecTokenCache;
        }
        return wVZipSecurityManager;
    }

    @TargetApi(12)
    WVZipSecurityManager() {
    }

    @TargetApi(12)
    public void put(String str, String str2, JSONObject jSONObject) {
        if (this.mLruCache != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.mLruCache.put(str, new AppResInfo(str2, jSONObject));
        }
    }

    @TargetApi(12)
    public String get(String str) {
        if (this.mLruCache == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.mLruCache.get(str).mHash;
    }

    @TargetApi(12)
    public int getLruSize() {
        if (this.mLruCache != null) {
            return this.mLruCache.size();
        }
        return 0;
    }

    public boolean isFileSecrity(String str, byte[] bArr, String str2, String str3) {
        try {
            TaoLog.d(this.TAG, "开始安全校验 ");
            long currentTimeMillis = System.currentTimeMillis();
            VerifyUtData verifyUtData = new VerifyUtData();
            boolean isFileSecrity = isFileSecrity(str, bArr, str2, verifyUtData, str3);
            if (WVMonitorService.getPerformanceMonitor() != null) {
                if (TaoLog.getLogStatus()) {
                    String str4 = this.TAG;
                    TaoLog.d(str4, "  安全校验 埋点信息 utdata.verifyResTime=【" + verifyUtData.verifyResTime + "】  utdata.verifyTime=【" + verifyUtData.verifyTime + "】  utdata.verifyError=【" + verifyUtData.verifyError + "】 LRUcache size =【 " + getLruSize() + "】");
                }
                String str5 = str;
                WVMonitorService.getPerformanceMonitor().didGetResourceVerifyCode(str5, verifyUtData.verifyResTime, verifyUtData.verifyTime, verifyUtData.verifyError, getLruSize());
                if (!isFileSecrity && TaoLog.getLogStatus()) {
                    String str6 = this.TAG;
                    TaoLog.d(str6, "  安全校验 失败 url=" + str);
                }
                if (TaoLog.getLogStatus()) {
                    String str7 = this.TAG;
                    TaoLog.d(str7, "  安全校验 成功 result =" + isFileSecrity + "cost time【" + (System.currentTimeMillis() - currentTimeMillis) + "】");
                }
            }
            return isFileSecrity;
        } catch (Exception unused) {
            return false;
        }
    }

    public double getAppSample(String str) {
        parseSampleMap(WVCommonConfig.commonConfig.verifySampleRate);
        if (str == null || this.mSampleMap == null || this.mSampleMap.size() <= 0) {
            return -1.0d;
        }
        try {
            double parseDouble = Double.parseDouble(this.mSampleMap.get(str));
            if (parseDouble < 0.0d || parseDouble > 1.0d) {
                return -1.0d;
            }
            return parseDouble;
        } catch (Exception e) {
            String str2 = this.TAG;
            TaoLog.d(str2, "获取【" + str + "】采样率失败" + "数据格式错误error :" + e.getMessage());
            return -1.0d;
        }
    }

    @TargetApi(12)
    public boolean isFileSecrity(String str, byte[] bArr, String str2, VerifyUtData verifyUtData, String str3) {
        String removeQueryParam = WVUrlUtil.removeQueryParam(str);
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mLruCache.get(removeQueryParam) == null) {
            int lastIndexOf = str2.lastIndexOf("/");
            if (lastIndexOf < 0) {
                String str4 = this.TAG;
                TaoLog.d(str4, "本地资源的绝对路径出错 path= " + str2);
                return false;
            }
            StringBuilder sb = new StringBuilder();
            int i = lastIndexOf + 1;
            sb.append(str2.substring(0, i));
            sb.append(ZipAppConstants.APP_RES_NAME);
            String sb2 = sb.toString();
            int validRunningZipPackage = ZipAppManager.getInstance().validRunningZipPackage(str2.substring(0, i) + ZipAppConstants.APP_RES_INC_NAME);
            int validRunningZipPackage2 = ZipAppManager.getInstance().validRunningZipPackage(sb2);
            if (validRunningZipPackage != ZipAppResultCode.SECCUSS) {
                verifyUtData.verifyError = validRunningZipPackage;
            } else if (validRunningZipPackage2 != ZipAppResultCode.SECCUSS) {
                verifyUtData.verifyError = validRunningZipPackage2;
            }
            verifyUtData.verifyResTime = System.currentTimeMillis() - currentTimeMillis;
            String str5 = this.TAG;
            TaoLog.e(str5, "validRunningZipPackage all time =【" + verifyUtData.verifyResTime + "】");
            if (verifyUtData.verifyError != ZipAppResultCode.SECCUSS) {
                return false;
            }
        }
        String md5ToHex = DigestUtils.md5ToHex(bArr);
        verifyUtData.verifyTime = System.currentTimeMillis() - currentTimeMillis;
        if (this.mLruCache != null && md5ToHex.equals(this.mLruCache.get(removeQueryParam))) {
            return true;
        }
        verifyUtData.verifyError = ZipAppResultCode.ERR_MD5_RES;
        return false;
    }

    public AppResInfo getAppResInfo(ZipAppInfo zipAppInfo, String str) {
        ZipGlobalConfig.CacheFileData isZcacheUrl;
        if (zipAppInfo == null && (zipAppInfo = WVPackageAppRuntime.getAppInfoByUrl(str)) == null && (isZcacheUrl = ConfigManager.getLocGlobalConfig().isZcacheUrl(str)) != null) {
            zipAppInfo = ConfigManager.getLocGlobalConfig().getAppInfo(isZcacheUrl.appName);
        }
        if (zipAppInfo == null) {
            return null;
        }
        String zipResAbsolutePath = ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, ZipAppConstants.APP_RES_NAME, false);
        String removeQueryParam = WVUrlUtil.removeQueryParam(str);
        if (this.mLruCache.get(removeQueryParam) == null) {
            int lastIndexOf = zipResAbsolutePath.lastIndexOf("/");
            if (lastIndexOf < 0) {
                String str2 = this.TAG;
                TaoLog.d(str2, "本地资源的绝对路径出错 path= " + zipResAbsolutePath);
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int i = lastIndexOf + 1;
            sb.append(zipResAbsolutePath.substring(0, i));
            sb.append(ZipAppConstants.APP_RES_NAME);
            String sb2 = sb.toString();
            synchronized (this.lock) {
                if (!new File(sb2).exists() && zipAppInfo != null && (zipAppInfo.installedSeq != 0 || !zipAppInfo.installedVersion.equals(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE))) {
                    zipAppInfo.installedSeq = 0;
                    zipAppInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                    TaoLog.i("ZCache", "清理本地异常文件,name=[" + zipAppInfo.name + "],seq=[" + zipAppInfo.installedSeq + Operators.ARRAY_END_STR);
                }
            }
            ZipAppManager.getInstance().validRunningZipPackage(zipResAbsolutePath.substring(0, i) + ZipAppConstants.APP_RES_INC_NAME);
            ZipAppManager.getInstance().validRunningZipPackage(sb2);
        }
        return this.mLruCache.get(removeQueryParam);
    }
}
