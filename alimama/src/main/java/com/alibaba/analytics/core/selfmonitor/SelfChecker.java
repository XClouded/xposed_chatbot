package com.alibaba.analytics.core.selfmonitor;

import alimama.com.unweventparse.constants.EventConstants;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.config.UTDBConfigEntity;
import com.alibaba.analytics.core.config.UTSampleConfBiz;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.sync.TnetUtil;
import com.alibaba.analytics.core.sync.UploadLogFromDB;
import com.alibaba.analytics.core.sync.UploadMgr;
import com.alibaba.analytics.core.sync.UrlWrapper;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.analytics.version.UTBuildInfo;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.sample.AMSamplingMgr;
import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.tao.log.TLogInitializer;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import org.json.JSONException;
import org.json.JSONObject;

public class SelfChecker implements SystemConfigMgr.IKVChangeListener {
    private static SelfChecker instance = new SelfChecker();

    public static SelfChecker getInstance() {
        return instance;
    }

    public void register() {
        SystemConfigMgr.getInstance().register("selfcheck", this);
    }

    public void onChange(final String str, final String str2) {
        Logger.e("SelfChecker", "key", str, "value", str2);
        TaskExecutor.getInstance().schedule((ScheduledFuture) null, new Runnable() {
            public void run() {
                SelfChecker.this.check(str, str2);
            }
        }, 5000);
    }

    public String check(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("current_time", "" + System.currentTimeMillis());
            jSONObject.put("is_init", "" + Variables.getInstance().isInit());
            jSONObject.put(RestUrlWrapper.FIELD_SDK_VERSION, "" + UTBuildInfo.getInstance().getFullSDKVersion());
            jSONObject.put("appkey", "" + Variables.getInstance().getAppkey());
            jSONObject.put("secret", "" + Variables.getInstance().getSecret());
            IUTRequestAuthentication requestAuthenticationInstance = Variables.getInstance().getRequestAuthenticationInstance();
            if (requestAuthenticationInstance == null) {
                jSONObject.put("security_mode", "-1");
            } else if (requestAuthenticationInstance instanceof UTBaseRequestAuthentication) {
                jSONObject.put("security_mode", "1");
            } else if (requestAuthenticationInstance instanceof UTSecuritySDKRequestAuthentication) {
                jSONObject.put("security_mode", "2");
            } else if (requestAuthenticationInstance instanceof UTSecurityThridRequestAuthentication) {
                jSONObject.put("security_mode", "3");
            }
            jSONObject.put("run_process", AppInfoUtil.getCurProcessName(Variables.getInstance().getContext()));
            jSONObject.put("ut_realtime_debug_switch", Variables.getInstance().isRealTimeDebug());
            jSONObject.put("ap_realtime_debug_switch", Variables.getInstance().isApRealTimeDebugging());
            jSONObject.put("ap_sampling_seed", AMSamplingMgr.getInstance().getSamplingSeed());
            jSONObject.put("upload_interval", UploadMgr.getInstance().getCurrentUploadInterval());
            samplingCheck(jSONObject, str2);
            boolean hasSuccess = UploadLogFromDB.getInstance().hasSuccess();
            jSONObject.put("upload_success", hasSuccess);
            jSONObject.put("upload_mode", UploadMgr.getInstance().getCurrentMode() + "");
            boolean isHttpService = Variables.getInstance().isHttpService();
            jSONObject.put("tnet_degrade", isHttpService);
            if (isHttpService) {
                jSONObject.put("tnet_error_code", TnetUtil.mErrorCode);
            }
            if (!hasSuccess) {
                jSONObject.put("http_error_code", UrlWrapper.mErrorCode);
            }
            List<? extends Entity> find = Variables.getInstance().getDbMgr().find(UTDBConfigEntity.class, (String) null, (String) null, -1);
            if (find != null) {
                for (int i = 0; i < find.size(); i++) {
                    UTDBConfigEntity uTDBConfigEntity = (UTDBConfigEntity) find.get(i);
                    jSONObject.put("entity.getGroupname()" + uTDBConfigEntity.getGroupname(), uTDBConfigEntity.getConfContent());
                }
            }
        } catch (Throwable th) {
            try {
                jSONObject.put("resport_error", th.getLocalizedMessage() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String jSONObject2 = jSONObject.toString();
        report(jSONObject2);
        return jSONObject2;
    }

    private void samplingCheck(JSONObject jSONObject, String str) {
        String str2;
        String str3;
        String str4;
        String str5;
        try {
            if (!TextUtils.isEmpty(str)) {
                JSONObject optJSONObject = new JSONObject(str).optJSONObject("cp");
                String str6 = null;
                if (optJSONObject != null) {
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject(DXBindingXConstant.AP);
                    if (optJSONObject2 != null) {
                        str4 = optJSONObject2.optString("type");
                        str3 = optJSONObject2.optString("module");
                        str5 = optJSONObject2.optString("point");
                    } else {
                        str5 = null;
                        str4 = null;
                        str3 = null;
                    }
                    JSONObject optJSONObject3 = optJSONObject.optJSONObject("ut");
                    if (optJSONObject3 != null) {
                        str6 = optJSONObject3.optString(EventConstants.EVENTID);
                        str2 = optJSONObject3.optString(EventConstants.UT.ARG1);
                    } else {
                        str2 = null;
                    }
                } else {
                    str2 = null;
                    str5 = null;
                    str4 = null;
                    str3 = null;
                }
                if (!TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str5)) {
                    jSONObject.put("ap_sampling_result", AMSamplingMgr.getInstance().checkSampled(EventType.getEventTypeByNameSpace(str4), str3, str5));
                }
                if (!TextUtils.isEmpty(str6)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(LogField.EVENTID.toString(), str6);
                    if (!TextUtils.isEmpty(str2)) {
                        hashMap.put(LogField.ARG1.toString(), str2);
                    }
                    jSONObject.put("ut_sampling_result", UTSampleConfBiz.getInstance().isSampleSuccess(hashMap));
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void report(String str) {
        File externalFilesDir = Variables.getInstance().getContext().getExternalFilesDir(TLogInitializer.DEFAULT_DIR);
        if (externalFilesDir != null) {
            File file = new File(externalFilesDir.getAbsolutePath() + File.separator + "analytics.log");
            if (file.exists()) {
                file.delete();
            } else {
                try {
                    if (!externalFilesDir.exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(str.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Throwable unused) {
                Logger.e();
            }
        }
    }
}
