package com.taobao.login4android.session;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.constants.SessionConstants;
import com.taobao.login4android.session.cookies.LoginCookie;
import com.taobao.login4android.session.cookies.LoginCookieUtils;
import com.taobao.login4android.thread.LoginThreadHelper;
import com.taobao.login4android.utils.FileUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.ArrayList;
import java.util.List;

public class SessionManager implements ISession {
    private static final String CLEAR_SESSION_ACTION = "NOTIFY_CLEAR_SESSION";
    public static final String CURRENT_PROCESS = "PROCESS_NAME";
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    private static final String NEW_SESSION_TAG = "newSession";
    public static final String NOTIFY_SESSION_VALID = "NOTIFY_SESSION_VALID";
    private static final String TAG = "loginsdk.LoginSessionManager";
    public static final String USERINFO = "userinfo";
    private static final Object checkLock = new Object();
    private static SessionManager instance;
    /* access modifiers changed from: private */
    public static BroadcastReceiver receiver;
    private static SecurityGuardManager securityGuardManager;
    private String extJson;
    private String mAutoLoginToken;
    /* access modifiers changed from: private */
    public Context mContext;
    private List<LoginCookie> mCookie = new ArrayList();
    private String mEcode;
    private String mEmail;
    private long mHavanaSsoTokenExpiredTime;
    private String mHeadPicLink;
    private int mInjectCookieCount;
    private boolean mIsCommentUsed;
    public String mLoginPhone;
    private int mLoginSite;
    private long mLoginTime;
    private boolean mNewSessionTag = false;
    private String mNick;
    private String mOldEncryptedUserId;
    private String mOldNick;
    private String mOldSid;
    private String mOldUserId;
    private String mSessionDisastergrd;
    private long mSessionExpiredTime;
    private String mShowNick;
    private String mSid;
    private String mSubSid;
    private String mUidDigest;
    private String mUserId;
    private String mUserName;
    /* access modifiers changed from: private */
    public SharedPreferences storage;

    protected SessionManager() {
    }

    private SessionManager(Context context) {
        if (context != null) {
            this.mContext = context;
            new Thread("login-session-init") {
                public void run() {
                    try {
                        boolean unused = SessionManager.DEBUG = (SessionManager.this.mContext.getApplicationInfo().flags & 2) != 0;
                    } catch (Exception unused2) {
                    }
                    BroadcastReceiver unused3 = SessionManager.receiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            String curProcessName = LoginThreadHelper.getCurProcessName(SessionManager.this.mContext);
                            if (intent != null) {
                                try {
                                    if (TextUtils.equals(intent.getAction(), SessionManager.CLEAR_SESSION_ACTION)) {
                                        if (!TextUtils.equals(curProcessName, intent.getStringExtra("PROCESS_NAME"))) {
                                            new Thread("init-session-data") {
                                                public void run() {
                                                    SessionManager.this.clearMemoryData();
                                                    SharedPreferences unused = SessionManager.this.storage = null;
                                                    SessionManager.this.initMemoryData();
                                                }
                                            }.start();
                                        }
                                        LoginStatus.resetLoginFlag();
                                    }
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                        }
                    };
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(SessionManager.CLEAR_SESSION_ACTION);
                    intentFilter.addAction(SessionManager.NOTIFY_SESSION_VALID);
                    try {
                        SessionManager.this.mContext.registerReceiver(SessionManager.receiver, intentFilter);
                    } catch (Exception unused4) {
                    }
                    SessionManager.this.initMemoryData();
                    if (SessionManager.this.checkSessionValid()) {
                        try {
                            Intent intent = new Intent(SessionManager.NOTIFY_SESSION_VALID);
                            intent.putExtra("PROCESS_NAME", LoginThreadHelper.getCurProcessName(SessionManager.this.mContext));
                            intent.setPackage(SessionManager.this.mContext.getPackageName());
                            SessionManager.this.mContext.sendBroadcast(intent);
                            LoginTLogAdapter.i(SessionManager.TAG, "sendBroadcast: NOTIFY_SESSION_VALID");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    public static synchronized SessionManager getInstance(Context context) {
        SessionManager sessionManager;
        synchronized (SessionManager.class) {
            if (instance == null && context != null) {
                instance = new SessionManager(context);
            }
            sessionManager = instance;
        }
        return sessionManager;
    }

    private void initStorage() {
        if (this.mContext != null && this.storage == null) {
            this.storage = this.mContext.getSharedPreferences(USERINFO, 4);
        }
    }

    /* access modifiers changed from: private */
    public void initMemoryData() {
        String sid = getSid();
        getSubSid();
        getEcode();
        getLoginToken();
        getNick();
        getSessionExpiredTime();
        getSsoToken();
        getUserId();
        getUserName();
        isCommentTokenUsed();
        isNewSessionTag();
        getExtJson();
        getLoginSite();
        getUidDigest();
        getOneTimeToken();
        getHavanaSsoTokenExpiredTime();
        doSidCheck(sid);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0044 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doSidCheck(java.lang.String r4) {
        /*
            r3 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0048 }
            if (r0 != 0) goto L_0x004c
            java.lang.String r0 = "%"
            boolean r0 = r4.contains(r0)     // Catch:{ Throwable -> 0x0048 }
            if (r0 == 0) goto L_0x004c
            java.lang.String r0 = "loginsdk.LoginSessionManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0048 }
            r1.<init>()     // Catch:{ Throwable -> 0x0048 }
            java.lang.String r2 = "clear session , cause sid = "
            r1.append(r2)     // Catch:{ Throwable -> 0x0048 }
            r1.append(r4)     // Catch:{ Throwable -> 0x0048 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0048 }
            com.taobao.login4android.log.LoginTLogAdapter.e((java.lang.String) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0048 }
            com.ut.mini.UTHitBuilders$UTCustomHitBuilder r0 = new com.ut.mini.UTHitBuilders$UTCustomHitBuilder     // Catch:{ Throwable -> 0x0044 }
            java.lang.String r1 = "SessionManagerSid"
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0044 }
            java.lang.String r1 = "Page_Extend"
            r0.setEventPage(r1)     // Catch:{ Throwable -> 0x0044 }
            java.lang.String r1 = "sid"
            r0.setProperty(r1, r4)     // Catch:{ Throwable -> 0x0044 }
            com.ut.mini.UTAnalytics r4 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x0044 }
            com.ut.mini.UTTracker r4 = r4.getDefaultTracker()     // Catch:{ Throwable -> 0x0044 }
            java.util.Map r0 = r0.build()     // Catch:{ Throwable -> 0x0044 }
            r4.send(r0)     // Catch:{ Throwable -> 0x0044 }
        L_0x0044:
            r3.clearMemoryData()     // Catch:{ Throwable -> 0x0048 }
            goto L_0x004c
        L_0x0048:
            r4 = move-exception
            r4.printStackTrace()
        L_0x004c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.session.SessionManager.doSidCheck(java.lang.String):void");
    }

    /* access modifiers changed from: private */
    public void clearMemoryData() {
        this.mSid = null;
        this.mSessionExpiredTime = 0;
        this.mEcode = null;
        this.mNick = null;
        this.mUserName = null;
        this.mUserId = null;
        this.mLoginTime = 0;
        this.mHeadPicLink = null;
        this.mOldSid = null;
        this.mOldNick = null;
        this.mOldUserId = null;
        this.mOldEncryptedUserId = null;
        this.extJson = null;
        this.mEmail = null;
        this.mLoginSite = 0;
        this.mUidDigest = null;
        this.mLoginPhone = null;
    }

    public boolean checkSessionValid() {
        return !TextUtils.isEmpty(getSid()) && System.currentTimeMillis() / 1000 < getSessionExpiredTime();
    }

    public boolean checkHavanaExpired() {
        return System.currentTimeMillis() / 1000 > getHavanaSsoTokenExpiredTime();
    }

    public boolean oneTimeTokenExpired() {
        return System.currentTimeMillis() - this.mLoginTime <= 900;
    }

    public String getByKey(String str) {
        initStorage();
        if (this.storage == null || !TextUtils.isEmpty(str)) {
            return "";
        }
        String string = this.storage.getString(str, "");
        if (!TextUtils.isEmpty(string)) {
            return decrypt(string);
        }
        return "";
    }

    public String getSid() {
        initStorage();
        if (TextUtils.isEmpty(this.mSid) && this.storage != null) {
            String string = this.storage.getString("sid", "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            addStorageSidUT(string);
            this.mSid = decrypt(string);
        }
        return this.mSid;
    }

    private void addStorageSidUT(String str) {
        try {
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("SessionManagerEncryptedSid");
            uTCustomHitBuilder.setEventPage("Page_Extend");
            uTCustomHitBuilder.setProperty("encrpytedSid", str);
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        } catch (Throwable unused) {
        }
    }

    public String getSubSid() {
        initStorage();
        if (TextUtils.isEmpty(this.mSubSid) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.SUBSID, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mSubSid = decrypt(string);
        }
        return this.mSubSid;
    }

    public void setSid(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set sid=" + str);
        }
        this.mSid = str;
        saveStorage("sid", encode(str));
        setOldSid(str);
    }

    public void setSubSid(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set sub sid=" + str);
        }
        this.mSubSid = str;
        saveStorage(SessionConstants.SUBSID, encode(str));
    }

    public String getOldSid() {
        initStorage();
        if (TextUtils.isEmpty(this.mOldSid) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.OLDSID, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mOldSid = decrypt(string);
        }
        return this.mOldSid;
    }

    private void setOldSid(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set OldSid=" + str);
        }
        this.mOldSid = str;
        saveStorage(SessionConstants.OLDSID, encode(str));
    }

    public String getEcode() {
        initStorage();
        if (TextUtils.isEmpty(this.mEcode) && this.storage != null) {
            try {
                String string = this.storage.getString("ecode", "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                }
                this.mEcode = decrypt(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mEcode;
    }

    public void setEcode(String str) {
        this.mEcode = str;
        saveStorage("ecode", encode(str));
    }

    public String getNick() {
        initStorage();
        if (TextUtils.isEmpty(this.mNick) && this.storage != null) {
            String string = this.storage.getString("nick", "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mNick = string;
        }
        return this.mNick;
    }

    public void setNick(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set nick=" + str);
        }
        this.mNick = str;
        saveStorage("nick", str);
        if (!TextUtils.isEmpty(str)) {
            setOldNick(str);
        }
    }

    public String getOldNick() {
        initStorage();
        if (TextUtils.isEmpty(this.mOldNick) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.OLDNICK, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mOldNick = string;
        }
        return this.mOldNick;
    }

    private void setOldNick(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set OldNick=" + str);
        }
        this.mOldNick = str;
        saveStorage(SessionConstants.OLDNICK, str);
    }

    public String getUserName() {
        initStorage();
        if (TextUtils.isEmpty(this.mUserName) && this.storage != null) {
            String string = this.storage.getString("username", "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mUserName = string;
        }
        return this.mUserName;
    }

    public void setUserName(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set userName=" + str);
        }
        this.mUserName = str;
        saveStorage("username", str);
    }

    public String getDisplayNick() {
        initStorage();
        if (TextUtils.isEmpty(this.mShowNick) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.SHOW_NICK, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mShowNick = string;
        }
        return this.mShowNick;
    }

    public void setDisplayNick(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set mShowNick=" + str);
        }
        this.mShowNick = str;
        saveStorage(SessionConstants.SHOW_NICK, str);
    }

    public String getUidDigest() {
        initStorage();
        if (TextUtils.isEmpty(this.mUidDigest) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.SESSION_KEY, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mUidDigest = string;
        }
        if (isDebug()) {
            LoginTLogAdapter.v(TAG, "get sessionKey=" + this.mUidDigest);
        }
        return this.mUidDigest;
    }

    public void setUidDigest(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set sessionKey=" + str);
        }
        this.mUidDigest = str;
        saveStorage(SessionConstants.SESSION_KEY, str);
    }

    public String getSessionDisastergrd() {
        initStorage();
        if (TextUtils.isEmpty(this.mSessionDisastergrd) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.SESSION_DISASTERGRD, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mSessionDisastergrd = string;
        }
        if (isDebug()) {
            LoginTLogAdapter.v(TAG, "get sessionDisastergrd=" + this.mSessionDisastergrd);
        }
        return this.mSessionDisastergrd;
    }

    public void setSessionDisastergrd(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set sessionDisastergrd=" + str);
        }
        this.mSessionDisastergrd = str;
        saveStorage(SessionConstants.SESSION_DISASTERGRD, str);
    }

    public String getUserId() {
        initStorage();
        if (TextUtils.isEmpty(this.mUserId) && this.storage != null) {
            String string = this.storage.getString("userId", "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mUserId = decrypt(string);
        }
        try {
            Long.parseLong(this.mUserId);
            return this.mUserId;
        } catch (Throwable unused) {
            clearSessionInfo();
            return null;
        }
    }

    public void setUserId(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set userId=" + str);
        }
        this.mUserId = str;
        saveStorage("userId", encode(str));
        if (!TextUtils.isEmpty(str)) {
            setOldUserId(str);
        }
    }

    public String getOldUserId() {
        initStorage();
        if (TextUtils.isEmpty(this.mOldUserId) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.OLDUSERID, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mOldUserId = decrypt(string);
        }
        if (isDebug()) {
            LoginTLogAdapter.v(TAG, "get old userId=" + this.mOldUserId);
        }
        return this.mOldUserId;
    }

    public void setOldUserId(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "setOldUserId=" + str);
        }
        this.mOldUserId = str;
        saveStorage(SessionConstants.OLDUSERID, encode(str));
    }

    public String getOldEncryptedUserId() {
        initStorage();
        if (TextUtils.isEmpty(this.mOldEncryptedUserId) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.OLD_ENCRYPTED_USERID, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mOldEncryptedUserId = decrypt(string);
        }
        return this.mOldEncryptedUserId;
    }

    public void setOldEncryptedUserId(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "setOldEncryptedUserId=" + str);
        }
        this.mOldEncryptedUserId = str;
        saveStorage(SessionConstants.OLD_ENCRYPTED_USERID, encode(str));
    }

    public String getLoginToken() {
        initStorage();
        if (TextUtils.isEmpty(this.mAutoLoginToken) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.AUTO_LOGIN_STR, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mAutoLoginToken = decrypt(string);
        }
        return this.mAutoLoginToken;
    }

    public void setLoginToken(String str) {
        saveStorage(SessionConstants.AUTO_LOGIN_STR, encode(str));
    }

    public String getSsoToken() {
        initStorage();
        if (this.storage != null) {
            String string = this.storage.getString("ssoToken", "");
            if (!TextUtils.isEmpty(string)) {
                return decrypt(string);
            }
        }
        return null;
    }

    public String getExtJson() {
        initStorage();
        if (TextUtils.isEmpty(this.extJson) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.EXT_JSON, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.extJson = decrypt(string);
        }
        return this.extJson;
    }

    public void setExtJson(String str) {
        this.extJson = str;
        saveStorage(SessionConstants.EXT_JSON, encode(str));
    }

    public void setSsoToken(String str) {
        saveStorage("ssoToken", encode(str));
    }

    public String getEmail() {
        initStorage();
        if (TextUtils.isEmpty(this.mEmail) && this.storage != null) {
            String string = this.storage.getString("email", "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mEmail = decrypt(string);
        }
        return this.mEmail;
    }

    public void setEmail(String str) {
        this.mEmail = str;
        saveStorage("email", encode(str));
    }

    public String getOneTimeToken() {
        initStorage();
        if (this.storage != null && !checkHavanaExpired()) {
            String string = this.storage.getString(SessionConstants.OneTimeTOKEN, "");
            if (!TextUtils.isEmpty(string)) {
                return decrypt(string);
            }
        }
        return null;
    }

    public void setOneTimeToken(String str) {
        saveStorage(SessionConstants.OneTimeTOKEN, encode(str));
    }

    public boolean isCommentTokenUsed() {
        initStorage();
        if (!this.mIsCommentUsed && this.storage != null) {
            this.mIsCommentUsed = this.storage.getBoolean(SessionConstants.COMMENT_TOKEN_USED, false);
        }
        return this.mIsCommentUsed;
    }

    public void setCommentTokenUsed(boolean z) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set commentTokenUsed=" + z);
        }
        this.mIsCommentUsed = z;
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            edit.putBoolean(SessionConstants.COMMENT_TOKEN_USED, z);
            edit.apply();
        }
    }

    public long getSessionExpiredTime() {
        initStorage();
        if (this.mSessionExpiredTime <= 0 && this.storage != null) {
            this.mSessionExpiredTime = this.storage.getLong(SessionConstants.SESSION_EXPIRED_TIME, 0);
        }
        return this.mSessionExpiredTime;
    }

    public void setSessionExpiredTime(long j) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set sessionExpiredTime=" + j);
        }
        this.mSessionExpiredTime = j;
        saveStorage(SessionConstants.SESSION_EXPIRED_TIME, j);
    }

    public long getHavanaSsoTokenExpiredTime() {
        initStorage();
        if (this.mHavanaSsoTokenExpiredTime <= 0 && this.storage != null) {
            this.mHavanaSsoTokenExpiredTime = this.storage.getLong(SessionConstants.HAVANA_SSO_TOKEN_EXPIRE, 0);
        }
        return this.mHavanaSsoTokenExpiredTime;
    }

    public void setHavanaSsoTokenExpiredTime(long j) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set havanaSsoTokenExpiredTime=" + this.mHavanaSsoTokenExpiredTime);
        }
        this.mHavanaSsoTokenExpiredTime = j;
        saveStorage(SessionConstants.HAVANA_SSO_TOKEN_EXPIRE, this.mHavanaSsoTokenExpiredTime);
    }

    public long getLoginTime() {
        initStorage();
        if (this.mLoginTime == 0 && this.storage != null) {
            this.mLoginTime = this.storage.getLong(SessionConstants.LOGIN_TIME, 0);
        }
        return this.mLoginTime;
    }

    public void setLoginTime(long j) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set loginTime=" + j);
        }
        this.mLoginTime = j;
        saveStorage(SessionConstants.LOGIN_TIME, j);
    }

    public int getLoginSite() {
        initStorage();
        this.mLoginSite = this.storage.getInt(SessionConstants.LOGIN_SITE, 0);
        return this.mLoginSite;
    }

    public void setLoginSite(int i) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set LoginSite = " + i);
        }
        this.mLoginSite = i;
        saveStorage(SessionConstants.LOGIN_SITE, i);
    }

    public void setInjectCookieCount(int i) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set session count = " + i);
        }
        this.mInjectCookieCount = i;
        saveStorage(SessionConstants.INJECT_COOKIE_COUNT, i);
    }

    public int getInjectCookieCount() {
        initStorage();
        if (this.mInjectCookieCount == 0 && this.storage != null) {
            this.mInjectCookieCount = this.storage.getInt(SessionConstants.INJECT_COOKIE_COUNT, 0);
        }
        return this.mInjectCookieCount;
    }

    public String getEventTrace() {
        initStorage();
        String string = this.storage != null ? this.storage.getString(SessionConstants.EVENT_TRACE, "") : null;
        return string != null ? string : "";
    }

    public void appendEventTrace(String str) {
        if (!TextUtils.isEmpty(str)) {
            LoginTLogAdapter.v(TAG, "logEventTrace : " + str);
            String str2 = getEventTrace() + str;
            int length = str2.length();
            if (length > 512) {
                try {
                    if (str2.contains(Operators.BLOCK_START_STR) && str2.contains("}")) {
                        int indexOf = str2.indexOf(Operators.BLOCK_START_STR);
                        int indexOf2 = str2.indexOf("}");
                        if (indexOf < indexOf2) {
                            saveStorage(SessionConstants.EVENT_TRACE, str2.substring(indexOf2 + 1));
                            return;
                        } else {
                            saveStorage(SessionConstants.EVENT_TRACE, str2.substring(indexOf));
                            return;
                        }
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                if (isDebug()) {
                    LoginTLogAdapter.v(TAG, "event length > 512, subString to 512");
                }
                str2 = str2.substring(length - 512, length);
            }
            saveStorage(SessionConstants.EVENT_TRACE, str2);
        }
    }

    public void removeEventTrace() {
        LoginTLogAdapter.v(TAG, "removeEventTrace");
        removeStorage(SessionConstants.EVENT_TRACE);
    }

    public String[] getSsoDomainList() {
        initStorage();
        if (this.storage == null) {
            return null;
        }
        String string = this.storage.getString(SessionConstants.SSO_DOMAIN_LIST, "");
        if (!TextUtils.isEmpty(string)) {
            return (String[]) JSONArray.parseArray(string).toArray(new String[0]);
        }
        return null;
    }

    public void setSsoDomainList(String[] strArr) {
        String str = "";
        if (strArr != null) {
            str = JSONArray.toJSONString(strArr);
        }
        if (isDebug()) {
            LoginTLogAdapter.v(TAG, "setSsoDomainList=" + str);
        }
        saveStorage(SessionConstants.SSO_DOMAIN_LIST, str);
    }

    public String getHeadPicLink() {
        initStorage();
        if (TextUtils.isEmpty(this.mHeadPicLink) && this.storage != null) {
            this.mHeadPicLink = this.storage.getString(SessionConstants.HEAD_PIC_LINK, "");
        }
        return this.mHeadPicLink;
    }

    public void setLoginPhone(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set loginPhone=" + str);
        }
        this.mLoginPhone = str;
        saveStorage(SessionConstants.LOGIN_PHONE, str);
    }

    public String getLoginPhone() {
        initStorage();
        if (TextUtils.isEmpty(this.mLoginPhone) && this.storage != null) {
            String string = this.storage.getString(SessionConstants.LOGIN_PHONE, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            this.mLoginPhone = decrypt(string);
        }
        return this.mLoginPhone;
    }

    public void setHeadPicLink(String str) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "set setHeadPicLink=" + str);
        }
        this.mHeadPicLink = str;
        saveStorage(SessionConstants.HEAD_PIC_LINK, this.mHeadPicLink);
    }

    public boolean isNewSessionTag() {
        initStorage();
        if (!this.mNewSessionTag && this.storage != null) {
            this.mNewSessionTag = this.storage.getBoolean(NEW_SESSION_TAG, false);
        }
        return this.mNewSessionTag;
    }

    public void setNewSessionTag(boolean z) {
        if (isDebug()) {
            LoginTLogAdapter.d(TAG, "setNewSessionTag=" + z);
        }
        this.mNewSessionTag = z;
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            edit.putBoolean(NEW_SESSION_TAG, z);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        }
    }

    public void onLoginSuccess(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, long j, String[] strArr, String[] strArr2, String[] strArr3, long j2, long j3, String str9) {
        String str10 = str;
        String str11 = str2;
        String str12 = str3;
        String str13 = str4;
        String str14 = str5;
        String str15 = str6;
        long j4 = j2;
        long j5 = j3;
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            if (!TextUtils.isEmpty(str5) || !TextUtils.equals(getOldNick(), str12)) {
                this.mHeadPicLink = str14;
                edit.putString(SessionConstants.HEAD_PIC_LINK, str14);
            }
            if (isDebug()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("sdk onLoginSuccess, saveSession. sid=");
                stringBuffer.append(str);
                stringBuffer.append(", nick=");
                stringBuffer.append(str12);
                stringBuffer.append(", userId=");
                stringBuffer.append(str13);
                stringBuffer.append(", autologintoken:");
                stringBuffer.append(!TextUtils.isEmpty(str6));
                stringBuffer.append(", ssotoken:");
                stringBuffer.append(!TextUtils.isEmpty(str7));
                stringBuffer.append(", expiresTime=");
                stringBuffer.append(j4);
                LoginTLogAdapter.d(TAG, stringBuffer.toString());
            }
            this.mSid = str10;
            edit.putString("sid", encode(str));
            this.mOldSid = str10;
            edit.putString(SessionConstants.OLDSID, encode(str));
            this.mSessionExpiredTime = j4;
            edit.putLong(SessionConstants.SESSION_EXPIRED_TIME, j4);
            this.mEcode = str11;
            edit.putString("ecode", encode(str2));
            this.mUserId = str13;
            edit.putString("userId", encode(str13));
            this.mOldUserId = str13;
            edit.putString(SessionConstants.OLDUSERID, encode(str13));
            this.mNick = str12;
            edit.putString("nick", str12);
            this.mOldNick = str12;
            edit.putString(SessionConstants.OLDNICK, str12);
            this.mUserName = str12;
            edit.putString("username", str12);
            this.mLoginTime = j5;
            edit.putLong(SessionConstants.LOGIN_TIME, j5);
            this.mLoginPhone = str9;
            edit.putString(SessionConstants.LOGIN_PHONE, encode(this.mLoginPhone));
            JSONObject jSONObject = new JSONObject();
            try {
                if (!TextUtils.isEmpty(str6)) {
                    this.mAutoLoginToken = str15;
                    edit.putString(SessionConstants.AUTO_LOGIN_STR, encode(str15));
                    jSONObject.put("event", (Object) "autoLoginToken!=null");
                } else {
                    jSONObject.put("event", (Object) "autoLoginToke=null");
                }
                jSONObject.put("delta", (Object) Long.valueOf(j4 - j5));
                appendEventTrace(JSON.toJSONString(jSONObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
            edit.putString("ssoToken", encode(str7));
            if (!TextUtils.isEmpty(str8)) {
                edit.putString(SessionConstants.OneTimeTOKEN, encode(str8));
            }
            if (j == 0) {
                this.mHavanaSsoTokenExpiredTime = (System.currentTimeMillis() / 1000) + 900;
            } else {
                this.mHavanaSsoTokenExpiredTime = (System.currentTimeMillis() / 1000) + j;
            }
            edit.putLong(SessionConstants.HAVANA_SSO_TOKEN_EXPIRE, this.mHavanaSsoTokenExpiredTime);
            edit.commit();
        }
        injectExternalCookies(strArr);
        try {
            injectCookie(strArr2, strArr3);
        } catch (Throwable unused) {
        }
        sendClearSessionBroadcast();
    }

    public void clearSessionInfo() {
        if (isDebug()) {
            LoginTLogAdapter.e(TAG, "Clear sessionInfo");
        }
        setSid((String) null);
        setSubSid((String) null);
        removeStorage(SessionConstants.SESSION_EXPIRED_TIME);
        setEcode((String) null);
        setNick((String) null);
        setUserId((String) null);
        setUserName((String) null);
        setHeadPicLink((String) null);
        setExtJson((String) null);
        setEmail((String) null);
        setOneTimeToken((String) null);
        setLoginPhone((String) null);
        removeStorage(SessionConstants.LOGIN_SITE);
        try {
            injectCookie((String[]) null, (String[]) null);
        } catch (Exception unused) {
            removeUTCookie();
            removeWeitaoCookie();
            if (this.mCookie != null) {
                this.mCookie.clear();
            }
            FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_COOKIE_NEW, "");
        }
        try {
            UTAnalytics.getInstance().updateUserAccount("", "", "");
        } catch (Throwable unused2) {
            UTAnalytics.getInstance().updateUserAccount("", "");
        }
        sendClearSessionBroadcast();
    }

    public void clearAutoLoginInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("event", (Object) "clearAutoLoginInfo");
            appendEventTrace(JSON.toJSONString(jSONObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginTLogAdapter.e(TAG, "Clear AutoLoginInfo");
        this.mAutoLoginToken = null;
        setLoginToken((String) null);
    }

    public boolean sendClearSessionBroadcast() {
        Intent intent = new Intent(CLEAR_SESSION_ACTION);
        intent.putExtra("PROCESS_NAME", LoginThreadHelper.getCurProcessName(this.mContext));
        intent.setPackage(this.mContext.getPackageName());
        this.mContext.sendBroadcast(intent);
        if (!isDebug()) {
            return true;
        }
        LoginTLogAdapter.i(TAG, "sendBroadcast: CLEAR_SESSION_ACTION");
        return true;
    }

    public synchronized void injectExternalCookies(String[] strArr) {
        if (!(this.mContext == null || strArr == null)) {
            for (String str : strArr) {
                if (!TextUtils.isEmpty(str)) {
                    LoginCookie parseCookie = LoginCookieUtils.parseCookie(str);
                    String httpDomin = LoginCookieUtils.getHttpDomin(parseCookie);
                    String loginCookie = parseCookie.toString();
                    if (isDebug()) {
                        LoginTLogAdapter.v(TAG, "add external cookie: " + loginCookie);
                    }
                    setCookie(httpDomin, loginCookie);
                    if (this.mCookie != null) {
                        this.mCookie.add(parseCookie);
                    }
                }
            }
            if (this.mCookie != null && !this.mCookie.isEmpty()) {
                FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_COOKIE_NEW, encode(JSON.toJSONString(this.mCookie)));
            }
        }
    }

    public synchronized void injectCookie(String[] strArr, String[] strArr2) {
        injectCookie(strArr, strArr2, false);
    }

    public synchronized void injectCookie(String[] strArr, String[] strArr2, boolean z) {
        if (this.mContext != null) {
            if (strArr != null) {
                if (isDebug()) {
                    LoginTLogAdapter.v(TAG, "injectCookie cookies != null");
                }
                ArrayList<LoginCookie> arrayList = new ArrayList<>();
                for (String str : strArr) {
                    if (!TextUtils.isEmpty(str)) {
                        LoginCookie parseCookie = LoginCookieUtils.parseCookie(str);
                        String httpDomin = LoginCookieUtils.getHttpDomin(parseCookie);
                        String loginCookie = parseCookie.toString();
                        if (isDebug()) {
                            LoginTLogAdapter.v(TAG, "add cookie: " + loginCookie);
                        }
                        setCookie(httpDomin, loginCookie);
                        if (TextUtils.equals(parseCookie.domain, ".taobao.com")) {
                            arrayList.add(parseCookie);
                        }
                        if (this.mCookie != null) {
                            this.mCookie.add(parseCookie);
                        }
                    }
                }
                if (strArr2 == null) {
                    strArr2 = getSsoDomainList();
                }
                if (strArr2 != null && strArr2.length > 0 && !arrayList.isEmpty()) {
                    for (LoginCookie loginCookie2 : arrayList) {
                        String str2 = loginCookie2.domain;
                        for (String str3 : strArr2) {
                            loginCookie2.domain = str3;
                            String httpDomin2 = LoginCookieUtils.getHttpDomin(loginCookie2);
                            String loginCookie3 = loginCookie2.toString();
                            if (isDebug()) {
                                LoginTLogAdapter.d(TAG, "add cookies to domain:" + str3 + ", cookie = " + loginCookie3);
                            }
                            setCookie(httpDomin2, loginCookie3);
                        }
                        loginCookie2.domain = str2;
                    }
                }
                setSsoDomainList(strArr2);
                if (Build.VERSION.SDK_INT >= 21) {
                    CookieManager.getInstance().flush();
                } else {
                    CookieSyncManager.createInstance(this.mContext).sync();
                }
                if (this.mCookie != null && !this.mCookie.isEmpty()) {
                    if (z) {
                        FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_External_H5_COOKIE, encode(JSON.toJSONString(this.mCookie)));
                    } else {
                        FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_COOKIE_NEW, encode(JSON.toJSONString(this.mCookie)));
                    }
                }
            } else {
                this.mCookie = getCookies();
                if (this.mCookie != null && !this.mCookie.isEmpty()) {
                    clearWebviewCookie(strArr2);
                    FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_COOKIE_NEW, "");
                    FileUtils.writeFileData(this.mContext, SessionConstants.INJECT_External_H5_COOKIE, "");
                }
                try {
                    CookieManager.getInstance().removeSessionCookie();
                    CookieManager.getInstance().removeExpiredCookie();
                    if (Build.VERSION.SDK_INT >= 21) {
                        CookieManager.getInstance().flush();
                    } else {
                        CookieSyncManager.createInstance(this.mContext).sync();
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        return;
    }

    private void clearWebviewCookie(String[] strArr) {
        ArrayList<LoginCookie> arrayList = new ArrayList<>();
        if (this.mCookie != null && !this.mCookie.isEmpty()) {
            for (int i = 0; i < this.mCookie.size(); i++) {
                LoginCookie loginCookie = this.mCookie.get(i);
                if (!TextUtils.isEmpty(loginCookie.domain)) {
                    String httpDomin = LoginCookieUtils.getHttpDomin(loginCookie);
                    LoginCookieUtils.expiresCookies(loginCookie);
                    setCookie(httpDomin, loginCookie.toString());
                    if (TextUtils.equals(loginCookie.domain, ".taobao.com")) {
                        arrayList.add(loginCookie);
                    }
                }
            }
            if (strArr == null) {
                strArr = getSsoDomainList();
            }
            if (strArr != null && strArr.length > 0 && !arrayList.isEmpty()) {
                for (LoginCookie loginCookie2 : arrayList) {
                    String str = loginCookie2.domain;
                    for (String str2 : strArr) {
                        loginCookie2.domain = str2;
                        String httpDomin2 = LoginCookieUtils.getHttpDomin(loginCookie2);
                        LoginCookieUtils.expiresCookies(loginCookie2);
                        setCookie(httpDomin2, loginCookie2.toString());
                    }
                    loginCookie2.domain = str;
                }
            }
            removeUTCookie();
            removeWeitaoCookie();
            if (isDebug()) {
                LoginTLogAdapter.v(TAG, "injectCookie cookies is null");
            }
            if (this.mCookie != null) {
                this.mCookie.clear();
            }
        }
    }

    public List<LoginCookie> getCookies() {
        ArrayList arrayList = new ArrayList();
        String readFileData = FileUtils.readFileData(this.mContext, SessionConstants.INJECT_COOKIE_NEW);
        if (readFileData == null || readFileData.isEmpty()) {
            return arrayList;
        }
        String decrypt = decrypt(readFileData);
        if (isDebug()) {
            LoginTLogAdapter.v(TAG, "get cookie from storage:" + decrypt);
        }
        try {
            return JSON.parseArray(decrypt, LoginCookie.class);
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    private void setCookie(String str, String str2) {
        try {
            CookieManager.getInstance().setCookie(str, str2);
        } catch (Throwable unused) {
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("setCookieException");
            uTCustomHitBuilder.setProperty("cookie", str2);
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        }
    }

    public void clearCookieManager() {
        if (this.mCookie == null || this.mCookie.size() == 0) {
            this.mCookie = getCookies();
        }
        clearWebviewCookie(getSsoDomainList());
    }

    public boolean recoverCookie() {
        CookieSyncManager createInstance = CookieSyncManager.createInstance(this.mContext);
        ArrayList<LoginCookie> arrayList = new ArrayList<>();
        List<LoginCookie> cookies = getCookies();
        if (cookies == null || cookies.size() <= 0) {
            return false;
        }
        for (LoginCookie next : cookies) {
            setCookie(next.domain, next.toString());
            if (TextUtils.equals(next.domain, ".taobao.com")) {
                arrayList.add(next);
            }
        }
        String[] ssoDomainList = getSsoDomainList();
        if (ssoDomainList != null && ssoDomainList.length > 0 && !arrayList.isEmpty()) {
            for (LoginCookie loginCookie : arrayList) {
                String str = loginCookie.domain;
                for (String str2 : ssoDomainList) {
                    loginCookie.domain = str2;
                    String httpDomin = LoginCookieUtils.getHttpDomin(loginCookie);
                    String loginCookie2 = loginCookie.toString();
                    if (isDebug()) {
                        LoginTLogAdapter.v(TAG, "add cookies to domain:" + str2 + ", cookie = " + loginCookie2);
                    }
                    setCookie(httpDomin, loginCookie2);
                }
                loginCookie.domain = str;
            }
        }
        createInstance.sync();
        return true;
    }

    public void clearSessionOnlyCookie() {
        if (isDebug()) {
            LoginTLogAdapter.i(TAG, "start clearSessionOnlyCookie");
        }
        try {
            CookieSyncManager createInstance = CookieSyncManager.createInstance(this.mContext);
            CookieManager.getInstance().removeSessionCookie();
            CookieManager.getInstance().removeExpiredCookie();
            createInstance.sync();
        } catch (Throwable unused) {
        }
    }

    private void removeUTCookie() {
        LoginCookie loginCookie = new LoginCookie();
        loginCookie.name = "unb";
        loginCookie.domain = ".taobao.com";
        loginCookie.path = "/";
        loginCookie.value = "";
        LoginCookieUtils.expiresCookies(loginCookie);
        try {
            CookieManager.getInstance().setCookie(LoginCookieUtils.getHttpDomin(loginCookie), loginCookie.toString());
        } catch (Exception unused) {
        }
    }

    private void removeWeitaoCookie() {
        LoginCookie loginCookie = new LoginCookie();
        loginCookie.name = "cookiej007";
        loginCookie.domain = ".jaeapp.com";
        loginCookie.path = "/";
        loginCookie.value = "";
        LoginCookieUtils.expiresCookies(loginCookie);
        try {
            CookieManager.getInstance().setCookie(LoginCookieUtils.getHttpDomin(loginCookie), loginCookie.toString());
        } catch (Throwable unused) {
        }
    }

    @SuppressLint({"NewApi"})
    public void saveStorage(String str, String str2) {
        initStorage();
        if (this.storage != null) {
            if (str2 == null) {
                removeStorage(str);
            } else {
                SharedPreferences.Editor edit = this.storage.edit();
                edit.putString(str, str2);
                edit.apply();
            }
            if (!this.mNewSessionTag) {
                setNewSessionTag(true);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void saveStorage(String str, long j) {
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            edit.putLong(str, j);
            edit.apply();
            if (!this.mNewSessionTag) {
                setNewSessionTag(true);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void saveStorage(String str, int i) {
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            edit.putInt(str, i);
            edit.apply();
            if (!this.mNewSessionTag) {
                setNewSessionTag(true);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void removeStorage(String str) {
        initStorage();
        if (this.storage != null) {
            SharedPreferences.Editor edit = this.storage.edit();
            edit.remove(str);
            edit.apply();
        }
    }

    private void initSecurityGuardManager() {
        if (securityGuardManager == null && this.mContext != null) {
            synchronized (checkLock) {
                if (securityGuardManager == null) {
                    securityGuardManager = SecurityGuardManager.getInstance(this.mContext.getApplicationContext());
                }
            }
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public String encode(String str) {
        IDynamicDataEncryptComponent dynamicDataEncryptComp;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            initSecurityGuardManager();
            if (!(securityGuardManager == null || (dynamicDataEncryptComp = securityGuardManager.getDynamicDataEncryptComp()) == null)) {
                String dynamicEncryptDDp = dynamicDataEncryptComp.dynamicEncryptDDp(str);
                return TextUtils.isEmpty(dynamicEncryptDDp) ? str : dynamicEncryptDDp;
            }
        } catch (Exception unused) {
        }
        return str;
    }

    public String decrypt(String str) {
        IDynamicDataEncryptComponent dynamicDataEncryptComp;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        LoginTLogAdapter.d(TAG, "decrpytedData = " + str);
        try {
            initSecurityGuardManager();
            if (securityGuardManager == null || (dynamicDataEncryptComp = securityGuardManager.getDynamicDataEncryptComp()) == null) {
                return "";
            }
            if (str.length() <= 4 || str.charAt(3) != '&') {
                return dynamicDataEncryptComp.dynamicDecrypt(str);
            }
            return dynamicDataEncryptComp.dynamicDecryptDDp(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
