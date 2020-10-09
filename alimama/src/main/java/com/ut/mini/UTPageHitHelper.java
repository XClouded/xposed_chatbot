package com.ut.mini;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.usertrack.UTHelper;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.ju.track.constants.Constants;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class UTPageHitHelper {
    private static final String FORCE_SPM_CNT = "force-spm-cnt";
    private static final String FORCE_SPM_URL = "force-spm-url";
    private static final int MAX_SKIP_CLEAR_PAGE_OBJECT_CACHE_CAPACITY = 100;
    private static final int MAX_SPM_OBJECT_CACHE_CAPACITY = 50;
    static final String SKIPBK = "skipbk";
    private static final String TAG = "UTPageHitHelper";
    static final String UTPARAM_CNT = "utparam-cnt";
    static final String UTPARAM_URL = "utparam-url";
    private static ArrayList<PageChangeListener> mPageChangerListeners = new ArrayList<>();
    private static UTPageHitHelper s_instance = new UTPageHitHelper();
    private Map<String, String> mBackupNextPageProperties = null;
    private Queue<String> mClearUTPageStateObjectList = new LinkedList();
    private String mCurPage = null;
    private String mCurrentPageCacheKey = null;
    private boolean mIsTurnOff = false;
    private String mLastCacheKey = null;
    private String mLastCacheKeyScmUrl = null;
    private String mLastCacheKeySpmUrl = null;
    private String mLastCacheKeyUtParam = null;
    private String mLastCacheKeyUtParamCnt = null;
    private Map<String, String> mNextPageProperties = new HashMap();
    private boolean mNextPageSkipBack = false;
    private Map<String, UTPageEventObject> mPageEventObjects = new HashMap();
    private Map<String, String> mPageProperties = new HashMap();
    private Map<String, UTPageStateObject> mPageStateObjects = new HashMap();
    private Queue<String> mSPMObjectList = new LinkedList();
    private Map<String, String> mSPMObjectMap = new HashMap();
    private Queue<UTPageEventObject> mSkipClearPageObjectList = new LinkedList();

    public interface PageChangeListener {
        void onPageAppear(Object obj);

        void onPageDisAppear(Object obj);
    }

    public static class UTPageStateObject {
        public boolean mIsBack = false;
        public boolean mIsFrame = false;
        public boolean mIsH5Page = false;
        boolean mIsSkipBack = false;
        boolean mIsSkipBackForever = false;
        public boolean mIsSwitchBackground = false;
        public String mScmPre = null;
        public String mScmUrl = null;
        public String mSpmCnt = null;
        public String mSpmPre = null;
        public String mSpmUrl = null;
        public String mUtparamCnt = null;
        public String mUtparamPre = null;
        public String mUtparamUrl = null;

        public Map<String, String> getPageStatMap(boolean z) {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(this.mSpmCnt)) {
                hashMap.put(Constants.PARAM_OUTER_SPM_CNT, this.mSpmCnt);
            }
            if (!TextUtils.isEmpty(this.mSpmUrl)) {
                hashMap.put("spm-url", this.mSpmUrl);
            }
            if (!TextUtils.isEmpty(this.mSpmPre)) {
                hashMap.put("spm-pre", this.mSpmPre);
            }
            if (!TextUtils.isEmpty(this.mScmPre)) {
                hashMap.put("scm-pre", this.mScmPre);
            }
            if (this.mIsSwitchBackground) {
                hashMap.put("isbf", "1");
            } else if (this.mIsFrame && z) {
                hashMap.put("isfm", "1");
            } else if (this.mIsBack) {
                hashMap.put("ut_isbk", "1");
            }
            if (!TextUtils.isEmpty(this.mUtparamCnt)) {
                hashMap.put("utparam-cnt", this.mUtparamCnt);
            }
            if (!TextUtils.isEmpty(this.mUtparamUrl)) {
                hashMap.put(UTPageHitHelper.UTPARAM_URL, this.mUtparamUrl);
            }
            if (!TextUtils.isEmpty(this.mUtparamPre)) {
                hashMap.put("utparam-pre", this.mUtparamPre);
            }
            return hashMap;
        }
    }

    private UTPageStateObject copyUTPageStateObject(UTPageStateObject uTPageStateObject) {
        if (uTPageStateObject == null) {
            return null;
        }
        UTPageStateObject uTPageStateObject2 = new UTPageStateObject();
        uTPageStateObject2.mSpmCnt = uTPageStateObject.mSpmCnt;
        uTPageStateObject2.mSpmUrl = uTPageStateObject.mSpmUrl;
        uTPageStateObject2.mSpmPre = uTPageStateObject.mSpmPre;
        uTPageStateObject2.mIsBack = uTPageStateObject.mIsBack;
        uTPageStateObject2.mIsFrame = uTPageStateObject.mIsFrame;
        uTPageStateObject2.mIsSwitchBackground = uTPageStateObject.mIsSwitchBackground;
        uTPageStateObject2.mUtparamCnt = uTPageStateObject.mUtparamCnt;
        uTPageStateObject2.mUtparamUrl = uTPageStateObject.mUtparamUrl;
        uTPageStateObject2.mUtparamPre = uTPageStateObject.mUtparamPre;
        uTPageStateObject2.mScmUrl = uTPageStateObject.mScmUrl;
        uTPageStateObject2.mScmPre = uTPageStateObject.mScmPre;
        uTPageStateObject2.mIsSkipBack = uTPageStateObject.mIsSkipBack;
        uTPageStateObject2.mIsSkipBackForever = uTPageStateObject.mIsSkipBackForever;
        return uTPageStateObject2;
    }

    public void setLastCacheKey(String str) {
        this.mLastCacheKey = str;
    }

    public void setLastCacheKeySpmUrl(String str) {
        this.mLastCacheKeySpmUrl = str;
    }

    public void setLastCacheKeyScmUrl(String str) {
        this.mLastCacheKeyScmUrl = str;
    }

    public void setLastCacheKeyUtParam(String str) {
        this.mLastCacheKeyUtParam = str;
    }

    public void setLastCacheKeyUtParamCnt(String str) {
        this.mLastCacheKeyUtParamCnt = str;
    }

    public String getLastCacheKey() {
        return this.mLastCacheKey;
    }

    public String getLastCacheKeySpmUrl() {
        return this.mLastCacheKeySpmUrl;
    }

    public String getLastCacheKeyScmUrl() {
        return this.mLastCacheKeyScmUrl;
    }

    public String getLastCacheKeyUtParam() {
        return this.mLastCacheKeyUtParam;
    }

    public String getLastCacheKeyUtParamCnt() {
        return this.mLastCacheKeyUtParamCnt;
    }

    public static class UTPageEventObject {
        private String mCacheKey = null;
        private boolean mIsH5Called = false;
        private boolean mIsPageAppearCalled = false;
        private boolean mIsSkipPage = false;
        private Map<String, String> mNextPageProperties = null;
        private long mPageAppearTimestamp = 0;
        private String mPageName = null;
        private Map<String, String> mPageProperties = new HashMap();
        private UTPageStatus mPageStatus = null;
        private int mPageStatusCode = 0;
        private long mPageStayTimstamp = 0;
        private Uri mPageUrl = null;
        private String mRefPage = null;

        public void setNextPageProperties(Map<String, String> map) {
            this.mNextPageProperties = map;
        }

        public Map<String, String> getNextPageProperties() {
            return this.mNextPageProperties;
        }

        public void setCacheKey(String str) {
            this.mCacheKey = str;
        }

        public String getCacheKey() {
            return this.mCacheKey;
        }

        public void resetPropertiesWithoutSkipFlagAndH5Flag() {
            this.mPageProperties = new HashMap();
            this.mPageAppearTimestamp = 0;
            this.mPageStayTimstamp = 0;
            this.mPageUrl = null;
            this.mPageName = null;
            this.mRefPage = null;
            if (this.mPageStatus == null || this.mPageStatus != UTPageStatus.UT_H5_IN_WebView) {
                this.mPageStatus = null;
            }
            this.mIsPageAppearCalled = false;
            this.mIsH5Called = false;
            this.mPageStatusCode = 0;
            this.mNextPageProperties = null;
        }

        public boolean isH5Called() {
            return this.mIsH5Called;
        }

        public void setH5Called() {
            this.mIsH5Called = true;
        }

        public void setToSkipPage() {
            this.mIsSkipPage = true;
        }

        public boolean isSkipPage() {
            return this.mIsSkipPage;
        }

        public void setPageAppearCalled() {
            this.mIsPageAppearCalled = true;
        }

        public boolean isPageAppearCalled() {
            return this.mIsPageAppearCalled;
        }

        public void setPageStatus(UTPageStatus uTPageStatus) {
            this.mPageStatus = uTPageStatus;
        }

        public UTPageStatus getPageStatus() {
            return this.mPageStatus;
        }

        public Map<String, String> getPageProperties() {
            return this.mPageProperties;
        }

        public void setPageProperties(Map<String, String> map) {
            this.mPageProperties = map;
        }

        public long getPageAppearTimestamp() {
            return this.mPageAppearTimestamp;
        }

        public void setPageAppearTimestamp(long j) {
            this.mPageAppearTimestamp = j;
        }

        public long getPageStayTimstamp() {
            return this.mPageStayTimstamp;
        }

        public void setPageStayTimstamp(long j) {
            this.mPageStayTimstamp = j;
        }

        public Uri getPageUrl() {
            return this.mPageUrl;
        }

        public void setPageUrl(Uri uri) {
            this.mPageUrl = uri;
        }

        public void setPageName(String str) {
            this.mPageName = str;
        }

        public String getPageName() {
            return this.mPageName;
        }

        public void setRefPage(String str) {
            this.mRefPage = str;
        }

        public String getRefPage() {
            return this.mRefPage;
        }

        public void setPageStatusCode(int i) {
            this.mPageStatusCode = i;
        }

        public int getPageStatusCode() {
            return this.mPageStatusCode;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void addPageChangerListener(com.ut.mini.UTPageHitHelper.PageChangeListener r2) {
        /*
            java.lang.Class<com.ut.mini.UTPageHitHelper> r0 = com.ut.mini.UTPageHitHelper.class
            monitor-enter(r0)
            if (r2 != 0) goto L_0x0007
            monitor-exit(r0)
            return
        L_0x0007:
            java.util.ArrayList<com.ut.mini.UTPageHitHelper$PageChangeListener> r1 = mPageChangerListeners     // Catch:{ all -> 0x0016 }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x0016 }
            if (r1 != 0) goto L_0x0014
            java.util.ArrayList<com.ut.mini.UTPageHitHelper$PageChangeListener> r1 = mPageChangerListeners     // Catch:{ all -> 0x0016 }
            r1.add(r2)     // Catch:{ all -> 0x0016 }
        L_0x0014:
            monitor-exit(r0)
            return
        L_0x0016:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.addPageChangerListener(com.ut.mini.UTPageHitHelper$PageChangeListener):void");
    }

    static synchronized void disPathcherPageChangerEvent(int i, Object obj) {
        synchronized (UTPageHitHelper.class) {
            int size = mPageChangerListeners.size();
            for (int i2 = 0; i2 < size; i2++) {
                PageChangeListener pageChangeListener = mPageChangerListeners.get(i2);
                if (pageChangeListener != null) {
                    if (i == 0) {
                        pageChangeListener.onPageAppear(obj);
                    } else {
                        pageChangeListener.onPageDisAppear(obj);
                    }
                }
            }
        }
    }

    public static UTPageHitHelper getInstance() {
        return s_instance;
    }

    /* access modifiers changed from: package-private */
    public synchronized Map<String, String> getNextPageProperties(Object obj) {
        if (obj == null) {
            return null;
        }
        return _getOrNewAUTPageEventObject(obj).getNextPageProperties();
    }

    /* access modifiers changed from: package-private */
    public synchronized void _releaseSkipFlagAndH5FlagPageObject(UTPageEventObject uTPageEventObject) {
        uTPageEventObject.resetPropertiesWithoutSkipFlagAndH5Flag();
        if (!this.mSkipClearPageObjectList.contains(uTPageEventObject)) {
            this.mSkipClearPageObjectList.add(uTPageEventObject);
        }
        if (this.mSkipClearPageObjectList.size() > 200) {
            for (int i = 0; i < 100; i++) {
                UTPageEventObject poll = this.mSkipClearPageObjectList.poll();
                if (poll != null && this.mPageEventObjects.containsKey(poll.getCacheKey())) {
                    this.mPageEventObjects.remove(poll.getCacheKey());
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void _releaseSPMCacheObj(String str) {
        if (!this.mSPMObjectList.contains(str)) {
            this.mSPMObjectList.add(str);
        }
        if (this.mSPMObjectList.size() > 100) {
            for (int i = 0; i < 50; i++) {
                String poll = this.mSPMObjectList.poll();
                if (poll != null && this.mSPMObjectMap.containsKey(poll)) {
                    this.mSPMObjectMap.remove(poll);
                }
            }
        }
    }

    @Deprecated
    public synchronized void turnOffAutoPageTrack() {
        this.mIsTurnOff = true;
    }

    public String getCurrentPageName() {
        return this.mCurPage;
    }

    /* access modifiers changed from: package-private */
    public void pageAppearByAuto(Activity activity) {
        if (!this.mIsTurnOff) {
            pageAppear(activity);
        }
    }

    private String _getPageEventObjectCacheKey(Object obj) {
        String str;
        if (obj instanceof String) {
            str = (String) obj;
        } else {
            str = obj.getClass().getSimpleName();
        }
        int hashCode = obj.hashCode();
        return str + hashCode;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isH52001(Object obj) {
        if (obj != null) {
            UTPageEventObject _getOrNewAUTPageEventObject = _getOrNewAUTPageEventObject(obj);
            if (_getOrNewAUTPageEventObject.getPageStatus() != null && _getOrNewAUTPageEventObject.getPageStatus() == UTPageStatus.UT_H5_IN_WebView) {
                Logger.d(TAG, "isH52001:true aPageObject", obj);
                return true;
            }
        }
        Logger.d(TAG, "isH52001:false aPageObject", obj);
        return false;
    }

    /* access modifiers changed from: package-private */
    public synchronized void setH5Called(Object obj) {
        if (obj != null) {
            UTPageEventObject _getOrNewAUTPageEventObject = _getOrNewAUTPageEventObject(obj);
            if (_getOrNewAUTPageEventObject.getPageStatus() != null) {
                _getOrNewAUTPageEventObject.setH5Called();
            }
        }
    }

    private synchronized UTPageEventObject _getOrNewAUTPageEventObject(Object obj) {
        String _getPageEventObjectCacheKey = _getPageEventObjectCacheKey(obj);
        if (this.mPageEventObjects.containsKey(_getPageEventObjectCacheKey)) {
            return this.mPageEventObjects.get(_getPageEventObjectCacheKey);
        }
        UTPageEventObject uTPageEventObject = new UTPageEventObject();
        this.mPageEventObjects.put(_getPageEventObjectCacheKey, uTPageEventObject);
        uTPageEventObject.setCacheKey(_getPageEventObjectCacheKey);
        return uTPageEventObject;
    }

    private synchronized void _putUTPageEventObjectToCache(String str, UTPageEventObject uTPageEventObject) {
        this.mPageEventObjects.put(str, uTPageEventObject);
    }

    private synchronized void _clearUTPageEventObjectCache(UTPageEventObject uTPageEventObject) {
        if (this.mPageEventObjects.containsKey(uTPageEventObject.getCacheKey())) {
            this.mPageEventObjects.remove(uTPageEventObject.getCacheKey());
        }
    }

    private synchronized void _removeUTPageEventObject(Object obj) {
        String _getPageEventObjectCacheKey = _getPageEventObjectCacheKey(obj);
        if (this.mPageEventObjects.containsKey(_getPageEventObjectCacheKey)) {
            this.mPageEventObjects.remove(_getPageEventObjectCacheKey);
        }
    }

    @Deprecated
    public synchronized void pageAppear(Object obj) {
        pageAppear(obj, (String) null, false);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01eb, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x014e */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0154 A[Catch:{ Throwable -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0155 A[Catch:{ Throwable -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0160 A[Catch:{ Throwable -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0189 A[Catch:{ Throwable -> 0x00f2 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void pageAppear(java.lang.Object r11, java.lang.String r12, boolean r13) {
        /*
            r10 = this;
            monitor-enter(r10)
            java.lang.String r0 = "UTPageHitHelper"
            r1 = 6
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x01ec }
            java.lang.String r2 = "pageAppear aPageObject"
            r3 = 0
            r1[r3] = r2     // Catch:{ all -> 0x01ec }
            r2 = 1
            r1[r2] = r11     // Catch:{ all -> 0x01ec }
            r4 = 2
            java.lang.String r5 = "aCustomPageName"
            r1[r4] = r5     // Catch:{ all -> 0x01ec }
            r4 = 3
            r1[r4] = r12     // Catch:{ all -> 0x01ec }
            r4 = 4
            java.lang.String r5 = "aIsDonotSkipFlag"
            r1[r4] = r5     // Catch:{ all -> 0x01ec }
            r4 = 5
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r13)     // Catch:{ all -> 0x01ec }
            r1[r4] = r5     // Catch:{ all -> 0x01ec }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x01ec }
            com.ut.mini.UTPvidHelper.pageAppear()     // Catch:{ all -> 0x01ec }
            com.ut.mini.module.trackerlistener.UTTrackerListenerMgr r0 = com.ut.mini.module.trackerlistener.UTTrackerListenerMgr.getInstance()     // Catch:{ all -> 0x01ec }
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ all -> 0x01ec }
            com.ut.mini.UTTracker r1 = r1.getDefaultTracker()     // Catch:{ all -> 0x01ec }
            r0.pageAppear(r1, r11, r12, r13)     // Catch:{ all -> 0x01ec }
            if (r11 == 0) goto L_0x01df
            java.lang.String r0 = r10._getPageEventObjectCacheKey(r11)     // Catch:{ all -> 0x01ec }
            if (r0 == 0) goto L_0x0049
            java.lang.String r1 = r10.mCurrentPageCacheKey     // Catch:{ all -> 0x01ec }
            boolean r0 = r0.equals(r1)     // Catch:{ all -> 0x01ec }
            if (r0 == 0) goto L_0x0049
            monitor-exit(r10)
            return
        L_0x0049:
            java.lang.String r0 = r10.mCurrentPageCacheKey     // Catch:{ all -> 0x01ec }
            if (r0 == 0) goto L_0x006e
            java.lang.String r0 = "lost 2001"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ all -> 0x01ec }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x01ec }
            r4.<init>()     // Catch:{ all -> 0x01ec }
            java.lang.String r5 = "Last page requires leave("
            r4.append(r5)     // Catch:{ all -> 0x01ec }
            java.lang.String r5 = r10.mCurrentPageCacheKey     // Catch:{ all -> 0x01ec }
            r4.append(r5)     // Catch:{ all -> 0x01ec }
            java.lang.String r5 = ")."
            r4.append(r5)     // Catch:{ all -> 0x01ec }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x01ec }
            r1[r3] = r4     // Catch:{ all -> 0x01ec }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x01ec }
        L_0x006e:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r0 = r10._getOrNewAUTPageEventObject(r11)     // Catch:{ all -> 0x01ec }
            if (r13 != 0) goto L_0x009e
            boolean r1 = r0.isSkipPage()     // Catch:{ all -> 0x01ec }
            if (r1 == 0) goto L_0x009e
            java.lang.String r12 = "skip page[pageAppear]"
            java.lang.Object[] r13 = new java.lang.Object[r2]     // Catch:{ all -> 0x01ec }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x01ec }
            r0.<init>()     // Catch:{ all -> 0x01ec }
            java.lang.String r1 = "page name:"
            r0.append(r1)     // Catch:{ all -> 0x01ec }
            java.lang.Class r11 = r11.getClass()     // Catch:{ all -> 0x01ec }
            java.lang.String r11 = r11.getSimpleName()     // Catch:{ all -> 0x01ec }
            r0.append(r11)     // Catch:{ all -> 0x01ec }
            java.lang.String r11 = r0.toString()     // Catch:{ all -> 0x01ec }
            r13[r3] = r11     // Catch:{ all -> 0x01ec }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ all -> 0x01ec }
            monitor-exit(r10)
            return
        L_0x009e:
            disPathcherPageChangerEvent(r3, r11)     // Catch:{ all -> 0x01ec }
            com.ut.mini.module.UTOperationStack r1 = com.ut.mini.module.UTOperationStack.getInstance()     // Catch:{ all -> 0x01ec }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x01ec }
            r4.<init>()     // Catch:{ all -> 0x01ec }
            java.lang.String r5 = "pageAppear:"
            r4.append(r5)     // Catch:{ all -> 0x01ec }
            java.lang.Class r5 = r11.getClass()     // Catch:{ all -> 0x01ec }
            java.lang.String r5 = r5.getSimpleName()     // Catch:{ all -> 0x01ec }
            r4.append(r5)     // Catch:{ all -> 0x01ec }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x01ec }
            r1.addAction(r4)     // Catch:{ all -> 0x01ec }
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x01ec }
            java.lang.String r1 = r1.getH5Url()     // Catch:{ all -> 0x01ec }
            r4 = 0
            if (r1 == 0) goto L_0x0103
            com.ut.mini.UTVariables r5 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x01ec }
            r5.setBackupH5Url(r1)     // Catch:{ all -> 0x01ec }
            android.net.Uri r1 = android.net.Uri.parse(r1)     // Catch:{ Throwable -> 0x00f2 }
            java.lang.String r5 = "spm"
            java.lang.String r5 = r1.getQueryParameter(r5)     // Catch:{ Throwable -> 0x00f2 }
            java.lang.String r6 = "scm"
            java.lang.String r1 = r1.getQueryParameter(r6)     // Catch:{ Throwable -> 0x00f2 }
            java.util.Map<java.lang.String, java.lang.String> r6 = r10.mPageProperties     // Catch:{ Throwable -> 0x00f2 }
            java.lang.String r7 = "spm"
            r6.put(r7, r5)     // Catch:{ Throwable -> 0x00f2 }
            java.util.Map<java.lang.String, java.lang.String> r5 = r10.mPageProperties     // Catch:{ Throwable -> 0x00f2 }
            java.lang.String r6 = "scm"
            r5.put(r6, r1)     // Catch:{ Throwable -> 0x00f2 }
            goto L_0x00fc
        L_0x00f2:
            r1 = move-exception
            java.lang.String r5 = ""
            java.lang.Object[] r6 = new java.lang.Object[r2]     // Catch:{ all -> 0x01ec }
            r6[r3] = r1     // Catch:{ all -> 0x01ec }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ all -> 0x01ec }
        L_0x00fc:
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x01ec }
            r1.setH5Url(r4)     // Catch:{ all -> 0x01ec }
        L_0x0103:
            java.lang.String r1 = _getPageName(r11)     // Catch:{ all -> 0x01ec }
            boolean r5 = com.ut.mini.extend.UTExtendSwitch.bJTrackExtend     // Catch:{ all -> 0x01ec }
            if (r5 == 0) goto L_0x014e
            java.lang.Class r5 = r11.getClass()     // Catch:{ Throwable -> 0x014e }
            java.lang.String r5 = r5.getSimpleName()     // Catch:{ Throwable -> 0x014e }
            java.lang.String r5 = com.ut.mini.extend.JTrackExtend.getPageName(r5)     // Catch:{ Throwable -> 0x014e }
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x014e }
            if (r6 != 0) goto L_0x014e
            java.lang.String r6 = r5.toLowerCase()     // Catch:{ Throwable -> 0x014e }
            java.lang.String r7 = "activity"
            boolean r6 = r6.endsWith(r7)     // Catch:{ Throwable -> 0x014e }
            if (r6 == 0) goto L_0x0133
            int r6 = r5.length()     // Catch:{ Throwable -> 0x014e }
            int r6 = r6 + -8
            java.lang.String r5 = r5.substring(r3, r6)     // Catch:{ Throwable -> 0x014e }
        L_0x0133:
            java.lang.String r6 = "JTrack"
            java.lang.Object[] r7 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x014e }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x014e }
            r8.<init>()     // Catch:{ Throwable -> 0x014e }
            java.lang.String r9 = "getPageName:"
            r8.append(r9)     // Catch:{ Throwable -> 0x014e }
            r8.append(r5)     // Catch:{ Throwable -> 0x014e }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x014e }
            r7[r3] = r8     // Catch:{ Throwable -> 0x014e }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r6, (java.lang.Object[]) r7)     // Catch:{ Throwable -> 0x014e }
            r1 = r5
        L_0x014e:
            boolean r5 = com.alibaba.analytics.utils.StringUtils.isEmpty(r12)     // Catch:{ all -> 0x01ec }
            if (r5 != 0) goto L_0x0155
            goto L_0x0156
        L_0x0155:
            r12 = r1
        L_0x0156:
            java.lang.String r1 = r0.getPageName()     // Catch:{ all -> 0x01ec }
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ all -> 0x01ec }
            if (r1 != 0) goto L_0x0164
            java.lang.String r12 = r0.getPageName()     // Catch:{ all -> 0x01ec }
        L_0x0164:
            r10.mCurPage = r12     // Catch:{ all -> 0x01ec }
            r0.setPageName(r12)     // Catch:{ all -> 0x01ec }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01ec }
            r0.setPageAppearTimestamp(r5)     // Catch:{ all -> 0x01ec }
            long r5 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x01ec }
            r0.setPageStayTimstamp(r5)     // Catch:{ all -> 0x01ec }
            com.ut.mini.UTVariables r12 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x01ec }
            java.lang.String r12 = r12.getRefPage()     // Catch:{ all -> 0x01ec }
            r0.setRefPage(r12)     // Catch:{ all -> 0x01ec }
            r0.setPageAppearCalled()     // Catch:{ all -> 0x01ec }
            java.util.Map<java.lang.String, java.lang.String> r12 = r10.mNextPageProperties     // Catch:{ all -> 0x01ec }
            if (r12 == 0) goto L_0x01ae
            java.util.Map<java.lang.String, java.lang.String> r12 = r10.mNextPageProperties     // Catch:{ all -> 0x01ec }
            r10.mBackupNextPageProperties = r12     // Catch:{ all -> 0x01ec }
            java.util.Map<java.lang.String, java.lang.String> r12 = r10.mNextPageProperties     // Catch:{ all -> 0x01ec }
            r0.setNextPageProperties(r12)     // Catch:{ all -> 0x01ec }
            java.util.Map r12 = r0.getPageProperties()     // Catch:{ all -> 0x01ec }
            if (r12 != 0) goto L_0x019e
            java.util.Map<java.lang.String, java.lang.String> r12 = r10.mNextPageProperties     // Catch:{ all -> 0x01ec }
            r0.setPageProperties(r12)     // Catch:{ all -> 0x01ec }
            goto L_0x01ae
        L_0x019e:
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ all -> 0x01ec }
            r1.<init>()     // Catch:{ all -> 0x01ec }
            r1.putAll(r12)     // Catch:{ all -> 0x01ec }
            java.util.Map<java.lang.String, java.lang.String> r12 = r10.mNextPageProperties     // Catch:{ all -> 0x01ec }
            r1.putAll(r12)     // Catch:{ all -> 0x01ec }
            r0.setPageProperties(r1)     // Catch:{ all -> 0x01ec }
        L_0x01ae:
            r10.mNextPageProperties = r4     // Catch:{ all -> 0x01ec }
            java.lang.String r12 = r10._getPageEventObjectCacheKey(r11)     // Catch:{ all -> 0x01ec }
            r10.mCurrentPageCacheKey = r12     // Catch:{ all -> 0x01ec }
            boolean r12 = r10.mNextPageSkipBack     // Catch:{ all -> 0x01ec }
            if (r12 == 0) goto L_0x01c4
            com.ut.mini.UTPageHitHelper$UTPageStateObject r12 = r10.getOrNewUTPageStateObject(r11)     // Catch:{ all -> 0x01ec }
            if (r12 == 0) goto L_0x01c4
            r12.mIsSkipBack = r2     // Catch:{ all -> 0x01ec }
            r10.mNextPageSkipBack = r3     // Catch:{ all -> 0x01ec }
        L_0x01c4:
            r10._clearUTPageEventObjectCache(r0)     // Catch:{ all -> 0x01ec }
            java.lang.String r12 = r10._getPageEventObjectCacheKey(r11)     // Catch:{ all -> 0x01ec }
            r10._putUTPageEventObjectToCache(r12, r0)     // Catch:{ all -> 0x01ec }
            if (r13 == 0) goto L_0x01ea
            boolean r12 = r0.isSkipPage()     // Catch:{ all -> 0x01ec }
            if (r12 == 0) goto L_0x01ea
            com.ut.mini.UTPageHitHelper$UTPageStateObject r11 = r10.getOrNewUTPageStateObject(r11)     // Catch:{ all -> 0x01ec }
            if (r11 == 0) goto L_0x01ea
            r11.mIsFrame = r2     // Catch:{ all -> 0x01ec }
            goto L_0x01ea
        L_0x01df:
            java.lang.String r11 = "pageAppear"
            java.lang.Object[] r12 = new java.lang.Object[r2]     // Catch:{ all -> 0x01ec }
            java.lang.String r13 = "The page object should not be null"
            r12[r3] = r13     // Catch:{ all -> 0x01ec }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ all -> 0x01ec }
        L_0x01ea:
            monitor-exit(r10)
            return
        L_0x01ec:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.pageAppear(java.lang.Object, java.lang.String, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void pageAppear(Object obj, String str) {
        pageAppear(obj, str, false);
    }

    @Deprecated
    public synchronized void updatePageProperties(Map<String, String> map) {
        if (map != null) {
            this.mPageProperties.putAll(map);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updatePageProperties(java.lang.Object r3, java.util.Map<java.lang.String, java.lang.String> r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x0032
            if (r4 == 0) goto L_0x0032
            int r0 = r4.size()     // Catch:{ all -> 0x0041 }
            if (r0 != 0) goto L_0x000c
            goto L_0x0032
        L_0x000c:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0041 }
            r0.<init>()     // Catch:{ all -> 0x0041 }
            r0.putAll(r4)     // Catch:{ all -> 0x0041 }
            com.ut.mini.UTPageHitHelper$UTPageEventObject r3 = r2._getOrNewAUTPageEventObject(r3)     // Catch:{ all -> 0x0041 }
            java.util.Map r4 = r3.getPageProperties()     // Catch:{ all -> 0x0041 }
            if (r4 != 0) goto L_0x0022
            r3.setPageProperties(r0)     // Catch:{ all -> 0x0041 }
            goto L_0x0030
        L_0x0022:
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ all -> 0x0041 }
            r1.<init>()     // Catch:{ all -> 0x0041 }
            r1.putAll(r4)     // Catch:{ all -> 0x0041 }
            r1.putAll(r0)     // Catch:{ all -> 0x0041 }
            r3.setPageProperties(r1)     // Catch:{ all -> 0x0041 }
        L_0x0030:
            monitor-exit(r2)
            return
        L_0x0032:
            java.lang.String r3 = ""
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0041 }
            r0 = 0
            java.lang.String r1 = "failed to update project properties"
            r4[r0] = r1     // Catch:{ all -> 0x0041 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x0041 }
            monitor-exit(r2)
            return
        L_0x0041:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.updatePageProperties(java.lang.Object, java.util.Map):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Map<java.lang.String, java.lang.String> getPageProperties(java.lang.Object r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 != 0) goto L_0x0006
            r3 = 0
            monitor-exit(r2)
            return r3
        L_0x0006:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0023 }
            r0.<init>()     // Catch:{ all -> 0x0023 }
            java.util.Map<java.lang.String, java.lang.String> r1 = r2.mPageProperties     // Catch:{ all -> 0x0023 }
            if (r1 == 0) goto L_0x0014
            java.util.Map<java.lang.String, java.lang.String> r1 = r2.mPageProperties     // Catch:{ all -> 0x0023 }
            r0.putAll(r1)     // Catch:{ all -> 0x0023 }
        L_0x0014:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r3 = r2._getOrNewAUTPageEventObject(r3)     // Catch:{ all -> 0x0023 }
            java.util.Map r3 = r3.getPageProperties()     // Catch:{ all -> 0x0023 }
            if (r3 == 0) goto L_0x0021
            r0.putAll(r3)     // Catch:{ all -> 0x0023 }
        L_0x0021:
            monitor-exit(r2)
            return r0
        L_0x0023:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageProperties(java.lang.Object):java.util.Map");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updatePageUtparam(java.lang.Object r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x0037
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r4)     // Catch:{ all -> 0x0034 }
            if (r0 == 0) goto L_0x000a
            goto L_0x0037
        L_0x000a:
            java.util.Map r0 = r2.getPageProperties(r3)     // Catch:{ all -> 0x0034 }
            java.lang.String r1 = ""
            if (r0 == 0) goto L_0x001b
            java.lang.String r1 = "utparam-cnt"
            java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x0034 }
            r1 = r0
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0034 }
        L_0x001b:
            java.lang.String r4 = r2.refreshUtParam(r4, r1)     // Catch:{ all -> 0x0034 }
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x0034 }
            if (r0 != 0) goto L_0x0032
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0034 }
            r0.<init>()     // Catch:{ all -> 0x0034 }
            java.lang.String r1 = "utparam-cnt"
            r0.put(r1, r4)     // Catch:{ all -> 0x0034 }
            r2.updatePageProperties(r3, r0)     // Catch:{ all -> 0x0034 }
        L_0x0032:
            monitor-exit(r2)
            return
        L_0x0034:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0037:
            monitor-exit(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.updatePageUtparam(java.lang.Object, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updatePageName(java.lang.Object r2, java.lang.String r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 == 0) goto L_0x0018
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)     // Catch:{ all -> 0x0015 }
            if (r0 == 0) goto L_0x000a
            goto L_0x0018
        L_0x000a:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r2 = r1._getOrNewAUTPageEventObject(r2)     // Catch:{ all -> 0x0015 }
            r2.setPageName(r3)     // Catch:{ all -> 0x0015 }
            r1.mCurPage = r3     // Catch:{ all -> 0x0015 }
            monitor-exit(r1)
            return
        L_0x0015:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L_0x0018:
            monitor-exit(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.updatePageName(java.lang.Object, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageUrl(Object obj, Uri uri) {
        if (obj != null) {
            _getOrNewAUTPageEventObject(obj).setPageUrl(uri);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getPageUrl(java.lang.Object r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            r0 = 0
            if (r3 != 0) goto L_0x0006
            monitor-exit(r2)
            return r0
        L_0x0006:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r3 = r2._getOrNewAUTPageEventObject(r3)     // Catch:{ all -> 0x001e }
            if (r3 == 0) goto L_0x001c
            android.net.Uri r1 = r3.getPageUrl()     // Catch:{ all -> 0x001e }
            if (r1 == 0) goto L_0x001c
            android.net.Uri r3 = r3.getPageUrl()     // Catch:{ all -> 0x001e }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x001e }
            monitor-exit(r2)
            return r3
        L_0x001c:
            monitor-exit(r2)
            return r0
        L_0x001e:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageUrl(java.lang.Object):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePageStatus(Object obj, UTPageStatus uTPageStatus) {
        if (obj != null) {
            _getOrNewAUTPageEventObject(obj).setPageStatus(uTPageStatus);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNextPageProperties(Map<String, String> map) {
        if (map != null) {
            if (this.mNextPageProperties == null) {
                this.mNextPageProperties = new HashMap(map);
            } else {
                this.mNextPageProperties.putAll(map);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNextPageUtparam(String str) {
        if (!TextUtils.isEmpty(str)) {
            String str2 = "";
            if (this.mNextPageProperties != null) {
                str2 = this.mNextPageProperties.get(UTPARAM_URL);
            } else {
                this.mNextPageProperties = new HashMap();
            }
            String refreshUtParam = refreshUtParam(str, str2);
            if (!TextUtils.isEmpty(refreshUtParam)) {
                HashMap hashMap = new HashMap();
                hashMap.put(UTPARAM_URL, refreshUtParam);
                this.mNextPageProperties.putAll(hashMap);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNextPageUtparamCnt(String str) {
        if (!TextUtils.isEmpty(str)) {
            String str2 = "";
            if (this.mNextPageProperties != null) {
                str2 = this.mNextPageProperties.get("utparam-cnt");
            } else {
                this.mNextPageProperties = new HashMap();
            }
            String refreshUtParam = refreshUtParam(str, str2);
            if (!TextUtils.isEmpty(refreshUtParam)) {
                HashMap hashMap = new HashMap();
                hashMap.put("utparam-cnt", refreshUtParam);
                this.mNextPageProperties.putAll(hashMap);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void setPageStatusCode(Object obj, int i) {
        if (obj != null) {
            _getOrNewAUTPageEventObject(obj).setPageStatusCode(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void pageDisAppearByAuto(Activity activity) {
        if (!this.mIsTurnOff) {
            pageDisAppear(activity, UTAnalytics.getInstance().getDefaultTracker());
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipPage(Object obj) {
        if (obj != null) {
            _getOrNewAUTPageEventObject(obj).setToSkipPage();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void skipBack(java.lang.Object r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 != 0) goto L_0x0005
            monitor-exit(r1)
            return
        L_0x0005:
            com.ut.mini.UTPageHitHelper$UTPageStateObject r2 = r1.getOrNewUTPageStateObject(r2)     // Catch:{ all -> 0x0010 }
            if (r2 == 0) goto L_0x000e
            r0 = 1
            r2.mIsSkipBack = r0     // Catch:{ all -> 0x0010 }
        L_0x000e:
            monitor-exit(r1)
            return
        L_0x0010:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.skipBack(java.lang.Object):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void skipNextPageBack() {
        this.mNextPageSkipBack = true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void skipBackForever(java.lang.Object r1, boolean r2) {
        /*
            r0 = this;
            monitor-enter(r0)
            if (r1 != 0) goto L_0x0005
            monitor-exit(r0)
            return
        L_0x0005:
            com.ut.mini.UTPageHitHelper$UTPageStateObject r1 = r0.getOrNewUTPageStateObject(r1)     // Catch:{ all -> 0x000f }
            if (r1 == 0) goto L_0x000d
            r1.mIsSkipBackForever = r2     // Catch:{ all -> 0x000f }
        L_0x000d:
            monitor-exit(r0)
            return
        L_0x000f:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.skipBackForever(java.lang.Object, boolean):void");
    }

    private void _clearPageDisAppearContext() {
        this.mPageProperties = new HashMap();
        this.mCurrentPageCacheKey = null;
        this.mCurPage = null;
        this.mBackupNextPageProperties = null;
        UTVariables.getInstance().setBackupH5Url((String) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:282:0x05e4, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:100:0x0239 */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x023f A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x024a A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0288 A[SYNTHETIC, Splitter:B:120:0x0288] */
    /* JADX WARNING: Removed duplicated region for block: B:196:0x0383 A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0391 A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x0492  */
    /* JADX WARNING: Removed duplicated region for block: B:234:0x04ad A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:253:0x0503 A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x0576 A[Catch:{ Exception -> 0x04b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x0582 A[Catch:{ Exception -> 0x04b3 }] */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void pageDisAppear(java.lang.Object r31, com.ut.mini.UTTracker r32) {
        /*
            r30 = this;
            r7 = r30
            r8 = r31
            r9 = r32
            monitor-enter(r30)
            java.lang.String r0 = "UTPageHitHelper"
            r10 = 2
            java.lang.Object[] r1 = new java.lang.Object[r10]     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "pageDisAppear aPageObject"
            r11 = 0
            r1[r11] = r2     // Catch:{ all -> 0x05e5 }
            r12 = 1
            r1[r12] = r8     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x05e5 }
            com.ut.mini.module.trackerlistener.UTTrackerListenerMgr r0 = com.ut.mini.module.trackerlistener.UTTrackerListenerMgr.getInstance()     // Catch:{ all -> 0x05e5 }
            r0.pageDisAppear(r9, r8)     // Catch:{ all -> 0x05e5 }
            if (r8 == 0) goto L_0x05d6
            java.lang.String r0 = r7.mCurrentPageCacheKey     // Catch:{ all -> 0x05e5 }
            if (r0 != 0) goto L_0x0031
            java.lang.String r0 = "pageDisAppear"
            java.lang.Object[] r1 = new java.lang.Object[r12]     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "UT has already recorded the page disappear event on this page"
            r1[r11] = r2     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x05e5 }
            monitor-exit(r30)
            return
        L_0x0031:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r13 = r30._getOrNewAUTPageEventObject(r31)     // Catch:{ all -> 0x05e5 }
            boolean r14 = r13.isPageAppearCalled()     // Catch:{ all -> 0x05e5 }
            if (r14 == 0) goto L_0x058a
            com.ut.mini.module.UTOperationStack r0 = com.ut.mini.module.UTOperationStack.getInstance()     // Catch:{ all -> 0x05e5 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x05e5 }
            r1.<init>()     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "pageDisAppear:"
            r1.append(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.Class r2 = r31.getClass()     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r2.getSimpleName()     // Catch:{ all -> 0x05e5 }
            r1.append(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x05e5 }
            r0.addAction(r1)     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageStatus r0 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x00a9
            com.ut.mini.UTPageStatus r0 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageStatus r1 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r0 != r1) goto L_0x00a9
            int r0 = r13.getPageStatusCode()     // Catch:{ all -> 0x05e5 }
            if (r12 != r0) goto L_0x0082
            java.util.Map<java.lang.String, java.lang.String> r0 = r7.mBackupNextPageProperties     // Catch:{ all -> 0x05e5 }
            r7.mNextPageProperties = r0     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTVariables r0 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = r1.getBackupH5Url()     // Catch:{ all -> 0x05e5 }
            r0.setH5Url(r1)     // Catch:{ all -> 0x05e5 }
        L_0x0082:
            int r0 = r13.getPageStatusCode()     // Catch:{ all -> 0x05e5 }
            if (r12 == r0) goto L_0x008e
            boolean r0 = r13.isH5Called()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x00a9
        L_0x008e:
            java.lang.String r0 = "pageDisAppear"
            java.lang.Object[] r1 = new java.lang.Object[r12]     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "UTTracker.PAGE_STATUS_CODE_302 or PageEventObject.isH5Called"
            r1[r11] = r2     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageHitHelper$UTPageStateObject r0 = r30.getOrNewUTPageStateObject(r31)     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x00a1
            r0.mIsH5Page = r11     // Catch:{ all -> 0x05e5 }
        L_0x00a1:
            r7._releaseSkipFlagAndH5FlagPageObject(r13)     // Catch:{ all -> 0x05e5 }
            r30._clearPageDisAppearContext()     // Catch:{ all -> 0x05e5 }
            monitor-exit(r30)
            return
        L_0x00a9:
            long r5 = r13.getPageAppearTimestamp()     // Catch:{ all -> 0x05e5 }
            long r0 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x05e5 }
            long r2 = r13.getPageStayTimstamp()     // Catch:{ all -> 0x05e5 }
            r4 = 0
            long r10 = r0 - r2
            boolean r0 = r8 instanceof android.app.Activity     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0146
            disPathcherPageChangerEvent(r12, r8)     // Catch:{ all -> 0x05e5 }
            boolean r0 = com.alibaba.analytics.utils.Logger.isDebug()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0105
            r0 = r8
            android.app.Activity r0 = (android.app.Activity) r0     // Catch:{ all -> 0x05e5 }
            android.content.Intent r0 = r0.getIntent()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0105
            r0 = r8
            android.app.Activity r0 = (android.app.Activity) r0     // Catch:{ all -> 0x05e5 }
            android.content.Intent r0 = r0.getIntent()     // Catch:{ all -> 0x05e5 }
            android.net.Uri r0 = r0.getData()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0105
            java.lang.String r0 = "pageDisAppear"
            java.lang.Object[] r2 = new java.lang.Object[r12]     // Catch:{ all -> 0x05e5 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x05e5 }
            r3.<init>()     // Catch:{ all -> 0x05e5 }
            java.lang.String r4 = "uri="
            r3.append(r4)     // Catch:{ all -> 0x05e5 }
            r4 = r8
            android.app.Activity r4 = (android.app.Activity) r4     // Catch:{ all -> 0x05e5 }
            android.content.Intent r4 = r4.getIntent()     // Catch:{ all -> 0x05e5 }
            android.net.Uri r4 = r4.getData()     // Catch:{ all -> 0x05e5 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x05e5 }
            r3.append(r4)     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x05e5 }
            r4 = 0
            r2[r4] = r3     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r0, (java.lang.Object[]) r2)     // Catch:{ all -> 0x05e5 }
        L_0x0105:
            android.net.Uri r0 = r13.getPageUrl()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0110
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x05e5 }
            goto L_0x0111
        L_0x0110:
            r0 = 0
        L_0x0111:
            r2 = r8
            android.app.Activity r2 = (android.app.Activity) r2     // Catch:{ all -> 0x05e5 }
            android.content.Intent r2 = r2.getIntent()     // Catch:{ all -> 0x05e5 }
            if (r2 == 0) goto L_0x011f
            android.net.Uri r2 = r2.getData()     // Catch:{ all -> 0x05e5 }
            goto L_0x0120
        L_0x011f:
            r2 = 0
        L_0x0120:
            if (r2 == 0) goto L_0x0127
            java.lang.String r3 = r2.toString()     // Catch:{ all -> 0x05e5 }
            goto L_0x0128
        L_0x0127:
            r3 = 0
        L_0x0128:
            if (r0 == 0) goto L_0x0130
            boolean r4 = r0.equals(r3)     // Catch:{ all -> 0x05e5 }
            if (r4 == 0) goto L_0x0138
        L_0x0130:
            if (r3 == 0) goto L_0x013a
            boolean r0 = r3.equals(r0)     // Catch:{ all -> 0x05e5 }
            if (r0 != 0) goto L_0x013a
        L_0x0138:
            r0 = 1
            goto L_0x013b
        L_0x013a:
            r0 = 0
        L_0x013b:
            android.net.Uri r3 = r13.getPageUrl()     // Catch:{ all -> 0x05e5 }
            if (r3 != 0) goto L_0x0146
            if (r0 == 0) goto L_0x0146
            r13.setPageUrl(r2)     // Catch:{ all -> 0x05e5 }
        L_0x0146:
            java.lang.String r0 = r13.getPageName()     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r13.getRefPage()     // Catch:{ all -> 0x05e5 }
            if (r2 == 0) goto L_0x0156
            int r3 = r2.length()     // Catch:{ all -> 0x05e5 }
            if (r3 != 0) goto L_0x0158
        L_0x0156:
            java.lang.String r2 = "-"
        L_0x0158:
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mPageProperties     // Catch:{ all -> 0x05e5 }
            if (r3 != 0) goto L_0x0161
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x05e5 }
            r3.<init>()     // Catch:{ all -> 0x05e5 }
        L_0x0161:
            boolean r4 = com.ut.mini.extend.UTExtendSwitch.bJTrackExtend     // Catch:{ all -> 0x05e5 }
            if (r4 == 0) goto L_0x0237
            boolean r4 = r8 instanceof android.app.Activity     // Catch:{ Throwable -> 0x0237 }
            if (r4 == 0) goto L_0x0237
            r4 = r8
            android.app.Activity r4 = (android.app.Activity) r4     // Catch:{ Throwable -> 0x0237 }
            android.content.Intent r4 = r4.getIntent()     // Catch:{ Throwable -> 0x0237 }
            android.net.Uri r4 = r4.getData()     // Catch:{ Throwable -> 0x0237 }
            if (r4 == 0) goto L_0x0198
            java.lang.String r1 = "JTrack"
            java.lang.Object[] r15 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x0237 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0237 }
            r12.<init>()     // Catch:{ Throwable -> 0x0237 }
            r18 = r0
            java.lang.String r0 = "uri:"
            r12.append(r0)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = r4.toString()     // Catch:{ Throwable -> 0x0239 }
            r12.append(r0)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = r12.toString()     // Catch:{ Throwable -> 0x0239 }
            r12 = 0
            r15[r12] = r0     // Catch:{ Throwable -> 0x0239 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r1, (java.lang.Object[]) r15)     // Catch:{ Throwable -> 0x0239 }
            goto L_0x019a
        L_0x0198:
            r18 = r0
        L_0x019a:
            java.lang.String r0 = r13.getPageName()     // Catch:{ Throwable -> 0x0239 }
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0239 }
            if (r0 != 0) goto L_0x01d1
            java.lang.String r0 = r13.getPageName()     // Catch:{ Throwable -> 0x0239 }
            java.util.Map r1 = com.ut.mini.extend.JTrackExtend.getArgsMap((java.lang.String) r0, (android.net.Uri) r4)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = "JTrack"
            r15 = 1
            java.lang.Object[] r12 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x0239 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0239 }
            r15.<init>()     // Catch:{ Throwable -> 0x0239 }
            r19 = r1
            java.lang.String r1 = "getArgsMap by pagename:"
            r15.append(r1)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r1 = r13.getPageName()     // Catch:{ Throwable -> 0x0239 }
            r15.append(r1)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r1 = r15.toString()     // Catch:{ Throwable -> 0x0239 }
            r15 = 0
            r12[r15] = r1     // Catch:{ Throwable -> 0x0239 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r0, (java.lang.Object[]) r12)     // Catch:{ Throwable -> 0x0239 }
            r16 = r19
            goto L_0x01d3
        L_0x01d1:
            r16 = 0
        L_0x01d3:
            if (r16 == 0) goto L_0x01df
            int r0 = r16.size()     // Catch:{ Throwable -> 0x0239 }
            if (r0 != 0) goto L_0x01dc
            goto L_0x01df
        L_0x01dc:
            r0 = r16
            goto L_0x020b
        L_0x01df:
            r0 = r8
            android.app.Activity r0 = (android.app.Activity) r0     // Catch:{ Throwable -> 0x0239 }
            java.util.Map r16 = com.ut.mini.extend.JTrackExtend.getArgsMap((android.app.Activity) r0, (android.net.Uri) r4)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = "JTrack"
            r1 = 1
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0239 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0239 }
            r1.<init>()     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r12 = "getArgsMap by activity:"
            r1.append(r12)     // Catch:{ Throwable -> 0x0239 }
            java.lang.Class r12 = r31.getClass()     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r12 = r12.getName()     // Catch:{ Throwable -> 0x0239 }
            r1.append(r12)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0239 }
            r12 = 0
            r4[r12] = r1     // Catch:{ Throwable -> 0x0239 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r0, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x0239 }
            goto L_0x01dc
        L_0x020b:
            if (r0 == 0) goto L_0x0239
            int r1 = r0.size()     // Catch:{ Throwable -> 0x0239 }
            if (r1 <= 0) goto L_0x0239
            r3.putAll(r0)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r1 = "JTrack"
            r4 = 1
            java.lang.Object[] r12 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0239 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0239 }
            r4.<init>()     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r15 = "ArgsMap:"
            r4.append(r15)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = com.alibaba.analytics.utils.StringUtils.convertMapToString(r0)     // Catch:{ Throwable -> 0x0239 }
            r4.append(r0)     // Catch:{ Throwable -> 0x0239 }
            java.lang.String r0 = r4.toString()     // Catch:{ Throwable -> 0x0239 }
            r4 = 0
            r12[r4] = r0     // Catch:{ Throwable -> 0x0239 }
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r1, (java.lang.Object[]) r12)     // Catch:{ Throwable -> 0x0239 }
            goto L_0x0239
        L_0x0237:
            r18 = r0
        L_0x0239:
            java.util.Map r0 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0246
            java.util.Map r0 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            r3.putAll(r0)     // Catch:{ all -> 0x05e5 }
        L_0x0246:
            boolean r0 = r8 instanceof com.ut.mini.IUTPageTrack     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0278
            r0 = r8
            com.ut.mini.IUTPageTrack r0 = (com.ut.mini.IUTPageTrack) r0     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = r0.getReferPage()     // Catch:{ all -> 0x05e5 }
            boolean r4 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ all -> 0x05e5 }
            if (r4 != 0) goto L_0x0258
            r2 = r1
        L_0x0258:
            java.util.Map r1 = r0.getPageProperties()     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x026b
            int r4 = r1.size()     // Catch:{ all -> 0x05e5 }
            if (r4 <= 0) goto L_0x026b
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mPageProperties     // Catch:{ all -> 0x05e5 }
            r3.putAll(r1)     // Catch:{ all -> 0x05e5 }
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mPageProperties     // Catch:{ all -> 0x05e5 }
        L_0x026b:
            java.lang.String r0 = r0.getPageName()     // Catch:{ all -> 0x05e5 }
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)     // Catch:{ all -> 0x05e5 }
            if (r1 != 0) goto L_0x0278
            r4 = r0
            r12 = r3
            goto L_0x027b
        L_0x0278:
            r12 = r3
            r4 = r18
        L_0x027b:
            r3 = r2
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            java.lang.String r16 = ""
            android.net.Uri r15 = r13.getPageUrl()     // Catch:{ all -> 0x05e5 }
            if (r15 == 0) goto L_0x0383
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Throwable -> 0x036e }
            r0.<init>()     // Catch:{ Throwable -> 0x036e }
            r20 = r1
            java.lang.String r1 = r7._getSpmByUri(r15)     // Catch:{ Throwable -> 0x036c }
            boolean r17 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x036c }
            if (r17 != 0) goto L_0x02e5
            r21 = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02e2 }
            r2.<init>()     // Catch:{ Throwable -> 0x02e2 }
            java.lang.Class r17 = r31.getClass()     // Catch:{ Throwable -> 0x02e2 }
            r22 = r3
            java.lang.String r3 = r17.getSimpleName()     // Catch:{ Throwable -> 0x02df }
            r2.append(r3)     // Catch:{ Throwable -> 0x02df }
            int r3 = r31.hashCode()     // Catch:{ Throwable -> 0x02df }
            r2.append(r3)     // Catch:{ Throwable -> 0x02df }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x02df }
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mSPMObjectMap     // Catch:{ Throwable -> 0x02df }
            boolean r3 = r3.containsKey(r2)     // Catch:{ Throwable -> 0x02df }
            if (r3 == 0) goto L_0x02ce
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mSPMObjectMap     // Catch:{ Throwable -> 0x02df }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ Throwable -> 0x02df }
            boolean r3 = r1.equals(r3)     // Catch:{ Throwable -> 0x02df }
            if (r3 == 0) goto L_0x02ce
            r3 = 1
            goto L_0x02cf
        L_0x02ce:
            r3 = 0
        L_0x02cf:
            if (r3 != 0) goto L_0x02e9
            java.lang.String r3 = "spm"
            r0.put(r3, r1)     // Catch:{ Throwable -> 0x02df }
            java.util.Map<java.lang.String, java.lang.String> r3 = r7.mSPMObjectMap     // Catch:{ Throwable -> 0x02df }
            r3.put(r2, r1)     // Catch:{ Throwable -> 0x02df }
            r7._releaseSPMCacheObj(r2)     // Catch:{ Throwable -> 0x02df }
            goto L_0x02e9
        L_0x02df:
            r0 = move-exception
            goto L_0x0375
        L_0x02e2:
            r0 = move-exception
            goto L_0x0373
        L_0x02e5:
            r21 = r2
            r22 = r3
        L_0x02e9:
            java.lang.String r2 = "utparam"
            java.lang.String r2 = r15.getQueryParameter(r2)     // Catch:{ Throwable -> 0x0368 }
            java.lang.String r3 = "scm"
            java.lang.String r3 = r15.getQueryParameter(r3)     // Catch:{ Throwable -> 0x0360 }
            boolean r16 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x0358 }
            if (r16 != 0) goto L_0x030a
            r23 = r1
            java.lang.String r1 = "scm"
            r0.put(r1, r3)     // Catch:{ Throwable -> 0x0303 }
            goto L_0x030c
        L_0x0303:
            r0 = move-exception
            r21 = r2
            r16 = r3
            goto L_0x0377
        L_0x030a:
            r23 = r1
        L_0x030c:
            java.lang.String r1 = "pg1stepk"
            java.lang.String r1 = r15.getQueryParameter(r1)     // Catch:{ Throwable -> 0x0356 }
            boolean r16 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0356 }
            if (r16 != 0) goto L_0x0322
            r24 = r2
            java.lang.String r2 = "pg1stepk"
            r0.put(r2, r1)     // Catch:{ Throwable -> 0x0320 }
            goto L_0x0324
        L_0x0320:
            r0 = move-exception
            goto L_0x035d
        L_0x0322:
            r24 = r2
        L_0x0324:
            java.lang.String r1 = "point"
            java.lang.String r1 = r15.getQueryParameter(r1)     // Catch:{ Throwable -> 0x0320 }
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0320 }
            if (r1 != 0) goto L_0x0337
            java.lang.String r1 = "issb"
            java.lang.String r2 = "1"
            r0.put(r1, r2)     // Catch:{ Throwable -> 0x0320 }
        L_0x0337:
            java.lang.String r1 = _getOutsideTTID(r15)     // Catch:{ Throwable -> 0x0320 }
            boolean r2 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0320 }
            if (r2 != 0) goto L_0x0348
            com.alibaba.analytics.core.ClientVariables r2 = com.alibaba.analytics.core.ClientVariables.getInstance()     // Catch:{ Throwable -> 0x0320 }
            r2.setOutsideTTID(r1)     // Catch:{ Throwable -> 0x0320 }
        L_0x0348:
            int r1 = r0.size()     // Catch:{ Throwable -> 0x0320 }
            if (r1 <= 0) goto L_0x0351
            r12.putAll(r0)     // Catch:{ Throwable -> 0x0320 }
        L_0x0351:
            r16 = r3
            r21 = r24
            goto L_0x038b
        L_0x0356:
            r0 = move-exception
            goto L_0x035b
        L_0x0358:
            r0 = move-exception
            r23 = r1
        L_0x035b:
            r24 = r2
        L_0x035d:
            r16 = r3
            goto L_0x0365
        L_0x0360:
            r0 = move-exception
            r23 = r1
            r24 = r2
        L_0x0365:
            r21 = r24
            goto L_0x0377
        L_0x0368:
            r0 = move-exception
            r23 = r1
            goto L_0x0377
        L_0x036c:
            r0 = move-exception
            goto L_0x0371
        L_0x036e:
            r0 = move-exception
            r20 = r1
        L_0x0371:
            r21 = r2
        L_0x0373:
            r22 = r3
        L_0x0375:
            r23 = r20
        L_0x0377:
            java.lang.String r1 = ""
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ all -> 0x05e5 }
            r2 = 0
            r3[r2] = r0     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r1, (java.lang.Object[]) r3)     // Catch:{ all -> 0x05e5 }
            goto L_0x038b
        L_0x0383:
            r20 = r1
            r21 = r2
            r22 = r3
            r23 = r20
        L_0x038b:
            com.ut.mini.UTPageHitHelper$UTPageStateObject r0 = r30.getOrNewUTPageStateObject(r31)     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0492
            com.ut.mini.UTPageStatus r1 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x03b9
            com.ut.mini.UTPageStatus r1 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageStatus r3 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r1 != r3) goto L_0x03b9
            boolean r1 = r0.mIsBack     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x03a6
            r7.clearUTPageStateObject(r12)     // Catch:{ all -> 0x05e5 }
        L_0x03a6:
            r1 = 0
            java.util.Map r2 = r0.getPageStatMap(r1)     // Catch:{ all -> 0x05e5 }
            r12.putAll(r2)     // Catch:{ all -> 0x05e5 }
            r28 = r5
            r26 = r10
            r25 = r14
            r9 = r22
            r10 = r4
            goto L_0x0435
        L_0x03b9:
            java.lang.String r2 = r30._getPageEventObjectCacheKey(r31)     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = r7.mLastCacheKey     // Catch:{ all -> 0x05e5 }
            boolean r3 = r2.equals(r3)     // Catch:{ all -> 0x05e5 }
            boolean r2 = r0.mIsSwitchBackground     // Catch:{ all -> 0x05e5 }
            if (r2 != 0) goto L_0x0410
            java.lang.String r2 = "1"
            java.lang.String r1 = "skipbk"
            java.lang.Object r1 = r12.get(r1)     // Catch:{ all -> 0x05e5 }
            boolean r1 = r2.equals(r1)     // Catch:{ all -> 0x05e5 }
            if (r1 != 0) goto L_0x03dd
            boolean r1 = r0.mIsSkipBackForever     // Catch:{ all -> 0x05e5 }
            if (r1 != 0) goto L_0x03dd
            boolean r1 = r0.mIsSkipBack     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x03e2
        L_0x03dd:
            r1 = 0
            r0.mIsBack = r1     // Catch:{ all -> 0x05e5 }
            r0.mIsSkipBack = r1     // Catch:{ all -> 0x05e5 }
        L_0x03e2:
            boolean r1 = r0.mIsBack     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x03f8
            boolean r1 = r0.mIsFrame     // Catch:{ all -> 0x05e5 }
            if (r1 == 0) goto L_0x03ed
            if (r3 == 0) goto L_0x03ed
            goto L_0x03f8
        L_0x03ed:
            r28 = r5
            r26 = r10
            r25 = r14
            r9 = r22
            r14 = r3
            r10 = r4
            goto L_0x0420
        L_0x03f8:
            r1 = r30
            r2 = r0
            r25 = r14
            r9 = r22
            r14 = r3
            r3 = r12
            r26 = r10
            r10 = r4
            r4 = r23
            r28 = r5
            r5 = r21
            r6 = r16
            r1.refreshUTPageStateObject(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x05e5 }
            goto L_0x0420
        L_0x0410:
            r28 = r5
            r26 = r10
            r25 = r14
            r9 = r22
            r1 = 0
            r14 = r3
            r10 = r4
            r0.mIsBack = r1     // Catch:{ all -> 0x05e5 }
            r7.clearUTPageStateObject(r12)     // Catch:{ all -> 0x05e5 }
        L_0x0420:
            boolean r2 = r0.mIsBack     // Catch:{ all -> 0x05e5 }
            if (r2 == 0) goto L_0x0427
            r7.clearUTPageStateObject(r12)     // Catch:{ all -> 0x05e5 }
        L_0x0427:
            java.util.Map r2 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            r7.forceSetSpm(r0, r2)     // Catch:{ all -> 0x05e5 }
            java.util.Map r2 = r0.getPageStatMap(r14)     // Catch:{ all -> 0x05e5 }
            r12.putAll(r2)     // Catch:{ all -> 0x05e5 }
        L_0x0435:
            java.lang.String r2 = r30._getPageEventObjectCacheKey(r31)     // Catch:{ all -> 0x05e5 }
            r7.setLastCacheKey(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r0.mSpmUrl     // Catch:{ all -> 0x05e5 }
            r7.setLastCacheKeySpmUrl(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r0.mScmUrl     // Catch:{ all -> 0x05e5 }
            r7.setLastCacheKeyScmUrl(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r0.mUtparamUrl     // Catch:{ all -> 0x05e5 }
            r7.setLastCacheKeyUtParam(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r0.mUtparamCnt     // Catch:{ all -> 0x05e5 }
            r7.setLastCacheKeyUtParamCnt(r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = ""
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x05e5 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x05e5 }
            r3.<init>()     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = "mLastCacheKey:"
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = r7.mLastCacheKey     // Catch:{ all -> 0x05e5 }
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = ",mLastCacheKeySpmUrl:"
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = r7.mLastCacheKeySpmUrl     // Catch:{ all -> 0x05e5 }
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = ",mLastCacheKeyUtParam:"
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = r7.mLastCacheKeyUtParam     // Catch:{ all -> 0x05e5 }
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = ",mLastCacheKeyUtParamCnt:"
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = r7.mLastCacheKeyUtParamCnt     // Catch:{ all -> 0x05e5 }
            r3.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x05e5 }
            r1 = 0
            r4[r1] = r3     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ all -> 0x05e5 }
            r2 = 1
            r0.mIsBack = r2     // Catch:{ all -> 0x05e5 }
            r0.mIsSwitchBackground = r1     // Catch:{ all -> 0x05e5 }
            goto L_0x049b
        L_0x0492:
            r28 = r5
            r26 = r10
            r25 = r14
            r9 = r22
            r10 = r4
        L_0x049b:
            com.alibaba.analytics.core.config.UTTPKBiz r0 = com.alibaba.analytics.core.config.UTTPKBiz.getInstance()     // Catch:{ Exception -> 0x04b3 }
            android.net.Uri r2 = r13.getPageUrl()     // Catch:{ Exception -> 0x04b3 }
            java.lang.String r0 = r0.getTpkString(r2, r12)     // Catch:{ Exception -> 0x04b3 }
            boolean r2 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)     // Catch:{ Exception -> 0x04b3 }
            if (r2 != 0) goto L_0x04c3
            java.lang.String r2 = "_tpk"
            r12.put(r2, r0)     // Catch:{ Exception -> 0x04b3 }
            goto L_0x04c3
        L_0x04b3:
            r0 = move-exception
            java.lang.String r2 = ""
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x05e5 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x05e5 }
            r1 = 0
            r4[r1] = r0     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ all -> 0x05e5 }
        L_0x04c3:
            java.util.Map r0 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x04e1
            java.util.Map r0 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "_allow_override_value"
            boolean r0 = r0.containsKey(r2)     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x04e1
            java.util.Map r0 = r13.getPageProperties()     // Catch:{ all -> 0x05e5 }
            r12.putAll(r0)     // Catch:{ all -> 0x05e5 }
            java.lang.String r0 = "_allow_override_value"
            r12.remove(r0)     // Catch:{ all -> 0x05e5 }
        L_0x04e1:
            java.lang.String r0 = "Page_Webview"
            boolean r0 = r0.equalsIgnoreCase(r10)     // Catch:{ all -> 0x05e5 }
            if (r0 != 0) goto L_0x04f7
            com.ut.mini.UTPageStatus r0 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageStatus r2 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r0 != r2) goto L_0x0535
            boolean r0 = isDefaultActivityName(r8, r10)     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x0535
        L_0x04f7:
            if (r15 == 0) goto L_0x0535
            java.lang.String r0 = r15.toString()     // Catch:{ all -> 0x05e5 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x05e5 }
            if (r2 != 0) goto L_0x0535
            java.lang.String r2 = "?"
            int r2 = r0.indexOf(r2)     // Catch:{ all -> 0x05e5 }
            r3 = -1
            if (r2 == r3) goto L_0x0512
            r1 = 0
            java.lang.String r2 = r0.substring(r1, r2)     // Catch:{ all -> 0x05e5 }
            goto L_0x0513
        L_0x0512:
            r2 = r0
        L_0x0513:
            boolean r1 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x05e5 }
            if (r1 != 0) goto L_0x051b
            r4 = r2
            goto L_0x051c
        L_0x051b:
            r4 = r10
        L_0x051c:
            java.lang.String r1 = ""
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x05e5 }
            java.lang.String r5 = "temp"
            r6 = 0
            r3[r6] = r5     // Catch:{ all -> 0x05e5 }
            r5 = 1
            r3[r5] = r0     // Catch:{ all -> 0x05e5 }
            java.lang.String r0 = "urlForPageName"
            r5 = 2
            r3[r5] = r0     // Catch:{ all -> 0x05e5 }
            r0 = 3
            r3[r0] = r2     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r1, (java.lang.Object[]) r3)     // Catch:{ all -> 0x05e5 }
            goto L_0x0536
        L_0x0535:
            r4 = r10
        L_0x0536:
            com.ut.mini.UTHitBuilders$UTPageHitBuilder r0 = new com.ut.mini.UTHitBuilders$UTPageHitBuilder     // Catch:{ all -> 0x05e5 }
            r0.<init>(r4)     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTHitBuilders$UTPageHitBuilder r1 = r0.setReferPage(r9)     // Catch:{ all -> 0x05e5 }
            r2 = r26
            com.ut.mini.UTHitBuilders$UTPageHitBuilder r1 = r1.setDurationOnPage(r2)     // Catch:{ all -> 0x05e5 }
            r1.setProperties(r12)     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.core.model.LogField r1 = com.alibaba.analytics.core.model.LogField.RECORD_TIMESTAMP     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x05e5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x05e5 }
            r2.<init>()     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = ""
            r2.append(r3)     // Catch:{ all -> 0x05e5 }
            r5 = r28
            r2.append(r5)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x05e5 }
            r0.setProperty(r1, r2)     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = "_priority"
            java.lang.String r2 = "4"
            r0.setProperty(r1, r2)     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTVariables r1 = com.ut.mini.UTVariables.getInstance()     // Catch:{ all -> 0x05e5 }
            r1.setRefPage(r4)     // Catch:{ all -> 0x05e5 }
            r1 = r32
            if (r1 == 0) goto L_0x0582
            java.util.Map r0 = r0.build()     // Catch:{ all -> 0x05e5 }
            java.util.Map r0 = com.ut.mini.UTPvidHelper.processPagePvid(r0)     // Catch:{ all -> 0x05e5 }
            r1.send(r0)     // Catch:{ all -> 0x05e5 }
            goto L_0x05b1
        L_0x0582:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException     // Catch:{ all -> 0x05e5 }
            java.lang.String r1 = "Tracker instance is null,please init sdk first."
            r0.<init>(r1)     // Catch:{ all -> 0x05e5 }
            throw r0     // Catch:{ all -> 0x05e5 }
        L_0x058a:
            r25 = r14
            java.lang.String r0 = "UT"
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x05e5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x05e5 }
            r2.<init>()     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = "Please call pageAppear first("
            r2.append(r3)     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = _getPageName(r31)     // Catch:{ all -> 0x05e5 }
            r2.append(r3)     // Catch:{ all -> 0x05e5 }
            java.lang.String r3 = ")."
            r2.append(r3)     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x05e5 }
            r3 = 0
            r1[r3] = r2     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x05e5 }
        L_0x05b1:
            boolean r0 = r13.isSkipPage()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x05bb
            r7._releaseSkipFlagAndH5FlagPageObject(r13)     // Catch:{ all -> 0x05e5 }
            goto L_0x05d0
        L_0x05bb:
            com.ut.mini.UTPageStatus r0 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r0 == 0) goto L_0x05cd
            com.ut.mini.UTPageStatus r0 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x05e5 }
            com.ut.mini.UTPageStatus r1 = r13.getPageStatus()     // Catch:{ all -> 0x05e5 }
            if (r0 != r1) goto L_0x05cd
            r7._releaseSkipFlagAndH5FlagPageObject(r13)     // Catch:{ all -> 0x05e5 }
            goto L_0x05d0
        L_0x05cd:
            r30._removeUTPageEventObject(r31)     // Catch:{ all -> 0x05e5 }
        L_0x05d0:
            if (r25 == 0) goto L_0x05e3
            r30._clearPageDisAppearContext()     // Catch:{ all -> 0x05e5 }
            goto L_0x05e3
        L_0x05d6:
            java.lang.String r0 = "pageDisAppear"
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "The page object should not be null"
            r3 = 0
            r1[r3] = r2     // Catch:{ all -> 0x05e5 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x05e5 }
        L_0x05e3:
            monitor-exit(r30)
            return
        L_0x05e5:
            r0 = move-exception
            monitor-exit(r30)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.pageDisAppear(java.lang.Object, com.ut.mini.UTTracker):void");
    }

    private void forceSetSpm(UTPageStateObject uTPageStateObject, Map<String, String> map) {
        if (uTPageStateObject != null && map != null) {
            String str = map.get(FORCE_SPM_CNT);
            if (!TextUtils.isEmpty(str)) {
                uTPageStateObject.mSpmCnt = str;
            }
            String str2 = map.get(FORCE_SPM_URL);
            if (!TextUtils.isEmpty(str2)) {
                uTPageStateObject.mSpmUrl = str2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00c5 A[Catch:{ Throwable -> 0x008a }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00f0 A[Catch:{ Throwable -> 0x008a }] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00f9 A[Catch:{ Throwable -> 0x008a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Map<java.lang.String, java.lang.String> getPageAllProperties(android.app.Activity r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            r0 = 0
            r1 = 1
            r2 = 0
            if (r12 == 0) goto L_0x010a
            java.lang.String r3 = r11.mCurrentPageCacheKey     // Catch:{ all -> 0x0108 }
            if (r3 != 0) goto L_0x000c
            goto L_0x010a
        L_0x000c:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r3 = r11._getOrNewAUTPageEventObject(r12)     // Catch:{ all -> 0x0108 }
            boolean r4 = r3.isPageAppearCalled()     // Catch:{ all -> 0x0108 }
            if (r4 != 0) goto L_0x0023
            java.lang.String r12 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = "activity isPageAppearCalled is false"
            r1[r2] = r3     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r12, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0108 }
            monitor-exit(r11)
            return r0
        L_0x0023:
            com.ut.mini.UTPageStatus r4 = r3.getPageStatus()     // Catch:{ all -> 0x0108 }
            if (r4 == 0) goto L_0x003e
            com.ut.mini.UTPageStatus r4 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x0108 }
            com.ut.mini.UTPageStatus r5 = r3.getPageStatus()     // Catch:{ all -> 0x0108 }
            if (r4 != r5) goto L_0x003e
            java.lang.String r12 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = "activity is UT_H5_IN_WebView"
            r1[r2] = r3     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r12, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0108 }
            monitor-exit(r11)
            return r0
        L_0x003e:
            com.ut.mini.UTPageHitHelper$UTPageStateObject r4 = r11.getOrNewUTPageStateObject(r12)     // Catch:{ all -> 0x0108 }
            com.ut.mini.UTPageHitHelper$UTPageStateObject r4 = r11.copyUTPageStateObject(r4)     // Catch:{ all -> 0x0108 }
            if (r4 != 0) goto L_0x0055
            java.lang.String r12 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = "getOrNewUTPageStateObject is null"
            r1[r2] = r3     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r12, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0108 }
            monitor-exit(r11)
            return r0
        L_0x0055:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0108 }
            r0.<init>()     // Catch:{ all -> 0x0108 }
            java.util.Map<java.lang.String, java.lang.String> r1 = r11.mPageProperties     // Catch:{ all -> 0x0108 }
            r0.putAll(r1)     // Catch:{ all -> 0x0108 }
            java.util.Map r1 = r3.getPageProperties()     // Catch:{ all -> 0x0108 }
            if (r1 == 0) goto L_0x006c
            java.util.Map r1 = r3.getPageProperties()     // Catch:{ all -> 0x0108 }
            r0.putAll(r1)     // Catch:{ all -> 0x0108 }
        L_0x006c:
            java.lang.String r1 = ""
            java.lang.String r5 = ""
            java.lang.String r6 = ""
            android.net.Uri r3 = r3.getPageUrl()     // Catch:{ all -> 0x0108 }
            if (r3 != 0) goto L_0x0082
            android.content.Intent r7 = r12.getIntent()     // Catch:{ all -> 0x0108 }
            if (r7 == 0) goto L_0x0082
            android.net.Uri r3 = r7.getData()     // Catch:{ all -> 0x0108 }
        L_0x0082:
            if (r3 == 0) goto L_0x00b4
            java.lang.String r7 = r11._getSpmByUri(r3)     // Catch:{ Throwable -> 0x008a }
            r1 = r7
            goto L_0x0092
        L_0x008a:
            r7 = move-exception
            java.lang.String r8 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e(r8, r7, r9)     // Catch:{ all -> 0x0108 }
        L_0x0092:
            java.lang.String r7 = "utparam"
            java.lang.String r7 = r3.getQueryParameter(r7)     // Catch:{ Throwable -> 0x009a }
            r5 = r7
            goto L_0x00a2
        L_0x009a:
            r7 = move-exception
            java.lang.String r8 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e(r8, r7, r9)     // Catch:{ all -> 0x0108 }
        L_0x00a2:
            java.lang.String r7 = "scm"
            java.lang.String r3 = r3.getQueryParameter(r7)     // Catch:{ Throwable -> 0x00ac }
            r8 = r1
            r10 = r3
            r9 = r5
            goto L_0x00b7
        L_0x00ac:
            r3 = move-exception
            java.lang.String r7 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r8 = new java.lang.Object[r2]     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e(r7, r3, r8)     // Catch:{ all -> 0x0108 }
        L_0x00b4:
            r8 = r1
            r9 = r5
            r10 = r6
        L_0x00b7:
            java.lang.String r12 = r11._getPageEventObjectCacheKey(r12)     // Catch:{ all -> 0x0108 }
            java.lang.String r1 = r11.mLastCacheKey     // Catch:{ all -> 0x0108 }
            boolean r12 = r12.equals(r1)     // Catch:{ all -> 0x0108 }
            boolean r1 = r4.mIsSwitchBackground     // Catch:{ all -> 0x0108 }
            if (r1 != 0) goto L_0x00f0
            java.lang.String r1 = "1"
            java.lang.String r3 = "skipbk"
            java.lang.Object r3 = r0.get(r3)     // Catch:{ all -> 0x0108 }
            boolean r1 = r1.equals(r3)     // Catch:{ all -> 0x0108 }
            if (r1 != 0) goto L_0x00db
            boolean r1 = r4.mIsSkipBackForever     // Catch:{ all -> 0x0108 }
            if (r1 != 0) goto L_0x00db
            boolean r1 = r4.mIsSkipBack     // Catch:{ all -> 0x0108 }
            if (r1 == 0) goto L_0x00df
        L_0x00db:
            r4.mIsBack = r2     // Catch:{ all -> 0x0108 }
            r4.mIsSkipBack = r2     // Catch:{ all -> 0x0108 }
        L_0x00df:
            boolean r1 = r4.mIsBack     // Catch:{ all -> 0x0108 }
            if (r1 == 0) goto L_0x00e9
            boolean r1 = r4.mIsFrame     // Catch:{ all -> 0x0108 }
            if (r1 == 0) goto L_0x00f5
            if (r12 == 0) goto L_0x00f5
        L_0x00e9:
            r5 = r11
            r6 = r4
            r7 = r0
            r5.refreshUTPageStateObject(r6, r7, r8, r9, r10)     // Catch:{ all -> 0x0108 }
            goto L_0x00f5
        L_0x00f0:
            r4.mIsBack = r2     // Catch:{ all -> 0x0108 }
            r11.clearUTPageStateObject(r0)     // Catch:{ all -> 0x0108 }
        L_0x00f5:
            boolean r1 = r4.mIsBack     // Catch:{ all -> 0x0108 }
            if (r1 == 0) goto L_0x00fc
            r11.clearUTPageStateObject(r0)     // Catch:{ all -> 0x0108 }
        L_0x00fc:
            r11.forceSetSpm(r4, r0)     // Catch:{ all -> 0x0108 }
            java.util.Map r12 = r4.getPageStatMap(r12)     // Catch:{ all -> 0x0108 }
            r0.putAll(r12)     // Catch:{ all -> 0x0108 }
            monitor-exit(r11)
            return r0
        L_0x0108:
            r12 = move-exception
            goto L_0x0117
        L_0x010a:
            java.lang.String r12 = "getPagePropertiesWithSpmAndUtparam"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = "activity or CurrentPageCacheKey is null"
            r1[r2] = r3     // Catch:{ all -> 0x0108 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r12, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0108 }
            monitor-exit(r11)
            return r0
        L_0x0117:
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageAllProperties(android.app.Activity):java.util.Map");
    }

    public static Map<String, String> encodeUtParam(Map<String, String> map) {
        if (map == null) {
            return map;
        }
        try {
            String str = map.get("utparam-cnt");
            if (!TextUtils.isEmpty(str)) {
                map.put("utparam-cnt", URLEncoder.encode(str));
            }
        } catch (Throwable th) {
            Logger.e((String) null, th, new Object[0]);
        }
        try {
            String str2 = map.get(UTPARAM_URL);
            if (!TextUtils.isEmpty(str2)) {
                map.put(UTPARAM_URL, URLEncoder.encode(str2));
            }
        } catch (Throwable th2) {
            Logger.e((String) null, th2, new Object[0]);
        }
        try {
            String str3 = map.get("utparam-pre");
            if (!TextUtils.isEmpty(str3)) {
                map.put("utparam-pre", URLEncoder.encode(str3));
            }
        } catch (Throwable th3) {
            Logger.e((String) null, th3, new Object[0]);
        }
        return map;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:14|15|16|(1:20)|(3:22|23|24)|25|26|(2:28|29)(4:30|31|(2:33|(5:35|(1:39)|40|(1:43)|(3:45|(1:47)(2:48|(1:50)(1:51))|52)(1:53))(1:54))|(1:56))) */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a3, code lost:
        return r1;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0039 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x003f A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0041 A[SYNTHETIC, Splitter:B:30:0x0041] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getPageSpmUrl(android.app.Activity r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 != 0) goto L_0x000a
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x0007:
            r6 = move-exception
            goto L_0x00a4
        L_0x000a:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r0 = r5._getOrNewAUTPageEventObject(r6)     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r1 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 == 0) goto L_0x0020
            com.ut.mini.UTPageStatus r1 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r2 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 != r2) goto L_0x0020
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x0020:
            java.lang.String r1 = ""
            android.net.Uri r2 = r0.getPageUrl()     // Catch:{ all -> 0x0007 }
            if (r2 != 0) goto L_0x0032
            android.content.Intent r3 = r6.getIntent()     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0032
            android.net.Uri r2 = r3.getData()     // Catch:{ all -> 0x0007 }
        L_0x0032:
            if (r2 == 0) goto L_0x0039
            java.lang.String r2 = r5._getSpmByUri(r2)     // Catch:{ Throwable -> 0x0039 }
            r1 = r2
        L_0x0039:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0007 }
            if (r2 != 0) goto L_0x0041
            monitor-exit(r5)
            return r1
        L_0x0041:
            com.ut.mini.UTPageHitHelper$UTPageStateObject r2 = r5.getOrNewUTPageStateObject(r6)     // Catch:{ all -> 0x0007 }
            if (r2 == 0) goto L_0x009e
            boolean r1 = r2.mIsBack     // Catch:{ all -> 0x0007 }
            boolean r3 = r2.mIsSwitchBackground     // Catch:{ all -> 0x0007 }
            if (r3 != 0) goto L_0x009c
            boolean r3 = r2.mIsSkipBackForever     // Catch:{ all -> 0x0007 }
            r4 = 0
            if (r3 != 0) goto L_0x0056
            boolean r3 = r2.mIsSkipBack     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0057
        L_0x0056:
            r1 = 0
        L_0x0057:
            java.lang.String r6 = r5._getPageEventObjectCacheKey(r6)     // Catch:{ all -> 0x0007 }
            java.lang.String r3 = r5.mLastCacheKey     // Catch:{ all -> 0x0007 }
            boolean r6 = r6.equals(r3)     // Catch:{ all -> 0x0007 }
            boolean r3 = r2.mIsFrame     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0068
            if (r6 == 0) goto L_0x0068
            r1 = 0
        L_0x0068:
            if (r1 != 0) goto L_0x0098
            java.util.Map r6 = r0.getPageProperties()     // Catch:{ all -> 0x0007 }
            java.lang.String r0 = "spm-url"
            java.lang.Object r0 = r6.get(r0)     // Catch:{ all -> 0x0007 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0007 }
            java.lang.String r1 = "spm_url"
            java.lang.Object r1 = r6.get(r1)     // Catch:{ all -> 0x0007 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0007 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0007 }
            if (r2 != 0) goto L_0x0085
            goto L_0x0096
        L_0x0085:
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0007 }
            if (r0 != 0) goto L_0x008d
            r0 = r1
            goto L_0x0096
        L_0x008d:
            java.lang.String r0 = "spm"
            java.lang.Object r6 = r6.get(r0)     // Catch:{ all -> 0x0007 }
            r0 = r6
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0007 }
        L_0x0096:
            r1 = r0
            goto L_0x009e
        L_0x0098:
            java.lang.String r6 = r2.mSpmUrl     // Catch:{ all -> 0x0007 }
            r1 = r6
            goto L_0x009e
        L_0x009c:
            java.lang.String r1 = r2.mSpmUrl     // Catch:{ all -> 0x0007 }
        L_0x009e:
            if (r1 != 0) goto L_0x00a2
            java.lang.String r1 = ""
        L_0x00a2:
            monitor-exit(r5)
            return r1
        L_0x00a4:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageSpmUrl(android.app.Activity):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0060, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getPageSpmPre(android.app.Activity r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 != 0) goto L_0x0009
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x0007:
            r6 = move-exception
            goto L_0x0061
        L_0x0009:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r0 = r5._getOrNewAUTPageEventObject(r6)     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r1 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 == 0) goto L_0x001f
            com.ut.mini.UTPageStatus r1 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r0 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 != r0) goto L_0x001f
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x001f:
            java.lang.String r0 = ""
            com.ut.mini.UTPageHitHelper$UTPageStateObject r1 = r5.getOrNewUTPageStateObject(r6)     // Catch:{ all -> 0x0007 }
            if (r1 == 0) goto L_0x005b
            boolean r2 = r1.mIsBack     // Catch:{ all -> 0x0007 }
            boolean r3 = r1.mIsSwitchBackground     // Catch:{ all -> 0x0007 }
            if (r3 != 0) goto L_0x0059
            boolean r3 = r1.mIsSkipBackForever     // Catch:{ all -> 0x0007 }
            r4 = 0
            if (r3 != 0) goto L_0x0036
            boolean r3 = r1.mIsSkipBack     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0037
        L_0x0036:
            r2 = 0
        L_0x0037:
            java.lang.String r6 = r5._getPageEventObjectCacheKey(r6)     // Catch:{ all -> 0x0007 }
            java.lang.String r3 = r5.mLastCacheKey     // Catch:{ all -> 0x0007 }
            boolean r6 = r6.equals(r3)     // Catch:{ all -> 0x0007 }
            boolean r3 = r1.mIsFrame     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0048
            if (r6 == 0) goto L_0x0048
            r2 = 0
        L_0x0048:
            if (r2 != 0) goto L_0x0055
            java.lang.String r6 = r5.mLastCacheKey     // Catch:{ all -> 0x0007 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x0007 }
            if (r6 != 0) goto L_0x005b
            java.lang.String r6 = r5.mLastCacheKeySpmUrl     // Catch:{ all -> 0x0007 }
            goto L_0x0057
        L_0x0055:
            java.lang.String r6 = r1.mSpmPre     // Catch:{ all -> 0x0007 }
        L_0x0057:
            r0 = r6
            goto L_0x005b
        L_0x0059:
            java.lang.String r0 = r1.mSpmPre     // Catch:{ all -> 0x0007 }
        L_0x005b:
            if (r0 != 0) goto L_0x005f
            java.lang.String r0 = ""
        L_0x005f:
            monitor-exit(r5)
            return r0
        L_0x0061:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageSpmPre(android.app.Activity):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0060, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getPageScmPre(android.app.Activity r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 != 0) goto L_0x0009
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x0007:
            r6 = move-exception
            goto L_0x0061
        L_0x0009:
            com.ut.mini.UTPageHitHelper$UTPageEventObject r0 = r5._getOrNewAUTPageEventObject(r6)     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r1 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 == 0) goto L_0x001f
            com.ut.mini.UTPageStatus r1 = com.ut.mini.UTPageStatus.UT_H5_IN_WebView     // Catch:{ all -> 0x0007 }
            com.ut.mini.UTPageStatus r0 = r0.getPageStatus()     // Catch:{ all -> 0x0007 }
            if (r1 != r0) goto L_0x001f
            java.lang.String r6 = ""
            monitor-exit(r5)
            return r6
        L_0x001f:
            java.lang.String r0 = ""
            com.ut.mini.UTPageHitHelper$UTPageStateObject r1 = r5.getOrNewUTPageStateObject(r6)     // Catch:{ all -> 0x0007 }
            if (r1 == 0) goto L_0x005b
            boolean r2 = r1.mIsBack     // Catch:{ all -> 0x0007 }
            boolean r3 = r1.mIsSwitchBackground     // Catch:{ all -> 0x0007 }
            if (r3 != 0) goto L_0x0059
            boolean r3 = r1.mIsSkipBackForever     // Catch:{ all -> 0x0007 }
            r4 = 0
            if (r3 != 0) goto L_0x0036
            boolean r3 = r1.mIsSkipBack     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0037
        L_0x0036:
            r2 = 0
        L_0x0037:
            java.lang.String r6 = r5._getPageEventObjectCacheKey(r6)     // Catch:{ all -> 0x0007 }
            java.lang.String r3 = r5.mLastCacheKey     // Catch:{ all -> 0x0007 }
            boolean r6 = r6.equals(r3)     // Catch:{ all -> 0x0007 }
            boolean r3 = r1.mIsFrame     // Catch:{ all -> 0x0007 }
            if (r3 == 0) goto L_0x0048
            if (r6 == 0) goto L_0x0048
            r2 = 0
        L_0x0048:
            if (r2 != 0) goto L_0x0055
            java.lang.String r6 = r5.mLastCacheKey     // Catch:{ all -> 0x0007 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x0007 }
            if (r6 != 0) goto L_0x005b
            java.lang.String r6 = r5.mLastCacheKeyScmUrl     // Catch:{ all -> 0x0007 }
            goto L_0x0057
        L_0x0055:
            java.lang.String r6 = r1.mScmPre     // Catch:{ all -> 0x0007 }
        L_0x0057:
            r0 = r6
            goto L_0x005b
        L_0x0059:
            java.lang.String r0 = r1.mScmPre     // Catch:{ all -> 0x0007 }
        L_0x005b:
            if (r0 != 0) goto L_0x005f
            java.lang.String r0 = ""
        L_0x005f:
            monitor-exit(r5)
            return r0
        L_0x0061:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.getPageScmPre(android.app.Activity):java.lang.String");
    }

    @Deprecated
    public synchronized void pageDisAppear(Object obj) {
        pageDisAppear(obj, UTAnalytics.getInstance().getDefaultTracker());
    }

    private static String _getOutsideTTID(Uri uri) {
        List<String> queryParameters;
        if (uri == null || (queryParameters = uri.getQueryParameters("ttid")) == null) {
            return null;
        }
        for (String next : queryParameters) {
            if (!next.contains(DinamicConstant.DINAMIC_PREFIX_AT) && !next.contains("%40")) {
                return next;
            }
        }
        return null;
    }

    private static String _getPageName(Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        return (simpleName == null || !simpleName.toLowerCase().endsWith("activity")) ? simpleName : simpleName.substring(0, simpleName.length() - 8);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r1 = _getPageName(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isDefaultActivityName(java.lang.Object r1, java.lang.String r2) {
        /*
            boolean r0 = r1 instanceof android.app.Activity
            if (r0 == 0) goto L_0x0012
            java.lang.String r1 = _getPageName(r1)
            if (r1 == 0) goto L_0x0012
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x0012
            r1 = 1
            return r1
        L_0x0012:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper.isDefaultActivityName(java.lang.Object, java.lang.String):boolean");
    }

    public void pageDestroyed(Activity activity) {
        String _getPageEventObjectCacheKey = _getPageEventObjectCacheKey(activity);
        if (this.mPageStateObjects.containsKey(_getPageEventObjectCacheKey)) {
            this.mPageStateObjects.remove(_getPageEventObjectCacheKey);
        }
        if (this.mClearUTPageStateObjectList.contains(_getPageEventObjectCacheKey)) {
            this.mClearUTPageStateObjectList.remove(_getPageEventObjectCacheKey);
        }
        _releaseUTPageStateObject();
    }

    public void pageSwitchBackground() {
        UTPageStateObject uTPageStateObject;
        if (this.mPageStateObjects.containsKey(this.mLastCacheKey) && (uTPageStateObject = this.mPageStateObjects.get(this.mLastCacheKey)) != null) {
            uTPageStateObject.mIsSwitchBackground = true;
        }
    }

    public synchronized UTPageStateObject getOrNewUTPageStateObject(Object obj) {
        if (!(obj instanceof Activity)) {
            return null;
        }
        String _getPageEventObjectCacheKey = _getPageEventObjectCacheKey(obj);
        if (!this.mClearUTPageStateObjectList.contains(_getPageEventObjectCacheKey)) {
            this.mClearUTPageStateObjectList.add(_getPageEventObjectCacheKey);
        }
        if (this.mPageStateObjects.containsKey(_getPageEventObjectCacheKey)) {
            return this.mPageStateObjects.get(_getPageEventObjectCacheKey);
        }
        UTPageStateObject uTPageStateObject = new UTPageStateObject();
        this.mPageStateObjects.put(_getPageEventObjectCacheKey, uTPageStateObject);
        return uTPageStateObject;
    }

    private synchronized void _releaseUTPageStateObject() {
        if (this.mClearUTPageStateObjectList.size() > 100) {
            for (int i = 0; i < 50; i++) {
                String poll = this.mClearUTPageStateObjectList.poll();
                if (poll != null && this.mPageStateObjects.containsKey(poll)) {
                    this.mPageStateObjects.remove(poll);
                }
            }
        }
    }

    private void clearUTPageStateObject(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            map.remove(Constants.PARAM_OUTER_SPM_CNT);
            map.remove("spm-url");
            map.remove("spm-pre");
            map.remove("utparam-cnt");
            map.remove(UTPARAM_URL);
            map.remove("utparam-pre");
            map.remove("scm-pre");
        }
    }

    private void refreshUTPageStateObject(UTPageStateObject uTPageStateObject, Map<String, String> map, String str, String str2, String str3) {
        String str4 = map.get(Constants.PARAM_OUTER_SPM_CNT);
        if (!TextUtils.isEmpty(str4)) {
            uTPageStateObject.mSpmCnt = str4;
        } else {
            uTPageStateObject.mSpmCnt = map.get("spm_cnt");
        }
        uTPageStateObject.mSpmUrl = getSpmUrl(map, str);
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            uTPageStateObject.mSpmPre = this.mLastCacheKeySpmUrl;
        } else {
            uTPageStateObject.mSpmPre = "";
        }
        if (!TextUtils.isEmpty(str3)) {
            uTPageStateObject.mScmUrl = str3;
        } else {
            uTPageStateObject.mScmUrl = map.get(UTHelper.SCM_URI_PARAMETER_KEY);
        }
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            uTPageStateObject.mScmPre = this.mLastCacheKeyScmUrl;
        } else {
            uTPageStateObject.mScmPre = "";
        }
        String str5 = map.get("utparam-cnt");
        if (!TextUtils.isEmpty(str5)) {
            uTPageStateObject.mUtparamCnt = str5;
        } else {
            uTPageStateObject.mUtparamCnt = "";
        }
        String str6 = "";
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            str6 = this.mLastCacheKeyUtParamCnt;
        }
        uTPageStateObject.mUtparamUrl = refreshUtParam(str2, refreshUtParam(map.get(UTPARAM_URL), str6));
        if (!TextUtils.isEmpty(this.mLastCacheKey)) {
            uTPageStateObject.mUtparamPre = this.mLastCacheKeyUtParam;
        } else {
            uTPageStateObject.mUtparamPre = "";
        }
    }

    private String getSpmUrl(Map<String, String> map, String str) {
        if (map == null) {
            return str;
        }
        String str2 = map.get("spm-url");
        if (!TextUtils.isEmpty(str2)) {
            return str2;
        }
        String str3 = map.get("spm_url");
        if (!TextUtils.isEmpty(str3)) {
            return str3;
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String str4 = map.get("spm");
        return !TextUtils.isEmpty(str4) ? str4 : "";
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String _getSpmByUri(android.net.Uri r7) {
        /*
            r6 = this;
            java.lang.String r0 = "spm"
            java.lang.String r0 = r7.getQueryParameter(r0)
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)
            r2 = 0
            if (r1 == 0) goto L_0x0031
            java.lang.String r1 = r7.toString()     // Catch:{ Exception -> 0x0029 }
            java.lang.String r3 = "UTF-8"
            java.lang.String r1 = java.net.URLDecoder.decode(r1, r3)     // Catch:{ Exception -> 0x0029 }
            android.net.Uri r1 = android.net.Uri.parse(r1)     // Catch:{ Exception -> 0x0029 }
            java.lang.String r7 = "spm"
            java.lang.String r7 = r1.getQueryParameter(r7)     // Catch:{ Exception -> 0x0024 }
            r0 = r7
            r7 = r1
            goto L_0x0031
        L_0x0024:
            r7 = move-exception
            r5 = r1
            r1 = r7
            r7 = r5
            goto L_0x002a
        L_0x0029:
            r1 = move-exception
        L_0x002a:
            java.lang.String r3 = ""
            java.lang.Object[] r4 = new java.lang.Object[r2]
            com.alibaba.analytics.utils.Logger.w(r3, r1, r4)
        L_0x0031:
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x0061
            java.lang.String r0 = "spm_url"
            java.lang.String r0 = r7.getQueryParameter(r0)
            boolean r1 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x0061
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0059 }
            java.lang.String r1 = "UTF-8"
            java.lang.String r7 = java.net.URLDecoder.decode(r7, r1)     // Catch:{ Exception -> 0x0059 }
            android.net.Uri r7 = android.net.Uri.parse(r7)     // Catch:{ Exception -> 0x0059 }
            java.lang.String r1 = "spm_url"
            java.lang.String r7 = r7.getQueryParameter(r1)     // Catch:{ Exception -> 0x0059 }
            r0 = r7
            goto L_0x0061
        L_0x0059:
            r7 = move-exception
            java.lang.String r1 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.alibaba.analytics.utils.Logger.w(r1, r7, r2)
        L_0x0061:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTPageHitHelper._getSpmByUri(android.net.Uri):java.lang.String");
    }

    public String refreshUtParam(String str, String str2) {
        Map<String, Object> parseJsonToMap;
        Map<String, Object> parseJsonToMap2;
        try {
            if (!TextUtils.isEmpty(str) && (parseJsonToMap = parseJsonToMap(str)) != null) {
                if (parseJsonToMap.size() >= 1) {
                    if (!TextUtils.isEmpty(str2) && (parseJsonToMap2 = parseJsonToMap(str2)) != null) {
                        if (parseJsonToMap2.size() >= 1) {
                            parseJsonToMap2.putAll(parseJsonToMap);
                            return JSON.toJSONString(parseJsonToMap2);
                        }
                    }
                    return str;
                }
            }
            return str2;
        } catch (Exception e) {
            Logger.d("", e);
            return "";
        }
    }

    private Map<String, Object> parseJsonToMap(String str) {
        try {
            return (Map) JSON.parseObject(str, Map.class);
        } catch (Exception unused) {
            return null;
        }
    }
}
