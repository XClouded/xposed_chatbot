package com.taobao.android.dinamic.expression.parser;

import android.text.TextUtils;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.exception.DinamicException;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.And;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.DoubleEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.DoubleGreater;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.DoubleGreaterEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.DoubleLess;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.DoubleLessEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Else;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Equal;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Find;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Get;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.IntEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.IntGreater;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.IntGreaterEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.IntLess;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.IntLessEqual;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Length;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Match;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Not;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Or;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.StringConcat;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.StringLowercase;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.StringSubstr;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.StringUppercase;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Trim;
import com.taobao.android.dinamic.expressionv2.ExepressionEvaluation.Triple;
import java.util.HashMap;
import java.util.Map;

public class DinamicDataParserFactory {
    private static Map<String, AbsDinamicDataParser> parsers = new HashMap();

    static {
        parsers.put("data", new DinamicExpressionParser());
        parsers.put(DinamicConstant.CONSTANT_PREFIX, new DinamicConstantParser());
        parsers.put(DinamicConstant.SUBDATA_PREFIX, new DinamicSubDataParser());
        parsers.put(DinamicConstant.APP_STYLE, new AppStyleParser());
        parsers.put(DinamicConstant.AND_PREFIX, new And());
        parsers.put(DinamicConstant.EQUAL_PREFIX, new Equal());
        parsers.put(DinamicConstant.LENGTH_PREFIX, new Length());
        parsers.put(DinamicConstant.NOT_PREFIX, new Not());
        parsers.put(DinamicConstant.ELSE_PREFIX, new Else());
        parsers.put(DinamicConstant.MATCH_PREFIX, new Match());
        parsers.put(DinamicConstant.LOWER_PREFIX, new StringLowercase());
        parsers.put(DinamicConstant.UPPER_PREFIX, new StringUppercase());
        parsers.put(DinamicConstant.CONCAT_PREFIX, new StringConcat());
        parsers.put(DinamicConstant.TRIPLE_PREFIX, new Triple());
        parsers.put(DinamicConstant.SUBSTR_PREFIX, new StringSubstr());
        parsers.put(DinamicConstant.FIND_PREFIX, new Find());
        parsers.put(DinamicConstant.AGET_PREFIX, new Get());
        parsers.put(DinamicConstant.DGET_PREFIX, new Get());
        parsers.put(DinamicConstant.OR_PREFIX, new Or());
        parsers.put(DinamicConstant.TRIM_PREFIX, new Trim());
        parsers.put(DinamicConstant.FLOAT_LITTER_PREFIX, new DoubleLess());
        parsers.put(DinamicConstant.FLOAT_LITTER_EQUAL_PREFIX, new DoubleLessEqual());
        parsers.put(DinamicConstant.FLOAT_BIGGER_EQUAL_PREFIX, new DoubleGreaterEqual());
        parsers.put(DinamicConstant.FLOAT_BIGGER_PREFIX, new DoubleGreater());
        parsers.put(DinamicConstant.FLOAT_EQUAL, new DoubleEqual());
        parsers.put(DinamicConstant.INT_BIGGER_EQUAL_PREFIX, new IntGreaterEqual());
        parsers.put(DinamicConstant.INT_BIGGER_PREFIX, new IntGreater());
        parsers.put(DinamicConstant.INT_LITTER_EQUAL_PREFIX, new IntLessEqual());
        parsers.put(DinamicConstant.INT_LITTER_PREFIX, new IntLess());
        parsers.put(DinamicConstant.INT_EQUAL, new IntEqual());
    }

    public static void registerParser(String str, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        if (TextUtils.isEmpty(str) || absDinamicDataParser == null) {
            throw new DinamicException("prefix or parser is null");
        } else if (parsers.get(str) == null) {
            parsers.put(str, absDinamicDataParser);
        } else {
            throw new DinamicException("registerParser failed, parser already register by current identify:" + str);
        }
    }

    public static void registerReplaceParser(String str, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        if (TextUtils.isEmpty(str) || absDinamicDataParser == null) {
            throw new DinamicException("prefix or parser is null");
        }
        parsers.put(str, absDinamicDataParser);
    }

    public static DinamicDataParser getParser(String str) {
        return parsers.get(str);
    }

    public static boolean containsKey(String str) {
        return parsers.containsKey(str);
    }
}
