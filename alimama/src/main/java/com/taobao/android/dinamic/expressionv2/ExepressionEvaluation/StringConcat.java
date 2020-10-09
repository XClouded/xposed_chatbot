package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class StringConcat extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        if (list != null) {
            if (list.size() > 1) {
                int size = list.size();
                Object obj = list.get(0);
                if (obj != null && (obj instanceof String)) {
                    for (int i = 1; i < size; i++) {
                        Object obj2 = list.get(i);
                        if (obj2 != null && (obj2 instanceof String)) {
                            obj = obj.toString().concat(obj2.toString());
                        }
                    }
                    return obj.toString();
                }
            }
        }
        return null;
    }
}
