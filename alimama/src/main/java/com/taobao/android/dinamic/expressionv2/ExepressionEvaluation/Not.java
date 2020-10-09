package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Not extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("NotEvaluation");
        if (list == null) {
            return null;
        }
        if (list.size() == 1) {
            return Boolean.valueOf(!NumberUtil.parseBoolean(list.get(0).toString()));
        }
        return list.size() == 0 ? true : null;
    }
}
