package com.taobao.android.dinamic;

import android.view.View;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.expression.DinamicExpression;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DinamicProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class DinamicDataBinder {
    DinamicDataBinder() {
    }

    static void bindData(View view, DinamicParams dinamicParams) {
        DinamicProperty viewProperty = DinamicViewUtils.getViewProperty(view);
        Map<String, String> map = viewProperty.dinamicProperty;
        Map<String, String> map2 = viewProperty.eventProperty;
        DinamicViewAdvancedConstructor viewConstructor = Dinamic.getViewConstructor(viewProperty.viewIdentify);
        if (!map.isEmpty()) {
            HashMap hashMap = new HashMap();
            ArrayList arrayList = new ArrayList(10);
            for (String next : map.keySet()) {
                Object value = DinamicExpression.getValue(map.get(next), viewProperty.viewIdentify, dinamicParams);
                hashMap.put(next, value);
                if (value == null && Dinamic.isDebugable()) {
                    DinamicLog.i(Dinamic.TAG, String.format("表达式 %s=%s 解析出来的结果为null", new Object[]{next, map.get(next)}));
                }
            }
            arrayList.addAll(hashMap.keySet());
            hashMap.putAll(viewProperty.fixedProperty);
            viewConstructor.bindDataImpl(view, hashMap, arrayList, dinamicParams);
        }
        if (!map2.isEmpty()) {
            view.setTag(DinamicTagKey.SUBDATA, dinamicParams.getCurrentData());
            viewConstructor.setEvents(view, dinamicParams);
        }
    }
}
