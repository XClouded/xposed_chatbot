package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class Find extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        Object[] objArr;
        if (list != null && list.size() == 2) {
            Object obj = list.get(0);
            Object obj2 = list.get(1);
            try {
                if (obj instanceof List) {
                    objArr = ((List) obj).toArray();
                } else {
                    objArr = (Object[]) obj;
                }
                for (int i = 0; i < objArr.length; i++) {
                    if (objArr[i].equals(obj2)) {
                        return objArr[i];
                    }
                }
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }
}
