package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Match extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        if (list == null || list.size() != 2) {
            return null;
        }
        Boolean valueOf = Boolean.valueOf(NumberUtil.parseBoolean(list.get(0).toString()));
        if (!(valueOf instanceof Boolean) || !valueOf.booleanValue()) {
            return null;
        }
        return list.get(1);
    }
}
