package com.ali.user.mobile.app.dataprovider;

import android.content.Context;
import com.ali.user.mobile.app.constant.SiteDescription;
import com.ali.user.mobile.app.report.info.Location;
import com.ali.user.mobile.model.RegionInfo;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public interface IDataProvider {
    boolean enableAlipaySSO();

    boolean enableMobilePwdLogin();

    boolean enableNumAuthService();

    boolean enableRegEmailCheck();

    boolean enableRegPwdCheck();

    String getAccountBindBizType();

    Map<String, String> getAdditionalHeaders();

    String getAlipaySsoDesKey();

    boolean getAppInfoFromServer();

    String getAppName();

    String getAppkey();

    Context getContext();

    Locale getCurrentLanguage();

    RegionInfo getCurrentRegion();

    String getDailyDomain();

    String getDeviceId();

    int getEnvType();

    String getGuideAppName();

    String getGuideBackground();

    String getGuideCloseResource();

    String getGuidePwdLoginResource();

    ImageLoader getImageLoader();

    String getImei();

    String getImsi();

    Location getLocation();

    String getLoginPriority();

    int getMaxHistoryAccount();

    int getMaxSessionSize();

    String getOceanAppkey();

    String getOnlineDomain();

    int getOrientation();

    String getPreDomain();

    String getProductVersion();

    String getQrCodeUrl();

    String getRegFrom();

    String getRegType();

    String getResultActivityPath();

    int getSite();

    List<SiteDescription> getSupportedSites();

    String getTID();

    String getTTID();

    ThreadPoolExecutor getThreadPoolExecutor();

    boolean isAccountChangeDegrade();

    boolean isAccountProfileExist();

    boolean isAppDebug();

    boolean isEnableVoiceMsg();

    boolean isForbidLoginFromBackground();

    boolean isForbiddenRefreshCookieInAutoLogin();

    boolean isLoginInRegModule();

    boolean isNeedAlipaySsoGuide();

    boolean isNeedPwdGuide();

    boolean isNeedTaobaoSsoGuide();

    boolean isNeedWindVaneInit();

    boolean isRefreshCookiesDegrade();

    boolean isSaveHistoryWithoutSalt();

    boolean isShowFamilyAccountTip();

    boolean isShowHistoryFragment();

    boolean isSmsLoginPriority();

    boolean isTaobaoApp();

    boolean needAccsLogin();

    boolean needEnterPriseRegister();

    void setAccountBindBizType(String str);

    void setAlipaySSOEnable(boolean z);

    void setAlipaySsoDesKey(String str);

    void setAppDebug(boolean z);

    void setAppInfoFromServer(boolean z);

    void setAppName(String str);

    void setAppkey(String str);

    void setContext(Context context);

    void setDeviceId(String str);

    void setEnableVoiceMsg(boolean z);

    void setEnvType(int i);

    void setGuideAppName(String str);

    void setGuideBackground(String str);

    void setGuideCloseResource(String str);

    void setGuildePwdLoginResource(String str);

    void setImageLoader(ImageLoader imageLoader);

    void setImei(String str);

    void setImsi(String str);

    void setIsTaobaoApp(boolean z);

    void setLanguage(Locale locale);

    void setLocation(Location location);

    void setLoginPriority(String str);

    void setMaxHistoryAccount(int i);

    void setMaxSessionSize(int i);

    void setNeedAlipaySsoGuide(boolean z);

    void setNeedPwdGuide(boolean z);

    void setNeedTaobaoSsoGuide(boolean z);

    void setNeedWindVaneInit(boolean z);

    void setProductVersion(String str);

    void setQrCodeUrl(String str);

    void setRegFrom(String str);

    void setRegType(String str);

    void setResultActivityPath(String str);

    void setSaveHistoryWithoutSalt(boolean z);

    void setSite(int i);

    void setSupportedSites(List<SiteDescription> list);

    void setTID(String str);

    void setTTID(String str);

    void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor);

    void setUseSeparateThreadPool(boolean z);

    boolean showHeadCountry();

    boolean supportFaceLogin();

    boolean supportMobileLogin();

    boolean supportPwdLogin();

    boolean useRegionFragment();

    boolean useSeparateThreadPool();
}
