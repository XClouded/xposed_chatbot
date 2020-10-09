package com.alibaba.android.update.state;

import android.content.SharedPreferences;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.OnProgressListener;
import com.alibaba.android.update.UpdatePreference;

public abstract class State {
    public ILogger logger;
    public SharedPreferences.Editor mEditor;
    public OnProgressListener mOnProgressListener;
    public UpdateProcessor mProcessor;
    public SharedPreferences mSp;

    public abstract void fail();

    public boolean install(String str, String str2, boolean z) {
        return false;
    }

    public boolean isSuspend() {
        return false;
    }

    public void resume() {
    }

    public void suspend() {
    }

    public void init(UpdateProcessor updateProcessor) {
        this.mProcessor = updateProcessor;
        this.mSp = UpdatePreference.getInstance(this.mProcessor.getContext()).getSharedPreferences();
        this.mEditor = this.mSp.edit();
        this.logger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
    }

    public void download(String str, String str2, boolean z, String str3, String str4, String str5, boolean z2, boolean z3) {
        this.mProcessor.setState(this.mProcessor.getDownloadingState());
    }

    public void finish() {
        this.mProcessor.setState(this.mProcessor.getDownloadedState());
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }
}
