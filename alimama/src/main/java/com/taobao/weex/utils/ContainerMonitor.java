package com.taobao.weex.utils;

import android.net.Uri;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ContainerMonitor {
    private static final String DIMENSION_URL_KEY = "url_key";
    private static final String KEY_MONITOR = "page_frame_with_container";
    private static final String MEASURE_PAGE_FS_TIME = "fs_time";
    private static final String MODULE = "weex_container";
    private static ContainerMonitor instance;
    private AtomicBoolean hasBeenInited = new AtomicBoolean(false);
    private Map<String, Map<String, Object>> mData = new ConcurrentHashMap();

    private ContainerMonitor() {
        init();
    }

    public static ContainerMonitor monitor() {
        if (instance == null) {
            synchronized (ContainerMonitor.class) {
                if (instance == null) {
                    instance = new ContainerMonitor();
                }
            }
        }
        return instance;
    }

    private void init() {
        if (this.hasBeenInited.compareAndSet(false, true)) {
            try {
                DimensionSet create = DimensionSet.create();
                create.addDimension(DIMENSION_URL_KEY);
                MeasureSet create2 = MeasureSet.create();
                create2.addMeasure(MEASURE_PAGE_FS_TIME);
                AppMonitor.register(MODULE, KEY_MONITOR, create2, create);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fsTime(String str, long j) {
        setData(str, MEASURE_PAGE_FS_TIME, Long.valueOf(j));
    }

    private synchronized void setData(String str, String str2, Object obj) {
        Map map = this.mData.get(str);
        if (map == null) {
            map = new ConcurrentHashMap();
            this.mData.put(str, map);
        }
        map.put(str2, obj);
    }

    public void commitData(String str) {
        try {
            Map map = this.mData.get(str);
            if (map != null) {
                if (map.size() != 0) {
                    DimensionValueSet create = DimensionValueSet.create();
                    create.setValue(DIMENSION_URL_KEY, str);
                    MeasureValueSet create2 = MeasureValueSet.create();
                    create2.setValue(MEASURE_PAGE_FS_TIME, (double) Long.parseLong(String.valueOf(map.get(MEASURE_PAGE_FS_TIME))));
                    AppMonitor.Stat.commit(MODULE, KEY_MONITOR, create, create2);
                    removeData(str);
                    return;
                }
            }
            removeData(str);
        } catch (Throwable th) {
            removeData(str);
            throw th;
        }
    }

    public static String getUrlKey(Uri uri) {
        if (uri == null || !uri.isHierarchical()) {
            return null;
        }
        String queryParameter = uri.getQueryParameter("wh_pid");
        Uri.Builder scheme = uri.buildUpon().clearQuery().scheme("");
        if (!TextUtils.isEmpty(queryParameter)) {
            scheme.appendPath(queryParameter);
        }
        String builder = scheme.toString();
        return builder.startsWith(HttpConstant.SCHEME_SPLIT) ? builder.substring(3) : builder;
    }

    private void removeData(String str) {
        this.mData.remove(str);
    }
}
