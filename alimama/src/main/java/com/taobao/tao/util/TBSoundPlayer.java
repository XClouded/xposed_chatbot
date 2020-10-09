package com.taobao.tao.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.RawRes;
import com.taobao.uikit.R;
import com.taobao.uikit.utils.SoundPlayer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class TBSoundPlayer {
    private static final String SETTINGS_SYSTEM_MESSAGE_SOUND = "systemMessageSound";
    private static final String SETTINGS_SYSTEM_SOUND = "systemSound";
    private static TBSoundPlayer _INSTANCE = null;
    private static boolean sOnlineConfig = true;
    private static HashMap sSceneConfigMap = new HashMap<Integer, String>() {
        {
            put(0, "message");
            put(1, "tap");
            put(2, "pullRefresh");
            put(3, "favorite");
            put(4, "cart");
            put(5, "like");
            put(6, "page");
            put(7, "page");
        }
    };
    private Context mContext;
    private boolean mEnable = true;
    private boolean mIsTouchSoundsEnabled = false;
    private boolean mMessageEnable = true;
    private SoundPlayer mRealPlayer;

    public static class Scene {
        public static final int CART = 4;
        public static final int FAVORITE = 3;
        public static final int LIKE = 5;
        public static final int PAY = 6;
        public static final int PUBLISH = 7;
        public static final int PUSH = 0;
        public static final int REFRESH = 2;
        public static final int TAP = 1;
    }

    public static TBSoundPlayer getInstance() {
        if (_INSTANCE == null) {
            synchronized (TBSoundPlayer.class) {
                _INSTANCE = new TBSoundPlayer();
            }
        }
        return _INSTANCE;
    }

    private TBSoundPlayer() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            this.mContext = (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            this.mContext = null;
        }
        this.mRealPlayer = SoundPlayer.getInstance(this.mContext);
        this.mRealPlayer.register(0, R.raw.sound_push);
        this.mRealPlayer.register(1, R.raw.sound_tap);
        this.mRealPlayer.register(2, R.raw.sound_refresh);
        this.mRealPlayer.register(3, R.raw.sound_favorite);
        this.mRealPlayer.register(5, R.raw.sound_like);
        this.mRealPlayer.register(6, R.raw.sound_page_success);
        this.mRealPlayer.register(4, R.raw.sound_add_to_cart);
        this.mRealPlayer.register(7, R.raw.sound_page_success);
    }

    public static void setOnlineConfig(boolean z) {
        sOnlineConfig = z;
    }

    public void play(@RawRes int i) {
        if (shouldPlaySound()) {
            this.mRealPlayer.play(i);
        }
    }

    public void play(String str) {
        if (shouldPlaySound()) {
            this.mRealPlayer.play(str);
        }
    }

    public void playScene(int i) {
        if (shouldPlayScene(i)) {
            this.mRealPlayer.playScene(i);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldPlaySound() {
        if (this.mContext == null) {
            return sOnlineConfig;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!defaultSharedPreferences.contains(SETTINGS_SYSTEM_SOUND)) {
            return sOnlineConfig;
        }
        this.mEnable = defaultSharedPreferences.getBoolean(SETTINGS_SYSTEM_SOUND, true);
        return this.mEnable;
    }

    /* access modifiers changed from: protected */
    public boolean shouldPlayScene(int i) {
        boolean z;
        if (this.mContext == null) {
            return sOnlineConfig;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        boolean contains = defaultSharedPreferences.contains(SETTINGS_SYSTEM_SOUND);
        if (contains) {
            this.mEnable = defaultSharedPreferences.getBoolean(SETTINGS_SYSTEM_SOUND, true);
        }
        this.mMessageEnable = defaultSharedPreferences.getBoolean(SETTINGS_SYSTEM_MESSAGE_SOUND, true);
        this.mIsTouchSoundsEnabled = Settings.System.getInt(this.mContext.getContentResolver(), "sound_effects_enabled", 1) != 0;
        if (i == 0) {
            return this.mMessageEnable;
        }
        if (contains) {
            z = this.mEnable;
        } else {
            z = sOnlineConfig;
        }
        if (!z || !this.mIsTouchSoundsEnabled) {
            return z;
        }
        return i == 2 || i == 7;
    }

    public Object getSound(int i) {
        return this.mRealPlayer.getSound(i);
    }

    public void updateSound(int i, Object obj) {
        if (obj instanceof Integer) {
            this.mRealPlayer.register(i, ((Integer) obj).intValue());
        } else if (obj instanceof String) {
            this.mRealPlayer.register(i, (String) obj);
        }
    }

    public void release() {
        this.mRealPlayer.release();
    }

    public static HashMap<Integer, String> getConfigMap() {
        return sSceneConfigMap;
    }
}
