package com.taobao.android.dinamicx.notification;

import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.ArrayList;
import java.util.List;

public class DXNotificationDownloadResult {
    private List<DXTemplateItem> failedTemplates;
    private List<DXTemplateItem> finishedTemplates;

    public DXNotificationDownloadResult(List<DXTemplateItem> list, List<DXTemplateItem> list2) {
        if (list != null) {
            this.finishedTemplates = new ArrayList(list);
        }
        if (list2 != null) {
            this.failedTemplates = new ArrayList(list2);
        }
    }

    public List<DXTemplateItem> getFinishedTemplates() {
        return this.finishedTemplates;
    }

    public void setFinishedTemplates(List<DXTemplateItem> list) {
        this.finishedTemplates = list;
    }

    public List<DXTemplateItem> getFailedTemplates() {
        return this.failedTemplates;
    }

    public void setFailedTemplates(List<DXTemplateItem> list) {
        this.failedTemplates = list;
    }
}
