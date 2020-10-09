package com.taobao.login4android.video;

import android.media.AudioRecord;
import android.os.CountDownTimer;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordFunc {
    public static final int FRAME_SIZE = 640;
    public static final int MAX_DB = 70;
    public static final String TAG = "login.AudioRecordFunc";
    private static AudioRecordFunc mInstance;
    private String audioName;
    private AudioRecord audioRecord;
    private int bufferSizeInBytes = 0;
    private boolean closedForLimit = false;
    private CountDownTimer countDownTimer = new CountDownTimer(15000, 500) {
        public void onTick(long j) {
        }

        public void onFinish() {
            AudioRecordFunc.getInstance().closeForLimit();
        }
    };
    private String folderPath = (DataProviderFactory.getApplicationContext().getCacheDir().getPath() + "/records/");
    private boolean isRecord = false;
    private Object mLock = new Object();
    private int mMaxRecordSeconds = 15;
    private int mMinRecordSeconds = 1;
    private long mStartRecordTime;
    private long mStopRecordTime;
    private double maxVolume = 0.0d;

    private AudioRecordFunc() {
        File file = new File(this.folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static synchronized AudioRecordFunc getInstance() {
        AudioRecordFunc audioRecordFunc;
        synchronized (AudioRecordFunc.class) {
            if (mInstance == null) {
                mInstance = new AudioRecordFunc();
            }
            audioRecordFunc = mInstance;
        }
        return audioRecordFunc;
    }

    public int startRecordAndFile() {
        this.closedForLimit = false;
        this.countDownTimer.start();
        if (this.isRecord) {
            return 1002;
        }
        try {
            if (this.audioRecord == null) {
                creatAudioRecord();
            }
            this.audioRecord.startRecording();
            this.isRecord = true;
            new CoordinatorWrapper().execute(new AudioRecordThread());
            return 1000;
        } catch (Throwable th) {
            th.printStackTrace();
            return RecordErrorCode.E_UNKOWN;
        }
    }

    public int startRecordAndCheckNoise() {
        this.maxVolume = 0.0d;
        this.closedForLimit = false;
        if (this.isRecord) {
            return 1002;
        }
        try {
            if (this.audioRecord == null) {
                creatAudioRecord();
            }
            this.audioRecord.startRecording();
            this.isRecord = true;
            new CoordinatorWrapper().execute(new CheckAudioRecordNoise());
            return 1000;
        } catch (Throwable th) {
            th.printStackTrace();
            return RecordErrorCode.E_UNKOWN;
        }
    }

    public void stopRecordAndFile() {
        this.mStopRecordTime = System.currentTimeMillis();
        close();
        this.closedForLimit = false;
    }

    public void closeForLimit() {
        close();
        this.closedForLimit = true;
    }

    public String getAudioName() {
        return this.audioName;
    }

    public double getMaxVolume() {
        return this.maxVolume;
    }

    private void close() {
        if (this.audioRecord != null) {
            this.isRecord = false;
            try {
                this.audioRecord.stop();
                this.audioRecord.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            this.audioRecord = null;
        }
        this.countDownTimer.cancel();
    }

    private void creatAudioRecord() {
        this.bufferSizeInBytes = AudioRecord.getMinBufferSize(AudioFileFunc.AUDIO_SAMPLE_RATE, 16, 2);
        this.audioRecord = new AudioRecord(1, AudioFileFunc.AUDIO_SAMPLE_RATE, 16, 2, this.bufferSizeInBytes);
    }

    class AudioRecordThread implements Runnable {
        AudioRecordThread() {
        }

        public void run() {
            AudioRecordFunc.this.writeOpusDateTOFile();
        }
    }

    class CheckAudioRecordNoise implements Runnable {
        CheckAudioRecordNoise() {
        }

        public void run() {
            AudioRecordFunc.this.calculateMaxVolume();
        }
    }

    /* access modifiers changed from: private */
    public void calculateMaxVolume() {
        int i = this.bufferSizeInBytes > 0 ? this.bufferSizeInBytes : FRAME_SIZE;
        short[] sArr = new short[i];
        while (this.isRecord) {
            int read = this.audioRecord.read(sArr, 0, i);
            if (-3 != read) {
                long j = 0;
                for (int i2 = 0; i2 < read; i2++) {
                    j += (long) Math.abs(sArr[i2]);
                }
                if (read != 0) {
                    double d = (double) j;
                    double d2 = (double) read;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    double log10 = Math.log10(d / d2) * 20.0d;
                    if (log10 > this.maxVolume) {
                        this.maxVolume = log10;
                    }
                    synchronized (this.mLock) {
                        try {
                            this.mLock.wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void writeOpusDateTOFile() {
        FileOutputStream fileOutputStream;
        this.mStartRecordTime = System.currentTimeMillis();
        short[] sArr = new short[320];
        try {
            this.audioName = this.folderPath + TimeUtil.getCurrentTime();
            File file = new File(this.audioName);
            if (file.exists()) {
                file.delete();
            }
            fileOutputStream = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            fileOutputStream = null;
        }
        VoiceCodecs voiceCodecs = new VoiceCodecs();
        voiceCodecs.open(true);
        while (this.isRecord) {
            if (!(-3 == this.audioRecord.read(sArr, 0, 320) || fileOutputStream == null)) {
                try {
                    byte[] bArr = new byte[FRAME_SIZE];
                    int bufferFrame = voiceCodecs.bufferFrame(sArr, bArr);
                    byte[] bArr2 = new byte[bufferFrame];
                    System.arraycopy(bArr, 0, bArr2, 0, bufferFrame);
                    fileOutputStream.write(bArr2);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            } catch (Throwable th) {
                voiceCodecs.close();
                throw th;
            }
        }
        voiceCodecs.close();
    }

    public boolean isClosedForLimit() {
        return this.closedForLimit;
    }

    public int getMinRecordSeconds() {
        return this.mMinRecordSeconds;
    }

    public int getMaxRecordSeconds() {
        return this.mMaxRecordSeconds;
    }

    public void setmMinRecordSeconds(int i) {
        this.mMinRecordSeconds = i;
    }

    public void setMaxRecordSeconds(int i) {
        this.mMaxRecordSeconds = i;
    }

    public long getRecordTime() {
        return (this.mStopRecordTime - this.mStartRecordTime) / 1000;
    }
}
