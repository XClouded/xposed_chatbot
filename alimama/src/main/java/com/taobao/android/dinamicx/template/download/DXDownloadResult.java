package com.taobao.android.dinamicx.template.download;

public class DXDownloadResult {
    boolean isSuccess;
    DXTemplateItem item;

    public DXTemplateItem getItem() {
        return this.item;
    }

    public void setItem(DXTemplateItem dXTemplateItem) {
        this.item = dXTemplateItem;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean z) {
        this.isSuccess = z;
    }
}
