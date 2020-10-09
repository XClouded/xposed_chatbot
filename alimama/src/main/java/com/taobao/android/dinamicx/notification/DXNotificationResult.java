package com.taobao.android.dinamicx.notification;

import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.List;

public class DXNotificationResult {
    public List<DXTemplateItem> failedTemplateItems;
    public List<DXTemplateItem> finishedTemplateItems;
    public List<DXTemplateUpdateRequest> templateUpdateRequestList;

    public DXNotificationResult(List<DXTemplateItem> list, List<DXTemplateItem> list2, List<DXTemplateUpdateRequest> list3) {
        this.finishedTemplateItems = list;
        this.failedTemplateItems = list2;
        this.templateUpdateRequestList = list3;
    }
}
