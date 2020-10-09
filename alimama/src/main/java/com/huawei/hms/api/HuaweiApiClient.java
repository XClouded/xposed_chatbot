package com.huawei.hms.api;

import android.app.Activity;
import android.content.Context;
import com.huawei.hianalytics.v2.HiAnalytics;
import com.huawei.hianalytics.v2.HiAnalyticsConf;
import com.huawei.hms.api.Api;
import com.huawei.hms.c.a;
import com.huawei.hms.c.h;
import com.huawei.hms.c.j;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.SubAppInfo;
import com.huawei.hms.support.api.entity.auth.PermissionInfo;
import com.huawei.hms.support.api.entity.auth.Scope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HuaweiApiClient implements ApiClient {

    public interface ConnectionCallbacks {
        public static final int CAUSE_API_CLIENT_EXPIRED = 3;
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected();

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public abstract void checkUpdate(Activity activity, CheckUpdatelistener checkUpdatelistener);

    public abstract void connect(Activity activity);

    public abstract void disconnect();

    public abstract Activity getTopActivity();

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract void onPause(Activity activity);

    public abstract void onResume(Activity activity);

    public abstract void setConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    public abstract void setConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public abstract boolean setSubAppInfo(SubAppInfo subAppInfo);

    public static final class Builder {
        private final Context a;
        private final List<Scope> b = new ArrayList();
        private final List<PermissionInfo> c = new ArrayList();
        private final Map<Api<?>, Api.ApiOptions> d = new HashMap();
        private OnConnectionFailedListener e;
        private ConnectionCallbacks f;

        public Builder(Context context) throws NullPointerException {
            a.a(context, "context must not be null.");
            this.a = context.getApplicationContext();
            h.a(this.a);
            boolean initFlag = HiAnalytics.getInitFlag();
            com.huawei.hms.support.log.a.b("HMS BI", "Builder->biInitFlag :" + initFlag);
            boolean d2 = j.d(context);
            com.huawei.hms.support.log.a.b("HMS BI", "Builder->biSetting :" + d2);
            if (!initFlag && !d2) {
                new HiAnalyticsConf.Builder(context).setEnableImei(true).setEnableUDID(true).setEnableSN(true).setCollectURL(0, "https://metrics1.data.hicloud.com:6447").create();
            }
        }

        public HuaweiApiClient build() {
            addApi(new Api("Core.API"));
            HuaweiApiClientImpl huaweiApiClientImpl = new HuaweiApiClientImpl(this.a);
            huaweiApiClientImpl.setScopes(this.b);
            huaweiApiClientImpl.setPermissionInfos(this.c);
            huaweiApiClientImpl.setApiMap(this.d);
            huaweiApiClientImpl.setConnectionCallbacks(this.f);
            huaweiApiClientImpl.setConnectionFailedListener(this.e);
            return huaweiApiClientImpl;
        }

        public Builder addConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
            a.a(connectionCallbacks, "listener must not be null.");
            this.f = connectionCallbacks;
            return this;
        }

        public Builder addOnConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
            a.a(onConnectionFailedListener, "listener must not be null.");
            this.e = onConnectionFailedListener;
            return this;
        }

        public Builder addScope(Scope scope) {
            a.a(scope, "scope must not be null.");
            this.b.add(scope);
            return this;
        }

        public Builder addApi(Api<? extends Api.ApiOptions.NotRequiredOptions> api) {
            this.d.put(api, (Object) null);
            if (HuaweiApiAvailability.HMS_API_NAME_GAME.equals(api.getApiName())) {
                com.huawei.hms.support.b.a a2 = com.huawei.hms.support.b.a.a();
                Context applicationContext = this.a.getApplicationContext();
                a2.a(applicationContext, "15060106", "|" + System.currentTimeMillis());
            }
            return this;
        }

        public <O extends Api.ApiOptions.HasOptions> Builder addApi(Api<O> api, O o) {
            a.a(api, "Api must not be null");
            a.a(o, "Null options are not permitted for this Api");
            this.d.put(api, o);
            if (api.getOptions() != null) {
                this.b.addAll(api.getOptions().getScopeList(o));
                this.c.addAll(api.getOptions().getPermissionInfoList(o));
            }
            return this;
        }
    }
}
