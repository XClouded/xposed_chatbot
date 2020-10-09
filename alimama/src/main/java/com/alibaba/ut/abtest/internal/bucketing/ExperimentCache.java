package com.alibaba.ut.abtest.internal.bucketing;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentType;
import com.alibaba.ut.abtest.internal.database.WhereCondition;
import com.alibaba.ut.abtest.internal.database.WhereConditionCollector;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.LruCache;
import com.alibaba.ut.abtest.internal.util.ServerClock;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import com.alibaba.ut.abtest.track.UriUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExperimentCache {
    private static final int MODEL_CACHE_MAX_COUNT = 260;
    private static final int MODEL_NOT_EXISTS_CACHE_MAX_COUNT = 150;
    private static final String TAG = "ExperimentCache";
    /* access modifiers changed from: private */
    public ExperimentGroupDao experimentGroupDao;
    private ConcurrentHashMap<Long, Long> groupId2experimentIdCache = new ConcurrentHashMap<>();
    private LruCache<String, List<ExperimentGroup>> modelCache = new LruCache<>(260);
    private final Object modelCacheLock = new Object();
    /* access modifiers changed from: private */
    public LruCache<String, Object> modelNotExistsCache = new LruCache<>(150);
    private Map<String, UriPathMap> uriCache = new HashMap();
    private final Object uriCacheLock = new Object();

    public ExperimentCache(ExperimentGroupDao experimentGroupDao2) {
        this.experimentGroupDao = experimentGroupDao2;
    }

    public void initialize() {
        try {
            WhereConditionCollector whereConditionCollector = new WhereConditionCollector();
            whereConditionCollector.whereAnd(new WhereCondition("end_time>?", Long.valueOf(ServerClock.now())), new WhereCondition[0]);
            WhereConditionCollector whereConditionCollector2 = new WhereConditionCollector();
            whereConditionCollector2.whereOr(new WhereCondition("type=?", Integer.valueOf(ExperimentType.AbUri.getValue())), new WhereCondition("type=?", Integer.valueOf(ExperimentType.Redirect.getValue())), new WhereCondition[0]);
            whereConditionCollector.whereAnd(whereConditionCollector2.combine(), new WhereCondition[0]);
            ArrayList queryByCurrentUser = this.experimentGroupDao.queryByCurrentUser((String[]) null, "id ASC", 0, 0, whereConditionCollector);
            if (queryByCurrentUser != null && !queryByCurrentUser.isEmpty()) {
                Iterator it = queryByCurrentUser.iterator();
                while (it.hasNext()) {
                    addItem(ExperimentBuilder.createExperimentGroup((ExperimentGroupDO) it.next()));
                }
            }
            WhereConditionCollector whereConditionCollector3 = new WhereConditionCollector();
            whereConditionCollector3.whereAnd(new WhereCondition("end_time>?", Long.valueOf(ServerClock.now())), new WhereCondition[0]);
            whereConditionCollector3.whereAnd(new WhereCondition("type=?", Integer.valueOf(ExperimentType.AbComponent.getValue())), new WhereCondition[0]);
            ArrayList queryByCurrentUser2 = this.experimentGroupDao.queryByCurrentUser((String[]) null, (String) null, 0, 260, whereConditionCollector3);
            if (queryByCurrentUser2 != null && !queryByCurrentUser2.isEmpty()) {
                Iterator it2 = queryByCurrentUser2.iterator();
                while (it2.hasNext()) {
                    addItem(ExperimentBuilder.createExperimentGroup((ExperimentGroupDO) it2.next()));
                }
            }
        } catch (Throwable th) {
            Analytics.commitFail(Analytics.SERVICE_ALARM, "ExperimentCache.initialize", th.getMessage(), Log.getStackTraceString(th));
            LogUtils.logE(TAG, "initialize", th);
        }
    }

    public void clear() {
        this.modelCache.evictAll();
        this.groupId2experimentIdCache.clear();
        synchronized (this.uriCacheLock) {
            this.uriCache.clear();
        }
    }

    /* access modifiers changed from: protected */
    public String getUriCacheKey(Uri uri) {
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) || scheme.startsWith("http")) {
            scheme = "http";
        }
        return scheme + ":" + uri.getAuthority();
    }

    public void addItem(ExperimentGroup experimentGroup) {
        if (experimentGroup != null) {
            if (experimentGroup.getType() == ExperimentType.AbComponent) {
                synchronized (this.modelCacheLock) {
                    List list = this.modelCache.get(experimentGroup.getKey());
                    if (list == null) {
                        list = new ArrayList();
                        this.modelCache.put(experimentGroup.getKey(), list);
                    }
                    list.add(experimentGroup);
                }
            } else if (experimentGroup.getType() == ExperimentType.AbUri || experimentGroup.getType() == ExperimentType.Redirect) {
                if (experimentGroup.getUri() == null) {
                    experimentGroup.setUri(UriUtils.parseURI(experimentGroup.getKey()));
                }
                if (experimentGroup.getUri() != null) {
                    String uriCacheKey = getUriCacheKey(experimentGroup.getUri());
                    synchronized (this.uriCacheLock) {
                        UriPathMap uriPathMap = this.uriCache.get(uriCacheKey);
                        if (uriPathMap == null) {
                            uriPathMap = new UriPathMap();
                            this.uriCache.put(uriCacheKey, uriPathMap);
                        }
                        uriPathMap.put(experimentGroup.getUri(), experimentGroup);
                    }
                }
            }
            this.groupId2experimentIdCache.put(Long.valueOf(experimentGroup.getId()), Long.valueOf(experimentGroup.getExperimentId()));
        }
    }

    public ExperimentGroup removeItem(ExperimentGroup experimentGroup) {
        if (experimentGroup == null) {
            return null;
        }
        this.groupId2experimentIdCache.remove(Long.valueOf(experimentGroup.getId()));
        if (experimentGroup.getType() == ExperimentType.AbComponent) {
            synchronized (this.modelCacheLock) {
                List list = this.modelCache.get(experimentGroup.getKey());
                if (list == null) {
                    return null;
                }
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ExperimentGroup experimentGroup2 = (ExperimentGroup) it.next();
                    if (experimentGroup2.getId() == experimentGroup.getId()) {
                        it.remove();
                        return experimentGroup2;
                    }
                }
            }
        } else if (experimentGroup.getType() == ExperimentType.AbUri || experimentGroup.getType() == ExperimentType.Redirect) {
            if (experimentGroup.getUri() == null) {
                experimentGroup.setUri(UriUtils.parseURI(experimentGroup.getKey()));
            }
            if (experimentGroup.getUri() != null) {
                String uriCacheKey = getUriCacheKey(experimentGroup.getUri());
                synchronized (this.uriCacheLock) {
                    UriPathMap uriPathMap = this.uriCache.get(uriCacheKey);
                    if (uriPathMap == null) {
                        return null;
                    }
                    ExperimentGroup remove = uriPathMap.remove(experimentGroup);
                    return remove;
                }
            }
        }
        return null;
    }

    public Long getExperimentId(long j) {
        return this.groupId2experimentIdCache.get(Long.valueOf(j));
    }

    public List<ExperimentGroup> getItem(Uri uri) {
        if (uri == null) {
            return null;
        }
        String uriCacheKey = getUriCacheKey(uri);
        synchronized (this.uriCacheLock) {
            UriPathMap uriPathMap = this.uriCache.get(uriCacheKey);
            if (uriPathMap == null) {
                return null;
            }
            List<ExperimentGroup> list = uriPathMap.get(uri);
            return list;
        }
    }

    public List<ExperimentGroup> getItem(final String str) {
        List<ExperimentGroup> list = this.modelCache.get(str);
        if (list == null || list.isEmpty()) {
            if (this.modelNotExistsCache.get(str) != null) {
                return null;
            }
            TaskExecutor.executeBackground(new Runnable() {
                public void run() {
                    try {
                        WhereConditionCollector whereConditionCollector = new WhereConditionCollector();
                        whereConditionCollector.whereAnd(new WhereCondition("key=?", str), new WhereCondition[0]);
                        ArrayList<ExperimentGroupDO> queryByCurrentUser = ExperimentCache.this.experimentGroupDao.queryByCurrentUser((String[]) null, (String) null, 0, 0, whereConditionCollector);
                        if (queryByCurrentUser == null || queryByCurrentUser.isEmpty()) {
                            ExperimentCache.this.modelNotExistsCache.put(str, Boolean.TRUE);
                            return;
                        }
                        for (ExperimentGroupDO createExperimentGroup : queryByCurrentUser) {
                            ExperimentCache.this.addItem(ExperimentBuilder.createExperimentGroup(createExperimentGroup));
                        }
                    } catch (Throwable th) {
                        LogUtils.logE(ExperimentCache.TAG, th.getMessage(), th);
                    }
                }
            });
        }
        return list;
    }
}
