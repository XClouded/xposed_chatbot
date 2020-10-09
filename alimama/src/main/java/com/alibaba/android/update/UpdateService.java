package com.alibaba.android.update;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.exception.StateNotFoundException;
import com.alibaba.android.update.state.UpdateProcessor;

public class UpdateService extends IntentService {
    public static final String EXTRA_APK_MD5 = "extra_update_apk_md5";
    public static final String EXTRA_DOWNLOAD_APK_SIZE = "extra_update_download_apk_size";
    public static final String EXTRA_DOWNLOAD_FILE_NAME = "extra_update_download_file_name";
    public static final String EXTRA_DOWNLOAD_PATCH_SIZE = "extra_update_download_patch_size";
    public static final String EXTRA_DOWNLOAD_TITLE_NAME = "extra_update_download_title_name";
    public static final String EXTRA_DOWNLOAD_URL = "extra_update_download_url";
    public static final String EXTRA_DOWNLOAD_URL_PATCH = "extra_update_download_url_patch";
    public static final String EXTRA_INSTALL_FILE_PATH = "extra_install_file_path";
    public static final String EXTRA_INSTALL_FILE_TITLE = "extra_install_file_title";
    public static final String EXTRA_MD5_IS_SWITCH_ON = "extra_md5_is_switch_on";
    public static final String EXTRA_PATCH_IS_SWITCH_ON = "extra_patch_is_switch_on";
    private static final String TAG = "UpdateService";
    public static final String TITLE_SILENT = "title_update_silent";
    private ILogger logger;
    private final IBinder mBinder = new LocalBinder();
    private SharedPreferences.Editor mEditor;
    public OnProgressListener mOnProgressListener;
    private SharedPreferences mSp;
    private UpdateProcessor mUpdateProcessor;

    public UpdateService() {
        super(TAG);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public UpdateService getService() {
            return UpdateService.this;
        }
    }

    public void onCreate() {
        super.onCreate();
        this.mSp = UpdatePreference.getInstance(this).getSharedPreferences();
        this.mEditor = this.mSp.edit();
        this.logger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        this.logger.logd(TAG, "update->UpdateService: onCreate");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        this.logger.logd(TAG, "update->UpdateService: onHandleIntent");
        try {
            this.mUpdateProcessor = new UpdateProcessor(this);
            String stringExtra = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
            String stringExtra2 = intent.getStringExtra(EXTRA_DOWNLOAD_URL_PATCH);
            String stringExtra3 = intent.getStringExtra(EXTRA_APK_MD5);
            boolean booleanExtra = intent.getBooleanExtra(EXTRA_MD5_IS_SWITCH_ON, false);
            String action = intent.getAction();
            boolean booleanExtra2 = intent.getBooleanExtra(EXTRA_PATCH_IS_SWITCH_ON, false);
            if (!UpdateActionType.ACTION_DOWNLOAD_COMLETE.toString().equals(action)) {
                this.mUpdateProcessor.init(stringExtra, stringExtra2, stringExtra3, booleanExtra);
            }
            this.mUpdateProcessor.setOnProgressListener(this.mOnProgressListener);
            ILogger iLogger = this.logger;
            iLogger.logd(TAG, "update->action: " + action + ", url: " + stringExtra + ", md5: " + stringExtra3);
            if (UpdateActionType.ACTION_DOWNLOAD_SILENT.toString().equals(action)) {
                this.logger.logd(TAG, "update->UpdateService: ACTION_DOWNLOAD_SILENT");
                if (TextUtils.isEmpty(action)) {
                    ILogger iLogger2 = this.logger;
                    iLogger2.logd(TAG, "update->action: " + action + ", url: " + stringExtra);
                } else if (isSilentAvailable()) {
                    String stringExtra4 = intent.getStringExtra(EXTRA_DOWNLOAD_FILE_NAME);
                    this.mUpdateProcessor.download(stringExtra, stringExtra2, booleanExtra2, intent.getStringExtra(EXTRA_DOWNLOAD_TITLE_NAME), stringExtra4, stringExtra3, booleanExtra, true, (String) null);
                }
            } else if (UpdateActionType.ACTION_DOWNLOAD_INSTALL.toString().equals(action)) {
                this.logger.logd(TAG, "update->UpdateService: ACTION_DOWNLOAD_INSTALL");
                if (TextUtils.isEmpty(action)) {
                    ILogger iLogger3 = this.logger;
                    iLogger3.logd(TAG, "update->action: " + action + ", url: " + stringExtra);
                    return;
                }
                String stringExtra5 = intent.getStringExtra(EXTRA_DOWNLOAD_FILE_NAME);
                if (TextUtils.isEmpty(stringExtra5)) {
                    stringExtra5 = getPackageName();
                }
                String stringExtra6 = intent.getStringExtra(EXTRA_DOWNLOAD_TITLE_NAME);
                String string = UpdatePreference.getInstance(this).getSharedPreferences().getString(UpdatePreference.KEY_DOWNLOADED_FILE_PATH, "");
                this.mUpdateProcessor.download(stringExtra, stringExtra2, booleanExtra2, stringExtra6, stringExtra5, stringExtra3, booleanExtra, false, string);
            } else if (UpdateActionType.ACTION_INSTALL.toString().equals(action)) {
                this.logger.logd(TAG, "update->UpdateService: ACTION_INSTALL");
                this.mUpdateProcessor.install(intent.getStringExtra(EXTRA_INSTALL_FILE_PATH), false);
            } else if (UpdateActionType.ACTION_DOWNLOAD_COMLETE.toString().equals(action)) {
                this.mUpdateProcessor.install(intent.getStringExtra(EXTRA_INSTALL_FILE_PATH), TITLE_SILENT.equals(intent.getStringExtra(EXTRA_INSTALL_FILE_TITLE)));
            } else if (UpdateActionType.ACTION_RESUME.toString().equals(action)) {
                this.mUpdateProcessor.resume();
            } else if (UpdateActionType.ACTION_SUSPEND.toString().equals(action)) {
                this.mUpdateProcessor.suspend();
            }
        } catch (StateNotFoundException e) {
            ILogger iLogger4 = this.logger;
            iLogger4.logd(TAG, "state not found: " + e.getMessage());
        }
    }

    private boolean isSilentAvailable() {
        boolean isSilentAvailable = UpdateUtils.isSilentAvailable(this);
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "update->UpdateService: isSilentAvailable, isSilent: " + isSilentAvailable);
        return isSilentAvailable;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }
}
