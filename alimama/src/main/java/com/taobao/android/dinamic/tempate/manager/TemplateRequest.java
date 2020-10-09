package com.taobao.android.dinamic.tempate.manager;

import android.text.TextUtils;

public class TemplateRequest {
    public String defaultTemplateAssetName;
    public String defaultTemplateId;
    public String templateId;

    public TemplateRequest(String str, String str2, String str3) {
        if (str == null) {
            throw new IllegalArgumentException("templateId is null.");
        } else if (str2 == null) {
            throw new IllegalArgumentException("defaultTemplateId is null.");
        } else if (!TextUtils.isEmpty(str3)) {
            this.templateId = str;
            this.defaultTemplateId = str2;
            this.defaultTemplateAssetName = str3;
        } else {
            throw new IllegalArgumentException("defaultTemplateAssetsName is null or empty.");
        }
    }
}
