package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Else extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        if (list == null || list.size() != 1) {
            return null;
        }
        return list.get(0);
    }
}
