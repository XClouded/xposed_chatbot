package com.alibaba.ha.adapter.service.activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityNameManager {
    private int activityListMaxLength;
    private List<String> activityNameList;

    private ActivityNameManager() {
        this.activityNameList = new ArrayList();
        this.activityListMaxLength = 20;
    }

    private static class InstanceCreater {
        /* access modifiers changed from: private */
        public static ActivityNameManager instance = new ActivityNameManager();

        private InstanceCreater() {
        }
    }

    public static synchronized ActivityNameManager getInstance() {
        ActivityNameManager access$100;
        synchronized (ActivityNameManager.class) {
            access$100 = InstanceCreater.instance;
        }
        return access$100;
    }

    public void addActivityName(String str) {
        if (str != null) {
            try {
                if (this.activityNameList.size() < this.activityListMaxLength) {
                    this.activityNameList.add(str);
                    return;
                }
                this.activityNameList.remove(0);
                if (this.activityNameList.size() < this.activityListMaxLength) {
                    this.activityNameList.add(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getActivityList() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < this.activityNameList.size()) {
            try {
                sb.append(this.activityNameList.get(i));
                if (i < this.activityNameList.size() - 1) {
                    sb.append("+");
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getLastActivity() {
        try {
            int size = this.activityNameList.size() - 1;
            if (size >= 0) {
                return this.activityNameList.get(size);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
