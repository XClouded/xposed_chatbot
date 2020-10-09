package com.huawei.hms.support.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.config.WVConfigManager;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.c.b;
import com.huawei.hms.core.aidl.AbstractMessageEntity;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.InnerApiClient;
import com.huawei.hms.support.api.client.InnerPendingResult;
import com.huawei.hms.support.api.client.Result;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.client.SubAppInfo;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.transport.DatagramTransport;
import com.huawei.hms.update.provider.UpdateProvider;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: PendingResultImpl */
public abstract class c<R extends Result, T extends IMessageEntity> extends InnerPendingResult<R> {
    /* access modifiers changed from: private */
    public CountDownLatch a;
    /* access modifiers changed from: private */
    public R b = null;
    private WeakReference<ApiClient> c;
    private String d = null;
    private long e = 0;
    protected DatagramTransport transport = null;

    public abstract R onComplete(T t);

    public c(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        this.d = str;
        a(apiClient, str, iMessageEntity, getResponseType());
    }

    public c(ApiClient apiClient, String str, IMessageEntity iMessageEntity, Class<T> cls) {
        a(apiClient, str, iMessageEntity, cls);
    }

    private void a(ApiClient apiClient, String str, IMessageEntity iMessageEntity, Class<T> cls) {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "init uri:" + str);
        this.d = str;
        if (apiClient == null) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "client is null");
            return;
        }
        this.c = new WeakReference<>(apiClient);
        this.a = new CountDownLatch(1);
        try {
            this.transport = (DatagramTransport) Class.forName(apiClient.getTransportName()).getConstructor(new Class[]{String.class, IMessageEntity.class, Class.class}).newInstance(new Object[]{str, iMessageEntity, cls});
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e2) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "gen transport error:" + e2.getMessage());
            throw new IllegalStateException("Instancing transport exception, " + e2.getMessage(), e2);
        }
    }

    /* access modifiers changed from: protected */
    public Class<T> getResponseType() {
        Type type;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass == null || (type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1]) == null) {
            return null;
        }
        return (Class) type;
    }

    public final R await() {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "await");
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return awaitOnAnyThread();
        }
        com.huawei.hms.support.log.a.d("PendingResultImpl", "await in main thread");
        throw new IllegalStateException("await must not be called on the UI thread");
    }

    public final R awaitOnAnyThread() {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "awaitOnAnyThread");
        this.e = System.currentTimeMillis();
        ApiClient apiClient = (ApiClient) this.c.get();
        if (!checkApiClient(apiClient)) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "client invalid");
            a(CommonCode.ErrorCode.CLIENT_API_INVALID, (IMessageEntity) null);
            return this.b;
        }
        this.transport.send(apiClient, new d(this));
        try {
            this.a.await();
        } catch (InterruptedException unused) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "await in anythread InterruptedException");
            a(CommonCode.ErrorCode.INTERNAL_ERROR, (IMessageEntity) null);
        }
        return this.b;
    }

    public R await(long j, TimeUnit timeUnit) {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "await timeout:" + j + " unit:" + timeUnit.toString());
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return awaitOnAnyThread(j, timeUnit);
        }
        com.huawei.hms.support.log.a.b("PendingResultImpl", "await in main thread");
        throw new IllegalStateException("await must not be called on the UI thread");
    }

    public final R awaitOnAnyThread(long j, TimeUnit timeUnit) {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "awaitOnAnyThread timeout:" + j + " unit:" + timeUnit.toString());
        this.e = System.currentTimeMillis();
        ApiClient apiClient = (ApiClient) this.c.get();
        if (!checkApiClient(apiClient)) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "client invalid");
            a(CommonCode.ErrorCode.CLIENT_API_INVALID, (IMessageEntity) null);
            return this.b;
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        this.transport.post(apiClient, new e(this, atomicBoolean));
        try {
            if (!this.a.await(j, timeUnit)) {
                atomicBoolean.set(true);
                a(CommonCode.ErrorCode.EXECUTE_TIMEOUT, (IMessageEntity) null);
            }
        } catch (InterruptedException unused) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "awaitOnAnyThread InterruptedException");
            a(CommonCode.ErrorCode.INTERNAL_ERROR, (IMessageEntity) null);
        }
        return this.b;
    }

    public final void setResultCallback(ResultCallback<R> resultCallback) {
        setResultCallback(Looper.getMainLooper(), resultCallback);
    }

    public final void setResultCallback(Looper looper, ResultCallback<R> resultCallback) {
        com.huawei.hms.support.log.a.b("PendingResultImpl", "setResultCallback");
        this.e = System.currentTimeMillis();
        if (looper == null) {
            looper = Looper.myLooper();
        }
        a aVar = new a(looper);
        ApiClient apiClient = (ApiClient) this.c.get();
        if (!checkApiClient(apiClient)) {
            com.huawei.hms.support.log.a.d("PendingResultImpl", "client is invalid");
            a(CommonCode.ErrorCode.CLIENT_API_INVALID, (IMessageEntity) null);
            aVar.a(resultCallback, this.b);
            return;
        }
        this.transport.post(apiClient, new f(this, aVar, resultCallback));
    }

    /* access modifiers changed from: private */
    public void a(int i, IMessageEntity iMessageEntity) {
        Status status;
        a(i);
        com.huawei.hms.support.log.a.b("PendingResultImpl", "setResult:" + i);
        Status commonStatus = (iMessageEntity == null || !(iMessageEntity instanceof AbstractMessageEntity)) ? null : ((AbstractMessageEntity) iMessageEntity).getCommonStatus();
        if (i == 0) {
            this.b = onComplete(iMessageEntity);
        } else {
            this.b = onError(i);
        }
        if (this.b != null && (status = this.b.getStatus()) != null && commonStatus != null) {
            int statusCode = status.getStatusCode();
            String statusMessage = status.getStatusMessage();
            int statusCode2 = commonStatus.getStatusCode();
            String statusMessage2 = commonStatus.getStatusMessage();
            if (statusCode != statusCode2) {
                com.huawei.hms.support.log.a.d("PendingResultImpl", "rstStatus code (" + statusCode + ") is not equal commonStatus code (" + statusCode2 + Operators.BRACKET_END_STR);
                com.huawei.hms.support.log.a.d("PendingResultImpl", "rstStatus msg (" + statusMessage + ") is not equal commonStatus msg (" + statusMessage2 + Operators.BRACKET_END_STR);
            } else if (TextUtils.isEmpty(statusMessage) && !TextUtils.isEmpty(statusMessage2)) {
                com.huawei.hms.support.log.a.b("PendingResultImpl", "rstStatus msg (" + statusMessage + ") is not equal commonStatus msg (" + statusMessage2 + Operators.BRACKET_END_STR);
                this.b.setStatus(new Status(statusCode, statusMessage2, status.getResolution()));
            }
        }
    }

    /* access modifiers changed from: protected */
    public R onError(int i) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type type = genericSuperclass != null ? ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0] : null;
        Class a2 = type != null ? com.huawei.hms.support.a.a.a(type) : null;
        if (a2 != null) {
            try {
                this.b = (Result) a2.newInstance();
                this.b.setStatus(new Status(i));
            } catch (Exception e2) {
                com.huawei.hms.support.log.a.d("PendingResultImpl", "on Error:" + e2.getMessage());
                return null;
            }
        }
        return this.b;
    }

    /* access modifiers changed from: protected */
    public boolean checkApiClient(ApiClient apiClient) {
        return apiClient != null && ((InnerApiClient) apiClient).innerIsConnected();
    }

    private void a(int i) {
        ApiClient apiClient;
        if (!com.huawei.hms.support.b.a.a().b() && (apiClient = (ApiClient) this.c.get()) != null && this.d != null && this.e != 0) {
            HashMap hashMap = new HashMap();
            hashMap.put(WVConfigManager.CONFIGNAME_PACKAGE, apiClient.getPackageName());
            hashMap.put("sdk_ver", String.valueOf(HuaweiApiAvailability.HMS_SDK_VERSION_CODE));
            String str = null;
            SubAppInfo subAppInfo = apiClient.getSubAppInfo();
            if (subAppInfo != null) {
                str = subAppInfo.getSubAppID();
            }
            if (str == null) {
                str = apiClient.getAppID();
            }
            hashMap.put("app_id", str);
            String[] split = this.d.split("\\.");
            if (split.length == 2) {
                hashMap.put(NotificationCompat.CATEGORY_SERVICE, split[0]);
                hashMap.put("api_name", split[1]);
            }
            hashMap.put("result", String.valueOf(i));
            hashMap.put("cost_time", String.valueOf(System.currentTimeMillis() - this.e));
            com.huawei.hms.support.b.a.a().a(apiClient.getContext(), "HMS_SDK_API_CALL", (Map<String, String>) hashMap);
            b.a(apiClient.getContext(), UpdateProvider.getLocalFile(apiClient.getContext(), "hms/config.txt"), UpdateProvider.getLocalFile(apiClient.getContext(), "hms/HwMobileServiceReport.txt"), this.d, this.e, i);
        }
    }

    /* compiled from: PendingResultImpl */
    protected static class a<R extends Result> extends Handler {
        public a() {
            this(Looper.getMainLooper());
        }

        public void a(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                Pair pair = (Pair) message.obj;
                b((ResultCallback) pair.first, (Result) pair.second);
            }
        }

        public a(Looper looper) {
            super(looper);
        }

        /* access modifiers changed from: protected */
        public void b(ResultCallback<? super R> resultCallback, R r) {
            resultCallback.onResult(r);
        }
    }
}
