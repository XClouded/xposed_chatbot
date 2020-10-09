package com.alibaba.aliweex.hc.cache;

import alimama.com.unwrouter.UNWRouter;
import android.net.Uri;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.pha.core.rescache.Package;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.analyzer.Config;
import java.util.ArrayList;
import java.util.Iterator;

public class CachePerf {
    public static final String FAIL_CODE_CACHE_PROCESS_CANCELED = "-111113";
    public static final String FAIL_CODE_CACHE_PROCESS_ERROR = "-111114";
    public static final String FAIL_CODE_PKG_REQUEST_FAIL = "-111112";
    public static final String FAIL_CODE_PKG_SIZE = "-111111";
    private static final String KEY_AVFS_CACHE_RATIO = "cacheAVFSCacheRatio";
    private static final String KEY_FSALL_TIME = "fsAllTime";
    private static final String KEY_FSBUNDLE_EXEC_TIME = "fsBundleExecTime";
    private static final String KEY_FSBUNDLE_NETWORK_TIME = "fsBundleNetworkTime";
    private static final String KEY_FSBUNDLE_RENDER_TIME = "fsBundleRenderTime";
    private static final String KEY_FSMODULE_CACHE_TIME = "fsModuleCacheTime";
    private static final String KEY_FSMODULE_DATA_MANAGE_TIME = "fsModuleDataManageTime";
    private static final String KEY_FSMODULE_EXECUTE_TIME = "fsModuleExecuteTime";
    private static final String KEY_FSMTOP_TIME = "fsMtopTime";
    private static final String KEY_FSRENDER_TIME = "fsRenderTime";
    private static final String KEY_MEM_CACHERATIO = "cacheMemRatio";
    private static final String KEY_NETWORK_RATIO = "cacheNetworkRatio";
    private static final String KEY_PROCESS_CACHE_ALL_TIME = "cacheProcessAllTime";
    private static final String KEY_REQUEST_ALL_PKGS_TIME = "cacheRequestAllPkgsTime";
    private static final String KEY_ZCACHE_RATIO = "cacheZcacheRatio";
    private static final String MONITOR_POINT_ALARM = "PageCacheAlarm";
    private static final String MONITOR_POINT_STAT = "PageCacheStat";
    private static final String MONITOR_POINT_WB_STAT = "PageCacheWBStat";
    private static boolean inited;
    public static final String[] keys = {KEY_REQUEST_ALL_PKGS_TIME, KEY_PROCESS_CACHE_ALL_TIME, KEY_MEM_CACHERATIO, KEY_ZCACHE_RATIO, KEY_NETWORK_RATIO, KEY_AVFS_CACHE_RATIO, KEY_FSMTOP_TIME, KEY_FSMODULE_CACHE_TIME, KEY_FSMODULE_EXECUTE_TIME, KEY_FSMODULE_DATA_MANAGE_TIME, KEY_FSRENDER_TIME, KEY_FSALL_TIME, KEY_FSBUNDLE_EXEC_TIME, KEY_FSBUNDLE_NETWORK_TIME, KEY_FSBUNDLE_RENDER_TIME};
    public static final String[] keysWb = {"prefetchTime"};
    private static CachePerf sInstance;
    private double avfsCacheRatio;
    long mFSAllTime;
    long mFSBundleExecTime;
    long mFSBundleNetworkTime;
    long mFSBundleRenderTime;
    long mFSModuleCacheTime;
    long mFSModuleDataManageTime;
    long mFSModuleExecuteTime;
    long mFSMtopTime;
    long mFSRenderTime;
    long mFSStartTime;
    private double memCacheRatio;
    private double networkRatio;
    public String pageName;
    long processCacheAllTime;
    long requestAllPkgsTime;
    private double zcacheRatio;

    public void alarmRequestSuccess(String str, long j, String str2) {
    }

    private CachePerf() {
    }

    public static CachePerf getInstance() {
        if (sInstance == null) {
            synchronized (CachePerf.class) {
                if (sInstance == null) {
                    sInstance = new CachePerf();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        if (!inited) {
            DimensionSet create = DimensionSet.create();
            create.addDimension(UNWRouter.PAGE_NAME);
            MeasureSet create2 = MeasureSet.create();
            for (String measure : keys) {
                create2.addMeasure(new Measure(measure));
            }
            AppMonitor.register("weex", MONITOR_POINT_STAT, create2, create);
            DimensionSet create3 = DimensionSet.create();
            create3.addDimension(UNWRouter.PAGE_NAME);
            create3.addDimension("traceId");
            MeasureSet create4 = MeasureSet.create();
            for (String measure2 : keysWb) {
                create4.addMeasure(new Measure(measure2));
            }
            AppMonitor.register("ClientCache", MONITOR_POINT_WB_STAT, create4, create3);
            inited = true;
        }
    }

    public void destroy() {
        this.pageName = null;
        this.requestAllPkgsTime = 0;
        this.processCacheAllTime = 0;
        this.memCacheRatio = 0.0d;
        this.zcacheRatio = 0.0d;
        this.networkRatio = 0.0d;
        this.avfsCacheRatio = 0.0d;
        this.mFSMtopTime = 0;
        this.mFSModuleCacheTime = 0;
        this.mFSModuleExecuteTime = 0;
        this.mFSModuleDataManageTime = 0;
        this.mFSRenderTime = 0;
        this.mFSStartTime = 0;
        this.mFSAllTime = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String append : keys) {
            sb.append(append);
            sb.append("=%s ");
        }
        return String.format(sb.toString(), new Object[]{Long.valueOf(this.requestAllPkgsTime), Long.valueOf(this.processCacheAllTime), Double.valueOf(this.memCacheRatio), Double.valueOf(this.zcacheRatio), Double.valueOf(this.networkRatio), Double.valueOf(this.avfsCacheRatio), Long.valueOf(this.mFSMtopTime), Long.valueOf(this.mFSModuleCacheTime), Long.valueOf(this.mFSModuleExecuteTime), Long.valueOf(this.mFSModuleDataManageTime), Long.valueOf(this.mFSRenderTime), Long.valueOf(this.mFSAllTime), Long.valueOf(this.mFSBundleExecTime), Long.valueOf(this.mFSBundleNetworkTime), Long.valueOf(this.mFSBundleRenderTime)});
    }

    public void commitFail(String str, String str2, String str3) {
        AppMonitor.Alarm.commitFail("weex", MONITOR_POINT_ALARM, str, str2, str3);
    }

    public void commitStatWBCache(String str, long j, String str2) {
        init();
        DimensionValueSet create = DimensionValueSet.create();
        if (!TextUtils.isEmpty(str)) {
            Uri parse = Uri.parse(str);
            String queryParameter = parse.getQueryParameter("wh_pid");
            Uri.Builder scheme = parse.buildUpon().clearQuery().scheme("");
            if (!TextUtils.isEmpty(queryParameter)) {
                scheme.appendPath(queryParameter);
            }
            String builder = scheme.toString();
            if (builder.startsWith(HttpConstant.SCHEME_SPLIT)) {
                builder = builder.substring(3);
            }
            create.setValue(UNWRouter.PAGE_NAME, builder);
            if (!TextUtils.isEmpty(str2)) {
                create.setValue("traceId", str2);
            }
            MeasureValueSet create2 = MeasureValueSet.create();
            create2.setValue("prefetchTime", (double) j);
            AppMonitor.Stat.commit("ClientCache", MONITOR_POINT_WB_STAT, create, create2);
        }
    }

    public void commitStatWeexCache() {
        init();
        DimensionValueSet create = DimensionValueSet.create();
        if (!TextUtils.isEmpty(this.pageName)) {
            Uri parse = Uri.parse(this.pageName);
            String queryParameter = parse.getQueryParameter("wh_pid");
            Uri.Builder scheme = parse.buildUpon().clearQuery().scheme("");
            if (!TextUtils.isEmpty(queryParameter)) {
                scheme.appendPath(queryParameter);
            }
            String builder = scheme.toString();
            if (builder.startsWith(HttpConstant.SCHEME_SPLIT)) {
                builder = builder.substring(3);
            }
            create.setValue(UNWRouter.PAGE_NAME, builder);
        }
        MeasureValueSet create2 = MeasureValueSet.create();
        create2.setValue(KEY_REQUEST_ALL_PKGS_TIME, (double) this.requestAllPkgsTime);
        create2.setValue(KEY_PROCESS_CACHE_ALL_TIME, (double) this.processCacheAllTime);
        create2.setValue(KEY_MEM_CACHERATIO, this.memCacheRatio);
        create2.setValue(KEY_ZCACHE_RATIO, this.zcacheRatio);
        create2.setValue(KEY_NETWORK_RATIO, this.networkRatio);
        create2.setValue(KEY_AVFS_CACHE_RATIO, this.avfsCacheRatio);
        create2.setValue(KEY_FSALL_TIME, (double) this.mFSAllTime);
        create2.setValue(KEY_FSMODULE_CACHE_TIME, (double) this.mFSModuleCacheTime);
        create2.setValue(KEY_FSMODULE_DATA_MANAGE_TIME, (double) this.mFSModuleDataManageTime);
        create2.setValue(KEY_FSMODULE_EXECUTE_TIME, (double) this.mFSModuleExecuteTime);
        create2.setValue(KEY_FSMTOP_TIME, (double) this.mFSMtopTime);
        create2.setValue(KEY_FSRENDER_TIME, (double) this.mFSRenderTime);
        create2.setValue(KEY_FSBUNDLE_EXEC_TIME, (double) this.mFSBundleExecTime);
        create2.setValue(KEY_FSBUNDLE_NETWORK_TIME, (double) this.mFSBundleNetworkTime);
        create2.setValue(KEY_FSBUNDLE_RENDER_TIME, (double) this.mFSBundleRenderTime);
        AppMonitor.Stat.commit("weex", MONITOR_POINT_STAT, create, create2);
        if (WXEnvironment.isApkDebugable()) {
            WeexCacheMsgPanel.d("缓存方案流程perf:allTime:" + this.processCacheAllTime + " memoryRatio:" + this.memCacheRatio + " networkRatio:" + this.networkRatio + " zcacheRatio:" + this.zcacheRatio + " avfsRatio:" + this.avfsCacheRatio);
        }
    }

    public void cacheRatioStatistic(ArrayList<Package.Item> arrayList) {
        int[] iArr = {0, 0, 0, 0};
        Iterator<Package.Item> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Package.Item next = it.next();
            i += next.depInfos.size();
            Iterator it2 = next.depInfos.iterator();
            while (it2.hasNext()) {
                Package.Info info = (Package.Info) it2.next();
                if (Config.TYPE_MEMORY.equals(info.from)) {
                    iArr[0] = iArr[0] + 1;
                } else if ("zcache".equals(info.from)) {
                    iArr[1] = iArr[1] + 1;
                } else if ("avfs".equals(info.from)) {
                    iArr[3] = iArr[3] + 1;
                } else if ("network".equals(info.from)) {
                    iArr[2] = iArr[2] + 1;
                }
            }
        }
        if (i > 0) {
            double d = (double) iArr[0];
            double d2 = (double) i;
            Double.isNaN(d2);
            double d3 = d2 * 1.0d;
            Double.isNaN(d);
            this.memCacheRatio = d / d3;
            double d4 = (double) iArr[1];
            Double.isNaN(d4);
            this.zcacheRatio = d4 / d3;
            double d5 = (double) iArr[2];
            Double.isNaN(d5);
            this.networkRatio = d5 / d3;
            double d6 = (double) iArr[3];
            Double.isNaN(d6);
            this.avfsCacheRatio = d6 / d3;
        }
    }
}
