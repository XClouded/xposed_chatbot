package com.taobao.android.dinamic.expressionv2.ExepressionEvaluation;

import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class StringSubstr extends AbsDinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        String str = null;
        if (list == null) {
            return null;
        }
        if (list.size() == 3) {
            Object obj = list.get(0);
            int integer = NumberUtil.toInteger(list.get(1));
            int integer2 = NumberUtil.toInteger(list.get(2));
            String valueOf = String.valueOf(obj);
            if (integer2 < 0 || integer + 1 > valueOf.length()) {
                return "";
            }
            if (integer < 0) {
                integer = 0;
            }
            int i = integer2 + integer;
            if (i > obj.toString().length()) {
                str = valueOf.substring(integer);
            }
            if (i - 1 < obj.toString().length()) {
                return ((String) obj).substring(integer, i);
            }
            return str;
        } else if (list.size() != 2) {
            return null;
        } else {
            Object obj2 = list.get(0);
            int integer3 = NumberUtil.toInteger(list.get(1));
            String valueOf2 = String.valueOf(obj2);
            if (integer3 + 1 > valueOf2.length()) {
                return "";
            }
            if (integer3 < 0) {
                integer3 = 0;
            }
            return valueOf2.substring(integer3);
        }
    }
}
