package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.alibaba.android.bindingx.core.LogProxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class OrientationDetector implements SensorEventListener {
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_A = Utils.newHashSet(15);
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_B = Utils.newHashSet(11);
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_C = Utils.newHashSet(1, 2);
    private static OrientationDetector sSingleton;
    private static final Object sSingletonLock = new Object();
    private final Set<Integer> mActiveSensors = new HashSet();
    private final Context mAppContext;
    private boolean mDeviceOrientationIsActive;
    private boolean mDeviceOrientationIsActiveWithBackupSensors;
    private Set<Integer> mDeviceOrientationSensors;
    private float[] mDeviceRotationMatrix;
    private Handler mHandler;
    private ArrayList<OnOrientationChangedListener> mListeners = new ArrayList<>();
    private float[] mMagneticFieldVector;
    private boolean mOrientationNotAvailable;
    private final List<Set<Integer>> mOrientationSensorSets;
    private double[] mRotationAngles;
    @VisibleForTesting
    SensorManagerProxy mSensorManagerProxy;
    private HandlerThread mThread;
    private float[] mTruncatedRotationVector;

    interface OnOrientationChangedListener {
        void onOrientationChanged(double d, double d2, double d3);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private OrientationDetector(@NonNull Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mOrientationSensorSets = Utils.newArrayList(DEVICE_ORIENTATION_SENSORS_A, DEVICE_ORIENTATION_SENSORS_B, DEVICE_ORIENTATION_SENSORS_C);
    }

    static OrientationDetector getInstance(Context context) {
        OrientationDetector orientationDetector;
        synchronized (sSingletonLock) {
            if (sSingleton == null) {
                sSingleton = new OrientationDetector(context);
            }
            orientationDetector = sSingleton;
        }
        return orientationDetector;
    }

    /* access modifiers changed from: package-private */
    public void addOrientationChangedListener(@NonNull OnOrientationChangedListener onOrientationChangedListener) {
        if (this.mListeners != null && !this.mListeners.contains(onOrientationChangedListener)) {
            this.mListeners.add(onOrientationChangedListener);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeOrientationChangedListener(@Nullable OnOrientationChangedListener onOrientationChangedListener) {
        if (this.mListeners == null) {
            return false;
        }
        if (onOrientationChangedListener != null) {
            return this.mListeners.remove(onOrientationChangedListener);
        }
        this.mListeners.clear();
        return true;
    }

    private boolean registerOrientationSensorsWithFallback(int i) {
        if (this.mOrientationNotAvailable) {
            return false;
        }
        if (this.mDeviceOrientationSensors != null) {
            String orientationSensorTypeUsed = getOrientationSensorTypeUsed();
            LogProxy.d("[OrientationDetector] register sensor:" + orientationSensorTypeUsed);
            return registerSensors(this.mDeviceOrientationSensors, i, true);
        }
        ensureRotationStructuresAllocated();
        for (Set<Integer> set : this.mOrientationSensorSets) {
            this.mDeviceOrientationSensors = set;
            if (registerSensors(this.mDeviceOrientationSensors, i, true)) {
                String orientationSensorTypeUsed2 = getOrientationSensorTypeUsed();
                LogProxy.d("[OrientationDetector] register sensor:" + orientationSensorTypeUsed2);
                return true;
            }
        }
        this.mOrientationNotAvailable = true;
        this.mDeviceOrientationSensors = null;
        this.mDeviceRotationMatrix = null;
        this.mRotationAngles = null;
        return false;
    }

    private String getOrientationSensorTypeUsed() {
        if (this.mOrientationNotAvailable) {
            return "NOT_AVAILABLE";
        }
        if (this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_A) {
            return "GAME_ROTATION_VECTOR";
        }
        if (this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_B) {
            return "ROTATION_VECTOR";
        }
        return this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_C ? "ACCELEROMETER_MAGNETIC" : "NOT_AVAILABLE";
    }

    public boolean start(int i) {
        LogProxy.d("[OrientationDetector] sensor started");
        boolean registerOrientationSensorsWithFallback = registerOrientationSensorsWithFallback(i);
        if (registerOrientationSensorsWithFallback) {
            setEventTypeActive(true);
        }
        return registerOrientationSensorsWithFallback;
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        LogProxy.d("[OrientationDetector] sensor stopped");
        unregisterSensors(new HashSet(this.mActiveSensors));
        setEventTypeActive(false);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        float[] fArr = sensorEvent.values;
        if (type != 11) {
            if (type != 15) {
                switch (type) {
                    case 1:
                        if (this.mDeviceOrientationIsActiveWithBackupSensors) {
                            getOrientationFromGeomagneticVectors(fArr, this.mMagneticFieldVector);
                            return;
                        }
                        return;
                    case 2:
                        if (this.mDeviceOrientationIsActiveWithBackupSensors) {
                            if (this.mMagneticFieldVector == null) {
                                this.mMagneticFieldVector = new float[3];
                            }
                            System.arraycopy(fArr, 0, this.mMagneticFieldVector, 0, this.mMagneticFieldVector.length);
                            return;
                        }
                        return;
                    default:
                        LogProxy.e("unexpected sensor type:" + type);
                        return;
                }
            } else if (this.mDeviceOrientationIsActive) {
                convertRotationVectorToAngles(fArr, this.mRotationAngles);
                gotOrientation(this.mRotationAngles[0], this.mRotationAngles[1], this.mRotationAngles[2]);
            }
        } else if (this.mDeviceOrientationIsActive && this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_B) {
            convertRotationVectorToAngles(fArr, this.mRotationAngles);
            gotOrientation(this.mRotationAngles[0], this.mRotationAngles[1], this.mRotationAngles[2]);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0104  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static double[] computeDeviceOrientationFromRotationMatrix(float[] r16, double[] r17) {
        /*
            r0 = r16
            int r1 = r0.length
            r2 = 9
            if (r1 == r2) goto L_0x0008
            return r17
        L_0x0008:
            r1 = 8
            r2 = r0[r1]
            r5 = 6
            r6 = 4
            r7 = 2
            r8 = 7
            r9 = 0
            r10 = 0
            r11 = 1
            int r2 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r2 <= 0) goto L_0x003e
            r2 = r0[r11]
            float r2 = -r2
            double r12 = (double) r2
            r2 = r0[r6]
            double r3 = (double) r2
            double r2 = java.lang.Math.atan2(r12, r3)
            r17[r10] = r2
            r2 = r0[r8]
            double r2 = (double) r2
            double r2 = java.lang.Math.asin(r2)
            r17[r11] = r2
            r2 = r0[r5]
            float r2 = -r2
            double r2 = (double) r2
            r0 = r0[r1]
            double r4 = (double) r0
            double r1 = java.lang.Math.atan2(r2, r4)
            r17[r7] = r1
        L_0x003a:
            r1 = 0
            goto L_0x00fe
        L_0x003e:
            r2 = r0[r1]
            int r2 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r2 >= 0) goto L_0x0082
            r2 = r0[r11]
            double r3 = (double) r2
            r2 = r0[r6]
            float r2 = -r2
            double r12 = (double) r2
            double r2 = java.lang.Math.atan2(r3, r12)
            r17[r10] = r2
            r2 = r0[r8]
            double r2 = (double) r2
            double r2 = java.lang.Math.asin(r2)
            double r2 = -r2
            r17[r11] = r2
            r2 = r17[r11]
            r8 = r17[r11]
            r12 = 0
            int r4 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r4 < 0) goto L_0x006b
            r14 = -4609115380302729960(0xc00921fb54442d18, double:-3.141592653589793)
            goto L_0x0070
        L_0x006b:
            r14 = 4614256656552045848(0x400921fb54442d18, double:3.141592653589793)
        L_0x0070:
            r4 = 0
            double r2 = r2 + r14
            r17[r11] = r2
            r2 = r0[r5]
            double r2 = (double) r2
            r0 = r0[r1]
            float r0 = -r0
            double r4 = (double) r0
            double r1 = java.lang.Math.atan2(r2, r4)
            r17[r7] = r1
            goto L_0x003a
        L_0x0082:
            r1 = r0[r5]
            r2 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            int r1 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r1 <= 0) goto L_0x00a6
            r1 = r0[r11]
            float r1 = -r1
            double r4 = (double) r1
            r1 = r0[r6]
            double r12 = (double) r1
            double r4 = java.lang.Math.atan2(r4, r12)
            r17[r10] = r4
            r0 = r0[r8]
            double r4 = (double) r0
            double r4 = java.lang.Math.asin(r4)
            r17[r11] = r4
            r17[r7] = r2
            goto L_0x003a
        L_0x00a6:
            r1 = r0[r5]
            int r1 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r1 >= 0) goto L_0x00e0
            r1 = r0[r11]
            double r4 = (double) r1
            r1 = r0[r6]
            float r1 = -r1
            double r12 = (double) r1
            double r4 = java.lang.Math.atan2(r4, r12)
            r17[r10] = r4
            r0 = r0[r8]
            double r4 = (double) r0
            double r4 = java.lang.Math.asin(r4)
            double r4 = -r4
            r17[r11] = r4
            r4 = r17[r11]
            r8 = r17[r11]
            r12 = 0
            int r0 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r0 < 0) goto L_0x00d3
            r14 = -4609115380302729960(0xc00921fb54442d18, double:-3.141592653589793)
            goto L_0x00d8
        L_0x00d3:
            r14 = 4614256656552045848(0x400921fb54442d18, double:3.141592653589793)
        L_0x00d8:
            r0 = 0
            double r4 = r4 + r14
            r17[r11] = r4
            r17[r7] = r2
            goto L_0x003a
        L_0x00e0:
            r1 = 3
            r1 = r0[r1]
            double r4 = (double) r1
            r1 = r0[r10]
            double r12 = (double) r1
            double r4 = java.lang.Math.atan2(r4, r12)
            r17[r10] = r4
            r0 = r0[r8]
            int r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r0 <= 0) goto L_0x00f8
            r2 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
        L_0x00f8:
            r17[r11] = r2
            r1 = 0
            r17[r7] = r1
        L_0x00fe:
            r3 = r17[r10]
            int r0 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r0 >= 0) goto L_0x010e
            r1 = r17[r10]
            r3 = 4618760256179416344(0x401921fb54442d18, double:6.283185307179586)
            double r1 = r1 + r3
            r17[r10] = r1
        L_0x010e:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.internal.OrientationDetector.computeDeviceOrientationFromRotationMatrix(float[], double[]):double[]");
    }

    private void convertRotationVectorToAngles(float[] fArr, double[] dArr) {
        if (fArr.length > 4) {
            System.arraycopy(fArr, 0, this.mTruncatedRotationVector, 0, 4);
            SensorManager.getRotationMatrixFromVector(this.mDeviceRotationMatrix, this.mTruncatedRotationVector);
        } else {
            SensorManager.getRotationMatrixFromVector(this.mDeviceRotationMatrix, fArr);
        }
        computeDeviceOrientationFromRotationMatrix(this.mDeviceRotationMatrix, dArr);
        for (int i = 0; i < 3; i++) {
            dArr[i] = Math.toDegrees(dArr[i]);
        }
    }

    private void getOrientationFromGeomagneticVectors(float[] fArr, float[] fArr2) {
        if (fArr != null && fArr2 != null && SensorManager.getRotationMatrix(this.mDeviceRotationMatrix, (float[]) null, fArr, fArr2)) {
            computeDeviceOrientationFromRotationMatrix(this.mDeviceRotationMatrix, this.mRotationAngles);
            gotOrientation(Math.toDegrees(this.mRotationAngles[0]), Math.toDegrees(this.mRotationAngles[1]), Math.toDegrees(this.mRotationAngles[2]));
        }
    }

    private SensorManagerProxy getSensorManagerProxy() {
        if (this.mSensorManagerProxy != null) {
            return this.mSensorManagerProxy;
        }
        SensorManager sensorManager = (SensorManager) this.mAppContext.getSystemService("sensor");
        if (sensorManager != null) {
            this.mSensorManagerProxy = new SensorManagerProxyImpl(sensorManager);
        }
        return this.mSensorManagerProxy;
    }

    private void setEventTypeActive(boolean z) {
        this.mDeviceOrientationIsActive = z;
        this.mDeviceOrientationIsActiveWithBackupSensors = z && this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_C;
    }

    private void ensureRotationStructuresAllocated() {
        if (this.mDeviceRotationMatrix == null) {
            this.mDeviceRotationMatrix = new float[9];
        }
        if (this.mRotationAngles == null) {
            this.mRotationAngles = new double[3];
        }
        if (this.mTruncatedRotationVector == null) {
            this.mTruncatedRotationVector = new float[4];
        }
    }

    private boolean registerSensors(Set<Integer> set, int i, boolean z) {
        HashSet<Integer> hashSet = new HashSet<>(set);
        hashSet.removeAll(this.mActiveSensors);
        if (hashSet.isEmpty()) {
            return true;
        }
        boolean z2 = false;
        for (Integer num : hashSet) {
            boolean registerForSensorType = registerForSensorType(num.intValue(), i);
            if (!registerForSensorType && z) {
                unregisterSensors(hashSet);
                return false;
            } else if (registerForSensorType) {
                this.mActiveSensors.add(num);
                z2 = true;
            }
        }
        return z2;
    }

    private void unregisterSensors(Iterable<Integer> iterable) {
        for (Integer next : iterable) {
            if (this.mActiveSensors.contains(next)) {
                getSensorManagerProxy().unregisterListener(this, next.intValue());
                this.mActiveSensors.remove(next);
            }
        }
    }

    private boolean registerForSensorType(int i, int i2) {
        SensorManagerProxy sensorManagerProxy = getSensorManagerProxy();
        if (sensorManagerProxy == null) {
            return false;
        }
        return sensorManagerProxy.registerListener(this, i, i2, getHandler());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void gotOrientation(double d, double d2, double d3) {
        if (this.mListeners != null) {
            try {
                Iterator<OnOrientationChangedListener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onOrientationChanged(d, d2, d3);
                }
            } catch (Throwable th) {
                LogProxy.e("[OrientationDetector] ", th);
            }
        }
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            this.mThread = new HandlerThread("DeviceOrientation");
            this.mThread.start();
            this.mHandler = new Handler(this.mThread.getLooper());
        }
        return this.mHandler;
    }
}
