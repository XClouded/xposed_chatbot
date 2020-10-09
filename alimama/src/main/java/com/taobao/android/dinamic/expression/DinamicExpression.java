package com.taobao.android.dinamic.expression;

import android.text.TextUtils;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.expression.parser.DinamicDataParser;
import com.taobao.android.dinamic.expression.parser.DinamicDataParserFactory;
import com.taobao.android.dinamic.expressionv2.ExpressionProcessor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.view.DinamicError;

public class DinamicExpression {
    public static final String currentVersion = "2.0";

    public static Object getValue(String str, String str2, DinamicParams dinamicParams) {
        if (dinamicParams == null || dinamicParams.getOriginalData() == null || str == null) {
            return null;
        }
        if (str.startsWith(DinamicConstant.DINAMIC_PREFIX_AT)) {
            return ExpressionProcessor.process(str, str2, dinamicParams);
        }
        char[] charArray = str.toCharArray();
        String str3 = null;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        boolean z = false;
        boolean z2 = false;
        Object obj = null;
        for (char c : charArray) {
            if ('$' == c) {
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer2 = new StringBuffer();
                z2 = false;
                stringBuffer = stringBuffer3;
                z = true;
            } else if ('{' == c && z) {
                String stringBuffer4 = stringBuffer.toString();
                if (DinamicDataParserFactory.containsKey(stringBuffer4)) {
                    str3 = stringBuffer4;
                    z = false;
                    z2 = true;
                } else {
                    str3 = stringBuffer4;
                    z = false;
                }
            } else if ('}' == c && z2) {
                String stringBuffer5 = stringBuffer2.toString();
                if (TextUtils.isEmpty(stringBuffer5)) {
                    obj = dinamicParams.getOriginalData();
                } else {
                    DinamicDataParser parser = DinamicDataParserFactory.getParser(str3);
                    if (parser != null) {
                        try {
                            obj = parser.parser(stringBuffer5, dinamicParams);
                        } catch (Throwable th) {
                            if (Dinamic.isDebugable()) {
                                DinamicLog.w("DinamicExpresstion", th, "parse express failed, parser=", parser.getClass().getName());
                            }
                            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_PARSER_EXCEPTION, str2);
                        }
                    } else {
                        dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_PARSER_NOT_FOUND, str2);
                    }
                }
                if (obj != null && (!(obj instanceof String) || !TextUtils.isEmpty(obj.toString()))) {
                    return obj;
                }
                z = false;
                z2 = false;
            } else if (z) {
                stringBuffer.append(c);
            } else if (z2) {
                stringBuffer2.append(c);
            }
        }
        return obj;
    }

    public static boolean checkParserVersion(DinamicParams dinamicParams) {
        return dinamicParams != null && dinamicParams.getViewResult().getDinamicTemplate().getCompilerVersion().equals("2.0");
    }
}
