package com.taobao.android.dinamic.expression.parser;

import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public abstract class AbsDinamicDataParser implements DinamicDataParser {
    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        return null;
    }

    public Object parser(String str, Object obj) {
        return null;
    }

    public Object parser(String str, String str2, Object obj, Object obj2) {
        return parser(str2, obj);
    }

    public Object parser(String str, DinamicParams dinamicParams) {
        return parser(dinamicParams.getModule(), str, dinamicParams.getOriginalData(), dinamicParams.getDinamicContext());
    }
}
