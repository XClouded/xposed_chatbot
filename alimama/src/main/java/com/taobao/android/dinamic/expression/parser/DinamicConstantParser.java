package com.taobao.android.dinamic.expression.parser;

import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public class DinamicConstantParser extends AbsDinamicDataParser {
    public Object parser(String str, Object obj) {
        return str;
    }

    public Object parser(String str, String str2, Object obj, Object obj2) {
        return parser(str2, obj);
    }

    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        return parser((String) list.get(0), dinamicParams.getOriginalData());
    }
}
