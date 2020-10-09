package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;
import java.util.Map;

public class Get extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        Object[] objArr;
        if (list != null && list.size() == 2) {
            Object obj = list.get(0);
            if (obj instanceof Map) {
                return ((Map) obj).get(list.get(1));
            }
            try {
                int integer = NumberUtil.toInteger(list.get(1));
                if (obj instanceof List) {
                    objArr = ((List) obj).toArray();
                } else {
                    objArr = (Object[]) obj;
                }
                if (integer < objArr.length) {
                    return objArr[integer];
                }
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }
}
