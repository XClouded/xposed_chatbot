package com.ali.user.mobile.app.dataprovider;

import android.content.Context;
import android.text.TextUtils;
import com.ali.user.mobile.app.constant.LoginPriority;
import com.ali.user.mobile.app.constant.SiteDescription;
import com.ali.user.mobile.app.report.info.Location;
import com.ali.user.mobile.config.AliuserGlobals;
import com.ali.user.mobile.model.CountryData;
import com.ali.user.mobile.model.RegType;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class DataProvider implements IDataProvider {
    protected String TID;
    protected String TTID;
    protected String alipaySsoDesKey;
    protected String appKey;
    protected String appName;
    protected Context context;
    protected String deviceId;
    protected boolean enableAlipaySSO = true;
    protected boolean enableAuthService = false;
    protected boolean enableMobilePwdLogin = false;
    protected boolean enableVoiceMsg = false;
    protected int envType = 3;
    protected boolean forbidRefreshCookieInAutologin;
    protected boolean forceShowPwdInAlert = false;
    protected String guideAppName;
    protected String guideBackground;
    protected String guideCloseResource;
    protected String guidePwdLoginResource;
    protected String imei;
    protected String imsi;
    protected boolean isAppDebug = false;
    protected boolean isForbidLoginFromBackground = false;
    protected boolean isTaobaoApp = false;
    protected Locale language = Locale.SIMPLIFIED_CHINESE;
    protected Location location;
    protected String loginPriority = LoginPriority.FACE_SMS_PWD.getPriority();
    private String mAccountBindBizType;
    private CountryData mDefaultCountry;
    protected boolean mGetAppInfoFromServer = false;
    protected ImageLoader mImageLoader;
    protected String mResultActivityPath;
    protected int maxHistoryAccount = 3;
    protected int maxSessionSize = 20;
    protected boolean needAccsLogin = false;
    protected boolean needAlipaySsoGuide = false;
    protected boolean needEnterPriseRegister = true;
    protected boolean needPwdGuide = true;
    protected boolean needTaobaoSsoGuide = false;
    protected boolean needWindVaneInit = false;
    protected int orientation = 1;
    protected String productVersion;
    protected String qrCodeUrl = "https://login.taobao.com/member/qrcode.htm?from=pcsdk&qrversion=1002";
    protected boolean refreshCookieDegrade;
    protected boolean regEmailCheck = false;
    protected String regFrom = "TB";
    protected boolean regPwdCheck = false;
    protected String regType = RegType.NATIVE_REG;
    protected boolean saveHistoryWithoutSalt = false;
    protected boolean showHeadCountry = true;
    protected boolean showHistoryFragment = true;
    protected int site = 0;
    protected boolean supportFaceLogin = false;
    protected boolean supportMobileLogin = true;
    protected boolean supportPwdLogin = true;
    protected List<SiteDescription> supportedSites = new ArrayList();
    protected ThreadPoolExecutor threadPool;
    protected boolean useRegionFragment = false;
    protected boolean useSeparateThreadPool = false;
    protected String version;

    public Map<String, String> getAdditionalHeaders() {
        return null;
    }

    public String getDailyDomain() {
        return "";
    }

    public CountryData getDefaultCountry() {
        return null;
    }

    public String getOceanAppkey() {
        return null;
    }

    public String getOnlineDomain() {
        return "";
    }

    public String getPreDomain() {
        return "";
    }

    public boolean isAccountChangeDegrade() {
        return false;
    }

    public boolean isAccountProfileExist() {
        return true;
    }

    public boolean isLoginInRegModule() {
        return false;
    }

    public boolean isShowFamilyAccountTip() {
        return false;
    }

    public boolean isSmsLoginPriority() {
        return false;
    }

    public String getRegType() {
        return this.regType;
    }

    public void setRegType(String str) {
        this.regType = str;
    }

    public void setResultActivityPath(String str) {
        this.mResultActivityPath = str;
    }

    public String getResultActivityPath() {
        if (TextUtils.isEmpty(this.mResultActivityPath)) {
            try {
                this.mResultActivityPath = DataProviderFactory.getApplicationContext().getPackageName() + ".ResultActivity";
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return this.mResultActivityPath;
    }

    public boolean enableNumAuthService() {
        return this.enableAuthService;
    }

    public boolean isEnableVoiceMsg() {
        return this.enableVoiceMsg;
    }

    public void setEnableVoiceMsg(boolean z) {
        this.enableVoiceMsg = z;
    }

    public boolean enableRegPwdCheck() {
        return this.regPwdCheck;
    }

    public void setRegPwdCheck(boolean z) {
        this.regPwdCheck = z;
    }

    public boolean enableRegEmailCheck() {
        return this.regEmailCheck;
    }

    public void setRegEmailCheck(boolean z) {
        this.regEmailCheck = z;
    }

    public String getLoginPriority() {
        return this.loginPriority;
    }

    public void setLoginPriority(String str) {
        this.loginPriority = str;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }

    public String getQrCodeUrl() {
        return this.qrCodeUrl;
    }

    public void setQrCodeUrl(String str) {
        this.qrCodeUrl = str;
    }

    public boolean showHeadCountry() {
        return this.showHeadCountry;
    }

    public void setShowHeadCountry(boolean z) {
        this.showHeadCountry = z;
    }

    public void setEnableMobilePwdLogin(boolean z) {
        this.enableMobilePwdLogin = z;
    }

    public boolean enableMobilePwdLogin() {
        return this.enableMobilePwdLogin;
    }

    public boolean useRegionFragment() {
        return this.useRegionFragment;
    }

    public void setUseRegionFragment(boolean z) {
        this.useRegionFragment = z;
    }

    public void setForceShowPwdInAlert(boolean z) {
        this.forceShowPwdInAlert = z;
    }

    public boolean isShowHistoryFragment() {
        return this.showHistoryFragment;
    }

    public void setShowHistoryFragment(boolean z) {
        this.showHistoryFragment = z;
    }

    public int getMaxHistoryAccount() {
        return this.maxHistoryAccount;
    }

    public void setMaxHistoryAccount(int i) {
        this.maxHistoryAccount = i;
    }

    public int getMaxSessionSize() {
        return this.maxSessionSize;
    }

    public void setMaxSessionSize(int i) {
        this.maxSessionSize = i;
    }

    public boolean isSaveHistoryWithoutSalt() {
        return this.saveHistoryWithoutSalt;
    }

    public void setSaveHistoryWithoutSalt(boolean z) {
        this.saveHistoryWithoutSalt = z;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public boolean isForbidLoginFromBackground() {
        return this.isForbidLoginFromBackground;
    }

    public boolean needAccsLogin() {
        return this.needAccsLogin;
    }

    public void setAlipaySSOEnable(boolean z) {
        this.enableAlipaySSO = z;
    }

    public boolean isNeedWindVaneInit() {
        return this.needWindVaneInit;
    }

    public void setNeedWindVaneInit(boolean z) {
        this.needWindVaneInit = z;
    }

    public boolean isAppDebug() {
        return this.isAppDebug;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public String getAppkey() {
        if (TextUtils.isEmpty(this.appKey)) {
            switch (getEnvType()) {
                case 0:
                case 1:
                    this.appKey = ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(2);
                    break;
                default:
                    this.appKey = ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0);
                    break;
            }
        }
        return this.appKey;
    }

    public void setAppkey(String str) {
        this.appKey = str;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public boolean isTaobaoApp() {
        return this.isTaobaoApp;
    }

    public void setIsTaobaoApp(boolean z) {
        this.isTaobaoApp = z;
    }

    public String getProductVersion() {
        return this.productVersion;
    }

    public void setProductVersion(String str) {
        this.productVersion = str;
    }

    public String getTTID() {
        return this.TTID;
    }

    public void setTTID(String str) {
        this.TTID = str;
    }

    public String getTID() {
        return this.TID;
    }

    public void setTID(String str) {
        this.TID = str;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String str) {
        this.imei = str;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String str) {
        this.imsi = str;
    }

    public int getEnvType() {
        return this.envType;
    }

    public void setEnvType(int i) {
        this.envType = i;
    }

    public void setAppDebug(boolean z) {
        this.isAppDebug = z;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location2) {
        this.location = location2;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPool;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    public void setSite(int i) {
        this.site = i;
        if (i == 17) {
            AliuserGlobals.isGUCSDK = true;
            AliuserGlobals.isOceanSDK = false;
        } else if (i == 4) {
            AliuserGlobals.isGUCSDK = false;
            AliuserGlobals.isOceanSDK = true;
        } else {
            AliuserGlobals.isOceanSDK = false;
            AliuserGlobals.isGUCSDK = false;
        }
    }

    public int getSite() {
        return this.site;
    }

    public List<SiteDescription> getSupportedSites() {
        return this.supportedSites;
    }

    public void setSupportedSites(List<SiteDescription> list) {
        this.supportedSites = list;
    }

    public void setRegFrom(String str) {
        this.regFrom = str;
    }

    public String getRegFrom() {
        return this.regFrom;
    }

    public boolean enableAlipaySSO() {
        return this.enableAlipaySSO;
    }

    public boolean useSeparateThreadPool() {
        return this.useSeparateThreadPool;
    }

    public void setUseSeparateThreadPool(boolean z) {
        this.useSeparateThreadPool = z;
    }

    public boolean isRefreshCookiesDegrade() {
        return this.refreshCookieDegrade;
    }

    public boolean isForbiddenRefreshCookieInAutoLogin() {
        return this.forbidRefreshCookieInAutologin;
    }

    public boolean needEnterPriseRegister() {
        return this.needEnterPriseRegister;
    }

    public String getAlipaySsoDesKey() {
        return this.alipaySsoDesKey;
    }

    public void setAlipaySsoDesKey(String str) {
        this.alipaySsoDesKey = str;
    }

    public boolean isNeedAlipaySsoGuide() {
        return this.needAlipaySsoGuide;
    }

    public void setNeedAlipaySsoGuide(boolean z) {
        this.needAlipaySsoGuide = z;
    }

    public boolean isNeedTaobaoSsoGuide() {
        return this.needTaobaoSsoGuide;
    }

    public void setNeedTaobaoSsoGuide(boolean z) {
        this.needTaobaoSsoGuide = z;
    }

    public boolean isNeedPwdGuide() {
        return this.needPwdGuide;
    }

    public void setNeedPwdGuide(boolean z) {
        this.needPwdGuide = z;
    }

    public String getGuideAppName() {
        return this.guideAppName;
    }

    public void setGuideAppName(String str) {
        this.guideAppName = str;
    }

    public String getGuideBackground() {
        return this.guideBackground;
    }

    public void setGuideBackground(String str) {
        this.guideBackground = str;
    }

    public String getGuidePwdLoginResource() {
        return this.guidePwdLoginResource;
    }

    public void setGuildePwdLoginResource(String str) {
        this.guidePwdLoginResource = str;
    }

    public String getGuideCloseResource() {
        return this.guideCloseResource;
    }

    public void setGuideCloseResource(String str) {
        this.guideCloseResource = str;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public Locale getCurrentLanguage() {
        return this.language;
    }

    public void setLanguage(Locale locale) {
        this.language = locale;
    }

    public RegionInfo getCurrentRegion() {
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.name = "中国大陆";
        regionInfo.code = "+86";
        regionInfo.domain = "CN";
        regionInfo.checkPattern = "^(86){0,1}1\\d{10}$";
        return regionInfo;
    }

    public boolean supportFaceLogin() {
        return this.supportFaceLogin;
    }

    public boolean supportMobileLogin() {
        return this.supportMobileLogin;
    }

    public boolean supportPwdLogin() {
        return this.supportPwdLogin;
    }

    public void setAccountBindBizType(String str) {
        this.mAccountBindBizType = str;
    }

    public String getAccountBindBizType() {
        return this.mAccountBindBizType;
    }

    public boolean getAppInfoFromServer() {
        return this.mGetAppInfoFromServer;
    }

    public void setAppInfoFromServer(boolean z) {
        this.mGetAppInfoFromServer = z;
    }
}
