package android.taobao.windvane.extra.jsbridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.WindVaneSDKForTB;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.HttpConnector;
import android.taobao.windvane.connect.HttpRequest;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.WVApiWrapper;
import android.taobao.windvane.extra.mtop.MtopApiAdapter;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WindVaneInterface;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.thread.LockObject;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.widget.Toast;

import com.taobao.orange.OConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WVServer extends WVApiPlugin implements Handler.Callback {
    public static final String API_SERVER = "WVServer";
    private static final int NOTIFY_RESULT = 500;
    private static final int NOT_REG_LOGIN = 510;
    static boolean NeedApiLock = false;
    private static final String TAG = "WVServer";
    static long lastlocktime;
    static long notiTime;
    private boolean isUserLogin;
    /* access modifiers changed from: private */
    public Object jsContext;
    /* access modifiers changed from: private */
    public final Object lockLock;
    /* access modifiers changed from: private */
    public LinkedBlockingQueue<LockObject> lockQueue;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public String mParams;
    /* access modifiers changed from: private */
    public boolean needLock;
    private ExecutorService singleExecutor;

    public WVServer() {
        this.mHandler = null;
        this.singleExecutor = Executors.newSingleThreadExecutor();
        this.lockQueue = new LinkedBlockingQueue<>();
        this.lockLock = new Object();
        this.jsContext = null;
        this.mParams = null;
        this.needLock = false;
        this.isUserLogin = false;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public boolean isLock() {
        return this.needLock;
    }

    public void setLock(boolean z) {
        this.needLock = z;
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        try {
            String url = wVCallBackContext.getWebview().getUrl();
            AppMonitorUtil.commitOffMonitor(url, "WVServer:" + str2, OConstant.CODE_POINT_EXP_BIND_SERVICE);
        } catch (Throwable unused) {
        }
        if (EnvUtil.isAppDebug()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - notiTime > 3600000) {
                notiTime = currentTimeMillis;
                if (this.mContext instanceof Activity) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                    builder.setMessage("因安全原因，lib-mtop.js 需升级至1.5.0以上，WVServer接口已废弃，请使用MtopWVPlugin。 详询 ：益零");
                    builder.setTitle("警告(仅debug版本会弹出)");
                    builder.setCancelable(true);
                    builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        }
        if (!"send".equals(str)) {
            return false;
        }
        if (!NeedApiLock || System.currentTimeMillis() - lastlocktime >= 5000) {
            NeedApiLock = false;
            send(wVCallBackContext, str2);
            return true;
        }
        Toast.makeText(this.mContext, "哎呦喂，被挤爆啦，请稍后重试", 1).show();
        return true;
    }

    @WindVaneInterface
    public void send(Object obj, String str) {
        this.singleExecutor.execute(new ServerRequestTask(obj, str));
    }

    private class ServerRequestTask implements Runnable {
        private Object context;
        private String params;

        public ServerRequestTask(Object obj, String str) {
            this.context = obj;
            this.params = str;
        }

        public void run() {
            LockObject lockObject;
            ServerParams access$000 = WVServer.this.parseParams(this.params);
            if (access$000 == null) {
                MtopResult mtopResult = new MtopResult(this.context);
                mtopResult.addData("ret", new JSONArray().put("HY_PARAM_ERR"));
                WVServer.this.callResult(mtopResult);
                return;
            }
            if (WVServer.this.needLock) {
                boolean z = false;
                synchronized (WVServer.this.lockLock) {
                    int size = WVServer.this.lockQueue.size();
                    lockObject = (LockObject) WVServer.this.lockQueue.peek();
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "queue size: " + size + " lock: " + lockObject);
                    }
                    if (WVServer.this.lockQueue.offer(new LockObject()) && size > 0) {
                        z = true;
                    }
                }
                if (z && lockObject != null) {
                    lockObject.lwait();
                }
            }
            String unused = WVServer.this.mParams = this.params;
            Object unused2 = WVServer.this.jsContext = this.context;
            HttpRequest access$700 = WVServer.this.wrapRequest(access$000);
            if (access$700 == null) {
                TaoLog.w("WVServer", "HttpRequest is null, and do nothing");
                return;
            }
            WVServer.this.parseResult(this.context, new HttpConnector().syncConnect(access$700));
        }
    }

    /* access modifiers changed from: private */
    public ServerParams parseParams(String str) {
        try {
            ServerParams serverParams = new ServerParams();
            JSONObject jSONObject = new JSONObject(str);
            serverParams.api = jSONObject.getString("api");
            serverParams.v = jSONObject.optString("v", "*");
            boolean z = false;
            serverParams.post = jSONObject.optInt("post", 0) != 0;
            serverParams.ecode = jSONObject.optInt("ecode", 0) != 0;
            if (jSONObject.optInt("isSec", 1) != 0) {
                z = true;
            }
            serverParams.isSec = z;
            JSONObject optJSONObject = jSONObject.optJSONObject("param");
            if (optJSONObject != null) {
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    serverParams.addData(next, optJSONObject.getString(next));
                }
            }
            return serverParams;
        } catch (JSONException unused) {
            TaoLog.e("WVServer", "parseParams error, param=" + str);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public HttpRequest wrapRequest(ServerParams serverParams) {
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.addParam("api", serverParams.api);
        apiRequest.addParam("v", serverParams.v);
        String str = null;
        if (WindVaneSDKForTB.wvAdapter == null) {
            TaoLog.w("WVServer", "wrapRequest wvAdapter is not exist.");
            if (serverParams.ecode) {
                this.mHandler.sendEmptyMessage(NOT_REG_LOGIN);
                return null;
            }
        } else {
            this.isUserLogin = false;
            Map<String, String> loginInfo = WindVaneSDKForTB.wvAdapter.getLoginInfo(this.mHandler);
            if (serverParams.ecode) {
                if (loginInfo == null) {
                    TaoLog.w("WVServer", "wrapRequest loginInfo is null.");
                } else {
                    apiRequest.addParam("sid", loginInfo.get("sid"));
                    apiRequest.addParam("ecode", loginInfo.get("ecode"));
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "login info, sid: " + loginInfo.get("sid") + " ecode: " + loginInfo.get("ecode"));
                    }
                }
            } else if (loginInfo != null) {
                apiRequest.addParam("sid", loginInfo.get("sid"));
            }
        }
        apiRequest.addDataParams(serverParams.getData());
        String mtopUrl = GlobalConfig.getMtopUrl();
        if (serverParams.isSec) {
            apiRequest.setSec(true);
            str = WVApiWrapper.formatBody(apiRequest, MtopApiAdapter.class);
        } else if (serverParams.post) {
            str = WVApiWrapper.formatBody(apiRequest, MtopApiAdapter.class);
        } else {
            mtopUrl = WVApiWrapper.formatUrl(apiRequest, MtopApiAdapter.class);
        }
        HttpRequest httpRequest = new HttpRequest(mtopUrl);
        httpRequest.setRedirect(false);
        if (str != null) {
            httpRequest.setMethod("POST");
            try {
                httpRequest.setPostData(str.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return httpRequest;
    }

    /* access modifiers changed from: private */
    public void parseResult(Object obj, HttpResponse httpResponse) {
        MtopResult mtopResult = new MtopResult(obj);
        mtopResult.addData("ret", new JSONArray().put("HY_FAILED"));
        mtopResult.addData("code", String.valueOf(httpResponse.getHttpCode()));
        if (!httpResponse.isSuccess() || httpResponse.getData() == null) {
            TaoLog.d("WVServer", "parseResult: request illegal, response is null");
            int httpCode = httpResponse.getHttpCode();
            if (httpCode == 420 || httpCode == 499 || httpCode == 599) {
                lastlocktime = System.currentTimeMillis();
                NeedApiLock = true;
                if (this.mHandler != null) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(WVServer.this.mContext, "哎呦喂，被挤爆啦，请稍后重试", 1).show();
                        }
                    });
                }
            } else if (httpCode >= 410 && httpCode <= 419) {
                Map<String, String> headers = httpResponse.getHeaders();
                String str = "http://h5.m.taobao.com/";
                if (headers != null && headers.containsKey("location")) {
                    str = headers.get("location");
                }
                Intent intent = new Intent();
                intent.setData(Uri.parse(str));
                intent.setPackage(this.mContext.getPackageName());
                try {
                    this.mContext.startActivity(intent);
                    if (this.mHandler != null) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(WVServer.this.mContext, " 哎呦喂，被挤爆啦，请稍后重试", 1).show();
                            }
                        });
                    }
                } catch (Exception unused) {
                }
            }
            callResult(mtopResult);
            return;
        }
        try {
            String str2 = new String(httpResponse.getData(), "utf-8");
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVServer", "parseResult: content=" + str2);
            }
            try {
                JSONObject jSONObject = new JSONObject(str2);
                jSONObject.put("code", String.valueOf(httpResponse.getHttpCode()));
                JSONArray jSONArray = jSONObject.getJSONArray("ret");
                int length = jSONArray.length();
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String string = jSONArray.getString(i);
                    if (string.startsWith("SUCCESS")) {
                        mtopResult.setSuccess(true);
                        break;
                    } else if (!string.startsWith("ERR_SID_INVALID")) {
                        i++;
                    } else if (WindVaneSDKForTB.wvAdapter != null) {
                        this.isUserLogin = true;
                        WindVaneSDKForTB.wvAdapter.login(this.mHandler);
                        return;
                    }
                }
                mtopResult.setData(jSONObject);
                callResult(mtopResult);
            } catch (Exception unused2) {
                TaoLog.e("WVServer", "parseResult mtop response parse fail, content: " + str2);
                callResult(mtopResult);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callResult(mtopResult);
        }
    }

    /* access modifiers changed from: private */
    public void callResult(MtopResult mtopResult) {
        Message obtain = Message.obtain();
        obtain.what = 500;
        obtain.obj = mtopResult;
        this.mHandler.sendMessage(obtain);
    }

    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 500) {
            if (message.obj instanceof MtopResult) {
                MtopResult mtopResult = (MtopResult) message.obj;
                if (mtopResult.isSuccess()) {
                    if (mtopResult.getJsContext() instanceof WVCallBackContext) {
                        ((WVCallBackContext) mtopResult.getJsContext()).success(mtopResult.toString());
                    }
                } else if (mtopResult.getJsContext() instanceof WVCallBackContext) {
                    ((WVCallBackContext) mtopResult.getJsContext()).error(mtopResult.toString());
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("WVServer", "call result, retString: " + mtopResult.toString());
                }
            }
            notifyNext();
            return true;
        } else if (i != NOT_REG_LOGIN) {
            switch (i) {
                case 0:
                    if (this.isUserLogin) {
                        MtopResult mtopResult2 = new MtopResult();
                        mtopResult2.addData("ret", new JSONArray().put("ERR_SID_INVALID"));
                        if (this.jsContext instanceof WVCallBackContext) {
                            ((WVCallBackContext) this.jsContext).error(mtopResult2.toString());
                        }
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d("WVServer", "login fail, call result, " + mtopResult2.toString());
                        }
                        this.isUserLogin = false;
                    }
                    notifyNext();
                    return true;
                case 1:
                    notifyNext();
                    this.isUserLogin = false;
                    this.singleExecutor.execute(new ServerRequestTask(this.jsContext, this.mParams));
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "login success, execute task, mParams:" + this.mParams);
                    }
                    return true;
                default:
                    return false;
            }
        } else {
            MtopResult mtopResult3 = new MtopResult();
            mtopResult3.addData("ret", new JSONArray().put("HY_FAILED"));
            mtopResult3.addData("code", "-1");
            if (this.jsContext instanceof WVCallBackContext) {
                ((WVCallBackContext) this.jsContext).error(mtopResult3.toString());
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVServer", "not reg login, call fail, " + mtopResult3.toString());
            }
            notifyNext();
            return true;
        }
    }

    private void notifyNext() {
        LockObject poll;
        if (this.needLock) {
            synchronized (this.lockLock) {
                poll = this.lockQueue.poll();
            }
            if (poll != null) {
                poll.lnotify();
            }
        }
    }

    public void onDestroy() {
        this.lockQueue.clear();
        this.jsContext = null;
    }

    private class MtopResult {
        private Object jsContext = null;
        private JSONObject result = new JSONObject();
        private boolean success = false;

        public MtopResult() {
        }

        public MtopResult(Object obj) {
            this.jsContext = obj;
        }

        public void setData(JSONObject jSONObject) {
            if (jSONObject != null) {
                this.result = jSONObject;
            }
        }

        public void addData(String str, String str2) {
            if (str != null && str2 != null) {
                try {
                    this.result.put(str, str2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void addData(String str, JSONArray jSONArray) {
            if (str != null && jSONArray != null) {
                try {
                    this.result.put(str, jSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isSuccess() {
            return this.success;
        }

        public void setSuccess(boolean z) {
            this.success = z;
        }

        public Object getJsContext() {
            return this.jsContext;
        }

        public String toString() {
            return this.result.toString();
        }
    }
}
