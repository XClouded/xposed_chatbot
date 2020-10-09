package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class DoubleLessEqual extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("DoubleLessEqual");
        if (list != null && list.size() == 2) {
            try {
                double parseDouble = Double.parseDouble(list.get(1).toString()) - Double.parseDouble(list.get(0).toString());
                if (1.0E-9d <= parseDouble || Math.abs(parseDouble) < 1.0E-9d) {
                    return true;
                }
            } catch (NumberFormatException unused) {
                DinamicLog.print("double cast error!");
                return false;
            }
        }
        return false;
    }
}
