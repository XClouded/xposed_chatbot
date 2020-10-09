package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.util.ArrayList;
import java.util.List;

public final class ApiClientMgr implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener, IActivityResumeCallback, IActivityPauseCallback, IActivityDestroyedCallback {
    private static final int APICLIENT_CONNECT_TIMEOUT = 30000;
    private static final Object APICLIENT_LOCK = new Object();
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT = 3000;
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG = 4;
    private static final int APICLIENT_TIMEOUT_HANDLE_MSG = 3;
    /* access modifiers changed from: private */
    public static final Object CALLBACK_LOCK = new Object();
    public static final ApiClientMgr INST = new ApiClientMgr();
    private static final int MAX_RESOLVE_TIMES = 3;
    private static final String PACKAGE_NAME_HIAPP = "com.huawei.appmarket";
    private static final Object STATIC_CALLBACK_LOCK = new Object();
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT = 3000;
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG = 5;
    private boolean allowResolveConnectError = false;
    private HuaweiApiClient apiClient;
    /* access modifiers changed from: private */
    public List<IClientConnectCallback> connCallbacks = new ArrayList();
    private Context context;
    private String curAppPackageName;
    private int curLeftResolveTimes = 3;
    /* access modifiers changed from: private */
    public boolean hasOverActivity = false;
    private boolean isResolving;
    /* access modifiers changed from: private */
    public BridgeActivity resolveActivity;
    private List<IClientConnectCallback> staticCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public Handler timeoutHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message message) {
            boolean z;
            synchronized (ApiClientMgr.CALLBACK_LOCK) {
                z = !ApiClientMgr.this.connCallbacks.isEmpty();
            }
            if (message != null && message.what == 3 && z) {
                HMSAgentLog.d("connect time out");
                HuaweiApiClient unused = ApiClientMgr.this.resetApiClient();
                ApiClientMgr.this.onConnectEnd(HMSAgent.AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            } else if (message != null && message.what == 4 && z) {
                HMSAgentLog.d("start activity time out");
                ApiClientMgr.this.onConnectEnd(HMSAgent.AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            } else if (message == null || message.what != 5 || !z) {
                return false;
            } else {
                HMSAgentLog.d("Discarded update dispose:hasOverActivity=" + ApiClientMgr.this.hasOverActivity + " resolveActivity=" + StrUtils.objDesc(ApiClientMgr.this.resolveActivity));
                if (ApiClientMgr.this.hasOverActivity && ApiClientMgr.this.resolveActivity != null && !ApiClientMgr.this.resolveActivity.isFinishing()) {
                    ApiClientMgr.this.onResolveErrorRst(13);
                }
                return true;
            }
        }
    });

    private ApiClientMgr() {
    }

    public void init(Application application) {
        HMSAgentLog.d("init");
        this.context = application.getApplicationContext();
        this.curAppPackageName = application.getPackageName();
        ActivityMgr.INST.unRegisterActivitResumeEvent(this);
        ActivityMgr.INST.registerActivitResumeEvent(this);
        ActivityMgr.INST.unRegisterActivitPauseEvent(this);
        ActivityMgr.INST.registerActivitPauseEvent(this);
        ActivityMgr.INST.unRegisterActivitDestroyedEvent(this);
        ActivityMgr.INST.registerActivitDestroyedEvent(this);
    }

    public void release() {
        HMSAgentLog.d("release");
        this.isResolving = false;
        this.resolveActivity = null;
        this.hasOverActivity = false;
        HuaweiApiClient apiClient2 = getApiClient();
        if (apiClient2 != null) {
            apiClient2.disconnect();
        }
        synchronized (APICLIENT_LOCK) {
            this.apiClient = null;
        }
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.clear();
        }
        synchronized (CALLBACK_LOCK) {
            this.connCallbacks.clear();
        }
    }

    public HuaweiApiClient getApiClient() {
        HuaweiApiClient resetApiClient;
        synchronized (APICLIENT_LOCK) {
            resetApiClient = this.apiClient != null ? this.apiClient : resetApiClient();
        }
        return resetApiClient;
    }

    public boolean isConnect(HuaweiApiClient huaweiApiClient) {
        return huaweiApiClient != null && huaweiApiClient.isConnected();
    }

    public void registerClientConnect(IClientConnectCallback iClientConnectCallback) {
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.add(iClientConnectCallback);
        }
    }

    public void removeClientConnectCallback(IClientConnectCallback iClientConnectCallback) {
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.remove(iClientConnectCallback);
        }
    }

    /* access modifiers changed from: private */
    public HuaweiApiClient resetApiClient() {
        HuaweiApiClient huaweiApiClient;
        if (this.context == null) {
            HMSAgentLog.e("HMSAgent not init");
            return null;
        }
        synchronized (APICLIENT_LOCK) {
            if (this.apiClient != null) {
                disConnectClientDelay(this.apiClient, 60000);
            }
            HMSAgentLog.d("reset client");
            this.apiClient = new HuaweiApiClient.Builder(this.context).addApi(HuaweiPush.PUSH_API).addConnectionCallbacks(INST).addOnConnectionFailedListener(INST).build();
            huaweiApiClient = this.apiClient;
        }
        return huaweiApiClient;
    }

    public void connect(IClientConnectCallback iClientConnectCallback, boolean z) {
        if (this.context == null) {
            aSysnCallback(-1000, iClientConnectCallback);
            return;
        }
        HuaweiApiClient apiClient2 = getApiClient();
        boolean z2 = false;
        if (apiClient2 == null || !apiClient2.isConnected()) {
            synchronized (CALLBACK_LOCK) {
                HMSAgentLog.d("client is invalid：size=" + this.connCallbacks.size());
                if (this.allowResolveConnectError || z) {
                    z2 = true;
                }
                this.allowResolveConnectError = z2;
                if (this.connCallbacks.isEmpty()) {
                    this.connCallbacks.add(iClientConnectCallback);
                    this.curLeftResolveTimes = 3;
                    startConnect();
                } else {
                    this.connCallbacks.add(iClientConnectCallback);
                }
            }
            return;
        }
        HMSAgentLog.d("client is valid");
        aSysnCallback(0, iClientConnectCallback);
    }

    private void startConnect() {
        this.curLeftResolveTimes--;
        HMSAgentLog.d("start thread to connect");
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                HuaweiApiClient apiClient = ApiClientMgr.this.getApiClient();
                if (apiClient != null) {
                    HMSAgentLog.d("connect");
                    Activity lastActivity = ActivityMgr.INST.getLastActivity();
                    ApiClientMgr.this.timeoutHandler.sendEmptyMessageDelayed(3, 30000);
                    apiClient.connect(lastActivity);
                    return;
                }
                HMSAgentLog.d("client is generate error");
                ApiClientMgr.this.onConnectEnd(-1002);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConnectEnd(int i) {
        HMSAgentLog.d("connect end:" + i);
        synchronized (CALLBACK_LOCK) {
            for (IClientConnectCallback aSysnCallback : this.connCallbacks) {
                aSysnCallback(i, aSysnCallback);
            }
            this.connCallbacks.clear();
            this.allowResolveConnectError = false;
        }
        synchronized (STATIC_CALLBACK_LOCK) {
            for (IClientConnectCallback aSysnCallback2 : this.staticCallbacks) {
                aSysnCallback(i, aSysnCallback2);
            }
            this.staticCallbacks.clear();
        }
    }

    private void aSysnCallback(final int i, final IClientConnectCallback iClientConnectCallback) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                HuaweiApiClient apiClient = ApiClientMgr.this.getApiClient();
                HMSAgentLog.d("callback connect: rst=" + i + " apiClient=" + apiClient);
                iClientConnectCallback.onConnect(i, apiClient);
            }
        });
    }

    public void onActivityResume(Activity activity) {
        HuaweiApiClient apiClient2 = getApiClient();
        if (apiClient2 != null) {
            HMSAgentLog.d("tell hmssdk: onResume");
            apiClient2.onResume(activity);
        }
        HMSAgentLog.d("is resolving:" + this.isResolving);
        if (this.isResolving && !PACKAGE_NAME_HIAPP.equals(this.curAppPackageName)) {
            if (activity instanceof BridgeActivity) {
                this.resolveActivity = (BridgeActivity) activity;
                this.hasOverActivity = false;
                HMSAgentLog.d("received bridgeActivity:" + StrUtils.objDesc(this.resolveActivity));
            } else if (this.resolveActivity != null && !this.resolveActivity.isFinishing()) {
                this.hasOverActivity = true;
                HMSAgentLog.d("received other Activity:" + StrUtils.objDesc(this.resolveActivity));
            }
            this.timeoutHandler.removeMessages(5);
            this.timeoutHandler.sendEmptyMessageDelayed(5, TBToast.Duration.MEDIUM);
        }
    }

    public void onActivityPause(Activity activity) {
        HuaweiApiClient apiClient2 = getApiClient();
        if (apiClient2 != null) {
            apiClient2.onPause(activity);
        }
    }

    public void onActivityDestroyed(Activity activity, Activity activity2) {
        if (activity2 == null) {
            resetApiClient();
        }
    }

    /* access modifiers changed from: package-private */
    public void onResolveErrorRst(int i) {
        HuaweiApiClient apiClient2;
        HMSAgentLog.d("result=" + i);
        this.isResolving = false;
        this.resolveActivity = null;
        this.hasOverActivity = false;
        if (i != 0 || (apiClient2 = getApiClient()) == null || apiClient2.isConnecting() || apiClient2.isConnected() || this.curLeftResolveTimes <= 0) {
            onConnectEnd(i);
        } else {
            startConnect();
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityLunched() {
        HMSAgentLog.d("resolve onActivityLunched");
        this.timeoutHandler.removeMessages(4);
        this.isResolving = true;
    }

    public void onConnected() {
        HMSAgentLog.d("connect success");
        this.timeoutHandler.removeMessages(3);
        onConnectEnd(0);
    }

    public void onConnectionSuspended(int i) {
        HMSAgentLog.d("connect suspended");
        connect(new EmptyConnectCallback("onConnectionSuspended try end:"), true);
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.timeoutHandler.removeMessages(3);
        if (connectionResult == null) {
            HMSAgentLog.e("result is null");
            onConnectEnd(-1002);
            return;
        }
        int errorCode = connectionResult.getErrorCode();
        HMSAgentLog.d("errCode=" + errorCode + " allowResolve=" + this.allowResolveConnectError);
        if (!HuaweiApiAvailability.getInstance().isUserResolvableError(errorCode) || !this.allowResolveConnectError) {
            onConnectEnd(errorCode);
            return;
        }
        Activity lastActivity = ActivityMgr.INST.getLastActivity();
        if (lastActivity != null) {
            try {
                this.timeoutHandler.sendEmptyMessageDelayed(4, TBToast.Duration.MEDIUM);
                Intent intent = new Intent(lastActivity, HMSAgentActivity.class);
                intent.putExtra(HMSAgentActivity.CONN_ERR_CODE_TAG, errorCode);
                intent.putExtra(BaseAgentActivity.EXTRA_IS_FULLSCREEN, UIUtils.isActivityFullscreen(lastActivity));
                lastActivity.startActivity(intent);
            } catch (Exception e) {
                HMSAgentLog.e("start HMSAgentActivity exception:" + e.getMessage());
                this.timeoutHandler.removeMessages(4);
                onConnectEnd(-1004);
            }
        } else {
            HMSAgentLog.d("no activity");
            onConnectEnd(-1001);
        }
    }

    private static void disConnectClientDelay(final HuaweiApiClient huaweiApiClient, int i) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                huaweiApiClient.disconnect();
            }
        }, (long) i);
    }
}
