package com.alibaba.android.prefetchx.core.image;

import android.os.SystemClock;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.config.OrangeRemoteConfigImpl;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.PhenixEvent;
import com.taobao.phenix.intf.event.PrefetchEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.TaobaoImageUrlStrategy;
import com.taobao.weaver.prefetch.PrefetchStatusResponse;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PFImage {
    private static volatile PFImage instance;
    public Boolean hasPhenix = null;
    /* access modifiers changed from: private */
    public boolean hasWMLPrefetchDependency = true;
    private Map<String, ImageConfigDO> imageConfigMap = null;
    RemoteConfigSpec.IImageModuleRemoteConfig imageRemoteConfig = PrefetchX.getInstance().getGlobalOnlineConfigManager().getImageModuleConfig();
    private long lastTimeRefreshConfigMap = 0;
    private int mConfigTryTimes = 0;
    private ImageConfigDO mDefaultImageConfigDO;
    ImageStrategyConfig mImageStrategyConfig = null;

    private PFImage() {
        if (this.imageRemoteConfig == null) {
            this.imageRemoteConfig = new OrangeRemoteConfigImpl.ImageModuleRemoteConf();
        }
        if (this.imageRemoteConfig.isImageByDefault()) {
            this.mDefaultImageConfigDO = new ImageConfigDO();
            this.mDefaultImageConfigDO.setCount(this.imageRemoteConfig.getDefaultImageCount());
            this.mDefaultImageConfigDO.setDenominator(this.imageRemoteConfig.getDefaultImageSizeDenominator());
        }
        getImageConfigWithMemoryCache("meanlessKeyForInitCache", this.imageRemoteConfig.getConfigMapMaxAgeInMemory());
    }

    public static PFImage getInstance() {
        if (instance == null) {
            synchronized (PFImage.class) {
                if (instance == null) {
                    instance = new PFImage();
                }
            }
        }
        return instance;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006f, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.JSONObject valid(java.util.List r5, java.lang.String r6) {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x0074
            int r5 = r5.size()
            if (r5 != 0) goto L_0x0009
            goto L_0x0074
        L_0x0009:
            java.lang.Class<com.alibaba.android.prefetchx.core.image.PFImage> r5 = com.alibaba.android.prefetchx.core.image.PFImage.class
            monitor-enter(r5)
            java.lang.Boolean r0 = r4.hasPhenix     // Catch:{ all -> 0x0071 }
            if (r0 != 0) goto L_0x004b
            boolean r0 = com.alibaba.android.prefetchx.PFUtil.isDebug()     // Catch:{ ClassNotFoundException -> 0x0029 }
            if (r0 == 0) goto L_0x001c
            java.lang.String r0 = "com.taobao.phenix.intf.Phenix"
            java.lang.Class.forName(r0)     // Catch:{ ClassNotFoundException -> 0x0029 }
            goto L_0x0021
        L_0x001c:
            java.lang.String r0 = "com.taobao.phenix.intf.b"
            java.lang.Class.forName(r0)     // Catch:{ ClassNotFoundException -> 0x0029 }
        L_0x0021:
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)     // Catch:{ ClassNotFoundException -> 0x0029 }
            r4.hasPhenix = r0     // Catch:{ ClassNotFoundException -> 0x0029 }
            goto L_0x006e
        L_0x0029:
            r0 = 0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)     // Catch:{ all -> 0x0071 }
            r4.hasPhenix = r0     // Catch:{ all -> 0x0071 }
            java.lang.String r0 = ""
            java.lang.String r1 = "-16605022"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0071 }
            r2.<init>()     // Catch:{ all -> 0x0071 }
            java.lang.String r3 = "no_phenix_support_"
            r2.append(r3)     // Catch:{ all -> 0x0071 }
            r2.append(r6)     // Catch:{ all -> 0x0071 }
            java.lang.String r6 = r2.toString()     // Catch:{ all -> 0x0071 }
            com.alibaba.fastjson.JSONObject r6 = com.alibaba.android.prefetchx.PFUtil.getJSCallbackError(r0, r1, r6)     // Catch:{ all -> 0x0071 }
            monitor-exit(r5)     // Catch:{ all -> 0x0071 }
            return r6
        L_0x004b:
            java.lang.Boolean r0 = r4.hasPhenix     // Catch:{ all -> 0x0071 }
            boolean r0 = r0.booleanValue()     // Catch:{ all -> 0x0071 }
            if (r0 != 0) goto L_0x006e
            java.lang.String r0 = ""
            java.lang.String r1 = "-16605022"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0071 }
            r2.<init>()     // Catch:{ all -> 0x0071 }
            java.lang.String r3 = "no_phenix_support_"
            r2.append(r3)     // Catch:{ all -> 0x0071 }
            r2.append(r6)     // Catch:{ all -> 0x0071 }
            java.lang.String r6 = r2.toString()     // Catch:{ all -> 0x0071 }
            com.alibaba.fastjson.JSONObject r6 = com.alibaba.android.prefetchx.PFUtil.getJSCallbackError(r0, r1, r6)     // Catch:{ all -> 0x0071 }
            monitor-exit(r5)     // Catch:{ all -> 0x0071 }
            return r6
        L_0x006e:
            monitor-exit(r5)     // Catch:{ all -> 0x0071 }
            r5 = 0
            return r5
        L_0x0071:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0071 }
            throw r6
        L_0x0074:
            java.lang.String r5 = ""
            java.lang.String r0 = "-16605021"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "no_images_"
            r1.append(r2)
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            com.alibaba.fastjson.JSONObject r5 = com.alibaba.android.prefetchx.PFUtil.getJSCallbackError(r5, r0, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.image.PFImage.valid(java.util.List, java.lang.String):com.alibaba.fastjson.JSONObject");
    }

    /* JADX WARNING: type inference failed for: r15v1, types: [boolean] */
    /* JADX WARNING: type inference failed for: r15v4 */
    /* JADX WARNING: type inference failed for: r15v5 */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0170  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01a8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<java.lang.Boolean, java.util.Map<java.lang.String, java.lang.Object>> prefetchImage(java.lang.String r19, java.lang.String r20) {
        /*
            r18 = this;
            r9 = r18
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IImageModuleRemoteConfig r0 = r9.imageRemoteConfig
            boolean r0 = r0.isImageEnable()
            r10 = 0
            if (r0 != 0) goto L_0x001f
            java.lang.String r0 = ""
            java.lang.String r1 = "-16605001"
            java.lang.String r2 = "prefetchx_image_disable"
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.android.prefetchx.PFUtil.getJSCallbackError(r0, r1, r2)
            android.util.Pair r1 = new android.util.Pair
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r10)
            r1.<init>(r2, r0)
            return r1
        L_0x001f:
            long r11 = java.lang.System.currentTimeMillis()
            java.lang.String r13 = com.alibaba.android.prefetchx.PFUtil.getUrlKey(r20)
            com.alibaba.android.prefetchx.config.RemoteConfigSpec$IImageModuleRemoteConfig r0 = r9.imageRemoteConfig
            int r0 = r0.getConfigMapMaxAgeInMemory()
            com.alibaba.android.prefetchx.core.image.ImageConfigDO r14 = r9.getImageConfigWithMemoryCache(r13, r0)
            if (r14 != 0) goto L_0x0047
            java.lang.String r0 = ""
            java.lang.String r1 = "-16605002"
            java.lang.String r2 = "prefetchx_image_no_config"
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.android.prefetchx.PFUtil.getJSCallbackError(r0, r1, r2)
            android.util.Pair r1 = new android.util.Pair
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r10)
            r1.<init>(r2, r0)
            return r1
        L_0x0047:
            boolean r0 = r9.hasWMLPrefetchDependency
            r15 = 4
            if (r0 == 0) goto L_0x007e
            com.taobao.weaver.prefetch.PrefetchStatusResponse r0 = new com.taobao.weaver.prefetch.PrefetchStatusResponse     // Catch:{ Throwable -> 0x0075 }
            r0.<init>()     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r1 = "image"
            r0.feature = r1     // Catch:{ Throwable -> 0x0075 }
            r0.status = r15     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r1 = "image配置获取"
            r0.message = r1     // Catch:{ Throwable -> 0x0075 }
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Throwable -> 0x0075 }
            r1.<init>()     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r2 = "imageConfigDO"
            r1.put(r2, r14)     // Catch:{ Throwable -> 0x0075 }
            r0.extra = r1     // Catch:{ Throwable -> 0x0075 }
            com.taobao.weaver.prefetch.WMLPrefetch r1 = com.taobao.weaver.prefetch.WMLPrefetch.getInstance()     // Catch:{ Throwable -> 0x0075 }
            com.taobao.weaver.prefetch.PrefetchStatusListener r1 = r1.getPrefetchStatusCallback()     // Catch:{ Throwable -> 0x0075 }
            r2 = r20
            r1.report(r2, r0)     // Catch:{ Throwable -> 0x0075 }
            goto L_0x007e
        L_0x0075:
            r9.hasWMLPrefetchDependency = r10
            java.lang.String r0 = "error in report. point image配置获取"
            java.lang.Throwable[] r1 = new java.lang.Throwable[r10]
            com.alibaba.android.prefetchx.PFLog.Image.w(r0, r1)
        L_0x007e:
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            r7 = 1
            com.alibaba.fastjson.JSONObject r2 = com.alibaba.fastjson.JSON.parseObject(r19)     // Catch:{ Throwable -> 0x00ac }
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ Throwable -> 0x00ac }
            r4.<init>()     // Catch:{ Throwable -> 0x00ac }
            int r5 = r14.getCount()     // Catch:{ Throwable -> 0x00ac }
            int r6 = r14.getDenominator()     // Catch:{ Throwable -> 0x00ac }
            java.util.List r0 = r14.getKeyList()     // Catch:{ Throwable -> 0x00ac }
            java.util.Map r16 = r14.getKeySize()     // Catch:{ Throwable -> 0x00ac }
            r1 = r18
            r3 = r8
            r15 = 1
            r7 = r0
            r17 = r8
            r8 = r16
            r1.findImageUrlInJson(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x00aa }
            goto L_0x00c2
        L_0x00aa:
            r0 = move-exception
            goto L_0x00b0
        L_0x00ac:
            r0 = move-exception
            r17 = r8
            r15 = 1
        L_0x00b0:
            java.lang.String r1 = "error in json parse & findImageUrlInJson"
            java.lang.Throwable[] r2 = new java.lang.Throwable[r15]
            r2[r10] = r0
            com.alibaba.android.prefetchx.PFLog.Image.w(r1, r2)
            java.lang.String r1 = "-166053003"
            java.lang.String r2 = "error in json parse & findImageUrlInJson"
            java.lang.Object[] r3 = new java.lang.Object[r10]
            com.alibaba.android.prefetchx.PFMonitor.Image.fail(r1, r2, r0, r3)
        L_0x00c2:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r1 = r17.size()
            if (r1 <= 0) goto L_0x0153
            java.util.Iterator r1 = r17.iterator()
        L_0x00d1:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0153
            java.lang.Object r2 = r1.next()
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r2 = (com.alibaba.android.prefetchx.core.image.PrefetchImageDO) r2
            java.lang.String r3 = r2.postfix
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x00fc
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r2.url
            r3.append(r4)
            java.lang.String r2 = r2.postfix
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            r0.add(r2)
            goto L_0x00d1
        L_0x00fc:
            boolean r3 = r2.orignalSize
            if (r3 == 0) goto L_0x0106
            java.lang.String r2 = r2.url
            r0.add(r2)
            goto L_0x00d1
        L_0x0106:
            boolean r3 = r2.fullWidth
            r4 = 360(0x168, float:5.04E-43)
            if (r3 == 0) goto L_0x011e
            com.taobao.tao.util.TaobaoImageUrlStrategy r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.getInstance()
            java.lang.String r2 = r2.url
            com.taobao.tao.image.ImageStrategyConfig r5 = r18.getImageStrategyConfig()
            java.lang.String r2 = r3.decideUrl((java.lang.String) r2, (int) r4, (com.taobao.tao.image.ImageStrategyConfig) r5)
            r0.add(r2)
            goto L_0x00d1
        L_0x011e:
            int r3 = r2.size
            if (r3 <= 0) goto L_0x013d
            com.taobao.tao.util.TaobaoImageUrlStrategy r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.getInstance()
            java.lang.String r5 = r2.url
            int r2 = r2.size
            int r2 = r2 * 360
            int r4 = r14.getViewport()
            int r2 = r2 / r4
            com.taobao.tao.image.ImageStrategyConfig r4 = r18.getImageStrategyConfig()
            java.lang.String r2 = r3.decideUrl((java.lang.String) r5, (int) r2, (com.taobao.tao.image.ImageStrategyConfig) r4)
            r0.add(r2)
            goto L_0x00d1
        L_0x013d:
            com.taobao.tao.util.TaobaoImageUrlStrategy r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.getInstance()
            java.lang.String r5 = r2.url
            int r2 = r2.denominator
            int r4 = r4 / r2
            com.taobao.tao.image.ImageStrategyConfig r2 = r18.getImageStrategyConfig()
            java.lang.String r2 = r3.decideUrl((java.lang.String) r5, (int) r4, (com.taobao.tao.image.ImageStrategyConfig) r2)
            r0.add(r2)
            goto L_0x00d1
        L_0x0153:
            java.util.List r1 = r14.getConstantImageUrl()
            if (r1 == 0) goto L_0x016a
            java.util.List r1 = r14.getConstantImageUrl()
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x016a
            java.util.List r1 = r14.getConstantImageUrl()
            r0.addAll(r1)
        L_0x016a:
            int r1 = r0.size()
            if (r1 <= 0) goto L_0x01a8
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            boolean r2 = com.alibaba.android.prefetchx.PFUtil.isDebug()
            if (r2 == 0) goto L_0x0180
            java.lang.String r2 = "prefetchImageUrls"
            r1.put(r2, r0)
        L_0x0180:
            java.lang.String r2 = "urlKey"
            r1.put(r2, r13)
            java.lang.String r2 = "denominator"
            int r3 = r14.getDenominator()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1.put(r2, r3)
            java.lang.String r2 = "start"
            java.lang.Long r3 = java.lang.Long.valueOf(r11)
            r1.put(r2, r3)
            r9.doPrefetchImage(r0, r1)
            android.util.Pair r0 = new android.util.Pair
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r15)
            r0.<init>(r2, r1)
            return r0
        L_0x01a8:
            r1 = 4
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.String r1 = "urlKey: "
            r0[r10] = r1
            r0[r15] = r13
            r1 = 2
            java.lang.String r2 = "  image finded but NO finalUrl assembed. "
            r0[r1] = r2
            r1 = 3
            r2 = r17
            r0[r1] = r2
            com.alibaba.android.prefetchx.PFLog.Image.d(r0)
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r1 = "errorMessage"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "image finded but NO finalUrl assembed. "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            r0.put(r1, r2)
            android.util.Pair r1 = new android.util.Pair
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r10)
            r1.<init>(r2, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.image.PFImage.prefetchImage(java.lang.String, java.lang.String):android.util.Pair");
    }

    /* access modifiers changed from: protected */
    public ImageConfigDO getImageConfigWithMemoryCache(String str, int i) {
        ImageConfigDO imageConfigDO;
        synchronized (PFImage.class) {
            long j = (long) (i * 1000);
            if (SystemClock.elapsedRealtime() - this.lastTimeRefreshConfigMap < j && this.imageConfigMap != null) {
                imageConfigDO = this.imageConfigMap.get(str);
            } else if (SystemClock.elapsedRealtime() - this.lastTimeRefreshConfigMap < j || this.imageConfigMap == null) {
                if (this.imageConfigMap == null || this.imageConfigMap.size() == 0) {
                    initImageConfigMapAsync(false);
                }
                imageConfigDO = null;
            } else {
                imageConfigDO = this.imageConfigMap.get(str);
                initImageConfigMapAsync(false);
            }
        }
        return (imageConfigDO != null || !this.imageRemoteConfig.isImageByDefault()) ? imageConfigDO : this.mDefaultImageConfigDO;
    }

    private void initImageConfigMapAsync(boolean z) {
        if (this.mConfigTryTimes > 10) {
            PFMonitor.Image.fail(PFConstant.Image.PF_IMAGE_CONFIG_EXCEPTION, "cannot get config over 10 times.", new Object[0]);
        } else if (!z) {
            PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                public void run() {
                    PFImage.this.initImageConfigMap();
                }
            });
        } else {
            PrefetchX.getInstance().getThreadExecutor().executeWithDelay(new Runnable() {
                public void run() {
                    PFImage.this.initImageConfigMap();
                }
            }, 3000);
        }
    }

    /* access modifiers changed from: private */
    public void initImageConfigMap() {
        this.mConfigTryTimes++;
        HashMap hashMap = new HashMap();
        Map<String, String> imageConfig = this.imageRemoteConfig.getImageConfig();
        if (imageConfig != null) {
            for (String next : imageConfig.keySet()) {
                try {
                    ImageConfigDO imageConfigDO = new ImageConfigDO();
                    imageConfigDO.setUrlKey(next);
                    JSONObject parseObject = JSON.parseObject(imageConfig.get(next));
                    Integer integer = parseObject.getInteger("count");
                    if (integer != null && integer.intValue() > 0) {
                        imageConfigDO.setCount(integer.intValue());
                    }
                    Integer integer2 = parseObject.getInteger("denominator");
                    if (integer2 != null && integer2.intValue() > 0) {
                        imageConfigDO.setDenominator(integer2.intValue());
                    }
                    imageConfigDO.setKeyList(convert(parseObject.getJSONArray("keyList")));
                    imageConfigDO.setConstantImageUrl(convert(parseObject.getJSONArray("constantImageUrl")));
                    JSONObject jSONObject = parseObject.getJSONObject("keySize");
                    if (jSONObject != null) {
                        HashMap hashMap2 = new HashMap();
                        for (String next2 : jSONObject.keySet()) {
                            hashMap2.put(next2, jSONObject.getInteger(next2));
                        }
                        imageConfigDO.setKeySize(hashMap2);
                    }
                    Integer integer3 = parseObject.getInteger("viewport");
                    if (integer3 != null && integer3.intValue() > 0) {
                        imageConfigDO.setViewport(integer3.intValue());
                    }
                    hashMap.put(next, imageConfigDO);
                } catch (Throwable th) {
                    PFMonitor.Image.fail(PFConstant.Image.PF_IMAGE_CONFIG_EXCEPTION, "error in resovle image config.", th, new Object[0]);
                }
            }
        } else {
            initImageConfigMapAsync(true);
        }
        synchronized (PFImage.class) {
            if (this.imageConfigMap != null) {
                this.imageConfigMap.clear();
            }
            this.imageConfigMap = hashMap;
            this.lastTimeRefreshConfigMap = SystemClock.elapsedRealtime();
        }
    }

    @NonNull
    private List<String> convert(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.size(); i++) {
                if (jSONArray.get(i) != null) {
                    arrayList.add(jSONArray.get(i).toString());
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00b1 A[Catch:{ Throwable -> 0x009c }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b2 A[Catch:{ Throwable -> 0x009c }] */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void findImageUrlInJson(com.alibaba.fastjson.JSONObject r20, java.util.List<com.alibaba.android.prefetchx.core.image.PrefetchImageDO> r21, java.util.Map<java.lang.String, java.lang.Object> r22, int r23, int r24, java.util.List<java.lang.String> r25, java.util.Map<java.lang.String, java.lang.Integer> r26) {
        /*
            r19 = this;
            r1 = r20
            r10 = r21
            r11 = r22
            r12 = r23
            r13 = r24
            r14 = r25
            r15 = r26
            int r0 = r21.size()
            if (r0 < r12) goto L_0x0015
            return
        L_0x0015:
            java.lang.String r0 = "width"
            boolean r0 = r1.containsKey(r0)     // Catch:{ Throwable -> 0x003e }
            if (r0 == 0) goto L_0x002b
            java.lang.String r0 = "width"
            java.lang.String r2 = "width"
            java.lang.Float r2 = r1.getFloat(r2)     // Catch:{ Throwable -> 0x003e }
            r11.put(r0, r2)     // Catch:{ Throwable -> 0x003e }
        L_0x002b:
            java.lang.String r0 = "moduleName"
            boolean r0 = r1.containsKey(r0)     // Catch:{ Throwable -> 0x003e }
            if (r0 == 0) goto L_0x003e
            java.lang.String r0 = "moduleName"
            java.lang.String r2 = "moduleName"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ Throwable -> 0x003e }
            r11.put(r0, r2)     // Catch:{ Throwable -> 0x003e }
        L_0x003e:
            java.util.Set r0 = r20.keySet()
            java.util.Iterator r16 = r0.iterator()
        L_0x0046:
            boolean r0 = r16.hasNext()
            if (r0 == 0) goto L_0x027b
            int r0 = r21.size()
            if (r0 < r12) goto L_0x0054
            goto L_0x027b
        L_0x0054:
            java.lang.Object r0 = r16.next()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.Object r2 = r1.get(r0)
            boolean r3 = r2 instanceof java.lang.String
            if (r3 == 0) goto L_0x01da
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x0046
            java.lang.String r3 = ".png"
            boolean r3 = r2.contains(r3)
            if (r3 != 0) goto L_0x007a
            java.lang.String r3 = ".jpg"
            boolean r3 = r2.contains(r3)
            if (r3 == 0) goto L_0x0046
        L_0x007a:
            java.lang.String r3 = "gw.alicdn.com"
            boolean r3 = r2.contains(r3)
            if (r3 != 0) goto L_0x008a
            java.lang.String r3 = "img.alicdn.com"
            boolean r3 = r2.contains(r3)
            if (r3 == 0) goto L_0x0046
        L_0x008a:
            r3 = 0
            r4 = 1
            if (r14 == 0) goto L_0x009f
            int r5 = r25.size()     // Catch:{ Throwable -> 0x009c }
            if (r5 <= 0) goto L_0x009f
            boolean r5 = r14.contains(r0)     // Catch:{ Throwable -> 0x009c }
            if (r5 != 0) goto L_0x009f
            r5 = 1
            goto L_0x00a0
        L_0x009c:
            r0 = move-exception
            goto L_0x01c6
        L_0x009f:
            r5 = 0
        L_0x00a0:
            if (r15 == 0) goto L_0x00af
            int r6 = r26.size()     // Catch:{ Throwable -> 0x009c }
            if (r6 <= 0) goto L_0x00af
            boolean r6 = r15.containsKey(r0)     // Catch:{ Throwable -> 0x009c }
            if (r6 != 0) goto L_0x00af
            r5 = 1
        L_0x00af:
            if (r5 == 0) goto L_0x00b2
            goto L_0x0046
        L_0x00b2:
            java.lang.String r5 = "getAvatar"
            boolean r5 = r2.contains(r5)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x00c8
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r0 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r0.url = r2     // Catch:{ Throwable -> 0x009c }
            r0.orignalSize = r4     // Catch:{ Throwable -> 0x009c }
            r10.add(r0)     // Catch:{ Throwable -> 0x009c }
            goto L_0x0046
        L_0x00c8:
            java.lang.String r5 = "bgImageUrl"
            boolean r5 = r5.equals(r0)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x00e0
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r0 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r0.url = r2     // Catch:{ Throwable -> 0x009c }
            java.lang.String r2 = "_640x640q90.jpg"
            r0.postfix = r2     // Catch:{ Throwable -> 0x009c }
            r10.add(r0)     // Catch:{ Throwable -> 0x009c }
            goto L_0x0046
        L_0x00e0:
            java.lang.String r5 = "shopLogo"
            boolean r5 = r5.equals(r0)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x00f8
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r0 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r0.url = r2     // Catch:{ Throwable -> 0x009c }
            java.lang.String r2 = "_200x200q90.jpg"
            r0.postfix = r2     // Catch:{ Throwable -> 0x009c }
            r10.add(r0)     // Catch:{ Throwable -> 0x009c }
            goto L_0x0046
        L_0x00f8:
            int r5 = r21.size()     // Catch:{ Throwable -> 0x009c }
            if (r5 < r12) goto L_0x0100
            goto L_0x027b
        L_0x0100:
            java.lang.String r5 = "height"
            boolean r5 = r1.containsKey(r5)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x017c
            java.lang.String r5 = "height"
            java.lang.String r5 = r1.getString(r5)     // Catch:{ Throwable -> 0x009c }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x009c }
            if (r5 != 0) goto L_0x017c
            java.lang.String r5 = "height"
            java.lang.Float r5 = r1.getFloat(r5)     // Catch:{ Throwable -> 0x009c }
            float r5 = r5.floatValue()     // Catch:{ Throwable -> 0x009c }
            r6 = 1128792064(0x43480000, float:200.0)
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 < 0) goto L_0x017c
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r0 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r0.url = r2     // Catch:{ Throwable -> 0x009c }
            java.lang.String r2 = "width"
            boolean r2 = r1.containsKey(r2)     // Catch:{ Throwable -> 0x009c }
            if (r2 == 0) goto L_0x013c
            java.lang.String r2 = "width"
            java.lang.Float r2 = r1.getFloat(r2)     // Catch:{ Throwable -> 0x009c }
            goto L_0x013d
        L_0x013c:
            r2 = 0
        L_0x013d:
            if (r2 != 0) goto L_0x015b
            java.lang.String r5 = "width"
            java.lang.Object r5 = r11.get(r5)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x015b
            java.lang.String r2 = "width"
            java.lang.Object r2 = r11.get(r2)     // Catch:{ Throwable -> 0x009c }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x009c }
            float r2 = java.lang.Float.parseFloat(r2)     // Catch:{ Throwable -> 0x009c }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Throwable -> 0x009c }
        L_0x015b:
            if (r2 == 0) goto L_0x0175
            float r5 = r2.floatValue()     // Catch:{ Throwable -> 0x009c }
            r6 = 1142947840(0x44200000, float:640.0)
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 == 0) goto L_0x0172
            float r2 = r2.floatValue()     // Catch:{ Throwable -> 0x009c }
            r5 = 1144750080(0x443b8000, float:750.0)
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 != 0) goto L_0x0175
        L_0x0172:
            r0.fullWidth = r4     // Catch:{ Throwable -> 0x009c }
            goto L_0x0177
        L_0x0175:
            r0.denominator = r13     // Catch:{ Throwable -> 0x009c }
        L_0x0177:
            r10.add(r0)     // Catch:{ Throwable -> 0x009c }
            goto L_0x0046
        L_0x017c:
            int r5 = r21.size()     // Catch:{ Throwable -> 0x009c }
            if (r5 < r12) goto L_0x0184
            goto L_0x027b
        L_0x0184:
            if (r15 == 0) goto L_0x01aa
            int r5 = r26.size()     // Catch:{ Throwable -> 0x009c }
            if (r5 <= 0) goto L_0x01aa
            boolean r5 = r15.containsKey(r0)     // Catch:{ Throwable -> 0x009c }
            if (r5 == 0) goto L_0x01aa
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r5 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r5.<init>()     // Catch:{ Throwable -> 0x009c }
            java.lang.Object r0 = r15.get(r0)     // Catch:{ Throwable -> 0x009c }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x009c }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x009c }
            r5.size = r0     // Catch:{ Throwable -> 0x009c }
            r5.url = r2     // Catch:{ Throwable -> 0x009c }
            r10.add(r5)     // Catch:{ Throwable -> 0x009c }
            goto L_0x0046
        L_0x01aa:
            int r0 = r21.size()     // Catch:{ Throwable -> 0x009c }
            if (r0 < r12) goto L_0x01b2
            goto L_0x027b
        L_0x01b2:
            com.alibaba.android.prefetchx.core.image.PrefetchImageDO r0 = new com.alibaba.android.prefetchx.core.image.PrefetchImageDO     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r0.denominator = r13     // Catch:{ Throwable -> 0x009c }
            r0.url = r2     // Catch:{ Throwable -> 0x009c }
            r10.add(r0)     // Catch:{ Throwable -> 0x009c }
            int r0 = r21.size()     // Catch:{ Throwable -> 0x009c }
            if (r0 < r12) goto L_0x0046
            goto L_0x027b
        L_0x01c6:
            java.lang.String r2 = "error in finding image url, will continue next."
            java.lang.Throwable[] r4 = new java.lang.Throwable[r4]
            r4[r3] = r0
            com.alibaba.android.prefetchx.PFLog.Image.w(r2, r4)
            java.lang.String r2 = "-166053002"
            java.lang.String r4 = "error in finding image url, will continue next."
            java.lang.Object[] r3 = new java.lang.Object[r3]
            com.alibaba.android.prefetchx.PFMonitor.Image.fail(r2, r4, r0, r3)
            goto L_0x0046
        L_0x01da:
            boolean r0 = r2 instanceof com.alibaba.fastjson.JSONObject
            if (r0 == 0) goto L_0x01f4
            r3 = r2
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3
            r2 = r19
            r4 = r21
            r5 = r22
            r6 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r2.findImageUrlInJson(r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0046
        L_0x01f4:
            boolean r0 = r2 instanceof com.alibaba.fastjson.JSONArray
            if (r0 == 0) goto L_0x0046
            com.alibaba.fastjson.JSONArray r2 = (com.alibaba.fastjson.JSONArray) r2
            java.util.Iterator r0 = r2.iterator()
        L_0x01fe:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0046
            java.lang.Object r2 = r0.next()
            boolean r3 = r2 instanceof com.alibaba.fastjson.JSONObject
            if (r3 == 0) goto L_0x0221
            r3 = r2
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3
            r2 = r19
            r4 = r21
            r5 = r22
            r6 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r2.findImageUrlInJson(r3, r4, r5, r6, r7, r8, r9)
            goto L_0x01fe
        L_0x0221:
            boolean r3 = r2 instanceof com.alibaba.fastjson.JSONArray
            if (r3 == 0) goto L_0x01fe
            com.alibaba.fastjson.JSONArray r2 = (com.alibaba.fastjson.JSONArray) r2
            java.util.Iterator r17 = r2.iterator()
        L_0x022b:
            boolean r2 = r17.hasNext()
            if (r2 == 0) goto L_0x01fe
            java.lang.Object r2 = r17.next()
            boolean r3 = r2 instanceof com.alibaba.fastjson.JSONObject
            if (r3 == 0) goto L_0x024e
            r3 = r2
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3
            r2 = r19
            r4 = r21
            r5 = r22
            r6 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r2.findImageUrlInJson(r3, r4, r5, r6, r7, r8, r9)
            goto L_0x022b
        L_0x024e:
            boolean r3 = r2 instanceof com.alibaba.fastjson.JSONArray
            if (r3 == 0) goto L_0x022b
            com.alibaba.fastjson.JSONArray r2 = (com.alibaba.fastjson.JSONArray) r2
            java.util.Iterator r18 = r2.iterator()
        L_0x0258:
            boolean r2 = r18.hasNext()
            if (r2 == 0) goto L_0x022b
            java.lang.Object r2 = r18.next()
            boolean r3 = r2 instanceof com.alibaba.fastjson.JSONObject
            if (r3 == 0) goto L_0x0258
            r3 = r2
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3
            r2 = r19
            r4 = r21
            r5 = r22
            r6 = r23
            r7 = r24
            r8 = r25
            r9 = r26
            r2.findImageUrlInJson(r3, r4, r5, r6, r7, r8, r9)
            goto L_0x0258
        L_0x027b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.prefetchx.core.image.PFImage.findImageUrlInJson(com.alibaba.fastjson.JSONObject, java.util.List, java.util.Map, int, int, java.util.List, java.util.Map):void");
    }

    public Pair<Boolean, Map<String, Object>> prefetchImage(List<String> list) {
        if (!this.imageRemoteConfig.isImageEnable()) {
            return new Pair<>(false, PFUtil.getJSCallbackError("", "-16605001", "prefetchx_image_disable"));
        }
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        if (PFUtil.isDebug()) {
            hashMap.put("prefetchImageUrls", list);
        }
        hashMap.put("urlKey", "Unkown");
        hashMap.put("denominator", "No-Need-size-already-in-Url");
        hashMap.put("start", Long.valueOf(currentTimeMillis));
        doPrefetchImage(list, hashMap);
        return new Pair<>(true, hashMap);
    }

    public Pair<Boolean, Map<String, Object>> prefetchImageWithSize(List<Map<String, String>> list, WXSDKInstance wXSDKInstance) {
        return prefetchImageWithSize(list, wXSDKInstance.getInstanceViewPortWidth(), wXSDKInstance.getBundleUrl());
    }

    public Pair<Boolean, Map<String, Object>> prefetchImageWithSize(List<Map<String, String>> list, int i, String str) {
        if (!this.imageRemoteConfig.isImageEnable()) {
            return new Pair<>(false, PFUtil.getJSCallbackError("", "-16605001", "prefetchx_image_disable"));
        }
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (Map next : list) {
            if (next.containsKey("url")) {
                arrayList.add(TaobaoImageUrlStrategy.getInstance().decideUrl((String) next.get("url"), Integer.valueOf((Integer.parseInt(next.get("size") != null ? (String) next.get("size") : "360") * 360) / i).intValue(), getImageStrategyConfig()));
            }
        }
        HashMap hashMap = new HashMap();
        if (PFUtil.isDebug()) {
            hashMap.put("prefetchImageUrls", arrayList);
        }
        hashMap.put("urlKey", PFUtil.getUrlKey(str));
        hashMap.put("denominator", "by-each-size");
        hashMap.put("start", Long.valueOf(currentTimeMillis));
        doPrefetchImage(arrayList, hashMap);
        return new Pair<>(true, hashMap);
    }

    /* access modifiers changed from: protected */
    public void doPrefetchImage(List<String> list, Map<String, Object> map) {
        String str;
        String str2;
        final List<String> list2 = list;
        final Map<String, Object> map2 = map;
        if (this.imageRemoteConfig.isLoadToMemory()) {
            ArrayList arrayList = new ArrayList();
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            for (final String next : list) {
                AnonymousClass5 r10 = r0;
                final ArrayList arrayList2 = arrayList;
                PhenixCreator load = Phenix.instance().load("common", next);
                final CountDownLatch countDownLatch2 = countDownLatch;
                CountDownLatch countDownLatch3 = countDownLatch;
                final Map<String, Object> map3 = map;
                final List<String> list3 = list;
                AnonymousClass5 r0 = new IPhenixListener<SuccPhenixEvent>() {
                    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                        String str;
                        String str2;
                        arrayList2.add(next);
                        countDownLatch2.countDown();
                        if (countDownLatch2.getCount() <= 0) {
                            Object[] objArr = new Object[8];
                            objArr[0] = "urlKey: ";
                            objArr[1] = map3.get("urlKey");
                            objArr[2] = " loaded ";
                            objArr[3] = Integer.valueOf(list3.size());
                            objArr[4] = " images to memory, with denominator ";
                            objArr[5] = map3.get("denominator");
                            if (map3.get("start") instanceof Long) {
                                str = " cost " + (System.currentTimeMillis() - ((Long) map3.get("start")).longValue()) + "ms";
                            } else {
                                str = "";
                            }
                            objArr[6] = str;
                            if (PFUtil.isDebug()) {
                                str2 = Operators.SPACE_STR + arrayList2;
                            } else {
                                str2 = "";
                            }
                            objArr[7] = str2;
                            PFLog.Image.d(objArr);
                            if (PFImage.this.hasWMLPrefetchDependency) {
                                try {
                                    PrefetchStatusResponse prefetchStatusResponse = new PrefetchStatusResponse();
                                    prefetchStatusResponse.feature = "image";
                                    prefetchStatusResponse.status = 1;
                                    prefetchStatusResponse.message = "image完成";
                                    map3.put("finalUrls", list3);
                                    prefetchStatusResponse.extra = map3;
                                    WMLPrefetch.getInstance().getPrefetchStatusCallback().report(String.valueOf(map3.get("urlKey")), prefetchStatusResponse);
                                } catch (Throwable unused) {
                                    boolean unused2 = PFImage.this.hasWMLPrefetchDependency = false;
                                    PFLog.Image.w("error in report. point image完成", new Throwable[0]);
                                }
                            }
                        }
                        return false;
                    }
                };
                final CountDownLatch countDownLatch4 = countDownLatch3;
                final Map<String, Object> map4 = map;
                final List<String> list4 = list;
                final ArrayList arrayList3 = arrayList;
                load.succListener(r10).failListener(new IPhenixListener<FailPhenixEvent>() {
                    public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                        String str;
                        String str2;
                        countDownLatch4.countDown();
                        if (countDownLatch4.getCount() <= 0) {
                            Object[] objArr = new Object[8];
                            objArr[0] = "urlKey: ";
                            objArr[1] = map4.get("urlKey");
                            objArr[2] = " loaded ";
                            objArr[3] = Integer.valueOf(list4.size());
                            objArr[4] = " images to memory, with denominator ";
                            objArr[5] = map4.get("denominator");
                            if (map4.get("start") instanceof Long) {
                                str = " cost " + (System.currentTimeMillis() - ((Long) map4.get("start")).longValue()) + "ms";
                            } else {
                                str = "";
                            }
                            objArr[6] = str;
                            if (PFUtil.isDebug()) {
                                str2 = Operators.SPACE_STR + arrayList3;
                            } else {
                                str2 = "";
                            }
                            objArr[7] = str2;
                            PFLog.Image.d(objArr);
                        }
                        return false;
                    }
                }).cancelListener(new IPhenixListener<PhenixEvent>() {
                    public boolean onHappen(PhenixEvent phenixEvent) {
                        String str;
                        String str2;
                        countDownLatch4.countDown();
                        if (countDownLatch4.getCount() <= 0) {
                            Object[] objArr = new Object[8];
                            objArr[0] = "urlKey: ";
                            objArr[1] = map4.get("urlKey");
                            objArr[2] = " loaded ";
                            objArr[3] = Integer.valueOf(list4.size());
                            objArr[4] = " images to memory, with denominator ";
                            objArr[5] = map4.get("denominator");
                            if (map4.get("start") instanceof Long) {
                                str = " cost " + (System.currentTimeMillis() - ((Long) map4.get("start")).longValue()) + "ms";
                            } else {
                                str = "";
                            }
                            objArr[6] = str;
                            if (PFUtil.isDebug()) {
                                str2 = Operators.SPACE_STR + arrayList3;
                            } else {
                                str2 = "";
                            }
                            objArr[7] = str2;
                            PFLog.Image.d(objArr);
                        }
                        return false;
                    }
                }).fetch();
                countDownLatch = countDownLatch3;
            }
            Object[] objArr = new Object[7];
            objArr[0] = "urlKey: ";
            objArr[1] = map2.get("urlKey");
            objArr[2] = " fired ";
            objArr[3] = Integer.valueOf(list.size());
            objArr[4] = " images to memory, with denominator ";
            objArr[5] = map2.get("denominator");
            if (map2.get("start") instanceof Long) {
                str2 = " cost " + (System.currentTimeMillis() - ((Long) map2.get("start")).longValue()) + "ms";
            } else {
                str2 = "";
            }
            objArr[6] = str2;
            PFLog.Image.d(objArr);
            return;
        }
        Phenix.instance().preload("common", list2).completeListener(new IPhenixListener<PrefetchEvent>() {
            public boolean onHappen(PrefetchEvent prefetchEvent) {
                String str;
                String str2;
                Object[] objArr = new Object[8];
                objArr[0] = "urlKey: ";
                objArr[1] = map2.get("urlKey");
                objArr[2] = " loaded ";
                objArr[3] = Integer.valueOf(list2.size());
                objArr[4] = " images, with denominator ";
                objArr[5] = map2.get("denominator");
                if (map2.get("start") instanceof Long) {
                    str = " cost " + (System.currentTimeMillis() - ((Long) map2.get("start")).longValue()) + "ms";
                } else {
                    str = "";
                }
                objArr[6] = str;
                if (PFUtil.isDebug()) {
                    str2 = Operators.SPACE_STR + list2;
                } else {
                    str2 = "";
                }
                objArr[7] = str2;
                PFLog.Image.d(objArr);
                return false;
            }
        }).fetch();
        Object[] objArr2 = new Object[7];
        objArr2[0] = "urlKey: ";
        objArr2[1] = map2.get("urlKey");
        objArr2[2] = " fired ";
        objArr2[3] = Integer.valueOf(list.size());
        objArr2[4] = " images, with denominator ";
        objArr2[5] = map2.get("denominator");
        if (map2.get("start") instanceof Long) {
            str = " cost " + (System.currentTimeMillis() - ((Long) map2.get("start")).longValue()) + "ms";
        } else {
            str = "";
        }
        objArr2[6] = str;
        PFLog.Image.d(objArr2);
    }

    private ImageStrategyConfig getImageStrategyConfig() {
        if (this.mImageStrategyConfig == null) {
            synchronized (this) {
                if (this.mImageStrategyConfig == null) {
                    ImageStrategyConfig.Builder newBuilderWithName = ImageStrategyConfig.newBuilderWithName("prefetchxImage", 0);
                    newBuilderWithName.setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality.q90);
                    newBuilderWithName.enableSharpen(false);
                    this.mImageStrategyConfig = newBuilderWithName.build();
                }
            }
        }
        return this.mImageStrategyConfig;
    }
}
