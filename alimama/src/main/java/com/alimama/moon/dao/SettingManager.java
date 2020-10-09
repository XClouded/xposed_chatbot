package com.alimama.moon.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.login4android.Login;

public final class SettingManager {
    private static final String LAST_CHECK_UPDATE_TIME = "last_check_update_time";
    public static final String LAST_VERSION = "last_version";
    public static final String MASTER_APPRENTICE_ISFIRST = "master_apprentice_is_first";
    public static final String MEMBERID = "member_id";
    public static final String MESSAGE_ID = "message_id";
    public static final String MID_H5_TAB_GUIDE_ISFIRST = "mid_h5_tab_guide_is_first";
    public static final String MINE_DOTE_READ = "mine_dote_read_";
    public static final String MINE_ITEM_COUNT = "mine_item_count";
    public static final String NOTICE_ID = "notice_id";
    public static final String SETTING_NAME = "setting";
    public static final String TOKEN = "token";
    public static final String USERID = "user_id";
    public static final String USER_GUIDE_ISFIRST = "user_guide_first";
    private static SettingManager mInstance;
    private SharedPreferences mCommPref = this.mContext.getSharedPreferences("setting", 0);
    private Context mContext;
    private SharedPreferences mCurUserPref = getCurUserPref();

    public boolean isPromotionSwitchOn() {
        return true;
    }

    public static SettingManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SettingManager.class) {
                if (mInstance == null) {
                    mInstance = new SettingManager(context);
                }
            }
        }
        return mInstance;
    }

    private SettingManager(Context context) {
        this.mContext = context;
    }

    public void setUserGuideFirst(boolean z) {
        SharedPreferences.Editor edit = this.mCommPref.edit();
        edit.putBoolean(USER_GUIDE_ISFIRST, z);
        edit.commit();
    }

    public boolean isUserGuideFirst() {
        return this.mCommPref.getBoolean(USER_GUIDE_ISFIRST, true);
    }

    public void setMidTabUserGuideFirst(boolean z) {
        SharedPreferences.Editor edit = this.mCommPref.edit();
        edit.putBoolean(MID_H5_TAB_GUIDE_ISFIRST, z);
        edit.commit();
    }

    public boolean isMidTabUserGuideFirst() {
        return this.mCommPref.getBoolean(MID_H5_TAB_GUIDE_ISFIRST, true);
    }

    public void setMasterApprenticeUserGuideFirst(boolean z) {
        SharedPreferences.Editor edit = this.mCommPref.edit();
        edit.putBoolean(MASTER_APPRENTICE_ISFIRST, z);
        edit.commit();
    }

    public boolean isMasterApprenticeUserGuideFirst() {
        return this.mCommPref.getBoolean(MASTER_APPRENTICE_ISFIRST, true);
    }

    public void setPostionRead(boolean z, int i) {
        if (this.mCommPref != null) {
            SharedPreferences.Editor edit = this.mCommPref.edit();
            edit.putBoolean(MINE_DOTE_READ + i, z);
            edit.apply();
        }
    }

    public int getMineItemCount() {
        if (this.mCommPref == null) {
            return 0;
        }
        return this.mCommPref.getInt(MINE_ITEM_COUNT, 0);
    }

    public void setLastVersion(int i) {
        if (this.mCommPref != null) {
            SharedPreferences.Editor edit = this.mCommPref.edit();
            edit.putInt(LAST_VERSION, i);
            edit.apply();
        }
    }

    public int getLastVersion() {
        if (this.mCommPref == null) {
            return -1;
        }
        return this.mCommPref.getInt(LAST_VERSION, -1);
    }

    public String getCurUser() {
        return Login.getNick();
    }

    public long getLastCheckUpdateTime() {
        return this.mCommPref.getLong(LAST_CHECK_UPDATE_TIME, 0);
    }

    public void setLastCheckUpdateTime(long j) {
        SharedPreferences.Editor edit = this.mCommPref.edit();
        edit.putLong(LAST_CHECK_UPDATE_TIME, j);
        edit.apply();
    }

    public SharedPreferences getCurUserPref() {
        String curUser = getCurUser();
        if (TextUtils.equals(curUser, "")) {
            return null;
        }
        Context context = this.mContext;
        return context.getSharedPreferences("setting_" + curUser, 0);
    }

    public void logout() {
        clearUserInfo();
    }

    public void clearUserInfo() {
        if (this.mCurUserPref != null) {
            SharedPreferences.Editor edit = this.mCurUserPref.edit();
            edit.remove("token");
            edit.remove(USERID);
            edit.remove(MEMBERID);
            edit.apply();
        }
    }

    public void setUserId(Long l) {
        if (this.mCurUserPref != null) {
            SharedPreferences.Editor edit = this.mCurUserPref.edit();
            edit.putLong(USERID, l.longValue());
            edit.apply();
        }
    }

    public String getUserId() {
        return TextUtils.isEmpty(Login.getUserId()) ? "0" : Login.getUserId();
    }

    public void setMemberId(Long l) {
        if (this.mCurUserPref == null) {
            this.mCurUserPref = getCurUserPref();
        }
        SharedPreferences.Editor edit = this.mCurUserPref.edit();
        edit.putLong(MEMBERID, l.longValue());
        edit.apply();
    }

    public String getMemberId() {
        if (this.mCurUserPref == null) {
            return "0";
        }
        return String.valueOf(Long.valueOf(this.mCurUserPref.getLong(MEMBERID, 0)));
    }

    public void setMessageId(Long l) {
        if (this.mCurUserPref != null) {
            SharedPreferences.Editor edit = this.mCurUserPref.edit();
            edit.putLong(MESSAGE_ID, l.longValue());
            edit.apply();
        }
    }

    public Long getMessageId() {
        if (this.mCurUserPref == null) {
            return 0L;
        }
        return Long.valueOf(this.mCurUserPref.getLong(MESSAGE_ID, 0));
    }

    public void setNoticeId(Long l) {
        if (this.mCurUserPref != null) {
            SharedPreferences.Editor edit = this.mCurUserPref.edit();
            edit.putLong(NOTICE_ID, l.longValue());
            edit.apply();
        }
    }

    public Long getNoticeId() {
        if (this.mCurUserPref == null) {
            return 0L;
        }
        return Long.valueOf(this.mCurUserPref.getLong(NOTICE_ID, 0));
    }
}
