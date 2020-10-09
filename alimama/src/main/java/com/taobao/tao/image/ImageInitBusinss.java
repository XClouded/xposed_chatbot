package com.taobao.tao.image;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.util.OssImageUrlStrategy;
import com.taobao.tao.util.TaobaoImageUrlStrategy;
import java.util.HashMap;

public class ImageInitBusinss {
    public static final String ALI_CDN_DOMAIN = "aliCdnDomain";
    public static final String CDN_10000_HEIGHT = "cdn10000Height";
    public static final String CDN_10000_WIDTH = "cdn10000Width";
    public static final String CDN_IMAGE_SIZE = "cdnImageSizes";
    public static final String CONVERT_EXCLUDE_PATH = "domainConvertExcludePath";
    public static final String DOMAIN_DEST = "domainDest";
    public static final String DOMAIN_SWITCH = "domainSwitch";
    public static final String EXACT_EXCLUDE_DOMAIN = "exactExcludeDomain";
    public static final String FUZZY_EXCLUDE_PATH = "fuzzyExcludePath";
    public static final String GLOBAL_SWITCH = "globalSwitch";
    public static final String HEIF_BUSINESS_WHITE_LIST = "heifBizWhiteList";
    public static final String HEIF_IMAGE_DOMAIN = "heifImageDomain";
    public static final String IMAGE_CONFIG = "android_image_strategy_config";
    public static final String IMAGE_STRATEGY = "image_strategy";
    public static final String LEVEL_MODEL_SIZES = "levelModelImageSizes";
    public static final String LEVEL_MODEL_XZSIZES = "levelModelXZImageSizes";
    public static final String LEVEL_RATIO = "levelRatio";
    public static final String MODULES = "modules";
    public static final String OSS_CDN_DOMAIN = "ossCdnDomain";
    public static final String OSS_FUZZY_EXCLUDE = "ossFuzzyExclude";
    public static final String SPECIAL_IMAGE_DOMAIN = "specialImageDomain";
    public static final String STRICT_ALI_CDN_DOMAIN = "strictCDNDomainWL";
    public static final String STRICT_CONVERT_EXCLUDE_PATH = "strictDomainConvertBL";
    public static final String STRICT_EXACT_EXCLUDE_DOMAIN = "strictExactDomainBL";
    public static final String TTL_MAX_TIME = "maxTTLTime";
    public static final String XZ_CDN_IMAGE_SIZE = "xzcdnImageSizes";
    private static HashMap<String, String> defaultConfig = new HashMap<>();
    private static TTLStrategyConfigListener mNotifyListener;
    private static ImageInitBusinss sInstance = null;
    private IImageExtendedSupport mImageExtendedSupport;
    private IImageStrategySupport mStrategySupport;

    static {
        defaultConfig.put(GLOBAL_SWITCH, "1");
        defaultConfig.put(DOMAIN_SWITCH, "1");
        defaultConfig.put(MODULES, "default,search,detail,shop,weitao,weapp,weappsharpen,bala,home,tbchannel");
        defaultConfig.put("default", "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q75\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put("search", "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put("detail", "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.SHOP, "{ \"highNetQ\": \"q75\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"s150\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.WEITAO, "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q75\", \"highNetSharpen\": \"s110\", \"lowNetSharpen\": \"s110\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.WEAPP, "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.WEAPPSHARPEN, "{ \"highNetQ\": \"q75\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"s150\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.BALA, "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.HOME, "{ \"highNetQ\": \"q90\", \"lowNetQ\": \"q50\", \"highNetSharpen\": \"\", \"lowNetSharpen\": \"\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
        defaultConfig.put(ImageStrategyConfig.TBCHANNEL, "{ \"highNetQ\": \"q50\", \"lowNetQ\": \"q30\", \"highNetSharpen\": \"s150\", \"lowNetSharpen\": \"s150\", \"highNetScale\": \"1\", \"lowNetScale\": \"1\", \"useWebP\": 1 }");
    }

    public static ImageInitBusinss getInstance() {
        return sInstance;
    }

    public static ImageInitBusinss newInstance(Application application, IImageStrategySupport iImageStrategySupport) {
        if (sInstance == null) {
            sInstance = new ImageInitBusinss(application, iImageStrategySupport);
        }
        return sInstance;
    }

    public static void setTTLNotifyListener(TTLStrategyConfigListener tTLStrategyConfigListener) {
        mNotifyListener = tTLStrategyConfigListener;
    }

    public static void setMinLogLevel(int i) {
        Logger.setMinLogLevel(i);
    }

    public ImageInitBusinss(Application application, IImageStrategySupport iImageStrategySupport) {
        this.mStrategySupport = iImageStrategySupport;
        TaobaoImageUrlStrategy.getInstance().initDip(application);
        Logger.i(Logger.COMMON_TAG, "construct ImageInitBusinss with IImageStrategySupport(webp support:%b)", Boolean.valueOf(this.mStrategySupport.isSupportWebP()));
    }

    public void setImageExtendedSupport(IImageExtendedSupport iImageExtendedSupport) {
        this.mImageExtendedSupport = iImageExtendedSupport;
    }

    public IImageExtendedSupport getImageExtendedSupport() {
        return this.mImageExtendedSupport;
    }

    public IImageStrategySupport getStrategySupport() {
        return this.mStrategySupport;
    }

    @SuppressLint({"NewApi"})
    public synchronized void notifyConfigsChange() {
        synchronized (this) {
            String configString = this.mStrategySupport.getConfigString(IMAGE_CONFIG, CDN_IMAGE_SIZE, "");
            String configString2 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, CDN_10000_WIDTH, "");
            String configString3 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, CDN_10000_HEIGHT, "");
            String configString4 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, XZ_CDN_IMAGE_SIZE, "");
            String configString5 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, LEVEL_MODEL_SIZES, "");
            String configString6 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, LEVEL_MODEL_XZSIZES, "");
            String configString7 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, DOMAIN_DEST, "");
            String configString8 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, ALI_CDN_DOMAIN, "");
            String configString9 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, OSS_CDN_DOMAIN, "");
            String configString10 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, EXACT_EXCLUDE_DOMAIN, "");
            String configString11 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, FUZZY_EXCLUDE_PATH, "");
            String configString12 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, OSS_FUZZY_EXCLUDE, "");
            String configString13 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, CONVERT_EXCLUDE_PATH, "");
            String str = configString9;
            String configString14 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, LEVEL_RATIO, "");
            String str2 = configString7;
            String configString15 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, DOMAIN_SWITCH, defaultConfig.get(DOMAIN_SWITCH));
            String configString16 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, GLOBAL_SWITCH, defaultConfig.get(GLOBAL_SWITCH));
            String configString17 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, HEIF_IMAGE_DOMAIN, "");
            String configString18 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, HEIF_BUSINESS_WHITE_LIST, "");
            String str3 = configString16;
            String str4 = configString11;
            String configString19 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, MODULES, defaultConfig.get(MODULES));
            String configString20 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, SPECIAL_IMAGE_DOMAIN, "");
            String str5 = configString10;
            String configString21 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, TTL_MAX_TIME, "");
            String str6 = configString8;
            String configString22 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, STRICT_ALI_CDN_DOMAIN, "");
            String configString23 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, STRICT_EXACT_EXCLUDE_DOMAIN, "");
            String configString24 = this.mStrategySupport.getConfigString(IMAGE_CONFIG, STRICT_CONVERT_EXCLUDE_PATH, "");
            String[] string2StrArray = string2StrArray(configString19);
            if (mNotifyListener != null) {
                mNotifyListener.notifyTTLConfigUpdate(configString20, configString21);
            }
            TaobaoImageUrlStrategy instance = TaobaoImageUrlStrategy.getInstance();
            int[] string2IntArray = string2IntArray(configString);
            int[] string2IntArray2 = string2IntArray(configString2);
            int[] string2IntArray3 = string2IntArray(configString3);
            int[] string2IntArray4 = string2IntArray(configString4);
            int[] string2IntArray5 = string2IntArray(configString5);
            int[] string2IntArray6 = string2IntArray(configString6);
            HashMap<String, TaobaoImageUrlStrategy.ServiceImageSwitch> ary2ServiceImageSwitchArr = ary2ServiceImageSwitchArr(string2StrArray);
            int[] string2IntArray7 = string2IntArray(configString18);
            String[] string2StrArray2 = string2StrArray(configString13);
            String str7 = str6;
            String[] string2StrArray3 = string2StrArray(str7);
            String str8 = configString19;
            String str9 = str5;
            String[] string2StrArray4 = string2StrArray(str9);
            String str10 = configString20;
            String str11 = str4;
            String[] string2StrArray5 = string2StrArray(str11);
            String str12 = str9;
            String str13 = str3;
            boolean str2Boolean = str2Boolean(str13);
            String str14 = str13;
            String str15 = configString15;
            String str16 = str7;
            TaobaoImageUrlStrategy taobaoImageUrlStrategy = instance;
            String str17 = configString12;
            String str18 = configString13;
            String str19 = str11;
            int[] iArr = string2IntArray4;
            int[] iArr2 = string2IntArray5;
            int[] iArr3 = string2IntArray6;
            HashMap<String, TaobaoImageUrlStrategy.ServiceImageSwitch> hashMap = ary2ServiceImageSwitchArr;
            String str20 = str2;
            String str21 = configString17;
            taobaoImageUrlStrategy.initImageUrlStrategy(string2IntArray, string2IntArray2, string2IntArray3, iArr, iArr2, iArr3, hashMap, str20, str21, str10, string2IntArray7, string2StrArray2, string2StrArray3, string2StrArray4, string2StrArray5, str2Boolean, str2Boolean(str15), configString14, true);
            String str22 = str;
            OssImageUrlStrategy.getInstance().setupConfigs(string2StrArray(str22), string2StrArray(str17));
            String str23 = configString22;
            TaobaoImageUrlStrategy.getInstance().updateStrictCDNDomainWhiteList(string2StrArray(str23));
            String str24 = configString23;
            TaobaoImageUrlStrategy.getInstance().updateStrictCDNDomainBlackList(string2StrArray(str24));
            TaobaoImageUrlStrategy.getInstance().updateStrictConvergenceBlackList(string2StrArray(configString24));
            Logger.i(Logger.COMMON_TAG, "orange notify(%s) update\ncdnImageSize:%s\ncdn10000Width:%s\ncdn10000Height:%s\nxzCdnSize:%s\nlevelModelSizes:%s\nlevelModelXzSizes:%s\ndomainDest:%s\nheifDomain:%s\nheifBizWL:%s\ndomainSwitch:%s\nglobalSwitch:%s\naliCdnDomain:%s\nexactExcludePath:%s\nfuzzyExcludePath:%s\nconvertExcludePath:%s\nmodules:%s\nlevelRatio:%s\nossCdnDomains:%s\nossFuzzyExcludes:%s\nstrictCDNDomainWL:%s\nstrictExactDomainBL:%s\nstrictDomainConvertBL:%s", IMAGE_CONFIG, configString, configString2, configString3, configString4, configString5, configString6, str2, configString17, configString18, str15, str14, str16, str12, str19, str18, str8, configString14, str22, str17, str23, str24, configString24);
        }
    }

    private HashMap<String, TaobaoImageUrlStrategy.ServiceImageSwitch> ary2ServiceImageSwitchArr(String[] strArr) {
        JSONObject jSONObject;
        HashMap<String, TaobaoImageUrlStrategy.ServiceImageSwitch> hashMap = new HashMap<>();
        for (String str : strArr) {
            String configString = this.mStrategySupport.getConfigString(IMAGE_CONFIG, str, defaultConfig.get(str));
            if (!TextUtils.isEmpty(configString)) {
                try {
                    jSONObject = JSONObject.parseObject(configString);
                } catch (Exception e) {
                    e.printStackTrace();
                    jSONObject = null;
                }
                if (jSONObject != null) {
                    TaobaoImageUrlStrategy.ServiceImageSwitch serviceImageSwitch = new TaobaoImageUrlStrategy.ServiceImageSwitch();
                    serviceImageSwitch.setAreaName(str);
                    serviceImageSwitch.setUseWebp(str2Boolean(jSONObject.getString("useWebP")));
                    serviceImageSwitch.setHighNetQ(jSONObject.getString("highNetQ"));
                    serviceImageSwitch.setLowNetQ(jSONObject.getString("lowNetQ"));
                    serviceImageSwitch.setHighNetSharpen(jSONObject.getString("highNetSharpen"));
                    serviceImageSwitch.setLowNetSharpen(jSONObject.getString("lowNetSharpen"));
                    serviceImageSwitch.setHighNetScale(str2Double(jSONObject.getString("highNetScale")));
                    serviceImageSwitch.setLowNetScale(str2Double(jSONObject.getString("lowNetScale")));
                    serviceImageSwitch.useCdnSizes(str2Boolean(jSONObject.getString("useCdnSizes")));
                    hashMap.put(str, serviceImageSwitch);
                }
            }
        }
        return hashMap;
    }

    private int[] string2IntArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        int[] iArr = new int[split.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = str2Number(split[i]);
        }
        return iArr;
    }

    private String[] string2StrArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split(",");
    }

    private boolean str2Boolean(String str) {
        return "true".equals(str) || "1".equals(str);
    }

    private int str2Number(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return 0;
        }
    }

    private double str2Double(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception unused) {
            return 1.0d;
        }
    }
}
