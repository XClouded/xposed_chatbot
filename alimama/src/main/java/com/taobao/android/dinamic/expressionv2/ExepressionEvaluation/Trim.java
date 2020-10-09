package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Trim extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        DinamicLog.print("TrimEvaluation");
        if (list == null || list.size() != 1) {
            return null;
        }
        try {
            String valueOf = String.valueOf(list.get(0));
            if (valueOf.length() != 0) {
                return valueOf.trim();
            }
            return null;
        } catch (ClassCastException unused) {
            DinamicLog.print("String cast error!");
            return null;
        }
    }
}
