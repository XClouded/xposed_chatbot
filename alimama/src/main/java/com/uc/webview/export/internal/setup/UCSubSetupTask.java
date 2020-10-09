package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class UCSubSetupTask<RETURN_TYPE extends UCSubSetupTask<RETURN_TYPE, CALLBACK_TYPE>, CALLBACK_TYPE extends UCSubSetupTask<RETURN_TYPE, CALLBACK_TYPE>> extends UCAsyncTask<RETURN_TYPE, CALLBACK_TYPE> {
    protected static final String OPTION_SHARE_CORE_SETUP_TASK_FLAG = "scst_flag";
    protected ClassLoader mCL;
    public ConcurrentHashMap<String, Object> mOptions = new ConcurrentHashMap<>();
    protected ClassLoader mShellCL;
    protected Pair<String, HashMap<String, String>> mStat;
    protected br mUCM;

    public UCSubSetupTask(UCAsyncTask uCAsyncTask) {
        super(uCAsyncTask);
    }

    public UCSubSetupTask() {
        super((Runnable) null);
    }

    public final RETURN_TYPE setUCM(br brVar) {
        this.mUCM = brVar;
        return this;
    }

    public br getUCM() {
        return this.mUCM;
    }

    public final RETURN_TYPE setClassLoader(ClassLoader classLoader) {
        this.mCL = classLoader;
        return this;
    }

    public final RETURN_TYPE setSdkShellClassLoader(ClassLoader classLoader) {
        this.mShellCL = classLoader;
        return this;
    }

    public RETURN_TYPE setup(String str, Object obj) {
        if (obj == null) {
            this.mOptions.remove(str);
        } else {
            this.mOptions.put(str, obj);
        }
        return this;
    }

    @Reflection
    public final Pair<String, HashMap<String, String>> getStat() {
        return this.mStat;
    }

    /* access modifiers changed from: protected */
    public RETURN_TYPE setOptions(ConcurrentHashMap<String, Object> concurrentHashMap) {
        ConcurrentHashMap<String, Object> concurrentHashMap2 = new ConcurrentHashMap<>();
        concurrentHashMap2.putAll(concurrentHashMap);
        this.mOptions = concurrentHashMap2;
        return this;
    }

    public final Object getOption(String str) {
        return this.mOptions.get(str);
    }

    public final Context getContext() {
        return (Context) getOption("CONTEXT");
    }

    public final void callbackStat(Pair<String, HashMap<String, String>> pair) {
        this.mStat = pair;
        callback(UCCore.EVENT_STAT);
    }

    /* compiled from: U4Source */
    public class a<CB_TYPE extends UCSubSetupTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public a() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCSubSetupTask.this.callbackStat(((UCSubSetupTask) obj).getStat());
        }
    }
}
