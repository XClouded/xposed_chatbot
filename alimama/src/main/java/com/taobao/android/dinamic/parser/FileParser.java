package com.taobao.android.dinamic.parser;

import android.content.res.XmlResourceParser;
import android.util.Log;
import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamic.view.ViewResult;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;

public class FileParser extends AbstractParser {
    private static final String TAG = "Home.FileParser";
    private Constructor<?> xmlBlockConstructor;

    public FileParser() {
        init();
    }

    private void init() {
        try {
            this.xmlBlockConstructor = Class.forName("android.content.res.XmlBlock").getConstructor(new Class[]{byte[].class});
            this.xmlBlockConstructor.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "Fail to get XmlBlock", e);
        }
    }

    public XmlPullParser openXmlResourceParser(String str, DinamicTemplate dinamicTemplate, ViewResult viewResult) {
        if (this.xmlBlockConstructor == null || dinamicTemplate == null) {
            viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_XML_BLOCK_CONSTRUCTOR_REFLECT_ERROR, DinamicError.ERROR_CODE_XML_BLOCK_CONSTRUCTOR_REFLECT_ERROR);
            return null;
        }
        DTemplateManager templateManagerWithModule = DTemplateManager.templateManagerWithModule(str);
        if (!templateManagerWithModule.getLayoutFileManager().isLocalLayoutFileExists(templateManagerWithModule.getTemplateKey(dinamicTemplate))) {
            viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_FILE_LOST, "downloaded file lost");
            return null;
        }
        try {
            byte[] readLocalTemplate = templateManagerWithModule.readLocalTemplate(dinamicTemplate);
            if (readLocalTemplate != null) {
                if (readLocalTemplate.length != 0) {
                    Object beforeProcess = beforeProcess(readLocalTemplate, viewResult);
                    try {
                        Object invoke = ReflectUtils.invoke(this.xmlBlockConstructor.newInstance(new Object[]{beforeProcess}), "newParser", new Object[0]);
                        if (invoke instanceof XmlResourceParser) {
                            return (XmlResourceParser) invoke;
                        }
                        viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_XML_RES_PARSER_ERROR, DinamicError.ERROR_CODE_XML_RES_PARSER_ERROR);
                        return null;
                    } catch (Exception e) {
                        viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_BYTE_TO_PARSER_ERROR, e.getMessage());
                        return null;
                    }
                }
            }
            viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_TEMPLATE_FILE_EMPTY, "downloaded file empty");
            return null;
        } catch (Exception e2) {
            viewResult.getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_BYTE_READ_ERROR, e2.getMessage());
            return null;
        }
    }
}
