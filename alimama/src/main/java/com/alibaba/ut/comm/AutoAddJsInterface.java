package com.alibaba.ut.comm;

import android.app.Activity;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.comm.ActivityLifecycleCB;
import com.alibaba.ut.utils.Logger;
import com.alibaba.ut.utils.ViewTools;
import java.util.HashMap;

public class AutoAddJsInterface implements ActivityLifecycleCB.ActivityResumedCallBack {
    public static AutoAddJsInterface instance = new AutoAddJsInterface();
    private HashMap<String, Boolean> mHasHookMap = new HashMap<>();

    public static AutoAddJsInterface getInstance() {
        return instance;
    }

    public void init() {
        ActivityLifecycleCB.getInstance().addResumedCallback(this);
    }

    public void onActivityResumed(Activity activity) {
        IWebView findWebView = ViewTools.findWebView(activity);
        if (!this.mHasHookMap.containsKey(Integer.valueOf(activity.hashCode()))) {
            if (findWebView != null) {
                findWebView.addJavascriptInterface(new JsBridge(findWebView), "UT4Aplus");
                Logger.i("hook success:", findWebView);
            } else {
                Logger.e("TAG", "cannot found webview");
            }
        }
        HashMap<String, Boolean> hashMap = this.mHasHookMap;
        hashMap.put(activity.hashCode() + "", true);
    }
}
