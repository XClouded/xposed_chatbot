package com.taobao.monitor.impl.processor.launcher;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class LinkManagerProcessor extends LauncherProcessor {
    private String b2f;
    private HashMap<String, String> cache = new HashMap<>();

    public LinkManagerProcessor() {
    }

    public LinkManagerProcessor(String str) {
        this.b2f = str;
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        if (!TextUtils.isEmpty(this.b2f)) {
            changeLauncherType(this.b2f);
        }
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map, long j) {
        Intent intent;
        super.onActivityCreated(activity, map, j);
        if ("com.taobao.browser.BrowserActivity".equals(activity.getClass().getName()) && (intent = activity.getIntent()) != null) {
            String dataString = intent.getDataString();
            HashMap<String, String> hashMap = this.cache;
            hashMap.put("" + activity.hashCode(), dataString);
            if (Web302Manager.instance().contains(dataString)) {
                this.launcherActivityName = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isTargetActivity(Activity activity) {
        if (!"com.taobao.browser.BrowserActivity".equals(activity.getClass().getName())) {
            return super.isTargetActivity(activity);
        }
        HashMap<String, String> hashMap = this.cache;
        return !Web302Manager.instance().contains(hashMap.get("" + activity.hashCode()));
    }
}
