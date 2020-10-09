package com.taobao.weex.module;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.android.shop.utils.ShopTrackHelper;
import com.taobao.android.task.Coordinator;
import com.taobao.monitor.terminator.ApmGodEye;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NavPrefetchShopFetchManager {
    public static final String PAGE_SHOPRENDER_ERROR = "Page_ShopRender_Error";
    public static final String SHOP_MODULE = "Shop";
    private static final String TAG = "NavPrefetchShop";
    private static NavPrefetchShopFetchManager instance;
    private Map<String, JSONObject> fetchCache = new ConcurrentHashMap();
    private Map<String, String> fetchCacheString = new ConcurrentHashMap();
    private SoftReference<FetchDataResultListener> mListener;
    private Map<String, Long> mtopStartTimeCahce = new ConcurrentHashMap();

    public interface FetchDataResultListener {
        void onReceiveFetchData(String str, JSONObject jSONObject, long j);

        void onReceiveFetchDataString(String str, String str2, long j);
    }

    private NavPrefetchShopFetchManager() {
    }

    public static NavPrefetchShopFetchManager getInstance() {
        if (instance == null) {
            synchronized (NavPrefetchShopFetchManager.class) {
                if (instance == null) {
                    instance = new NavPrefetchShopFetchManager();
                }
            }
        }
        return instance;
    }

    /* access modifiers changed from: private */
    public void putShopFetchData(String str, JSONObject jSONObject, long j) {
        if (this.fetchCache.size() > 1) {
            AppMonitor.Alarm.commitFail(SHOP_MODULE, PAGE_SHOPRENDER_ERROR, str, "-61007", "fetchCache more than 1, clear. " + this.fetchCache.keySet());
            reportFail("-61007", "fetchCache more than 1, will clear.", "dataProcess", this.fetchCache.keySet());
            this.fetchCache.clear();
        }
        if (this.mListener == null || this.mListener.get() == null) {
            this.fetchCache.put(str, jSONObject);
            this.mtopStartTimeCahce.put(str, Long.valueOf(j));
            return;
        }
        this.mListener.get().onReceiveFetchData(str, jSONObject, j);
    }

    /* access modifiers changed from: private */
    public void putShopFetchDataString(String str, String str2, long j) {
        if (this.fetchCache.size() > 1) {
            AppMonitor.Alarm.commitFail(SHOP_MODULE, PAGE_SHOPRENDER_ERROR, str, "-61007", "fetchCache more than 1, will clear." + this.fetchCache.keySet());
            reportFail("-61007", "fetchCache more than 1, will clear.", "dataProcess", this.fetchCache.keySet());
            this.fetchCache.clear();
        }
        if (this.mListener == null || this.mListener.get() == null) {
            this.fetchCacheString.put(str, str2);
            this.mtopStartTimeCahce.put(str, Long.valueOf(j));
            return;
        }
        this.mListener.get().onReceiveFetchDataString(str, str2, j);
    }

    public void setFetchDataListener(String str, FetchDataResultListener fetchDataResultListener) {
        if (fetchDataResultListener != null) {
            if (!TextUtils.isEmpty(str) && this.fetchCache.get(str) != null) {
                fetchDataResultListener.onReceiveFetchData(str, this.fetchCache.remove(str), this.mtopStartTimeCahce.get(str) != null ? this.mtopStartTimeCahce.get(str).longValue() : System.currentTimeMillis());
            } else if (TextUtils.isEmpty(str) || this.fetchCacheString.get(str) == null) {
                this.mListener = new SoftReference<>(fetchDataResultListener);
            } else {
                fetchDataResultListener.onReceiveFetchDataString(str, this.fetchCacheString.remove(str), this.mtopStartTimeCahce.get(str) != null ? this.mtopStartTimeCahce.get(str).longValue() : System.currentTimeMillis());
            }
        }
    }

    public void clear() {
        this.fetchCache.clear();
    }

    public void removeFetchDataListener(FetchDataResultListener fetchDataResultListener) {
        if (fetchDataResultListener != null && this.mListener != null && fetchDataResultListener == this.mListener.get()) {
            this.mListener.clear();
        }
    }

    public void processDataPrefetchInNavAsync(String str) {
        ApmGodEye.onStage("BIZ", "shopRender_fetch_send_before", new Map[0]);
        final long currentTimeMillis = System.currentTimeMillis();
        final String str2 = str;
        Coordinator.execute(new Coordinator.TaggedRunnable("shopRender-data-prefetch-in-nav") {
            /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r12 = this;
                    r0 = 1
                    r1 = 0
                    java.lang.String r2 = r6     // Catch:{ Throwable -> 0x02e6 }
                    android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r3 = r2.isHierarchical()     // Catch:{ Throwable -> 0x02e6 }
                    if (r3 != 0) goto L_0x000f
                    return
                L_0x000f:
                    java.lang.String r3 = "h5.m.taobao.com"
                    java.lang.String r4 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r3 = r3.equals(r4)     // Catch:{ Throwable -> 0x02e6 }
                    if (r3 == 0) goto L_0x003d
                    java.lang.String r3 = "/weex/viewpage.htm"
                    java.lang.String r4 = r2.getPath()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r3 = r3.equals(r4)     // Catch:{ Throwable -> 0x02e6 }
                    if (r3 == 0) goto L_0x003d
                    java.lang.String r3 = "weex_original_url"
                    java.lang.String r3 = r2.getQueryParameter(r3)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x02e6 }
                    if (r3 != 0) goto L_0x003d
                    java.lang.String r3 = "weex_original_url"
                    java.lang.String r2 = r2.getQueryParameter(r3)     // Catch:{ Throwable -> 0x02e6 }
                    android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Throwable -> 0x02e6 }
                L_0x003d:
                    java.lang.String r3 = "shopId"
                    java.lang.String r3 = r2.getQueryParameter(r3)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x0085
                    java.lang.String r4 = r6     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x0085
                    java.lang.String r4 = r6     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = "shopId"
                    boolean r4 = r4.contains(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x0085
                    java.lang.String r4 = "shopId="
                    java.lang.String r5 = r6     // Catch:{ Throwable -> 0x02e6 }
                    int r5 = r5.indexOf(r4)     // Catch:{ Throwable -> 0x02e6 }
                    if (r5 <= 0) goto L_0x0085
                    java.lang.String r6 = r6     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r7 = "&"
                    int r6 = r6.indexOf(r7, r5)     // Catch:{ Throwable -> 0x02e6 }
                    r7 = -1
                    if (r6 == r7) goto L_0x006d
                    goto L_0x0073
                L_0x006d:
                    java.lang.String r6 = r6     // Catch:{ Throwable -> 0x02e6 }
                    int r6 = r6.length()     // Catch:{ Throwable -> 0x02e6 }
                L_0x0073:
                    int r7 = r4.length()     // Catch:{ Throwable -> 0x02e6 }
                    int r7 = r7 + r5
                    if (r7 >= r6) goto L_0x0085
                    java.lang.String r3 = r6     // Catch:{ Throwable -> 0x02e6 }
                    int r4 = r4.length()     // Catch:{ Throwable -> 0x02e6 }
                    int r5 = r5 + r4
                    java.lang.String r3 = r3.substring(r5, r6)     // Catch:{ Throwable -> 0x02e6 }
                L_0x0085:
                    com.alibaba.android.prefetchx.PrefetchX r4 = com.alibaba.android.prefetchx.PrefetchX.getInstance()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.isReady()     // Catch:{ Throwable -> 0x02e6 }
                    r5 = 0
                    if (r4 == 0) goto L_0x010d
                    boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x010d
                    com.alibaba.android.prefetchx.core.data.PFMtop r4 = com.alibaba.android.prefetchx.core.data.PFMtop.getInstance()     // Catch:{ Throwable -> 0x02e6 }
                    android.app.Application r6 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = r4.generatePrefetchString(r6, r2, r5)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r6 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x02e6 }
                    if (r6 != 0) goto L_0x013e
                    mtopsdk.mtop.domain.MtopRequest r5 = new mtopsdk.mtop.domain.MtopRequest     // Catch:{ Throwable -> 0x02e6 }
                    r5.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    com.alibaba.fastjson.JSONObject r4 = com.alibaba.fastjson.JSON.parseObject(r4)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "api"
                    java.lang.String r6 = r4.getString(r6)     // Catch:{ Throwable -> 0x02e6 }
                    r5.setApiName(r6)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "v"
                    java.lang.String r6 = r4.getString(r6)     // Catch:{ Throwable -> 0x02e6 }
                    r5.setVersion(r6)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "true"
                    java.lang.String r7 = "ecode"
                    java.lang.String r7 = r4.getString(r7)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r6 = r6.equals(r7)     // Catch:{ Throwable -> 0x02e6 }
                    r5.setNeedEcode(r6)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "data"
                    com.alibaba.fastjson.JSONObject r6 = r4.getJSONObject(r6)     // Catch:{ Throwable -> 0x02e6 }
                    if (r6 != 0) goto L_0x00de
                    java.lang.String r6 = "param"
                    com.alibaba.fastjson.JSONObject r6 = r4.getJSONObject(r6)     // Catch:{ Throwable -> 0x02e6 }
                L_0x00de:
                    java.util.HashMap r4 = new java.util.HashMap     // Catch:{ Throwable -> 0x02e6 }
                    r4.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    if (r6 == 0) goto L_0x0101
                    java.util.Set r7 = r6.keySet()     // Catch:{ Throwable -> 0x02e6 }
                    java.util.Iterator r7 = r7.iterator()     // Catch:{ Throwable -> 0x02e6 }
                L_0x00ed:
                    boolean r8 = r7.hasNext()     // Catch:{ Throwable -> 0x02e6 }
                    if (r8 == 0) goto L_0x0101
                    java.lang.Object r8 = r7.next()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r9 = r6.getString(r8)     // Catch:{ Throwable -> 0x02e6 }
                    r4.put(r8, r9)     // Catch:{ Throwable -> 0x02e6 }
                    goto L_0x00ed
                L_0x0101:
                    r5.dataParams = r4     // Catch:{ Throwable -> 0x02e6 }
                    java.util.Map<java.lang.String, java.lang.String> r4 = r5.dataParams     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = mtopsdk.mtop.util.ReflectUtil.converMapToDataStr(r4)     // Catch:{ Throwable -> 0x02e6 }
                    r5.setData(r4)     // Catch:{ Throwable -> 0x02e6 }
                    goto L_0x013e
                L_0x010d:
                    com.alibaba.android.prefetchx.PrefetchXLauncher r4 = new com.alibaba.android.prefetchx.PrefetchXLauncher     // Catch:{ Throwable -> 0x011d }
                    r4.<init>()     // Catch:{ Throwable -> 0x011d }
                    android.app.Application r6 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Throwable -> 0x011d }
                    java.util.HashMap r7 = new java.util.HashMap     // Catch:{ Throwable -> 0x011d }
                    r7.<init>()     // Catch:{ Throwable -> 0x011d }
                    r4.init(r6, r7)     // Catch:{ Throwable -> 0x011d }
                    goto L_0x0137
                L_0x011d:
                    r4 = move-exception
                    r10 = r4
                    java.lang.String r4 = "Shop"
                    java.lang.String r6 = "Page_ShopRender_Error"
                    java.lang.String r7 = "-61008"
                    java.lang.String r8 = "PrefetchX init exception in nav."
                    com.alibaba.mtl.appmonitor.AppMonitor.Alarm.commitFail(r4, r6, r7, r8)     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.weex.module.NavPrefetchShopFetchManager r6 = com.taobao.weex.module.NavPrefetchShopFetchManager.this     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r7 = "-61008"
                    java.lang.String r8 = "PrefetchX init exception in nav."
                    java.lang.String r9 = "dataProcess"
                    java.lang.Object[] r11 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x02e6 }
                    r6.reportFail(r7, r8, r9, r10, r11)     // Catch:{ Throwable -> 0x02e6 }
                L_0x0137:
                    java.lang.String r4 = "NavPrefetchShop"
                    java.lang.String r6 = "main-cost PrefetchX not inited. ignore prefetch in nav this time."
                    android.util.Log.w(r4, r6)     // Catch:{ Throwable -> 0x02e6 }
                L_0x013e:
                    if (r5 != 0) goto L_0x01d6
                    boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x01d6
                    java.lang.String r4 = "true"
                    com.taobao.orange.OrangeConfig r6 = com.taobao.orange.OrangeConfig.getInstance()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r7 = "shop_render"
                    java.lang.String r8 = "use_static_default_mtop_prefetch"
                    java.lang.String r9 = "true"
                    java.lang.String r6 = r6.getConfig(r7, r8, r9)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r6)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x01d6
                    java.lang.String r4 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "market."
                    boolean r4 = r4.contains(r6)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x01cf
                    java.lang.String r4 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = ".taobao.com"
                    boolean r4 = r4.contains(r6)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x01cf
                    java.lang.String r4 = "/apps/market/shop/weex.html"
                    java.lang.String r6 = r2.getPath()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r6)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x01cf
                    mtopsdk.mtop.domain.MtopRequest r4 = new mtopsdk.mtop.domain.MtopRequest     // Catch:{ Throwable -> 0x02e6 }
                    r4.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = "mtop.taobao.wireless.shop.fetch"
                    r4.setApiName(r5)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = "2.0"
                    r4.setVersion(r5)     // Catch:{ Throwable -> 0x02e6 }
                    r4.setNeedEcode(r1)     // Catch:{ Throwable -> 0x02e6 }
                    java.util.HashMap r5 = new java.util.HashMap     // Catch:{ Throwable -> 0x02e6 }
                    r5.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "shopId"
                    java.lang.String r7 = "shopId"
                    java.lang.String r7 = r2.getQueryParameter(r7)     // Catch:{ Throwable -> 0x02e6 }
                    r5.put(r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "sellerId"
                    java.lang.String r7 = "sellerId"
                    java.lang.String r7 = r2.getQueryParameter(r7)     // Catch:{ Throwable -> 0x02e6 }
                    r5.put(r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "shopNavi"
                    java.lang.String r7 = "shop_navi"
                    java.lang.String r7 = r2.getQueryParameter(r7)     // Catch:{ Throwable -> 0x02e6 }
                    r5.put(r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "originUrl"
                    java.lang.String r7 = "shop_origin"
                    java.lang.String r7 = r2.getQueryParameter(r7)     // Catch:{ Throwable -> 0x02e6 }
                    r5.put(r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                    r4.dataParams = r5     // Catch:{ Throwable -> 0x02e6 }
                    java.util.Map<java.lang.String, java.lang.String> r5 = r4.dataParams     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = mtopsdk.mtop.util.ReflectUtil.converMapToDataStr(r5)     // Catch:{ Throwable -> 0x02e6 }
                    r4.setData(r5)     // Catch:{ Throwable -> 0x02e6 }
                    r5 = r4
                L_0x01cf:
                    java.lang.String r4 = "NavPrefetchShop"
                    java.lang.String r6 = "main-cost no prefetch string by PrefetchX. use default mtop config （hard code）."
                    android.util.Log.w(r4, r6)     // Catch:{ Throwable -> 0x02e6 }
                L_0x01d6:
                    if (r5 != 0) goto L_0x02a2
                    java.lang.String r4 = "NavPrefetchShop"
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02e6 }
                    r5.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "main-cost no prefetch string by PrefetchX. "
                    r5.append(r6)     // Catch:{ Throwable -> 0x02e6 }
                    r5.append(r3)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x02e6 }
                    android.util.Log.w(r4, r5)     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r2.isHierarchical()     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 == 0) goto L_0x0267
                    java.lang.String r4 = "detail.m.tmall.com"
                    java.lang.String r5 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x0246
                    java.lang.String r4 = "/app/tb-source-app/shopindex/pages/index"
                    java.lang.String r5 = r2.getPath()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x0246
                    java.lang.String r4 = "/app/tb-source-app/wz111/pages/error"
                    java.lang.String r5 = r2.getPath()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x0246
                    java.lang.String r4 = "ts.tmall.com"
                    java.lang.String r5 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x0246
                    java.lang.String r4 = "tmyp.tmall.com"
                    java.lang.String r5 = r2.getHost()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x02e6 }
                    if (r4 != 0) goto L_0x0246
                    java.lang.String r4 = "/app/trip/fliggy-shop/pages/index"
                    java.lang.String r2 = r2.getPath()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r2 = r4.equals(r2)     // Catch:{ Throwable -> 0x02e6 }
                    if (r2 != 0) goto L_0x0246
                    java.lang.String r2 = r6     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = "pages.tmall.com/wow/"
                    boolean r2 = r2.contains(r4)     // Catch:{ Throwable -> 0x02e6 }
                    if (r2 == 0) goto L_0x0267
                L_0x0246:
                    java.lang.String r2 = "NavPrefetchShop"
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02e6 }
                    r4.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r5 = "empty prefetch string. "
                    r4.append(r5)     // Catch:{ Throwable -> 0x02e6 }
                    r4.append(r3)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r3 = " "
                    r4.append(r3)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r3 = r6     // Catch:{ Throwable -> 0x02e6 }
                    r4.append(r3)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r3 = r4.toString()     // Catch:{ Throwable -> 0x02e6 }
                    android.util.Log.d(r2, r3)     // Catch:{ Throwable -> 0x02e6 }
                    goto L_0x02a1
                L_0x0267:
                    java.util.Random r2 = new java.util.Random     // Catch:{ Throwable -> 0x02e6 }
                    r2.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    r4 = 100
                    int r2 = r2.nextInt(r4)     // Catch:{ Throwable -> 0x02e6 }
                    r4 = 94
                    if (r2 <= r4) goto L_0x02a1
                    java.lang.String r2 = "Shop"
                    java.lang.String r4 = "Page_ShopRender_Error"
                    java.lang.String r5 = r6     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r6 = "-61006"
                    java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02e6 }
                    r7.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r8 = "empty prefetch string."
                    r7.append(r8)     // Catch:{ Throwable -> 0x02e6 }
                    r7.append(r3)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x02e6 }
                    com.alibaba.mtl.appmonitor.AppMonitor.Alarm.commitFail(r2, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.weex.module.NavPrefetchShopFetchManager r2 = com.taobao.weex.module.NavPrefetchShopFetchManager.this     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = "-61006"
                    java.lang.String r5 = "empty prefetch string."
                    java.lang.String r6 = "dataProcess"
                    java.lang.Object[] r7 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x02e6 }
                    r7[r1] = r3     // Catch:{ Throwable -> 0x02e6 }
                    r2.reportFail(r4, r5, r6, r7)     // Catch:{ Throwable -> 0x02e6 }
                L_0x02a1:
                    return
                L_0x02a2:
                    com.taobao.tao.remotebusiness.MtopBusiness r4 = com.taobao.tao.remotebusiness.MtopBusiness.build((mtopsdk.mtop.domain.MtopRequest) r5)     // Catch:{ Throwable -> 0x02e6 }
                    mtopsdk.mtop.domain.ProtocolEnum r5 = mtopsdk.mtop.domain.ProtocolEnum.HTTPSECURE     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.tao.remotebusiness.MtopBusiness r5 = r4.protocol((mtopsdk.mtop.domain.ProtocolEnum) r5)     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.tao.remotebusiness.MtopBusiness r5 = r5.useCache()     // Catch:{ Throwable -> 0x02e6 }
                    mtopsdk.mtop.domain.MethodEnum r6 = mtopsdk.mtop.domain.MethodEnum.GET     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.tao.remotebusiness.MtopBusiness r5 = r5.reqMethod((mtopsdk.mtop.domain.MethodEnum) r6)     // Catch:{ Throwable -> 0x02e6 }
                    com.taobao.weex.module.NavPrefetchShopFetchManager$1$1 r6 = new com.taobao.weex.module.NavPrefetchShopFetchManager$1$1     // Catch:{ Throwable -> 0x02e6 }
                    r6.<init>(r2, r3)     // Catch:{ Throwable -> 0x02e6 }
                    r5.registerListener((com.taobao.tao.remotebusiness.IRemoteListener) r6)     // Catch:{ Throwable -> 0x02e6 }
                    r4.startRequest()     // Catch:{ Throwable -> 0x02e6 }
                    boolean r2 = com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ Throwable -> 0x02e6 }
                    if (r2 == 0) goto L_0x02fe
                    java.lang.String r2 = "NavPrefetchShop"
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02e6 }
                    r3.<init>()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = "main-cost navFetchData fired in nav. "
                    r3.append(r4)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r4 = r4.getName()     // Catch:{ Throwable -> 0x02e6 }
                    r3.append(r4)     // Catch:{ Throwable -> 0x02e6 }
                    java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x02e6 }
                    android.util.Log.i(r2, r3)     // Catch:{ Throwable -> 0x02e6 }
                    goto L_0x02fe
                L_0x02e6:
                    r2 = move-exception
                    r7 = r2
                    java.lang.String r2 = "error in sending processDataPrefetchInNavAsync navFetchData."
                    com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r7)
                    com.taobao.weex.module.NavPrefetchShopFetchManager r3 = com.taobao.weex.module.NavPrefetchShopFetchManager.this
                    java.lang.String r4 = "-61010"
                    java.lang.String r5 = "error in sending processDataPrefetchInNavAsync navFetchData."
                    java.lang.String r6 = "dataProcess"
                    java.lang.Object[] r8 = new java.lang.Object[r0]
                    java.lang.String r0 = r6
                    r8[r1] = r0
                    r3.reportFail(r4, r5, r6, r7, r8)
                L_0x02fe:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.module.NavPrefetchShopFetchManager.AnonymousClass1.run():void");
            }
        }, 20);
    }

    /* access modifiers changed from: private */
    public void reportFail(String str, String str2, String str3, Object... objArr) {
        ShopTrackHelper.sReportFail(str, str2, str3, objArr);
    }

    /* access modifiers changed from: private */
    public void reportFail(String str, String str2, String str3, Throwable th, Object... objArr) {
        ShopTrackHelper.sReportFail(str, str2, str3, th, objArr);
    }
}
