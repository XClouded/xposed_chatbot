package com.taobao.login4android.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.text.TextUtils;
import com.ali.user.mobile.app.constant.UTConstant;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.idst.nls.internal.codec.OupsCodec;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.login4android.login.LoginController;
import com.taobao.login4android.video.UploadTask;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import java.util.Properties;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyJsbridge extends WVApiPlugin {
    /* access modifiers changed from: private */
    public String TAG = "loginsdk.JSBridgeService";
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    private BroadcastReceiver mLoginReceiver;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("checkNoise".equals(str)) {
            checkNoise(str2, wVCallBackContext);
            return true;
        } else if ("startRecord".equals(str)) {
            startRecord(str2, wVCallBackContext);
            return true;
        } else if ("stopRecord".equals(str)) {
            stopRecord(str2, wVCallBackContext);
            return true;
        } else if ("testOups".equals(str)) {
            testOups(str2, wVCallBackContext);
            return true;
        } else if ("testUploader".equals(str)) {
            testUploader(str2, wVCallBackContext);
            return true;
        } else if (!"aluApplyIVToken".equals(str)) {
            return false;
        } else {
            applyIVToken(str2, wVCallBackContext);
            return true;
        }
    }

    private synchronized void applyIVToken(String str, final WVCallBackContext wVCallBackContext) {
        this.mCallback = wVCallBackContext;
        try {
            String string = new JSONObject(str).getString("actionType");
            if (this.mWebView != null) {
                try {
                    String host = Uri.parse(this.mWebView.getUrl()).getHost();
                    if (DataProviderFactory.getDataProvider().getEnvType() == LoginEnvType.ONLINE.getSdkEnvType() && !host.endsWith(".taobao.com") && !host.endsWith(".tmall.com")) {
                        ivErrorCallback(wVCallBackContext, -3, "invalid host");
                        return;
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            if (this.mLoginReceiver == null) {
                this.mLoginReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if (intent != null) {
                            switch (AnonymousClass9.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.valueOf(intent.getAction()).ordinal()]) {
                                case 1:
                                    String stringExtra = intent.getStringExtra("token");
                                    if (!TextUtils.isEmpty(stringExtra)) {
                                        WVResult wVResult = new WVResult();
                                        wVResult.setResult(WVResult.SUCCESS);
                                        wVResult.addData("token", stringExtra);
                                        if (VerifyJsbridge.this.mCallback != null) {
                                            VerifyJsbridge.this.mCallback.success(wVResult);
                                            return;
                                        }
                                        return;
                                    }
                                    VerifyJsbridge.this.ivErrorCallback(wVCallBackContext, -2, "empty token");
                                    return;
                                case 2:
                                    VerifyJsbridge.this.ivErrorCallback(wVCallBackContext, intent.getIntExtra("errorCode", 1100), intent.getStringExtra("message"));
                                    return;
                                default:
                                    return;
                            }
                        }
                    }
                };
                LoginBroadcastHelper.registerLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            }
            LoginController.getInstance().navToIVByScene(DataProviderFactory.getApplicationContext(), string, DataProviderFactory.getDataProvider().getSite());
        } catch (Exception unused) {
            ivErrorCallback(wVCallBackContext, -1, "error param");
        }
        return;
    }

    /* renamed from: com.taobao.login4android.video.VerifyJsbridge$9  reason: invalid class name */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                com.taobao.login4android.broadcast.LoginAction[] r0 = com.taobao.login4android.broadcast.LoginAction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction = r0
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_IV_SUCCESS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$taobao$login4android$broadcast$LoginAction     // Catch:{ NoSuchFieldError -> 0x001f }
                com.taobao.login4android.broadcast.LoginAction r1 = com.taobao.login4android.broadcast.LoginAction.NOTIFY_IV_FAIL     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.video.VerifyJsbridge.AnonymousClass9.<clinit>():void");
        }
    }

    public void onDestroy() {
        if (this.mLoginReceiver != null) {
            LoginBroadcastHelper.unregisterLoginReceiver(DataProviderFactory.getApplicationContext(), this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
        this.mCallback = null;
        super.onDestroy();
    }

    private synchronized void testUploader(String str, final WVCallBackContext wVCallBackContext) {
        try {
            final WVResult wVResult = new WVResult();
            UploadTask.getInstance().setResultCallback(new UploadTask.ResultCallback() {
                public void onSuccess(String str) {
                    wVResult.setResult(WVResult.SUCCESS);
                    wVResult.addData("url", str);
                    wVCallBackContext.success(wVResult);
                }

                public void onFail(String str) {
                    wVResult.setResult("HY_FAILED");
                    wVResult.addData("msg", str);
                    wVCallBackContext.error(wVResult);
                }
            });
            UploadTask.getInstance().uploadAsync(DataProviderFactory.getApplicationContext(), "/storage/emulated/0/records/20170510102205", "a/b");
        } catch (Throwable th) {
            th.printStackTrace();
            WVResult wVResult2 = new WVResult();
            wVResult2.setResult("HY_FAILED");
            wVResult2.addData(AgooConstants.MESSAGE_FLAG, (Object) th);
            wVCallBackContext.error(wVResult2);
        }
        return;
    }

    private synchronized void testOups(String str, WVCallBackContext wVCallBackContext) {
        try {
            boolean isOpen = new OupsCodec().isOpen();
            WVResult wVResult = new WVResult();
            wVResult.addData(AgooConstants.MESSAGE_FLAG, (Object) Boolean.valueOf(isOpen));
            wVResult.setResult(WVResult.SUCCESS);
            wVCallBackContext.success(wVResult);
        } catch (Throwable th) {
            WVResult wVResult2 = new WVResult();
            wVResult2.setResult("HY_FAILED");
            wVResult2.addData(AgooConstants.MESSAGE_FLAG, (Object) th);
            wVCallBackContext.error(wVResult2);
        }
        return;
    }

    private synchronized void checkNoise(final String str, final WVCallBackContext wVCallBackContext) {
        try {
            PermissionProposer.buildPermissionTask(wVCallBackContext.getWebview().getContext(), new String[]{"android.permission.RECORD_AUDIO"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    try {
                        JSONObject jSONObject = new JSONObject(str);
                        VerifyJsbridge.this.testRecordNoise(wVCallBackContext, jSONObject.getInt("checkSeconds"), jSONObject.getInt("maxVolume"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        VerifyJsbridge.this.errorCallback(wVCallBackContext, UTConstant.VERIFY_CHECKNOISE, RecordErrorCode.E_UNKOWN);
                    }
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                    VerifyJsbridge.this.errorCallback(wVCallBackContext, UTConstant.VERIFY_CHECKNOISE, RecordErrorCode.E_NO_PERMISSION);
                }
            }).execute();
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback(wVCallBackContext, UTConstant.VERIFY_CHECKNOISE, RecordErrorCode.E_UNKOWN);
        }
        return;
    }

    /* access modifiers changed from: private */
    public void successCallback(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        wVResult.setResult(WVResult.SUCCESS);
        wVResult.addData(WXDebugConstants.ENV_DEVICE_MODEL, Build.MODEL);
        wVCallBackContext.success(wVResult);
        Properties properties = new Properties();
        properties.put("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
        if (!TextUtils.isEmpty(str)) {
            UserTrackAdapter.sendUT(str, properties);
        }
    }

    /* access modifiers changed from: private */
    public void errorCallback(WVCallBackContext wVCallBackContext, String str, int i) {
        WVResult wVResult = new WVResult();
        wVResult.setResult("HY_FAILED");
        wVResult.addData("code", (Object) Integer.valueOf(i));
        wVResult.addData(WXDebugConstants.ENV_DEVICE_MODEL, Build.MODEL);
        wVCallBackContext.error(wVResult);
        Properties properties = new Properties();
        properties.put("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
        properties.put("code", Integer.valueOf(i));
        if (!TextUtils.isEmpty(str)) {
            UserTrackAdapter.sendUT(str, properties);
        }
    }

    /* access modifiers changed from: private */
    public void ivErrorCallback(WVCallBackContext wVCallBackContext, int i, String str) {
        WVResult wVResult = new WVResult();
        wVResult.setResult("HY_FAILED");
        wVResult.addData("code", (Object) Integer.valueOf(i));
        wVResult.addData("message", str);
        wVCallBackContext.error(wVResult);
    }

    /* access modifiers changed from: private */
    public void testRecordNoise(WVCallBackContext wVCallBackContext, int i, int i2) {
        int startRecordAndCheckNoise = AudioRecordFunc.getInstance().startRecordAndCheckNoise();
        if (1000 == startRecordAndCheckNoise || 1002 == startRecordAndCheckNoise) {
            final int i3 = i2;
            final WVCallBackContext wVCallBackContext2 = wVCallBackContext;
            new CountDownTimer((long) (i * 1000), 500) {
                public void onTick(long j) {
                }

                public void onFinish() {
                    AudioRecordFunc.getInstance().stopRecordAndFile();
                    if (AudioRecordFunc.getInstance().getMaxVolume() < ((double) i3)) {
                        VerifyJsbridge.this.successCallback(wVCallBackContext2, UTConstant.VERIFY_CHECKNOISE);
                    } else {
                        VerifyJsbridge.this.errorCallback(wVCallBackContext2, UTConstant.VERIFY_CHECKNOISE, RecordErrorCode.E_NOISY);
                    }
                }
            }.start();
            return;
        }
        errorCallback(wVCallBackContext, UTConstant.VERIFY_CHECKNOISE, startRecordAndCheckNoise);
    }

    private synchronized void startRecord(final String str, final WVCallBackContext wVCallBackContext) {
        try {
            PermissionProposer.buildPermissionTask(wVCallBackContext.getWebview().getContext(), new String[]{"android.permission.RECORD_AUDIO"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    try {
                        JSONObject jSONObject = new JSONObject(str);
                        int i = jSONObject.getInt("maxSeconds");
                        int i2 = jSONObject.getInt("minSeconds");
                        AudioRecordFunc instance = AudioRecordFunc.getInstance();
                        instance.setMaxRecordSeconds(i);
                        instance.setmMinRecordSeconds(i2);
                        instance.startRecordAndFile();
                        VerifyJsbridge.this.successCallback(wVCallBackContext, UTConstant.VERIFY_STARTRECORD);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        VerifyJsbridge.this.errorCallback(wVCallBackContext, UTConstant.VERIFY_STARTRECORD, RecordErrorCode.E_UNKOWN);
                    }
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                    VerifyJsbridge.this.errorCallback(wVCallBackContext, UTConstant.VERIFY_STARTRECORD, RecordErrorCode.E_NO_PERMISSION);
                }
            }).execute();
        } catch (Exception unused) {
            errorCallback(wVCallBackContext, UTConstant.VERIFY_STARTRECORD, RecordErrorCode.E_UNKOWN);
        }
        return;
    }

    public void onPause() {
        super.onPause();
        try {
            AudioRecordFunc.getInstance().stopRecordAndFile();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private synchronized void stopRecord(String str, WVCallBackContext wVCallBackContext) {
        try {
            final long currentTimeMillis = System.currentTimeMillis();
            AudioRecordFunc instance = AudioRecordFunc.getInstance();
            if (instance.isClosedForLimit()) {
                errorCallback(wVCallBackContext, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_15_SECONDS_LIMMIT);
                AudioRecordFunc.getInstance().stopRecordAndFile();
                return;
            }
            instance.stopRecordAndFile();
            long recordTime = instance.getRecordTime();
            if (recordTime > ((long) instance.getMaxRecordSeconds())) {
                errorCallback(wVCallBackContext, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_15_SECONDS_LIMMIT);
                return;
            } else if (recordTime < ((long) instance.getMinRecordSeconds())) {
                errorCallback(wVCallBackContext, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_1_SECONDS_LIMIT);
                return;
            } else {
                String audioName = instance.getAudioName();
                if (audioName != null) {
                    final WVResult wVResult = new WVResult();
                    final WVCallBackContext wVCallBackContext2 = wVCallBackContext;
                    UploadTask.getInstance().setResultCallback(new UploadTask.ResultCallback() {
                        public void onSuccess(String str) {
                            wVResult.setResult(WVResult.SUCCESS);
                            wVResult.addData("voiceUrl", str);
                            wVResult.addData(WXDebugConstants.ENV_DEVICE_MODEL, Build.MODEL);
                            wVCallBackContext2.success(wVResult);
                            Properties properties = new Properties();
                            properties.put("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
                            UserTrackAdapter.sendUT(UTConstant.VERIFY_STOPRECORD, properties);
                            long currentTimeMillis = System.currentTimeMillis();
                            String access$500 = VerifyJsbridge.this.TAG;
                            TLogAdapter.e(access$500, "upload time=" + (currentTimeMillis - currentTimeMillis));
                        }

                        public void onFail(String str) {
                            String access$500 = VerifyJsbridge.this.TAG;
                            TLogAdapter.e(access$500, "msg=" + str);
                            VerifyJsbridge.this.errorCallback(wVCallBackContext2, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_UPLOAD_FAIL);
                        }
                    });
                    UploadTask.getInstance().uploadAsync(DataProviderFactory.getApplicationContext(), audioName, "a/b");
                } else {
                    errorCallback(wVCallBackContext, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_UNKOWN);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
            errorCallback(wVCallBackContext, UTConstant.VERIFY_STOPRECORD, RecordErrorCode.E_UNKOWN);
        }
        return;
    }
}
