package com.alimama.union.app.rxnetwork;

public class ApiInfo {
    public static ApiInfo API_CONFIG_QUERY = new ApiInfo("mtop.alimama.etao.config.query", "1.0");
    public static ApiInfo API_HOME_INDEX = new ApiInfo("mtop.alimama.moon.provider.app.index.get", "1.0", true, true);
    public static ApiInfo API_MESSAGE_COUNT = new ApiInfo("mtop.alimama.union.mc.box.count", "1.0", true, true);
    public static ApiInfo API_MESSAGE_LIST = new ApiInfo("mtop.alimama.union.mc.box.list", "1.0");
    public static ApiInfo API_MESSAGE_READ = new ApiInfo("mtop.alimama.union.mc.box.read", "1.0", true, true);
    public static ApiInfo API_SEARCH_HOT_TAG = new ApiInfo("mtop.alimama.moon.provider.tac.service.call", "1.0", true, true);
    public static ApiInfo CHANGE_MOBILE = new ApiInfo("mtop.alimama.media.account.changeMobile", "1.0", true, true);
    public static ApiInfo FEEDBACK_API = new ApiInfo("com.taobao.client.user.feedback2", "1.0", false, true);
    public static ApiInfo GRADE_DETAIL = new ApiInfo("mtop.alimama.moon.provider.user.gradedetail.get", "1.0");
    public static ApiInfo SCENE_FRAGMENT = new ApiInfo("mtop.alimama.moon.provider.page.content.get", "1.0");
    public static final ApiInfo SEARCH_API = new ApiInfo("mtop.alimama.union.xt.en.api.entry", "2.0", false, true);
    public static ApiInfo SEND_MOBILE_CODE = new ApiInfo("mtop.alimama.media.account.sendMobileCode", "1.0", true, true);
    public static ApiInfo SHARE_INFO = new ApiInfo("mtop.alimama.moon.provider.shareinfo.get", "1.0", true, true);
    public static ApiInfo SHARE_INFO_V2 = new ApiInfo("mtop.alimama.moon.provider.shareinfo.get", "2.0", true, true);
    public static ApiInfo TAO_CODE_GENERATE = new ApiInfo("mtop.taobao.sharepassword.genpassword", "1.0");
    public static ApiInfo TAO_CODE_ITEM_INFO_API = new ApiInfo("mtop.alimama.moon.provider.taotoken.convert.get", "1.0", true, true);
    public static ApiInfo TOOL_FRAGMENT = new ApiInfo("mtop.alimama.moon.provider.page.content.get", "1.0");
    public static ApiInfo WITHDRAW = new ApiInfo("mtop.alimama.media.account.withDraw", "1.0", true, true);
    public static ApiInfo WITHDRAW_CHECK_BALANCE = new ApiInfo("mtop.alimama.media.account.checkBalance", "1.0", true, true);
    public static ApiInfo WITHDRAW_GET_BALANCE = new ApiInfo("mtop.alimama.media.account.getAccountByTaobaoUserId", "1.0", true, true);
    private final String mAPI;
    private final boolean mNeedECode;
    private final boolean mNeedSession;
    private boolean mUseWua;
    private final String mVersion;

    private ApiInfo(String str, String str2, boolean z, boolean z2) {
        this.mAPI = str;
        this.mVersion = str2;
        this.mNeedECode = z;
        this.mNeedSession = z2;
    }

    private ApiInfo(String str, String str2) {
        this(str, str2, false, true);
    }

    public String getAPIName() {
        return this.mAPI;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public boolean needSession() {
        return this.mNeedSession;
    }

    public boolean needECode() {
        return this.mNeedECode;
    }

    public boolean useWua() {
        return this.mUseWua;
    }

    public ApiInfo setUseWua(boolean z) {
        this.mUseWua = z;
        return this;
    }

    public String toString() {
        return "ApiInfo{mVersion='" + this.mVersion + '\'' + ", mAPI='" + this.mAPI + '\'' + '}';
    }
}
