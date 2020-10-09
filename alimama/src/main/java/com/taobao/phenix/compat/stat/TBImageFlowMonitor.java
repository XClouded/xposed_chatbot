package com.taobao.phenix.compat.stat;

import android.os.Build;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.taobao.analysis.abtest.ABTestCenter;
import com.taobao.analysis.fulltrace.FullTraceAnalysis;
import com.taobao.analysis.fulltrace.RequestInfo;
import com.taobao.analysis.scene.SceneIdentifier;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.phenix.cache.disk.OnlyCacheFailedException;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.StatMonitor4Phenix;
import com.taobao.phenix.decode.DecodeException;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.network.HttpCodeResponseException;
import com.taobao.phenix.loader.network.IncompleteResponseException;
import com.taobao.phenix.loader.network.NetworkResponseException;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.request.ImageStatistics;
import com.taobao.rxm.schedule.PairingThrottlingScheduler;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class TBImageFlowMonitor extends TBImageBaseMonitor implements ImageFlowMonitor, Pexode.ForcedDegradationListener, PairingThrottlingScheduler.DegradationListener {
    static final String BITMAP_POOL_ALARM_POINT = "BitmapPoolHit";
    static final String BUCKET_INFO = "bucketInfo";
    static final String BUSINESS_DIMEN = "bizName";
    static final String CDN_IP_PORT_DIMEN = "cdnIpPort";
    static final String CONNECT_TYPE_DIMEN = "connectType";
    static final String DATA_FROM_DIMEN = "dataFrom";
    static final String DEVICE_LEVEL_DIMEN = "deviceLevel";
    static final String DISK_CACHE_ALARM_POINT = "DiskCacheHit";
    static final String DOMAIN_DIMEN = "domain";
    static final String EAGLEID = "eagleid";
    static final String ERROR_ANALYSIS_CODE = "analysisErrorCode";
    static final String ERROR_DESC = "desc";
    static final String ERROR_DESC_PREFIX = "analysisReason::";
    static final String ERROR_DIMEN = "error";
    static final String ERROR_ORIGIN_CODE = "originErrorCode";
    static final String ERROR_ORIGIN_URL = "originUrl";
    static final String ERROR_POINT = "ImageError";
    static final String ERROR_URL = "url";
    static final String FIRST_DATA_MEASURE = "firstData";
    static final String FORCED_NO_ASHMEM_COUNTER_POINT = "Forced2NoAshmem";
    static final String FORCED_NO_IN_BITMAP_COUNTER_POINT = "Forced2NoInBitmap";
    static final String FORCED_SYSTEM_COUNTER_POINT = "Forced2System";
    static final String FORCED_UNLIMITED_NETWORK_COUNTER_POINT = "Forced2UnlimitedNetwork";
    static final String FORMAT_DIMEN = "format";
    static final String HIT_CDN_CACHE_DIMEN = "hitCdnCache";
    static final String LAUNCH_EXTERNAL_DIMEN = "appLaunchExternal";
    static final String LAUNCH_TYPE_DIMEN = "launchType";
    static final String MEM_CACHE_ALARM_POINT = "MemCacheHit";
    static final String MODULE_NAME = "ImageLib_Rx";
    static final String MONITOR_POINT = "ImageFlow";
    static final String NAVI_URL_DIMEN = "naviUrl";
    static final String NETWORK_SERVER_RT = "serverRT";
    static final String NEW_THREAD_MODEL = "newThreadModel";
    static final String ORIGINAL_URL = "originalUrl";
    static final String PAGE_URL_DIMEN = "pageURL";
    static final String REQUEST_START_TIME = "requestStartTime";
    static final String REQUEST_URL = "RequestUrl";
    static final String RESPONSE_CODE_MEASURE = "responseCode";
    static final String SCHEDULE_FACTOR_DIMEN = "scheduleFactor";
    private static final int SCHEDULE_PATIENT_TIME = 5000;
    static final String SDK_INIT_TIME = "sdkInitTime";
    static final String SEND_BEFORE_MEASURE = "sendBefore";
    static final String SINCE_LAST_LAUNCH_DIMEN = "sinceLastLaunchInternal";
    static final String SINCE_LAUNCH_DIMEN = "sinceAppLaunchInterval";
    static final String SIZE_MEASURE = "size";
    static final String SIZ_RANGE_DIMEN = "sizeRange";
    static final String SPEED_MEASURE = "speed";
    private static final String TAG = "FlowMonitor";
    static final String TTL_ERROR_EAGLE_ID = "eagleId";
    static final String TTL_ERROR_MAX_AGE = "maxAge";
    static final String TTL_ERROR_POINT = "ImageTTLException";
    static final String TTL_ERROR_URL = "url";
    static final String TTL_EXPERIMENT_ID = "ttlExperimentId";
    static final String TTL_GET_TIME = "ttlGetTime";
    static final String TTL_PUT_TIME = "ttlPutTime";
    static final String WINDOW_NAME_DIMEN = "windowName";
    static final String YIXIU_BUCKET = "yixiuBucket";
    private int mDegradationBits;
    private TBImageRetrieveABListener mListener;
    private NavigationInfoObtainer mNavInfoObtainer;
    private final NetworkAnalyzer mNetworkAnalyzer;
    private NonCriticalErrorReporter mNonCriticalErrorReporter;
    private int mNonCriticalReportCoverage;
    private boolean mRegisteredException;
    private boolean mRegisteredTTLException;
    protected int mStatCoverage;

    public int getMinimumScheduleTime2StatWaitSize() {
        return 150;
    }

    public TBImageFlowMonitor(int i, NetworkAnalyzer networkAnalyzer) {
        this.mNetworkAnalyzer = networkAnalyzer;
        this.mStatCoverage = i;
        this.mNonCriticalReportCoverage = 100;
    }

    public TBImageFlowMonitor(int i, int i2, NetworkAnalyzer networkAnalyzer) {
        this.mNetworkAnalyzer = networkAnalyzer;
        this.mStatCoverage = i;
        this.mNonCriticalReportCoverage = i2;
    }

    public TBImageFlowMonitor(int i, int i2, NetworkAnalyzer networkAnalyzer, TBImageRetrieveABListener tBImageRetrieveABListener) {
        this.mNetworkAnalyzer = networkAnalyzer;
        this.mStatCoverage = i;
        this.mNonCriticalReportCoverage = i2;
        this.mListener = tBImageRetrieveABListener;
    }

    public void setNavigationInfoObtainer(NavigationInfoObtainer navigationInfoObtainer) {
        this.mNavInfoObtainer = navigationInfoObtainer;
        UnitedLog.i(TAG, "set navigation info obtainer=%s", navigationInfoObtainer);
    }

    public void setNonCriticalErrorReporter(NonCriticalErrorReporter nonCriticalErrorReporter) {
        this.mNonCriticalErrorReporter = nonCriticalErrorReporter;
    }

    public void setImageWarningSize(int i) {
        int unused = ImageSizeWarningException.sImageWarningSize = i;
        UnitedLog.i(TAG, "set image warning size=%d", Integer.valueOf(i));
    }

    private void reportTTLException(ImageStatistics imageStatistics) {
        if (imageStatistics.mReportTTLException) {
            if (!this.mRegisteredTTLException) {
                registerTTLException();
            }
            if (imageStatistics.getExtras() != null && !TextUtils.isEmpty(imageStatistics.getExtras().get("max-age"))) {
                String checkUrlLength = checkUrlLength(imageStatistics.getUriInfo().getPath());
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                create.setValue(TTL_ERROR_MAX_AGE, imageStatistics.getExtras().get("max-age"));
                create.setValue(TTL_ERROR_EAGLE_ID, imageStatistics.getExtras().get(EAGLEID));
                create.setValue("url", checkUrlLength);
                AppMonitor.Stat.commit(MODULE_NAME, TTL_ERROR_POINT, create, create2);
            }
        }
    }

    private synchronized void registerTTLException() {
        if (!this.mRegisteredTTLException) {
            UnitedLog.d(TAG, "TTL exception register start", new Object[0]);
            DimensionSet create = DimensionSet.create();
            create.addDimension("url");
            create.addDimension(TTL_ERROR_EAGLE_ID);
            create.addDimension(TTL_ERROR_MAX_AGE);
            AppMonitor.register(MODULE_NAME, TTL_ERROR_POINT, (MeasureSet) null, create);
            this.mRegisteredTTLException = true;
            UnitedLog.d(TAG, "TTL exception register end", new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void registerAppMonitor() {
        if (!this.mRegistered) {
            UnitedLog.d(TAG, "image flow register start", new Object[0]);
            DimensionSet create = DimensionSet.create();
            create.addDimension("domain");
            create.addDimension("error");
            create.addDimension("bizName");
            create.addDimension(FORMAT_DIMEN);
            create.addDimension(DATA_FROM_DIMEN);
            create.addDimension(SCHEDULE_FACTOR_DIMEN);
            create.addDimension(HIT_CDN_CACHE_DIMEN);
            create.addDimension(CONNECT_TYPE_DIMEN);
            create.addDimension(CDN_IP_PORT_DIMEN);
            create.addDimension(WINDOW_NAME_DIMEN);
            create.addDimension(NAVI_URL_DIMEN);
            create.addDimension(PAGE_URL_DIMEN);
            create.addDimension(LAUNCH_TYPE_DIMEN);
            create.addDimension(LAUNCH_EXTERNAL_DIMEN);
            create.addDimension(SINCE_LAST_LAUNCH_DIMEN);
            create.addDimension(SINCE_LAUNCH_DIMEN);
            create.addDimension(DEVICE_LEVEL_DIMEN);
            create.addDimension(BUCKET_INFO);
            create.addDimension(YIXIU_BUCKET);
            create.addDimension(NEW_THREAD_MODEL);
            create.addDimension(SDK_INIT_TIME);
            create.addDimension(REQUEST_START_TIME);
            create.addDimension(REQUEST_URL);
            create.addDimension(ORIGINAL_URL);
            create.addDimension(TTL_EXPERIMENT_ID);
            create.addDimension(TTL_GET_TIME);
            create.addDimension(TTL_PUT_TIME);
            MeasureSet create2 = MeasureSet.create();
            newMeasure2Set(create2, ImageStatistics.KEY_READ_MEMORY_CACHE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_READ_DISK_CACHE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_READ_LOCAL_FILE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, "connect", Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, "download", Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_BITMAP_DECODE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_BITMAP_SCALE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_SCHEDULE_TIME, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_WAIT_FOR_MAIN, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, ImageStatistics.KEY_TOTAL_TIME, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(60000.0d));
            newMeasure2Set(create2, "size", Double.valueOf(0.0d), (Double) null, (Double) null);
            newMeasure2Set(create2, SPEED_MEASURE, Double.valueOf(0.0d), (Double) null, (Double) null);
            newMeasure2Set(create2, ImageStatistics.KEY_MASTER_WAIT_SIZE, Double.valueOf(0.0d), (Double) null, (Double) null);
            newMeasure2Set(create2, ImageStatistics.KEY_NETWORK_WAIT_SIZE, Double.valueOf(0.0d), (Double) null, (Double) null);
            newMeasure2Set(create2, ImageStatistics.KEY_DECODE_WAIT_SIZE, Double.valueOf(0.0d), (Double) null, (Double) null);
            newMeasure2Set(create2, FIRST_DATA_MEASURE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, SEND_BEFORE_MEASURE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, RESPONSE_CODE_MEASURE, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            newMeasure2Set(create2, NETWORK_SERVER_RT, Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(30000.0d));
            AppMonitor.register(MODULE_NAME, MONITOR_POINT, create2, create);
            this.mRegistered = true;
            UnitedLog.d(TAG, "image flow register end", new Object[0]);
        }
    }

    public synchronized void registerErrorMonitor() {
        if (!this.mRegisteredException) {
            UnitedLog.d(TAG, "image error register start", new Object[0]);
            DimensionSet create = DimensionSet.create();
            create.addDimension("url");
            create.addDimension(WINDOW_NAME_DIMEN);
            create.addDimension(NAVI_URL_DIMEN);
            create.addDimension("bizName");
            create.addDimension(ERROR_ANALYSIS_CODE);
            create.addDimension(ERROR_ORIGIN_CODE);
            create.addDimension("desc");
            create.addDimension(FORMAT_DIMEN);
            create.addDimension(DATA_FROM_DIMEN);
            create.addDimension(ERROR_ORIGIN_URL);
            create.addDimension(PAGE_URL_DIMEN);
            AppMonitor.register(MODULE_NAME, ERROR_POINT, (MeasureSet) null, create);
            this.mRegisteredException = true;
            UnitedLog.d(TAG, "image error register end", new Object[0]);
        }
    }

    private static String getHostFromPath(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.startsWith("//")) {
            i = 2;
        } else {
            int indexOf = str.indexOf(HttpConstant.SCHEME_SPLIT);
            i = indexOf < 0 ? 0 : indexOf + 3;
        }
        if (i >= str.length()) {
            return "";
        }
        int indexOf2 = str.indexOf(47, i);
        if (indexOf2 < 0) {
            indexOf2 = str.length();
        }
        String substring = str.substring(i, indexOf2);
        return (substring.contains(Operators.BLOCK_START_STR) || substring.contains(",") || !substring.contains(".")) ? "" : substring;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r2 = r2.get(com.taobao.phenix.common.Constant.BUNDLE_BIZ_CODE);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getBizIdFromExtras(java.util.Map<java.lang.String, java.lang.String> r2) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x000e
            java.lang.String r0 = "bundle_biz_code"
            java.lang.Object r2 = r2.get(r0)
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x000d
            goto L_0x000e
        L_0x000d:
            return r2
        L_0x000e:
            java.lang.String r2 = ""
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.stat.TBImageFlowMonitor.getBizIdFromExtras(java.util.Map):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r2 = r2.get(com.taobao.phenix.common.Constant.ORIGIN_URL);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getOriUrlFromExtras(java.util.Map<java.lang.String, java.lang.String> r2) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x000e
            java.lang.String r0 = "origin_url"
            java.lang.Object r2 = r2.get(r0)
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x000d
            goto L_0x000e
        L_0x000d:
            return r2
        L_0x000e:
            java.lang.String r2 = ""
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.stat.TBImageFlowMonitor.getOriUrlFromExtras(java.util.Map):java.lang.String");
    }

    private int updateMeasureValue(MeasureValueSet measureValueSet, String str, Map<String, Integer> map) {
        Integer num = map.get(str);
        if (num == null) {
            return 0;
        }
        measureValueSet.setValue(str, (double) num.intValue());
        return num.intValue();
    }

    private String getPageUrlFromExtras(Map<String, String> map) {
        String str;
        if (map == null || (str = map.get(PAGE_URL_DIMEN)) == null || TextUtils.isEmpty(str)) {
            return "";
        }
        int indexOf = str.indexOf(63, 0);
        if (indexOf < 0) {
            indexOf = str.length();
        }
        return str.substring(0, indexOf);
    }

    public void onSuccess(ImageStatistics imageStatistics) {
        String str;
        String str2;
        boolean z;
        ImageSizeWarningException imageSizeWarningException;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        int i;
        ImageStatistics imageStatistics2 = imageStatistics;
        if (!commitFullTrace(imageStatistics2, 1, "")) {
            reportTTLException(imageStatistics);
            if (imageStatistics2 == null || imageStatistics.isDuplicated() || imageStatistics.getFromType() == ImageStatistics.FromType.FROM_UNKNOWN || imageStatistics.getDetailCost() == null) {
                UnitedLog.d(TAG, "skipped commit onSuccess, statistic=%s", imageStatistics2);
            } else if (!filterOutThisStat(this.mStatCoverage)) {
                String path = imageStatistics.getUriInfo().getPath();
                registerAppMonitor();
                String checkUrlLength = checkUrlLength(path);
                String checkUrlLength2 = checkUrlLength(getOriUrlFromExtras(imageStatistics.getExtras()));
                ImageStatistics.FromType fromType = imageStatistics.getFromType();
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                create.setValue("domain", getHostFromPath(path));
                create.setValue("error", "0");
                create.setValue("bizName", getBizIdFromExtras(imageStatistics.getExtras()));
                MimeType format = imageStatistics.getFormat();
                create.setValue(FORMAT_DIMEN, format != null ? format.getMinorName() : "unknown");
                create.setValue(DATA_FROM_DIMEN, String.valueOf(fromType.value));
                create.setValue(PAGE_URL_DIMEN, getPageUrlFromExtras(imageStatistics.getExtras()));
                create.setValue(NEW_THREAD_MODEL, StatMonitor4Phenix.mIsUseNewThreadModel ? "1" : "0");
                create.setValue(SDK_INIT_TIME, String.valueOf(StatMonitor4Phenix.mInitTime));
                create.setValue(REQUEST_START_TIME, String.valueOf(imageStatistics.getRequestStartTime()));
                create.setValue(REQUEST_URL, checkUrlLength);
                create.setValue(ORIGINAL_URL, checkUrlLength2);
                create.setValue(TTL_GET_TIME, String.valueOf(imageStatistics2.mTTLGetTime));
                create.setValue(TTL_PUT_TIME, String.valueOf(imageStatistics2.mTTLPutTime));
                if (this.mNavInfoObtainer != null) {
                    str = this.mNavInfoObtainer.getCurrentWindowName();
                    create.setValue(WINDOW_NAME_DIMEN, str);
                    create.setValue(NAVI_URL_DIMEN, this.mNavInfoObtainer.getCurrentUrl());
                } else {
                    str = null;
                }
                if (this.mListener != null) {
                    long experimentId = this.mListener.getExperimentId();
                    if (experimentId > 0) {
                        create.setValue(TTL_EXPERIMENT_ID, String.valueOf(experimentId));
                    }
                }
                Map<String, Integer> detailCost = imageStatistics.getDetailCost();
                updateMeasureValue(create2, ImageStatistics.KEY_WAIT_FOR_MAIN, detailCost);
                updateMeasureValue(create2, ImageStatistics.KEY_TOTAL_TIME, detailCost);
                updateMeasureValue(create2, ImageStatistics.KEY_READ_MEMORY_CACHE, detailCost);
                int updateMeasureValue = updateMeasureValue(create2, ImageStatistics.KEY_SCHEDULE_TIME, detailCost) / getMinimumScheduleTime2StatWaitSize();
                create.setValue(SCHEDULE_FACTOR_DIMEN, String.valueOf(updateMeasureValue));
                if (updateMeasureValue > 0) {
                    updateMeasureValue(create2, ImageStatistics.KEY_MASTER_WAIT_SIZE, detailCost);
                    updateMeasureValue(create2, ImageStatistics.KEY_NETWORK_WAIT_SIZE, detailCost);
                    updateMeasureValue(create2, ImageStatistics.KEY_DECODE_WAIT_SIZE, detailCost);
                }
                if (StatMonitor4Phenix.mIsFullTrackValid) {
                    create.setValue(LAUNCH_TYPE_DIMEN, String.valueOf(SceneIdentifier.getStartType()));
                    create.setValue(LAUNCH_EXTERNAL_DIMEN, SceneIdentifier.isUrlLaunch() ? "1" : "0");
                    create.setValue(SINCE_LAUNCH_DIMEN, String.valueOf(imageStatistics.getRequestStartTime() - SceneIdentifier.getAppLaunchTime()));
                    create.setValue(DEVICE_LEVEL_DIMEN, String.valueOf(SceneIdentifier.getDeviceLevel()));
                    create.setValue(BUCKET_INFO, SceneIdentifier.getBucketInfo());
                    create.setValue(YIXIU_BUCKET, ABTestCenter.getUTABTestBucketId("PHENIX"));
                    if (SceneIdentifier.getStartType() != 1) {
                        create.setValue(SINCE_LAST_LAUNCH_DIMEN, String.valueOf((int) (SceneIdentifier.getAppLaunchTime() - SceneIdentifier.getLastLaunchTime())));
                    }
                }
                boolean z2 = fromType != ImageStatistics.FromType.FROM_MEMORY_CACHE;
                if (z2) {
                    int size = imageStatistics.getSize();
                    updateMeasureValue(create2, ImageStatistics.KEY_BITMAP_DECODE, detailCost);
                    z = z2;
                    create2.setValue("size", (double) size);
                    if (size <= 20480) {
                        str4 = "0_20K";
                    } else if (size <= 51200) {
                        str4 = "20_50K";
                    } else if (size <= 102400) {
                        str4 = "50_100K";
                    } else if (size <= 204800) {
                        str4 = "100_200K";
                    } else if (size <= 512000) {
                        str4 = "200_500K";
                    } else {
                        if (size % IoUtils.DEFAULT_IMAGE_TOTAL_SIZE == 0) {
                            i = size;
                        } else {
                            i = ((size + IoUtils.DEFAULT_IMAGE_TOTAL_SIZE) / IoUtils.DEFAULT_IMAGE_TOTAL_SIZE) * IoUtils.DEFAULT_IMAGE_TOTAL_SIZE;
                        }
                        str4 = ((i - IoUtils.DEFAULT_IMAGE_TOTAL_SIZE) / 1024) + "_" + (i / 1024) + "K";
                    }
                    create.setValue(SIZ_RANGE_DIMEN, str4);
                    switch (imageStatistics.getFromType()) {
                        case FROM_LOCAL_FILE:
                            str3 = str;
                            updateMeasureValue(create2, ImageStatistics.KEY_READ_LOCAL_FILE, detailCost);
                            break;
                        case FROM_DISK_CACHE:
                            str3 = str;
                            updateMeasureValue(create2, ImageStatistics.KEY_READ_DISK_CACHE, detailCost);
                            break;
                        case FROM_LARGE_SCALE:
                            str3 = str;
                            updateMeasureValue(create2, ImageStatistics.KEY_READ_DISK_CACHE, detailCost);
                            updateMeasureValue(create2, ImageStatistics.KEY_BITMAP_SCALE, detailCost);
                            break;
                        case FROM_NETWORK:
                            updateMeasureValue(create2, ImageStatistics.KEY_READ_DISK_CACHE, detailCost);
                            int updateMeasureValue2 = updateMeasureValue(create2, "connect", detailCost);
                            int updateMeasureValue3 = updateMeasureValue(create2, "download", detailCost);
                            Map<String, String> extras = imageStatistics.getExtras();
                            if (extras == null || this.mNetworkAnalyzer == null) {
                                str2 = str;
                                str10 = null;
                                str9 = null;
                                str8 = null;
                                str7 = null;
                                str6 = null;
                                str5 = null;
                            } else {
                                String str11 = extras.get(this.mNetworkAnalyzer.keyOfHitCdnCache());
                                if (str11 != null) {
                                    create.setValue(HIT_CDN_CACHE_DIMEN, str11);
                                }
                                String str12 = extras.get(this.mNetworkAnalyzer.keyOfConnectType());
                                if (str12 != null) {
                                    create.setValue(CONNECT_TYPE_DIMEN, str12);
                                }
                                str9 = extras.get(this.mNetworkAnalyzer.keyOfCdnIpPort());
                                if (str9 != null) {
                                    create.setValue(CDN_IP_PORT_DIMEN, str9);
                                }
                                String str13 = extras.get(this.mNetworkAnalyzer.keyOfFirstData());
                                if (str13 != null) {
                                    str7 = str11;
                                    str6 = str12;
                                    create2.setValue(FIRST_DATA_MEASURE, (double) Long.parseLong(str13));
                                } else {
                                    str7 = str11;
                                    str6 = str12;
                                }
                                str10 = extras.get(this.mNetworkAnalyzer.keyOfSendBefore());
                                if (str10 != null) {
                                    str2 = str;
                                    str5 = str13;
                                    create2.setValue(SEND_BEFORE_MEASURE, (double) Long.parseLong(str10));
                                } else {
                                    str2 = str;
                                    str5 = str13;
                                }
                                str8 = extras.get(this.mNetworkAnalyzer.keyOfResponseCode());
                                if (str8 != null) {
                                    create2.setValue(RESPONSE_CODE_MEASURE, (double) Long.parseLong(str8));
                                }
                                String str14 = extras.get(this.mNetworkAnalyzer.keyOfServerRt());
                                if (str14 != null) {
                                    create2.setValue(NETWORK_SERVER_RT, (double) Long.parseLong(str14));
                                }
                            }
                            UnitedLog.d(TAG, "network sub-stats: node=%s hit=%s type=%s connect=%d download=%d firstData=%s sendBefore=%s responseCode=%s size=%d, PATH=%s", str9, str7, str6, Integer.valueOf(updateMeasureValue2), Integer.valueOf(updateMeasureValue3), str5, str10, str8, Integer.valueOf(size), path);
                            int i2 = updateMeasureValue3 + updateMeasureValue2;
                            if (i2 > 0) {
                                create2.setValue(SPEED_MEASURE, (double) (size / i2));
                            }
                            imageSizeWarningException = ImageSizeWarningException.newWarningExceptionIfExceeded(size);
                            break;
                    }
                } else {
                    z = z2;
                }
                str3 = str;
                imageSizeWarningException = null;
                AppMonitor.Stat.commit(MODULE_NAME, MONITOR_POINT, create, create2);
                UnitedLog.d(TAG, "commit complete onSuccess, statistics=%s, path=%s", imageStatistics2, path);
                if (z) {
                    commitAlarm(DISK_CACHE_ALARM_POINT, imageStatistics.getDiskCacheHitCount(), imageStatistics.getDiskCacheMissCount(), String.valueOf(imageStatistics.getDiskCachePriority()));
                    commitAlarm(BITMAP_POOL_ALARM_POINT, imageStatistics.getBitmapPoolHitCount(), imageStatistics.getBitmapPoolMissCount(), str2);
                }
                commitAlarm(MEM_CACHE_ALARM_POINT, imageStatistics.getMemCacheHitCount(), imageStatistics.getMemCacheMissCount(), (String) null);
                if (imageSizeWarningException != null) {
                    onFail(imageStatistics2, imageSizeWarningException);
                }
            } else if (UnitedLog.isLoggable(3)) {
                UnitedLog.d(TAG, "filter this stat cause of sampling, statistic=%s ", imageStatistics2);
            }
        }
    }

    private String checkUrlLength(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.length() > 256 ? str.substring(0, 256) : str;
    }

    private void commitAlarm(String str, int i, int i2, String str2) {
        for (int i3 = 0; i3 < i; i3++) {
            if (str2 == null) {
                AppMonitor.Alarm.commitSuccess(MODULE_NAME, str);
            } else {
                AppMonitor.Alarm.commitSuccess(MODULE_NAME, str, str2);
            }
        }
        for (int i4 = 0; i4 < i2; i4++) {
            if (str2 == null) {
                AppMonitor.Alarm.commitFail(MODULE_NAME, str, (String) null, (String) null);
            } else {
                AppMonitor.Alarm.commitFail(MODULE_NAME, str, str2, (String) null, (String) null);
            }
        }
    }

    public String getFullTraceErrorCode(Throwable th) {
        boolean z = th instanceof NetworkResponseException;
        if ((th instanceof DecodeException) || z) {
            return analyzeErrorCode(th);
        }
        return "";
    }

    public void onFail(ImageStatistics imageStatistics, Throwable th) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        ImageStatistics imageStatistics2 = imageStatistics;
        Throwable th2 = th;
        if (!commitFullTrace(imageStatistics2, 0, getFullTraceErrorCode(th2))) {
            if (imageStatistics2 == null || imageStatistics.isDuplicated() || (th2 instanceof OnlyCacheFailedException) || (this.mNetworkAnalyzer != null && this.mNetworkAnalyzer.isNoNetworkException(th2))) {
                Object[] objArr = new Object[2];
                ImageStatistics imageStatistics3 = imageStatistics;
                objArr[0] = imageStatistics3;
                objArr[1] = imageStatistics3 != null ? imageStatistics.getUriInfo().getPath() : "unknown";
                UnitedLog.d(TAG, "skipped commit onFail, statistics=%s, path=%s", objArr);
                return;
            }
            String path = imageStatistics.getUriInfo().getPath();
            boolean z = th2 instanceof NetworkResponseException;
            boolean z2 = th2 instanceof DecodeException;
            String hostFromPath = getHostFromPath(path);
            String bizIdFromExtras = getBizIdFromExtras(imageStatistics.getExtras());
            MimeType format = imageStatistics.getFormat();
            String minorName = format != null ? format.getMinorName() : "unknown";
            String pageUrlFromExtras = getPageUrlFromExtras(imageStatistics.getExtras());
            if (!(th2 instanceof ImageSizeWarningException) && !filterOutThisStat(this.mStatCoverage)) {
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                create.setValue("domain", hostFromPath);
                create.setValue("error", "1");
                create.setValue("bizName", bizIdFromExtras);
                create.setValue(FORMAT_DIMEN, minorName);
                create.setValue(DATA_FROM_DIMEN, "0");
                create.setValue(PAGE_URL_DIMEN, pageUrlFromExtras);
                AppMonitor.Stat.commit(MODULE_NAME, MONITOR_POINT, create, create2);
            }
            if (this.mNavInfoObtainer != null) {
                str2 = this.mNavInfoObtainer.getCurrentWindowName();
                str = this.mNavInfoObtainer.getCurrentUrl();
            } else {
                str2 = null;
                str = null;
            }
            if (z || z2) {
                String analyzeErrorCode = analyzeErrorCode(th2);
                if (analyzeErrorCode != null) {
                    registerErrorMonitor();
                    DimensionValueSet create3 = DimensionValueSet.create();
                    MeasureValueSet create4 = MeasureValueSet.create();
                    create3.setValue("url", path);
                    create3.setValue("bizName", bizIdFromExtras);
                    create3.setValue(ERROR_ANALYSIS_CODE, analyzeErrorCode);
                    if (z) {
                        str8 = analyzeErrorCode;
                        create3.setValue(ERROR_ORIGIN_CODE, String.valueOf(((NetworkResponseException) th2).getHttpCode()));
                        create3.setValue("desc", ERROR_DESC_PREFIX + th.getMessage());
                    } else {
                        str8 = analyzeErrorCode;
                        create3.setValue(ERROR_ORIGIN_CODE, AlipayAuthConstant.LoginResult.SUCCESS);
                        create3.setValue("desc", ERROR_DESC_PREFIX + th.toString());
                    }
                    create3.setValue(FORMAT_DIMEN, minorName);
                    create3.setValue(DATA_FROM_DIMEN, String.valueOf(imageStatistics.getFromType().value));
                    create3.setValue(ERROR_ORIGIN_URL, getOriUrlFromExtras(imageStatistics.getExtras()));
                    if (!(str2 == null && str == null)) {
                        create3.setValue(WINDOW_NAME_DIMEN, str2);
                        create3.setValue(NAVI_URL_DIMEN, str);
                    }
                    create3.setValue(PAGE_URL_DIMEN, pageUrlFromExtras);
                    AppMonitor.Stat.commit(MODULE_NAME, ERROR_POINT, create3, create4);
                } else {
                    str8 = analyzeErrorCode;
                }
                str3 = str8;
            } else {
                str3 = null;
            }
            if (!z && !filterOutThisStat(this.mNonCriticalReportCoverage) && this.mNonCriticalErrorReporter != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("url", path);
                hashMap.put("domain", hostFromPath);
                hashMap.put(FORMAT_DIMEN, minorName);
                hashMap.put("bizName", bizIdFromExtras);
                hashMap.put("size", Integer.valueOf(imageStatistics.getSize()));
                hashMap.put(WINDOW_NAME_DIMEN, str2);
                hashMap.put(NAVI_URL_DIMEN, str);
                hashMap.put("isRetrying", Boolean.valueOf(imageStatistics.isRetrying()));
                hashMap.put("supportAshmem", Boolean.valueOf(Pexode.isAshmemSupported()));
                hashMap.put("supportInBitmap", Boolean.valueOf(Pexode.isInBitmapSupported()));
                hashMap.put("degradationBits", Integer.valueOf(this.mDegradationBits));
                hashMap.put("statusOfTBReal", Phenix.instance().schedulerBuilder().build().forCpuBound().getStatus());
                hashMap.put("sdkInt", Integer.valueOf(Build.VERSION.SDK_INT));
                Map<String, String> extras = imageStatistics.getExtras();
                if (extras == null || this.mNetworkAnalyzer == null) {
                    str6 = null;
                    str5 = null;
                    str4 = null;
                } else {
                    str5 = extras.get(this.mNetworkAnalyzer.keyOfCdnIpPort());
                    str6 = extras.get(this.mNetworkAnalyzer.keyOfHitCdnCache());
                    str4 = extras.get(this.mNetworkAnalyzer.keyOfConnectType());
                }
                if (str5 == null) {
                    str5 = "";
                }
                hashMap.put(CDN_IP_PORT_DIMEN, str5);
                if (str4 == null) {
                    str4 = "";
                }
                hashMap.put(CONNECT_TYPE_DIMEN, str4);
                if (str6 == null) {
                    str6 = "";
                }
                hashMap.put(HIT_CDN_CACHE_DIMEN, str6);
                String classShortName = RuntimeUtil.getClassShortName(th.getClass());
                if (z2) {
                    DecodeException decodeException = (DecodeException) th2;
                    classShortName = classShortName + ":" + decodeException.getDecodedError() + "[Local?" + decodeException.isLocalUri() + ", Disk?" + decodeException.isDataFromDisk() + Operators.ARRAY_END_STR;
                    Throwable cause = decodeException.getCause();
                    if (cause != null) {
                        str7 = classShortName;
                        th2 = cause;
                        this.mNonCriticalErrorReporter.onNonCriticalErrorHappen(str3, th2, hashMap);
                        UnitedLog.w(TAG, "report non-critical error, details=%s, path=%s", hashMap, path);
                    }
                }
                str7 = classShortName;
                this.mNonCriticalErrorReporter.onNonCriticalErrorHappen(str3, th2, hashMap);
                UnitedLog.w(TAG, "report non-critical error, details=%s, path=%s", hashMap, path);
            }
            UnitedLog.w(TAG, "commit complete onFail, analysisCode=%s, throwable=%s, window=%s, navi=%s, path=%s", str3, th2, str2, str, path);
        }
    }

    private String analyzeErrorCode(Throwable th) {
        if (th instanceof IncompleteResponseException) {
            return "101010";
        }
        if (th instanceof HttpCodeResponseException) {
            int httpCode = ((HttpCodeResponseException) th).getHttpCode();
            if (httpCode == 404) {
                return "102010";
            }
            return httpCode == 503 ? "103010" : "104000";
        }
        if (th instanceof ImageSizeWarningException) {
            int access$200 = ((ImageSizeWarningException) th).exceededTimes;
            if (access$200 >= 1 && access$200 < 2) {
                return "801010";
            }
            if (access$200 >= 2 && access$200 < 4) {
                return "801020";
            }
            if (access$200 >= 4) {
                return "801090";
            }
        }
        if (this.mNetworkAnalyzer == null) {
            return null;
        }
        if (this.mNetworkAnalyzer.isReadTimeoutException(th)) {
            return "101011";
        }
        if (this.mNetworkAnalyzer.isCertificateException(th)) {
            return "103011";
        }
        if (this.mNetworkAnalyzer.isInvalidHostException(th)) {
            return "201010";
        }
        if (this.mNetworkAnalyzer.isConnectTimeoutException(th)) {
            return "201011";
        }
        if (this.mNetworkAnalyzer.isInvalidUrlException(th)) {
            return "201012";
        }
        if (this.mNetworkAnalyzer.isIndifferentException(th)) {
            return "901000";
        }
        if (!(th instanceof DecodeException)) {
            return null;
        }
        DecodeException decodeException = (DecodeException) th;
        return decodeException.getDecodedError() + "[Local?" + decodeException.isLocalUri() + ", Disk?" + decodeException.isDataFromDisk() + Operators.ARRAY_END_STR;
    }

    public void onForcedDegrade2System() {
        this.mDegradationBits = (this.mDegradationBits & -2) + 1;
        AppMonitor.Counter.commit(MODULE_NAME, FORCED_SYSTEM_COUNTER_POINT, 1.0d);
    }

    public void onForcedDegrade2NoAshmem() {
        this.mDegradationBits = (this.mDegradationBits & -3) + 2;
        AppMonitor.Counter.commit(MODULE_NAME, FORCED_NO_ASHMEM_COUNTER_POINT, 1.0d);
    }

    public void onForcedDegrade2NoInBitmap() {
        this.mDegradationBits = (this.mDegradationBits & -5) + 4;
        AppMonitor.Counter.commit(MODULE_NAME, FORCED_NO_IN_BITMAP_COUNTER_POINT, 1.0d);
    }

    public void onDegrade2Unlimited() {
        this.mDegradationBits = (this.mDegradationBits & -9) + 8;
        AppMonitor.Counter.commit(MODULE_NAME, FORCED_UNLIMITED_NETWORK_COUNTER_POINT, 1.0d);
    }

    private static class ImageSizeWarningException extends NetworkResponseException {
        /* access modifiers changed from: private */
        public static int sImageWarningSize;
        /* access modifiers changed from: private */
        public final int exceededTimes;

        /* access modifiers changed from: private */
        public static ImageSizeWarningException newWarningExceptionIfExceeded(int i) {
            if (sImageWarningSize <= 0 || i < sImageWarningSize) {
                return null;
            }
            return new ImageSizeWarningException(i);
        }

        public ImageSizeWarningException(int i) {
            super(200, "image size[" + i + "] exceeded " + (i / sImageWarningSize) + " times");
            this.exceededTimes = i / sImageWarningSize;
        }
    }

    public void onStart(ImageStatistics imageStatistics) {
        if (StatMonitor4Phenix.mIsFullTrackValid && TextUtils.isEmpty(imageStatistics.mFullTraceId)) {
            imageStatistics.mFullTraceId = FullTraceAnalysis.getInstance().createRequest("picture");
        }
    }

    public void onCancel(ImageStatistics imageStatistics) {
        if (StatMonitor4Phenix.mIsFullTrackValid) {
            commitFullTrace(imageStatistics, 2, "");
        }
    }

    private boolean commitFullTrace(ImageStatistics imageStatistics, int i, String str) {
        if (!StatMonitor4Phenix.mIsFullTrackValid || imageStatistics == null || TextUtils.isEmpty(imageStatistics.mFullTraceId)) {
            return false;
        }
        boolean z = imageStatistics.mIsOnlyFullTrack;
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.ret = i;
        requestInfo.bizId = imageStatistics.mBizId;
        if (str == null) {
            str = "";
        }
        requestInfo.bizErrorCode = str;
        if (imageStatistics.getExtras() != null) {
            String str2 = imageStatistics.getExtras().get(EAGLEID);
            if (str2 == null) {
                str2 = "";
            }
            requestInfo.serverTraceId = str2;
        }
        if (imageStatistics.getFromType() != ImageStatistics.FromType.FROM_NETWORK) {
            requestInfo.protocolType = "cache";
            if (imageStatistics.getFromType() == ImageStatistics.FromType.FROM_DISK_CACHE || imageStatistics.getFromType() == ImageStatistics.FromType.FROM_LOCAL_FILE) {
                requestInfo.rspDeflateSize = imageStatistics.mRspDeflateSize;
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[traceId:");
            sb.append(imageStatistics.mFullTraceId);
            sb.append(Operators.ARRAY_END_STR);
            sb.append("|end");
            sb.append(",ret=");
            sb.append(i);
            Map<String, Integer> detailCost = imageStatistics.getDetailCost();
            if (detailCost != null) {
                sb.append(",totalTime=");
                sb.append(getDetailTime(detailCost, ImageStatistics.KEY_TOTAL_TIME));
                sb.append(",wait2Main=");
                sb.append(getDetailTime(detailCost, ImageStatistics.KEY_WAIT_FOR_MAIN));
                int detailTime = getDetailTime(detailCost, ImageStatistics.KEY_SCHEDULE_TIME);
                sb.append(",scheduleTime=");
                sb.append(detailTime);
                sb.append(",decodeTime=");
                sb.append(getDetailTime(detailCost, ImageStatistics.KEY_BITMAP_DECODE));
                sb.append(",networkConnect=");
                sb.append(getDetailTime(detailCost, "connect"));
                sb.append(",networkDownload=");
                sb.append(getDetailTime(detailCost, "download"));
                if (detailTime > 5000 && Phenix.instance().schedulerBuilder().hasBuild()) {
                    sb.append("|");
                    sb.append(Phenix.instance().schedulerBuilder().build().forDecode().getStatus());
                }
            }
            UnitedLog.e("Phenix", sb.toString(), new Object[0]);
        }
        Map<String, Integer> detailCost2 = imageStatistics.getDetailCost();
        if (detailCost2 != null && detailCost2.containsKey(ImageStatistics.KEY_BITMAP_DECODE)) {
            requestInfo.deserializeTime = (long) detailCost2.get(ImageStatistics.KEY_BITMAP_DECODE).intValue();
        }
        requestInfo.url = imageStatistics.getUriInfo().getPath();
        requestInfo.bizReqStart = imageStatistics.getRequestStartTime();
        requestInfo.bizReqProcessStart = imageStatistics.mReqProcessStart;
        requestInfo.bizRspProcessStart = imageStatistics.mRspProcessStart;
        requestInfo.bizRspCbDispatch = imageStatistics.mRspCbDispatch;
        requestInfo.bizRspCbStart = imageStatistics.mRspCbStart;
        requestInfo.bizRspCbEnd = imageStatistics.mRspCbEnd;
        FullTraceAnalysis.getInstance().commitRequest(imageStatistics.mFullTraceId, "picture", requestInfo);
        return z;
    }

    private int getDetailTime(Map<String, Integer> map, String str) {
        if (map == null || !map.containsKey(str)) {
            return 0;
        }
        return map.get(str).intValue();
    }
}
