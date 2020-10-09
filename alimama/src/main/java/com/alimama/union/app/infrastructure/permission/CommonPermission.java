package com.alimama.union.app.infrastructure.permission;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import com.alimama.moon.usertrack.UTHelper;

public class CommonPermission implements Permission {
    private String permission;
    private Integer requestCode;

    public CommonPermission(String str, Integer num) {
        this.permission = str;
        this.requestCode = num;
    }

    public boolean hasPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, this.permission) == 0;
    }

    public int checkPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            UTHelper.sendControlHit(UTHelper.PAGE_NAME_SD_PERMISSION, UTHelper.CONTROL_NAME_HAVE_PERMISSION);
        }
        return ActivityCompat.checkSelfPermission(activity, this.permission);
    }

    public void requestPermission(Activity activity) {
        if (!hasPermission(activity)) {
            ActivityCompat.requestPermissions(activity, new String[]{this.permission}, this.requestCode.intValue());
        }
    }

    public boolean shouldShowPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, this.permission);
    }

    public void openPermissionSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:com.alimama.moon"));
        activity.startActivity(intent);
    }
}
