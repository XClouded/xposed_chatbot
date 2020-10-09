package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import java.math.BigDecimal;
import java.util.List;

public class Equal extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        if (list == null || list.size() != 2) {
            return null;
        }
        Object obj = list.get(0);
        Object obj2 = list.get(1);
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.getClass().equals(obj2.getClass())) {
            return Boolean.valueOf(obj.equals(obj2));
        }
        if (!(obj instanceof Number) || !(obj2 instanceof Number)) {
            return Boolean.valueOf(obj.equals(obj2));
        }
        return Boolean.valueOf(compareNumber(obj, obj2));
    }

    /* access modifiers changed from: protected */
    public boolean compareNumber(Object obj, Object obj2) {
        if ((obj instanceof BigDecimal) || (obj2 instanceof BigDecimal)) {
            if (NumberUtil.toBigDecimal(obj).compareTo(NumberUtil.toBigDecimal(obj2)) == 0) {
                return true;
            }
            return false;
        } else if (NumberUtil.isFloatingPointNumber(obj) || NumberUtil.isFloatingPointNumber(obj2)) {
            if (NumberUtil.toDouble(obj) == NumberUtil.toDouble(obj2)) {
                return true;
            }
            return false;
        } else if (NumberUtil.toLong(obj) == NumberUtil.toLong(obj2)) {
            return true;
        } else {
            return false;
        }
    }
}
