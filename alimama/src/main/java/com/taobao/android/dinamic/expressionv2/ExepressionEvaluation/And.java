package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class And extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("AndEvaluation");
        if (list == null || list.size() <= 1) {
            return null;
        }
        int size = list.size();
        try {
            boolean parseBoolean = NumberUtil.parseBoolean(list.get(0).toString());
            for (int i = 1; i < size; i++) {
                parseBoolean = parseBoolean && NumberUtil.parseBoolean(list.get(i).toString());
            }
            return Boolean.valueOf(parseBoolean);
        } catch (ClassCastException unused) {
            DinamicLog.print("boolean cast error!");
            return null;
        }
    }
}
