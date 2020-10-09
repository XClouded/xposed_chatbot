package android.taobao.windvane.extra.uc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.webkit.ValueCallback;

import androidx.annotation.Keep;

import com.taobao.accs.ACCSManager;
import com.taobao.orange.OConstant;
import com.uc.webview.export.extension.UCCore;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Keep
public class WVWebPushService implements WVEventListener {
    private static final String TAG = "WVWebPushService";
    private static WVWebPushService mInstance = null;
    private static final String serviceClassName = "android.taobao.windvane.extra.jsbridge.WVACCSService";
    private static final String swServiceId = "sw-push";
    private Context mContext = null;
    private Handler mMessageHandler = null;
    private List<String> mResultDataList = null;

    @Keep
    public static WVWebPushService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WVWebPushService(context);
            mInstance.init();
        }
        return mInstance;
    }

    private void init() {
        WVEventService.getInstance().addEventListener(this);
        this.mResultDataList = new ArrayList();
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "WVEventService.getInstance().addEventListener");
        }
    }

    WVWebPushService(Context context) {
        this.mContext = context;
        try {
            ACCSManager.registerSerivce(this.mContext.getApplicationContext(), swServiceId, serviceClassName);
            if (TaoLog.getLogStatus()) {
                TaoLog.i(TAG, "ACCSManager.registerSerivce");
            }
        } catch (Exception e) {
            TaoLog.e(TAG, e.getStackTrace().toString());
        }
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        switch (i) {
            case WVEventId.ACCS_ONDATA /*5001*/:
                String str = objArr[0];
                String str2 = new String(objArr[1]);
                try {
                    TaoLog.i(TAG, "WVWebPushService, " + str + ": " + str2);
                    if (!swServiceId.equalsIgnoreCase(str)) {
                        return null;
                    }
                    this.mResultDataList.add(str2);
                    sendServiceWorkderPushMessage();
                    return null;
                } catch (Throwable th) {
                    TaoLog.e(TAG, "WVWebPushService,  onEvent: " + th.getMessage());
                    return null;
                }
            case WVEventId.ACCS_ONCONNECTED /*5002*/:
                if (!TaoLog.getLogStatus()) {
                    return null;
                }
                TaoLog.i(TAG, "ACCS connect");
                return null;
            case WVEventId.ACCS_ONDISONNECTED /*5003*/:
                if (!TaoLog.getLogStatus()) {
                    return null;
                }
                TaoLog.i(TAG, "ACCS disconnect");
                return null;
            default:
                return null;
        }
    }

    private void sendServiceWorkderPushMessage() {
        if (this.mResultDataList.size() > 0) {
            if (this.mMessageHandler == null) {
                this.mMessageHandler = new Handler();
            }
            this.mMessageHandler.postDelayed(new Runnable() {
                public void run() {
                    WVWebPushService.this.doSendServiceWorkderPushMessage();
                }
            }, 2000);
        }
    }

    public void doSendServiceWorkderPushMessage() {
        String remove;
        try {
            if (this.mResultDataList.size() <= 0 || (remove = this.mResultDataList.remove(0)) == null) {
                return;
            }
            if (remove.length() > 0) {
                WVUCWebView wVUCWebView = new WVUCWebView(this.mContext);
                if (wVUCWebView.getCurrentViewCoreType() == 3) {
                    JSONObject jSONObject = new JSONObject(remove);
                    String optString = jSONObject.optString("workerId");
                    String optString2 = jSONObject.optString("data");
                    Bundle bundle = new Bundle();
                    bundle.putString("appId", optString);
                    bundle.putString("message", optString2);
                    bundle.putString("messageId", System.currentTimeMillis() + "");
                    if (TaoLog.getLogStatus()) {
                        TaoLog.i(TAG, "WVWebPushService, send message to uc core: " + bundle + ", uc core type: " + wVUCWebView.getCurrentViewCoreType());
                    }
                    UCCore.notifyCoreEvent(2, bundle, new ValueCallback<Object>() {
                        public void onReceiveValue(Object obj) {
                            if (obj != null) {
                                Bundle bundle = (Bundle) obj;
                                String string = bundle.getString("appId");
                                String string2 = bundle.getString("messageId");
                                String string3 = bundle.getString("result");
                                TaoLog.i(WVWebPushService.TAG, "sendServiceWorkerPushMessage onReceiveValue appid=" + string + ", messageId=" + string2 + ", result=" + string3);
                            }
                        }
                    });
                }
                wVUCWebView.destroy();
            }
        } catch (Throwable th) {
            TaoLog.e(TAG, "WVWebPushService,  onEvent: " + th.getMessage());
        }
    }

    public String getUtdidBySdk() {
        try {
            Method declaredMethod = Class.forName(OConstant.REFLECT_UTDID).getDeclaredMethod("getUtdid", new Class[]{Context.class});
            declaredMethod.setAccessible(true);
            String str = (String) declaredMethod.invoke((Object) null, new Object[]{this.mContext});
            TaoLog.i(TAG, "getUtdidBySdk utdid: " + str);
            return str;
        } catch (Throwable th) {
            TaoLog.e(TAG, "getUtdidBySdk cd exception : " + th);
            return null;
        }
    }
}
