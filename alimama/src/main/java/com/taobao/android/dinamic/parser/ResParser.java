package com.taobao.android.dinamic.parser;

import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.ViewResult;
import org.xmlpull.v1.XmlPullParser;

public class ResParser extends AbstractParser {
    private static final String TAG = "Home.ResParser";

    public XmlPullParser openXmlResourceParser(String str, DinamicTemplate dinamicTemplate, ViewResult viewResult) {
        return DTemplateManager.templateManagerWithModule(str).getLayoutParser(dinamicTemplate);
    }
}
