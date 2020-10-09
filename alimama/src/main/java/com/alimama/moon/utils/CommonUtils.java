package com.alimama.moon.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.ali.user.mobile.log.TLogAdapter;
import com.alimama.moon.App;
import com.alimama.union.app.taotokenConvert.TaoCodeTransferPresenter;
import java.util.List;

public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static long getSafeLongValue(String str, long j) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return j;
        }
    }

    public static int getSafeIntValue(String str) {
        return getSafeIntValue(str, 0);
    }

    public static int getSafeIntValue(String str, int i) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            return i;
        }
    }

    public static boolean isMonkeyBuild() {
        return "monkey".equalsIgnoreCase("release");
    }

    public static boolean copyToClipboard(CharSequence charSequence) {
        ClipboardManager clipboardManager = (ClipboardManager) App.sApplication.getSystemService("clipboard");
        if (clipboardManager == null) {
            return false;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(TaoCodeTransferPresenter.TAO_CODE_CLIP_DATA_LABEL, charSequence));
        return true;
    }

    public static void clearClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) App.sApplication.getSystemService("clipboard");
        if (clipboardManager != null) {
            try {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""));
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    public static void resumeContentToClipboard(String str) {
        ClipboardManager clipboardManager;
        if (!TextUtils.isEmpty(str) && (clipboardManager = (ClipboardManager) App.sApplication.getSystemService("clipboard")) != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(TaoCodeTransferPresenter.ORDER_CHECK_CLIP_DATA_LABEL, str));
        }
    }

    public static String getClipboardContent() {
        ClipData primaryClip;
        ClipData.Item itemAt;
        ClipboardManager clipboardManager = (ClipboardManager) App.sApplication.getSystemService("clipboard");
        if (clipboardManager == null || (primaryClip = clipboardManager.getPrimaryClip()) == null || (itemAt = primaryClip.getItemAt(0)) == null) {
            return "";
        }
        CharSequence coerceToText = itemAt.coerceToText(App.sApplication);
        if (TextUtils.isEmpty(coerceToText)) {
            return "";
        }
        return coerceToText.toString();
    }

    public static String getProcessName(Context context) {
        if (context == null) {
            return "[process_name_empty]";
        }
        int myPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null || runningAppProcesses.isEmpty()) {
            return "[process_name_empty]";
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next != null) {
                try {
                    if (next.pid == myPid) {
                        return next.processName;
                    }
                } catch (Exception e) {
                    TLogAdapter.d("get-process-name", " e: " + e.getMessage());
                }
            }
        }
        return "[process_name_empty]";
    }

    public static String getTopActivity(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        return runningTasks != null ? runningTasks.get(0).topActivity.toString() : "";
    }
}
