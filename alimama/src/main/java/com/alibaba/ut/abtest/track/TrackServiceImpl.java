package com.alibaba.ut.abtest.track;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.aliweex.adapter.module.WXUserTrackModule;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.ut.abtest.bucketing.decision.DebugTrack;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.ExperimentBuilder;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack;
import com.alibaba.ut.abtest.internal.track.ExperimentServerTrackPO;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.LruCache;
import com.alibaba.ut.abtest.internal.util.TrackUtils;
import com.alibaba.ut.abtest.internal.util.Utils;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.weex.el.parse.Operators;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class TrackServiceImpl implements TrackService {
    private static final String TAG = "TrackServiceImpl";
    private static final String TRACK_KEY_AB = "utabtest";
    private static final String TRACK_PREFIX = "aliabtest";
    private static final String UT_PARAM = "utparam-cnt";
    private Set<ExperimentTrack> activateAppTracks = new HashSet();
    private final Object activateAppTracksLock = new Object();
    private ConcurrentHashMap<String, ExperimentGroup> activateBuckets = new ConcurrentHashMap<>();
    private LruCache<String, Set<ExperimentTrack>> activatePageObjectTracks = new LruCache<>(80);
    private HashMap<String, Set<TrackUriMapper>> activateTrackUriMappers = new HashMap<>();
    private final Object activateTrackUriMappersLock = new Object();
    private ConcurrentHashMap<String, Set<ExperimentTrack>> activateTracks = new ConcurrentHashMap<>();
    private int[] subscribeUTEventIds = null;
    private ConcurrentHashMap<String, Integer> traceActivates = new ConcurrentHashMap<>();
    private Set<Integer> utEventIds = Collections.synchronizedSet(new HashSet());
    private final Object utEventLock = new Object();

    public TrackServiceImpl() {
        this.utEventIds.add(2001);
        this.utEventIds.add(2101);
        this.utEventIds.add(Integer.valueOf(SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM));
        refreshSubscribeUTEventIds();
    }

    private void refreshSubscribeUTEventIds() {
        if (this.subscribeUTEventIds == null || this.subscribeUTEventIds.length != this.utEventIds.size()) {
            synchronized (this.utEventLock) {
                try {
                    this.subscribeUTEventIds = new int[this.utEventIds.size()];
                    int i = 0;
                    for (Integer intValue : this.utEventIds) {
                        this.subscribeUTEventIds[i] = intValue.intValue();
                        i++;
                    }
                } catch (Exception e) {
                    LogUtils.logW(TAG, e.getMessage(), e);
                    this.subscribeUTEventIds = new int[]{2001, 2101, SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM};
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x018b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addActivateExperimentGroup(com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup r16, java.lang.Object r17) {
        /*
            r15 = this;
            r6 = r15
            if (r16 == 0) goto L_0x01b2
            java.util.List r1 = r16.getGroups()
            if (r1 == 0) goto L_0x01b2
            java.util.List r1 = r16.getGroups()
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L_0x0015
            goto L_0x01b2
        L_0x0015:
            java.util.List r0 = r16.getGroups()
            java.util.Iterator r7 = r0.iterator()
            r9 = 0
            r10 = 0
        L_0x001f:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L_0x01a6
            java.lang.Object r0 = r7.next()
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup r0 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup) r0
            java.lang.String r1 = "TrackServiceImpl"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "添加已激活的实验分组, experimentKey="
            r2.append(r3)
            java.lang.String r3 = r0.getKey()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r1, r2)
            long r1 = r0.getExperimentId()
            r3 = 0
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x0059
            long r1 = r0.getExperimentId()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            goto L_0x005d
        L_0x0059:
            java.lang.String r1 = r0.getKey()
        L_0x005d:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup> r2 = r6.activateBuckets
            java.lang.Object r2 = r2.get(r1)
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup r2 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup) r2
            r11 = 1
            if (r2 == 0) goto L_0x00ec
            long r3 = r2.getReleaseId()
            long r12 = r0.getReleaseId()
            int r5 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1))
            if (r5 == 0) goto L_0x00ab
            java.lang.String r3 = "TrackServiceImpl"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "实验数据发生变化,删除旧数据. key="
            r4.append(r5)
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r3, r4)
            java.util.List r2 = r2.getTracks()
            if (r2 == 0) goto L_0x00ec
            boolean r3 = r2.isEmpty()
            if (r3 != 0) goto L_0x00ec
            java.util.Iterator r2 = r2.iterator()
        L_0x009b:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x00ec
            java.lang.Object r3 = r2.next()
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack r3 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack) r3
            r15.removeActivateExperimentTrack(r3)
            goto L_0x009b
        L_0x00ab:
            if (r17 != 0) goto L_0x00c7
            java.lang.String r0 = "TrackServiceImpl"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "实验数据未发生变化, key="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r0, r1)
            r14 = r9
            goto L_0x01a3
        L_0x00c7:
            java.lang.String r2 = "TrackServiceImpl"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "实验数据未发生变化，页面对象不为空。key="
            r3.append(r4)
            r3.append(r1)
            java.lang.String r4 = ", pageObject="
            r3.append(r4)
            java.lang.String r4 = com.alibaba.ut.abtest.internal.util.TrackUtils.generateUTPageObjectKey(r17)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r2, r3)
            r12 = 0
            goto L_0x00ed
        L_0x00ec:
            r12 = 1
        L_0x00ed:
            if (r12 == 0) goto L_0x00f4
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup> r2 = r6.activateBuckets
            r2.put(r1, r0)
        L_0x00f4:
            java.util.List r13 = r0.getTracks()
            if (r13 == 0) goto L_0x018b
            boolean r2 = r13.isEmpty()
            if (r2 == 0) goto L_0x0102
            goto L_0x018b
        L_0x0102:
            java.lang.String r2 = "TrackServiceImpl"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "实验包含埋点规则信息, key="
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = ",埋点规则数量="
            r3.append(r1)
            int r1 = r13.size()
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logW(r2, r1)
            com.alibaba.ut.abtest.track.TrackId r4 = new com.alibaba.ut.abtest.track.TrackId
            r4.<init>()
            long r1 = r0.getReleaseId()
            r14 = r9
            long r8 = r0.getId()
            java.lang.String r1 = com.alibaba.ut.abtest.internal.util.TrackUtils.generateAbTrackId(r1, r8)
            r4.setAbTrackId(r1)
            if (r12 != 0) goto L_0x013e
            if (r17 == 0) goto L_0x01a3
        L_0x013e:
            long r1 = r0.getId()
            r0 = r15
            r3 = r13
            r5 = r17
            r0.addActivateExperimentTracks(r1, r3, r4, r5)
            if (r12 == 0) goto L_0x01a3
            java.util.Iterator r0 = r13.iterator()
        L_0x014f:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x01a3
            java.lang.Object r1 = r0.next()
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack r1 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack) r1
            boolean r2 = r1.isAppScope()
            if (r2 == 0) goto L_0x0162
            r14 = 1
        L_0x0162:
            int[] r2 = r1.getEventIds()
            if (r2 == 0) goto L_0x014f
            int[] r1 = r1.getEventIds()
            int r2 = r1.length
            r3 = 0
        L_0x016e:
            if (r3 >= r2) goto L_0x014f
            r4 = r1[r3]
            java.util.Set<java.lang.Integer> r5 = r6.utEventIds
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)
            boolean r5 = r5.contains(r8)
            if (r5 != 0) goto L_0x0188
            java.util.Set<java.lang.Integer> r5 = r6.utEventIds
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r5.add(r4)
            r10 = 1
        L_0x0188:
            int r3 = r3 + 1
            goto L_0x016e
        L_0x018b:
            r14 = r9
            java.lang.String r0 = "TrackServiceImpl"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "实验未包含埋点规则信息, key="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.alibaba.ut.abtest.internal.util.LogUtils.logW(r0, r1)
        L_0x01a3:
            r9 = r14
            goto L_0x001f
        L_0x01a6:
            r14 = r9
            if (r14 == 0) goto L_0x01ac
            r15.trackApp()
        L_0x01ac:
            if (r10 == 0) goto L_0x01b1
            r15.refreshSubscribeUTEventIds()
        L_0x01b1:
            return
        L_0x01b2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.track.TrackServiceImpl.addActivateExperimentGroup(com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup, java.lang.Object):void");
    }

    public void trackApp() {
        String appActivateTrackId = getAppActivateTrackId();
        if (!TextUtils.isEmpty(appActivateTrackId)) {
            UTAnalytics.getInstance().getDefaultTracker().setGlobalProperty("aliab", appActivateTrackId);
            try {
                AppMonitor.setGlobalProperty("aliab", appActivateTrackId);
            } catch (Throwable unused) {
            }
        }
    }

    private void trackPage(TrackId trackId, Object obj) {
        String trackUtParam = ABContext.getInstance().getTrackService().getTrackUtParam(trackId, 2001, UTAnalytics.getInstance().getDefaultTracker().getPageProperties(obj));
        UTAnalytics.getInstance().getDefaultTracker().updatePageUtparam(obj, trackUtParam);
        UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(trackUtParam);
    }

    private void addActivateExperimentTracks(long j, List<ExperimentTrack> list, TrackId trackId, Object obj) {
        Iterator<ExperimentTrack> it;
        Set set;
        long j2 = j;
        TrackId trackId2 = trackId;
        Object obj2 = obj;
        Iterator<ExperimentTrack> it2 = list.iterator();
        while (it2.hasNext()) {
            ExperimentTrack next = it2.next();
            if (next != null) {
                if (next.isAppScope()) {
                    next.setTrackId(trackId2);
                    next.setGroupId(j2);
                    synchronized (this.activateAppTracksLock) {
                        this.activateAppTracks.add(next);
                    }
                }
                if (next.getEventIds() != null && next.getEventIds().length != 0 && next.getPageNames() != null && next.getPageNames().length != 0) {
                    int[] eventIds = next.getEventIds();
                    int length = eventIds.length;
                    int i = 0;
                    while (i < length) {
                        int i2 = eventIds[i];
                        String[] pageNames = next.getPageNames();
                        int length2 = pageNames.length;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= length2) {
                                it = it2;
                                break;
                            }
                            String str = pageNames[i3];
                            if (obj2 == null) {
                                it = it2;
                                set = this.activateTracks.get(getActivateExperimentTrackKey(i2, str));
                                if (set == null) {
                                    set = Collections.synchronizedSet(new HashSet());
                                    this.activateTracks.put(getActivateExperimentTrackKey(i2, str), set);
                                }
                            } else {
                                it = it2;
                                if (i2 == 2001) {
                                    trackPage(trackId2, obj2);
                                    break;
                                }
                                set = this.activatePageObjectTracks.get(getActivateExperimentTrackKey(i2, str, TrackUtils.generateUTPageObjectKey(obj)));
                                if (set == null) {
                                    set = Collections.synchronizedSet(new HashSet());
                                    this.activatePageObjectTracks.put(getActivateExperimentTrackKey(i2, str, TrackUtils.generateUTPageObjectKey(obj)), set);
                                }
                            }
                            next.setTrackId(trackId2);
                            next.setGroupId(j2);
                            set.add(next);
                            i3++;
                            it2 = it;
                            obj2 = obj;
                        }
                        i++;
                        it2 = it;
                        obj2 = obj;
                    }
                    Iterator<ExperimentTrack> it3 = it2;
                    for (String str2 : next.getPageNames()) {
                        Uri parseURI = UriUtils.parseURI(str2, false);
                        if (parseURI != null && !TextUtils.isEmpty(parseURI.getScheme())) {
                            TrackUriMapper trackUriMapper = new TrackUriMapper();
                            trackUriMapper.setPageName(str2);
                            trackUriMapper.setUri(parseURI);
                            trackUriMapper.setGroupId(j2);
                            synchronized (this.activateTrackUriMappersLock) {
                                String uriKey = Utils.getUriKey(parseURI);
                                Set set2 = this.activateTrackUriMappers.get(uriKey);
                                if (set2 == null) {
                                    set2 = new HashSet();
                                    this.activateTrackUriMappers.put(uriKey, set2);
                                }
                                set2.add(trackUriMapper);
                            }
                        }
                    }
                    it2 = it3;
                    obj2 = obj;
                } else {
                    return;
                }
            }
        }
    }

    private String getActivateExperimentTrackKey(int i, String str) {
        return i + "_" + str;
    }

    private String getActivateExperimentTrackKey(int i, String str, String str2) {
        return i + "_" + str + "_" + str2;
    }

    private void removeActivateExperimentTrack(ExperimentTrack experimentTrack) {
        if (experimentTrack.isAppScope()) {
            synchronized (this.activateAppTracksLock) {
                this.activateAppTracks.remove(experimentTrack);
            }
        }
        for (Set next : this.activateTracks.values()) {
            if (next != null && !next.isEmpty()) {
                next.remove(experimentTrack);
            }
        }
        if (experimentTrack.getGroupId() > 0) {
            synchronized (this.activateTrackUriMappersLock) {
                for (Set next2 : this.activateTrackUriMappers.values()) {
                    if (next2 != null) {
                        if (!next2.isEmpty()) {
                            Iterator it = next2.iterator();
                            while (it.hasNext()) {
                                TrackUriMapper trackUriMapper = (TrackUriMapper) it.next();
                                if (trackUriMapper != null) {
                                    if (trackUriMapper.getGroupId() == experimentTrack.getGroupId()) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<ExperimentTrack> getActivateExperimentTracks(int i, String str, String str2) {
        Set<ExperimentTrack> set = this.activateTracks.get(getActivateExperimentTrackKey(i, str));
        return set == null ? this.activatePageObjectTracks.get(getActivateExperimentTrackKey(i, str, str2)) : set;
    }

    public void removeActivateExperiment(String str) {
        List<ExperimentTrack> tracks;
        ExperimentGroup remove = this.activateBuckets.remove(str);
        if (remove != null && (tracks = remove.getTracks()) != null && !tracks.isEmpty()) {
            for (ExperimentTrack removeActivateExperimentTrack : tracks) {
                removeActivateExperimentTrack(removeActivateExperimentTrack);
            }
        }
    }

    public int[] getSubscribeUTEventIds() {
        int[] iArr;
        synchronized (this.utEventLock) {
            iArr = this.subscribeUTEventIds;
        }
        return iArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a7 A[Catch:{ Throwable -> 0x00a2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.ut.abtest.track.TrackId getTrackId(java.lang.String r1, int r2, java.lang.String r3, java.lang.String r4, java.lang.String r5, java.util.Map<java.lang.String, java.lang.String> r6, java.lang.String r7) {
        /*
            r0 = this;
            java.util.HashSet r3 = new java.util.HashSet
            r3.<init>()
            java.util.Set r2 = r0.getActivateExperimentTracks(r2, r1, r7)     // Catch:{ Throwable -> 0x002d }
            if (r2 == 0) goto L_0x0037
            boolean r4 = r2.isEmpty()     // Catch:{ Throwable -> 0x002d }
            if (r4 != 0) goto L_0x0037
            java.util.Iterator r2 = r2.iterator()     // Catch:{ Throwable -> 0x002d }
        L_0x0015:
            boolean r4 = r2.hasNext()     // Catch:{ Throwable -> 0x002d }
            if (r4 == 0) goto L_0x0037
            java.lang.Object r4 = r2.next()     // Catch:{ Throwable -> 0x002d }
            com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack r4 = (com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack) r4     // Catch:{ Throwable -> 0x002d }
            com.alibaba.ut.abtest.track.TrackId r4 = r4.getTrackId()     // Catch:{ Throwable -> 0x002d }
            java.lang.String r4 = r4.getAbTrackId()     // Catch:{ Throwable -> 0x002d }
            r3.add(r4)     // Catch:{ Throwable -> 0x002d }
            goto L_0x0015
        L_0x002d:
            r2 = move-exception
            java.lang.String r4 = "TrackServiceImpl"
            java.lang.String r5 = r2.getMessage()
            com.alibaba.ut.abtest.internal.util.LogUtils.logE(r4, r5, r2)
        L_0x0037:
            r2 = 0
            if (r6 == 0) goto L_0x00a4
            boolean r4 = r6.isEmpty()     // Catch:{ Throwable -> 0x00a2 }
            if (r4 != 0) goto L_0x00a4
            java.lang.String r4 = "weex"
            java.lang.Object r4 = r6.get(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r5 = "1"
            boolean r4 = android.text.TextUtils.equals(r4, r5)     // Catch:{ Throwable -> 0x00a2 }
            if (r4 == 0) goto L_0x0059
            java.lang.String r1 = "url"
            java.lang.Object r1 = r6.get(r1)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x00a2 }
            goto L_0x0097
        L_0x0059:
            java.lang.String r4 = "_ish5"
            java.lang.Object r4 = r6.get(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r5 = "1"
            boolean r4 = android.text.TextUtils.equals(r4, r5)     // Catch:{ Throwable -> 0x00a2 }
            if (r4 == 0) goto L_0x0096
            java.lang.String r4 = "_h5url"
            java.lang.Object r4 = r6.get(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x00a2 }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x00a2 }
            if (r5 == 0) goto L_0x007f
            java.lang.String r4 = "url"
            java.lang.Object r4 = r6.get(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x00a2 }
        L_0x007f:
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x00a2 }
            if (r5 == 0) goto L_0x008d
            java.lang.String r4 = "webview_url"
            java.lang.Object r4 = r6.get(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x00a2 }
        L_0x008d:
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x00a2 }
            if (r5 == 0) goto L_0x0094
            goto L_0x0097
        L_0x0094:
            r1 = r4
            goto L_0x0097
        L_0x0096:
            r1 = r2
        L_0x0097:
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00a2 }
            if (r4 != 0) goto L_0x00a4
            android.net.Uri r1 = com.alibaba.ut.abtest.track.UriUtils.parseURI(r1)     // Catch:{ Throwable -> 0x00a2 }
            goto L_0x00a5
        L_0x00a2:
            r1 = move-exception
            goto L_0x00c8
        L_0x00a4:
            r1 = r2
        L_0x00a5:
            if (r1 == 0) goto L_0x00d1
            java.lang.String r4 = "utabtest"
            java.lang.String r1 = r1.getQueryParameter(r4)     // Catch:{ Throwable -> 0x00a2 }
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00a2 }
            if (r4 != 0) goto L_0x00d1
            java.lang.String[] r1 = com.alibaba.ut.abtest.internal.util.TrackUtils.splitAbTrackId(r1)     // Catch:{ Throwable -> 0x00a2 }
            if (r1 == 0) goto L_0x00d1
            int r4 = r1.length     // Catch:{ Throwable -> 0x00a2 }
            if (r4 <= 0) goto L_0x00d1
            int r4 = r1.length     // Catch:{ Throwable -> 0x00a2 }
            r5 = 0
        L_0x00be:
            if (r5 >= r4) goto L_0x00d1
            r6 = r1[r5]     // Catch:{ Throwable -> 0x00a2 }
            r3.add(r6)     // Catch:{ Throwable -> 0x00a2 }
            int r5 = r5 + 1
            goto L_0x00be
        L_0x00c8:
            java.lang.String r4 = "TrackServiceImpl"
            java.lang.String r5 = r1.getMessage()
            com.alibaba.ut.abtest.internal.util.LogUtils.logE(r4, r5, r1)
        L_0x00d1:
            boolean r1 = r3.isEmpty()
            if (r1 != 0) goto L_0x00e5
            com.alibaba.ut.abtest.track.TrackId r2 = new com.alibaba.ut.abtest.track.TrackId
            r2.<init>()
            java.lang.String r1 = "."
            java.lang.String r1 = com.alibaba.ut.abtest.internal.util.Utils.join((java.util.Collection) r3, (java.lang.String) r1)
            r2.setAbTrackId(r1)
        L_0x00e5:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.track.TrackServiceImpl.getTrackId(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.String):com.alibaba.ut.abtest.track.TrackId");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0027  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getTrackUtParam(com.alibaba.ut.abtest.track.TrackId r7, int r8, java.util.Map<java.lang.String, java.lang.String> r9) {
        /*
            r6 = this;
            if (r9 == 0) goto L_0x0024
            java.lang.String r0 = "utparam-cnt"
            java.lang.Object r9 = r9.get(r0)
            java.lang.String r9 = (java.lang.String) r9
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            if (r0 != 0) goto L_0x0024
            java.lang.String r8 = r6.decodeIfNeed(r9, r8)
            com.alibaba.ut.abtest.track.TrackServiceImpl$1 r9 = new com.alibaba.ut.abtest.track.TrackServiceImpl$1
            r9.<init>()
            java.lang.reflect.Type r9 = r9.getType()
            java.lang.Object r8 = com.alibaba.ut.abtest.internal.util.JsonUtil.fromJson((java.lang.String) r8, (java.lang.reflect.Type) r9)
            java.util.Map r8 = (java.util.Map) r8
            goto L_0x0025
        L_0x0024:
            r8 = 0
        L_0x0025:
            if (r8 == 0) goto L_0x007f
            java.lang.String r9 = r7.getAbTrackId()
            boolean r9 = android.text.TextUtils.isEmpty(r9)
            if (r9 != 0) goto L_0x0084
            java.lang.String r9 = "utabtest"
            java.lang.Object r9 = r8.get(r9)
            java.lang.String r9 = (java.lang.String) r9
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            if (r0 != 0) goto L_0x0084
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r9)
            java.lang.String r1 = "."
            r0.append(r1)
            java.lang.String r1 = r7.getAbTrackId()
            java.lang.String r2 = "\\."
            java.lang.String[] r1 = r1.split(r2)
            int r2 = r1.length
            r3 = 0
        L_0x0058:
            if (r3 >= r2) goto L_0x006d
            r4 = r1[r3]
            boolean r5 = r9.contains(r4)
            if (r5 != 0) goto L_0x006a
            r0.append(r4)
            java.lang.String r4 = "."
            r0.append(r4)
        L_0x006a:
            int r3 = r3 + 1
            goto L_0x0058
        L_0x006d:
            int r9 = r0.length()
            int r9 = r9 + -1
            java.lang.StringBuilder r9 = r0.deleteCharAt(r9)
            java.lang.String r9 = r9.toString()
            r7.setAbTrackId(r9)
            goto L_0x0084
        L_0x007f:
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
        L_0x0084:
            java.lang.String r9 = r7.getAbTrackId()
            boolean r9 = android.text.TextUtils.isEmpty(r9)
            if (r9 != 0) goto L_0x0097
            java.lang.String r9 = "utabtest"
            java.lang.String r7 = r7.getAbTrackId()
            r8.put(r9, r7)
        L_0x0097:
            java.lang.String r7 = com.alibaba.ut.abtest.internal.util.JsonUtil.toJson((java.util.Map<java.lang.String, ?>) r8)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.track.TrackServiceImpl.getTrackUtParam(com.alibaba.ut.abtest.track.TrackId, int, java.util.Map):java.lang.String");
    }

    private String decodeIfNeed(String str, int i) {
        if (i != 2001) {
            return str;
        }
        try {
            return str.contains("aliabtest") ? URLDecoder.decode(str, "utf-8") : str;
        } catch (Exception unused) {
            return str;
        }
    }

    public void traceActivate(ExperimentActivateGroup experimentActivateGroup, DebugTrack debugTrack) {
        Long experimentGroupId;
        if (experimentActivateGroup.getTrackIds() != null && !experimentActivateGroup.getTrackIds().isEmpty()) {
            for (String next : experimentActivateGroup.getTrackIds()) {
                Long experimentId = experimentActivateGroup.getExperimentId(next);
                if ((experimentId == null || !ABContext.getInstance().getConfigService().isTrack1022ExperimentDisabled(experimentId)) && ((experimentGroupId = experimentActivateGroup.getExperimentGroupId(next)) == null || !ABContext.getInstance().getConfigService().isTrack1022GroupDisabled(experimentGroupId))) {
                    Integer num = this.traceActivates.get(next);
                    if (num == null) {
                        sendTrackActivate(next, 1, debugTrack);
                        this.traceActivates.put(next, 0);
                    } else {
                        this.traceActivates.put(next, Integer.valueOf(num.intValue() + 1));
                    }
                }
            }
        }
    }

    public void traceActivateNotSend() {
        if (this.traceActivates != null && !this.traceActivates.isEmpty()) {
            for (Map.Entry next : this.traceActivates.entrySet()) {
                if (next.getValue() != null && ((Integer) next.getValue()).intValue() > 0) {
                    sendTrackActivate((String) next.getKey(), ((Integer) next.getValue()).intValue(), (DebugTrack) null);
                }
            }
            this.traceActivates.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void sendTrackActivate(String str, int i, DebugTrack debugTrack) {
        UTHitBuilders.UTHitBuilder uTHitBuilder = new UTHitBuilders.UTHitBuilder();
        uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "1022");
        uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG1, WXUserTrackModule.ENTER);
        uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, "version=1.4.1.0,id=" + str);
        UTAnalytics.getInstance().getDefaultTracker().send(uTHitBuilder.build());
    }

    public boolean addActivateServerExperimentGroup(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            LogUtils.logW(TAG, "服务端实验配置为空！");
            return false;
        }
        if (str.startsWith(Operators.BLOCK_START_STR) || str.endsWith("}")) {
            try {
                str = new JSONObject(str).optString("dataTrack");
            } catch (Throwable th) {
                LogUtils.logW(TAG, "服务端实验配置格式不合法！内容=" + str, th);
            }
        }
        if (TextUtils.isEmpty(str)) {
            LogUtils.logW(TAG, "服务端实验配置为空！");
            return false;
        }
        List list = (List) JsonUtil.fromJson(str, new TypeReference<List<ExperimentServerTrackPO>>() {
        }.getType());
        if (list == null || list.isEmpty()) {
            LogUtils.logW(TAG, "服务端实验埋点规则格式非法！内容=" + str);
            return false;
        }
        List<ExperimentGroup> createExperimentGroups = ExperimentBuilder.createExperimentGroups(list, str);
        if (createExperimentGroups == null || createExperimentGroups.isEmpty()) {
            return true;
        }
        ExperimentActivateGroup experimentActivateGroup = new ExperimentActivateGroup();
        experimentActivateGroup.addGroups(createExperimentGroups);
        addActivateExperimentGroup(experimentActivateGroup, obj);
        traceActivate(experimentActivateGroup, (DebugTrack) null);
        return true;
    }

    public String getPageActivateTrackId(String str) {
        Set<ExperimentTrack> set = this.activateTracks.get(getActivateExperimentTrackKey(2001, str));
        if (set == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        for (ExperimentTrack trackId : set) {
            TrackId trackId2 = trackId.getTrackId();
            if (trackId2 != null) {
                hashSet.add(trackId2.getAbTrackId());
            }
        }
        return Utils.join((Collection) hashSet, ".");
    }

    public String getAppActivateTrackId() {
        synchronized (this.activateAppTracksLock) {
            if (this.activateAppTracks.isEmpty()) {
                return null;
            }
            HashSet hashSet = new HashSet();
            for (ExperimentTrack next : this.activateAppTracks) {
                if (next.getTrackId() != null) {
                    hashSet.add(next.getTrackId().getAbTrackId());
                }
            }
            String join = Utils.join((Collection) hashSet, ".");
            return join;
        }
    }
}
