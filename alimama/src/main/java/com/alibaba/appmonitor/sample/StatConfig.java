package com.alibaba.appmonitor.sample;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;
import java.util.ArrayList;

@TableName("ap_stat")
public class StatConfig extends AMConifg {
    @Column("detail")
    public int detail;

    public boolean isDetail() {
        return this.detail == 1;
    }

    public boolean isDetail(String str, String str2) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(str);
        arrayList.add(str2);
        return detail(arrayList);
    }

    private boolean detail(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return isDetail();
        }
        String remove = arrayList.remove(0);
        if (isContains(remove)) {
            return ((StatConfig) getNext(remove)).detail(arrayList);
        }
        return isDetail();
    }
}
