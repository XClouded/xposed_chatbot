package com.taobao.android.dinamic.parser;

import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.ViewResult;
import org.xmlpull.v1.XmlPullParser;

public class AbstractParser implements Parser {
    public byte[] beforeProcess(byte[] bArr, ViewResult viewResult) {
        return bArr;
    }

    public XmlPullParser openXmlResourceParser(String str, DinamicTemplate dinamicTemplate, ViewResult viewResult) {
        return null;
    }
}
