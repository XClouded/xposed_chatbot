package com.taobao.android.dinamic.view;

import java.util.HashMap;

public class DinamicError {
    public static final String ERROR_CODE_BYTE_READ_ERROR = "byteReadError";
    public static final String ERROR_CODE_BYTE_TO_PARSER_ERROR = "byteToParserError";
    public static final String ERROR_CODE_EVENT_HANDLER_EXCEPTION = "eventHandlerException";
    public static final String ERROR_CODE_EVENT_HANDLER_NOT_FOUND = "eventHandlerNotFound";
    public static final String ERROR_CODE_OTHER = "other";
    public static final String ERROR_CODE_TEMPLATE_FILE_EMPTY = "templateFileEmpty";
    public static final String ERROR_CODE_TEMPLATE_FILE_LOST = "templateFileLost";
    public static final String ERROR_CODE_TEMPLATE_INFO_ERROR = "templateInfoError";
    public static final String ERROR_CODE_TEMPLATE_NOT_FOUND = "templateNotFound";
    public static final String ERROR_CODE_TEMPLATE_PARSER_EXCEPTION = "parserException";
    public static final String ERROR_CODE_TEMPLATE_PARSER_NOT_FOUND = "parserNotFound";
    public static final String ERROR_CODE_VIEW_EXCEPTION = "viewException";
    public static final String ERROR_CODE_VIEW_NOT_FOUND = "viewNotFound";
    public static final String ERROR_CODE_XML_BLOCK_CONSTRUCTOR_REFLECT_ERROR = "xmlBlockConstructorReflectError";
    public static final String ERROR_CODE_XML_RES_PARSER_ERROR = "xmlResourceParserError";
    private HashMap<String, String> errorMap = new HashMap<>();
    private String module;

    public DinamicError(String str) {
        this.module = str;
    }

    public boolean isEmpty() {
        return this.errorMap.isEmpty();
    }

    public void addErrorCodeWithInfo(String str, String str2) {
        if (!this.errorMap.containsKey(str)) {
            HashMap<String, String> hashMap = this.errorMap;
            hashMap.put(str, this.module + ":" + str2 + ";");
        }
        HashMap<String, String> hashMap2 = this.errorMap;
        hashMap2.put(str, this.errorMap.get(str) + str2 + ";");
    }

    public String getDescriptionForCode(String str) {
        return this.errorMap.get(str);
    }

    public String getAllErrorDescription() {
        return this.errorMap.toString();
    }

    public HashMap<String, String> getErrorMap() {
        return this.errorMap;
    }
}
