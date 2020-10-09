package com.ut.mini;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.config.UTClientConfigMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import java.util.List;

class RepeatExposurePageMgr {
    private static final String PAGE_ALL = "a";
    private static final String PAGE_BLACK = "b";
    private static final String PAGE_WHITE = "w";
    private static final String PAGE_WHITE_HTTP_HEAD = "http";
    private static final String REPEAT_EXPOSURE_KEY = "repeatExposure";
    private static final String SP_REPEAT_EXPOSURE_KEY = "repeatExposure";
    private static final String SP_REPEAT_EXPOSURE_NAME = "ut_repeatExposure";
    private static final String TAG = "RepeatExposurePageMgr";
    private static RepeatExposurePageMgr mInstance = new RepeatExposurePageMgr();
    private boolean mAllBlack = false;
    /* access modifiers changed from: private */
    public boolean mGetConfigFromServer = false;
    private boolean mInit = false;
    private List<String> mRepeatExposurePageBlackList;
    private List<String> mRepeatExposurePageWhiteList;

    private RepeatExposurePageMgr() {
    }

    public void init() {
        if (!this.mInit) {
            this.mInit = true;
            TaskExecutor.getInstance().submit(new Runnable() {
                public void run() {
                    synchronized (RepeatExposurePageMgr.this) {
                        if (!RepeatExposurePageMgr.this.mGetConfigFromServer) {
                            String access$100 = RepeatExposurePageMgr.this.getConfigFromSp();
                            Logger.d(RepeatExposurePageMgr.TAG, "getConfigFromSp", access$100);
                            RepeatExposurePageMgr.this.changeConfig(access$100);
                        }
                    }
                }
            });
            UTClientConfigMgr.getInstance().registerConfigChangeListener(new UTClientConfigMgr.IConfigChangeListener() {
                public String getKey() {
                    return "repeatExposure";
                }

                public void onChange(String str) {
                    synchronized (RepeatExposurePageMgr.this) {
                        boolean unused = RepeatExposurePageMgr.this.mGetConfigFromServer = true;
                        Logger.d(RepeatExposurePageMgr.TAG, "getConfigFromServer", str);
                        RepeatExposurePageMgr.this.changeConfig(str);
                        RepeatExposurePageMgr.this.putConfigToSp(str);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void changeConfig(java.lang.String r5) {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            r4.reset()     // Catch:{ Exception -> 0x0051 }
            java.lang.Class<java.util.Map> r2 = java.util.Map.class
            java.lang.Object r5 = com.alibaba.fastjson.JSONObject.parseObject((java.lang.String) r5, r2)     // Catch:{ Exception -> 0x0051 }
            java.util.HashMap r5 = (java.util.HashMap) r5     // Catch:{ Exception -> 0x0051 }
            java.lang.String r2 = "b"
            java.lang.Object r2 = r5.get(r2)     // Catch:{ Exception -> 0x0051 }
            java.util.List r2 = (java.util.List) r2     // Catch:{ Exception -> 0x0051 }
            r4.mRepeatExposurePageBlackList = r2     // Catch:{ Exception -> 0x0051 }
            java.util.List<java.lang.String> r2 = r4.mRepeatExposurePageBlackList     // Catch:{ Exception -> 0x0051 }
            if (r2 == 0) goto L_0x0022
            java.util.List<java.lang.String> r2 = r4.mRepeatExposurePageBlackList     // Catch:{ Exception -> 0x0051 }
            int r2 = r2.size()     // Catch:{ Exception -> 0x0051 }
            goto L_0x0023
        L_0x0022:
            r2 = 0
        L_0x0023:
            java.lang.String r3 = "w"
            java.lang.Object r3 = r5.get(r3)     // Catch:{ Exception -> 0x0052 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ Exception -> 0x0052 }
            r4.mRepeatExposurePageWhiteList = r3     // Catch:{ Exception -> 0x0052 }
            java.lang.String r3 = "a"
            java.lang.Object r5 = r5.get(r3)     // Catch:{ Exception -> 0x0052 }
            java.util.List r5 = (java.util.List) r5     // Catch:{ Exception -> 0x0052 }
            if (r5 == 0) goto L_0x004e
            int r3 = r5.size()     // Catch:{ Exception -> 0x0052 }
            if (r3 != r0) goto L_0x004e
            java.lang.String r3 = "b"
            java.lang.Object r5 = r5.get(r1)     // Catch:{ Exception -> 0x0052 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x0052 }
            boolean r5 = r3.equalsIgnoreCase(r5)     // Catch:{ Exception -> 0x0052 }
            if (r5 == 0) goto L_0x004e
            r4.mAllBlack = r0     // Catch:{ Exception -> 0x0052 }
            goto L_0x0055
        L_0x004e:
            r4.mAllBlack = r1     // Catch:{ Exception -> 0x0052 }
            goto L_0x0055
        L_0x0051:
            r2 = 0
        L_0x0052:
            r4.reset()
        L_0x0055:
            if (r2 >= r0) goto L_0x0063
            boolean r5 = r4.mAllBlack
            if (r5 != 0) goto L_0x0063
            com.ut.mini.RepeatExposureQueueMgr r5 = com.ut.mini.RepeatExposureQueueMgr.getInstance()
            r5.stop()
            goto L_0x006a
        L_0x0063:
            com.ut.mini.RepeatExposureQueueMgr r5 = com.ut.mini.RepeatExposureQueueMgr.getInstance()
            r5.start()
        L_0x006a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.RepeatExposurePageMgr.changeConfig(java.lang.String):void");
    }

    private void reset() {
        try {
            if (this.mRepeatExposurePageBlackList != null) {
                this.mRepeatExposurePageBlackList.clear();
            }
            if (this.mRepeatExposurePageWhiteList != null) {
                this.mRepeatExposurePageWhiteList.clear();
            }
        } catch (Exception unused) {
        }
        this.mAllBlack = false;
    }

    public static RepeatExposurePageMgr getInstance() {
        return mInstance;
    }

    public boolean isRepeatExposurePage(String str) {
        if (TextUtils.isEmpty(str) || str.startsWith("http")) {
            return false;
        }
        try {
            if (this.mRepeatExposurePageBlackList != null && this.mRepeatExposurePageBlackList.contains(str)) {
                return true;
            }
            if (this.mRepeatExposurePageWhiteList == null || !this.mRepeatExposurePageWhiteList.contains(str)) {
                return this.mAllBlack;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public String getConfigFromSp() {
        Context context = ClientVariables.getInstance().getContext();
        if (context == null) {
            return null;
        }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SP_REPEAT_EXPOSURE_NAME, 0);
            if (sharedPreferences != null) {
                return sharedPreferences.getString("repeatExposure", (String) null);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void putConfigToSp(String str) {
        SharedPreferences.Editor edit;
        Context context = ClientVariables.getInstance().getContext();
        if (context != null) {
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences(SP_REPEAT_EXPOSURE_NAME, 0);
                if (sharedPreferences != null && (edit = sharedPreferences.edit()) != null) {
                    edit.putString("repeatExposure", str);
                    edit.apply();
                }
            } catch (Exception unused) {
            }
        }
    }
}
