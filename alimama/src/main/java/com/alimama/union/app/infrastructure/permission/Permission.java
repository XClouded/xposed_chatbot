package com.alimama.union.app.infrastructure.permission;

import android.app.Activity;

public interface Permission {
    public static final int REQUEST_CAMERA = 3;
    public static final int REQUEST_PERMISSION_READ_CONTACTS = 1;
    public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

    int checkPermission(Activity activity);

    boolean hasPermission(Activity activity);

    void openPermissionSetting(Activity activity);

    void requestPermission(Activity activity);

    boolean shouldShowPermissionRationale(Activity activity);
}
