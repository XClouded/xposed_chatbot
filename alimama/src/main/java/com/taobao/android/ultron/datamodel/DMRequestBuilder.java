package com.taobao.android.ultron.datamodel;

import android.content.Context;
import android.content.SharedPreferences;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.datamodel.imp.DMRequester;
import com.taobao.android.ultron.utils.DebugUtils;
import java.util.Map;

public class DMRequestBuilder {
    public static final String NAMESPACE = "trade_debug";
    String mApi;
    boolean mAsync = false;
    int mBizId;
    String mBizName = "default";
    Context mContext;
    IDMContext mDMContext;
    String mDomain;
    boolean mGzip = true;
    Map<String, String> mHeaders;
    boolean mNeedEcode = true;
    boolean mNeedSession = true;
    Map<String, String> mParams;
    boolean mPostMethod = true;
    SharedPreferences mPreference;
    Class<?> mResponseClazz;
    boolean mSubmit = false;
    IDMComponent mTriggerComponent;
    String mUnitStrategy;
    boolean mUseWua = false;
    String mVersion;

    public Class<?> getResponseClazz() {
        return this.mResponseClazz;
    }

    public IDMComponent getTriggerComponent() {
        return this.mTriggerComponent;
    }

    public boolean isAsync() {
        return this.mAsync;
    }

    public boolean isSubmit() {
        return this.mSubmit;
    }

    public String getApi() {
        if (DebugUtils.isDebuggable(this.mContext)) {
            return this.mPreference.getString(this.mApi, this.mApi);
        }
        return this.mApi;
    }

    public String getVersion() {
        if (!DebugUtils.isDebuggable(this.mContext)) {
            return this.mVersion;
        }
        SharedPreferences sharedPreferences = this.mPreference;
        return sharedPreferences.getString(this.mApi + ".version", this.mVersion);
    }

    public boolean isNeedSession() {
        return this.mNeedSession;
    }

    public boolean isNeedEcode() {
        return this.mNeedEcode;
    }

    public String getDomain() {
        return this.mDomain;
    }

    public String getUnitStrategy() {
        return this.mUnitStrategy;
    }

    public int getBizId() {
        return this.mBizId;
    }

    public boolean isPostMethod() {
        return this.mPostMethod;
    }

    public boolean isUseWua() {
        return this.mUseWua;
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public String getBizName() {
        return this.mBizName;
    }

    public IDMContext getDMContext() {
        return this.mDMContext;
    }

    public boolean isGzip() {
        return this.mGzip;
    }

    public void setGzip(boolean z) {
        this.mGzip = z;
    }

    public DMRequestBuilder(Context context) {
        this.mContext = context;
        if (DebugUtils.isDebuggable(this.mContext)) {
            this.mPreference = this.mContext.getSharedPreferences(NAMESPACE, 0);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public DMRequestBuilder api(String str) {
        this.mApi = str;
        return this;
    }

    public DMRequestBuilder version(String str) {
        this.mVersion = str;
        return this;
    }

    public DMRequestBuilder bizId(int i) {
        this.mBizId = i;
        return this;
    }

    public DMRequestBuilder postMethod(boolean z) {
        this.mPostMethod = z;
        return this;
    }

    public DMRequestBuilder useWua(boolean z) {
        this.mUseWua = z;
        return this;
    }

    public DMRequestBuilder needSession(boolean z) {
        this.mNeedSession = z;
        return this;
    }

    public DMRequestBuilder needEcode(boolean z) {
        this.mNeedEcode = z;
        return this;
    }

    public DMRequestBuilder domain(String str) {
        this.mDomain = str;
        return this;
    }

    public DMRequestBuilder unitStrategy(String str) {
        this.mUnitStrategy = str;
        return this;
    }

    public DMRequestBuilder params(Map<String, String> map) {
        this.mParams = map;
        return this;
    }

    public DMRequestBuilder requestHeaders(Map<String, String> map) {
        this.mHeaders = map;
        return this;
    }

    public DMRequestBuilder bizName(String str) {
        this.mBizName = str;
        return this;
    }

    public IDMRequester build() {
        if (!checkParams()) {
            return null;
        }
        return new DMRequester(this);
    }

    public IDMRequester buildPage(IDMContext iDMContext) {
        if (!checkParams() || iDMContext == null) {
            return null;
        }
        this.mDMContext = iDMContext;
        return new DMRequester(this);
    }

    public IDMRequester buildAdjust(IDMComponent iDMComponent, IDMContext iDMContext) {
        if (!checkParams() || iDMContext == null) {
            return null;
        }
        this.mTriggerComponent = iDMComponent;
        this.mAsync = true;
        this.mDMContext = iDMContext;
        return new DMRequester(this);
    }

    public IDMRequester buildSubmit(Class<?> cls, IDMContext iDMContext) {
        if (!checkParams() || iDMContext == null) {
            return null;
        }
        this.mSubmit = true;
        this.mResponseClazz = cls;
        this.mDMContext = iDMContext;
        return new DMRequester(this);
    }

    private boolean checkParams() {
        if (this.mApi == null || this.mApi.length() <= 0 || this.mVersion == null || this.mVersion.length() <= 0) {
            return false;
        }
        return true;
    }
}
