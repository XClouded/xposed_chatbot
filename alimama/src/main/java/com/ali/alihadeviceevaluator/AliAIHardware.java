package com.ali.alihadeviceevaluator;

import android.util.Log;
import com.ali.alihadeviceevaluator.AliHardwareInitializer;
import com.ali.alihadeviceevaluator.network.RemoteDeviceManager;
import com.ali.alihadeviceevaluator.util.Global;
import com.ali.alihadeviceevaluator.util.KVStorageUtils;

class AliAIHardware implements RemoteDeviceManager.DataCaptureListener {
    public static final String K_LAST_TIMESTAMP = "lasttimestamp";
    public static final String K_SCORE = "score";
    public static final String K_SWITCH = "switch";
    public static final String K_VALID_PERIOD = "validperiod";
    private static final long QUERY_DELAY_DURATION = 5000;
    AliHardwareInitializer.HardwareListener deviceListener;
    /* access modifiers changed from: private */
    public volatile float deviceScore = -1.0f;
    private volatile boolean isPending = false;
    private volatile float localDeviceScore = -1.0f;
    /* access modifiers changed from: private */
    public float removeDeviceScore = -1.0f;

    AliAIHardware() {
    }

    public AliAIHardware setHardwareListener(AliHardwareInitializer.HardwareListener hardwareListener) {
        this.deviceListener = hardwareListener;
        return this;
    }

    public void start() {
        loadDeviceLevel();
    }

    public float getDeviceScore() {
        if (this.deviceScore != -1.0f) {
            return this.deviceScore;
        }
        if (this.localDeviceScore != -1.0f) {
            return this.localDeviceScore;
        }
        return -1.0f;
    }

    private boolean loadFromStorage() {
        if (!KVStorageUtils.getSP().contains(K_SCORE)) {
            return false;
        }
        this.localDeviceScore = KVStorageUtils.getSP().getFloat(K_SCORE, 100.0f);
        return true;
    }

    public static int getDeviceLevel(float f) {
        if (!KVStorageUtils.getSP().getBoolean("switch", true)) {
            return -3;
        }
        if (f >= 80.0f) {
            return 0;
        }
        if (f >= 20.0f) {
            return 1;
        }
        return f >= 0.0f ? 2 : -2;
    }

    private void loadDeviceLevel() {
        loadFromStorage();
        if (isLocalScoreValid()) {
            Log.d(Global.TAG, "load ai score from local. score = " + this.localDeviceScore);
            this.deviceScore = this.localDeviceScore;
            notifyListener(this.deviceScore);
            return;
        }
        Global.handler.postDelayed(new Runnable() {
            public void run() {
                AliAIHardware.this.fetchDeviceScore();
            }
        }, QUERY_DELAY_DURATION);
    }

    private boolean isLocalScoreValid() {
        long j;
        if (!KVStorageUtils.getSP().contains(K_SCORE) || !KVStorageUtils.getSP().contains(K_LAST_TIMESTAMP)) {
            return false;
        }
        if (!KVStorageUtils.getSP().contains(K_VALID_PERIOD)) {
            j = 24;
        } else {
            j = KVStorageUtils.getSP().getLong(K_VALID_PERIOD, 0);
        }
        if (System.currentTimeMillis() < KVStorageUtils.getSP().getLong(K_LAST_TIMESTAMP, 0) + Global.hour2Ms(j)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void fetchDeviceScore() {
        if (!isLocalScoreValid() && !this.isPending) {
            Log.d(Global.TAG, "score request");
            new RemoteDeviceManager(this).fetchData(getDeviceScore());
            this.isPending = true;
        }
    }

    public void onReceive(final float f) {
        Log.d(Global.TAG, "load ai score from remote. score = " + f);
        this.isPending = false;
        Global.handler.post(new Runnable() {
            public void run() {
                if (f > 0.0f && f <= 100.0f) {
                    float unused = AliAIHardware.this.removeDeviceScore = f;
                    float unused2 = AliAIHardware.this.deviceScore = AliAIHardware.this.removeDeviceScore;
                    AliAIHardware.this.notifyListener(AliAIHardware.this.deviceScore);
                    KVStorageUtils.getEditor().putLong(AliAIHardware.K_LAST_TIMESTAMP, System.currentTimeMillis());
                    KVStorageUtils.getEditor().putFloat(AliAIHardware.K_SCORE, f);
                    KVStorageUtils.getEditor().commit();
                }
            }
        });
    }

    public void onFailed() {
        Log.e(Global.TAG, "load ai score from remote failed!!!");
        if (this.localDeviceScore != -1.0f) {
            notifyListener(this.localDeviceScore);
        } else {
            notifyListener(100.0f);
        }
        this.isPending = false;
    }

    /* access modifiers changed from: private */
    public void notifyListener(float f) {
        if (this.deviceListener != null) {
            this.deviceListener.onDeviceLevelChanged(getDeviceLevel(f), (float) ((int) f));
        }
    }

    public void onForeGround() {
        if (!isLocalScoreValid()) {
            Global.handler.postDelayed(new Runnable() {
                public void run() {
                    AliAIHardware.this.fetchDeviceScore();
                }
            }, QUERY_DELAY_DURATION);
        }
    }
}
