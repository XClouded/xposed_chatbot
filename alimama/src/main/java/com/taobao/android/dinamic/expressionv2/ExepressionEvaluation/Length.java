package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;
import java.util.Map;

public class Length extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("LengthEvaluation");
        if (list == null || list.size() != 1) {
            return null;
        }
        Object obj = list.get(0);
        try {
            if (obj instanceof String) {
                return String.valueOf(((String) obj).length());
            }
            if (obj instanceof Object[]) {
                return String.valueOf(((Object[]) obj).length);
            }
            if (obj instanceof Map) {
                return String.valueOf(((Map) obj).size());
            }
            if (obj instanceof List) {
                return String.valueOf(((List) obj).size());
            }
            return null;
        } catch (ClassCastException unused) {
            DinamicLog.print("String cast error!");
            return null;
        }
    }
}
