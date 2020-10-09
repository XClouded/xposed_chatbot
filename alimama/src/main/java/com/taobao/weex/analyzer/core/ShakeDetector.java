package com.taobao.weex.analyzer.core;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.IPermissionHandler;

public class ShakeDetector implements SensorEventListener, IPermissionHandler {
    private static final int MAGNITUDE_THRESHOLD = 25;
    private static final int MAX_SAMPLES = 25;
    private static final int MIN_TIME_BETWEEN_SAMPLES_MS = 20;
    private static final int PERCENT_OVER_THRESHOLD_FOR_SHAKE = 66;
    private static final int VISIBLE_TIME_RANGE_MS = 500;
    private Config mConfig;
    private int mCurrentIndex;
    private long mLastTimestamp;
    @Nullable
    private double[] mMagnitudes;
    @Nullable
    private SensorManager mSensorManager;
    private ShakeListener mShakeListener;
    @Nullable
    private long[] mTimestamps;

    public interface ShakeListener {
        void onShake();
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public ShakeDetector(@Nullable ShakeListener shakeListener, @Nullable Config config) {
        this.mShakeListener = shakeListener;
        this.mConfig = config;
    }

    public void start(@Nullable SensorManager sensorManager) {
        Sensor defaultSensor;
        if (sensorManager == null) {
            return;
        }
        if ((this.mConfig == null || isPermissionGranted(this.mConfig)) && (defaultSensor = sensorManager.getDefaultSensor(1)) != null) {
            this.mSensorManager = sensorManager;
            this.mLastTimestamp = -1;
            this.mCurrentIndex = 0;
            this.mMagnitudes = new double[25];
            this.mTimestamps = new long[25];
            this.mSensorManager.registerListener(this, defaultSensor, 2);
        }
    }

    public void stop() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
            this.mSensorManager = null;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mTimestamps != null && this.mMagnitudes != null && sensorEvent.timestamp - this.mLastTimestamp >= 20) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            this.mLastTimestamp = sensorEvent.timestamp;
            this.mTimestamps[this.mCurrentIndex] = sensorEvent.timestamp;
            this.mMagnitudes[this.mCurrentIndex] = Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
            maybeDispatchShake(sensorEvent.timestamp);
            this.mCurrentIndex = (this.mCurrentIndex + 1) % 25;
        }
    }

    private void maybeDispatchShake(long j) {
        if (this.mTimestamps != null && this.mMagnitudes != null) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < 25; i3++) {
                int i4 = ((this.mCurrentIndex - i3) + 25) % 25;
                if (j - this.mTimestamps[i4] < 500) {
                    i2++;
                    if (this.mMagnitudes[i4] >= 25.0d) {
                        i++;
                    }
                }
            }
            double d = (double) i;
            double d2 = (double) i2;
            Double.isNaN(d);
            Double.isNaN(d2);
            if (d / d2 > 0.66d && this.mShakeListener != null) {
                this.mShakeListener.onShake();
            }
        }
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return config.isEnableShake();
    }
}
