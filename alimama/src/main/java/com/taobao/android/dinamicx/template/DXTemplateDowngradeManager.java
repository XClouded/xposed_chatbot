package com.taobao.android.dinamicx.template;

import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.HashMap;
import java.util.Map;

public class DXTemplateDowngradeManager {
    private int downgradeMaxCount;
    private Map<String, Integer> downgradeTimesMap = new HashMap();

    public DXTemplateDowngradeManager(int i) {
        this.downgradeMaxCount = i;
    }

    public void setUpMaxDowngradeCount(int i) {
        this.downgradeMaxCount = i;
    }

    public DXTemplateItem fetchTemplate(String str, long j, DXTemplateItem dXTemplateItem) {
        int i;
        if (this.downgradeMaxCount == 1) {
            return DXTemplateInfoManager.getInstance().getSelfOrPresetTemplate(str, j, dXTemplateItem);
        }
        Integer num = this.downgradeTimesMap.get(dXTemplateItem.name);
        if (num == null) {
            i = 0;
        } else {
            i = num.intValue();
        }
        if (i >= this.downgradeMaxCount) {
            return DXTemplateInfoManager.getInstance().getSelfOrPresetTemplate(str, j, dXTemplateItem);
        }
        return DXTemplateInfoManager.getInstance().getAvailableTemplate(str, j, dXTemplateItem);
    }

    public DXTemplateItem fetchPresetTemplate(String str, long j, DXTemplateItem dXTemplateItem) {
        return DXTemplateInfoManager.getInstance().getPresetTemplate(str, j, dXTemplateItem);
    }

    public void startStrategy(String str, long j, DXTemplateItem dXTemplateItem) {
        int i;
        Integer num = this.downgradeTimesMap.get(dXTemplateItem.name);
        if (num == null) {
            i = 0;
        } else {
            i = num.intValue();
        }
        switch (DXTemplateInfoManager.getInstance().downgradeTemplate(str, j, dXTemplateItem)) {
            case 1:
                this.downgradeTimesMap.put(dXTemplateItem.name, Integer.valueOf(i + 1));
                return;
            case 2:
                this.downgradeTimesMap.put(dXTemplateItem.name, Integer.valueOf(this.downgradeMaxCount));
                return;
            default:
                return;
        }
    }

    public void resetDowngradeCount(long j) {
        this.downgradeTimesMap.clear();
        DXTemplateInfoManager.getInstance().clearTemplateInfoCache(j);
    }
}
