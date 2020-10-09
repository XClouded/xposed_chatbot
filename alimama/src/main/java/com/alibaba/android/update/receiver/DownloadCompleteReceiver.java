package com.alibaba.android.update.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.alibaba.android.update.UpdateActionType;
import com.alibaba.android.update.UpdatePreference;
import com.alibaba.android.update.UpdateService;
import com.alibaba.android.update.UpdateUtils;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DCReceiver";

    public void onReceive(Context context, Intent intent) {
        if (context != null && "android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
            SharedPreferences sharedPreferences = UpdatePreference.getInstance(context).getSharedPreferences();
            long j = sharedPreferences.getLong(UpdatePreference.KEY_DOWNLOAD_ID, -1);
            long longExtra = intent.getLongExtra("extra_download_id", -1);
            if (longExtra == j) {
                String installApkPathById = UpdateUtils.getInstallApkPathById(context, longExtra);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(UpdatePreference.KEY_DOWNLOADED_FILE_PATH, installApkPathById);
                edit.commit();
                String downloadTitleById = UpdateUtils.getDownloadTitleById(context, longExtra);
                Intent intent2 = new Intent(context, UpdateService.class);
                intent2.setAction(UpdateActionType.ACTION_DOWNLOAD_COMLETE.toString());
                intent2.putExtra(UpdateService.EXTRA_INSTALL_FILE_PATH, installApkPathById);
                intent2.putExtra(UpdateService.EXTRA_INSTALL_FILE_TITLE, downloadTitleById);
                context.startService(intent2);
            }
        }
    }
}
