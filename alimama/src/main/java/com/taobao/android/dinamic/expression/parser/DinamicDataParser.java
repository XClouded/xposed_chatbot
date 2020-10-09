package com.taobao.android.dinamic.expression.parser;

import com.taobao.android.dinamic.model.DinamicParams;
import java.util.List;

public interface DinamicDataParser {
    Object evalWithArgs(List list, DinamicParams dinamicParams);

    Object parser(String str, DinamicParams dinamicParams);

    Object parser(String str, Object obj);

    Object parser(String str, String str2, Object obj, Object obj2);
}
