package mtopsdk.config;

import android.content.Context;
import androidx.annotation.NonNull;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.cache.domain.ApiCacheDo;
import mtopsdk.mtop.cache.domain.AppConfigDo;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppConfigManager {
    private static final String CACEH_KEY_TYPE = "kt=";
    private static final String CACHE_KEY_LIST = "ks=";
    private static final String FILE_DIR_MTOP = "/mtop";
    private static final String FILE_NAME_API_CACHE_CONFIG = "apiCacheConf";
    private static final String FILE_NAME_APP_CONFIG = "appConf";
    private static final String TAG = "mtopsdk.AppConfigManager";
    private static AppConfigManager instance;
    ConcurrentHashMap<String, ApiCacheDo> apiCacheGroup = new ConcurrentHashMap<>();
    private Set<String> tradeUnitApiSet = new HashSet();

    public static AppConfigManager getInstance() {
        if (instance == null) {
            synchronized (AppConfigManager.class) {
                if (instance == null) {
                    instance = new AppConfigManager();
                }
            }
        }
        return instance;
    }

    public ApiCacheDo getApiCacheDoByKey(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return this.apiCacheGroup.get(str);
    }

    public void addApiCacheDoToGroup(String str, ApiCacheDo apiCacheDo) {
        if (!StringUtils.isBlank(str) && apiCacheDo != null) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, "[addApiCacheDoToGroup] apiCacheDo:" + apiCacheDo);
            }
            this.apiCacheGroup.put(str, apiCacheDo);
        }
    }

    public boolean isTradeUnitApi(String str) {
        return this.tradeUnitApiSet.contains(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0084 A[Catch:{ Exception -> 0x00ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0085 A[Catch:{ Exception -> 0x00ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x008a A[Catch:{ Exception -> 0x00ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008f A[Catch:{ Exception -> 0x00ba }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0094 A[Catch:{ Exception -> 0x00ba }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseCacheControlHeader(@androidx.annotation.NonNull java.lang.String r11, @androidx.annotation.NonNull mtopsdk.mtop.cache.domain.ApiCacheDo r12) {
        /*
            r10 = this;
            if (r11 == 0) goto L_0x00dd
            if (r12 != 0) goto L_0x0006
            goto L_0x00dd
        L_0x0006:
            java.lang.String r0 = ","
            java.lang.String[] r0 = r11.split(r0)
            int r1 = r0.length
            r2 = 0
            r3 = 0
        L_0x000f:
            if (r3 >= r1) goto L_0x00dc
            r4 = r0[r3]
            java.lang.String r5 = "of=on"
            boolean r5 = r5.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x00ba }
            r6 = 1
            if (r5 == 0) goto L_0x0020
            r12.offline = r6     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x0020:
            java.lang.String r5 = "private=false"
            boolean r5 = r5.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x002c
            r12.privateScope = r2     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x002c:
            java.lang.String r5 = "kt="
            boolean r5 = r4.contains(r5)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0099
            java.lang.String r5 = "kt="
            int r5 = r5.length()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r5 = r4.substring(r5)     // Catch:{ Exception -> 0x00ba }
            r7 = -1
            int r8 = r5.hashCode()     // Catch:{ Exception -> 0x00ba }
            r9 = 64897(0xfd81, float:9.094E-41)
            if (r8 == r9) goto L_0x0076
            r9 = 69104(0x10df0, float:9.6835E-41)
            if (r8 == r9) goto L_0x006c
            r9 = 72638(0x11bbe, float:1.01788E-40)
            if (r8 == r9) goto L_0x0062
            r9 = 2402104(0x24a738, float:3.366065E-39)
            if (r8 == r9) goto L_0x0058
            goto L_0x0080
        L_0x0058:
            java.lang.String r8 = "NONE"
            boolean r5 = r5.equals(r8)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0080
            r5 = 1
            goto L_0x0081
        L_0x0062:
            java.lang.String r6 = "INC"
            boolean r5 = r5.equals(r6)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0080
            r5 = 2
            goto L_0x0081
        L_0x006c:
            java.lang.String r6 = "EXC"
            boolean r5 = r5.equals(r6)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0080
            r5 = 3
            goto L_0x0081
        L_0x0076:
            java.lang.String r6 = "ALL"
            boolean r5 = r5.equals(r6)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0080
            r5 = 0
            goto L_0x0081
        L_0x0080:
            r5 = -1
        L_0x0081:
            switch(r5) {
                case 0: goto L_0x0094;
                case 1: goto L_0x008f;
                case 2: goto L_0x008a;
                case 3: goto L_0x0085;
                default: goto L_0x0084;
            }     // Catch:{ Exception -> 0x00ba }
        L_0x0084:
            goto L_0x00d8
        L_0x0085:
            java.lang.String r5 = "EXC"
            r12.cacheKeyType = r5     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x008a:
            java.lang.String r5 = "INC"
            r12.cacheKeyType = r5     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x008f:
            java.lang.String r5 = "NONE"
            r12.cacheKeyType = r5     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x0094:
            java.lang.String r5 = "ALL"
            r12.cacheKeyType = r5     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x0099:
            java.lang.String r5 = "ks="
            boolean r5 = r4.contains(r5)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x00b7
            java.lang.String r5 = "ks="
            int r5 = r5.length()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r5 = r4.substring(r5)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r6 = "\\|"
            java.lang.String[] r5 = r5.split(r6)     // Catch:{ Exception -> 0x00ba }
            java.util.List r5 = java.util.Arrays.asList(r5)     // Catch:{ Exception -> 0x00ba }
            r12.cacheKeyItems = r5     // Catch:{ Exception -> 0x00ba }
        L_0x00b7:
            r12.cacheControlHeader = r11     // Catch:{ Exception -> 0x00ba }
            goto L_0x00d8
        L_0x00ba:
            java.lang.String r5 = "mtopsdk.AppConfigManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "[parseCacheControlHeader] parse item in CacheControlHeader error.item ="
            r6.append(r7)
            r6.append(r4)
            java.lang.String r4 = ",CacheControlHeader="
            r6.append(r4)
            r6.append(r11)
            java.lang.String r4 = r6.toString()
            mtopsdk.common.util.TBSdkLog.w(r5, r4)
        L_0x00d8:
            int r3 = r3 + 1
            goto L_0x000f
        L_0x00dc:
            return
        L_0x00dd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.config.AppConfigManager.parseCacheControlHeader(java.lang.String, mtopsdk.mtop.cache.domain.ApiCacheDo):void");
    }

    public boolean parseAppConfig(@NonNull String str, String str2) {
        JSONArray optJSONArray;
        JSONArray optJSONArray2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject optJSONObject = jSONObject.optJSONObject("clientCache");
            if (optJSONObject == null || (optJSONArray = optJSONObject.optJSONArray("clientCacheAppConfList")) == null) {
                return false;
            }
            for (int length = optJSONArray.length() - 1; length >= 0; length--) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(length);
                if (optJSONObject2 != null) {
                    String optString = optJSONObject2.optString("api");
                    String optString2 = optJSONObject2.optString("v");
                    String optString3 = optJSONObject2.optString("block");
                    String concatStr2LowerCase = StringUtils.concatStr2LowerCase(optString, optString2);
                    ApiCacheDo apiCacheDoByKey = getInstance().getApiCacheDoByKey(concatStr2LowerCase);
                    if (apiCacheDoByKey != null) {
                        apiCacheDoByKey.blockName = optString3;
                    } else {
                        getInstance().addApiCacheDoToGroup(concatStr2LowerCase, new ApiCacheDo(optString, optString2, optString3));
                    }
                }
            }
            JSONObject optJSONObject3 = jSONObject.optJSONObject("unit");
            if (!(optJSONObject3 == null || (optJSONArray2 = optJSONObject3.optJSONArray("tradeUnitApiList")) == null)) {
                HashSet hashSet = new HashSet();
                for (int length2 = optJSONArray2.length() - 1; length2 >= 0; length2--) {
                    JSONObject optJSONObject4 = optJSONArray2.optJSONObject(length2);
                    if (optJSONObject4 != null) {
                        hashSet.add(StringUtils.concatStr2LowerCase(optJSONObject4.optString("api"), optJSONObject4.optString("v")));
                    }
                }
                this.tradeUnitApiSet = hashSet;
            }
            return true;
        } catch (Exception e) {
            TBSdkLog.e(TAG, str2, "[parseAppConfig]parse appConf node error.", e);
            return false;
        }
    }

    public void reloadAppConfig(MtopConfig mtopConfig) {
        if (mtopConfig != null) {
            try {
                File file = new File(mtopConfig.context.getExternalFilesDir((String) null).getAbsoluteFile() + FILE_DIR_MTOP);
                AppConfigDo appConfigDo = (AppConfigDo) MtopUtils.readObject(file, FILE_NAME_APP_CONFIG);
                if (appConfigDo != null && StringUtils.isNotBlank(appConfigDo.appConf) && appConfigDo.appConfigVersion > mtopConfig.xAppConfigVersion) {
                    synchronized (mtopConfig.lock) {
                        if (appConfigDo.appConfigVersion > mtopConfig.xAppConfigVersion && getInstance().parseAppConfig(appConfigDo.appConf, "")) {
                            mtopConfig.xAppConfigVersion = appConfigDo.appConfigVersion;
                            TBSdkLog.i(TAG, "[reloadAppConfig] reload appConf succeed. appConfVersion=" + mtopConfig.xAppConfigVersion);
                        }
                    }
                }
                Map map = (Map) MtopUtils.readObject(file, FILE_NAME_API_CACHE_CONFIG);
                if (map != null) {
                    for (Map.Entry entry : map.entrySet()) {
                        String str = (String) entry.getKey();
                        ApiCacheDo apiCacheDo = (ApiCacheDo) entry.getValue();
                        ApiCacheDo apiCacheDo2 = this.apiCacheGroup.get(str);
                        if (apiCacheDo2 == null) {
                            this.apiCacheGroup.put(str, apiCacheDo);
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(TAG, "[reloadAppConfig] add apiCacheDo config,apiKey=" + str);
                            }
                        } else if (!apiCacheDo2.equals(apiCacheDo)) {
                            apiCacheDo2.cacheControlHeader = apiCacheDo.cacheControlHeader;
                            apiCacheDo2.privateScope = apiCacheDo.privateScope;
                            apiCacheDo2.offline = apiCacheDo.offline;
                            apiCacheDo2.cacheKeyType = apiCacheDo.cacheKeyType;
                            apiCacheDo2.cacheKeyItems = apiCacheDo.cacheKeyItems;
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                TBSdkLog.i(TAG, "[reloadAppConfig] update apiCacheDo config,apiKey=" + str);
                            }
                        }
                    }
                }
            } catch (Exception unused) {
                TBSdkLog.e(TAG, "[reloadAppConfig] reload appConf file error.");
            }
        }
    }

    public void storeApiCacheDoMap(final Context context, final String str) {
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            public void run() {
                try {
                    MtopUtils.writeObject(AppConfigManager.this.apiCacheGroup, new File(context.getExternalFilesDir((String) null).getAbsoluteFile() + AppConfigManager.FILE_DIR_MTOP), AppConfigManager.FILE_NAME_API_CACHE_CONFIG);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(AppConfigManager.TAG, str, "[storeApiCacheDoMap] save apiCacheConf succeed.");
                    }
                } catch (Exception e) {
                    TBSdkLog.e(AppConfigManager.TAG, str, "[storeApiCacheDoMap] save apiCacheConf error.", e);
                }
            }
        });
    }
}
