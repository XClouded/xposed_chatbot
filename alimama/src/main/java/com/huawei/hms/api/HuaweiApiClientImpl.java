package com.huawei.hms.api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.taobao.windvane.config.WVConfigManager;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.huawei.hms.api.Api;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.c.g;
import com.huawei.hms.c.j;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.core.aidl.RequestHeader;
import com.huawei.hms.core.aidl.e;
import com.huawei.hms.core.aidl.f;
import com.huawei.hms.support.api.ResolveResult;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.BundleResult;
import com.huawei.hms.support.api.client.InnerApiClient;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.client.SubAppInfo;
import com.huawei.hms.support.api.entity.auth.PermissionInfo;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.api.entity.core.CheckConnectInfo;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.entity.core.ConnectInfo;
import com.huawei.hms.support.api.entity.core.ConnectResp;
import com.huawei.hms.support.api.entity.core.DisconnectInfo;
import com.huawei.hms.support.api.entity.core.DisconnectResp;
import com.huawei.hms.support.api.entity.core.JosGetNoticeResp;
import com.huawei.hms.update.provider.UpdateProvider;
import com.huawei.updatesdk.UpdateSdkAPI;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.uc.webview.export.media.MessageID;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HuaweiApiClientImpl extends HuaweiApiClient implements ServiceConnection, InnerApiClient {
    private static final Object a = new Object();
    private final Context b;
    private final String c;
    private String d;
    private String e;
    private volatile e f;
    private String g;
    /* access modifiers changed from: private */
    public WeakReference<Activity> h;
    private WeakReference<Activity> i;
    /* access modifiers changed from: private */
    public boolean j = false;
    /* access modifiers changed from: private */
    public AtomicInteger k = new AtomicInteger(1);
    private List<Scope> l;
    private List<PermissionInfo> m;
    private Map<Api<?>, Api.ApiOptions> n;
    private SubAppInfo o;
    private long p = 0;
    private int q = 0;
    private HuaweiApiClient.ConnectionCallbacks r;
    private HuaweiApiClient.OnConnectionFailedListener s;
    private Handler t = null;
    /* access modifiers changed from: private */
    public CheckUpdatelistener u = null;
    private CheckUpdateCallBack v = new f(this);

    public HuaweiApiClientImpl(Context context) {
        this.b = context;
        this.c = j.a(context);
        this.d = this.c;
        this.e = j.c(context);
    }

    public Context getContext() {
        return this.b;
    }

    public String getPackageName() {
        return this.b.getPackageName();
    }

    public String getAppID() {
        return this.d;
    }

    public String getCpID() {
        return this.e;
    }

    public String getTransportName() {
        return IPCTransport.class.getName();
    }

    public final SubAppInfo getSubAppInfo() {
        return this.o;
    }

    public List<Scope> getScopes() {
        return this.l;
    }

    public void setScopes(List<Scope> list) {
        this.l = list;
    }

    public List<PermissionInfo> getPermissionInfos() {
        return this.m;
    }

    public void setPermissionInfos(List<PermissionInfo> list) {
        this.m = list;
    }

    public Map<Api<?>, Api.ApiOptions> getApiMap() {
        return this.n;
    }

    public void setApiMap(Map<Api<?>, Api.ApiOptions> map) {
        this.n = map;
    }

    public e getService() {
        return this.f;
    }

    public String getSessionId() {
        return this.g;
    }

    public void setHasShowNotice(boolean z) {
        this.j = z;
    }

    public void connect(Activity activity) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "====== HMSSDK version: 20603306 ======");
        int i2 = this.k.get();
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter connect, Connection Status: " + i2);
        if (i2 != 3 && i2 != 5 && i2 != 2 && i2 != 4) {
            this.h = new WeakReference<>(activity);
            this.i = new WeakReference<>(activity);
            this.d = TextUtils.isEmpty(this.c) ? j.a(this.b) : this.c;
            int b2 = b();
            com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "connect minVersion:" + b2);
            HuaweiApiAvailability.setServicesVersionCode(b2);
            int isHuaweiMobileServicesAvailable = HuaweiMobileServicesUtil.isHuaweiMobileServicesAvailable(this.b, b2);
            com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "In connect, isHuaweiMobileServicesAvailable result: " + isHuaweiMobileServicesAvailable);
            this.q = new g(this.b).b(HuaweiApiAvailability.SERVICES_PACKAGE);
            if (isHuaweiMobileServicesAvailable == 0) {
                a(5);
                if (!e()) {
                    a(1);
                    com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "In connect, bind core service fail");
                    a();
                    return;
                }
                f();
            } else if (this.s != null) {
                this.s.onConnectionFailed(new ConnectionResult(isHuaweiMobileServicesAvailable));
            }
        }
    }

    /* access modifiers changed from: private */
    public void a() {
        if (this.s != null) {
            this.s.onConnectionFailed(new ConnectionResult(j.e(this.b) ? 7 : 6));
        }
    }

    private int b() {
        int b2 = j.b(this.b);
        if (b2 != 0 && b2 >= 20503000) {
            return b2;
        }
        int c2 = c();
        if (d()) {
            if (c2 < 20503000) {
                return 20503000;
            }
            return c2;
        } else if (c2 < 20600000) {
            return 20600000;
        } else {
            return c2;
        }
    }

    private int c() {
        Integer num;
        int intValue;
        Map<Api<?>, Api.ApiOptions> apiMap = getApiMap();
        int i2 = 0;
        if (apiMap == null) {
            return 0;
        }
        for (Api<?> apiName : apiMap.keySet()) {
            String apiName2 = apiName.getApiName();
            if (!TextUtils.isEmpty(apiName2) && (num = HuaweiApiAvailability.getApiMap().get(apiName2)) != null && (intValue = num.intValue()) > i2) {
                i2 = intValue;
            }
        }
        return i2;
    }

    private boolean d() {
        if (this.n == null) {
            return false;
        }
        for (Api<?> apiName : this.n.keySet()) {
            if (HuaweiApiAvailability.HMS_API_NAME_GAME.equals(apiName.getApiName())) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void a(int i2) {
        this.k.set(i2);
    }

    private boolean e() {
        Intent intent = new Intent(HuaweiApiAvailability.SERVICES_ACTION);
        intent.setPackage(HuaweiApiAvailability.SERVICES_PACKAGE);
        return this.b.bindService(intent, this, 1);
    }

    private void f() {
        synchronized (a) {
            if (this.t != null) {
                this.t.removeMessages(2);
            } else {
                this.t = new Handler(Looper.getMainLooper(), new g(this));
            }
            this.t.sendEmptyMessageDelayed(2, 5000);
        }
    }

    private void g() {
        synchronized (a) {
            if (this.t != null) {
                this.t.removeMessages(2);
                this.t = null;
            }
        }
    }

    public void disconnect() {
        int i2 = this.k.get();
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter disconnect, Connection Status: " + i2);
        switch (i2) {
            case 2:
                a(4);
                return;
            case 3:
                a(4);
                i();
                return;
            case 5:
                g();
                a(4);
                return;
            default:
                return;
        }
    }

    public boolean isConnected() {
        if (this.q == 0) {
            this.q = new g(this.b).b(HuaweiApiAvailability.SERVICES_PACKAGE);
        }
        if (this.q >= 20504000) {
            return innerIsConnected();
        }
        long currentTimeMillis = System.currentTimeMillis() - this.p;
        if (currentTimeMillis > 0 && currentTimeMillis < 300000) {
            return innerIsConnected();
        }
        if (!innerIsConnected()) {
            return false;
        }
        Status status = com.huawei.hms.support.api.a.a.a((ApiClient) this, new CheckConnectInfo()).awaitOnAnyThread(2000, TimeUnit.MILLISECONDS).getStatus();
        if (status.isSuccess()) {
            this.p = System.currentTimeMillis();
            return true;
        }
        int statusCode = status.getStatusCode();
        com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "isConnected is false, statuscode:" + statusCode);
        if (statusCode == 907135004) {
            return false;
        }
        m();
        a(1);
        this.p = System.currentTimeMillis();
        return false;
    }

    public boolean isConnecting() {
        int i2 = this.k.get();
        return i2 == 2 || i2 == 5;
    }

    public boolean innerIsConnected() {
        return this.k.get() == 3 || this.k.get() == 4;
    }

    public boolean setSubAppInfo(SubAppInfo subAppInfo) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter setSubAppInfo");
        if (subAppInfo == null) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "subAppInfo is null");
            return false;
        }
        String subAppID = subAppInfo.getSubAppID();
        if (TextUtils.isEmpty(subAppID)) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "subAppId is empty");
            return false;
        }
        if (subAppID.equals(TextUtils.isEmpty(this.c) ? j.a(this.b) : this.c)) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "subAppId is host appid");
            return false;
        }
        this.o = new SubAppInfo(subAppInfo);
        return true;
    }

    public void checkUpdate(Activity activity, CheckUpdatelistener checkUpdatelistener) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter checkUpdate");
        if (checkUpdatelistener == null) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "listener is null!");
        } else if (activity == null || activity.isFinishing()) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "checkUpdate, activity is illegal: " + activity);
            checkUpdatelistener.onResult(-1);
        } else {
            this.u = checkUpdatelistener;
            UpdateSdkAPI.checkClientOTAUpdate(activity, this.v, true, 0, true);
            h();
        }
    }

    public void onResume(Activity activity) {
        if (activity != null) {
            com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", UmbrellaConstants.LIFECYCLE_RESUME);
            this.i = new WeakReference<>(activity);
        }
    }

    public void onPause(Activity activity) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", MessageID.onPause);
    }

    public Activity getTopActivity() {
        return (Activity) this.i.get();
    }

    private void h() {
        if (!com.huawei.hms.support.b.a.a().b()) {
            HashMap hashMap = new HashMap();
            hashMap.put(WVConfigManager.CONFIGNAME_PACKAGE, getPackageName());
            hashMap.put("sdk_ver", String.valueOf(HuaweiApiAvailability.HMS_SDK_VERSION_CODE));
            String str = null;
            SubAppInfo subAppInfo = getSubAppInfo();
            if (subAppInfo != null) {
                str = subAppInfo.getSubAppID();
            }
            if (str == null) {
                str = getAppID();
            }
            hashMap.put("app_id", str);
            String[] split = "core.checkUpdate".split("\\.");
            if (split.length == 2) {
                hashMap.put(NotificationCompat.CATEGORY_SERVICE, split[0]);
                hashMap.put("api_name", split[1]);
            }
            hashMap.put("result", "0");
            hashMap.put("cost_time", "0");
            com.huawei.hms.support.b.a.a().a(getContext(), "HMS_SDK_API_CALL", (Map<String, String>) hashMap);
            com.huawei.hms.c.b.a(getContext(), UpdateProvider.getLocalFile(getContext(), "hms/config.txt"), UpdateProvider.getLocalFile(getContext(), "hms/HwMobileServiceReport.txt"), "core.checkUpdate", System.currentTimeMillis(), 0);
        }
    }

    public void setConnectionCallbacks(HuaweiApiClient.ConnectionCallbacks connectionCallbacks) {
        this.r = connectionCallbacks;
    }

    public void setConnectionFailedListener(HuaweiApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.s = onConnectionFailedListener;
    }

    private void i() {
        com.huawei.hms.support.api.a.a.a((ApiClient) this, j()).setResultCallback(new b(this, (f) null));
    }

    private DisconnectInfo j() {
        ArrayList arrayList = new ArrayList();
        if (this.n != null) {
            for (Api<?> apiName : this.n.keySet()) {
                arrayList.add(apiName.getApiName());
            }
        }
        return new DisconnectInfo(this.l, arrayList);
    }

    /* access modifiers changed from: private */
    public void a(ResolveResult<DisconnectResp> resolveResult) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter onDisconnectionResult, disconnect from server result: " + resolveResult.getStatus().getStatusCode());
        m();
        a(1);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter onServiceConnected.");
        g();
        this.f = e.a.a(iBinder);
        if (this.f == null) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "In onServiceConnected, mCoreService must not be null.");
            m();
            a(1);
            if (this.s != null) {
                this.s.onConnectionFailed(new ConnectionResult(10));
            }
        } else if (this.k.get() == 5) {
            a(2);
            k();
        } else if (this.k.get() != 3) {
            m();
        }
    }

    private void k() {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter sendConnectApiServceRequest.");
        com.huawei.hms.support.api.a.a.a((ApiClient) this, l()).setResultCallback(new a(this, (f) null));
    }

    private ConnectInfo l() {
        String c2 = new g(this.b).c(this.b.getPackageName());
        if (c2 == null) {
            c2 = "";
        }
        return new ConnectInfo(getApiNameList(), this.l, c2, this.o == null ? null : this.o.getSubAppID());
    }

    /* access modifiers changed from: private */
    public void b(ResolveResult<ConnectResp> resolveResult) {
        ConnectResp value = resolveResult.getValue();
        if (value != null) {
            this.g = value.sessionId;
        }
        String subAppID = this.o == null ? null : this.o.getSubAppID();
        if (!TextUtils.isEmpty(subAppID)) {
            this.d = subAppID;
        }
        int statusCode = resolveResult.getStatus().getStatusCode();
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter onConnectionResult, connect to server result: " + statusCode);
        if (Status.SUCCESS.equals(resolveResult.getStatus())) {
            if (resolveResult.getValue() != null) {
                ProtocolNegotiate.getInstance().negotiate(resolveResult.getValue().protocolVersion);
            }
            a(3);
            if (this.r != null) {
                this.r.onConnected();
            }
            n();
        } else if (resolveResult.getStatus() == null || resolveResult.getStatus().getStatusCode() != 1001) {
            m();
            a(1);
            if (this.s != null) {
                this.s.onConnectionFailed(new ConnectionResult(statusCode));
            }
        } else {
            m();
            a(1);
            if (this.r != null) {
                this.r.onConnectionSuspended(3);
            }
        }
    }

    private void m() {
        j.a(this.b, (ServiceConnection) this);
    }

    private void n() {
        if (this.j) {
            com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Connect notice has been shown.");
        } else if (HuaweiApiAvailability.getInstance().isHuaweiMobileNoticeAvailable(this.b) == 0) {
            com.huawei.hms.support.api.a.a.a(this, 0, HuaweiApiAvailability.HMS_SDK_VERSION_NAME).setResultCallback(new c(this, (f) null));
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter onServiceDisconnected.");
        this.f = null;
        a(1);
        if (this.r != null) {
            this.r.onConnectionSuspended(1);
        }
    }

    public int asyncRequest(Bundle bundle, String str, int i2, ResultCallback<BundleResult> resultCallback) {
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Enter asyncRequest.");
        if (resultCallback == null || str == null || bundle == null) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "arguments is invalid.");
            return CommonCode.ErrorCode.ARGUMENTS_INVALID;
        } else if (!innerIsConnected()) {
            com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "client is unConnect.");
            return CommonCode.ErrorCode.CLIENT_API_INVALID;
        } else {
            com.huawei.hms.core.aidl.b bVar = new com.huawei.hms.core.aidl.b(str, i2);
            f a2 = com.huawei.hms.core.aidl.a.a(bVar.c());
            bVar.a(bundle);
            RequestHeader requestHeader = new RequestHeader(getAppID(), getPackageName(), HuaweiApiAvailability.HMS_SDK_VERSION_CODE, getSessionId());
            requestHeader.setApiNameList(getApiNameList());
            bVar.b = a2.a((IMessageEntity) requestHeader, new Bundle());
            try {
                getService().a(bVar, new h(this, resultCallback));
                return 0;
            } catch (RemoteException e2) {
                com.huawei.hms.support.log.a.d("HuaweiApiClientImpl", "remote exception:" + e2.getMessage());
                return CommonCode.ErrorCode.INTERNAL_ERROR;
            }
        }
    }

    public List<String> getApiNameList() {
        ArrayList arrayList = new ArrayList();
        if (this.n != null) {
            for (Api<?> apiName : this.n.keySet()) {
                arrayList.add(apiName.getApiName());
            }
        }
        return arrayList;
    }

    private class b implements ResultCallback<ResolveResult<DisconnectResp>> {
        private b() {
        }

        /* synthetic */ b(HuaweiApiClientImpl huaweiApiClientImpl, f fVar) {
            this();
        }

        /* renamed from: a */
        public void onResult(ResolveResult<DisconnectResp> resolveResult) {
            new Handler(Looper.getMainLooper()).post(new j(this, resolveResult));
        }
    }

    private class a implements ResultCallback<ResolveResult<ConnectResp>> {
        private a() {
        }

        /* synthetic */ a(HuaweiApiClientImpl huaweiApiClientImpl, f fVar) {
            this();
        }

        /* renamed from: a */
        public void onResult(ResolveResult<ConnectResp> resolveResult) {
            new Handler(Looper.getMainLooper()).post(new i(this, resolveResult));
        }
    }

    private class c implements ResultCallback<ResolveResult<JosGetNoticeResp>> {
        private c() {
        }

        /* synthetic */ c(HuaweiApiClientImpl huaweiApiClientImpl, f fVar) {
            this();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x000c, code lost:
            r4 = r4.getValue();
         */
        /* renamed from: a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onResult(com.huawei.hms.support.api.ResolveResult<com.huawei.hms.support.api.entity.core.JosGetNoticeResp> r4) {
            /*
                r3 = this;
                if (r4 == 0) goto L_0x004e
                com.huawei.hms.support.api.client.Status r0 = r4.getStatus()
                boolean r0 = r0.isSuccess()
                if (r0 == 0) goto L_0x004e
                java.lang.Object r4 = r4.getValue()
                com.huawei.hms.support.api.entity.core.JosGetNoticeResp r4 = (com.huawei.hms.support.api.entity.core.JosGetNoticeResp) r4
                android.content.Intent r0 = r4.getNoticeIntent()
                if (r0 == 0) goto L_0x004e
                int r4 = r4.getStatusCode()
                if (r4 != 0) goto L_0x004e
                java.lang.String r4 = "HuaweiApiClientImpl"
                java.lang.String r1 = "get notice has intent."
                com.huawei.hms.support.log.a.b(r4, r1)
                com.huawei.hms.api.HuaweiApiClientImpl r4 = com.huawei.hms.api.HuaweiApiClientImpl.this
                java.lang.ref.WeakReference r4 = r4.h
                java.lang.Object r4 = r4.get()
                android.app.Activity r4 = (android.app.Activity) r4
                com.huawei.hms.api.HuaweiApiClientImpl r1 = com.huawei.hms.api.HuaweiApiClientImpl.this
                android.app.Activity r1 = r1.getTopActivity()
                android.app.Activity r4 = com.huawei.hms.c.j.a((android.app.Activity) r4, (android.app.Activity) r1)
                if (r4 != 0) goto L_0x0045
                java.lang.String r4 = "HuaweiApiClientImpl"
                java.lang.String r0 = "showNotice no valid activity!"
                com.huawei.hms.support.log.a.d(r4, r0)
                return
            L_0x0045:
                com.huawei.hms.api.HuaweiApiClientImpl r1 = com.huawei.hms.api.HuaweiApiClientImpl.this
                r2 = 1
                boolean unused = r1.j = r2
                r4.startActivity(r0)
            L_0x004e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.huawei.hms.api.HuaweiApiClientImpl.c.onResult(com.huawei.hms.support.api.ResolveResult):void");
        }
    }
}
