package android.taobao.windvane.file;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class WVFileUtils {
    private static final String TAG = "WVFileUtils";
    private static String sAuthority;

    public static ArrayList<String> getFileListbyDir(File file) {
        TaoLog.i(TAG, file.getPath());
        ArrayList<String> arrayList = new ArrayList<>();
        getFileList(file, arrayList);
        return arrayList;
    }

    private static void getFileList(File file, ArrayList<String> arrayList) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File fileList : listFiles) {
                    getFileList(fileList, arrayList);
                }
                return;
            }
            return;
        }
        arrayList.add(file.getAbsolutePath());
    }

    public static void setAuthority(String str) {
        sAuthority = str;
    }

    public static Uri getFileUri(Context context, File file) {
        if (file == null || context == null) {
            return null;
        }
        try {
            if (context.getApplicationInfo().targetSdkVersion < 24 || Build.VERSION.SDK_INT < 24) {
                return Uri.fromFile(file);
            }
            String str = sAuthority;
            if (TextUtils.isEmpty(str)) {
                str = context.getPackageName() + ".interactProvider";
            }
            return FileProvider.getUriForFile(context, str, file);
        } catch (Throwable th) {
            TaoLog.w("WVUtils", "Failed to get file uri:" + th.getMessage());
            return null;
        }
    }
}
