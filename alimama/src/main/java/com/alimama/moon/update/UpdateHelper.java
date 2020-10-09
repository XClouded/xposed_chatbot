package com.alimama.moon.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.android.update.UpdateActionType;
import com.alibaba.android.update.UpdateManager;
import com.alibaba.android.update.UpdateService;
import com.alibaba.android.update4mtl.data.ResponseUpdateInfo;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.permission.Permission;

public final class UpdateHelper {
    public static boolean checkPermissionGranted(Activity activity, Permission permission) {
        if (activity == null) {
            return false;
        }
        if (permission.checkPermission(activity) != -1) {
            return true;
        }
        if (!permission.shouldShowPermissionRationale(activity)) {
            ToastUtil.showToast((Context) activity, (int) R.string.permission_request_sdcard);
        }
        permission.requestPermission(activity);
        return false;
    }

    public static void startUpdateService(Activity activity, ResponseUpdateInfo responseUpdateInfo) {
        String fileNameByString = UpdateManager.getFileNameByString(responseUpdateInfo.url);
        Intent intent = new Intent(activity, UpdateService.class);
        intent.setAction(UpdateActionType.ACTION_DOWNLOAD_INSTALL.toString());
        intent.putExtra(UpdateService.EXTRA_DOWNLOAD_URL, responseUpdateInfo.url);
        intent.putExtra(UpdateService.EXTRA_DOWNLOAD_URL_PATCH, responseUpdateInfo.patchUrl);
        intent.putExtra(UpdateService.EXTRA_APK_MD5, responseUpdateInfo.md5);
        intent.putExtra(UpdateService.EXTRA_DOWNLOAD_FILE_NAME, fileNameByString);
        intent.putExtra(UpdateService.EXTRA_DOWNLOAD_TITLE_NAME, activity.getString(R.string.app_name));
        intent.setPackage(activity.getPackageName());
        activity.startService(intent);
    }
}
