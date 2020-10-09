package com.ut.mini;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.core.config.UTTPKItem;
import com.alibaba.analytics.core.model.LogConstant;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.model.UTMCLogFields;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.ut.abtest.track.TrackUTPlugin;
import com.ut.mini.UTConstants;
import com.ut.mini.exposure.ExposureUtils;
import com.ut.mini.exposure.TrackerFrameLayout;
import com.ut.mini.module.UTOperationStack;
import com.ut.mini.module.plugin.IUTPluginForEachDelegate;
import com.ut.mini.module.plugin.UTPlugin;
import com.ut.mini.module.plugin.UTPluginMgr;
import com.ut.mini.module.trackerlistener.UTTrackerListenerMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class UTTracker {
    public static final int PAGE_STATUS_CODE_302 = 1;
    /* access modifiers changed from: private */
    public static List<String> s_logfield_cache;
    private static Pattern s_p = Pattern.compile("(\\|\\||[\t\r\n]|\u0001|\u0000)+");
    private String mAppkey = null;
    private Map<String, String> mArgsMap = new ConcurrentHashMap();
    private String mTrackerId = null;

    static {
        s_logfield_cache = null;
        s_logfield_cache = new ArrayList(34);
        for (LogField valueOf : LogField.values()) {
            s_logfield_cache.add(String.valueOf(valueOf).toLowerCase());
        }
    }

    /* access modifiers changed from: package-private */
    public void setTrackId(String str) {
        this.mTrackerId = str;
    }

    public synchronized void setGlobalProperty(String str, String str2) {
        if (StringUtils.isEmpty(str) || str2 == null) {
            Logger.e("setGlobalProperty", "key is null or key is empty or value is null,please check it!");
        } else {
            this.mArgsMap.put(str, str2);
        }
    }

    public synchronized String getGlobalProperty(String str) {
        if (str == null) {
            return null;
        }
        return this.mArgsMap.get(str);
    }

    public synchronized void removeGlobalProperty(String str) {
        if (str != null) {
            if (this.mArgsMap.containsKey(str)) {
                this.mArgsMap.remove(str);
            }
        }
    }

    private static String getStringNoBlankAndDLine(String str) {
        return (str == null || "".equals(str)) ? str : s_p.matcher(str).replaceAll("");
    }

    private static String checkField(String str) {
        return getStringNoBlankAndDLine(str);
    }

    private Map<String, String> checkMapFields(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator<String> it = map.keySet().iterator();
        if (it != null) {
            while (it.hasNext()) {
                try {
                    String next = it.next();
                    if (next != null) {
                        hashMap.put(next, checkField(map.get(next)));
                    }
                } catch (Throwable th) {
                    Logger.e("[checkMapFields]", th, new Object[0]);
                }
            }
        }
        return hashMap;
    }

    public void send(Map<String, String> map) {
        HashMap hashMap;
        String str;
        UTTrackerListenerMgr.getInstance().send(this, map);
        if (map != null) {
            try {
                int intValue = Integer.valueOf(map.get(LogField.EVENTID.toString())).intValue();
                if (map.containsKey(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT)) {
                    map.remove(UTConstants.PrivateLogFields.FLAG_BUILD_MAP_BY_UT);
                    hashMap = map;
                } else {
                    hashMap = new HashMap();
                }
                hashMap.putAll(this.mArgsMap);
                if (hashMap != map) {
                    hashMap.putAll(map);
                }
                if (!StringUtils.isEmpty(this.mTrackerId)) {
                    hashMap.put("_track_id", this.mTrackerId);
                }
                if (ClientVariables.getInstance().isAliyunOSPlatform()) {
                    hashMap.put(UTMCLogFields.ALIYUN_PLATFORM_FLAG.toString(), "yes");
                }
                String timestamp = ClientVariables.getInstance().getTimestamp();
                if (!StringUtils.isEmpty(timestamp)) {
                    hashMap.put(LogConstant.UTPVID_T, timestamp);
                }
                if (!map.containsKey(UTConstants.PrivateLogFields.FLAG_USE_ALL_MAP_FIELDS)) {
                    dropAllIllegalKey(hashMap);
                } else {
                    hashMap.remove(UTConstants.PrivateLogFields.FLAG_USE_ALL_MAP_FIELDS);
                }
                translateFieldsName(hashMap);
                fillReserve1Fields(hashMap);
                fillReservesFields(hashMap);
                if (intValue == 2101 || intValue == 2102) {
                    UTOperationStack instance = UTOperationStack.getInstance();
                    instance.addAction("ctrlClicked:" + map.get(LogField.ARG1.toString()));
                    hashMap.put("_priority", "4");
                }
                if (UTPluginMgr.getInstance().isOpen()) {
                    final String str2 = map.get(LogField.PAGE.toString());
                    final String str3 = map.get(LogField.ARG1.toString());
                    final String str4 = map.get(LogField.ARG2.toString());
                    final String str5 = map.get(LogField.ARG3.toString());
                    final int i = intValue;
                    final Map<String, String> map2 = map;
                    final Map<String, String> map3 = hashMap;
                    UTPluginMgr.getInstance().forEachPlugin(new IUTPluginForEachDelegate() {
                        public void onPluginForEach(UTPlugin uTPlugin) {
                            Map<String, String> onEventDispatch;
                            if (UTPlugin.isEventIDInRange(uTPlugin.getAttentionEventIds(), i) && (onEventDispatch = uTPlugin.onEventDispatch(str2, i, str3, str4, str5, map2)) != null && onEventDispatch.size() > 0) {
                                String str = "";
                                for (String next : onEventDispatch.keySet()) {
                                    if (UTTracker.s_logfield_cache.contains(String.valueOf(next).toLowerCase())) {
                                        onEventDispatch.remove(next);
                                    }
                                    if (TrackUTPlugin.UT_PARAM.equals(next)) {
                                        str = onEventDispatch.get(next);
                                    }
                                }
                                if (!TextUtils.isEmpty(str)) {
                                    onEventDispatch.put(TrackUTPlugin.UT_PARAM, UTPageHitHelper.getInstance().refreshUtParam(str, (String) map3.get(TrackUTPlugin.UT_PARAM)));
                                }
                                map3.putAll(onEventDispatch);
                            }
                        }
                    });
                }
                if (intValue == 2001) {
                    UTPageHitHelper.encodeUtParam(hashMap);
                }
                UTPvidHelper.processOtherPvid(intValue, hashMap);
                if (intValue == 2201) {
                    try {
                        str = map.get(LogField.PAGE.toString());
                    } catch (Exception unused) {
                        str = "";
                    }
                    if (RepeatExposurePageMgr.getInstance().isRepeatExposurePage(str)) {
                        RepeatExposureQueueMgr.getInstance().putExposureEvent(hashMap);
                        return;
                    }
                }
                UTAnalytics.getInstance().transferLog(hashMap);
            } catch (Exception unused2) {
            }
        }
    }

    private static void dropAllIllegalKey(Map<String, String> map) {
        if (map != null) {
            if (map.containsKey(LogField.IMEI.toString())) {
                map.remove(LogField.IMEI.toString());
            }
            if (map.containsKey(LogField.IMSI.toString())) {
                map.remove(LogField.IMSI.toString());
            }
            if (map.containsKey(LogField.CARRIER.toString())) {
                map.remove(LogField.CARRIER.toString());
            }
            if (map.containsKey(LogField.ACCESS.toString())) {
                map.remove(LogField.ACCESS.toString());
            }
            if (map.containsKey(LogField.ACCESS_SUBTYPE.toString())) {
                map.remove(LogField.ACCESS_SUBTYPE.toString());
            }
            if (map.containsKey(LogField.CHANNEL.toString())) {
                map.remove(LogField.CHANNEL.toString());
            }
            if (map.containsKey(LogField.LL_USERNICK.toString())) {
                map.remove(LogField.LL_USERNICK.toString());
            }
            if (map.containsKey(LogField.USERNICK.toString())) {
                map.remove(LogField.USERNICK.toString());
            }
            if (map.containsKey(LogField.LL_USERID.toString())) {
                map.remove(LogField.LL_USERID.toString());
            }
            if (map.containsKey(LogField.USERID.toString())) {
                map.remove(LogField.USERID.toString());
            }
            if (map.containsKey(LogField.SDKVERSION.toString())) {
                map.remove(LogField.SDKVERSION.toString());
            }
            if (map.containsKey(LogField.START_SESSION_TIMESTAMP.toString())) {
                map.remove(LogField.START_SESSION_TIMESTAMP.toString());
            }
            if (map.containsKey(LogField.UTDID.toString())) {
                map.remove(LogField.UTDID.toString());
            }
            if (map.containsKey(LogField.SDKTYPE.toString())) {
                map.remove(LogField.SDKTYPE.toString());
            }
            if (map.containsKey(LogField.RESERVE2.toString())) {
                map.remove(LogField.RESERVE2.toString());
            }
            if (map.containsKey(LogField.RESERVE3.toString())) {
                map.remove(LogField.RESERVE3.toString());
            }
            if (map.containsKey(LogField.RESERVE4.toString())) {
                map.remove(LogField.RESERVE4.toString());
            }
            if (map.containsKey(LogField.RESERVE5.toString())) {
                map.remove(LogField.RESERVE5.toString());
            }
            if (map.containsKey(LogField.RESERVES.toString())) {
                map.remove(LogField.RESERVES.toString());
            }
        }
    }

    private static void translateFieldsName(Map<String, String> map) {
        if (map != null) {
            if (map.containsKey("_field_os")) {
                map.remove("_field_os");
                map.put(LogField.OS.toString(), map.get("_field_os"));
            }
            if (map.containsKey("_field_os_version")) {
                map.remove("_field_os_version");
                map.put(LogField.OSVERSION.toString(), map.get("_field_os_version"));
            }
        }
    }

    private void fillReserve1Fields(Map<String, String> map) {
        map.put(LogField.SDKTYPE.toString(), Constants.SDK_TYPE);
        if (!TextUtils.isEmpty(this.mAppkey)) {
            map.put(LogField.APPKEY.toString(), this.mAppkey);
        } else {
            map.put(LogField.APPKEY.toString(), ClientVariables.getInstance().getAppKey());
        }
    }

    private static void fillReservesFields(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        if (map.containsKey("_track_id")) {
            String str = map.get("_track_id");
            map.remove("_track_id");
            if (!StringUtils.isEmpty(str)) {
                hashMap.put("_tkid", str);
            }
        }
        if (hashMap.size() > 0) {
            map.put(LogField.RESERVES.toString(), StringUtils.convertMapToString(hashMap));
        }
        if (!map.containsKey(LogField.PAGE.toString())) {
            map.put(LogField.PAGE.toString(), "UT");
        }
    }

    public void pageAppear(Object obj) {
        pageAppear(obj, (String) null, false);
    }

    public void pageAppear(Object obj, String str) {
        pageAppear(obj, str, false);
    }

    public void pageAppearDonotSkip(Object obj) {
        pageAppear(obj, (String) null, true);
    }

    public void pageAppearDonotSkip(Object obj, String str) {
        pageAppear(obj, str, true);
    }

    public void pageAppear(Object obj, String str, boolean z) {
        UTPageHitHelper.getInstance().pageAppear(obj, str, z);
    }

    public void pageDisAppear(Object obj) {
        UTPageHitHelper.getInstance().pageDisAppear(obj, this);
    }

    public void updateNextPageProperties(Map<String, String> map) {
        UTTrackerListenerMgr.getInstance().updateNextPageProperties(this, map);
        UTPageHitHelper.getInstance().updateNextPageProperties(map);
    }

    public void updateNextPageUtparam(String str) {
        UTTrackerListenerMgr.getInstance().updateNextPageUtparam(str);
        UTPageHitHelper.getInstance().updateNextPageUtparam(str);
    }

    public void updateNextPageUtparamCnt(String str) {
        UTPageHitHelper.getInstance().updateNextPageUtparamCnt(str);
    }

    public void setPageStatusCode(Object obj, int i) {
        UTPageHitHelper.getInstance().setPageStatusCode(obj, i);
    }

    public void updatePageName(Object obj, String str) {
        UTTrackerListenerMgr.getInstance().updatePageName(this, obj, str);
        UTPageHitHelper.getInstance().updatePageName(obj, str);
    }

    public void updatePageProperties(Object obj, Map<String, String> map) {
        UTTrackerListenerMgr.getInstance().updatePageProperties(this, obj, map);
        UTPageHitHelper.getInstance().updatePageProperties(obj, map);
    }

    public Map<String, String> getPageProperties(Object obj) {
        return UTPageHitHelper.getInstance().getPageProperties(obj);
    }

    public void updatePageUtparam(Object obj, String str) {
        UTTrackerListenerMgr.getInstance().updatePageUtparam(obj, str);
        UTPageHitHelper.getInstance().updatePageUtparam(obj, str);
    }

    public void updatePageStatus(Object obj, UTPageStatus uTPageStatus) {
        UTPageHitHelper.getInstance().updatePageStatus(obj, uTPageStatus);
    }

    public void skipPageBack(Activity activity) {
        UTPageHitHelper.getInstance().skipBack(activity);
    }

    public void skipNextPageBack() {
        UTPageHitHelper.getInstance().skipNextPageBack();
    }

    @Deprecated
    public void skipPageBackForever(Activity activity, boolean z) {
        UTPageHitHelper.getInstance().skipBackForever(activity, z);
    }

    public String getPageSpmUrl(Activity activity) {
        return UTPageHitHelper.getInstance().getPageSpmUrl(activity);
    }

    public String getPageSpmPre(Activity activity) {
        return UTPageHitHelper.getInstance().getPageSpmPre(activity);
    }

    public Map<String, String> getPageAllProperties(Activity activity) {
        return UTPageHitHelper.getInstance().getPageAllProperties(activity);
    }

    public String getPageScmPre(Activity activity) {
        return UTPageHitHelper.getInstance().getPageScmPre(activity);
    }

    public void updatePageUrl(Object obj, Uri uri) {
        UTPageHitHelper.getInstance().updatePageUrl(obj, uri);
    }

    public void addTPKItem(UTTPKItem uTTPKItem) {
        UTTPKBiz.getInstance().addTPKItem(uTTPKItem);
    }

    public void addTPKCache(String str, String str2) {
        UTTPKBiz.getInstance().addTPKCache(str, str2);
    }

    public void skipPage(Object obj) {
        UTPageHitHelper.getInstance().skipPage(obj);
    }

    /* access modifiers changed from: protected */
    public void setAppKey(String str) {
        this.mAppkey = str;
    }

    /* access modifiers changed from: protected */
    public void setH5Url(String str) {
        if (str != null) {
            UTVariables.getInstance().setH5Url(str);
        }
    }

    public void setExposureTag(View view, String str, String str2, Map<String, String> map) {
        ExposureUtils.setExposure(view, str, str2, map);
    }

    public void refreshExposureData() {
        TrackerFrameLayout.refreshExposureData();
    }

    public void refreshExposureData(String str) {
        TrackerFrameLayout.refreshExposureData(str);
    }

    public void refreshExposureDataByViewId(String str, String str2) {
        TrackerFrameLayout.refreshExposureDataByViewId(str, str2);
    }

    public void commitExposureData() {
        TrackerFrameLayout.commitExposureData();
    }

    public void setCommitImmediatelyExposureBlock(String str) {
        TrackerFrameLayout.setCommitImmediatelyExposureBlock(str);
    }
}
