package android.taobao.windvane.packageapp;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WVCustomPackageAppConfig {
    private static final String TAG = "WVCustomPackageAppConfig";
    private static volatile WVCustomPackageAppConfig instance;
    public String customConfig = "{}";
    /* access modifiers changed from: private */
    public int mComboRqCount = 0;
    /* access modifiers changed from: private */
    public int updateCount = 0;
    private String v = "0";

    static /* synthetic */ int access$006(WVCustomPackageAppConfig wVCustomPackageAppConfig) {
        int i = wVCustomPackageAppConfig.mComboRqCount - 1;
        wVCustomPackageAppConfig.mComboRqCount = i;
        return i;
    }

    public static WVCustomPackageAppConfig getInstance() {
        if (instance == null) {
            synchronized (WVCustomPackageAppConfig.class) {
                if (instance == null) {
                    instance = new WVCustomPackageAppConfig();
                }
            }
        }
        return instance;
    }

    public synchronized void resetConfig() {
        this.v = "0";
        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_CUSTOM, "0");
    }

    public void updateCustomConfig(WVConfigUpdateCallback wVConfigUpdateCallback, String str, String str2) {
        if (WVCommonConfig.commonConfig.packageAppStatus != 2) {
            if (wVConfigUpdateCallback != null) {
                wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
            }
        } else if ("3".equals(GlobalConfig.zType)) {
            updateCustomInfos(WVConfigManager.getInstance().getConfigUrl(Constants.LogTransferLevel.L6, "0", WVConfigUtils.getTargetValue(), str2), wVConfigUpdateCallback, (List) null, str2);
        } else {
            ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
            ArrayList arrayList = new ArrayList();
            this.updateCount = 0;
            for (Map.Entry<String, ZipAppInfo> value : locGlobalConfig.getAppsTable().entrySet()) {
                ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                boolean z = zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE || zipAppInfo.status == ZipAppConstants.ZIP_REMOVED;
                if (zipAppInfo.isOptional && !z) {
                    arrayList.add(zipAppInfo.name);
                }
            }
            if (!arrayList.isEmpty()) {
                this.updateCount = arrayList.size();
                Collections.sort(arrayList, Collator.getInstance(Locale.ENGLISH));
                if (WVCommonConfig.commonConfig.customsDirectQueryLimit <= arrayList.size()) {
                    if (TextUtils.isEmpty(str)) {
                        str = WVConfigManager.getInstance().getConfigUrl(Constants.LogTransferLevel.L6, "0", WVConfigUtils.getTargetValue(), str2);
                    }
                    updateCustomInfos(str, wVConfigUpdateCallback, arrayList, str2);
                    return;
                }
                updateByCombo(arrayList, wVConfigUpdateCallback, str2);
            } else if (wVConfigUpdateCallback != null) {
                wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateByCombo(List<String> list, WVConfigUpdateCallback wVConfigUpdateCallback, String str) {
        Iterator<String> it = list.iterator();
        this.mComboRqCount = 0;
        while (it.hasNext()) {
            this.mComboRqCount++;
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < WVCommonConfig.commonConfig.customsComboLimit && it.hasNext(); i++) {
                arrayList.add(it.next());
            }
            updateCustomComboInfos(getConfigUrl(arrayList, str), wVConfigUpdateCallback, arrayList, str);
        }
    }

    private void updateCustomComboInfos(final String str, final WVConfigUpdateCallback wVConfigUpdateCallback, final List<String> list, String str2) {
        ConnectManager.getInstance().connect(str, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                WVCustomPackageAppConfig.access$006(WVCustomPackageAppConfig.this);
                if (httpResponse != null && httpResponse.getData() != null) {
                    try {
                        if (WVCustomPackageAppConfig.this.parseComboConfig(new String(httpResponse.getData(), "utf-8"), list)) {
                            if (wVConfigUpdateCallback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                                wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, WVCustomPackageAppConfig.this.updateCount);
                            }
                        } else if (wVConfigUpdateCallback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                        }
                    } catch (Exception e) {
                        if (wVConfigUpdateCallback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVCustomPackageAppConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (wVConfigUpdateCallback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }

            public void onError(int i, String str) {
                WVCustomPackageAppConfig.access$006(WVCustomPackageAppConfig.this);
                if (wVConfigUpdateCallback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                    wVConfigUpdateCallback.updateError(str, str);
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCustomPackageAppConfig.TAG, "update custom package failed! : " + str);
                super.onError(i, str);
            }
        });
    }

    private void updateCustomInfos(String str, WVConfigUpdateCallback wVConfigUpdateCallback, List list, String str2) {
        final WVConfigUpdateCallback wVConfigUpdateCallback2 = wVConfigUpdateCallback;
        final List list2 = list;
        final String str3 = str2;
        final String str4 = str;
        ConnectManager.getInstance().connectSync(str, new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse httpResponse, int i) {
                if (httpResponse != null && httpResponse.getData() != null) {
                    try {
                        String str = new String(httpResponse.getData(), "utf-8");
                        if ("3".equals(GlobalConfig.zType)) {
                            TaoLog.i("ZCache", "custom 3.0");
                            WVCustomPackageAppConfig.this.customConfig = str;
                            wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                        } else if (!WVCustomPackageAppConfig.this.parseConfig(str, list2)) {
                            if (wVConfigUpdateCallback2 != null) {
                                wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                            }
                        } else if (list2 != null && list2.size() > 0) {
                            WVCustomPackageAppConfig.this.updateByCombo(list2, wVConfigUpdateCallback2, str3);
                        } else if (wVConfigUpdateCallback2 != null) {
                            wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                        }
                    } catch (UnsupportedEncodingException e) {
                        if (wVConfigUpdateCallback2 != null) {
                            wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVCustomPackageAppConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (wVConfigUpdateCallback2 != null) {
                    wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }

            public void onError(int i, String str) {
                if (wVConfigUpdateCallback2 != null) {
                    wVConfigUpdateCallback2.updateError(str4, str);
                    wVConfigUpdateCallback2.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCustomPackageAppConfig.TAG, "update custom package failed! : " + str);
                super.onError(i, str);
            }
        });
    }

    public String getConfigUrl(List<String> list, String str) {
        Iterator<String> it = list.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(WVConfigManager.getInstance().configDomainByEnv());
        sb.append("/appconfig/");
        sb.append(str);
        sb.append("/");
        String stringVal = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "abt", "a");
        char charAt = stringVal.charAt(0);
        if ('a' > charAt || charAt > 'c') {
            stringVal = "a";
        }
        sb.append(stringVal);
        sb.append("/");
        if (list.size() > 1) {
            sb.append("??");
        }
        for (int i = 0; i < WVCommonConfig.commonConfig.customsComboLimit && it.hasNext(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(it.next());
            sb.append("/app.json");
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public boolean parseComboConfig(String str, List<String> list) {
        String[] split;
        if (TextUtils.isEmpty(str) || (split = str.split("\\\n\\\n")) == null || split.length == 0 || split.length != list.size()) {
            return false;
        }
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        Iterator<String> it = list.iterator();
        for (int i = 0; i < split.length; i++) {
            try {
                JSONObject jSONObject = new JSONObject(split[i]);
                Iterator<String> keys = jSONObject.keys();
                if (keys.hasNext()) {
                    String next = keys.next();
                    String next2 = it.next();
                    if (!next.equals(UploadQueueMgr.MSGTYPE_INTERVAL) || jSONObject.optInt(next, -1) != -1) {
                        JSONObject optJSONObject = jSONObject.optJSONObject(next);
                        if (optJSONObject != null) {
                            String optString = optJSONObject.optString("v", "");
                            if (!TextUtils.isEmpty(optString)) {
                                ZipAppInfo appInfo = locGlobalConfig.getAppInfo(next);
                                if (appInfo == null) {
                                    appInfo = new ZipAppInfo();
                                }
                                appInfo.v = optString;
                                appInfo.name = next;
                                appInfo.s = optJSONObject.optLong("s", 0);
                                appInfo.f = optJSONObject.optLong("f", 5);
                                appInfo.t = optJSONObject.optLong("t", 0);
                                appInfo.z = optJSONObject.optString("z", "");
                                appInfo.isOptional = true;
                            }
                        }
                    } else {
                        ZipAppInfo appInfo2 = locGlobalConfig.getAppInfo(next2);
                        if (appInfo2 == null) {
                            appInfo2 = new ZipAppInfo();
                        }
                        appInfo2.name = next2;
                        appInfo2.isOptional = true;
                        appInfo2.status = ZipAppConstants.ZIP_REMOVED;
                        appInfo2.f |= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
                    }
                }
            } catch (JSONException unused) {
            }
        }
        TaoLog.v(TAG, "解析成功 combo 一次");
        ConfigManager.saveGlobalConfigToloc(locGlobalConfig);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean parseConfig(String str, List list) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.v = jSONObject.optString("v", "0");
            if ("0".equals(this.v)) {
                return false;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject("apps");
            Iterator<String> keys = optJSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                long optLong = optJSONObject.optLong(next, 0);
                if (optLong != 0) {
                    new ZipAppInfo();
                    ZipAppInfo appInfo = locGlobalConfig.getAppInfo(next);
                    if (appInfo != null) {
                        if (optLong <= appInfo.s) {
                            list.remove(next);
                        }
                        appInfo.s = optLong;
                    }
                }
            }
            ConfigManager.saveGlobalConfigToloc(locGlobalConfig);
            return true;
        } catch (JSONException unused) {
        }
    }
}
