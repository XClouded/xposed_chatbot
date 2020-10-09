package com.alibaba.aliweex.adapter.module;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.alibaba.aliweex.utils.MemoryMonitor;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;

public class WXDeviceModule extends WXModule {
    private Sensor accelerometerSensor = null;
    float[] accelerometerValues = new float[3];
    /* access modifiers changed from: private */
    public HashMap datasMap = new HashMap();
    /* access modifiers changed from: private */
    public Hashtable<JSCallback, Long> lastUpdateTimes = new Hashtable<>();
    private Sensor magneticFieldSensor = null;
    float[] magneticFieldValues = new float[3];
    private Sensor orientationSensor = null;
    float[] orientationValues = new float[3];
    final SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorEvent sensorEvent2 = sensorEvent;
            if (sensorEvent2.sensor.getType() == 3) {
                System.arraycopy(sensorEvent2.values, 0, WXDeviceModule.this.orientationValues, 0, WXDeviceModule.this.orientationValues.length);
            } else if (sensorEvent2.sensor.getType() == 2) {
                System.arraycopy(sensorEvent2.values, 0, WXDeviceModule.this.magneticFieldValues, 0, WXDeviceModule.this.magneticFieldValues.length);
            } else if (sensorEvent2.sensor.getType() == 1) {
                System.arraycopy(sensorEvent2.values, 0, WXDeviceModule.this.accelerometerValues, 0, WXDeviceModule.this.accelerometerValues.length);
            }
            Enumeration keys = WXDeviceModule.this.successCallbacks.keys();
            while (keys.hasMoreElements()) {
                JSCallback jSCallback = (JSCallback) keys.nextElement();
                if (jSCallback != null) {
                    Double d = (Double) WXDeviceModule.this.successCallbacks.get(jSCallback);
                    long currentTimeMillis = System.currentTimeMillis();
                    long j = 0;
                    if (WXDeviceModule.this.lastUpdateTimes.containsKey(jSCallback)) {
                        j = ((Long) WXDeviceModule.this.lastUpdateTimes.get(jSCallback)).longValue();
                    }
                    if (((double) (currentTimeMillis - j)) >= d.doubleValue()) {
                        WXDeviceModule.this.lastUpdateTimes.put(jSCallback, Long.valueOf(currentTimeMillis));
                        float[] fArr = new float[3];
                        float[] fArr2 = new float[9];
                        SensorManager.getRotationMatrix(fArr2, (float[]) null, WXDeviceModule.this.accelerometerValues, WXDeviceModule.this.magneticFieldValues);
                        SensorManager.getOrientation(fArr2, fArr);
                        SensorManager.getOrientation(fArr2, fArr);
                        double d2 = -Math.toDegrees((double) fArr[0]);
                        while (d2 < 0.0d) {
                            d2 += 360.0d;
                        }
                        double degrees = Math.toDegrees((double) fArr[2]);
                        if (degrees > 90.0d) {
                            degrees -= 180.0d;
                        } else if (degrees < -90.0d) {
                            degrees += 180.0d;
                        }
                        if (d2 != 0.0d || degrees != 0.0d) {
                            WXDeviceModule.this.datasMap.put("alpha", Double.valueOf(d2));
                            WXDeviceModule.this.datasMap.put("beta", Float.valueOf(-WXDeviceModule.this.orientationValues[1]));
                            WXDeviceModule.this.datasMap.put("gamma", Double.valueOf(degrees));
                            jSCallback.invokeAndKeepAlive(WXDeviceModule.this.datasMap);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    };
    private int sensorSpeed = 3;
    private SensorManager sm = null;
    /* access modifiers changed from: private */
    public Hashtable<JSCallback, Double> successCallbacks = new Hashtable<>();

    @JSMethod(uiThread = false)
    public void getLevel(JSCallback jSCallback) {
        String deviceInfo = MemoryMonitor.getDeviceInfo();
        HashMap hashMap = new HashMap(1);
        hashMap.put("summary", deviceInfo);
        jSCallback.invoke(hashMap);
    }

    @JSMethod(uiThread = false)
    public void getMemoryInfo(JSCallback jSCallback) {
        String memoryStatus = MemoryMonitor.getMemoryStatus();
        HashMap hashMap = new HashMap(1);
        hashMap.put("evaluatedStatus", memoryStatus);
        jSCallback.invoke(hashMap);
    }

    @JSMethod(uiThread = false)
    public void watchOrientation(String str, JSCallback jSCallback, JSCallback jSCallback2) {
        double d = 83.0d;
        try {
            double optDouble = new JSONObject(str).optDouble("interval", 83.0d);
            d = 16.7d;
            if (optDouble >= 16.7d) {
                d = optDouble;
            }
        } catch (JSONException unused) {
        }
        if (d < 50.0d) {
            this.sensorSpeed = 0;
        } else if (d < 100.0d) {
            this.sensorSpeed = 1;
        }
        this.sm = (SensorManager) this.mWXSDKInstance.getContext().getSystemService("sensor");
        this.sm.registerListener(this.sensorEventListener, this.sm.getDefaultSensor(-1), 3);
        this.accelerometerSensor = this.sm.getDefaultSensor(1);
        this.magneticFieldSensor = this.sm.getDefaultSensor(2);
        this.orientationSensor = this.sm.getDefaultSensor(3);
        this.sm.registerListener(this.sensorEventListener, this.accelerometerSensor, this.sensorSpeed);
        this.sm.registerListener(this.sensorEventListener, this.magneticFieldSensor, this.sensorSpeed);
        this.sm.registerListener(this.sensorEventListener, this.orientationSensor, this.sensorSpeed);
        if (this.successCallbacks != null) {
            this.successCallbacks.put(jSCallback, Double.valueOf(d));
        }
    }

    @JSMethod(uiThread = false)
    public void stopOrientation(String str) {
        if (this.sm != null) {
            this.sm.unregisterListener(this.sensorEventListener);
            this.sm = null;
        }
        if (this.successCallbacks != null) {
            this.successCallbacks.clear();
        }
        if (this.lastUpdateTimes != null) {
            this.lastUpdateTimes.clear();
        }
    }

    public void onActivityPause() {
        if (this.sm != null) {
            this.sm.unregisterListener(this.sensorEventListener);
        }
        super.onActivityPause();
    }

    public void onActivityResume() {
        if (this.sm != null) {
            this.sm.registerListener(this.sensorEventListener, this.accelerometerSensor, this.sensorSpeed);
            this.sm.registerListener(this.sensorEventListener, this.magneticFieldSensor, this.sensorSpeed);
            this.sm.registerListener(this.sensorEventListener, this.orientationSensor, this.sensorSpeed);
        }
        super.onActivityResume();
    }
}
