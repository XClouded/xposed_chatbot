package com.taobao.login4android.video;

import android.os.Environment;

public class AudioFileFunc {
    public static final String AUDIO_AMR_FILENAME = "Audio.amr";
    public static final int AUDIO_INPUT = 1;
    private static final String AUDIO_RAW_FILENAME = "OupsAudio.raw";
    public static final int AUDIO_SAMPLE_RATE = 16000;
    private static final String AUDIO_WAV_FILENAME = "OupsAudio.wav";

    public static boolean isSdcardExit() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}
