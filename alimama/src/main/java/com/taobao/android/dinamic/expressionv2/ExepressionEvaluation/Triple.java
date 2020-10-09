package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Triple extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("TripleEvaluation");
        if (list == null || list.size() != 3) {
            return false;
        }
        try {
            if (NumberUtil.parseBoolean(list.get(0).toString())) {
                return (String) list.get(1);
            }
            return (String) list.get(2);
        } catch (ClassCastException unused) {
            return false;
        }
    }
}
