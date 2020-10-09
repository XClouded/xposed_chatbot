package com.taobao.weex.analyzer.core.logcat.ats;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.analyzer.core.logcat.ats.ATSMessageReceiver;
import com.taobao.weex.analyzer.core.logcat.ats.UploadManager;
import com.taobao.weex.utils.WXLogUtils;

public class ATSUploadService extends Service implements UploadManager.Callback {
    private static final String COMMAND_START_UPLOAD = "start_upload";
    private static final String COMMAND_STOP_UPLOAD = "stop_upload";
    private static final String COMMAND_SYNC_STATE = "sync_state";
    private static final String PARAM_COMMAND = "cmd";
    private static final String PARAM_WS_URL = "url";
    static final String STATE_BEFORE_CONNECT = "before_connect";
    static final String STATE_BEFORE_DISCONNECT = "before_disconnect";
    static final String STATE_BEFORE_UPLOAD_LOG = "before_upload_log";
    static final String STATE_CLOSE = "close";
    static final String STATE_ERROR = "error";
    static final String STATE_ON_OPEN = "open";
    static final String STATE_RECEIVE_OSS_URL = "receive_oss_url";
    static final String STATE_UPLOAD_LOG = "upload_log";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public UploadManager mUploadManager;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        WXLogUtils.w("weex-analyzer", "ats service is created");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        WXLogUtils.w("weex-analyzer", "[ATSUploadService] ats service will start");
        String stringExtra = intent.getStringExtra("cmd");
        String stringExtra2 = intent.getStringExtra("url");
        if (COMMAND_START_UPLOAD.equals(stringExtra)) {
            if (this.mUploadManager != null && this.mUploadManager.isConnected()) {
                this.mUploadManager.disconnect();
            }
            if (this.mUploadManager == null) {
                this.mUploadManager = UploadManager.create();
            }
            if (!TextUtils.isEmpty(stringExtra2)) {
                WXLogUtils.d("weex-analyzer", "[ATSUploadService] try connect to: " + stringExtra2);
                this.mUploadManager.connect(stringExtra2, this);
                return 2;
            }
            WXLogUtils.d("weex-analyzer", "[ATSUploadService] empty web socket url");
            return 2;
        } else if (COMMAND_STOP_UPLOAD.equals(stringExtra)) {
            if (this.mUploadManager == null || !this.mUploadManager.isConnected() || !this.mUploadManager.isUploading()) {
                return 2;
            }
            this.mUploadManager.stopUpload();
            return 2;
        } else if (!COMMAND_SYNC_STATE.equals(stringExtra) || this.mUploadManager == null || !this.mUploadManager.isConnected() || !this.mUploadManager.isUploading()) {
            return 2;
        } else {
            ATSMessageReceiver.ATSMessage aTSMessage = new ATSMessageReceiver.ATSMessage(STATE_UPLOAD_LOG, (String) null);
            aTSMessage.count = this.mUploadManager.getCount();
            sendMessage(aTSMessage);
            return 2;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mUploadManager != null) {
            this.mUploadManager.stopUpload();
            this.mUploadManager.disconnect();
            this.mUploadManager = null;
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        WXLogUtils.w("weex-analyzer", "[ATSUploadService] ats service is destroyed");
    }

    private void sendMessage(@NonNull ATSMessageReceiver.ATSMessage aTSMessage) {
        String str = aTSMessage.message;
        String str2 = aTSMessage.state;
        int i = aTSMessage.count;
        Intent intent = new Intent("action_ats_message");
        intent.putExtra("message", str);
        intent.putExtra("state", str2);
        intent.putExtra("count", i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static void syncState(@NonNull Context context) {
        Intent intent = new Intent(context, ATSUploadService.class);
        intent.putExtra("cmd", COMMAND_SYNC_STATE);
        context.startService(intent);
    }

    public static void startAndThenUpload(@NonNull Context context, @Nullable String str) {
        Intent intent = new Intent(context, ATSUploadService.class);
        intent.putExtra("url", str);
        intent.putExtra("cmd", COMMAND_START_UPLOAD);
        context.startService(intent);
    }

    public static void stopUpload(@NonNull Context context) {
        Intent intent = new Intent(context, ATSUploadService.class);
        intent.putExtra("cmd", COMMAND_STOP_UPLOAD);
        context.startService(intent);
    }

    public static void stop(@NonNull Context context) {
        context.stopService(new Intent(context, ATSUploadService.class));
    }

    public void onBeforeConnect() {
        sendMessage(new ATSMessageReceiver.ATSMessage(STATE_BEFORE_CONNECT, (String) null));
    }

    public void onBeforeDisconnect() {
        sendMessage(new ATSMessageReceiver.ATSMessage(STATE_BEFORE_DISCONNECT, (String) null));
    }

    public void onOpen() {
        sendMessage(new ATSMessageReceiver.ATSMessage("open", (String) null));
        if (this.mUploadManager != null) {
            this.mUploadManager.startUpload();
        }
    }

    public void onReceivedOSSUrl(String str) {
        sendMessage(new ATSMessageReceiver.ATSMessage(STATE_RECEIVE_OSS_URL, str));
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (ATSUploadService.this.mUploadManager != null) {
                    ATSUploadService.this.mUploadManager.disconnect();
                }
            }
        }, 2000);
    }

    public void onBeforeUpload() {
        sendMessage(new ATSMessageReceiver.ATSMessage(STATE_BEFORE_UPLOAD_LOG, (String) null));
    }

    public void onUploadLog(int i, String str) {
        ATSMessageReceiver.ATSMessage aTSMessage = new ATSMessageReceiver.ATSMessage(STATE_UPLOAD_LOG, str);
        aTSMessage.count = i;
        sendMessage(aTSMessage);
    }

    public void onClose(int i, String str) {
        sendMessage(new ATSMessageReceiver.ATSMessage("close", str));
    }

    public void onError(String str) {
        sendMessage(new ATSMessageReceiver.ATSMessage("error", str));
    }
}
