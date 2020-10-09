package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class DoubleLess extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("DoubleLess");
        if (list != null && list.size() == 2) {
            try {
                if (Double.parseDouble(list.get(1).toString()) - Double.parseDouble(list.get(0).toString()) >= 1.0E-9d) {
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
