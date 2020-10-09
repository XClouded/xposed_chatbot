package com.taobao.login4android.video;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.utils.FileUtil;
import com.taobao.login4android.constants.LoginEnvType;
import com.uploader.export.ITaskListener;
import com.uploader.export.ITaskResult;
import com.uploader.export.IUploaderManager;
import com.uploader.export.IUploaderTask;
import com.uploader.export.TaskError;
import com.uploader.export.UploaderCreator;
import com.uploader.portal.UploaderDependencyImpl;
import com.uploader.portal.UploaderEnvironmentImpl;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadTask {
    public static final String TAG = "login.UploadTask";
    static Handler handler;
    private static UploadTask mUploadTask;
    /* access modifiers changed from: private */
    public ResultCallback resultCallback;
    private IUploaderManager uploaderManager;

    public interface ResultCallback {
        void onFail(String str);

        void onSuccess(String str);
    }

    private UploadTask() {
    }

    public static synchronized UploadTask getInstance() {
        UploadTask uploadTask;
        synchronized (UploadTask.class) {
            if (mUploadTask == null) {
                synchronized (UploadTask.class) {
                    if (mUploadTask == null) {
                        mUploadTask = new UploadTask();
                    }
                }
            }
            uploadTask = mUploadTask;
        }
        return uploadTask;
    }

    public void setResultCallback(ResultCallback resultCallback2) {
        this.resultCallback = resultCallback2;
    }

    public boolean uploadAsync(Context context, final String str, String str2) {
        if (this.uploaderManager == null) {
            init(context);
        }
        return this.uploaderManager.uploadAsync(new IUploaderTask() {
            public String getBizType() {
                return "voice-oss";
            }

            public String getFilePath() {
                return str;
            }

            public String getFileType() {
                return FileUtil.getExtensionName(str);
            }

            public Map<String, String> getMetaInfo() {
                return new HashMap();
            }
        }, new ITaskListener() {
            public void onPause(IUploaderTask iUploaderTask) {
            }

            public void onProgress(IUploaderTask iUploaderTask, int i) {
            }

            public void onResume(IUploaderTask iUploaderTask) {
            }

            public void onStart(IUploaderTask iUploaderTask) {
            }

            public void onWait(IUploaderTask iUploaderTask) {
            }

            public void onSuccess(IUploaderTask iUploaderTask, @Nullable ITaskResult iTaskResult) {
                if (iTaskResult != null) {
                    try {
                        String optString = new JSONObject(iTaskResult.getBizResult()).optString("ossObjectKey");
                        if (!TextUtils.isEmpty(optString)) {
                            UploadTask.this.resultCallback.onSuccess(optString);
                            try {
                                FileUtil.deleteFile(new File(iUploaderTask.getFilePath()));
                                return;
                            } catch (Throwable th) {
                                th.printStackTrace();
                                return;
                            }
                        }
                    } catch (JSONException unused) {
                    }
                }
                UploadTask.this.resultCallback.onFail("File Url is null");
            }

            public void onFailure(IUploaderTask iUploaderTask, TaskError taskError) {
                TLogAdapter.d(UploadTask.TAG, "onFailure ");
                if (UploadTask.this.resultCallback != null) {
                    ResultCallback access$000 = UploadTask.this.resultCallback;
                    access$000.onFail("onFailure " + taskError.info);
                }
            }

            public void onCancel(IUploaderTask iUploaderTask) {
                TLogAdapter.d(UploadTask.TAG, "onCancel");
                if (UploadTask.this.resultCallback != null) {
                    UploadTask.this.resultCallback.onFail("onCancel");
                }
            }
        }, handler);
    }

    private void init(final Context context) {
        handler = new Handler(Looper.getMainLooper());
        this.uploaderManager = UploaderCreator.get();
        if (!this.uploaderManager.isInitialized()) {
            AnonymousClass3 r0 = new UploaderEnvironmentImpl(context) {
                public String getAppVersion() {
                    try {
                        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                    } catch (Exception unused) {
                        return "1.0.0";
                    }
                }

                public String getAppKey() {
                    return DataProviderFactory.getDataProvider().getAppkey();
                }

                public int getEnvironment() {
                    int envType = DataProviderFactory.getDataProvider().getEnvType();
                    if (envType == LoginEnvType.ONLINE.getSdkEnvType()) {
                        return 0;
                    }
                    if (envType == LoginEnvType.PRE.getSdkEnvType()) {
                        return 1;
                    }
                    if (envType == LoginEnvType.DEV.getSdkEnvType()) {
                        return 2;
                    }
                    return 0;
                }
            };
            r0.setEnvironment(0);
            this.uploaderManager.initialize(context, new UploaderDependencyImpl(context, r0));
        }
    }
}
