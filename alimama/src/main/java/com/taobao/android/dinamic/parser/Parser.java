package com.taobao.android.dinamic.parser;

import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.ViewResult;
import org.xmlpull.v1.XmlPullParser;

public interface Parser {
    XmlPullParser openXmlResourceParser(String str, DinamicTemplate dinamicTemplate, ViewResult viewResult);
}
