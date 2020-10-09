package com.taobao.android.dinamic.expression.parser;

import android.text.TextUtils;
import com.taobao.android.dinamic.expression.parser.resolver.ValueResolverFactory;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;
import java.util.StringTokenizer;

public class DinamicSubDataParser extends AbsDinamicDataParser {
    private static final String DELIMITER = " .[]";

    public Object parser(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return obj;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, DELIMITER, false);
        while (stringTokenizer.hasMoreTokens()) {
            obj = ValueResolverFactory.getValue(obj, stringTokenizer.nextToken());
        }
        return obj;
    }

    public Object parser(String str, String str2, Object obj, Object obj2) {
        return parser(str2, obj);
    }

    public Object parser(String str, DinamicParams dinamicParams) {
        return parser(dinamicParams.getModule(), str, dinamicParams.getCurrentData(), dinamicParams.getDinamicContext());
    }

    public Object evalWithArgs(List list, DinamicParams dinamicParams) {
        return parser((String) list.get(0), dinamicParams.getCurrentData());
    }
}
