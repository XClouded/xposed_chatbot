package alimama.com.unwupdate;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IUpdateManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import kotlin.UByte;

public class UpdateController implements DownLoadListener {
    private static final String ACTION_CANCEL_DOWNLOAD = ".cancelDownloadApk";
    private static final String MSG_DOWNLOAD_FAIL = "下载失败";
    private static final String MSG_DOWNLOAD_FINISH_TICKER_TEXT = "下载已完成，请到系统通知栏查看和安装";
    private static final String MSG_DOWNLOAD_FINISH_TITLE = "下载已完成，点击安装";
    private static final String MSG_DOWN_LOAD_START = "已转入后台下载，请稍候";
    private static final String MSG_ERROR_URL = "错误的下载地址";
    private static final int NOTIFY_ID = 15500;
    private static final String TAG = "UpdateController";
    private static UpdateController sInstance;
    private String mActionCancel;
    private String mApkPath;
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private String mData;
    private String mDownLoadErrorMsg = MSG_DOWNLOAD_FAIL;
    /* access modifiers changed from: private */
    public DownloadTask mDownloadTask;
    private int mIcon;
    private String mMd5;
    Notification mNotification = null;
    NotificationManager mNotificationManager = null;
    private PackageInfo mPackageInfo;
    private String mPackageName;

    private void deleteApkFile() {
    }

    public void onCancel() {
    }

    private UpdateController() {
    }

    public static UpdateController getInstance() {
        if (sInstance == null) {
            sInstance = new UpdateController();
        }
        return sInstance;
    }

    public void init(Context context, int i) {
        if (context != null) {
            IUpdateManager iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class);
            if (iUpdateManager != null) {
                this.mContext = context;
                this.mIcon = i;
                try {
                    this.mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    this.mPackageName = this.mPackageInfo.packageName;
                    this.mActionCancel = this.mPackageName + ACTION_CANCEL_DOWNLOAD;
                    this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
                    if (Version.hasOreo()) {
                        String str = ((UpdateParams) iUpdateManager.getUpdateParams()).downloadChannelId;
                        NotificationChannel notificationChannel = new NotificationChannel(str, ((UpdateParams) iUpdateManager.getUpdateParams()).downloadChannelName, 2);
                        notificationChannel.setDescription(((UpdateParams) iUpdateManager.getUpdateParams()).downloadChannelDesc);
                        this.mNotificationManager.createNotificationChannel(notificationChannel);
                        this.mBuilder = new NotificationCompat.Builder(this.mContext, str);
                        return;
                    }
                    this.mBuilder = new NotificationCompat.Builder(this.mContext);
                } catch (PackageManager.NameNotFoundException unused) {
                    throw new RuntimeException("Can not find package information");
                }
            }
        } else {
            throw new IllegalArgumentException("How content can be null?");
        }
    }

    private void notifyDownloadFinish() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(268435456);
            intent.setDataAndType(Uri.fromFile(new File(this.mApkPath)), "application/vnd.android.package-archive");
            PendingIntent activity = PendingIntent.getActivity(this.mContext, 0, intent, 0);
            this.mBuilder.setContentText("下载进度...100%");
            this.mNotification = this.mBuilder.build();
            this.mNotification.icon = this.mIcon;
            this.mNotification.flags = 16;
            this.mNotification.contentIntent = activity;
            this.mNotificationManager.notify(NOTIFY_ID, this.mNotification);
        } catch (Exception unused) {
        }
        openFile();
    }

    private void notifyDownLoadStart() {
        showToastMessage(MSG_DOWN_LOAD_START);
    }

    private void notifyDownLoading(int i) {
        try {
            PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.mActionCancel), 0);
            NotificationCompat.Builder builder = this.mBuilder;
            builder.setContentText("下载进度..." + i + "%, 点击取消下载");
            this.mNotification = this.mBuilder.build();
            this.mNotification.icon = this.mIcon;
            this.mNotification.flags = 16;
            this.mNotification.contentIntent = broadcast;
            this.mNotificationManager.notify(NOTIFY_ID, this.mNotification);
        } catch (Exception unused) {
        }
    }

    public void beginDownLoad(String str, UpdateResult updateResult) {
        beginDownLoad(str, false);
        this.mData = updateResult != null ? updateResult.jsonData : "";
        this.mMd5 = updateResult != null ? updateResult.md5 : "";
    }

    public void beginDownLoad(String str, boolean z) {
        String wantFilesPath = DiskFileUtils.wantFilesPath(this.mContext, true);
        String substring = str.substring(str.lastIndexOf("/") + 1);
        this.mApkPath = wantFilesPath + File.separator + "downloads" + File.separator + substring;
        this.mDownloadTask = new DownloadTask(this, str, this.mApkPath, this.mMd5);
        notifyDownLoadStart();
        if (z) {
            deleteApkFile();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.mActionCancel);
        this.mContext.registerReceiver(new CancelBroadcastReceiver(), intentFilter);
        Thread thread = new Thread(this.mDownloadTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void openFile() {
        IUpdateManager iUpdateManager;
        Uri uri;
        if (TextUtils.equals(this.mMd5, getFileMd5()) && (iUpdateManager = (IUpdateManager) UNWManager.getInstance().getService(IUpdateManager.class)) != null) {
            Intent intent = new Intent("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(268435457);
                uri = FileProvider.getUriForFile(this.mContext, ((UpdateParams) iUpdateManager.getUpdateParams()).fileProviderAuthority, new File(this.mApkPath));
            } else {
                intent.setFlags(268435456);
                uri = Uri.fromFile(new File(this.mApkPath));
            }
            if (Build.VERSION.SDK_INT <= 19 && this.mContext != null) {
                for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentActivities(intent, 65536)) {
                    this.mContext.grantUriPermission(resolveInfo.activityInfo.packageName, uri, 1);
                }
            }
            if (uri != null && this.mContext != null && this.mNotificationManager != null) {
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                this.mContext.startActivity(intent);
                this.mNotificationManager.cancel(NOTIFY_ID);
            }
        }
    }

    private String getFileMd5() {
        String fileMD5Impl = getFileMD5Impl(new File(this.mApkPath));
        return TextUtils.isEmpty(fileMD5Impl) ? "empty" : fileMD5Impl;
    }

    public static String getFileMD5Impl(File file) {
        if (!file.isFile()) {
            return null;
        }
        byte[] bArr = new byte[1024];
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read != -1) {
                    instance.update(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    return bytesToHexString(instance.digest());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public void onDone(boolean z, int i) {
        if (!z) {
            switch (i) {
                case 1:
                    this.mNotificationManager.cancelAll();
                    notifyDownloadFinish();
                    return;
                case 2:
                    this.mNotificationManager.cancel(NOTIFY_ID);
                    deleteApkFile();
                    showToastMessage(MSG_ERROR_URL);
                    return;
                case 3:
                    this.mNotificationManager.cancel(NOTIFY_ID);
                    deleteApkFile();
                    showToastMessage(this.mDownLoadErrorMsg);
                    return;
                default:
                    return;
            }
        }
    }

    public void onPercentUpdate(int i) {
        notifyDownLoading(i);
    }

    private void showToastMessage(String str) {
        Toast makeText = Toast.makeText(this.mContext, str, 1);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    private class CancelBroadcastReceiver extends BroadcastReceiver {
        private CancelBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            UpdateController.this.mDownloadTask.cancel();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    UpdateController.this.mNotificationManager.cancel(UpdateController.NOTIFY_ID);
                }
            }, 300);
        }
    }
}
