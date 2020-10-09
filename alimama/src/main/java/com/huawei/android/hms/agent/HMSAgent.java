package com.huawei.android.hms.agent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.huawei.android.hms.agent.common.ActivityMgr;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.CheckUpdateApi;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.IClientConnectCallback;
import com.huawei.android.hms.agent.common.INoProguard;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.DeleteTokenApi;
import com.huawei.android.hms.agent.push.EnableReceiveNormalMsgApi;
import com.huawei.android.hms.agent.push.EnableReceiveNotifyMsgApi;
import com.huawei.android.hms.agent.push.GetPushStateApi;
import com.huawei.android.hms.agent.push.GetTokenApi;
import com.huawei.android.hms.agent.push.QueryAgreementApi;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNormalMsgHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.android.hms.agent.push.handler.GetPushStateHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.android.hms.agent.push.handler.QueryAgreementHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.taobao.weex.el.parse.Operators;

public final class HMSAgent implements INoProguard {
    public static final String CURVER = "020603306";
    private static final String VER_020503001 = "020503001";
    private static final String VER_020600001 = "020600001";
    private static final String VER_020600200 = "020600200";
    private static final String VER_020601002 = "020601002";
    private static final String VER_020601302 = "020601302";
    private static final String VER_020603306 = "020603306";

    public static final class AgentResultCode {
        public static final int APICLIENT_TIMEOUT = -1007;
        public static final int CALL_EXCEPTION = -1008;
        public static final int EMPTY_PARAM = -1009;
        public static final int HMSAGENT_NO_INIT = -1000;
        public static final int HMSAGENT_SUCCESS = 0;
        public static final int NO_ACTIVITY_FOR_USE = -1001;
        public static final int ON_ACTIVITY_RESULT_ERROR = -1005;
        public static final int REQUEST_REPEATED = -1006;
        public static final int RESULT_IS_NULL = -1002;
        public static final int START_ACTIVITY_ERROR = -1004;
        public static final int STATUS_IS_NULL = -1003;
    }

    private HMSAgent() {
    }

    private static boolean checkSDKVersion(Context context) {
        long parseLong = Long.parseLong("020603306") / 1000;
        if (20603 == parseLong) {
            return true;
        }
        String str = "error: HMSAgent major version code (" + parseLong + ") does not match HMSSDK major version code (" + 20603 + Operators.BRACKET_END_STR;
        HMSAgentLog.e(str);
        Toast.makeText(context, str, 1).show();
        return false;
    }

    public static boolean init(Activity activity) {
        return init((Application) null, activity);
    }

    public static boolean init(Application application) {
        return init(application, (Activity) null);
    }

    public static boolean init(Application application, Activity activity) {
        if (application == null && activity == null) {
            HMSAgentLog.e("the param of method HMSAgent.init can not be null !!!");
            return false;
        }
        if (application == null) {
            application = activity.getApplication();
        }
        if (application == null) {
            HMSAgentLog.e("the param of method HMSAgent.init app can not be null !!!");
            return false;
        }
        if (activity != null && activity.isFinishing()) {
            activity = null;
        }
        if (!checkSDKVersion(application)) {
            return false;
        }
        HMSAgentLog.i("init HMSAgent 020603306 with hmssdkver 20603306");
        ActivityMgr.INST.init(application, activity);
        ApiClientMgr.INST.init(application);
        return true;
    }

    public static void destroy() {
        HMSAgentLog.i("destroy HMSAgent");
        ActivityMgr.INST.release();
        ApiClientMgr.INST.release();
    }

    public static void connect(Activity activity, final ConnectHandler connectHandler) {
        HMSAgentLog.i("start connect");
        ApiClientMgr.INST.connect(new IClientConnectCallback() {
            public void onConnect(final int i, HuaweiApiClient huaweiApiClient) {
                if (connectHandler != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            connectHandler.onConnect(i);
                        }
                    });
                }
            }
        }, true);
    }

    public static void checkUpdate(Activity activity, CheckUpdateHandler checkUpdateHandler) {
        new CheckUpdateApi().checkUpdate(activity, checkUpdateHandler);
    }

    public static final class Push {
        public static void getToken(GetTokenHandler getTokenHandler) {
            new GetTokenApi().getToken(getTokenHandler);
        }

        public static void deleteToken(String str, DeleteTokenHandler deleteTokenHandler) {
            new DeleteTokenApi().deleteToken(str, deleteTokenHandler);
        }

        public static void getPushState(GetPushStateHandler getPushStateHandler) {
            new GetPushStateApi().getPushState(getPushStateHandler);
        }

        public static void enableReceiveNotifyMsg(boolean z, EnableReceiveNotifyMsgHandler enableReceiveNotifyMsgHandler) {
            new EnableReceiveNotifyMsgApi().enableReceiveNotifyMsg(z, enableReceiveNotifyMsgHandler);
        }

        public static void enableReceiveNormalMsg(boolean z, EnableReceiveNormalMsgHandler enableReceiveNormalMsgHandler) {
            new EnableReceiveNormalMsgApi().enableReceiveNormalMsg(z, enableReceiveNormalMsgHandler);
        }

        public static void queryAgreement(QueryAgreementHandler queryAgreementHandler) {
            new QueryAgreementApi().queryAgreement(queryAgreementHandler);
        }
    }
}
