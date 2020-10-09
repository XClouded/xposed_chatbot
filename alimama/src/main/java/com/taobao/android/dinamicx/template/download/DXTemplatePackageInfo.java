package com.taobao.android.dinamicx.template.download;

import java.util.Map;

public class DXTemplatePackageInfo implements Cloneable {
    public String mainFilePath;
    public Map<String, String> subFilePathDict;

    /* access modifiers changed from: protected */
    public DXTemplatePackageInfo clone() {
        DXTemplatePackageInfo dXTemplatePackageInfo = new DXTemplatePackageInfo();
        dXTemplatePackageInfo.mainFilePath = this.mainFilePath;
        dXTemplatePackageInfo.subFilePathDict = this.subFilePathDict;
        return dXTemplatePackageInfo;
    }
}
