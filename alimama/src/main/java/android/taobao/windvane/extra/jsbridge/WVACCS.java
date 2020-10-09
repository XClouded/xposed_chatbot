package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.taobao.accs.ACCSManager;
import com.taobao.accs.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class WVACCS extends WVApiPlugin {
    private static final String serviceClassName = "android.taobao.windvane.extra.jsbridge.WVACCSService";
    private static final String serviceIdDefault = "windvane";
    private ArrayList<String> serviceIdList = new ArrayList<>();

    public static class ACCSWVEventListener implements WVEventListener {
        private WeakReference<IWVWebView> webview;

        public ACCSWVEventListener(IWVWebView iWVWebView) {
            this.webview = new WeakReference<>(iWVWebView);
        }

        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            IWVWebView iWVWebView = (IWVWebView) this.webview.get();
            if (iWVWebView == null) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.e("ACCS", "webview is recycled");
                }
                return null;
            }
            switch (i) {
                case WVEventId.ACCS_ONDATA /*5001*/:
                    String str = objArr[0];
                    String str2 = new String(objArr[1]);
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("serviceId", str);
                        jSONObject.put("resultData", str2);
                        String jSONObject2 = jSONObject.toString();
                        iWVWebView.fireEvent("WV.Event.ACCS.OnData", jSONObject2);
                        if (TaoLog.getLogStatus()) {
                            TaoLog.i("ACCS", jSONObject2);
                            break;
                        }
                    } catch (Throwable unused) {
                        break;
                    }
                    break;
                case WVEventId.ACCS_ONCONNECTED /*5002*/:
                    iWVWebView.fireEvent("WV.Event.ACCS.OnConnected", "{}");
                    if (TaoLog.getLogStatus()) {
                        TaoLog.e("ACCS", "ACCS connect");
                        break;
                    }
                    break;
                case WVEventId.ACCS_ONDISONNECTED /*5003*/:
                    iWVWebView.fireEvent("WV.Event.ACCS.OnDisConnected", "{}");
                    if (TaoLog.getLogStatus()) {
                        TaoLog.e("ACCS", "ACCS disconnect");
                        break;
                    }
                    break;
            }
            return null;
        }
    }

    public void initialize(Context context, IWVWebView iWVWebView) {
        super.initialize(context, iWVWebView);
        init(context);
    }

    private void init(Context context) {
        WVEventService.getInstance().addEventListener(new ACCSWVEventListener(this.mWebView));
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("bindService".equals(str)) {
            bindService(wVCallBackContext, str2);
            return true;
        } else if ("unBindService".equals(str)) {
            unBindService(wVCallBackContext, str2);
            return true;
        } else if ("setData".equals(str)) {
            setData(wVCallBackContext, str2);
            return true;
        } else if (!"connectionState".equals(str)) {
            return false;
        } else {
            connectionState(wVCallBackContext, str2);
            return true;
        }
    }

    private void bindService(WVCallBackContext wVCallBackContext, String str) {
        String str2;
        try {
            str2 = new JSONObject(str).optString("serviceId", "");
        } catch (JSONException unused) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            str2 = null;
        }
        if (TextUtils.isEmpty(str2)) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            return;
        }
        if (this.serviceIdList == null) {
            try {
                this.serviceIdList = new ArrayList<>();
                this.serviceIdList.add("windvane");
                ACCSManager.registerSerivce(this.mContext.getApplicationContext(), "windvane", serviceClassName);
            } catch (Exception unused2) {
            }
        }
        if (this.serviceIdList.contains(str2)) {
            wVCallBackContext.success();
        } else if (this.mContext != null) {
            this.serviceIdList.add(str2);
            ACCSManager.registerSerivce(this.mContext.getApplicationContext(), str2, serviceClassName);
            wVCallBackContext.success();
        } else {
            wVCallBackContext.error();
        }
    }

    private void unBindService(WVCallBackContext wVCallBackContext, String str) {
        String str2;
        try {
            str2 = new JSONObject(str).optString("serviceId", "");
        } catch (JSONException unused) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            str2 = null;
        }
        if (TextUtils.isEmpty(str2)) {
            wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            return;
        }
        if (this.serviceIdList == null) {
            this.serviceIdList = new ArrayList<>();
        }
        if (!this.serviceIdList.contains(str2)) {
            wVCallBackContext.success();
        } else if (this.mContext != null) {
            this.serviceIdList.remove(str2);
            ACCSManager.unregisterService(this.mContext.getApplicationContext(), str2);
            wVCallBackContext.success();
        } else {
            wVCallBackContext.error();
        }
    }

    private void setData(WVCallBackContext wVCallBackContext, String str) {
        ACCSManager.AccsRequest accsRequest;
        String str2;
        String str3;
        URL url;
        WVCallBackContext wVCallBackContext2 = wVCallBackContext;
        try {
            JSONObject jSONObject = new JSONObject(str);
            str2 = jSONObject.optString("serviceId", "");
            try {
                if (TextUtils.isEmpty(str2)) {
                    WVResult wVResult = new WVResult();
                    wVResult.addData("msg", "serviceId " + str2 + " is not bind!");
                    wVCallBackContext2.error(wVResult);
                    return;
                }
                String optString = jSONObject.optString("userId", "");
                JSONObject optJSONObject = jSONObject.optJSONObject("options");
                str3 = jSONObject.optString("data", "");
                if (optJSONObject == null) {
                    try {
                        wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
                    } catch (JSONException unused) {
                        accsRequest = null;
                        wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
                        wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
                    }
                } else {
                    String optString2 = optJSONObject.optString(Constants.KEY_DATA_ID, "");
                    String optString3 = optJSONObject.optString("host", "");
                    String optString4 = optJSONObject.optString("tag", "");
                    boolean optBoolean = optJSONObject.optBoolean("isUnit", false);
                    int optInt = optJSONObject.optInt("timeout", 0);
                    String optString5 = optJSONObject.optString(Constants.KEY_TARGET, "");
                    String optString6 = optJSONObject.optString(Constants.KEY_BUSINESSID, "");
                    try {
                        url = new URL(optString3);
                    } catch (Exception unused2) {
                        url = null;
                    }
                    accsRequest = new ACCSManager.AccsRequest(optString, str2, str3.getBytes(), optString2, optString5, url, optString6);
                    try {
                        accsRequest.setTag(optString4);
                        accsRequest.setIsUnitBusiness(optBoolean);
                        accsRequest.setTimeOut(optInt);
                    } catch (JSONException unused3) {
                    }
                    if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3) || accsRequest == null) {
                        wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
                    }
                    ACCSManager.sendData(this.mContext, accsRequest);
                    wVCallBackContext.success();
                }
            } catch (JSONException unused4) {
                str3 = null;
                accsRequest = null;
                wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
                if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                }
                wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
            }
        } catch (JSONException unused5) {
            str3 = null;
            str2 = null;
            accsRequest = null;
            wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
            wVCallBackContext2.error(new WVResult("HY_PARAM_ERR"));
        }
    }

    private void connectionState(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            if (ACCSManager.getChannelState(this.mContext) == null) {
                wVResult.addData("status", "false");
                wVCallBackContext.error();
            }
        } catch (Exception unused) {
            wVResult.addData("status", "false");
            wVCallBackContext.error();
        }
        wVResult.addData("status", "true");
        wVCallBackContext.success(wVResult);
    }

    public void onDestroy() {
        if (!(this.mContext == null || this.serviceIdList == null)) {
            for (int i = 0; i < this.serviceIdList.size(); i++) {
                ACCSManager.unregisterService(this.mContext.getApplicationContext(), this.serviceIdList.get(i));
            }
            this.serviceIdList.clear();
            this.serviceIdList = null;
        }
        super.onDestroy();
    }
}
