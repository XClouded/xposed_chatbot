package com.alimama.union.app.infrastructure.image.download;

import android.app.Activity;
import android.content.Context;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.permission.Permission;
import javax.inject.Inject;
import javax.inject.Named;

public class StoragePermissionValidator {
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission permission;

    public StoragePermissionValidator() {
        App.getAppComponent().inject(this);
    }

    public boolean checkRequiredPermission(Activity activity) {
        switch (this.permission.checkPermission(activity)) {
            case -1:
                if (!this.permission.shouldShowPermissionRationale(activity)) {
                    ToastUtil.showToast((Context) activity, (int) R.string.permission_request_sdcard);
                }
                this.permission.requestPermission(activity);
                return false;
            case 0:
                return true;
            default:
                return false;
        }
    }
}
