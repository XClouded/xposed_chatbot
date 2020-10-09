package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class StringUppercase extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        if (list != null && list.size() == 1) {
            Object obj = list.get(0);
            if (obj instanceof String) {
                return ((String) obj).toUpperCase();
            }
        }
        return null;
    }
}
