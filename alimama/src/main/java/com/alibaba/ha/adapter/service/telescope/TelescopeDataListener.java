package com.alibaba.ha.adapter.service.telescope;

import com.ali.telescope.interfaces.TelescopeEventData;
import com.alibaba.ha.adapter.AliHaAdapter;
import java.util.Map;

public class TelescopeDataListener implements TelescopeEventData {
    private String module = "EVENT_TRACE";
    private String tag = "TELESCOPE";

    public void onListener(long j, String str, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        if (obj2 != null && (obj2 instanceof String)) {
            AliHaAdapter.getInstance().tLogService.loge(this.module, this.tag, (String) obj2);
        }
    }
}
