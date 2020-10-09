package android.taobao.windvane.jsbridge.api;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.util.TaoLog;

import com.rd.animation.AbsAnimation;
import com.taobao.android.dinamic.property.DAttrConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class WVMotion extends WVApiPlugin implements Handler.Callback {
    private static final int SHAKE_STOP = 1;
    private static final String TAG = "WVMotion";
    private BlowSensor blowSensor;
    /* access modifiers changed from: private */
    public long currentTime = 0;
    /* access modifiers changed from: private */
    public long currentTime2 = 0;
    /* access modifiers changed from: private */
    public long frequency = 0;
    /* access modifiers changed from: private */
    public long frequency2 = 0;
    private Handler handler = new Handler(Looper.getMainLooper(), this);
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    protected SensorEventListener mSensorListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (9 == sensorEvent.sensor.getType() && WVMotion.this.frequency <= System.currentTimeMillis() - WVMotion.this.currentTime) {
                float[] fArr = sensorEvent.values;
                String str = "{\"x\":\"" + ((-fArr[0]) / 10.0f) + "\",\"y\":\"" + ((-fArr[1]) / 10.0f) + "\",\"z\":\"" + ((-fArr[2]) / 10.0f) + "\"}";
                if (WVMotion.this.mCallback != null) {
                    WVMotion.this.mCallback.fireEvent("motion.gyro", str);
                } else {
                    WVMotion.this.stopListenGyro();
                }
                long unused = WVMotion.this.currentTime = System.currentTimeMillis();
            }
        }
    };
    protected SensorEventListener mSensorListener2 = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (4 == sensorEvent.sensor.getType() && WVMotion.this.frequency2 <= System.currentTimeMillis() - WVMotion.this.currentTime2) {
                float[] fArr = sensorEvent.values;
                String str = "{\"alpha\":\"" + fArr[0] + "\",\"beta\":\"" + fArr[1] + "\",\"gama\":\"" + fArr[2] + "\"}";
                if (WVMotion.this.mCallback != null) {
                    WVMotion.this.mCallback.fireEvent("WV.Event.Motion.RotationRate", str);
                } else {
                    WVMotion.this.stopListenRota();
                }
                long unused = WVMotion.this.currentTime2 = System.currentTimeMillis();
            }
        }
    };
    private ShakeListener mShakeListener = null;
    private SensorManager sm = null;
    private Vibrator vibrator;

    public boolean execute(String str, final String str2, final WVCallBackContext wVCallBackContext) {
        if ("listeningShake".equals(str)) {
            listeningShake(wVCallBackContext, str2);
        } else if ("vibrate".equals(str)) {
            vibrate(wVCallBackContext, str2);
        } else if ("listenBlow".equals(str)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.RECORD_AUDIO"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVMotion.this.listenBlow(wVCallBackContext, str2);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("msg", "NO_PERMISSION");
                        wVCallBackContext.error(wVResult);
                    }
                }).execute();
            } catch (Exception unused) {
            }
        } else if ("stopListenBlow".equals(str)) {
            stopListenBlow(wVCallBackContext, str2);
        } else if ("listenGyro".equals(str)) {
            listenGyro(wVCallBackContext, str2);
        } else if (!"listenRotationRate".equals(str)) {
            return false;
        } else {
            listenRotationRate(wVCallBackContext, str2);
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b5, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void listeningShake(android.taobao.windvane.jsbridge.WVCallBackContext r9, java.lang.String r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x00b6 }
            r0.<init>()     // Catch:{ all -> 0x00b6 }
            r1 = 500(0x1f4, double:2.47E-321)
            boolean r3 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x00b6 }
            r4 = 1
            r5 = 0
            if (r3 != 0) goto L_0x0061
            java.lang.String r1 = "utf-8"
            java.lang.String r1 = java.net.URLDecoder.decode(r10, r1)     // Catch:{ Exception -> 0x0018 }
            r10 = r1
            goto L_0x002f
        L_0x0018:
            java.lang.String r1 = "WVMotion"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b6 }
            r2.<init>()     // Catch:{ all -> 0x00b6 }
            java.lang.String r3 = "listeningShake: param decode error, param="
            r2.append(r3)     // Catch:{ all -> 0x00b6 }
            r2.append(r10)     // Catch:{ all -> 0x00b6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00b6 }
            android.taobao.windvane.util.TaoLog.e(r1, r2)     // Catch:{ all -> 0x00b6 }
            r5 = 1
        L_0x002f:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0041 }
            r1.<init>(r10)     // Catch:{ JSONException -> 0x0041 }
            java.lang.String r2 = "on"
            boolean r2 = r1.getBoolean(r2)     // Catch:{ JSONException -> 0x0041 }
            java.lang.String r3 = "frequency"
            long r6 = r1.optLong(r3)     // Catch:{ JSONException -> 0x0041 }
            goto L_0x0063
        L_0x0041:
            java.lang.String r1 = "WVMotion"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b6 }
            r2.<init>()     // Catch:{ all -> 0x00b6 }
            java.lang.String r3 = "listeningShake: param parse to JSON error, param="
            r2.append(r3)     // Catch:{ all -> 0x00b6 }
            r2.append(r10)     // Catch:{ all -> 0x00b6 }
            java.lang.String r10 = r2.toString()     // Catch:{ all -> 0x00b6 }
            android.taobao.windvane.util.TaoLog.e(r1, r10)     // Catch:{ all -> 0x00b6 }
            java.lang.String r10 = "HY_PARAM_ERR"
            r0.setResult(r10)     // Catch:{ all -> 0x00b6 }
            r9.error((android.taobao.windvane.jsbridge.WVResult) r0)     // Catch:{ all -> 0x00b6 }
            monitor-exit(r8)
            return
        L_0x0061:
            r6 = r1
            r2 = 0
        L_0x0063:
            if (r5 == 0) goto L_0x0077
            boolean r10 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x00b6 }
            if (r10 == 0) goto L_0x0072
            java.lang.String r10 = "WVMotion"
            java.lang.String r1 = "listeningShake: isFail"
            android.taobao.windvane.util.TaoLog.w(r10, r1)     // Catch:{ all -> 0x00b6 }
        L_0x0072:
            r9.error((android.taobao.windvane.jsbridge.WVResult) r0)     // Catch:{ all -> 0x00b6 }
            monitor-exit(r8)
            return
        L_0x0077:
            if (r2 == 0) goto L_0x009b
            java.lang.String r10 = "WVMotion"
            java.lang.String r1 = "listeningShake: start ..."
            android.taobao.windvane.util.TaoLog.d(r10, r1)     // Catch:{ all -> 0x00b6 }
            android.taobao.windvane.jsbridge.api.ShakeListener r10 = r8.mShakeListener     // Catch:{ all -> 0x00b6 }
            if (r10 != 0) goto L_0x008d
            android.taobao.windvane.jsbridge.api.ShakeListener r10 = new android.taobao.windvane.jsbridge.api.ShakeListener     // Catch:{ all -> 0x00b6 }
            android.content.Context r1 = r8.mContext     // Catch:{ all -> 0x00b6 }
            r10.<init>(r1)     // Catch:{ all -> 0x00b6 }
            r8.mShakeListener = r10     // Catch:{ all -> 0x00b6 }
        L_0x008d:
            android.taobao.windvane.jsbridge.api.ShakeListener r10 = r8.mShakeListener     // Catch:{ all -> 0x00b6 }
            android.taobao.windvane.jsbridge.api.WVMotion$MyShakeListener r1 = new android.taobao.windvane.jsbridge.api.WVMotion$MyShakeListener     // Catch:{ all -> 0x00b6 }
            r1.<init>(r9, r6)     // Catch:{ all -> 0x00b6 }
            r10.setOnShakeListener(r1)     // Catch:{ all -> 0x00b6 }
            r9.success((android.taobao.windvane.jsbridge.WVResult) r0)     // Catch:{ all -> 0x00b6 }
            goto L_0x00b4
        L_0x009b:
            java.lang.String r10 = "WVMotion"
            java.lang.String r0 = "listeningShake: stop."
            android.taobao.windvane.util.TaoLog.d(r10, r0)     // Catch:{ all -> 0x00b6 }
            android.os.Message r10 = new android.os.Message     // Catch:{ all -> 0x00b6 }
            r10.<init>()     // Catch:{ all -> 0x00b6 }
            r10.what = r4     // Catch:{ all -> 0x00b6 }
            r10.obj = r9     // Catch:{ all -> 0x00b6 }
            android.os.Handler r9 = r8.handler     // Catch:{ all -> 0x00b6 }
            if (r9 == 0) goto L_0x00b4
            android.os.Handler r9 = r8.handler     // Catch:{ all -> 0x00b6 }
            r9.sendMessage(r10)     // Catch:{ all -> 0x00b6 }
        L_0x00b4:
            monitor-exit(r8)
            return
        L_0x00b6:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVMotion.listeningShake(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }

    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i != 1) {
            switch (i) {
                case 4101:
                    if (!this.isAlive) {
                        return true;
                    }
                    WVResult wVResult = new WVResult();
                    wVResult.setSuccess();
                    wVResult.addData("pass", "1");
                    if (this.mCallback != null) {
                        this.mCallback.fireEvent("motion.blow", wVResult.toJsonString());
                    }
                    return true;
                case 4102:
                    if (this.mCallback != null) {
                        this.mCallback.error(new WVResult());
                    }
                    return true;
                default:
                    return false;
            }
        } else {
            stopShake();
            if (message.obj instanceof WVCallBackContext) {
                ((WVCallBackContext) message.obj).success(new WVResult());
            }
            return true;
        }
    }

    protected class MyShakeListener implements ShakeListener.OnShakeListener {
        private long frequency = 0;
        private long mLastUpdateTime = 0;
        private WVCallBackContext wvCallback = null;

        public MyShakeListener(WVCallBackContext wVCallBackContext, long j) {
            this.wvCallback = wVCallBackContext;
            this.frequency = j;
        }

        public void onShake() {
            if (WVMotion.this.isAlive) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.mLastUpdateTime >= this.frequency) {
                    WVResult wVResult = new WVResult();
                    wVResult.setSuccess();
                    if (this.wvCallback != null) {
                        this.wvCallback.fireEvent("motion.shake", wVResult.toJsonString());
                    }
                    this.mLastUpdateTime = currentTimeMillis;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopListenGyro() {
        if (this.sm != null) {
            if (this.mSensorListener != null) {
                this.sm.unregisterListener(this.mSensorListener);
            }
            this.sm = null;
        }
    }

    /* access modifiers changed from: private */
    public void stopListenRota() {
        if (this.sm != null) {
            if (this.mSensorListener2 != null) {
                this.sm.unregisterListener(this.mSensorListener2);
            }
            this.sm = null;
        }
    }

    private void stopShake() {
        if (this.mShakeListener != null) {
            this.mShakeListener.stop();
            this.mShakeListener = null;
        }
    }

    public void onDestroy() {
        stopShake();
        stopListenGyro();
        stopListenRota();
        if (this.vibrator != null) {
            this.vibrator.cancel();
            this.vibrator = null;
        }
        this.mCallback = null;
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
    }

    @TargetApi(9)
    public void onResume() {
        if (!(this.sm == null || this.mSensorListener == null)) {
            this.sm.registerListener(this.mSensorListener, this.sm.getDefaultSensor(9), 3);
        }
        if (this.mShakeListener != null) {
            this.mShakeListener.resume();
        }
        if (this.blowSensor != null) {
            this.blowSensor.start();
        }
        super.onResume();
    }

    public void onPause() {
        if (!(this.sm == null || this.mSensorListener == null)) {
            this.sm.unregisterListener(this.mSensorListener);
        }
        if (this.mShakeListener != null) {
            this.mShakeListener.pause();
        }
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
        super.onPause();
    }

    public synchronized void vibrate(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            int optInt = new JSONObject(str).optInt("duration", AbsAnimation.DEFAULT_ANIMATION_TIME);
            if (optInt < 0) {
                optInt = AbsAnimation.DEFAULT_ANIMATION_TIME;
            }
            if (this.vibrator == null) {
                this.vibrator = (Vibrator) this.mContext.getSystemService("vibrator");
            }
            this.vibrator.vibrate((long) optInt);
            TaoLog.d("WVMotion", "vibrate: start ...");
            wVCallBackContext.success(new WVResult());
        } catch (JSONException unused) {
            TaoLog.e("WVMotion", "vibrate: param parse to JSON error, param=" + str);
            wVResult.setResult("HY_PARAM_ERR");
            wVCallBackContext.error(wVResult);
        }
    }

    public synchronized void stopListenBlow(WVCallBackContext wVCallBackContext, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "stopListenBlow: stopped. " + str);
        }
        if (this.blowSensor != null) {
            this.blowSensor.stop();
            this.blowSensor = null;
        }
        wVCallBackContext.success(new WVResult());
    }

    public synchronized void listenBlow(WVCallBackContext wVCallBackContext, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "listenBlow: start. " + str);
        }
        this.mCallback = wVCallBackContext;
        if (this.blowSensor != null) {
            this.blowSensor.stop();
        }
        this.blowSensor = new BlowSensor(this.handler);
        this.blowSensor.start();
        wVCallBackContext.success(new WVResult());
    }

    public synchronized void listenRotationRate(WVCallBackContext wVCallBackContext, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "listenRotationRate:  " + str);
        }
        WVResult wVResult = new WVResult();
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.frequency2 = (long) jSONObject.optInt("frequency", 100);
            boolean optBoolean = jSONObject.optBoolean(DAttrConstant.VIEW_EVENT_FLAG);
            this.mCallback = wVCallBackContext;
            if (this.sm == null) {
                this.sm = (SensorManager) this.mContext.getSystemService("sensor");
            }
            if (optBoolean) {
                this.sm.registerListener(this.mSensorListener2, this.sm.getDefaultSensor(4), 3);
                this.currentTime = System.currentTimeMillis();
            } else {
                stopListenRota();
            }
            wVCallBackContext.success(new WVResult());
        } catch (JSONException unused) {
            TaoLog.e("WVMotion", "vibrate: param parse to JSON error, param=" + str);
            wVResult.setResult("HY_PARAM_ERR");
            wVCallBackContext.error(wVResult);
        }
    }

    public synchronized void listenGyro(WVCallBackContext wVCallBackContext, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVMotion", "listenGyro:  " + str);
        }
        WVResult wVResult = new WVResult();
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.frequency = (long) jSONObject.optInt("frequency", 100);
            boolean optBoolean = jSONObject.optBoolean(DAttrConstant.VIEW_EVENT_FLAG);
            this.mCallback = wVCallBackContext;
            if (this.sm == null) {
                this.sm = (SensorManager) this.mContext.getSystemService("sensor");
            }
            if (optBoolean) {
                this.sm.registerListener(this.mSensorListener, this.sm.getDefaultSensor(9), 3);
                this.currentTime = System.currentTimeMillis();
            } else {
                stopListenGyro();
            }
            wVCallBackContext.success(new WVResult());
        } catch (JSONException unused) {
            TaoLog.e("WVMotion", "vibrate: param parse to JSON error, param=" + str);
            wVResult.setResult("HY_PARAM_ERR");
            wVCallBackContext.error(wVResult);
        }
    }
}
