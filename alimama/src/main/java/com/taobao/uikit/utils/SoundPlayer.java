package com.taobao.uikit.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.RawRes;
import com.taobao.uikit.R;
import java.util.HashMap;

public class SoundPlayer {
    private static final String TAG = "SoundPlayer";
    protected static SoundPlayer _INSTANCE;
    private Context mContext;
    private HashMap<Integer, Object> mScenesMap;
    private boolean mShoudPlay = true;
    private SoundPool mSoundPool;
    private HashMap<Object, Integer> mSoundsLoaded;

    protected SoundPlayer(Context context) {
        this.mContext = context;
        initSoundPool();
        this.mScenesMap = new HashMap<>();
    }

    public static SoundPlayer getInstance(Context context) {
        if (_INSTANCE == null) {
            synchronized (SoundPlayer.class) {
                _INSTANCE = new SoundPlayer(context);
            }
        }
        return _INSTANCE;
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mSoundPool = new SoundPool.Builder().setMaxStreams(2).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        } else {
            this.mSoundPool = new SoundPool(2, 2, 0);
        }
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                if (i2 == 0) {
                    soundPool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });
        this.mSoundsLoaded = new HashMap<>();
    }

    public void setEnable(boolean z) {
        this.mShoudPlay = z;
    }

    public void play(@RawRes int i) {
        if (this.mShoudPlay) {
            if (this.mSoundPool == null) {
                initSoundPool();
            }
            if (this.mSoundsLoaded.containsKey(Integer.valueOf(i))) {
                this.mSoundPool.play(this.mSoundsLoaded.get(Integer.valueOf(i)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            } else if (this.mContext != null) {
                this.mSoundsLoaded.put(Integer.valueOf(i), Integer.valueOf(this.mSoundPool.load(this.mContext, i, 1)));
            }
        } else {
            Log.v(TAG, "Sonification has been turn off");
        }
    }

    public void play(String str) {
        if (this.mSoundsLoaded.containsKey(str)) {
            this.mSoundPool.play(this.mSoundsLoaded.get(str).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            return;
        }
        this.mSoundsLoaded.put(str, Integer.valueOf(this.mSoundPool.load(str, 1)));
    }

    public void playScene(int i) {
        if (this.mScenesMap.containsKey(Integer.valueOf(i))) {
            Object obj = this.mScenesMap.get(Integer.valueOf(i));
            if (obj instanceof Integer) {
                play(((Integer) obj).intValue());
            } else if (obj instanceof String) {
                play((String) obj);
            }
        } else {
            Log.e(TAG, "sound with sceneId=" + i + " not binded");
        }
    }

    public void register(int i, int i2) {
        if (this.mScenesMap.containsKey(Integer.valueOf(i))) {
            Log.v(TAG, "sound has been registered for id=" + i + ", will replace it");
        }
        this.mScenesMap.put(Integer.valueOf(i), Integer.valueOf(i2));
    }

    public void register(int i, String str) {
        if (this.mScenesMap.containsKey(Integer.valueOf(i))) {
            Log.v(TAG, "sound has been registered for id=" + i + ", will replace it");
        }
        if (TextUtils.isEmpty(str)) {
            switch (i) {
                case 0:
                    register(i, R.raw.sound_push);
                    return;
                case 1:
                    register(i, R.raw.sound_tap);
                    return;
                case 2:
                    register(i, R.raw.sound_refresh);
                    return;
                case 3:
                    register(i, R.raw.sound_favorite);
                    return;
                case 4:
                    register(i, R.raw.sound_add_to_cart);
                    return;
                case 5:
                    register(i, R.raw.sound_like);
                    return;
                case 6:
                    register(i, R.raw.sound_page_success);
                    return;
                case 7:
                    register(i, R.raw.sound_page_success);
                    return;
                default:
                    return;
            }
        } else {
            this.mScenesMap.put(Integer.valueOf(i), str);
        }
    }

    public Object getSound(int i) {
        if (this.mScenesMap != null && this.mScenesMap.containsKey(Integer.valueOf(i))) {
            return this.mScenesMap.get(Integer.valueOf(i));
        }
        Log.d(TAG, "No sound has been registered with scene id=" + i);
        return null;
    }

    public void release() {
        if (this.mSoundPool != null) {
            this.mSoundPool.release();
            this.mSoundsLoaded.clear();
            this.mSoundsLoaded = null;
            this.mSoundPool = null;
        }
    }
}
