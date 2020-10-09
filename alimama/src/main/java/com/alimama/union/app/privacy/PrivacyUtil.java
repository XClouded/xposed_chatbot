package com.alimama.union.app.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.app.ActivityCompat;

public class PrivacyUtil {
    public static final String PRIVACY_DETAIL_JUMP_URL = "https://terms.alicdn.com/legal-agreement/terms/suit_bu1_ali_mama_division/suit_bu1_ali_mama_division201709111812_13128.html?spm=a219t.11816995.a214tr9.3.2a8f75a571nxIC";
    private static final String PRIVACY_DIALOG_FIRST_APPEAR = "privacy_dialog_first_appear";

    public static boolean hasWriteExternalStorage(Context context) {
        return ActivityCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static boolean hasContactPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, "android.permission.READ_CONTACTS") == 0;
    }

    public static void setPrivacyDialogAppeared(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PRIVACY_DIALOG_FIRST_APPEAR, 0).edit();
        edit.putBoolean(PRIVACY_DIALOG_FIRST_APPEAR, true);
        edit.apply();
    }

    public static boolean isPrivacyDialogAppeared(Context context) {
        return context.getSharedPreferences(PRIVACY_DIALOG_FIRST_APPEAR, 0).getBoolean(PRIVACY_DIALOG_FIRST_APPEAR, false);
    }
}
