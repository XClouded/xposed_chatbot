package com.taobao.statistic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTConstants;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTHybridHelper;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.UTTracker;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class TBS {

    public static class CrashHandler {

        public interface OnCrashCaughtListener {
            Arg OnCrashCaught(Thread thread, Throwable th, Arg arg);
        }

        public interface OnDaemonThreadCrashCaughtListener {
            void OnDaemonThreadCrashCaught(Thread thread);
        }

        @Deprecated
        public static void disableEffect() {
        }

        @Deprecated
        public static void removeDaemonCrashCaughtListener(String str) {
        }

        @Deprecated
        public static void setContinueWhenDaemonThreadUncaughException() {
        }

        @Deprecated
        public static void setOnCrashCaughtListener(OnCrashCaughtListener onCrashCaughtListener) {
        }

        @Deprecated
        public static void setOnDaemonCrashCaughtListener(OnDaemonThreadCrashCaughtListener onDaemonThreadCrashCaughtListener) {
        }

        @Deprecated
        public static void setOnDaemonCrashCaughtListener(String str, OnDaemonThreadCrashCaughtListener onDaemonThreadCrashCaughtListener) {
        }

        @Deprecated
        public static void setSubmitToSystemFlag() {
        }

        @Deprecated
        public static void setToastStyle(int i, String str) {
        }

        @Deprecated
        public static void turnOff() {
        }

        @Deprecated
        public static void withRestart(Activity activity, int i, int i2) {
        }
    }

    public static class Network {
        @Deprecated
        public static void download(String str, String str2, long j, long j2, String str3, String str4, boolean z) {
        }

        @Deprecated
        public static void download(String str, String str2, long j, long j2, boolean z) {
        }

        @Deprecated
        public static void pushArrive(String str) {
        }

        @Deprecated
        public static void pushDisplay(String str) {
        }

        @Deprecated
        public static void pushView(String str) {
        }

        @Deprecated
        public static void searchKeyword(String str, String str2) {
        }

        @Deprecated
        public static void updateUTCookie(String str, Map<String, String> map) {
        }

        @Deprecated
        public static void updateUTSIDToCookie(String str) {
        }

        @Deprecated
        public static void weiboShare(String str, String str2) {
        }
    }

    @Deprecated
    public interface OnInitFinishListener {
        void onFinish(int i);
    }

    @Deprecated
    public static void init() {
    }

    @Deprecated
    public static void setChannel(String str) {
    }

    @Deprecated
    public static void setEnvironment(Context context) {
    }

    @Deprecated
    public static void setEnvironment(Context context, String str) {
    }

    @Deprecated
    public static void setKey(String str, String str2) {
    }

    @Deprecated
    public static void setVersion(String str) {
    }

    @Deprecated
    public static void swithFromSimplePipeLineToCommon(boolean z) {
    }

    @Deprecated
    public static void trade(String str) {
    }

    @Deprecated
    public static void turnDebug() {
    }

    @Deprecated
    public static void turnOnSecuritySDKSupport() {
    }

    @Deprecated
    public static void uninit() {
    }

    public static class DelayEventObject {
        private String mBeginOrEnd = "begin";
        private Properties mProperties = null;
        private long mTimestamp = 0;

        public void setTimestamp(long j) {
            this.mTimestamp = j;
        }

        public long getTimestamp() {
            return this.mTimestamp;
        }

        public void setProperties(Properties properties) {
            this.mProperties = properties;
        }

        public Properties getProperties() {
            return this.mProperties;
        }

        public void setBegin() {
            this.mBeginOrEnd = "begin";
        }

        public void setEnd() {
            this.mBeginOrEnd = "end";
        }

        public boolean isBegin() {
            return this.mBeginOrEnd.equals("begin");
        }
    }

    public static void updateUserAccount(String str) {
        UTAnalytics.getInstance().updateUserAccount(str, (String) null);
    }

    public static void updateUserAccount(String str, String str2) {
        UTAnalytics.getInstance().updateUserAccount(str, str2);
    }

    public static void updateGPSInfo(String str, double d, double d2) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(str, 1005, "" + d, "" + d2, (String) null, (Map<String, String>) null).build());
    }

    public static void updateSessionProperties(Properties properties) {
        UTAnalytics.getInstance().updateSessionProperties(MapUtils.convertPropertiesToMap(properties));
    }

    static void updateNextPageProperties(Properties properties) {
        UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(MapUtils.convertPropertiesToMap(properties));
    }

    public static void userRegister(String str) {
        UTAnalytics.getInstance().userRegister(str);
    }

    public static class Ext {
        private static final String PAGE_EXTEND = "Page_Extend";
        private static Object lCommitEventLockObj = new Object();
        private static HashMap<String, DelayEventObject> mDelayEventObject = new HashMap<>();

        public static void commitEvent(String str, Properties properties) {
            if (!StringUtils.isEmpty(str)) {
                UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str);
                uTCustomHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
                UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
                if (defaultTracker != null) {
                    defaultTracker.send(uTCustomHitBuilder.build());
                    return;
                }
                Logger.w("TBS", "please call UTAnalytics.getInstance().setAppApplicationInstance() before this method");
            }
        }

        public static void commitEventBegin(String str, Properties properties) {
            if (!StringUtils.isEmpty(str)) {
                synchronized (lCommitEventLockObj) {
                    if (mDelayEventObject.size() > 2000) {
                        mDelayEventObject.clear();
                    }
                    if (mDelayEventObject.containsKey(str)) {
                        mDelayEventObject.remove(str);
                    }
                    DelayEventObject delayEventObject = new DelayEventObject();
                    delayEventObject.setTimestamp(System.currentTimeMillis());
                    delayEventObject.setProperties(properties);
                    delayEventObject.setBegin();
                    mDelayEventObject.put(str, delayEventObject);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0097, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void commitEventEnd(java.lang.String r8, java.util.Properties r9) {
            /*
                boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r8)
                if (r0 == 0) goto L_0x0007
                return
            L_0x0007:
                java.lang.Object r0 = lCommitEventLockObj
                monitor-enter(r0)
                java.util.HashMap<java.lang.String, com.taobao.statistic.TBS$DelayEventObject> r1 = mDelayEventObject     // Catch:{ all -> 0x0098 }
                boolean r1 = r1.containsKey(r8)     // Catch:{ all -> 0x0098 }
                if (r1 == 0) goto L_0x0096
                java.util.HashMap<java.lang.String, com.taobao.statistic.TBS$DelayEventObject> r1 = mDelayEventObject     // Catch:{ all -> 0x0098 }
                java.lang.Object r1 = r1.get(r8)     // Catch:{ all -> 0x0098 }
                com.taobao.statistic.TBS$DelayEventObject r1 = (com.taobao.statistic.TBS.DelayEventObject) r1     // Catch:{ all -> 0x0098 }
                java.util.HashMap<java.lang.String, com.taobao.statistic.TBS$DelayEventObject> r2 = mDelayEventObject     // Catch:{ all -> 0x0098 }
                r2.remove(r8)     // Catch:{ all -> 0x0098 }
                if (r1 == 0) goto L_0x0096
                boolean r2 = r1.isBegin()     // Catch:{ all -> 0x0098 }
                if (r2 == 0) goto L_0x0096
                java.util.Properties r2 = r1.getProperties()     // Catch:{ all -> 0x0098 }
                java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x0098 }
                r3.<init>()     // Catch:{ all -> 0x0098 }
                if (r2 == 0) goto L_0x0039
                java.util.Map r2 = com.alibaba.analytics.utils.MapUtils.convertPropertiesToMap(r2)     // Catch:{ all -> 0x0098 }
                r3.putAll(r2)     // Catch:{ all -> 0x0098 }
            L_0x0039:
                if (r9 == 0) goto L_0x0042
                java.util.Map r9 = com.alibaba.analytics.utils.MapUtils.convertPropertiesToMap(r9)     // Catch:{ all -> 0x0098 }
                r3.putAll(r9)     // Catch:{ all -> 0x0098 }
            L_0x0042:
                java.util.HashMap r9 = new java.util.HashMap     // Catch:{ all -> 0x0098 }
                r9.<init>()     // Catch:{ all -> 0x0098 }
                com.alibaba.analytics.core.model.LogField r2 = com.alibaba.analytics.core.model.LogField.EVENTID     // Catch:{ all -> 0x0098 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0098 }
                java.lang.String r4 = "19999"
                r9.put(r2, r4)     // Catch:{ all -> 0x0098 }
                com.alibaba.analytics.core.model.LogField r2 = com.alibaba.analytics.core.model.LogField.ARG1     // Catch:{ all -> 0x0098 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0098 }
                r9.put(r2, r8)     // Catch:{ all -> 0x0098 }
                com.alibaba.analytics.core.model.LogField r8 = com.alibaba.analytics.core.model.LogField.ARG3     // Catch:{ all -> 0x0098 }
                java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0098 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0098 }
                r2.<init>()     // Catch:{ all -> 0x0098 }
                java.lang.String r4 = ""
                r2.append(r4)     // Catch:{ all -> 0x0098 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0098 }
                long r6 = r1.getTimestamp()     // Catch:{ all -> 0x0098 }
                r1 = 0
                long r4 = r4 - r6
                r2.append(r4)     // Catch:{ all -> 0x0098 }
                java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0098 }
                r9.put(r8, r1)     // Catch:{ all -> 0x0098 }
                r9.putAll(r3)     // Catch:{ all -> 0x0098 }
                java.lang.String r8 = "_bmbu"
                java.lang.String r1 = "yes"
                r9.put(r8, r1)     // Catch:{ all -> 0x0098 }
                com.ut.mini.UTAnalytics r8 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ all -> 0x0098 }
                com.ut.mini.UTTracker r8 = r8.getDefaultTracker()     // Catch:{ all -> 0x0098 }
                r8.send(r9)     // Catch:{ all -> 0x0098 }
                monitor-exit(r0)     // Catch:{ all -> 0x0098 }
                return
            L_0x0096:
                monitor-exit(r0)     // Catch:{ all -> 0x0098 }
                return
            L_0x0098:
                r8 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0098 }
                throw r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.statistic.TBS.Ext.commitEventEnd(java.lang.String, java.util.Properties):void");
        }

        public static void commitEvent(String str, int i) {
            commitEvent(str, i, (Object) null, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(String str, int i, Object obj) {
            commitEvent(str, i, obj, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(String str, int i, Object obj, Object obj2) {
            commitEvent(str, i, obj, obj2, (Object) null, (String[]) null);
        }

        public static void commitEvent(String str, int i, Object obj, Object obj2, Object obj3) {
            commitEvent(str, i, obj, obj2, obj3, (String[]) null);
        }

        /* access modifiers changed from: private */
        public static String _convertStringAToKVSString(String... strArr) {
            if (strArr != null && strArr.length == 0) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            if (strArr != null && strArr.length > 0) {
                boolean z = false;
                for (int i = 0; i < strArr.length; i++) {
                    if (!StringUtils.isEmpty(strArr[i])) {
                        if (z) {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append(strArr[i]);
                        z = true;
                    }
                }
            }
            return stringBuffer.toString();
        }

        public static void commitEvent(String str, int i, Object obj, Object obj2, Object obj3, String... strArr) {
            String _convertStringAToKVSString = _convertStringAToKVSString(strArr);
            HashMap hashMap = new HashMap();
            hashMap.put(LogField.PAGE.toString(), str);
            String logField = LogField.EVENTID.toString();
            hashMap.put(logField, "" + i);
            hashMap.put(LogField.ARG1.toString(), StringUtils.convertObjectToString(obj));
            hashMap.put(LogField.ARG2.toString(), StringUtils.convertObjectToString(obj2));
            hashMap.put(LogField.ARG3.toString(), StringUtils.convertObjectToString(obj3));
            if (_convertStringAToKVSString != null) {
                hashMap.put(LogField.ARGS.toString(), _convertStringAToKVSString);
            }
            hashMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
            UTAnalytics.getInstance().getDefaultTracker().send(hashMap);
        }

        public static void commitEvent(int i) {
            commitEvent("Page_Extend", i, (Object) null, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(int i, Object obj) {
            commitEvent("Page_Extend", i, obj, (Object) null, (Object) null, (String[]) null);
        }

        public static void commitEvent(int i, Object obj, Object obj2) {
            commitEvent("Page_Extend", i, obj, obj2, (Object) null, (String[]) null);
        }

        public static void commitEvent(int i, Object obj, Object obj2, Object obj3) {
            commitEvent("Page_Extend", i, obj, obj2, obj3, (String[]) null);
        }

        public static void commitEvent(int i, Object obj, Object obj2, Object obj3, String... strArr) {
            commitEvent("Page_Extend", i, obj, obj2, obj3, strArr);
        }
    }

    public static class Page {
        @Deprecated
        public static void create(String str) {
        }

        @Deprecated
        public static void create(String str, String str2) {
        }

        @Deprecated
        public static void destroy(String str) {
        }

        @Deprecated
        public static void enter(String str) {
        }

        @Deprecated
        public static void enterWithPageName(String str, String str2) {
        }

        @Deprecated
        public static void goBack() {
        }

        @Deprecated
        public static void leave(String str) {
        }

        @Deprecated
        public static void updatePageName(String str, String str2) {
        }

        @Deprecated
        public static void updatePageProperties(String str, Properties properties) {
        }

        public static void ctrlClicked(CT ct, String str) {
            _commitCtrlEvent((String) null, 2101, ct, str, 0, new String[0]);
        }

        private static String _getCurPageName() {
            String currentPageName = UTPageHitHelper.getInstance().getCurrentPageName();
            if (currentPageName == null || currentPageName.startsWith("Page_")) {
                return currentPageName;
            }
            return "Page_" + currentPageName;
        }

        private static String _calPageName(String str) {
            if (str == null || str.startsWith("Page_")) {
                return str;
            }
            return "Page_" + str;
        }

        private static String _calControlName(String str, CT ct, String str2) {
            if (str == null || str2 == null) {
                return null;
            }
            if (!str.startsWith("Page_")) {
                str = "Page_" + str;
            }
            return str + "_" + ct.toString() + "-" + str2;
        }

        /* access modifiers changed from: private */
        public static void _commitCtrlEvent(String str, int i, CT ct, String str2, int i2, String... strArr) {
            String str3;
            if (str == null) {
                str3 = _getCurPageName();
            } else {
                str3 = _calPageName(str);
            }
            if (!StringUtils.isEmpty(str3)) {
                String _calControlName = _calControlName(str3, ct, str2);
                if (!StringUtils.isEmpty(_calControlName)) {
                    String access$000 = Ext._convertStringAToKVSString(strArr);
                    HashMap hashMap = new HashMap();
                    hashMap.put(LogField.PAGE.toString(), str3);
                    String logField = LogField.EVENTID.toString();
                    hashMap.put(logField, "" + i);
                    hashMap.put(LogField.ARG1.toString(), _calControlName);
                    hashMap.put(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT, "yes");
                    if (i == 2102) {
                        String logField2 = LogField.ARG3.toString();
                        hashMap.put(logField2, "" + i2);
                    }
                    if (access$000 != null) {
                        hashMap.put(LogField.ARGS.toString(), access$000);
                    }
                    UTAnalytics.getInstance().getDefaultTracker().send(hashMap);
                }
            }
        }

        public static void buttonClicked(String str) {
            ctrlClicked(CT.Button, str);
        }

        public static void ctrlLongClicked(CT ct, String str) {
            _commitCtrlEvent((String) null, 2103, ct, str, 0, new String[0]);
        }

        public static void itemSelected(CT ct, String str, int i) {
            _commitCtrlEvent((String) null, 2102, ct, str, i, new String[0]);
        }
    }

    public static class Adv {
        @Deprecated
        public static void destroy(String str, String... strArr) {
        }

        @Deprecated
        public static void easyTraceEnter(String str, boolean z, String... strArr) {
        }

        @Deprecated
        public static void easyTraceInternalCtrlClick(String str, String str2, String... strArr) {
        }

        @Deprecated
        public static void easyTraceLeave(String str, boolean z, String... strArr) {
        }

        @Deprecated
        public static void enter(String str, String... strArr) {
        }

        @Deprecated
        public static void enterWithPageName(String str, String str2, String... strArr) {
        }

        @Deprecated
        public static void keepKvs(String str, String... strArr) {
        }

        @Deprecated
        public static void leave(String str, String... strArr) {
        }

        @Deprecated
        public static void onCaughException(Throwable th) {
        }

        @Deprecated
        public static void putKvs(String str, Object obj) {
        }

        @Deprecated
        public static void turnOffLogFriendly() {
        }

        @Deprecated
        public static void unKeepKvs(String str, String... strArr) {
        }

        /* access modifiers changed from: package-private */
        @Deprecated
        public void forceUpload() {
        }

        public static void ctrlClicked(String str, CT ct, String str2, String... strArr) {
            Page._commitCtrlEvent(str, 2101, ct, str2, 0, strArr);
        }

        @Deprecated
        public static String getUtsid() {
            return UTTeamWork.getInstance().getUtsid();
        }

        public static void ctrlClicked(CT ct, String str, String... strArr) {
            Page._commitCtrlEvent((String) null, 2101, ct, str, 0, strArr);
        }

        public static void ctrlClickedOnPage(String str, CT ct, String str2, String... strArr) {
            Page._commitCtrlEvent(str, 2101, ct, str2, 0, strArr);
        }

        public static void ctrlLongClicked(String str, CT ct, String str2, String... strArr) {
            Page._commitCtrlEvent(str, 2103, ct, str2, 0, strArr);
        }

        public static void ctrlLongClicked(CT ct, String str, String... strArr) {
            Page._commitCtrlEvent((String) null, 2103, ct, str, 0, strArr);
        }

        public static void ctrlLongClickedOnPage(String str, CT ct, String str2, String... strArr) {
            Page._commitCtrlEvent(str, 2103, ct, str2, 0, strArr);
        }

        public static void itemSelected(String str, CT ct, String str2, int i, String... strArr) {
            Page._commitCtrlEvent(str, 2102, ct, str2, i, strArr);
        }

        public static void itemSelected(CT ct, String str, int i, String... strArr) {
            Page._commitCtrlEvent((String) null, 2102, ct, str, i, strArr);
        }

        public static void itemSelectedOnPage(String str, CT ct, String str2, int i, String... strArr) {
            Page._commitCtrlEvent(str, 2102, ct, str2, i, strArr);
        }

        public static void ctrlClicked(String str, CT ct, String str2) {
            Page._commitCtrlEvent(str, 2101, ct, str2, 0, new String[0]);
        }

        public static void ctrlClickedOnPage(String str, CT ct, String str2) {
            Page._commitCtrlEvent(str, 2101, ct, str2, 0, new String[0]);
        }

        public static void ctrlLongClicked(String str, CT ct, String str2) {
            Page._commitCtrlEvent(str, 2103, ct, str2, 0, new String[0]);
        }

        public static void ctrlLongClickedOnPage(String str, CT ct, String str2) {
            Page._commitCtrlEvent(str, 2103, ct, str2, 0, new String[0]);
        }

        public static void itemSelected(String str, CT ct, String str2, int i) {
            Page._commitCtrlEvent(str, 2102, ct, str2, i, new String[0]);
        }

        public static void itemSelectedOnPage(String str, CT ct, String str2, int i) {
            Page._commitCtrlEvent(str, 2103, ct, str2, i, new String[0]);
        }
    }

    public static class EasyTrace {
        @Deprecated
        public static void easyTraceCtrlClickManual(View view, String str, String... strArr) {
        }

        @Deprecated
        public static void easyTraceCtrlClickManual(View view, String... strArr) {
        }

        @Deprecated
        public static void easyTraceUpdatePageNameManual(String str, String str2) {
        }

        @Deprecated
        public static Runtime getUserTrackRuntime() {
            return null;
        }

        public static void easyTraceEnterManual(Activity activity, String str) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(activity);
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, str);
        }

        public static void easyTraceLeaveManual(Activity activity, String str) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, str);
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
        }

        public static void easyTraceUpdatePageNameManual(Activity activity, String str) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, str);
        }

        @TargetApi(11)
        public static void easyTraceFragmentEnterManual(Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(fragment);
        }

        @TargetApi(11)
        public static void easyTraceFragmentLeaveManual(Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(fragment);
        }

        public static void easyTraceFragmentV4EnterManual(androidx.fragment.app.Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(fragment);
        }

        public static void easyTraceFragmentV4LeaveManual(androidx.fragment.app.Fragment fragment) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(fragment);
        }

        @Deprecated
        public static void updateEasyTraceActivityProperties(Activity activity, Properties properties) {
            if (UTAnalytics.getInstance().getDefaultTracker() != null) {
                UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, MapUtils.convertPropertiesToMap(properties));
            }
        }

        @Deprecated
        public static void updateEasyTraceActivityPropertiesManual(Activity activity, String str, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, MapUtils.convertPropertiesToMap(properties));
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(activity, str);
        }

        @TargetApi(11)
        public static void updateEasyTraceFragmentProperties(Fragment fragment, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(fragment, MapUtils.convertPropertiesToMap(properties));
        }

        @Deprecated
        public static void updateEasyTraceFragmentV4Properties(androidx.fragment.app.Fragment fragment, Properties properties) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(fragment, MapUtils.convertPropertiesToMap(properties));
        }
    }

    @Deprecated
    public static void h5UT(String str, WebView webView) {
        h5UT((String) null, str, webView);
    }

    private static void h5UT(String str, String str2, Object obj) {
        HashMap hashMap = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str2);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (!StringUtils.isEmpty(next)) {
                    String string = jSONObject.getString(next);
                    if (!StringUtils.isEmpty(string)) {
                        hashMap.put(next, string);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (obj != null) {
            UTHybridHelper.getInstance().h5UT(hashMap, obj);
        }
    }

    public static void setH5Url(String str) {
        UTHybridHelper.getInstance().setH5Url(str);
    }

    @Deprecated
    public static synchronized void updateSharedProprety(String str, String str2) {
        synchronized (TBS.class) {
        }
    }

    @Deprecated
    public static synchronized String getSharedProperty(String str) {
        synchronized (TBS.class) {
        }
        return null;
    }

    @Deprecated
    public static void h5UT(String str, Context context) {
        h5UT((String) null, str, context);
    }
}
