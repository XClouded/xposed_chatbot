package android.taobao.windvane.jsbridge.api;

import android.media.AudioRecord;
import android.os.Handler;

import com.huawei.hms.support.api.entity.core.JosStatusCodes;

import java.util.Timer;
import java.util.TimerTask;

public class BlowSensor {
    public static final int BLOW_HANDLER_BLOWING = 4101;
    public static final int BLOW_HANDLER_FAIL = 4102;
    private int BLOW_ACTIVI = 3000;
    private int SAMPLE_RATE_IN_HZ = JosStatusCodes.RTN_CODE_COMMON_ERROR;
    private AudioRecord ar;
    private int bs = 100;
    private byte[] buffer;
    private Handler mHandler;
    private Timer mTimer;
    private int number = 1;
    private long time = 1;

    public BlowSensor(Handler handler) {
        this.mHandler = handler;
        this.bs = AudioRecord.getMinBufferSize(this.SAMPLE_RATE_IN_HZ, 16, 2);
        this.ar = new AudioRecord(1, this.SAMPLE_RATE_IN_HZ, 16, 2, this.bs);
    }

    /* access modifiers changed from: private */
    public void recordBlow() {
        try {
            this.number++;
            Thread.sleep(8);
            long currentTimeMillis = System.currentTimeMillis();
            int read = this.ar.read(this.buffer, 0, this.bs) + 1;
            int i = 0;
            for (int i2 = 0; i2 < this.buffer.length; i2++) {
                i += this.buffer[i2] * this.buffer[i2];
            }
            int i3 = i / read;
            this.time += System.currentTimeMillis() - currentTimeMillis;
            if ((this.time >= 500 || this.number > 5) && i3 > this.BLOW_ACTIVI) {
                this.mHandler.sendEmptyMessage(4101);
                this.number = 1;
                this.time = 1;
            }
        } catch (Exception unused) {
            this.mHandler.sendEmptyMessage(4102);
            stop();
        }
    }

    public void start() {
        try {
            this.ar.startRecording();
            this.buffer = new byte[this.bs];
            this.mTimer = new Timer("WVBlowTimer");
            this.mTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    BlowSensor.this.recordBlow();
                }
            }, 0, 100);
        } catch (Exception unused) {
            stop();
        }
    }

    public void stop() {
        try {
            if (this.ar != null) {
                this.ar.stop();
                this.ar.release();
                this.bs = 100;
            }
        } catch (Exception unused) {
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }
}
