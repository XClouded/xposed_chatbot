package com.taobao.android.ultron.datamodel.util;

import com.taobao.android.ultron.common.model.IDMEvent;
import java.util.List;
import java.util.Map;

public class RollbackUtils {
    public static void recordEventMap(Map<String, List<IDMEvent>> map) {
        List<IDMEvent> list;
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                if (!(next == null || (list = (List) next.getValue()) == null)) {
                    for (IDMEvent iDMEvent : list) {
                        if (iDMEvent != null) {
                            iDMEvent.record();
                        }
                    }
                }
            }
        }
    }

    public static void rollbackEventMap(Map<String, List<IDMEvent>> map) {
        List<IDMEvent> list;
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                if (!(next == null || (list = (List) next.getValue()) == null)) {
                    for (IDMEvent iDMEvent : list) {
                        if (iDMEvent != null) {
                            iDMEvent.rollBack();
                        }
                    }
                }
            }
        }
    }
}
