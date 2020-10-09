package android.taobao.windvane.extra.uc;

import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;

import com.taobao.android.nav.Nav;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UCNetworkDelegate implements Handler.Callback {
    public static final int CHANGE_WEBVIEW_URL = 276;
    private static final int RECEIVE_RESPONSE_CODE = 274;
    private static final int REMOVE_WEBVIEW_CODE = 275;
    private static final int SEND_REQUEST_CODE = 273;
    private static final String TAG = "UCNetworkDelegate";
    private static UCNetworkDelegate instance = new UCNetworkDelegate();
    private Handler mHandler = new WVThread("Windvane", (Handler.Callback) this).getHandler();
    private ConcurrentHashMap<WeakReference<WVUCWebView>, String> mWebViewsInfoMap = new ConcurrentHashMap<>();

    public static synchronized UCNetworkDelegate getInstance() {
        UCNetworkDelegate uCNetworkDelegate;
        synchronized (UCNetworkDelegate.class) {
            uCNetworkDelegate = instance;
        }
        return uCNetworkDelegate;
    }

    private UCNetworkDelegate() {
    }

    public ConcurrentHashMap<WeakReference<WVUCWebView>, String> getWebViews() {
        return this.mWebViewsInfoMap;
    }

    public void removeWebview(WVUCWebView wVUCWebView) {
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = REMOVE_WEBVIEW_CODE;
        obtainMessage.obj = wVUCWebView;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onUrlChange(WVUCWebView wVUCWebView, String str) {
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = CHANGE_WEBVIEW_URL;
        ArrayList arrayList = new ArrayList();
        arrayList.add(wVUCWebView);
        arrayList.add(str);
        obtainMessage.obj = arrayList;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onSendRequest(Map<String, String> map, String str) {
        if (this.mWebViewsInfoMap != null && map != null && str != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = SEND_REQUEST_CODE;
            Hashtable hashtable = new Hashtable();
            hashtable.put("url", str);
            String str2 = map.get("Referer");
            hashtable.put(Nav.KExtraReferrer, TextUtils.isEmpty(str2) ? "" : str2);
            hashtable.put("start", String.valueOf(System.currentTimeMillis()));
            obtainMessage.obj = hashtable;
            TaoLog.d(TAG, "onSendRequest : " + str + " Referer: " + str2);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void onFinish(int i, String str) {
        if (this.mWebViewsInfoMap != null && str != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 274;
            Hashtable hashtable = new Hashtable();
            hashtable.put("url", str);
            String valueOf = String.valueOf(i);
            hashtable.put("statusCode", valueOf);
            hashtable.put("end", String.valueOf(System.currentTimeMillis()));
            obtainMessage.obj = hashtable;
            TaoLog.d(TAG, "onFinish : " + str + " statusCode: " + valueOf);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case SEND_REQUEST_CODE /*273*/:
                dealSendRequest(message.obj);
                return false;
            case 274:
                dealReceiveResponse(message.obj);
                return false;
            case REMOVE_WEBVIEW_CODE /*275*/:
                dealRemoveWebView(message.obj);
                break;
            case CHANGE_WEBVIEW_URL /*276*/:
                break;
            default:
                return false;
        }
        dealUrlChange(message.obj);
        return false;
    }

    private void dealRemoveWebView(Object obj) {
        if ((obj instanceof WVUCWebView) && this.mWebViewsInfoMap != null) {
            Enumeration<WeakReference<WVUCWebView>> keys = this.mWebViewsInfoMap.keys();
            while (keys.hasMoreElements()) {
                WeakReference nextElement = keys.nextElement();
                WVUCWebView wVUCWebView = (WVUCWebView) nextElement.get();
                if (wVUCWebView != null && obj.equals(wVUCWebView)) {
                    this.mWebViewsInfoMap.remove(nextElement);
                    return;
                }
            }
        }
    }

    private void dealUrlChange(Object obj) {
        if (obj instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) obj;
            if ((arrayList.get(0) instanceof WVUCWebView) && (arrayList.get(1) instanceof String)) {
                WVUCWebView wVUCWebView = (WVUCWebView) arrayList.get(0);
                String removeHashCode = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme((String) arrayList.get(1)));
                Enumeration<WeakReference<WVUCWebView>> keys = this.mWebViewsInfoMap.keys();
                while (keys.hasMoreElements()) {
                    WeakReference nextElement = keys.nextElement();
                    WVUCWebView wVUCWebView2 = (WVUCWebView) nextElement.get();
                    if (wVUCWebView2 != null && wVUCWebView.equals(wVUCWebView2)) {
                        wVUCWebView2.clearH5MonitorData();
                        this.mWebViewsInfoMap.put(nextElement, removeHashCode);
                        return;
                    }
                }
                this.mWebViewsInfoMap.put(new WeakReference(wVUCWebView), removeHashCode);
            }
        }
    }

    private void dealSendRequest(Object obj) {
        if (obj instanceof Hashtable) {
            Hashtable hashtable = (Hashtable) obj;
            String removeHashCode = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme((String) hashtable.get("url")));
            String removeScheme = WVUrlUtil.removeScheme((String) hashtable.get(Nav.KExtraReferrer));
            if (!TextUtils.isEmpty(removeScheme)) {
                if (this.mWebViewsInfoMap.containsValue(removeScheme)) {
                    Enumeration<WeakReference<WVUCWebView>> keys = this.mWebViewsInfoMap.keys();
                    while (keys.hasMoreElements()) {
                        WeakReference nextElement = keys.nextElement();
                        if (nextElement.get() != null && this.mWebViewsInfoMap.get(nextElement).equals(removeScheme)) {
                            assembleRequestData(hashtable, removeHashCode, removeScheme, nextElement);
                            return;
                        }
                    }
                    return;
                }
                Enumeration<WeakReference<WVUCWebView>> keys2 = this.mWebViewsInfoMap.keys();
                while (keys2.hasMoreElements()) {
                    WeakReference nextElement2 = keys2.nextElement();
                    if (nextElement2.get() != null && ((WVUCWebView) nextElement2.get()).containsH5MonitorData(removeScheme)) {
                        assembleRequestData(hashtable, removeHashCode, removeScheme, nextElement2);
                        return;
                    }
                }
            } else if (this.mWebViewsInfoMap.containsValue(removeHashCode)) {
                Enumeration<WeakReference<WVUCWebView>> keys3 = this.mWebViewsInfoMap.keys();
                while (keys3.hasMoreElements()) {
                    WeakReference nextElement3 = keys3.nextElement();
                    WVUCWebView wVUCWebView = (WVUCWebView) nextElement3.get();
                    if (nextElement3.get() != null && this.mWebViewsInfoMap.get(nextElement3).equals(removeHashCode)) {
                        assembleRequestData(hashtable, removeHashCode, removeScheme, nextElement3);
                        return;
                    }
                }
            }
        }
    }

    private void assembleRequestData(Hashtable<String, String> hashtable, String str, String str2, WeakReference<WVUCWebView> weakReference) {
        WVUCWebView wVUCWebView = (WVUCWebView) weakReference.get();
        if (wVUCWebView != null) {
            wVUCWebView.insertH5MonitorData(str, "url", TextUtils.isEmpty(str) ? "" : str);
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
            wVUCWebView.insertH5MonitorData(str, Nav.KExtraReferrer, str2);
            wVUCWebView.insertH5MonitorData(str, "start", String.valueOf(Long.parseLong(hashtable.get("start")) - wVUCWebView.mPageStart));
        }
    }

    private void dealReceiveResponse(Object obj) {
        if (obj instanceof Hashtable) {
            Hashtable hashtable = (Hashtable) obj;
            String removeHashCode = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme((String) hashtable.get("url")));
            Enumeration<WeakReference<WVUCWebView>> keys = this.mWebViewsInfoMap.keys();
            while (keys.hasMoreElements()) {
                WeakReference nextElement = keys.nextElement();
                String str = this.mWebViewsInfoMap.get(nextElement);
                if (str != null && str.contains(removeHashCode)) {
                    assembleResponseData(hashtable, removeHashCode, nextElement);
                    return;
                }
            }
            Enumeration<WeakReference<WVUCWebView>> keys2 = this.mWebViewsInfoMap.keys();
            while (keys2.hasMoreElements()) {
                WeakReference nextElement2 = keys2.nextElement();
                WVUCWebView wVUCWebView = (WVUCWebView) nextElement2.get();
                if (wVUCWebView != null && wVUCWebView.containsH5MonitorData(removeHashCode)) {
                    assembleResponseData(hashtable, removeHashCode, nextElement2);
                    return;
                }
            }
        }
    }

    private void assembleResponseData(Hashtable<String, String> hashtable, String str, WeakReference<WVUCWebView> weakReference) {
        WVUCWebView wVUCWebView = (WVUCWebView) weakReference.get();
        if (wVUCWebView != null) {
            wVUCWebView.insertH5MonitorData(str, "statusCode", hashtable.get("statusCode"));
            wVUCWebView.insertH5MonitorData(str, "end", String.valueOf(Long.parseLong(hashtable.get("end")) - wVUCWebView.mPageStart));
        }
    }

    public String getBizCodeByUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Enumeration<WeakReference<WVUCWebView>> keys = this.mWebViewsInfoMap.keys();
        while (keys.hasMoreElements()) {
            WeakReference nextElement = keys.nextElement();
            WVUCWebView wVUCWebView = (WVUCWebView) nextElement.get();
            if (wVUCWebView != null && str.equals(WVUrlUtil.removeQueryParam(this.mWebViewsInfoMap.get(nextElement)))) {
                TaoLog.i(TAG, "Get bizCode : " + wVUCWebView.bizCode);
                return wVUCWebView.bizCode;
            }
        }
        return "";
    }
}
