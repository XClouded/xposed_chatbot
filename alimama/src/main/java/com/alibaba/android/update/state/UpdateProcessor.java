package com.alibaba.android.update.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.OnProgressListener;
import com.alibaba.android.update.UpdatePreference;
import com.alibaba.android.update.UpdateUtils;
import com.alibaba.android.update.exception.StateNotFoundException;
import com.alibaba.android.update.proxy.Constants;
import java.io.File;

public class UpdateProcessor {
    private static final String TAG = "UpdateProcessor";
    private ILogger logger = ((ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER));
    private Context mContext;
    private State mCurrentState;
    private State mDownloadedState;
    private State mDownloadingState;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSp;
    private State mUndownloadState;

    public void fail() {
    }

    public UpdateProcessor(Context context) throws StateNotFoundException {
        this.mContext = context;
        ServiceProxy proxy = ServiceProxyFactory.getProxy(Constants.PROXY_UPDATE_CORE);
        this.mUndownloadState = (State) proxy.getService(Constants.UNDOWNLOAD_STATE_SERVICE);
        this.mDownloadingState = (State) proxy.getService(Constants.DOWNLOADING_STATE_SERVICE);
        this.mDownloadedState = (State) proxy.getService(Constants.DOWNLOADED_STATE_SERVICE);
        if (this.mUndownloadState == null) {
            throw new StateNotFoundException("undownload state not found");
        } else if (this.mDownloadingState == null) {
            throw new StateNotFoundException("downloading state not found");
        } else if (this.mDownloadedState != null) {
            this.mUndownloadState.init(this);
            this.mDownloadingState.init(this);
            this.mDownloadedState.init(this);
            this.mSp = UpdatePreference.getInstance(context).getSharedPreferences();
            this.mEditor = this.mSp.edit();
        } else {
            throw new StateNotFoundException("downloaded state not found");
        }
    }

    public void init(String str, String str2, String str3, boolean z) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "StateMachine: url: " + str + ", patchURL: " + str2 + ", md5: " + str3 + ", isVerify: " + z);
        String string = this.mSp.getString(UpdatePreference.KEY_DOWNLOAD_URL, "");
        String string2 = this.mSp.getString(UpdatePreference.KEY_DOWNLOAD_URL_PATCH, "");
        String string3 = this.mSp.getString(UpdatePreference.KEY_DOWNLOADED_FILE_PATH, "");
        boolean z2 = this.mSp.getBoolean(UpdatePreference.KEY_DOWNLOADING, false);
        if (TextUtils.equals(str, string) || TextUtils.equals(str2, string2)) {
            String path = Uri.parse(UpdateUtils.decode(string3)).getPath();
            if (!new File(path).exists()) {
                ILogger iLogger2 = this.logger;
                iLogger2.logd(TAG, "init: state->unkown 本地尚未有下载文件, path: " + path);
                if (z2) {
                    this.logger.logd(TAG, "init: state->downloading 已在下载中，本地尚未下载安装文件");
                    setState(getDownloadingState());
                    return;
                }
                this.logger.logd(TAG, "init: state->undownload 初次未下载");
                setState(getUndownloadState());
            } else if (z) {
                if (UpdateUtils.isValid(path, str3)) {
                    this.logger.logd(TAG, "init: state->downloaded md5校验成功, 文件已下载完成");
                    setState(getDownloadedState());
                } else if (this.mSp.getBoolean(UpdatePreference.KEY_DOWNLOAD_COMPLETE, false)) {
                    this.logger.logd(TAG, "init: state->undownload 文件下载完成, 但是md5校验失败, 很可能文件被串改, 需要重新下载");
                    setState(getUndownloadState());
                } else if (z2) {
                    this.logger.logd(TAG, "init: state->downloading");
                    setState(getDownloadingState());
                } else {
                    this.logger.logd(TAG, "init: state->unknow 状态出错重置为未下载");
                    setState(getUndownloadState());
                }
            } else if (this.mSp.getBoolean(UpdatePreference.KEY_DOWNLOAD_COMPLETE, false)) {
                this.logger.logd(TAG, "init: state->download complete");
                setState(getDownloadedState());
            } else if (z2) {
                this.logger.logd(TAG, "init: state->downloading");
                setState(getDownloadingState());
            } else {
                this.logger.logd(TAG, "init: state->unknow 状态出错重置为未下载");
                setState(getUndownloadState());
            }
        } else {
            this.logger.logd(TAG, "init: state->undownload");
            setState(getUndownloadState());
        }
    }

    public void download(String str, String str2, boolean z, String str3, String str4, String str5, boolean z2, boolean z3, String str6) {
        if (this.mCurrentState == this.mUndownloadState) {
            this.mCurrentState.download(str, str2, z, str3, str4, str5, z2, z3);
        } else if (this.mCurrentState == this.mDownloadedState) {
            this.logger.logd(TAG, "update->UpdateService: ACTION_DOWNLOAD_INSTALL downloaded already");
            String str7 = str6;
            install(str7, z3, str5, z2);
        }
    }

    public void suspend() {
        this.mDownloadingState.suspend();
    }

    public void resume() {
        this.mDownloadingState.resume();
    }

    public void install(String str, boolean z, String str2, boolean z2) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "install - filePath: " + str + ", isSilent: " + z);
        this.mDownloadingState.finish();
        if (z) {
            this.logger.logd(TAG, "静默下载模式不执行安装操作");
        } else {
            this.mDownloadedState.install(str, str2, z2);
        }
    }

    public void install(String str, boolean z) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "install - filePath: " + str + ", isSilent: " + z);
        this.mDownloadingState.finish();
        if (z) {
            this.logger.logd(TAG, "静默下载模式不执行安装操作");
            return;
        }
        this.mDownloadedState.install(str, this.mSp.getString(UpdatePreference.KEY_APK_MD5, ""), this.mSp.getBoolean(UpdatePreference.KEY_APK_MD5_IS_SWITCH_ON, false));
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setState(State state) {
        ILogger iLogger = this.logger;
        StringBuilder sb = new StringBuilder();
        sb.append("setState: ");
        sb.append(state);
        iLogger.logd(TAG, sb.toString() == null ? "" : state.toString());
        this.mCurrentState = state;
    }

    public State getUndownloadState() {
        return this.mUndownloadState;
    }

    public State getDownloadedState() {
        return this.mDownloadedState;
    }

    public State getDownloadingState() {
        return this.mDownloadingState;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        if (this.mCurrentState != null) {
            this.mCurrentState.setOnProgressListener(onProgressListener);
        }
    }
}
