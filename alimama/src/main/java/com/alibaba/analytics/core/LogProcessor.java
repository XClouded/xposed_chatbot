package com.alibaba.analytics.core;

import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.UTRealtimeConfBiz;
import com.alibaba.analytics.core.logbuilder.LogPriorityMgr;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.core.sync.UploadLogFromCache;
import com.alibaba.analytics.utils.Logger;
import java.util.List;
import java.util.Map;

public class LogProcessor {
    public static void process(Map<String, String> map) {
        boolean z;
        Logger.d();
        if (map != null) {
            String str = map.get(LogField.EVENTID.toString());
            if (!map.containsKey("_priority")) {
                if ("2201".equalsIgnoreCase(str)) {
                    map.put("_priority", "4");
                }
                if ("2202".equalsIgnoreCase(str)) {
                    map.put("_priority", Constants.LogTransferLevel.L6);
                }
            }
            String str2 = "3";
            if (map.containsKey("_priority")) {
                str2 = map.remove("_priority");
            }
            String configLogLevel = LogPriorityMgr.getInstance().getConfigLogLevel(str);
            if (!TextUtils.isEmpty(configLogLevel)) {
                str2 = configLogLevel;
            }
            if (map.containsKey("_sls")) {
                map.remove("_sls");
                z = true;
            } else {
                z = false;
            }
            int topicId = UTRealtimeConfBiz.getInstance().isRealtimeLogSampled() ? UTRealtimeConfBiz.getInstance().getTopicId(map) : 0;
            Log log = new Log(str2, (List<String>) null, str, map);
            if (topicId > 0) {
                Logger.d("", "topicId", Integer.valueOf(topicId));
                log.setTopicId(topicId);
                UploadLogFromCache.getInstance().addLog(log);
            }
            if (z) {
                LogStoreMgr.getInstance().addLogAndSave(log);
            } else {
                LogStoreMgr.getInstance().add(log);
            }
        }
    }
}
